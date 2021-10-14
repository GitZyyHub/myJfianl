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

import java.util.ArrayList;
import java.util.List;

public class TaskStatisticsService {

    @Inject
    StatisticsTypeUtils statisticsTypeUtils;

    public R taskDistribution(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return distributionDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return distributionDeptStatisticsTime(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TASK_TYPE.getType()) {
            return distributionType(workParams);
        }
        return null;
    }

    private R distributionType(WorkParams workParams) {
        Record record = statisticsTypeUtils.analyzeTime(workParams);
        record.set("isParent",workParams.getIsParent());
        Integer accomplish = 0, unfinished = 0, postpone = 0;
        List<Record> list = new ArrayList<>();
        if (workParams.getPara() == null) {
            accomplish = Db.template("statistics.taskDistributionType", record.set("type", 1).getColumns()).queryInt();
            unfinished = Db.template("statistics.taskDistributionType", record.set("type", 2).getColumns()).queryInt();
            postpone = Db.template("statistics.taskDistributionType", record.set("type", 3).getColumns()).queryInt();
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
            Integer typeNum = Db.template("statistics.taskDistributionType", record.set("type", workParams.getPara()).getColumns()).queryInt();
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
        record.set("isParent",workParams.getIsParent());
        List<Record> isAll = Db.find(Db.getSqlPara("statistics.taskDistributionTime", record.set("isAll",true)));
        List<Record> notAll = Db.find(Db.getSqlPara("statistics.taskDistributionTime", record.set("isAll",false)));
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

    private R distributionDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        statisticsType.set("isParent",workParams.getIsParent());
        List<Record> isAll;
        List<Record> notAll;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            isAll = Db.find(Db.getSqlPara("statistics.taskDistributionDept1", statisticsType.set("isAll", true)));
            notAll = Db.find(Db.getSqlPara("statistics.taskDistributionDept1", statisticsType.set("isAll", false)));
        } else {
            isAll = Db.find(Db.getSqlPara("statistics.taskDistributionDept2", statisticsType.set("isAll", true)));
            notAll = Db.find(Db.getSqlPara("statistics.taskDistributionDept2", statisticsType.set("isAll", false)));
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

    public R taskWarning(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return warningDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return warningTime(workParams);
        }
        return null;
    }

    private R warningTime(WorkParams workParams) {
        Record timeStatisticsField = statisticsTypeUtils.getTimeStatisticsField(workParams);
        timeStatisticsField.set("isParent",workParams.getIsParent());
        List<Record> warning = new ArrayList<>();
        List<Record> warn = new ArrayList<>();
        List<Record> list = new ArrayList<>();
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            warn = Db.find(Db.getSqlPara("statistics.taskWarningStatisticsTime", timeStatisticsField.set("warning", true)));
            warning  = Db.find(Db.getSqlPara("statistics.taskWarningStatisticsTime", timeStatisticsField.set("warning", false)));
        } else {
            warn = Db.find(Db.getSqlPara("statistics.taskWarningStatisticsTime", timeStatisticsField.set("warning", true)));
            warning = Db.find(Db.getSqlPara("statistics.taskWarningStatisticsTime", timeStatisticsField.set("warning", false)));
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
        statisticsType.set("isParent",workParams.getIsParent());
        List<Record> warning = new ArrayList<>();
        List<Record> warn = new ArrayList<>();
        List<Record> list = new ArrayList<>();
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            warn  = Db.find(Db.getSqlPara("statistics.taskWarningDept1", statisticsType.set("warning", true)));
            warning = Db.find(Db.getSqlPara("statistics.taskWarningDept1", statisticsType.set("warning", false)));
        } else {
            warn  = Db.find(Db.getSqlPara("statistics.taskWarningDept2", statisticsType.set("warning", true)));
            warning = Db.find(Db.getSqlPara("statistics.taskWarningDept2", statisticsType.set("warning", false)));
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
