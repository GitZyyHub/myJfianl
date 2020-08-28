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
import com.kakarote.crm9.erp.crm.service.CrmContractService;
import com.kakarote.crm9.erp.crm.service.CrmCustomerService;
import com.kakarote.crm9.erp.crm.service.CrmReceivablesService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wyq
 */
public class BiCustomerService {
    @Inject
    private BiTimeUtil biTimeUtil;

    /**
     * 客户总量分析图
     * @author wyq
     */
    public R totalCustomerStats(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getInt("cycleNum");
        Integer beginTime = record.getInt("beginTime");
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.template("bi.customer.totalCustomerStats",record.set("timeList",timeList).getColumns()).find();
        return R.ok().put("data",recordList);
    }

    public R increaseCustomer(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Record record = biTimeUtil.analyzeTime(biParams);
        List<Integer> customerIdList = Db.template("bi.customer.increaseCustomer", record.set("userId", biParams.getUserId()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(customerIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmCustomerService.class).queryPageList(basePageRequest, true);
    }

    public R dealCustomer(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Record record = biTimeUtil.analyzeTime(biParams);
        List<Integer> customerIdList = Db.template("bi.customer.dealCustomer", record.set("userId", biParams.getUserId()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(customerIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmCustomerService.class).queryPageList(basePageRequest, true);
    }

    public R totalContractMoney(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Record record = biTimeUtil.analyzeTime(biParams);
        List<Integer> contractIdList = Db.template("bi.customer.totalContractMoney", record.set("userId", biParams.getUserId()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "contract_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(contractIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmContractService.class).queryPageList(basePageRequest, true);
    }

    public R totalReceivablesMoney(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Record record = biTimeUtil.analyzeTime(biParams);
        List<Integer> receivablesIdList = Db.template("bi.customer.totalReceivablesMoney", record.set("userId", biParams.getUserId()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "receivables_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(receivablesIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmReceivablesService.class).queryPageList(basePageRequest, true);
    }

    /**
     * 客户总量分析表
     * @author wyq
     */
    public R totalCustomerTable(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",new String[]{});
        }
        String[] userIdsArr = userIds.split(",");
        List<Record> recordList = Db.template("bi.customer.totalCustomerTable",record.set("userIds",userIdsArr).getColumns()).find();
        Record total = Db.findFirst(Db.getSqlPara("bi.customer.sumTotalCustomerTable", record));
        recordList.forEach(r -> {
            r.set("dealCustomerRate",r.getInt("customerNum") != 0 ? r.getInt("dealCustomerNum")*100/r.getInt("customerNum") : 0);
        });
//        total.set("dealCustomerRate", total.getInt("customerNum") != 0 ? total.getInt("dealCustomerNum") * 100 / total.getInt("customerNum") : 0);
        return R.ok().put("data", Kv.by("list", recordList).set("total", total));
    }

    /**
     * 客户跟进次数分析图
     * @author wyq
     */
    public R customerRecordStats(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getInt("cycleNum");
        Integer beginTime = record.getInt("beginTime");
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.template("bi.customer.customerRecordStats",record.set("timeList",timeList).getColumns()).find();
        return R.ok().put("data",recordList);
    }

    /**
     * 客户跟进次数分析表
     * @author wyq
     */
    public R customerRecordInfo(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",new String[]{});
        }
        String[] userIdsArr = userIds.split(",");
        List<Record> recordList = Db.template("bi.customer.customerRecordInfo",record.set("userIds",userIdsArr).getColumns()).find();
        Record total = Db.findFirst(Db.getSqlPara("bi.customer.sumCustomerRecordInfo", record.getColumns()));
        return R.ok().put("data", Kv.by("list", recordList).set("total", total));
    }

    public R followUpCustomer(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Record record = biTimeUtil.analyzeTime(biParams);
        List<Integer> customerIdList = Db.template("bi.customer.followUpCustomer", record.set("userId", biParams.getUserId()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(customerIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmCustomerService.class).queryPageList(basePageRequest, true);
    }

    /**
     * 客户跟进方式分析
     * @author wyq
     */
    public R customerRecodCategoryStats(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String sqlDateFormat = record.getStr("sqlDateFormat");
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            userIds = "0";
        }
        String[] idsArr = userIds.split(",");
        Integer beginTime = record.getInt("beginTime");
        Integer finalTime = record.getInt("finalTime");
        List<Record> recordList = Db.find(Db.getSqlPara("bi.customer.customerRecordCategoryStats", Kv.by("sqlDateFormat",sqlDateFormat).set("ids",idsArr).set("beginTime",beginTime).set("finalTime",finalTime)));
        return R.ok().put("data",recordList);
    }

    /**
     * 客户转化率分析图
     * @author wyq
     */
    public R customerConversionStats(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getInt("cycleNum");
        Integer beginTime = record.getInt("beginTime");
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        Record result = Db.template("bi.customer.customerConversionStats",record.set("timeList",timeList).getColumns()).findFirst();
        List<Record> recordList = new ArrayList<>();
        Map<String, Object> columns = result.getColumns();
        for (String type : columns.keySet()) {
            recordList.add(new Record().set("type",type).set("pro",columns.get(type)==null?0:columns.get(type)));
        }
        recordList = recordList.stream().sorted(Comparator.comparing(record1 -> record1.get("type"))).collect(Collectors.toList());
        return R.ok().put("data",recordList);
    }

    /**
     * 客户转化率分析表
     * @author wyq
     */
    public R customerConversionInfo(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String sqlDateFormat = "%Y%m";
        if (biParams.getType().length() == 8){
            sqlDateFormat = "%Y%m%d";
        }
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            userIds = "0";
        }
        String[] idsArr = userIds.split(",");
        Page<Record> recordPage = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("bi.customer.customerConversionInfo", Kv.by("sqlDateFormat", sqlDateFormat).set("ids", idsArr).set("time", biParams.getType())));
        return R.ok().put("data", recordPage);
    }

    /**
     *公海客户分析图
     * @author wyq
     */
    public R poolStats(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getInt("cycleNum");
        Integer beginTime = record.getInt("beginTime");
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.template("bi.customer.poolStats",record.set("timeList",timeList).getColumns()).find();
        return R.ok().put("data",recordList);
    }

    /**
     *公海客户分析表
     * @author wyq
     */
    public R poolTable(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            userIds = "0";
        }
        String[] idsArr = userIds.split(",");
        record.set("ids",idsArr);
        List<Record> recordList = Db.find(Db.getSqlPara("bi.customer.poolTable",record));
        Record total = Db.findFirst(Db.getSqlPara("bi.customer.sumPoolTable", record.getColumns()));
        return R.ok().put("data", Kv.by("list", recordList).set("total", total));
    }

    public R receiveFromPool(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Record record = biTimeUtil.analyzeTime(biParams);
        List<Integer> customerIdList = Db.template("bi.customer.receiveFromPool", record.set("userId", biParams.getUserId()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(customerIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmCustomerService.class).queryPageList(basePageRequest, true);
    }

    public R putInPool(BasePageRequest<BiParams> basePageRequest) {
        BiParams biParams = basePageRequest.getData();
        Record record = biTimeUtil.analyzeTime(biParams);
        List<Integer> customerIdList = Db.template("bi.customer.putInPool", record.set("userId", biParams.getUserId()).getColumns()).query();
        JSONObject data = new JSONObject().fluentPut("bi", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", CollectionUtil.join(customerIdList, ",")));
        basePageRequest.setJsonObject(data);
        return Aop.get(CrmCustomerService.class).queryPageList(basePageRequest, true);
    }

    /**
     * 员工客户成交周期图
     * @author wyq
     */
    public R employeeCycle(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        Integer cycleNum = record.getInt("cycleNum");
        Integer beginTime = record.getInt("beginTime");
        List<Integer> timeList = new ArrayList<>();
        for (int i=1; i <= cycleNum;i++){
            timeList.add(beginTime);
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.template("bi.customer.employeeCycle",record.set("timeList",timeList).getColumns()).find();
        return R.ok().put("data",recordList);
    }

    /**
     * 员工客户成交周期表
     * @author wyq
     */
    public R employeeCycleInfo(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        if (StrUtil.isEmpty(userIds)){
            return R.ok().put("data",new String[]{});
        }
        String[] userIdsArr = userIds.split(",");
        List<Record> recordList = Db.template("bi.customer.employeeCycleInfo",record.set("userIds",userIdsArr).getColumns()).find();
        Record total = Db.findFirst(Db.getSqlPara("bi.customer.sumEmployeeCycleInfo", record.getColumns()));
        return R.ok().put("data", Kv.by("list", recordList).set("total", total));
    }

    /**
     * 地区成交周期
     * @author wyq
     */
    public R districtCycle(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String[] districtArr = new String[]{"北京","上海","天津","广东","浙江","海南","福建","湖南","湖北","重庆","辽宁","吉林","黑龙江","河北","河南","山东","陕西","甘肃","青海","新疆","山西","四川","贵州","安徽","江西","江苏","云南","内蒙古","广西","西藏","宁夏"};
        List<Record> recordList = Db.template("bi.customer.districtCycle",record.set("districts",districtArr).getColumns()).find();
        Record total = Db.findFirst(Db.getSqlPara("bi.customer.sumDistrictCycle", record.getColumns()));
        return R.ok().put("data", Kv.by("list", recordList).set("total", total));
    }

    /**
     * 产品成交周期
     * @author wyq
     */
    public R productCycle(BiParams biParams){
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "customer", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        List<Record> productList = Db.find("select product_id,name from 72crm_crm_product");
        if (CollectionUtil.isEmpty(productList)){
            return R.ok().put("data",new ArrayList<>());
        }
        List<Record> recordList = Db.template("bi.customer.productCycle",record.set("products",productList).getColumns()).find();
        Record total = Db.findFirst(Db.getSqlPara("bi.customer.sumProductCycle", record.getColumns()));
        return R.ok().put("data", Kv.by("list", recordList).set("total", total));
    }
}
