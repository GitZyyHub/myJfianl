package com.kakarote.crm9.erp.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.card.service.AdminVisitingCardService;
import com.kakarote.crm9.utils.BaseUtil;

import java.util.List;

/**
 * @author zxy
 * 名片表
 */
public class AdminVisitingCardController extends Controller {
    @Inject
    private AdminVisitingCardService service;
    /**
     * @author zxy
     * 设为员工访问认名片
     */
    public void setSaveCard(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(service.setSaveCard(jsonObject));
    }
    /**
     * @author zxy
     * 移除员工访问认名片
     */
    public void deleteByUserId(@Para("userId") Long userId){
        renderJson(service.deleteByUserId(userId));
    }

    /**
     * @author zxy
     * 根据员工id，获取名片
     */
    @Clear
    public void queryByUserId(@Para("userId") Long userId){
        renderJson(service.queryByUserId(userId));
    }
    /**
     * @author zxy
     * pc获取名片信息
     */
    public void queryPcByUserId(){
        Long userId = BaseUtil.getUserId();
        renderJson(service.queryByUserId(userId));
    }
    /**
     * @author zxy
     * 小程序保存个人信息
     */
    @Clear
    public void save(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        BaseUtil.setUser(jsonObject.getLong("userId"));
        renderJson(service.save(jsonObject));
    }
    /**
     * @author zxy
     * pc保存个人信息
     */
    public void savePc(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(service.save(jsonObject));
    }

    /**
     * @author zxy
     * 保存海报
     */
    public void upload(){
        String prefix= BaseUtil.getDate();
        renderJson(service.upload(getFile("file",prefix),prefix,getPara("userId")));
    }

    public void checkAuth(){
        renderJson(service.checkAuth());
    }

    /**
     * @author zxy
     * 设为关联人员
     */
    public void relevance(){
        List<Long> userIds = JSON.parseArray(getRawData(),Long.class);
        renderJson(service.relevance(userIds));
    }

    /**
     * @author zxy
     * 解除关联人员
     */
    public void relieve(){
        List<Long> userIds = JSON.parseArray(getRawData(),Long.class);
        renderJson(service.relieve(userIds));
    }

}
