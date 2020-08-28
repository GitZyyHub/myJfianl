package com.kakarote.crm9.erp.admin.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;

import java.util.Date;

public class AdminLogService {
    public Page<Record> queryLoginLogList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Date startTime = jsonObject.getDate("startTime");
        Date endTime = jsonObject.getDate("endTime");
        String ids = jsonObject.getString("userIds");
        return Db.template("admin.log.queryLoginLogList", Kv.by("startTime",startTime).set("endTime",endTime).set("userIds",ids)).paginate(basePageRequest.getPage(),basePageRequest.getLimit());
    }

    public Page<Record> querySystemLogList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Date startTime = jsonObject.getDate("startTime");
        Date endTime = jsonObject.getDate("endTime");
        String ids = jsonObject.getString("userIds");
        String types = jsonObject.getString("types");
        String behavior = jsonObject.getString("behavior");
        return Db.template("admin.log.querySystemLogList", Kv.by("startTime", startTime).set("endTime", endTime).set("userIds", ids).set("types", types).set("behavior", behavior)).paginate(basePageRequest.getPage(), basePageRequest.getLimit());
    }

    public Page<Record> queryActionRecordList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Date startTime = jsonObject.getDate("startTime");
        Date endTime = jsonObject.getDate("endTime");
        String ids = jsonObject.getString("userIds");
        String types = jsonObject.getString("types");
        String behavior = jsonObject.getString("behavior");
        Page<Record> page = Db.template("admin.log.queryActionRecordList", Kv.by("startTime", startTime).set("endTime", endTime).set("userIds", ids).set("types", types).set("behavior", behavior)).paginate(basePageRequest.getPage(), basePageRequest.getLimit());
        page.getList().forEach(record -> {
            record.set("types", CrmEnum.parse(record.getInt("types")).getRemarks());
            if (ObjectUtil.isNotEmpty(record.getInt("behavior"))) {
                record.set("behavior", BehaviorEnum.parse(record.getInt("behavior")).getName());
            }
        });
        return page;
    }
}
