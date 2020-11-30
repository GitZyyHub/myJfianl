package com.kakarote.crm9.erp.statistics.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.erp.statistics.service.WorkStatisticsService;

/**
 * @author zyy
 */
public class WorkStatisticsController extends Controller {

    @Inject
    WorkStatisticsService workStatisticsService;

    public void workDeptStatistics(@Para("") WorkParams workParams){
        renderJson(workStatisticsService.workDeptStatistics(workParams));
    }


}
