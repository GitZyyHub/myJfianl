package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.entity.TaskFile;
import com.kakarote.crm9.erp.work.entity.TaskHoursRecord;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.TaskFileService;
import com.kakarote.crm9.erp.work.service.TaskHoursService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author zyy
 */
public class TaskHoursController extends Controller {


    @Inject
    TaskHoursService taskHoursService;

    @Inject
    AdminFieldService adminFieldService;

    @Inject
    TaskFileService taskFileService;

    /**
     * 分页查询任务工时
     */
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(taskHoursService.queryPageList(basePageRequest,false));
    }

    /**
     * 创建工时提报
     */
    @Permissions({"project:taskHours:save","project:taskHours:update"})
    public void setTaskHours(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(taskHoursService.setTaskHours(jsonObject));
    }

    /**
     * 根据任务id查询工时状态
     */
    public void queryStatusByTaskId(@Para("taskId")String taskId){
        renderJson(R.ok().put("data",Db.queryStr("select (case check_status when 0 then '待审核' when 1 then '通过' when 2 then '未通过' end) check_status from 72crm_task_hours where subtasks_id=?",taskId)));
    }

    /**
     * 分页查询工时导出记录
     */
    @Permissions({"project:taskHours:exportRecord"})
    public void queryHoursRecord(BasePageRequest basePageRequest){
        renderJson(taskHoursService.queryHoursRecord(basePageRequest));
    }

    /**
     * 工时撤回（删除）
     */
    @Permissions({"project:taskHours:withdraw"})
    public void delHours(String hoursIds){
        renderJson(taskHoursService.delHours(hoursIds));
    }

    /**
     * 上传成果
     * @param taskFile
     */
    @Permissions({"project:taskHours:uploadResults"})
    public void upload(@Para("") TaskFile taskFile) {
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(taskFileService.upload(jsonObject));
    }


    /**
     * 工时导出-分页查询任务工时
     */
    /*public void queryPageListhoursExport(BasePageRequest basePageRequest){
        renderJson(taskHoursService.queryPageList(basePageRequest,false));
    }*/

    /**
     * ???导出记录列表
     */
    @Permissions({"project:hoursExcelexport:index"})
    public void hoursRecordList(BasePageRequest basePageRequest){
        renderJson(taskHoursService.hoursRecordList(basePageRequest));
    }

    /**
     * ???导出工时导出记录
     */
    @Permissions({"project:hoursExcelexport:excelexport"})
    public void hoursRecordExport(@Para("workIds")String workIds){
        List<String> strings = null;
        if(!StrUtil.isEmpty(workIds)){
            strings = Arrays.asList(workIds.split(","));
        }

        //添加导出记录
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {

            List<Record> records = Db.find(Db.getSqlPara("task.hours.hoursRecordExport", Kv.by("workIds", strings)));

            List<Map<String, Object>> list = new ArrayList<>();
            records.forEach(record -> {
                list.add(record.getColumns());
            });

            List<Record> headList = new ArrayList<>();
            addField(headList,false);

            headList.forEach(head -> {
                if (!Objects.equals(head.getStr("field_name"), head.getStr("name"))) {
                    writer.addHeaderAlias(StrUtil.toUnderlineCase(head.getStr("field_name")), head.getStr("name"));
                } else {
                    writer.addHeaderAlias(head.getStr("field_name"), head.getStr("name"));
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
            response.setHeader("Content-Disposition", "attachment;filename=hoursExportList.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * 导出工时???
     */
    @Permissions({"project:taskHours:excelexport"})
    @Before(Tx.class)
    public void hoursExport(BasePageRequest<Work> basePageRequest,@Para("hoursIds") String hoursIds){
        /*List<String> strings = null;
        if(!StrUtil.isEmpty(hoursIds)){
            strings = Arrays.asList(hoursIds.split(","));
        }

        //添加导出记录
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {

            List<Record> records = Db.find(Db.getSqlPara("task.hours.queryHoursByIdExport", Kv.by("hoursIds", strings).set("taskId",taskId)));

            List<Map<String, Object>> list = new ArrayList<>();
            records.forEach(record -> {
                list.add(record.getColumns());
            });

            List<Record> headList = new ArrayList<>();
            addField(headList,true);*/
        JSONObject json = basePageRequest.getJsonObject();
        String taskId = json.getString("taskId");
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.TASK_HOURS.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = (JSONObject)taskHoursService.queryPageList(basePageRequest, false).get("data");
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"check_user_id","create_user_id","hours_id","main_user_id","subtasks_id","task_id"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = Aop.get(AdminFieldService.class).queryListHead(new AdminFieldSort().setLabel(CrmEnum.TASK_HOURS.getType()));

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

            Integer workId = Db.queryInt("select work_id from 72crm_task where task_id=?", taskId);
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            String format = simpleDateFormat.format(date);
            String str = format.substring(2);
            TaskHoursRecord taskHoursRecord = new TaskHoursRecord();
            taskHoursRecord.setWorkId(workId);
            taskHoursRecord.setCreateUserId(BaseUtil.getUserId());
            taskHoursRecord.setCreateTime(date);
            if(!StrUtil.isEmpty(taskId)){
                taskHoursRecord.setTaskId(Long.parseLong(taskId));
            }
            String time = str + taskId ;
            taskHoursRecord.setFileName(time);
            taskHoursRecord.save();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename="+time+".xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);


        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }
    private void addField(List<Record> headList,boolean tag){
        if(tag){
            headList.add(new Record().set("field_name", "hours_id").set("name", "序号"));
            headList.add(new Record().set("field_name", "create_time").set("name", "发起时间"));
            headList.add(new Record().set("field_name", "examine_time").set("name", "审批时间"));
            headList.add(new Record().set("field_name", "check_status").set("name", "审核结果"));
            headList.add(new Record().set("field_name", "main_user_name").set("name", "负责人名称"));
            headList.add(new Record().set("field_name", "main_user_area").set("name", "负责人区域"));
            headList.add(new Record().set("field_name", "main_user_dept").set("name", "负责人部门"));
            headList.add(new Record().set("field_name", "work_name").set("name", "项目名称"));
            headList.add(new Record().set("field_name", "work_num").set("name", "项目编号"));
            headList.add(new Record().set("field_name", "work_area").set("name", "项目区域"));
            headList.add(new Record().set("field_name", "task_name").set("name", "任务名称"));
            headList.add(new Record().set("field_name", "task_num").set("name", "任务编号"));
            headList.add(new Record().set("field_name", "subtasks_name").set("name", "子任务名称"));
            headList.add(new Record().set("field_name", "subtasks_num").set("name", "子任务编号"));
            headList.add(new Record().set("field_name", "working_hours").set("name", "工时"));
        }else {
            headList.add(new Record().set("field_name", "create_time").set("name", "导出时间"));
            headList.add(new Record().set("field_name", "file_name").set("name", "导出文件名称"));
            headList.add(new Record().set("field_name", "create_name").set("name", "导出人员"));
        }
    }

}
