package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.service.CrmActivityService;
import com.kakarote.crm9.erp.work.entity.*;
import com.kakarote.crm9.utils.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class TaskService {
    @Inject
    private CrmActivityService crmActivityService;

    @Inject
    private AdminFileService adminFileService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    public R setTaskClass(WorkTaskClass taskClass) {
        boolean bol;
        if (taskClass.getClassId() == null) {
            Work work = new Work().findById(taskClass.getWorkId());
//            Integer isOpen = work.getIsOpen();
//            if (isOpen == 0 && !AuthUtil.isWorkAuth(taskClass.getWorkId().toString(), "taskClass:save")) {
//                return R.noAuth();
//            }
            Integer orderNum = Db.queryInt("select max(order_num) from `72crm_work_task_class` where work_id = ?", taskClass.getWorkId());
            taskClass.setOrderNum(orderNum + 1);
            taskClass.setCreateUserId(BaseUtil.getUser().getUserId());
            taskClass.setCreateTime(new Date());
            bol = taskClass.save();
            actionRecordUtil.addTaskClassRecord(taskClass, BehaviorEnum.SAVE);
        } else {
            Integer workId = Db.queryInt("select work_id from `72crm_work_task_class` where class_id = ?", taskClass.getClassId());
            if (ObjectUtil.isEmpty(workId)) {
                return R.error("项目不存在");
            }
//            if (!AuthUtil.isWorkAuth(workId.toString(), "taskClass:update")) {
//                return R.noAuth();
//            }
            bol = taskClass.update();
            actionRecordUtil.addTaskClassRecord(taskClass, BehaviorEnum.UPDATE);
        }
        return R.isSuccess(bol);
    }


    public void changeOrderTaskClass(String originalClassId, String targetClassId) {
        WorkTaskClass originalClass = WorkTaskClass.dao.findById(originalClassId);
        WorkTaskClass targetClass = WorkTaskClass.dao.findById(targetClassId);
        Integer originalClassOrderId = originalClass.getOrderNum();
        Integer targetClassOrderId = targetClass.getOrderNum();
        Db.update("update 72crm_work_task_class setUser order_id = ? where class_id = ?", originalClassOrderId, targetClassId);
        Db.update("update 72crm_work_task_class setUser order_id = ? where class_id = ?", targetClassOrderId, originalClassId);
    }

    @Before(Tx.class)
    public R setTask(Task task, TaskRelation taskRelation) {
        AdminUser user = BaseUtil.getUser();
        boolean bol;
        if (task.getLabelId() != null) {
            task.setLabelId(TagUtil.fromString(task.getLabelId()));
        }
        if (task.getTaskId() == null) {
            if (task.getMainUserId() == null) {
                task.setMainUserId(user.getUserId());
            }
            if (task.getOwnerUserId() != null) {
                Set<Long> ownerUserId = TagUtil.toLongSet(task.getOwnerUserId());
                ownerUserId.add(user.getUserId());
                task.setOwnerUserId(TagUtil.fromLongSet(ownerUserId));
            } else {
                task.setOwnerUserId("," + user.getUserId() + ",");
            }
            task.setCreateTime(new Date());
            task.setUpdateTime(new Date());
            task.setCreateUserId(user.getUserId());
            task.setBatchId(IdUtil.simpleUUID());
            bol = task.save();
            actionRecordUtil.addObjectActionRecord(CrmEnum.TASK, task.getTaskId(), BehaviorEnum.SAVE);
            WorkTaskLog workTaskLog = new WorkTaskLog();
            workTaskLog.setUserId(user.getUserId());
            workTaskLog.setTaskId(task.getTaskId());
            workTaskLog.setContent("添加了新任务 " + task.getName());
            saveWorkTaskLog(workTaskLog);
            if (bol) {
                //分配给我的任务
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_TASK_ALLOCATION, task, user.getUserId());
                //我参与的任务
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_TASK_JOIN, task, user.getUserId());
            }
        } else {
            task.setUpdateTime(new Date());
            bol = getWorkTaskLog(task, user.getUserId());
        }
        if (taskRelation.getBusinessIds() != null || taskRelation.getContactsIds() != null || taskRelation.getContractIds() != null || taskRelation.getCustomerIds() != null) {
            Db.deleteById("72crm_task_relation", "task_id", task.getTaskId());
            taskRelation.setCreateTime(DateUtil.date());
            taskRelation.setTaskId(task.getTaskId());
            taskRelation.save();
            crmActivityService.addActivity(2, CrmActivityEnum.TASK, task.getTaskId(), "");
        }
        task.getMainUserId();
        return bol ? R.ok().put("data", Kv.by("task_id", task.getTaskId())) : R.error();
    }


    /**
     * @author hmb
     * 查询任务信息
     */
    public R queryTaskInfo(String taskId) {
        Integer isDelete = Db.queryInt("select ishidden from 72crm_task where task_id = ?", taskId);
        if (1 == isDelete) {
            return R.error("该任务已被删除");
        }
        return getTaskInfo(taskId);
    }

    /**
     * @author hmb
     * 回收站查询任务信息
     */
    public R getTaskInfo(String taskId) {
        Record mainTask = transfer(taskId);
        adminFileService.queryByBatchId(mainTask.get("batch_id"), mainTask);
        List<Record> recordList = Db.find("select task_id from 72crm_task where pid = ?", taskId);
        List<Record> childTaskList = new ArrayList<>();
        if (recordList != null && recordList.size() > 0) {
            recordList.forEach(childTaskRecord -> {
                String childTaskId = childTaskRecord.getStr("task_id");
                Record childTask = transfer(childTaskId);
                adminFileService.queryByBatchId(childTask.getStr("batch_id"), childTask);
                childTaskList.add(childTask);
            });
        }
        mainTask.set("childTask", childTaskList);
        if (mainTask.getInt("work_id") != null && mainTask.getInt("work_id") != 0){
            mainTask.set("authList",Aop.get(WorkService.class).auth(BaseUtil.getUserId(),mainTask.getInt("work_id")).get("work"));
        }
        return R.ok().put("data", mainTask);
    }


    private Record transfer(String taskId) {
        Record task = Db.findFirst("select a.*,b.name as workName from 72crm_task a left join `72crm_work` b on a.work_id = b.work_id where task_id = ?", taskId);
        task.set("stop_time", DateUtil.formatDate(task.getDate("stop_time")));
        task.set("mainUser", Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", task.getInt("main_user_id")));
        task.set("createUser", Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", task.getInt("create_user_id")));
        ArrayList<Record> labelList = new ArrayList<>();
        ArrayList<Record> ownerUserList = new ArrayList<>();
        if (StrUtil.isNotBlank(task.getStr("label_id"))) {
            String[] labelIds = task.getStr("label_id").split(",");
            for (String labelId : labelIds) {
                if (StrUtil.isNotBlank(labelId)) {
                    Record label = Db.findFirst("select label_id,name as labelName,color from 72crm_work_task_label where label_id = ?", labelId);
                    labelList.add(label);
                }
            }
        }
        if (StrUtil.isNotBlank(task.getStr("owner_user_id"))) {
            String[] ownerUserIds = task.getStr("owner_user_id").split(",");
            for (String ownerUserId : ownerUserIds) {
                if (StrUtil.isNotBlank(ownerUserId)) {
                    Record ownerUser = Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", ownerUserId);
                    ownerUserList.add(ownerUser);
                }
            }
        }
        Record relation = Db.findFirst("select * FROM 72crm_task_relation where task_id = ?", taskId);
        List<Record> customerList = new ArrayList<>();
        List<Record> contactsList = new ArrayList<>();
        List<Record> businessList = new ArrayList<>();
        List<Record> contractList = new ArrayList<>();
        if (relation != null) {
            if (StrUtil.isNotBlank(relation.getStr("customer_ids"))) {
                String[] customerIds = relation.getStr("customer_ids").split(",");
                for (String customerId : customerIds) {
                    if (StrUtil.isNotBlank(customerId)) {
                        Record customer = Db.findFirst("select customer_id,customer_name  from 72crm_crm_customer where customer_id = ?", customerId);
                        if (customer != null) {
                            customerList.add(customer);
                        }
                    }
                }
            }

            if (StrUtil.isNotBlank(relation.getStr("contacts_ids"))) {
                String[] contactsIds = relation.getStr("contacts_ids").split(",");

                for (String contactsId : contactsIds) {
                    if (StrUtil.isNotBlank(contactsId)) {
                        Record contacts = Db.findFirst("select contacts_id,name from 72crm_crm_contacts  where contacts_id = ?", contactsId);
                        if (contacts != null) {
                            contactsList.add(contacts);
                        }
                    }
                }
            }
            if (StrUtil.isNotBlank(relation.getStr("business_ids"))) {
                String[] businessIds = relation.getStr("business_ids").split(",");

                for (String businessId : businessIds) {
                    if (StrUtil.isNotBlank(businessId)) {
                        Record business = Db.findFirst("select business_id,business_name  from 72crm_crm_business  where business_id = ?", businessId);
                        if (business != null) {
                            businessList.add(business);
                        }
                    }
                }
            }
            if (StrUtil.isNotBlank(relation.getStr("contract_ids"))) {
                String[] contractIds = relation.getStr("contract_ids").split(",");
                for (String contractId : contractIds) {
                    if (StrUtil.isNotBlank(contractId)) {
                        Record contract = Db.findFirst("select contract_id,name from 72crm_crm_contract  where contract_id = ?", contractId);
                        if (contract != null) {
                            contractList.add(contract);
                        }
                    }
                }
                task.set("contractList", contractList);
            }
        }
        task.set("customerList", customerList);
        task.set("contactsList", contactsList);
        task.set("businessList", businessList);
        task.set("contractList", contractList);

        return task.set("labelList", labelList).set("ownerUserList", ownerUserList);
    }

    /**
     * 查询任务列表
     */
    public R queryTaskList(BasePageRequest<Task> basePageRequest, Boolean isExport) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer status = jsonObject.getInteger("status");
        Integer priority = jsonObject.getInteger("priority");
        String date = jsonObject.getString("dueDate");
        Integer mold = jsonObject.getInteger("mold");
        Long userId = jsonObject.getLong("userId");
        String name = jsonObject.getString("search");
        List<Long> userIds = new ArrayList<>();
        if (mold == null) {
            userIds.add(BaseUtil.getUser().getUserId());
        } else if (mold == 1 && userId == null) {
            userIds = Aop.get(AdminUserService.class).queryUserIdsByParentId(BaseUtil.getUser().getUserId());
        } else {
            List<Long> list = Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUser().getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
            for (Long id : list) {
                if (id.equals(userId)) {
                    userIds.add(userId);
                }
            }
        }
        if (BaseUtil.isAdmin() && type == 0 && mold == null) {
            userIds = Db.query("select user_id from `72crm_admin_user`");
        }
        return getTaskList(type, status, priority, date, userIds, basePageRequest, name, isExport);
    }

    /**
     * 查询任务列表
     */
    public R getTaskList(Integer type, Integer status, Integer priority, String date, List<Long> userIds, BasePageRequest<Task> basePageRequest, String name, Boolean isExport) {
        Page<Record> page = new Page<>();
        R result = R.ok();
        if (userIds.size() == 0) {
            result.put("allTask", 0).put("stopTask", 0);
            return result.put("data", basePageRequest.getPageType() == 0 ? page.getList() : page);
        } else {
            Kv kv = Kv.by("type", type).set("userIds", userIds).set("status", status).set("priority", priority).set("date", date).set("taskName", name);
            Record record = Db.template("work.task.queryTaskCount", kv).findFirst();
            result.putAll(record.getColumns());
            if (isExport) {
                List<Record> recordList = Db.find(Db.getSqlPara("work.task.getTaskListExport", kv));
                List<Map<String, Object>> list = new ArrayList<>();
                recordList.forEach(r -> {
                    //拼接关联业务内容
                    String relateCrmWork = "";
                    if (StrUtil.isNotEmpty(r.getStr("customer_ids"))) {
                        relateCrmWork = relateCrmWork + "客户 【" + r.getStr("customer_name") + "】\n";
                    }
                    if (StrUtil.isNotEmpty(r.getStr("contacts_ids"))) {
                        relateCrmWork = relateCrmWork + "联系人 【" + r.getStr("contacts_name") + "】\n";
                    }
                    if (StrUtil.isNotEmpty(r.getStr("business_ids"))) {
                        relateCrmWork = relateCrmWork + "商机 【" + r.getStr("business_name") + "】\n";
                    }
                    if (StrUtil.isNotEmpty(r.getStr("contract_ids"))) {
                        relateCrmWork = relateCrmWork + "合同 【" + r.getStr("contract_name") + "】\n";
                    }
                    r.set("relateCrmWork", relateCrmWork);
                    list.add(r.getColumns());
                });
                return R.ok().put("data", list);
            }
            if (basePageRequest.getPageType() == 0) {
                List<Record> recordList = Db.find(Db.getSqlPara("work.task.getTaskList", kv));
                return result.put("data", queryUser(recordList));
            } else {
                page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("work.task.getTaskList", kv));
                page.setList(queryUser(page.getList()));
                return result.put("data", page);
            }
        }
    }

    public List<Record> queryUser(List<Record> tasks) {
        ArrayList<Record> labelList;
        ArrayList<Record> ownerUserList;
        for (Record task : tasks) {
            Integer mainUserId = task.getInt("main_user_id");
            Record mainUser = Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", mainUserId);
            task.set("mainUser", mainUser);
            labelList = new ArrayList<>();
            ownerUserList = new ArrayList<>();
            if (StrUtil.isNotBlank(task.getStr("label_id"))) {
                String[] labelIds = task.getStr("label_id").split(",");
                for (String labelId : labelIds) {
                    if (StrUtil.isNotBlank(labelId)) {
                        Record label = Db.findFirst("select label_id,name as labelName , color from 72crm_work_task_label where label_id = ?", labelId);
                        labelList.add(label);
                    }
                }
            }
            if (StrUtil.isNotBlank(task.getStr("owner_user_id"))) {
                String[] ownerUserIds = task.getStr("owner_user_id").split(",");
                for (String ownerUserId : ownerUserIds) {
                    if (StrUtil.isNotBlank(ownerUserId)) {
                        Record ownerUser = Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", ownerUserId);
                        ownerUserList.add(ownerUser);
                    }
                }
            }
            TaskRelation taskRelation = TaskRelation.dao.findFirst(" select * from 72crm_task_relation where task_id = ?", task.getInt("task_id"));
            Integer start = 0;
            if (taskRelation != null) {
                start = queryCount(start, taskRelation.getBusinessIds());
                start = queryCount(start, taskRelation.getContactsIds());
                start = queryCount(start, taskRelation.getContractIds());
                start = queryCount(start, taskRelation.getCustomerIds());
            }
            task.set("relationCount", start);
            if (task.getDate("stop_time") != null) {
                Calendar date = Calendar.getInstance();
                date.setTime(DateUtil.date());
                //设置开始时间
                Calendar begin = Calendar.getInstance();
                begin.setTime(task.getDate("stop_time"));
                if (date.after(begin) && task.getInt("status") != 5 && task.getInt("status") != 2) {
                    task.set("is_end", 1);
                } else {
                    task.set("is_end", 0);
                }
            } else {
                task.set("is_end", 0);
            }
            task.set("labelList", labelList).set("ownerUserList", ownerUserList);
        }
        return tasks;
    }

    private Integer queryCount(Integer start, String str) {
        // start 开始个数
        if (str != null) {
            String[] ownerUserIds = str.split(",");
            for (String ownerUserId : ownerUserIds) {
                if (StrUtil.isNotBlank(ownerUserId)) {
                    ++start;
                }
            }
        }

        return start;
    }

    public R queryWorkTaskLog(Integer taskId) {
        List<Record> recordList = Db.find(Db.getSqlPara("work.task.myWorkLog", Kv.by("taskId", taskId)));
        return R.ok().put("data", recordList);
    }

    private void saveWorkTaskLog(WorkTaskLog workTaskLog) {
        workTaskLog.setCreateTime(DateUtil.date());
        workTaskLog.setLogId(null);
        workTaskLog.save();
    }

    @Before(Tx.class)
    private boolean getWorkTaskLog(Task task, Long userId) {
        WorkTaskLog workTaskLog = new WorkTaskLog();
        workTaskLog.setUserId(userId);
        workTaskLog.setTaskId(task.getTaskId());

        Task auldTask = Task.dao.findById(task.getTaskId());

        if (auldTask.getStatus() != 5 && Objects.equals(5, task.getStatus())) {
            //标记为结束的任务
            Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_TASK_OVER, task, userId);
        }

        task.update();
        //添加参与任务通知
        if (StrUtil.isNotEmpty(task.getOwnerUserId())) {
            List<Long> oldUserIdList = StrUtil.splitTrim(auldTask.getOwnerUserId(), ",").stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<Long> userIdList = StrUtil.splitTrim(task.getOwnerUserId(), ",").stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            userIdList.retainAll(oldUserIdList);
            userIdList.forEach(id -> Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_TASK_JOIN, task, id));
        }

        Set<Map.Entry<String, Object>> newEntries = task._getAttrsEntrySet();
        Set<Map.Entry<String, Object>> oldEntries = auldTask._getAttrsEntrySet();
        newEntries.forEach(x -> {
            oldEntries.forEach(y -> {
                Object oldValue = y.getValue();
                Object newValue = x.getValue();
                if (oldValue instanceof Date) {
                    oldValue = DateUtil.formatDate((Date) oldValue);
                }
                if (newValue instanceof Date) {
                    newValue = DateUtil.formatDate((Date) newValue);
                }
                if (oldValue == null || "".equals(oldValue)) {
                    oldValue = "空";
                }
                if (newValue == null || "".equals(newValue)) {
                    newValue = "空";
                }
                if (x.getKey().equals(y.getKey()) && !oldValue.equals(newValue)) {
                    if (!"update_time".equals(y.getKey()) && !"label_id".equals(y.getKey()) && !"owner_user_id".equals(y.getKey())) {
                        if ("priority".equals(y.getKey())) {
                            String value = "";
                            if (1 == parseInt(newValue.toString())) {
                                value = "低";
                            } else if (parseInt(newValue.toString()) == 2) {
                                value = "中";
                            } else if (parseInt(newValue.toString()) == 3) {
                                value = "高";
                            } else {
                                value = "无";
                            }
                            workTaskLog.setContent("修改 优先级 为：" + value + "");
                            actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "修改 优先级 为：" + value);
                        } else {
                            if ("main_user_id".equals(y.getKey())) {
                                newValue = Db.queryStr("select realname from `72crm_admin_user` where user_id = ?", newValue);
                                if (newValue == null || "".equals(newValue) || "空".equals(newValue)) {
                                    workTaskLog.setContent("修改" + getTableName(y.getKey()) + "为：无");
                                    actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "修改" + getTableName(y.getKey()) + "为：无");
                                }else {
                                    workTaskLog.setContent("修改" + getTableName(y.getKey()) + "为："+newValue);
                                    actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "修改" + getTableName(y.getKey()) + "为：" + newValue);
                                }
                            } else if ("start_time".equals(y.getKey()) ||"end_time".equals(y.getKey()) || "name".equals(y.getKey()) || "description".equals(y.getKey())) {
                                workTaskLog.setContent("修改" + getTableName(y.getKey()) + "为：" + newValue);
                                actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "修改" + getTableName(y.getKey()) + "为：" + newValue);
                            }else if ("status".equals(y.getKey())) {
                                if (newValue.equals(1) ){
                                    newValue = "未完成";
                                }else if( newValue.equals(5)){
                                    newValue = "完成";
                                }
                                workTaskLog.setContent("修改" + getTableName(y.getKey()) + "为：" + newValue);
                                actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "修改" + getTableName(y.getKey()) + "为：" + newValue);
                            }
                        }

                    }

                    if (StrUtil.isNotEmpty(workTaskLog.getContent())) {
                        saveWorkTaskLog(workTaskLog);
                    }

                }
            });
        });
        //判断是否修改了标签
        if (task.toRecord().getColumns().containsKey("label_id")) {
            WorkTaskLabel workTaskLabel;
            if (StrUtil.isEmpty(auldTask.getLabelId())) {
                //旧数据没有标签 直接添加
                String[] labelName = task.getLabelId().split(",");
                for (String id : labelName) {
                    if (StrUtil.isNotBlank(id)) {
                        workTaskLabel = WorkTaskLabel.dao.findById(id);
                        workTaskLog.setContent("增加了标签 " + workTaskLabel.getName());
                        actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "增加了标签 " + workTaskLabel.getName());
                        saveWorkTaskLog(workTaskLog);
                    }
                }
            } else {
                //旧数据有标签 自动添加或修改
                if (StrUtil.isNotEmpty(task.getLabelId())){
                    String[] labelName = task.getLabelId().split(",");
                    for (String id : labelName) {
                        if (StrUtil.isNotBlank(id)) {
                            if (!auldTask.getLabelId().contains("," + id + ",")) {
                                workTaskLabel = WorkTaskLabel.dao.findById(id);
                                workTaskLog.setContent("增加了标签 " + workTaskLabel.getName());
                                saveWorkTaskLog(workTaskLog);
                                actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "增加了标签 " + workTaskLabel.getName());
                            }
                        }
                    }
                }
            }
        }
        //判断是参与人
        if (task.toRecord().getColumns().containsKey("owner_user_id")) {
            AdminUser adminUser;
            if (StrUtil.isEmpty(auldTask.getOwnerUserId())) {
                //判断旧数据没有参与人
                String[] userIds = task.getOwnerUserId().split(",");
                for (String id : userIds) {
                    if (StrUtil.isNotBlank(id)) {
                        adminUser = AdminUser.dao.findById(id);
                        workTaskLog.setContent("添加 " + adminUser.getRealname() + "参与任务");
                        saveWorkTaskLog(workTaskLog);
                        actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "添加 " + adminUser.getRealname() + "参与任务");
                    }
                }
            } else {
                //判断旧数据有参与人
                if (StrUtil.isNotEmpty(task.getOwnerUserId())){
                    String[] userIds = task.getOwnerUserId().split(",");
                    for (String id : userIds) {
                        if (StrUtil.isNotBlank(id)) {
                            if (!auldTask.getOwnerUserId().contains("," + id + ",")) {
                                adminUser = AdminUser.dao.findById(id);
                                workTaskLog.setContent("添加 " + adminUser.getRealname() + "参与任务");
                                saveWorkTaskLog(workTaskLog);
                                actionRecordUtil.addTaskUpdateRecord(workTaskLog.getTaskId(), "添加 " + adminUser.getRealname() + "参与任务");
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private String getTableName(String key) {
        if ("name".equals(key)) {
            return "任务名称";
        } else if ("start_time".equals(key)) {
            return "开始时间";
        } else if ("stop_time".equals(key)) {
            return "结束时间";
        } else if ("description".equals(key)) {
            return "任务描述";
        } else if ("main_user_id".equals(key)) {
            return "负责人";
        }else if ("status".equals(key)) {
            return "状态";
        }
        return "";
    }

    /**
     * @author zxy
     * 添加任务与业务关联
     */
    public R saveTaskRelation(TaskRelation taskRelation, Long userId) {
        if (taskRelation.getBusinessIds() != null || taskRelation.getContactsIds() != null || taskRelation.getContractIds() != null || taskRelation.getCustomerIds() != null) {
            crmActivityService.addActivity(2, CrmActivityEnum.TASK, taskRelation.getTaskId(), "");
        }
        Db.delete("delete from `72crm_task_relation` where task_id = ?", taskRelation.getTaskId());
        taskRelation.setCreateTime(DateUtil.date());
        return taskRelation.save() ? R.ok() : R.error();
    }


    @Before(Tx.class)
    public R deleteTask(Integer taskId) {
        Task task = new Task().dao().findById(taskId);
        if (task == null) {
            return R.error("任务不存在！");
        }
        boolean bol;
        if (task.getPid() != 0) {
            bol = task.delete();
        } else {
            bol = Db.update("update 72crm_task set ishidden = 1,hidden_time = now() where task_id = ?", taskId) > 0;
        }
        adminFileService.removeByBatchId(Db.queryStr("select batch_id from `72crm_task` where task_id = ?", taskId));
        return bol ? R.ok() : R.error();
    }


    /**
     * @author zxy
     * crm查询任务
     */
    public R queryTaskRelation(BasePageRequest<TaskRelation> basePageRequest) {
        TaskRelation relation = basePageRequest.getData();
        if (AuthUtil.oaAuth(relation.toRecord())) {
            return R.noAuth();
        }
        Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("work.task.queryTaskRelation", Kv.by("businessIds", relation.getBusinessIds()).set("contactsIds", relation.getContactsIds()).set("contractIds", relation.getContractIds()).set("customerIds", relation.getCustomerIds())));
        paginate.getList().forEach(this::composeUser);
        return R.ok().put("data", paginate);
    }

    private void composeUser(Record record) {
        Long createUserId = record.getLong("create_user_id");
        record.set("createUser", Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", createUserId));
        Long mainUserId = record.getLong("main_user_id");
        record.set("mainUser", Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", mainUserId));
        String ownerUserId = record.getStr("owner_user_id");
        List<Record> ownerUserList = new ArrayList<>();
        TagUtil.toLongSet(ownerUserId).forEach(userId -> ownerUserList.add(Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?", userId)));
        record.set("ownerUserList", ownerUserList);
    }


    public R getTaskList(BasePageRequest basePageRequest, String labelId, String ishidden) {
        Page<Record> recordList = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("work.task.myTask", Kv.by("userId", BaseUtil.getUser().getUserId()).set("labelId", labelId).set("ishidden", ishidden)));
        return R.ok().put("data", recordList);
    }

    public R archiveByTaskId(Integer taskId) {
        int update = Db.update("update  `72crm_task` set is_archive = 1,archive_time = now() where task_id = ?", taskId);
        actionRecordUtil.addObjectActionRecord(CrmEnum.TASK, taskId, BehaviorEnum.ARCHIVE);
        return update > 0 ? R.ok() : R.error();
    }

    public R deleteOwnerUser(Integer taskId, Long userId) {
        Task task = Task.dao.findById(taskId);
        AdminUser adminUser = AdminUser.dao.findById(userId);
        Set<Long> ownerUserIds = TagUtil.toLongSet(task.getOwnerUserId());
        if (!ownerUserIds.contains(userId)){
            R.error("参与人不存在");
        }
        ownerUserIds.remove(userId);
        task.setOwnerUserId(TagUtil.fromLongSet(ownerUserIds));
        WorkTaskLog workTaskLog = new WorkTaskLog();
        workTaskLog.setUserId(BaseUtil.getUserId());
        workTaskLog.setTaskId(task.getTaskId());
        workTaskLog.setContent("添加了新任务 " + task.getName());
        workTaskLog.setContent("将 " + adminUser.getRealname() + "从任务中移除");
        saveWorkTaskLog(workTaskLog);
        return task.update()?R.ok():R.error();
    }

    public R deleteLabel(Integer taskId, Integer labelId) {
        Task task = Task.dao.findById(taskId);
        WorkTaskLabel taskLabel = WorkTaskLabel.dao.findById(labelId);
        Set<Integer> labelIds = TagUtil.toSet(task.getLabelId());
        if (!labelIds.contains(labelId)) {
            R.error("标签不存在");
        }
        labelIds.remove(labelId);
        task.setLabelId(TagUtil.fromSet(labelIds));
        WorkTaskLog workTaskLog = new WorkTaskLog();
        workTaskLog.setUserId(BaseUtil.getUserId());
        workTaskLog.setTaskId(task.getTaskId());
        workTaskLog.setContent("删除了标签 " + taskLabel.getName());
        saveWorkTaskLog(workTaskLog);
        return task.update()?R.ok():R.error();
    }

    public List<Record> export(Integer classId) {
        return Db.find("select * from 72crm_task where class_id = ?", classId);
    }
}
