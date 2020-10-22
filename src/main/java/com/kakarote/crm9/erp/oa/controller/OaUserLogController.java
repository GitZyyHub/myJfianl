package com.kakarote.crm9.erp.oa.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.oa.entity.OaUserLog;
import com.kakarote.crm9.erp.oa.service.OaLogService;
import com.kakarote.crm9.erp.oa.service.OaUserLogService;
import com.kakarote.crm9.utils.R;

/**
 * @author zyy
 */
public class OaUserLogController extends Controller {

    @Inject
    OaUserLogService oaUserLogService;

    @Inject
    private OaLogService oaLogService;

    /**
     * 查询用户日志
     */
    public void findUserLog(@Para("") BasePageRequest<OaUserLog> basePageRequest){
        renderJson(oaUserLogService.findUserLog(basePageRequest));
    }


    /**
     * 根据日志id删除日志
     * @param logId 日志ID
     * @author zhangzhiwei
     */
    public void deleteById(@Para("logId") Integer logId){
        /*boolean oaAuth = AuthUtil.isOaAuth(OaEnum.LOG_TYPE_KEY, logId);
        if(oaAuth){renderJson(R.noAuth());return;}*/
        renderJson(oaUserLogService.deleteById(logId) ? R.ok() : R.error("删除失败"));
    }

    /**
     * 添加日志
     * @author zhangzhiwei
     */
    public void addOrUpdate(){
        renderJson(oaUserLogService.saveAndUpdate(JSON.parseObject(getRawData())));
    }


}
