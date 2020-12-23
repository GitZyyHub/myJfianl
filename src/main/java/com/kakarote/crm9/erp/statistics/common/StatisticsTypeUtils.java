package com.kakarote.crm9.erp.statistics.common;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.utils.BaseUtil;
import net.sf.cglib.asm.$Handle;

import java.sql.Struct;
import java.util.*;

/**
 * @author zyy
 */
public class StatisticsTypeUtils {

    public Record getDpteStatisticsField(WorkParams workParams) {
        Record record = analyzeTime(workParams);
        String deptId = workParams.getDeptId();
        String area = workParams.getArea();
        List deptIds = new ArrayList<>();
        if(!StrUtil.isEmpty(deptId) || !StrUtil.isEmpty(area)){
            //如果筛选
            List<String> strings ;
            if(!StrUtil.isEmpty(deptId)){
                strings = Arrays.asList(deptId.split(","));
            }else {
                strings = Arrays.asList(area.split(","));
            }
            for (String string : strings) {
                HashMap<String, Object> map = new HashMap<>();
                int id = Integer.parseInt(string);
                List<Integer> integers = Aop.get(AdminUserService.class).queryChileDeptIds(id, BaseConstant.AUTH_DATA_RECURSION_NUM);
                integers.add(id);
                StringBuffer stringBuffer = new StringBuffer();
                for (Integer integer : integers) {
                    stringBuffer.append(integer+",");
                }
                String s = stringBuffer.toString();
                map.put("below",s.substring(0,s.length()-1));
                map.put("dept",Integer.parseInt(string));
                deptIds.add(map);
            }
            record.set("deptIds",deptIds);
        }else {
            //默认搜索
            deptIds = Aop.get(AdminUserService.class).queryChileDeptIds(BaseUtil.getDept(), BaseConstant.AUTH_DATA_RECURSION_NUM);
            List<Record> records = Db.template("statistics.queryDept", Kv.by("deptIds", deptIds)).find();
            record.set("deptIds", records);
        }
        record.set("isDept", workParams.getTypeId());
        return record;
    }

    public Record getTimeStatisticsField(WorkParams workParams) {
        Record record = analyzeTime(workParams);
        Integer cycleNum = record.getInt("cycleNum");
        Integer beginTime = record.getInt("beginTime");
        List<Object> timeList = new ArrayList<>();
        //获得横坐标和查询条件
        for (int i=1; i <= cycleNum;i++){
            Map<String, Integer> map = new HashMap<>();
            map.put("startTime",beginTime);
            beginTime = estimateTime(beginTime);
            map.put("endTime",beginTime);
            timeList.add(map);
        }
        record.set("timeList",timeList);
        return record;
    }
    public Record getWorkphaseStatisticsField(WorkParams workParams) {
        Record record = analyzeTime(workParams);
        List<String> records = new ArrayList<>();
        String search = workParams.getSearch();
        if(!StrUtil.isEmpty(search)){
            records.add(search);
        }else {
            if(workParams.getTypeId() == StatisticsFieldEnum.WORKPHASE.getType()){
                records = Arrays.asList("项目立项", "需求阶段", "设计阶段", "开发测试", "项目初验", "试运行", "项目终验", "结项");
            }else if(workParams.getTypeId() == StatisticsFieldEnum.WORKTYPE.getType()){
                records = Arrays.asList("已结项","未结项");
            }
        }
        record.set("search",records);
        record.set("type",workParams.getTypeId());
        return record;
    }

    public Record getRiskLevelStatisticsField(WorkParams workParams) {
        Record record = analyzeTime(workParams);
        List<String> records = new ArrayList<>();
        String search = workParams.getSearch();
        if(!StrUtil.isEmpty(search)){
            records.add(search);
        }else {
            records = Arrays.asList("高", "中", "低");
        }
        record.set("search",records);
        record.set("type",workParams.getTypeId());
        return record;
    }



    public Record analyzeTime(WorkParams workParams) {
        Date beginDate = DateUtil.date();
        Date endDate = DateUtil.date();
        Integer cycleNum = 12;
        String sqlDateFormat = "%Y%m";
        String dateFormat = "yyyyMM";
        String type = workParams.getType();
        String startTime = workParams.getStartTime();
        String endTime = workParams.getEndTime();
        if (StrUtil.isNotEmpty(type)) {
            if ("year".equals(type)) {
                beginDate = DateUtil.beginOfYear(DateUtil.date());
                endDate = DateUtil.endOfYear(DateUtil.date());
            } else if ("lastYear".equals(type)) {
                beginDate = DateUtil.beginOfYear(DateUtil.offsetMonth(DateUtil.date(), -12));
                endDate = DateUtil.endOfYear(DateUtil.offsetMonth(DateUtil.date(), -12));
            } else if ("quarter".equals(type)) {
                beginDate = DateUtil.beginOfQuarter(DateUtil.date());
                endDate = DateUtil.endOfQuarter(DateUtil.date());
                cycleNum = 3;
            } else if ("lastQuarter".equals(type)) {
                beginDate = DateUtil.beginOfQuarter(DateUtil.offsetMonth(DateUtil.date(), -3));
                endDate = DateUtil.endOfQuarter(DateUtil.offsetMonth(DateUtil.date(), -3));
                cycleNum = 3;
            } else if ("month".equals(type)) {
                beginDate = DateUtil.beginOfMonth(DateUtil.date());
                endDate = DateUtil.endOfMonth(DateUtil.date());
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = (int) DateUtil.between(beginDate, endDate, DateUnit.DAY) + 1;
            } else if ("lastMonth".equals(type)) {
                beginDate = DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(), -1));
                endDate = DateUtil.endOfMonth(DateUtil.offsetMonth(DateUtil.date(), -1));
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = (int) DateUtil.between(beginDate, endDate, DateUnit.DAY) + 1;
            } else if ("week".equals(type)) {
                beginDate = DateUtil.beginOfWeek(DateUtil.date());
                endDate = DateUtil.endOfWeek(DateUtil.date());
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 7;
            } else if ("lastWeek".equals(type)) {
                beginDate = DateUtil.beginOfWeek(DateUtil.offsetWeek(DateUtil.date(), -1));
                endDate = DateUtil.endOfWeek(DateUtil.offsetWeek(DateUtil.date(), -1));
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 7;
            } else if ("today".equals(type)) {
                beginDate = DateUtil.beginOfDay(DateUtil.date());
                endDate = DateUtil.endOfDay(DateUtil.date());
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 1;
            } else if ("yesterday".equals(type)) {
                beginDate = DateUtil.offsetDay(DateUtil.date(), -1);
                endDate = DateUtil.offsetDay(DateUtil.date(), -1);
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 1;
            }
        } else if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
            Date start;
            Date end;
            if (startTime.length() == 6) {
                start = DateUtil.parse(startTime, "yyyyMM");
                end = DateUtil.endOfMonth(DateUtil.parse(endTime, "yyyyMM"));
            } else {
                start = DateUtil.parse(startTime);
                end = DateUtil.parse(endTime);
            }
            Integer startMonth = Integer.valueOf(DateUtil.format(start, "yyyyMM"));
            Integer endMonth = Integer.valueOf(DateUtil.format(end, "yyyyMM"));
            if (startMonth.equals(endMonth)) {
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                Long diffDay = DateUtil.between(start, end, DateUnit.DAY);
                cycleNum = diffDay.intValue() + 1;
            } else {
                sqlDateFormat = "%Y%m";
                dateFormat = "yyyyMM";
                Integer diffYear = Integer.valueOf(endMonth.toString().substring(0, 4)) - Integer.valueOf(startMonth.toString().substring(0, 4));
                Integer diffMonth = endMonth % 100 - startMonth % 100 + 1;
                cycleNum = diffYear * 12 + diffMonth;
            }
            beginDate = start;
            endDate = end;
        }
        Integer beginTime = Integer.valueOf(DateUtil.format(beginDate, dateFormat));
        Integer finalTime = Integer.valueOf(DateUtil.format(endDate, dateFormat));
        return new Record().set("sqlDateFormat", sqlDateFormat).set("dateFormat", dateFormat).set("beginDate", beginDate).set("endDate", endDate).set("cycleNum", cycleNum).set("beginTime", beginTime).set("finalTime", finalTime);
    }

    private Integer estimateTime(Integer beginTime){
        if (beginTime < 1000000 && beginTime%100 == 12){
            beginTime = beginTime + 89;
        }else if (beginTime > 1000000){
            int month = DateUtil.month(DateUtil.parse(beginTime.toString(),"yyyyMMdd"))+1;
            if (month == 1||month == 3||month == 5||month == 7||month == 8||month == 10){
                if (beginTime%100 == 31){
                    beginTime = beginTime + 70;
                }else {
                    beginTime++;
                }
            }else if (month == 4||month == 6||month == 9||month == 11){
                if (beginTime%100 == 30){
                    beginTime = beginTime + 71;
                }else {
                    beginTime++;
                }
            }else if (month == 2){
                int year = DateUtil.year(DateUtil.parse(beginTime.toString(),"yyyyMMdd"));
                if (DateUtil.isLeapYear(year) && beginTime%100 == 29){
                    beginTime = beginTime + 72;
                }else if (!DateUtil.isLeapYear(year) && beginTime%100 == 28){
                    beginTime = beginTime + 73;
                }else {
                    beginTime++;
                }
            }else if (month == 12){
                if (beginTime%100 == 31){
                    beginTime = beginTime + 8870;
                }else{
                    beginTime++;
                }
            }
        }else {
            beginTime++;
        }
        return beginTime;
    }


    public Record appendSqlCondition(WorkParams workParams, Record record) {
        JSONObject jsonObject = workParams.getJsonObject();
        Map<String, Object> innerMap = jsonObject.getInnerMap();
        for (Map.Entry<String, Object> entry : innerMap.entrySet()) {
            record.set(entry.getKey(), entry.getValue());
        }
        return record;
    }



}
