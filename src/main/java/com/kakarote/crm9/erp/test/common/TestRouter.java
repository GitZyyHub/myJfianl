package com.kakarote.crm9.erp.test.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.test.controller.TestAdminLoginController;

/**
 * @author zyy
 */
public class TestRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new TestInterceptor());
        add("/testLogin", TestAdminLoginController.class);
    }
}
