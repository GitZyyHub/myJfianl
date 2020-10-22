package com.kakarote.crm9.erp.crm.service;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * crm模块操作记录
 *
 * @author hmb
 */
public class CrmRecordService<T extends Model> {

    public R queryRecordList(String actionId, String crmTypes) {
        List<Record> recordList = Db.find(Db.getSql("crm.record.queryRecordList"), actionId, crmTypes);
        recordList.forEach(record -> {
            List<String> list = JSON.parseArray(record.getStr("content"), String.class);
            record.set("content", list);
        });
        return R.ok().put("data", recordList);
    }


    /**
     * @author wyq
     * 查询跟进记录类型
     */
    public R queryRecordOptions() {
        List<String> list = Db.query("select value from 72crm_admin_config where name = 'followRecordOption'");
        return R.ok().put("data", list);
    }

    /**
     * @author wyq
     * 设置跟进记录类型
     */
    @Before(Tx.class)
    public R setRecordOptions(List<String> list) {
        Db.delete("delete from 72crm_admin_config where name = 'followRecordOption'");
        for (String str : list) {
            AdminConfig adminConfig = new AdminConfig();
            adminConfig.setName("followRecordOption");
            adminConfig.setValue(str);
            adminConfig.setDescription("跟进记录选项");
            adminConfig.save();
        }
        return R.ok();
    }

    public void deleteActionRecord(CrmEnum crmEnum, String... ids) {
        Kv kv = Kv.by("type", crmEnum.getType()).set("ids", ids);
        Db.template("crm.record.deleteRecord", kv).delete();
    }

}
