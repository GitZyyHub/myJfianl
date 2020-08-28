package com.kakarote.crm9.erp.work.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.RequestBody;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.annotation.WorkPermissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.WorkService;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hmb
 */
public class WorkController extends Controller {

    @Inject
    private WorkService workService;

    /**
     * @author hmb
     * 设置项目
     * @param work 项目对象
     */
    @Permissions({"project:projectManage:save"})
    public void setWork(@Para("") Work work){
        renderJson(workService.setWork(work));
    }

    @WorkPermissions({"work:project:setWork"})
    public void updateWork(@Para("") Work work) {
        renderJson(workService.setWork(work));
    }

    @SysLog(LogEnum.PROJECT)
    public void getWorkById(){
        String workId = getPara("workId");
        renderJson(workService.getWorkById(workId));
    }

    /**
     * @author hmb
     * 删除项目
     */
    @WorkPermissions({"work:project:setWork"})
    public void deleteWork(){
        Integer workId = getParaToInt("workId");
        renderJson(workService.deleteWork(workId));
    }
    /**
     * @author hmb
     * 查询项目名列表
     */
    public void queryWorkNameList(){
        renderJson(workService.queryWorkNameList());
    }
    /**
     * @author hmb
     * 根据项目id查询任务板
     */
    public void queryTaskByWorkId(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(workService.queryTaskByWorkId(jsonObject));
    }

    /**
     * 根据项目id查询任务板（负责人视图）
     */
    public void queryOwnerTaskListByWorkId() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(workService.queryOwnerTaskListByWorkId(jsonObject));
    }

    /**
     * @author hmb
     * 根据项目id查询项目附件
     */
    public void queryTaskFileByWorkId(BasePageRequest<JSONObject> pageRequest){
        renderJson(workService.queryTaskFileByWorkId(pageRequest));
    }

    /**
     * @author hmb
     * 查询归档项目列表
     */
    @SysLog(LogEnum.PROJECT_ARCHIVE)
    public void queryArchiveWorkList(BasePageRequest pageRequest){
        renderJson(workService.queryArchiveWorkList(pageRequest));
    }

    /**
     * @author hmb
     * 项目统计
     */
    @SysLog(LogEnum.PROJECT_STATISTICS)
    public void workStatistics(){
        String workId = getPara("workId");
        renderJson(workService.workStatistics(workId));
    }

    /**
     * @author hmb
     * 查询项目成员
     */
    public void queryWorkOwnerList(){
        String workId = getPara("workId");
        renderJson(workService.queryWorkOwnerList(workId));
    }

    /**
     * @author hmb
     * 修改项目任务排序
     */
    @WorkPermissions({"work:project:setTaskOrder"})
    public void updateOrder(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(workService.updateOrder(jsonObject));
    }

    /**
     * @author hmb
     * 退出项目
     */
    public void leave(){
        Integer workId = getParaToInt("workId");
        Long userId = BaseUtil.getUserId();
        renderJson(workService.leave(workId,userId));
    }

    /**
     * @author hmb
     * 删除项目成员
     */
    @WorkPermissions({"work:project:setWork"})
    public void removeWorkOwnerUser(){
        Integer workId = getParaToInt("workId");
        Long userId = getLong("ownerUserId");
        renderJson(workService.leave(workId,userId));
    }

    /**
     * 查询项目管理角色列表
     * @author wyq
     */
    public void queryRoleList(){
        renderJson(workService.queryRoleList());
    }

    /**
     * @author wyq
     * 查询项目设置成员管理列表
     */
    public void queryOwnerRoleList(@Para("workId")Integer workId){
        renderJson(workService.queryOwnerRoleList(workId));
    }

    /**
     * @author wyq
     * 保存项目角色管理设置
     */
    public void setOwnerRole(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workService.setOwnerRole(jsonObject));
    }

    /**
     * @author hmb
     * 删除任务列表
     */
    @WorkPermissions({"work:project:deleteTaskClass"})
    public void deleteTaskList(){
        Integer classId = getParaToInt("classId");
        Integer workId = getParaToInt("workId");
        renderJson(workService.deleteTaskList(workId,classId));
    }

    /**
     * @author hmb
     * 归档已完成的任务
     */
    public void archiveTask(){
        String classId = getPara("classId");
        renderJson(workService.archiveTask(classId));
    }

    public void archiveTaskByOwner(@Para("userId") Long userId, @Para("workId") Integer workId) {
        renderJson(workService.archiveTask(userId, workId));
    }

    /**
     * @author hmb
     * 归档任务
     */
    public void archList(){
        String workId = getPara("workId");
        renderJson(workService.archList(workId));
    }

    /**
     * @author hmb
     * 移除项目成员
     */
    public void remove(@Para("ownerUserId") Long ownerUserId,@Para("workId") Integer workId){
        renderJson(workService.remove(ownerUserId,workId));
    }

    /**
     * @author hmb
     * 项目class排序
     */
    @WorkPermissions({"work:project:updateClassOrder"})
    public void updateClassOrder(){
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(workService.updateClassOrder(jsonObject));
    }


    /**
     * 撤销任务归档
     */
    public void activation(@Para("taskId") Integer taskId){
        renderJson(workService.activation(taskId));
    }

    /**
     * @author wyq
     * 查询有项目模块查看权限的员工
     */
    public void queryProjectUser(){
        renderJson(workService.queryProjectUser());
    }

    /**
     * 项目收藏
     */
    public void collect(@Para("workId") Integer workId){
        renderJson(workService.collect(workId));
    }

    /**
     *修改项目排序
     */
    public void updateWorkOrder(@RequestBody JSONObject data){
        List<Integer> workIds = data.getJSONArray("workIds").toJavaList(Integer.class);
        workService.updateWorkOrder(workIds);
        renderJson(R.ok());
    }

    @WorkPermissions({"work:project:excelExport"})
    public void workTaskExport(@Para("workId") Integer workId) {
        List<Map<String, Object>> list = workService.workTaskExport(workId);
        Aop.get(ActionRecordUtil.class).addExportActionRecord(CrmEnum.TASK, list.size());
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
            writer.addHeaderAlias("class_name", "所在任务列表");
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.merge(11, "项目任务信息");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 12; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=workTask.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出项目任务信息错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 导入模板
     */
    public void downloadExcel() {
        List<Record> recordList = new LinkedList<>();
        recordList.add(new Record().set("name", "任务名称").set("is_null", 1).set("type", 1));
        recordList.add(new Record().set("name", "任务描述").set("is_null", 0).set("type", 1));
        recordList.add(new Record().set("name", "开始时间").set("is_null", 0).set("type", 4));
        recordList.add(new Record().set("name", "结束时间").set("is_null", 0).set("type", 4));
        recordList.add(new Record().set("name", "负责人").set("is_null", 0).set("type", 1));
        recordList.add(new Record().set("name", "参与人").set("is_null", 0).set("type", 1));
        recordList.add(new Record().set("name", "所属任务列表").set("is_null", 1).set("type", 1));
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("任务导入表");
        sheet.setDefaultRowHeight((short) 400);
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < recordList.size(); i++) {
            if (Objects.equals(recordList.get(i).getInt("type"), FormTypeEnum.DATE.getType())) {
                CellStyle dateStyle = wb.createCellStyle();
                DataFormat dateFormat = wb.createDataFormat();
                dateStyle.setDataFormat(dateFormat.getFormat(DatePattern.NORM_DATE_PATTERN));
                sheet.setDefaultColumnStyle(i, dateStyle);
            } else {
                sheet.setDefaultColumnStyle(i, textStyle);
            }
            sheet.setColumnWidth(i, 20 * 256);
        }
        CellStyle cellStyle = wb.createCellStyle();
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeight((short) (6 * 256));
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //开启自动换行
        cellStyle.setWrapText(true);
        HSSFRichTextString title = new HSSFRichTextString("任务导入模板\n(*)为必填项\n负责人必须为系统用户\n如果项目内有2个名称相同的任务列表名默认取第一个，如果列表名不存在会创建新的列表\n多个参与人时分隔符请使用英文逗号");
        //一个单元格内设置两种字体风格
        Font firstFont = wb.createFont();
        firstFont.setFontHeightInPoints((short) 16);
        firstFont.setBold(true);
        Font secondFont = wb.createFont();
        secondFont.setFontHeightInPoints((short) 10);
        title.applyFont(0, 6, firstFont);
        title.applyFont(6, title.length(), secondFont);
        titleRow.createCell(0).setCellValue(title);
        titleRow.getCell(0).setCellStyle(cellStyle);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, recordList.size() - 1);
        sheet.addMergedRegion(region);
        try {
            HSSFRow row = sheet.createRow(1);
            for (int i = 0; i < recordList.size(); i++) {
                Record record = recordList.get(i);
                // 在第一行第一个单元格，插入选项
                HSSFCell cell = row.createCell(i);
                // 普通写入操作
                if (record.getInt("is_null") == 1) {
                    cell.setCellValue(record.getStr("name") + "(*)");
                } else {
                    cell.setCellValue(record.getStr("name"));
                }
            }
            HttpServletResponse response = getResponse();

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=task_import.xls");
            wb.write(response.getOutputStream());

        } catch (Exception e) {
            Log.getLog(getClass()).error("error", e);
        } finally {
            try {
                wb.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        renderNull();
    }

    /**
     * excel导入任务
     */
    @WorkPermissions({"work:project:excelImport"})
    public void excelImport() {
        UploadFile file = getFile("file", "/excel/");
        Integer workId = getParaToInt("workId");
        if (ObjectUtil.isEmpty(workId)) {
            renderJson(R.error("项目id不能为空"));
            return;
        }
        renderJson(workService.excelImport(file, workId));
    }

    /**
     * 下载错误模板
     */

    public void downloadErrorExcel() {
        String token = get("token");
        File file = new File(BaseConstant.UPLOAD_PATH + "excel/" + token);
        if (file.exists()) {
            renderFile(file, "import_error.xlsx");
        } else {
            renderNull();
        }
    }
}
