package com.kakarote.crm9.erp.bi.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.bi.service.BiCustomerService;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wyq
 */
public class BiCustomerController extends Controller {
    @Inject
    BiCustomerService biCustomerService;

    /**
     * 客户总量分析柱状图
     */
    @Permissions("bi:customer:read")
    public void totalCustomerStats(@Para("")BiParams biParams){
        renderJson(biCustomerService.totalCustomerStats(biParams));
    }

    /**
     * 客户总量分析表
     * @author wyq
     */
    @Permissions("bi:customer:read")
    @SysLog(LogEnum.CUSTOMER_TOTAL)
    public void totalCustomerTable(@Para("")BiParams biParams){
        renderJson(biCustomerService.totalCustomerTable(biParams));
    }

    public void totalCustomerTableExport(@Para("") BiParams biParams) {
        Kv kv = (Kv) biCustomerService.totalCustomerTable(biParams).get("data");
        List<Record> recordList = (ArrayList<Record>) kv.get("list");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        Record total = (Record) kv.get("total");
        total.set("dealCustomerRate", "");
        list.add(total.getColumns());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("realname", "员工姓名");
            writer.addHeaderAlias("customerNum", "新增客户数");
            writer.addHeaderAlias("dealCustomerNum", "成交客户数");
            writer.addHeaderAlias("dealCustomerRate", "客户成交率");
            writer.addHeaderAlias("contractMoney", "合同总金额");
            writer.addHeaderAlias("receivablesMoney", "回款金额");
            writer.merge(5, "客户总量分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 6; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=totalCustomerTable.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出客户总量分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 新增客户详情
     */
    @Permissions("bi:customer:read")
    public void increaseCustomer(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biCustomerService.increaseCustomer(basePageRequest));
    }

    /**
     * 成交客户详情
     */
    @Permissions("bi:customer:read")
    public void dealCustomer(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biCustomerService.dealCustomer(basePageRequest));
    }

    /**
     * 合同金额详情
     */
    @Permissions("bi:customer:read")
    public void totalContractMoney(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biCustomerService.totalContractMoney(basePageRequest));
    }

    /**
     * 回款金额详情
     */
    @Permissions("bi:customer:read")
    public void totalReceivablesMoney(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biCustomerService.totalReceivablesMoney(basePageRequest));
    }

    /**
     * 客户跟进次数分析
     * @author wyq
     */
    @Permissions("bi:customer:read")
    @SysLog(LogEnum.CUSTOMER_RECORD)
    public void customerRecordStats(@Para("")BiParams biParams){
        renderJson(biCustomerService.customerRecordStats(biParams));
    }

    /**
     * 客户跟进次数分析表
     * @author wyq
     */
    @Permissions("bi:customer:read")
    public void customerRecordInfo(@Para("")BiParams biParams){
        renderJson(biCustomerService.customerRecordInfo(biParams));
    }

    public void customerRecordInfoExport(@Para("") BiParams biParams) {
        Kv kv = (Kv) biCustomerService.customerRecordInfo(biParams).get("data");
        List<Record> recordList = (ArrayList<Record>) kv.get("list");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        Record total = (Record) kv.get("total");
        list.add(total.getColumns());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("realname", "员工姓名");
            writer.addHeaderAlias("recordCount", "跟进次数");
            writer.addHeaderAlias("customerCount", "跟进客户数");
            writer.merge(2, "客户跟进次数分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 3; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=customerRecordInfo.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出客户跟进次数分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 跟进客户详情
     */
    @Permissions("bi:customer:read")
    public void followUpCustomer(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biCustomerService.followUpCustomer(basePageRequest));
    }

    /**
     * 客户跟进方式分析
     * @author wyq
     */
    @Permissions("bi:customer:read")
    @SysLog(LogEnum.CUSTOMER_RECORD_MODE)
    public void customerRecodCategoryStats(@Para("")BiParams biParams){
        renderJson(biCustomerService.customerRecodCategoryStats(biParams));
    }

    public void customerRecodCategoryStatsExport(@Para("") BiParams biParams) {
        List<Record> recordList = (ArrayList<Record>) biCustomerService.customerRecodCategoryStats(biParams).get("data");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        Record total = new Record();
        Long totalRecordNum = recordList.stream().collect(Collectors.summarizingInt(r -> r.getInt("recordNum"))).getSum();
        total.set("category", "总计").set("recordNum", totalRecordNum).set("proportion", "100%");
        list.add(total.getColumns());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("category", "跟进方式");
            writer.addHeaderAlias("recordNum", "个数");
            writer.addHeaderAlias("proportion", "占比(%)");
            writer.merge(2, "客户跟进方式分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 3; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=customerRecordCategoryStats.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出客户跟进方式分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 客户转化率分析图
     * @author wyq
     */
    @Permissions("bi:customer:read")
    @SysLog(LogEnum.CUSTOMER_CONVERSION)
    public void customerConversionStats(@Para("")BiParams biParams){
        renderJson(biCustomerService.customerConversionStats(biParams));
    }

    /**
     * 客户转化率分析表
     * @author wyq
     */
    @Permissions("bi:customer:read")
    public void customerConversionInfo(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biCustomerService.customerConversionInfo(basePageRequest));
    }

    /**
     *公海客户分析图
     * @author wyq
     */
    @Permissions("bi:customer:read")
    @SysLog(LogEnum.CUSTOMER_POOL_TOTAL)
    public void poolStats(@Para("")BiParams biParams){
        renderJson(biCustomerService.poolStats(biParams));
    }

    /**
     *公海客户分析表
     * @author wyq
     */
    @Permissions("bi:customer:read")
    public void poolTable(@Para("")BiParams biParams){
        renderJson(biCustomerService.poolTable(biParams));
    }

    public void poolTableExport(@Para("") BiParams biParams) {
        Kv kv = (Kv) biCustomerService.poolTable(biParams).get("data");
        List<Record> recordList = (ArrayList<Record>) kv.get("list");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        Record total = (Record) kv.get("total");
        list.add(total.getColumns());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("realname", "姓名");
            writer.addHeaderAlias("deptName", "部门");
            writer.addHeaderAlias("receiveNum", "公海池领取客户数");
            writer.addHeaderAlias("putInNum", "进入公海客户数");
            writer.merge(3, "公海客户分析");
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
            response.setHeader("Content-Disposition", "attachment;filename=poolTable.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出公海客户分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 公海领取客户详情
     *
     * @param basePageRequest
     */
    @Permissions("bi:customer:read")
    public void receiveFromPool(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biCustomerService.receiveFromPool(basePageRequest));
    }

    /**
     * 进入公海客户详情
     *
     * @param basePageRequest
     */
    @Permissions("bi:customer:read")
    public void putInPool(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biCustomerService.putInPool(basePageRequest));
    }

    /**
     * 员工客户成交周期图
     * @author wyq
     */
    @Permissions("bi:customer:read")
    @SysLog(LogEnum.CUSTOMER_CYCLE)
    public void employeeCycle(@Para("")BiParams biParams){
        renderJson(biCustomerService.employeeCycle(biParams));
    }

    /**
     * 员工客户成交周期表
     * @author wyq
     */
    @Permissions("bi:customer:read")
    public void employeeCycleInfo(@Para("")BiParams biParams){
        renderJson(biCustomerService.employeeCycleInfo(biParams));
    }

    public void employeeCycleInfoExport(@Para("") BiParams biParams) {
        Kv kv = (Kv) biCustomerService.employeeCycleInfo(biParams).get("data");
        List<Record> recordList = (ArrayList<Record>) kv.get("list");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        Record total = (Record) kv.get("total");
        total.set("cycle", "");
        list.add(total.getColumns());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("realname", "姓名");
            writer.addHeaderAlias("cycle", "成交周期（天）");
            writer.addHeaderAlias("customerNum", "成交客户数");
            writer.merge(2, "员工客户成交周期分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 3; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=employeeCycleInfo.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出员工客户成交周期分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 地区成交周期图
     * @author wyq
     */
    @Permissions("bi:customer:read")
    public void districtCycle(@Para("")BiParams biParams){
        renderJson(biCustomerService.districtCycle(biParams));
    }

    public void districtCycleExport(@Para("") BiParams biParams) {
        Kv kv = (Kv) biCustomerService.districtCycle(biParams).get("data");
        List<Record> recordList = (ArrayList<Record>) kv.get("list");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        Record total = (Record) kv.get("total");
        total.set("cycle", "");
        list.add(total.getColumns());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("type", "地区");
            writer.addHeaderAlias("cycle", "成交周期（天）");
            writer.addHeaderAlias("customerNum", "成交客户数");
            writer.merge(2, "地区成交周期分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 3; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=districtCycleInfo.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出地区成交周期分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 产品成交周期
     * @author wyq
     */
    @Permissions("bi:customer:read")
    public void productCycle(@Para("")BiParams biParams){
        renderJson(biCustomerService.productCycle(biParams));
    }

    public void productCycleExport(@Para("") BiParams biParams) {
        Kv kv = (Kv) biCustomerService.productCycle(biParams).get("data");
        List<Record> recordList = (ArrayList<Record>) kv.get("list");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        Record total = (Record) kv.get("total");
        total.set("cycle", "");
        list.add(total.getColumns());
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("productName", "产品名称");
            writer.addHeaderAlias("cycle", "成交周期（天）");
            writer.addHeaderAlias("customerNum", "成交客户数");
            writer.merge(2, "产品成交周期分析");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 3; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=productCycleInfo.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出产品成交周期分析错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }
}
