package com.kakarote.crm9.erp.bi.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.crm.service.CrmBusinessService;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.List;

public class BiFunnelService {
    @Inject
    BiTimeUtil biTimeUtil;

    /**
     * 销售漏斗
     * @author zxy
     */
    public R sellFunnel(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "business", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        List<Record> list = new ArrayList<>();
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        String[] userIdss = userIds.split(",");
        Integer status = biTimeUtil.analyzeType(biParams.getType());
        list = Db.find(Db.getSqlPara("bi.funnel.sellFunnel",
                Kv.by("userIds",userIdss).set("type",status).set("startTime",biParams.getStartTime()).
                        set("endTime",biParams.getEndTime()).set("typeId",biParams.getTypeId())));
        return R.ok().put("data",list);
    }

    public R businessNum(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Record record = biTimeUtil.analyzeType(biParams);
        List<Integer> businessIdList = Db.template("bi.funnel.businessNum", record.set("typeId", biParams.getTypeId()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "business_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(businessIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmBusinessService.class).queryPageList(basePageRequest, true);
    }

    /**
     * 新增商机分析
     * @author zxy
     */
    public R addBusinessAnalyze(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "business", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getInt("cycleNum");
        String userIds = record.getStr("userIds");
        Integer beginTime = record.getInt("beginTime");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.template("bi.funnel.addBusinessAnalyze", record.set("timeList", timeList).getColumns()).find();
        return R.ok().put("data",recordList);
    }

    public R sellFunnelList(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "business", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        String[] userIdss = userIds.split(",");
        Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("bi.funnel.sellFunnelList", Kv.by("userIds", userIdss).set("time", biParams.getType()).set("sqlDateFormat", record.getStr("sqlDateFormat"))));
        return R.ok().put("data", page);
    }
    /**
     * 商机转化率分析
     * @author zxy
     */
    public R win(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "business", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getInt("cycleNum");
        String userIds = record.getStr("userIds");
        Integer beginTime = record.getInt("beginTime");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.template("bi.funnel.win", record.set("timeList", timeList).getColumns()).find();
        return R.ok().put("data",recordList);
    }
}
