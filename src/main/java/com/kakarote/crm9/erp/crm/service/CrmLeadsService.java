package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminField;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.*;
import java.util.stream.Collectors;

public class CrmLeadsService extends AbstractBasePageList {
    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmRecordService crmRecordService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    /**
     * @author wyq
     * 分页条件查询线索
     */
    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        CrmEnum crmEnum = CrmEnum.CRM_LEADS;
        Kv kv = getKv(basePageRequest, crmEnum, isDataAuth);
        if (kv.containsKey("error")){
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.leads.queryLeadPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.leads.queryLeadPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        resultJsonObject.put("list", recordPage);
        Integer count = Db.template("crm.leads.queryLeadPageListCount",kv).queryInt();
        resultJsonObject.put("totalRow", count);
        return R.ok().put("data",resultJsonObject);
    }

    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (leads_name like '%{search}%' or telephone like '%{search}%' or mobile like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }

    @Override
    public String querySelectSql() {
        return "select *,(select count(*) from 72crm_crm_leads_user_star where user_id = " + BaseUtil.getUserId() + " and leads_id = views.leads_id) as star";
    }

    @Override
    public JSONObject queryDefaultScene() {
        return new JSONObject().fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", "0"));
    }

    /**
     * @author wyq
     * 新增或更新线索
     */
    @Before(Tx.class)
    public R addOrUpdate(JSONObject object) {
        CrmLeads crmLeads = object.getObject("entity", CrmLeads.class);
        String batchId = StrUtil.isNotEmpty(crmLeads.getBatchId()) ? crmLeads.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(object.getJSONArray("field"), Kv.by("batchId", batchId).set("dataTableName", "72crm_crm_leads_data"));
        save(object.getJSONArray("field"), batchId);
        if(StrUtil.isEmpty(crmLeads.getEmail())){
            crmLeads.setEmail(null);
        }
        if (crmLeads.getLeadsId() != null) {
            crmLeads.setCustomerId(0);
            crmLeads.setUpdateTime(DateUtil.date());
            actionRecordUtil.updateRecord(new CrmLeads().dao().findById(crmLeads.getLeadsId()), crmLeads, CrmEnum.CRM_LEADS);
            return crmLeads.update() ? R.ok() : R.error();
        } else {
            crmLeads.setCreateTime(DateUtil.date());
            crmLeads.setUpdateTime(DateUtil.date());
            crmLeads.setCreateUserId(BaseUtil.getUser().getUserId());
            if (crmLeads.getOwnerUserId() == null) {
                crmLeads.setOwnerUserId(BaseUtil.getUser().getUserId());
            }
            crmLeads.setBatchId(batchId);
            boolean save = crmLeads.save();
            actionRecordUtil.addRecord(crmLeads.getLeadsId(), CrmEnum.CRM_LEADS);
            return save ? R.ok() : R.error();
        }
    }

    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_leads_data", "batch_id", batchId);
        List<CrmLeadsData> Fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            CrmLeadsData fieldv = TypeUtils.castToJavaBean(obj, CrmLeadsData.class);
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            Fieldvs.add(fieldv);
        });
        Db.batchSave(Fieldvs, 100);
        return true;
    }

    /**
     * @author wyq
     * 基本信息
     */
    public List<Record> information(Integer leadsId) {
        Record record = queryById(leadsId);
        List<String> keyList = Arrays.asList("leads_name", "mobile", "email","address","telephone","remark","next_time");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间","更新时间","下次联系时间","最后跟进时间");
        List<Record> recordList = adminFieldService.queryInformation(CrmEnum.CRM_LEADS,record, keyList);
        return recordList.stream().sorted(Comparator.comparingInt(r->-r.getInt("field_type"))).peek(r-> {
            r.remove("field_type", "setting", "type");
            if (systemFieldList.contains(r.getStr("name"))){
                r.set("sysInformation",1);
            }else {
                r.set("sysInformation",0);
            }
        }).collect(Collectors.toList());
    }

    /**
     * @author wyq
     * 根据线索id查询
     */
    public Record queryById(Integer leadsId) {
        Record record = Db.findFirst(Db.getSql("crm.leads.queryById"), BaseUtil.getUserId(), leadsId);
        List<Record> recordList = Db.find("select name,value from `72crm_crm_leads_data` where batch_id = ?", record.getStr("batch_id"));
        recordList.forEach(field -> record.set(field.getStr("name"), field.getStr("value")));
        return record;
    }

    /**
     * @author wyq
     * 根据id 删除线索
     */
    @Before(Tx.class)
    public R deleteByIds(String leadsIds) {
        String[] idsList = StrUtil.split(leadsIds,",");
        Kv kv= Kv.by("ids", idsList);
        List<String> batchList = Db.template("crm.leads.queryLeadsBatchId",kv).query();
        //删除跟进记录
        Aop.get(CrmActivityService.class).deleteActivityRecord(CrmActivityEnum.LEADS,idsList);
        //删除字段操作记录
        Aop.get(CrmRecordService.class).deleteActionRecord(CrmEnum.CRM_LEADS,idsList);
        actionRecordUtil.addDeleteActionRecord(CrmEnum.CRM_LEADS, idsList);
        //删除自定义字段
        Db.template("admin.field.deleteFieldv", Kv.by("batchList", batchList).set("dataTableName", "72crm_crm_leads_data")).delete();
        //删除文件
        Aop.get(AdminFileService.class).removeByBatchId(batchList.toArray(new String[0]));
        Db.template("crm.leads.deleteByIds", kv).delete();
        return R.ok();
    }

    /**
     * @author wyq
     * 变更负责人
     */
    @Before(Tx.class)
    public R updateOwnerUserId(String leadsIds, Long ownerUserId) {
        String[] ids = leadsIds.split(",");
        int update = Db.update(Db.getSqlPara("crm.leads.updateOwnerUserId", Kv.by("ownerUserId", ownerUserId).set("ids", ids)));
        for (String id : ids) {
            actionRecordUtil.addConversionRecord(Integer.valueOf(id), CrmEnum.CRM_LEADS, ownerUserId);
        }
        return update > 0 ? R.ok() : R.error();
    }

    /**
     * @author wyq
     * 线索转客户
     */
    @Before(Tx.class)
    public R translate(String leadsIds) {
        String[] leadsIdsArr = leadsIds.split(",");
        for (String leadsId : leadsIdsArr) {
            Record crmLeads = queryById(Integer.valueOf(leadsId));
            if (1 == crmLeads.getInt("is_transform")) {
                return R.error("已转化线索不能再次转化");
            }
            List<Record> leadsFields = adminFieldService.list(CrmEnum.CRM_LEADS.getType(), 0);
            CrmCustomer crmCustomer = new CrmCustomer();
            crmCustomer.setCustomerName(crmLeads.getStr("leads_name"));
            crmCustomer.setNextTime(crmLeads.getDate("next_time"));
            crmCustomer.setMobile(crmLeads.getStr("mobile"));
            crmCustomer.setTelephone(crmLeads.getStr("telephone"));
            crmCustomer.setDealStatus(0);
            crmCustomer.setCreateUserId(BaseUtil.getUser().getUserId());
            crmCustomer.setOwnerUserId(crmLeads.getLong("owner_user_id"));
            crmCustomer.setCreateTime(new Date());
            crmCustomer.setUpdateTime(new Date());
            crmCustomer.setReceiveTime(new Date());
            crmCustomer.setRoUserId(",");
            crmCustomer.setRwUserId(",");
            crmCustomer.setDetailAddress(crmLeads.getStr("address"));
            crmCustomer.setLocation("");
            crmCustomer.setAddress("");
            crmCustomer.setLng("");
            crmCustomer.setLat("");
            crmCustomer.setRemark("");
            String customerBatchId = IdUtil.simpleUUID();
            crmCustomer.setBatchId(customerBatchId);
            List<AdminField> customerFields = AdminField.dao.find("select field_id,name,field_name,field_type,is_unique,is_null from 72crm_admin_field where label = '2'");
            List<CrmCustomerData> customerDataList = new ArrayList<>();
            for (Record leadsField : leadsFields) {
                for (AdminField customerField : customerFields) {
                    Integer isUnique = customerField.getIsUnique();
                    if (leadsField.get("relevant") != null && customerField.getFieldId().equals(leadsField.get("relevant"))) {
                        if (customerField.getFieldType().equals(1)) {
                            crmCustomer.set(customerField.getFieldName(), crmLeads.get(leadsField.get("field_name")));
                        } else {
                            CrmCustomerData crmCustomerData = new CrmCustomerData();
                            crmCustomerData.setValue(crmLeads.get(leadsField.get("name")));
                            crmCustomerData.setFieldId(customerField.getFieldId());
                            crmCustomerData.setName(customerField.getName());
                            customerDataList.add(crmCustomerData);
                        }
                        continue;
                    }
                    if (customerField.getFieldType().equals(1)) {
                        continue;
                    }
                    if ("客户来源".equals(customerField.getName()) && "线索来源".equals(leadsField.getStr("name"))) {
                        if (isUnique == 1 && Db.template("crm.customer.queryFieldDuplicate1",Kv.by("key",customerField.getName()).set("value",crmLeads.get(leadsField.get("name")))).queryInt()>0){
                            return R.error(customerField.getName()+"已存在");
                        }
                        CrmCustomerData crmCustomerData = new CrmCustomerData();
                        crmCustomerData.setValue(crmLeads.get(leadsField.get("name")));
                        crmCustomerData.setFieldId(customerField.getFieldId());
                        crmCustomerData.setName(customerField.getName());
                        customerDataList.add(crmCustomerData);
                    }
                    if ("客户行业".equals(customerField.getName()) && "客户行业".equals(leadsField.getStr("name"))) {
                        if (isUnique == 1 && Db.template("crm.customer.queryFieldDuplicate1",Kv.by("key",customerField.getName()).set("value",crmLeads.get(leadsField.get("name")))).queryInt()>0){
                            return R.error(customerField.getName()+"已存在");
                        }
                        CrmCustomerData crmCustomerData = new CrmCustomerData();
                        crmCustomerData.setValue(crmLeads.get(leadsField.get("name")));
                        crmCustomerData.setFieldId(customerField.getFieldId());
                        crmCustomerData.setName(customerField.getName());
                        customerDataList.add(crmCustomerData);
                    }
                    if ("客户级别".equals(customerField.getName()) && "客户级别".equals(leadsField.getStr("name"))) {
                        if (isUnique == 1 && Db.template("crm.customer.queryFieldDuplicate1",Kv.by("key",customerField.getName()).set("value",crmLeads.get(leadsField.get("name")))).queryInt()>0){
                            return R.error(customerField.getName()+"已存在");
                        }
                        CrmCustomerData crmCustomerData = new CrmCustomerData();
                        crmCustomerData.setValue(crmLeads.get(leadsField.get("name")));
                        crmCustomerData.setFieldId(customerField.getFieldId());
                        crmCustomerData.setName(customerField.getName());
                        customerDataList.add(crmCustomerData);
                    }
                }
            }
            for (AdminField customerField : customerFields) {
                Integer isNull = customerField.getIsNull();
                Integer isUnique = customerField.getIsUnique();
                String name = customerField.getName();
                Map<String, Object> customerMap = crmCustomer.toRecord().getColumns();
                for (String key : customerMap.keySet()) {
                    if (key.equals(customerField.getFieldName())){
                        Object value=customerMap.get(key);
                        if(value!=null&&!"".equals(value.toString())){
                            if (isUnique == 1 && Db.template("crm.customer.queryFieldDuplicate",Kv.by("key",key).set("value",value)).queryInt()>0){
                                return R.error(name+"已存在");
                            }
                        }
                    }
                }
            }
            crmCustomer.save();
            boolean isMaxOwner = Aop.get(CrmCustomerService.class).isMaxOwner(crmLeads.getLong("owner_user_id"), new String[]{crmCustomer.getCustomerId().toString()});
            if (!isMaxOwner) {
                return R.error("该员工拥有客户数已达上限");
            }
            actionRecordUtil.addConversionCustomerRecord(crmCustomer.getCustomerId(), CrmEnum.CRM_CUSTOMER.getType() + "", crmCustomer.getCustomerName());
            save(customerDataList, customerBatchId);
            Db.update("update 72crm_crm_leads set is_transform = 1,update_time = ?,customer_id = ? where leads_id = ?",
                    DateUtil.date(), crmCustomer.getCustomerId(), Integer.valueOf(leadsId));
            List<CrmActionRecord> crmActionRecordList = CrmActionRecord.dao.find("select * from `72crm_crm_action_record` where action_id = ? and types = 1", Integer.valueOf(leadsId));
            List<CrmActivity> crmActivityList = CrmActivity.dao.find("select * from 72crm_crm_activity where activity_type = 1 and type = 1 and activity_type_id = ?", Integer.valueOf(leadsId));
            List<AdminFile> adminFileList = new ArrayList<>();
            crmActionRecordList.forEach(crmActionRecord -> {
                crmActionRecord.setId(null);
                crmActionRecord.setTypes(String.valueOf(CrmEnum.CRM_CUSTOMER.getType()));
                crmActionRecord.setActionId(crmCustomer.getCustomerId());
            });
            Db.batchSave(crmActionRecordList,500);
            if (crmActivityList.size() != 0) {
                crmActivityList.forEach(crmActivity -> {
                    List<AdminFile> leadsRecordFiles = AdminFile.dao.find("select * from 72crm_admin_file where batch_id = ?", crmActivity.getBatchId());
                    String customerRecordBatchId = IdUtil.simpleUUID();
                    leadsRecordFiles.forEach(adminFile -> {
                        adminFile.setBatchId(customerRecordBatchId);
                        adminFile.setFileId(null);
                    });
                    adminFileList.addAll(leadsRecordFiles);
                    crmActivity.setBatchId(customerRecordBatchId);
                    crmActivity.setActivityId(null);
                    crmActivity.setActivityType(CrmEnum.CRM_CUSTOMER.getType());
                    crmActivity.setActivityTypeId(crmCustomer.getCustomerId());
                    crmActivity.setCreateTime(DateUtil.date());
                });
                Db.batchSave(crmActivityList, 100);
            }
            List<AdminFile> fileList = AdminFile.dao.find("select * from 72crm_admin_file where batch_id = ?", crmLeads.getStr("batch_id"));
            if (fileList.size() != 0) {
                fileList.forEach(adminFile -> {
                    adminFile.setBatchId(customerBatchId);
                    adminFile.setFileId(null);
                });
            }
            adminFileList.addAll(fileList);
            Db.batchSave(adminFileList, 100);
        }
        return R.ok();
    }

    /**
     * 保存自定义字段
     *
     * @param array   参数对象
     * @param batchId 批次ID
     * @return 操作结果
     */
    public boolean save(List<CrmCustomerData> array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_customer_data", "batch_id", batchId);
        array.forEach(fieldv -> {
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            fieldv.save();
        });
        return true;
    }

    /**
     * @author wyq
     * 查询编辑字段
     */
    public List<Record> queryField(Integer leadsId) {
        Record leads = queryById(leadsId);
        return adminFieldService.queryUpdateField(CrmEnum.CRM_LEADS.getType(), leads);
    }

    /**
     * @author wyq
     * 获取线索导入查重字段
     */
    public R getCheckingField() {
        return R.ok().put("data", "线索名称");
    }

    /**
     * @author wyq
     * 附件列表
     */
    public R queryFileList(Integer leadsId) {
        SqlPara sqlPara=Db.getSqlPara("crm.leads.queryFileList", Kv.by("leadsId", leadsId));
        return R.ok().put("data", Db.find(sqlPara));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public R num(Integer leadsId) {
        Kv kv=Kv.by("leadsId", leadsId);
        Record record = Db.findFirst(Db.getSqlPara("crm.leads.queryNum", kv));
        return R.ok().put("data", record);
    }

    /**
     * 标星（关注）
     */
    public R star(Integer leadsId) {
        Long userId = BaseUtil.getUserId();
        CrmLeadsUserStar userStar = CrmLeadsUserStar.dao.findFirst("select * from 72crm_crm_leads_user_star where user_id = ? and leads_id = ?", userId, leadsId);
        List<Integer> leadsIdList = Db.query("select leads_id from 72crm_crm_leads_user_star where user_id = ?", userId);
        if (ObjectUtil.isEmpty(userStar)) {
            new CrmLeadsUserStar().setUserId(userId).setLeadsId(leadsId).save();
            leadsIdList.add(leadsId);
        } else {
            userStar.delete();
            leadsIdList.remove(leadsId);
        }
        String leadsIds = CollectionUtil.isEmpty(leadsIdList) ? "0" : CollectionUtil.join(leadsIdList, ",");
        JSONObject data = new JSONObject().fluentPut("leads_id", new JSONObject().fluentPut("name", "leads_id").fluentPut("condition", "in").fluentPut("value", leadsIds));
        Db.update("update 72crm_admin_scene set data = ? where type = 1 and user_id = ? and bydata = 'star'", data.toString(), userId);
        return R.ok();
    }
}
