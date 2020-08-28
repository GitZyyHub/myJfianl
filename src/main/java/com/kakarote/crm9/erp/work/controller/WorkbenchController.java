package com.kakarote.crm9.erp.work.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.work.service.WorkbenchService;
import com.kakarote.crm9.utils.BaseUtil;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 工作台
 */
public class WorkbenchController extends Controller{

    @Inject
    private WorkbenchService workbenchService;

    /**
     * @author hmb
     * 我的任务
     */
    @SysLog(LogEnum.PROJECT_WORKBENCH)
    public void myTask(){
        renderJson(workbenchService.myTask(BaseUtil.getUser().getUserId()));
    }

    public void workBenchTaskExport() {
        List<Map<String, Object>> list = workbenchService.workBenchTaskExport();
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
            writer.addHeaderAlias("is_top", "所在任务列表");
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.merge(11, "工作台任务信息");
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
            response.setHeader("Content-Disposition", "attachment;filename=myTask.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出工作台任务信息错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * @author hmb
     * 任务日历
     */
    public void dateList(){
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        renderJson(workbenchService.dateList(startTime,endTime));
    }

    /**
     * @author hmb
     * 修改工作台任务排序
     */
    public void updateTop(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(workbenchService.updateTop(jsonObject));
    }
}
