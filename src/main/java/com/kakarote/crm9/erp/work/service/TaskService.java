package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;

import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.common.WorkPermissions;
import com.kakarote.crm9.erp.work.common.WorkRendAuthEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.*;
import com.kakarote.crm9.utils.*;
import com.mysql.jdbc.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zyy
 */
public class TaskService extends AbstractBasePageList {

    @Inject
    AdminFieldService adminFieldService;

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;

    /**
     * 设置任务（添加/编辑）
     * @param jsonObject
     * @return
     */
    @Before(Tx.class)
    public R setTask(JSONObject jsonObject) {
        Task task = jsonObject.getObject("entity", Task.class);
        Date startTime = task.getStartTime();
        Date endTime = task.getEndTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        if (startTime != null && endTime != null) {
            if(!startTime.before(endTime)){
                return R.error("结束时间应大于开始时间");
            }else if((startTime.getTime() - endTime.getTime()) / (24 * 60 * 60 * 1000) > 30){
                return R.error("开始时间到结束时间不能超过30天");
            }
        }
        if(task.getPid()!=null&&task.getWorkId()==null){
            Task byId = Aop.get(Task.class).dao().findById(task.getPid());
            task.setWorkId(byId.getWorkId());
        }
        Set<Long> longs = new HashSet<>();
        if (!StringUtils.isNullOrEmpty(task.getOwnerUserId())) {
            longs = TagUtil.toLongSet(task.getOwnerUserId());
            task.setOwnerUserId(TagUtil.fromLongSet(longs));
        }
        longs.add(task.getMainUserId());
        Date date = new Date();
        boolean temp;
        if (task.getTaskId() == null) {
            longs.add(BaseUtil.getUserId());
            if(StringUtils.isNullOrEmpty(task.getTaskNum())){
                AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 21");
                String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                task.setTaskNum(result);
            }
            task.setCreateTime(date);
            task.setBatchId(IdUtil.simpleUUID());
            task.setUpdateTime(new Date());
            task.setCreateUserId(BaseUtil.getUserId());
            temp = task.save();
            if(task.getPid()!=null){
                workHistoricalRecordService.add(task.getWorkId(), WorkRecordEnum.SUBTASKS, BehaviorEnum.SAVE, task.getTaskNum(), null, null,task.getTaskId());
            }else {
                workHistoricalRecordService.add(task.getWorkId(), WorkRecordEnum.TASK, BehaviorEnum.SAVE, task.getTaskNum(), null, null,task.getTaskId());
            }
        } else {
            temp = true;
            task.setUpdateTime(date);
            Record first = Db.findFirst("SELECT t1.task_id,t1.work_id,(select work_name from 72crm_work where work_id=t1.work_id) " +
                    "work_name,t1.task_num,t1.class_id,t1.owner_user_id,t1.pid,t1.task_name,t1.description,t1.`status`,DATE_FORMAT( t1.start_time, '%Y-%m-%d %H:%i:%S' ) " +
                    "start_time, DATE_FORMAT( t1.end_time, '%Y-%m-%d %H:%i:%S' ) end_time,t1.completeness,t1.priority,t1.difficulty,t1.degree,t1.create_time,t1.update_time," +
                    "t1.before_id,(select realname from 72crm_admin_user where user_id=t1.main_user_id) main_user_id,milestone_id,(select milestone_name from " +
                    "72crm_milestone where milestone_id=t1.milestone_id) milestone_name FROM 72crm_task AS t1 WHERE t1.task_id = ?", task.getTaskId());
            task.update();
            Record update = Db.findFirst("SELECT t1.task_id,t1.work_id,(select work_name from 72crm_work where work_id=t1.work_id) " +
                    "work_name,t1.task_num,t1.class_id,t1.owner_user_id,t1.pid,t1.task_name,t1.description,t1.`status`,DATE_FORMAT( t1.start_time, '%Y-%m-%d %H:%i:%S' ) " +
                    "start_time, DATE_FORMAT( t1.end_time, '%Y-%m-%d %H:%i:%S' ) end_time,t1.completeness,t1.priority,t1.difficulty,t1.degree,t1.create_time,t1.update_time," +
                    "t1.before_id,(select realname from 72crm_admin_user where user_id=t1.main_user_id) main_user_id,milestone_id,(select milestone_name from " +
                    "72crm_milestone where milestone_id=t1.milestone_id) milestone_name FROM 72crm_task AS t1 WHERE t1.task_id = ?", task.getTaskId());
            if(task.getPid()!=null){
                workHistoricalRecordService.add(task.getWorkId(), WorkRecordEnum.SUBTASKS, BehaviorEnum.UPDATE, task.getTaskNum(),update, first, task.getTaskId());
            }else {
                workHistoricalRecordService.add(task.getWorkId(), WorkRecordEnum.TASK, BehaviorEnum.UPDATE, task.getTaskNum(),update, first, task.getTaskId());
            }

        }
        //设置任务关联人员
        WorkService.setWorkRelation(longs, task.getTaskId(),WorkUserEnum.TYPE_1.getType(),task.getWorkId());

        return temp ? R.ok() : R.error("设置任务错误");
    }


    /**
     * 分页查询任务??
     */
    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        CrmEnum task = CrmEnum.TASK;
        Kv kv = getKv(basePageRequest, task, isDataAuth);
        kv.set("_work_id",basePageRequest.getJsonObject().getInteger("workId"));
        kv.set("_front",basePageRequest.getJsonObject().getInteger("front"));
        kv.set("_milestone_id",basePageRequest.getJsonObject().getInteger("milestoneId"));
        kv.set("_before_id",basePageRequest.getJsonObject().getInteger("beforeId"));
        kv.set("_pid",basePageRequest.getJsonObject().getInteger("pid"));
        kv.set("_status",basePageRequest.getJsonObject().get("status"));
        kv.set("_userId", BaseUtil.getUserId());
        if(kv.get("scene")!=null){
            queryDataAuth(WorkUserEnum.TYPE_1,kv,CrmEnum.TASK,null, WorkRendAuthEnum.TASK,Integer.valueOf(kv.getStr("scene")));
        }else {
            queryDataAuth(WorkUserEnum.TYPE_1,kv,CrmEnum.TASK,null, WorkRendAuthEnum.TASK,null);
        }
        if(kv == null){
            return R.error("无权访问");
        }
        SqlPara sqlPara = Db.getSqlPara("work.task.advancedSearch", kv);
        List<Record> records = Db.find(sqlPara);
        records.forEach(record -> {
            //List<Record> users = Db.find(Db.getSql("work.queryWorkUserByNumId"), record.getStr("task_id"),1);
            List<String> owner_user_id = Arrays.asList(record.getStr("owner_user_id").split(","));
            List<Record> users = Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", owner_user_id)));
            String userId = users.stream().map(user -> user.getStr("user_id")).collect(Collectors.toList()).toString();
            String realname = users.stream().map(user -> user.getStr("realname")).collect(Collectors.toList()).toString();
            record.set("owner_user_id",userId.substring(1,userId.length()-1));
            record.set("owner_user_name",realname.substring(1,realname.length()-1));
        });
        JSONObject resultJsonObject = new JSONObject();
        Integer integer = Db.template("work.task.queryTaskPageListCount", kv).queryInt();
        resultJsonObject.put("totalRow", integer);
        resultJsonObject.put("list", records);
        return R.ok().put("data",resultJsonObject);
    }

    public R querySubtasksByTaskHours(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer taskId = jsonObject.getInteger("pid");
        List<Record> records = Db.find(Db.getSql("work.task.querySubtasksByTaskHours"), taskId);
        for (int i = 0; i < records.size(); i++) {
            if(records.get(i).getInt("task_id") == taskId){
                records.remove(i);
            }
        }
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("list",records);
        return R.ok().put("data",jsonObject1);
    }

    /**
     *
     */

    /**
     * 查询待办任务
     */
    public R waiting(BasePageRequest basePageRequest, boolean isDataAuth){
        CrmEnum task = CrmEnum.TASK;
        Kv kv = getKv(basePageRequest, task, isDataAuth);
        JSONObject jsonObject = basePageRequest.getJsonObject();
        if(jsonObject.get("userId")!=null){
            kv.set("_userId", jsonObject.get("userId"));
        }else {
            kv.set("_userId", BaseUtil.getUserId());
        }
        kv.set("isWaiting", jsonObject.get("isWaiting"));
        kv.set("workId", jsonObject.get("workId"));
        queryDataAuth(WorkUserEnum.TYPE_1,kv,CrmEnum.TASK,null, WorkRendAuthEnum.TASK,1,kv.getLong("_userId"));
        if(kv == null){
            return R.error("无权访问");
        }
        SqlPara sqlPara = Db.getSqlPara("work.task.waiting", kv);
        List<Record> records = Db.find(sqlPara);
        records.forEach(record -> {
            if(!StrUtil.isEmpty(record.getStr("pid"))){
                record.set("task_name",record.getStr("p_name"));
                record.set("task_id",record.getStr("pid"));
                record.set("subtasks_name",record.getStr("task_name"));
                record.set("subtasks_id",record.getStr("task_id"));
            }
            List<String> owner_user_id = Arrays.asList(record.getStr("owner_user_id").split(","));
            List<Record> users = Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", owner_user_id)));
            String userId = users.stream().map(user -> user.getStr("user_id")).collect(Collectors.toList()).toString();
            String realname = users.stream().map(user -> user.getStr("realname")).collect(Collectors.toList()).toString();
            record.set("owner_user_id",userId.substring(1,userId.length()-1));
            record.set("owner_user_name",realname.substring(1,realname.length()-1));
            /*List<Record> users = Db.find(Db.getSql("work.queryWorkUserByNumId"), record.getStr("task_id"),1);
            String userId = users.stream().map(user -> user.getStr("user_id")).collect(Collectors.toList()).toString();
            String realname = users.stream().map(user -> user.getStr("realname")).collect(Collectors.toList()).toString();
            record.set("owner_user_id",userId.substring(1,userId.length()-1));
            record.set("owner_user_name",realname.substring(1,realname.length()-1));*/
        });
        JSONObject resultJsonObject = new JSONObject();
        Integer integer = Db.template("work.task.waitingCount", kv).queryInt();
        resultJsonObject.put("totalRow", integer);
        resultJsonObject.put("list", records);
        return R.ok().put("data",resultJsonObject);
    }
    /**
     * 根据ID查询单个任务
     */
    public Record queryById(Integer taskId){
        Record first = Db.findFirst(Db.getSql("work.task.queryById"), taskId);
        Record data = Db.findFirst("select examine_record_id,hours_id from 72crm_task_hours where subtasks_id = ?", first.getStr("task_id"));
        try {
            first.set("examine_record_id",data.getStr("examine_record_id"));
            first.set("hours_id",data.getStr("hours_id"));
        } catch (NullPointerException e) {
        }

        List<String> owner_user_id = Arrays.asList(first.getStr("owner_user_id").split(","));
        List<Record> users = Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", owner_user_id)));

        //List<Record> records = Db.find(Db.getSql("work.queryWorkUserByNumId"), first.getStr("task_id"), 1);
        Integer checkStatus = Db.queryInt("select check_status from 72crm_task_hours where subtasks_id =?", first.getStr("task_id"));
        first.set("check_status",checkStatus);
        first.set("owner_user_id",users);
        first.set("user_id",BaseUtil.getUserId());
        return first;
    }

    /**
     * 查询任务详情信息
     */
    public R findFieldDetails(Integer id) {
        List<Record> fields = adminFieldService.getFields(CrmEnum.TASK.getType(), id);
        List<Record> fieldDetails = adminFieldService.findFieldDetails(CrmEnum.TASK, fields, id);
        return R.ok().put("data",fieldDetails);
    }

    /**
     * 查询子任务详情信息
     */
    public R subtasksFindFieldDetails(Integer id) {
        List<Record> fields = adminFieldService.getFields(CrmEnum.SUBTASKS.getType(), id);
        List<Record> fieldDetails = adminFieldService.findFieldDetails(CrmEnum.TASK, fields, id);
        return R.ok().put("data",fieldDetails);
    }

    /**
     * 删除任务校验
     *
     * @return
     */
    public R delTaskCheck(String ids) {
        List<String> strings = Arrays.asList(ids.split(","));
        for (String string : strings) {
            Task byId = Task.dao.findById(string);
            if(byId==null){
                continue;
            }
            Record first = Db.findFirst("select before_id from 72crm_task where task_id=?", string);
            if(StrUtil.isEmpty(first.getStr("before_id"))){
                return R.error("任务："+first.getStr("task_name")+"存在前置任务");
            }
        }
        Kv kv = Kv.by("ids", strings);
        Integer subtasksId = Db.template("work.task.countTaskBySubtasksId", kv).queryInt();
        if(subtasksId != 0){
            return R.error("有任务存在任务工时不能删除");
    }
        Integer integer = Db.template("work.task.countTaskByBeforeId", kv).queryInt();
        if(integer != 0){
            return R.error("有任务存在子任务是否继续");
        }
        return R.ok("是否删除"+strings.size()+"条任务");
    }
    /**
     * 批量删除任务
     */
    @Before(Tx.class)
    public R delTask(String ids){
        List<String> strings = Arrays.asList(ids.split(","));
        for (String string : strings) {
            Task byId = Task.dao.findById(string);
            if(byId==null){
                continue;
            }
            Record first = Db.findFirst("select before_id from 72crm_task where task_id=?", string);
            if(!StrUtil.isEmpty(first.getStr("before_id"))){
                return R.error("任务："+first.getStr("task_name")+"存在前置任务");
            }
        }
        Kv kv = Kv.by("ids", strings);
        Integer subtasksId = Db.template("work.task.countTaskBySubtasksId", kv).queryInt();
        if(subtasksId != 0){
            return R.error("有任务存在任务工时不能删除");
        }
        Integer integer = Db.template("work.task.countTaskByBeforeId", kv).queryInt();
        if(integer != 0){
            return R.error("有任务存在子任务是否继续");
        }
        //List<String> strings = Arrays.asList(ids.split(","));
        int delete = Db.template("work.task.deleteTask", Kv.by("ids", ids)).delete();
        Db.template("task.file.deleteFileByTaskIds", Kv.by("ids", ids)).delete();
        Db.template("task.file.deleteConfigByTaskIds", Kv.by("ids", ids)).delete();
        int deleteTaskByPid = Db.template("work.task.deleteTaskByPid", Kv.by("ids", ids)).delete();
        return delete==strings.size()?R.ok():R.error("删除"+delete+"条数据，未删除+"+(strings.size()-delete));
    }

    /**
     * 查询任务的项目信息
     */
    public R queryWorkByTaskId(Integer taskId) {
        Integer integer = Db.queryInt("select work_id from 72crm_task where task_id = ?", taskId);
        List<Record> fields = adminFieldService.getFields(CrmEnum.WORK.getType(), integer);
        List<Record> fieldDetails = adminFieldService.findFieldDetails(CrmEnum.TASK, fields, taskId);
        return R.ok().put("data",fieldDetails);
    }

    /**
     * 操作任务关联人员表
     *
     * @param longs
     * @param task
     */
    private void setTaskRelation(Collection<Long> longs, Task task) {
        longs.remove(null);
        Long taskId = task.getTaskId();
        Db.deleteById("72crm_work_user_record", "num_id", taskId);
        Date date = new Date();
        longs.forEach(user -> {
            WorkUserRecord workUser = new WorkUserRecord();

            workUser.setNumId(task.getTaskId()).setUserId(user).setType(WorkUserEnum.TYPE_1.getType());
            workUser.save();
        });
    }

    /**
     * 查询对应任务的参与人员
     */
    public R findOwnerUser(String taskId){
        List<Record> records = Db.find(Db.getSql("work.task.findOwnerUser"), taskId);
        return R.ok().put("data",records);
    }

    /**
     * 批量添加任务参与人员
     * @param taskId
     * @param users
     * @return
     */
    public R addOwnerUser(String taskId, String users) {
        List<Long> userIds = Db.query("select user_id from 72crm_work_user_record where num_id=? and type=1", taskId);
        List<String> strings = Arrays.asList(users.split(","));
        HashSet<Long> set = new HashSet<>();
        set.addAll(userIds);
        strings.forEach(str->{
            set.add(Long.parseLong(str));
        });
        set.removeAll(userIds);
        List<WorkUserRecord> list = new ArrayList<>();
        set.forEach(str->{
            WorkUserRecord workUserRecord = new WorkUserRecord();
            workUserRecord.setNumId(Long.parseLong(taskId));
            workUserRecord.setUserId(str);
            workUserRecord.setType(1);
            list.add(workUserRecord);
        });
        Db.batchSave(list,list.size());
        return R.ok();
    }

    /**
     * 批量删除任务参与人员
     * @param
     * @return
     */
    public R delOwnerUser(String taskId, String users){
        List<String> strings = Arrays.asList(users.split(","));
        boolean temp = Db.tx(()->{
            int delete =0;
            for (String string : strings) {
                delete+= Db.delete("delete from 72crm_work_user_record where num_id=? and user_id=? and type=1", taskId, string);
            }
            return delete == strings.size()? true:false;
        });
        return temp?R.ok():R.error("删除失败");
    }

    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (task_name like '%{search}%' or task_id like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }

    public R findTaskFileById(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject().getJSONObject("data");
        SqlPara sqlPara = Db.getSqlPara("work.task.findTaskFileByBatchId",jsonObject);
        List<Record> records = Db.find(sqlPara);
        Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), sqlPara);
        return R.ok().put("data",paginate);
    }

    public R userTask(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer userId = jsonObject.getInteger("userId");
        List<Integer> query = Db.query("select num_id from 72crm_work_user_record where user_id=? and type=?", userId,WorkUserEnum.TYPE_1.getType());
        Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("work.task.queryTaskByIds", Kv.by("ids", query)));
        return R.ok().put("data",page);
    }


    /**
     * 操作自定义字段
     *
     * @param
     * @param task
     */
   /* private boolean setTaskData(JSONArray fields, Task task) {
        String batchId = task.getBatchId();
        List<TaskData> objects = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_task_data", "batch_id", batchId);
        fields.forEach(field -> {
            String formType = JSONObject.parseObject(JSONObject.toJSONString(field)).getString("formType");
            if(StringUtils.isNullOrEmpty(formType)){
                return;
            }
            TaskData taskData = TypeUtils.castToJavaBean(field, TaskData.class);
            taskData.setCreateTime(new Date());
            taskData.setBatchId(batchId);
            //多选类型选项按照固定顺序保存，以便高级筛选进行精确匹配
            if ("checkbox".equals(formType)) {
                List<String> optionList = Arrays.stream(taskData.getValue().split(",")).collect(Collectors.toList());
                CollectionUtil.sortByPinyin(optionList);
                taskData.setValue(CollectionUtil.join(optionList, ","));
            }
            objects.add(taskData);
        });
        Db.batchSave(objects, 100);
        return true;
    }*/
}
