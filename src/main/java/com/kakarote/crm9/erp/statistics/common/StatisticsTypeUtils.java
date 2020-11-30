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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zyy
 */
public interface StatisticsTypeUtils {

    public default Record getStatisticsType(WorkParams workParams) {
        Integer deptId = workParams.getDeptId();
        List<Integer> deptIds = new ArrayList<>();
        if (deptId != null) {
            //获得下级部门
            deptIds = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
            deptIds.add(deptId);
        }else {
            deptIds = Aop.get(AdminUserService.class).queryChileDeptIds(BaseUtil.getDept(), BaseConstant.AUTH_DATA_RECURSION_NUM);
            deptIds.add(BaseUtil.getDept());
        }
        List<Integer> adds = new ArrayList<>();

        List<Integer> records = Db.template("statistics.queryDept", Kv.by("deptIds", deptIds)).query();
        Record record = new Record();
        record.set("deptIds", records);
        record.set("isDept", workParams.getIsDept());
        return record;
    }


    public default Record analyzeTime(WorkParams workParams) {
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

    public default Record appendSqlCondition(WorkParams workParams, Record record) {
        JSONObject jsonObject = workParams.getJsonObject();
        Map<String, Object> innerMap = jsonObject.getInnerMap();
        for (Map.Entry<String, Object> entry : innerMap.entrySet()) {
            record.set(entry.getKey(), entry.getValue());
        }
        return record;
    }
}
