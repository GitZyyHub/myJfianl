package com.kakarote.crm9.erp.statistics.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.statistics.common.StatisticsFieldEnum;
import com.kakarote.crm9.erp.statistics.common.StatisticsTypeUtils;
import com.kakarote.crm9.erp.statistics.entity.WorkParams;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.service.WorkService;
import com.kakarote.crm9.utils.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zyy
 */
public class RiskStatisticsService {

    @Inject
    StatisticsTypeUtils statisticsTypeUtils;

    @Inject
    WorkService workService;

    public R riskPercentage(WorkParams workParams) {
        String deptId = workParams.getDeptId();
        String area = workParams.getArea();
        DecimalFormat df = new DecimalFormat("0%");
        if(!StrUtil.isEmpty(deptId) || !StrUtil.isEmpty(area)){
            Kv kv = new Kv();
            if(!StrUtil.isEmpty(deptId)){
                kv.set("isDept",2).set("dept",deptId);
            }else {
                kv.set("isDept",1).set("dept",area);
            }
            List<Record> workIds = Db.find(Db.getSqlPara("statistics.queryWorkIdByDeptId2", kv));
            List<Record> list = new ArrayList<>();
            for (Record workId : workIds) {
                Record record = new Record();
                String risknum = Db.template("statistics.riskNum",Kv.by("workId",workId.get("work_id")).set("userId",workId.get("manager_id"))).queryStr();
                String complete = Db.template("statistics.riskNum",Kv.by("workId",workId.get("work_id")).set("userId",workId.get("manager_id")).set("isComplete",1)).queryStr();
                //相除
                if("0".equals(risknum)||"0".equals(complete)){
                    record.set("name", Aop.get(Work.class).findById(workId.get("work_id")).getWorkName());
                    record.set("rate",0);
                    list.add(record);
                    continue;
                }
                double rate = workService.divide(Double.parseDouble(complete), Double.parseDouble(risknum), 2);
                record.set("name", Aop.get(Work.class).findById(workId.get("work_id")).getWorkName());
                String format = df.format(rate);
                record.set("rate",format.substring(0, format.length() - 1));
                list.add(record);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list",list);
            return R.ok().put("data",jsonObject);
        }else {
            return R.ok().put("data",new JSONObject());
        }
    }

    public R riskState(WorkParams workParams) {
        if (workParams.getTypeId() == StatisticsFieldEnum.WORKTYPE.getType()) {
            return riskWorkType(workParams);
        }else if (workParams.getTypeId() == StatisticsFieldEnum.RISKLEVEL.getType()) {
            return riskLevel(workParams);
        }
        return R.error("查询失败");
    }


    public R riskWorkType(WorkParams workParams){
        Record workphaseStatisticsField = statisticsTypeUtils.getWorkphaseStatisticsField(workParams);
        List<String> search = workphaseStatisticsField.get("search");
        List<Record> records = new ArrayList<>();
        for (String s : search) {
            List<Integer> works = Db.template("statistics.queryWorkState", Kv.by("x", s)).query();
            Record record = new Record();
            record.set("name",s);
            record.set("complete",Db.template("statistics.riskWorkType", Kv.by("state", 0).set("works",works)).queryInt());
            record.set("unfinished",Db.template("statistics.riskWorkType", Kv.by("state", 1).set("works",works)).queryInt());
            records.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        return R.ok().put("data",jsonObject);
    }

    private R riskLevel(WorkParams workParams) {
        Record riskLevelStatisticsField = statisticsTypeUtils.getRiskLevelStatisticsField(workParams);
        List<String> search = riskLevelStatisticsField.get("search");
        List<Record> records = new ArrayList<>();
        for (String s : search) {
            Record record = new Record();
            List<Object> query = Db.query("select work_id from 72crm_work where 1=1 and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')");
            if(query.size()==0){
                record.set("name",s);
                record.set("complete",0);
                record.set("unfinished",0);
            }else {
                record.set("name",s);
                record.set("complete",Db.template("statistics.riskLevel", Kv.by("state", 0).set("search",s).set("workIds",query)).queryInt());
                record.set("unfinished",Db.template("statistics.riskLevel", Kv.by("state", 1).set("search",s).set("workIds",query)).queryInt());
            }
            records.add(record);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        return R.ok().put("data",jsonObject);
    }
}
