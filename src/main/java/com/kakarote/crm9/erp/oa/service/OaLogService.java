package com.kakarote.crm9.erp.oa.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.service.CrmActivityService;
import com.kakarote.crm9.erp.oa.common.OaEnum;
import com.kakarote.crm9.erp.oa.entity.*;
import com.kakarote.crm9.utils.*;

import java.util.*;
import java.util.stream.Collectors;

public class OaLogService {

    @Inject
    private AdminFileService adminFileService;
    @Inject
    private OaCommentService commentService;
    @Inject
    private CrmActivityService crmActivityService;

    /**
     * 查询日志
     *
     * @param basePageRequest 分页参数
     * @author zhangzhiwei
     */
    public Page<OaLog> queryList(BasePageRequest basePageRequest) {
        JSONObject object = getLogListObject(basePageRequest);
        Page<OaLog> recordList = OaLog.dao.template("oa.log.queryList", object).paginate(basePageRequest.getPage(),basePageRequest.getLimit());
        recordList.getList().forEach((record -> {
            queryLogDetail(record, BaseUtil.getUserId());
        }));
        return recordList;
    }

    private JSONObject getLogListObject(BasePageRequest basePageRequest) {
        JSONObject object = basePageRequest.getJsonObject();
        AdminUser user = BaseUtil.getUser();
        //getIntValue 为空会返回0
        Integer by = object.getIntValue("by");
        BiParams biParams = new BiParams();
        biParams.setType(object.getString("type"));
        Record recordInfo = Aop.get(BiTimeUtil.class).analyzeTime(biParams);
        object.put("by", by);
        String createUserIdOne = object.getString("createUserId");
        String createUserIdTwo = object.getString("createUserId");
        if (StrUtil.isEmpty(createUserIdOne)) {
            object.put("isAdmin", BaseUtil.isAdmin());
        }
        if (!user.getRoles().contains(BaseUtil.getSuperRole()) && StrUtil.isNotEmpty(createUserIdOne)) {
            createUserIdOne = AuthUtil.filterUserId(createUserIdOne);
        }
        List<Long> createUserIdOneList = new ArrayList<>();
        if (StrUtil.isNotEmpty(createUserIdOne) && !",".equals(createUserIdOne)) {
            createUserIdOneList = Arrays.stream(createUserIdOne.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }
        List<Long> createUserIdTwoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(createUserIdTwo) && !",".equals(createUserIdTwo)) {
            createUserIdTwoList = Arrays.stream(createUserIdTwo.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }
        List<Long> userIds = new ArrayList<>();
        if (by.equals(1)) {
            userIds.add(user.getUserId());
        } else if (by.equals(2)) {
            object.put("send_user_ids", user.getUserId());
            if (createUserIdTwoList.size() > 0) {
                userIds.addAll(createUserIdTwoList);
            }
        } else if (by.equals(0)) {
            if (createUserIdTwoList.size() > 0) {
                object.put("userIds1", createUserIdOneList);
                object.put("userIds2", createUserIdTwoList);
            } else {
                userIds.addAll(Aop.get(AdminUserService.class).queryUserByParentUser(user.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
                userIds.add(user.getUserId());
                object.put("userIds1", userIds);
            }
            object.put("send_user_ids", user.getUserId());
        }
        object.put("userIds", userIds);
        object.putAll(recordInfo.getColumns());
        return object;
    }

    public void queryLogDetail(OaLog oaLog, Long userId) {
        oaLog.put(adminFileService.queryByBatchId(oaLog.getBatchId()));
        oaLog.put("sendUserList", (StrUtil.isNotEmpty(oaLog.getSendUserIds()) && oaLog.getSendUserIds().split(",").length > 0) ? Db.find(Db.getSqlPara("admin.user.queryByIds", Kv.by("ids", oaLog.getStr("send_user_ids").split(",")))) : new ArrayList<>());
        oaLog.put("customerList", (StrUtil.isNotEmpty(oaLog.getStr("customer_ids")) && oaLog.getStr("customer_ids").split(",").length > 0) ? Db.find(Db.getSqlPara("crm.customer.queryByIds", Kv.by("ids", oaLog.getStr("customer_ids").split(",")))) : new ArrayList<>());
        oaLog.put("businessList", (StrUtil.isNotEmpty(oaLog.getStr("business_ids")) && oaLog.getStr("business_ids").split(",").length > 0) ? Db.find(Db.getSqlPara("crm.business.queryByIds", Kv.by("ids", oaLog.getStr("business_ids").split(",")))) : new ArrayList<>());
        oaLog.put("contactsList", (StrUtil.isNotEmpty(oaLog.getStr("contacts_ids")) && oaLog.getStr("contacts_ids").split(",").length > 0) ? Db.find(Db.getSqlPara("crm.contact.queryByIds", Kv.by("ids", oaLog.getStr("contacts_ids").split(",")))) : new ArrayList<>());
        oaLog.put("contractList", (StrUtil.isNotEmpty(oaLog.getStr("contract_ids")) && oaLog.getStr("contract_ids").split(",").length > 0) ? Db.find(Db.getSqlPara("crm.contract.queryByIds", Kv.by("ids", oaLog.getStr("contract_ids").split(",")))) : new ArrayList<>());
        oaLog.put("createUser", Db.findFirst(Db.getSql("admin.user.queryUserByUserId"), oaLog.getCreateUserId()));
        int isEdit = userId.equals(oaLog.getCreateUserId()) ? 1 : 0;
        int isDel = 0;
        if ((System.currentTimeMillis() - 1000 * 3600 * 72) > oaLog.getCreateTime().getTime()) {
            if (BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole())) {
                isDel = 1;
            }
        } else {
            if (isEdit == 1 ) {
                isDel = 1;
            }
        }
        oaLog.put("permission", new JSONObject().fluentPut("is_update", isEdit).fluentPut("is_delete", isDel));
        if(!Objects.equals(0,oaLog.getInt("getBulletin"))){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("customerCount",oaLog.get("customerCount"));
            jsonObject.put("businessCount",oaLog.get("businessCount"));
            jsonObject.put("contractCount",oaLog.get("contractCount"));
            jsonObject.put("receivablesMoney",oaLog.get("receivablesMoney"));
            jsonObject.put("recordCount",oaLog.get("recordCount"));
            oaLog.put("bulletin",jsonObject);
        }

    }

    /**
     * @param object object
     * @return 响应结果
     * @author zhangzhiwei
     */
    @Before(Tx.class)
    public R saveAndUpdate(JSONObject object) {
        AdminUser user = BaseUtil.getUser();
        Integer type=object.getInteger("getBulletin");
        OaLog oaLog = object.toJavaObject(OaLog.class);
        OaLogRelation oaLogRelation = object.toJavaObject(OaLogRelation.class);
        oaLog.setCreateUserId(user.getUserId());
        oaLog.setCreateTime(new Date());
        oaLog.setReadUserIds(",,");
        oaLog.setSendUserIds(TagUtil.fromString(oaLog.getSendUserIds()));
        oaLog.setSendDeptIds(TagUtil.fromString(oaLog.getSendDeptIds()));
        if (oaLog.getLogId() != null) {
            boolean oaAuth = AuthUtil.isOaAuth(OaEnum.LOG_TYPE_KEY, oaLog.getLogId());
            if (oaAuth) {
                return R.noAuth();
            }
            oaLog.update();
            Aop.get(ActionRecordUtil.class).addOaLogUpdateRecord(CrmEnum.OA_LOG, oaLog.getLogId());
            Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_LOG_SEND, oaLog, user.getUserId());
        } else {
            oaLog.save();
            Aop.get(ActionRecordUtil.class).addOaLogSaveRecord(CrmEnum.OA_LOG, oaLog.getLogId());
            if(Objects.equals(type,1)){
                saveLogRecord(oaLog.getLogId());
            }
            Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_LOG_SEND, oaLog, user.getUserId());
        }
        if (oaLogRelation != null) {
            Db.deleteById("72crm_oa_log_relation", "log_id", oaLog.getLogId());
            oaLogRelation.setLogId(oaLog.getLogId());
            oaLogRelation.setBusinessIds(TagUtil.fromString(oaLogRelation.getBusinessIds()));
            oaLogRelation.setContactsIds(TagUtil.fromString(oaLogRelation.getContactsIds()));
            oaLogRelation.setContractIds(TagUtil.fromString(oaLogRelation.getContractIds()));
            oaLogRelation.setCustomerIds(TagUtil.fromString(oaLogRelation.getCustomerIds()));
            oaLogRelation.setCreateTime(DateUtil.date());
            oaLogRelation.save();
            crmActivityService.addActivity(2, CrmActivityEnum.LOG, oaLog.getLogId(), "");
        }
        return R.ok();
    }


    /**
     * 根据id获取日志
     *
     * @param id 日志ID
     * @author zhangzhiwei
     */
    public OaLog queryById(Integer id) {
        OaLog oaLog = OaLog.dao.findFirst(Db.getSqlPara("oa.log.queryList", Kv.by("logId", id)));
        queryLogDetail(oaLog, BaseUtil.getUser().getUserId());
        return oaLog;
    }

    /**
     * 根据id删除日志
     *
     * @param logId 日志ID
     * @author zhangzhiwei
     */
    @Before(Tx.class)
    public boolean deleteById(Integer logId) {
        OaLog oaLog = OaLog.dao.findById(logId);
        if (oaLog != null) {
            Db.deleteById("72crm_oa_log_relation", "log_id", logId);
            adminFileService.removeByBatchId(oaLog.getBatchId());
            Db.deleteById("72crm_oa_log", "log_id", logId);
            commentService.deleteComment(2, logId);
            return true;
        }
        return false;
    }

    /**
     *
     * @param logId 日志ID
     * @author zhangzhiwei
     */
    public void readLog(Integer logId) {
        OaLog oaLog = OaLog.dao.findById(logId);
        HashSet<String> hashSet = new HashSet<>(StrUtil.splitTrim(oaLog.getReadUserIds(), ","));
        hashSet.add(BaseUtil.getUser().getUserId().toString());
        oaLog.setReadUserIds("," + String.join(",", hashSet) + ",");
        oaLog.update();
    }

    /**
     * 查询crm关联日志
     */
    public R queryLogRelation(BasePageRequest<OaLogRelation> basePageRequest) {
        OaLogRelation relation = basePageRequest.getData();
        if (AuthUtil.oaAuth(relation.toRecord())) {
            return R.noAuth();
        }
        Page<OaLog> recordPage = OaLog.dao.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("oa.log.queryLogRelation", Kv.by("businessIds", relation.getBusinessIds()).set("contactsIds", relation.getContactsIds()).set("contractIds", relation.getContractIds()).set("customerIds", relation.getCustomerIds())));
        AdminUser user = BaseUtil.getUser();
        recordPage.getList().forEach((record -> {
            queryLogDetail(record, user.getUserId());
        }));
        return R.ok().put("data", recordPage);
    }

    /**
     * 查询日志统计信息
     *
     * @return R r
     * @author zhangzhiwei
     */
    public R queryLogBulletin() {
        String beginTime = DateUtil.beginOfMonth(new Date()).toDateStr();
        String endTime = DateUtil.endOfMonth(new Date()).toDateStr();
        Kv kv = Kv.by("userId", BaseUtil.getUserId()).set("beginTime", beginTime).set("endTime", endTime);
        Record record = Db.template("oa.log.queryLogBulletin", kv).findFirst();
        return R.ok().put("data", record);
    }

    /**
     * 保存日志和销售简报关联信息
     * @param logId 日志ID
     */
    private void saveLogRecord(Integer logId){
        OaLogRecord logRecord=new OaLogRecord();
        logRecord.setLogId(logId);
        Record record=Db.template("crm.Instrument.queryBulletinByLog",Kv.by("userIds", BaseUtil.getUserId())).findFirst();
        logRecord.put(record);
        logRecord.setCreateTime(new Date());
        logRecord.save();
        List<OaLogBulletin> bulletinList= OaLogBulletin.dao.template("crm.Instrument.queryBulletinByLogInfo",Kv.by("userIds", BaseUtil.getUserId())).find();
        Date date=new Date();
        bulletinList.forEach(bulletin->{
            bulletin.setLogId(logId);
            bulletin.setCreateTime(date);
        });
        Db.batchSave(bulletinList,200);
    }

    public R queryLogBulletinByType(BasePageRequest<OaLogBulletin> pageRequest) {
        OaLogBulletin data = pageRequest.getData();
        JSONObject jsonObject=pageRequest.getJsonObject();
        String search = jsonObject.getString("search");
        Integer crmType = jsonObject.getInteger("crmType");
        String camelField = jsonObject.getString("sortField");
        String sortField = StrUtil.toUnderlineCase(camelField);
        if(! ParamsUtil.isValid(sortField)){
            return R.error("参数包含非法字段");
        }
        Integer order = jsonObject.getInteger("order");
        Integer today=jsonObject.getInteger("today");
        String orderType = "asc";
        if (order != null){
            orderType=order==1?"asc":"desc";
        }
        Integer type = data.getType();
        List<Integer> typeIds=new ArrayList<>();
        if(Objects.equals(today,1)){
            List<OaLogBulletin> bulletinList= OaLogBulletin.dao.template("crm.Instrument.queryBulletinByLogInfo",Kv.by("userIds", BaseUtil.getUserId())).find();
            bulletinList.forEach(oaLogBulletin -> {
                if(oaLogBulletin.getType().equals(type)){
                    typeIds.add(oaLogBulletin.getTypeId());
                }
            });
        }else {
            typeIds.addAll(Db.query("SELECT type_id FROM `72crm_oa_log_bulletin` where log_id=? and type=?",data.getLogId(),type));
        }

        Page<Record> paginate = Db.template("oa.log.queryLogBulletinByType", Kv.by("type", type).set("typeIds", typeIds).set("search", search).set("sortField", sortField).set("orderType", orderType).set("crmType", crmType)).paginate(pageRequest.getPage(), pageRequest.getLimit());
        if (type == 5){
            paginate.getList().forEach(record -> crmActivityService.buildActivityRelation(record));
        }
        return  R.ok().put("data",paginate);
    }

    public R queryLogRecordCount(Integer logId, Integer today){
        List<Integer> typeIds=new ArrayList<>();
        if(Objects.equals(today,1)){
            List<OaLogBulletin> bulletinList = OaLogBulletin.dao.template("crm.Instrument.queryBulletinByLogInfo", Kv.by("userIds", BaseUtil.getUserId())).find();
            bulletinList.forEach(oaLogBulletin -> {
                if(oaLogBulletin.getType().equals(5)){
                    typeIds.add(oaLogBulletin.getTypeId());
                }
            });
        }else {
            typeIds.addAll(Db.query("SELECT type_id FROM `72crm_oa_log_bulletin` where log_id=? and type=?",logId,5));
        }
        List<Record> recordList = Db.template("oa.log.queryLogRecordCount",Kv.by("typeIds",typeIds)).find();
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_LEADS.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_LEADS.getType()).set("count", 0));
        }
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_CUSTOMER.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_CUSTOMER.getType()).set("count", 0));
        }
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_CONTACTS.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_CONTACTS.getType()).set("count", 0));
        }
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_BUSINESS.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_BUSINESS.getType()).set("count", 0));
        }
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_CONTRACT.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_CONTRACT.getType()).set("count", 0));
        }
        return R.ok().put("data", recordList);
    }

    /**
     * 导出
     */
    public List<Map<String, Object>> export(BasePageRequest basePageRequest) {
        JSONObject object = getLogListObject(basePageRequest);
        List<Record> recordList = Db.template("oa.log.queryExportList", object).find();
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.OA_LOG, recordList.size());
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach((record -> {
            //拼接接收者内容
            String sendName = "";
            if (StrUtil.isNotEmpty(record.getStr("send_user_name"))) {
                sendName = sendName + record.getStr("send_user_name") + ",";
            }
            if (StrUtil.isNotEmpty(record.getStr("send_dept_name"))) {
                sendName = sendName + record.getStr("send_dept_name") + ",";
            }
            if (StrUtil.isNotEmpty(sendName)) {
                sendName = sendName.substring(0, sendName.length() - 1);
            }
            //拼接关联业务内容
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
            //拼接评论回复内容
            List<Record> commentList = Db.find(Db.getSql("oa.log.queryCommentList"), record.getInt("log_id"));
            StringBuilder stringBuilder = new StringBuilder();
            commentList.forEach(comment -> {
                stringBuilder.append(comment.getStr("create_user_name")).append("：");
                if (!comment.getInt("main_id").equals(0)) {
                    stringBuilder.append("@").append(comment.getStr("reply_name")).append(" ");
                }
                stringBuilder.append(comment.getStr("content")).append("\n");
            });
            list.add(record.set("send_name", sendName).set("relateCrmWork", relateCrmWork.trim()).set("comment", stringBuilder.toString().trim()).getColumns());
        }));
        return list;
    }

    /**
     * 查询日志完成情况统计
     */
    public R queryCompleteStats(Integer type) {
        Integer status = Db.queryInt("select status from 72crm_oa_log_rule where type = ? ", type);
        if (status == 0) {
            return R.ok().put("data", Kv.by("status", status));
        }
        Kv kv = getCycleTime(type);
        if (ObjectUtil.isEmpty(kv)) {
            return R.ok().put("data", Kv.by("totalNum", 0).set("completeNum", 0));
        }
        Integer completeNum = Db.template("oa.log.queryCompleteStats", kv).queryInt();
        return R.ok().put("data", Kv.by("totalNum", kv.get("totalNum")).set("completeNum", completeNum));
    }

    /**
     * 查询日志完成情况
     */
    public Page<OaLog> queryCompleteOaLogList(BasePageRequest<OaLogRule> basePageRequest){
        Integer type = basePageRequest.getData().getType();
        Kv kv = getCycleTime(type);
        if (ObjectUtil.isEmpty(kv)){
            return new Page<>();
        }
        String search = basePageRequest.getJsonObject().getString("search");
        if (StrUtil.isNotEmpty(search)) {
            kv.set("search", search);
        }
        JSONObject object = getLogListObject(basePageRequest);
        kv.set("isAdmin", object.get("isAdmin")).set("userIds1", object.get("userIds1")).set("userIds2", object.get("userIds2")).set("send_user_ids", object.get("send_user_ids"));
        Page<OaLog> page =  OaLog.dao.template("oa.log.queryCompleteOaLogList",kv).paginate(basePageRequest.getPage(),basePageRequest.getLimit());
        page.getList().forEach((log -> {
            queryLogDetail(log, BaseUtil.getUserId());
        }));
        return page;
    }

    /**
     * 查询日志未完成情况
     */
    public Page<AdminUser> queryIncompleteOaLogList(BasePageRequest<OaLogRule> basePageRequest){
        Integer type = basePageRequest.getData().getType();
        Kv kv = getCycleTime(type);
        if (ObjectUtil.isEmpty(kv)){
            return new Page<>();
        }
        String search = basePageRequest.getJsonObject().getString("search");
        if (StrUtil.isNotEmpty(search)) {
            kv.set("search", search);
        }
        return AdminUser.dao.template("oa.log.queryIncompleteOaLogList",kv).paginate(basePageRequest.getPage(),basePageRequest.getLimit());
    }

    private Kv getCycleTime(Integer type) {
        OaLogRule rule = OaLogRule.dao.findFirst("select * from 72crm_oa_log_rule where type = ?",type);
        List<Long> userIdList;
        //为空代表范围是全部员工
        if (StrUtil.isEmpty(rule.getMemberUserId())) {
            userIdList = Db.query("select user_id from 72crm_admin_user where status = 1");
        } else {
            userIdList = Arrays.stream(rule.getMemberUserId().split(",")).map(Long::parseLong).collect(Collectors.toList());
        }
        AuthUtil.filterUserId(userIdList);
        Integer totalNum = userIdList.size();
        String start = "";
        String end = "";
        if (type.equals(1)){
            Integer dayOfWeek = DateUtil.dayOfWeek(new Date());
            //dayOfWeek 是 1 周日 2-7 周一到周六 数据库是 1-6 周一到周六 7 周日 所以要转换下
            dayOfWeek = dayOfWeek.equals(0) ? 7 : dayOfWeek-1;
            String[] effectiveDayArr = rule.getEffectiveDay().split(",");
            List<Integer> dayList = Arrays.stream(effectiveDayArr).map(Integer::parseInt).collect(Collectors.toList());
            if (!dayList.contains(dayOfWeek)){
                return new Kv();
            }
            String nowDay = DateUtil.format(new Date(),"yyyy-MM-dd");
            start = nowDay+" "+rule.getStartTime();
            end = nowDay+" "+rule.getEndTime();
        }else if (type.equals(2)){
            Date beginDay = DateUtil.beginOfWeek(new Date());
            start = DateUtil.format(DateUtil.offsetDay(beginDay, rule.getStartDay() - 1), "yyyy-MM-dd") + " 00:00";
            end = DateUtil.format(DateUtil.offsetDay(beginDay, rule.getEndDay() - 1), "yyyy-MM-dd") + " 12:00";
        }else if (type.equals(3)){
            Date beginDay = DateUtil.beginOfMonth(new Date());
            start = DateUtil.format(DateUtil.offsetDay(beginDay, rule.getStartDay() - 1), "yyyy-MM-dd") + " 00:00";
            Integer day = DateUtil.dayOfMonth(DateUtil.endOfMonth(new Date()));
            end = DateUtil.format(DateUtil.offsetDay(beginDay, day > rule.getEndDay() ? rule.getEndDay() - 1 : day), "yyyy-MM-dd") + " 12:00";
        }
        return Kv.by("start", start).set("end", end).set("userIds", userIdList).set("totalNum", totalNum).set("type", type);
    }

    /**
     * 查询日志提交设置
     * @return
     */
    public List<Record> queryOaLogRuleList(){
        List<Record> recordList = Db.find("select * from 72crm_oa_log_rule");
        recordList.forEach(record -> {
            List<Record> userList = Db.find("select user_id,realname,img from 72crm_admin_user where find_in_set(user_id,?)", record.getStr("member_user_id"));
            record.set("memberUser", userList);
        });
        return recordList;
    }

    /**
     * 设置日志提交设置
     */
    @Before(Tx.class)
    public R setOaLogRule(JSONArray jsonArray) {
        List<OaLogRule> ruleList = jsonArray.toJavaList(OaLogRule.class);
        ruleList.forEach(Model::update);
        return R.ok();
    }
}
