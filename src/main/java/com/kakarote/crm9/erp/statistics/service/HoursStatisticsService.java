package com.kakarote.crm9.erp.statistics.service;

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
import java.util.ArrayList;
import java.util.List;

public class HoursStatisticsService {

    @Inject
    StatisticsTypeUtils statisticsTypeUtils;

    public R distribution(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return distributionDept(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.TIME.getType()) {
            return distributionDeptStatisticsTime(workParams);
        } else if (workParams.getTypeId() == StatisticsFieldEnum.HOURS_TYPE.getType()) {
            return distributionType(workParams);
        }
        return R.error("查询失败");
    }

    private R distributionType(WorkParams workParams) {
        Record workphaseStatisticsField = statisticsTypeUtils.getWorkphaseStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.hoursDistributionType", workphaseStatisticsField));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    private R distributionDeptStatisticsTime(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.hoursDistributionTime", statisticsType));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    private R distributionDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            records = Db.find(Db.getSqlPara("statistics.hoursDistributionDept1", statisticsType));
        } else {
            records = Db.find(Db.getSqlPara("statistics.hoursDistributionDept2", statisticsType));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R ranking(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return rankingDept(workParams);
        }
        return R.error("查询失败");
    }

    private R rankingDept(WorkParams workParams) {
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> list = new ArrayList<>();
        List<Record> records;
        List<Record> recordsResources;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            records = Db.find(Db.getSqlPara("statistics.rankingDept1", statisticsType));
            recordsResources = Db.find(Db.getSqlPara("statistics.rankingDeptResources1", statisticsType));

        } else {
            records = Db.find(Db.getSqlPara("statistics.rankingDept2", statisticsType));
            recordsResources = Db.find(Db.getSqlPara("statistics.rankingDeptResources2", statisticsType));
        }
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            Record recordsResource = recordsResources.get(i);
            BigDecimal recordCounts = new BigDecimal(record.getStr("counts"));
            BigDecimal recordsResourceCounts = new BigDecimal(recordsResource.getStr("counts"));
            Record result = new Record();
            if(recordCounts.equals(BigDecimal.ZERO)||recordsResourceCounts.equals(BigDecimal.ZERO)){
                result.set("name",record.getStr("name"));
                result.set("counts",0);
            }else {
                BigDecimal multiply = recordsResourceCounts.multiply(new BigDecimal(8));
                BigDecimal divide = recordCounts.divide(multiply, 2, BigDecimal.ROUND_HALF_UP);
                result.set("name",record.getStr("name"));
                result.set("counts",divide.toString());
            }
            list.add(result);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        return R.ok().put("data", jsonObject);
    }
}
