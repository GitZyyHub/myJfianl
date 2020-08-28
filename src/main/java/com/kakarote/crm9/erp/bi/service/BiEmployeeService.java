package com.kakarote.crm9.erp.bi.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbTemplate;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.crm.service.CrmContractService;
import com.kakarote.crm9.erp.crm.service.CrmReceivablesService;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wyq
 */
public class BiEmployeeService {
    @Inject
    BiTimeUtil biTimeUtil;

    /**
     * 业绩分析
     * @author wyq
     */
    public R contractNumStats(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "contract", "read");
        biParams.setMenuId(menuId);
        String type = biParams.getType();
        Integer year = biParams.getYear();
        Record record = biTimeUtil.analyzeType(biParams);
        Integer cycleNum = 12;
        Integer beginTime = year * 100 + 1;
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        record.set("timeList",timeList);
        DbTemplate template;
        if ("contractNum".equals(type)){
            template = Db.template("bi.employee.contractNum",record.getColumns());
        }else if ("contractMoney".equals(type)){
            template = Db.template("bi.employee.contractMoney",record.getColumns());
        }else if ("receivables".equals(type)){
            template = Db.template("bi.employee.receivables",record.getColumns());
        }else {
            return R.error("type不符合要求");
        }
        List<Record> recordList = template.find();
        recordList.forEach(r -> {
            BigDecimal thisMonth = new BigDecimal(r.getStr("thisMonth"));
            BigDecimal lastMonth = new BigDecimal(r.getStr("lastMonth"));
            BigDecimal lastYear = new BigDecimal(r.getStr("lastYear"));
            r.set("lastMonthGrowth",thisMonth.compareTo(new BigDecimal(0)) != 0 && lastMonth.compareTo(new BigDecimal(0)) != 0 ? thisMonth.multiply(new BigDecimal(100)).divide(lastMonth,2,BigDecimal.ROUND_HALF_UP) : 0);
            r.set("lastYearGrowth",thisMonth.compareTo(new BigDecimal(0)) != 0 && lastYear.compareTo(new BigDecimal(0)) != 0 ?thisMonth.multiply(new BigDecimal(100)).divide(lastYear,2,BigDecimal.ROUND_HALF_UP) : 0);
            r.remove("lastMonth");
            r.remove("lastYear");
        });
        return R.ok().put("data",recordList);
    }

    public R contractByMonth(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "contract", "read");
        biParams.setMenuId(menuId);
        Record record = new Record().set("userIds", biTimeUtil.analyzeAuth(biParams));
        List<Integer> contractIdList = Db.template("bi.employee.contractByMonth", record.set("month", biParams.getMonth()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "contract_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(contractIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmContractService.class).queryPageList(basePageRequest, true);
    }

    public R receivablesByMonth(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "contract", "read");
        biParams.setMenuId(menuId);
        Record record = new Record().set("userIds", biTimeUtil.analyzeAuth(biParams));
        List<Integer> receivablesIdList = Db.template("bi.employee.receivablesByMonth", record.set("month", biParams.getMonth()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "receivables_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(receivablesIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmReceivablesService.class).queryPageList(basePageRequest, true);
    }

    /**
     * 合同汇总表
     * @author wyq
     */
    public R totalContract(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "contract", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String sqlDateFormat = record.getStr("sqlDateFormat");
        String userIds = record.getStr("userIds");
        Integer beginTime = record.getInt("beginTime");
        Integer finalTime = record.getInt("finalTime");
        Integer cycleNum = record.getInt("cycleNum");
        Record total = Db.findFirst(Db.getSqlPara("bi.employee.totalContract", Kv.by("sqlDateFormat", sqlDateFormat).set("beginTime", beginTime).set("finalTime", finalTime).set("userIds", TagUtil.toLongSet(userIds))));
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.template("bi.employee.totalContractTable",record.set("timeList",timeList).getColumns()).find();
        return R.ok().put("data",total.set("list",recordList));
    }

    /**
     * 合同关联的回款
     */
    public R receivablesByContract(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "contract", "read");
        biParams.setMenuId(menuId);
        Record record = new Record().set("userIds", biTimeUtil.analyzeAuth(biParams));
        List<Integer> receivablesIdList = Db.template("bi.employee.receivablesByContract", record.set("month", biParams.getMonth()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "receivables_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(receivablesIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmReceivablesService.class).queryPageList(basePageRequest, true);
    }

}
