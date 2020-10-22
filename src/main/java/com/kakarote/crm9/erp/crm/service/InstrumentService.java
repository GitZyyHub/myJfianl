package com.kakarote.crm9.erp.crm.service;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.erp.bi.common.MonthEnum;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.erp.bi.service.BiRankingService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmInstrumentSort;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;

import java.math.BigDecimal;
import java.util.*;

public class InstrumentService{
    @Inject
    private BiTimeUtil biTimeUtil;

    @Inject
    private AdminFileService adminFileService;

    @Inject
    private CrmActivityService crmActivityService;

    @Inject
    private CrmCustomerService crmCustomerService;

    @Inject
    private CrmContactsService crmContactsService;

    @Inject
    private CrmBusinessService crmBusinessService;

    @Inject
    private CrmContractService crmContractService;

    @Inject
    private CrmReceivablesService crmReceivablesService;


    /**
     * 销售简报
     * @author zhangzhiwei
     */
    public R queryBulletin(String type, String userIds, String startTime, String endTime){
        BiParams biParams = new BiParams();
        biParams.setType(type);
        biParams.setStartTime(startTime);
        biParams.setEndTime(endTime);
        Record record = biTimeUtil.analyzeTime(biParams);
        record.set("userIds",userIds);
        Record info = Db.findFirst(Db.getSqlPara("crm.Instrument.queryBulletin", record.getColumns()));
        boolean isCustom=!StrUtil.isAllEmpty(startTime,endTime);
        Record prevRecord=new Record();
        BigDecimal bigDecimal=new BigDecimal("0");
        if(!isCustom){
            Record newRecord = new Record().setColumns(record.getColumns()).set("type", type);
            biTimeUtil.prevAnalyzeType(newRecord);
            newRecord.set("userIds",userIds);
            prevRecord=Db.findFirst(Db.getSqlPara("crm.Instrument.queryBulletin",newRecord.getColumns()));
            for (String columnName : info.getColumnNames()) {
                BigDecimal decimal=new BigDecimal(prevRecord.getLong(columnName));
                BigDecimal newdecimal= BigDecimal.valueOf(info.getLong(columnName));
                if(decimal.equals(bigDecimal) || newdecimal.equals(bigDecimal)){
                    prevRecord.set(columnName,bigDecimal);
                }else {
                    prevRecord.set(columnName,newdecimal.subtract(decimal).divide(decimal,4,BigDecimal.ROUND_HALF_UP));
                }
            }
        }else {
            for (String columnName : info.getColumnNames()) {
                prevRecord.set(columnName,bigDecimal);
            }
        }
        return R.ok().put("data", info).put("prev",prevRecord);
    }

    /**
     * 数据汇总
     * @author zhangzhiwei
     */
    public R queryDataInfo(String type, String userIds, String startTime, String endTime){
        BiParams biParams = new BiParams();
        biParams.setType(type);
        biParams.setStartTime(startTime);
        biParams.setEndTime(endTime);
        Record record = biTimeUtil.analyzeTime(biParams);
        record.set("userIds",userIds);
        Record costumer=Db.template("crm.Instrument.dataInfoCostumer",record.getColumns()).findFirst();
        Record activity=Db.template("crm.Instrument.dataInfoActivity",record.getColumns()).findFirst();
        Record business=Db.template("crm.Instrument.dataInfoBusiness",record.getColumns()).findFirst();
        Record contract=Db.template("crm.Instrument.dataInfoContract",record.getColumns()).findFirst();
        Record receivables=Db.template("crm.Instrument.dataInfoReceivables",record.getColumns()).findFirst();
        return R.ok().put("data",costumer.setColumns(activity).setColumns(business).setColumns(contract).setColumns(receivables));

    }

    /**
     * @author zhangzhiwei
     * 销售简报的数据查看详情
     */
    public R queryBulletinInfo(BasePageRequest basePageRequest, String userIds, String type, Integer label, Integer checkStatus, Integer moneyType, String startTime, String endTime) {
        BiParams biParams = new BiParams();
        biParams.setType(type);
        biParams.setStartTime(startTime);
        biParams.setEndTime(endTime);
        Record record = biTimeUtil.analyzeTime(biParams);
        JSONObject jsonObject=new JSONObject();
        if (StrUtil.isEmpty(userIds)) {
            userIds = "0";
        }
        jsonObject.put("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("value", userIds).fluentPut("condition", "in"));
        if (moneyType != null) {
            if (moneyType == 1) {
                jsonObject.put("order_date", new JSONObject().fluentPut("name", "order_date").fluentPut("formType", "date").fluentPut("start", DateUtil.formatDate(record.getDate("beginDate"))).fluentPut("end", DateUtil.formatDate(record.getDate("endDate"))));
            } else if (moneyType == 2) {
                jsonObject.put("return_time", new JSONObject().fluentPut("name", "return_time").fluentPut("formType", "date").fluentPut("start", DateUtil.formatDate(record.getDate("beginDate"))).fluentPut("end", DateUtil.formatDate(record.getDate("endDate"))));
            }
        } else {
            String start = "";
            String end = "";
            if (record.getInt("cycleNum") == 1){
                start = DateUtil.formatDateTime(DateUtil.beginOfDay(record.getDate("beginDate")));
                end = DateUtil.formatDateTime(DateUtil.endOfDay(record.getDate("endDate")));
            }else {
                start = DateUtil.formatDateTime(record.getDate("beginDate"));
                end = DateUtil.formatDateTime(record.getDate("endDate"));
            }
            jsonObject.put("create_time", new JSONObject().fluentPut("name", "create_time").fluentPut("formType", "datetime").fluentPut("start", start).fluentPut("end", end));
        }
        if (checkStatus != null && checkStatus == 1) {
            jsonObject.put("check_status", new JSONObject().fluentPut("name", "check_status").fluentPut("value", "1").fluentPut("condition", "is"));
        }
        basePageRequest.getJsonObject().put("data",jsonObject);
        CrmEnum crmEnum = CrmEnum.parse(label);
        R result;
        switch (crmEnum){
            case CRM_CUSTOMER:result = crmCustomerService.queryPageList(basePageRequest,false);break;
            case CRM_CONTACTS:result = crmContactsService.queryPageList(basePageRequest,false);break;
            case CRM_BUSINESS:result = crmBusinessService.queryPageList(basePageRequest,false);break;
            case CRM_CONTRACT:result = crmContractService.queryPageList(basePageRequest,false);break;
            case CRM_RECEIVABLES:result = crmReceivablesService.queryPageList(basePageRequest,false);break;
            default:result = R.error();break;
        }
        return result;
    }

    /**
     * 业绩指标
     */
    public R queryPerformance(String startTime, String endTime, String userIds, Integer deptId, Integer status, String type) {
        BiParams biParams = new BiParams();
        boolean b = true;
        biParams.setType(type);
        biParams.setStartTime(startTime);
        biParams.setEndTime(endTime);
        Record record = biTimeUtil.analyzeTime(biParams);
        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
            Date start = DateUtil.parseDate(startTime);
            Date end = DateUtil.parseDate(endTime);
            Integer startMonth = Integer.valueOf(DateUtil.format(start, "yyyyMM"));
            Integer endMonth = Integer.valueOf(DateUtil.format(end, "yyyyMM"));
            b = !startMonth.equals(endMonth);
            if (start.getTime() == DateUtil.beginOfMonth(start).getTime() && end.getTime() == DateUtil.beginOfDay(DateUtil.endOfMonth(start)).getTime()) {
                String sqlDateFormat = "%Y%m";
                String dateFormat = "yyyyMM";
                Integer beginTime = Integer.valueOf(DateUtil.format(start, dateFormat));
                Integer finalTime = Integer.valueOf(DateUtil.format(end, dateFormat));
                record.set("cycleNum", 1).set("beginTime", beginTime).set("finalTime", finalTime).set("sqlDateFormat", sqlDateFormat).set("dateFormat", dateFormat);
                b = true;
            }
        }
        Integer cycleNum = record.getInt("cycleNum");
        if ("month".equals(type) || "lastMonth".equals(type)) {
            cycleNum = 1;
        }
        Integer beginTime = record.getInt("beginTime");
        List<String> mouthList=new ArrayList<>();
        for(int i = 1; i <= cycleNum; i++){
            String month= MonthEnum.valueOf(DateUtil.parse(beginTime.toString(),record.getStr("dateFormat")).monthStartFromOne());
            mouthList.add(month);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        if ("week".equals(type) || "lastWeek".equals(type) || "today".equals(type) || "yesterday".equals(type)) {
            b = false;
        }
        Kv kv = Kv.by("label", status).set(record.getColumns()).set("queryItem", b);
        kv.set("userIds", userIds).set("dept", deptId).set("monthList", mouthList).set("year", DateUtil.year(DateUtil.parse(record.getStr("beginTime"), record.getStr("dateFormat"))));
        Record dataRecord = Db.template("crm.Instrument.queryPerformance", kv).findFirst();
        BigDecimal achievementMoneys = dataRecord.getBigDecimal("achievementMoneys");
        if (achievementMoneys != null) {
            if (achievementMoneys.compareTo(BigDecimal.ZERO) == 0) {
                dataRecord.set("proportion", new BigDecimal("100"));
            } else {
                BigDecimal bigDecimal = dataRecord.getBigDecimal("money").divide(achievementMoneys, 4, BigDecimal.ROUND_HALF_UP);
                bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
                dataRecord.set("proportion", bigDecimal);
            }
        }
        dataRecord.set(1==status?"contractMoneys":"receivablesMoneys",dataRecord.get("money"));
        return R.ok().put("data",dataRecord);
    }
    /**
     * 销售趋势（新）
     * type 1.今天 2.昨天 3.本周 4.上周 5.本月6.上月7.本季度8.上季度9.本年10上年11.自定义
     * userIds 员工id拼写id之间用‘,’隔开
     * deptIds 部门id
     * @author zhangzhiwei
     */
    public R salesTrend(String type, String userIds, String deptIds, String startTime, String endTime, Integer label) {
        BiParams biParams = new BiParams();
        Record record = new Record();
        if (StrUtil.isNotEmpty(type)){
            if (type.equals("year")||type.equals("lastYear")) {
                biParams.setType(type);
                record = biTimeUtil.analyzeTime(biParams);
            }else {
                endTime = DateUtil.format(new Date(),"yyyy-MM-dd");
                startTime = DateUtil.format(DateUtil.offsetMonth(new Date(),-12),"yyyy-MM-dd");
            }
        }
        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
            Integer startMonth = Integer.valueOf(DateUtil.format(DateUtil.parse(startTime),"yyyyMM"));
            Integer endMonth = Integer.valueOf(DateUtil.format(DateUtil.parse(endTime),"yyyyMM"));
            if (endMonth - startMonth < 100){
                endTime = DateUtil.format(new Date(),"yyyy-MM-dd");
                startTime = DateUtil.format(DateUtil.offsetMonth(new Date(),-12),"yyyy-MM-dd");
            }
            biParams.setStartTime(startTime);
            biParams.setEndTime(endTime);
            record = biTimeUtil.analyzeTime(biParams);
        }
        Integer cycleNum = record.getInt("cycleNum");
        String sqlDateFormat = record.getStr("sqlDateFormat");
        Integer beginTime = record.getInt("beginTime");
        List<Record> recordList = new ArrayList<>();
        for (int i = 1; i <= cycleNum; i++) {
            Kv kv = Kv.by("beginTime", beginTime).set("sqlDateFormat", sqlDateFormat).set("userIds", userIds);
            kv.set("month", MonthEnum.valueOf(DateUtil.parse(beginTime.toString(), record.getStr("dateFormat")).monthStartFromOne()));
            kv.set("dept", deptIds);
            kv.set("label", label);
            kv.set("year", DateUtil.year(DateUtil.parse(beginTime.toString(), record.getStr("dateFormat"))));
            recordList.add(Db.findFirst(Db.getSqlPara("bi.base.salesTrend", kv)));
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", recordList);
        return R.ok().put("data", jsonObject);
    }

    public R sellFunnel(String type, String userIds, String startTime, String endTime, Integer typeId){
        List<Record> list = new ArrayList<>();
        Record sum_money = null;
        Record sum_shu = null;
        Record sum_ying = null;
        if (StrUtil.isNotEmpty(userIds)){
            String[] userIdss = userIds.split(",");
            Integer status = biTimeUtil.analyzeType(type);
            list = Db.find(Db.getSqlPara("bi.funnel.sellFunnel", Kv.by("userIds", userIdss).set("type", status).set("startTime", startTime).set("endTime", endTime).set("typeId", typeId)));
            sum_money = Db.findFirst(Db.getSqlPara("bi.funnel.sellFunnelSum", Kv.by("userIds", userIdss).set("type", status).set("startTime", startTime).set("endTime", endTime).set("typeId", typeId)));
            sum_shu = Db.findFirst(Db.getSqlPara("bi.funnel.sellFunnelSum", Kv.by("userIds", userIdss).set("type", status).set("startTime", startTime).set("endTime", endTime).set("typeId", typeId).set("isEnd", 2)));
            sum_ying = Db.findFirst(Db.getSqlPara("bi.funnel.sellFunnelSum", Kv.by("userIds", userIdss).set("type", status).set("startTime", startTime).set("endTime", endTime).set("typeId", typeId).set("isEnd", 1)));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);
        jsonObject.put("sum_money", sum_money != null ? sum_money.getBigDecimal("money") : 0);
        jsonObject.put("sum_shu", sum_shu != null ? sum_shu.getBigDecimal("money") : 0);
        jsonObject.put("sum_ying", sum_ying != null ? sum_ying.getBigDecimal("money") : 0);
        return R.ok().put("data", jsonObject);
    }

    public R queryRecordCount(String status, String userIds, String startTime, String endTime){
        //1.今天 2.昨天 3.本周 4.上周 5.本月6.上月7.本季度8.上季度9.本年10上年
        Integer type = biTimeUtil.analyzeType(status);
        List<Record> recordList = Db.find(Db.getSqlPara("crm.Instrument.queryRecordCount", Kv.by("type", type).set("userIds", TagUtil.toLongSet(userIds)).set("startTime", startTime).set("endTime", endTime)));
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_LEADS.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_LEADS.getType()).set("count", 0));
        }
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_CUSTOMER.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_CUSTOMER.getType()).set("count", 0));
        }
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_CONTACTS.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_CONTACTS.getType()).set("count", 0));
        }
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_BUSINESS.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_BUSINESS.getType()).set("count", 0));
        }
        if(recordList.stream().noneMatch(record -> CrmEnum.CRM_CONTRACT.getType()==record.getInt("crmType"))){
            recordList.add(new Record().set("crmType", CrmEnum.CRM_CONTRACT.getType()).set("count", 0));
        }
        return R.ok().put("data", recordList);
    }


    public R queryRecordList(BasePageRequest request, String status, String userIds, String startTime, String endTime, String crmType, Integer queryType) {
        //1.今天 2.昨天 3.本周 4.上周 5.本月6.上月7.本季度8.上季度9.本年10上年
        Integer type = biTimeUtil.analyzeType(status);
        Page<Record> paginate = Db.paginate(request.getPage(), request.getLimit(), Db.getSqlPara("crm.Instrument.queryRecordList", Kv.by("crmType", crmType).set("type", type).set("userIds", TagUtil.toLongSet(userIds)).set("startTime", startTime).set("endTime", endTime).set("queryType", queryType)));
        paginate.getList().forEach(record -> crmActivityService.buildActivityRelation(record));
        return R.ok().put("data", paginate);
    }

    public R forgottenCustomerCount(String userIds) {
        Kv kv = Kv.create();
        Integer sevenDays = Db.template("crm.Instrument.forgottenCustomerCount", Kv.by("day", 7).set("userIds",userIds)).queryInt();
        Integer fifteenDays = Db.template("crm.Instrument.forgottenCustomerCount", Kv.by("day", 15).set("userIds",userIds)).queryInt();
        Integer oneMonth = Db.template("crm.Instrument.forgottenCustomerCount", Kv.by("day", 30).set("userIds",userIds)).queryInt();
        Integer threeMonth = Db.template("crm.Instrument.forgottenCustomerCount", Kv.by("day", 90).set("userIds",userIds)).queryInt();
        Integer sixMonth = Db.template("crm.Instrument.forgottenCustomerCount", Kv.by("day", 180).set("userIds",userIds)).queryInt();
        Integer  unContactCustomerCount = Db.template("crm.Instrument.unContactCustomerCount",Kv.by("userIds",userIds)).queryInt();
        kv.set("sevenDays",sevenDays).set("fifteenDays",fifteenDays).set("oneMonth",oneMonth).set("threeMonth",threeMonth).set("sixMonth",sixMonth).set("unContactCustomerCount",unContactCustomerCount);
        return R.ok().put("data",kv);
    }

    public R forgottenCustomerPageList(BasePageRequest basePageRequest, String userIds) {
        Integer day = basePageRequest.getJsonObject().getInteger("day");
        JSONObject jsonObject=new JSONObject();
        List<Integer> customerIds = Db.template("crm.Instrument.forgottenCustomer", Kv.by("day", day).set("userIds",userIds)).query();
        if (customerIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        jsonObject.put("customerIds",new JSONObject().fluentPut("name","customer_id").fluentPut("value",StrUtil.join(",",customerIds)).fluentPut("condition","in"));
        basePageRequest.getJsonObject().put("data",jsonObject);
        return crmCustomerService.queryPageList(basePageRequest,false);
    }

    public R unContactCustomerPageList(BasePageRequest basePageRequest, String userIds) {
        String search = basePageRequest.getJsonObject().getString("search");
        List<Integer> customerIds = Db.template("crm.Instrument.unContactCustomer", Kv.by("search",search).set("userIds",userIds)).query();
        if (customerIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("customerIds",new JSONObject().fluentPut("name","customer_id").fluentPut("value",StrUtil.join(",",customerIds)).fluentPut("condition","in"));
        basePageRequest.getJsonObject().put("data",jsonObject);
        return  crmCustomerService.queryPageList(basePageRequest,true);
    }

    public R queryModelSort() {
        List<Record> leftList = Db.find("select model_id,is_hidden from 72crm_crm_instrument_sort where user_id = ? and list = 1 order by sort", BaseUtil.getUserId());
        List<Record> rightList = Db.find("select model_id,is_hidden from 72crm_crm_instrument_sort where user_id = ? and list = 2 order by sort", BaseUtil.getUserId());
        if (leftList.size() == 0 && rightList.size() == 0) {
            leftList.add(new Record().set("model_id", 1).set("is_hidden", 0));
            leftList.add(new Record().set("model_id", 5).set("is_hidden", 0));
            leftList.add(new Record().set("model_id", 7).set("is_hidden", 0));
            rightList.add(new Record().set("model_id", 2).set("is_hidden", 0));
            rightList.add(new Record().set("model_id", 4).set("is_hidden", 0));
            rightList.add(new Record().set("model_id", 6).set("is_hidden", 0));
        }
        return R.ok().put("data", Kv.by("left", leftList).set("right", rightList));
    }

    @Before(Tx.class)
    public R setModelSort(JSONObject jsonObject) {
        List<CrmInstrumentSort> leftList = jsonObject.getJSONArray("left").toJavaList(CrmInstrumentSort.class);
        Db.delete("delete from 72crm_crm_instrument_sort where user_id = ?", BaseUtil.getUserId());
        for (int i = 0; i < leftList.size(); i++) {
            CrmInstrumentSort instrumentSort = leftList.get(i);
            instrumentSort.setList(1);
            instrumentSort.setUserId(BaseUtil.getUserId());
            instrumentSort.setSort(i);
            instrumentSort.save();
        }
        List<CrmInstrumentSort> rightList = jsonObject.getJSONArray("right").toJavaList(CrmInstrumentSort.class);
        for (int i = 0; i < rightList.size(); i++) {
            CrmInstrumentSort crmInstrumentSort = rightList.get(i);
            crmInstrumentSort.setList(2);
            crmInstrumentSort.setUserId(BaseUtil.getUserId());
            crmInstrumentSort.setSort(i);
            crmInstrumentSort.save();
        }
        return R.ok();
    }

    public R ranking(String type, Integer label, String startTime, String endTime, String userIds) {
        BiParams biParams = new BiParams();
        biParams.setType(type);
        //合同、回款金额排行筛选当天，按当月算，跨月或跨年就按照这几个月或年的目标之和计算，需特殊处理
        if (label == 1 || label == 2){
            if (StrUtil.isNotEmpty(type) && (type.equals("today") || type.equals("yesterday") || type.equals("week") || type.equals("lastWeek"))){
                startTime = DateUtil.format(DateUtil.beginOfMonth(DateUtil.date()),"yyyy-MM-dd");
                endTime = DateUtil.format(DateUtil.endOfMonth(DateUtil.date()), "yyyy-MM-dd");
                biParams.setType(null);
            }
        }
        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
            if (label == 1 || label == 2) {
                Date start = DateUtil.parseDate(startTime);
                Date end = DateUtil.parseDate(endTime);
                Integer startMonth = Integer.valueOf(DateUtil.format(start, "yyyyMM"));
                Integer endMonth = Integer.valueOf(DateUtil.format(end, "yyyyMM"));
                if (startMonth.equals(endMonth)){
                    startTime = DateUtil.format(DateUtil.beginOfMonth(start),"yyyy-MM-dd");
                    endTime = DateUtil.format(DateUtil.endOfMonth(end), "yyyy-MM-dd");
                }else {
                    startTime = DateUtil.format(DateUtil.beginOfMonth(DateUtil.parseDate(startTime)),"yyyy-MM-dd");
                    endTime = DateUtil.format(DateUtil.endOfMonth(DateUtil.parseDate(endTime)), "yyyy-MM-dd");
                }
            }
            biParams.setStartTime(startTime);
            biParams.setEndTime(endTime);
        }
        Record record = Aop.get(BiTimeUtil.class).analyzeTime(biParams);
        record.set("userIds", userIds);
        List<Record> recordList = new ArrayList<>();
        Record self = new Record();
        switch (label) {
            case 1:
                recordList = contractRanKing(record);
                break;
            case 2:
                recordList = receivablesRanKing(record);
                break;
            case 3:
                recordList = Aop.get(BiRankingService.class).contractCountRanKing(record);
                break;
            case 4:
                recordList = Aop.get(BiRankingService.class).customerCountRanKing(record);
                break;
            case 5:
                recordList = Aop.get(BiRankingService.class).contactsCountRanKing(record);
                break;
            case 6:
                recordList = Aop.get(BiRankingService.class).businessRanKing(record);
                break;
            case 7:
                recordList = Aop.get(BiRankingService.class).businessMoneyRanKing(record);
                break;
            case 8:
                recordList = Aop.get(BiRankingService.class).recordCountRanKing(record);
                break;
            default:
                break;
        }
        for (int i = 0; i < recordList.size(); i++) {
            Record user = recordList.get(i);
            if (BaseUtil.getUserId().equals(user.getLong("user_id"))) {
                self = user.set("sort", i + 1);
            }
        }
        return R.ok().put("data", Kv.by("self", self).set("ranking", recordList.size() > 10 ? recordList.subList(0, 9) : recordList));
    }

    private List<Record> contractRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        record.set("monthMap",getMonthMap(record.getDate("beginDate"),record.getDate("endDate")));
        return Db.find(Db.getSqlPara("crm.Instrument.contractRanKing", record.getColumns()));
    }

    private List<Record> receivablesRanKing(Record record) {
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        record.set("monthMap",getMonthMap(record.getDate("beginDate"),record.getDate("endDate")));
        return Db.find(Db.getSqlPara("crm.Instrument.receivablesRanKing", record.getColumns()));
    }

    private List<Map<String,String>> getMonthMap(Date beginDate, Date endDate){
        List<Map<String,String>> monthMap = new ArrayList<>();
        Integer beginYear = DateUtil.year(beginDate);
        Integer endYear = DateUtil.year(endDate);
        Integer beginMonth = DateUtil.month(beginDate);
        Integer endMonth = DateUtil.month(endDate);
        for (int i = 0; i <= endYear - beginYear; i++) {
            int year = beginYear + i;
            StringBuilder stringBuilder = new StringBuilder("(");
            Map<String,String> map = new HashMap<>();
            int startNum = i == 0 ? beginMonth : 0;
            for (int j = startNum; j < 12; j++) {
                stringBuilder.append("ifnull(").append(MonthEnum.valueOf(j+1)).append(",0) +");
                if (endYear.equals(year) && endMonth.equals(j)){
                    break;
                }
            }
            String month = stringBuilder.toString();
            map.put("year", Integer.toString(year));
            map.put("month",month.substring(0,month.length() - 1) + ")");
            monthMap.add(map);
        }
        return monthMap;
    }
}
