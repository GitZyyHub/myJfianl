package com.kakarote.crm9.erp.admin.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminMenuService;
import com.kakarote.crm9.utils.R;

public class AdminMenuController extends Controller {
    @Inject
    private AdminMenuService adminMenuService;


    /**
     * @author wyq
     * 展示全部菜单
     */
    public void getAllMenuList(){
        renderJson(R.ok().put("data", adminMenuService.getMenuList(0, BaseConstant.AUTH_DATA_RECURSION_NUM)));
    }

    /**
     * 通过角色菜单查询菜单
     * @param roleType 角色类型
     */
    public void getMenuListByType(@Para("roleType") Integer roleType){
        renderJson(adminMenuService.getMenuListByType(roleType));
    }

    /**
     * @author hmb
     * 展示全部菜单
     */
    public void getWorkMenuList(){
        renderJson(adminMenuService.getMenuListByType(5));
    }
}
