package com.kakarote.crm9.erp.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.AdminModelSort;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * @author wyq
 */
public class AdminConfigService {

    public List<AdminModelSort> queryHeaderModelSort() {
        return AdminModelSort.dao.find("select * from 72crm_admin_model_sort where type = 1 and user_id = ? order by sort", BaseUtil.getUserId());
    }

    public R setHeaderModelSort(JSONArray jsonArray) {
        List<AdminModelSort> list = jsonArray.toJavaList(AdminModelSort.class);
        list.forEach(adminModelSort -> adminModelSort.setType(1));
        Db.delete("delete from 72crm_admin_model_sort where user_id = ? and type = 1", BaseUtil.getUserId());
        Db.batchSave(list, 100);
        return R.ok();
    }

    public String firstModel() {
        return Db.queryStr("select model from 72crm_admin_model_sort where type = 2 order by sort limit 1");
    }
}
