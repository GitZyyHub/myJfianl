package com.kakarote.crm9.erp.work.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.work.controller.*;

public class WorkRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new WorkInterceptor());
        addInterceptor(new WorkAuthInterceptor());
        add("/work", WorkController.class);
        add("/task", TaskController.class);
        add("/work/milestone",MilestoneController.class);
        add("/work/resources",WorkResourcesSettingController.class);
        add("/work/risk",WorkRiskController.class);
        add("/work/change",WorkChangeController.class);
        add("/task/config",TaskConfigController.class);
        add("/work/config",ConfigManageController.class);
        add("/work/demand",WorkDemandController.class);
        add("/work/historical",WorkHistoricalRecordController.class);
        add("/work/product",WorkProductController.class);
        add("/work/product/funtion",WorkProductFuntionController.class);
        add("/task/hours",TaskHoursController.class);
        add("/task/file",TaskFileController.class);
        add("/work/file",WorkTaskFileController.class);
        add("/task/historical",WorkTaskHistoricalController.class);
        add("/task/hoursLock",TaskHoursLockController.class);
        add("/task/hoursRecord",TaskHoursRecordController.class);
        add("/taskLabel",LabelsController.class);
       /* add("/workbench", WorkbenchController.class);
        add("/workTrash", TrashController.class);*/
    }

}
