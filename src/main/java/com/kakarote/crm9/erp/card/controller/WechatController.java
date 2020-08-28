package com.kakarote.crm9.erp.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.interceptor.ErpInterceptor;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.card.service.WechatService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;


@Clear
public class WechatController extends Controller {

    @Inject
    private static WechatService wechatService;

    @Inject
    private AdminRoleService adminRoleService;

    /**
     * 跟进微信小程序appid和code获取微信Openid
     */

    public void queryWechat(@Para("code") String code, @Para("userId") Long userId, @Para("appid") String appid, @Para("type") Integer type) {
        renderJson(wechatService.queryWechat(code, userId, appid, type));
    }

    /**
     * openid绑定员工
     */
    public void saveleadsUser() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        String openid = jsonObject.getString("openid");
        Long userId = jsonObject.getLong("userId");
        renderJson(wechatService.saveleadsUser(openid, userId));
    }

    /**
     * openid绑定员工
     */
    public void bindingUser() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(wechatService.bindingUser(jsonObject));
    }

    /**
     * 绑定线索信息
     */
    public void bindingLeads() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(wechatService.bindingLeads(jsonObject));
    }

    /**
     * 回调函数 获取encryptedData 解密获取用户信息
     */
    public void callback() {
        JSONObject jsonObject = JSON.parseObject(getRawData());
        R r = wechatService.queryPhone(jsonObject);
        renderJson(r);
    }

    /**
     * 后台获取小程序二维码
     */
    @Before(ErpInterceptor.class)
    public void query(@Para("type") Integer type) {
        renderJson(wechatService.query(type, BaseUtil.getUserId()));
    }

    /**
     * 小程序获取小程序二维码
     */

    public void queryMiNi(@Para("type") Integer type, @Para("userId") Long userId) {
        BaseUtil.setUser(userId);
        renderJson(wechatService.query(type,userId));
    }

}
