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
import com.kakarote.crm9.erp.statistics.service.ProductStatisticsService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zyy
 */
public class ProductStatisticsController extends Controller {

    @Inject
    ProductStatisticsService productStatisticsService;

    /**
     * 项目排行榜
     * @param workParams
     */
    public void productUsage(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.productUsage(workParams));
    }

    /**
     * 产品复用率-项目
     * @param workParams
     */
    public void productPercentage(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.productPercentage(workParams));
    }

    /**
     * 项目集
     * @param workParams
     */
    public void workSet(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.workSet(workParams));
    }

    /**
     * 项目集穿透
     * @param workParams
     */
    public void searchWorkSet(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.searchWorkSet(workParams));
    }

    /**
     * 资源交付成本投入
     * @param workParams
     */
    public void resourceCost(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.resourceCost(workParams));
    }

    /**
     * 资源交付成本投入-穿透图
     * @param workParams
     */
    public void resourceCostType(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.resourceCostType(workParams));
    }

    public void productUsageExport(@Para("")WorkParams workParams){
        Export(workParams,1);
    }

    public void productPercentageExport(@Para("")WorkParams workParams){
        Export(workParams,2);
    }

    public void workSetExport(@Para("")WorkParams workParams){
        Export(workParams,3);
    }

    public void searchWorkSetExport(@Para("")WorkParams workParams){
        Export(workParams,4);
    }

    public void resourceCostTypeExport(@Para("")WorkParams workParams){
        Export(workParams,5);
    }

    @NotAction
    private void Export(WorkParams workParams,Integer type){
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            JSONObject jsonObject = null;
            if(type==1){
                jsonObject = (JSONObject)productStatisticsService.productUsage(workParams).get("data");
            }else if(type==2){
                jsonObject = (JSONObject)productStatisticsService.productPercentage(workParams).get("data");
            }else if(type==3){
                jsonObject = (JSONObject)productStatisticsService.workSet(workParams).get("data");
            }else if(type==4){
                jsonObject = (JSONObject)productStatisticsService.searchWorkSet(workParams).get("data");
            }else if(type==5){
                jsonObject = (JSONObject)productStatisticsService.resourceCostType(workParams).get("data");
            }
            List<Record> records = jsonObject.getJSONArray("list").toJavaList(Record.class);
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"productId","productPercentage","product_id","product_percentage"};
            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                record.remove(s);
                list.add(record.getColumns());
            }

            List<Record> headList = new ArrayList<>();
            addField(headList, workParams.getTypeId()==null?null:StatisticsFieldEnum.parse(workParams.getTypeId()),type);

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
            response.setHeader("Content-Disposition", "attachment;filename=productStatistics.xls");

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
            names = Arrays.asList("产品名称", "产品数量");
            fieldName = Arrays.asList("name", "counts");
        }else if(type==2){
            names = Arrays.asList("产品名称", "产品复用百分比");
            fieldName = Arrays.asList("name", "percentage");
        }else if(type==3){
            names = Arrays.asList("产品名称", "产品复用百分比");
            fieldName = Arrays.asList("productName", "percentage");
        }else if(type==4){
            names = Arrays.asList(statisticsFieldEnum.getContent(), "产品复用百分比");
            fieldName = Arrays.asList("name", "percentage");
        }else if(type==5){
            names = Arrays.asList(statisticsFieldEnum.getContent(), "产品投入项目占比");
            fieldName = Arrays.asList("name", "percentage");
        }
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName",fieldName.get(i));
            record.set("name",names.get(i));
            fields.add(record);
        }
    }


}
