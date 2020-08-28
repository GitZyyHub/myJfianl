package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.annotation.WorkPermissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.oa.common.OaEnum;
import com.kakarote.crm9.erp.work.entity.Task;
import com.kakarote.crm9.erp.work.entity.TaskRelation;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.entity.WorkTaskClass;
import com.kakarote.crm9.erp.work.service.TaskService;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hmb
 */
public class TaskController extends Controller{

    @Inject
    private TaskService taskService;
    @Inject
    private AdminUserService userService;



    /**
     * @param taskClass 任务类别对象
     * @author hmb
     * 设置任务类别
     */
    @WorkPermissions({"work:project:saveTaskClass"})
    public void saveWorkTaskClass(@Para("") WorkTaskClass taskClass){
        renderJson(taskService.setTaskClass(taskClass));
    }

    public void setTaskClass(@Para("") WorkTaskClass taskClass){
        renderJson(taskService.setTaskClass(taskClass));
    }

    @WorkPermissions({"work:project:updateTaskClass"})
    public void updateWorkTaskClass(@Para("") WorkTaskClass taskClass){
        renderJson(taskService.setTaskClass(taskClass));
    }

    /**
     * @author hmb
     * 交换任务列表排序
     */
    public void changeOrderTaskClass(){
        String originalClassId = getPara("originalClassId");
        String targetClassId = getPara("targetClassId");
        taskService.changeOrderTaskClass(originalClassId, targetClassId);
        renderJson(R.ok());
    }

    /**
     * @param task 任务对象
     * @author hmb
     * 设置oa任务
     */
    public void setTask(@Para("") Task task){
        if (task.getPid() != null && task.getPid() != 0) {
            Task oldTask = Task.dao.findById(task.getPid());
            if (oldTask.getWorkId() == null && !AuthUtil.isOaAuth(OaEnum.TASK_TYPE_KEY, task.getPid())) {
                renderJson(R.noAuth());
                return;
            }
        }
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:saveTask"})
    public void saveWorkTask(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setTaskStatus"})
    public void setWorkTaskStatus(@Para("") Task task){
//        Task oldTask = Task.dao.findById(task.getTaskId());
        //标记完成时默认放在下方未完成的首位
//        if(oldTask.getStatus() != 5 && task.getStatus() == 5){
//            Integer orderNum = Db.queryInt("select max(order_num) from 72crm_task where class_id = ? and work_id = ? and (status = 3 or status = 1)", oldTask.getClassId(), oldTask.getWorkId());
//            if (orderNum > 1) {
//                task.setOrderNum(orderNum + 1);
//                Db.update("update 72crm_task set order_num = order_num + 1 where order_num > ? and class_id = ? and work_id = ?", orderNum, oldTask.getClassId(), oldTask.getWorkId());
//            }
//        }
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setTaskTitle"})
    public void setWorkTaskTitle(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setTaskDescription"})
    public void setWorkTaskDescription(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setTaskMainUser"})
    public void setWorkTaskMainUser(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setTaskOwnerUser"})
    public void setWorkTaskOwnerUser(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setTaskTime"})
    public void setWorkTaskTime(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setTaskLabel"})
    public void setWorkTaskLabel(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setTaskPriority"})
    public void setWorkTaskPriority(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:addChildTask"})
    public void addWorkChildTask(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:updateChildTask"})
    public void updateWorkChildTask(@Para("") Task task){
        renderJson(authTask(task));
    }

    @WorkPermissions({"work:project:setChildTaskStatus"})
    public void setWorkChildTaskStatus(@Para("") Task task){
        renderJson(authTask(task));
    }

    private R authTask(Task task){
        if(StrUtil.isNotEmpty(task.getOwnerUserId())){
            task.setOwnerUserId(TagUtil.fromString(task.getOwnerUserId()));
        }
        if(task.getStartTime() != null && task.getStopTime() != null){
            if(task.getStartTime().getTime() > task.getStopTime().getTime()){
                return R.error("开始时间不能大于结束时间");
            }
        }
        String customerIds = getPara("customerIds");
        String contactsIds = getPara("contactsIds");
        String businessIds = getPara("businessIds");
        String contractIds = getPara("contractIds");
        TaskRelation taskRelation = new TaskRelation();
        if(customerIds != null || contactsIds != null || businessIds != null || contractIds != null){
            taskRelation.setBusinessIds(TagUtil.fromString(businessIds));
            taskRelation.setContactsIds(TagUtil.fromString(contactsIds));
            taskRelation.setContractIds(TagUtil.fromString(contractIds));
            taskRelation.setCustomerIds(TagUtil.fromString(customerIds));
        }
        return taskService.setTask(task, taskRelation);
    }

    /**
     * 删除任务参与人
     * @param taskId
     * @param userId
     */
    public void deleteOwnerUser(@Para("taskId") Integer taskId,@Para("userId")Long userId){
        renderJson(taskService.deleteOwnerUser(taskId, userId));
    }

    @WorkPermissions({"work:project:setTaskOwnerUser"})
    public void deleteWorkTaskOwnerUser(@Para("taskId") Integer taskId,@Para("userId")Long userId){
        renderJson(taskService.deleteOwnerUser(taskId, userId));
    }

    /**
     *
     * @param taskId
     * @param labelId
     */
    @WorkPermissions({"work:project:setTaskLabel"})
    public void deleteLabel(@Para("taskId") Integer taskId, @Para("labelId") Integer labelId) {
        renderJson(taskService.deleteLabel(taskId, labelId));
    }


    /**
     * @author hmb
     * 查询任务列表
     */
    public void getTaskList(BasePageRequest basePageRequest){
        String labelId = getPara("labelId");
        String ishidden = getPara("ishidden");
        renderJson(taskService.getTaskList(basePageRequest, labelId, ishidden));
    }


    /**
     * @author hmb
     * 查询任务信息
     */
    public void queryTaskInfo(){
        String taskId = getPara("taskId");
        Integer number = Db.queryInt("select count(*) from 72crm_task where task_id = ?",taskId);
        if (number == 0){
            renderJson(R.error("已被删除"));
            return;
        }
        if (!BaseUtil.isAdmin() && !BaseUtil.getUser().getRoles().contains(BaseUtil.getWorkAdminRole())) {
            Task task = Task.dao.findById(taskId);
            boolean auth = true;
            if (Objects.equals(0, task.getWorkId())) {
                auth = AuthUtil.isOaAuth(OaEnum.TASK_TYPE_KEY, Integer.valueOf(taskId));
            } else {
                Work work = Work.dao.findById(task.getWorkId());
                if (TagUtil.toLongSet(work.getOwnerUserId()).contains(BaseUtil.getUserId()) || work.getIsOpen() == 1) {
                    auth = false;
                }
            }
            if (auth) {
                renderJson(R.noAuth());
                return;
            }
        }
        renderJson(taskService.queryTaskInfo(taskId));
    }

    /**
     * @author hmb
     * 回收站查询任务信息
     */
    public void queryTrashTaskInfo(){
        String taskId = getPara("taskId");
        boolean oaAuth = AuthUtil.isOaAuth(OaEnum.TASK_TYPE_KEY, Integer.valueOf(taskId));
        if(oaAuth){renderJson(R.noAuth());return;}
        renderJson(taskService.getTaskInfo(taskId));
    }
    /**
     * 查询任务列表 oa
     */
    @SysLog({LogEnum.MY_TASK,LogEnum.SUB_TASK})
    public void queryTaskList(BasePageRequest<Task> basePageRequest){
        renderJson(taskService.queryTaskList(basePageRequest, false));
    }

    /**
     * 任务导出 oa
     */
    public void oaTaskExport(BasePageRequest<Task> basePageRequest) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) taskService.queryTaskList(basePageRequest, true).get("data");
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("name", "任务名称");
            writer.addHeaderAlias("description", "任务描述");
            writer.addHeaderAlias("main_user_name", "负责人");
            writer.addHeaderAlias("start_time", "开始时间");
            writer.addHeaderAlias("stop_time", "结束时间");
            writer.addHeaderAlias("label_name", "标签");
            writer.addHeaderAlias("owner_user_name", "参与人");
            writer.addHeaderAlias("priority", "优先级");
            writer.addHeaderAlias("create_user_name", "创建人");
            writer.addHeaderAlias("create_time", "创建时间");
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.merge(10, "办公任务信息");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 11; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=OaTask.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出办公任务信息错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * @author zxy
     * 根据任务id查询活动日志 oa
     * taskId 任务id
     */
    public void queryTaskLog(){
        Integer taskId = getParaToInt("taskId");
        if (!BaseUtil.isAdmin() && !BaseUtil.getUser().getRoles().contains(BaseUtil.getWorkAdminRole())) {
            Task task = Task.dao.findById(taskId);
            boolean auth = true;
            if (Objects.equals(0, task.getWorkId())) {
                auth = AuthUtil.isOaAuth(OaEnum.TASK_TYPE_KEY, taskId);
            } else {
                Work work = Work.dao.findById(task.getWorkId());
                if (TagUtil.toLongSet(work.getOwnerUserId()).contains(BaseUtil.getUserId()) || work.getIsOpen() == 1) {
                    auth = false;
                }
            }
            if (auth) {
                renderJson(R.noAuth());
                return;
            }
        }
        renderJson(taskService.queryWorkTaskLog(taskId));
    }

    /**
     * @author zxy
     * 添加任务与业务关联
     */
    public void saveTaskRelation(@Para("") TaskRelation taskRelation){
        renderJson(taskService.saveTaskRelation(taskRelation, BaseUtil.getUser().getUserId()));
    }

    @WorkPermissions({"work:project:saveTaskRelation"})
    public void saveWorkTaskRelation(@Para("") TaskRelation taskRelation){
        renderJson(taskService.saveTaskRelation(taskRelation, BaseUtil.getUser().getUserId()));
    }

    /**
     * @author hmb
     * 删除任务
     * taskId 任务id
     */
    public void deleteTask(){
        Integer taskId = getParaToInt("taskId");
        renderJson(taskService.deleteTask(taskId));
    }

    @WorkPermissions({"work:project:deleteTask"})
    public void deleteWorkTask(){
        Integer taskId = getParaToInt("taskId");
        renderJson(taskService.deleteTask(taskId));
    }

    @WorkPermissions({"work:project:deleteChildTask"})
    public void deleteWorkChildTask(){
        Integer taskId = getParaToInt("taskId");
        renderJson(taskService.deleteTask(taskId));
    }

    /**
     * @author wyq
     * crm查询关联任务
     */
    public void queryTaskRelation(@Para("") BasePageRequest<TaskRelation> basePageRequest){
        renderJson(taskService.queryTaskRelation(basePageRequest));
    }

    /**
     * @author hmb
     * 根据任务id归档任务
     *
     * @param taskId 任务ID
     */
    @WorkPermissions({"work:project:archiveTask"})
    public void archiveByTaskId(@Para("taskId") Integer taskId){
        renderJson(taskService.archiveByTaskId(taskId));
    }

    /**
     * 上传任务附件
     */
    @WorkPermissions({"work:project:uploadTaskFile"})
    public void uploadWorkTaskFile(){
        String prefix = BaseUtil.getDate();
        renderJson(Aop.get(AdminFileService.class).upload(getFile("file", prefix), getPara("batchId"), getPara("type"), prefix));
    }

    /**
     * 删除任务附件
     */
    @WorkPermissions({"work:project:deleteTaskFile"})
    public void deleteWorkTaskFile(){
        renderJson(Aop.get(AdminFileService.class).removeById(getPara("id")));
    }
}
