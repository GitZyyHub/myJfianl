package com.kakarote.crm9.erp.statistics.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.statistics.common.StatisticsFieldEnum;
import com.kakarote.crm9.erp.statistics.common.StatisticsTypeUtils;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.utils.R;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MilestoneStatisticsService {

    @Inject
    StatisticsTypeUtils statisticsTypeUtils;


    public R milestoneDistribution(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return distributionDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return distributionDeptStatisticsTime(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.MILESTONE_TYPE.getType()) {
            return distributionType(workParams);
        }
        return R.error("查询失败");
    }

    private R distributionDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> isAll;
        List<Record> notAll;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            isAll = Db.find(Db.getSqlPara("statistics.distributionDept1", statisticsType.set("isAll", true)));
            notAll = Db.find(Db.getSqlPara("statistics.distributionDept1", statisticsType.set("isAll", false)));
        } else {
            isAll = Db.find(Db.getSqlPara("statistics.distributionDept2", statisticsType.set("isAll", true)));
            notAll = Db.find(Db.getSqlPara("statistics.distributionDept2", statisticsType.set("isAll", false)));
        }

        List<Record> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(isAll) && CollectionUtil.isNotEmpty(notAll)) {
            for (Record is : isAll) {
                for (Record not : notAll) {
                    if (is.getStr("name").equals(not.getStr("name"))) {
                        Record record = new Record();
                        record.set("name", is.getStr("name"));
                        record.set("num", is.getInt("counts"));
                        record.set("postpone", not.getInt("counts"));
                        list.add(record);
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }

    private R distributionType(WorkParams workParams) {
        Record record = statisticsTypeUtils.analyzeTime(workParams);
        Integer accomplish = 0, unfinished = 0, postpone = 0;
        List<Record> list = new ArrayList<>();
        if (workParams.getPara() == null) {
            accomplish = Db.template("statistics.distributionType", record.set("type", 1).getColumns()).queryInt();
            unfinished = Db.template("statistics.distributionType", record.set("type", 2).getColumns()).queryInt();
            postpone = Db.template("statistics.distributionType", record.set("type", 3).getColumns()).queryInt();
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
            Integer typeNum = Db.template("statistics.distributionType", record.set("type", workParams.getPara()).getColumns()).queryInt();
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
        List<Record> isAll = Db.find(Db.getSqlPara("statistics.distributionDeptStatisticsTime", record));
        List<Record> notAll = Db.find(Db.getSqlPara("statistics.distributionDeptStatisticsTime", record.set("isAll",true)));

        List<Record> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(isAll) && CollectionUtil.isNotEmpty(notAll)) {
            for (Record is : isAll) {
                for (Record not : notAll) {
                    if (is.getStr("name").equals(not.getStr("name"))) {
                        Record r = new Record();
                        r.set("name", is.getStr("name"));
                        r.set("num", is.getInt("counts"));
                        r.set("postpone", not.getInt("counts"));
                        list.add(r);
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }


    public R milestoneWarning(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return warningDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return warningTime(workParams);
        }
        return null;
    }

    private R warningTime(WorkParams workParams) {
        Record timeStatisticsField = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> warning = new ArrayList<>();
        List<Record> warn = new ArrayList<>();
        List<Record> list = new ArrayList<>();
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            warn = Db.find(Db.getSqlPara("statistics.warningStatisticsTime", timeStatisticsField.set("warning", true)));
            warning = Db.find(Db.getSqlPara("statistics.warningStatisticsTime", timeStatisticsField.set("warning", false)));
        } else {
            warn = Db.find(Db.getSqlPara("statistics.warningStatisticsTime", timeStatisticsField.set("warning", true)));
            warning = Db.find(Db.getSqlPara("statistics.warningStatisticsTime", timeStatisticsField.set("warning", false)));
        }
        if (CollectionUtil.isNotEmpty(warning) && CollectionUtil.isNotEmpty(warning)) {
            for (Record is : warning) {
                for (Record not : warn) {
                    if (is.getStr("name").equals(not.getStr("name"))) {
                        Record record = new Record();
                        record.set("name", is.getStr("name"));
                        record.set("warning", is.getInt("counts"));
                        record.set("warn", not.getInt("counts"));
                        list.add(record);
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }

    private R warningDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> warning = new ArrayList<>();
        List<Record> warn = new ArrayList<>();
        List<Record> list = new ArrayList<>();
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            warn = Db.find(Db.getSqlPara("statistics.warningDept1", statisticsType.set("warning", true)));
            warning = Db.find(Db.getSqlPara("statistics.warningDept1", statisticsType.set("warning", false)));
        } else {
            warn = Db.find(Db.getSqlPara("statistics.warningDept2", statisticsType.set("warning", true)));
            warning = Db.find(Db.getSqlPara("statistics.warningDept2", statisticsType.set("warning", false)));
        }
        if (CollectionUtil.isNotEmpty(warning) && CollectionUtil.isNotEmpty(warning)) {
            for (Record is : warning) {
                for (Record not : warn) {
                    if (is.getStr("name").equals(not.getStr("name"))) {
                        Record record = new Record();
                        record.set("name", is.getStr("name"));
                        record.set("warning", is.getInt("counts"));
                        record.set("warn", not.getInt("counts"));
                        list.add(record);
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }
}
 /*Integer warning = 0;
        Integer warn = 0;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String format = dateTimeFormatter.format(LocalDateTime.now());
        BigDecimal bigDecimal = new BigDecimal(format);
        Map<String,Record> map = new HashMap<>();
        for (Record record : records) {
            BigDecimal startTime = new BigDecimal(record.getStr("start_time"));
            BigDecimal endTime = new BigDecimal(record.getStr("end_time"));
            BigDecimal sum = endTime.subtract(startTime);
            BigDecimal subtract = endTime.subtract(bigDecimal);
            BigDecimal divide = subtract.divide(sum,2, BigDecimal.ROUND_HALF_UP);
            Record name = map.get(record.getStr("name"));
            if(name==null){
                name = new Record();
                map.put(record.getStr("name"),name);
            }
            if(divide.doubleValue()<0.1){
                ++warning;
                Integer anInt = name.getInt("warning");
                if(anInt==null){
                    name.set("warning",warning);
                }else {
                    name.set("warning",anInt+warning);
                }
            }
            if(subtract.doubleValue()<0){
                ++warn;
                Integer anInt = name.getInt("warn");
                if(anInt==null){
                    name.set("warn",warn);
                }else {
                    name.set("warn",anInt+warn);
                }
            }
        }*/