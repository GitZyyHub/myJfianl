package com.kakarote.crm9;


import cn.hutool.core.util.ReUtil;
import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp._MappingKit;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.WorkService;
import com.kakarote.crm9.utils.R;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class test {

    protected static DruidPlugin dp;
    protected static ActiveRecordPlugin arp = null;
    protected static C3p0Plugin c3p0Plugin = null;
    @BeforeAll
    static void initAll(){
        if(c3p0Plugin==null){
            PropKit.use("config.properties");
            c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
            c3p0Plugin.start();
        }

        if (arp==null) {
            arp = new ActiveRecordPlugin(c3p0Plugin);
            // 打印sql语句
            //arp.setShowSql(true);
            // 数据库映射
            _MappingKit.mapping(arp);
            arp.start();
        }
        //System.out.println("Begin...");
    }
    @AfterAll
    static void tearDownAll() {
        WorkService workService = new WorkService();
        JSONObject jsonObject = new JSONObject();
        Work work = new Work();
        work.setWorkName("测试100000");
        work.setWorkPhase("项目立项");
        work.setWorkType("销售开拓");
        work.setDeptId(16672);
        work.setArea(16671);
        work.setManagerId(16362);
        work.setSalesId(16362L);
        work.setStartTime(new Date());
        work.setEndTime(new Date());
        jsonObject.put("entity",work);
        System.out.println(LocalDateTime.now());
        for (int i = 0; i < 100000; i++) {
            workService.setWork(jsonObject);
        }
        System.out.println(LocalDateTime.now());
    }


    /*@Before
    public void before() {
        // 配置C3p0数据库连接池插件
        C3p0Plugin cp = EyaoSmartConfig.createC3p0Plugin();
        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
        arp.setDialect(new AnsiSqlDialect());// 使用AnsiSqlDialect
        arp.setContainerFactory(new CaseInsensitiveContainerFactory());
        arp.setShowSql(true);
        // 所有配置在 MappingKit 中搞定
        _MappingKit.mapping(arp);
        cp.start();
        arp.start();
    }

    @Test
    public void test01(){
        PaaApplyPrimary.dao.findById("PAA2017012018030198062864");
    }

    @Test
    public void test02(){
        String userId = "EMP2016112314173715944824";
        String orgId = "ORG004";
        String orgunitId = "OU001";
        Page<PaaApplyPrimary> page = PaaApplyPrimary.dao.findPromotionalActivityApplyPersonalTask(1, 2, userId, orgId, orgunitId, 2017);
        System.out.println(page.getList());
        System.out.println(page.getTotalRow());
    }*/
    public static void main(String[] args) throws Exception {

        tearDownAll();


        /*WorkService workService = new WorkService();
        JSONObject jsonObject = new JSONObject();
        Work work = new Work();
        work.setWorkName("测试100000");
        work.setWorkPhase("项目立项");
        work.setWorkType("销售开拓");
        work.setDeptId(16672);
        work.setArea(16671);
        work.setManagerId(16362);
        work.setSalesId(16362L);
        work.setStartTime(new Date());
        work.setEndTime(new Date());
        jsonObject.put("entity",work);
        System.out.println(LocalDateTime.now());
        for (int i = 0; i < 100000; i++) {
            workService.setWork(jsonObject);
        }
        System.out.println(LocalDateTime.now());*/
    }

}

