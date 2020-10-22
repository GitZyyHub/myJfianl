package com.kakarote.crm9.erp.work.common;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminMenu;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminMenuService;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.*;

/**
 * @author zyy
 */
public class WorkAuthVerify {
    
    

    @Inject
    private AdminMenuService adminMenuService;

    public static boolean isPermissions(String permissions) {
        JSONObject jsonObject = Aop.get(AdminRoleService.class).auth(BaseUtil.getUserId());
        //组装应有权限列表
        List<String> arr = AuthUtil.queryAuth(jsonObject, "");
        boolean isRelease = false;
        if (!isRelease) {
            if (arr.contains(permissions)) {
                isRelease = true;
            }
        }
        if (!isRelease) {
            return false;
        }
        return true;
    }

    /**
     * 查看是否有查看列表权限，如果有是什么查看权限
     * @param permissions
     * @return
     */
    /*public static JSONObject checkQueryAuth(String permissions){
        AdminUser user = BaseUtil.getUser();
        List<Integer> roles = user.getRoles();
        Long userId = user.getUserId();
        if (BaseUtil.getSuperUser().equals(userId) || roles.contains(BaseUtil.getSuperRole())) {
            return null;
        }
        List<Object> menuIds = new ArrayList<>();
        for (Integer role : roles) {
            menuIds.addAll(Db.query("SELECT menu_id FROM `72crm_admin_role_menu` WHERE  role_id=?"));
        }
        for (Object menuId : menuIds) {
            
        }

        return null;
    }*/
    public Map<String, Boolean> checkQueryAuth(String[] permissions,Long userId){
        JSONObject jsonObject = new JSONObject();
        List<AdminMenu> menuRecords;
        List<Integer> roleIds = queryRoleIdsByUserId(userId);
        if (roleIds.contains(BaseUtil.getSuperRole()) || BaseUtil.getSuperUser().equals(userId)) { //如果是超级管理员
            menuRecords = adminMenuService.queryMenuList();
        } else {
            menuRecords = adminMenuService.queryMenuByUserId(userId);
            //如果是项目管理员查询创建项目角色
            if (roleIds.contains(BaseUtil.getWorkAdminRole())) {
                menuRecords.add(AdminMenu.dao.template("admin.menu.queryMenuList", Kv.by("remarks", "projectSave")).findFirst());
            }
        }
        List<AdminMenu> adminMenus = adminMenuService.queryMenuList(0);
        for (AdminMenu adminMenu : adminMenus) {
            JSONObject object = new JSONObject();
            List<AdminMenu> adminMenuList = adminMenuService.queryMenuList(adminMenu.getMenuId());
            for (AdminMenu menu : adminMenuList) {
                JSONObject authObject = new JSONObject();
                for (AdminMenu record : menuRecords) {
                    if (menu.getMenuId().equals(record.getParentId().intValue())) {
                        authObject.put(record.getRealm(), true);
                    }
                }
                if (!authObject.isEmpty()) {
                    object.put(menu.getRealm(), authObject);
                }
            }
            if (!object.isEmpty()) {
                jsonObject.put(adminMenu.getRealm(), object);
            }
        }
        List<String> arr= AuthUtil.queryAuth(jsonObject, "");
        boolean isRelease=false;
        Map<String, Boolean> result = new HashMap<>();
        result.put("code",false);
        for (String key : permissions) {
            if(!isRelease){
                if(arr.contains(key)){
                    result.put("code",true);
                    if(permissions[1].equals(key)){
                        result.put("auth",true);
                    }else {
                        result.put("auth",false);
                    }
                }
            }
        }
        return result;
    }

    private List<Integer> queryRoleIdsByUserId(Long userId) {
        return Db.query(Db.getSql("admin.role.queryRoleIdsByUserId"), userId);
    }
}
