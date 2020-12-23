package com.kakarote.crm9.erp.statistics.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.admin.entity.AdminFieldSort;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.statistics.common.StatisticsFieldEnum;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.erp.statistics.service.WorkStatisticsService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class WorkStatisticsController extends Controller {

    @Inject
    WorkStatisticsService workStatisticsService;

    /**
     * 项目分布统计表
     * @param workParams
     */
    public void workDistribution(@Para("") WorkParams workParams){
        renderJson(workStatisticsService.workDistribution(workParams));
    }

    /**
     * 项目盈亏统计表
     * @param workParams
     */
    public void workProfit(@Para("") WorkParams workParams){
        renderJson(workStatisticsService.workProfit(workParams));
    }

    /**
     * 项目成本统计表
     * @param
     */
    public void workCost(@Para("") WorkParams workParams){
        renderJson(workStatisticsService.workCost(workParams));
    }

    /**
     * 项目预警
     * @param workParams
     */
    public void workWarning(@Para("") WorkParams workParams){
        renderJson(workStatisticsService.workWarning(workParams));
    }

    public void workDistributionExport(@Para("") WorkParams workParams){
        Export(workParams,1);
    }

    public void workProfitExport(@Para("") WorkParams workParams){
        Export(workParams,2);
    }

    public void workCostExport(@Para("") WorkParams workParams){
        Export(workParams,3);
    }

    public void workWarningExport(@Para("") WorkParams workParams){
        Export(workParams,4);
    }


    @NotAction
    private void Export(WorkParams workParams,Integer type){
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = null;
            if(type==1){
                jsonObject = (JSONObject)workStatisticsService.workDistribution(workParams).get("data");
            }else if(type==2){
                jsonObject = (JSONObject)workStatisticsService.workProfit(workParams).get("data");
            }else if(type==3){
                jsonObject = (JSONObject)workStatisticsService.workCost(workParams).get("data");
            }else if(type==4){
                jsonObject = (JSONObject)workStatisticsService.workWarning(workParams).get("data");
            }
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"deptId"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = new ArrayList<>();
            addField(headList,StatisticsFieldEnum.parse(workParams.getTypeId()),type);

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
            response.setHeader("Content-Disposition", "attachment;filename=workStatistics.xls");

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
            names = Arrays.asList(statisticsFieldEnum.getContent(), "项目数量");
            fieldName = Arrays.asList("name", "counts");
        }else if(type==2){
            names = Arrays.asList(statisticsFieldEnum.getContent(), "预算","有效金额","实际成本");
            fieldName = Arrays.asList("name", "budget","effective_amount","practical_cost");
        }else if(type==3){
            names = Arrays.asList(statisticsFieldEnum.getContent(), "区域成本数量","百分比");
            fieldName = Arrays.asList("name", "num","rate");
        }else if(type==4){
            names = Arrays.asList(statisticsFieldEnum.getContent(), "预警数量", "告警数量");
            fieldName = Arrays.asList("name", "setting_warning_num", "setting_alarm_num");
        }
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName",fieldName.get(i));
            record.set("name",names.get(i));
            fields.add(record);
        }
    }

}
