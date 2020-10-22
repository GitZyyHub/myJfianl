package com.kakarote.crm9.erp.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.oa.entity.OaLogFieldType;
import com.kakarote.crm9.erp.oa.service.OaLogFieldService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zyy
 */
public class OaLogFieldController extends Controller {

    @Inject
    OaLogFieldService oaLogFieldTypeService;


    public void findList(){
        List<Record> records = Db.find(Db.getSql("oa.logFieldType.find"));
        List<List<Record>> objects = new ArrayList<>();
        records.forEach(record -> {
            List<Record> fields = Db.find(Db.getSqlPara("oa.logField.findByTypeId", Kv.by("typeId", record.getStr("field_type_id"))));
            objects.add(fields);
            record.set("data",objects);
        });
        renderJson(records);
    }

    /**
     * 根据日志类型id查询日志类型详情
     * @param
     */
    public void findListByTypeId(){
        Integer typeId = getInt("typeId");
        List<Record> records = Db.find(Db.getSqlPara("oa.logField.findByTypeId", Kv.by("typeId",typeId)));
        renderJson(records);
    }

    /**
     * 更新日志字段
     * @param
     */
    public void updateLogField(){
        String rawData = getRawData();
        List<OaLogFieldType> oaLogFieldTypes = JSONObject.parseArray(rawData, OaLogFieldType.class);
        renderJson(oaLogFieldTypeService.updateLogField(oaLogFieldTypes));
    }


}
