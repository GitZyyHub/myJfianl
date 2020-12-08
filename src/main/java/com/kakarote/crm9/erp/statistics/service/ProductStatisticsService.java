package com.kakarote.crm9.erp.statistics.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.statistics.common.StatisticsFieldEnum;
import com.kakarote.crm9.erp.statistics.common.StatisticsTypeUtils;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.erp.work.service.WorkService;
import com.kakarote.crm9.utils.R;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zyy
 */
public class ProductStatisticsService {

    @Inject
    StatisticsTypeUtils statisticsTypeUtils;

    @Inject
    WorkService workService;

    public R productUsage(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return usageArea(workParams);
        }else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return usageTime(workParams);
        }else {
            workParams.setTypeId(0);
            return usageAll(workParams);
        }
    }

    public R usageAll(WorkParams workParams){
        Record dpteStatisticsField = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Integer> demandIds = Db.template("statistics.queryDemandByWork", dpteStatisticsField.getColumns()).query();
        List<Record> records = Db.find(Db.getSqlPara("statistics.usageAll", Kv.by("demandIds", demandIds)));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        return R.ok().put("data",jsonObject);
    }

    public R usageArea(WorkParams workParams){
        Record dpteStatisticsField = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            records = Db.find(Db.getSqlPara("statistics.usageArea2", dpteStatisticsField));
        } else {
            records = Db.find(Db.getSqlPara("statistics.usageArea1", dpteStatisticsField));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        return R.ok().put("data",jsonObject);
    }

    public R usageTime(WorkParams workParams){
        Record dpteStatisticsField = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.usageTime", dpteStatisticsField));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        return R.ok().put("data",jsonObject);
    }

    public R productPercentage(WorkParams workParams) {
        JSONObject jsonObject = new JSONObject();
        List<Object> demandIds = Db.query(Db.getSql("statistics.queryDemandIdsByWorkId"), workParams.getSearch());
        List<Record> products = Db.find(Db.getSqlPara("statistics.queryProductByWorkId",Kv.by("demandIds",demandIds)));
        List<Record> list = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0%");
        for (Record product : products) {
            Record record = new Record();
            record.set("name",product.get("product_name"));
            String count = Db.template("statistics.queryFunctionByDemandIdsAndProductIdCount", Kv.by("demandIds", demandIds).set("productId", product.get("product_id"))).queryStr();
            String productPercentage = product.getStr("product_percentage");
            if("0".equals(count)||"0".equals(productPercentage)){
                record.set("percentage","0");
            }else {
                double divide = workService.divide(workService.divide(Double.parseDouble(productPercentage), Double.parseDouble("100"), 2), Double.parseDouble(count), 2);
                String format = df.format(divide);
                record.set("percentage",format.substring(0,format.length()-1));
            }
            list.add(record);
        }
        List<Object> productids = products.stream().map(x -> x.get("product_id")).collect(Collectors.toList());
        String productPercentage = (products.stream().collect(Collectors.summarizingInt(p -> p.getInt("product_percentage"))).getSum())+"";
        String count = Db.template("statistics.queryFunctionByDemandIdsAndProductIdCount", Kv.by("demandIds", demandIds).set("productIds", productids).set("multi",1)).queryStr();
        if("0".equals(count)||"0".equals(productPercentage)){
            jsonObject.put("percentage","0");
        }else {
            double divide = workService.divide(workService.divide(Double.parseDouble(productPercentage), Double.parseDouble("100"), 2), Double.parseDouble(count), 2);
            String format = df.format(divide );
            jsonObject.put("percentage",format.substring(0,format.length()-1));
        }
        jsonObject.put("list",list);
        return R.ok().put("data",jsonObject);
    }


    public R workSet(WorkParams workParams) {
        Integer allProductPercentage=0,
                allCounts=0;
        DecimalFormat df = new DecimalFormat("0%");
        List<Record> records = Db.find(Db.getSql("statistics.queryProductDetails"));
        for (Record record : records) {
            Integer productPercentage = record.getInt("product_percentage");
            Integer counts = record.getLong("counts").intValue();
            if(0==productPercentage||0==counts){
                record.set("percentage","0");
            }else {
                double divide = workService.divide(workService.divide(Double.parseDouble(productPercentage.toString()), Double.parseDouble("100"), 2), Double.parseDouble(counts.toString()), 2);
                String format = df.format(divide);
                record.set("percentage",format.substring(0,format.length()-1));
            }
            allProductPercentage+=productPercentage;
            allCounts+=counts;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        if(0==allProductPercentage||0==allCounts){
            jsonObject.put("percentage","0");
        }else {
            double divide = workService.divide(workService.divide(Double.parseDouble(allProductPercentage.toString()), Double.parseDouble("100"), 2), Double.parseDouble(allCounts.toString()), 2);
            String format = df.format(divide);
            jsonObject.put("percentage",format.substring(0,format.length()-1));
        }
        return R.ok().put("data",jsonObject);
    }

    public R searchWorkSet(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return workSetArea(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return workSetTime(workParams);
        }
        return R.error("查询失败");
    }

    public R workSetArea(WorkParams workParams){
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        JSONObject jsonObject = new JSONObject();
        List<Record> list = new ArrayList<>();
        Object sqlDateFormat = statisticsType.get("sqlDateFormat");
        Object beginTime = statisticsType.get("beginTime");
        Object finalTime = statisticsType.get("finalTime");
        Kv set = Kv.by("sqlDateFormat", sqlDateFormat).set("beginTime", beginTime).set("finalTime", finalTime);
        if(!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            List<Map<String,Object>> deptIds = statisticsType.get("deptIds");
            for (Map<String, Object> deptId : deptIds) {
                List<Object> query = Db.template("statistics.queryWorkInDept2", set.set("i", deptId.get("below"))).query();
                Record record = new Record();
                calculate(query,record,list,deptId.get("dept").toString());
                /*record.set("name", deptId.get("dept"));
                String Percentage = Db.template("statistics.sumWorkPercentage", Kv.by("i", query)).queryStr();
                String counts = Db.template("statistics.countWorkFunction", Kv.by("i", query)).queryStr();
                DecimalFormat df = new DecimalFormat("0%");
                if ("0".equals(Percentage) || "0".equals(counts)) {
                    record.set("percentage", "0");
                } else {
                    double divide = workService.divide(workService.divide(Double.parseDouble(Percentage), Double.parseDouble("100"), 2), Double.parseDouble(counts), 2);
                    String format = df.format(divide);
                    record.set("percentage", format.substring(0, format.length() - 1));
                }
                list.add(record);*/
            }
        }else {
            List<Record> deptIds = statisticsType.get("deptIds");
            for (Record deptId : deptIds) {
                Record record = new Record();
                List<Object> query = Db.template("statistics.queryWorkInDept1", set.set("deptId", deptId.get("dept_id"))).query();
                calculate(query,record,list,deptId.get("name"));
            }
        }
        jsonObject.put("list",list);
        return R.ok().put("data",jsonObject);
    }

    private void calculate(List<Object> query,Record record,List<Record> list,String name){
        record.set("name", name);
        String Percentage = Db.template("statistics.sumWorkPercentage", Kv.by("i", query)).queryStr();
        String counts = Db.template("statistics.countWorkFunction", Kv.by("i", query)).queryStr();
        DecimalFormat df = new DecimalFormat("0%");
        if ("0".equals(Percentage) || "0".equals(counts)) {
            record.set("percentage", "0");
        } else {
            double divide = workService.divide(workService.divide(Double.parseDouble(Percentage), Double.parseDouble("100"), 2), Double.parseDouble(counts), 2);
            String format = df.format(divide);
            record.set("percentage", format.substring(0, format.length() - 1));
        }
        list.add(record);
    }

    public R workSetTime(WorkParams workParams){
        Record timeStatisticsField = statisticsTypeUtils.getTimeStatisticsField(workParams);
        Object sqlDateFormat = timeStatisticsField.get("sqlDateFormat");
        Kv by = Kv.by("sqlDateFormat", sqlDateFormat);
        List<Record> list = new ArrayList<>();
        List<Map<String, Integer>> timeList = timeStatisticsField.get("timeList");
        for (Map<String, Integer> time : timeList) {
            List<Object> query = Db.template("statistics.queryWorkInTime", by.set("startTime", time.get("startTime")).set("endTime", time.get("endTime"))).query();
            Record record = new Record();
            calculate(query,record,list,time.get("startTime").toString());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",list);
        return R.ok().put("data",jsonObject);
    }
}
