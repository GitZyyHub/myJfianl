package com.kakarote.crm9.erp.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.crm.service.CrmReturnVisitService;
import com.kakarote.crm9.utils.R;

/**
 * @author wyq
 */
public class CrmReturnVisitController extends Controller {
    @Inject
    private CrmReturnVisitService crmReturnVisitService;

    @Permissions({"crm:visit:index"})
    public void queryPageList(BasePageRequest basePageRequest) {
        renderJson(crmReturnVisitService.queryPageList(basePageRequest, true));
    }

    @Permissions({"crm:visit:read"})
    public void queryById(@Para("visitId") Integer visitId) {
        renderJson(R.ok().put("data", crmReturnVisitService.queryById(visitId)));
    }

    @Permissions({"crm:visit:save", "crm:visit:update"})
    public void addOrUpdate() {
        JSONObject jsonObject = JSONObject.parseObject(getRawData());
        renderJson(crmReturnVisitService.addOrUpdate(jsonObject));
    }

    /**
     * @author wyq
     * 附件列表
     */
    public void queryFileList(@Para("visitId") Integer visitId) {
        renderJson(crmReturnVisitService.queryFileList(visitId));
    }

    /**
     * @author wyq
     * 根据id 删除客户回访
     */
    @Permissions({"crm:visit:delete"})
    @NotNullValidate(value = "visitIds", message = "客户回访id不能为空")
    public void deleteByIds(@Para("visitIds") String visitIds) {
        renderJson(crmReturnVisitService.deleteByIds(visitIds));
    }

    /**
     * 查询客户回访提醒设置
     */
    public void queryReturnVisitRemindConfig() {
        renderJson(crmReturnVisitService.queryReturnVisitRemindConfig());
    }

    /**
     * 修改客户回访提醒设置
     */
    public void updateReturnVisitRemindConfig(@Para("") AdminConfig adminConfig) {
        renderJson(crmReturnVisitService.updateReturnVisitRemindConfig(adminConfig));
    }
}
