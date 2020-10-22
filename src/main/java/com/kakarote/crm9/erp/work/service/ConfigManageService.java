package com.kakarote.crm9.erp.work.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;

import com.kakarote.crm9.erp.work.entity.Task;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.entity.WorkConfig;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.*;

/**
 * @author zyy
 */
public class ConfigManageService {

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;

    /**
     * 分页查询任务配置计划
     */
    public R queryPageList(BasePageRequest basePageRequest){
        int page = basePageRequest.getPage();
        int limit = basePageRequest.getLimit();
        JSONObject jsonObject = basePageRequest.getJsonObject();
        String search = jsonObject.getString("search");
        Integer associatedId = jsonObject.getInteger("associatedId");
        Kv kv = Kv.by("associatedId", associatedId).set("search", search).set("type","2");
        if(jsonObject.get("excel") != null){
            List<Record> records = Db.find(Db.getSqlPara("work.queryTaskConfigByTaskId", kv));
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("list", records);
            return R.ok().put("data", resultJsonObject);
        }else {
            return R.ok().put("data",Db.paginate(page, limit, Db.getSqlPara("work.queryTaskConfigByTaskId", kv)));
        }

    }


    /**
     * 设置配置管理
     * @param
     * @return
     */
    public R setConfig( JSONObject jsonObject) {
        WorkConfig workConfig = jsonObject.getObject("entity", WorkConfig.class);
        workConfig.setType(2);
        boolean temp;
        Work byId = Aop.get(Work.class).findById(workConfig.getAssociatedId());
        workConfig.setAssociatedId(byId.getWorkId());
        workConfig.setAssociatedName(byId.getWorkName());
        workConfig.setAssociatedNum(byId.getWorkNum());
        if (workConfig.getConfigId() == null) {
            workConfig.setCreateUserId(BaseUtil.getUserId());
            workConfig.setCreateTime(new Date());
            workConfig.setUpdateTime(new Date());
            temp = workConfig.save();
        } else {
            workConfig.setUpdateTime(new Date());
            List<Map<String, Object>> list = new ArrayList<>();
            Record first = Db.findFirst(Db.getSql("work.queryTaskConfigById"), workConfig.getConfigId());
            temp=true;
            try {
                workConfig.update();
                Record update = Db.findFirst(Db.getSql("work.queryTaskConfigById"), workConfig.getConfigId());
                //添加变更记录
                workHistoricalRecordService.add(workConfig.getWorkId(),WorkRecordEnum.WORK_CONFIG, BehaviorEnum.UPDATE, workConfig.getConfigId(), first, update,workConfig.getConfigId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HashSet<Long> set = new HashSet<>();
        set.add(workConfig.getMainUserId());
        set.add(workConfig.getCreateUserId());
        WorkService.setWorkRelation(set,workConfig.getConfigId(), WorkUserEnum.CONFIGURATION.getType(),workConfig.getWorkId());
        return temp?R.ok():R.error("更新失败");
    }

    /**
     * 根据ID查询任务配置计划
     */
    public R queryById(Integer id){
        return R.ok().put("data",Db.findFirst(Db.getSql("work.queryTaskConfigById"),id));
    }

    /**
     * 批量删除任务配置计划
     * @param ids
     * @return
     */
    public R delConfig(@Para("ids") String ids){
        String[] split = ids.split(",");
        Db.delete("DELETE FROM 72crm_work_config WHERE config_id in ("+split.toString()+")");
        return R.ok();
    }
}
