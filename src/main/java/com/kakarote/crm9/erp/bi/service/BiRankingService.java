package com.kakarote.crm9.erp.bi.service;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.List;

public class BiRankingService {

    @Inject
    BiTimeUtil biTimeUtil;

    public List<Record> contractRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return Db.find(Db.getSqlPara("bi.ranking.contractRanKing", record.getColumns()));
    }

    public List<Record> receivablesRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return Db.find(Db.getSqlPara("bi.ranking.receivablesRanKing", record.getColumns()));
    }

    public List<Record> contractCountRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return Db.find(Db.getSqlPara("bi.ranking.contractCountRanKing", record.getColumns()));
    }

    public R productCountRanKing(BiParams biParams){
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
       list = Db.find(Db.getSqlPara("bi.ranking.productCountRanKing",
                record.getColumns()));
        return R.ok().put("data",list);
    }

    public List<Record> customerCountRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return Db.find(Db.getSqlPara("bi.ranking.customerCountRanKing", record.getColumns()));
    }

    public List<Record> contactsCountRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return Db.find(Db.getSqlPara("bi.ranking.contactsCountRanKing", record.getColumns()));
    }
    public R customerGenjinCountRanKing(BiParams biParams){
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        list = Db.find(Db.getSqlPara("bi.ranking.customerGenjinCountRanKing",
                record.getColumns()));
        return R.ok().put("data",list);
    }

    public List<Record> recordCountRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return Db.find(Db.getSqlPara("bi.ranking.recordCountRanKing", record.getColumns()));
    }
    public R contractProductRanKing(BiParams biParams){
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        String[] userIdsArr = userIds.split(",");
        Integer status = biTimeUtil.analyzeType(biParams.getType());
        list = Db.find(Db.getSqlPara("bi.ranking.contractProductRanKing",
                Kv.by("userIds",userIdsArr).set("type",status).set("startTime",biParams.getStartTime()).set("endTime",biParams.getEndTime())));
        return R.ok().put("data",list);
    }
    public R travelCountRanKing(BiParams biParams){
        biParams.setIsUser(0);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        list = Db.find(Db.getSqlPara("bi.ranking.travelCountRanKing", record.getColumns()));
        return R.ok().put("data",list);
    }
    public R productSellRanKing(BiParams biParams){
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        String[] userIdsArr = userIds.split(",");
        Integer status = biTimeUtil.analyzeType(biParams.getType());
        list = Db.find(Db.getSqlPara("bi.ranking.productSellRanKing",
                Kv.by("userIds",userIdsArr).set("type",status).set("startTime",biParams.getStartTime()).set("endTime",biParams.getEndTime())));
        return R.ok().put("data",list);
    }

    public List<Record> businessRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return Db.find(Db.getSqlPara("bi.ranking.businessRanKing", record.getColumns()));
    }

    public List<Record> businessMoneyRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return Db.find(Db.getSqlPara("bi.ranking.businessMoneyRanKing", record.getColumns()));
    }

    public R addressAnalyse(BiParams biParams){
        Record record = biTimeUtil.analyzeType(biParams);
        String [] addressArr = biTimeUtil.getAddress();
        List<Record> list = Db.template("bi.ranking.addressAnalyse", record.set("address", addressArr).getColumns()).find();
        return R.ok().put("data",list);
    }
    public R portrait(BiParams biParams){
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        String[] userIdsArr = userIds.split(",");
        Integer status = biTimeUtil.analyzeType(biParams.getType());
        list = Db.find(Db.getSqlPara("bi.ranking.portrait",
                Kv.by("userIds", userIdsArr).set("type", status).set("startTime", biParams.getStartTime()).set("endTime", biParams.getEndTime())));
        return R.ok().put("data",list);
    }
    public R portraitLevel(BiParams biParams){
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        String[] userIdsArr = userIds.split(",");
        Integer status = biTimeUtil.analyzeType(biParams.getType());
        list = Db.find(Db.getSqlPara("bi.ranking.portraitLevel",
                Kv.by("userIds", userIdsArr).set("type", status).set("startTime", biParams.getStartTime()).set("endTime", biParams.getEndTime())));
        return R.ok().put("data",list);
    }
    public R portraitSource(BiParams biParams){
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> list = new ArrayList<>();
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",list);
        }
        String[] userIdsArr = userIds.split(",");
        Integer status = biTimeUtil.analyzeType(biParams.getType());
        list = Db.find(Db.getSqlPara("bi.ranking.portraitSource",
                Kv.by("userIds", userIdsArr).set("type", status).set("startTime", biParams.getStartTime()).set("endTime", biParams.getEndTime())));
        return R.ok().put("data",list);
    }
}
