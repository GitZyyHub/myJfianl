package com.kakarote.crm9.erp.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.admin.service.AdminConfigService;
import com.kakarote.crm9.utils.R;

/**
 * @author wyq
 */
public class AdminConfigController extends Controller {
    @Inject
    private AdminConfigService adminConfigService;

    /**
     * 查询首页顶部导航栏设置
     */
    public void queryHeaderModelSort() {
        renderJson(R.ok().put("data", adminConfigService.queryHeaderModelSort()));
    }

    /**
     * 修改首页顶部导航栏设置
     */
    public void setHeaderModelSort() {
        JSONArray jsonArray = JSON.parseArray(getRawData());
        renderJson(adminConfigService.setHeaderModelSort(jsonArray));
    }
}
