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
        add("/taskLabel", LabelController.class);
        add("/workbench", WorkbenchController.class);
        add("/workTrash", TrashController.class);
    }

}
