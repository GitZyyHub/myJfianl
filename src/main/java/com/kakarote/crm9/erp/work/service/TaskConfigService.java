package com.kakarote.crm9.erp.work.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;

import com.kakarote.crm9.erp.work.entity.Task;
import com.kakarote.crm9.erp.work.entity.WorkConfig;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.*;

/**
 * @author zyy
 */
public class TaskConfigService {

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;

    /**
     * 分页查询任务配置计划
     */
    public R queryPageList(BasePageRequest basePageRequest) {
        int page = basePageRequest.getPage();
        int limit = basePageRequest.getLimit();
        JSONObject jsonObject = basePageRequest.getJsonObject();
        String search = jsonObject.getString("search");
        Integer associatedId = jsonObject.getInteger("associatedId");
        String workId = jsonObject.getString("workId");
        Kv kv = Kv.by("associatedId", associatedId).set("search", search).set("type", "1").set("workId", workId);
        if (jsonObject.get("excel") != null) {
            List<Record> records = Db.find(Db.getSqlPara("work.queryTaskConfigByTaskId", kv));
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("list", records);
            return R.ok().put("data", resultJsonObject);
        } else {
            Page<Record> paginate = Db.paginate(page, limit, Db.getSqlPara("work.queryTaskConfigByTaskId", kv));
            return R.ok().put("data", paginate);
        }
    }


    /**
     * 设置任务配置计划
     *
     * @param
     * @return
     */
    @Before(Tx.class)
    public R setConfig(JSONObject jsonObject) {
        WorkConfig workConfig = jsonObject.getObject("entity", WorkConfig.class);
        workConfig.setType(1);
        boolean temp;
        Task byId = Aop.get(Task.class).findById(workConfig.getAssociatedId());
        if(byId !=null){
            workConfig.setAssociatedId(byId.getTaskId());
            workConfig.setAssociatedName(byId.getTaskName());
            workConfig.setAssociatedNum(byId.getTaskNum());
        }

        if (workConfig.getConfigId() == null) {
            workConfig.setCreateUserId(BaseUtil.getUserId());
            workConfig.setCreateTime(new Date());
            workConfig.setUpdateTime(new Date());
            temp = workConfig.save();
            workHistoricalRecordService.add(workConfig.getWorkId(), WorkRecordEnum.TASK_CONFIG, BehaviorEnum.SAVE, workConfig.getConfigId(), null,null, workConfig.getConfigId());

        } else {
            workConfig.setUpdateTime(new Date());
            List<Map<String, Object>> list = new ArrayList<>();
            Record first = Db.findFirst("SELECT config_items,DATE_FORMAT( plan_time, '%Y-%m-%d' ) plan_time," +
                    "(SELECT realname FROM 72crm_admin_user WHERE user_id = c.main_user_id) main_user_name," +
                    "(SELECT realname FROM 72crm_admin_user WHERE user_id = c.create_user_id) create_user_name," +
                    " associated_name,associated_num,version,DATE_FORMAT( version_time, '%Y-%m-%d' ) version_time,version_describe FROM 72crm_work_config c WHERE config_id=?", workConfig.getConfigId());

            temp = true;
            try {
                workConfig.update();
                Record update = Db.findFirst("SELECT config_items,DATE_FORMAT( plan_time, '%Y-%m-%d' ) plan_time," +
                        "(SELECT realname FROM 72crm_admin_user WHERE user_id = c.main_user_id) main_user_name," +
                        "(SELECT realname FROM 72crm_admin_user WHERE user_id = c.create_user_id) create_user_name," +
                        " associated_name,associated_num,version,DATE_FORMAT( version_time, '%Y-%m-%d' ) version_time,version_describe FROM 72crm_work_config c WHERE config_id=?", workConfig.getConfigId());
                //添加变更记录
                workHistoricalRecordService.add(workConfig.getWorkId(), WorkRecordEnum.TASK_CONFIG, BehaviorEnum.UPDATE, workConfig.getConfigId(), update,first, workConfig.getConfigId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WorkConfig wbyId = Aop.get(WorkConfig.class).dao().findById(workConfig.getConfigId());
        HashSet<Long> set = new HashSet<>();
        set.add(wbyId.getMainUserId());
        set.add(wbyId.getCreateUserId());
        WorkService.setWorkRelation(set, workConfig.getConfigId(), WorkUserEnum.TASKCONFIG.getType(), workConfig.getWorkId());
        return temp ? R.ok() : R.error("更新失败");
    }

    /**
     * 根据ID查询任务配置计划
     */
    public R queryById(Integer id) {
        return R.ok().put("data", Db.findFirst(Db.getSql("work.queryTaskConfigById"), id));
    }

    /**
     * 批量删除任务配置计划
     *
     * @param ids
     * @return
     */
    public R delConfig(@Para("ids") String ids) {
        String strings = Arrays.asList(ids.split(",")).toString();
        Db.delete("DELETE FROM 72crm_work_config WHERE config_id in (" + strings.substring(1, strings.length() - 1) + ")");
        return R.ok();
    }


}
