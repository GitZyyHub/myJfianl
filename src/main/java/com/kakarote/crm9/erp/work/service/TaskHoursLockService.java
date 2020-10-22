package com.kakarote.crm9.erp.work.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.work.entity.TaskHoursLock;
import com.kakarote.crm9.utils.R;

import java.util.Date;
import java.util.List;

/**
 * @author zyy
 */
public class TaskHoursLockService {


    public R queryPageList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        if (jsonObject.get("excel") != null) {
            List<Record> records = Db.find(Db.getSqlPara("work.task.queryTaskHoursLockPageList"));
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("list", records);
            return R.ok().put("data", resultJsonObject);
        } else {
            Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("work.task.queryTaskHoursLockPageList"));
            return R.ok().put("data", paginate);
        }
    }

    public R setHoursLock(JSONObject jsonObject) {
        TaskHoursLock entity = jsonObject.getObject("entity", TaskHoursLock.class);
        if (entity.getId() == null) {
            entity.setCreateTime(new Date());
            entity.save();
        } else {
            entity.update();
        }
        return R.ok();
    }


}
