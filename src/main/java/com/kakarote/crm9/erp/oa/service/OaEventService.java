package com.kakarote.crm9.erp.oa.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminMessage;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmBusiness;
import com.kakarote.crm9.erp.crm.entity.CrmContacts;
import com.kakarote.crm9.erp.crm.entity.CrmContract;
import com.kakarote.crm9.erp.crm.entity.CrmCustomer;
import com.kakarote.crm9.erp.oa.common.RepetitionType;
import com.kakarote.crm9.erp.oa.entity.*;
import com.kakarote.crm9.erp.oa.service.repetitionstrategy.AbstractRepetitionStrategy;
import com.kakarote.crm9.erp.oa.service.repetitionstrategy.RepetitionContext;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;

import java.util.*;
import java.util.stream.Collectors;

public class OaEventService {

    public List<OaEventDTO> queryList(JSONObject data) {
        long startTime = data.getLong("startTime");
        long endTime = data.getLong("endTime");
        JSONArray typeIds = data.getJSONArray("typeIds");
        Long userId = data.getLong("userId");
        if (userId == null){
            userId = BaseUtil.getUserId();
        }
        List<OaEvent> oaEventList = new OaEvent().dao().template("oa.event.queryList",Kv.by("userId",userId).set("startTime",startTime).set("endTime",endTime).set("typeIds",typeIds)).find();
        return queryList(oaEventList, startTime, endTime);
    }

    private List<OaEventDTO> queryList(List<OaEvent> oaEventList,long startTime,long endTime){
        List<OaEventDTO> recordList=new ArrayList<>();
        Set<String> batchIds = oaEventList.stream().map(OaEvent::getBatchId).collect(Collectors.toSet());
        List<OaEventUpdateRecord> oaEventUpdateRecordList = new OaEventUpdateRecord().dao().template("oa.event.queryEventRecordList",Kv.by("batchIds",batchIds)).find();
        Map<String, Integer> map = new HashMap<>();
        oaEventUpdateRecordList.forEach(settingEvent -> {
            String key=settingEvent.getEventId()+"_"+DateUtil.formatDate(new Date(settingEvent.getTime()));
            map.put(key,settingEvent.getStatus());
        });
        Map<Integer, List<OaEvent>> collect = oaEventList.stream().collect(Collectors.groupingBy(OaEvent::getRepetitionType));
        collect.forEach((repetitionType,list)-> {
            AbstractRepetitionStrategy repetitionStrategy = RepetitionContext.getRepetitionStrategy(repetitionType);
            repetitionStrategy.setSpecialDayMap(map);
            List<OaEventDTO> resultList = repetitionStrategy.processQuery(list, startTime, endTime);
            recordList.addAll(resultList);
        });
        return recordList;
    }

    @Before(Tx.class)
    public R save(JSONObject data) {
        OaEvent event = data.getObject("event", OaEvent.class);
        OaEventRelation eventRelation = null;
        if (data.containsKey("relation")){
            eventRelation  = data.getObject("relation", OaEventRelation.class);
        }
        List<OaEventNotice> notices = new ArrayList<>();
        if (data.containsKey("notice")){
           notices = data.getJSONArray("notice").toJavaList(OaEventNotice.class);
        }
        AbstractRepetitionStrategy repetitionStrategy = RepetitionContext.getRepetitionStrategy(event.getRepetitionType());
        event = repetitionStrategy.processTime(event);
        String batchId = IdUtil.simpleUUID();
        event.setBatchId(batchId);
        event.setCreateUserId(BaseUtil.getUserId());
        event.setCreateTime(new Date());
        boolean save = event.save();
        Aop.get(ActionRecordUtil.class).addObjectActionRecord(CrmEnum.OA_CALENDAR, event.getEventId(), BehaviorEnum.SAVE);
        if (eventRelation != null){
            eventRelation.setEventId(event.getEventId());
            eventRelation.setCreateTime(new Date());
            eventRelation.save();
        }
        for (OaEventNotice notice : notices) {
            notice.setEventId(event.getEventId());
        }
        Db.batchSave(notices,10);
        eventNoticeUpdate(event,event.getEventId());
        return save?R.ok():R.error();
    }


    @Before(Tx.class)
    public R update(JSONObject data) {
        Integer type = data.getInteger("type");
        Long time = data.getLong("time");
        OaEvent event = data.getObject("event", OaEvent.class);
        Integer eventId = event.getEventId();
        boolean save;
        if (type == 2){
            AbstractRepetitionStrategy repetitionStrategy = RepetitionContext.getRepetitionStrategy(event.getRepetitionType());
            event = repetitionStrategy.processTime(event);
            event.setEventId(null);
            event.setCreateUserId(BaseUtil.getUserId());
            event.setCreateTime(new Date());
            save = event.save();
            OaEventUpdateRecord eventUpdateRecord = OaEventUpdateRecord.dao.findFirst("select * from `72crm_oa_event_update_record` where new_event_id = ? and status = 2", eventId);
            if (eventUpdateRecord != null){
                OaEvent.dao.deleteById(eventId);
                eventUpdateRecord.setNewEventId(event.getEventId());
                eventUpdateRecord.update();
            }else {
                OaEvent oldEvent = OaEvent.dao.findById(eventId);
                if (oldEvent.getRepetitionType().equals(RepetitionType.NO_REPETITION.getType())){
                    oldEvent.delete();
                    Db.template("oa.event.deleteEventRelation", Kv.by("eventIds",eventId)).delete();
                }else {
                    eventUpdateRecord = new OaEventUpdateRecord();
                    eventUpdateRecord.setEventId(eventId);
                    eventUpdateRecord.setTime(time);
                    eventUpdateRecord.setStatus(type);
                    eventUpdateRecord.setBatchId(event.getBatchId());
                    eventUpdateRecord.setNewEventId(event.getEventId());
                    eventUpdateRecord.save();
                }
            }
        }else {
            List<Integer> eventIds = Db.query("select new_event_id from `72crm_oa_event_update_record` where time > ? and batch_id = ?", time, event.getBatchId());
            Db.template("oa.event.deleteEventByIds",Kv.by("eventIds",eventIds)).delete();
            Db.delete("delete  from `72crm_oa_event_update_record` where time > ? and batch_id = ?", time, event.getBatchId());
            Db.update("update `72crm_oa_event` set repeat_end_time = ? where event_id = ?", new Date(time), eventId);
            AbstractRepetitionStrategy repetitionStrategy = RepetitionContext.getRepetitionStrategy(event.getRepetitionType());
            event = repetitionStrategy.processTime(event);
            event.setEventId(null);
            event.setCreateUserId(BaseUtil.getUserId());
            event.setCreateTime(new Date());
            save = event.save();
            OaEventUpdateRecord eventUpdateRecord = new OaEventUpdateRecord();
            eventUpdateRecord.setEventId(eventId);
            eventUpdateRecord.setTime(time);
            eventUpdateRecord.setStatus(type);
            eventUpdateRecord.setBatchId(event.getBatchId());
            eventUpdateRecord.setNewEventId(event.getEventId());
            eventUpdateRecord.save();
        }
        OaEventRelation eventRelation = null;
        if (data.containsKey("relation")){
            eventRelation  = data.getObject("relation", OaEventRelation.class);
        }
        if (eventRelation != null){
            eventRelation.setEventId(event.getEventId());
            eventRelation.setCreateTime(new Date());
            eventRelation.save();
        }
        List<OaEventNotice> notices = new ArrayList<>();
        if (data.containsKey("notice")){
            notices = data.getJSONArray("notice").toJavaList(OaEventNotice.class);
        }
        if (CollUtil.isNotEmpty(notices)){
            for (OaEventNotice notice : notices) {
                notice.setId(null);
                notice.setEventId(event.getEventId());
            }
            Db.batchSave(notices,10);
        }
        if (time <= System.currentTimeMillis()){
            eventNoticeUpdate(event,eventId);
        }
        return save?R.ok():R.error();
    }

    @Before(Tx.class)
    public R delete(JSONObject data) {
        Integer type = data.getInteger("type");
        Long time = data.getLong("time");
        Integer eventId = data.getInteger("eventId");
        String batchId = data.getString("batchId");
        if (type == 1){
            final OaEvent oaEvent = OaEvent.dao.findById(eventId);
            if (oaEvent.getRepetitionType().equals(RepetitionType.NO_REPETITION.getType())){
                oaEvent.delete();
            }else {
                OaEventUpdateRecord eventUpdateRecord = new OaEventUpdateRecord();
                eventUpdateRecord.setEventId(eventId);
                eventUpdateRecord.setTime(time);
                eventUpdateRecord.setStatus(type);
                eventUpdateRecord.setBatchId(batchId);
                eventUpdateRecord.save();
            }
        }else {
            List<Integer> eventIds = Db.template("oa.event.queryEventId", Kv.by("batchId",batchId)).query();
            Db.template("oa.event.deleteEventRelation", Kv.by("eventIds",eventIds)).delete();
            Db.template("oa.event.deleteEventByBatchId",Kv.by("batchId",batchId)).delete();
        }
        return R.ok();
    }

    public R queryById(JSONObject data) {
        Integer eventId = data.getInteger("eventId");
        long startTime = data.getLong("startTime");
        long endTime = data.getLong("endTime");
        OaEvent oaEvent = OaEvent.dao.template("oa.event.queryById",Kv.by("eventId",eventId)).findFirst();
        if (oaEvent == null){
            return R.error("日程已删除");
        }
        oaEvent.setStartTime(new Date(startTime));
        oaEvent.setEndTime(new Date(endTime));
        List<Record> ownerUserList = Db.template("admin.user.queryByIds", Kv.by("ids", TagUtil.toLongSet(oaEvent.getOwnerUserIds()))).find();
        oaEvent.put("ownerUserList",ownerUserList);
        List<OaEventNotice> noticeList = OaEventNotice.dao.template("oa.event.queryNoticeById", Kv.by("eventId",eventId)).find();
        oaEvent.put("noticeList",noticeList);
        Record record = Db.findFirst("select * from `72crm_oa_event_relation` where event_id = ?", eventId);
        List<CrmCustomer> customerList = new ArrayList<>();
        if (record != null){
            if(record.getStr("customer_ids") != null && ! record.getStr("customer_ids").isEmpty()){
                for(Integer customerId : TagUtil.toSet(record.getStr("customer_ids"))){
                    CrmCustomer crmCustomer = CrmCustomer.dao.findById(customerId);
                    if(crmCustomer != null){
                        customerList.add(crmCustomer);
                    }
                }
            }
            oaEvent.put("customerList", customerList);
            List<CrmContacts> contactsList = new ArrayList<>();
            if(record.getStr("contacts_ids") != null && ! record.getStr("contacts_ids").isEmpty()){
                for(Integer contactsId : TagUtil.toSet(record.getStr("contacts_ids"))){
                    CrmContacts crmContacts = CrmContacts.dao.findById(contactsId);
                    if(crmContacts != null){
                        contactsList.add(crmContacts);
                    }
                }
            }
            oaEvent.put("contactsList", contactsList);
            List<CrmBusiness> businessList = new ArrayList<>();
            if(record.getStr("business_ids") != null && ! record.getStr("business_ids").isEmpty()){
                for(Integer businessId : TagUtil.toSet(record.getStr("business_ids"))){
                    CrmBusiness crmBusiness = CrmBusiness.dao.findById(businessId);
                    if(crmBusiness != null){
                        businessList.add(crmBusiness);
                    }
                }
            }
            oaEvent.put("businessList", businessList);
            List<CrmContract> contractList = new ArrayList<>();
            if(record.getStr("contract_ids") != null && ! record.getStr("contract_ids").isEmpty()){
                for(Integer contractId : TagUtil.toSet(record.getStr("contract_ids"))){
                    CrmContract crmContract = CrmContract.dao.findById(contractId);
                    if(crmContract != null){
                        contractList.add(crmContract);
                    }
                }
            }
            oaEvent.put("contractList", contractList);
        }
        return R.ok().put("data",oaEvent);
    }

    public R queryListStatus(JSONObject data) {
        List<OaEventDTO> recordList = queryList(data);
        Set<String> dateList = new HashSet<>();
        recordList.forEach(eventDTO -> {
            Long startTime = eventDTO.getStartTime();
            Long endTime = eventDTO.getEndTime();
            Set<String> collect = DateUtil.rangeToList(DateUtil.date(startTime), DateUtil.date(endTime), DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).collect(Collectors.toSet());
            dateList.addAll(collect);
        });
        long startTime = data.getLong("startTime");
        long endTime = data.getLong("endTime");
        long nowDate = DateUtil.beginOfDay(DateUtil.date()).getTime();
        if (nowDate > startTime) {
            startTime = nowDate;
        }
        Long userId = data.getLong("userId");
        if (userId == null) {
            userId = BaseUtil.getUserId();
        }
        List<String> types = Db.template("oa.calendar.queryUserType",Kv.by("userId",userId)).query();
        if (types.contains("3")){
            AdminConfig adminConfig = AdminConfig.dao.findFirst("select * from 72crm_admin_config where name = 'expiringContractDays'");
            if (ObjectUtil.isNotEmpty(adminConfig) && adminConfig.getStatus() == 1) {
                List<String> endContractTimeList = Db.template("oa.calendar.endContract", Kv.by("userId", userId).set("startTime", new Date(startTime)).set("endTime", new Date(endTime)).set("expiringDay", adminConfig.getValue())).query();
                dateList.addAll(endContractTimeList);
            }
        }
        if (types.contains("2")) {
            List<String> customerTimeList = Db.template("oa.calendar.eventCustomer", Kv.by("userId", userId).set("startTime", startTime).set("endTime", endTime)).query();
            dateList.addAll(customerTimeList);
        }
        if (types.contains("4")) {
            List<String> receiveContractTimeList = Db.template("oa.calendar.receiveContract", Kv.by("userId", userId).set("startTime", new Date(startTime)).set("endTime", new Date(endTime))).query();
            dateList.addAll(receiveContractTimeList);
        }
        if (types.contains("1")) {
            List<Record> taskList = Aop.get(OaCalendarService.class).eventTask(data);
            taskList.forEach(task -> {
                Set<String> collect = DateUtil.rangeToList(DateUtil.date(task.getLong("startTime")), DateUtil.date(task.getLong("endTime")), DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).collect(Collectors.toSet());
                dateList.addAll(collect);
            });
        }
        return R.ok().put("data",dateList);
    }


    /**
     * 添加修改重新推算日程提醒
     */
    private void eventNoticeUpdate(OaEvent oaEvent,Integer oldEventId){
        Db.template("oa.event.deleteNoticeMessage",Kv.by("eventId",oldEventId)).delete();
        eventNotice(CollUtil.toList(oaEvent));
    }


    /**
     * 定时器日程提醒
     */
    public void eventNoticeCron(){
        DateTime nowDate = DateUtil.date();
        long startTime = DateUtil.beginOfDay(nowDate).getTime();
        long endTime = DateUtil.endOfDay(nowDate).getTime();
        List<OaEvent> oaEventList = OaEvent.dao.template("oa.event.queryList",Kv.by("startTime",startTime).set("endTime",endTime)).find();
        eventNotice(oaEventList);
    }


    private void eventNotice(List<OaEvent> oaEventList){
        DateTime nowDate = DateUtil.date();
        long startTime = DateUtil.beginOfDay(nowDate).getTime();
        long endTime = DateUtil.endOfDay(nowDate.offsetNew(DateField.DAY_OF_YEAR, 30)).getTime();
        List<OaEventDTO> recordList = queryList(oaEventList, startTime, endTime);
        AdminMessageEnum messageEnum = AdminMessageEnum.OA_EVENT_MESSAGE;
        for (OaEventDTO event : recordList) {
            Integer eventId = event.getEventId();
            List<OaEventNotice> noticeList = OaEventNotice.dao.template("oa.event.queryNoticeById", Kv.by("eventId",eventId)).find();
            if (CollUtil.isEmpty(noticeList)){
                continue;
            }
            Long eventStartTime = event.getStartTime();
            String title = event.getTitle();
            String ownerUserIds = event.getOwnerUserIds();
            List<AdminMessage> messageList = new ArrayList<>();
            for (OaEventNotice notice : noticeList) {
                Integer type = notice.getType();
                Integer value = notice.getValue();
                long millis = 0L;
                switch (type){
                    case 1:
                        millis = DateUnit.MINUTE.getMillis()*value;break;
                    case 2:
                        millis = DateUnit.HOUR.getMillis()*value;break;
                    case 3:
                        millis = DateUnit.DAY.getMillis()*value;break;
                    default:break;
                }
                Date createTime = new Date(eventStartTime-millis);
                if (DateUtil.isSameDay(createTime,nowDate)){
                    //TODO 添加日程提醒
                    for (Long userId : TagUtil.toLongSet(ownerUserIds)) {
                        AdminMessage adminMessage = new AdminMessage();
                        adminMessage.setCreateTime(createTime);
                        adminMessage.setCreateUser(event.getCreateUserId());
                        adminMessage.setType(messageEnum.getType());
                        adminMessage.setLabel(messageEnum.getLabel());
                        adminMessage.setTitle(title);
                        adminMessage.setRecipientUser(userId);
                        adminMessage.setTypeId(eventId);
                        adminMessage.setContent(JSON.toJSONString(Kv.by("startTime",event.getStartTime()).set("endTime",event.getEndTime()).set("type",type).set("value",value)));
                        messageList.add(adminMessage);
                    }
                }
            }
            Db.batchSave(messageList,20);
        }
    }


    public void saveSysCalendarType(Integer typeId){
        List<Long> userIds = Db.query("select user_id from `72crm_admin_user`");
        List<OaCalendarTypeUser> oaCalendarTypeUsers = new ArrayList<>();
        userIds.forEach(userId->{
            OaCalendarTypeUser oaCalendarTypeUser = new OaCalendarTypeUser();
            oaCalendarTypeUser.setTypeId(typeId);
            oaCalendarTypeUser.setUserId(userId);
            oaCalendarTypeUsers.add(oaCalendarTypeUser);
        });
        Db.batchSave(oaCalendarTypeUsers,50);
    }
}
