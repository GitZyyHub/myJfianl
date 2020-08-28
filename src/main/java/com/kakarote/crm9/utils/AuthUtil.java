package com.kakarote.crm9.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.oa.common.OaEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wyq
 */
public class AuthUtil {

    public static Map<String, String> getCrmTablePara(CrmEnum Enum) {
        Map<String, String> tableParaMap = new HashMap<>();
        switch (Enum) {
            case CRM_CUSTOMER:
            case CRM_CUSTOMER_POOL:
                tableParaMap.put("tableName", "72crm_crm_customer");
                tableParaMap.put("tableId", "customer_id");
                break;
            case CRM_LEADS:
                tableParaMap.put("tableName", "72crm_crm_leads");
                tableParaMap.put("tableId", "leads_id");
                break;
            case CRM_CONTRACT:
                tableParaMap.put("tableName", "72crm_crm_contract");
                tableParaMap.put("tableId", "contract_id");
                break;
            case CRM_CONTACTS:
                tableParaMap.put("tableName", "72crm_crm_contacts");
                tableParaMap.put("tableId", "contacts_id");
                break;
            case CRM_BUSINESS:
                tableParaMap.put("tableName", "72crm_crm_business");
                tableParaMap.put("tableId", "business_id");
                break;
            case CRM_RECEIVABLES:
                tableParaMap.put("tableName", "72crm_crm_receivables");
                tableParaMap.put("tableId", "receivables_id");
                break;
            case CRM_PRODUCT:
                tableParaMap.put("tableName", "72crm_crm_product");
                tableParaMap.put("tableId", "product_id");
                break;
            case CRM_MARKETING:
                tableParaMap.put("tableName", "72crm_crm_marketing");
                tableParaMap.put("tableId", "marketing_id");
                break;
            default:
                return tableParaMap;
        }
        return tableParaMap;
    }

    public static boolean isCrmAuth(CrmEnum crmEnum, Integer id) {
        String authSql = "select count(*) from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = " + id +
                getCrmAuthSql(crmEnum, 1);
        return Db.queryInt(authSql) == 0;
    }

    public static boolean isCrmOperateAuth(CrmEnum crmEnum, Integer id) {
        String authSql = "select count(*) from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = " + id +
                getCrmAuthSql(crmEnum, 0);
        return Db.queryInt(authSql) == 0;
    }

    public static boolean oaAuth(Record record) {
        boolean auth = false;
        if (record.getStr("business_ids") != null) {
            auth = isCrmAuth(CrmEnum.CRM_BUSINESS, Integer.valueOf(record.getStr("business_ids")));
        } else if (record.getStr("contacts_ids") != null) {
            auth = isCrmAuth(CrmEnum.CRM_CONTACTS, Integer.valueOf(record.getStr("contacts_ids")));
        } else if (record.getStr("contract_ids") != null) {
            auth = isCrmAuth(CrmEnum.CRM_CONTRACT, Integer.valueOf(record.getStr("contract_ids")));
        } else if (record.getStr("customer_ids") != null) {
            auth = isCrmAuth(CrmEnum.CRM_CUSTOMER, Integer.valueOf(record.getStr("customer_ids")));
        }
        return auth;
    }

    public static boolean isOaAuth(OaEnum oaEnum, Integer id) {
        if (id == null) {
            return false;
        }
        Map<String, String> tablePara = getOaTablePara(oaEnum);
        Long userId = BaseUtil.getUserId();
        List<Integer> roleIds = Aop.get(AdminRoleService.class).queryRoleIdsByUserId(userId);

        //超级管理员角色拥有最高权限
        if (roleIds.contains(BaseUtil.getSuperRole()) || userId.equals(BaseUtil.getSuperUser())) {
            return false;
        }
        if (tablePara == null) {
            List<Long> userIds = Db.query("select create_user_id from `72crm_oa_examine` where examine_id = ? union all select b.examine_user from `72crm_oa_examine_record` as a left join `72crm_oa_examine_log` b on a.record_id = b.record_id where a.examine_id = ?", id, id);
            return !userIds.contains(userId);
        }
        StringBuilder authSql = new StringBuilder("select count(*) from  ");
        if ("72crm_task".equals(tablePara.get("tableName"))) {
            if (roleIds.contains(BaseUtil.getWorkAdminRole())) {
                return false;
            }
            List<Long> childIdList = Aop.get(AdminUserService.class).queryChileUserIds(userId, 20);
            authSql.append(tablePara.get("tableName")).append(" where ").append(tablePara.get("tableId")).append(" = ").append(id);
            if (childIdList != null && childIdList.size() > 0) {
                authSql.append(" and (").append(" (owner_user_id like CONCAT('%,','").append(userId).append("',',%')").append(" or main_user_id = ").append(userId).append(") ");
                childIdList.forEach(childId -> {
                    authSql.append(" or (owner_user_id like CONCAT('%,','").append(childId).append("',',%')").append(" or main_user_id = ").append(childId).append(") ");
                });
                authSql.append(") ");
            } else {
                authSql.append(" and ").append(" (owner_user_id like CONCAT('%,','").append(userId).append("',',%')").append(" or main_user_id = ").append(userId).append(") ");
            }
        } else if ("72crm_task_comment".equals(tablePara.get("tableName"))) {
            authSql.append(tablePara.get("tableName")).append(" where ").append(tablePara.get("tableId")).append(" = ").append(id).append(" and user_id = ").append(userId);
        } else {
            authSql.append(tablePara.get("tableName")).append(" where ").append(tablePara.get("tableId")).append(" = ").append(id).append(" and create_user_id = ").append(userId);
        }
        return Db.queryInt(authSql.toString()) == 0;
    }

    private static Map<String, String> getOaTablePara(OaEnum oaEnum) {
        Map<String, String> tableParaMap = new HashMap<>(2);
        switch (oaEnum) {
            case LOG_TYPE_KEY:
                tableParaMap.put("tableName", "72crm_oa_log");
                tableParaMap.put("tableId", "log_id");
                break;
            case EVENT_TYPE_KEY:
                tableParaMap.put("tableName", "72crm_oa_event");
                tableParaMap.put("tableId", "event_id");
                break;
            case ANNOUNCEMENT_TYPE_KEY:
                tableParaMap.put("tableName", "72crm_oa_announcement");
                tableParaMap.put("tableId", "announcement_id");
                break;
            case TASK_TYPE_KEY:
                tableParaMap.put("tableName", "72crm_task");
                tableParaMap.put("tableId", "task_id");
                break;
            case EXAMINE_TYPE_KEY:
                tableParaMap.put("tableName", "72crm_oa_examine");
                tableParaMap.put("tableId", "examine_id");
                break;
            case COMMENT_TYPE_KEY:
                tableParaMap.put("tableName", "72crm_task_comment");
                tableParaMap.put("tableId", "comment_id");
                break;
            default:
                return null;
        }
        return tableParaMap;
    }


    public static boolean isWorkAdmin() {
        if (BaseUtil.getUserId().equals(BaseUtil.getSuperUser())) {
            return true;
        } else {
            return BaseUtil.getUser().getRoles().contains(BaseUtil.getWorkAdminRole()) || BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole());
        }
    }

    public static boolean isWorkAuth(String workId, String realm) {
        AdminUser user = BaseUtil.getUser();
        Integer roleId = Db.queryInt("select role_id from `72crm_work_user` where work_id = ? and user_id = ?", workId, user.getUserId());
        //判断是否是超级管理员、项目管理员
        if (user.getUserId().equals(BaseUtil.getSuperUser()) || user.getRoles().contains(BaseUtil.getSuperRole()) || user.getRoles().contains(BaseUtil.getWorkAdminRole()) || (roleId != null && roleId.equals(BaseUtil.getSmallAdminRole()))) {
            return true;
        } else {
            List<String> realmData = Db.query(Db.getSql("work.queryRealmByRoleId"), roleId);
            if (realmData == null || realmData.size() == 0) {
                return false;
            }
            return realmData.contains(realm);
        }
    }

    /**
     * 团队成员是否有操作权限
     */
    public static boolean isRwAuth(Integer id, CrmEnum crmEnum) {
        String authSql = "select count(*) from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = " + id +
                getCrmAuthSql(crmEnum, 0);
        return Db.queryInt(authSql) == 0;
    }

    /**
     * 客户接口判断公海不需要拦截
     *
     * @param customerId
     * @return
     */
    public static boolean isPoolAuth(Integer customerId) {
        Integer ownerUserId = Db.queryInt("select owner_user_id from `72crm_crm_customer` where customer_id = ?", customerId);
        if (ownerUserId == null) {
            return false;
        } else {
            return AuthUtil.isCrmAuth(CrmEnum.CRM_CUSTOMER, customerId);
        }
    }

    /**
     * 根据当前用户的本人及下属userId取交集
     *
     * @param userIds
     * @return
     */
    public static String filterUserId(String userIds) {
        Long userId = BaseUtil.getUserId();
        if (userId.equals(BaseUtil.getSuperUser()) || BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole())) {
            return userIds;
        }
        List<Long> userIdList = StrUtil.splitTrim(userIds, ",").stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Long> subUserIdList = Aop.get(AdminUserService.class).queryChileUserIds(userId, BaseConstant.AUTH_DATA_RECURSION_NUM);
        subUserIdList.add(userId);
        userIdList.retainAll(subUserIdList);
        return CollectionUtil.join(userIdList, ",");
    }

    public static List<Long> filterUserId(List<Long> userIdList) {
        Long userId = BaseUtil.getUserId();
        if (userId.equals(BaseUtil.getSuperUser()) || BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole())) {
            return userIdList;
        }
        List<Long> subUserIdList = Aop.get(AdminUserService.class).queryChileUserIds(userId, BaseConstant.AUTH_DATA_RECURSION_NUM);
        subUserIdList.add(userId);
        userIdList.retainAll(subUserIdList);
        return userIdList;
    }

    public static String filterDeptId(String deptIds) {
        if (BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) || BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole())) {
            return deptIds;
        }
        List<Integer> deptIdList = Arrays.stream(deptIds.split(",")).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        Integer deptId = BaseUtil.getUser().getDeptId();
        List<Integer> subDeptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
        subDeptIdList.add(deptId);
        deptIdList.retainAll(subDeptIdList);
        return CollectionUtil.join(deptIdList, ",");
    }

    public static boolean isSubDept(Integer deptId) {
        List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(BaseUtil.getUser().getDeptId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
        return deptId.equals(BaseUtil.getUser().getDeptId()) || deptIdList.contains(deptId);
    }

    public static boolean isSubUser(Long userId) {
        if (!BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()) && !BaseUtil.getUserId().equals(BaseUtil.getSuperUser())) {
            List<Long> userIdList = Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
            return userId.equals(BaseUtil.getUserId()) || userIdList.contains(userId);
        }
        return true;
    }

    public static List<String> queryAuth(Map<String, Object> map, String key) {
        List<String> permissions = new ArrayList<>();
        map.keySet().forEach(str -> {
            if (map.get(str) instanceof Map) {
                permissions.addAll(queryAuth((Map<String, Object>) map.get(str), key + str + ":"));
            } else {
                permissions.add(key + str);
            }
        });
        return permissions;
    }

    /**
     * 拼客户管理数据权限sql
     *
     * @param crmEnum
     * @return
     */
    public static String getCrmAuthSql(CrmEnum crmEnum, String alias, Integer readOnly) {
        AdminUser user = BaseUtil.getUser();
        List<Integer> roles = user.getRoles();
        Long userId = user.getUserId();
        if (BaseUtil.getSuperUser().equals(userId) || roles.contains(BaseUtil.getSuperRole()) || crmEnum.equals(CrmEnum.CRM_PRODUCT) || crmEnum.equals(CrmEnum.CRM_CUSTOMER_POOL)) {
            return "";
        }
        String tableName = crmEnum.getTableName();
        StringBuilder conditions = new StringBuilder();
        List<Long> longs = Aop.get(AdminUserService.class).queryUserByAuth(userId, StrUtil.subAfter(tableName, "72crm_crm_", false));
        if (longs != null && longs.size() > 0) {
            if (crmEnum.equals(CrmEnum.CRM_MARKETING)) {
                conditions.append(" and (");
                longs.forEach(id -> conditions.append(" {alias}owner_user_id like CONCAT('%,','").append(id).append("',',%') or ").append("  {alias}relation_user_id like CONCAT('%,','").append(id).append("',',%') or"));
                conditions.delete(conditions.length()-2,conditions.length());
            } else {
                conditions.append(" and ({alias}owner_user_id in (").append(StrUtil.join(",", longs)).append(")");
                if (crmEnum.equals(CrmEnum.CRM_CUSTOMER) || crmEnum.equals(CrmEnum.CRM_BUSINESS) || crmEnum.equals(CrmEnum.CRM_CONTRACT)) {
                    if (readOnly != null && 0 == readOnly) {
                        conditions.append(" or {alias}rw_user_id like CONCAT('%,','").append(userId).append("',',%')");
                    } else {
                        conditions.append(" or {alias}ro_user_id like CONCAT('%,','").append(userId).append("',',%')").append(" or {alias}rw_user_id like CONCAT('%,','").append(userId).append("',',%')");
                    }
                }
            }
            conditions.append(")");
        }
        Map<String, String> map = new HashMap<>();
        if (StrUtil.isEmpty(alias)) {
            map.put("alias", "");
        } else {
            map.put("alias", alias + ".");
        }
        return StrUtil.format(conditions.toString(), map);
    }

    public static String getCrmAuthSql(CrmEnum crmEnum, Integer readOnly) {
        return getCrmAuthSql(crmEnum, "", readOnly);
    }

    /**
     * 根据数据权限查询权限范围内员工
     */
    public static List<Long> getUserIdByAuth(Integer menuId) {
        List<Long> authUserIdList = new ArrayList<>();
        List<Long> allUserIdList = Db.query("select user_id from 72crm_admin_user");
        if (BaseUtil.isAdmin()) {
            authUserIdList = allUserIdList;
        } else {
            Integer dataType = Db.queryInt(Db.getSql("bi.base.queryDataTypeByUserId"), BaseUtil.getUserId(), menuId);
            if (dataType == null) {
                authUserIdList.clear();
            } else if (dataType == 1) {
                authUserIdList.add(BaseUtil.getUserId());
            } else if (dataType == 2) {
                authUserIdList = Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                authUserIdList.add(BaseUtil.getUserId());
            } else if (dataType == 3) {
                authUserIdList = Db.query("select user_id from 72crm_admin_user where dept_id = ?", BaseUtil.getUser().getDeptId());
            } else if (dataType == 4) {
                List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(BaseUtil.getUser().getDeptId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(BaseUtil.getUser().getDeptId());
                SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIdList));
                authUserIdList = Db.query(sqlPara.getSql(), sqlPara.getPara());
            } else if (dataType == 5) {
                authUserIdList = allUserIdList;
            }
        }
        return authUserIdList;
    }

    /**
     * 公海管理权限判断
     */
    public static boolean isPoolAdmin(Integer poolId) {
        if (!BaseUtil.isAdmin()) {
            return Db.queryInt(Db.getSql("crm.customerPool.queryDeleteAuth"), poolId, BaseUtil.getUserId()) == 0;
        }
        return false;
    }
}
