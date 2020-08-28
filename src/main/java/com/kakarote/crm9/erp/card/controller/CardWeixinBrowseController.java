package com.kakarote.crm9.erp.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.card.entity.CardWeixinBrowse;
import com.kakarote.crm9.erp.card.service.CardWeixinBrowseService;

@Clear
public class CardWeixinBrowseController extends Controller {
    @Inject
    private CardWeixinBrowseService service;

    /**
     * @author zxy
     * 添加微信浏览
     */
    public void save(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(service.save(jsonObject));
    }
    /**
     * @author zxy
     * 根据名片夹id分页查询微信浏览人
     */
    public void queryPage(BasePageRequest<CardWeixinBrowse> base){
        renderJson(service.queryPage(base));
    }
    /**
     * @author zxy
     * 根据员工id分页查询微信浏览人个数
     */
    public void queryCount(@Para("userId") Long userId){
        renderJson(service.queryCount(userId));
    }
}
