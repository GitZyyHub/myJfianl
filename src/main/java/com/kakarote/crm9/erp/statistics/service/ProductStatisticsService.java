package com.kakarote.crm9.erp.statistics.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.admin.entity.AdminDept;
import com.kakarote.crm9.erp.statistics.common.StatisticsFieldEnum;
import com.kakarote.crm9.erp.statistics.common.StatisticsTypeUtils;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.WorkService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
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
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return usageTime(workParams);
        } else {
            workParams.setTypeId(0);
            return usageAll(workParams);
        }
    }

    public R usageAll(WorkParams workParams) {
        Record dpteStatisticsField = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Integer> demandIds = Db.template("statistics.queryDemandByWork", dpteStatisticsField.getColumns()).query();
        List<Record> records = Db.find(Db.getSqlPara("statistics.usageAll", Kv.by("demandIds", demandIds)));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R usageArea(WorkParams workParams) {
        if (StrUtil.isEmpty(workParams.getDeptId()) || StrUtil.isEmpty(workParams.getArea())) {
            workParams.setDeptId(BaseUtil.getDept().toString());
        }
        Record dpteStatisticsField = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records;
        records = Db.find(Db.getSqlPara("statistics.usageArea2", dpteStatisticsField));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R usageTime(WorkParams workParams) {
        Record dpteStatisticsField = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.usageTime", dpteStatisticsField));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R productPercentage(WorkParams workParams) {
        JSONObject jsonObject = new JSONObject();
        List<Object> demandIds = Db.query(Db.getSql("statistics.queryDemandIdsByWorkId"), workParams.getSearch());
        List<Record> products = Db.find(Db.getSqlPara("statistics.queryProductByWorkId", Kv.by("demandIds", demandIds)));
        List<Record> list = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0%");
        for (Record product : products) {
            Record record = new Record();
            record.set("name", product.get("product_name"));
            String count = Db.template("statistics.queryFunctionByDemandIdsAndProductIdCount", Kv.by("demandIds", demandIds).set("productId", product.get("product_id"))).queryStr();
            String productPercentage = product.getStr("product_percentage");
            if ("0".equals(count) || "0".equals(productPercentage)) {
                record.set("percentage", "0");
            } else {
                double divide = workService.divide(workService.divide(Double.parseDouble(productPercentage), Double.parseDouble("100"), 2), Double.parseDouble(count), 2);
                String format = df.format(divide);
                record.set("percentage", format.substring(0, format.length() - 1));
            }
            list.add(record);
        }
        List<Object> productids = products.stream().map(x -> x.get("product_id")).collect(Collectors.toList());
        String productPercentage = (products.stream().collect(Collectors.summarizingInt(p -> p.getInt("product_percentage"))).getSum()) + "";
        String count = Db.template("statistics.queryFunctionByDemandIdsAndProductIdCount", Kv.by("demandIds", demandIds).set("productIds", productids).set("multi", 1)).queryStr();
        String workName = Aop.get(Work.class).findById(workParams.getSearch()).getWorkName();
        jsonObject.put("workName", workName);
        if ("0".equals(count) || "0".equals(productPercentage)) {
            jsonObject.put("percentage", "0");
        } else {
            double divide = workService.divide(workService.divide(Double.parseDouble(productPercentage), Double.parseDouble("100"), 2), Double.parseDouble(count), 2);
            String format = df.format(divide);
            jsonObject.put("percentage", format.substring(0, format.length() - 1));
        }
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }


    public R workSet(WorkParams workParams) {
        Integer allProductPercentage = 0,
                allCounts = 0;
        DecimalFormat df = new DecimalFormat("0%");
        List<Object> query = Db.query("select demand_id from 72crm_work_demand where work_id in (select work_id from 72crm_work where (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）'))");
        List<Record> records = Db.find(Db.getSqlPara("statistics.queryProductDetails",Kv.by("demandIds",query)));
        for (Record record : records) {
            Integer productPercentage = record.getInt("product_percentage");
            Integer counts = record.getLong("counts").intValue();
            if (0 == productPercentage || 0 == counts) {
                record.set("percentage", "0");
            } else {
                double divide = workService.divide(workService.divide(Double.parseDouble(productPercentage.toString()), Double.parseDouble("100"), 2), Double.parseDouble(counts.toString()), 2);
                String format = df.format(divide);
                record.set("percentage", format.substring(0, format.length() - 1));
            }
            allProductPercentage += productPercentage;
            allCounts += counts;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        if (0 == allProductPercentage || 0 == allCounts) {
            jsonObject.put("percentage", "0");
        } else {
            double divide = workService.divide(workService.divide(Double.parseDouble(allProductPercentage.toString()), Double.parseDouble("100"), 2), Double.parseDouble(allCounts.toString()), 2);
            String format = df.format(divide);
            jsonObject.put("percentage", format.substring(0, format.length() - 1));
        }
        return R.ok().put("data", jsonObject);
    }

    public R searchWorkSet(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return workSetArea(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return workSetTime(workParams);
        }
        return R.error("查询失败");
    }

    public R workSetArea(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        JSONObject jsonObject = new JSONObject();
        List<Record> list = new ArrayList<>();
        Object sqlDateFormat = statisticsType.get("sqlDateFormat");
        Object beginTime = statisticsType.get("beginTime");
        Object finalTime = statisticsType.get("finalTime");
        Kv set = Kv.by("sqlDateFormat", sqlDateFormat).set("beginTime", beginTime).set("finalTime", finalTime);
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            List<Map<String, Object>> deptIds = statisticsType.get("deptIds");
            for (Map<String, Object> deptId : deptIds) {
                List<Object> query = Db.template("statistics.queryWorkInDept2", set.set("i", deptId.get("below"))).query();
                Record record = new Record();
                calculate(query, record, list, Aop.get(AdminDept.class).findById(deptId.get("dept")).getName(), workParams.getSearch());
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
        } else {
            List<Record> deptIds = statisticsType.get("deptIds");
            for (Record deptId : deptIds) {
                Record record = new Record();
                List<Object> query = Db.template("statistics.queryWorkInDept1", set.set("deptId", deptId.get("dept_id"))).query();
                calculate(query, record, list, deptId.get("name"), workParams.getSearch());
            }
        }
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }

    private void calculate(List<Object> query, Record record, List<Record> list, String name, String search) {
        record.set("name", name);
        String Percentage = Db.template("statistics.sumWorkPercentage", Kv.by("i", query).set("productId", search)).queryStr();
        String counts = Db.template("statistics.countWorkFunction", Kv.by("i", query).set("productId", search)).queryStr();
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

    public R workSetTime(WorkParams workParams) {
        Record timeStatisticsField = statisticsTypeUtils.getTimeStatisticsField(workParams);
        Object sqlDateFormat = timeStatisticsField.get("sqlDateFormat");
        Kv by = Kv.by("sqlDateFormat", sqlDateFormat);
        List<Record> list = new ArrayList<>();
        List<Map<String, Integer>> timeList = timeStatisticsField.get("timeList");
        for (Map<String, Integer> time : timeList) {
            List<Object> query = Db.template("statistics.queryWorkInTime", by.set("startTime", time.get("startTime")).set("endTime", time.get("endTime"))).query();
            Record record = new Record();
            calculate(query, record, list, time.get("startTime").toString(), workParams.getSearch());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }

    public R resourceCost(WorkParams workParams) {
        List<List<Object>> list = new ArrayList<>();
        List<Integer> query = Db.query("select work_id from 72crm_work where (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')");
        DecimalFormat df = new DecimalFormat("0%");
        setCostType(list,query,df);
        List<Object> deptIds = Db.query("select dept_id from 72crm_admin_dept where name like '%产品%' ");
        List<Object> userIds = Db.template("admin.user.queryUserIdByDeptIds", Kv.by("deptIds", deptIds)).query();
        List<Object> records = new ArrayList<>();
        for (List<Object> li : list) {
            //查询人员总工时 / 项目总工时
            String userHours = Db.template("statistics.sumHours", Kv.by("userIds", userIds).set("workIds", li)).queryStr();
            String workHours = Db.template("statistics.sumHours", Kv.by("workIds", li)).queryStr();
            if ("0".equals(userHours) || "0".equals(workHours)) {
                records.add( "0");
            } else {
                double divide = workService.divide(Double.parseDouble(userHours), Double.parseDouble(workHours), 2);
                String format = df.format(divide);
                records.add(format.substring(0, format.length() - 1));
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        return R.ok().put("data",jsonObject);
    }

    public R resourceCostType(WorkParams workParams){
        List<List<Object>> list = new ArrayList<>();
        List<Integer> query = Db.query("select work_id from 72crm_work where (area=? or dept_id=?) and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')",workParams.getSearch(),workParams.getSearch());
        DecimalFormat df = new DecimalFormat("0%");
        setCostType(list,query,df);
        List<Object> deptIds = Db.query("select dept_id from 72crm_admin_dept where name like '%产品%' ");
        List<Object> userIds = Db.template("admin.user.queryUserIdByDeptIds", Kv.by("deptIds", deptIds)).query();
        if(workParams.getTypeId()>list.size()){
            return R.error("查询错误");
        }
        List<Object> li = list.get(workParams.getTypeId());
        List<Record> records = new ArrayList<>();
        for (Object i : li) {
            Record record = new Record();
            String workName = Aop.get(Work.class).findById(i).getWorkName();
            record.set("name",workName);
            //查询人员总工时 / 项目总工时
            List<Object> workId = Arrays.asList(i);
            String userHours = Db.template("statistics.sumHours", Kv.by("userIds", userIds).set("workIds", workId)).queryStr();
            String workHours = Db.template("statistics.sumHours", Kv.by("workIds", workId)).queryStr();
            if ("0".equals(userHours) || "0".equals(workHours)) {
                record.set("percentage","0");
            } else {
                double divide = workService.divide(Double.parseDouble(userHours), Double.parseDouble(workHours), 2);
                String format = df.format(divide);
                record.set("percentage",format.substring(0, format.length() - 1));
            }
            records.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        return R.ok().put("data",jsonObject);
    }

    private void setCostType(List<List<Object>> list,List<Integer> query,DecimalFormat df){
        //存放不同项目的ID
        List<Object> zero = new ArrayList<>();
        List<Object> thirty = new ArrayList<>();
        List<Object> five = new ArrayList<>();
        List<Object> seventy = new ArrayList<>();
        List<Object> ninety = new ArrayList<>();
        for (Integer integer : query) {
            Map<String, Object> map = new HashMap<>();
            List<Object> demandIds = Db.query(Db.getSql("statistics.queryDemandIdsByWorkId"), integer);
            List<Record> products = Db.find(Db.getSqlPara("statistics.queryProductByWorkId", Kv.by("demandIds", demandIds)));
            List<Object> productids = products.stream().map(x -> x.get("product_id")).collect(Collectors.toList());
            String productPercentage = (products.stream().collect(Collectors.summarizingInt(p -> p.getInt("product_percentage"))).getSum()) + "";
            String count = Db.template("statistics.queryFunctionByDemandIdsAndProductIdCount", Kv.by("demandIds", demandIds).set("productIds", productids).set("multi", 1)).queryStr();

            if ("0".equals(count) || "0".equals(productPercentage)) {
                zero.add(integer);
            } else {
                double divide = workService.divide(workService.divide(Double.parseDouble(productPercentage), Double.parseDouble("100"), 2), Double.parseDouble(count), 2);
                String format = df.format(divide);
                int i = Integer.parseInt(format.substring(0, format.length() - 1));
                if (i < 30) {
                    zero.add(integer);
                } else if (30 <= i && i < 50) {
                    thirty.add(integer);
                } else if (50 <= i && i < 70) {
                    five.add(integer);
                } else if (70 <= i && i < 90) {
                    seventy.add(integer);
                } else  {
                    ninety.add(integer);
                }
            }
        }
        list.add(zero);
        list.add(thirty);
        list.add(five);
        list.add(seventy);
        list.add(ninety);
    }
}
