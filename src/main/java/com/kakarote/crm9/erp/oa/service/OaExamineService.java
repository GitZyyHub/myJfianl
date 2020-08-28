package com.kakarote.crm9.erp.oa.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
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
import com.kakarote.crm9.erp.admin.entity.AdminExamineLog;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmBusiness;
import com.kakarote.crm9.erp.crm.entity.CrmContacts;
import com.kakarote.crm9.erp.crm.entity.CrmContract;
import com.kakarote.crm9.erp.crm.entity.CrmCustomer;
import com.kakarote.crm9.erp.crm.service.CrmActivityService;
import com.kakarote.crm9.erp.oa.common.OaEnum;
import com.kakarote.crm9.erp.oa.entity.*;
import com.kakarote.crm9.utils.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class OaExamineService{

    @Inject
    private AdminFileService adminFileService;

    @Inject
    private AdminFieldService adminFieldService;


    @Inject
    private CrmActivityService crmActivityService;


    public R myInitiate(BasePageRequest request) {
        JSONObject jsonObject = request.getJsonObject();
        Long userId = BaseUtil.getUser().getUserId();
        if(request.getPageType() == 0){
            List<Record> recordList = Db.find(Db.getSqlPara("oa.examine.myInitiate", Kv.by("userId", userId).set("categoryId", jsonObject.getInteger("categoryId")).set("status", jsonObject.getInteger("checkStatus")).set("startTime", jsonObject.getDate("startTime")).set("endTime", jsonObject.getDate("endTime")).set("isAdmin",BaseUtil.isAdmin())));
            transfer(recordList);
            return R.ok().put("data", recordList);
        }else{
            Page<Record> recordList = Db.paginate(request.getPage(), request.getLimit(), Db.getSqlPara("oa.examine.myInitiate", Kv.by("userId", userId).set("categoryId", jsonObject.getInteger("categoryId")).set("status", jsonObject.getInteger("checkStatus")).set("startTime", jsonObject.getDate("startTime")).set("endTime", jsonObject.getDate("endTime")).set("isAdmin",BaseUtil.isAdmin())));
            transfer(recordList.getList());
            return R.ok().put("data", recordList);
        }
    }

    public R myOaExamine(BasePageRequest request) {
        Long userId = BaseUtil.getUser().getUserId();
        JSONObject jsonObject = request.getJsonObject();
        if(request.getPageType() == 0){
            List<Record> recordList = Db.find(Db.getSqlPara("oa.examine.myOaExamine", Kv.by("userId", userId).set("categoryId", jsonObject.getInteger("categoryId")).set("status", jsonObject.getInteger("status")).set("startTime", jsonObject.getDate("startTime")).set("endTime", jsonObject.getDate("endTime")).set("isAdmin",BaseUtil.isAdmin())));
            transfer(recordList);
            return R.ok().put("data", recordList);
        }else{
            Page<Record> recordList = Db.paginate(request.getPage(), request.getLimit(), Db.getSqlPara("oa.examine.myOaExamine", Kv.by("userId", userId).set("categoryId", jsonObject.getInteger("categoryId")).set("status", jsonObject.getString("status")).set("startTime", jsonObject.getDate("startTime")).set("endTime", jsonObject.getDate("endTime")).set("isAdmin",BaseUtil.isAdmin())));
            transfer(recordList.getList());
            return R.ok().put("data", recordList);
        }
    }

    public void transfer(List<Record> recordList){
        Long userId=BaseUtil.getUserId();
        recordList.forEach(record -> {
            setRelation(record);
            AdminUser user=AdminUser.dao.findByIdLoadColumns(record.getLong("create_user_id"),"user_id,realname,img,parent_id");
            record.set("createUser", user);
            String batchId = record.getStr("batch_id");
            Integer examineStatus = record.getInt("examine_status");
            SqlPara examineSqlPara=Db.getSqlPara("oa.examine.queryExamineUserByExamineLog",record);
            record.set("examineName",Db.queryStr(examineSqlPara.getSql(),examineSqlPara.getPara()));
            adminFileService.queryByBatchId(batchId, record);
            setCountRecord(record);
            List<Integer> roles = BaseUtil.getUser().getRoles();
            Map<String,Integer> permission = new HashMap<>();
            Long createUserId = record.getLong("create_user_id");
            //超级管理员、创建人可以更新 2审核拒绝 4 已撤回 的审批
            boolean isUpdate = (userId.equals(BaseUtil.getSuperUser()) || createUserId.equals(userId) || roles.contains(BaseUtil.getSuperRole())) && (examineStatus == 4 || examineStatus == 2);
            if(isUpdate){
                permission.put("isUpdate", 1);
            }else{
                permission.put("isUpdate", 0);
            }
            //超级管理员可以删除1审核通过 2审核拒绝 4已撤回 的审批，创建人可以删除4 已撤回 的审批
            boolean isSuperDelete = (userId.equals(BaseUtil.getSuperUser()) || roles.contains(BaseUtil.getSuperRole())) && (examineStatus == 1 || examineStatus == 2 || examineStatus == 4);
            boolean isCreateDelete = createUserId.equals(userId) && (examineStatus == 4);
            if(isSuperDelete || isCreateDelete){
                permission.put("isDelete", 1);
            }else{
                permission.put("isDelete", 0);
            }
            //超级管理员、创建人可以撤回 0未审核 4审核中 的审批
            boolean isRecheck = (userId.equals(BaseUtil.getSuperUser()) || createUserId.equals(userId) || roles.contains(BaseUtil.getSuperRole())) && (examineStatus == 0 || examineStatus == 3);
            if(isRecheck){
                permission.put("isRecheck", 1);
            }else{
                permission.put("isRecheck", 0);
            }
            SqlPara sqlPara = Db.getSqlPara("oa.examine.queryExamineLog", Kv.by("recordId", record.getInt("examine_record_id")).set("examineUser", userId).set("stepId", record.getInt("examine_step_id")));
            Record oaExamineLog = Db.findFirst(sqlPara);
            if(oaExamineLog != null){
                permission.put("isCheck", 1);
            }else{
                permission.put("isCheck", 0);
            }
            record.set("permission", permission);
        });
    }

    private void setCountRecord(Record record){
        Integer examineId = record.getInt("examine_id");
        String categoryTitle = record.getStr("categoryTitle");
        Integer count = Db.queryInt("select count(*) as count from 72crm_oa_examine_travel where examine_id = ?", examineId);
        StringBuilder causeTitle = new StringBuilder();
        if(count > 0){
            causeTitle.append(count);
            switch(categoryTitle){
                case "出差审批":
                    causeTitle.append("个行程，共");
                    if(record.get("duration") != null){
                        causeTitle.append(record.getBigDecimal("duration"));
                    }else{
                        causeTitle.append(0);
                    }
                    causeTitle.append("天。");
                    break;
                case "差旅报销":
                    causeTitle.append("个报销事项，共");
                    if(record.get("money") != null){
                        causeTitle.append(record.getInt("money"));
                    }else{
                        causeTitle.append(0);
                    }
                    causeTitle.append("元。");
                    break;
                default:
                    break;
            }
        }
        record.set("causeTitle", causeTitle.toString());
    }

    @Before(Tx.class)
    public R setOaExamine(JSONObject jsonObject){
        AdminUser user = BaseUtil.getUser();
        OaExamine oaExamine = jsonObject.getObject("oaExamine", OaExamine.class);
        JSONArray oaExamineTravelList = jsonObject.getJSONArray("oaExamineTravelList");
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.EXAMINE_TYPE_KEY, oaExamine.getExamineId());
        if(oaAuth){
            return R.noAuth();
        }
        if(oaExamine.getStartTime() != null && oaExamine.getEndTime() != null){
            if((oaExamine.getStartTime().compareTo(oaExamine.getEndTime())) >= 0){
                return R.error("审批结束时间早于开始时间");
            }
        }
        if(oaExamineTravelList != null){
            for(Object json : oaExamineTravelList){
                OaExamineTravel oaExamineTravel = TypeUtils.castToJavaBean(json, OaExamineTravel.class);
                if (oaExamineTravel.getStartTime() != null && oaExamineTravel.getEndTime() != null){
                    if((oaExamineTravel.getStartTime().compareTo(oaExamineTravel.getEndTime())) >= 0){
                        return R.error("差旅结束时间早于开始时间");
                    }
                }
            }
        }
        boolean bol;
        String batchId = StrUtil.isNotEmpty(oaExamine.getBatchId()) ? oaExamine.getBatchId() : IdUtil.simpleUUID();
        save(jsonObject.getJSONArray("field"), batchId);
        oaExamine.setBatchId(batchId);
        String checkUserIds = jsonObject.getString("checkUserId");
        Integer categoryId = oaExamine.getCategoryId();
        OaExamineCategory oaExamineCategory = OaExamineCategory.dao.findById(categoryId);
        if (oaExamineCategory.getStatus().equals(0) || oaExamineCategory.getIsDeleted().equals(1)) {
            return R.error("该审批流已被停用或删除");
        }
        OaExamineStep oaExamineStep = new OaExamineStep();
        Integer examineType = oaExamineCategory.getExamineType();
        if(oaExamineCategory.getExamineType() == 1){
            oaExamineStep = OaExamineStep.dao.findFirst("SELECT * FROM 72crm_oa_examine_step WHERE category_id = ? ORDER BY step_num LIMIT 0,1", categoryId);
        }
        Integer recordId = null;
        //创建审批记录
        if (oaExamine.getDuration() != null && oaExamine.getDuration().compareTo(new BigDecimal(1000))>=0){
            return R.error("时长必须是三位数");
        }
        if(oaExamine.getExamineId() == null){
            oaExamine.setCreateUserId(user.getUserId());
            oaExamine.setCreateTime(new Date());
            bol = oaExamine.save();
            if (bol) {
                Aop.get(ActionRecordUtil.class).addOaExamineActionRecord(CrmEnum.OA_EXAMINE, oaExamine.getExamineId(), BehaviorEnum.SAVE);
            }
        }else{
            oaExamine.setUpdateTime(new Date());
            bol = oaExamine.update();
            if (bol) {
                Aop.get(ActionRecordUtil.class).addOaExamineActionRecord(CrmEnum.OA_EXAMINE, oaExamine.getExamineId(), BehaviorEnum.UPDATE);
            }
            Db.delete("delete from 72crm_oa_examine_travel where examine_id = ?", oaExamine.getExamineId());
            Db.delete("delete from 72crm_oa_examine_relation where examine_id = ?", oaExamine.getExamineId());
            recordId = Db.queryInt("select  record_id from 72crm_oa_examine_record where examine_id = ? limit 1", oaExamine.getExamineId());
        }
        oaExamine = new OaExamine().findById(oaExamine.getExamineId());
        OaExamineRecord oaExamineRecord = new OaExamineRecord();
        oaExamineRecord.setExamineId(oaExamine.getExamineId());
        oaExamineRecord.setExamineStepId(oaExamineStep.getStepId());
        oaExamineRecord.setExamineStatus(3);
        //生成审批记录
        if(recordId == null){
            oaExamineRecord.setCreateUser(user.getUserId());
            oaExamineRecord.setCreateTime(new Date());
            oaExamineRecord.save();
        }else{
            oaExamineRecord.setRecordId(recordId);
            oaExamineRecord.update();
            //更新审核日志状态
            Db.update("update 72crm_oa_examine_log set is_recheck = 1 where record_id = ?", recordId);
        }

        //生成审批日志
        if(examineType == 1){
            Integer stepType = oaExamineStep.getStepType();
            if(stepType == 1){
                Long userid=Db.queryLong("select parent_id from 72crm_admin_user where user_id = ?", BaseUtil.getUser().getUserId());
                checkUserIds =Objects.isNull(userid)?null:userid.toString();
            }else if(stepType == 4){
                Long userid=Db.queryLong("select parent_id from 72crm_admin_user where user_id = (select parent_id from 72crm_admin_user where user_id = ?)", BaseUtil.getUser().getUserId());
                checkUserIds =Objects.isNull(userid)?null:userid.toString();
            }else{
                checkUserIds = oaExamineStep.getCheckUserId();
            }
        }
        //找不到人就让超级管理员审核
        if("0".equals(checkUserIds) ||StrUtil.isEmpty(checkUserIds)){
            checkUserIds = BaseUtil.getSuperUser() + "";
        }
        if(StrUtil.isEmpty(checkUserIds)){
            //没有审核人，审批结束
            oaExamineRecord.setExamineStatus(1);
            oaExamineRecord.update();
        }else{
            //如果是重新提交，将之前的审核日志全部变为撤回状态,此处无需判断，因为第一次提交的话还没有审核日志，不会影响
            Db.update("update 72crm_oa_examine_log set is_recheck = 1 where record_id = ?", oaExamineRecord.getRecordId());
            //添加审核日志
            for(Long userId : TagUtil.toLongSet(checkUserIds)){
                OaExamineLog oaExamineLog = new OaExamineLog();
                oaExamineLog.setRecordId(oaExamineRecord.getRecordId());
                oaExamineLog.setOrderId(1);
                if(oaExamineStep.getStepId() != null){
                    oaExamineLog.setExamineStepId(oaExamineStep.getStepId());
                }
                oaExamineLog.setExamineStatus(0);
                oaExamineLog.setCreateUser(user.getUserId());
                oaExamineLog.setCreateTime(new Date());
                oaExamineLog.setExamineUser(userId);
                oaExamineLog.save();
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_EXAMINE_NOTICE,oaExamineLog,BaseUtil.getUserId());
            }
        }
        if(jsonObject.get("oaExamineRelation") != null){
            OaExamineRelation oaExamineRelation = jsonObject.getObject("oaExamineRelation", OaExamineRelation.class);
            oaExamineRelation.setRId(null);
            oaExamineRelation.setBusinessIds(TagUtil.fromString(oaExamineRelation.getBusinessIds()));
            oaExamineRelation.setContactsIds(TagUtil.fromString(oaExamineRelation.getContactsIds()));
            oaExamineRelation.setContractIds(TagUtil.fromString(oaExamineRelation.getContractIds()));
            oaExamineRelation.setCustomerIds(TagUtil.fromString(oaExamineRelation.getCustomerIds()));
            oaExamineRelation.setExamineId(oaExamine.getExamineId());
            oaExamineRelation.setCreateTime(new Date());
            oaExamineRelation.save();
            crmActivityService.addActivity(2, CrmActivityEnum.EXAMINE,oaExamine.getExamineId(),"");
        }
        if(oaExamineTravelList != null){
            for(Object json : oaExamineTravelList){
                OaExamineTravel oaExamineTravel = TypeUtils.castToJavaBean(json, OaExamineTravel.class);
                oaExamineTravel.setTravelId(null);
                oaExamineTravel.setExamineId(oaExamine.getExamineId());
                oaExamineTravel.save();
            }
        }
        return bol ? R.ok() : R.error();
    }

    /**
     * 保存自定义字段
     *
     * @param array   参数对象
     * @param batchId 批次ID
     * @return 操作结果
     */
    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_oa_examine_data", "batch_id", batchId);
        List<OaExamineData> Fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            OaExamineData fieldv = TypeUtils.castToJavaBean(obj, OaExamineData.class);
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            Fieldvs.add(fieldv);
        });
        Db.batchSave(Fieldvs, 100);
        return true;
    }


    public R oaExamine(OaExamineLog nowadayExamineLog, Long nextUserId){
        //当前审批人
        Long auditUserId = BaseUtil.getUser().getUserId();
        String checkUserIds = "";
        if(nextUserId != null){
            checkUserIds = nextUserId + "";
        }
        Integer recordId = nowadayExamineLog.getRecordId();
        Integer status = nowadayExamineLog.getExamineStatus();
        //根据审核记录id查询审核记录
        OaExamineRecord examineRecord = OaExamineRecord.dao.findById(recordId);
        if(status == 4){
            if(! examineRecord.getCreateUser().equals(auditUserId) && ! auditUserId.equals(BaseUtil.getSuperUser())){
                return R.error("当前用户没有审批权限！");
            }
        }else{
            SqlPara sqlPara = Db.getSqlPara("oa.examine.queryExamineLog", Kv.by("recordId", recordId).set("examineUser", auditUserId).set("stepId", examineRecord.get("examine_step_id")));
            Record oaExamineLog = Db.findFirst(sqlPara);
            //【判断当前审批人是否有审批权限
            if(oaExamineLog == null){
                return R.error("当前用户没有审批权限或审批已撤回！");
            }
        }
        examineRecord.setExamineStatus(status);
        Integer examineId = examineRecord.getExamineId();
        //查询审批流程
        OaExamine examine = OaExamine.dao.findById(examineId);
        //查询审批类型
        OaExamineCategory examineCategory = OaExamineCategory.dao.findById(examine.getCategoryId());
        //查询当前审批步骤
        OaExamineStep examineStep = OaExamineStep.dao.findById(examineRecord.getExamineStepId());
        //查询当前审核日志
        Long createUserId = examine.getCreateUserId();
        Record log;
        if(examineCategory.getExamineType() == 1){
            log = Db.findFirst("select log_id,order_id from 72crm_oa_examine_log where record_id = ? and examine_step_id = ? and examine_user = ? and is_recheck = 0", examineRecord.getRecordId(), examineRecord.getExamineStepId(), auditUserId);
        }else{
            log = Db.findFirst("select log_id,order_id from 72crm_oa_examine_log where record_id = ? and examine_status = 0 and examine_user = ? and is_recheck = 0", examineRecord.getRecordId(), auditUserId);
        }
        nowadayExamineLog.setExamineUser(auditUserId);
        if(log != null){
            nowadayExamineLog.setLogId(log.getLong("log_id"));
            nowadayExamineLog.setOrderId(log.getInt("order_id"));
        }

        //审核日志 添加审核人
        nowadayExamineLog.setExamineTime(DateUtil.date());
        if(status != 4){
            nowadayExamineLog.update();
        }
        if(status == 2){
            //判断审核拒绝
            if(examineStep != null && examineStep.getStepType() == 2){
//                examineRecord.setExamineStatus(3);
                Record record = Db.findFirst(Db.getSqlPara("oa.examine.queryCountByStepId", Kv.by("recordId", recordId).set("stepId", examineStep.getStepId())));
                if(record.getInt("toCount") == 0){
                    examineRecord.setExamineStatus(status);
                }
            }
        }else if(status == 4){
            examineRecord.setExamineStatus(4);
            //先查询该审批流程的审批步骤的第一步
            OaExamineStep oneExamineStep = OaExamineStep.dao.findFirst("SELECT * FROM 72crm_oa_examine_step WHERE category_id = ? ORDER BY step_num LIMIT 0,1", examine.getCategoryId());
            //判断审核撤回
            OaExamineLog examineLog = new OaExamineLog();
            examineLog.setExamineUser(auditUserId);
            examineLog.setCreateTime(DateUtil.date());
            examineLog.setCreateUser(auditUserId);
            examineLog.setExamineStatus(status);
            examineLog.setIsRecheck(1);
            examineLog.setExamineTime(new Date());
            if(examineCategory.getExamineType() == 1){
                examineRecord.setExamineStepId(oneExamineStep.getStepId());
                examineLog.setExamineStepId(examineStep.getStepId());
                examineLog.setOrderId(examineStep.getStepNum());
            }else{
                Integer orderId = Db.queryInt("select order_id from 72crm_oa_examine_log where record_id = ? and is_recheck = 0 and examine_status !=0 order by order_id desc limit 1 ", recordId);
                if(orderId == null){
                    orderId = 1;
                }
                examineLog.setOrderId(orderId);
            }
            examineLog.setRecordId(examineRecord.getRecordId());
            examineLog.setRemarks(nowadayExamineLog.getRemarks());
            examineLog.save();
            Aop.get(ActionRecordUtil.class).addOaExamineActionRecord(CrmEnum.OA_EXAMINE, examineId, BehaviorEnum.RECHECK_EXAMINE);
            //更新审核日志状态
            Db.update("update 72crm_oa_examine_log set is_recheck = 1 where record_id = ?", recordId);
        }else{
            //审核通过
            //判断该审批流程类型
            OaExamineStep nextExamineStep = null;
            boolean flag = true;
            if(examineCategory.getExamineType() == 1){
                //固定审批
                //查询下一个审批步骤
                nextExamineStep =
                        OaExamineStep.dao.findFirst(Db.getSql("oa.examine.queryExamineStepByNextExamineIdOrderByStepId"), examineCategory.getCategoryId(), examineRecord.getExamineStepId());


                //判断是否是并签
                if(examineStep.getStepType() == 3){
                    //查询当前并签是否都完成
                    //根据审核记录ID，审核步骤ID，查询审核日志
                    // List<AdminExamineLog> examineLogs = AdminExamineLog.dao.find(Db.getSql("admin.examineLog.queryNowadayExamineLogByRecordIdAndStepId"),examineRecord.getRecordId(),examineRecord.getExamineStepId());
                    //当前并签人员
                    for(Long userId : TagUtil.toLongSet(examineStep.getCheckUserId())){
                        AdminExamineLog examineLog = AdminExamineLog.dao.findFirst(Db.getSql("oa.examine.queryNowadayExamineLogByRecordIdAndStepId"), examineRecord.getRecordId(), examineRecord.getExamineStepId(), userId);
                        if(examineLog.getExamineStatus() == 0){
                            //并签未走完
                            flag = false;
                            break;
                        }
                    }
                    //并签未完成
                    if(! flag){
                        examineRecord.setExamineStatus(3);
                    }
                }
                if(flag){
                    //判断是否有下一步流程
                    if(nextExamineStep != null){
                        //有下一步流程
                        examineRecord.setExamineStatus(3);
                        examineRecord.setExamineStepId(nextExamineStep.getStepId());
                        Integer stepType = nextExamineStep.getStepType();
                        //生成审批日志
                        if(stepType == 1){
                            checkUserIds = Db.queryInt("select parent_id from 72crm_admin_user where user_id = ?", createUserId) + "";
                        }else if(stepType == 4){
                            AdminUser adminUser = AdminUser.dao.findById(nowadayExamineLog.getExamineUser());
                            if (adminUser != null && adminUser.getParentId() != null) {
                                checkUserIds = adminUser.getParentId()+"";
                            }else {
                                checkUserIds = BaseUtil.getSuperUser()+"";
                            }
                        }else{
                            checkUserIds = nextExamineStep.getCheckUserId();
                        }
                    }
                    if("0".equals(checkUserIds) && ! createUserId.equals(BaseUtil.getSuperUser())){
                        checkUserIds = BaseUtil.getSuperUser() + "";
                    }
                }
            }
            if(((examineCategory.getExamineType() == 2) || (examineCategory.getExamineType() == 1 && flag)) && StrUtil.isEmpty(checkUserIds)){
                //没有上级，审核通过
                examineRecord.setExamineStatus(1);
            }else{
                examineRecord.setExamineStatus(3);
                //添加审核日志
                for(Long userId : TagUtil.toLongSet(checkUserIds)){
                    OaExamineLog oaExamineLog = new OaExamineLog();
                    oaExamineLog.setRecordId(examineRecord.getRecordId());
                    oaExamineLog.setOrderId(nowadayExamineLog.getOrderId() + 1);
                    if(nextExamineStep != null){
                        oaExamineLog.setOrderId(nextExamineStep.getStepNum());
                        oaExamineLog.setExamineStepId(nextExamineStep.getStepId());
                    }
                    oaExamineLog.setExamineStatus(0);
                    oaExamineLog.setCreateUser(BaseUtil.getUser().getUserId());
                    oaExamineLog.setCreateTime(new Date());
                    oaExamineLog.setExamineUser(userId);
                    oaExamineLog.save();
                    Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_EXAMINE_NOTICE,oaExamineLog,examine.getCreateUserId());
                }
            }

        }
        examineRecord.setRemarks(nowadayExamineLog.getRemarks());
        if(examineRecord.getExamineStatus().equals(1)){
            Aop.get(ActionRecordUtil.class).addOaExamineActionRecord(CrmEnum.OA_EXAMINE, examineId, BehaviorEnum.PASS_EXAMINE);
            Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_EXAMINE_PASS,examineRecord,auditUserId);
        }else if(examineRecord.getExamineStatus().equals(2)){
            Aop.get(ActionRecordUtil.class).addOaExamineActionRecord(CrmEnum.OA_EXAMINE, examineId, BehaviorEnum.REJECT_EXAMINE);
            Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_EXAMINE_REJECT,examineRecord,auditUserId);
        }
        examineRecord.setRemarks(null);
        return examineRecord.update() ? R.ok() : R.error();
    }

    public R queryOaExamineInfo(String id){
        Record oaExamineInfo = Db.findFirst(Db.getSql("oa.examine.queryExamineById"), id);
        oaExamineInfo.set("createUser", Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", oaExamineInfo.getLong("create_user_id")));
        String batchId = oaExamineInfo.getStr("batch_id");
        setRelation(oaExamineInfo);
        Record first = Db.findFirst("select * from 72crm_oa_examine_record where examine_id = ?", id);
        oaExamineInfo.set("record", first);
        oaExamineInfo.set("examine_record_id", first.get("record_id"));
        adminFileService.queryByBatchId(batchId, oaExamineInfo);
        List<Record> examineTravelList = Db.find("select * from 72crm_oa_examine_travel where examine_id = ?", id);
        examineTravelList.forEach(record -> adminFileService.queryByBatchId(record.getStr("batch_id"), record));
        oaExamineInfo.set("examineTravelList", examineTravelList);
        return R.ok().put("data", oaExamineInfo);
    }

    public R getField(String id, Integer isDetail){
        Record oaExamineInfo = Db.findFirst("select * from 72crm_oa_examine where examine_id = ?", id);
        String categoryId = oaExamineInfo.getStr("category_id");
        OaExamineCategory oaExamineCategory = new OaExamineCategory().findById(categoryId);
        List<Record> examineTravelList = Db.find(Db.getSql("oa.examine.queryTravel"), oaExamineInfo.getInt("examine_id"));
        examineTravelList.forEach(record -> adminFileService.queryByBatchId(record.getStr("batchId"), record));
        List<Record> recordList = new ArrayList<>();
        FieldUtil fieldUtil = new FieldUtil(recordList);
        String[] arr = new String[0];
        switch(oaExamineCategory.getType()){
            case 1:
                fieldUtil.oaFieldAdd("content", "审批内容", "text", arr, 1, 0, oaExamineInfo.get("content"), "", 3, 1)
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.get("remark"), "", 3, 1);
                break;
            case 2:
                fieldUtil.oaFieldAdd("type_id", "请假类型", "select", new String[]{"年假", "事假", "病假", "产假", "调休", "婚假", "丧假", "其他"}, 1, 0, oaExamineInfo.get("type_id"), "", 3, 1)
                        .oaFieldAdd("content", "审批内容", "text", arr, 1, 0, oaExamineInfo.get("content"), "", 3, 1)
                        .oaFieldAdd("start_time", "开始时间", "datetime", arr, 1, 0, oaExamineInfo.get("start_time"), "", 3, 1)
                        .oaFieldAdd("end_time", "结束时间", "datetime", arr, 1, 0, oaExamineInfo.get("end_time"), "", 3, 1)
                        .oaFieldAdd("duration", "时长(天)", "floatnumber", arr, 1, 0, String.valueOf(oaExamineInfo.getBigDecimal("duration")), "", 3, 1)
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.get("remark"), "", 3, 1);
                break;
            case 3:
                fieldUtil.oaFieldAdd("content", "出差事由", "text", arr, 1, 0, oaExamineInfo.get("content"), "", 3, 1)
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.get("remark"), "", 3, 1)
                        .oaFieldAdd("cause", "行程明细", "business_cause", arr, 1, 0, examineTravelList, "", 3, 1)
                        .oaFieldAdd("duration", "时长(天)", "floatnumber", arr, 1, 0, String.valueOf(oaExamineInfo.getBigDecimal("duration")), "", 3, 1);
                break;
            case 4:
                fieldUtil.oaFieldAdd("content", "加班原因", "text", arr, 1, 0, oaExamineInfo.get("content"), "", 3, 1)
                        .oaFieldAdd("start_time", "开始时间", "datetime", arr, 1, 0, oaExamineInfo.get("start_time"), "", 3, 1)
                        .oaFieldAdd("end_time", "结束时间", "datetime", arr, 1, 0, oaExamineInfo.get("end_time"), "", 3, 1)
                        .oaFieldAdd("duration", "加班总天数", "floatnumber", arr, 1, 0, oaExamineInfo.get("duration"), "", 3, 1)
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.get("remark"), "", 3, 1);
                break;
            case 5:
                fieldUtil.oaFieldAdd("content", "差旅事由", "text", arr, 1, 0, oaExamineInfo.get("content"), "", 3, 1)
                        .oaFieldAdd("cause", "费用明细", "examine_cause", arr, 1, 0, examineTravelList, "", 3, 1)
                        .oaFieldAdd("money", "报销总金额", "floatnumber", arr, 1, 0, String.valueOf(oaExamineInfo.getInt("money")), "", 3, 1)
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.get("remark"), "", 3, 1);
                break;
            case 6:
                fieldUtil.oaFieldAdd("content", "借款事由", "text", arr, 1, 0, oaExamineInfo.get("content"), "", 3, 1)
                        .oaFieldAdd("money", "借款金额（元）", "floatnumber", arr, 1, 0, String.valueOf(oaExamineInfo.getInt("money")), "", 3, 1)
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.get("remark"), "", 3, 1);
                break;
            default:
                fieldUtil.oaFieldAdd("content", "审批事由", "textarea", arr, 1, 0, oaExamineInfo.get("content"), "", 1, 1)
                        .oaFieldAdd("remark", "备注", "textarea", arr, 1, 0, oaExamineInfo.get("remark"), "", 1, 1);
                List<Record> recordList1 = Db.find(Db.getSql("oa.examine.queryFieldByBatchId"), categoryId, oaExamineInfo.getStr("batch_id"));
                adminFieldService.recordToFormType(recordList1);
                adminFieldService.transferFieldList(recordList1, isDetail);
                recordList.addAll(recordList1);
                break;
        }
        return R.ok().put("data", fieldUtil.getRecordList());
    }


    private void setRelation(Record relationRecord){
        List<Record> recordList = Db.find("select * from 72crm_oa_examine_relation where examine_id = ?", relationRecord.getInt("examine_id"));
        for(Record record : recordList){
            List<CrmCustomer> customerList = new ArrayList<>();
            if(record.getStr("customer_ids") != null && ! record.getStr("customer_ids").isEmpty()){
                for(Integer customerId : TagUtil.toSet(record.getStr("customer_ids"))){
                    CrmCustomer crmCustomer = CrmCustomer.dao.findById(customerId);
                    if(crmCustomer != null){
                        customerList.add(crmCustomer);
                    }
                }
            }
            relationRecord.set("customerList", customerList);
            List<CrmContacts> contactsList = new ArrayList<>();
            if(record.getStr("contacts_ids") != null && ! record.getStr("contacts_ids").isEmpty()){
                for(Integer contactsId : TagUtil.toSet(record.getStr("contacts_ids"))){
                    CrmContacts crmContacts = CrmContacts.dao.findById(contactsId);
                    if(crmContacts != null){
                        contactsList.add(crmContacts);
                    }
                }
            }
            relationRecord.set("contactsList", contactsList);
            List<CrmBusiness> businessList = new ArrayList<>();
            if(record.getStr("business_ids") != null && ! record.getStr("business_ids").isEmpty()){
                for(Integer businessId : TagUtil.toSet(record.getStr("business_ids"))){
                    CrmBusiness crmBusiness = CrmBusiness.dao.findById(businessId);
                    if(crmBusiness != null){
                        businessList.add(crmBusiness);
                    }
                }
            }
            relationRecord.set("businessList", businessList);
            List<CrmContract> contractList = new ArrayList<>();
            if(record.getStr("contract_ids") != null && ! record.getStr("contract_ids").isEmpty()){
                for(Integer contractId : TagUtil.toSet(record.getStr("contract_ids"))){
                    CrmContract crmContract = CrmContract.dao.findById(contractId);
                    if(crmContract != null){
                        contractList.add(crmContract);
                    }
                }
            }
            relationRecord.set("contractList", contractList);
        }

    }

    @Before(Tx.class)
    public R deleteOaExamine(Integer oaExamineId){
        Integer recordId = Db.queryInt("select record_id from 72crm_oa_examine_record where examine_id  = ? limit 1", oaExamineId);
        OaExamineRecord oaExamineRecord = new OaExamineRecord().findById(recordId);
        boolean bol = oaExamineRecord.getExamineStatus() != 4  && (!BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) || !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()));
        if (bol){
            return R.noAuth();
        }
        Aop.get(ActionRecordUtil.class).addOaExamineActionRecord(CrmEnum.OA_EXAMINE, oaExamineId, BehaviorEnum.DELETE);
        Db.delete("delete from `72crm_oa_examine_data` where batch_id = (select `72crm_oa_examine`.batch_id from `72crm_oa_examine` where examine_id = ?)", oaExamineId);
        Db.delete("delete from 72crm_oa_examine where examine_id = ?", oaExamineId);
        Db.delete("delete from 72crm_oa_examine_relation where examine_id = ?", oaExamineId);
        Db.delete("delete from 72crm_oa_examine_travel where examine_id = ?", oaExamineId);
        Db.delete("delete from 72crm_oa_examine_record where record_id = ?", recordId);
        Db.delete("delete from 72crm_oa_examine_log where record_id = ?", recordId);
        return R.ok();
    }

    public R queryExaminStep(String categoryId){
        Record record = Db.findFirst("select examine_type from 72crm_oa_examine_category where category_id = ?", categoryId);
        Integer examineType = record.getInt("examine_type");
        if(examineType == 1){
            List<Record> recordList = Db.find("select * from 72crm_oa_examine_step where category_id = ?", categoryId);
            recordList.forEach(step -> {
                List<Record> userList = Db.find(Db.getSqlPara("admin.user.queryByIds", Kv.by("ids", step.getStr("check_user_id").split(","))));
                step.set("userList", userList);
            });
            record.set("examineSteps", recordList);
        }
        return R.ok().put("data", record);
    }

    public R queryExamineLogList(Integer recordId){
        Integer examineType = Db.queryInt(Db.getSql("oa.examine.queryExamineTypeByRecordId"), recordId);
        List<Record> logs = null;
        if(examineType == 1){
            logs = Db.find(Db.getSql("oa.examine.queryExamineLogByRecordIdByStep"), recordId);
        }else{
            logs = Db.find(Db.getSql("oa.examine.queryExamineLogByRecordIdByStep1"), recordId);
        }
        return R.ok().put("data", logs);
    }


    public R queryExamineRecordList(Integer recordId){
        JSONObject jsonObject = new JSONObject();
        Record examineRecord = Db.findFirst(Db.getSql("oa.examine.queryExamineRecordById"), recordId);
        Integer examineStatus = examineRecord.getInt("examine_status");
        //如果当前审批已撤回
        if(examineRecord.getInt("examine_status") == 4){
            jsonObject.put("examineType", 1);
            Record user = Db.findFirst(Db.getSql("oa.examine.queryUserByRecordId"), recordId);
            examineRecord.set("userList", user);
            List<Record> records = new ArrayList<>();
            records.add(examineRecord);
            jsonObject.put("steps", records);
            return R.ok().put("data", jsonObject);
        }
        OaExamine oaExamine = OaExamine.dao.findById(examineRecord.getInt("examine_id"));
        OaExamineCategory oaExamineCategory = OaExamineCategory.dao.findById(oaExamine.getCategoryId());
        List<Record> list = new ArrayList<>();
        Record rec = Db.findFirst(Db.getSql("oa.examine.queryRecordAndId"), recordId);
        Long auditUserId = BaseUtil.getUser().getUserId();
        //jsonObject.put("isRecheck",0);
        //判断是否有撤回权限

        if((auditUserId.equals(examineRecord.getLong("create_user")) || auditUserId.equals(BaseUtil.getSuperUser())) && (examineStatus == 0 ||examineStatus == 3)){
            jsonObject.put("isRecheck", 1);
        }else{
            jsonObject.put("isRecheck", 0);
        }
        if(oaExamineCategory.getExamineType() == 2){
            Record log = Db.findFirst(Db.getSqlPara("oa.examine.queryRecordByUserIdAndStatus", Kv.by("create_user", rec.getLong("create_user")).set("examineTime", rec.getDate("examineTime"))));
            rec.set("examinUser", log);
            list.add(rec);
            //授权审批
            List<Record> logs = Db.find(Db.getSql("oa.examine.queryExamineLogAndUserByRecordId"), recordId);
            logs.forEach(r -> {
                r.set("examinUser", Db.findFirst(Db.getSql("oa.examine.queryExamineLogAndUserByLogId"), r.getInt("log_id")));
            });
            list.addAll(logs);
            SqlPara sqlPara = Db.getSqlPara("oa.examine.queryExamineLog", Kv.by("recordId", recordId).set("examineUser", auditUserId).set("stepId", examineRecord.get("examine_step_id")));
            Record oaExamineLog = Db.findFirst(sqlPara);
            if(oaExamineLog != null){
                jsonObject.put("isCheck", 1);
            }else{
                jsonObject.put("isCheck", 0);
            }
            jsonObject.put("examineType", 2);
            jsonObject.put("steps", list);
        }else{
            jsonObject.put("examineType", 1);
            //固定审批
            List<Record> steps = Db.find("select * from 72crm_oa_examine_step where  category_id = ? ORDER BY step_num", oaExamineCategory.getCategoryId());
            steps.forEach(step -> {
                if(step.getInt("step_type") == 1){
                    //负责人主管
                    List<Record> logs = Db.find(Db.getSql("oa.examine.queryUserByRecordIdAndStepIdAndStatus"), recordId, step.getInt("step_id"));
                    //已经创建审核日志
                    if(logs != null && logs.size() > 0){
                        for(Record record : logs){
                            step.set("examine_status", record.getInt("examine_status"));
                        }
                        step.set("userList", logs);
                    }else{
                        step.set("examine_status", 0);
                        //还未创建审核日志
                        //查询负责人主管
                        List<Record> r = Db.find(Db.getSql("oa.examine.queryUserByUserId"), oaExamine.getCreateUserId());
                        if(r == null || r.size() == 0){
                            r = Db.find(Db.getSql("oa.examine.queryUserByUserIdAnd"), BaseUtil.getSuperUser());
                        }
                        step.set("userList", r);
                    }
                }else if(step.getInt("step_type") == 2 || step.getInt("step_type") == 3){
                    //先判断是否已经审核过
                    List<Record> logs = Db.find(Db.getSql("oa.examine.queryUserByRecordIdAndStepIdAndStatus"), recordId, step.getInt("step_id"));
                    if(logs != null && logs.size() != 0){
                        //已经创建审核日志
                        int status = 0;
                        if(step.getInt("step_type") == 2){
                            Optional<Record> optional = logs.stream().filter(log -> log.getDate("examineTime") != null).sorted(Comparator.comparingLong(log -> log.getDate("examineTime").getTime())).findFirst();
                            if (optional.isPresent()){
                                status = optional.get().getInt("examine_status");
                            }
                        }
                        if(step.getInt("step_type") == 3){
                            int i = 0;
                            for(Record record : logs){
                                if(record.getInt("examine_status") == 2){
                                    status = 2;
                                }
                                if(record.getInt("examine_status") == 1){
                                    i++;
                                }
                            }
                            if(i == logs.size()){
                                status = 1;
                            }
                        }
                        step.set("examine_status", status);
                        step.set("userList", logs);
                    }else{
                        //该步骤还未审核
                        logs = new ArrayList<>();
                        for(Integer userId : TagUtil.toSet(step.getStr("check_user_id"))){
                            logs.add(Db.findFirst(Db.getSql("oa.examine.queryUserByUserIdAndStatus"), userId));
                        }
                        step.set("examine_status", 0);
                        step.set("userList", logs);
                    }
                }else{
                    //主管的主管
                    List<Record> logs = Db.find(Db.getSql("oa.examine.queryUserByRecordIdAndStepIdAndStatus"), recordId, step.getInt("step_id"));
                    //已经创建审核日志
                    if(logs != null && logs.size() != 0){
                        for(Record record : logs){
                            step.set("examine_status", record.getInt("examine_status"));
                        }
                        step.set("userList", logs);
                    }else{
                        step.set("examine_status", 0);
                        //还未创建审核日志
                        //查询负责人主管的主管
                        OaExamineStep examineStep =  OaExamineStep.dao.findById(step.get("step_id"));
                        Long userId = queryStep(examineStep,recordId,oaExamine.getCreateUserId());
                        //查询负责人主管的主管
                        List<Record> r = Db.find(Db.getSql("oa.examine.queryUserByUserIdAnd"),userId);
                        step.set("userList", r);
                    }
                }
            });
            SqlPara sqlPara = Db.getSqlPara("oa.examine.queryExamineLog", Kv.by("recordId", recordId).set("examineUser", auditUserId).set("stepId", examineRecord.get("examine_step_id")));
            Record oaExamineLog = Db.findFirst(sqlPara);
            if(oaExamineLog != null  && (examineRecord.getInt("examine_status") == 3 || examineRecord.getInt("examine_status") == 0)){
                jsonObject.put("isCheck", 1);
            }else{
                jsonObject.put("isCheck", 0);
            }
            List<Record> logs = Db.find(Db.getSqlPara("oa.examine.queryRecordByUserIdAndStatus", Kv.by("create_user", rec.getInt("create_user")).set("examineTime", rec.getDate("examineTime"))));
            rec.set("userList", logs).set("stepType", 5);
            list.add(rec);
            list.addAll(steps);
            jsonObject.put("steps", list);
        }
        return R.ok().put("data", jsonObject);

    }


    public R queryExamineRelation(BasePageRequest<OaExamineRelation> pageRequest){
        OaExamineRelation relation = pageRequest.getData();
        if(AuthUtil.oaAuth(relation.toRecord())){
            return R.noAuth();
        }
        Page<Record> paginate = Db.paginate(pageRequest.getPage(), pageRequest.getLimit(), Db.getSqlPara("oa.examine.queryExamineRelation", Kv.by("businessIds", relation.getBusinessIds()).set("contactsIds", relation.getContactsIds()).set("contractIds", relation.getContractIds()).set("customerIds", relation.getCustomerIds())));
        transfer(paginate.getList());
        return R.ok().put("data", paginate);
    }

    /**
     * 递归查询上一审核人主管第一级审批步骤
     */
    private Long queryStep(OaExamineStep step, Integer recordId, Long ownerUserId){
        Record r = Db.findFirst(Db.getSqlPara("oa.examine.queryExamineUserByExamineStepId",
                Kv.by("recordId",recordId).set("examineStepId",step.getStepId()).set("examineStatus",1)));
        if (r == null) {
            //获取上一个审核步骤
            OaExamineStep lastStep = OaExamineStep.dao.findFirst(Db.getSql("oa.examine.queryExamineStepByLastExamineIdOrderByStepId"), step.getCategoryId(), step.getStepId());
            if (lastStep == null){
                AdminUser adminUser = AdminUser.dao.findById(ownerUserId);
                if (adminUser.getParentId() == null){
                    return ownerUserId;
                }else {
                    return adminUser.getParentId();
                }
            }
            Long userId = queryStep(lastStep, recordId,ownerUserId);
            if (userId != null) {
                AdminUser adminUser = AdminUser.dao.findById(userId);
                if (adminUser.getParentId() == null){
                    return userId;
                }else {
                    return adminUser.getParentId();
                }
            }else{
                AdminUser adminUser = AdminUser.dao.findById(ownerUserId);
                if (adminUser.getParentId() == null){
                    return ownerUserId;
                }else {
                    return adminUser.getParentId();
                }
            }
        }else {
            //已经生成审核
            AdminUser adminUser = AdminUser.dao.findById(r.getLong("examine_user"));
            if (adminUser.getParentId() == null){
                return r.getLong("examine_user");
            }else {
                return adminUser.getParentId();
            }
        }
    }

    /**
     * 导出
     */
    public List<Map<String, Object>> export(BasePageRequest basePageRequest, Integer type, String title, List<Record> fieldList) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer queryType = jsonObject.getInteger("queryType");
        List<Record> examineList = new ArrayList<>();
        Long userId = BaseUtil.getUserId();
        //我发出的
        if (queryType == 1) {
            examineList = Db.template("oa.examine.myInitiateExcel", Kv.by("userId", userId).set("categoryId", jsonObject.getInteger("categoryId")).set("status", jsonObject.getInteger("checkStatus")).set("startTime", jsonObject.getDate("startTime")).set("endTime", jsonObject.getDate("endTime")).set("isAdmin", BaseUtil.isAdmin())).find();
            //待我审批的
        } else if (queryType == 2) {
            examineList = Db.template("oa.examine.myOaExamineExcel", Kv.by("userId", userId).set("createUserId", jsonObject.getLong("createUserId")).set("categoryId", jsonObject.getInteger("categoryId")).set("status", jsonObject.getInteger("status")).set("startTime", jsonObject.getDate("startTime")).set("endTime", jsonObject.getDate("endTime")).set("isAdmin", BaseUtil.isAdmin())).find();
        }
        List<Integer> examineIdList = examineList.stream().map(record -> record.getInt("examine_id")).collect(Collectors.toList());
        List<String> batchIdList = examineList.stream().map(record -> record.getStr("batch_id")).collect(Collectors.toList());
        List<Record> recordList;
        //根据审批类型不同 查询sql不同
        if (type == 5 || type == 3) {
            Kv kv = Kv.by("examineIds", examineIdList);
            recordList = Db.find(Db.getSqlPara("oa.examine.queryTravelExamineList", kv));
        } else if (type == 0) {
            List<Record> fields = fieldList.stream().filter(field -> field.getInt("field_type") != 1).collect(Collectors.toList());
            Kv kv=Kv.by("examineIds", examineIdList).set("batchIds", batchIdList).set("fieldMap", fields);
            recordList = Db.find(Db.getSqlPara("oa.examine.queryCustomExamineList", kv));
        } else {
            Kv kv=Kv.by("examineIds", examineIdList);
            recordList = Db.find(Db.getSqlPara("oa.examine.queryExamineList", kv));
        }
        List<Map<String, Object>> list = new ArrayList<>();
        switch (type) {
            case 1:
                recordList.forEach(record -> {
                    record.set("category", "普通审批");
                    putRelateCrmWork(record);
                    list.add(record.getColumns());
                });
                break;
            case 2:
                recordList.forEach(record -> {
                    record.set("category", "请假审批-" + record.getStr("type_id"));
                    putRelateCrmWork(record);
                    list.add(record.getColumns());
                });
                break;
            case 3:
                for (int i = 0; i < recordList.size(); i++) {
                    Record record = recordList.get(i);
                    //同一审批，从第二个起前面重复字段不用再显示一遍
                    if (i > 0 && record.getInt("examine_id").equals(recordList.get(i - 1).getInt("examine_id"))) {
                        record.set("category", "").set("create_time", "").set("create_user_name", "").set("examine_status", "")
                                .set("examine_user_name", "").set("previous_examine_user_name", "").set("content", "")
                                .set("remark", "").set("duration", "").set("relateCrmWork", "");
                    } else {
                        record.set("category", "出差审批");
                        putRelateCrmWork(record);
                    }
                    list.add(record.getColumns());
                }
                break;
            case 4:
                recordList.forEach(record -> {
                    record.set("category", "加班审批");
                    putRelateCrmWork(record);
                    list.add(record.getColumns());
                });
                break;
            case 5:
                for (int i = 0; i < recordList.size(); i++) {
                    Record record = recordList.get(i);
                    //同一审批，从第二个起前面重复字段不用再显示一遍
                    if (i > 0 && record.getInt("examine_id").equals(recordList.get(i - 1).getInt("examine_id"))) {
                        record.set("category", "").set("create_time", "").set("create_user_name", "").set("examine_status", "")
                                .set("examine_user_name", "").set("previous_examine_user_name", "").set("content", "")
                                .set("money", "").set("remark", "").set("relateCrmWork", "");
                    } else {
                        record.set("category", "差旅报销");
                        putRelateCrmWork(record);
                    }
                    list.add(record.getColumns());
                }
                break;
            case 6:
                recordList.forEach(record -> {
                    record.set("category", "借款申请");
                    putRelateCrmWork(record);
                    list.add(record.getColumns());
                });
                break;
            case 0:
                recordList.forEach(record -> {
                    record.set("category", title);
                    putRelateCrmWork(record);
                    list.add(record.getColumns());
                });
                break;
            default:
                break;
        }
        return list;
    }

    private void putRelateCrmWork(Record record) {
        String relateCrmWork = "";
        if (StrUtil.isNotEmpty(record.getStr("customer_ids"))) {
            relateCrmWork = relateCrmWork + "客户 【" + record.getStr("customer_name") + "】\n";
        }
        if (StrUtil.isNotEmpty(record.getStr("contacts_ids"))) {
            relateCrmWork = relateCrmWork + "联系人 【" + record.getStr("contacts_name") + "】\n";
        }
        if (StrUtil.isNotEmpty(record.getStr("business_ids"))) {
            relateCrmWork = relateCrmWork + "商机 【" + record.getStr("business_name") + "】\n";
        }
        if (StrUtil.isNotEmpty(record.getStr("contract_ids"))) {
            relateCrmWork = relateCrmWork + "合同 【" + record.getStr("contract_name") + "】\n";
        }
        record.set("relateCrmWork", relateCrmWork.trim());
    }
}
