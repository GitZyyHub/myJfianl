package com.kakarote.crm9.erp.crm.service;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.utils.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrmActivityService{


    @Inject
    private AdminFileService adminFileService;

    @Before(Tx.class)
    public R addCrmActivityRecord(CrmActivity crmActivity){
        if (crmActivity.getNextTime() != null && crmActivity.getNextTime().getTime() < System.currentTimeMillis()){
            return R.error("最后跟进时间必须大于当前时间");
        }
        AdminUser user = BaseUtil.getUser();
        String batchId = StrUtil.isEmpty(crmActivity.getBatchId())? IdUtil.simpleUUID():crmActivity.getBatchId();
        crmActivity.setType(1);
        crmActivity.setCreateUserId(user.getUserId());
        crmActivity.setBatchId(batchId);
        crmActivity.save();
        updateNextTime(crmActivity);
        Integer activityType = crmActivity.getActivityType();
        if (activityType.equals(CrmEnum.CRM_LEADS.getType())) {
            Db.update("update 72crm_crm_leads set followup = 1 where leads_id = ?", crmActivity.getActivityTypeId());
        } else if (activityType.equals(CrmEnum.CRM_CUSTOMER.getType())) {
            Db.update("update 72crm_crm_customer set followup = 1 where customer_id = ?", crmActivity.getActivityTypeId());
        }
        Aop.get(ActionRecordUtil.class).addFollowupActionRecord(activityType, crmActivity.getActivityTypeId());
        return R.ok();
    }


    /**
     * 更新下次联系时间
     * @param crmActivity
     */
    private void updateNextTime(CrmActivity crmActivity){
        Date nextTime = crmActivity.getNextTime();
        Integer activityType = crmActivity.getActivityType();
        Integer activityTypeId = crmActivity.getActivityTypeId();
        if(activityType.equals(CrmEnum.CRM_LEADS.getType())){
            CrmLeads crmLeads = new CrmLeads();
            crmLeads.setLeadsId(activityTypeId);
            crmLeads.setLastTime(new Date());
            crmLeads.setLastContent(crmActivity.getContent());
            if (nextTime != null) {
                crmLeads.setNextTime(nextTime);
            }
            crmLeads.setUpdateTime(new Date());
            crmLeads.update();
        }else if(activityType.equals(CrmEnum.CRM_CUSTOMER.getType())){
            CrmCustomer crmCustomer = new CrmCustomer();
            crmCustomer.setCustomerId(activityTypeId);
            crmCustomer.setLastTime(new Date());
            crmCustomer.setLastContent(crmActivity.getContent());
            if (nextTime != null) {
                crmCustomer.setNextTime(nextTime);
            }
            crmCustomer.setUpdateTime(new Date());
            crmCustomer.setFollowup(1);
            crmCustomer.update();
        }else if(activityType.equals(CrmEnum.CRM_CONTACTS.getType())){
            CrmContacts crmContacts = new CrmContacts();
            crmContacts.setContactsId(activityTypeId);
            crmContacts.setLastTime(new Date());
            if (nextTime != null) {
                crmContacts.setNextTime(nextTime);
            }
            crmContacts.setUpdateTime(new Date());
            crmContacts.update();
        }else if(activityType.equals(CrmEnum.CRM_BUSINESS.getType())){
            CrmBusiness crmBusiness = new CrmBusiness();
            crmBusiness.setBusinessId(activityTypeId);
            crmBusiness.setLastTime(new Date());
            if (nextTime != null) {
                crmBusiness.setNextTime(nextTime);
            }
            crmBusiness.setUpdateTime(new Date());
            crmBusiness.update();
        }else if(activityType.equals(CrmEnum.CRM_CONTRACT.getType())){
            CrmContract crmContract = new CrmContract();
            crmContract.setContractId(activityTypeId);
            crmContract.setLastTime(new Date());
            crmContract.setUpdateTime(new Date());
            crmContract.update();
        }
        List<CrmContacts> crmContactsList = new ArrayList<>();
        List<CrmBusiness> businessList = new ArrayList<>();
        List<CrmActivityRelation> activityRelationList = new ArrayList<>();
        if(crmActivity.getContactsIds() != null){
            for(Integer contactId : TagUtil.toSet(crmActivity.getContactsIds())){
                CrmContacts crmContacts = new CrmContacts();
                crmContacts.setContactsId(contactId);
                crmContacts.setNextTime(nextTime);
                CrmActivityRelation activityRelation = new CrmActivityRelation();
                activityRelation.setActivityId(crmActivity.getActivityId());
                activityRelation.setType(CrmEnum.CRM_CONTACTS.getType());
                activityRelation.setTypeId(contactId);
                activityRelationList.add(activityRelation);
                crmContactsList.add(crmContacts);
            }
        }
        if(crmActivity.getBusinessIds() != null){
            for(Integer businessId : TagUtil.toSet(crmActivity.getBusinessIds())){
                CrmBusiness crmBusiness = new CrmBusiness();
                crmBusiness.setBusinessId(businessId);
                crmBusiness.setNextTime(nextTime);
                CrmActivityRelation activityRelation = new CrmActivityRelation();
                activityRelation.setActivityId(crmActivity.getActivityId());
                activityRelation.setType(CrmEnum.CRM_BUSINESS.getType());
                activityRelation.setTypeId(businessId);
                activityRelationList.add(activityRelation);
                businessList.add(crmBusiness);
            }
        }
        Db.batchUpdate(businessList,20);
        Db.batchUpdate(crmContactsList,20);
        Db.batchSave(activityRelationList,20);
    }

    public R getCrmActivityPageList(BasePageRequest<CrmActivity> pageRequest){
        CrmActivity crmActivity = pageRequest.getData();
        Integer activityType = crmActivity.getActivityType();
        Integer activityTypeId = crmActivity.getActivityTypeId();
        int page = pageRequest.getPage()-1;
        Kv kv=Kv.by("activityType",activityType).set("activityTypeId",activityTypeId).set("crmType",crmActivity.getCrmType()).set("activityType",crmActivity.getActivityType()).set("page",page);
        List<String> times=Db.template("crm.activity.getActivityCountByTime",kv).query();
        String time = null;
        List<Record> recordList;
        if(times.size()==0){
            recordList = new ArrayList<>();
        }else {
            time=times.get(0);
            kv.remove("page");
            recordList = Db.template("crm.activity.getCrmActivityPageList",kv.set("time",time)).find();
            recordList.parallelStream().forEach(record -> {
                if (record.getInt("type") == 1 || record.getInt("type") == 4) {
                    buildActivityRelation(record);
                }
            });
        }
        return R.ok().put("data",Kv.by("time",time).set("list",recordList)).put("lastPage",times.size()<2);
    }

    public void  buildActivityRelation(Record record){
        if (record.getInt("status")==1){
            adminFileService.queryByBatchId(record.getStr("batch_id"),record);
        }
        if(record.getInt("activity_type") == CrmEnum.CRM_CUSTOMER.getType()){
            String businessIds = record.getStr("business_ids");
            List<CrmBusiness> businessList = new ArrayList<>();
            if(businessIds != null){
                List<String> businessIdsArr = StrUtil.splitTrim(businessIds,",");
                for(String businessId : businessIdsArr){
                    businessList.add(CrmBusiness.dao.findById(Integer.valueOf(businessId)));
                }
            }
            String contactsIds = record.getStr("contacts_ids");
            List<CrmContacts> contactsList = new ArrayList<>();
            if(contactsIds != null){
                List<String> contactsIdsArr = StrUtil.splitTrim(contactsIds,",");
                for(String contactsId : contactsIdsArr){
                        contactsList.add(CrmContacts.dao.findById(Integer.valueOf(contactsId)));
                }
            }
            record.set("business_list", businessList).set("contacts_list", contactsList);
        }
    }

    /**
     * 添加活动记录
     * @param type 2 创建记录 3 商机阶段变更
     * @param activityEnum  crm活动类型
     * @param activityTypeId 对象id
     * @param businessChange 商机变化阶段
     */
    public void addActivity(Integer type, CrmActivityEnum activityEnum, Integer activityTypeId, String businessChange){
        String content = "";
        if(type == 2){
            content = "创建了"+activityEnum.getRemarks()+": ";
        }else if(type == 3){
            content = businessChange;
        }
        CrmActivity crmActivity = new CrmActivity();
        crmActivity.setType(type);
        crmActivity.setActivityType(activityEnum.getType());
        crmActivity.setActivityTypeId(activityTypeId);
        crmActivity.setContent(content);
        crmActivity.setCreateUserId(BaseUtil.getUserId());
        crmActivity.save();
    }

    public R deleteCrmActivityRecord(Integer activityId){
        CrmActivity crmActivity = new CrmActivity().findById(activityId);
        if(crmActivity.getType() != 1){
            return R.error("只能删除跟进记录");
        }
        adminFileService.removeByBatchId(crmActivity.getBatchId());
        //查询是否存在上一次跟进记录，存在时更新对应数据的最后跟进时间和内容。
        CrmActivity lastRecord = CrmActivity.dao.findFirst(Db.getSql("crm.activity.queryLastRecord"), crmActivity.getActivityType(), crmActivity.getActivityTypeId(), activityId);
        if (ObjectUtil.isNotEmpty(lastRecord)) {
            CrmEnum crmEnum = CrmEnum.parse(lastRecord.getActivityType());
            Db.update(Db.getSqlPara("crm.activity.updateLastRecord", Kv.by("id", lastRecord.getActivityTypeId()).set("tableName", crmEnum.getTableName()).set("tableId", crmEnum.getTableId()).set("type", crmEnum.getType()).set("lastTime", lastRecord.getCreateTime()).set("lastContent", lastRecord.getContent())));
        }
        return crmActivity.delete()?R.ok():R.error();
    }

    public void deleteActivityRecord(CrmActivityEnum crmActivityEnum,String... ids){
        Kv kv=Kv.by("type",crmActivityEnum.getType()).set("ids",ids);
        Db.template("crm.activity.delete",kv).delete();
    }

    /**
     * 外勤签到
     */
    public R outworkSign(CrmActivity crmActivity) {
        AdminUser user = BaseUtil.getUser();
        String batchId = StrUtil.isEmpty(crmActivity.getBatchId()) ? IdUtil.simpleUUID() : crmActivity.getBatchId();
        crmActivity.setType(4);
        crmActivity.setCreateUserId(user.getUserId());
        crmActivity.setBatchId(batchId);
        crmActivity.save();
        return R.ok();
    }

    /**
     * app外勤统计
     *
     * @param basePageRequest
     * @return
     */
    public R queryOutworkStats(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "crm", "outwork", "read");
        Kv kv = Kv.by("startTime", jsonObject.getDate("startTime")).set("endTime", jsonObject.getDate("endTime")).set("userId", AuthUtil.getUserIdByAuth(menuId));
        Page<Record> page = Db.template("crm.activity.queryOutworkStats", kv).paginate(basePageRequest.getPage(), basePageRequest.getLimit());
        return R.ok().put("data", page);
    }

    /**
     * app外勤详情
     */
    public R queryOutworkList(BasePageRequest basePageRequest) {
        Long userId = basePageRequest.getJsonObject().getLong("userId");
        Date startTime = basePageRequest.getJsonObject().getDate("startTime");
        Date endTime = basePageRequest.getJsonObject().getDate("endTime");
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "crm", "outwork", "read");
        List<Long> authUserIdList = AuthUtil.getUserIdByAuth(menuId);
        if (!authUserIdList.contains(userId)) {
            return R.error("无权限查询该用户");
        }
        Page<Record> recordList = Db.template("crm.activity.queryOutworkList", Kv.by("userId", userId).set("startTime", startTime).set("endTime", endTime)).paginate(basePageRequest.getPage(), basePageRequest.getLimit());
        recordList.getList().forEach(record -> Aop.get(CrmActivityService.class).buildActivityRelation(record));
        return R.ok().put("data", recordList);
    }

    /**
     * app 查询签到照片上传设置
     */
    public R queryPictureSetting() {
        Integer status = Db.queryInt("select status from 72crm_admin_config where name = 'pictureSetting'");
        if (status == null) {
            AdminConfig adminConfig = new AdminConfig();
            adminConfig.setStatus(1);
            adminConfig.setName("pictureSetting");
            adminConfig.save();
            status = 1;
        }
        return R.ok().put("data", status);
    }

    /**
     * app 查询签到照片上传设置
     */
    public R setPictureSetting(Integer status) {
        Db.update("update 72crm_admin_config set status = ? where name = 'pictureSetting'", status);
        return R.ok();
    }

    /**
     * 删除外勤签到
     */
    public R deleteOutworkSign(Integer activityId) {
        Long userId = Db.queryLong("select create_user_id from 72crm_crm_activity where activity_id = ?", activityId);
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "crm", "outwork", "delete");
        List<Long> authUserIdList = AuthUtil.getUserIdByAuth(menuId);
        if (!authUserIdList.contains(userId)) {
            return R.error("无权限删除该记录");
        }
        CrmActivity.dao.deleteById(activityId);
        return R.ok();
    }
}
