package com.kakarote.crm9.erp.admin.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.admin.service.AdminLogService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdminLogController extends Controller {
    @Inject
    private AdminLogService adminLogService;

    /**
     * 查询登录日志
     */
    public void queryLoginLogList(BasePageRequest basePageRequest){
        renderJson(R.ok().put("data",adminLogService.queryLoginLogList(basePageRequest)));
    }

    /**
     * 导出登录日志
     */
    public void loginLogExport(@Para("startTime")Date startTime, @Para("endTime")Date endTime, @Para("userIds")String userIds) {
        List<Record> recordList = Db.find(Db.getSqlPara("admin.log.queryLoginLogList", Kv.by("startTime", startTime).set("endTime", endTime).set("userIds", userIds)));
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("realname", "用户");
            writer.addHeaderAlias("login_time", "登录时间");
            writer.addHeaderAlias("ip_address", "ip地址");
            writer.addHeaderAlias("login_address", "登录地址");
            writer.addHeaderAlias("device_type", "设备类型");
            writer.addHeaderAlias("core", "终端内核");
            writer.addHeaderAlias("platform", "平台");
            writer.addHeaderAlias("auth_result", "认证结果");
            writer.merge(7, "登录日志");
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
            response.setHeader("Content-Disposition", "attachment;filename=loginLog.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出登录日志错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 查看系统操作日志
     **/
     public void querySystemLogList(BasePageRequest basePageRequest){
        renderJson(R.ok().put("data",adminLogService.querySystemLogList(basePageRequest)));
     }

     /**
      * 导出系统操作日志
     */
    public void systemLogExport(@Para("startTime")Date startTime, @Para("endTime")Date endTime, @Para("userIds")String userIds, @Para("model")Integer model, @Para("behavior")String behavior) {
        List<Record> recordList = Db.find(Db.getSqlPara("admin.log.querySystemLogList", Kv.by("startTime", startTime).set("endTime", endTime).set("userIds", userIds)));
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("realname", "用户");
            writer.addHeaderAlias("create_time", "时间");
            writer.addHeaderAlias("ip_address", "ip地址");
            writer.addHeaderAlias("model", "模块");
            writer.addHeaderAlias("behavior", "行为");
            writer.addHeaderAlias("object", "对象");
            writer.addHeaderAlias("detail", "详情");
            writer.merge(6, "系统操作日志");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 7; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=systemLog.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出系统操作日志错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

    /**
     * 查看数据操作日志
     **/
    public void queryActionRecordList(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data", adminLogService.queryActionRecordList(basePageRequest)));
    }

    /**
     * 导出数据操作日志
     */
    public void actionRecordExport(@Para("startTime") Date startTime, @Para("endTime") Date endTime, @Para("userIds") String userIds, @Para("types") String types, @Para("behavior") String behavior) {
        List<Record> recordList = Db.find(Db.getSqlPara("admin.log.queryActionRecordList", Kv.by("startTime", startTime).set("endTime", endTime).set("userIds", userIds).set("types", types).set("behavior", behavior)));
        recordList.forEach(record -> {
            record.set("types", CrmEnum.parse(record.getInt("types")).getRemarks());
            if (ObjectUtil.isNotEmpty(record.getInt("behavior"))) {
                record.set("behavior", BehaviorEnum.parse(record.getInt("behavior")).getName());
            }
        });
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> list.add(record.getColumns()));
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("realname", "用户");
            writer.addHeaderAlias("create_time", "时间");
            writer.addHeaderAlias("ip_address", "ip地址");
            writer.addHeaderAlias("types", "模块");
            writer.addHeaderAlias("behavior", "行为");
            writer.addHeaderAlias("object", "对象");
            writer.addHeaderAlias("detail", "详情");
            writer.merge(6, "数据操作日志");
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 7; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=actionRecord.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            Log.getLog(getClass()).error("导出数据操作日志错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
        renderNull();
    }

}
