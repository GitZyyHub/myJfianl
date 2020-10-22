package com.kakarote.crm9.erp.admin.service;

import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminMenu;
import com.kakarote.crm9.erp.admin.entity.AdminRole;
import com.kakarote.crm9.erp.admin.entity.AdminRoleMenu;
import com.kakarote.crm9.utils.R;

import java.util.List;

public class AdminMenuService {


    /**
     * 通过用户ID筛选菜单
     * @param userId 用户ID
     * @return 菜单列表myTask
     * @author zhangzhiwei
     */
    public List<AdminMenu> queryMenuByUserId(Long userId){
        return queryMenuByAuth(Kv.by("userId",userId));
    }

    /**
     * 通过角色ID筛选菜单
     * @param roleId 角色ID
     * @return 菜单列表
     * @author zhangzhiwei
     */
    public List<AdminMenu> queryMenuByRoleId(Integer roleId){
        return queryMenuByAuth(Kv.by("roleId",roleId));
    }

    /**
     * 通过角色ID或用户ID筛选菜单
     * @param kv data
     * @return 菜单列表
     * @author zhangzhiwei
     */
    public List<AdminMenu> queryMenuByAuth(Kv kv) {
        return AdminMenu.dao.template("admin.menu.queryMenuByAuth",kv).find();
    }

    /**
     * 查询全部菜单
     * @return 菜单列表
     * @author zhangzhiwei
     */
    public List<AdminMenu> queryMenuList(){
        return queryMenuList(null);
    }

    /**
     * 通过parentId查询菜单
     * @param parentId parentId
     * @return 菜单列表
     * @author zhangzhiwei
     */
    public List<AdminMenu> queryMenuList(Integer parentId) {
        Kv data=Kv.by("parentId",parentId);
        return AdminMenu.dao.template("admin.menu.queryMenuList",data).find();
    }

    /**
     * 通过parentId和realm查询菜单
     * @return 菜单列表
     * @author zhangzhiwei
     */
    public AdminMenu queryMenuListByRealm(Integer parentId, String realm){
        Kv data=Kv.by("parentId",parentId).set("realm",realm);
        return AdminMenu.dao.template("admin.menu.queryMenuList",data).findFirst();
    }

    /**
     * 通过parentId和realm不等于指定值查询菜单
     * @return 菜单列表
     * @author zhangzhiwei
     */
    public List<AdminMenu> queryMenuListByNotRealm(Integer parentId, String... notRealm) {
        Kv data=Kv.by("parentId",parentId).set("notRealm",notRealm);
        return AdminMenu.dao.template("admin.menu.queryMenuList",data).find();
    }

    public List<AdminMenu> getMenuList(Integer parentId, Integer deepness) {
        List<AdminMenu> adminMenus = queryMenuList(parentId);
        if (deepness != 0) {
            adminMenus.forEach(adminMenu -> {
                if (!adminMenu.getMenuType().equals(3)) {
                    adminMenu.put("childMenu", getMenuList(adminMenu.getMenuId(), deepness - 1));
                }
            });
        }
        return adminMenus;
    }

    private List<AdminMenu> getMenuList(Integer parentId, Integer deepness, String... realm) {
        List<AdminMenu> adminMenus = queryMenuListByNotRealm(parentId,realm);
        if (deepness != 0) {
            adminMenus.forEach(adminMenu -> {
                if (!adminMenu.getMenuType().equals(3)) {
                    adminMenu.put("childMenu", getMenuList(adminMenu.getMenuId(), deepness - 1));
                }
            });
        }
        return adminMenus;
    }

    public R getMenuListByType(Integer type) {
        Kv kv = new Kv();
        String realm;
        if (type == 1) {
            realm="manage";
        } else if (type == 2) {
            realm="crm";
            AdminMenu bi = queryMenuListByRealm(0,"bi");
            List<AdminMenu> biList = getMenuList(bi.getMenuId(), BaseConstant.AUTH_DATA_RECURSION_NUM, "oa");
            bi.put("childMenu", biList);
            kv.set("bi", bi);
        } else if(type==5){
            realm="work";
        } else if (type == 7) {
            realm="oa";
            AdminMenu bi = queryMenuListByRealm(0,"bi");
            String[] realmArr = new String[]{"achievement", "business", "customer", "contract", "product", "portrait", "ranking","call"};
            List<AdminMenu> biList = getMenuList(bi.getMenuId(), BaseConstant.AUTH_DATA_RECURSION_NUM, realmArr);
            bi.put("childMenu", biList);
            kv.set("bi", bi);
        } else if (type == 8) {
            realm="project";
        } else {
            realm="";
        }
        AdminMenu data = queryMenuListByRealm(0,realm);
        data.put("childMenu", getMenuList(data.getMenuId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
        kv.set("data", data);
        return R.ok().put("data", kv);
    }


    /**
     * @author wyq
     * 保存角色权限
     */
    @Before(Tx.class)
    public boolean saveRoleMenu(Integer roleId, Integer dateType, List<Integer> menuIdList) {
        AdminRole adminRole = new AdminRole();
        adminRole.setRoleId(roleId);
        if (dateType != null) {
            adminRole.setDataType(dateType);
        }
        adminRole.update();
        Db.delete(Db.getSql("admin.role.deleteRoleMenu"), roleId);
        if (null == menuIdList || menuIdList.size() == 0) {
            return true;
        }
        for (Integer menuId : menuIdList) {
            AdminRoleMenu adminRoleMenu = new AdminRoleMenu();
            adminRoleMenu.setRoleId(roleId);
            adminRoleMenu.setMenuId(menuId);
            adminRoleMenu.save();
        }
        return true;
    }
}
