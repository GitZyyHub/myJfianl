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
import com.kakarote.crm9.erp.statistics.service.RiskStatisticsService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class RiskStatisticsController extends Controller {

    @Inject
    RiskStatisticsService riskStatisticsService;

    public void riskPercentage(@Para("") WorkParams workParams){
        renderJson(riskStatisticsService.riskPercentage(workParams));
    }

    public void riskState(@Para("") WorkParams workParams){
        renderJson(riskStatisticsService.riskState(workParams));
    }

    public void riskPercentageExport(@Para("") WorkParams workParams){
        Export(workParams,1);
    }

    public void riskStateExport(@Para("") WorkParams workParams){
        Export(workParams,2);
    }

    @NotAction
    private void Export(WorkParams workParams,Integer type){
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = null;
            if(type==1){
                jsonObject = (JSONObject)riskStatisticsService.riskPercentage(workParams).get("data");
            }else if(type==2){
                jsonObject = (JSONObject)riskStatisticsService.riskState(workParams).get("data");
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
            response.setHeader("Content-Disposition", "attachment;filename=riskStatistics.xls");

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
        if(type==1){
            names = Arrays.asList(statisticsFieldEnum.getContent(), "完成率");
            fieldName = Arrays.asList("name", "rate");
        }else if(type==2){
            names = Arrays.asList(statisticsFieldEnum.getContent(), "完成数","未完成数");
            fieldName = Arrays.asList("name", "complete","unfinished");
        }
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName",fieldName.get(i));
            record.set("name",names.get(i));
            fields.add(record);
        }
    }
}
