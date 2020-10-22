package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
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
import com.kakarote.crm9.common.config.redis.Redis;
import com.kakarote.crm9.common.config.redis.RedisManager;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.utils.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class CrmCustomerService extends AbstractBasePageList {
    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmRecordService crmRecordService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    @Inject
    private CrmBusinessService crmBusinessService;

    @Inject
    private CrmActivityService crmActivityService;


    /**
     * @author wyq
     * 分页条件查询客户
     */
    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        CrmEnum crmEnum = CrmEnum.CRM_CUSTOMER;
        Kv kv = getKv(basePageRequest, crmEnum, isDataAuth);
        if (kv.containsKey("error")) {
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.customer.queryCustomerPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.customer.queryCustomerPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        if (recordPage.size() > 0) {
            List<Integer> customerIdList = recordPage.stream().map(record -> record.getInt("customer_id")).collect(Collectors.toList());
            List<CrmCustomerPool> poolList = CrmCustomerPool.dao.find("select * from 72crm_crm_customer_pool where status = 1 and put_in_rule = 1");
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
                    Record record = rule.toRecord().set("customerIds", customerIdList);
                    if (CollectionUtil.isNotEmpty(userIdsList)) {
                        record.set("ids", userIdsList);
                    }
                    List<Record> recordList = new ArrayList<>();
                    if (rule.getType().equals(1)) {
                        recordList = Db.template("crm.customer.putInPoolByRecord", record.getColumns()).find();
                    } else if (rule.getType().equals(2)) {
                        recordList = Db.template("crm.customer.putInPoolByBusiness", record.getColumns()).find();
                    } else if (rule.getType().equals(3)) {
                        recordList = Db.template("crm.customer.putInPoolByDealStatus", record.getColumns()).find();
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
            recordPage.forEach(record -> record.set("poolDay", customerMap.get(record.getInt("customer_id"))));
        }
        resultJsonObject.put("list", recordPage);
        Integer count = Db.template("crm.customer.queryCustomerPageListCount", kv).queryInt();
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
    public String querySelectSql() {
        return Db.getSql("crm.customer.getCustomerPageList") +
                "(select count(*) from 72crm_crm_customer_user_star where user_id = " + BaseUtil.getUserId() + " and customer_id = views.customer_id) as star";
    }

    @Override
    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject jsonObject = super.appendOtherSql(isDataAuth, kv, crmEnum);
        String conditions = " and owner_user_id is not null and status != 3 ";
        jsonObject.put("sql", jsonObject.getString("sql") + conditions);
        return jsonObject;
    }


    /**
     * @author wyq
     * 新增或更新客户
     */
    @Before(Tx.class)
    public R addOrUpdate(JSONObject jsonObject, String type) {
        CrmCustomer crmCustomer = jsonObject.getObject("entity", CrmCustomer.class);
        boolean b = !BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()) && Db.queryInt(Db.getSql("crm.customer.queryIsRoUser"), BaseUtil.getUserId(), crmCustomer.getCustomerId()) > 0;
        if (crmCustomer.getCustomerId() != null && b) {
            return R.error("没有权限");
        }
        String batchId = StrUtil.isNotEmpty(crmCustomer.getBatchId()) ? crmCustomer.getBatchId() : IdUtil.simpleUUID();
        if (crmCustomer.getCustomerId() != null) {
            actionRecordUtil.updateRecord(jsonObject.getJSONArray("field"), Kv.by("batchId", batchId).set("dataTableName", "72crm_crm_customer_data"));
        }
        if (!save(jsonObject.getJSONArray("field"), batchId)) {
            return R.error();
        }
        if (StrUtil.isEmpty(crmCustomer.getEmail())) {
            crmCustomer.setEmail(null);
        }
        if (crmCustomer.getCustomerId() != null) {
            CrmCustomer oldCrmCustomer = new CrmCustomer().dao().findById(crmCustomer.getCustomerId());
            actionRecordUtil.updateRecord(oldCrmCustomer, crmCustomer, CrmEnum.CRM_CUSTOMER);
            crmCustomer.setUpdateTime(DateUtil.date());
            return crmCustomer.update() ? R.ok() : R.error();
        } else {
            crmCustomer.setCreateTime(DateUtil.date());
            crmCustomer.setUpdateTime(DateUtil.date());
            crmCustomer.setReceiveTime(DateUtil.date());
            crmCustomer.setCreateUserId(BaseUtil.getUser().getUserId());
            if ("noImport".equals(type)) {
                crmCustomer.setOwnerUserId(BaseUtil.getUser().getUserId());
            }
            crmCustomer.setBatchId(batchId);
            crmCustomer.setRwUserId(",");
            crmCustomer.setRoUserId(",");
            if (!queryCustomerSettingNum(1, crmCustomer.getOwnerUserId())) {
                return R.error("拥有客户已达上限，无法新增");
            }
            boolean save = crmCustomer.save();
            crmActivityService.addActivity(2, CrmActivityEnum.CUSTOMER, crmCustomer.getCustomerId(), "");
            actionRecordUtil.addRecord(crmCustomer.getCustomerId(), CrmEnum.CRM_CUSTOMER);
            return save ? R.ok().put("data", Kv.by("customer_id", crmCustomer.getCustomerId()).set("customer_name", crmCustomer.getCustomerName())) : R.error();
        }
    }

    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_customer_data", "batch_id", batchId);
        List<CrmCustomerData> fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            String formType = JSONObject.parseObject(JSONObject.toJSONString(obj)).getString("formType");
            CrmCustomerData fieldv = TypeUtils.castToJavaBean(obj, CrmCustomerData.class);
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            //多选类型选项按照固定顺序保存，以便高级筛选进行精确匹配
            if ("checkbox".equals(formType)) {
                List<String> optionList = Arrays.stream(fieldv.getValue().split(",")).collect(Collectors.toList());
                CollectionUtil.sortByPinyin(optionList);
                fieldv.setValue(CollectionUtil.join(optionList, ","));
            }
            fieldvs.add(fieldv);
        });
        Db.batchSave(fieldvs, 100);
        return true;
    }

    /**
     * @author wyq
     * 根据客户id查询
     */
    public Record queryById(Integer customerId, Integer poolId) {
        Record record = Db.findFirst(Db.getSql("crm.customer.queryById"), BaseUtil.getUserId(), customerId);
        List<Record> recordList = Db.find("select name,value from `72crm_crm_customer_data` where batch_id = ?", record.getStr("batch_id"));
        if (ObjectUtil.isNotEmpty(poolId)) {
            List<String> nameList = Db.query("select name from 72crm_crm_customer_pool_field_setting where pool_id = ? and is_hidden = 1", poolId);
            recordList.removeIf(r -> nameList.contains(r.getStr("name")));
        }
        recordList.forEach(field -> record.set(field.getStr("name"), field.getStr("value")));
        return record;
    }

    /**
     * @author wyq
     * 基本信息
     */
    public List<Record> information(Integer customerId, Integer poolId) {
        Record record = queryById(customerId, null);
        List<String> keyList = Arrays.asList("customer_name", "next_time", "website", "remark", "telephone", "mobile", "email", "last_time", "update_time", "create_time");
        List<Record> recordList = adminFieldService.queryInformation(CrmEnum.CRM_CUSTOMER, record, keyList);
        List<String> systemFieldList = Arrays.asList("最后跟进时间", "创建人", "创建时间", "更新时间", "下次联系时间", "最后跟进记录", "成交状态", "负责人获取客户时间");
        recordList.add(new Record().set("name", "成交状态").set("value", record.getInt("deal_status") == 0 ? "未成交" : "已成交").set("formType", "deal_status").set("field_type", 1));
        recordList.add(new Record().set("name", "地区定位").set("value", Kv.by("location", record.getStr("location"))
                .set("address", record.getStr("address"))
                .set("detailAddress", record.getStr("detail_address"))
                .set("lng", record.getStr("lng"))
                .set("lat", record.getStr("lat"))).set("formType", "map_address").set("field_type", 1));
        List<Record> list = recordList.stream().sorted(Comparator.comparingInt(r -> -r.getInt("field_type"))).peek(r -> {
            r.remove("field_type", "setting", "type");
            if (systemFieldList.contains(r.getStr("name"))) {
                r.set("sysInformation", 1);
            } else {
                r.set("sysInformation", 0);
            }
        }).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(poolId)) {
            List<String> nameList = Db.query("select name from 72crm_crm_customer_pool_field_setting where pool_id = ? and is_hidden = 1", poolId);
            list.removeIf(r -> nameList.contains(r.getStr("name")) || "负责人".equals(r.getStr("name")) || "负责人获取客户时间".equals(r.getStr("name")));
        } else {
            list.removeIf(r -> "前负责人".equals(r.getStr("name")));
        }
        return list;
    }

    /**
     * @author wyq
     * 根据客户id查找商机
     */
    public R queryBusiness(BasePageRequest<CrmCustomer> basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer customerId = jsonObject.getInteger("customerId");
        String search = jsonObject.getString("search");
        Integer pageType = basePageRequest.getPageType();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_BUSINESS, "a", 1);
        if (0 == pageType) {
            List<Record> recordList = Db.find(Db.getSqlPara("crm.customer.queryBusiness", Kv.by("customerId", customerId).set("businessName", search).set("userId", BaseUtil.getUserId()).set("conditions", conditions)));
            crmBusinessService.setBusinessStatus(recordList);
            return R.ok().put("data", recordList);
        } else {
            Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.customer.queryBusiness", Kv.by("customerId", customerId).set("businessName", search).set("userId", BaseUtil.getUserId()).set("conditions", conditions)));
            crmBusinessService.setBusinessStatus(paginate.getList());
            return R.ok().put("data", paginate);
        }
    }


    /**
     * @author wyq
     * 根据客户id查询联系人
     */
    public R queryContacts(BasePageRequest<CrmCustomer> basePageRequest) {
        Integer customerId = basePageRequest.getData().getCustomerId();
        Integer pageType = basePageRequest.getPageType();
        String search = basePageRequest.getJsonObject().getString("search");
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_CONTACTS, 1);
        if (0 == pageType) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.customer.queryContacts", Kv.by("customerId", customerId).set("contactsName", search).set("conditions", conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.customer.queryContacts", Kv.by("customerId", customerId).set("contactsName", search).set("conditions", conditions))));
        }
    }

    /**
     * @auyhor wyq
     * 根据客户id查询合同
     */
    public R queryContract(BasePageRequest<CrmCustomer> basePageRequest) {
        Integer customerId = basePageRequest.getData().getCustomerId();
        Integer pageType = basePageRequest.getPageType();
        String search = basePageRequest.getJsonObject().getString("search");
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_CONTRACT, "a", 1);
        if (basePageRequest.getData().getCheckstatus() != null) {
            SqlPara sqlPara = Db.getSqlPara("crm.customer.queryPassContract", Kv.by("customerId", customerId).set("checkStatus", basePageRequest.getData().getCheckstatus()).set("contractName", search).set("conditions", conditions));
            if (0 == pageType) {
                return R.ok().put("data", Db.find(sqlPara));
            } else {
                return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), sqlPara));
            }
        }
        if (0 == pageType) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.customer.queryContract", Kv.by("customerId", customerId).set("contractName", search).set("userId", BaseUtil.getUserId()).set("conditions", conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.customer.queryContract", Kv.by("customerId", customerId).set("contractName", search).set("conditions", conditions))));
        }
    }

    /**
     * @author wyq
     * 根据客户id查询回款计划
     */
    public R queryReceivablesPlan(BasePageRequest<CrmCustomer> basePageRequest) {
        Integer customerId = basePageRequest.getData().getCustomerId();
        Integer pageType = basePageRequest.getPageType();
        if (0 == pageType) {
            return R.ok().put("data", Db.find(Db.getSql("crm.customer.queryReceivablesPlan"), customerId));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), new SqlPara().setSql(Db.getSql("crm.customer.queryReceivablesPlan")).addPara(customerId)));
        }
    }

    /**
     * @author wyq
     * 根据客户id查询回款
     */
    public R queryReceivables(BasePageRequest<CrmCustomer> basePageRequest) {
        Integer customerId = basePageRequest.getData().getCustomerId();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_RECEIVABLES, "a", 1);
        if (0 == basePageRequest.getPageType()) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.customer.queryReceivables", Kv.by("customerId", customerId).set("conditions", conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.customer.queryReceivables", Kv.by("customerId", customerId).set("conditions", conditions))));
        }
    }

    /**
     * 根据客户id查询回访
     */
    public R queryReturnVisit(BasePageRequest<CrmCustomer> basePageRequest) {
        Integer customerId = basePageRequest.getData().getCustomerId();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_RETURN_VISIT, "a", 1);
        if (0 == basePageRequest.getPageType()) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.customer.queryReturnVisit", Kv.by("customerId", customerId).set("conditions", conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.customer.queryReturnVisit", Kv.by("customerId", customerId).set("conditions", conditions))));
        }
    }

    /**
     * 根据客户id查询发票
     */
    public R queryInvoice(BasePageRequest<CrmCustomer> basePageRequest) {
        Integer customerId = basePageRequest.getData().getCustomerId();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_INVOICE, "a", 1);
        if (0 == basePageRequest.getPageType()) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.customer.queryInvoice", Kv.by("customerId", customerId).set("conditions", conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.customer.queryInvoice", Kv.by("customerId", customerId).set("conditions", conditions))));
        }
    }

    /**
     * 根据客户id查询发票抬头信息
     */
    public R queryInvoiceInfo(BasePageRequest<CrmCustomer> basePageRequest) {
        Integer customerId = basePageRequest.getData().getCustomerId();
        if (0 == basePageRequest.getPageType()) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.customer.queryInvoiceInfo", Kv.by("customerId", customerId))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.customer.queryInvoiceInfo", Kv.by("customerId", customerId))));
        }
    }

    public R queryCallRecord(BasePageRequest<CrmCustomer> basePageRequest) {
        Integer customerId = basePageRequest.getData().getCustomerId();
        if (0 == basePageRequest.getPageType()) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.customer.queryCallRecord", Kv.by("customerId", customerId))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.customer.queryCallRecord", Kv.by("customerId", customerId))));
        }
    }

    /**
     * @author wyq
     * 根据id删除客户
     */
    @Before(Tx.class)
    public R deleteByIds(String customerIds) {
        String[] idsList = StrUtil.split(customerIds, ",");
        for (String id : idsList) {
            if (!BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()) && Db.queryInt(Db.getSql("crm.customer.queryIsRoUser"), BaseUtil.getUserId(), id) > 0) {
                return R.error("没有权限");
            }
        }
        Integer contactsNum = Db.queryInt(Db.getSql("crm.customer.queryContactsNumber"), customerIds);
        Integer businessNum = Db.queryInt(Db.getSql("crm.customer.queryBusinessNumber"), customerIds);
        if (contactsNum > 0 || businessNum > 0) {
            return R.error("该条数据与其他数据有必要关联，请勿删除");
        }
        Kv kv = Kv.by("ids", idsList).set("status", 3);
        Db.template("crm.customer.updateCustomer", kv).update();
        List<String> batchList = Db.template("crm.customer.queryCustomerBatchId", kv).query();
        //删除文件
        Aop.get(AdminFileService.class).removeByBatchId(batchList.toArray(new String[0]));
        //删除跟进记录
        Aop.get(CrmActivityService.class).deleteActivityRecord(CrmActivityEnum.CUSTOMER, idsList);
        //删除字段操作记录
        Aop.get(CrmRecordService.class).deleteActionRecord(CrmEnum.CRM_CUSTOMER, idsList);
        actionRecordUtil.addDeleteActionRecord(CrmEnum.CRM_CUSTOMER, idsList);
        //删除自定义字段
        Db.template("admin.field.deleteFieldv", Kv.by("batchList", batchList).set("dataTableName", "72crm_crm_customer_data")).delete();
        return R.ok();
    }

    /**
     * @author wyq
     * 客户锁定
     */
    @Before(Tx.class)
    public R lock(CrmCustomer crmCustomer) {
        Integer status = crmCustomer.getStatus();
        if (status != 1 && status != 2) {
            return R.error("参数错误");
        }
        String[] ids = crmCustomer.getIds().split(",");
        for (String id : ids) {
            if (!BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()) && Db.queryInt(Db.getSql("crm.customer.queryIsRoUser"), BaseUtil.getUserId(), id) > 0) {
                return R.error("没有权限");
            }
        }
        SqlPara sqlPara = Db.getSqlPara("crm.customer.queryDealNum", Kv.by("ids", ids));
        Integer number = Db.queryInt(sqlPara.getSql(), sqlPara.getPara());
        if (number > 0) {
            return R.error("已成交客户无需锁定");
        }
        if (!isMaxLock(status, ids)) {
            return R.error("有员工可锁定客户数达到上限");
        }
        actionRecordUtil.addIsLockRecord(ids, CrmEnum.CRM_CUSTOMER, status);
        Db.template("crm.customer.updateCustomer", Kv.by("ids", ids).set("status", status)).update();
        return R.ok();
    }

    public boolean isMaxLock(Integer status, String[] ids) {
        if (Objects.equals(status, 2)) {
            SqlPara sqlPara = Db.getSqlPara("crm.customer.lockNum", Kv.by("ids", ids));
            List<Record> recordList = Db.find(sqlPara);
            for (Record record : recordList) {
                return this.queryCustomerSettingNum(2, record.getLong("userId"), record.getInt("num"));
            }
        }
        return true;
    }

    /**
     * @author wyq
     * 设置成交状态
     */
    public R setDealStatus(String ids, Integer dealStatus) {
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            if (!BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()) && Db.queryInt(Db.getSql("crm.customer.queryIsRoUser"), BaseUtil.getUserId(), id) > 0) {
                return R.error("没有权限");
            }
            SqlPara sqlPara = Db.getSqlPara("crm.customer.queryCustomerSettingByUserId", Kv.by("userId", id).set("type", 1));
            List<Record> crmCustomerSettings = Db.find(sqlPara);
            boolean b = crmCustomerSettings.stream().anyMatch(record -> record.getInt("customer_deal") == 1);
            if (b) {
                CrmCustomer customer = CrmCustomer.dao.findById(id);
                if (0 == dealStatus && 1 == customer.getDealStatus()) {
                    if (!queryCustomerSettingNum(1, customer.getOwnerUserId())) {
                        return R.error("所选客户中有负责人拥有未成交客户数已达上限");
                    }
                }
            }
        }
        Db.template("crm.customer.updateCustomer", Kv.by("ids", idsArr).set("dealTime", new Date()).set("dealStatus", dealStatus)).update();
        return R.ok();
    }

    /**
     * @author wyq
     * 变更负责人
     */
    public R updateOwnerUserId(CrmCustomer crmCustomer) {
        Integer customerId = crmCustomer.getCustomerId();
        if (AuthUtil.isRwAuth(customerId, CrmEnum.CRM_CUSTOMER)) {
            return R.error("没有权限");
        }
        String memberId = "," + crmCustomer.getNewOwnerUserId() + ",";
        Db.update(Db.getSql("crm.customer.deleteMember"), memberId, memberId, customerId);
        CrmCustomer oldCustomer = CrmCustomer.dao.findById(customerId);
        crmCustomer.setCustomerId(customerId);
        crmCustomer.setOwnerUserId(crmCustomer.getNewOwnerUserId());
        crmCustomer.setFollowup(0);
        crmCustomer.setIsReceive(1);
        crmCustomer.setReceiveTime(DateUtil.date());
        if (2 == crmCustomer.getTransferType() && !crmCustomer.getOwnerUserId().equals(oldCustomer.getOwnerUserId())) {
            if (1 == crmCustomer.getPower()) {
                crmCustomer.setRoUserId(oldCustomer.getRoUserId() + oldCustomer.getOwnerUserId() + ",");
            }
            if (2 == crmCustomer.getPower()) {
                crmCustomer.setRwUserId(oldCustomer.getRwUserId() + oldCustomer.getOwnerUserId() + ",");
            }
            Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CUSTOMER_USER, crmCustomer, oldCustomer.getOwnerUserId());
        }
        crmCustomer.update();
        return R.ok();
    }


    /**
     * @author wyq
     * 查询团队成员
     */
    public List<Record> getMembers(Integer customerId) {
        CrmCustomer crmCustomer = CrmCustomer.dao.findById(customerId);
        if (null == crmCustomer) {
            return null;
        }
        List<Record> recordList = new ArrayList<>();
        if (crmCustomer.getOwnerUserId() != null) {
            Record ownerUser = Db.findFirst(Db.getSql("crm.customer.getMembers"), crmCustomer.getOwnerUserId());
            if (ownerUser != null) {
                recordList.add(ownerUser.set("power", "负责人权限").set("groupRole", "负责人"));
            }
        }
        String roUserId = crmCustomer.getRoUserId();
        String rwUserId = crmCustomer.getRwUserId();
        String memberIds = roUserId + rwUserId.substring(1);
        if (",".equals(memberIds)) {
            return recordList;
        }
        String[] memberIdsArr = memberIds.substring(1, memberIds.length() - 1).split(",");
        Set<String> memberIdsSet = new HashSet<>(Arrays.asList(memberIdsArr));
        for (String memberId : memberIdsSet) {
            Record record = Db.findFirst(Db.getSql("crm.customer.getMembers"), memberId);
            if (roUserId.contains(memberId)) {
                record.set("power", "只读").set("groupRole", "普通成员");
            }
            if (rwUserId.contains(memberId)) {
                record.set("power", "读写").set("groupRole", "普通成员");
            }
            recordList.add(record);
        }
        return recordList;
    }

    /**
     * @author wyq
     * 添加团队成员
     */
    public R addMember(CrmCustomer crmCustomer) {
        String[] customerIdsArr = crmCustomer.getIds().split(",");
        String[] memberArr = crmCustomer.getMemberIds().split(",");
        StringBuilder stringBuffer = new StringBuilder();
        for (String id : customerIdsArr) {
            if (AuthUtil.isCrmOperateAuth(CrmEnum.CRM_CUSTOMER, Integer.valueOf(id))) {
                return R.error("没有权限");
            }
            CrmCustomer oldCustomer = CrmCustomer.dao.findById(Integer.valueOf(id));
            Long ownerUserId = oldCustomer.getOwnerUserId();
            for (String memberId : memberArr) {
                if (ownerUserId.equals(Long.valueOf(memberId))) {
                    return R.error("负责人不能重复选为团队成员!");
                }
                Db.update(Db.getSql("crm.customer.deleteMember"), "," + memberId + ",", "," + memberId + ",", Integer.valueOf(id));
            }
            if (1 == crmCustomer.getPower()) {
                stringBuffer.setLength(0);
                String roUserId = stringBuffer.append(CrmCustomer.dao.findById(Integer.valueOf(id)).getRoUserId()).append(crmCustomer.getMemberIds()).append(",").toString();
                Db.update("update 72crm_crm_customer set ro_user_id = ? where customer_id = ?", roUserId, Integer.valueOf(id));
            }
            if (2 == crmCustomer.getPower()) {
                stringBuffer.setLength(0);
                String rwUserId = stringBuffer.append(CrmCustomer.dao.findById(Integer.valueOf(id)).getRwUserId()).append(crmCustomer.getMemberIds()).append(",").toString();
                Db.update("update 72crm_crm_customer set rw_user_id = ? where customer_id = ?", rwUserId, Integer.valueOf(id));
            }
            List<Long> roUserIdList = StrUtil.splitTrim(oldCustomer.getRoUserId(), ",").stream().map(Long::parseLong).collect(Collectors.toList());
            List<Long> rwUserIdList = StrUtil.splitTrim(oldCustomer.getRwUserId(), ",").stream().map(Long::parseLong).collect(Collectors.toList());
            roUserIdList.addAll(rwUserIdList);
            List<Long> memberList = Arrays.stream(memberArr).map(Long::parseLong).collect(Collectors.toList());
            memberList.removeAll(roUserIdList);
            memberList.forEach(memberId -> {
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CUSTOMER_USER, oldCustomer, memberId);
                actionRecordUtil.addMemberActionRecord(CrmEnum.CRM_CUSTOMER, Integer.valueOf(id), memberId);
            });
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 删除团队成员
     */
    @Before(Tx.class)
    public R deleteMembers(CrmCustomer crmCustomer) {
        String[] customerIdsArr = crmCustomer.getIds().split(",");
        String[] memberArr = crmCustomer.getMemberIds().split(",");
        for (String id : customerIdsArr) {
            if (AuthUtil.isCrmOperateAuth(CrmEnum.CRM_CUSTOMER, Integer.valueOf(id))) {
                return R.error("没有权限");
            }
            CrmCustomer customer = CrmCustomer.dao.findById(Integer.valueOf(id));
            for (String memberId : memberArr) {
                Db.update(Db.getSql("crm.customer.deleteMember"), "," + memberId + ",", "," + memberId + ",", Integer.valueOf(id));
                if (!Long.valueOf(memberId).equals(BaseUtil.getUserId())) {
                    Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CUSTOMER_REMOVE_TEAM, customer, Long.parseLong(memberId));
                    actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.CRM_CUSTOMER, Integer.valueOf(id), Long.parseLong(memberId), false);
                }else {
                    Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CUSTOMER_TEAM_EXIT, customer, customer.getOwnerUserId());
                    actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.CRM_CUSTOMER, Integer.valueOf(id), Long.parseLong(memberId), true);
                }
            }
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 自助退出团队成员
     */
    @Before(Tx.class)
    public R exitTeam(Integer customerId) {
        Long userId = BaseUtil.getUserId();
        CrmCustomer customer = CrmCustomer.dao.findById(customerId);
        if (customer.getOwnerUserId().equals(userId)) {
            return R.error("负责人不能退出团队！");
        }
        Db.update(Db.getSql("crm.customer.deleteMember"), "," + userId + ",", "," + userId + ",", customerId);
        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_CUSTOMER_TEAM_EXIT, customer, customer.getOwnerUserId());
        return R.ok();
    }


    /**
     * @author wyq
     * 根据客户ids获取合同ids
     */
    public String getContractIdsByCustomerIds(String customerIds) {
        String[] customerIdsArr = customerIds.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for (String id : customerIdsArr) {
            List<Record> recordList = Db.find("select contract_id from 72crm_crm_contract where customer_id = ?", id);
            if (recordList != null) {
                for (Record record : recordList) {
                    stringBuffer.append(",").append(record.getStr("contract_id"));
                }
            }
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(0);
        }
        return stringBuffer.toString();
    }

    /**
     * @author wyq
     * 根据客户ids获取商机ids
     */
    public String getBusinessIdsByCustomerIds(String customerIds) {
        String[] customerIdsArr = customerIds.split(",");
        StringBuilder stringBuffer = new StringBuilder();
        for (String id : customerIdsArr) {
            List<Record> recordList = Db.find("select business_id from 72crm_crm_business where customer_id = ?", id);
            if (recordList != null) {
                for (Record record : recordList) {
                    stringBuffer.append(",").append(record.getStr("business_id"));
                }
            }
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(0);
        }
        return stringBuffer.toString();
    }

    /**
     * @author wyq
     * 查询编辑字段
     */
    public List<Record> queryField(Integer customerId) {
        Record customer = queryById(customerId, null);
        List<Record> fieldList = adminFieldService.queryUpdateField(CrmEnum.CRM_CUSTOMER.getType(), customer);
        fieldList.add(new Record().set("fieldName", "map_address")
                .set("name", "地区定位")
                .set("value", Kv.by("location", customer.getStr("location"))
                        .set("address", customer.getStr("address"))
                        .set("detailAddress", customer.getStr("detail_address"))
                        .set("lng", customer.getStr("lng"))
                        .set("lat", customer.getStr("lat")))
                .set("formType", "map_address")
                .set("isNull", 0));
        return fieldList;
    }

    /**
     * @author zxy
     * 客户保护规则设置
     */
    @Before(Tx.class)
    public R updateRulesSetting(Integer dealDay, Integer followupDay, Integer type, Integer remindDay, Integer remindConfig) {
        Db.update("update 72crm_admin_config set value = ? where name = 'customerPoolSettingDealDays'", dealDay);
        Db.update("update 72crm_admin_config set value = ? where name = 'customerPoolSettingFollowupDays'", followupDay);
        Db.update("update 72crm_admin_config set status = ? where name = 'customerPoolSetting'", type);
        if (remindDay > 0) {
            Db.update("update 72crm_admin_config set status = ?,value = ? where name = 'putInPoolRemindDays'", remindConfig, remindDay);
        } else {
            Db.update("update 72crm_admin_config set status = 0 where name = 'putInPoolRemindDays'");
        }
        return R.ok();
    }

    /**
     * @author zxy
     * 获取客户保护规则设置
     */
    @Before(Tx.class)
    public R getRulesSetting() {
        String dealDay = Db.queryStr("select value from 72crm_admin_config where name = 'customerPoolSettingDealDays'");
        String followupDay = Db.queryStr("select value from 72crm_admin_config where name = 'customerPoolSettingFollowupDays'");
        Integer type = Db.queryInt("select status from 72crm_admin_config where name = 'customerPoolSetting'");
        AdminConfig remindConfig = AdminConfig.dao.findFirst("select * from 72crm_admin_config where name = 'putInPoolRemindDays'");

        if (dealDay == null) {
            dealDay = "3";
            AdminConfig adminConfig = new AdminConfig();
            adminConfig.setName("customerPoolSettingDealDays");
            adminConfig.setValue(dealDay);
            adminConfig.save();
        }
        if (followupDay == null) {
            followupDay = "7";
            AdminConfig adminConfig = new AdminConfig();
            adminConfig.setName("customerPoolSettingFollowupDays");
            adminConfig.setValue(followupDay);
            adminConfig.save();
        }
        if (type == null) {
            type = 0;
            AdminConfig adminConfig = new AdminConfig();
            adminConfig.setName("customerPoolSetting");
            adminConfig.setStatus(type);
            adminConfig.save();
        }
        if (remindConfig == null) {
            remindConfig = new AdminConfig();
            remindConfig.setStatus(0);
            remindConfig.setValue("3");
            remindConfig.setName("putInPoolRemindDays");
            remindConfig.save();
        }
        AdminConfig config = AdminConfig.dao.findFirst("select status,value from 72crm_admin_config where name = 'expiringContractDays' limit 1");
        if (config == null) {
            config = new AdminConfig();
            config.setStatus(0);
            config.setName("expiringContractDays");
            config.setValue("3");
            config.setDescription("合同到期提醒");
            config.save();
        }
        return R.ok().put("data", Kv.by("dealDay", dealDay)
                .set("followupDay", followupDay)
                .set("customerConfig", type)
                .set("contractConfig", config.getStatus())
                .set("contractDay", config.getValue())
                .set("putInPoolRemindConfig", remindConfig.getStatus())
                .set("putInPoolRemindDays", remindConfig.getValue()));
    }

    /**
     * 客户放入公海
     *
     * @author zxy
     */
    @Before(Tx.class)
    public R updateCustomerByIds(String ids, Integer poolId) {
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            if (!BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()) && Db.queryInt(Db.getSql("crm.customer.queryIsRoUser"), BaseUtil.getUserId(), id) > 0) {
                return R.error("无权限放入");
            }
            CrmCustomer crmCustomer = CrmCustomer.dao.findById(Integer.valueOf(id));
            CrmOwnerRecord crmOwnerRecord = new CrmOwnerRecord();
            crmOwnerRecord.setTypeId(Integer.valueOf(id));
            crmOwnerRecord.setType(CrmEnum.CRM_CUSTOMER_POOL.getType());
            crmOwnerRecord.setPreOwnerUserId(crmCustomer.getOwnerUserId());
            crmOwnerRecord.setCreateTime(DateUtil.date());
            crmOwnerRecord.save();
            Db.update("update 72crm_crm_customer set pre_owner_user_id = ? where customer_id = ?", crmCustomer.getOwnerUserId(), Integer.valueOf(id));
            CrmCustomerPoolRelation relation = new CrmCustomerPoolRelation();
            relation.setCustomerId(Integer.valueOf(id));
            relation.setPoolId(poolId);
            relation.save();
        }
        Kv kv = Kv.by("putInPool", 1).set("ids", ids);
        Db.template("crm.customer.updateCustomer", kv).update();
        Db.template("crm.customer.updateContacts", kv).update();
        actionRecordUtil.addPutIntoTheOpenSeaRecord(TagUtil.toSet(ids), CrmEnum.CRM_CUSTOMER);
        return R.ok();
    }

    /**
     * 领取或分配客户
     *
     * @author zxy
     */
    public R getCustomersByIds(Integer poolId, String ids, Long userId, Integer isReceive) {
        if (userId == null) {
            userId = BaseUtil.getUser().getUserId();
        }
        if (AuthUtil.isPoolAdmin(poolId) && isReceive == 1) {
            return R.error("无权分配");
        }
        String[] idsArr = ids.split(",");
        if (!isMaxOwner(userId, idsArr)) {
            return R.error("该员工拥有客户数已达上限");
        }
        //公海领取频率限制
        CrmCustomerPool pool = CrmCustomerPool.dao.findById(poolId);
        if (isReceive == 2) {
            if (pool.getReceiveSetting() != null && pool.getReceiveSetting() == 1) {
                Integer idsLength = idsArr.length;
                if (idsLength > pool.getReceiveNum()) {
                    return R.error("领取客户数超过限制");
                }
                Redis redis = RedisManager.getRedis();
                Integer num = redis.get("receiveNum:poolId_" + poolId + ":userId_" + userId);
                if (ObjectUtil.isNotEmpty(num) && (num + idsLength > pool.getReceiveNum())) {
                    return R.error("今日领取次数超过上限");
                }
                long expireTime = (DateUtil.endOfDay(DateUtil.date()).getTime() - System.currentTimeMillis()) / 1000;
                redis.setex("receiveNum:poolId_" + poolId + ":userId_" + userId, (int) expireTime, ObjectUtil.isEmpty(num) ? idsLength : num + idsLength);
            }
        }
        AtomicLong atomicUserId = new AtomicLong(userId);
        List<String> errorList = new ArrayList<>();
        Db.tx(() -> {
            actionRecordUtil.addDistributionRecord(ids, CrmEnum.CRM_CUSTOMER, isReceive.equals(1) ? atomicUserId.longValue() : null);
            for (String id : idsArr) {
                //前负责人领取限制，从前负责人脱手开始计算天数
                if (isReceive == 2) {
                    if (pool.getPreOwnerSetting() == 1) {
                        Integer days = Db.template("crm.customer.queryOutDays", Kv.by("userId", atomicUserId.longValue()).set("customerId", Integer.parseInt(id))).queryInt();
                        if (days != null && days <= pool.getPreOwnerSettingDay()) {
                            errorList.add("前负责人在限制时间内不能领取");
                            return false;
                        }
                    }
                }
                CrmOwnerRecord crmOwnerRecord = new CrmOwnerRecord();
                crmOwnerRecord.setTypeId(Integer.valueOf(id));
                crmOwnerRecord.setType(CrmEnum.CRM_CUSTOMER_POOL.getType());
                crmOwnerRecord.setPostOwnerUserId(atomicUserId.longValue());
                crmOwnerRecord.setCreateTime(DateUtil.date());
                crmOwnerRecord.save();
                Db.update("update 72crm_crm_contacts set owner_user_id = ? where customer_id = ?", atomicUserId.longValue(), id);
                Db.delete("delete from 72crm_crm_customer_pool_relation where customer_id = ?", id);
            }
            Db.update(Db.getSqlPara("crm.customer.getCustomersByIds", Kv.by("userId", atomicUserId.longValue()).set("now", DateUtil.date()).set("ids", idsArr).set("isReceive", isReceive)));
            Db.template("crm.customer.updateContacts1", Kv.by("ids",idsArr).set("userId",atomicUserId.longValue())).update();
            return true;
        });
        if (CollectionUtil.isNotEmpty(errorList)) {
            return R.error(errorList.get(0));
        }
        return R.ok();
    }

    public boolean isMaxOwner(Long ownerUserId, String[] ids) {
        SqlPara sqlPara = Db.getSqlPara("crm.customer.ownerNum", Kv.by("ids", ids).set("ownerUserId", ownerUserId));
        Integer number = Db.queryInt(sqlPara.getSql(), sqlPara.getPara());
        return queryCustomerSettingNum(1, ownerUserId, number);
    }

    /**
     * @author wyq
     * 获取客户导入查重字段
     */
    public R getCheckingField() {
        return R.ok().put("data", "客户名称");
    }

    public R customerSetting(CrmCustomerSetting customerSetting, String ipAddress) {
        List<String> errorList = new ArrayList<>();
        Db.tx(() -> {
            Db.delete("delete from `72crm_crm_customer_settinguser` where setting_id = ?", customerSetting.getSettingId());
            customerSetting.setCreateTime(DateUtil.date());
            if (customerSetting.getSettingId() != null) {
                customerSetting.update();
            } else {
                customerSetting.save();
            }
            Integer type = customerSetting.getType();
            for (Integer deptId : customerSetting.getDeptList()) {
                SqlPara sqlPara = Db.getSqlPara("crm.customer.getCustomerSettingCount", Kv.by("type", type).set("deptId", deptId));
                Integer count = Db.queryInt(sqlPara.getSql(), sqlPara.getPara());
                if (count > 0) {
                    errorList.add("已经有员工或部门信息包含在别的规则里面");
                    return false;
                }
                CrmCustomerSettinguser crmCustomerSettingUser = new CrmCustomerSettinguser();
                crmCustomerSettingUser.setDeptId(deptId);
                crmCustomerSettingUser.setSettingId(customerSetting.getSettingId());
                crmCustomerSettingUser.setType(2);
                crmCustomerSettingUser.save();
            }
            for (Long userId : customerSetting.getUserList()) {
                SqlPara sqlPara = Db.getSqlPara("crm.customer.getCustomerSettingCount", Kv.by("type", type).set("userId", userId));
                Integer count = Db.queryInt(sqlPara.getSql(), sqlPara.getPara());
                if (count > 0) {
                    errorList.add("已经有员工或部门信息包含在别的规则里面");
                    return false;
                }
                CrmCustomerSettinguser crmCustomerSettingUser = new CrmCustomerSettinguser();
                crmCustomerSettingUser.setUserId(userId);
                crmCustomerSettingUser.setSettingId(customerSetting.getSettingId());
                crmCustomerSettingUser.setType(1);
                crmCustomerSettingUser.save();
            }
            return true;
        });
        if (CollectionUtil.isNotEmpty(errorList)) {
            return R.error(errorList.get(0));
        }
        return R.ok();
    }

    public Page<CrmCustomerSetting> queryCustomerSetting(BasePageRequest<CrmCustomerSetting> pageRequest) {
        Page<CrmCustomerSetting> paginate = CrmCustomerSetting.dao.paginate(pageRequest.getPage(), pageRequest.getLimit(), CrmCustomerSetting.dao.getSqlPara("crm.customer.queryCustomerSetting", Kv.by("type", pageRequest.getData().getType())));
        paginate.getList().forEach(crmCustomerSetting -> {
            List<Record> deptList = new ArrayList<>();
            List<Record> userList = new ArrayList<>();
            if (StrUtil.isNotEmpty(crmCustomerSetting.getStr("deptIds"))) {
                deptList = Db.find(Db.getSqlPara("admin.dept.queryByIds", Kv.by("ids", crmCustomerSetting.getStr("deptIds").split(","))));
            }
            if (StrUtil.isNotEmpty(crmCustomerSetting.getStr("userIds"))) {
                userList = Db.find(Db.getSqlPara("admin.user.queryByIds", Kv.by("ids", crmCustomerSetting.getStr("userIds").split(","))));
            }
            crmCustomerSetting.put("userIds", userList);
            crmCustomerSetting.put("deptIds", deptList);
        });
        return paginate;
    }

    public R queryEditCustomerSetting(Integer settingId) {
        CrmCustomerSetting crmCustomerSetting = CrmCustomerSetting.dao.findById(settingId);
        crmCustomerSetting.put("userIds", Db.query("SELECT user_id FROM 72crm_crm_customer_settinguser WHERE type='1' and setting_id =?", settingId));
        crmCustomerSetting.put("deptIds", Db.query("SELECT dept_id FROM 72crm_crm_customer_settinguser WHERE type='2' and setting_id =?", settingId));
        return R.ok().put("data", crmCustomerSetting);
    }

    @Before(Tx.class)
    public R deleteCustomerSetting(Integer settingId) {
        CrmCustomerSetting crmCustomerSetting = CrmCustomerSetting.dao.findById(settingId);
        if (crmCustomerSetting != null) {
            crmCustomerSetting.delete();
            Db.delete("delete from `72crm_crm_customer_settinguser` where setting_id = ?", settingId);
        }
        return R.ok();
    }

    public boolean queryCustomerSettingNum(int type, Long userId) {
        return queryCustomerSettingNum(type, userId, 1);
    }

    public boolean queryCustomerSettingNum(int type, Long userId, int offset) {
        SqlPara sqlPara = Db.getSqlPara("crm.customer.queryCustomerSettingByUserId", Kv.by("userId", userId).set("type", type));
        List<Record> crmCustomerSettings = Db.find(sqlPara);
        if (crmCustomerSettings.size() > 0) {
            Map<Integer, List<Record>> typeCollect = crmCustomerSettings.stream().collect(Collectors.groupingBy(record -> record.getInt("type")));
            Integer customerNum;
            Integer customerDeal;
            if (typeCollect.get(1) != null) {
                Record record = typeCollect.get(1).get(0);
                customerNum = record.getInt("customer_num");
                customerDeal = record.getInt("customer_deal");
            } else {
                Record record = typeCollect.get(2).get(0);
                customerNum = record.getInt("customer_num");
                customerDeal = record.getInt("customer_deal");
            }
            SqlPara para = Db.getSqlPara("crm.customer.queryCustomerSettingNum", Kv.by("deal", customerDeal).set("type", type));
            Integer count = Db.queryInt(para.getSql() + " and owner_user_id = ? ", userId);
            return count + offset <= customerNum;
        }
        return true;
    }

    /**
     * 设置首要联系人
     *
     * @return
     */
    public R setContacts(Integer customer, Integer contactsId) {
        if (customer == null || contactsId == null) {
            return R.error("参数错误");
        }
        CrmCustomer crmCustomer = CrmCustomer.dao.findById(customer);
        crmCustomer.setContactsId(contactsId);
        return R.isSuccess(crmCustomer.update());
    }

    public R dataCheck(String name, String phone) {
        Kv kv = new Kv();
        if (StrUtil.isAllEmpty(name, phone)) {
            return R.error("参数错误");
        }
        kv.set("name", name);
        kv.set("phone", phone);
        return R.ok().put("data", Db.template("crm.customer.dataCheck", kv).find());
    }

    /**
     * @author wyq
     * 附件列表
     */
    public R queryFileList(Integer customerId) {
        SqlPara sqlPara=Db.getSqlPara("crm.customer.queryFileList", Kv.by("customerId", customerId));
        return R.ok().put("data",Db.find(sqlPara));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public R num(Integer customerId) {
        String businessCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_BUSINESS, 1);
        String contractCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_CONTRACT, 1);
        String receivablesCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_RECEIVABLES, 1);
        String contactsCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_CONTACTS, 1);
        String returnVisitCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_RETURN_VISIT, 1);
        String invoiceCon = AuthUtil.getCrmAuthSql(CrmEnum.CRM_INVOICE, 1);
        Kv kv = Kv.by("customerId", customerId).set("businessCon", businessCon).set("contractCon", contractCon).set("contactsCon", contactsCon).set("receivablesCon", receivablesCon).set("returnVisitCon", returnVisitCon).set("invoiceCon", invoiceCon);
        Record record = Db.findFirst(Db.getSqlPara("crm.customer.queryNum", kv));
        return R.ok().put("data", record);
    }

    /**
     * 附近的客户
     */
    public R nearbyCustomer(String lng, String lat, Integer type, Integer radius, Long ownerUserId) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "crm", "customer", "nearbyCustomer");
        List<Long> authUserIdList = AuthUtil.getUserIdByAuth(menuId);
        Kv kv = Kv.by("lng", lng).set("lat", lat).set("type", type).set("radius", radius).set("ownerUserId", ownerUserId).set("userIds", authUserIdList);
        if (ObjectUtil.isEmpty(type) || type.equals(9)) {
            List<Integer> poolIdList;
            if (BaseUtil.isAdmin()) {
                poolIdList = Db.query("select pool_id from 72crm_crm_customer_pool");
            } else {
                poolIdList = Db.template("crm.customer.queryPoolIdByUserId", Kv.by("userId", BaseUtil.getUserId()).set("deptId", BaseUtil.getUser().getDeptId())).query();
            }
            kv.set("poolIds", poolIdList);
        }
        return R.ok().put("data", Db.template("crm.customer.queryNearbyCustomer", kv).find());
    }

    /**
     * 标星（关注）
     */
    public R star(Integer customerId) {
        Long userId = BaseUtil.getUserId();
        CrmCustomerUserStar userStar = CrmCustomerUserStar.dao.findFirst("select * from 72crm_crm_customer_user_star where user_id = ? and customer_id = ?", userId, customerId);
        List<Integer> customerIdList = Db.query("select customer_id from 72crm_crm_customer_user_star where user_id = ?", userId);
        if (ObjectUtil.isEmpty(userStar)) {
            new CrmCustomerUserStar().setUserId(userId).setCustomerId(customerId).save();
            customerIdList.add(customerId);
        } else {
            userStar.delete();
            customerIdList.remove(customerId);
        }
        String customerIds = CollectionUtil.isEmpty(customerIdList) ? "0" : CollectionUtil.join(customerIdList, ",");
        JSONObject data = new JSONObject().fluentPut("customer_id", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", customerIds));
        Db.update("update 72crm_admin_scene set data = ? where type = 2 and user_id = ? and bydata = 'star'", data.toString(), userId);
        return R.ok();
    }
}
