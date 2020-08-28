package com.kakarote.crm9.erp.card.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.card.entity.CardWeixinLeadsUser;
import com.kakarote.crm9.erp.card.service.CardWeixinLeadsUserService;

@Clear
public class CardWeixinLeadsUserController extends Controller {
    @Inject
    private CardWeixinLeadsUserService service;
    /**
     * @author zxy
     * 添加名片夹
     */
   /* public void save(){
        JSONObject jsonObject = JSON.parseObject(getRawData());
        renderJson(service.save(jsonObject));
    }*/
    /**
     * @author zxy
     * 查询名片夹，根据微信线索查询我扫的员工
     */
    public void queryPage(BasePageRequest<CardWeixinLeadsUser> base){
        renderJson(service.queryPage(base));
    }
    /**
     * @author zxy
     * 删除名片夹
     */
    public void deleteById(){
        Integer weixinUserId = JSON.parseObject(getRawData()).getInteger("weixinUserId");
        renderJson(service.deleteById(weixinUserId));
    }

    /**
     * @author zxy
     * 查询名片线索
     */
    public void queryByWeixinLeadsId(BasePageRequest<CardWeixinLeadsUser> base){
        renderJson(service.queryPage(base));
    }
}
