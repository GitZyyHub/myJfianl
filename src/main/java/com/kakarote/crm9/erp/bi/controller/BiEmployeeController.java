package com.kakarote.crm9.erp.bi.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.bi.service.BiEmployeeService;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wyq
 */
public class BiEmployeeController extends Controller {
    @Inject
    BiEmployeeService biEmployeeService;

    /**
     * 合同数量分析
     * @author wyq
     */
    @Permissions("bi:contract:read")
    @NotNullValidate(value = "year",message = "年份不能为空")
    @SysLog(LogEnum.CONTRACT_COUNT)
    public void contractNumStats(@Para("")BiParams biParams){
        biParams.setType("contractNum");
        renderJson(biEmployeeService.contractNumStats(biParams));
    }

    public void contractNumStatsExport(@Para("") BiParams biParams) {
        biParams.setType("contractNum");
        List<Record> recordList = (ArrayList<Record>) biEmployeeService.contractNumStats(biParams).get("data");
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        Map<String, Object> thisMonth = new HashMap<>();
        thisMonth.put("type", "当月合同数量(个)");
        Map<String, Object> lastMonthGrowth = new HashMap<>();
        lastMonthGrowth.put("type", "环比增长（%）");
        Map<String, Object> lastYearGrowth = new HashMap<>();
        lastYearGrowth.put("type", "同比增长（%）");
        recordList.forEach(record -> {
            monthList.add(record.getStr("month"));
            thisMonth.put(record.getStr("month"), record.getStr("thisMonth"));
            lastMonthGrowth.put(record.getStr("month"), record.getStr("lastMonthGrowth"));
            lastYearGrowth.put(record.getStr("month"), record.getStr("lastYearGrowth"));
        });
        list.add(thisMonth);
        list.add(lastMonthGrowth);
        list.add(lastYearGrowth);
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("type", "日期");
            monthList.forEach(month -> writer.addHeaderAlias(month, month));
            writer.merge(12, "合同数量分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 13; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=contractNumStats.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出合同数量分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 合同金额分析
     * @author wyq
     */
    @Permissions("bi:contract:read")
    @SysLog(LogEnum.CONTRACT_MONEY)
    public void contractMoneyStats(@Para("")BiParams biParams){
        biParams.setType("contractMoney");
        renderJson(biEmployeeService.contractNumStats(biParams));
    }

    public void contractMoneyStatsExport(@Para("") BiParams biParams) {
        biParams.setType("contractMoney");
        List<Record> recordList = (ArrayList<Record>) biEmployeeService.contractNumStats(biParams).get("data");
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        Map<String, Object> thisMonth = new HashMap<>();
        thisMonth.put("type", "当月合同金额(元)");
        Map<String, Object> lastMonthGrowth = new HashMap<>();
        lastMonthGrowth.put("type", "环比增长（%）");
        Map<String, Object> lastYearGrowth = new HashMap<>();
        lastYearGrowth.put("type", "同比增长（%）");
        recordList.forEach(record -> {
            monthList.add(record.getStr("month"));
            thisMonth.put(record.getStr("month"), record.getStr("thisMonth"));
            lastMonthGrowth.put(record.getStr("month"), record.getStr("lastMonthGrowth"));
            lastYearGrowth.put(record.getStr("month"), record.getStr("lastYearGrowth"));
        });
        list.add(thisMonth);
        list.add(lastMonthGrowth);
        list.add(lastYearGrowth);
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("type", "日期");
            monthList.forEach(month -> writer.addHeaderAlias(month, month));
            writer.merge(12, "合同金额分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 13; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=contractMoneyStats.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出合同金额分析错误：", e);
        } finally {
            // 关闭writer，释放内存

        }
        renderNull();
    }

    /**
     * 回款金额分析
     * @author wyq
     */
    @Permissions("bi:contract:read")
    @SysLog(LogEnum.CONTRACT_BACK_MONEY)
    public void receivablesMoneyStats(@Para("")BiParams biParams){
        biParams.setType("receivables");
        renderJson(biEmployeeService.contractNumStats(biParams));
    }

    public void receivablesMoneyStatsExport(@Para("") BiParams biParams) {
        biParams.setType("receivables");
        List<Record> recordList = (ArrayList<Record>) biEmployeeService.contractNumStats(biParams).get("data");
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        Map<String, Object> thisMonth = new HashMap<>();
        thisMonth.put("type", "当月回款金额(元)");
        Map<String, Object> lastMonthGrowth = new HashMap<>();
        lastMonthGrowth.put("type", "环比增长（%）");
        Map<String, Object> lastYearGrowth = new HashMap<>();
        lastYearGrowth.put("type", "同比增长（%）");
        recordList.forEach(record -> {
            monthList.add(record.getStr("month"));
            thisMonth.put(record.getStr("month"), record.getStr("thisMonth"));
            lastMonthGrowth.put(record.getStr("month"), record.getStr("lastMonthGrowth"));
            lastYearGrowth.put(record.getStr("month"), record.getStr("lastYearGrowth"));
        });
        list.add(thisMonth);
        list.add(lastMonthGrowth);
        list.add(lastYearGrowth);
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("type", "日期");
            monthList.forEach(month -> writer.addHeaderAlias(month, month));
            writer.merge(12, "回款金额分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 13; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=receivablesMoneyStats.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出回款金额分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 合同汇总表
     * @author wyq
     */
    @Permissions("bi:contract:read")
    @SysLog(LogEnum.CONTRACT_SUMMARY)
    public void totalContract(@Para("")BiParams biParams){
        renderJson(biEmployeeService.totalContract(biParams));
    }

    public void totalContractExport(@Para("") BiParams biParams) {
        Record record = (Record) biEmployeeService.totalContract(biParams).get("data");
        List<Record> recordList = (ArrayList<Record>) record.get("list");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(r -> list.add(r.getColumns()));
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("type", "日期");
            writer.addHeaderAlias("contractNum", "签约合同数（个）");
            writer.addHeaderAlias("contractMoney", "签约合同金额（元）");
            writer.addHeaderAlias("receivablesMoney", "回款金额（元）");
            writer.merge(3, "合同汇总表");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 4; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=totalContract.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出合同汇总表错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 合同详情
     */
    public void contractByMonth(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biEmployeeService.contractByMonth(basePageRequest));
    }

    /**
     * 回款详情
     */
    public void receivablesByMonth(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biEmployeeService.receivablesByMonth(basePageRequest));
    }

    /**
     * 合同关联的回款
     */
    public void receivablesByContract(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biEmployeeService.receivablesByContract(basePageRequest));
    }
}
