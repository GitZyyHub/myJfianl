package com.kakarote.crm9.erp.card.common;

import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import com.kakarote.crm9.common.config.JfinalConfig;


public class WxMaServiceCongigImpl extends WxMaServiceImpl {

    public WxMaServiceCongigImpl() {
        WxMiNiAppOpenRedisConfigStorage miNiAppConfig = new WxMiNiAppOpenRedisConfigStorage();
        miNiAppConfig.setAppid(JfinalConfig.prop.get("MiNiApp.AppId"));
        miNiAppConfig.setSecret(JfinalConfig.prop.get("MiNiApp.Secret"));
        setWxMaConfig(miNiAppConfig);
    }

}
