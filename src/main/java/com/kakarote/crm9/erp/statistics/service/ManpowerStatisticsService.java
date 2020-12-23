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

import java.util.List;

/**
 * @author zyy
 */
public class ManpowerStatisticsService {

    @Inject
    StatisticsTypeUtils statisticsTypeUtils;

    public R manpowerDistribution(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.DEPT.getType() || workParams.getTypeId() == StatisticsFieldEnum.AREA.getType()) {
            return distributionDept(workParams);
        }
        return R.error("查询失败");
    }

    public R distributionDept(WorkParams workParams){
        Record statisticsType = statisticsTypeUtils.getDpteStatisticsField(workParams);
        List<Record> records;
        if (!StrUtil.isEmpty(workParams.getDeptId()) || !StrUtil.isEmpty(workParams.getArea())) {
            records = Db.find(Db.getSqlPara("statistics.queryUserByDept2", statisticsType));
        } else {
            records = Db.find(Db.getSqlPara("statistics.queryUserByDept1", statisticsType));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }

    public R distributionDeptStatisticsTime(WorkParams workParams){
        Record statisticsType = statisticsTypeUtils.getTimeStatisticsField(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.queryUserByTime", statisticsType));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        return R.ok().put("data", jsonObject);
    }
}
