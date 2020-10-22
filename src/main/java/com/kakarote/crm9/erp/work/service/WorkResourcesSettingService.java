package com.kakarote.crm9.erp.work.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.WorkResourcesSetting;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author zyy
 */
public class WorkResourcesSettingService {

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;

    /**
     * 分页查询资源配置
     */
    public R queryPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv kv = Kv.by("_workId", jsonObject.get("workId"));
        kv.set("page",basePageRequest.getPage()).set("limit",basePageRequest.getLimit());
        if(jsonObject.get("search") !=null ){
            kv.set("search",jsonObject.get("search"));
        }
        SqlPara sqlPara = Db.getSqlPara("work.queryResourcePageList", kv);
        if(jsonObject.get("excel") != null){
            List<Record> records = Db.find(sqlPara);
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("list", records);
            return R.ok().put("data", resultJsonObject);
        }else {
            Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), sqlPara);
            return R.ok().put("data",paginate);
        }

    }

    /**
     * 设置项目资源配置
     * @param jsonObject
     * @return
     */
    public R setResources(JSONObject jsonObject) {
        WorkResourcesSetting entity = jsonObject.getObject("entity", WorkResourcesSetting.class);
        if(entity.getResourcesId() == null){
            entity.setCreateTime(new Date());
            entity.setCreateUserId(BaseUtil.getUserId());
            entity.save();
            workHistoricalRecordService.add( entity.getWorkId(),WorkRecordEnum.RESOURCE, BehaviorEnum.SAVE,  entity.getResourcesId(),null,null,  entity.getResourcesId());
        }else {
            Record first = Db.findFirst(Db.getSql("work.queryResourceById"), entity.getResourcesId());
            if(first==null){
                return R.error("项目资源配置不存在");
            }
            entity.update();
            Record after = Db.findFirst(Db.getSql("work.queryResourceById"), entity.getResourcesId());
            workHistoricalRecordService.add(entity.getWorkId(),WorkRecordEnum.RESOURCE, BehaviorEnum.UPDATE, entity.getResourcesId(),first,after,  entity.getResourcesId());
        }
        HashSet<Long> set = new HashSet<>();
        set.add(entity.getMainUserId());
        set.add(entity.getCreateUserId());
        WorkService.setWorkRelation(set,entity.getResourcesId(), WorkUserEnum.TASKCONFIG.getType(),entity.getWorkId());
        return R.ok();
    }

    /**
     * 删除项目资源配置
     * @param ids
     * @return
     */
    public R delResources(String ids){
        String[] split = ids.split(",");
        for (String s : split) {
            Db.delete("DELETE from 72crm_work_resources_setting WHERE resources_id=?",s);
        }
        return R.ok();
    }


}
