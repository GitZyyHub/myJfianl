package com.kakarote.crm9.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.kakarote.crm9.common.config.cache.CaffeineCache;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminField;
import com.kakarote.crm9.erp.admin.entity.AdminScene;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.work.common.WorkRendAuthEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.ParamsUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hmb
 */
public abstract class AbstractBasePageList {


    /**
     * 获取所有的sql条件
     *
     * @param basePageRequest
     * @param crmEnum
     * @param isDataAuth
     * @return
     */
    public Kv getKv(BasePageRequest basePageRequest, CrmEnum crmEnum, boolean isDataAuth) {
        AdminUser adminUser = BaseUtil.getUser();
        Kv kv = new Kv();
        List<JSONObject> queryList = new ArrayList<>();
        JSONObject jsonObject = basePageRequest.getJsonObject();
        JSONObject sceneData = queryScene(jsonObject);
        if(sceneData.get("scene")!=null){
            sceneData = new JSONObject();
            kv.set("scene",sceneData.get("data"));//???只有任务有查询下属场景
        }
        Map<String, AdminField> fieldMap = getAdminFieldMap(crmEnum);
        if (!appendSqlCondition(kv, fieldMap, queryList, sceneData)) {
            return Kv.by("error", "参数包含非法字段");
        }
        String search = jsonObject.getString("search");
        if (StrUtil.isNotEmpty(search)) {
            if (!ParamsUtil.isValid(search)) {
                return Kv.by("error", "参数包含非法字段");
            }
            queryList.add(appendSqlSearch(search));
            if (!kv.containsKey("fixed")) {
                kv.set("fixed", true);
            }
        }
        if (!appendOrderSql(jsonObject, kv, fieldMap)) {
            return Kv.by("error", "参数包含非法字段");
        }
        String selectSql = querySelectSql();
        List<String> batchList = queryBatchId(queryList, kv, crmEnum);
        queryList.add(appendOtherSql(isDataAuth, kv, crmEnum));
        if (StrUtil.isEmpty(jsonObject.getString("excel"))) {
            kv.set("page", (basePageRequest.getPage() - 1) * basePageRequest.getLimit()).set("limit", basePageRequest.getLimit());
        }
        kv.set("select", selectSql).set("queryList", queryList).set("fieldMap", fieldMap).set("batchList", batchList).set("data",sceneData);

        return kv;
    }

    /**
     * 查询场景
     *
     * @param jsonObject
     * @return
     */
    private JSONObject queryScene(JSONObject jsonObject) {
        Integer sceneId = jsonObject.getInteger("sceneId");
        AdminScene byId = AdminScene.dao.findById(sceneId);
        try {
            JSONObject data = new JSONObject();
            if("{}".equals(byId.getData())){
                data.put("scene", "2");
                return data;
            }else if("{\"oneself\":\"oneself\"}".equals(byId.getData())){
                data.put("scene", "1");
                return data;
            }
        } catch (NullPointerException e) {

        }
        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) {
            data = new JSONObject();
        }
        if (sceneId != null && sceneId != 0) {
            String sceneData = byId.getData();
            if (!StrUtil.isEmpty(sceneData)) {
                data.putAll(JSON.parseObject(AdminScene.dao.findById(sceneId).getData()));
            }
        } else {
            data.putAll(queryDefaultScene());
        }
        return data;
    }

    /**
     * 查询默认默认场景
     *
     * @return
     */
    public JSONObject queryDefaultScene() {
        return new JSONObject();
    }


    /**
     * 查询自定义字段
     *
     * @param crmEnum
     * @return
     */
    public Map<String, AdminField> getAdminFieldMap(CrmEnum crmEnum) {
        int type = crmEnum.getType();
        if (crmEnum.equals(CrmEnum.CRM_CUSTOMER_POOL)) {
            type = CrmEnum.CRM_CUSTOMER.getType();
        }
        Map<String, AdminField> adminFieldMap = CaffeineCache.ME.get("field:", type);
        if (adminFieldMap == null) {
            List<AdminField> adminFields = AdminField.dao.find("SELECT field_name,name,field_type,type FROM 72crm_admin_field WHERE label=? and is_hidden = 0", type);
            adminFieldMap = new HashMap<>();
            for (AdminField adminField : adminFields) {
                adminFieldMap.put(adminField.getFieldName(), adminField);
                if (adminField.getType().equals(FormTypeEnum.STRUCTURE.getType()) && !adminFieldMap.containsKey("dept")) {
                    adminFieldMap.put("dept", null);
                } else if (adminField.getType().equals(FormTypeEnum.USER.getType()) && !adminFieldMap.containsKey("user")) {
                    adminFieldMap.put("user", null);
                }
            }
            AdminField adminField = new AdminField();
            adminField.setFieldType(1);
            adminFieldMap.put("owner_user_id", adminField);
            adminFieldMap.put("create_user_id", adminField);
            adminFieldMap.put("create_time", adminField);
            adminFieldMap.put("update_time", adminField);
            adminFieldMap.put("is_transform", adminField);
            adminFieldMap.put("deal_status", adminField);
            adminFieldMap.put("customer_id", adminField);
            adminFieldMap.put("contract_id", adminField);
            adminFieldMap.put("leads_id", adminField);
            adminFieldMap.put("receivables_id", adminField);
            adminFieldMap.put("check_status", adminField);
            adminFieldMap.put("received_money", adminField);
            adminFieldMap.put("unreceived_money", adminField);
            adminFieldMap.put("contacts_id", adminField);
            adminFieldMap.put("product_id", adminField);
            adminFieldMap.put("business_id", adminField);
            adminFieldMap.put("create_user_name", adminField);
            adminFieldMap.put("owner_user_name", adminField);
            adminFieldMap.put("last_content", adminField);
            adminFieldMap.put("last_time", adminField);
            CaffeineCache.ME.put("field:", type, adminFieldMap);
        }
        return adminFieldMap;
    }

    /**
     * 拼装场景和高级筛选查询sql
     *
     * @param kv
     * @param fieldMap  自定义字段
     * @param queryList 返回查询数据
     * @param data      条件数据
     * @return 非法参数校验
     */
    private boolean appendSqlCondition(Kv kv, Map<String, AdminField> fieldMap, List<JSONObject> queryList, JSONObject data) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        if (data != null) {
            data.forEach((k, v) -> jsonObjectList.add(JSON.parseObject(v.toString())));
        }
        for (JSONObject jsonObject : jsonObjectList) {
            String condition = jsonObject.getString("condition");
            String value = jsonObject.getString("value");
            String name = jsonObject.getString("name");
            StringBuilder conditions = new StringBuilder();
            JSONObject sqlObject = new JSONObject();
            if (!ParamsUtil.isValid(name)) {
                return false;
            }
            if (StrUtil.isNotEmpty(value) && !ParamsUtil.isValid(value)) {
                return false;
            }
            if (StrUtil.isNotEmpty(jsonObject.getString("start")) && !ParamsUtil.isValid(jsonObject.getString("start"))) {
                return false;
            }
            if (StrUtil.isNotEmpty(jsonObject.getString("end")) && !ParamsUtil.isValid(jsonObject.getString("end"))) {
                return false;
            }
            String formType = jsonObject.getString("formType");
            if ("business_type".equals(formType)) {
                conditions.append(" and ").append(name).append(" = ").append(jsonObject.getInteger("typeId"));
                if (StrUtil.isNotEmpty(jsonObject.getString("statusId"))) {
                    if ("win".equals(jsonObject.getString("statusId"))) {
                        conditions.append(" and is_end = 1");
                    } else if ("lose".equals(jsonObject.getString("statusId"))) {
                        conditions.append(" and is_end = 2");
                    } else if ("invalid".equals(jsonObject.getString("statusId"))) {
                        conditions.append(" and is_end = 3");
                    } else {
                        conditions.append(" and status_id = ").append(jsonObject.getString("statusId")).append(" and is_end = 0");
                    }
                }
                sqlObject.put("type", 1);
            } else if ("map_address".equals(formType)) {
                String address = value.substring(0, value.length() - 1);
                conditions.append(" and ").append(name).append(" like '%").append(address).append("%'");
                sqlObject.put("type", 1);
            } else {
                //判断不为空和为空不校验value
                boolean isNull = Arrays.asList("isNull", "isNotNull").contains(condition);
                //时间处不校验value
                boolean isTime = StrUtil.isNotEmpty(jsonObject.getString("start")) || StrUtil.isNotEmpty(jsonObject.getString("end"));
                if (StrUtil.isNotEmpty(value) || isNull || isTime) {
                    if ("takePart".equals(condition)) {
                        conditions.append(" and (ro_user_id like '%,").append(value).append(",%' or rw_user_id like '%,").append(value).append(",%')");
                        sqlObject.put("type", 1);
                    } else {
                        sqlObject.put("name", name);
                        conditions.append(" and ").append(name);
                        if ("datetime".equals(formType)) {
                            conditions.append(" between '").append(jsonObject.getString("start")).append("' and '").append(jsonObject.getString("end")).append("'");
                            sqlObject.put("connector", "between");
                            sqlObject.put("value", "'" + jsonObject.getString("start") + "'" + " and '" + jsonObject.getString("end") + "'");
                        } else if ("date".equals(formType)) {
                            conditions.append(" between '").append(jsonObject.getString("start")).append("' and '").append(jsonObject.getString("end")).append("'");
                            sqlObject.put("connector", "between");
                            sqlObject.put("value", "'" + jsonObject.getString("start") + "'" + " and '" + jsonObject.getString("end") + "'");
                        } else if ("checkbox".equals(formType)) {
                            if ("contains".equals(condition)) {
                                String[] options = value.split(",");
                                StringBuilder sqlForQueryBatchId = new StringBuilder(" is not null");
                                for (int i = 0; i < options.length; i++) {
                                    String option = options[i];
                                    if (i == 0) {
                                        conditions.append(" is not null and find_in_set('").append(option).append("',").append(name).append(")");
                                        //为queryBatchId方法做的特殊处理，该方法字段名统一为value
                                        sqlForQueryBatchId.append(" and find_in_set('").append(option).append("',value)");
                                    } else {
                                        conditions.append(" and find_in_set('").append(option).append("',").append(name).append(")");
                                        sqlForQueryBatchId.append(" and find_in_set('").append(option).append("',value)");
                                    }
                                }
                                conditions.append(")");
                                sqlObject.put("connector", "");
                                sqlObject.put("value", sqlForQueryBatchId.toString());
                            } else if ("is".equals(condition)) {
                                //数据库是按固定顺序排序后存储，此处做同样处理即可精确匹配
                                List<String> optionList = Arrays.stream(value.split(",")).collect(Collectors.toList());
                                CollectionUtil.sortByPinyin(optionList);
                                conditions.append(" = '").append(CollectionUtil.join(optionList, ",")).append("'");
                                sqlObject.put("connector", "=");
                                sqlObject.put("value", "'" + CollectionUtil.join(optionList, ",") + "'");
                            }
                        } else if ("is".equals(condition)) {
                            conditions.append(" = '").append(value).append("'");
                            sqlObject.put("connector", "=");
                            sqlObject.put("value", "'" + value + "'");
                        } else if ("isNot".equals(condition)) {
                            conditions.append(" != '").append(value).append("'");
                            sqlObject.put("connector", "!=");
                            sqlObject.put("value", "'" + value + "'");
                        } else if ("contains".equals(condition)) {
                            conditions.append(" like '%").append(value).append("%'");
                            sqlObject.put("connector", "like");
                            sqlObject.put("value", "'%" + value + "%'");
                        } else if ("notContains".equals(condition)) {
                            conditions.append(" not like '%").append(value).append("%'");
                            sqlObject.put("connector", "not like");
                            sqlObject.put("value", "'%" + value + "%'");
                        } else if ("isNull".equals(condition)) {
                            conditions.append(" is null");
                            sqlObject.put("connector", "is null");
                            sqlObject.put("value", "");
                        } else if ("isNotNull".equals(condition)) {
                            conditions.append(" is not null");
                            sqlObject.put("connector", "is not null");
                            sqlObject.put("value", "");
                        } else if ("gt".equals(condition)) {
                            conditions.append(" > ").append(value);
                            sqlObject.put("connector", ">");
                            sqlObject.put("value", value);
                        } else if ("egt".equals(condition)) {
                            conditions.append(" >= ").append(value);
                            sqlObject.put("connector", ">=");
                            sqlObject.put("value", value);
                        } else if ("lt".equals(condition)) {
                            conditions.append(" < ").append(value);
                            sqlObject.put("connector", "<");
                            sqlObject.put("value", value);
                        } else if ("elt".equals(condition)) {
                            conditions.append(" <= ").append(value);
                            sqlObject.put("connector", "<=");
                            sqlObject.put("value", value);
                        } else if ("in".equals(condition)) {
                            conditions.append(" in (").append(value).append(")");
                            sqlObject.put("connector", "in");
                            sqlObject.put("value", "(" + value + ")");
                        } else if ("not in".equals(condition)) {
                            conditions.append("not in (").append(value).append(")");
                            sqlObject.put("connector", "in");
                            sqlObject.put("value", "(" + value + ")");
                        }
                        // 关联表的字段 fieldType设为3
                        sqlObject.put("type", fieldMap.get(name) != null ? fieldMap.get(name).getFieldType() : 3);
                    }
                } else {
                    continue;
                }
            }
            sqlObject.put("sql", conditions.toString());
            if (sqlObject.get("type").equals(1) && !kv.containsKey("fixed")) {
                kv.put("fixed", true);
            } else if (sqlObject.get("type").equals(0) && !kv.containsKey("field")) {
                kv.put("field", true);
            }
            queryList.add(sqlObject);
        }
        return true;
    }

    /**
     * 拼装搜索框数据
     *
     * @param search
     * @return
     */
    public abstract JSONObject appendSqlSearch(String search);

    /**
     * 拼装其他条件（权限、自定义扩展）
     *
     * @return
     */
    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject sqlObject = new JSONObject();
        StringBuilder conditions = new StringBuilder();
        if (isDataAuth) {
            conditions.append(AuthUtil.getCrmAuthSql(crmEnum, 1));
        }
        sqlObject.put("sql", conditions.toString());
        sqlObject.put("type", 1);
        if (!kv.containsKey("fixed")) {
            kv.set("fixed", true);
        }
        return sqlObject;
    }

    /**
     * 拼装排序sql
     */
    private boolean appendOrderSql(JSONObject jsonObject, Kv kv, Map<String, AdminField> fieldMap) {
        String camelField = jsonObject.getString("sortField");
        AdminField adminField = fieldMap.get(StrUtil.toUnderlineCase(camelField));
        //adminField为空代表排序字段是关联表中字段，不是当前表里的，所以也要转为下划线命名
        String sortField = ObjectUtil.isEmpty(adminField) || Objects.equals(1, adminField.getFieldType()) ? StrUtil.toUnderlineCase(camelField) : camelField;
        String orderNum = jsonObject.getString("order");
        if (!ParamsUtil.isValid(sortField)) {
            return false;
        } else {
            if (StrUtil.isEmpty(sortField) || StrUtil.isEmpty(orderNum)) {
                kv.set("orderByKey", "update_time").set("orderByType", "desc").set("fieldType", 1);
            } else {
                orderNum = "2".equals(orderNum) ? "asc" : "desc";
                int fieldType=fieldMap.get(sortField) != null ? fieldMap.get(sortField).getFieldType() : 0;
                kv.set("orderByKey", fieldType==1?sortField:"update_time").set("orderByType", orderNum).set("fieldType", 1);
            }
            return true;
        }
    }

    /**
     * 查询sql
     *
     * @return
     */
    public String querySelectSql() {
        return "select * ";
    }

    /**
     * 自定义字段的查询
     *
     * @author zhangzhiwei
     */
    private List<String> queryBatchId(List<JSONObject> queryList, Kv kv, CrmEnum crmEnum) {
        List<String> arrayList = new ArrayList<>();
        queryList.forEach(jsonObject -> {
            if (jsonObject.containsKey("type") && (jsonObject.get("type").equals(0) || jsonObject.get("type").equals(2))) {
                JSONObject object = (JSONObject) jsonObject.clone();
                object.put("batchList", arrayList);
                object.put("dataTableName", crmEnum.getDataTableName());
                SqlPara sqlPara;
                if (jsonObject.get("type").equals(0)) {
                    sqlPara = Db.getSqlPara("admin.scene.queryBatchId1", object);
                } else {
                    Kv paras = Kv.by("dataTableName", crmEnum.getDataTableName()).set("label", crmEnum.getType()).set("name", object.getString("name"))
                            .set("connector", object.getString("connector"))
                            .set("value", object.getString("value")).set("batchList", arrayList);
                    sqlPara = Db.getSqlPara("admin.scene.queryBatchId2", paras);
                }
                arrayList.clear();
                List<String> batchIdList = Db.query(sqlPara.getSql(), sqlPara.getPara());
                if (batchIdList.size() == 0) {
                    arrayList.add("");
                } else {
                    arrayList.addAll(batchIdList);
                }
            }
        });
        if (arrayList.size() == 0 && kv.containsKey("field")) {
            arrayList.add("");
        }
        return arrayList;
    }

    /**
     * 数据权限-查询
     * @param workUserEnum 关联的模块
     * @param kv           条件列表
     * @param crmEnum       查询的所属模块
     * @param workId        项目id
     * @param workRendAuthEnum  查询权限ID
     * @param integer       是否为空（查询下属）
     */
    public void queryDataAuth(WorkUserEnum workUserEnum, Kv kv, CrmEnum crmEnum, Integer workId, WorkRendAuthEnum workRendAuthEnum,Integer integer){
        AdminUser user = BaseUtil.getUser();
        List<Integer> roles = user.getRoles();
        Long userId = user.getUserId();
        if (BaseUtil.getSuperUser().equals(userId) || roles.contains(BaseUtil.getSuperRole()) || crmEnum.equals(CrmEnum.CRM_PRODUCT) || crmEnum.equals(CrmEnum.CRM_CUSTOMER_POOL)) {
            if(integer == null){
                integer = 5;
            }
        }
        if(integer == null){
            integer = Db.queryInt(Db.getSql("admin.role.queryRoleDataType"), userId, workRendAuthEnum.getMenuId());
        }else {
            Integer role = Db.queryInt("SELECT DISTINCT max(b.data_type) data_type FROM 72crm_admin_user_role AS a JOIN 72crm_admin_role AS b ON a.role_id=b.role_id AND user_id=?", userId);
            if(role < integer){
                integer = role;
            }
        }
        if(integer == 2||integer==1){
            List<Object> strings = null;
            if(integer == 2) {
                String subUserId = getSubUserId(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                strings = Arrays.asList(subUserId.split(","));
            }else if(integer == 1) {
                strings = Arrays.asList(userId);
            }
            strings.removeAll(Collections.singleton(""));
            Kv set = Kv.by("ids", strings).set("ty", workUserEnum.getType());
            if(workId!=null){
                set.set("workId", workId);
            }
            List<Record> records = Db.find(Db.getSqlPara("work.queryDataAuth", set));
            List<Integer> numId = records.stream().map(record -> {
                return record.getInt("num_id");
            }).collect(Collectors.toList());
            kv.set("dataAuth",numId);
        }else if(integer == 5){
            return;
        }else {
            kv = null;
        }
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
        return StrUtil.isNotEmpty(ids.toString().trim()) ? ids.toString() : userId.toString();
    }

}



/*
package com.kakarote.crm9.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.kakarote.crm9.common.config.cache.CaffeineCache;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.enums.CrmEnum;
import com.kakarote.crm9.erp.admin.entity.AdminField;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;

import java.util.*;
import java.util.stream.Collectors;

*/
/**
 * @author zyy
 *//*

public abstract class AbstractBasePageList {

    public Kv getKv(BasePageRequest basePageRequest, CrmEnum crmEnum, boolean isDataAuth){

        JSONObject jsonObject = basePageRequest.getJsonObject();
        //查询某个模块所有字段
        List<AdminField> adminFields = getAdminFieldList(crmEnum);

        Map<String, AdminField> fields = new HashMap<>();
        Map<String, AdminField> fixed = new HashMap<>();
        List<Map<String,Object>> sortField = new ArrayList<>();

        Kv kv = new Kv();
        List<JSONObject> queryList = new ArrayList<>();
        Map<String, AdminField> fieldMap = getAdminFieldMap(crmEnum);
        JSONObject sceneData = new JSONObject();

        for (AdminField field : adminFields) {
            //如果是自定义字段
            if(field.getFieldType() == 0){
                fields.put(field.getFieldName(),field);
            }else {
                fixed.put(field.getFieldName(),field);
            }
            //高级查询条件
            JSONObject object = jsonObject.getJSONObject("data").getJSONObject(field.getFieldName());
            if(object != null){
                sortField.add(object);
                appendSqlCondition(kv,queryList,object,fixed);
            }
        }

        String search = jsonObject.getString("search");
        //普通模糊搜索
        if (StrUtil.isNotEmpty(search)) {
            if (!ParamsUtil.isValid(search)) {
                return Kv.by("error", "参数包含非法字段");
            }
            queryList.add(appendSqlSearch(search));
        }
        //排序
        if (!appendOrderSql(jsonObject, kv, fieldMap)) {
            return Kv.by("error", "参数包含非法字段");
        }
        queryList.add(appendOtherSql(isDataAuth, kv, crmEnum));
        kv.set("page", (basePageRequest.getPage() - 1) * basePageRequest.getLimit()).set("limit", basePageRequest.getLimit());
        kv.set("queryList", queryList).set("adminFields", adminFields).set("data",sceneData)
                .set("table",crmEnum.getTableName()).set("tableField",crmEnum.getDataTableName())
                .set("fields",fields);

        return kv;
    }

    public boolean appendSqlCondition(Kv kv,  List<JSONObject> queryList, JSONObject jsonObject,Map<String, AdminField> fixed){

            String condition = jsonObject.getString("condition");
            String value = jsonObject.getString("value");
            String name = jsonObject.getString("name");
            StringBuilder conditions = new StringBuilder();
            JSONObject sqlObject = new JSONObject();
            if (!ParamsUtil.isValid(name)) {
                return false;
            }
            if (StrUtil.isNotEmpty(value) && !ParamsUtil.isValid(value)) {
                return false;
            }
            if (StrUtil.isNotEmpty(jsonObject.getString("start")) && !ParamsUtil.isValid(jsonObject.getString("start"))) {
                return false;
            }
            if (StrUtil.isNotEmpty(jsonObject.getString("end")) && !ParamsUtil.isValid(jsonObject.getString("end"))) {
                return false;
            }
            String formType = jsonObject.getString("formType");
            if ("business_type".equals(formType)) {
                conditions.append(" and ").append(name).append(" = ").append(jsonObject.getInteger("typeId"));
                if (StrUtil.isNotEmpty(jsonObject.getString("statusId"))) {
                    if ("win".equals(jsonObject.getString("statusId"))) {
                        conditions.append(" and is_end = 1");
                    } else if ("lose".equals(jsonObject.getString("statusId"))) {
                        conditions.append(" and is_end = 2");
                    } else if ("invalid".equals(jsonObject.getString("statusId"))) {
                        conditions.append(" and is_end = 3");
                    } else {
                        conditions.append(" and status_id = ").append(jsonObject.getString("statusId")).append(" and is_end = 0");
                    }
                }
                sqlObject.put("type", 1);
            } else if ("map_address".equals(formType)) {
                String address = value.substring(0, value.length() - 1);
                conditions.append(" and ").append(name).append(" like '%").append(address).append("%'");
                sqlObject.put("type", 1);
            } else {
                //判断不为空和为空不校验value
                boolean isNull = Arrays.asList("isNull", "isNotNull").contains(condition);
                //时间处不校验value
                boolean isTime = StrUtil.isNotEmpty(jsonObject.getString("start")) || StrUtil.isNotEmpty(jsonObject.getString("end"));
                if (StrUtil.isNotEmpty(value) || isNull || isTime) {
                    if ("takePart".equals(condition)) {
                        conditions.append(" and (ro_user_id like '%,").append(value).append(",%' or rw_user_id like '%,").append(value).append(",%')");
                        sqlObject.put("type", 1);
                    } else {
                        sqlObject.put("name", name);
                        conditions.append(" and ").append(name);
                        if ("datetime".equals(formType)) {
                            conditions.append(" between '").append(jsonObject.getString("start")).append("' and '").append(jsonObject.getString("end")).append("'");
                            sqlObject.put("connector", "between");
                            sqlObject.put("value", "'" + jsonObject.getString("start") + "'" + " and '" + jsonObject.getString("end") + "'");
                        } else if ("date".equals(formType)) {
                            conditions.append(" between '").append(jsonObject.getString("start")).append("' and '").append(jsonObject.getString("end")).append("'");
                            sqlObject.put("connector", "between");
                            sqlObject.put("value", "'" + jsonObject.getString("start") + "'" + " and '" + jsonObject.getString("end") + "'");
                        } else if ("checkbox".equals(formType)) {
                            if ("contains".equals(condition)) {
                                String[] options = value.split(",");
                                StringBuilder sqlForQueryBatchId = new StringBuilder(" is not null");
                                for (int i = 0; i < options.length; i++) {
                                    String option = options[i];
                                    if (i == 0) {
                                        conditions.append(" is not null and find_in_set('").append(option).append("',").append(name).append(")");
                                        //为queryBatchId方法做的特殊处理，该方法字段名统一为value
                                        sqlForQueryBatchId.append(" and find_in_set('").append(option).append("',value)");
                                    } else {
                                        conditions.append(" and find_in_set('").append(option).append("',").append(name).append(")");
                                        sqlForQueryBatchId.append(" and find_in_set('").append(option).append("',value)");
                                    }
                                }
                                conditions.append(")");
                                sqlObject.put("connector", "");
                                sqlObject.put("value", sqlForQueryBatchId.toString());
                            } else if ("is".equals(condition)) {
                                //数据库是按固定顺序排序后存储，此处做同样处理即可精确匹配
                                List<String> optionList = Arrays.stream(value.split(",")).collect(Collectors.toList());
                                CollectionUtil.sortByPinyin(optionList);
                                conditions.append(" = '").append(CollectionUtil.join(optionList, ",")).append("'");
                                sqlObject.put("connector", "=");
                                sqlObject.put("value", "'" + CollectionUtil.join(optionList, ",") + "'");
                            }
                        } else if ("is".equals(condition)) {
                            conditions.append(" = '").append(value).append("'");
                            sqlObject.put("connector", "=");
                            sqlObject.put("value", "'" + value + "'");
                        } else if ("isNot".equals(condition)) {
                            conditions.append(" != '").append(value).append("'");
                            sqlObject.put("connector", "!=");
                            sqlObject.put("value", "'" + value + "'");
                        } else if ("contains".equals(condition)) {
                            conditions.append(" like '%").append(value).append("%'");
                            sqlObject.put("connector", "like");
                            sqlObject.put("value", "'%" + value + "%'");
                        } else if ("notContains".equals(condition)) {
                            conditions.append(" not like '%").append(value).append("%'");
                            sqlObject.put("connector", "not like");
                            sqlObject.put("value", "'%" + value + "%'");
                        } else if ("isNull".equals(condition)) {
                            conditions.append(" is null");
                            sqlObject.put("connector", "is null");
                            sqlObject.put("value", "");
                        } else if ("isNotNull".equals(condition)) {
                            conditions.append(" is not null");
                            sqlObject.put("connector", "is not null");
                            sqlObject.put("value", "");
                        } else if ("gt".equals(condition)) {
                            conditions.append(" > ").append(value);
                            sqlObject.put("connector", ">");
                            sqlObject.put("value", value);
                        } else if ("egt".equals(condition)) {
                            conditions.append(" >= ").append(value);
                            sqlObject.put("connector", ">=");
                            sqlObject.put("value", value);
                        } else if ("lt".equals(condition)) {
                            conditions.append(" < ").append(value);
                            sqlObject.put("connector", "<");
                            sqlObject.put("value", value);
                        } else if ("elt".equals(condition)) {
                            conditions.append(" <= ").append(value);
                            sqlObject.put("connector", "<=");
                            sqlObject.put("value", value);
                        } else if ("in".equals(condition)) {
                            conditions.append(" in (").append(value).append(")");
                            sqlObject.put("connector", "in");
                            sqlObject.put("value", "(" + value + ")");
                        } else if ("not in".equals(condition)) {
                            conditions.append("not in (").append(value).append(")");
                            sqlObject.put("connector", "in");
                            sqlObject.put("value", "(" + value + ")");
                        }
                        // 关联表的字段 fieldType设为3
                        sqlObject.put("type", fixed.get(name) != null ? fixed.get(name).getFieldType() : 3);
                    }
                } else {
                    return true;
                }
            }
            sqlObject.put("sql", conditions.toString());

            queryList.add(sqlObject);

            return true;

    }

    */
/**
     * 查询自定义字段
     *
     * @param crmEnum
     * @return
     *//*

    public Map<String, AdminField> getAdminFieldMap(CrmEnum crmEnum) {
        int type = crmEnum.getType();

        Map<String, AdminField> adminFieldMap = new HashMap<>();
        if (adminFieldMap == null || adminFieldMap.size() == 0 ) {
            List<AdminField> adminFields = AdminField.dao.find("SELECT field_name,name,field_type,type FROM 72crm_admin_field WHERE label=? and is_hidden = 0", type);
            adminFieldMap = new HashMap<>();
            for (AdminField adminField : adminFields) {
                adminFieldMap.put(adminField.getFieldName(), adminField);
            }
        }
        return adminFieldMap;
    }

    */
/**
     * 拼装其他条件（权限、自定义扩展）
     *
     * @return
     *//*

    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject sqlObject = new JSONObject();
        StringBuilder conditions = new StringBuilder();
        sqlObject.put("sql", conditions.toString());
        sqlObject.put("type", 1);
        return sqlObject;
    }

    */
/**
     * 拼装排序sql
     *//*

    private boolean appendOrderSql(JSONObject jsonObject, Kv kv, Map<String, AdminField> fieldMap) {
        String camelField = jsonObject.getString("sortField");
        AdminField adminField = fieldMap.get(StrUtil.toUnderlineCase(camelField));
        //adminField为空代表排序字段是关联表中字段，不是当前表里的，所以也要转为下划线命名
        String sortField = ObjectUtil.isEmpty(adminField) || Objects.equals(1, adminField.getFieldType()) ? StrUtil.toUnderlineCase(camelField) : camelField;
        String orderNum = jsonObject.getString("order");
        if (!ParamsUtil.isValid(sortField)) {
            return false;
        } else {
            if (StrUtil.isEmpty(sortField) || StrUtil.isEmpty(orderNum)) {
                kv.set("orderByKey", "create_time").set("orderByType", "desc").set("fieldType", 1);
            } else {
                orderNum = "2".equals(orderNum) ? "asc" : "desc";
                int fieldType=fieldMap.get(sortField) != null ? fieldMap.get(sortField).getFieldType() : 0;
                kv.set("orderByKey", fieldType==1?sortField:"create_time").set("orderByType", orderNum).set("fieldType", 1);
            }
            return true;
        }
    }

    */
/**
     * 拼装搜索框数据
     *
     * @param search
     * @return
     *//*

    public abstract JSONObject appendSqlSearch(String search);

    */
/**
     * 查询某个模块所有字段
     * @param crmEnum
     * @return
     *//*

    public List<AdminField> getAdminFieldList(CrmEnum crmEnum){
        return AdminField.dao.find("SELECT field_name,name,field_type,type FROM 72crm_admin_field WHERE label=? and is_hidden = 0", crmEnum.getType());
    }


}
*/
