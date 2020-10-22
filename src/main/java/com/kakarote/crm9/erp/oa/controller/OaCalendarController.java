package com.kakarote.crm9.erp.oa.controller;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.annotation.RequestBody;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.crm.service.CrmBackLogService;
import com.kakarote.crm9.erp.oa.entity.OaCalendarType;
import com.kakarote.crm9.erp.oa.service.OaCalendarService;
import com.kakarote.crm9.utils.R;

/**
 * 日历模块接口
 * @author hmb
 */
public class OaCalendarController extends Controller {


    @Inject
    private OaCalendarService oaCalendarService;

    @Inject
    private CrmBackLogService crmBackLogService;


    /**
     * 添加/更新日历类型
     * @param oaCalendarType
     */
    public void addOrUpdate(@Para("") OaCalendarType oaCalendarType){
        renderJson(oaCalendarService.addOrUpdateType(oaCalendarType));
    }

    /**
     * 删除日历类型
     * @param typeId
     */
    public void delete(@Para("typeId") Integer typeId){
        renderJson(oaCalendarService.deleteType(typeId));
    }

    /**
     * 根据查询后台日历类型列表
     */
    public void queryTypeList(){
        renderJson(oaCalendarService.queryTypeList());
    }

    /**
     * 根据查询个人日历类型列表
     */
    public void queryTypeListByUser(@Para("userId") Long userId){
        renderJson(oaCalendarService.queryTypeListByUser(userId));
    }

    /**
     * 修改用户展示的日历类型
     */
    public void updateTypeUser(@RequestBody JSONObject jsonObject){
        renderJson(oaCalendarService.updateTypeUser(jsonObject));
    }

    /**
     * 日程任务
     */
    public void eventTask(@RequestBody JSONObject jsonObject){
        renderJson(R.ok().put("data",oaCalendarService.eventTask(jsonObject)));
    }

    /**
     * crm统计--前端展示日历使用
     */
    public void eventCrm(@RequestBody JSONObject jsonObject){
        renderJson(oaCalendarService.eventCrm(jsonObject));
    }

    /**
     * 日程客户列表
     * @param basePageRequest
     */
    public void eventCustomer(BasePageRequest basePageRequest){
        renderJson(oaCalendarService.eventCustomer(basePageRequest));
    }


    /**
     * type = 1 当日到期合同
     * type = 2 当日回款合同
     * @param basePageRequest
     */
    public void eventContract(BasePageRequest basePageRequest){
        renderJson(oaCalendarService.eventContract(basePageRequest));
    }


}
