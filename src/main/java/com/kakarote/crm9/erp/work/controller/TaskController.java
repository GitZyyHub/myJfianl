package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.CrmExcelUtil;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.TaskService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.tio.utils.jfinal.P;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class TaskController extends Controller {

    @Inject
    TaskService taskService;

    @Inject
    AdminFieldService adminFieldService;

    /**
     * 设置任务(添加/编辑)!!
     */
    @Permissions({"project:task:save","project:task:update","project:task:createSubtasks"})
    public void setTask(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(taskService.setTask(jsonObject));
    }

    /**
     * 分页查询任务
     */
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(taskService.queryPageList(basePageRequest,false));
    }

    /**
     * 根据主任务ID查询已经提报工时的子任务
     */
    public void querySubtasksByTaskHours(BasePageRequest basePageRequest){
        renderJson(taskService.querySubtasksByTaskHours(basePageRequest));
    }

    /**
     * 查询待办任务
     */
    public void waiting(BasePageRequest basePageRequest){
        renderJson(taskService.waiting(basePageRequest,false));
    }

    /**
     * 查询我的任务
     */
    public void myTask(BasePageRequest basePageRequest){
        renderJson(taskService.waiting(basePageRequest,false));
    }

    /**
     * 根据用户ID查询任务
     */
    public void userTask(BasePageRequest basePageRequest){
        renderJson(taskService.userTask(basePageRequest));
    }

    /**
     * 查询任务详情信息
     */
    @Permissions({"project:task:readDetails"})
    public void findFieldDetails(@Para("id")Integer id){
        renderJson(taskService.findFieldDetails(id));
    }

    /**
     * 查询子任务详情信息
     */
    public void subtasksFindFieldDetails(@Para("id")Integer id){
        renderJson(taskService.subtasksFindFieldDetails(id));
    }

    /**
     * 查询任务的项目信息!!!
     */
    @Permissions({"project:taskWorkInformation:details"})
    public void queryWorkByTaskId(@Para("taskId")Integer taskId){
        renderJson(taskService.queryWorkByTaskId(taskId));
    }

    /**
     * 根据Id查询任务
     */
    public void queryById(@Para("id") Integer id){
        renderJson(R.ok().put("data",taskService.queryById(id)));
    }

    /**
     * 查询任务下所有资料
     */
    public void findTaskFileById(BasePageRequest basePageRequest){
        renderJson(taskService.findTaskFileById(basePageRequest));
    }

    /**
     * 删除检查
     */
    public void delTaskCheck(@Para("ids")String ids){
        renderJson(taskService.delTaskCheck(ids));
    }
    /**
     * 批量删除id
     */
    @Permissions({"project:task:delete"})
    public void delTask(@Para("ids")String ids){
        renderJson(taskService.delTask(ids));
    }

    /**
     * 查询对应任务的参与人员
     */
    public void findOwnerUser(@Para("taskId") String taskId){
        renderJson(taskService.findOwnerUser(taskId));
    }

    /**
     * 批量添加参与人
     */
    @Permissions({"project:task:saveParticipant"})
    public void addOwnerUser(@Para("ids") String taskId,@Para("memberIds")String users){
        renderJson(taskService.addOwnerUser(taskId,users));
    }

    /**
     * 批量删除参与人
     */
    @Permissions({"project:task:delParticipant"})
    public void delOwnerUser(@Para("ids") String taskId,@Para("memberIds")String users){
        renderJson(taskService.delOwnerUser(taskId,users));
    }


    /**
     * 导入
     */
    public void excelImport(@Para("file") UploadFile file,@Para("ownerUserId") Long ownerUserId,@Para("repeatHandling") Integer repeatHandling,@Para("workId")String workId) {
        Long messageId = new CrmExcelUtil().addWork(CrmEnum.TASK, file.getFile().getAbsolutePath(), null, null, repeatHandling,workId);
        renderJson(R.ok().put("data",messageId));
    }
    /**
     * 下载导入模版
     */
    public void updateTaskTemplate(){
        List<Record> fields = adminFieldService.getFields(CrmEnum.TASK.getType(), null);
        for (int i = 0; i < fields.size(); i++) {
            Record record = fields.get(i);
            String filed_name = record.getStr("field_name");
            if("workName".equals(filed_name)){
                fields.get(i).set("name","项目编号");
            }else if("beforeId".equals(filed_name)){
                fields.get(i).set("name","前置任务编号");
            }else if("milestoneId".equals(filed_name)){
                fields.get(i).set("name","里程碑编号");
            }
        }
        for (Record field : fields) {

        }
        //列标题
        List<String> handers = new ArrayList<>();
        //下拉框数据(数组)
        List<String[]> downData = new ArrayList<>();
        //下拉列的序号
        List<String> downRows = new ArrayList<>();

        int temp = 0;
        for (Record field : fields) {
            handers.add(field.getStr("name"));
            if(Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()){
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp+"");
            }
            temp++;
        }
        try(HSSFWorkbook wb = com.kakarote.crm9.utils.ExcelUtil.createExcelTemplate("任务导入模版",handers, downData, downRows,null,null);) {

            HttpServletResponse response = getResponse();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=task_import.xls");

            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * 导出任务
     */
    @Permissions({"project:task:excelexport"})
    public void taskExport(BasePageRequest<Work> basePageRequest){
        JSONObject json = basePageRequest.getJsonObject();
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.TASK.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject ;
            if(json.get("isWaiting")!=null){
                jsonObject = (JSONObject)taskService.waiting(basePageRequest, false).get("data");
            }else {
                jsonObject = (JSONObject)taskService.queryPageList(basePageRequest, false).get("data");
            }
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"batch_id","task_id","before_id","main_user_id","milestone_id","owner_user_id","p_name","pid"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.TASK.getType()));

            headList.forEach(head -> {
                if (!Objects.equals(head.getStr("fieldName"), head.getStr("name"))) {
                    writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("fieldName")), head.getStr("name"));
                } else {
                    writer.addHeaderAlias(head.getStr("fieldName"), head.getStr("name"));
                }
            });
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);

            for (int i = 0; i < headList.size(); i++) {
                writer.setColumnWidth(i, 25);
            }

            HttpServletResponse response = getResponse();

            response.setCharacterEncoding("UTF-8");

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=task.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }


    /*--------------子任务-------------------*/
    /**
     * 设置任务(添加/编辑)!!!
     */
    @Permissions({"project:subtasks:save","project:subtasks:update"})
    public void setSubtasks(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(taskService.setTask(jsonObject));
    }

    /**
     * 分页查询任务
     */
    public void queryPageListSubtasks(BasePageRequest basePageRequest){
        renderJson(taskService.queryPageList(basePageRequest,false));
    }

    /**
     * 批量删除id
     */
    @Permissions({"project:subtasks:delete"})
    public void delSubtasks(@Para("ids")String ids){
        renderJson(taskService.delTask(ids));
    }

    @Permissions({"project:subtasks:excelexport"})
    public void subtasksExport(@Para("taskIds") String taskIds){
        List<String> strings = null;
        if(!StrUtil.isEmpty(taskIds)){
            strings = Arrays.asList(taskIds.split(","));
        }
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {

            List<Record> records = Db.find(Db.getSqlPara("work.task.querySubtasksExport", Kv.by("taskIds", strings)));

            List<Map<String, Object>> list = new ArrayList<>();
            records.forEach(record -> {
                list.add(record.getColumns());
            });
            List<Record> headList = adminFieldService.queryListHead(new AdminFieldSort().setLabel(29));
            // List<Record> headList = adminFieldService.getFields(CrmEnum.WORK.getType(), null);
            headList.forEach(head -> {
                if (!Objects.equals(head.getStr("fieldName"), head.getStr("name"))) {
                    writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("fieldName")), head.getStr("name"));
                } else {
                    writer.addHeaderAlias(head.getStr("fieldName"), head.getStr("name"));
                }
            });

            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);

            for (int i = 0; i < headList.size(); i++) {
                writer.setColumnWidth(i, 25);
            }

            HttpServletResponse response = getResponse();

            response.setCharacterEncoding("UTF-8");

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=task.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }
}
