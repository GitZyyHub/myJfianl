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
public class WorkTaskHistoricalService {


    public R queryPageList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv by = Kv.by("taskId", jsonObject.get("taskId"));
        if(jsonObject.get("excel") != null){
            List<Record> records = Db.find(Db.getSqlPara("work.task.historical.queryPageList", by));
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("list",records);
            return  R.ok().put("data",jsonObject1);
        }
        Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("work.task.historical.queryPageList", by));
        return R.ok().put("data",paginate);
    }
}
