package com.kakarote.crm9.erp.bi.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.bi.service.BiService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class BiController extends Controller {
    @Inject
    private BiService biService;

    /**
     * 产品销售情况统计
     * @author zxy
     * startTime 开始时间 endTime 结束时间 userId用户ID deptId部门ID
     */
    @Permissions("bi:product:read")
    @SysLog(LogEnum.PRODUCT_SALES)
    public void productStatistics(@Para("")BiParams biParams){
        renderJson(R.ok().put("data", biService.queryProductSell(biParams)));
    }

    public void productStatisticsExport(@Para("") BiParams biParams) {
        List<Map<String, Object>> list = biService.productSellExport(biParams);
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("categoryName", "产品分类");
            writer.addHeaderAlias("productName", "产品名称");
            writer.addHeaderAlias("contracNum", "合同编号");
            writer.addHeaderAlias("ownerUserName", "负责人");
            writer.addHeaderAlias("customerName", "客户名称");
            writer.addHeaderAlias("productPrice", "销售单价");
            writer.addHeaderAlias("productNum", "数量");
            writer.addHeaderAlias("productSubtotal", "订单产品小计");
            writer.merge(7, "产品销售情况统计");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 8; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=productStatistics.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出产品销售情况统计错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 获取商业智能业绩目标完成情况
     * @author wyq
     */
    @Permissions("bi:achievement:read")
    @NotNullValidate(value = "year",message = "year不能为空")
    @NotNullValidate(value = "type",message = "type不能为空")
    @SysLog(LogEnum.TARGET_COMPLETE)
    public void taskCompleteStatistics(@Para("year") String year, @Para("type") Integer type, @Para("deptId") Integer deptId, @Para("userId") Long userId, @Para("isUser") Integer isUser) {
        renderJson(R.ok().put("data", biService.taskCompleteStatistics(year, type, deptId, userId, isUser)));
    }

    public void taskCompleteStatisticsExport(@Para("year") String year, @Para("type") Integer type, @Para("deptId") Integer deptId, @Para("userId") Long userId, @Para("isUser") Integer isUser) {
        List<Map<String, Object>> list = biService.taskCompleteStatisticsExport(year, type, deptId, userId, isUser);
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("name", "名称");
            writer.addHeaderAlias("month", "月份");
            writer.addHeaderAlias("achievement", "目标");
            writer.addHeaderAlias("money", "完成");
            writer.addHeaderAlias("rate", "完成率");
            writer.merge(4, "业绩目标完成情况统计");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 5; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=taskCompleteStatistics.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出业绩目标完成情况统计错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }
}
