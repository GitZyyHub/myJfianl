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
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminExamineLog;
import com.kakarote.crm9.erp.admin.entity.AdminNumberSetting;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminExamineRecordService;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.utils.*;

import java.util.*;
import java.util.stream.Collectors;

public class CrmContractService extends AbstractBasePageList {

    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmRecordService crmRecordService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    @Inject
    private AdminExamineRecordService examineRecordService;

    @Inject
    private CrmActivityService crmActivityService;


    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        Kv kv = getKv(basePageRequest, CrmEnum.CRM_CONTRACT, isDataAuth);
        if (kv.containsKey("error")){
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.contract.queryContractPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.contract.queryContractPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        resultJsonObject.put("list", recordPage);
        Record record = Db.template("crm.contract.queryContractPageListCount", kv).findFirst();
        resultJsonObject.put("totalRow", record.getInt("count"));
        if(recordPage.size() > 0){
            resultJsonObject.put("money", record.remove("count"));
        }
        return R.ok().put("data", resultJsonObject);
    }


    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (name like '%{search}%' or customer_name like '%{search}%' or num like '%{search}%')";
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
     * 根据id查询合同
     */
    public Record queryById(Integer id) {
        Record record = Db.findFirst(Db.getSql("crm.contract.queryByContractId"), id);
        AdminUser user = BaseUtil.getUser();
        if (user.getUserId().equals(BaseUtil.getSuperUser()) || user.getRoles().contains(BaseUtil.getSuperRole())){
            record.set("isAdmin",1);
        }else {
            record.set("isAdmin",0);
        }
        List<Record> recordList = Db.find("select name,value from `72crm_crm_contract_data` where batch_id = ?", record.getStr("batch_id"));
        recordList.forEach(field -> record.set(field.getStr("name"), field.getStr("value")));
        return record;
    }

    /**
     * 根据id查询合同基本信息
     */
    public List<Record> information(Integer id) {
        Record record = queryById(id);
        List<String> keyList = Arrays.asList("num","name","order_date","money","start_time","end_time","remark");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间","更新时间","下次联系时间","最后跟进时间","已收款金额","未收款金额");
        List<Record> recordList = adminFieldService.queryInformation(CrmEnum.CRM_CONTRACT,record, keyList);
        recordList.add(new Record().set("name","审核状态").set("value",record.getInt("check_status")).set("formType", "check_status").set("field_type",1));
        recordList.add(new Record().set("name","商机名称").set("value",new Record().set("businessId",record.getInt("business_id")).set("businessName",record.getStr("business_name"))).set("formType","business").set("field_type",1));
        recordList.add(new Record().set("name","客户签约人").set("value",new Record().set("contacts_id",record.getInt("contacts_id")).set("contacts_name",record.getStr("contacts_name"))).set("formType","contacts").set("field_type",1));
        recordList.add(new Record().set("name","公司签约人").set("value",Db.template("admin.user.queryByIds",Kv.by("ids",TagUtil.toSet(record.getStr("company_user_id")))).find()).set("formType","user").set("field_type",1));
        recordList.add(new Record().set("name","客户名称").set("value",new Record().set("customerId",record.getInt("customer_id")).set("customerName",record.getStr("customer_name"))).set("formType","customer").set("field_type",1));
        recordList.add(new Record().set("name", "已收款金额").set("value", record.getBigDecimal("received_money")).set("formType", FormTypeEnum.NUMBER.getFormType()).set("field_type", 1));
        recordList.add(new Record().set("name", "未收款金额").set("value", record.getBigDecimal("unreceived_money")).set("formType", FormTypeEnum.NUMBER.getFormType()).set("field_type", 1));
        recordList.forEach(record1 -> {
            String name = record1.getStr("name");
            if ("下单时间".equals(name) || "合同开始时间".equals(name) || "合同结束时间".equals(name)){
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
     * 根据id删除合同
     */
    @Before(Tx.class)
    public R deleteByIds(String contractIds) {

        String[] idsArr = contractIds.split(",");
        List<CrmReceivables> list = CrmReceivables.dao.find(Db.getSqlPara("crm.receivables.queryReceivablesByContractIds", Kv.by("contractIds", idsArr)));
        if (list.size() > 0) {
            return R.error("该数据已被其他模块引用，不能被删除！");
        }
        for (String id : idsArr) {
            CrmContract contract = CrmContract.dao.findById(id);
            boolean bol = (contract.getCheckStatus() != 4 && contract.getCheckStatus() != 5) && (!BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()));
            if (bol){
                return R.noAuth();
            }
            //删除合同产品关联
            Db.delete("delete from 72crm_crm_contract_product where contract_id = ?", id);
            //删除跟进记录
            Aop.get(CrmActivityService.class).deleteActivityRecord(CrmActivityEnum.CONTRACT,id);
            //删除字段操作记录
            Aop.get(CrmRecordService.class).deleteActionRecord(CrmEnum.CRM_CONTRACT,contract.getContractId().toString());
            actionRecordUtil.addDeleteActionRecord(CrmEnum.CRM_CONTRACT, idsArr);
            //删除自定义字段
            Db.template("admin.field.deleteFieldv", Kv.by("batchList", contract.getBatchId()).set("dataTableName", "72crm_crm_contract_data")).delete();
            //删除文件
            Aop.get(AdminFileService.class).removeByBatchId(contract.getBatchId());
            if (ObjectUtil.isNotEmpty(contract.getExamineRecordId())){
                Aop.get(AdminExamineRecordService.class).deleteExamine(contract.getExamineRecordId());
            }
            contract.setCheckStatus(7);
            contract.update();
        }
        return R.ok();
    }

    /**
     * 添加或修改
     */
    @Before(Tx.class)
    public R saveAndUpdate(JSONObject jsonObject) {
        CrmContract crmContract = jsonObject.getObject("entity", CrmContract.class);
        if (crmContract.getContractId() != null && AuthUtil.isCrmOperateAuth(CrmEnum.CRM_CONTRACT, crmContract.getContractId())) {
            return R.error("没有权限");
        }
        if (ParamsUtil.isOutMoney(crmContract.getMoney()) || ParamsUtil.isOutMoney(crmContract.getTotalPrice())){
            return R.outMaxMoney();
        }
        String batchId = StrUtil.isNotEmpty(crmContract.getBatchId()) ? crmContract.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(jsonObject.getJSONArray("field"), Kv.by("batchId", batchId).set("dataTableName", "72crm_crm_contract_data"));
        save(jsonObject.getJSONArray("field"), batchId);
        boolean flag;
        Record examineData = new Record();
        if (crmContract.getStartTime() != null && crmContract.getEndTime() != null && crmContract.getStartTime().compareTo(crmContract.getEndTime()) > 0) {
            return R.error("结束时间应大于开始时间");
        }
        crmContract.setUnreceivedMoney(crmContract.getMoney());

        Integer examineCount = Db.queryInt("select count(1) from 72crm_admin_examine where category_type = 1 and status = 1");

        if (crmContract.getContractId() == null) {
            AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 6");
            if (adminConfig.getStatus() == 1 && StrUtil.isEmpty(crmContract.getNum())) {
                String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, crmContract.getOrderDate());
                crmContract.setNum(result);
            }
            Integer contract = Db.queryInt(Db.getSql("crm.contract.queryByNum"), crmContract.getNum());
            if (contract != 0) {
                return R.error("合同编号已存在，请校对后再添加！");
            }
            crmContract.setCreateUserId(BaseUtil.getUser().getUserId());
            crmContract.setBatchId(batchId);
            crmContract.setCreateTime(DateUtil.date());
            crmContract.setUpdateTime(DateUtil.date());
            crmContract.setRoUserId(",");
            crmContract.setRwUserId(",");

            crmContract.setOwnerUserId(BaseUtil.getUser().getUserId());

            if (examineCount > 0) {
                examineData = examineRecordService.saveExamineRecord(1, jsonObject.getLong("checkUserId"), crmContract.getOwnerUserId(), null, crmContract.getCheckStatus());
                if (examineData.getInt("status") == 0) {
                    return R.error("没有启动的审核步骤，不能添加！");
                } else if (examineData.getInt("status") == 2) {
                    return R.error("没有审批人，不能添加！");
                } else {
                    crmContract.setExamineRecordId(examineData.getInt("recordId"));
                }
                if (crmContract.getCheckStatus() != null && crmContract.getCheckStatus() == 5) {
                    crmContract.setCheckStatus(5);
                } else {
                    crmContract.setCheckStatus(0);
                }
            } else {
                if (crmContract.getCheckStatus() == null) {
                    crmContract.setCheckStatus(1);
                }
            }
            flag = crmContract.save();
            crmActivityService.addActivity(2, CrmActivityEnum.CONTRACT,crmContract.getContractId(),"");
            actionRecordUtil.addRecord(crmContract.getContractId(), CrmEnum.CRM_CONTRACT);
        } else {
            CrmContract contract = CrmContract.dao.findById(crmContract.getContractId());
//            if (!contract.getCreateUserId().equals(BaseUtil.getUserId())){
//                return R.error("只能编辑自己创建的合同");
//            }
            if (contract.getCheckStatus() == 8) {
                return R.error("合同已作废，不能编辑");
            }
            if (contract.getCheckStatus() == 1) {
                return R.error("已通过的合同不能编辑");
            }
            if (contract.getCheckStatus() != 4 && contract.getCheckStatus() != 2 && contract.getCheckStatus() != 5) {
                return R.error("不能编辑，请先撤回再编辑！");
            }
            if (examineCount > 0) {
                examineData = examineRecordService.saveExamineRecord(1,
                        jsonObject.getLong("checkUserId"), contract.getOwnerUserId(),
                        contract.getExamineRecordId(), crmContract.getCheckStatus());
                if (examineData.getInt("status") == 0) {
                    return R.error("没有启动的审核步骤，不能添加！");
                } else if (examineData.getInt("status") == 2) {
                    return R.error("没有审批人，不能添加！");
                } else {
                    crmContract.setExamineRecordId(examineData.getInt("recordId"));
                }
                if (crmContract.getCheckStatus() != null) {
                    crmContract.setCheckStatus(5);
                } else {
                    crmContract.setCheckStatus(0);
                }
            } else {
                if (crmContract.getCheckStatus() == null) {
                    crmContract.setCheckStatus(1);
                }
                if (contract.getExamineRecordId() != null) {
                    Db.update("update 72crm_admin_examine_log set is_recheck = 1 where record_id = ?", contract.getExamineRecordId());
                }
            }
            crmContract.setUpdateTime(DateUtil.date());
            actionRecordUtil.updateRecord(new CrmContract().dao().findById(crmContract.getContractId()), crmContract, CrmEnum.CRM_CONTRACT);
            flag = crmContract.update();
        }
        //判断当前是否提交审核，提交审核需要生成一条系统通知
        if (0 == crmContract.getCheckStatus() ){
            Aop.get(ActionRecordUtil.class).addCrmExamineActionRecord(CrmEnum.CRM_CONTRACT, examineData.getInt("recordId"), BehaviorEnum.SUBMIT_EXAMINE);
            List<Long> examineLogIdList = examineData.get("examineLogIds");
            examineLogIdList.forEach(examineLogId ->{
                AdminExamineLog examineLog = AdminExamineLog.dao.findById(examineLogId);
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_EXAMINE,examineLog,BaseUtil.getUserId());
            });
        }
        JSONArray jsonArray = jsonObject.getJSONArray("product");
        if (jsonArray != null) {
            List<CrmContractProduct> contractProductList = jsonArray.toJavaList(CrmContractProduct.class);
            //删除之前的合同产品关联表
            Db.delete(Db.getSql("crm.contract.deleteByContractId"), crmContract.getContractId());
            if (crmContract.getBusinessId() != null) {
                Db.delete("delete from 72crm_crm_business_product where business_id = ?", crmContract.getBusinessId());
            }
            if (contractProductList != null) {
                for (CrmContractProduct crmContractProduct : contractProductList) {
                    crmContractProduct.setContractId(crmContract.getContractId());
                    crmContractProduct.save();
                    if (crmContract.getBusinessId() != null) {
                        CrmBusinessProduct crmBusinessProduct = new CrmBusinessProduct()._setOrPut(crmContractProduct.toRecord().getColumns());
                        crmBusinessProduct.setRId(null);
                        crmBusinessProduct.setBusinessId(crmContract.getBusinessId());
                        crmBusinessProduct.save();
                    }
                }
            }

        }

        return R.isSuccess(flag);

    }

    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_contract_data", "batch_id", batchId);
        List<CrmContractData> Fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            CrmContractData fieldv = TypeUtils.castToJavaBean(obj, CrmContractData.class);
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            Fieldvs.add(fieldv);
        });
        Db.batchSave(Fieldvs, 100);
        return true;
    }

    /**
     * 根据条件查询合同
     */
    public List<CrmContract> queryList(CrmContract crmContract) {
        StringBuilder sql = new StringBuilder("select * from 72crm_crm_contract where 1 = 1 ");
        if (crmContract.getCustomerId() != null) {
            sql.append(" and  customer_id = ").append(crmContract.getCustomerId());
        }
        if (crmContract.getBusinessId() != null) {
            sql.append(" and  business_id = ").append(crmContract.getBusinessId());
        }
        return CrmContract.dao.find(sql.toString());
    }



    /**
     * 根据合同id查询回款计划
     *
     * @param id
     * @author HJP
     */
    public List<CrmReceivablesPlan> queryReceivablesPlanById(Integer id) {
        return CrmReceivablesPlan.dao.find(Db.getSql("crm.receivablesplan.queryReceivablesPlanById"), id);
    }

    /**
     * 根据客户id变更负责人
     *
     * @author wyq
     */
    public R updateOwnerUserId(CrmCustomer crmCustomer) {
        CrmContract crmContract = new CrmContract();
        crmContract.setNewOwnerUserId(crmCustomer.getNewOwnerUserId());
        crmContract.setTransferType(crmCustomer.getTransferType());
        crmContract.setPower(crmCustomer.getPower());
        String contractIds = Db.queryStr("select GROUP_CONCAT(contract_id) from 72crm_crm_contract where customer_id in (" + crmCustomer.getCustomerIds() + ")");
        if (StrUtil.isEmpty(contractIds)) {
            return R.ok();
        }
        crmContract.setContractIds(contractIds);
        return transfer(crmContract);
    }

    /**
     * @author wyq
     * 根据合同id变更负责人
     */
    public R transfer(CrmContract crmContract) {
        String[] contractIdsArr = crmContract.getContractIds().split(",");
        for (String contractId : contractIdsArr) {
            if (AuthUtil.isRwAuth(Integer.parseInt(contractId), CrmEnum.CRM_CONTRACT)) {
                return R.error("无权限转移");
            }
            String memberId = "," + crmContract.getNewOwnerUserId() + ",";
            Db.update(Db.getSql("crm.contract.deleteMember"), memberId, memberId, Integer.valueOf(contractId));
            CrmContract oldContract = CrmContract.dao.findById(Integer.valueOf(contractId));
            if (oldContract.getCheckStatus() == 8) {
                return R.error("已作废的合同不能转移");
            }
            crmContract.setContractId(Integer.valueOf(contractId));
            crmContract.setOwnerUserId(crmContract.getNewOwnerUserId());
            if (2 == crmContract.getTransferType() && !crmContract.getOwnerUserId().equals(oldContract.getOwnerUserId())) {
                if (1 == crmContract.getPower()) {
                    crmContract.setRoUserId(oldContract.getRoUserId() + oldContract.getOwnerUserId() + ",");
                }
                if (2 == crmContract.getPower()) {
                    crmContract.setRwUserId(oldContract.getRwUserId() + oldContract.getOwnerUserId() + ",");
                }
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_USER,crmContract,oldContract.getOwnerUserId());
            }
            crmContract.update();
            actionRecordUtil.addConversionRecord(Integer.valueOf(contractId), CrmEnum.CRM_CONTRACT, crmContract.getNewOwnerUserId());
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 查询团队成员
     */
    public List<Record> getMembers(Integer contractId) {
        CrmContract crmContract = CrmContract.dao.findById(contractId);
        List<Record> recordList = new ArrayList<>();
        if (null != crmContract.getOwnerUserId()) {
            Record ownerUser = Db.findFirst(Db.getSql("crm.customer.getMembers"), crmContract.getOwnerUserId());
            recordList.add(ownerUser.set("power", "负责人权限").set("groupRole", "负责人"));
        }
        String roUserId = crmContract.getRoUserId();
        String rwUserId = crmContract.getRwUserId();
        String memberIds = roUserId + rwUserId.substring(1);
        if (",".equals(memberIds)) {
            return recordList;
        }
        String[] memberIdsArr = memberIds.substring(1, memberIds.length() - 1).split(",");
        Set<String> memberIdsSet = new HashSet<>(Arrays.asList(memberIdsArr));
        for (String memberId : memberIdsSet) {
            Record record = Db.findFirst(Db.getSql("crm.customer.getMembers"), memberId);
            if (roUserId.contains(memberId)) {
                record.set("power", "只读").set("groupRole", "普通成员");
            }
            if (rwUserId.contains(memberId)) {
                record.set("power", "读写").set("groupRole", "普通成员");
            }
            recordList.add(record);
        }
        return recordList;
    }

    /**
     * @author wyq
     * 添加团队成员
     */
    @Before(Tx.class)
    public R addMember(CrmContract crmContract) {
        List<String> contractIdsArr = StrUtil.splitTrim(crmContract.getIds(),",");
        String[] memberArr = crmContract.getMemberIds().split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (String id : contractIdsArr) {
            CrmContract oldContract = CrmContract.dao.findById(Integer.valueOf(id));
            if (oldContract.getCheckStatus() == 8) {
                return R.error("已作废的合同不能添加团队成员");
            }
            if (AuthUtil.isCrmOperateAuth(CrmEnum.CRM_CONTRACT, Integer.valueOf(id))) {
                return R.error("没有权限");
            }
            Long ownerUserId = oldContract.getOwnerUserId();
            for (String memberId : memberArr) {
                if (ownerUserId.equals(Long.valueOf(memberId))) {
                    return R.error("负责人不能重复选为团队成员");
                }
                Db.update(Db.getSql("crm.contract.deleteMember"), "," + memberId + ",", "," + memberId + ",", Integer.valueOf(id));
            }
            if (1 == crmContract.getPower()) {
                stringBuilder.setLength(0);
                String roUserId = stringBuilder.append(CrmContract.dao.findById(Integer.valueOf(id)).getRoUserId()).append(crmContract.getMemberIds()).append(",").toString();
                Db.update("update 72crm_crm_contract set ro_user_id = ? where contract_id = ?", roUserId, Integer.valueOf(id));
            }
            if (2 == crmContract.getPower()) {
                stringBuilder.setLength(0);
                String rwUserId = stringBuilder.append(CrmContract.dao.findById(Integer.valueOf(id)).getRwUserId()).append(crmContract.getMemberIds()).append(",").toString();
                Db.update("update 72crm_crm_contract set rw_user_id = ? where contract_id = ?", rwUserId, Integer.valueOf(id));
            }
            List<Long> roUserIdList = StrUtil.splitTrim(oldContract.getRoUserId(),",").stream().map(Long::parseLong).collect(Collectors.toList());
            List<Long> rwUserIdList = StrUtil.splitTrim(oldContract.getRwUserId(),",").stream().map(Long::parseLong).collect(Collectors.toList());
            roUserIdList.addAll(rwUserIdList);
            List<Long> memberList = Arrays.stream(memberArr).map(Long::parseLong).collect(Collectors.toList());
            memberList.removeAll(roUserIdList);
            memberList.forEach(memberId -> {
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_USER, oldContract, memberId);
                actionRecordUtil.addMemberActionRecord(CrmEnum.CRM_CONTRACT, Integer.valueOf(id), memberId);
            });
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 删除团队成员
     */
    @Before(Tx.class)
    public R deleteMembers(CrmContract crmContract) {
        String[] contractIdsArr = crmContract.getIds().split(",");
        String[] memberArr = crmContract.getMemberIds().split(",");
        for (String id : contractIdsArr) {
            CrmContract oldContract = CrmContract.dao.findById(Integer.valueOf(id));
            if (oldContract.getCheckStatus() == 8) {
                return R.error("已作废的合同不能删除团队成员");
            }
            if (AuthUtil.isCrmOperateAuth(CrmEnum.CRM_CONTRACT, Integer.valueOf(id))) {
                return R.error("没有权限");
            }
            CrmContract contract = CrmContract.dao.findById(Integer.valueOf(id));
            for (String memberId : memberArr) {
                Db.update(Db.getSql("crm.contract.deleteMember"), "," + memberId + ",", "," + memberId + ",", Integer.valueOf(id));
                if (!Long.valueOf(memberId).equals(BaseUtil.getUserId())){
                    Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_REMOVE_TEAM, contract, Long.parseLong(memberId));
                    actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.CRM_CONTRACT, Integer.valueOf(id), Long.parseLong(memberId), false);
                }else {
                    Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_TEAM_EXIT, contract, contract.getOwnerUserId());
                    actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.CRM_CONTRACT, Integer.valueOf(id), Long.parseLong(memberId), true);
                }
            }
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 自助退出团队成员
     */
    @Before(Tx.class)
    public R exitTeam(Integer contractId) {
        Long userId = BaseUtil.getUserId();
        CrmContract contract = CrmContract.dao.findById(contractId);
        if (contract.getOwnerUserId().equals(userId)){
            return R.error("负责人不能退出团队！");
        }
        Db.update(Db.getSql("crm.contract.deleteMember"), "," + userId + ",", "," + userId + ",", contractId);
        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CONTRACT_TEAM_EXIT, contract, contract.getOwnerUserId());
        return R.ok();
    }

    /**
     * @author wyq
     * 查询编辑字段
     */
    public List<Record> queryField(Integer contractId) {
        Record contract = queryById(contractId);
        List<Record> list = new ArrayList<>();
        list.add(new Record().set("customer_id", contract.getInt("customer_id")).set("customer_name", contract.getStr("customer_name")));
        contract.set("customer_id", list);
        list = new ArrayList<>();
        if (contract.getStr("business_id") != null && contract.getInt("business_id") != 0) {
            list.add(new Record().set("business_id", contract.getInt("business_id")).set("business_name", contract.getStr("business_name")));
        }
        contract.set("business_id", list);
        list = new ArrayList<>();
        if (contract.getStr("contacts_id") != null && contract.getInt("contacts_id") != 0) {
            list.add(new Record().set("contacts_id", contract.getStr("contacts_id")).set("name", contract.getStr("contacts_name")));
        }
        contract.set("contacts_id", list);
        List<Record> fieldList = adminFieldService.queryUpdateField(CrmEnum.CRM_CONTRACT.getType(), contract);
        Kv kv = Kv.by("discount_rate", contract.getBigDecimal("discount_rate"))
                .set("product", Db.find(Db.getSql("crm.contract.queryBusinessProduct"), contractId))
                .set("total_price", contract.getStr("total_price"));
        fieldList.add(new Record().set("field_name", "product").set("name", "产品").set("value", kv).set("form_type", "product").set("setting", new String[]{}).set("is_null", 0).set("field_type", 1));
        return fieldList;
    }


    /**
     * 根据合同ID查询产品
     */
    public R qureyProductListByContractId(BasePageRequest<CrmContractProduct> basePageRequest) {

        Integer pageType = basePageRequest.getPageType();
        Record record = Db.findFirst(Db.getSql("crm.product.querySubtotalByContractId"), basePageRequest.getData().getContractId());
        if (record.getStr("money") == null) {
            record.set("money", 0);
        }
        if (0 == pageType) {
            record.set("list", Db.find(Db.getSql("crm.product.queryProductPageList"), basePageRequest.getData().getContractId()));
            return R.ok().put("data", record);
        } else {
            Page<Record> page = Db.paginateByFullSql(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSql("crm.product.queryProductPagecount"), Db.getSql("crm.product.queryProductPageList"), basePageRequest.getData().getContractId());
            record.set("pageNumber", page.getPageNumber());
            record.set("pageSize", page.getPageSize());
            record.set("totalPage", page.getTotalPage());
            record.set("totalRow", page.getTotalRow());
            record.set("list", page.getList());
            return R.ok().put("data", record);
        }
    }

    /**
     * 根据合同id查询回访
     */
    public R queryReturnVisit(BasePageRequest<CrmContract> basePageRequest) {
        Integer contractId = basePageRequest.getData().getContractId();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_RETURN_VISIT, "a", 1);
        if (0 == basePageRequest.getPageType()) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.contract.queryReturnVisit", Kv.by("contractId", contractId).set("conditions", conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.contract.queryReturnVisit", Kv.by("contractId", contractId).set("conditions", conditions))));
        }
    }

    /**
     * 查询合同到期提醒设置
     */
    public R queryContractConfig() {
        AdminConfig config = AdminConfig.dao.findFirst(Db.getSql("crm.contract.queryContractConfig"));
        if (config == null) {
            config = new AdminConfig();
            config.setStatus(0);
            config.setName("expiringContractDays");
            config.setValue("3");
            config.setDescription("合同到期提醒");
            config.save();
        }
        return R.ok().put("data", config);
    }

    /**
     * 修改合同到期提醒设置
     */
    @Before(Tx.class)
    public R setContractConfig(Integer status, Integer contractDay) {
        if (status == 1 && contractDay == null) {
            return R.error("contractDay不能为空");
        }
        Integer number = Db.update(Db.getSqlPara("crm.contract.setContractConfig", Kv.by("status", status).set("contractDay", contractDay)));
        if (0 == number) {
            AdminConfig adminConfig = new AdminConfig();
            adminConfig.setStatus(0);
            adminConfig.setName("expiringContractDays");
            adminConfig.setValue("3");
            adminConfig.setDescription("合同到期提醒");
            adminConfig.save();
        }
        return R.ok();
    }

    public R contractDiscard(Integer contractId) {
        actionRecordUtil.addObjectActionRecord(CrmEnum.CRM_CONTRACT, contractId, BehaviorEnum.CANCEL_EXAMINE);
        return Db.template("crm.contract.contractDiscard",Kv.by("contractId",contractId)).update()>0?R.ok():R.error();
    }

    /**
     * @author wyq
     * 附件列表
     */
    public R queryFileList(Integer contractId) {
        SqlPara sqlPara=Db.getSqlPara("crm.contract.queryFileList", Kv.by("contractId", contractId));
        return R.ok().put("data", Db.find(sqlPara));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public R num(Integer contractId) {
        String receivableCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_RECEIVABLES, 1);
        String returnVisitCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_RETURN_VISIT, 1);
        String invoiceCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_INVOICE, 1);
        Kv kv = Kv.by("contractId", contractId).set("receivableCon", receivableCon).set("returnVisitCon", returnVisitCon).set("invoiceCon", invoiceCon);
        Record record = Db.findFirst(Db.getSqlPara("crm.contract.queryNum",kv ));
        return R.ok().put("data", record);
    }

    @Override
    public String querySelectSql() {
        return "select *,(select group_concat(realname) from `72crm_admin_user` where find_in_set(user_id,views.company_user_id)) as company_user_name";
    }
}

