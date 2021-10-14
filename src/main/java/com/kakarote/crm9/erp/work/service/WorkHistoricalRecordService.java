package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.common.WorkAuthVerify;
import com.kakarote.crm9.erp.work.entity.*;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.mysql.cj.util.StringUtils;
import net.sf.cglib.asm.$Label;

import java.util.*;

/**
 * @author zyy
 */
public class WorkHistoricalRecordService {

    /**
     * 查询不同模块历史变更记录
     * @param temp 是否是查询汇总项目历史记录
     * @param
     */
    public R queryRecordList(BasePageRequest basePageRequest,boolean temp) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer workId = jsonObject.getInteger("workId");
        Integer operationId = jsonObject.getInteger("operationId");
        Object excel = jsonObject.get("excel");

        boolean auth = false;
        if (type == WorkRecordEnum.MILESTONE.getType()) {
            auth = WorkAuthVerify.isPermissions("project:milestone:readHistorical");
        } else if (type == WorkRecordEnum.DEMAND.getType()) {
            auth = WorkAuthVerify.isPermissions("project:demand:readHistorical");
        } else if (type == WorkRecordEnum.TASK_CONFIG.getType()) {
            auth = WorkAuthVerify.isPermissions("project:configManage:readHistorical");
        } else if (type == WorkRecordEnum.RESOURCE.getType()) {
            auth = WorkAuthVerify.isPermissions("project:resources:readHistorical");
        } else if (type == WorkRecordEnum.WORK_CONFIG.getType()) {
            auth = WorkAuthVerify.isPermissions("project:workConfig:readHistorical");
        } else if (type == WorkRecordEnum.TASK.getType()) {
            auth = WorkAuthVerify.isPermissions("project:task:readHistorical");
        } else if (type == WorkRecordEnum.WORK.getType() ||type == WorkRecordEnum.WORK_DETAILS.getType() ) {
            auth = WorkAuthVerify.isPermissions("project:projectManage:readHistorical");
        } else if (type == WorkRecordEnum.SUBTASKS.getType()) {
            auth = WorkAuthVerify.isPermissions("project:task:readHistorical");
        } else {
            auth = WorkAuthVerify.isPermissions("project:historical:index");
        }
        if (!auth) {
            return R.error("无权限");
        }
        Page<Record> page = new Page<>();
        WorkRecordEnum parse = WorkRecordEnum.parse(type);
        List<Integer> integers = Arrays.asList(2, 3, 4, 5, 6,13);
        List<Integer> task = Arrays.asList(7, 12, 11);
        Integer tag = 0;
        List<Record> records;
        SqlPara sqlPara = null;
        if (integers.contains(parse.getType())) {
            sqlPara = Db.getSqlPara("work.queryHistoricalRecordList",
                    Kv.by("type", parse.getTypeName()).set("operationId", operationId).set("workId",workId));
        } else if (task.contains(parse.getType())) {
            sqlPara = Db.getSqlPara("work.queryTaskHistoricalList",
                    Kv.by("type", parse.getTypeName()).set("taskId", jsonObject.get("taskId")).set("operationId", operationId).set("workId",workId));
            tag = 1;
        } else {
            if(temp){
                sqlPara = Db.getSqlPara("work.queryHistoricalList",
                        Kv.by("workId", jsonObject.get("workId")));
                tag = 2;
            }else {
                sqlPara = Db.getSqlPara("work.queryHistoricalRecordList",
                        Kv.by("type", parse.getTypeName()).set("operationId", operationId).set("workId",operationId));
            }
        }
        if(excel != null){
            records = Db.find(sqlPara);
        }else {
            page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), sqlPara);
            records = page.getList();
        }
        List<Record> objects = new ArrayList<>();
        for (Record record : records) {
            if (tag == 1) {
                if (record.getStr("module_two").equals(record.getStr("module_one"))) {
                    record.set("module_two", null);
                }
            } else if (tag == 2) {
                if (record.getStr("module_two").equals(record.getStr("module_three"))) {
                    record.set("module_three", null);
                }
            }
            AdminUser operation = Aop.get(AdminUser.class).dao().findById(record.getStr("operation_user_id"));
            record.set("operation_user_name", operation.getRealname());
            objects.add(record);
        }
        page.setList(objects);
        if (jsonObject.get("excel") != null) {
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("list", objects);
            return R.ok().put("data", resultJsonObject);
        } else {
            return R.ok().put("data", page);
        }
    }

    /**
     * 修改时，添加不同模块历史变更记录
     *
     * @param workRecordEnum 修改的模块
     * @param behaviorEnum   修改类型
     * @param getId          修改的当前编号（没有编号使用ID）
     * @param first          变更前数据
     * @param after          变更后数据
     * @param operationId    变更对象的ID
     */
    public void add(Long workId, WorkRecordEnum workRecordEnum, BehaviorEnum behaviorEnum, Object getId, Record first, Record after, Object operationId) {

        Work byId = Aop.get(Work.class).dao.findById(workId);
        WorkHistorical workHistorical = new WorkHistorical();
        workHistorical.setOperationUserId(BaseUtil.getUserId());
        workHistorical.setChangeTime(new Date());
        workHistorical.setWorkName(byId.getWorkName());
        workHistorical.setModuleOne(WorkRecordEnum.WORK.getTypeName());
        workHistorical.setModuleTwo(workRecordEnum.getTypeName());
        workHistorical.setBehavior(behaviorEnum.getName());
        workHistorical.setNumber(getId);
        workHistorical.setCreateTime(new Date());
        workHistorical.setOperationId(operationId);
        workHistorical.setWorkId(workId);
        WorkTaskHistorical workTaskHistorical = null;
        WorkHistoricalRecord workHistoricalRecord = null;

        List<Record> records = new ArrayList<>();

        if (workRecordEnum.getType() == 12 || workRecordEnum.getType() == 11 || workRecordEnum.getType() == 7) {
            //如果是任务模块修改
            workHistorical.setModuleTwo(WorkRecordEnum.TASK.getTypeName());
            workHistorical.setModuleThree(workRecordEnum.getTypeName());
            workTaskHistorical = new WorkTaskHistorical();
            workTaskHistorical.setOperationUserId(BaseUtil.getUserId());
            workTaskHistorical.setChangeTime(new Date());
            workTaskHistorical.setModuleOne(WorkRecordEnum.TASK.getTypeName());
            workTaskHistorical.setModuleTwo(workRecordEnum.getTypeName());
            workTaskHistorical.setBehavior(behaviorEnum.getName());
            workTaskHistorical.setNumber(getId);
            workTaskHistorical.setCreateTime(new Date());
            workTaskHistorical.setOperationId(operationId);
            workTaskHistorical.setWorkId(workId);
            if(after!=null){
                workTaskHistorical.setTaskId(after.getLong("task_id"));
            }
            Long temp = null;
            if (workRecordEnum.getType() == 11) {
                records = Db.find(Db.getSql("admin.field.queryField"), CrmEnum.SUBTASKS.getType());
                temp = Aop.get(Task.class).dao().findById(operationId).getTaskId();
            } else if (workRecordEnum.getType() == 12) {
                records = Db.find(Db.getSql("admin.field.queryField"), CrmEnum.TASK_HOURS.getType());
                temp = Aop.get(TaskHours.class).dao().findById(operationId).getTaskId();
            } else if (workRecordEnum.getType() == 7) {
                records = Db.find(Db.getSql("admin.field.queryField"), CrmEnum.TASK.getType());
                temp = Aop.get(Task.class).dao().findById(operationId).getTaskId();
            }
            workTaskHistorical.setTaskId(temp);
        } else {
            workHistoricalRecord = new WorkHistoricalRecord();
            workHistoricalRecord.setOperationUserId(BaseUtil.getUserId());
            workHistoricalRecord.setChangeTime(new Date());
            workHistoricalRecord.setModule(workRecordEnum.getTypeName());
            workHistoricalRecord.setBehavior(behaviorEnum.getName());
            workHistoricalRecord.setNumber(getId);
            workHistoricalRecord.setCreateTime(new Date());
            workHistoricalRecord.setOperationId(operationId);
            workHistoricalRecord.setWorkId(workId);
            if (workRecordEnum.getType() == 2) {
                records = Db.find(Db.getSql("admin.field.queryField"), CrmEnum.MILESTONE.getType());
            }else if (workRecordEnum.getType() == 1 || workRecordEnum.getType() == 13) {
                List<Record> structures = Db.find(Db.getSql("work.queryStructure"), "72crm_work");
                for (Record structure : structures) {
                    records.add(new Record().set("field_name", structure.getStr("columnName")).set("name", structure.getStr("columnComment")));
                }
            } else if (workRecordEnum.getType() == 3) {
                records = Db.find(Db.getSql("admin.field.queryField"), CrmEnum.DEMAND.getType());
            } else if (workRecordEnum.getType() == 6) {
                records.add(new Record().set("field_name", "resources_name").set("name", "资源名称"));
                records.add(new Record().set("field_name", "resources_describe").set("name", "资源描述"));
                records.add(new Record().set("field_name", "resources_provide").set("name", "资源提供方"));
                records.add(new Record().set("field_name", "main_user_id").set("name", "负责人"));
                records.add(new Record().set("field_name", "plan_time").set("name", "计划时间"));
                records.add(new Record().set("field_name", "actual_time").set("name", "实际时间"));
                records.add(new Record().set("field_name", "resources_status").set("name", "资源状态"));
            } else if (workRecordEnum.getType() == 4 || workRecordEnum.getType() == 5) {
                records.add(new Record().set("field_name", "config_items").set("name", "配置项"));
                records.add(new Record().set("field_name", "plan_time").set("name", "计划时间"));
                records.add(new Record().set("field_name", "main_user_name").set("name", "负责人"));
                records.add(new Record().set("field_name", "version").set("name", "版本"));
                records.add(new Record().set("field_name", "version_time").set("name", "版本时间"));
                records.add(new Record().set("field_name", "version_describe").set("name", "版本说明"));
                if (workRecordEnum.getType() == 4) {
                    records.add(new Record().set("field_name", "associated_name").set("name", "任务名称"));
                    records.add(new Record().set("field_name", "associated_num").set("name", "任务编号"));
                } else if (workRecordEnum.getType() == 5) {
                    records.add(new Record().set("field_name", "associated_name").set("name", "项目名称"));
                    records.add(new Record().set("field_name", "associated_num").set("name", "项目编号"));
                }
            } else if (workRecordEnum.getType() == 8) {
                List<Record> structures = Db.find(Db.getSql("work.queryStructure"), "72crm_work_risk");
                for (Record structure : structures) {
                    records.add(new Record().set("field_name", structure.getStr("columnName")).set("name", structure.getStr("columnComment")));
                }
            } else if (workRecordEnum.getType() == 9 || workRecordEnum.getType() == 10) {
                List<Record> structures = Db.find(Db.getSql("work.queryStructure"), "72crm_task_file");
                for (Record structure : structures) {
                    records.add(new Record().set("field_name", structure.getStr("columnName")).set("name", structure.getStr("columnComment")));
                }
            }
        }

        if (behaviorEnum.getType() != 1) {
            //如果是编辑
            List<Map<String, Object>> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : after.getColumns().entrySet()) {
                String key = entry.getKey();
                System.out.println("key的值-------------------》"+key);
                if ("create_time".equals(key) || "update_time".equals(key) || "create_user_id".equals(key)) {
                    continue;
                }
                try {
                    if (first.getStr(key).equals(after.getStr(key))) {
                        continue;
                    }
                } catch (Exception e) {
                    if (StrUtil.isEmpty(first.getStr(key)) && StrUtil.isEmpty(after.getStr(key))) {
                        continue;
                    }
                }

                String contentAfter = first.getStr(key);
                String contentBefore = after.getStr(key);

                if ("create_user_id".equals(key)) {
                    //查询修改人
                    workHistoricalRecord.setContentAfter(Db.queryStr("select uaername from 72crm_admin_user where user_id=?", contentAfter));
                    workHistoricalRecord.setContentBefore(Db.queryStr("select uaername from 72crm_admin_user where user_id=?", contentBefore));
                } else {

                    //当任务历史记录不为空时，就添加
                    if (StringUtils.isNullOrEmpty((String) contentAfter)) {
                        if (workTaskHistorical != null) {
                            workTaskHistorical.setContentAfter(null);
                        } else {
                            workHistoricalRecord.setContentAfter(null);
                        }
                        workHistorical.setContentAfter(null);
                    } else {
                        if (workTaskHistorical != null) {
                            workTaskHistorical.setContentAfter(contentAfter);
                        } else {
                            workHistoricalRecord.setContentAfter(contentAfter);
                        }
                        workHistorical.setContentAfter(contentAfter);
                    }
                    for (Record record : records) {
                        String field_name = record.getStr("field_name");
                        System.out.println("field_name的值是：--------------------》"+field_name);
                        if (record.getStr("field_name").equals(key)) {
                            if (workTaskHistorical != null) {
                                workTaskHistorical.setField(record.getStr("name"));
                            } else {
                                workHistoricalRecord.setField(record.getStr("name"));
                            }
                            workHistorical.setField(record.getStr("name"));
                        }
                    }
                    if (workTaskHistorical != null) {
                        if (StrUtil.isEmpty(workTaskHistorical.getField())) {
                            workTaskHistorical.setField(key);
                            workHistorical.setField(key);
                        }
                    } else {
                        if (StrUtil.isEmpty(workHistoricalRecord.getField())) {
                            workHistoricalRecord.setField(key);
                            workHistorical.setField(key);
                        }
                    }
                    if (workTaskHistorical != null) {
                        workTaskHistorical.setContentBefore(contentBefore);
                    } else {
                        workHistoricalRecord.setContentBefore(contentBefore);
                    }
                    workHistorical.setContentBefore(contentBefore);
                }
                if (workTaskHistorical != null) {
                    workTaskHistorical.save();
                    workTaskHistorical.setId(null);
                } else {
                    workHistoricalRecord.save();
                    workHistoricalRecord.setId(null);
                }
                workHistorical.save();
                workHistorical.setId(null);
            }

        } else {
            if (workTaskHistorical != null) {
                workTaskHistorical.save();
            } else {
                workHistoricalRecord.save();
            }
            workHistorical.save();
        }
        if (workTaskHistorical != null) {
            Db.delete("DELETE from 72crm_work_task_historical where content_before is null and content_after is null and behavior!='新建'");
        } else {
            Db.delete("DELETE from 72crm_work_historical_record where content_before is null and content_after is null and behavior!='新建'");
        }
        Db.delete("DELETE from 72crm_work_historical where content_before is null and content_after is null and behavior!='新建'");
    }

    /**
     * 新建时，添加不同模块历史变更记录
     *
     * @param workRecordEnum
     * @param behaviorEnum
     * @param jsonObject
     */
    public void addRecord(WorkRecordEnum workRecordEnum, BehaviorEnum behaviorEnum, JSONObject jsonObject) {
        WorkHistoricalRecord workHistoricalRecord = new WorkHistoricalRecord();
        workHistoricalRecord.setChangeTime(new Date());
        workHistoricalRecord.setModule(workRecordEnum.getTypeName());
        workHistoricalRecord.setBehavior(behaviorEnum.getName());
        if (behaviorEnum.getType() != 1) {
            workHistoricalRecord.setContentBefore(jsonObject.getString("contentBefore"));
            workHistoricalRecord.setContentAfter(jsonObject.getString("contentAfter"));
        }
        workHistoricalRecord.save();
    }

    /**
     * 查询不同模块的变更记录
     */
    /*public R queryPageList(BasePageRequest basePageRequest){
        int page = basePageRequest.getPage();
        int limit = basePageRequest.getLimit();
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        WorkRecordEnum parse = WorkRecordEnum.parse(type);
        Kv kv = Kv.by("page", page);
        kv.set("limit", limit).set("type",parse.getType()).set("search",jsonObject.get("search"));
        List<Record> records = Db.find(Db.getSqlPara("work.queryHistoricalRecordByType", kv));
        return R.ok().put("date",records);
    }*/


}
