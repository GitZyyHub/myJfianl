package com.kakarote.crm9.erp.statistics.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.statistics.common.StatisticsTypeUtils;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * @author zyy
 */
public class WorkStatisticsService implements StatisticsTypeUtils {

    public R workDeptStatistics(WorkParams workParams){
        Record statisticsType = getStatisticsType(workParams);
        List<Record> records = Db.find(Db.getSqlPara("statistics.workDeptStatistics", statisticsType));
        return R.ok().put("data",records);
    }


}
