package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmReturnVisit;
import com.kakarote.crm9.erp.crm.entity.CrmReturnVisitData;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.ToolUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wyq
 */
public class CrmReturnVisitService extends AbstractBasePageList {
    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        CrmEnum crmEnum = CrmEnum.CRM_RETURN_VISIT;
        Kv kv = getKv(basePageRequest, crmEnum, isDataAuth);
        if (kv.containsKey("error")) {
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.returnVisit.queryReturnVisitPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.returnVisit.queryReturnVisitPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        resultJsonObject.put("list", recordPage);
        Integer count = Db.template("crm.returnVisit.queryReturnVisitPageListCount", kv).queryInt();
        resultJsonObject.put("totalRow", count);
        return R.ok().put("data", resultJsonObject);
    }

    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and visit_number like '%{search}%'";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }

    public R addOrUpdate(JSONObject jsonObject) {
        CrmReturnVisit crmReturnVisit = jsonObject.getObject("entity", CrmReturnVisit.class);
        String batchId = StrUtil.isNotEmpty(crmReturnVisit.getBatchId()) ? crmReturnVisit.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(jsonObject.getJSONArray("field"), Kv.by("batchId", batchId).set("dataTableName", "72crm_crm_return_visit_data"));
        save(jsonObject.getJSONArray("field"), batchId);
        if (crmReturnVisit.getVisitId() != null) {
            crmReturnVisit.setUpdateTime(DateUtil.date());
            actionRecordUtil.updateRecord(new CrmReturnVisit().dao().findById(crmReturnVisit.getVisitId()), crmReturnVisit, CrmEnum.CRM_RETURN_VISIT);
            return crmReturnVisit.update() ? R.ok() : R.error();
        } else {
            AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 17");
            if (adminConfig.getStatus() == 1 && StrUtil.isEmpty(crmReturnVisit.getVisitNumber())) {
                String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                crmReturnVisit.setVisitNumber(result);
            }
            crmReturnVisit.setCreateTime(DateUtil.date());
            crmReturnVisit.setUpdateTime(DateUtil.date());
            crmReturnVisit.setCreateUserId(BaseUtil.getUserId());
            if (crmReturnVisit.getOwnerUserId() == null) {
                crmReturnVisit.setOwnerUserId(BaseUtil.getUserId());
            }
            crmReturnVisit.setBatchId(batchId);
            boolean save = crmReturnVisit.save();
            actionRecordUtil.addRecord(crmReturnVisit.getVisitId(), CrmEnum.CRM_RETURN_VISIT);
            return save ? R.ok() : R.error();
        }
    }

    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_return_visit_data", "batch_id", batchId);
        List<CrmReturnVisitData> Fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            CrmReturnVisitData fieldv = TypeUtils.castToJavaBean(obj, CrmReturnVisitData.class);
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            Fieldvs.add(fieldv);
        });
        Db.batchSave(Fieldvs, 100);
        return true;
    }

    public Record queryById(Integer visitId) {
        Record record = Db.findFirst(Db.getSql("crm.returnVisit.queryById"), visitId);
        List<Record> recordList = Db.find("select name,value from `72crm_crm_return_visit_data` where batch_id = ?", record.getStr("batch_id"));
        recordList.forEach(field -> record.set(field.getStr("name"), field.getStr("value")));
        return record;
    }

    /**
     * 基本信息
     */
    public List<Record> information(Integer visitId) {
        Record record = queryById(visitId);
        List<String> keyList = Arrays.asList("visit_number", "visit_time");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间", "更新时间");
        List<Record> recordList = adminFieldService.queryInformation(CrmEnum.CRM_RETURN_VISIT, record, keyList);
        recordList.add(new Record().set("name", "回访人").set("value", new Record().set("user_id", record.getInt("user_id")).set("realname", record.getStr("owner_user_name"))).set("formType", "single_user").set("field_type", 1));
        recordList.add(new Record().set("name", "客户名称").set("value", new Record().set("customer_id", record.getInt("customer_id")).set("customer_name", record.getStr("customer_name"))).set("formType", "customer").set("field_type", 1));
        recordList.add(new Record().set("name", "联系人").set("value", new Record().set("contacts_id", record.getInt("contacts_id")).set("contacts_name", record.getStr("contacts_name"))).set("formType", "contacts").set("field_type", 1));
        recordList.add(new Record().set("name", "合同编号").set("value", new Record().set("contract_id", record.getInt("contract_id")).set("contract_num", record.getStr("contract_num"))).set("formType", "contract").set("field_type", 1));
        return recordList.stream().sorted(Comparator.comparingInt(r -> -r.getInt("field_type"))).peek(r -> {
            r.remove("field_type", "setting", "type");
            if (systemFieldList.contains(r.getStr("name"))) {
                r.set("sysInformation", 1);
            } else {
                r.set("sysInformation", 0);
            }
        }).collect(Collectors.toList());
    }

    /**
     * @author wyq
     * 查询编辑字段
     */
    public List<Record> queryField(Integer visitId) {
        Record visit = queryById(visitId);
        List<Record> list = new ArrayList<>();
        list.add(new Record().set("customer_id", visit.getInt("customer_id")).set("customer_name", visit.getStr("customer_name")));
        visit.set("customer_id", list);
        list = new ArrayList<>();
        if (visit.getStr("contract_id") != null && visit.getInt("contract_id") != 0) {
            list.add(new Record().set("contract_id", visit.getInt("contract_id")).set("num", visit.getStr("contract_num")));
        }
        visit.set("contract_id", list);
        list = new ArrayList<>();
        if (visit.getStr("contacts_id") != null && visit.getInt("contacts_id") != 0) {
            list.add(new Record().set("contacts_id", visit.getInt("contacts_id")).set("name", visit.getStr("contacts_name")));
        }
        visit.set("contacts_id", list);
        return adminFieldService.queryUpdateField(CrmEnum.CRM_RETURN_VISIT.getType(), visit);
    }

    /**
     * @author wyq
     * 附件列表
     */
    public R queryFileList(Integer visitId) {
        SqlPara sqlPara = Db.getSqlPara("crm.returnVisit.queryFileList", Kv.by("visitId", visitId));
        return R.ok().put("data", Db.find(sqlPara));
    }

    /**
     * @author wyq
     * 根据id 删除客户回访
     */
    @Before(Tx.class)
    public R deleteByIds(String visitIds) {
        String[] idsList = StrUtil.split(visitIds, ",");
        Kv kv = Kv.by("ids", idsList);
        List<String> batchList = Db.template("crm.returnVisit.queryReturnVisitBatchId", kv).query();
        //删除自定义字段
        Db.template("admin.field.deleteFieldv", Kv.by("batchList", batchList).set("dataTableName", "72crm_crm_return_visit_data")).delete();
        //删除文件
        Aop.get(AdminFileService.class).removeByBatchId(batchList.toArray(new String[0]));
        Db.template("crm.returnVisit.deleteByIds", kv).delete();
        return R.ok();
    }

    public R queryReturnVisitRemindConfig() {
        Record record = Db.findFirst("select * from 72crm_admin_config where name = 'returnVisitRemindConfig'");
        return R.ok().put("data", record);
    }

    public R updateReturnVisitRemindConfig(AdminConfig adminConfig) {
        Db.update("update 72crm_admin_config set status = ?,value = ? where name = 'returnVisitRemindConfig'", adminConfig.getStatus(), adminConfig.getValue());
        return R.ok();
    }
}
