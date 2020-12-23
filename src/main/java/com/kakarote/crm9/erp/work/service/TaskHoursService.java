package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.json.Json;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.service.AdminExamineRecordService;

import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.common.WorkRendAuthEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.Task;
import com.kakarote.crm9.erp.work.entity.TaskFile;
import com.kakarote.crm9.erp.work.entity.TaskHours;
import com.kakarote.crm9.utils.AbstractBasePageList;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.ToolUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author zyy
 */
public class TaskHoursService extends AbstractBasePageList {

    @Inject
    private AdminExamineRecordService examineRecordService;

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;


    /**
     * 分页查询任务工时???
     */
    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        CrmEnum crmEnum = CrmEnum.TASK_HOURS;
        JSONObject jsonObject1 = basePageRequest.getJsonObject();
        Kv kv = getKv(basePageRequest, crmEnum, isDataAuth);
        kv.set("taskId", jsonObject1.get("taskId"));
        kv.set("task", jsonObject1.get("task"));
        queryDataAuth(WorkUserEnum.HOURS, kv, CrmEnum.TASK_HOURS, null, WorkRendAuthEnum.HOURS, null);
        if (kv == null) {
            return R.error("无权限");
        }
        List<Record> records = Db.find(Db.getSqlPara("task.hours.queryPageList", kv));
        String time = Db.queryStr("select lock_time from 72crm_task_hours_lock order by create_time desc limit 0,1");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Record record : records) {
            if (record.getInt("task_id").equals(record.getInt("subtasks_id"))) {
                record.set("subtasks_id", null);
                record.set("subtasks_name", null);
            }
            //判断工时是否被锁定
            try {
                if (!StrUtil.isEmpty(time)) {
                    String create = record.getStr("create_time");
                    Date createTime = simpleDateFormat.parse(create);
                    Date lockTime = simpleDateFormat.parse(time);
                    if (createTime.before(lockTime)) {
                        record.set("is_lock", 0);
                    } else {
                        record.set("is_lock", 1);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Integer integer = Db.template("task.hours.queryPageListCount", kv).queryInt();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        jsonObject.put("totalRow", integer);
        return R.ok().put("data", jsonObject);
    }

    /**
     * 查询当前用户提报的工时
     */
    public R queryHoursByUserId(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer userId = jsonObject.getInteger("userId");
        Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("task.hours.queryHoursByUserId", Kv.by("userId", userId)));
        return R.ok().put("data", page);
    }

    /**
     * 设置工时
     *
     * @param jsonObject
     * @return
     */
    @Before(Tx.class)
    public R setTaskHours(JSONObject jsonObject) {
        TaskHours taskHours = jsonObject.getObject("entity", TaskHours.class);
        Task task = Task.dao.findById(taskHours.getTaskId());
        if (taskHours.getSubtasksId() != null) {
            Task subtasks = Task.dao.findById(taskHours.getSubtasksId());
            if (subtasks == null) {
                return R.error("任务不存在");
            }
        }

        if (task == null) {
            return R.error("任务不存在");
        }
        Date startTime = taskHours.getStartTime();
        Date endTime = taskHours.getEndTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

        if (startTime != null && endTime != null) {
            if(!startTime.before(endTime)){
                return R.error("结束时间应大于开始时间");
            }else if((startTime.getTime() - endTime.getTime()) / (24 * 60 * 60 * 1000) > 30){
                return R.error("开始时间到结束时间不能超过30天");
            }
        }
        if (taskHours.getHoursId() == null) {
            Integer integer;
            if (taskHours.getSubtasksId() != null) {
                integer = Db.queryInt("select count(1) from 72crm_task_hours where task_id=? and subtasks_id=?", taskHours.getTaskId(), taskHours.getSubtasksId());
            } else {
                integer = Db.queryInt("select count(1) from 72crm_task_hours where task_id=? and subtasks_id=?", taskHours.getTaskId(), taskHours.getTaskId());
            }
            if (integer != 0)
                return R.error("任务工时已经存在");
            Record first = Db.findFirst("select * from 72crm_task_hours where task_id=?", taskHours.getTaskId());

            taskHours.setCreateUserId(BaseUtil.getUserId());
            taskHours.setCreateTime(new Date());

            if (taskHours.getSubtasksId() == null) {
                taskHours.setSubtasksId(taskHours.getTaskId()).setSubtasksName(task.getTaskName());
            }

            taskHours.setWorkId(task.getWorkId());
            taskHours.save();
            workHistoricalRecordService.add(task.getWorkId(), WorkRecordEnum.HOURS, BehaviorEnum.SAVE, taskHours.getHoursId(), null, null, taskHours.getHoursId());

        } else {
            TaskHours temp = TaskHours.dao.findById(taskHours.getHoursId());
            if (temp.getCheckStatus() != 2)
                return R.error("审核已经通过或者进入审核状态不能再次修改");
            Record records = Db.findFirst("select DATE_FORMAT( lock_time, '%Y-%m-%d %H:%i:%S' ) lock_time from 72crm_task_hours_lock order by lock_time desc limit 0,1 ");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if (temp.getCreateTime().before(simpleDateFormat.parse(records.getStr("lock_time")))) {
                    return R.error("工时被被锁定");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            taskHours.setUpdateTime(new Date());
            Record first = Db.findFirst(Db.getSqlPara("task.hours.queryById",Kv.by("hoursId",taskHours.getHoursId())));
            taskHours.update();
            Record after = Db.findFirst(Db.getSqlPara("task.hours.queryById",Kv.by("hoursId",taskHours.getHoursId())));
            workHistoricalRecordService.add(task.getWorkId(), WorkRecordEnum.HOURS, BehaviorEnum.UPDATE, taskHours.getHoursId(), after, first, taskHours.getHoursId());
        }

        Record examineData = new Record();
        Integer examineCount = Db.queryInt("select count(1) from 72crm_admin_examine where category_type = 4 and status = 1");
        if (examineCount > 0) {
            TaskHours byId = Aop.get(TaskHours.class).findById(taskHours.getHoursId());
            if(byId.getExamineRecordId() != null){

                if(byId.getExamineRecordId()!=null){
                    Integer examineRecordId = byId.getExamineRecordId();
                    //删除旧审核数据
                    Record first = Db.findFirst("select log_id,examine_step_id from 72crm_admin_examine_log where record_id=?", examineRecordId);
                    Db.delete("DELETE from 72crm_admin_examine_log WHERE log_id=?", first.getStr("log_id"));
                    Db.delete("DELETE from 72crm_admin_examine_record WHERE record_id=?", examineRecordId);
                    Db.delete("DELETE from 72crm_admin_examine_step WHERE step_id=?", first.getInt("examine_step_id"));
                }
            }
            examineData = examineRecordService.saveExamineRecord(4, jsonObject.getLong("checkUserId"), taskHours.getMainUserId(), null, taskHours.getCheckStatus());
            if (examineData.getInt("status") == 0) {
                return R.error("没有启动的审核步骤，不能添加！");
            } else if (examineData.getInt("status") == 2) {
                return R.error("没有审批人，不能添加！");
            } else {
                taskHours.setExamineRecordId(examineData.getInt("recordId"));
            }

            if (taskHours.getCheckStatus() != null && taskHours.getCheckStatus() == 5)
                taskHours.setCheckStatus(5);
            else
                taskHours.setCheckStatus(0);

        } else {
            if (taskHours.getCheckStatus() == null)
                taskHours.setCheckStatus(1);
        }
        taskHours.update();

        TaskHours byId = Aop.get(TaskHours.class).dao().findById(taskHours.getHoursId());
        HashSet<Long> set = new HashSet<>();
        set.add(byId.getCheckUserId());
        set.add(byId.getMainUserId());
        set.add(byId.getCreateUserId());
        WorkService.setWorkRelation(set, taskHours.getHoursId(), WorkUserEnum.HOURS.getType(), taskHours.getWorkId());
        return R.ok();
    }

    /**
     * 批量删除工时
     *
     * @param hoursIds
     * @return
     */
    @Before(Tx.class)
    public R delHours(String hoursIds) {
        List<String> strings = Arrays.asList(hoursIds.split(","));
        for (String string : strings) {
            TaskHours byId = Aop.get(TaskHours.class).dao().findById(string);
            if (byId.getCheckStatus() != null) {
                if (byId.getCheckStatus() == 1) {
                    return R.error("审核已经通过不能撤回");
                }
                Integer examineRecordId = byId.getExamineRecordId();
                //删除审核记录
                if(examineRecordId!=null){
                    Record first = Db.findFirst("select log_id,examine_step_id from 72crm_admin_examine_log where record_id=?", examineRecordId);
                    Db.delete("DELETE from 72crm_admin_examine_log WHERE log_id=?", first.getStr("log_id"));
                    Db.delete("DELETE from 72crm_admin_examine_record WHERE record_id=?", examineRecordId);
                    Db.delete("DELETE from 72crm_admin_examine_step WHERE step_id=?", first.getInt("examine_step_id"));
                }
            }
            Db.delete("DELETE from 72crm_task_file WHERE subtasks_id=?",byId.getSubtasksId());
            Aop.get(TaskHours.class).deleteById(string);
        }
        return R.ok();
    }

    public R queryHoursRecord(BasePageRequest basePageRequest) {
        return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(),
                Db.getSqlPara("task.hours.queryHoursRecord", Kv.by("taskId", basePageRequest.getJsonObject().get("taskId")))));
    }

    /**
     * 导出记录列表
     *
     * @return
     */
    public R hoursRecordList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv kv = Kv.by("taskId", jsonObject.get("taskId")).set("workId", jsonObject.get("workId"));
        if (jsonObject.get("excel") != null) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("list", Db.find(Db.getSqlPara("task.hours.hoursRecordList", kv)));
            return R.ok().put("data", jsonObject1);
        }
        Page<Record> recordList = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("task.hours.hoursRecordList", kv));
        return R.ok().put("data", recordList);
    }


    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (task_name like '%{search}%' or subtasks_name like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }


    /**
     * 添加修改
     *
     * @param
     * @param
     */
    public R upload(JSONObject jsonObject) {
        TaskFile taskFile = jsonObject.getObject("entity", TaskFile.class);
        String name = taskFile.getName();
        taskFile.setFileName(name);
        int i = name.lastIndexOf(".");
        name = name.substring(0, i);
        taskFile.setName(name);
        if (taskFile.getSubtasksId() == null) {
            taskFile.setSubtasksId(taskFile.getTaskId());
        }
        List<Integer> query = Db.query("select check_status from 72crm_task_hours where subtasks_id=?", taskFile.getSubtasksId());
        if (!query.contains(1)) {
            return R.error("没有通过的工时审核");
        }
        if (taskFile.getFileId() == null) {
            taskFile.setCreateUserId(BaseUtil.getUserId());
            taskFile.setCreateTime(new Date());
            taskFile.save();
        } else {
            taskFile.update();
        }
        return R.ok();
    }


    public Record queryById(Object id) {
        return Db.findFirst("select * from 72crm_task_hours where hours_id=?", id);
    }

}
