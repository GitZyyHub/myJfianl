package com.kakarote.crm9.erp.bi.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.bi.service.BiRankingService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BiRankingController extends Controller {
    @Inject
    private BiRankingService service;

    @Inject
    private BiTimeUtil biTimeUtil;
    /**
     * 合同金额排行榜
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_CONTRACT)
    public void contractRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        renderJson(R.ok().put("data", service.contractRanKing(record)));
    }

    public void contractRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        List<Record> recordList = service.contractRanKing(record);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "签订人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("money", "合同金额（元）");
            writer.merge(3, "合同金额排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=contractRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出合同金额排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 回款金额排行榜
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_RECEIVABLES)
    public void receivablesRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        renderJson(R.ok().put("data", service.receivablesRanKing(record)));
    }

    public void receivablesRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        List<Record> recordList = service.receivablesRanKing(record);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "签订人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("money", "回款金额（元）");
            writer.merge(3, "回款金额排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=receivablesRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出回款金额排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 签约合同排行榜
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_SIGNING)
    public void contractCountRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        renderJson(R.ok().put("data", service.contractCountRanKing(record)));
    }

    public void contractCountRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        List<Record> recordList = service.contractCountRanKing(record);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "签订人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "签约合同数（个）");
            writer.merge(3, "签约合同排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=contractCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出签约合同排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 产品销量排行榜
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_PRODUCT)
    public void productCountRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        renderJson(service.productCountRanKing(biParams));
    }

    public void productCountRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        List<Record> recordList = (ArrayList<Record>) service.productCountRanKing(biParams).get("data");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "签订人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "产品销量");
            writer.merge(3, "产品销量排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=productCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出产品销量排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 新增客户数排行榜
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_ADD_CUSTOMER)
    public void customerCountRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        renderJson(R.ok().put("data", service.customerCountRanKing(record)));
    }

    public void customerCountRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        List<Record> recordList = service.customerCountRanKing(record);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "创建人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "新增客户数（个）");
            writer.merge(3, "新增客户数排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=customerCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出新增客户数排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 新增联系人排行榜
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_ADD_CONTACTS)
    public void contactsCountRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        renderJson(R.ok().put("data", service.contactsCountRanKing(record)));
    }

    public void contactsCountRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        List<Record> recordList = service.contactsCountRanKing(record);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "创建人");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "新增联系人数（个）");
            writer.merge(3, "新增联系人排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=contactsCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出新增联系人排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 跟进客户数排行榜
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_RECORD_CUSTOMER)
    public void customerGenjinCountRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        renderJson(service.customerGenjinCountRanKing(biParams));
    }

    public void customerGenjinCountRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        List<Record> recordList = (List<Record>) service.customerGenjinCountRanKing(biParams).get("data");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "员工");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "跟进客户数（个）");
            writer.merge(3, "跟进客户数排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=customerFollowupCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出跟进客户数排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 跟进次数排行榜
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_RECORD_NUN)
    public void recordCountRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        renderJson(R.ok().put("data", service.recordCountRanKing(record)));
    }

    public void recordCountRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        List<Record> recordList = service.recordCountRanKing(record);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "员工");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "跟进次数（次）");
            writer.merge(3, "跟进次数排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=FollowupRecordCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出跟进次数排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 产品分类销量分析
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.PRODUCT_CATEGORY)
    public void contractProductRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        renderJson(service.contractProductRanKing(biParams));
    }

    /**
     * 出差次数排行
     * @author zxy
     */
    @Permissions("bi:ranking:read")
    @SysLog(LogEnum.RANKING_EXAMINE)
    public void travelCountRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        renderJson(service.travelCountRanKing(biParams));
    }

    public void travelCountRanKingExport(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        List<Record> recordList = (List<Record>) service.travelCountRanKing(biParams).get("data");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record r = recordList.get(i);
            list.add(r.set("order", i + 1).getColumns());
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("order", "公司总排名");
            writer.addHeaderAlias("realname", "员工");
            writer.addHeaderAlias("structureName", "部门");
            writer.addHeaderAlias("count", "出差次数（次）");
            writer.merge(3, "出差次数排行");
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
            response.setHeader("Content-Disposition", "attachment;filename=travelCountRanKing.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出出差次数排行错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 产品销售情况统计
     * @author zxy
     */
    @Permissions("bi:product:read")
    public void productSellRanKing(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        renderJson(service.productSellRanKing(biParams));
    }

    /**
     * @author wyq
     * 新增商机排行
     */
    public void businessRanKing(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        renderJson(R.ok().put("data", service.businessRanKing(record)));
    }

    /**
     * @author wyq
     * 新增商机金额排行
     */
    public void businessMoneyRanKing(@Para("") BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "ranking", "read");
        biParams.setMenuId(menuId);
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        renderJson(R.ok().put("data", service.businessMoneyRanKing(record)));
    }

    /**
     * 城市分布分析
     * @author zxy
     */
    @Permissions("bi:portrait:read")
    @SysLog(LogEnum.PORTRAIT_ADDRESS)
    public void addressAnalyse(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "portrait", "read");
        biParams.setMenuId(menuId);
        renderJson(service.addressAnalyse(biParams));
    }
    /**
     * 客户行业分析
     * @author zxy
     */
    @Permissions("bi:portrait:read")
    @SysLog(LogEnum.PORTRAIT_CUSTOMER_INDUSTRY)
    public void portrait(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "portrait", "read");
        biParams.setMenuId(menuId);
        renderJson(service.portrait(biParams));
    }
    /**
     * 客户级别分析
     * @author zxy
     */
    @Permissions("bi:portrait:read")
    @SysLog(LogEnum.PORTRAIT_CUSTOMER_LEVEL)
    public void portraitLevel(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "portrait", "read");
        biParams.setMenuId(menuId);
        renderJson(service.portraitLevel(biParams));
    }
    /**
     * 客户来源分析
     * @author zxy
     */
    @Permissions("bi:portrait:read")
    @SysLog(LogEnum.PORTRAIT_CUSTOMER_SOURCE)
    public void portraitSource(@Para("")BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "portrait", "read");
        biParams.setMenuId(menuId);
        renderJson(service.portraitSource(biParams));
    }
}
