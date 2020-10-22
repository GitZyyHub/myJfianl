package com.kakarote.crm9.erp.bi.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.bi.service.BiWorkService;
import com.kakarote.crm9.utils.R;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BiWorkController extends Controller {

    @Inject
    private BiWorkService biWorkService;


    /**
     * 查询日志统计信息
     * @author zhang
     */
    @Permissions("bi:oa:read")
    @SysLog(LogEnum.LOG_ANALYSIS)
    public void logStatistics(@Para("") BiParams biParams){
        renderJson(R.ok().put("data",biWorkService.logStatistics(biParams)));
    }

    /**
     * 日志详情
     */
    public void allLog(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biWorkService.logList(basePageRequest, null));
    }

    /**
     * 已评论日志详情
     */
    public void commentLog(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biWorkService.logList(basePageRequest, 1));
    }

    /**
     * 未评论日志详情
     */
    public void unCommentLog(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(biWorkService.logList(basePageRequest, 2));
    }

    /**
     * 导出日志统计信息
     * @author zhangzhiwei
     */
    public void logStatisticsExport(@Para("") BiParams biParams)throws IOException {
        List<Record> recordList=biWorkService.logStatistics(biParams);
        List<Map<String,Object>> mapList=new LinkedList<>();
        recordList.forEach(record -> mapList.add(record.remove("img","user_id","username").getColumns()));
        ExcelWriter writer=null;
        try {
            writer = ExcelUtil.getWriter();
            writer.addHeaderAlias("realname", "员工");
            writer.addHeaderAlias("count", "填写数");
            writer.addHeaderAlias("unCommentCount", "未评论数");
            writer.addHeaderAlias("commentCount", "已评论数");
            writer.merge(3, "日志分析");
            HttpServletResponse response = getResponse();
            writer.write(mapList, true);
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
            response.setHeader("Content-Disposition", "attachment;filename=logStatistics.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
        renderNull();
    }

    /**
     * 查询审批统计信息
     * @author zhang
     */
    @Permissions("bi:oa:read")
    @SysLog(LogEnum.EXAMINE_ANALYSIS)
    public void examineStatistics(@Para("") BiParams biParams){
        renderJson(R.ok().put("data",biWorkService.examineStatistics(biParams)));
    }

    /**
     * 导出审批统计信息
     * @author zhangzhiwei
     */
    @SuppressWarnings("unchecked")
    public void examineStatisticsExport(@Para("") BiParams biParams)throws IOException {
        JSONObject object=biWorkService.examineStatistics(biParams);
        List<Map<String,Object>> mapList=new LinkedList<>();
        ExcelWriter writer=null;
        try {
            writer = ExcelUtil.getWriter();
            List<Record> categoryList= (List<Record>) object.get("categoryList");
            writer.addHeaderAlias("realname", "员工");
            for (Record record : categoryList) {
                writer.addHeaderAlias("count_"+record.get("category_id"), record.get("title"));
            }
            writer.merge(categoryList.size(), "审批分析");
            HttpServletResponse response = getResponse();
            ((List<Record>)object.get("userList")).forEach(record -> mapList.add(record.remove("img","user_id","username").getColumns()));
            writer.write(mapList, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < categoryList.size(); i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=examineStatistics.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
        renderNull();
    }
    /**
     * 查询审批详情
     * @author zhangzhiwei
     */
    public void examineInfo(BasePageRequest basePageRequest){
        renderJson(R.ok().put("data",biWorkService.examineInfo(basePageRequest)));
    }
}
