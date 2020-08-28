package com.kakarote.crm9.erp.crm.cron;

import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.crm.entity.CrmCustomerPool;
import com.kakarote.crm9.erp.crm.service.CrmCustomerPoolService;
import com.kakarote.crm9.utils.BaseUtil;

import java.util.List;

/**
 * 客户定时放入公海
 *
 * @author zhangzhiwei
 */
public class CrmCustomerCron implements Runnable {
    @Override
    public void run() {

            try {
                List<CrmCustomerPool> poolList = CrmCustomerPool.dao.find("select pool_id,member_user_id from 72crm_crm_customer_pool where status = 1 and put_in_rule = 1");
                Aop.get(CrmCustomerPoolService.class).putInInternational(poolList);
            } finally {
                BaseUtil.removeThreadLocal();
            }
    }
}
