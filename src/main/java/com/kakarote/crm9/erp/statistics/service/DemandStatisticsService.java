package com.kakarote.crm9.erp.statistics.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.statistics.common.StatisticsFieldEnum;
import com.kakarote.crm9.erp.statistics.common.StatisticsTypeUtils;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.utils.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemandStatisticsService {

    @Inject
    StatisticsTypeUtils statisticsTypeUtils;

    public R demandDistribution(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return distributionDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return distributionDeptStatisticsTime(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.DEMAND_STATUS.getType()) {
            return distributionStatus(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.DEMAND_TYPE.getType()) {
            return distributionType(workParams);
        }
        return R.error("查询失败");
    }

    private R distributionType(WorkParams workParams) {
        Record workphaseStatisticsField = statisticsTypeUtils.getWorkphaseStatisticsField(workParams);
        List<Record> records = Db.template("statistics.demandDistributionType", workphaseStatisticsField.getColumns()).find();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    private R distributionStatus(WorkParams workParams) {
        Record record = statisticsTypeUtils.analyzeTime(workParams);
        record.set("type",workParams.getPara());
        Integer accomplish = 0, unfinished = 0, postpone = 0;
        List<Record> list = new ArrayList<>();
        if (workParams.getPara() == null) {
            accomplish = Db.template("statistics.demandDistributionStatus", record.set("type", 1).getColumns()).queryInt();
            unfinished = Db.template("statistics.demandDistributionStatus", record.set("type", 2).getColumns()).queryInt();
            postpone = Db.template("statistics.demandDistributionStatus", record.set("type", 3).getColumns()).queryInt();
            Record record1 = new Record();
            record1.set("name","完成");
            record1.set("counts",accomplish);
            list.add(record1);
            Record record2 = new Record();
            record2.set("name","未完成");
            record2.set("counts",unfinished);
            list.add(record2);
            Record record3 = new Record();
            record3.set("name","延期");
            record3.set("counts",postpone);
            list.add(record3);
        } else {
            Integer typeNum = Db.template("statistics.demandDistributionStatus", record.set("type", workParams.getPara()).getColumns()).queryInt();
            switch (workParams.getPara()) {
                case 1:
                    accomplish = typeNum;
                    Record record1 = new Record();
                    record1.set("name","完成");
                    record1.set("counts",accomplish);
                    list.add(record1);
                    break;
                case 2:
                    unfinished = typeNum;
                    Record record2 = new Record();
                    record2.set("name","未完成");
                    record2.set("counts",unfinished);
                    list.add(record2);
                    break;
                case 3:
                    postpone = typeNum;
                    Record record3 = new Record();
                    record3.set("name","延期");
                    record3.set("counts",postpone);
                    list.add(record3);
                    break;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",list);
        return R.ok().put("data", jsonObject);
    }

    private R distributionDeptStatisticsTime(WorkParams workParams) {
        Record record = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> list = Db.template("statistics.demandDistributionTime", record.getColumns()).find();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }

    private R distributionDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            records = Db.find(Db.getSqlPara("statistics.demandDistributionDept1", statisticsType.getColumns()));
        } else {
            records = Db.find(Db.getSqlPara("statistics.demandDistributionDept2", statisticsType.getColumns()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R product(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> products;
        List<Record> notproducts;
        if(!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())){
            notproducts = Db.find(Db.getSqlPara("statistics.productCount1", statisticsType.set("isAll",false).set("product",true).getColumns()));
            products  = Db.find(Db.getSqlPara("statistics.productCount1", statisticsType.set("isAll",false).set("product",false).getColumns()));
        }else {
            notproducts = Db.find(Db.getSqlPara("statistics.productCount2", statisticsType.set("isAll",false).set("product",true).getColumns()));
            products  = Db.find(Db.getSqlPara("statistics.productCount2", statisticsType.set("isAll",false).set("product",false).getColumns()));
        }
        List<Record> list = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Record product = products.get(i);
            Record notproduct = notproducts.get(i);
            Record record = new Record();
            record.set("name",product.getStr("name"));
            record.set("product",product.getInt("counts"));
            record.set("notproduct",notproduct.getInt("counts"));
            list.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }

    public R individuation(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> queryDemdands;
        queryDemdands = Db.find(Db.getSqlPara("statistics.queryDemdand1", statisticsType));
        /*if(!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())){
            queryDemdands = Db.find(Db.getSqlPara("statistics.queryDemdand1", statisticsType));
        }else {
            queryDemdands = Db.find(Db.getSqlPara("statistics.queryDemdand2", statisticsType));
        }*/

        Map<String, List<Integer>> deptMap = new HashMap<>();
        for (Record record : queryDemdands) {
            List<Integer> name = deptMap.get(record.getStr("name"));
            if(CollectionUtil.isEmpty(name)){
                name = new ArrayList<>();
            }
            name.add(record.getInt("demand_id"));
            deptMap.put(record.getStr("name"),name);
        }
        List<Record> list = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : deptMap.entrySet()) {
            List<Integer> value = entry.getValue();
            Integer integer = Db.template("statistics.individuationCount",Kv.by("demandIds", value)).queryInt();
            //总需求数
            int size = value.size();
            int i = size - integer;
            Record record = new Record();
            record.set("name","个性化");
            record.set("counts",i);
            list.add(record);
            record = new Record();
            record.set("name","非个性化");
            record.set("counts",integer);
            list.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }
}
        /*for (int i = 0; i < counts.size(); i++) {
            Record count = counts.get(i);
            Record product = products.get(i);
            Record notproduct = notproducts.get(i);
            BigDecimal countNum = new BigDecimal(count.getStr("counts"));
            BigDecimal productNum = new BigDecimal(product.getStr("counts"));
            BigDecimal notproductNum = new BigDecimal(notproduct.getStr("counts"));
            Record record = new Record();
            BigDecimal divide;
            BigDecimal divide1;
            if(countNum.equals(BigDecimal.ZERO)){
                divide = BigDecimal.ZERO;
                divide1 = BigDecimal.ZERO;
            }else {
                divide = productNum.divide(countNum, 2, BigDecimal.ROUND_HALF_UP);
                divide1 = notproductNum.divide(countNum, 2, BigDecimal.ROUND_HALF_UP);
            }
            record.set("name",count.getStr("name"));
            record.set("product",divide.doubleValue());
            record.set("notproduct",divide1.doubleValue());
            list.add(record);
        }*/

       /* List<Record> records = Db.find(Db.getSqlPara("statistics.individuationCount", statisticsType));
        Map<String, List<Record>> deptMap = new HashMap<>();
        for (Record record : records) {
            List<Record> name = deptMap.get(record.getStr("name"));
            Record demand = new Record();
            demand.set(record.getStr("demand_id"),record.getStr("product_percentage"));
            if(CollectionUtil.isNotEmpty(name)){
                name = new ArrayList<>();
            }
            name.add(demand);
            deptMap.put(record.getStr("name"),name);
        }
        for (Map.Entry<String, List<Record>> entry : deptMap.entrySet()) {
            List<Record> value = entry.getValue();
            int size = value.size();
            for (Record record : value) {

            }
        }*/