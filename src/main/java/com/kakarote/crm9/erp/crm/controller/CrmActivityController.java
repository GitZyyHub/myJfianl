package com.kakarote.crm9.erp.crm.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmActivity;
import com.kakarote.crm9.erp.crm.service.CrmActivityService;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.R;

/**
 * @author : hmb
 * create at:  2019/9/25 0025  11:08
 * @description: crm活动
 */
public class CrmActivityController extends Controller{

    @Inject
    private CrmActivityService crmActivityService;

    /**
     * 添加跟进记录
     * @param crmActivity
     */
    @NotNullValidate(value = "activityTypeId",message = "类型id不能为空")
    @NotNullValidate(value = "activityType",message = "类型不能为空")
    @NotNullValidate(value = "content",message = "内容不能为空")
    @NotNullValidate(value = "category",message = "跟进类型不能为空")
    public void addCrmActivityRecord(@Para("") CrmActivity crmActivity){
        boolean auth = AuthUtil.isCrmAuth(CrmEnum.parse(crmActivity.getActivityType()), crmActivity.getActivityTypeId());
        if(auth){renderJson(R.noAuth()); return; }
        renderJson(crmActivityService.addCrmActivityRecord(crmActivity));
    }

    /**
     * 分页查询活动列表
     * @param pageRequest
     */
    public void getCrmActivityPageList(BasePageRequest<CrmActivity> pageRequest){
        CrmActivity crmActivity = pageRequest.getData();
        boolean isPool = true;
        if (crmActivity.getCrmType() == 2){
            isPool = AuthUtil.isPoolAuth(crmActivity.getActivityId());
        }
        if (isPool){
            boolean auth = AuthUtil.isCrmAuth(CrmEnum.parse(crmActivity.getCrmType()), crmActivity.getActivityTypeId());
            if(auth){renderJson(R.noAuth()); return; }
        }
        renderJson(crmActivityService.getCrmActivityPageList(pageRequest));
    }

    /**
     * 删除跟进记录
     * @param activityId
     */
    public void deleteCrmActivityRecord(@Para("activityId") Integer activityId){
        renderJson(crmActivityService.deleteCrmActivityRecord(activityId));
    }

    /**
     * 外勤签到
     */
    @Permissions("crm:outwork:save")
    public void outworkSign(@Para("") CrmActivity crmActivity) {
        renderJson(crmActivityService.outworkSign(crmActivity));
    }

    /**
     * app外勤统计
     *
     * @param basePageRequest
     * @return
     */
    @Permissions("crm:outwork:read")
    public void queryOutworkStats(BasePageRequest basePageRequest) {
        renderJson(crmActivityService.queryOutworkStats(basePageRequest));
    }

    /**
     * app外勤详情
     */
    @Permissions("crm:outwork:read")
    public void queryOutworkList(BasePageRequest basePageRequest) {
        renderJson(crmActivityService.queryOutworkList(basePageRequest));
    }

    /**
     * app 查询签到照片上传设置
     */
    public void queryPictureSetting() {
        renderJson(crmActivityService.queryPictureSetting());
    }

    /**
     * app 设置签到照片上传
     */
    @Permissions("crm:outwork:setting")
    public void setPictureSetting(@Para("status") Integer status) {
        renderJson(crmActivityService.setPictureSetting(status));
    }

    /**
     * 删除外勤签到
     */
    @Permissions("crm:outwork:delete")
    public void deleteOutworkSign(@Para("activityId") Integer activityId) {
        renderJson(crmActivityService.deleteOutworkSign(activityId));
    }
}
