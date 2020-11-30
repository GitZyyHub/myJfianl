package com.kakarote.crm9.erp.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
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
import com.kakarote.crm9.common.config.cache.CaffeineCache;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.admin.entity.AdminField;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.entity.AdminFieldStyle;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.crm.entity.CrmCustomerPoolFieldSort;
import com.kakarote.crm9.erp.crm.entity.CrmCustomerPoolFieldStyle;
import com.kakarote.crm9.erp.crm.entity.CrmCustomerPoolRule;
import com.kakarote.crm9.erp.work.entity.Milestone;
import com.kakarote.crm9.erp.work.entity.TaskHours;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.*;
import com.kakarote.crm9.utils.*;
import com.mysql.jdbc.StringUtils;
import org.apache.poi.util.Internal;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class AdminFieldService {

    @Inject
    WorkService workService;

    @Inject
    MilestoneService milestoneService;

    @Inject
    TaskService taskService;

    @Inject
    WorkDemandService workDemandService;

    @Inject
    WorkProductService workProductService;

    @Inject
    WorkProductFuntionService workProductFuntionService;

    @Inject
    TaskHoursService taskHoursServicel;

    @Inject
    ConfigManageService configManageService;

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;

    /**
     * 查询字段详情
     *
     * @param fieldType
     * @param id
     * @return
     */
    public List<Record> getFields(Integer fieldType, Integer id) {
        List<Record> records = Db.find(Db.getSql("admin.field.queryField"), fieldType);
        List<Record> fields = new ArrayList<>();
        CrmEnum crmEnum = CrmEnum.parse(fieldType);
        records.forEach(record -> {
            String fieldName = record.getStr("field_name");
            if (crmEnum == CrmEnum.WORK || crmEnum == CrmEnum.TASK || crmEnum == CrmEnum.MILESTONE || crmEnum == CrmEnum.SUBTASKS || crmEnum == CrmEnum.PRODUCT) {
                if(fieldName.equals("work_num") || fieldName.equals("task_num") || fieldName.equals("milestone_num") || fieldName.equals("product_number")){
                    Integer status = Db.queryInt("select status from 72crm_admin_config where name = 'numberSetting' and value = ?", crmEnum.getType());
                    if (status == 1) {
                        if (record.getInt("field_type") == 1) {
                            record.set("autoGeneNumber", 1);
                        }
                    }
                }
            }
            String field_name = ToolUtils.lineToHump(record.getStr("field_name"));
            record.set("field_name", field_name);
            FormTypeEnum type = FormTypeEnum.parse(record.getInt("type"));
            record.set("formType", type.getFormType());
            String options = record.getStr("options");
            if (!StringUtils.isNullOrEmpty(options)) {
                String[] split = options.split(",");
                record.set("setting", split);
            }
            record.set("is_unique", 0);
            fields.add(record);
        });
        List<Record> list = new ArrayList<>();
        if (id != null) {
            Record record = null;
            if (id != null) {
                if (CrmEnum.WORK.equals(crmEnum)) {
                    record = workService.queryField(crmEnum, id);
                } else if (CrmEnum.MILESTONE.equals(crmEnum)) {
                    record = milestoneService.queryById(id);
                } else if (CrmEnum.TASK.equals(crmEnum)) {
                    record = taskService.queryById(id);
                } else if (CrmEnum.SUBTASKS.equals(crmEnum)) {
                    record = taskService.queryById(id);
                } else if (CrmEnum.DEMAND.equals(crmEnum)) {
                    record = workDemandService.queryById(id);
                } else if (CrmEnum.PRODUCT.equals(crmEnum)) {
                    record = workProductService.queryById(id);
                } else if (CrmEnum.PRODUCT_FUNCTIONE.equals(crmEnum)) {
                    record = workProductFuntionService.queryById(id);
                } else if (CrmEnum.TASK_HOURS.equals(crmEnum)) {
                    record = taskHoursServicel.queryById(id);
                    if (record.getInt("task_id") == record.getInt("subtasks_id")) {
                        record.remove("subtasks_id");
                    }
                } else if (CrmEnum.WORKCONFIG.equals(crmEnum)) {
                    record = configManageService.queryById(id);
                }
            }

            for (Record field : fields) {
                if ("workPhase".equals(field.getStr("field_name"))) {
                    field.set("authLevel", 2);
                } else {
                    field.set("authLevel", 3);
                }
                field.set("sysInformation", 0);

                try {
                    if (field.getInt("type") == FormTypeEnum.SINGLE_USER.getType()) {
                        field.set("value",
                                Db.find("select user_id,realname from 72crm_admin_user where user_id=?",
                                        record.getStr(ToolUtils.humpToLine(field.getStr("field_name")))));
                    }/*else if(field.getInt("type") == FormTypeEnum.USER.getType()){
                        String field_name = record.getStr(ToolUtils.humpToLine(field.getStr("field_name")));
                        field.set("value",
                                Db.find("select user_id,realname from 72crm_admin_user where user_id in ("+
                                        record.getStr(ToolUtils.humpToLine(field.getStr("field_name")))+")"));
                    }*/ else if (field.getInt("type") == FormTypeEnum.STRUCTURE.getType()) {
                        field.set("value",
                                Db.find("SELECT dept_id,`name` FROM `72crm_admin_dept` WHERE dept_id=?",
                                        record.getStr(ToolUtils.humpToLine(field.getStr("field_name")))));
                    } else if (field.getInt("type") == FormTypeEnum.CHECKBOX.getType()) {
                        String field_name = record.getStr(ToolUtils.humpToLine(field.getStr("field_name")));
                        field.set("value", field_name.split(","));
                    } else if (field.getInt("type") == FormTypeEnum.CUSTOMER.getType()) {
                        field.set("value",
                                Db.find("SELECT customer_id,`customer_name` FROM `72crm_crm_customer` WHERE customer_id=?",
                                        record.getStr(ToolUtils.humpToLine(field.getStr("field_name")))));
                    } else if (field.getInt("type") == FormTypeEnum.TASK.getType()) {
                        field.set("value",
                                Db.find("SELECT task_id,task_name FROM 72crm_task WHERE task_id =?",
                                        record.getStr(ToolUtils.humpToLine(field.getStr("field_name")))));
                    } else if (field.getInt("type") == FormTypeEnum.SUBTASK.getType()) {
                        field.set("value",
                                Db.find("SELECT task_id,task_name FROM 72crm_task WHERE task_id =?",
                                        record.getStr(ToolUtils.humpToLine(field.getStr("field_name")))));
                    } else if (field.getInt("type") == FormTypeEnum.MILESTONE.getType()) {
                        String field_name = ToolUtils.humpToLine(field.getStr("field_name"));
                        field.set("value",
                                Db.find("SELECT milestone_id,milestone_name FROM 72crm_milestone WHERE milestone_id =?",
                                        record.getStr(field_name)));
                    } else if (field.getInt("type") == FormTypeEnum.WORK.getType()) {
                        field.set("value",
                                Db.find("SELECT work_id,work_name from 72crm_work WHERE work_id=?",
                                        record.getStr(ToolUtils.humpToLine(field.getStr("field_name")))));
                    } else {
                        //String field_name = record.get(ToolUtils.humpToLine(field.getStr("field_name")));
                        field.set("value", record.get(ToolUtils.humpToLine(field.getStr("field_name"))));
                    }

                    //自动编号开启，对应字段增加标识

                } catch (Exception e) {
                    field.set("value", "查询出错");
                }
                if (!"createUserId".equals(field.getStr("field_name"))) {
                    list.add(field);
                }
            }
        }
        if (list.size() > 0) {
            return list;
        }
        return fields;
    }

    /**
     * 详情页面修改对应字段内容
     *
     * @param jsonObject
     * @return
     */
    @Before(Tx.class)
    public R updateInformation(JSONObject jsonObject) {

        Integer id = jsonObject.getInteger("id");
        Record firsts = Db.findFirst(Db.getSql("work.queryWorkById"),id);
        if ("结项".equals(firsts.getStr("work_phase"))) {
            return R.error("当前项目阶段为结项，如果需要修改信息请先修改项目阶段！");
        }
        String batchId = jsonObject.getString("batchId");
        Integer label = jsonObject.getInteger("label");
        JSONArray list = jsonObject.getJSONArray("list");
        CrmEnum parse = CrmEnum.parse(label);
        Record record = new Record();
        record.set(parse.getTableId(), id);
        for (int i = 0; i < list.size(); i++) {
            JSONObject field = list.getJSONObject(i);
            String fieldName = ToolUtils.humpToLine(field.getString("fieldName"));
            Object value = field.get("value");
            record.set(fieldName, value);
        }
        Record first = Db.findFirst(Db.getSql("work.queryWorkById"), id);
        Db.update(parse.getTableName(), parse.getTableId(), record);
        Record after = Db.findFirst(Db.getSql("work.queryWorkById"), id);
        Work work = Aop.get(Work.class).findById(id);
        workHistoricalRecordService.add(work.getWorkId(), WorkRecordEnum.WORK_DETAILS, BehaviorEnum.UPDATE, work.getWorkNum(), after, first, work.getWorkId());
        return R.ok();
    }

    /**
     * 补充详情页面数据
     *
     * @param fields
     * @param id
     * @return
     */
    public List<Record> findFieldDetails(CrmEnum crmEnum, List<Record> fields, Integer id) {
        List<String> sysInformationName = Arrays.asList("创建时间", "创建人", "更新时间");
        List<String> sysInformationFieldName = Arrays.asList("create_time", "create_user_id", "update_time");
        //Record sysInformation = Db.findFirst(Db.getSql("work.queryWorkSysInformationById"), id);
        Record sysInformation = Db.findFirst("SELECT DATE_FORMAT(t.create_time,'%Y-%m-%d %H:%i:%S') create_time," +
                "(select realname from 72crm_admin_user where user_id = t.create_user_id)create_user_id,DATE_FORMAT(t.update_time,'%Y-%m-%d %H:%i:%S') update_time FROM " + crmEnum.getTableName() + " t WHERE " + crmEnum.getTableId() + "=" + id);
        for (int i = 0; i < sysInformationFieldName.size(); i++) {
            Record record = new Record();
            record.set("authLevel", 2).set("sysInformation", 1).set("fieldName", sysInformationFieldName.get(i))
                    .set("name", sysInformationName.get(i)).set("value", sysInformation.getStr(sysInformationFieldName.get(i)))
                    .set("formType", "text").set("isNull", 0).set("isUnique", 0).set("remark", null).set("inputTips", null).set("defaultValue", null)
                    .set("label", CrmEnum.WORK.getType()).set("type", 1).set("sorting", 0).set("options", null).set("fieldType", 1);
            fields.add(record);
        }
        return fields;
    }


    private static Map<Integer, List<String>> fieldColumnNameProperties = new HashMap<>();

    static {
        fieldColumnNameProperties.put(CrmEnum.CRM_LEADS.getType(), Arrays.asList("leads_id", "is_transform", "followup", "leads_name", "customer_id", "next_time", "telephone", "mobile", "address", "remark", "create_user_id", "owner_user_id", "create_time", "update_time", "batch_id", "last_time", "last_content"));
        fieldColumnNameProperties.put(CrmEnum.CRM_CUSTOMER.getType(), Arrays.asList("customer_id", "customer_name", "followup", "is_lock", "next_time", "deal_status", "deal_time", "contacts_id", "mobile", "telephone", "website", "remark", "create_user_id", "owner_user_id", "ro_user_id", "rw_user_id", "address", "location", "detail_address", "lng", "lat", "create_time", "update_time", "batch_id", "status", "last_time", "pool_time", "is_receive", "last_content", "receive_time"));
        fieldColumnNameProperties.put(CrmEnum.CRM_CONTACTS.getType(), Arrays.asList("contacts_id", "name", "next_time", "mobile", "telephone", "email", "post", "customer_id", "address", "remark", "create_user_id", "owner_user_id", "create_time", "update_time", "batch_id", "last_time"));
        fieldColumnNameProperties.put(CrmEnum.CRM_BUSINESS.getType(), Arrays.asList("business_id", "type_id", "status_id", "next_time", "customer_id", "contacts_id", "deal_date", "business_name", "money", "discount_rate", "total_price", "remark", "create_user_id", "owner_user_id", "create_time", "update_time", "batch_id", "ro_user_id", "rw_user_id", "is_end", "status_remark", "status", "last_time"));
        fieldColumnNameProperties.put(CrmEnum.CRM_CONTRACT.getType(), Arrays.asList("contract_id", "name", "customer_id", "business_id", "check_status", "examine_record_id", "order_date", "create_user_id", "owner_user_id", "create_time", "update_time", "num", "start_time", "end_time", "money", "discount_rate", "total_price", "types", "payment_type", "batch_id", "ro_user_id", "rw_user_id", "contacts_id", "remark", "company_user_id", "last_time", "received_money", "unreceived_money"));
        fieldColumnNameProperties.put(CrmEnum.CRM_RECEIVABLES.getType(), Arrays.asList("receivables_id", "number", "plan_id", "customer_id", "contract_id", "check_status", "examine_record_id", "return_time", "return_type", "money", "remark", "create_user_id", "owner_user_id", "create_time", "update_time", "remarks", "batch_id"));
        fieldColumnNameProperties.put(CrmEnum.CRM_PRODUCT.getType(), Arrays.asList("product_id", "name", "num", "unit", "price", "status", "category_id", "description", "create_user_id", "owner_user_id", "create_time", "update_time", "batch_id"));
    }

    /**
     * @author wyq
     * 查询新增字段列表
     */
    public List<Record> queryAddField(CrmEnum crmEnum) {
        List<Record> fieldList = Db.find(Db.getSql("admin.field.queryAddField"), crmEnum.getType());
        recordToFormType(fieldList);
        if (crmEnum == CrmEnum.CRM_CUSTOMER) {
            Record map = new Record();
            fieldList.add(map.set("field_name", "map_address").set("name", "地区定位").set("form_type", "map_address").set("is_null", 0).set("field_type", 1));
        } else if (crmEnum == CrmEnum.CRM_BUSINESS) {
            Optional<Record> recordOptional = fieldList.stream().filter(record -> "remark".equals(record.getStr("field_name"))).findFirst();
            recordOptional.ifPresent(fieldList::remove);
            fieldList.add(new Record().set("field_name", "type_id").set("name", "商机状态组").set("value", "").set("form_type", "business_type").set("setting", new String[0]).set("is_null", 1).set("field_type", FormTypeEnum.TEXT.getType()));
            fieldList.add(new Record().set("field_name", "status_id").set("name", "商机阶段").set("value", "").set("form_type", "business_status").set("setting", new String[0]).set("is_null", 1).set("field_type", FormTypeEnum.TEXT.getType()));
            recordOptional.ifPresent(fieldList::add);
        } else if (crmEnum == CrmEnum.CRM_PRODUCT) {
            fieldList.forEach(record -> {
                if ("category".equals(record.getStr("field_name"))) {
                    record.set("value", new String[0]);
                }
            });
        } else if (crmEnum == CrmEnum.CRM_RETURN_VISIT) {
            fieldList.forEach(record -> {
                if ("owner_user_id".equals(record.getStr("field_name"))) {
                    List<Record> userList = new ArrayList<>();
                    userList.add(new Record().set("user_id", BaseUtil.getUserId()).set("realname", BaseUtil.getUser().getRealname()));
                    record.set("default_value", userList);
                }
            });
        }
        if (crmEnum == CrmEnum.CRM_BUSINESS || crmEnum == CrmEnum.CRM_CONTRACT) {
            Record record = new Record();
            fieldList.add(record.set("field_name", "product").set("name", "产品").set("value", Kv.by("discount_rate", "").set("product", new ArrayList<>()).set("total_price", "")).set("formType", "product").set("setting", new String[]{}).set("is_null", 0).set("field_type", FormTypeEnum.TEXT.getType()));
        }
        //自动编号开启，对应字段增加标识
        if (crmEnum == CrmEnum.CRM_RECEIVABLES || crmEnum == CrmEnum.CRM_CONTRACT || crmEnum == CrmEnum.CRM_RETURN_VISIT) {
            Integer status = Db.queryInt("select status from 72crm_admin_config where name = 'numberSetting' and value = ?", crmEnum.getType());
            if (status == 1) {
                fieldList.forEach(field -> {
                    String fieldName = field.getStr("field_name");
                    boolean b = fieldName.equals("num") || fieldName.equals("number") || fieldName.equals("visit_number");
                    if (b && field.getInt("field_type") == 1) {
                        field.set("autoGeneNumber", 1);
                    }
                });
            }
        }
        return fieldList;
    }

    /**
     * @author wyq
     * 查询编辑字段列表
     */
    public List<Record> queryUpdateField(Integer label, Record record) {
        CrmEnum crmEnum = CrmEnum.parse(label);
        List<Record> recordList = Db.find(Db.getSql("admin.field.queryAddField"), label);
        recordList.forEach(field -> {
            if (field.getInt("type") == FormTypeEnum.FILE.getType()) {
                field.set("value", Db.find("select file_id, name, size, create_time, file_path, file_type, batch_id from 72crm_admin_file where batch_id = ?", StrUtil.isNotEmpty(record.get(field.getStr("field_name"))) ? record.get(field.getStr("field_name")) : ""));
            } else if (field.getInt("type") == FormTypeEnum.USER.getType() || field.getInt("type") == FormTypeEnum.SINGLE_USER.getType()) {
                field.set("value", Db.find("select user_id,realname from 72crm_admin_user where find_in_set(user_id,ifnull(?,0))", record.getStr(field.getStr("field_name"))));
            } else if (field.getInt("type") == FormTypeEnum.STRUCTURE.getType()) {
                field.set("value", Db.find("select dept_id,name from 72crm_admin_dept where find_in_set(dept_id,ifnull(?,0))", record.getStr(field.getStr("field_name"))));
            } else if (field.getInt("field_type") == 2) {
                field.set("value", record.get(field.getStr("name")) != null ? record.get(field.getStr("name")) : "");
            } else {
                field.set("value", record.get(field.getStr("field_name")) != null ? record.get(field.getStr("field_name")) : "");
            }

            //自动编号开启，对应字段增加标识
            if (crmEnum == CrmEnum.CRM_RECEIVABLES || crmEnum == CrmEnum.CRM_CONTRACT || crmEnum == CrmEnum.CRM_RETURN_VISIT) {
                Integer status = Db.queryInt("select status from 72crm_admin_config where name = 'numberSetting' and value = ?", crmEnum.getType());
                if (status == 1) {
                    String fieldName = field.getStr("field_name");
                    boolean b = fieldName.equals("num") || fieldName.equals("number") || fieldName.equals("visit_number");
                    if (b && field.getInt("field_type") == 1) {
                        field.set("autoGeneNumber", 1);
                    }
                }
            }
        });
        recordToFormType(recordList);
        return recordList;
    }

    /**
     * 查询详情页基本信息
     *
     * @param crmEnum
     * @param record
     * @param fieldKey
     */
    public List<Record> queryInformation(CrmEnum crmEnum, Record record, List<String> fieldKey) {
        List<Record> recordList = Db.find(Db.getSql("admin.field.queryInformationField"), crmEnum.getType());
        Map<Integer, List<Record>> fieldTypeMap = recordList.stream().collect(Collectors.groupingBy(filed -> filed.getInt("field_type")));
        List<Record> resultList = new ArrayList<>();
        fieldTypeMap.forEach((type, list) -> {
            if (type == 1) {
                list.forEach(field -> {
                    String fieldName = field.getStr("field_name");
                    if (fieldKey.contains(fieldName)) {
                        field.set("value", record.get(fieldName) != null ? record.get(fieldName) : "");
                        resultList.add(field);
                    }
                });
            } else {
                list.forEach(field -> {
                    if (field.getInt("type") == FormTypeEnum.FILE.getType()) {
                        field.set("value", Db.find("select file_id, name, size, create_time, file_path, file_type, batch_id from 72crm_admin_file where batch_id = ?", StrUtil.isNotEmpty(record.get(field.getStr("field_name"))) ? record.get(field.getStr("field_name")) : ""));
                    } else if (field.getInt("type") == FormTypeEnum.USER.getType()) {
                        field.set("value", Db.find("select user_id,realname from 72crm_admin_user where find_in_set(user_id,ifnull(?,0))", record.getStr(field.getStr("field_name"))));
                    } else if (field.getInt("type") == FormTypeEnum.STRUCTURE.getType()) {
                        field.set("value", Db.find("select dept_id,name from 72crm_admin_dept where find_in_set(dept_id,ifnull(?,0))", record.getStr(field.getStr("field_name"))));
                    } else {
                        field.set("value", record.get(field.getStr("name")) != null ? record.get(field.getStr("name")) : "");
                    }
                    resultList.add(field);
                });
            }
        });
        recordToFormType(resultList);
        if (!crmEnum.equals(CrmEnum.CRM_RETURN_VISIT)) {
            resultList.add(new Record().set("name", "负责人").set("value", Db.find("select user_id,realname from 72crm_admin_user where user_id = ?", record.getStr("owner_user_id"))).set("formType", FormTypeEnum.USER.getFormType()).set("field_type", 1));
        }
        if (crmEnum.equals(CrmEnum.CRM_CUSTOMER)) {
            resultList.add(new Record().set("name", "前负责人").set("value", Db.find("select user_id,realname from 72crm_admin_user where user_id = ?", record.getStr("pre_owner_user_id"))).set("formType", FormTypeEnum.USER.getFormType()).set("field_type", 1));
            resultList.add(new Record().set("name", "负责人获取客户时间").set("value", record.getDate("receive_time")).set("formType", FormTypeEnum.DATETIME.getFormType()).set("field_type", 1));
        }
        if (crmEnum.equals(CrmEnum.CRM_CUSTOMER) || crmEnum.equals(CrmEnum.CRM_LEADS)) {
            resultList.add(new Record().set("name", "最后跟进记录").set("value", record.getStr("last_content")).set("formType", FormTypeEnum.TEXTAREA.getFormType()).set("field_type", 1));
        }
        resultList.add(new Record().set("name", "创建人").set("value", Db.find("select user_id,realname from 72crm_admin_user where user_id = ?", record.getStr("create_user_id"))).set("formType", FormTypeEnum.USER.getFormType()).set("field_type", 1));
        resultList.add(new Record().set("name", "创建时间").set("value", record.getDate("create_time")).set("formType", FormTypeEnum.DATETIME.getFormType()).set("field_type", 1));
        resultList.add(new Record().set("name", "更新时间").set("value", record.getDate("update_time")).set("formType", FormTypeEnum.DATETIME.getFormType()).set("field_type", 1));
        if (!crmEnum.equals(CrmEnum.CRM_PRODUCT) && !crmEnum.equals(CrmEnum.CRM_RECEIVABLES) && !crmEnum.equals(CrmEnum.CRM_RETURN_VISIT)) {
            resultList.add(new Record().set("name", "最后跟进时间").set("value", record.getDate("last_time")).set("formType", FormTypeEnum.DATETIME.getFormType()).set("field_type", 1));
        }
        return resultList;
    }

    /**
     * author zhangzhiwei
     * 保存自定义字段信息
     *
     * @param jsonObject 详见接口文档
     * @return R
     */
    public R save(JSONObject jsonObject) {
        List<String> errorList = new ArrayList<>();
        Db.tx(() -> {
            JSONArray adminFields = jsonObject.getJSONArray("data");
            Map<String, List<AdminField>> collect = adminFields.stream().map(adminField -> TypeUtils.castToJavaBean(adminField, AdminField.class)).collect(Collectors.groupingBy(AdminField::getName));
            for (Map.Entry<String, List<AdminField>> entry : collect.entrySet()) {
                if (entry.getValue().size() > 1) {
                    errorList.add("自定义表单名称不能重复！");
                    return false;
                }
            }
            Integer label = jsonObject.getInteger("label");
            if (label != 10) {
                List<String> fieldNameList = adminFields.stream()
                        .map(object -> TypeUtils.castToJavaBean(object, AdminField.class))
                        .filter(adminField -> adminField.getFieldType() == 0)
                        .map(AdminField::getName)
                        .collect(Collectors.toList());
                Collection<String> intersection = CollUtil.intersection(fieldColumnNameProperties.get(label), fieldNameList);
                if (CollUtil.isNotEmpty(intersection)) {
                    errorList.add("[" + CollUtil.join(intersection, ".") + "]自定义表单名称与系统字段重复，请使用其他字段！");
                    return false;
                }
            }
            Integer categoryId = jsonObject.getInteger("categoryId");
            if (categoryId != null && Db.queryInt("select ifnull(is_sys,0) from 72crm_oa_examine_category where category_id = ?", categoryId) == 1) {
                errorList.add("系统审批类型暂不支持编辑");
                return false;
            }
            List<Integer> arr = new ArrayList<>();
            adminFields.forEach(object -> {
                AdminField field = TypeUtils.castToJavaBean(object, AdminField.class);
                if (field.getFieldId() != null) {
                    arr.add(field.getFieldId());
                }
            });
            List<String> nameList = Db.query("select field_name from 72crm_admin_field where label = ?", label);
            if (arr.size() > 0) {
                Kv kv = Kv.by("ids", arr).set("label", label).set("categoryId", categoryId);
                if (label == 10) {
                    kv.set("dataTableName", "72crm_oa_examine_data");
                } else {
                    kv.set("dataTableName", CrmEnum.parse(label).getDataTableName());
                }
                CrmEnum.parse(label);
                SqlPara sql = Db.getSqlPara("admin.field.deleteByChooseId", Kv.by("ids", arr).set("label", label).set("categoryId", categoryId));
                SqlPara sqlPara = Db.getSqlPara("admin.field.deleteByFieldValue", kv);
                Db.delete(sqlPara.getSql(), sqlPara.getPara());
                Db.delete(sql.getSql(), sql.getPara());
            }
            List<String> fieldList = new ArrayList<>();
            for (int i = 0; i < adminFields.size(); i++) {
                adminFields.getJSONObject(i).remove("value");
                Object defaultValue = adminFields.getJSONObject(i).get("defaultValue");
                if (defaultValue instanceof JSONArray && ((JSONArray) defaultValue).size() == 0) {
                    adminFields.getJSONObject(i).remove("defaultValue");
                }
                AdminField entity = TypeUtils.castToJavaBean(adminFields.get(i), AdminField.class);
                entity.setUpdateTime(DateUtil.date());
                //只有fieldType为0 才需要name和fieldName同步
                if (entity.getFieldType() == null || entity.getFieldType() == 0) {
                    entity.setFieldName(entity.getName());
                }
                if (label == 10) {
                    entity.setExamineCategoryId(jsonObject.getInteger("categoryId"));
                }
                entity.setSorting(i);
                entity.set("label", label);
                if (entity.getFieldId() != null) {
                    entity.update();
                    //只有原始字段才会有关联字段 比如客户名称
                    if (entity.getFieldType() == 1) {
                        updateRelateFieldName(entity);
                    }
                    if (label == 10) {
                        Db.update("update 72crm_oa_examine_data set name = ? where field_id = ?", entity.getName(), entity.getFieldId());
                    } else {
                        Db.template("admin.field.updateDataName", Kv.by("tableName", CrmEnum.parse(entity.getLabel()).getDataTableName()).set("name", entity.getName()).set("fieldId", entity.getFieldId())).update();
                    }
                    Db.update(Db.getSqlPara("admin.field.updateFieldSort", entity));
                    //客户字段更新会影响公海字段设置,同步更新
                    if (label.equals(CrmEnum.CRM_CUSTOMER.getType())) {
                        Db.update(Db.getSqlPara("admin.field.updatePoolFieldSetting", entity));
                    }
                } else {
                    entity.save();
                }

                //编辑客户级别选项时 需要在公海设置里去掉被删除选项的设置
                if (label.equals(CrmEnum.CRM_CUSTOMER.getType()) && entity.getFieldName().equals("客户级别") && entity.getFieldType().equals(2)) {
                    List<String> optionList = Arrays.stream(entity.getOptions().split(",")).collect(Collectors.toList());
                    List<CrmCustomerPoolRule> ruleList = CrmCustomerPoolRule.dao.find("select * from 72crm_crm_customer_pool_rule where customer_level_setting = 2");
                    ruleList.forEach(rule -> {
                        if (!optionList.contains(rule.getLevel())) {
                            rule.delete();
                        }
                    });
                }

                //关于非隐藏字段的判断
                if (entity.getIsHidden().equals(1)) {
                    String[] fieldNameArr;
                    if (entity.getLabel().equals(CrmEnum.CRM_LEADS.getType())) {
                        fieldNameArr = new String[]{"leads_id", "leads_name", "客户级别"};
                    } else if (entity.getLabel().equals(CrmEnum.CRM_CUSTOMER.getType())) {
                        fieldNameArr = new String[]{"customer_name", "客户级别"};
                    } else if (entity.getLabel().equals(CrmEnum.CRM_CONTACTS.getType())) {
                        fieldNameArr = new String[]{"customer_id", "name"};
                    } else if (entity.getLabel().equals(CrmEnum.CRM_PRODUCT.getType())) {
                        fieldNameArr = new String[]{"name", "category_id", "price", "是否上下架"};
                    } else if (entity.getLabel().equals(CrmEnum.CRM_BUSINESS.getType())) {
                        fieldNameArr = new String[]{"business_name", "contract_id"};
                    } else if (entity.getLabel().equals(CrmEnum.CRM_CONTRACT.getType())) {
                        fieldNameArr = new String[]{"customer_id", "business_id", "num", "money", "order_date"};
                    } else if (entity.getLabel().equals(CrmEnum.CRM_RECEIVABLES.getType())) {
                        fieldNameArr = new String[]{"customer_id", "contract_id", "number", "plan_id"};
                    } else if (entity.getLabel().equals(CrmEnum.CRM_RETURN_VISIT.getType())) {
                        fieldNameArr = new String[]{"customer_id", "contract_id"};
                    } else {
                        break;
                    }
                    List<String> keyList = Arrays.asList(fieldNameArr);
                    if (keyList.contains(entity.getFieldName())) {
                        errorList.add("系统关联字段不能隐藏");
                        return false;
                    }
                    if (entity.getIsNull() == 1) {
                        errorList.add("必填选项不能隐藏");
                        return false;
                    }
                } else {
                    fieldList.add(entity.getFieldName());
                }
            }

            //用之前的字段减去现在显示的字段，剩余的是需要从列表页表头中删除的字段
            nameList.removeAll(fieldList);
            if (nameList.size() != 0) {
                List<String> underLineNameList = nameList.stream().map(StrUtil::toCamelCase).collect(Collectors.toList());
                Db.update(Db.getSqlPara("admin.field.deleteFieldSort", Kv.by("label", label).set("names", underLineNameList)));
                Db.update(Db.getSqlPara("admin.field.deleteCustomerPoolFieldSetting", Kv.by("names", underLineNameList)));
                Db.update(Db.getSqlPara("admin.field.deleteCustomerPoolField", Kv.by("names", underLineNameList)));
            }
            CaffeineCache.ME.removeAll("field:");
            return true;
        });
        if (CollectionUtil.isNotEmpty(errorList)) {
            return R.error(errorList.get(0));
        }
        return R.ok();
    }

    /**
     * 更新关联字段名称
     *
     * @param adminField
     */
    private void updateRelateFieldName(AdminField adminField) {
        String fieldName = adminField.getFieldName();
        Kv kv = Kv.by("name", adminField.getName());
        switch (fieldName) {
            case "customer_id":
            case "customer_name":
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "customer_name").set("label", CrmEnum.CRM_CUSTOMER.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "customer_id").set("label", CrmEnum.CRM_CONTACTS.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "customer_id").set("label", CrmEnum.CRM_BUSINESS.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "customer_id").set("label", CrmEnum.CRM_CONTRACT.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "customer_id").set("label", CrmEnum.CRM_RECEIVABLES.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "customer_id").set("label", CrmEnum.CRM_RECEIVABLES_PLAN.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "customer_id").set("label", CrmEnum.CRM_RETURN_VISIT.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "customerName").set("label", CrmEnum.CRM_CUSTOMER.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "customerName").set("label", CrmEnum.CRM_CONTACTS.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "customerName").set("label", CrmEnum.CRM_BUSINESS.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "customerName").set("label", CrmEnum.CRM_CONTRACT.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "customerName").set("label", CrmEnum.CRM_RECEIVABLES.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "customerName").set("label", CrmEnum.CRM_RECEIVABLES_PLAN.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "customerName").set("label", CrmEnum.CRM_RETURN_VISIT.getType())));
                break;
            case "business_id":
            case "business_name":
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "business_name").set("label", CrmEnum.CRM_BUSINESS.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "business_id").set("label", CrmEnum.CRM_CONTRACT.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "businessName").set("label", CrmEnum.CRM_BUSINESS.getType())));
                Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "businessName").set("label", CrmEnum.CRM_CONTRACT.getType())));
                break;
            case "contract_id":
            case "num":
                if (adminField.getLabel() != CrmEnum.CRM_PRODUCT.getType()) {
                    Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "num").set("label", CrmEnum.CRM_CONTRACT.getType())));
                    Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "contract_id").set("label", CrmEnum.CRM_RECEIVABLES.getType())));
                    Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "contract_id").set("label", CrmEnum.CRM_RECEIVABLES_PLAN.getType())));
                    Db.update(Db.getSqlPara("admin.field.updateRelateFieldName", kv.set("fieldName", "contract_id").set("label", CrmEnum.CRM_RETURN_VISIT.getType())));
                    Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "num").set("label", CrmEnum.CRM_CONTRACT.getType())));
                    Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "contactsName").set("label", CrmEnum.CRM_RECEIVABLES.getType())));
                    Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "contactsName").set("label", CrmEnum.CRM_RECEIVABLES_PLAN.getType())));
                    Db.update(Db.getSqlPara("admin.field.updateRelateSortFieldName", kv.set("fieldName", "contactsName").set("label", CrmEnum.CRM_RETURN_VISIT.getType())));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 查询唯一字段
     *
     * @param crmEnum 字段类型
     * @param record  参数
     * @return 数据库存在的数量
     * @author zhangzhiwei
     */
    public Integer verify(CrmEnum crmEnum, Record record) {
        //TODO 空值不参与验证
        if (StrUtil.isEmpty(record.getStr("value"))) {
            return 0;
        }
        if (record.get("field_type").equals(1)) {
            return Db.template("admin.field.queryFixedIsExist", record.set("tableName", crmEnum.getName()).getColumns()).queryInt();
        } else {
            return Db.template("admin.field.queryFieldIsExist", record.set("dataTableName", crmEnum.getDataTableName()).getColumns()).queryInt();
        }
    }

    public R queryFields() {
        List<Record> records = Db.find(Db.getSqlPara("admin.field.queryFields"));
        return R.ok().put("data", records);
    }

    public void recordToFormType(List<Record> recordList) {
        for (Record record : recordList) {
            Integer dataType = record.getInt("type");
            FormTypeEnum typeEnum = FormTypeEnum.parse(dataType);
            record.set("formType", typeEnum.getFormType());
            if (dataType == FormTypeEnum.CHECKBOX.getType()) {
                recordValueToArray(record);
            } else if (dataType == FormTypeEnum.USER.getType()) {
                record.set("default_value", new ArrayList<>(0));
            } else if (dataType == FormTypeEnum.STRUCTURE.getType()) {
                record.set("default_value", new ArrayList<>(0));
            }
            if (FormTypeEnum.SELECT.getType() == dataType || FormTypeEnum.CHECKBOX.getType() == dataType) {
                if (record.getStr("options") != null) {
                    record.set("setting", record.getStr("options").split(","));
                }
            } else {
                record.set("setting", new String[]{});
            }
        }
    }

    private void recordValueToArray(Record record) {
        record.set("default_value", StrUtil.isNotEmpty(record.getStr("default_value")) ? record.getStr("default_value").split(",") : new String[]{});
        record.set("value", StrUtil.isNotEmpty(record.getStr("value")) ? record.getStr("value").split(",") : new String[]{});
    }

    /**
     * @author wyq
     * 查询fieldType为0且不隐藏的字段
     */
    public List<Record> customFieldList(Integer label) {
        List<Record> recordList = Db.find(Db.getSql("admin.field.customerFieldList"), label);
        recordToFormType(recordList);
        return recordList;
    }

    public List<Record> list(Integer label, Integer isHidden) {
        return list(label, isHidden, null);
    }

    public List<Record> list(Integer label, Integer isHidden, String categoryId) {
        List<Record> recordList = Db.find(Db.getSqlPara("admin.field.list", Kv.by("label", label).set("isHidden", isHidden).set("categoryId", categoryId)));
        recordToFormType(recordList);
        if (categoryId == null) {
            return recordList;
        }
        FieldUtil fieldUtil = new FieldUtil(recordList);
        return fieldUtil.getRecordList();
    }

    public R setFelidStyle(Kv kv) {
        String types = kv.getStr("types");
        int type = CrmEnum.parse(types).getType();
        AdminFieldStyle adminFleldStyle = AdminFieldStyle.dao.findFirst(AdminFieldStyle.dao.getSql("admin.field.queryFieldStyle"), type, kv.getStr("field"), BaseUtil.getUser().getUserId());
        if (adminFleldStyle != null) {
            adminFleldStyle.setStyle(new BigDecimal(kv.getStr("width")).intValue());
            adminFleldStyle.update();
        } else {
            adminFleldStyle = new AdminFieldStyle();
            adminFleldStyle.setType(type);
            adminFleldStyle.setCreateTime(new Date());
            adminFleldStyle.setStyle(new BigDecimal(kv.getStr("width")).intValue());
            adminFleldStyle.setFieldName(kv.getStr("field"));
            adminFleldStyle.setUserId(BaseUtil.getUser().getUserId());
            adminFleldStyle.save();
        }
        return R.ok().put("data", "编辑成功");
    }

    public R setPoolFieldStyle(Kv kv) {
        Integer poolId = Integer.valueOf(kv.getStr("poolId"));
        CrmCustomerPoolFieldStyle fleldStyle = CrmCustomerPoolFieldStyle.dao.findFirst(AdminFieldStyle.dao.getSql("admin.field.queryPoolFieldStyle"), kv, kv.getStr("field"), BaseUtil.getUserId());
        if (fleldStyle != null) {
            fleldStyle.setStyle(new BigDecimal(kv.getStr("width")).intValue());
            fleldStyle.update();
        } else {
            fleldStyle = new CrmCustomerPoolFieldStyle();
            fleldStyle.setPoolId(poolId);
            fleldStyle.setCreateTime(new Date());
            fleldStyle.setStyle(new BigDecimal(kv.getStr("width")).intValue());
            fleldStyle.setFieldName(kv.getStr("field"));
            fleldStyle.setUserId(BaseUtil.getUserId());
            fleldStyle.save();
        }
        return R.ok().put("data", "编辑成功");
    }

    public List<AdminFieldStyle> queryFieldStyle(String type) {
        Long userId = BaseUtil.getUser().getUserId();
        return AdminFieldStyle.dao.find("select * from 72crm_admin_field_style where type = ? and user_id = ?", type, userId);
    }

    public List<AdminFieldStyle> queryPoolFieldStyle(Integer poolId) {
        return AdminFieldStyle.dao.find("select * from 72crm_crm_customer_pool_field_style where pool_id = ? and user_id = ?", poolId, BaseUtil.getUserId());
    }

    /**
     * @author wyq
     * 查询客户管理列表页字段
     */
    @Before(Tx.class)
    public List<Record> queryListHead(AdminFieldSort adminFieldSort) {
        //查看userid是否存在于顺序表，没有则插入
        Long userId = BaseUtil.getUser().getUserId();
        Integer number = Db.queryInt("select count(*) from 72crm_admin_field_sort where user_id = ? and label = ?", userId, adminFieldSort.getLabel());
        if (0 == number) {
            List<Record> fieldList = list(adminFieldSort.getLabel(), 0);
            List<AdminFieldSort> sortList = new LinkedList<>();
            FieldUtil fieldUtil = new FieldUtil(sortList, userId, adminFieldSort.getLabel());
            if (null != fieldList) {
                for (Record record : fieldList) {
                    if (CrmEnum.CRM_RETURN_VISIT.getType() == adminFieldSort.getLabel() && record.getStr("field_name").equals("owner_user_id")) {
                        fieldUtil.add("owner_user_name", record.getStr("name"), record.getInt("type"), record.getInt("field_id"));
                    } else {
                        fieldUtil.add(record.getStr("field_name"), record.getStr("name"), record.getInt("type"), record.getInt("field_id"));
                    }
                }
            }
            if (CrmEnum.CRM_CUSTOMER.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("status", "锁定状态", 5).add("dealStatus", "成交状态", 3).add("lastTime", "最后跟进时间", 4)
                        .add("lastContent", "最后跟进记录", FormTypeEnum.TEXT.getType())
                        .add("detailAddress", "详细地址", FormTypeEnum.TEXT.getType())
                        .add("address", "地区定位", FormTypeEnum.MAP_ADDRESS.getType())
                        .add("poolDay", "距进入公海天数", FormTypeEnum.NUMBER.getType());
            } else if (CrmEnum.CRM_BUSINESS.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("typeName", "商机状态组", 3).add("statusName", "商机阶段", 3).add("lastTime", "最后跟进时间", 4);
            } else if (CrmEnum.CRM_CONTRACT.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("checkStatus", "审核状态", 5).add("receivedMoney", "已收款金额", 6).add("unreceivedMoney", "未收款金额", 6).add("lastTime", "最后跟进时间", 4);
            } else if (CrmEnum.CRM_RECEIVABLES.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("checkStatus", "审核状态", 5).add("contractMoney", "合同金额", 6);
            } else if (CrmEnum.CRM_LEADS.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("lastTime", "最后跟进时间", 4)
                        .add("lastContent", "最后跟进记录", FormTypeEnum.TEXT.getType());
            } else if (CrmEnum.CRM_CONTACTS.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("lastTime", "最后跟进时间", 4);
            } else if (CrmEnum.CRM_CUSTOMER_POOL.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("lastContent", "最后跟进记录", FormTypeEnum.TEXT.getType());
            } else if (CrmEnum.TASK_HOURS.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("check_user_name", "审批人", FormTypeEnum.TEXT.getType())
                        .add("create_user_name", "创建人", FormTypeEnum.TEXT.getType())
                        .add("create_time", "发起时间", FormTypeEnum.DATE.getType())
                        .add("examine_time", "审批时间", FormTypeEnum.DATE.getType())
                        .add("check_status", "审核状态", FormTypeEnum.TEXT.getType());
            } else if (CrmEnum.SUBTASKS.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("task_num", "子任务编号", FormTypeEnum.TEXT.getType());
            } else if (CrmEnum.MILESTONE.getType() == adminFieldSort.getLabel()) {
                fieldUtil.add("updateTime", "更新时间", 4).add("createTime", "创建时间", 4);
            }


            if (CrmEnum.TASK.getType() != adminFieldSort.getLabel() && CrmEnum.CRM_RETURN_VISIT.getType() != adminFieldSort.getLabel()
                    && CrmEnum.DEMAND.getType() != adminFieldSort.getLabel() && CrmEnum.MILESTONE.getType() != adminFieldSort.getLabel()
                    && CrmEnum.TASK_HOURS.getType() != adminFieldSort.getLabel() && CrmEnum.WORK.getType() != adminFieldSort.getLabel()
                    && CrmEnum.WORKCONFIG.getType() != adminFieldSort.getLabel()&& CrmEnum.SUBTASKS.getType() != adminFieldSort.getLabel()
                    && CrmEnum.PRODUCT.getType() != adminFieldSort.getLabel()&& CrmEnum.PRODUCT_FUNCTIONE.getType() != adminFieldSort.getLabel()
            ) {
                fieldUtil.add("ownerUserName", "负责人", 1);
            }
            if (CrmEnum.DEMAND.getType() != adminFieldSort.getLabel()
                    && CrmEnum.TASK_HOURS.getType() != adminFieldSort.getLabel()
                    && CrmEnum.SUBTASKS.getType() != adminFieldSort.getLabel()
                    && CrmEnum.MILESTONE.getType() != adminFieldSort.getLabel()
            ) {
                fieldUtil.add("updateTime", "更新时间", 4).add("createTime", "创建时间", 4)
                        .add("createUserName", "创建人", 1);
            }
            fieldUtil.getAdminFieldSortList().forEach(fieldSort -> {
                String fieldName = StrUtil.toCamelCase(fieldSort.getFieldName());
                fieldSort.setFieldName(fieldName);
                if ("customerId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("customerName");
                } else if ("categoryId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("categoryName");
                } else if ("contactsId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("contactsName");
                } else if ("companyUserId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("companyUserName");
                } else if ("businessId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("businessName");
                } else if ("contractId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("contractNum");
                } else if ("planId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("planNum");
                } else if ("salesId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("salesName");
                } else if ("managerId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("managerName");
                } else if ("deptId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("deptName");
                } else if ("workId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("workName");
                } else if ("taskId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("taskName");
                } else if ("mainUserId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("mainUserName");
                } else if ("ownerUserId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("ownerUserName");
                } else if ("milestoneId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("milestoneName");
                } else if ("beforeId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("beforeName");
                } else if ("exploitUserId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("exploitUserName");
                } else if ("createUserId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("createUserName");
                } else if ("subtasksId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("subtasksName");
                } else if ("pid".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("pName");
                } else if ("associatedId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("associatedName");
                } else if ("exploitUserId".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("exploitUserName");
                } else if ("area".equals(fieldSort.getFieldName())) {
                    fieldSort.setFieldName("areaName");
                }

            });
            sortList = fieldUtil.getAdminFieldSortList();
            for (int i = 0; i < sortList.size(); i++) {
                AdminFieldSort newUserFieldSort = sortList.get(i);
                newUserFieldSort.setSort(i).save();
            }
        }
        List<Record> recordList = Db.findByCache("field:", "listHead:" + adminFieldSort.getLabel() + userId, Db.getSql("admin.field.queryListHead"), adminFieldSort.getLabel(), userId);
        recordToFormType(recordList);
        return recordList;
    }

    /**
     * 查询公海列表页字段
     */
    public List<Record> queryPoolListHead(Integer poolId) {
        Long userId = BaseUtil.getUserId();
        Integer number = Db.queryInt("select count(*) from 72crm_crm_customer_pool_field_sort where user_id = ? and pool_id = ?", userId, poolId);
        List<Record> fieldList;
        if (number.equals(0)) {
            fieldList = Db.find(Db.getSql("admin.field.queryDefaultPoolField"), poolId);
            for (int i = 0; i < fieldList.size(); i++) {
                Record record = fieldList.get(i);
                CrmCustomerPoolFieldSort fieldSort = new CrmCustomerPoolFieldSort()._setAttrs(record.getColumns());
                fieldSort.setUserId(userId).setSort(i).setIsHidden(0).save();
            }
        }
        fieldList = Db.findByCache("field:", "listHead:" + poolId + userId, Db.getSql("admin.field.queryPoolListHead"), poolId, userId);
        return fieldList;
    }

    /**
     * @author wyq
     * 查询字段排序隐藏设置
     */
    @Before(Tx.class)
    public R queryFieldConfig(AdminFieldSort adminFieldSort) {
        Long userId = BaseUtil.getUser().getUserId();
        //查出自定义字段，查看顺序表是否存在该字段，没有则插入，设为隐藏
        List<Record> fieldList = list(adminFieldSort.getLabel(), 0);
        for (Record record : fieldList) {
            String name = record.getStr("name");
            Integer number = Db.queryInt("select count(1) from 72crm_admin_field_sort where user_id = ? and label = ? and name = ?", userId, adminFieldSort.getLabel(), name);

            if (number.equals(0)) {
                AdminFieldSort newField = new AdminFieldSort();
                newField.setFieldName(record.getStr("field_name")).setType(record.getInt("type")).setName(name).setLabel(adminFieldSort.getLabel()).setIsHide(1).setUserId(userId).setSort(1);
                if ("customer_id".equals(newField.getFieldName())) {
                    newField.setFieldName("customerName");
                } else if ("category_id".equals(newField.getFieldName())) {
                    newField.setFieldName("categoryName");
                } else if ("contacts_id".equals(newField.getFieldName())) {
                    newField.setFieldName("contactsName");
                } else if ("company_user_id".equals(newField.getFieldName())) {
                    newField.setFieldName("companyUserName");
                } else if ("business_id".equals(newField.getFieldName())) {
                    newField.setFieldName("businessName");
                } else if ("contract_id".equals(newField.getFieldName())) {
                    newField.setFieldName("contractNum");
                } else if ("plan_id".equals(newField.getFieldName())) {
                    newField.setFieldName("planNum");
                }
                newField.save();
            }
        }
        List<Record> noHideList = Db.find(Db.getSql("admin.field.queryFieldConfig"), 0, adminFieldSort.getLabel(), userId);
        List<Record> hideList = Db.find(Db.getSql("admin.field.queryFieldConfig"), 1, adminFieldSort.getLabel(), userId);
        Integer poolConfig = Db.queryInt("select count(1) from 72crm_crm_customer_pool where status = 1");
        return R.ok().put("data", Kv.by("value", noHideList).set("hide_value", hideList).set("poolConfig", poolConfig > 1 ? 0 : 1));
    }

    /**
     * @author wyq
     * 查询公海字段排序隐藏设置
     */
    @Before(Tx.class)
    public R queryPoolFieldConfig(CrmCustomerPoolFieldSort fieldSort) {
        Long userId = BaseUtil.getUser().getUserId();
        //查出自定义字段，查看顺序表是否存在该字段，没有则插入，设为隐藏
        List<Record> fieldList = Db.find("select * from 72crm_crm_customer_pool_field_setting where pool_id = ? and is_hidden = 0", fieldSort.getPoolId());
        for (Record record : fieldList) {
            String fieldName = record.getStr("field_name");
            Integer number = Db.queryInt("select count(1) from 72crm_crm_customer_pool_field_sort where user_id = ? and pool_id = ? and field_name = ?", userId, fieldSort.getPoolId(), fieldName);
            if (number.equals(0)) {
                CrmCustomerPoolFieldSort newField = new CrmCustomerPoolFieldSort();
                newField.setFieldName(fieldName).setType(record.getInt("type")).setName(record.getStr("name")).setPoolId(fieldSort.getPoolId()).setIsHidden(1).setUserId(userId).setSort(1);
                newField.save();
            }
        }
        List<Record> noHideList = Db.find(Db.getSql("admin.field.queryPoolFieldConfig"), 0, fieldSort.getPoolId(), userId);
        List<Record> hideList = Db.find(Db.getSql("admin.field.queryPoolFieldConfig"), 1, fieldSort.getPoolId(), userId);
        return R.ok().put("data", Kv.by("value", noHideList).set("hide_value", hideList));
    }

    /**
     * @author wyq
     * 客户管理列表页字段是否隐藏
     */
    @Before(Tx.class)
    public R fieldConfig(AdminFieldSort adminFieldSort) {
        Long userId = BaseUtil.getUser().getUserId();
        String[] sortArr = adminFieldSort.getNoHideIds().split(",");
        if (sortArr.length < 2) {
            return R.error("至少显示2列");
        }
        for (int i = 0; i < sortArr.length; i++) {
            Db.update(Db.getSql("admin.field.sort"), i + 1, adminFieldSort.getLabel(), userId, sortArr[i]);
        }
        if (null != adminFieldSort.getHideIds()) {
            String[] hideIdsArr = adminFieldSort.getHideIds().split(",");
            Db.update(Db.getSqlPara("admin.field.isHide", Kv.by("ids", hideIdsArr).set("label", adminFieldSort.getLabel()).set("userId", userId)));
        }
        CaffeineCache.ME.remove("field:", "listHead:" + adminFieldSort.getLabel() + userId);
        return R.ok();
    }

    /**
     * @author wyq
     * 公海列表页字段排序隐藏设置
     */
    @Before(Tx.class)
    public R poolFieldConfig(CrmCustomerPoolFieldSort fieldSort) {
        Long userId = BaseUtil.getUser().getUserId();
        String[] sortArr = fieldSort.getNoHideIds().split(",");
        if (sortArr.length < 2) {
            return R.error("至少显示2列");
        }
        for (int i = 0; i < sortArr.length; i++) {
            Db.update(Db.getSql("admin.field.poolSort"), i + 1, fieldSort.getPoolId(), userId, sortArr[i]);
        }
        if (null != fieldSort.getHideIds()) {
            String[] hideIdsArr = fieldSort.getHideIds().split(",");
            Db.update(Db.getSqlPara("admin.field.poolIsHide", Kv.by("ids", hideIdsArr).set("poolId", fieldSort.getPoolId()).set("userId", userId)));
        }
        CaffeineCache.ME.remove("field:", "listHead:" + fieldSort.getPoolId() + userId);
        return R.ok();
    }


    /**
     * 自定义字段人员和部门转换
     *
     * @param recordList 自定义字段列表
     * @param isDetail   1 value返回name字符串 2 value返回id数组字符串
     * @author hmb
     */
    public void transferFieldList(List<Record> recordList, Integer isDetail) {
        recordList.forEach(record -> {
            Integer dataType = record.getInt("type");
            if (isDetail == 2) {
                if (FormTypeEnum.USER.getType() == dataType) {
                    if (StrUtil.isNotEmpty(record.getStr("value"))) {
                        record.set("value", TagUtil.toLongSet(record.getStr("value")));
                    }
                } else if (FormTypeEnum.STRUCTURE.getType() == dataType) {
                    if (StrUtil.isNotEmpty(record.getStr("value"))) {
                        record.set("value", TagUtil.toSet(record.getStr("value")));
                    }
                }
            } else {
                if (FormTypeEnum.USER.getType() == dataType) {
                    if (StrUtil.isNotEmpty(record.getStr("value"))) {
                        record.set("value", Db.queryStr("select group_concat(realname) from `72crm_admin_user` where user_id in (" + record.getStr("value") + ")"));
                    }
                } else if (FormTypeEnum.STRUCTURE.getType() == dataType) {
                    if (StrUtil.isNotEmpty(record.getStr("value"))) {
                        record.set("value", Db.queryStr("select group_concat(name) from `72crm_admin_dept` where dept_id in (" + record.getStr("value") + ")"));
                    }
                }
            }
            if (dataType == FormTypeEnum.FILE.getType()) {
                if (StrUtil.isNotEmpty(record.getStr("value"))) {
                    record.set("value", Db.find("select file_id, name, size, create_time, file_path, file_type, batch_id from 72crm_admin_file where batch_id = ?", record.getStr("value")));
                }
            }
        });
    }


}
