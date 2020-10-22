package com.kakarote.crm9.erp.crm.service;

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
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminExamineLog;
import com.kakarote.crm9.erp.admin.service.AdminExamineRecordService;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.crm.entity.CrmContract;
import com.kakarote.crm9.erp.crm.entity.CrmReceivables;
import com.kakarote.crm9.erp.crm.entity.CrmReceivablesData;
import com.kakarote.crm9.erp.crm.entity.CrmReceivablesPlan;
import com.kakarote.crm9.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CrmReceivablesService extends AbstractBasePageList {

    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmRecordService crmRecordService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    @Inject
    private AdminExamineRecordService examineRecordService;


    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        Kv kv = getKv(basePageRequest, CrmEnum.CRM_RECEIVABLES, isDataAuth);
        if (kv.containsKey("error")){
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.receivables.queryReceivablesPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.receivables.queryReceivablesPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        resultJsonObject.put("list", recordPage);
        Record record = Db.template("crm.receivables.queryReceivablesPageListCount", kv).findFirst();
        resultJsonObject.put("totalRow", record.getInt("count"));
        if (recordPage.size() > 0) {
            resultJsonObject.put("money", new Record().set("receivablesMoney", record.getStr("receivablesMoney")));
        }
        return R.ok().put("data", resultJsonObject);
    }


    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (customer_name like '%{search}%' or number like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 3);
        return sqlObject;
    }

    @Override
    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject jsonObject = super.appendOtherSql(isDataAuth, kv, crmEnum);
        String conditions = " and check_status != 7 ";
        jsonObject.put("sql", jsonObject.getString("sql") + conditions);
        return jsonObject;
    }

    /**
     * 分页查询回款
     */
    public Page<Record> queryPage(BasePageRequest<CrmReceivables> basePageRequest) {
        return Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.receivables.getReceivablesPageList"));
    }

    /**
     * 新建或者修改回款
     *
     * @param jsonObject
     */
    @Before(Tx.class)
    public R saveOrUpdate(JSONObject jsonObject) {
        CrmReceivables crmReceivables = jsonObject.getObject("entity", CrmReceivables.class);
        CrmContract crmContract= CrmContract.dao.findById(crmReceivables.getContractId());
        if(crmContract==null||!crmContract.getCheckStatus().equals(1)){
            return R.error("当前合同未审核通过，不能添加回款");
        }
        if (ParamsUtil.isOutMoney(crmReceivables.getMoney())){
            return R.outMaxMoney();
        }
        crmReceivables.setCreateUserId(BaseUtil.getUser().getUserId());
        String batchId = StrUtil.isNotEmpty(crmReceivables.getBatchId()) ? crmReceivables.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(jsonObject.getJSONArray("field"), Kv.by("batchId", batchId).set("dataTableName", "72crm_crm_receivables_data"));
        save(jsonObject.getJSONArray("field"), batchId);
        Integer examineCount = Db.queryInt("select count(1) from 72crm_admin_examine where category_type = 2 and status = 1");
        Record examineData = new Record();
        if (crmReceivables.getReceivablesId() == null) {
            AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 7");
            if (adminConfig.getStatus() == 1 && StrUtil.isEmpty(crmReceivables.getNumber())) {
                String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                crmReceivables.setNumber(result);
            }
            Integer count = Db.queryInt(Db.getSql("crm.receivables.queryByNumber"), crmReceivables.getNumber());
            if (count != null && count > 0) {
                return R.error("回款编号已存在，请校对后再添加！");
            }
            crmReceivables.setCreateTime(DateUtil.date());
            crmReceivables.setUpdateTime(DateUtil.date());
            crmReceivables.setBatchId(batchId);
            crmReceivables.setOwnerUserId(BaseUtil.getUser().getUserId());

            if (examineCount > 0) {
                examineData = examineRecordService.saveExamineRecord(2,
                        jsonObject.getLong("checkUserId"),
                        crmReceivables.getOwnerUserId(), null, crmReceivables.getCheckStatus());
                if (examineData.getInt("status") == 0) {
                    return R.error("没有启动的审核步骤，不能添加！");
                } else if (examineData.getInt("status") == 2) {
                    return R.error("没有审批人，不能添加！");
                } else {
                    crmReceivables.setExamineRecordId(examineData.getInt("recordId"));
                }
                if (crmReceivables.getCheckStatus() == null || crmReceivables.getCheckStatus() != 5) {
                    crmReceivables.setCheckStatus(0);
                }
            } else {
                if (crmReceivables.getCheckStatus() == null || crmReceivables.getCheckStatus() != 5) {
                    crmReceivables.setCheckStatus(1);
                }
            }
            crmReceivables.save();
            CrmReceivablesPlan crmReceivablesPlan = CrmReceivablesPlan.dao.findById(crmReceivables.getPlanId());
            if (crmReceivablesPlan != null) {
                crmReceivablesPlan.setReceivablesId(crmReceivables.getReceivablesId());
                crmReceivablesPlan.setUpdateTime(DateUtil.date());
                crmReceivablesPlan.update();
            }
            Aop.get(CrmActivityService.class).addActivity(2, CrmActivityEnum.RECEIVABLES,crmReceivables.getReceivablesId(),"");
            actionRecordUtil.addRecord(crmReceivables.getReceivablesId(), CrmEnum.CRM_RECEIVABLES);
        } else {
            CrmReceivables receivables = CrmReceivables.dao.findById(crmReceivables.getReceivablesId());
            if (!receivables.getCreateUserId().equals(BaseUtil.getUserId()) && examineCount > 0) {
                return R.error("只能编辑自己创建的回款");
            }
            if (receivables.getCheckStatus() != 4 && receivables.getCheckStatus() != 2 && receivables.getCheckStatus() != 5) {
                return R.error("不能编辑，请先撤回再编辑！");
            }
            if (examineCount > 0) {
                examineData = examineRecordService.saveExamineRecord(2,
                        jsonObject.getLong("checkUserId"),
                        receivables.getOwnerUserId(),
                        receivables.getExamineRecordId(), crmReceivables.getCheckStatus());
                if (examineData.getInt("status") == 0) {
                    return R.error("没有启动的审核步骤，不能添加！");
                } else if (examineData.getInt("status") == 2) {
                    return R.error("没有审批人，不能添加！");
                } else {
                    crmReceivables.setExamineRecordId(examineData.getInt("recordId"));
                }
                if (crmReceivables.getCheckStatus() == null || crmReceivables.getCheckStatus() != 5) {
                    crmReceivables.setCheckStatus(0);
                }
            } else {
                if (crmReceivables.getCheckStatus() == null || crmReceivables.getCheckStatus() != 5) {
                    crmReceivables.setCheckStatus(1);
                }
                if (receivables.getExamineRecordId() != null) {
                    Db.update("update 72crm_admin_examine_log set is_recheck = 1 where record_id = ?", receivables.getExamineRecordId());
                }
            }
            actionRecordUtil.updateRecord(new CrmReceivables().dao().findById(crmReceivables.getReceivablesId()), crmReceivables, CrmEnum.CRM_RECEIVABLES);
            crmReceivables.setUpdateTime(DateUtil.date());
            Db.update("update 72crm_crm_receivables_plan set receivables_id = null where receivables_id = ?",receivables.getReceivablesId());
            CrmReceivablesPlan crmReceivablesPlan = CrmReceivablesPlan.dao.findById(crmReceivables.getPlanId());
            if (crmReceivablesPlan != null) {
                crmReceivablesPlan.setReceivablesId(crmReceivables.getReceivablesId());
                crmReceivablesPlan.setUpdateTime(DateUtil.date());
                crmReceivablesPlan.update();
            }
            crmReceivables.update();
        }
        actionRecordUtil.addCrmExamineActionRecord(CrmEnum.CRM_RECEIVABLES, examineData.getInt("recordId"), BehaviorEnum.SUBMIT_EXAMINE);
        addExamineNotice(crmReceivables, examineData);
        return R.ok();
    }

    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_receivables_data", "batch_id", batchId);
        List<CrmReceivablesData> Fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            CrmReceivablesData fieldv = TypeUtils.castToJavaBean(obj, CrmReceivablesData.class);
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            Fieldvs.add(fieldv);
        });
        Db.batchSave(Fieldvs, 100);
        return true;
    }

    private void addExamineNotice(CrmReceivables crmReceivables, Record examineData) {
        //判断当前是否提交审核，提交审核需要生成一条系统通知
        if (ObjectUtil.equal(crmReceivables.getCheckStatus(), 0)) {
            List<Long> examineLogIdList = examineData.get("examineLogIds");
            examineLogIdList.forEach(examineLogId ->{
                AdminExamineLog examineLog = AdminExamineLog.dao.findById(examineLogId);
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE,examineLog, BaseUtil.getUserId());
            });
        }
    }

    /**
     * 根据id查询回款
     */
    public Record queryById(Integer id) {
        Record record = Db.findFirst(Db.getSql("crm.receivables.queryReceivablesById"), id);
        List<Record> recordList = Db.find("select name,value from `72crm_crm_receivables_data` where batch_id = ?", record.getStr("batch_id"));
        recordList.forEach(field -> record.set(field.getStr("name"), field.getStr("value")));
        return record;
    }

    /**
     * 根据id查询回款基本信息
     */
    public List<Record> information(Integer id) {
        Record record = queryById(id);
        List<String> keyList = Arrays.asList("number", "return_time", "money", "remark", "return_type");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间","更新时间");
        List<Record> recordList = adminFieldService.queryInformation(CrmEnum.CRM_RECEIVABLES,record, keyList);
        recordList.add(new Record().set("name","审核状态").set("value",record.getInt("check_status")).set("formType", "check_status").set("field_type",1));
        recordList.add(new Record().set("name","合同编号").set("value",new Record().set("contactId",record.getInt("contract_id")).set("contractNum",record.getStr("contract_num"))).set("formType","contract").set("field_type",1));
        recordList.add(new Record().set("name","合同名称").set("value",new Record().set("contactId",record.getInt("contract_id")).set("contractNum",record.getStr("contract_name"))).set("formType","contract").set("field_type",1));
        recordList.add(new Record().set("name","客户名称").set("value",new Record().set("customerId",record.getInt("customer_id")).set("customerName",record.getStr("customer_name"))).set("formType","customer").set("field_type",1));
        recordList.add(new Record().set("name", "期数").set("value", record.getStr("plan_num")).set("formType", "plan_num").set("field_type", FormTypeEnum.NUMBER.getType()));
        recordList.forEach(record1 -> {
            String name = record1.getStr("name");
            if ("回款日期".equals(name)){
                if (ObjectUtil.isNotEmpty(record1.get("value"))){
                    record1.set("value",DateUtil.formatDate(record1.getDate("value")));
                }
            }
        });
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
     * 根据id删除回款
     */
    @Before(Tx.class)
    public R deleteByIds(String receivablesIds) {
        String[] idsArr = receivablesIds.split(",");
        List<CrmReceivables> list = CrmReceivables.dao.find(Db.getSqlPara("crm.receivablesplan.queryReceivablesReceivablesId", Kv.by("receivablesIds", idsArr)));
        if (list.size() > 0) {
            return R.error("该数据已被其他模块引用，不能被删除！");
        }
        for (String id : idsArr) {
            CrmReceivables receivables = CrmReceivables.dao.findById(id);
            boolean bol = (receivables.getCheckStatus() != 4 && receivables.getCheckStatus() != 5) && (!BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()));
            if (bol){
                return R.noAuth();
            }
            CrmContract contract = CrmContract.dao.findById(receivables.getContractId());
            if (receivables.getCheckStatus() == 1){
                //还原合同金额
                Db.template("crm.contract.updateUnreceivedMoney", Kv.by("money", receivables.getMoney()).set("contractId", contract.getContractId())).update();
            }
            Db.delete("delete FROM 72crm_crm_receivables_data where batch_id = ?", receivables.getBatchId());
            //删除跟进记录
            Aop.get(CrmActivityService.class).deleteActivityRecord(CrmActivityEnum.RECEIVABLES,id);
            //删除字段操作记录
            Aop.get(CrmRecordService.class).deleteActionRecord(CrmEnum.CRM_RECEIVABLES,receivables.getReceivablesId().toString());
            actionRecordUtil.addDeleteActionRecord(CrmEnum.CRM_RECEIVABLES, idsArr);
            //删除自定义字段
            Db.template("admin.field.deleteFieldv", Kv.by("batchList", receivables.getBatchId()).set("dataTableName", "72crm_crm_receivables_data")).delete();
            //删除文件
            Aop.get(AdminFileService.class).removeByBatchId(receivables.getBatchId());
            if (ObjectUtil.isNotEmpty(receivables.getExamineRecordId())) {
                Aop.get(AdminExamineRecordService.class).deleteExamine(receivables.getExamineRecordId());
            }
            receivables.setCheckStatus(7);
            receivables.update();
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 查询编辑字段
     */
    public List<Record> queryField(Integer receivablesId) {
        Record receivables = queryById(receivablesId);
        List<Record> list = new ArrayList<>();
        list.add(new Record().set("customer_id", receivables.getInt("customer_id")).set("customer_name", receivables.getStr("customer_name")));
        receivables.set("customer_id", list);
        list = new ArrayList<>();
        list.add(new Record().set("contract_id", receivables.getStr("contract_id")).set("contract_num", receivables.getStr("contract_num")));
        receivables.set("contract_id", list);
        return adminFieldService.queryUpdateField(CrmEnum.CRM_RECEIVABLES.getType(), receivables);
    }



    /**
     * 根据条件查询回款
     */
    public List<Record> queryList(CrmReceivables receivables) {
        String sq = "select * from 72crm_crm_receivables where 1 = 1 ";
        StringBuffer sql = new StringBuffer(sq);
        if (receivables.getCustomerId() != null) {
            sql.append(" and customer_id = ").append(receivables.getCustomerId());
        }
        if (receivables.getContractId() != null) {
            sql.append(" and contract_id = ").append(receivables.getContractId());
        }
        return Db.find(sql.toString());
    }

    /**
     * 根据合同id查询回款
     */
    public R queryListByContractId(BasePageRequest<CrmReceivables> basePageRequest) {
        Integer pageType = basePageRequest.getPageType();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_RECEIVABLES, "rec", 1);
        if (0 == pageType) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.receivables.queryReceivablesPageList", Kv.by("contractId",basePageRequest.getData().getContractId()).set("conditions",conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.receivables.queryReceivablesPageList", Kv.by("contractId",basePageRequest.getData().getContractId()).set("conditions",conditions))));
        }
    }

    /**
     * 回款转移
     */
    @Permissions("crm:receivables:transfer")
    public R transfer(String receivablesIds, Long newOwnerUserId){
        String[] receivablesIdsArr = receivablesIds.split(",");
        for (String receivablesId : receivablesIdsArr){
            CrmReceivables crmReceivables = new CrmReceivables();
            crmReceivables.setReceivablesId(Integer.valueOf(receivablesId));
            crmReceivables.setOwnerUserId(newOwnerUserId);
            crmReceivables.update();
            actionRecordUtil.addConversionRecord(Integer.valueOf(receivablesId), CrmEnum.CRM_RECEIVABLES, newOwnerUserId);
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 附件列表
     */
    public R queryFileList(Integer receivablesId) {
        SqlPara sqlPara=Db.getSqlPara("crm.receivables.queryFileList", Kv.by("receivablesId", receivablesId));
        return R.ok().put("data", Db.find(sqlPara));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public R num(Integer receivablesId) {
        Kv kv=Kv.by("receivablesId", receivablesId);
        Record record = Db.findFirst(Db.getSqlPara("crm.receivables.queryNum", kv));
        return R.ok().put("data", record);
    }

    /**
     * @author wyq
     * 回款导出
     */
    public List<Record> exportReceivables(Kv kv) {
        return Db.find(Db.getSqlPara("crm.receivables.excelExport", kv));
    }
}
