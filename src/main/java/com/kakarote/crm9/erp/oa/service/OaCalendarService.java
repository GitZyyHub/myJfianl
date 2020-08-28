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
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.common.AdminModelEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.crm.service.CrmContractService;
import com.kakarote.crm9.erp.crm.service.CrmCustomerService;
import com.kakarote.crm9.erp.oa.entity.OaCalendarType;
import com.kakarote.crm9.erp.oa.entity.OaCalendarTypeUser;
import com.kakarote.crm9.utils.AdminLogUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OaCalendarService {

    @Inject
    private AdminSceneService adminSceneService;

    @Inject
    private CrmCustomerService crmCustomerService;

    @Inject
    private AdminUserService adminUserService;

    @Inject
    private CrmContractService crmContractService;

    public R addOrUpdateType(OaCalendarType oaCalendarType) {
        boolean bol;
        if (oaCalendarType.getTypeId() == null) {
            oaCalendarType.setCreateTime(new Date());
            oaCalendarType.setCreateUserId(BaseUtil.getUserId());
            oaCalendarType.setType(2);
            bol = oaCalendarType.save();
            Aop.get(OaEventService.class).saveSysCalendarType(oaCalendarType.getTypeId());
        } else {
            oaCalendarType.setUpdateTime(new Date());
            bol = oaCalendarType.update();
        }
        return bol ? R.ok() : R.error();
    }


    @Before(Tx.class)
    public R deleteType(Integer typeId) {
        List<Integer> eventIds = Db.query("select event_id from `72crm_oa_event` where type_id = ?", typeId);
        Db.template("oa.calendar.deleteEventRecordByEventId", Kv.by("eventIds", eventIds)).delete();
        Db.delete("delete  from `72crm_oa_event` where type_id = ?", typeId);
        return OaCalendarType.dao.deleteById(typeId) ? R.ok() : R.error();
    }

    public R queryTypeList() {
        List<OaCalendarType> oaCalendarTypeList = OaCalendarType.dao.template("oa.calendar.queryTypeList").find();
        return R.ok().put("data", oaCalendarTypeList);
    }

    @Before(Tx.class)
    public R updateTypeUser(JSONObject jsonObject) {
        List<Integer> typeIds = jsonObject.getJSONArray("typeIds").toJavaList(Integer.class);
        Long userId = jsonObject.getLong("userId");
        if (userId == null) {
            userId = BaseUtil.getUserId();
        }
        if (typeIds == null || typeIds.size() == 0){
            return R.error("请至少选中一个类型");
        }
        AdminUser user = BaseUtil.getUser();
        Db.delete("delete from `72crm_oa_calendar_type_user` where user_id = ?", userId);
        List<OaCalendarTypeUser> oaCalendarTypeUserList = new ArrayList<>();
        for (Integer typeId : typeIds) {
            OaCalendarTypeUser oaCalendarTypeUser = new OaCalendarTypeUser();
            oaCalendarTypeUser.setUserId(userId);
            oaCalendarTypeUser.setTypeId(typeId);
            oaCalendarTypeUserList.add(oaCalendarTypeUser);
        }
        Db.batchSave(oaCalendarTypeUserList, 20);
        return R.ok();
    }

    public R queryTypeListByUser(Long userId) {
        if (userId == null) {
            userId = BaseUtil.getUserId();
        }
        List<OaCalendarType> oaCalendarTypeList = OaCalendarType.dao.findAll();
        List<Integer> typeIds = Db.template("oa.calendar.queryTypeListByUser", Kv.by("userId", userId)).query();
        if (typeIds == null || typeIds.size() == 0){
            List<OaCalendarTypeUser> typeUserList = new ArrayList<>();
            oaCalendarTypeList.forEach(type->{
                Integer typeId = type.getTypeId();
                OaCalendarTypeUser typeUser = new OaCalendarTypeUser();
                typeUser.setUserId(BaseUtil.getUserId());
                typeUser.setTypeId(typeId);
                typeUserList.add(typeUser);
            });
            Db.batchSave(typeUserList,20);
            oaCalendarTypeList = oaCalendarTypeList.stream().peek(type -> {
                    type.put("select", true);
            }).collect(Collectors.toList());
        }else {
            oaCalendarTypeList = oaCalendarTypeList.stream().peek(type -> {
                if (typeIds.contains(type.getTypeId())) {
                    type.put("select", true);
                } else {
                    type.put("select", false);
                }
            }).collect(Collectors.toList());
        }

        return R.ok().put("data", oaCalendarTypeList);
    }


    public List<Record> eventTask(JSONObject data) {
        Long userId = data.getLong("userId");
        long startTime = data.getLong("startTime");
        long endTime = data.getLong("endTime");
        if (userId == null) {
            userId = BaseUtil.getUserId();
        }
        return Db.template("oa.calendar.eventTask", Kv.by("userId", userId).set("startTime", startTime).set("endTime", endTime)).find();
    }

    public R eventCrm(JSONObject data) {
        Long userId = data.getLong("userId");
        long startTime = data.getLong("startTime");
        long endTime = data.getLong("endTime");
        long nowDate = DateUtil.beginOfDay(DateUtil.date()).getTime();
        if (nowDate > startTime) {
            startTime = nowDate;
        }
        if (userId == null) {
            userId = BaseUtil.getUserId();
        }
        Kv result = Kv.create();
        AdminConfig adminConfig = AdminConfig.dao.findFirst("select * from 72crm_admin_config where name = 'expiringContractDays'");
        if (adminConfig.getStatus() == 0 || ObjectUtil.isNull(adminConfig)) {
            result.set("endContractTimeList", new ArrayList<>());
        } else {
            List<String> endContractTimeList = Db.template("oa.calendar.endContract", Kv.by("userId", userId).set("startTime", new Date(startTime)).set("endTime", new Date(endTime)).set("expiringDay", adminConfig.getValue())).query();
            result.set("endContractTimeList", endContractTimeList);
        }
        List<String> customerTimeList = Db.template("oa.calendar.eventCustomer", Kv.by("userId", userId).set("startTime", startTime).set("endTime", endTime)).query();
        List<String> receiveContractTimeList = Db.template("oa.calendar.receiveContract", Kv.by("userId", userId).set("startTime", new Date(startTime)).set("endTime", new Date(endTime))).query();
        result.set("customerTimeList", customerTimeList).set("receiveContractTimeList", receiveContractTimeList);
        return R.ok().put("data", result);
    }

    public R eventContract(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Long userId = jsonObject.getLong("userId");
        Long time = jsonObject.getLong("time");
        Integer type = jsonObject.getInteger("type");
        if (userId == null) {
            userId = BaseUtil.getUserId();
        }
        List<Integer> contractIds;
        List<Record> records = null;
        if (type == 1) {
            AdminConfig adminConfig = AdminConfig.dao.findFirst("select * from 72crm_admin_config where name = 'expiringContractDays'");
            if (adminConfig.getStatus() == 0 || ObjectUtil.isNull(adminConfig)) {
                contractIds = new ArrayList<>();
            } else {
                contractIds = Db.template("oa.calendar.endContractList", Kv.by("userId", userId).set("time", new Date(time)).set("expiringDay", adminConfig.getValue())).query();
            }
        } else {
            records = Db.template("oa.calendar.receiveContractList", Kv.by("userId", userId).set("time", new Date(time))).find();
            contractIds = records.stream().map(record -> record.getInt("contract_id")).collect(Collectors.toList());
        }

        if (contractIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("calendar", new JSONObject().fluentPut("name", "contract_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", contractIds)));
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        if (type == 2){
            JSONObject json = (JSONObject) crmContractService.queryPageList(basePageRequest, false).get("data");
            JSONArray list = json.getJSONArray("list");
            JSONArray listArray = new JSONArray();
            for (Record record : records) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject jsonObject1 = list.getJSONObject(i).getJSONObject("columns");
                    if (jsonObject1.getInteger("contract_id").equals(record.getInt("contract_id"))){
                        jsonObject1.putAll(record.getColumns());
                        listArray.add(jsonObject1);
                    }
                }
            }
            json.put("list",listArray);
            return R.ok().put("data",json);
        }else {
            return crmContractService.queryPageList(basePageRequest, false);
        }
    }

    public R eventCustomer(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Long userId = jsonObject.getLong("userId");
        Long time = jsonObject.getLong("time");
        if (userId == null) {
            userId = BaseUtil.getUserId();
        }
        List<Integer> customerIds = Db.template("oa.calendar.eventCustomerList", Kv.by("userId", userId).set("time",new Date(time))).query();
        if (customerIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("event", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", customerIds)));
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        return crmCustomerService.queryPageList(basePageRequest, false);
    }
}
