package com.kakarote.crm9.erp.statistics.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.statistics.common.StatisticsFieldEnum;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.erp.statistics.service.ConfigStatisticsService;
import com.kakarote.crm9.erp.statistics.service.DemandStatisticsService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ConfigStatisticsController extends Controller {

    @Inject
    ConfigStatisticsService demandStatisticsService;

    public void configDistribution(@Para("")WorkParams workParams){
        renderJson(demandStatisticsService.configDistribution(workParams));
    }
    public void configDistributionExport(@Para("") WorkParams workParams){
        Export(workParams,1);
    }

    @NotAction
    private void Export(WorkParams workParams,Integer type){
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = null;

            if(type==1){
                jsonObject = (JSONObject)demandStatisticsService.configDistribution(workParams).get("data");
            }
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"deptId"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = new ArrayList<>();
            addField(headList, StatisticsFieldEnum.parse(workParams.getTypeId()),type);

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

            response.setCharacterEncoding("UTF-8");

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=milestoneStatistics.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }


    private void addField(List<Record> fields,StatisticsFieldEnum statisticsFieldEnum,Integer type){
        List<String> names = null;
        List<String> fieldName = null;
        if(type==1 ){
            names = Arrays.asList(statisticsFieldEnum.getContent(), "数量");
            fieldName = Arrays.asList( "name","counts");
        }
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName",fieldName.get(i));
            record.set("name",names.get(i));
            fields.add(record);
        }
    }
}
