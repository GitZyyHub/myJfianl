package com.kakarote.crm9.erp.card.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.card.entity.CardWeixinLeads;
import com.kakarote.crm9.erp.card.service.CardWeixinLeadsService;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zxy
 * 微信线索
 */
public class CardWeixinLeadsController extends Controller {
    @Inject
    private CardWeixinLeadsService service;
    /**
     * @author zxy
     * 添加
     */
    public void save(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(service.save(jsonObject));
    }
    /**
     * @author zxy
     * 分页查询
     */
    public void queryPage(BasePageRequest<CardWeixinLeads> base){
        renderJson(service.queryPage(base));
    }
    /**
     * @author zxy
     * 删除
     */
    public void deleteByIds(){
        String weixinLeadsIds = JSON.parseObject(getRawData()).getString("weixinLeadsIds");
        String [] idss =  weixinLeadsIds.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String id: idss ) {
            ids.add(Integer.valueOf(id));
        }
        renderJson(service.deleteByIds(ids));
    }
    /**
     * @author zxy
     * 转化为线索
     */
    public void changeLeads(){
        String weixinLeadsIds = JSON.parseObject(getRawData()).getString("weixinLeadsIds");
        String [] idss =  weixinLeadsIds.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String id: idss ) {
            ids.add(Integer.valueOf(id));
        }
        renderJson(service.changeLeads(ids));
    }
    /**
     * @author zxy
     * 导出微信线索
     */
    public void exportLeads(BasePageRequest<CardWeixinLeads> base){
        ExcelWriter writer = ExcelUtil.getWriter();

        List<Record> recordList = service.queryListByIds(base);
        writer.addHeaderAlias("weixin_name","微信名称");
        writer.addHeaderAlias("mobile","手机号");
        writer.addHeaderAlias("owner_user_name","负责人");
        writer.addHeaderAlias("create_time","创建时间");
        writer.merge(3,"微信线索信息");
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> {
            list.add(record.getColumns());
        });
        writer.setOnlyAlias(true);
        writer.write(list,true);
        writer.setRowHeight(0, 20);
        writer.setRowHeight(1, 20);
        for (int i = 0; i < list.size(); i++) {
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
        HttpServletResponse response = getResponse();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=weixinLedas.xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.flush(out);
    }
    /**
     * @author zxy
     * 微信小程序分页查询
     */
    @Clear
    public void queryMiNiPage(BasePageRequest<CardWeixinLeads> base){
        renderJson(service.queryMiNiPage(base));
    }
}
