package com.kakarote.crm9.erp.test.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * @author zyy
 */
public class TestInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        inv.invoke();
    }

}
