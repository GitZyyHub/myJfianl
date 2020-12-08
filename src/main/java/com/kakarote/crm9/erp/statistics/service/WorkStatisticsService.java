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
import com.kakarote.crm9.erp.work.service.WorkService;
import com.kakarote.crm9.utils.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zyy
 */
public class WorkStatisticsService {

    @Inject
    StatisticsTypeUtils statisticsTypeUtils;

    @Inject
    WorkService workService;

    public R workDistribution(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return distributionDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return distributionDeptStatisticsTime(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.WORKPHASE.getType()) {
            return distributionWorkphase(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.WORKTYPE.getType()) {
            return distributionWorkphase(workParams);
        }
        return R.error("查询失败");
    }

    public R distributionDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            records = Db.find(Db.getSqlPara("statistics.workDeptStatistics2", statisticsType));
        } else {
            records = Db.find(Db.getSqlPara("statistics.workDeptStatistics1", statisticsType));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }


    public R distributionDeptStatisticsTime(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.workTimeStatistics", statisticsType));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R distributionWorkphase(WorkParams workParams) {
        Record workphaseStatisticsField = statisticsTypeUtils.getWorkphaseStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.workWorkphaseStatistics", workphaseStatisticsField));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }


    public R workProfit(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return profitDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return profitTime(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.WORKPHASE.getType()) {
            return profitWorkphase(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.WORKTYPE.getType()) {
            return profitWorkphase(workParams);
        }
        return R.error("查询失败");
    }

    public R profitDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            records = Db.find(Db.getSqlPara("statistics.profitDept2", statisticsType));
        } else {
            records = Db.find(Db.getSqlPara("statistics.profitDept1", statisticsType));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R profitTime(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.profitTime", statisticsType));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R profitWorkphase(WorkParams workParams) {
        Record workphaseStatisticsField = statisticsTypeUtils.getWorkphaseStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.profitWorkphase", workphaseStatisticsField));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R workCost(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return costDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return costTime(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.WORKTYPE.getType()) {
            return costWorkphase(workParams);
        }
        return R.error("查询失败");
    }

    public R costDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records = new ArrayList<>();
        List<Record> isAll = new ArrayList<>();
        List<Record> notAll = new ArrayList<>();
        Integer rate = workParams.getRate();
        double temp = workService.divide(Double.parseDouble(rate.toString()), 100, 2);
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            isAll = Db.find(Db.getSqlPara("statistics.costDept2",  statisticsType.set("isAll",2)));
            notAll = Db.find(Db.getSqlPara("statistics.costDept2", statisticsType.set("isAll",2).set("rate",temp)));
        } else {
            isAll = Db.find(Db.getSqlPara("statistics.costDept1", statisticsType.set("isAll", 1)));
            notAll = Db.find(Db.getSqlPara("statistics.costDept1", statisticsType.set("isAll",2).set("rate",temp)));

        }
        DecimalFormat df = new DecimalFormat("0%");
        for (int i = 0; i < isAll.size(); i++) {
            Record all = isAll.get(i);
            String allNum = all.getStr("counts");
            String notAllNum = notAll.get(i).getStr("counts");
            Record record = new Record();
            if(!"0".equals(allNum)||!"0".equals(notAllNum)){
                double divide = workService.divide(Double.parseDouble(notAllNum), Double.parseDouble(allNum), 2);
                String format = df.format(divide);
                record.set("rate",format.substring(0,format.length()-1));
            }else {
                record.set("rate",0);
            }
            record.set("name",all.getStr("name"));
            record.set("num",notAllNum);
            records.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R costTime(WorkParams workParams) {
        List<Record> records = new ArrayList<>();
        Integer rate = workParams.getRate();
        double temp = workService.divide(Double.parseDouble(rate.toString()), 100, 2);
        Record statisticsType = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> records1 = Db.find(Db.getSqlPara("statistics.costTime", statisticsType.set("isAll", 1)));
        List<Record> records2 = Db.find(Db.getSqlPara("statistics.costTime", statisticsType.set("isAll", 2).set("rate", temp)));
        DecimalFormat df = new DecimalFormat("0%");
        for (int i = 0; i < records1.size(); i++) {
            Record record = new Record();
            String allNum = records1.get(i).getStr("counts");
            String notAllNum = records2.get(i).getStr("counts");
            if(!"0".equals(allNum)||!"0".equals(notAllNum)){
                double divide = workService.divide(Double.parseDouble(notAllNum), Double.parseDouble(allNum), 2);
                String format = df.format(divide);
                record.set("rate",format.substring(0,format.length()-1));
            }else {
                record.set("rate",0);
            }
            record.set("name",records1.get(i).getStr("name"));
            record.set("num",notAllNum);
            records.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R costWorkphase(WorkParams workParams) {
        List<Record> records = new ArrayList<>();
        Integer rate = workParams.getRate();
        double temp = workService.divide(Double.parseDouble(rate.toString()), 100, 2);
        Record workphaseStatisticsField = statisticsTypeUtils.getWorkphaseStatisticsField(workParams);
        List<Record> records1 = Db.find(Db.getSqlPara("statistics.costWorkphase", workphaseStatisticsField.set("isAll", 1)));
        List<Record> records2 = Db.find(Db.getSqlPara("statistics.costWorkphase", workphaseStatisticsField.set("isAll", 2).set("rate", temp)));
        DecimalFormat df = new DecimalFormat("0%");
        for (int i = 0; i < records1.size(); i++) {
            Record record = new Record();
            String allNum = records1.get(i).getStr("counts");
            String notAllNum = records2.get(i).getStr("counts");
            if(!"0".equals(allNum)||!"0".equals(notAllNum)){
                double divide = workService.divide(Double.parseDouble(notAllNum), Double.parseDouble(allNum), 2);
                String format = df.format(divide);
                record.set("rate",format.substring(0,format.length()-1));
            }else {
                record.set("rate",0);
            }
            record.set("name",records1.get(i).getStr("name"));
            record.set("num",notAllNum);
            records.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R workWarning(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return warningDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return warningTime(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.WORKPHASE.getType()) {
            return costWorkphase(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.WORKTYPE.getType()) {
            return costWorkphase(workParams);
        }
        return R.error("查询失败");
    }

    public R warningDept(WorkParams workParams) {
        Record dpteStatisticsField = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records = new ArrayList<>();
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            //筛选
            List<Map<String, Object>> deptIds = (List<Map<String, Object>>) dpteStatisticsField.get("deptIds");
            for (Map<String, Object> deptId : deptIds) {
                //筛选的区域/部门id
                Record record = new Record();
                Object dept = deptId.get("dept");
                Object below = deptId.get("below");
                List<Record> works = Db.find(Db.getSqlPara("statistics.queryWorkIdByDeptId", Kv.by("below", below).set("isDept", dpteStatisticsField.get("isDept"))
                        .set("sqlDateFormat",dpteStatisticsField.get("sqlDateFormat")).set("beginTime",dpteStatisticsField.get("beginTime")).set("finalTime",dpteStatisticsField.get("finalTime"))));
                if(works.size()==0){
                    record.set("settingAlarmNum", 0);
                    record.set("settingWarningNum", 0);
                    record.set("name", Aop.get(AdminDept.class).findById(dept).getName());
                    records.add(record);
                    continue;
                }
                queryWarning(works,record,records, Aop.get(AdminDept.class).findById(dept).getName());

            }
        } else {
            List<Record> deptIds = dpteStatisticsField.get("deptIds");
            for (Record deptId : deptIds) {
                Object id = deptId.get("dept_id");
                Record record = new Record();
                Kv set = Kv.by("below", id).set("isDept", dpteStatisticsField.get("isDept"))
                        .set("sqlDateFormat", dpteStatisticsField.get("sqlDateFormat")).set("beginTime", dpteStatisticsField.get("beginTime")).set("finalTime", dpteStatisticsField.get("finalTime"));
                List<Record> works = Db.find(Db.getSqlPara("statistics.queryWorkIdByDeptId", set));
                if(works.size()==0){
                    record.set("setting_alarm_num", 0);
                    record.set("setting_warning_num", 0);
                    record.set("name", Aop.get(AdminDept.class).findById(id).getName());
                    records.add(record);
                    continue;
                }
                queryWarning(works,record,records, Aop.get(AdminDept.class).findById(id).getName());

            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R  warningTime(WorkParams workParams){
        Record timeStatisticsField = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Map<String, Object>> timeList = timeStatisticsField.get("timeList");
        List<Record> r = new ArrayList<>();
        for (Map<String, Object> time : timeList) {
            time.put("sqlDateFormat",timeStatisticsField.get("sqlDateFormat"));
            List<Record> records = Db.find(Db.getSqlPara("statistics.warningTime", time));
            Record record = new Record();
            if(records.size()==0){
                record.set("setting_alarm_num", 0);
                record.set("setting_warning_num", 0);
                record.set("name", time.get("startTime"));
                r.add(record);
                continue;
            }
            queryWarning(records,record,r, time.get("startTime"));

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", r);
        return R.ok().put("data", jsonObject);
    }

    private void queryWarning(List<Record> records,Record record,List<Record> r,Object name){
        //预警数量
        Integer settingWarningNum = Db.template("statistics.settingWarningNum", Kv.by("works", records)).queryInt();
        //警告数量
        Integer settingAlarmNum = Db.template("statistics.settingAlarmNum", Kv.by("works", records)).queryInt();
        record.set("setting_alarm_num", settingAlarmNum);
        record.set("setting_warning_num", settingWarningNum);
        record.set("name", name);
        r.add(record);
    }
}
