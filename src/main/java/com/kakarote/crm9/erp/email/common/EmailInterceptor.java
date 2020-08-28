package com.kakarote.crm9.erp.email.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.utils.R;

public class EmailInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        //设置邮箱账号
        EmailAccount.set();
        if(EmailAccount.get()==null){
            invocation.getController().renderJson(R.error("请先绑定邮箱账号！"));
            return;
        }
        try {
            invocation.invoke();
        }finally {
            EmailAccount.remove();
        }

    }
}
