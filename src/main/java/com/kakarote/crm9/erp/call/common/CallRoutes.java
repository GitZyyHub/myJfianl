package com.kakarote.crm9.erp.call.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.call.controller.CallController;

public class CallRoutes extends Routes {
    @Override
    public void config() {
        add("/call", CallController.class);
    }
}
