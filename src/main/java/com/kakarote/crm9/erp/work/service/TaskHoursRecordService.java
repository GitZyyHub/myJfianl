package com.kakarote.crm9.erp.work.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * @author zyy
 */
public class TaskHoursRecordService {

    public R queryPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Object taskId = jsonObject.get("taskId");
        Object workId = jsonObject.get("workId");
        Kv kv = Kv.by("taskId", taskId).set("workId", workId);
        if (jsonObject.get("excel") != null) {
            List<Record> records = Db.find(Db.getSqlPara("task.hours.queryHoursRecord",kv));
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("list", records);
            return R.ok().put("data", resultJsonObject);
        } else {
            Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("task.hours.queryHoursRecord",kv));
            return R.ok().put("data", paginate);
        }
    }
}
