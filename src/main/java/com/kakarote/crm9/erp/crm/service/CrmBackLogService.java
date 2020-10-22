package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.config.redis.RedisManager;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.crm.entity.CrmCustomerPool;
import com.kakarote.crm9.erp.crm.entity.CrmCustomerPoolRule;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author wyq
 */
public class CrmBackLogService {
    @Inject
    private AdminSceneService adminSceneService;

    @Inject
    private CrmCustomerService crmCustomerService;

    @Inject
    private CrmLeadsService crmLeadsService;

    @Inject
    private CrmContractService crmContractService;

    @Inject
    private CrmReceivablesService crmReceivablesService;

    /**
     * 待办事项数量统计
     */
    public R num() {
        Long userId = BaseUtil.getUserId();
        JSONObject jsonObject= Aop.get(AdminRoleService.class).auth(userId);
        List<String> authList= AuthUtil.queryAuth(jsonObject, "");
        Kv kv = RedisManager.getRedis().get("queryBackLogNum:"+userId.toString());;
        if(kv!=null){
            return R.ok().put("data", kv);
        }else {
            kv=new Kv();
        }
        if (authList.contains("crm:customer:index")){
            Integer todayCustomer = Db.queryInt(Db.getSql("crm.backLog.todayCustomerNum"), userId);
            Integer followCustomer = Db.queryInt(Db.getSql("crm.backLog.followCustomerNum"), userId);
            kv.set("todayCustomer", todayCustomer).set("followCustomer", followCustomer);
            List<CrmCustomerPool> poolList = CrmCustomerPool.dao.find("select * from 72crm_crm_customer_pool where status = 1 and put_in_rule = 1 and remind_setting = 1");
            Set<Integer> customerIdSet = new HashSet<>();
            poolList.forEach(pool -> {
                List<Long> userIdsList = new ArrayList<>();
                List<Record> recordList = new ArrayList<>();
                if (StrUtil.isNotEmpty(pool.getMemberDeptId())) {
                    userIdsList = Db.template("admin.user.queryUserIdByDeptId", Kv.by("deptIds", pool.getMemberDeptId())).query();
                }
                if (StrUtil.isNotEmpty(pool.getMemberUserId())) {
                    userIdsList.addAll(Arrays.stream(pool.getMemberUserId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
                }
                List<CrmCustomerPoolRule> ruleList = CrmCustomerPoolRule.dao.find("select * from 72crm_crm_customer_pool_rule where pool_id = ?", pool.getPoolId());
                for (CrmCustomerPoolRule rule : ruleList) {
                    Record record = rule.toRecord().set("remindDay", pool.getRemindDay()).set("userIds", BaseUtil.getUserId());
                    if (CollectionUtil.isNotEmpty(userIdsList)) {
                        record.set("ids", userIdsList);
                    }
                    if (rule.getType().equals(1)) {
                        recordList.addAll(Db.template("crm.backLog.putInPoolByRecord", record.getColumns()).find());
                    } else if (rule.getType().equals(2)) {
                        recordList.addAll(Db.template("crm.backLog.putInPoolByBusiness", record.getColumns()).find());
                    } else if (rule.getType().equals(3)) {
                        Integer startDay = record.getInt("limit_day") - record.getInt("remindDay");
                        record.set("startDay", startDay);
                        recordList.addAll(Db.template("crm.backLog.putInPoolByDealStatus", record.getColumns()).find());
                    }
                }
                List<Integer> customerIdList = recordList.stream().map(record -> record.getInt("customer_id")).collect(Collectors.toList());
                customerIdSet.addAll(customerIdList);
            });
            if (poolList.size() > 0) {
                kv.set("putInPoolRemind", customerIdSet.size());
            }
        }
        if (authList.contains("crm:leads:index")){
            Integer followLeads = Db.queryInt(Db.getSql("crm.backLog.followLeadsNum"), userId);
            kv.set("followLeads", followLeads);
        }
        if (authList.contains("crm:contract:index")){
            AdminConfig adminConfig = AdminConfig.getConfig("expiringContractDays");
            if (1 == adminConfig.getStatus()) {
                Integer endContract = Db.queryInt(Db.getSql("crm.backLog.endContractNum"), adminConfig.getValue(), userId);
                kv.set("endContract", endContract);
            }
            Integer checkContract = Db.queryInt(Db.getSql("crm.backLog.checkContractNum"), userId);
            kv.set("checkContract", checkContract);

            AdminConfig returnVisitRemindConfig = AdminConfig.getConfig("returnVisitRemindConfig");
            if (1 == returnVisitRemindConfig.getStatus()) {
                Integer returnVisitRemind = Db.template("crm.backLog.returnVisitRemindNum", Kv.by("remindDay", returnVisitRemindConfig.getValue()).set("userId", userId)).queryInt();
                kv.set("returnVisitRemind", returnVisitRemind);
            }
        }

        if (authList.contains("crm:receivables:index")){
            Integer checkReceivables = Db.queryInt(Db.getSql("crm.backLog.checkReceivablesNum"), userId);
            Integer remindReceivablesPlan = Db.queryInt(Db.getSql("crm.backLog.remindReceivablesPlanNum"), userId);
            kv.set("checkReceivables", checkReceivables).set("remindReceivablesPlan", remindReceivablesPlan);
        }
        RedisManager.getRedis().setex("queryBackLogNum:"+userId.toString(),60,kv);
        return R.ok().put("data", kv);
    }

    /**
     * 今日需联系客户
     * 今日需要联系为下次联系时间是今天且未跟进
     * 已逾期是过了下次联系时间那天的且未跟进的客户
     * 已联系是下次联系时间是今天且已经跟进的客户
     */
    public R todayCustomer(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer isSub = jsonObject.getInteger("isSub");
        StringBuilder stringBuffer = new StringBuilder("select customer_id from 72crm_crm_customer as a where a.status != 3 and ");
        if (type == 1) {
            stringBuffer.append(" to_days(a.next_time) = to_days(now()) and ifnull(a.last_time,now()) < a.next_time");
        } else if (type == 2) {
            stringBuffer.append(" to_days(a.last_time) < to_days(a.next_time) and to_days(a.next_time) < to_days(now())");
        } else if (type == 3) {
            stringBuffer.append("to_days(a.last_time) = to_days(now()) and to_days(a.next_time) = to_days(now())");
        } else {
            return R.error("type类型不正确");
        }
        if (isSub == 1) {
            stringBuffer.append(" and a.owner_user_id = ").append(BaseUtil.getUserId());
        } else if (isSub == 2) {
            String ids = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
            stringBuffer.append(" and a.owner_user_id in (").append(ids).append(")");
        } else {
            return R.error("isSub参数不正确");
        }
        List<Integer> customerIds = Db.query(stringBuffer.toString());
        if (customerIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("backLog", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", customerIds)));
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        return crmCustomerService.queryPageList(basePageRequest, false);
    }

    /**
     * 标记线索为已跟进
     */
    public R setLeadsFollowup(String ids) {
        Db.update(Db.getSqlPara("crm.backLog.setLeadsFollowup", Kv.by("ids", ids)));
        return R.ok();
    }

    /**
     * 分配给我的线索
     */
    public R followLeads(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer isSub = jsonObject.getInteger("isSub");
        StringBuilder stringBuffer = new StringBuilder("select leads_id from 72crm_crm_leads as a where");
        if (type == 1) {
            stringBuffer.append(" a.followup = 0 and a.is_transform = 0");
        } else if (type == 2) {
            stringBuffer.append(" a.followup = 1 and a.is_transform = 0");
        } else {
            return R.error("type类型不正确");
        }
        if (isSub == 1) {
            stringBuffer.append(" and a.owner_user_id = ").append(BaseUtil.getUserId());
        } else if (isSub == 2) {
            String ids = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
            stringBuffer.append(" and a.owner_user_id in (").append(ids).append(")");
        } else {
            return R.error("isSub参数不正确");
        }
        List<Integer> leadsIds = Db.query(stringBuffer.toString());
        if (leadsIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("backLog", new JSONObject().fluentPut("name", "leads_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", leadsIds)));
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        return crmLeadsService.queryPageList(basePageRequest, false);
    }

    /**
     * 标记客户为已跟进
     */
    public R setCustomerFollowup(String ids) {
        List<String> stringList = StrUtil.splitTrim(ids, ",");
        if (stringList.size() > 0) {
            Db.update(Db.getSqlPara("crm.backLog.setCustomerFollowup", Kv.by("ids", stringList)));
        }
        return R.ok();
    }

    /**
     * 分配给我的客户
     */
    public R followCustomer(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer isSub = jsonObject.getInteger("isSub");
        StringBuilder stringBuffer = new StringBuilder("select customer_id from 72crm_crm_customer as a where a.is_receive = 1 and a.status = 1");
        if (type == 1) {
            stringBuffer.append(" and  a.followup = 0");
        } else if (type == 2) {
            stringBuffer.append(" and  a.followup = 1");
        } else {
            return R.error("type类型不正确");
        }
        if (isSub == 1) {
            stringBuffer.append(" and a.owner_user_id = ").append(BaseUtil.getUserId());
        } else if (isSub == 2) {
            String ids = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
            stringBuffer.append(" and a.owner_user_id in (").append(ids).append(")");
        } else {
            return R.error("isSub参数不正确");
        }
        List<Integer> customerIds = Db.query(stringBuffer.toString());
        if (customerIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("backLog", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", customerIds)));
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        return crmCustomerService.queryPageList(basePageRequest, false);
    }

    /**
     * 待审核合同
     */
    public R checkContract(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer isSub = jsonObject.getInteger("isSub");
        StringBuilder stringBuffer = new StringBuilder("select contract_id from 72crm_crm_contract as a inner join 72crm_admin_examine_record as b on a.examine_record_id = b.record_id left join 72crm_admin_examine_log as c on b.record_id = c.record_id where c.is_recheck != 1 and");
        if (type == 1) {
            stringBuffer.append(" ifnull(b.examine_step_id, 1) = ifnull(c.examine_step_id, 1) and a.check_status in (0,3) and c.examine_status in (0,3)");
        } else if (type == 2) {
            stringBuffer.append(" a.check_status in (1,2)");
        } else {
            return R.error("type类型不正确");
        }
        if (isSub == 1) {
            stringBuffer.append(" and c.examine_user = ").append(BaseUtil.getUserId());
        } else if (isSub == 2) {
            String ids = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
            stringBuffer.append(" and c.examine_user in (").append(ids).append(")");
        } else {
            return R.error("isSub参数不正确");
        }
        List<Integer> contractIds = Db.query(stringBuffer.toString());
        if (contractIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("backLog", new JSONObject().fluentPut("name", "contract_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", contractIds)));
        jsonObject.fluentPut("data", data).fluentPut("search", "").fluentPut("type", 6);
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        return crmContractService.queryPageList(basePageRequest, false);
    }

    /**
     * 待审核回款
     */
    public R checkReceivables(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer isSub = jsonObject.getInteger("isSub");
        StringBuilder stringBuffer = new StringBuilder("select receivables_id from 72crm_crm_receivables as a inner join 72crm_admin_examine_record as b on a.examine_record_id = b.record_id left join 72crm_admin_examine_log as c on b.record_id = c.record_id where");
        if (type == 1) {
            stringBuffer.append(" ifnull(b.examine_step_id, 1) = ifnull(c.examine_step_id, 1) and a.check_status in (0,3)");
        } else if (type == 2) {
            stringBuffer.append(" a.check_status in (1,2)");
        } else {
            return R.error("type类型不正确");
        }
        if (isSub == 1) {
            stringBuffer.append(" and c.examine_user = ").append(BaseUtil.getUserId());
        } else if (isSub == 2) {
            String ids = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
            stringBuffer.append(" and c.examine_user in (").append(ids).append(")");
        } else {
            return R.error("isSub参数不正确");
        }
        List<Integer> receivablesIds = Db.query(stringBuffer.toString());
        if (receivablesIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("backLog", new JSONObject().fluentPut("name", "receivables_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", receivablesIds)));
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        return crmReceivablesService.queryPageList(basePageRequest, false);
    }

    /**
     * 待回款提醒
     */
    public R remindReceivables(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer isSub = jsonObject.getInteger("isSub");
        StringBuilder stringBuffer = new StringBuilder("from 72crm_crm_receivables_plan as a inner join 72crm_crm_customer as b on a.customer_id = b.customer_id inner join 72crm_crm_contract as c on a.contract_id = c.contract_id where");
        if (type == 1) {
            stringBuffer.append(" to_days(a.return_date) >= to_days(now()) and to_days(a.return_date) <= to_days(now())+a.remind and receivables_id is null");
        } else if (type == 2) {
            stringBuffer.append(" receivables_id is not null");
        } else if (type == 3) {
            stringBuffer.append(" to_days(a.return_date) < to_days(now()) and receivables_id is null");
        } else {
            return R.error("type类型不正确");
        }
        if (isSub == 1) {
            stringBuffer.append(" and c.owner_user_id = ").append(BaseUtil.getUserId());
        } else if (isSub == 2) {
            String ids = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
            stringBuffer.append(" and c.owner_user_id in (").append(ids).append(")");
        } else {
            return R.error("isSub参数不正确");
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        if (data != null) {
            stringBuffer.append(getConditionSql(data));
        }
        Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), "select a.num,a.customer_id,b.customer_name,a.contract_id,c.num as contractNum,a.money,a.return_date,a.return_type,a.remind,a.remark", stringBuffer.toString());
        return R.ok().put("data", page);
    }

    /**
     * 即将到期的合同
     */
    public R endContract(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer type = jsonObject.getInteger("type");
        Integer isSub = jsonObject.getInteger("isSub");
        AdminConfig adminConfig = AdminConfig.dao.findFirst("select * from 72crm_admin_config where name = 'expiringContractDays'");
        StringBuilder stringBuffer = new StringBuilder("select contract_id from 72crm_crm_contract as a where check_status = 1 and");
        if (type == 1) {
            if (adminConfig.getStatus() == 0 || ObjectUtil.isNull(adminConfig)) {
                return R.ok().put("data", new Page<>());
            }
            stringBuffer.append(" to_days(end_time) >= to_days(now()) and to_days(end_time) <= to_days(now())+").append(adminConfig.getValue());
        } else if (type == 2) {
            stringBuffer.append(" to_days(end_time) < to_days(now())");
        } else {
            return R.error("type类型不正确");
        }
        if (isSub == 1) {
            stringBuffer.append(" and owner_user_id = ").append(BaseUtil.getUserId());
        } else if (isSub == 2) {
            String ids = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
            stringBuffer.append(" and owner_user_id in (").append(ids).append(")");
        } else {
            return R.error("isSub参数不正确");
        }
        List<Integer> contractIds = Db.query(stringBuffer.toString());
        if (contractIds.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("backLog", new JSONObject().fluentPut("name", "contract_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", contractIds)));
        jsonObject.fluentPut("data", data).fluentPut("search", "").fluentPut("type", 6);
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        return crmContractService.queryPageList(basePageRequest, false);
    }

    /**
     * @author wyq
     * 待进入客户池提醒
     */
    public R putInPoolRemind(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer isSub = jsonObject.getInteger("isSub");
        String userIds;
        if (isSub == 1) {
            userIds = BaseUtil.getUserId().toString();
        } else if (isSub == 2) {
            userIds = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
        } else {
            return R.error("isSub参数不正确");
        }
        List<CrmCustomerPool> poolList = CrmCustomerPool.dao.find("select * from 72crm_crm_customer_pool where status = 1 and put_in_rule = 1 and remind_setting = 1");
        if (poolList.size() == 0) {
            return R.ok().put("data", new Page());
        }
        Map<Integer, Integer> customerMap = new HashMap<>();
        poolList.forEach(pool -> {
            List<Long> userIdsList = new ArrayList<>();
            if (StrUtil.isNotEmpty(pool.getMemberDeptId())) {
                userIdsList = Db.template("admin.user.queryUserIdByDeptId", Kv.by("deptIds", pool.getMemberDeptId())).query();
            }
            if (StrUtil.isNotEmpty(pool.getMemberUserId())) {
                userIdsList.addAll(Arrays.stream(pool.getMemberUserId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            List<CrmCustomerPoolRule> ruleList = CrmCustomerPoolRule.dao.find("select * from 72crm_crm_customer_pool_rule where pool_id = ?", pool.getPoolId());
            for (CrmCustomerPoolRule rule : ruleList) {
                Record record = rule.toRecord().set("remindDay", pool.getRemindDay()).set("userIds", userIds);
                if (CollectionUtil.isNotEmpty(userIdsList)) {
                    record.set("ids", userIdsList);
                }
                List<Record> recordList = new ArrayList<>();
                if (rule.getType().equals(1)) {
                    recordList = Db.template("crm.backLog.putInPoolByRecord", record.getColumns()).find();
                } else if (rule.getType().equals(2)) {
                    recordList = Db.template("crm.backLog.putInPoolByBusiness", record.getColumns()).find();
                } else if (rule.getType().equals(3)) {
                    recordList = Db.template("crm.backLog.putInPoolByDealStatus", record.getColumns()).find();
                }
                recordList.forEach(r -> {
                    Integer customerId = r.getInt("customer_id");
                    if (customerMap.containsKey(customerId) && customerMap.get(customerId) <= r.getInt("poolDay")) {
                        return;
                    }
                    customerMap.put(customerId, r.getInt("poolDay"));
                });
            }
        });
        Set<Integer> customerIdSet = customerMap.keySet();
        if (customerIdSet.size() == 0) {
            return R.ok().put("data", new Page());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("backLog", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", customerIdSet)));
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        R r = crmCustomerService.queryPageList(basePageRequest, false);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.get("data")));
        List<Record> list = object.getJSONArray("list").toJavaList(Record.class);
        list.forEach(record -> record.set("poolDay", customerMap.get(record.getInt("customer_id"))));
        object.fluentPut("list", list);
        return r.put("data", object);
    }

    /**
     * 待回访客户
     */
    public R returnVisitRemind(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer isSub = jsonObject.getInteger("isSub");
        String userIds;
        if (isSub == 1) {
            userIds = BaseUtil.getUserId().toString();
        } else if (isSub == 2) {
            userIds = adminSceneService.getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1);
        } else {
            return R.error("isSub参数不正确");
        }
        AdminConfig adminConfig = AdminConfig.getConfig("returnVisitRemindConfig");
        List<Integer> contractIdList = Db.template("crm.backLog.returnVisitRemind", Kv.by("remindDay", adminConfig.getValue()).set("userIds", userIds)).query();
        if (contractIdList.size() == 0) {
            return R.ok().put("data", new Page<>());
        }
        JSONObject data = jsonObject.getJSONObject("data") != null ? jsonObject.getJSONObject("data") : new JSONObject();
        data.fluentPut("backLog", new JSONObject().fluentPut("name", "contract_id").fluentPut("condition", "in").fluentPut("value", StrUtil.join(",", contractIdList)));
        jsonObject.fluentPut("data", data).fluentPut("search", "").fluentPut("type", 6);
        basePageRequest.setJsonObject(jsonObject.fluentPut("data", data));
        return crmContractService.queryPageList(basePageRequest, false);
    }

    private String getConditionSql(JSONObject data) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        if (data != null) {
            data.forEach((k, v) -> jsonObjectList.add(JSON.parseObject(v.toString())));
        }
        StringBuilder conditionSqlSb = new StringBuilder();
        for (JSONObject jsonObject : jsonObjectList) {
            String condition = jsonObject.getString("condition");
            String value = jsonObject.getString("value");
            String formType = jsonObject.getString("formType");
            if (StrUtil.isNotEmpty(value) || StrUtil.isNotEmpty(jsonObject.getString("start")) || StrUtil.isNotEmpty(jsonObject.getString("end")) || "business_type".equals(jsonObject.getString("formType"))) {
                conditionSqlSb.append(" and ").append("a.").append(jsonObject.getString("name"));
                if ("is".equals(condition)) {
                    conditionSqlSb.append(" = '").append(value).append("'");
                } else if ("isNot".equals(condition)) {
                    conditionSqlSb.append(" != '").append(value).append("'");
                } else if ("contains".equals(condition)) {
                    conditionSqlSb.append(" like '%").append(value).append("%'");
                } else if ("notContains".equals(condition)) {
                    conditionSqlSb.append(" not like '%").append(value).append("%'");
                } else if ("isNull".equals(condition)) {
                    conditionSqlSb.append(" is null");
                } else if ("isNotNull".equals(condition)) {
                    conditionSqlSb.append(" is not null");
                } else if ("gt".equals(condition)) {
                    conditionSqlSb.append(" > ").append(value);
                } else if ("egt".equals(condition)) {
                    conditionSqlSb.append(" >= ").append(value);
                } else if ("lt".equals(condition)) {
                    conditionSqlSb.append(" < ").append(value);
                } else if ("elt".equals(condition)) {
                    conditionSqlSb.append(" <= ").append(value);
                } else if ("in".equals(condition)) {
                    conditionSqlSb.append(" in (").append(value).append(")");
                }
                if ("datetime".equals(formType)) {
                    conditionSqlSb.append(" between '").append(jsonObject.getString("start")).append("' and '").append(jsonObject.getString("end")).append("'");
                }
                if ("date".equals(formType)) {
                    conditionSqlSb.append(" between '").append(jsonObject.getString("startDate")).append("' and '").append(jsonObject.getString("endDate")).append("'");
                }
            }
        }
        return conditionSqlSb.toString();
    }
}
