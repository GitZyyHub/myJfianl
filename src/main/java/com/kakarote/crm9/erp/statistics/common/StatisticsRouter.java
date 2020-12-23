package com.kakarote.crm9.erp.statistics.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.statistics.controller.ManpowerStatisticsController;
import com.kakarote.crm9.erp.statistics.controller.ProductStatisticsController;
import com.kakarote.crm9.erp.statistics.controller.RiskStatisticsController;
import com.kakarote.crm9.erp.statistics.controller.WorkStatisticsController;
import com.kakarote.crm9.erp.work.controller.*;

public class StatisticsRouter extends Routes {
    @Override
    public void config() {
        add("/statistics", WorkStatisticsController.class);
        add("/riskStatistics", RiskStatisticsController.class);
        add("/productStatistics", ProductStatisticsController.class);
        add("/manpowerStatistics", ManpowerStatisticsController.class);
    }

}
