package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.cache.CaffeineCache;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.common.AdminModelEnum;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.utils.AdminLogUtil;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.*;
import java.util.stream.Collectors;

public class CrmCustomerPoolService extends AbstractBasePageList {

    public R queryPoolPageList(BasePageRequest basePageRequest){
        CrmEnum crmEnum = CrmEnum.CRM_CUSTOMER_POOL;
        Kv kv = getKv(basePageRequest,crmEnum,true);
        Integer poolId = basePageRequest.getJsonObject().getInteger("poolId");
        if (poolId == null) {
            poolId = Db.queryInt("select pool_id from 72crm_crm_customer_pool order by create_time desc limit 1");
        }
        kv.set("poolId", poolId);
        if (kv.containsKey("error")){
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.customerPool.queryCustomerPoolPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.customerPool.queryCustomerPoolPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        resultJsonObject.put("list", recordPage);
        Integer count = Db.template("crm.customerPool.queryCustomerPoolPageListCount", kv).queryInt();
        resultJsonObject.put("totalRow", count);
        return R.ok().put("data", resultJsonObject);
    }

    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (customer_name like '%{search}%' or telephone like '%{search}%' or mobile like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }

    @Override
    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject sqlObject = new JSONObject();
        sqlObject.put("sql", " and owner_user_id is null and status != 3");
        sqlObject.put("type", 1);
        if(!kv.containsKey("fixed")){
            kv.set("fixed",true);
        }
        return sqlObject;
    }

    /**
     * 后台公海设置列表页
     */
    public R queryPoolSettingList(BasePageRequest basePageRequest) {
        Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), "select a.pool_id,a.pool_name,a.status,IFNULL(b.count,0) as customerNum,a.admin_user_id,a.member_user_id,a.member_dept_id", "from 72crm_crm_customer_pool as a left join (select pool_id,count(DISTINCT customer_id) as count from 72crm_crm_customer_pool_relation group by pool_id) as b on a.pool_id = b.pool_id");
        page.getList().forEach(record -> {
            List<Record> adminUserList = Db.find(Db.getSqlPara("crm.customerPool.queryUser", Kv.by("ids", record.getStr("admin_user_id"))));
            List<Record> userList = Db.find(Db.getSqlPara("crm.customerPool.queryUser", Kv.by("ids", record.getStr("member_user_id"))));
            List<Record> deptList = Db.find(Db.getSqlPara("crm.customerPool.queryMemberDept", Kv.by("ids", record.getStr("member_dept_id"))));
            record.set("adminUser", adminUserList).set("memberUser", userList).set("memberDept", deptList);
        });
        return R.ok().put("data", page);
    }

    /**
     * 公海选择列表
     */
    public R queryPoolNameList() {
        List<Record> recordList = Db.find("select pool_id,pool_name from 72crm_crm_customer_pool");
        return R.ok().put("data", recordList);
    }

    /**
     * 公海选择列表
     */
    public R queryPoolNameListByAuth() {
        Kv kv = Kv.by("userId", BaseUtil.getUserId()).set("deptId", BaseUtil.getUser().getDeptId());
        if (!BaseUtil.isAdmin()) {
            kv.set("noAdmin", 1);
        }
        List<Record> recordList = Db.find(Db.getSqlPara("crm.customerPool.queryPoolNameListByAuth", kv));
        return R.ok().put("data", recordList);
    }

    /**
     * 公海客户转移
     */
    @Before(Tx.class)
    public R transfer(Integer prePoolId, Integer postPoolId) {
        Integer status = Db.queryInt("select status from 72crm_crm_customer_pool where pool_id = ?", postPoolId);
        if (status == 0) {
            return R.error("不能向停用公海转入客户");
        }
        List<Integer> prePooCustomerIdList = Db.query("select customer_id from 72crm_crm_customer_pool_relation where pool_id = ?", prePoolId);
        Db.delete("delete from 72crm_crm_customer_pool_relation where pool_id = ?", prePoolId);
        List<Integer> postPooCustomerIdList = Db.query("select customer_id from 72crm_crm_customer_pool_relation where pool_id = ?", postPoolId);
        prePooCustomerIdList.removeAll(postPooCustomerIdList);
        prePooCustomerIdList.forEach(customerId -> {
            CrmCustomerPoolRelation relation = new CrmCustomerPoolRelation();
            relation.setPoolId(postPoolId);
            relation.setCustomerId(customerId);
            relation.save();
        });
        return R.ok();
    }

    /**
     * 公海启停
     */
    public R changeStatus(Integer poolId, Integer status) {
        Integer customerNum = Db.queryInt("select count(1) from 72crm_crm_customer_pool_relation where pool_id = ?", poolId);
        if (customerNum > 0 && status == 0) {
            return R.error("公海内有客户，不能停用");
        }
        Integer poolNum = Db.queryInt("select count(1) from 72crm_crm_customer_pool where status = 1");
        CrmCustomerPool pool = CrmCustomerPool.dao.findById(poolId);
        if (pool.getStatus().equals(1) && poolNum.equals(1) && status.equals(0)) {
            return R.error("最后一个启用公海不能停用");
        }
        return Db.update("update 72crm_crm_customer_pool set status = ? where pool_id = ?", status, poolId) > 0 ? R.ok() : R.error();
    }

    /**
     * 后台查询公海设置详情
     */
    public R queryPoolById(Integer poolId) {
        CrmCustomerPool customerPool = CrmCustomerPool.dao.findById(poolId);
        List<Record> adminUserList = Db.find(Db.getSqlPara("crm.customerPool.queryUser", Kv.by("ids", customerPool.getAdminUserId())));
        List<Record> userList = Db.find(Db.getSqlPara("crm.customerPool.queryUser", Kv.by("ids", customerPool.getMemberUserId())));
        List<Record> deptList = Db.find(Db.getSqlPara("crm.customerPool.queryMemberDept", Kv.by("ids", customerPool.getMemberDeptId())));
        Record pool = customerPool.toRecord().set("adminUser", adminUserList).set("memberUser", userList).set("memberDept", deptList);
        List<Record> ruleList = Db.find("select * from 72crm_crm_customer_pool_rule where pool_id = ?", poolId);
        //客户级别设置转为前端需要的数据结构
        Map<Integer, List<Record>> map = ruleList.stream().collect(Collectors.groupingBy(receod -> receod.getInt("type")));
        List<Record> newRuleList = new ArrayList<>();
        map.forEach((k, v) -> {
            Record record = new Record();
            List<Record> levelSettingList = new ArrayList<>();
            v.forEach(r -> {
                record.set("rule_id", r.getInt("rule_id"));
                record.set("pool_id", r.getInt("pool_id"));
                record.set("type", k);
                record.set("deal_handle", r.getInt("deal_handle"));
                record.set("business_handle", r.getInt("business_handle"));
                record.set("customer_level_setting", r.getInt("customer_level_setting"));
                levelSettingList.add(new Record().set("level", r.getStr("level")).set("limit_day", r.getInt("limit_day")));
            });
            record.set("level", levelSettingList);
            newRuleList.add(record);
        });
        List<Record> fieldList = Db.find("select * from 72crm_crm_customer_pool_field_setting where pool_id = ?", poolId);
        Integer customerNum = Db.queryInt("select count(1) from 72crm_crm_customer_pool_relation where pool_id = ?", poolId);
        return R.ok().put("data", pool.set("customerNum", customerNum).set("rule", newRuleList).set("field", fieldList));
    }

    /**
     * 获取公海默认字段
     */
    public R queryPoolField() {
        List<Record> fieldList = Db.find(Db.getSql("crm.customerPool.queryCustomerField"), CrmEnum.CRM_CUSTOMER.getType());
        fieldList.forEach(record -> record.set("field_name", StrUtil.toCamelCase(record.getStr("field_name"))));
        fieldList.add(new Record().set("field_id", null).set("field_name", "dealStatus").set("name", "成交状态").set("type", 3));
        fieldList.add(new Record().set("field_id", null).set("field_name", "lastTime").set("name", "最后跟进时间").set("type", 4));
        fieldList.add(new Record().set("field_id", null).set("field_name", "lastContent").set("name", "最后跟进记录").set("type", 2));
        fieldList.add(new Record().set("field_id", null).set("field_name", "updateTime").set("name", "更新时间").set("type", 4));
        fieldList.add(new Record().set("field_id", null).set("field_name", "createTime").set("name", "创建时间").set("type", 4));
        fieldList.add(new Record().set("field_id", null).set("field_name", "createUserName").set("name", "创建人").set("type", 10));
        fieldList.add(new Record().set("field_id", null).set("field_name", "detailAddress").set("name", "详细地址").set("type", 1));
        fieldList.add(new Record().set("field_id", null).set("field_name", "address").set("name", "地区定位").set("type", 18));
        fieldList.add(new Record().set("field_id", null).set("field_name", "preOwnerUserName").set("name", "前负责人").set("type", 10));
        Aop.get(AdminFieldService.class).recordToFormType(fieldList);
        return R.ok().put("data", fieldList);
    }

    /**
     * 获取客户级别选项
     */
    public R queryCustomerLevel() {
        String options = Db.queryStr("select options from 72crm_admin_field where label = 2 and field_name = '客户级别' and field_type= 2");
        List<String> optionList = Arrays.stream(options.split(",")).collect(Collectors.toList());
        return R.ok().put("data", optionList);
    }

    /**
     * 设置公海
     */
    @Before(Tx.class)
    public R setCustomerPool(JSONObject jsonObject,String ipAddress) {
        CrmCustomerPool customerPool = JSONObject.toJavaObject(jsonObject, CrmCustomerPool.class);
        if (customerPool.getPoolId() == null) {
            if ((StrUtil.isEmpty(customerPool.getMemberUserId()) && StrUtil.isEmpty(customerPool.getMemberDeptId())) || StrUtil.isEmpty(customerPool.getAdminUserId())) {
                return R.error("公海管理员或公海成员不能为空");
            }
            customerPool.setCreateUserId(BaseUtil.getUserId());
            customerPool.setCreateTime(DateUtil.date());
            customerPool.save();
        } else {
            customerPool.update();
            Db.delete("delete from 72crm_crm_customer_pool_rule where pool_id = ?", customerPool.getPoolId());
        }
        JSONArray ruleList = jsonObject.getJSONArray("rule");
        ruleList.forEach(object -> {
            JSONObject rule = TypeUtils.castToJavaBean(object, JSONObject.class);
            JSONArray levelSettingList = rule.getJSONArray("level");
            levelSettingList.forEach(levelObject -> {
                JSONObject levelSetting = TypeUtils.castToJavaBean(levelObject, JSONObject.class);
                CrmCustomerPoolRule crmCustomerPoolRule = new CrmCustomerPoolRule();
                crmCustomerPoolRule.setPoolId(customerPool.getPoolId());
                crmCustomerPoolRule.setType(rule.getInteger("type"));
                crmCustomerPoolRule.setDealHandle(rule.getInteger("dealHandle"));
                crmCustomerPoolRule.setBusinessHandle(rule.getInteger("businessHandle"));
                crmCustomerPoolRule.setCustomerLevelSetting(rule.getInteger("customerLevelSetting"));
                crmCustomerPoolRule.setLevel(levelSetting.getString("level"));
                crmCustomerPoolRule.setLimitDay(levelSetting.getInteger("limitDay"));
                crmCustomerPoolRule.save();
            });
        });
        List<CrmCustomerPoolFieldSetting> fieldList = jsonObject.getJSONArray("field").toJavaList(CrmCustomerPoolFieldSetting.class);
        for (CrmCustomerPoolFieldSetting field : fieldList) {
            field.setPoolId(customerPool.getPoolId());
            if (field.getSettingId() != null) {
                field.update();
                //同步更新列表页字段排序表
                //设为隐藏时从公海列表页字段排序表删除该字段
                if (field.getIsHidden() == 1) {
                    Db.delete("delete from 72crm_crm_customer_pool_field_sort where field_name = ? and pool_id = ?", field.getFieldName(), customerPool.getPoolId());
                    //不设为隐藏时更新字段
                } else {
                    Db.update(Db.getSqlPara("crm.customerPool.updateFieldSort", field.toRecord().set("poolId", customerPool.getPoolId()).getColumns()));
                }
                CaffeineCache.ME.removeAll("field:");
            } else {
                field.save();
            }
        }
        return R.ok();
    }

    /**
     * 删除公海
     */
    @Before(Tx.class)
    public R deleteCustomerPool(Integer poolId) {
        Integer customerNum = Db.queryInt("select count(1) from 72crm_crm_customer_pool_relation where pool_id = ?", poolId);
        if (customerNum > 0) {
            return R.error("公海有客户，不能删除");
        }
        Integer poolNum = Db.queryInt("select count(1) from 72crm_crm_customer_pool where status = 1");
        CrmCustomerPool pool = CrmCustomerPool.dao.findById(poolId);
        if (pool.getStatus().equals(1) && poolNum.equals(1)) {
            return R.error("最后一个启用公海不能删除");
        }
        CrmCustomerPool.dao.deleteById(poolId);
        Db.delete("delete from 72crm_crm_customer_pool_rule where pool_id = ?", poolId);
        Db.delete("delete from 72crm_crm_customer_pool_field_setting where pool_id = ?", poolId);
        return R.ok();
    }


    /**
     * 定时将客户放入公海
     */
    public void putInInternational(List<CrmCustomerPool> poolList) {
        poolList.forEach(pool -> {
            List<Long> userIdsList = new ArrayList<>();
            if (StrUtil.isNotEmpty(pool.getMemberDeptId())) {
                userIdsList = Db.template("admin.user.queryUserIdByDeptId", Kv.by("deptIds", pool.getMemberDeptId())).query();
            }
            if (StrUtil.isNotEmpty(pool.getMemberUserId())) {
                userIdsList.addAll(Arrays.stream(pool.getMemberUserId().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            Set<Integer> customerIdSet = new HashSet<>();
            List<CrmCustomerPoolRule> ruleList = CrmCustomerPoolRule.dao.find("select * from 72crm_crm_customer_pool_rule where pool_id = ?", pool.getPoolId());
            for (CrmCustomerPoolRule rule : ruleList) {
                Record record = rule.toRecord();
                if (CollectionUtil.isNotEmpty(userIdsList)) {
                    record.set("ids", userIdsList);
                }
                if (rule.getType().equals(1)) {
                    customerIdSet.addAll(Db.template("crm.customerPool.putInPoolByRecord", record.getColumns()).query());
                } else if (rule.getType().equals(2)) {
                    customerIdSet.addAll(Db.template("crm.customerPool.putInPoolByBusiness", record.getColumns()).query());
                } else if (rule.getType().equals(3)) {
                    customerIdSet.addAll(Db.template("crm.customerPool.putInPoolByDealStatus", record.getColumns()).query());
                }
            }
            List<Integer> customerIdList = Db.query("select customer_id from 72crm_crm_customer_pool_relation where pool_id = ?", pool.getPoolId());
            customerIdSet.removeAll(customerIdList);
            customerIdSet.forEach(customerId -> {
                CrmCustomer crmCustomer = CrmCustomer.dao.findById(customerId);
                if (ObjectUtil.isNotEmpty(crmCustomer.getOwnerUserId())) {
                    CrmOwnerRecord crmOwnerRecord = new CrmOwnerRecord();
                    crmOwnerRecord.setTypeId(customerId);
                    crmOwnerRecord.setType(CrmEnum.CRM_CUSTOMER_POOL.getType());
                    crmOwnerRecord.setPreOwnerUserId(crmCustomer.getOwnerUserId());
                    crmOwnerRecord.setCreateTime(DateUtil.date());
                    crmOwnerRecord.save();
                    Db.update("update 72crm_crm_customer set pre_owner_user_id = ? where customer_id = ?", crmOwnerRecord.getPreOwnerUserId(), customerId);
                }
                CrmCustomerPoolRelation customerPoolRelation = new CrmCustomerPoolRelation();
                customerPoolRelation.setCustomerId(customerId);
                customerPoolRelation.setPoolId(pool.getPoolId());
                customerPoolRelation.save();
            });
            Db.template("crm.customer.updateContacts", Kv.by("ids",customerIdSet)).update();
            Db.update(Db.getSqlPara("crm.customerPool.putInInternational", Kv.by("ids", customerIdSet)));
        });
    }

    @Before(Tx.class)
    public R deleteByIds(Integer poolId, String customerIds) {
        if (AuthUtil.isPoolAdmin(poolId)) {
            return R.error("无权删除");
        }
        String[] idsList = StrUtil.split(customerIds, ",");
        Integer contactsNum = Db.queryInt(Db.getSql("crm.customer.queryContactsNumber"), customerIds);
        Integer businessNum = Db.queryInt(Db.getSql("crm.customer.queryBusinessNumber"), customerIds);
        if (contactsNum > 0 || businessNum > 0) {
            return R.error("该条数据与其他数据有必要关联，请勿删除");
        }
        Kv kv = Kv.by("ids", idsList).set("status", 3);
        Db.template("crm.customer.updateCustomer", kv).update();
        List<String> batchList = Db.template("crm.customer.queryCustomerBatchId", kv).query();
        //从公海删除客户
        Db.template("crm.customerPool.deletePoolRelation", Kv.by("ids", idsList)).delete();
        //删除文件
        Aop.get(AdminFileService.class).removeByBatchId(batchList.toArray(new String[0]));
        //删除跟进记录
        Aop.get(CrmActivityService.class).deleteActivityRecord(CrmActivityEnum.CUSTOMER, idsList);
        //删除字段操作记录
        Aop.get(CrmRecordService.class).deleteActionRecord(CrmEnum.CRM_CUSTOMER, idsList);
        //删除自定义字段
        Db.template("admin.field.deleteFieldv", Kv.by("batchList", batchList).set("dataTableName", "72crm_crm_customer_data")).delete();
        return R.ok();
    }
}
