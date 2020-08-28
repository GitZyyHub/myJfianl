package com.kakarote.crm9.erp.card.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.card.controller.*;


public class CardRouter extends Routes {
    @Override
    public void config() {
        add("/wechat", WechatController.class);
        add("/CrmProductDetailImg", CrmProductDetailImgController.class);
        add("/CrmWeixinLeads", CardWeixinLeadsController.class);
        add("/officialImg", AdminOfficialImgController.class);
        add("/visitingCard", AdminVisitingCardController.class);
        add("/WeixinBrowse", CardWeixinBrowseController.class);
        add("/WeixinLeadsUser", CardWeixinLeadsUserController.class);
    }

}
