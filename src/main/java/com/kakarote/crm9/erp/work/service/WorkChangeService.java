package com.kakarote.crm9.erp.work.service;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.WorkChange;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;
import com.kakarote.crm9.utils.ToolUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zyy
 */
public class WorkChangeService {


    @Inject
    WorkService workService;

    /**
     * 分页查询变更表
     *
     * @param basePageRequest
     * @return
     */
    public R queryPageList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv kv = Kv.by("_batchId", jsonObject.get("batch_id"));
        kv.set("_workId", jsonObject.get("workId"));
        if (jsonObject.get("search") != null)
            kv.set("_search", jsonObject.get("search"));

        if (jsonObject.get("excel") == null) {
            kv.set("page", basePageRequest.getPage()-1);
            kv.set("limit", basePageRequest.getLimit());
        }
        SqlPara sqlPara = Db.getSqlPara("work.queryChangePageList", kv);
        Integer integer = Db.template("work.queryChangePageListCount", kv).queryInt();
        List<Record> records = Db.find(sqlPara);
        records.forEach(record -> {
            List<Record> ownerUser = Db.find(Db.getSql("work.queryWorkUserByNumId"), record.getStr("change_id"), WorkUserEnum.TYPE_2.getType());
            record.set("owner_user", ownerUser);
            StringBuffer stringBuffer = new StringBuffer();
            for (Record user : ownerUser) {
                stringBuffer.append(user.getStr("realname") + ",");
            }
            record.set("owner_user_name", stringBuffer.toString());

            stringBuffer = new StringBuffer();
            List<Record> forwardUser = Db.find(Db.getSql("work.queryWorkUserByNumId"), record.getStr("change_id"), WorkUserEnum.TYPE_3.getType());
            record.set("forward_user", forwardUser);
            for (Record user : forwardUser) {
                stringBuffer.append(user.getStr("realname") + ",");
            }
            record.set("forward_user_name", stringBuffer.toString());

        });

        JSONObject resultJsonObject = new JSONObject();
        resultJsonObject.put("list", records);
        resultJsonObject.put("totalRow", integer);
        return R.ok().put("data", resultJsonObject);

    }

    /**
     * 根据ID查询变更表
     *
     * @param id
     * @return
     */
    public R queryById(Integer id) {
        Record record = Db.findById("72crm_work_change", "change_id", id);
        List<String> change_id = Db.query(Db.getSql("work.queryChangeUserByChangeId"), record.getInt("change_id"));
        record.set("owner_user_name", StringUtils.join(change_id, ","));
        return R.ok().put("data", record);
    }

    /**
     * 设置变更表
     *
     * @param jsonObject
     * @return
     */
    @Before(Tx.class)
    public R setWorkChange(JSONObject jsonObject) {
        WorkChange entity = jsonObject.getObject("entity", WorkChange.class);

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
        Set<Long> ownerUserId = null;
        if (!com.mysql.cj.util.StringUtils.isNullOrEmpty(entity.getOwnerUserId())) {
            ownerUserId = TagUtil.toLongSet(entity.getOwnerUserId());
            entity.setOwnerUserId(TagUtil.fromLongSet(ownerUserId));
        }
        Set<Long> forwardUserId = null;
        if (!com.mysql.cj.util.StringUtils.isNullOrEmpty(entity.getForwardUser())) {
            forwardUserId = TagUtil.toLongSet(entity.getForwardUser());
            entity.setForwardUser(TagUtil.fromLongSet(forwardUserId));
        }

        if (entity.getChangeId() == null) {
            /*if (StrUtil.isEmpty(entity.getChangeNum())) {
                AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 27");
                String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                entity.setChangeNum(result);
            }*/
            entity.setCreateTime(new Date());
            entity.setBatchId(IdUtil.simpleUUID());
            entity.setUpdateTime(new Date());
            entity.save();
        } else {
            Db.deleteById("72crm_work_change_user", "change_id", entity.getChangeId());
            entity.setUpdateTime(new Date());

            entity.update();
        }
        if (!ownerUserId.isEmpty()) {
            workService.setWorkRelation(ownerUserId, entity.getChangeId(), WorkUserEnum.TYPE_2.getType(), entity.getWorkId());
        }
        if (!forwardUserId.isEmpty()) {
            workService.setWorkRelation(forwardUserId, entity.getChangeId(), WorkUserEnum.TYPE_3.getType(), entity.getWorkId());
        }
        return R.ok();
    }

    /**
     * 删除
     */
    public R delWorkChange(String ids) {
        String[] split = ids.split(",");
        Db.tx(() -> {
            for (String s : split) {
                if (!Aop.get(WorkChange.class).dao().deleteById(s)) return false;
                Db.delete("DELETE FROM 72crm_work_change_user WHERE change_id = ?", s);
            }
            return true;
        });
        return R.ok();
    }


}
