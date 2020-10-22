package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
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
        Kv kv = getKv(basePageRequest, crmEnum, isDataAuth);
        kv.set("taskId",basePageRequest.getJsonObject().get("taskId"));
        queryDataAuth(WorkUserEnum.TASK_HOURS,kv,CrmEnum.TASK_HOURS,null, WorkRendAuthEnum.HOURS,null);
        if(kv == null){
            return R.error("无权限");
        }
        List<Record> records = Db.find(Db.getSqlPara("task.hours.queryPageList", kv));
        String time = Db.queryStr("select lock_time from 72crm_task_hours_lock order by create_time desc limit 0,1");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!StrUtil.isEmpty(time)){
            try {
                Date lockTime = simpleDateFormat.parse(time);
                for (Record record : records) {
                    String create = record.getStr("create_time");
                    Date createTime = simpleDateFormat.parse(create);
                    //判断工时是否被锁定
                    if(createTime.before(lockTime)){
                        record.set("is_lock",0);
                    }else {
                        record.set("is_lock",1);
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
     * 设置工时
     *
     * @param jsonObject
     * @return
     */
    @Before(Tx.class)
    public R setTaskHours(JSONObject jsonObject) {
        TaskHours taskHours = jsonObject.getObject("entity", TaskHours.class);
        Task task = Task.dao.findById(taskHours.getTaskId());
        if(taskHours.getSubtasksId()!=null){
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
        if (startTime != null && endTime != null && startTime.compareTo(endTime) > 0) {
            return R.error("结束时间应大于开始时间");
        } else if ((startTime.getTime() - endTime.getTime()) / (24 * 60 * 60 * 1000) > 30) {
            return R.error("开始时间到结束时间不能超过30天");
        } else {
            Long i = (endTime.getTime() - startTime.getTime()) / (60 * 60 * 1000);
            taskHours.setWorkingHours(i);
        }
        Record examineData = new Record();
        Integer examineCount = Db.queryInt("select count(1) from 72crm_admin_examine where category_type = 4 and status = 1");
        if (taskHours.getHoursId() == null) {
            /*Integer integer = Db.queryInt("select count(1) from 72crm_task_hours where task_id=? and subtasks_id=?", taskHours.getTaskId(), taskHours.getSubtasksId());
            if (integer != 0)
                return R.error("任务工时已经存在");*/
            Record first = Db.findFirst("select * from 72crm_task_hours where task_id=?", taskHours.getTaskId());
            if (first == null) {
                //添加主任务到工时
                if (taskHours.getSubtasksId() != null) {
                    TaskHours hours = new TaskHours();
                    hours.setTaskId(task.getTaskId()).setCreateTime(new Date()).setUpdateTime(new Date()).setTaskName(task.getTaskName())
                            .setSubtasksId(task.getTaskId()).setSubtasksName(task.getTaskName()).setCreateUserId(BaseUtil.getUserId());
                    hours.save();
                }else {
                    taskHours.setSubtasksId(task.getTaskId()).setSubtasksName(taskHours.getTaskName());
                }
            }else {
                //如果当前存在的主任务没有审核记录说明存在的主任务是被子任务带入的并没有去审核
                if(first.getStr("task_id").equals(first.getStr("subtasks_id"))){
                    TaskHours.dao.deleteById(first.getStr("hours_id"));
                }
            }

            taskHours.setCreateUserId(BaseUtil.getUserId());
            taskHours.setCreateTime(new Date());

            if (examineCount > 0) {
                examineData = examineRecordService.saveExamineRecord(4, jsonObject.getLong("checkUserId"), taskHours.getMainUserId(), null, taskHours.getCheckStatus());
                if (examineData.getInt("status") == 0) {
                    return R.error("没有启动的审核步骤，不能添加！");
                } else if (examineData.getInt("status") == 2) {
                    return R.error("没有审批人，不能添加！");
                } else {
                    taskHours.setExamineRecordId(examineData.getInt("recordId"));
                    //添加审批人!!!
                    //Db.queryInt("")
                }

                if (taskHours.getCheckStatus() != null && taskHours.getCheckStatus() == 5)
                    taskHours.setCheckStatus(5);
                else
                    taskHours.setCheckStatus(0);

            } else {
                if (taskHours.getCheckStatus() == null)
                    taskHours.setCheckStatus(1);
            }
            if(taskHours.getSubtasksId()==null){
                taskHours.setSubtasksId(taskHours.getTaskId()).setSubtasksName(task.getTaskName());
            }
            taskHours.save();
            workHistoricalRecordService.add(taskHours.getWorkId(), WorkRecordEnum.HOURS, BehaviorEnum.SAVE, taskHours.getHoursId(), null, null,taskHours.getHoursId());

        } else {
            TaskHours temp = TaskHours.dao.findById(taskHours.getHoursId());
            if (temp.getCheckStatus() == 1)
                return R.error("审核已经通过不能再次修改");
            if(!temp.getCreateTime().before(new Date())){
                return R.error("工时被被锁定");
            }
            taskHours.setCheckStatus(temp.getCheckStatus());
            taskHours.setExamineRecordId(temp.getExamineRecordId());
            taskHours.setUpdateTime(new Date());
            Record first = queryById(taskHours.getHoursId());
            taskHours.update();
            Record after = queryById(taskHours.getHoursId());
            workHistoricalRecordService.add(taskHours.getWorkId(), WorkRecordEnum.HOURS, BehaviorEnum.UPDATE, taskHours.getHoursId(), first, after,taskHours.getHoursId());
        }
        HashSet<Long> set = new HashSet<>();
        set.add(taskHours.getCheckUserId());
        set.add(taskHours.getMainUserId());
        set.add(taskHours.getCreateUserId());
        WorkService.setWorkRelation(set,taskHours.getHoursId(), WorkUserEnum.HOURS.getType(),taskHours.getWorkId());
        return R.ok();
    }

    /**
     * 批量删除工时
     * @param hoursIds
     * @return
     */
    public R delHours(String hoursIds) {
        List<String> strings = Arrays.asList(hoursIds.split(","));
        for (String string : strings) {
            TaskHours byId = Aop.get(TaskHours.class).dao().findById(string);
            if(byId.getCheckStatus() == 1){
                continue;
            }
            Integer examineRecordId = byId.getExamineRecordId();
            //删除审核记录
            Record first = Db.findFirst("select log_id,examine_step_id from 72crm_admin_examine_log where record_id=?", examineRecordId);
            Db.delete("72crm_admin_examine_log",first.getInt("log_id"));
            Db.delete("72crm_admin_examine_record",examineRecordId);
            Db.delete("72crm_admin_examine_step",first.getInt("examine_step_id"));
            Aop.get(TaskHours.class).deleteById(string);
        }
        return R.ok();
    }

    public R queryHoursRecord(BasePageRequest basePageRequest) {
        return R.ok().put("data",Db.paginate(basePageRequest.getPage(),basePageRequest.getLimit(),
                Db.getSqlPara("task.hours.queryHoursRecord",Kv.by("taskId",basePageRequest.getJsonObject().get("taskId")))));
    }

    /**
     *
     * 导出记录列表
     * @return
     */
    public R hoursRecordList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv kv = Kv.by("taskId", jsonObject.get("taskId")).set("workId", jsonObject.get("workId"));
        Page<Record> recordList = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("task.hours.hoursRecordList", kv));
        return R.ok().put("data",recordList);
    }


    @Override
    public JSONObject appendSqlSearch(String search) {
        return null;
    }


    /**
     * 添加修改
     * @param
     * @param
     */
    public R upload(JSONObject jsonObject) {
        TaskFile taskFile = jsonObject.getObject("entity", TaskFile.class);
        String name = taskFile.getName();
        taskFile.setFileName(name);
        int i = name.lastIndexOf(".");
        name = name.substring(0,i);
        taskFile.setName(name);
        if(taskFile.getSubtasksId() == null){
            taskFile.setSubtasksId(taskFile.getTaskId());
        }
        List<Integer> query = Db.query("select check_status from 72crm_task_hours where subtasks_id=?", taskFile.getSubtasksId());
        if(!query.contains(1)){
            return R.error("没有通过的工时审核");
        }
        if(taskFile.getFileId() == null){
            taskFile.setCreateUserId(BaseUtil.getUserId());
            taskFile.setCreateTime(new Date());
            taskFile.save();
        }else {
            taskFile.update();
        }
        return R.ok();
    }



    public Record queryById(Object id){
        return  Db.findFirst("select * from 72crm_task_hours where hours_id=?",id);
    }
}
