package com.kakarote.crm9.erp.statistics.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.erp.statistics.service.ProductStatisticsService;

/**
 * @author zyy
 */
public class ProductStatisticsController extends Controller {

    @Inject
    ProductStatisticsService productStatisticsService;

    public void productUsage(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.productUsage(workParams));
    }

    public void productPercentage(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.productPercentage(workParams));
    }

    public void workSet(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.workSet(workParams));
    }

    public void searchWorkSet(@Para("")WorkParams workParams){
        renderJson(productStatisticsService.searchWorkSet(workParams));
    }

}
