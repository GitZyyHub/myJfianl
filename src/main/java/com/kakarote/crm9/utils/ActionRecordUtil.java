package com.kakarote.crm9.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmActionRecord;
import com.kakarote.crm9.erp.work.entity.WorkTaskClass;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author wyq
 */
public class ActionRecordUtil<T extends Model> {

    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(1, 20, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2048), new ThreadPoolExecutor.AbortPolicy());

    public static class ActionRecordTask implements Runnable {
        private CrmActionRecord actionRecord;
        private static final Integer batchNumber = 5;
        private static final String SAVE_RECORD_TEMPLATE = "INSERT INTO `72crm_crm_action_record` (`create_user_id`, `create_time`, `ip_address`, `types`, `action_id`, `object`, `behavior`, `content`, `detail`) VALUES ('{}', '{}', '{}', '{}', {}, '{}', '{}', '{}', '{}')";
        private static final List<String> sqlList = new CopyOnWriteArrayList<>();

        ActionRecordTask(CrmActionRecord actionRecord) {
            this.actionRecord = actionRecord;
        }

        @Override
        public void run() {
            if (ObjectUtil.isEmpty(actionRecord)) {
                return;
            }
            if (sqlList.size() >= batchNumber) {
                saveActionRecord();
            }
            sqlList.add(StrUtil.format(SAVE_RECORD_TEMPLATE, actionRecord.getCreateUserId(), DateUtil.formatDateTime(actionRecord.getCreateTime()), actionRecord.getIpAddress(), actionRecord.getTypes(), actionRecord.getActionId(), actionRecord.getObject() != null ? actionRecord.getObject() : "", actionRecord.getBehavior(), actionRecord.getContent() != null ? actionRecord.getContent() : "", actionRecord.getDetail()));
        }

        static void saveActionRecord() {
            List<String> list = new ArrayList<>(sqlList);
            //底层已经做过size为0的判断，此处不再限制
            try {
                Db.batch(list, batchNumber);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqlList.clear();
            }
        }
    }

    /**
     * 属性kv
     */
    private static Map<Integer, Kv> propertiesMap = new HashMap<>();

    static {
        propertiesMap.put(CrmEnum.CRM_LEADS.getType(), Kv.by("leadsName", "线索名称").set("address", "地址").set("mobile", "手机").set("nextTime", "下次联系时间").set("remark", "备注").set("telephone", "电话"));
        propertiesMap.put(CrmEnum.CRM_CUSTOMER.getType(), Kv.by("customerName", "客户名称").set("address", "省市区").set("location", "详细地址").set("mobile", "手机").set("nextTime", "下次联系时间").set("remark", "备注").set("telephone", "电话").set("website", "网址"));
        propertiesMap.put(CrmEnum.CRM_CONTACTS.getType(), Kv.by("name", "姓名").set("customerId", "客户姓名").set("mobile", "手机").set("nextTime", "下次联系时间").set("remark", "备注").set("telephone", "电话").set("email", "电子邮箱").set("post", "职务").set("address", "地址"));
        propertiesMap.put(CrmEnum.CRM_BUSINESS.getType(), Kv.by("businessName", "商机名称").set("customerId", "客户姓名").set("money", "商机金额").set("dealDate", "预计成交日期").set("remark", "备注").set("typeId", "商机状态组").set("statusId", "商机阶段").set("totalPrice", "总金额").set("discountRate", "整单折扣（%）"));
        propertiesMap.put(CrmEnum.CRM_CONTRACT.getType(), Kv.by("num", "合同编号").set("name", "合同名称").set("customerId", "客户名称").set("contactsId", "客户签约人").set("businessId", "商机名称").set("orderDate", "下单时间").set("money", "合同金额").set("startTime", "合同开始时间").set("endTime", "合同结束时间").set("companyUserId", "公司签约人").set("remark", "备注").set("totalPrice", "总金额").set("discountRate", "整单折扣（%）"));
        propertiesMap.put(CrmEnum.CRM_RECEIVABLES.getType(), Kv.by("number", "回款编号").set("customerId", "客户姓名").set("contractId", "合同编号").set("returnTime", "回款日期").set("money", "回款金额").set("planId", "期数").set("remark", "备注"));
        propertiesMap.put(CrmEnum.CRM_PRODUCT.getType(), Kv.by("name", "产品名称").set("categoryId", "产品类型").set("num", "产品编码").set("price", "价格").set("description", "产品描述"));
        propertiesMap.put(CrmEnum.CRM_MARKETING.getType(), Kv.by("marketingName", "活动名称").set("crmType", "关联对象").set("relationUserId", "参与人员").set("marketingType", "活动类型").set("startTime", "开始时间").set("endTime", "截止时间").set("browse", "浏览数").set("submitNum", "提交数").set("marketingMoney", "活动预算").set("address", "活动地址").set("synopsis", "活动简介"));
        propertiesMap.put(CrmEnum.WORK.getType(), Kv.by("name", "项目名称").set("description", "描述").set("isOpen", "可见范围").set("ownerUserId", "项目成员"));
        propertiesMap.put(CrmEnum.CRM_RETURN_VISIT.getType(), Kv.by("visitNumber", "回访编号").set("visitTime", "回访时间").set("ownerUserId", "回访人").set("customerId", "客户名称").set("contractId", "合同编号").set("contactsId", "联系人"));
        propertiesMap.put(CrmEnum.CRM_INVOICE.getType(), Kv.by("invoiceApplyNumber", "发票申请编号").set("customerId", "客户名称").set("contractId", "合同编号").set("invoiceMoney", "开票金额").set("invoiceDate", "开票日期").set("invoiceType", "开票类型").set("remark", "备注").set("titleType", "抬头类型").set("invoiceTitle", "开票抬头").set("taxNumber", "纳税识别号").set("depositBank", "开户银行").set("depositAccount", "开户账户").set("depositAddress", "开票地址").set("contactsName", "联系人名称").set("contactsTelephone", "联系方式").set("contactsAddress", "邮寄地址"));
    }


    private List<String> textList = new ArrayList<>();

    /**
     * 更新记录
     *
     * @param oldObj  之前对象
     * @param newObj  新对象
     * @param crmEnum 类型
     */
    @SuppressWarnings("unchecked")
    public void updateRecord(T oldObj, T newObj, CrmEnum crmEnum) {
        try {
            CrmActionRecord crmActionRecord = new CrmActionRecord();
            crmActionRecord.setCreateUserId(BaseUtil.getUser().getUserId());
            crmActionRecord.setCreateTime(new Date());
            searchChange(textList, oldObj._getAttrsEntrySet(), (newObj)._getAttrsEntrySet(), crmEnum.getType());
            crmActionRecord.setTypes(crmEnum.getType() + "");
            Integer actionId = oldObj.getInt(TableMapping.me().getTable(oldObj.getClass()).getPrimaryKey()[0]);
            crmActionRecord.setActionId(actionId);
            if (!crmEnum.equals(CrmEnum.WORK)) {
                crmActionRecord.setContent(JSON.toJSONString(textList));
            }
            crmActionRecord.setIpAddress(BaseUtil.getIp());
            crmActionRecord.setDetail(StrUtil.join("", textList));
            crmActionRecord.setBehavior(BehaviorEnum.UPDATE.getType());
            String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
            crmActionRecord.setObject(name);
            if (textList.size() > 0) {
                ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
                THREAD_POOL.execute(actionRecordTask);
            }
        } finally {
            textList.clear();
        }

    }

    public void addRecord(Integer actionId, CrmEnum crmEnum) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(BaseUtil.getUser().getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType() + "");
        crmActionRecord.setActionId(actionId);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("新建了" + crmEnum.getRemarks());
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        crmActionRecord.setDetail("新建了" + crmEnum.getRemarks() + "：" + name);
        crmActionRecord.setBehavior(BehaviorEnum.SAVE.getType());
        crmActionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    /**
     * 通用模板，无需特殊处理的操作记录适用
     *
     * @param crmEnum
     * @param actionId
     * @param behaviorEnum
     */
    public void addObjectActionRecord(CrmEnum crmEnum, Integer actionId, BehaviorEnum behaviorEnum) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(behaviorEnum.getType());
        actionRecord.setActionId(actionId);
        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        String detail;
        switch (behaviorEnum) {
            case CANCEL_EXAMINE:
                detail = "将" + crmEnum.getRemarks() + "：" + name + "作废";
                break;
            case FOLLOW_UP:
                detail = "给" + crmEnum.getRemarks() + "：" + name + "新建了跟进记录";
                break;
            default:
                detail = behaviorEnum.getName() + "了" + crmEnum.getRemarks() + "：" + name;
                break;
        }
        actionRecord.setDetail(detail);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void updateRecord(JSONArray jsonArray, Kv kv) {
        textList.clear();
        if (jsonArray == null) {
            return;
        }
        List<Record> oldFieldList = Db.find(Db.getSqlPara("crm.record.queryFieldValue", kv));
        jsonArray.forEach(json -> {
            JSONObject newField = (JSONObject) json;
            String name = newField.getString("name");
            for (Record oldField : oldFieldList) {
                String oldFieldValue;
                String newFieldValue;
                if (StrUtil.isEmpty(oldField.getStr("value")) && StrUtil.isEmpty(newField.getString("value"))) {
                    continue;
                }
                if (StrUtil.isEmpty(oldField.getStr("value"))) {
                    oldFieldValue = "空";
                } else {
                    oldFieldValue = oldField.getStr("value");
                }
                if (StrUtil.isEmpty(newField.getString("value"))) {
                    newFieldValue = "空";
                } else {
                    newFieldValue = newField.getString("value");
                }
                if (oldField.getStr("name").equals(name) && !oldFieldValue.equals(newFieldValue)) {
                    textList.add("将" + oldField.getStr("name") + " 由" + oldFieldValue + "修改为" + newFieldValue + "。");
                }
            }
        });
    }

    private void searchChange(List<String> textList, Set<Map.Entry<String, Object>> oldEntries, Set<Map.Entry<String, Object>> newEntries, Integer crmTypes) {
        oldEntries.forEach(x -> newEntries.forEach(y -> {
            String oldKey = StrUtil.toCamelCase(x.getKey());
            String newKey = StrUtil.toCamelCase(y.getKey());
            if (propertiesMap.get(crmTypes).containsKey(oldKey)) {
                Object oldValue = x.getValue();
                Object newValue = y.getValue();
                if (oldValue instanceof Date) {
                    oldValue = DateUtil.formatDateTime((Date) oldValue);
                }
                if (newValue instanceof Date) {
                    newValue = DateUtil.formatDateTime((Date) newValue);
                }
                if (ObjectUtil.isEmpty(oldValue)) {
                    oldValue = "空";
                }
                if (ObjectUtil.isEmpty(newValue)) {
                    newValue = "空";
                }
                if (newKey.equals(oldKey) && !oldValue.equals(newValue)) {
                    switch (oldKey) {
                        case "companyUserId":
                            newValue = Db.queryStr("select realname from `72crm_admin_user` where user_id = ?", newValue);
                            oldValue = Db.queryStr("select realname from `72crm_admin_user` where user_id = ?", oldValue);
                            break;
                        case "customerId":
                            newValue = Db.queryStr("select customer_name from `72crm_crm_customer` where customer_id = ?", newValue);
                            oldValue = Db.queryStr("select customer_name from `72crm_crm_customer` where customer_id = ?", oldValue);
                            break;
                        case "businessId":
                            newValue = Db.queryStr("select business_name from `72crm_crm_business` where business_id = ?", newValue);
                            oldValue = Db.queryStr("select business_name from `72crm_crm_business` where business_id = ?", oldValue);
                            break;
                        case "contractId":
                            newValue = Db.queryStr("select num from `72crm_crm_contract` where contract_id = ?", newValue);
                            oldValue = Db.queryStr("select num from `72crm_crm_contract` where contract_id = ?", oldValue);
                            break;
                        case "contactsId":
                            newValue = Db.queryStr("select name from `72crm_crm_contacts` where contacts_id = ?", newValue);
                            oldValue = Db.queryStr("select name from `72crm_crm_contacts` where contacts_id = ?", oldValue);
                            break;
                        case "typeId":
                            newValue = Db.queryStr("select name from `72crm_crm_business_type` where type_id = ?", newValue);
                            oldValue = Db.queryStr("select name from `72crm_crm_business_type` where type_id = ?", oldValue);
                            break;
                        case "statusId":
                            newValue = Db.queryStr("select name from `72crm_crm_business_status` where status_id = ?", newValue);
                            oldValue = Db.queryStr("select name from `72crm_crm_business_status` where status_id = ?", oldValue);
                            break;
                        case "planId":
                            newValue = Db.queryStr("select num from `72crm_crm_receivables_plan` where plan_id = ?", newValue);
                            oldValue = Db.queryStr("select num from `72crm_crm_receivables_plan` where plan_id = ?", oldValue);
                            break;
                        case "categoryId":
                            newValue = Db.queryStr("select name from `72crm_crm_product_category` where category_id = ?", newValue);
                            oldValue = Db.queryStr("select name from `72crm_crm_product_category` where category_id = ?", oldValue);
                            break;
                        case "crmType":
                            newValue = newValue.equals(1) ? "线索" : "客户";
                            oldValue = oldValue.equals(1) ? "线索" : "客户";
                            break;
                        case "relationUserId":
                        case "ownerUserId":
                            newValue = Db.queryStr("select GROUP_CONCAT(realname) from `72crm_admin_user` where find_in_set(user_id,?)", newValue);
                            oldValue = Db.queryStr("select GROUP_CONCAT(realname) from `72crm_admin_user` where find_in_set(user_id,?)", oldValue);
                            break;
                        case "isOpen":
                            newValue = newValue.equals(1) ? "公开" : "私有";
                            oldValue = oldValue.equals(1) ? "公开" : "私有";
                        default:
                            break;
                    }

                    if (ObjectUtil.isEmpty(oldValue)) {
                        oldValue = "空";
                    }
                    if (ObjectUtil.isEmpty(newValue)) {
                        newValue = "空";
                    }
                    textList.add("将" + propertiesMap.get(crmTypes).get(oldKey) + " 由" + oldValue + "修改为" + newValue + "。");
                }
            }
        }));
    }

    /**
     * 添加转移记录
     *
     * @param actionId
     */
    public void addConversionRecord(Integer actionId, CrmEnum crmEnum, Long userId) {
        String userName = Db.queryStr("select realname from 72crm_admin_user where user_id = ?", userId);
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(BaseUtil.getUser().getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType() + "");
        crmActionRecord.setActionId(actionId);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("将" + crmEnum.getRemarks() + "转移给：" + userName);
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        crmActionRecord.setDetail("将" + crmEnum.getRemarks() + "：" + name + "转移给：" + userName);
        crmActionRecord.setBehavior(BehaviorEnum.CHANGE_OWNER.getType());
        crmActionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    /**
     * 添加(锁定/解锁)记录
     */
    public void addIsLockRecord(String[] ids, CrmEnum crmEnum, Integer isLock) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(BaseUtil.getUser().getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType() + "");
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        ArrayList<String> strings = new ArrayList<>();
        if (isLock == 2) {
            strings.add("将客户锁定。");
            crmActionRecord.setBehavior(BehaviorEnum.LOCK.getType());
        } else {
            strings.add("将客户解锁。");
            crmActionRecord.setBehavior(BehaviorEnum.UNLOCK.getType());
        }
        crmActionRecord.setContent(JSON.toJSONString(strings));
        for (String actionId : ids) {
            String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
            String detail;
            if (isLock == 2) {
                detail = "将客户：" + name + "锁定";
            } else {
                detail = "将客户：" + name + "解锁";
            }
            crmActionRecord.setDetail(detail);
            crmActionRecord.setId(null);
            crmActionRecord.setActionId(Integer.valueOf(actionId));
            crmActionRecord.setObject(name);
            ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
            THREAD_POOL.execute(actionRecordTask);
        }
    }

    /**
     * 线索转化客户
     *
     * @param actionId
     * @param crmTypes
     */
    public void addConversionCustomerRecord(Integer actionId, String crmTypes, String name) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(BaseUtil.getUser().getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmTypes);
        crmActionRecord.setActionId(actionId);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("将线索\"" + name + "\"转化为客户");
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        crmActionRecord.setDetail(strings.get(0));
        crmActionRecord.setBehavior(BehaviorEnum.TRANSFER.getType());
        crmActionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    /**
     * 放入公海
     */
    public void addPutIntoTheOpenSeaRecord(Collection actionIds, CrmEnum crmEnum) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        if (BaseUtil.getUser() == null) {
            crmActionRecord.setCreateUserId(BaseUtil.getSuperUser());
        } else {
            crmActionRecord.setCreateUserId(BaseUtil.getUser().getUserId());
        }
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType() + "");
        ArrayList<String> strings = new ArrayList<>();
        strings.add("将客户放入公海");
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        crmActionRecord.setBehavior(BehaviorEnum.PUT_IN_POOL.getType());
        for (Object actionId : actionIds) {
            String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
            crmActionRecord.setDetail("将客户：" + name + "放入公海");
            crmActionRecord.remove("id");
            crmActionRecord.setActionId((Integer) actionId);
            crmActionRecord.setObject(name);
            ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
            THREAD_POOL.execute(actionRecordTask);
        }
    }


    /**
     * 添加分配客户记录
     */
    public void addDistributionRecord(String actionId, CrmEnum crmEnum, Long userId) {
        for (String id : actionId.split(",")) {
            if (StrUtil.isEmpty(id)) {
                continue;
            }
            ArrayList<String> strings = new ArrayList<>();
            CrmActionRecord crmActionRecord = new CrmActionRecord();
            crmActionRecord.setCreateUserId(BaseUtil.getUser().getUserId());
            crmActionRecord.setCreateTime(new Date());
            crmActionRecord.setTypes(crmEnum.getType() + "");
            crmActionRecord.setActionId(Integer.valueOf(id));
            String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
            if (userId == null) {
                //领取
                strings.add("领取了客户");
                crmActionRecord.setDetail("领取了客户：" + name);
                crmActionRecord.setBehavior(BehaviorEnum.RECEIVE.getType());
            } else {
                String userName = Db.queryStr("select realname from 72crm_admin_user where user_id = ?", userId);
                //管理员分配
                strings.add("将客户分配给：" + userName);
                crmActionRecord.setDetail("将客户：" + name + "分配给：" + userName);
                crmActionRecord.setBehavior(BehaviorEnum.DISTRIBUTE.getType());
            }
            crmActionRecord.setContent(JSON.toJSONString(strings));
            crmActionRecord.setIpAddress(BaseUtil.getIp());
            crmActionRecord.setObject(name);
            ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
            THREAD_POOL.execute(actionRecordTask);
        }
    }

    public void addDeleteActionRecord(CrmEnum crmEnum, String... ids) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(BehaviorEnum.DELETE.getType());
        for (String actionId : ids) {
            actionRecord.setActionId(Integer.valueOf(actionId));
            String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", Integer.valueOf(actionId));
            actionRecord.setDetail("删除了" + crmEnum.getRemarks() + "：" + name);
            actionRecord.setObject(name);
            ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
            THREAD_POOL.execute(actionRecordTask);
        }
    }

    public void addMemberActionRecord(CrmEnum crmEnum, Integer actionId, Long userId) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(BehaviorEnum.ADD_MEMBER.getType());
        actionRecord.setActionId(actionId);
        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        String userName = Db.queryStr("select realname from 72crm_admin_user where user_id = ?", userId);
        actionRecord.setDetail("给" + crmEnum.getRemarks() + "：" + name + "添加了团队成员：" + userName);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addDeleteMemberActionRecord(CrmEnum crmEnum, Integer actionId, Long userId, boolean isSelf) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setActionId(actionId);
        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        if (isSelf) {
            actionRecord.setBehavior(BehaviorEnum.EXIT_MEMBER.getType());
            actionRecord.setDetail("退出了" + crmEnum.getRemarks() + "：" + name + "的团队成员");
        } else {
            actionRecord.setBehavior(BehaviorEnum.REMOVE_MEMBER.getType());
            String userName = Db.queryStr("select realname from 72crm_admin_user where user_id = ?", userId);
            actionRecord.setDetail("移除了" + crmEnum.getRemarks() + "：" + name + "的团队成员：" + userName);
        }
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addExportActionRecord(CrmEnum crmEnum, Integer number) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(BehaviorEnum.EXCEL_EXPORT.getType());
        actionRecord.setDetail("导出了" + number + "条" + crmEnum.getRemarks());
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addFollowupActionRecord(Integer crmType, Integer actionId) {
        CrmEnum crmEnum = CrmEnum.parse(crmType);
        addObjectActionRecord(crmEnum, actionId, BehaviorEnum.FOLLOW_UP);
    }

    public void addMarketingUpdateStatusRecord(String[] ids, CrmEnum crmEnum, Integer status) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(BaseUtil.getUser().getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType() + "");
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        if (status == 1) {
            crmActionRecord.setBehavior(BehaviorEnum.START.getType());
        } else {
            crmActionRecord.setBehavior(BehaviorEnum.STOP.getType());
        }
        for (String actionId : ids) {
            String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
            String detail;
            if (status == 1) {
                detail = "将活动：" + name + "启用";
            } else {
                detail = "将活动：" + name + "停用";
            }
            crmActionRecord.setDetail(detail);
            crmActionRecord.setId(null);
            crmActionRecord.setActionId(Integer.valueOf(actionId));
            crmActionRecord.setObject(name);
            ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
            THREAD_POOL.execute(actionRecordTask);
        }
    }

    public void addOaLogSaveRecord(CrmEnum crmEnum, Integer actionId) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(BehaviorEnum.SAVE.getType());
        actionRecord.setActionId(actionId);
        actionRecord.setDetail("新建了" + crmEnum.getRemarks() + "：" + DateUtil.formatDate(new Date()));
        actionRecord.setObject(DateUtil.formatDate(new Date()));
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addOaLogUpdateRecord(CrmEnum crmEnum, Integer actionId) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(BehaviorEnum.UPDATE.getType());
        actionRecord.setActionId(actionId);
        String date = Db.queryStr("select DATE_FORMAT(create_time,'%Y-%m-%d') from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        actionRecord.setDetail("编辑了" + crmEnum.getRemarks() + "：" + date);
        actionRecord.setObject(date);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addOaExamineActionRecord(CrmEnum crmEnum, Integer actionId, BehaviorEnum behaviorEnum) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(behaviorEnum.getType());
        actionRecord.setActionId(actionId);
        String content = Db.queryStr("select content from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        if (content.length() > 20) {
            content = content.substring(0, 20) + "...";
        }
        String prefix = "";
        switch (behaviorEnum) {
            case SAVE:
                prefix = "新建了";
                break;
            case UPDATE:
                prefix = "编辑了";
                break;
            case RECHECK_EXAMINE:
                prefix = "撤回了";
                break;
            case PASS_EXAMINE:
                prefix = "通过了";
                break;
            case REJECT_EXAMINE:
                prefix = "驳回了";
                break;
            case DELETE:
                prefix = "删除了";
                break;
            default:
                break;
        }
        actionRecord.setDetail(prefix + crmEnum.getRemarks() + "：" + content);
        actionRecord.setObject(content);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addCrmExamineActionRecord(CrmEnum crmEnum, Integer actionId, BehaviorEnum behaviorEnum) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(behaviorEnum.getType());
        actionRecord.setActionId(actionId);
        String number = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where examine_record_id = ?", actionId);
        String prefix = "";
        switch (behaviorEnum) {
            case SUBMIT_EXAMINE:
                prefix = "提交了";
                break;
            case RECHECK_EXAMINE:
                prefix = "撤回了";
                break;
            case PASS_EXAMINE:
                prefix = "通过了";
                break;
            case REJECT_EXAMINE:
                prefix = "驳回了";
                break;
            default:
                break;
        }
        actionRecord.setDetail(prefix + crmEnum.getRemarks() + "：" + number);
        actionRecord.setObject(number);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addTaskClassRecord(WorkTaskClass taskClass, BehaviorEnum behaviorEnum) {
        CrmEnum crmEnum = CrmEnum.WORK;
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType() + "");
        actionRecord.setBehavior(behaviorEnum.getType());
        actionRecord.setActionId(taskClass.getWorkId());
        String name = Db.queryStr("select name from 72crm_work where work_id = ?", taskClass.getWorkId());
        String detail = "";
        if (behaviorEnum.equals(BehaviorEnum.SAVE)) {
            detail = "新建了任务列表：" + taskClass.getName();
        } else if (behaviorEnum.equals(BehaviorEnum.UPDATE)) {
            String oldClassName = Db.queryStr("select name from 72crm_work_task_class where class_id = ?", taskClass.getClassId());
            detail = "将任务列表名称由" + oldClassName + "改为" + taskClass.getName();
        } else if (behaviorEnum.equals(BehaviorEnum.DELETE)) {
            String className = Db.queryStr("select name from 72crm_work_task_class where class_id = ?", taskClass.getClassId());
            detail = "删除了任务列表：" + className;
        }
        actionRecord.setDetail(detail);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addTaskUpdateRecord(Integer taskId, String detail) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(BaseUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(CrmEnum.TASK.getType() + "");
        actionRecord.setBehavior(BehaviorEnum.UPDATE.getType());
        actionRecord.setActionId(taskId);
        String name = Db.queryStr("select name from 72crm_task where task_id = ?", taskId);
        actionRecord.setDetail(detail);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }
}
