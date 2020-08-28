package com.kakarote.crm9.erp.email.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.email.controller.EmailAccountController;
import com.kakarote.crm9.erp.email.controller.EmailRecordController;

public class EmailRoutes extends Routes {
    @Override
    public void config() {
        addInterceptor(new EmailInterceptor());
        add("/EmailAccount", EmailAccountController.class);
        add("/EmailRecord", EmailRecordController.class);
    }
}
