package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.WorkRisk;
import com.kakarote.crm9.utils.R;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author zyy
 */
public class WorkRiskService {

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;

    /**
     * 分页查询风险
     *
     * @param
     */
    public R queryPageList(BasePageRequest<WorkRisk> basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv by = Kv.by("workId", jsonObject.get("workId"));
        if (jsonObject.get("search") != null) {
            by.put("search", jsonObject.get("search"));
        }
        List<Record> lists;
        Page<Record> recordPage = null;

        SqlPara sqlPara = Db.getSqlPara("work.queryRiskByBatchId", by);;
        if(jsonObject.get("excel") != null){
            lists = Db.find(sqlPara);
        }else {
            recordPage = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), sqlPara);
            lists = recordPage.getList();
        }
        List<Record> trackingUser = null;
        List<Record> solveUsers = null;
        List<Record> foundUsers = null;
        for (Record list : lists) {

            List<String> trackingUsers = Arrays.asList(list.getStr("tracking_user_id").split(","));
            List<String> solveUser = Arrays.asList(list.getStr("solve_user_id").split(","));
            List<String> foundUser = Arrays.asList(list.getStr("found_user_id").split(","));
            trackingUser = Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", trackingUsers)));
            solveUsers = Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", solveUser)));
            foundUsers = Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", foundUser)));
            list.set("trackingUser", trackingUser);
            list.set("solveUser", solveUsers);
            list.set("foundUser", foundUsers);

            StringBuffer stringBuffer = new StringBuffer();
            for (Record record : trackingUser) {
                stringBuffer.append(record.getStr("realname")+",");
            }
            list.set("tracking_user", stringBuffer.toString());

            stringBuffer= new StringBuffer();
            for (Record record : solveUsers) {
                stringBuffer.append(record.getStr("realname")+",");
            }
            list.set("solve_user",stringBuffer.toString());

            stringBuffer= new StringBuffer();
            for (Record record : foundUsers) {
                stringBuffer.append(record.getStr("realname")+",");
            }
            list.set("found_user",stringBuffer.toString());
        }
        if (jsonObject.get("excel") != null) {
            JSONObject resultJsonObject = new JSONObject();
            resultJsonObject.put("list", lists);
            return R.ok().put("data", resultJsonObject);
        } else {
            return R.ok().put("data", recordPage);
        }
    }

    /**
     * 根据ID查询风险
     */
    public R queryById(Integer id) {
        Record first = Db.findFirst(Db.getSql("work.queryRiskById"), id);

        List<String> trackingUsers = Arrays.asList(first.getStr("tracking_user_id").split(","));
        List<String> solveUser = Arrays.asList(first.getStr("solve_user_id").split(","));
        List<String> foundUser = Arrays.asList(first.getStr("found_user_id").split(","));

        first.set("trackingUser", Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", trackingUsers))));
        first.set("solveUser", Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", solveUser))));
        first.set("foundUser", Db.find(Db.getSqlPara("admin.user.queryUserIdAndRealNameByIds", Kv.by("ids", foundUser))));
        return R.ok().put("data", first);
    }

    /**
     * 添加修改风险
     *
     * @param jsonObject
     * @return
     */
    @Before(Tx.class)
    public R setRisk(JSONObject jsonObject) {
        WorkRisk entity = jsonObject.getObject("entity", WorkRisk.class);
        if (entity.getRiskId() == null) {
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.save();
            workHistoricalRecordService.add(entity.getWorkId(), WorkRecordEnum.RISK, BehaviorEnum.SAVE, entity.getRiskId(), null, null,entity.getRiskId());

        } else {
            entity.setUpdateTime(new Date());
            Record first = Db.findFirst("select * from 72crm_work_risk where risk_id =?", entity.getRiskId());
            entity.update();
            Record update = Db.findFirst("select * from 72crm_work_risk where risk_id =?", entity.getRiskId());
            workHistoricalRecordService.add(entity.getWorkId(), WorkRecordEnum.RISK, BehaviorEnum.UPDATE, entity.getRiskId(),  update,first,entity.getRiskId());
        }
        List<String> founds = Arrays.asList(entity.getFoundUserId().split(","));
        List<String> trachings = Arrays.asList(entity.getTrackingUserId().split(","));
        HashSet<Long> set = new HashSet<>();
        if(!StrUtil.isEmpty(entity.getSolveUserId())){
            List<String> solves = Arrays.asList(entity.getSolveUserId().split(","));
            solves.forEach(found->set.add(Long.parseLong(found)));
        }
        founds.forEach(found->set.add(Long.parseLong(found)));
        trachings.forEach(found->set.add(Long.parseLong(found)));
        WorkService.setWorkRelation(set,entity.getRiskId(), WorkUserEnum.HOURS.getType(),entity.getWorkId());
        return R.ok();
    }

    /**
     * 删除风险
     *
     * @param ids
     * @return
     */
    public R delRisk(String ids) {
        String[] split = ids.split(",");
        if (split.length != 0) {
            for (String s : split)
                Db.deleteById("72crm_work_risk", "risk_id", s);
        }
        return R.ok();
    }


}
