package com.kakarote.crm9.erp.oa.cron;

import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.oa.service.OaEventService;
import com.kakarote.crm9.utils.BaseUtil;


public class EventNoticeCron implements Runnable {
    @Override
    public void run() {

            try {
                Aop.get(OaEventService.class).eventNoticeCron();
            } finally {
                BaseUtil.removeThreadLocal();
            }
    }
}
