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
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CrmContactsService extends AbstractBasePageList {
    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmRecordService crmRecordService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    @Inject
    private CrmActivityService crmActivityService;

    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        Kv kv = getKv(basePageRequest,CrmEnum.CRM_CONTACTS,isDataAuth);
        if (kv.containsKey("error")){
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.contact.queryContactsPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.contact.queryContactsPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        resultJsonObject.put("list", recordPage);
        Integer count = Db.template("crm.contact.queryContactsPageListCount", kv).queryInt();
        resultJsonObject.put("totalRow", count);
        return R.ok().put("data", resultJsonObject);
    }

    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (name like '%{search}%' or telephone like '%{search}%' or mobile like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }

    @Override
    public String querySelectSql() {
        return "select *,(select count(*) from 72crm_crm_contacts_user_star where user_id = " + BaseUtil.getUserId() + " and contacts_id = views.contacts_id) as star";
    }

    @Override
    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject jsonObject = super.appendOtherSql(isDataAuth, kv, crmEnum);
        String conditions = " and owner_user_id is not null ";
        jsonObject.put("sql", jsonObject.getString("sql") + conditions);
        return jsonObject;
    }

    /**
     * @author wyq
     * 根据id查询联系人
     */
    public Record queryById(Integer contactsId){
        Record crmContacts = Db.findFirst(Db.getSql("crm.contact.queryById"), BaseUtil.getUserId(), contactsId);
        List<Record> recordList = Db.find("select name,value from `72crm_crm_contacts_data` where batch_id = ?", crmContacts.getStr("batch_id"));
        recordList.forEach(field->crmContacts.set(field.getStr("name"),field.getStr("value")));
        return crmContacts;
    }

    /**
     * @author wyq
     * 基本信息
     */
    public List<Record> information(Integer contactsId){
        Record record = queryById(contactsId);
        List<String> keyList = Arrays.asList("name", "telephone", "mobile","email","post","address","next_time","remark");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间","更新时间","下次联系时间","最后跟进时间");
        List<Record> recordList = adminFieldService.queryInformation(CrmEnum.CRM_CONTACTS,record, keyList);
        recordList.add(new Record().set("name","客户名称").set("value",new Record().set("customerId",record.getInt("customer_id")).set("customerName",record.getStr("customer_name"))).set("formType","customer").set("field_type",1));
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
     * 根据联系人id查询商机
     */
    public R queryBusiness(BasePageRequest<CrmContacts> basePageRequest){
        Integer contactsId = basePageRequest.getData().getContactsId();
        Integer pageType = basePageRequest.getPageType();
        if (0 == pageType){
            List<Record> recordList = Db.find(Db.getSql("crm.contact.queryBusiness"), contactsId);
            Aop.get(CrmBusinessService.class).setBusinessStatus(recordList);
            return R.ok().put("data", recordList);
        }else {
            Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), new SqlPara().setSql(Db.getSql("crm.contact.queryBusiness")).addPara(contactsId));
            Aop.get(CrmBusinessService.class).setBusinessStatus(page.getList());
            return R.ok().put("data", page);
        }
    }

    /**
     * @author wyq
     * 联系人关联商机
     */
    @Before(Tx.class)
    public R relateBusiness(Integer contactsId, String businessIds){
        String[] businessIdsArr = businessIds.split(",");
        Db.delete("delete from 72crm_crm_contacts_business where contacts_id = ? ",contactsId);
        for (String id:businessIdsArr){
            CrmContactsBusiness crmContactsBusiness = new CrmContactsBusiness();
            crmContactsBusiness.setContactsId(contactsId);
            crmContactsBusiness.setBusinessId(Integer.valueOf(id));
            crmContactsBusiness.save();
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 联系人解除关联商机
     */
    public R unrelateBusiness(Integer contactsId, String businessIds){
        String[] idsArr = businessIds.split(",");
        Kv kv=Kv.by("contactsId",contactsId).set("ids",idsArr);
        SqlPara sqlPara = Db.getSqlPara("crm.contact.unrelateBusiness",kv);
        //解除关联商机时将首要联系人也清空
        SqlPara sqlPara1= Db.getSqlPara("crm.contact.unrelateBusinessContacts",kv);
        Db.delete(sqlPara.getSql(),sqlPara.getPara());
        Db.update(sqlPara1);
        return R.ok();
    }

    /**
     * @author wyq
     * 新建或更新联系人
     */
    @Before(Tx.class)
    public R addOrUpdate(JSONObject jsonObject){
        CrmContacts crmContacts = jsonObject.getObject("entity",CrmContacts.class);
        String batchId = StrUtil.isNotEmpty(crmContacts.getBatchId()) ? crmContacts.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(jsonObject.getJSONArray("field"), Kv.by("batchId", batchId).set("dataTableName", "72crm_crm_contacts_data"));
        save(jsonObject.getJSONArray("field"), batchId);
        if(StrUtil.isEmpty(crmContacts.getEmail())){
            crmContacts.setEmail(null);
        }
        boolean isSuccess;
        if (crmContacts.getContactsId() != null) {
            crmContacts.setUpdateTime(DateUtil.date());
            actionRecordUtil.updateRecord(new CrmContacts().dao().findById(crmContacts.getContactsId()), crmContacts, CrmEnum.CRM_CONTACTS);
            isSuccess=crmContacts.update();
        }else {
            crmContacts.setCreateTime(DateUtil.date());
            crmContacts.setUpdateTime(DateUtil.date());
            crmContacts.setCreateUserId(BaseUtil.getUserId());
            if (crmContacts.getOwnerUserId() == null){
                crmContacts.setOwnerUserId(BaseUtil.getUserId());
            }
            crmContacts.setBatchId(batchId);
            isSuccess = crmContacts.save();
            if (jsonObject.getInteger("businessId") != null){
                CrmContactsBusiness crmContactsBusiness = new CrmContactsBusiness();
                crmContactsBusiness.setBusinessId(jsonObject.getInteger("businessId"));
                crmContactsBusiness.setContactsId(crmContacts.getContactsId());
                crmContactsBusiness.save();
            }
            crmActivityService.addActivity(2, CrmActivityEnum.CONTACTS,crmContacts.getContactsId(),"");
            actionRecordUtil.addRecord(crmContacts.getContactsId(), CrmEnum.CRM_CONTACTS);
        }
        //查询是否存在首要联系人
        CrmCustomer customer=CrmCustomer.dao.findById(crmContacts.getCustomerId());
        if(customer!=null&&customer.getContactsId()==null){
            customer.setContactsId(crmContacts.getContactsId());
            customer.update();
        }
        return R.isSuccess(isSuccess);
    }

    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_contacts_data", "batch_id", batchId);
        List<CrmContactsData> Fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            CrmContactsData fieldv = TypeUtils.castToJavaBean(obj, CrmContactsData.class);
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
     * 根据id删除联系人
     */
    @Before(Tx.class)
    public R deleteByIds(String contactsIds){
        List<String> idList = StrUtil.splitTrim(contactsIds,",");
        for (String id : idList) {
            Integer number = Db.queryInt("select count(*) from 72crm_crm_contract where contacts_id = ? and check_status != 7", id);
            if (number > 0) {
                return R.error("该数据已被其他模块引用，不能被删除！");
            }
            CrmContacts contacts = CrmContacts.dao.findById(id);
            if (contacts != null) {
                //删除自定义字段
                Db.deleteById("72crm_crm_contacts_data", "batch_id", contacts.getBatchId());
                Db.deleteById("72crm_crm_contacts_business", "contacts_id", contacts.getContactsId());
                Db.template("crm.contact.updateCustomerContacts", contacts.getContactsId()).update();
                Db.template("crm.contact.updateBusinessContacts", contacts.getContactsId()).update();
                //删除跟进记录
                Aop.get(CrmActivityService.class).deleteActivityRecord(CrmActivityEnum.CONTACTS, contacts.getContactsId().toString());
                //删除字段操作记录
                Aop.get(CrmRecordService.class).deleteActionRecord(CrmEnum.CRM_CONTACTS, contacts.getContactsId().toString());
                actionRecordUtil.addDeleteActionRecord(CrmEnum.CRM_CONTACTS, contactsIds.split(","));
                //删除文件
                Aop.get(AdminFileService.class).removeByBatchId(contacts.getBatchId());
                contacts.delete();
            }
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 联系人转移
     */
    @Before(Tx.class)
    public R transfer(CrmContacts crmContacts){
        String[] contactsIdsArr = crmContacts.getContactsIds().split(",");
        for(String contactsId : contactsIdsArr){
            if (AuthUtil.isRwAuth(Integer.parseInt(contactsId), CrmEnum.CRM_CONTACTS)) {
                return R.error("无权限转移");
            }
            actionRecordUtil.addConversionRecord(Integer.valueOf(contactsId), CrmEnum.CRM_CONTACTS, crmContacts.getNewOwnerUserId());
        }
        int update = Db.update(Db.getSqlPara("crm.contact.transfer", Kv.by("ownerUserId", crmContacts.getNewOwnerUserId()).set("ids", contactsIdsArr)));
        return update > 0 ? R.ok() : R.error();
    }

    /**
     * 根据客户id变更负责人
     * @param customerId 客户ID
     * @param ownerUserId 负责人ID
     */
    public R updateOwnerUserId(Integer customerId, Long ownerUserId){
        List<Integer> contactsIdList = Db.query("select contacts_id from 72crm_crm_contacts where customer_id = ?",customerId);
        for (Integer contactsId : contactsIdList) {
            if (AuthUtil.isRwAuth(contactsId, CrmEnum.CRM_CONTACTS)) {
                return R.error("无权限转移");
            }
        }
        int update = Db.update("update 72crm_crm_contacts set owner_user_id = ? where customer_id = ?", ownerUserId, customerId);
        actionRecordUtil.addConversionRecord(customerId, CrmEnum.CRM_CUSTOMER, ownerUserId);
        return R.ok();
    }

    /**
     * @author wyq
     * 查询编辑字段
     */
    public List<Record> queryField(Integer contactsId) {
        Record contacts = queryById(contactsId);
        List<Record> customerList = new ArrayList<>();
        Record customer = new Record();
        customerList.add(customer.set("customer_id",contacts.getInt("customer_id")).set("customer_name",contacts.getStr("customer_name")));
        contacts.set("customer_id",customerList);
        return adminFieldService.queryUpdateField(CrmEnum.CRM_CONTACTS.getType(),contacts);
    }

    /**
     * @author wyq
     * 获取联系人导入查重字段
     */
    public R getCheckingField(){
        return R.ok().put("data","联系人姓名,电话,手机");
    }

    /**
     * @author wyq
     * 附件列表
     */
    public R queryFileList(Integer contactsId) {
        SqlPara sqlPara=Db.getSqlPara("crm.contact.queryFileList", Kv.by("contactsId", contactsId));
        return R.ok().put("data", Db.find(sqlPara));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public R num(Integer contactsId) {
        Kv kv=Kv.by("contactsId", contactsId);
        Record record = Db.findFirst(Db.getSqlPara("crm.contact.queryNum", kv));
        return R.ok().put("data", record);
    }

    /**
     * 标星（关注）
     */
    public R star(Integer contactsId) {
        Long userId = BaseUtil.getUserId();
        CrmContactsUserStar userStar = CrmContactsUserStar.dao.findFirst("select * from 72crm_crm_contacts_user_star where user_id = ? and contacts_id = ?", userId, contactsId);
        List<Integer> contactsIdList = Db.query("select contacts_id from 72crm_crm_contacts_user_star where user_id = ?", userId);
        if (ObjectUtil.isEmpty(userStar)) {
            new CrmContactsUserStar().setUserId(userId).setContactsId(contactsId).save();
            contactsIdList.add(contactsId);
        } else {
            userStar.delete();
            contactsIdList.remove(contactsId);
        }
        String contactsIds = CollectionUtil.isEmpty(contactsIdList) ? "0" : CollectionUtil.join(contactsIdList, ",");
        JSONObject data = new JSONObject().fluentPut("contacts_id", new JSONObject().fluentPut("name", "contacts_id").fluentPut("condition", "in").fluentPut("value", contactsIds));
        Db.update("update 72crm_admin_scene set data = ? where type = 3 and user_id = ? and bydata = 'star'", data.toString(), userId);
        return R.ok();
    }
}
