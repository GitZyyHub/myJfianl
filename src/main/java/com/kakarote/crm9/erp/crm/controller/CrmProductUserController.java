package com.kakarote.crm9.erp.crm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.crm.service.CrmProductUserService;

/**
 * @author zxy
 * 产品员工小程序显示关联表
 */
public class CrmProductUserController extends Controller {
    @Inject
    private CrmProductUserService service;
    /**
     * 设置员工关联的产品
     */
    public void save(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(service.save(jsonObject));
    }
    /**
     * 查询员工关联产品
     */
    public void queryByUserId(@Para("userId") Long userId){
        renderJson(service.queryByUserId(userId));
    }
}
