package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;

import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.common.WorkPermissions;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.Milestone;
import com.kakarote.crm9.erp.work.entity.WorkHistoricalRecord;
import com.kakarote.crm9.utils.AbstractBasePageList;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.ToolUtils;
import com.mysql.jdbc.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zyy
 */
public class MilestoneService extends AbstractBasePageList {

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;


    /**
     * 查询里程碑
     *
     * @param basePageRequest
     * @param isDataAuth
     * @return
     */
    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        CrmEnum milestone = CrmEnum.MILESTONE;
        Kv kv = getKv(basePageRequest, milestone, isDataAuth);
        Object workId = basePageRequest.getJsonObject().get("workId");
        kv.set("_workId", workId);
        List<Record> records = Db.find(Db.getSqlPara("work.findMilestoneById", kv));
        Integer integer = Db.template("work.queryMilestonePageListCount", kv).queryInt();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", records);
        jsonObject.put("totalRow", integer);
        return R.ok().put("data", jsonObject);
    }

    /**
     * 删除里程碑
     *
     * @param
     * @return
     */
    public R delMilestone(String milestoneIds) {
        String[] split = milestoneIds.split(",");
        for (String id : split) {
            Integer integer = Db.queryInt("SELECT COUNT(1) FROM 72crm_task WHERE milestone_id =?", id);
            if (integer != 0) {
                return R.error("里程碑下存在任务");
            }
            Milestone byId = Aop.get(Milestone.class).dao().findById(id);
            boolean milestone_id = Db.deleteById("72crm_milestone", "milestone_id", id);
        }

        return R.ok();
    }

    /**
     * 查询里程碑名
     *
     * @param
     * @return
     */
    public R queryMilestoneIdAndName() {
        /*Integer integer = Db.queryInt("SELECT count(1) FROM `72crm_milestone` ");
        if(integer == 0 ){
            return R.error("里程碑已删除");
        }*/
        List<Record> records = Db.find("SELECT milestone_id,milestone_name FROM `72crm_milestone`");
        return R.ok().put("data", records);
    }

    /**
     * 设置里程碑
     *
     * @param jsonObject
     * @return
     */
    @Before(Tx.class)
    public R setMilestone(JSONObject jsonObject) {
        //变更后数据
        JSONObject data = jsonObject.getJSONObject("entity");
        Milestone entity = jsonObject.getObject("entity", Milestone.class);
        Long userId = BaseUtil.getUserId();

        Date startTime = entity.getStartTime();
        Date endTime = entity.getEndTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        if (startTime != null && endTime != null) {
            if(!startTime.before(endTime)){
                return R.error("结束时间应大于开始时间");
            }else if((startTime.getTime() - endTime.getTime()) / (24 * 60 * 60 * 1000) > 30){
                return R.error("开始时间到结束时间不能超过30天");
            }
        }

        /*if (startTime != null && endTime != null && !startTime.before(endTime) && fmt.format(startTime).equals(fmt.format(endTime))) {
            return R.error("结束时间应大于开始时间");
        }*/
        boolean setting;
        if (entity.getMilestoneId() == null) {
            if (StrUtil.isEmpty(entity.getMilestoneNum())) {
                AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 20");
                String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                entity.setMilestoneNum(result);
            }
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setCreateUserId(BaseUtil.getUserId());
            setting = entity.save();
            workHistoricalRecordService.add( entity.getWorkId(),WorkRecordEnum.MILESTONE, BehaviorEnum.SAVE, entity.getMilestoneNum(),null,null, entity.getMilestoneId());
        } else {
            setting = true;
            //变更前数据
            String tableName = CrmEnum.MILESTONE.getTableName();
            String tableId = CrmEnum.MILESTONE.getTableId();
            Record first = Db.findFirst("SELECT milestone_name,milestone_num,work_id,degree,DATE_FORMAT( start_time, '%Y-%m-%d %H:%i:%S' ) start_time, DATE_FORMAT( end_time, '%Y-%m-%d %H:%i:%S' ) end_time,(select task_name from 72crm_task where task_id=m.task_id) task_id,instructions,(select realname from 72crm_admin_user where user_id=m.main_user_id) main_user_id FROM 72crm_milestone m WHERE milestone_id=?", entity.getMilestoneId());
            if (first == null) {
                return R.error("里程碑不存在");
            }
            entity.setUpdateTime(new Date());
            entity.update();
            Record after = Db.findFirst("SELECT milestone_name,milestone_num,work_id,degree,DATE_FORMAT( start_time, '%Y-%m-%d %H:%i:%S' ) start_time, DATE_FORMAT( end_time, '%Y-%m-%d %H:%i:%S' ) end_time,(select task_name from 72crm_task where task_id=m.task_id) task_id,instructions,(select realname from 72crm_admin_user where user_id=m.main_user_id) main_user_id FROM 72crm_milestone m WHERE milestone_id=?", entity.getMilestoneId());
            //添加变更记录
            workHistoricalRecordService.add( entity.getWorkId(),WorkRecordEnum.MILESTONE, BehaviorEnum.UPDATE, entity.getMilestoneNum(),after,first,entity.getMilestoneId());
        }
        HashSet<Long> set = new HashSet<>();
        set.add(entity.getMainUserId());
        WorkService.setWorkRelation(set, entity.getMilestoneId(), WorkUserEnum.MILESTONE.getType(),entity.getWorkId());
        return setting ? R.ok() : R.error();
    }

    public R updateDegree(String ids, String degree) {

        Db.update(Db.getSqlPara("work.updateMilestoneDegree", Kv.by("ids",ids).set("degree",degree)));
        return R.ok();
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public Record queryById(Integer id) {
        Record first = Db.findFirst(Db.getSql("work.queryMilestoneById"), id);
        return first;
    }

    /**
     * 普通搜索
     *
     * @param search
     * @return
     */
    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (milestone_name like '%{search}%' or milestone_num like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }



}
