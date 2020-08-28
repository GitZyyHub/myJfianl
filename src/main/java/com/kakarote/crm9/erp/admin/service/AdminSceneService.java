package com.kakarote.crm9.erp.admin.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminScene;
import com.kakarote.crm9.erp.admin.entity.AdminSceneDefault;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.service.CrmBusinessService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.FieldUtil;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminSceneService{
    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmBusinessService crmBusinessService;

    /**
     * @author wyq
     * 查询场景字段
     */
    public R queryField(Integer label){
        List<Record> fieldList = Db.find("select field_name,name,type,options from 72crm_admin_field where label = ? and field_type = 1 and is_hidden = 0", label != CrmEnum.CRM_CUSTOMER_POOL.getType() ? label : CrmEnum.CRM_CUSTOMER.getType());
        Aop.get(AdminFieldService.class).recordToFormType(fieldList);
        FieldUtil fieldUtil;
        String[] settingArr = new String[]{};
        if(CrmEnum.CRM_LEADS.getType() == label){
            fieldUtil = new FieldUtil(fieldList);
            fieldUtil.add("last_time", "最后跟进时间", "datetime", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_CUSTOMER.getType() == label){
            fieldUtil = new FieldUtil(fieldList);
            List<Record> dealStatusList = new ArrayList<>();
            dealStatusList.add(new Record().set("name","未成交").set("value",0));
            dealStatusList.add(new Record().set("name","已成交").set("value",1));
            fieldUtil.add("deal_status", "成交状态", "dealStatus", dealStatusList)
                    .add("last_time", "最后跟进时间", "datetime", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr)
                    .add("detail_address", "详细地址", "text", settingArr)
                    .add("address", "地区定位", "map_address", settingArr);
        }else if(CrmEnum.CRM_CONTACTS.getType() == label){
            fieldList.forEach(field -> {
                if (field.getStr("field_name").equals("customer_id")) {
                    field.set("field_name", "customer_name");
                }
            });
            fieldUtil = new FieldUtil(fieldList);
            fieldUtil.add("last_time", "最后跟进时间", "datetime", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr)
                    .add("pool_day", "距进入公海天数", "poolDay", settingArr);
        }else if(CrmEnum.CRM_PRODUCT.getType() == label){
            fieldList.forEach(field -> {
                if (field.getStr("field_name").equals("customer_id")) {
                    field.set("field_name", "customer_name");
                }
            });
            fieldUtil = new FieldUtil(fieldList);
            fieldUtil.add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_BUSINESS.getType() == label){
            fieldList.forEach(field -> {
                if (field.getStr("field_name").equals("customer_id")) {
                    field.set("field_name", "customer_name");
                }
            });
            fieldUtil = new FieldUtil(fieldList);
            fieldUtil.add("type_id", "商机状态组", "business_type", crmBusinessService.queryBusinessStatusOptions("condition"))
                    .add("last_time", "最后跟进时间", "datetime", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_CONTRACT.getType() == label){
            fieldList.forEach(field -> {
                if (field.getStr("field_name").equals("customer_id")) {
                    field.set("field_name", "customer_name");
                } else if (field.getStr("field_name").equals("business_id")) {
                    field.set("field_name", "business_name");
                } else if (field.getStr("field_name").equals("contacts_id")) {
                    field.set("field_name", "contacts_name");
                }
            });
            fieldUtil = new FieldUtil(fieldList);
            List<Map<String,Object>> checkList = new ArrayList<>();
            checkList.add(new JSONObject().fluentPut("name", "待审核").fluentPut("value", 0));
            checkList.add(new JSONObject().fluentPut("name", "通过").fluentPut("value", 1));
            checkList.add(new JSONObject().fluentPut("name", "拒绝").fluentPut("value", 2));
            checkList.add(new JSONObject().fluentPut("name", "审核中").fluentPut("value", 3));
            checkList.add(new JSONObject().fluentPut("name", "已撤回").fluentPut("value", 4));
            checkList.add(new JSONObject().fluentPut("name", "未提交").fluentPut("value", 5));
            checkList.add(new JSONObject().fluentPut("name", "已作废").fluentPut("value", 8));
            fieldUtil.add("check_status", "审核状态", "checkStatus", checkList)
                    .add("last_time", "最后跟进时间", "datetime", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_RECEIVABLES.getType() == label){
            fieldList.forEach(field -> {
                if (field.getStr("field_name").equals("customer_id")) {
                    field.set("field_name", "customer_name");
                } else if (field.getStr("field_name").equals("plan_id")) {
                    field.set("field_name", "plan_num");
                } else if (field.getStr("field_name").equals("contract_id")) {
                    field.set("field_name", "contract_num");
                }
            });
            fieldUtil = new FieldUtil(fieldList);
            List<Map<String,Object>> checkList = new ArrayList<>();
            checkList.add(new JSONObject().fluentPut("name", "待审核").fluentPut("value", 0));
            checkList.add(new JSONObject().fluentPut("name", "通过").fluentPut("value", 1));
            checkList.add(new JSONObject().fluentPut("name", "拒绝").fluentPut("value", 2));
            checkList.add(new JSONObject().fluentPut("name", "审核中").fluentPut("value", 3));
            checkList.add(new JSONObject().fluentPut("name", "已撤回").fluentPut("value", 4));
            checkList.add(new JSONObject().fluentPut("name", "未提交").fluentPut("value", 5));
            fieldUtil.add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        } else if (CrmEnum.CRM_RETURN_VISIT.getType() == label) {
            fieldList.forEach(field -> {
                if (field.getStr("field_name").equals("customer_id")) {
                    field.set("field_name", "customer_name");
                } else if (field.getStr("field_name").equals("contacts_id")) {
                    field.set("field_name", "contacts_name");
                } else if (field.getStr("field_name").equals("contract_id")) {
                    field.set("field_name", "contract_num");
                }
            });
            fieldUtil = new FieldUtil(fieldList);
            fieldUtil.add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else{
            return R.error("场景label不符合要求！");
        }
        fieldList = fieldUtil.getRecordList();
        List<Record> records = adminFieldService.customFieldList(label);
        if (fieldList != null && records != null) {
            records.removeIf(record -> record.getStr("formType").equals("file"));
            fieldList.addAll(records);
        }
        return R.ok().put("data", fieldList);
    }

    public R queryPoolField(Integer poolId) {
        List<Record> fieldList = Db.find(Db.getSql("admin.scene.queryPoolField"), poolId);
        List<Record> dealStatusList = new ArrayList<>();
        dealStatusList.add(new Record().set("name", "未成交").set("value", 0));
        dealStatusList.add(new Record().set("name", "已成交").set("value", 1));
        Aop.get(AdminFieldService.class).recordToFormType(fieldList);
        fieldList.forEach(field -> {
            if ("dealStatus".equals(field.getStr("field_name"))) {
                field.set("setting", dealStatusList).set("formType", "dealStatus");
            } else if ("createUserName".equals(field.getStr("field_name"))) {
                field.set("field_name", "create_user_id");
            } else if ("preOwnerUserName".equals(field.getStr("field_name"))) {
                field.set("field_name", "pre_owner_user_id");
            }

            String fieldName=field.getStr("field_name");
            //如果fieldName不包含中文则尝试转换成下划线格式
            fieldName=ReUtil.contains("[\u4e00-\u9fa5]",fieldName)?fieldName:StrUtil.toUnderlineCase(fieldName);
            field.set("field_name", fieldName);
        });
        return R.ok().put("data", fieldList);
    }

    /**
     * @author wyq
     * 增加场景
     */
    @Before(Tx.class)
    public R addScene(AdminScene adminScene){
        Long userId = BaseUtil.getUser().getUserId();
        adminScene.setIsHide(0).setSort(99999).setIsSystem(0).setCreateTime(DateUtil.date()).setUserId(userId);
        adminScene.save();
        if(1 == adminScene.getIsDefault()){
            AdminSceneDefault adminSceneDefault = new AdminSceneDefault();
            adminSceneDefault.setSceneId(adminScene.getSceneId()).setType(adminScene.getType()).setUserId(userId).save();
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 更新场景
     */
    @Before(Tx.class)
    public R updateScene(AdminScene adminScene){
        Long userId = BaseUtil.getUser().getUserId();
        AdminScene oldAdminScene = AdminScene.dao.findById(adminScene.getSceneId());
        if(1 == adminScene.getIsDefault()){
            Db.update("update 72crm_admin_scene_default set scene_id = ? where user_id = ? and type = ?", adminScene.getSceneId(), userId, oldAdminScene.getType());
        }
        adminScene.setUserId(userId).setType(oldAdminScene.getType()).setSort(oldAdminScene.getSort()).setIsSystem(oldAdminScene.getIsSystem()).setUpdateTime(DateUtil.date());
        return R.isSuccess(adminScene.update());
    }

    /**
     * @author wyq
     * 设置默认场景
     */
    @Before(Tx.class)
    public R setDefaultScene(Integer sceneId){
        Long userId = BaseUtil.getUser().getUserId();
        AdminScene oldAdminScene = AdminScene.dao.findById(sceneId);
        Db.delete("delete from 72crm_admin_scene_default where user_id = ? and type = ?", userId, oldAdminScene.getType());
        AdminSceneDefault adminSceneDefault = new AdminSceneDefault();
        return adminSceneDefault.setSceneId(sceneId).setType(oldAdminScene.getType()).setUserId(userId).save() ? R.ok() : R.error();
    }

    /**
     * @author wyq
     * 删除场景
     */
    @Before(Tx.class)
    public R deleteScene(AdminScene adminScene){
        if(1 == AdminScene.dao.findById(adminScene.getSceneId()).getIsSystem()){
            return R.error("系统场景不能删除");
        }
        return AdminScene.dao.deleteById(adminScene.getSceneId()) ? R.ok() : R.error();
    }

    /**
     * @author wyq
     * 查询场景
     */
    @Before(Tx.class)
    public R queryScene(Integer type){
        Long userId = BaseUtil.getUser().getUserId();
        //查询userId下是否有系统场景，没有则插入
        Integer number = Db.queryInt(Db.getSql("admin.scene.querySystemNumber"), type, userId);
        if(number.equals(0)){
            AdminScene systemScene = new AdminScene();
            systemScene.setUserId(userId).setSort(0).setData("").setIsHide(0).setIsSystem(1).setCreateTime(DateUtil.date()).setType(type);
            JSONObject ownerObject = new JSONObject();
            ownerObject.fluentPut("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("condition", "is").fluentPut("value", userId));
            JSONObject subOwnerObject = new JSONObject();
            subOwnerObject.fluentPut("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("condition", "in").fluentPut("value", getSubUserId(userId, BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1)));
            if(CrmEnum.CRM_LEADS.getType() == type){
                systemScene.setName("全部线索").setData(new JSONObject().fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", 0)).toString()).save();
                ownerObject.fluentPut("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("condition", "is").fluentPut("value", userId)).fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", 0));
                systemScene.setSceneId(null).setName("我负责的线索").setData(ownerObject.toString()).save();
                subOwnerObject.fluentPut("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("condition", "in").fluentPut("value", getSubUserId(userId, BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1))).fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", 0));
                systemScene.setSceneId(null).setName("下属负责的线索").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", "1"));
                systemScene.setSceneId(null).setName("已转化的线索").setData(jsonObject.toString()).setBydata("transform").save();
                jsonObject.clear();
                List<Integer> leadsIdList = Db.query("select leads_id from 72crm_crm_leads_user_star where user_id = ?", userId);
                String leadsIds = CollectionUtil.isEmpty(leadsIdList) ? "0" : CollectionUtil.join(leadsIdList, ",");
                jsonObject.fluentPut("leads_id", new JSONObject().fluentPut("name", "leads_id").fluentPut("condition", "in").fluentPut("value", leadsIds));
                systemScene.setSceneId(null).setName("我关注的线索").setData(jsonObject.toString()).setBydata("star").save();
            }else if(CrmEnum.CRM_CUSTOMER.getType() == type){
                systemScene.setName("全部客户").save();
                systemScene.setSceneId(null).setName("我负责的客户").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的客户").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("ro_user_id", new JSONObject().fluentPut("name", "ro_user_id").fluentPut("condition", "takePart").fluentPut("value", userId));
                systemScene.setSceneId(null).setName("我参与的客户").setData(jsonObject.toString()).save();
                jsonObject.clear();
                List<Integer> customerIdList = Db.query("select customer_id from 72crm_crm_customer_user_star where user_id = ?", userId);
                String customerIds = CollectionUtil.isEmpty(customerIdList) ? "0" : CollectionUtil.join(customerIdList, ",");
                jsonObject.fluentPut("customer_id", new JSONObject().fluentPut("name", "customer_id").fluentPut("condition", "in").fluentPut("value", customerIds));
                systemScene.setSceneId(null).setName("我关注的客户").setData(jsonObject.toString()).setBydata("star").save();
            }else if(CrmEnum.CRM_CONTACTS.getType() == type){
                systemScene.setName("全部联系人").save();
                systemScene.setSceneId(null).setName("我负责的联系人").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的联系人").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                List<Integer> contactsIdList = Db.query("select contacts_id from 72crm_crm_contacts_user_star where user_id = ?", userId);
                String contactsIds = CollectionUtil.isEmpty(contactsIdList) ? "0" : CollectionUtil.join(contactsIdList, ",");
                jsonObject.fluentPut("contacts_id", new JSONObject().fluentPut("name", "contacts_id").fluentPut("condition", "in").fluentPut("value", contactsIds));
                systemScene.setSceneId(null).setName("我关注的联系人").setData(jsonObject.toString()).setBydata("star").save();
            }else if(CrmEnum.CRM_PRODUCT.getType() == type){
                systemScene.setName("全部产品").save();
                systemScene.setSceneId(null).setName("上架的产品").setData(new JSONObject().fluentPut("是否上下架", new JSONObject().fluentPut("name", "是否上下架").fluentPut("condition", "is").fluentPut("value", "上架")).toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("是否上下架", new JSONObject().fluentPut("name", "是否上下架").fluentPut("condition", "is").fluentPut("value", "下架"));
                systemScene.setSceneId(null).setName("下架的产品").setData(jsonObject.toString()).save();
            }else if(CrmEnum.CRM_BUSINESS.getType() == type){
                systemScene.setName("全部商机").save();
                systemScene.setSceneId(null).setName("我负责的商机").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的商机").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("ro_user_id", new JSONObject().fluentPut("name", "ro_user_id").fluentPut("condition", "takePart").fluentPut("value", userId));
                systemScene.setSceneId(null).setName("我参与的商机").setData(jsonObject.toString()).save();
                jsonObject.clear();
                List<Integer> businessIdList = Db.query("select business_id from 72crm_crm_business_user_star where user_id = ?", userId);
                String businessIds = CollectionUtil.isEmpty(businessIdList) ? "0" : CollectionUtil.join(businessIdList, ",");
                jsonObject.fluentPut("business_id", new JSONObject().fluentPut("name", "business_id").fluentPut("condition", "in").fluentPut("value", businessIds));
                systemScene.setSceneId(null).setName("我关注的商机").setData(jsonObject.toString()).setBydata("star").save();
            }else if(CrmEnum.CRM_CONTRACT.getType() == type){
                systemScene.setName("全部合同").save();
                systemScene.setSceneId(null).setName("我负责的合同").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的合同").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("ro_user_id", new JSONObject().fluentPut("name", "ro_user_id").fluentPut("condition", "takePart").fluentPut("value", userId));
                systemScene.setSceneId(null).setName("我参与的合同").setData(jsonObject.toString()).save();
            }else if(CrmEnum.CRM_RECEIVABLES.getType() == type){
                systemScene.setName("全部回款").save();
                systemScene.setSceneId(null).setName("我负责的回款").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的回款").setData(subOwnerObject.toString()).save();
            } else if (CrmEnum.CRM_RETURN_VISIT.getType() == type) {
                systemScene.setName("全部回访").save();
                systemScene.setSceneId(null).setName("我负责的回访").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的回访").setData(subOwnerObject.toString()).save();
            }
        }
        return R.ok().put("data", Db.find(Db.getSql("admin.scene.queryScene"), type, userId));
    }

    /**
     * 递归查询下属id
     */
    public String getSubUserId(Long userId, Integer deepness){
        StringBuilder ids = new StringBuilder();
        if(deepness > 0){
            List<Long> list = Db.query("select user_id from 72crm_admin_user where parent_id = ?", userId);
            if(list != null && list.size() > 0){
                for(Long l : list){
                    ids.append(",").append(l).append(getSubUserId(l, deepness - 1));
                }
            }
        }
        return StrUtil.isNotEmpty(ids.toString().trim()) ? ids.toString() : ",0";
    }

    /**
     * @author wyq
     * 查询场景设置
     */
    public R querySceneConfig(AdminScene adminScene){
        Long userId = BaseUtil.getUser().getUserId();
        List<Record> valueList = Db.find(Db.getSql("admin.scene.queryScene"), adminScene.getType(), userId);
        for(Record scene : valueList){
            if(StrUtil.isNotEmpty(scene.getStr("data"))){
                JSONObject jsonObject = JSON.parseObject(scene.getStr("data"));
                scene.set("data", jsonObject);
            }
        }
        List<Record> hideValueList = Db.find(Db.getSql("admin.scene.queryHideScene"), adminScene.getType(), userId);
        for(Record hideScene : hideValueList){
            if(StrUtil.isNotEmpty(hideScene.getStr("data"))){
                JSONObject jsonObject = JSON.parseObject(hideScene.getStr("data"));
                hideScene.set("data", jsonObject);
            }
        }
        return R.ok().put("data", Kv.by("value", valueList).set("hide_value", hideValueList));
    }

    /**
     * @author wyq
     * 设置场景
     */
    @Before(Tx.class)
    public R sceneConfig(AdminScene adminScene){
        Long userId = BaseUtil.getUser().getUserId();
        String[] sortArr = adminScene.getNoHideIds().split(",");
        for(int i = 0; i < sortArr.length; i++){
            Db.update(Db.getSql("admin.scene.sort"), i + 1, adminScene.getType(), userId, sortArr[i]);
        }
        if(null != adminScene.getHideIds()){
            String[] hideIdsArr = adminScene.getHideIds().split(",");
            Record number = Db.findFirst(Db.getSqlPara("admin.scene.queryIsHideSystem", Kv.by("ids", hideIdsArr)));
            if(number.getInt("number") > 0){
                return R.error("系统场景不能隐藏");
            }
            Db.update(Db.getSqlPara("admin.scene.isHide", Kv.by("ids", hideIdsArr).set("type", adminScene.getType()).set("userId", userId)));
        }
        return R.ok();
    }
}
