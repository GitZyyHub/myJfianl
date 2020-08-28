package com.kakarote.crm9.erp.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.common.annotation.RequestBody;
import com.kakarote.crm9.erp.oa.service.OaEventService;
import com.kakarote.crm9.utils.R;

public class OaEventController extends Controller {
    @Inject
    private OaEventService oaEventService;

    /**
     * 保存日程
     */
    public void save(@RequestBody JSONObject jsonObject){
        renderJson(oaEventService.save(jsonObject));
    }

    /**
     * 修改日程
     */
    public void update(@RequestBody JSONObject jsonObject){
        renderJson(oaEventService.update(jsonObject));
    }

    /**
     * 删除日程
     * type 1 只删除本次 2 删除此系列
     * @param jsonObject
     */
    public void delete(@RequestBody JSONObject jsonObject){
        renderJson(oaEventService.delete(jsonObject));
    }

    /**
     * 查询日程
     */
    public void queryList(@RequestBody JSONObject jsonObject){
        renderJson(R.ok().put("data",oaEventService.queryList(jsonObject)));
    }

    public void queryListStatus(@RequestBody JSONObject jsonObject){
        renderJson(oaEventService.queryListStatus(jsonObject));
    }


    public void queryById(@RequestBody JSONObject jsonObject){
        renderJson(oaEventService.queryById(jsonObject));
    }

}
