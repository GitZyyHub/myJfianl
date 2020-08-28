package com.kakarote.crm9.erp.bi.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.bi.service.BiFunnelService;

public class BiFunnelController extends Controller {
    @Inject
    private BiFunnelService service;
    /**
     * 销售漏斗
     * @author zxy
     */
    @Permissions("bi:business:read")
    @SysLog(LogEnum.BUSINESS_FUNNEL)
    public void sellFunnel(@Para("")BiParams biParams){
        renderJson(service.sellFunnel(biParams));
    }

    /**
     * 商机数详情
     */
    public void businessNum(@Para("") BasePageRequest<BiParams> basePageRequest) {
        renderJson(service.businessNum(basePageRequest));
    }

    /**
     * 新增商机分析图
     * @author zxy
     */
    @Permissions("bi:business:read")
    @SysLog(LogEnum.BUSINESS_ADD_BUSINESS)
    public void addBusinessAnalyze(@Para("")BiParams biParams){
        renderJson(service.addBusinessAnalyze(biParams));
    }
    /**
     * 新增商机分析表
     * @author zxy
     */
    @Permissions("bi:business:read")
    public void sellFunnelList(BasePageRequest<BiParams> basePageRequest) {
        renderJson(service.sellFunnelList(basePageRequest));
    }
    /**
     * 商机转化率分析
     * @author zxy
     */
    @Permissions("bi:business:read")
    @SysLog(LogEnum.BUSINESS_CONVERSION)
    public void win(@Para("")BiParams biParams){
        renderJson(service.win(biParams));
    }
}
