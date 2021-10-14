package com.kakarote.crm9.erp.statistics.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.statistics.controller.*;
import com.kakarote.crm9.erp.work.controller.*;

public class StatisticsRouter extends Routes {
    @Override
    public void config() {
        add("/statistics", WorkStatisticsController.class);
        add("/riskStatistics", RiskStatisticsController.class);
        add("/productStatistics", ProductStatisticsController.class);
        add("/manpowerStatistics", ManpowerStatisticsController.class);
        add("/milestoneStatistics", MilestoneStatisticsController.class);
        add("/taskStatistics", TaskStatisticsController.class);
        add("/demandStatistics", DemandStatisticsController.class);
        add("/configStatistics", ConfigStatisticsController.class);
        add("/hoursStatistics", HoursStatisticsController.class);
        add("/resourcesStatistics", ResourcesSettingStatisticsController.class);
    }

}
