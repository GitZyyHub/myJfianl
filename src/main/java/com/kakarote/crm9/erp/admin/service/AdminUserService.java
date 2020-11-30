package com.kakarote.crm9.erp.admin.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.*;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.config.redis.RedisManager;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.common.PinyinEnum;
import com.kakarote.crm9.erp.admin.entity.AdminAttention;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.entity.AdminUserRole;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.WorkUser;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.ParamsUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Struct;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AdminUserService {
    @Inject
    private AdminRoleService adminRoleService;
    @Inject
    private AdminDeptService adminDeptService;

    @Before(Tx.class)
    public R setUser(AdminUser adminUser, String roleIds) {
        boolean bol;
        if (!BaseUtil.getSuperUser().equals(adminUser.getUserId()) && (adminUser.getParentId() == null || adminUser.getParentId() == 0)) {
            return R.error("请设置直属上级");
        }
        updateScene(adminUser);
        Integer integer = Db.queryInt("select count(*) from 72crm_admin_user where job_number = ? and user_id!=?", adminUser.getJobNumber(),adminUser.getUserId());
        if (integer > 0) {
            return R.error("工号重复");
        }
        if (adminUser.getUserId() == null) {
            if (!ReUtil.isMatch("^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$", adminUser.getPassword())) {
                return R.error("密码必须由 6-20位字母、数字组成");
            }
            Integer count = Db.queryInt("select count(*) from 72crm_admin_user where username = ?", adminUser.getUsername());
            if (count > 0) {
                return R.error("手机号重复！");
            }

            String salt = IdUtil.fastSimpleUUID();
            adminUser.setNum(RandomUtil.randomNumbers(15));
            adminUser.setSalt(salt);
            adminUser.setPassword(BaseUtil.sign((adminUser.getUsername().trim() + adminUser.getPassword().trim()), salt));
            adminUser.setCreateTime(new Date());
            adminUser.setMobile(adminUser.getUsername());
            bol = adminUser.save();
            List<String> sqlList = new ArrayList<>();
            sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '电话无人接听', '跟进记录常用语')");
            sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户无意向', '跟进记录常用语')");
            sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语')");
            sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语')");
            Db.batch(sqlList, 4);
        } else {
            if (adminUser.getParentId() != null && adminUser.getParentId() != 0) {
                List<Record> topUserList = queryTopUserList(adminUser.getUserId());
                boolean isContain = false;
                for (Record record : topUserList) {
                    if (record.getLong("user_id").equals(adminUser.getParentId())) {
                        isContain = true;
                        break;
                    }
                }
                if (!isContain) {
                    return R.error("该员工的下级员工不能设置为直属上级");
                }
            }
            String username = Db.queryStr("select username from 72crm_admin_user where user_id = ?", adminUser.getUserId());
            if (!username.equals(adminUser.getUsername())) {
                return R.error("用户名不能修改！");
            }
            bol = adminUser.update();
            Db.delete("delete from 72crm_admin_user_role where user_id = ?", adminUser.getUserId());
            Db.delete("delete from 72crm_admin_scene where user_id = ? and is_system = 1", adminUser.getUserId());
        }
        if (StrUtil.isNotBlank(roleIds)) {
            Long userId = adminUser.getUserId();
            for (Integer roleId : TagUtil.toSet(roleIds)) {
                AdminUserRole adminUserRole = new AdminUserRole();
                adminUserRole.setUserId(userId);
                adminUserRole.setRoleId(roleId);
                adminUserRole.save();
            }
        }
        return R.isSuccess(bol);
    }

    private void updateScene(AdminUser adminUser) {
        List<Long> ids = new ArrayList<>();
        if (adminUser.getUserId() == null && adminUser.getParentId() != null) {
            ids.add(adminUser.getParentId());
        } else if (adminUser.getUserId() != null) {
            AdminUser oldAdminUser = AdminUser.dao.findById(adminUser.getUserId());
            if (oldAdminUser.getParentId() == null && adminUser.getParentId() != null) {
                ids.add(adminUser.getParentId());
            } else if (oldAdminUser.getParentId() != null && !oldAdminUser.getParentId().equals(adminUser.getParentId())) {
                ids.add(oldAdminUser.getParentId());
                ids.add(adminUser.getParentId());
            }
        }
        if (ids.size() > 0) {
            HashSet<Long> idsSet = new HashSet<>();
            ids.forEach(id -> idsSet.addAll(queryTopUserId(id, BaseConstant.AUTH_DATA_RECURSION_NUM)));
            if (idsSet.size() > 0) {
                SqlPara sqlPara = Db.getSqlPara("admin.user.updateScene", Kv.by("ids", idsSet));
                Db.delete(sqlPara.getSql(), sqlPara.getPara());
            }
        }
    }

    /**
     * @param userId 用户ID
     * @author wyq
     * 查询上级id
     */
    private List<Long> queryTopUserId(Long userId, Integer deepness) {
        List<Long> arrUsers = new ArrayList<>();
        if (deepness-- > 0) {
            AdminUser adminUser = AdminUser.dao.findById(userId);
            if (adminUser != null) {
                if (adminUser.getParentId() != null && !adminUser.getParentId().equals(0L)) {
                    arrUsers.addAll(queryTopUserId(adminUser.getParentId(), deepness));
                }
                arrUsers.add(adminUser.getUserId());
            }
        }
        return arrUsers;
    }

    /**
     * 重置用户信息
     *
     * @return 用户信息
     */
    public AdminUser resetUser(HttpServletRequest request) {
        AdminUser adminUser = AdminUser.dao.findFirst(Db.getSql("admin.user.queryUserByUserId"), BaseUtil.getUserId());
        adminUser.setRoles(adminRoleService.queryRoleIdsByUserId(adminUser.getUserId()));
        RedisManager.getRedis().setex(BaseUtil.getToken(request), 360000, adminUser);
        adminUser.remove("password", "salt");
        return adminUser;
    }

    public R queryUserList(BasePageRequest<AdminUser> request, String roleId, String search, String isResources, String workId) {
        List<Integer> deptIdList = new ArrayList<>();
        JSONObject jsonObject = request.getJsonObject();
        if (request.getData().getDeptId() != null) {
            deptIdList.add(request.getData().getDeptId());
            deptIdList.addAll(queryChileDeptIds(request.getData().getDeptId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
        }
        AdminUser adminUser = request.getData();
        Kv kv = Kv.by("name", adminUser.getRealname()).set("deptId", deptIdList).set("status", adminUser.getStatus()).set("roleId", roleId);
        kv.set("search", jsonObject.get("search"));
        List<String> pinyinSearchList = new ArrayList<>();
        if (StrUtil.isNotEmpty(search)) {
            //根据汉语首字母查询
            search = search.trim();

            if (ReUtil.isMatch("^[a-zA-Z]*", search)) {
                for (int i = 0; i < search.length(); i++) {
                    String word = search.substring(i, i + 1).toUpperCase();
                    if (word.equals("U") || word.equals("I") || word.equals("V")) {
                        break;
                    }
                    PinyinEnum pinyinEnum = PinyinEnum.valueOf(word);
                    int number = i + 1;
                    pinyinSearchList.add(" CONVERT(substring(a.realname," + number + ",1) USING gbk) between '" + pinyinEnum.getFirstWord() + "' and '" + pinyinEnum.getLastWord() + "' ");
                }
                kv.set("pinyinSearch", pinyinSearchList);
            }
        }
        if (jsonObject.get("isResources") != null) {
            kv.set("isResources", 1);
        }
        if (jsonObject.get("workId") != null) {
            kv.set("workId", jsonObject.get("workId"));
        }
        kv.set("label", request.getData().getLabel());

        StringBuilder conditions = new StringBuilder("1=1");
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                JSONObject json = JSON.parseObject(entry.getValue().toString());
                String condition = json.getString("condition");
                String value = json.getString("value");
                String name = json.getString("name");
                String formType = json.getString("formType");
                if (!ParamsUtil.isValid(name)) {
                    return R.error("参数包含非法字段");
                }
                if (StrUtil.isNotEmpty(value) && !ParamsUtil.isValid(value)) {
                    return R.error("参数包含非法字段");
                }
                if (StrUtil.isNotEmpty(json.getString("start")) && !ParamsUtil.isValid(json.getString("start"))) {
                    return R.error("参数包含非法字段");
                }
                if (StrUtil.isNotEmpty(json.getString("end")) && !ParamsUtil.isValid(json.getString("end"))) {
                    return R.error("参数包含非法字段");
                }
                boolean isNull = Arrays.asList("isNull", "isNotNull").contains(condition);
                boolean isTime = StrUtil.isEmpty(json.getString("start")) || StrUtil.isEmpty(json.getString("end"));
                if (formType.equals("date") && isTime) {
                    return R.error("需选择起始和结束时间");
                } else if (!formType.equals("date") && !isNull && StrUtil.isEmpty(value)) {
                    return R.error("value不能为空");
                }
                conditions.append(" and ").append(StrUtil.toUnderlineCase(name));
                if ("date".equals(formType)) {
                    conditions.append(" between '").append(json.getString("start")).append("' and '").append(json.getString("end")).append("'");
                } else if ("is".equals(condition)) {
                    conditions.append(" = '").append(value).append("'");
                } else if ("isNot".equals(condition)) {
                    conditions.append(" != '").append(value).append("'");
                } else if ("contains".equals(condition)) {
                    conditions.append(" like '%").append(value).append("%'");
                } else if ("notContains".equals(condition)) {
                    conditions.append(" not like '%").append(value).append("%'");
                } else if ("isNull".equals(condition)) {
                    conditions.append(" is null");
                } else if ("isNotNull".equals(condition)) {
                    conditions.append(" is not null");
                } else if ("gt".equals(condition)) {
                    conditions.append(" > ").append(value);
                } else if ("egt".equals(condition)) {
                    conditions.append(" >= ").append(value);
                } else if ("lt".equals(condition)) {
                    conditions.append(" < ").append(value);
                } else if ("elt".equals(condition)) {
                    conditions.append(" <= ").append(value);
                } else if ("in".equals(condition)) {
                    conditions.append(" in (").append(value).append(")");
                } else if ("not in".equals(condition)) {
                    conditions.append(" not in (").append(value).append(")");
                }
            }
            Kv by = Kv.by("conditions", conditions.toString());
            List<Object> query = Db.template("work.task.queryTaskBySearch", by).query();
            query.addAll(Db.template("work.task.queryTaskIdByIds", Kv.by("ids", query)).query());
            kv.set("userIds", Db.template("work.queryDataUserId", Kv.by("ids", query).set("by", WorkUserEnum.TYPE_1.getType())).query());
        }
        kv.set("sort",jsonObject.get("sort"));
        SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserList", kv);
        if (request.getPageType() == 0) {
            List<Record> recordList = Db.find(sqlPara);
            return R.ok().put("data", recordList);
        } else {
            Page<Record> paginate = Db.paginate(request.getPage(), request.getLimit(), sqlPara);
            return R.ok().put("data", paginate);
        }
    }

    public R queryUserMiNiList(BasePageRequest<AdminUser> request) {
        AdminUser adminUser = request.getData();
        Kv kv = Kv.by("name", adminUser.getRealname());
        kv.set("label", request.getData().getLabel());
        SqlPara sqlPara = Db.getSqlPara("admin.user.queryMiNi", kv);
        if (request.getPageType() == 0) {
            List<Record> recordList = Db.find(sqlPara);
            return R.ok().put("data", recordList);
        } else {
            Page<Record> paginate = Db.paginate(request.getPage(), request.getLimit(), sqlPara);
            return R.ok().put("data", paginate);
        }
    }

    /**
     * 查询可设置为上级的user
     */
    public List<Record> queryTopUserList(Long userId) {
        List<Record> recordList = Db.find("select user_id,realname,parent_id from 72crm_admin_user");
        List<Long> subUserList = queryChileUserIds(userId, BaseConstant.AUTH_DATA_RECURSION_NUM);
        recordList.removeIf(record -> subUserList.contains(record.getLong("user_id")));
        recordList.removeIf(record -> record.getLong("user_id").equals(userId));
        return recordList;
    }

    /**
     * 查询本部门下的所有部门id
     *
     * @param deptId 当前部门id
     */
    public List<Integer> queryChileDeptIds(Integer deptId, Integer deepness) {
        List<Integer> list = Db.query("select dept_id from 72crm_admin_dept where pid = ?", deptId);
        if (list.size() != 0 && deepness > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                list.addAll(queryChileDeptIds(list.get(i), deepness - 1));
            }
        }
        return list;
    }

    public String querySubUserIds(Long userId) {
        List<Long> userIdList = Aop.get(AdminUserService.class).queryChileUserIds(userId, BaseConstant.AUTH_DATA_RECURSION_NUM);
        userIdList.add(BaseUtil.getUserId());
        return CollectionUtil.join(userIdList, ",");
    }

    /**
     * 查询本用户下的所有下级id
     *
     * @param userId 当前用户id
     */
    public List<Long> queryChileUserIds(Long userId, Integer deepness) {
        List<Long> query = Db.query("select user_id from 72crm_admin_user where parent_id = ?", userId);
        if (deepness > 0) {
            for (int i = 0, size = query.size(); i < size; i++) {
                query.addAll(queryChileUserIds(query.get(i), deepness - 1));
            }
        }
        HashSet<Long> set = new HashSet<>(query);
        query.clear();
        query.addAll(set);
        return query;
    }

    /**
     * 查询指定用户所有下级用户
     */
    public List<Record> querySubUserList(Long userId) {
        List<Long> userIdList = queryChileUserIds(userId, BaseConstant.AUTH_DATA_RECURSION_NUM);
        return Db.find(Db.getSqlPara("admin.user.querySubUserList", Kv.by("ids", userIdList)));
    }


    public R resetPassword(String ids, String pwd) {
        if (!ReUtil.isMatch("^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$", pwd)) {
            return R.error("密码必须由 6-20位字母、数字组成");
        }
        for (String id : ids.split(",")) {
            AdminUser adminUser = new AdminUser().dao().findById(id);
            String password = BaseUtil.sign(adminUser.getUsername() + pwd, adminUser.getSalt());
            Db.update("update 72crm_admin_user set password = ? where user_id = ?", password, id);
            BaseUtil.userExit(adminUser.getUserId(), null);
        }
        return R.ok();
    }

    public R querySuperior(String realName) {
        return R.ok().put("data", Db.find(Db.getSqlPara("admin.user.querySuperior", Kv.by("name", realName))));
    }

    public R queryListName(BasePageRequest<AdminUser> basePageRequest) {
        String name = basePageRequest.getJsonObject().getString("search");
        Integer deptId = basePageRequest.getData().getDeptId();
        String order = "2".equals(basePageRequest.getJsonObject().get("initial")) ? "desc" : "asc";
        Kv kv = Kv.by("name", name).set("deptId", deptId).set("userId", BaseUtil.getUserId()).set("status", 1);
        kv.set("order", order);
        Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("admin.user.queryUserByRealNameAndDeptId", kv));
        return R.ok().put("data", page);
    }

    /**
     * @author zxy
     * 查询系统下属用户列表
     */
    public List<Long> queryUserIdsByParentId(Long userId) {
        String sql = "select user_id from 72crm_admin_user where parent_id = ? ";
        List<Record> records = Db.find(sql, userId);
        List<Long> userIds = new ArrayList<>();
        for (Record record : records) {
            userIds.add(record.getLong("user_id"));
        }
        return userIds;
    }


    /**
     * @author zxy
     * 根据部门查询用户id
     */
    public String queryUserIdsByDept(String deptIds) {
        if (StrUtil.isEmpty(deptIds)) {
            return null;
        }
        SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIds));
        List<Long> users = Db.query(sqlPara.getSql(), sqlPara.getPara());
        return StrUtil.join(",", users);
    }

    public R queryAllUserList() {
        List<Record> recordList = Db.find(Db.getSqlPara("admin.user.queryUserList"));
        return R.ok().put("data", recordList);
    }

    public R setUserStatus(String ids, String status) {
        for (Long id : TagUtil.toLongSet(ids)) {
            if ("0".equals(status)) {
                if (id.equals(BaseUtil.getSuperUser())) {
                    return R.error("超级管理员用户不可禁用");
                }
                //查询是否有未完成的任务
                Integer taskCount = Db.queryInt("SELECT COUNT(1) from 72crm_task WHERE main_user_id=? and create_user_id=? and status!='已完成'", id, id);
                Integer taskUserCount = Db.queryInt("SELECT COUNT(1) from 72crm_task WHERE task_id in(" +
                        "SELECT num_id FROM 72crm_work_user_record WHERE user_id=?" +
                        ") and status!='已完成'", id);
                if (taskCount != 0 || taskUserCount != 0) {
                    return R.error("存在没有完成的任务不能删除");
                }

                BaseUtil.userExit(id, null);
            } else if ("1".equals(status)) {
                Integer roleCount = Db.queryInt("select count(1) from `72crm_admin_user_role` where user_id = ?", id);
                if (roleCount == 0) {
                    return R.error("请先给用户设置角色");
                }
                AdminUser adminUser = new AdminUser().findById(id);
                if (adminUser.getDeptId() == null) {
                    return R.error("请先给用户设置部门");
                }
                if (adminUser.getParentId() == null || adminUser.getParentId() == 0) {
                    return R.error("请先给用户设置直属上级");
                }
            }
            Db.update("update 72crm_admin_user set status = ? where user_id = ?", status, id);
        }
        return R.ok();
    }

    public boolean updateImg(String url, Long userId) {
        AdminUser adminUser = AdminUser.dao.findById(userId);
        adminUser.setImg(url);
        return adminUser.update();
    }

    public R updateUser(AdminUser adminUser) {
        if (!BaseUtil.getUser().getUsername().equals(adminUser.getUsername())) {
            return R.error("用户名不可修改");
        }
        adminUser.setUserId(BaseUtil.getUserId());
        boolean b = false;
        if (StrUtil.isNotEmpty(adminUser.getPassword())) {
            if (!ReUtil.isMatch("^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$", adminUser.getPassword())) {
                return R.error("密码必须由 6-20位字母、数字组成");
            }
            b = true;
            adminUser.setSalt(IdUtil.simpleUUID());
            adminUser.setPassword(BaseUtil.sign((adminUser.getUsername().trim() + adminUser.getPassword().trim()), adminUser.getSalt()));
        }
        boolean update = adminUser.update();
        if (b && update) {
            BaseUtil.userExit(adminUser.getUserId(), null);
        }
        return R.isSuccess(update, "修改失败");
    }

    /**
     * 查询当前用户权限 用于菜单展示 FFF
     *
     * @param userId 用户id
     * @param realm  当前操作的菜单 链接地址
     * @return 当前可展示用户的数据
     */
    public List<Long> queryUserByAuth(Long userId, String realm) {
        if (ObjectUtil.equal("return_visit", realm)) {
            realm = "visit";
        }
        List<Long> adminUsers = new ArrayList<>();
        //查询用户数据权限，从高到低排序
        List<Integer> list = Db.query(Db.getSql("admin.role.queryDataTypeByUserId"), userId);
        if (list.size() == 0) {
            //无权限查询自己的数据
            adminUsers.add(userId);
            return adminUsers;
        }
        List<Record> userRoleList = Db.find(Db.getSqlPara("admin.role.queryUserRoleListByUserId", Kv.by("userId", userId).set("realm", realm)));
        if (list.size() == 1 && userRoleList.size() == 1) {
            //如果为1的话 验证是否有最高权限，否则及有多个权限
            //拥有最高数据权限
            if (list.contains(5)) {
                return null;
            } else {
                AdminUser adminUser = AdminUser.dao.findById(userId);
                if (list.contains(4)) {
                    List<Record> records = adminDeptService.queryDeptByParentDept(adminUser.getDeptId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                    List<Integer> deptIds = new ArrayList<>();
                    deptIds.add(adminUser.getDeptId());
                    records.forEach(record -> deptIds.add(record.getInt("id")));
                    SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIds));
                    adminUsers.addAll(Db.query(sqlPara.getSql(), sqlPara.getPara()));
                } else if (list.contains(3)) {
                    queryUserByDeptId(adminUser.getDeptId()).forEach(record -> adminUsers.add(record.getLong("id")));
                }

                if (list.contains(2)) {
                    adminUsers.addAll(queryUserByParentUser(adminUser.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
                }
                adminUsers.add(adminUser.getUserId());
            }
        } else {
            if (realm != null && !"".equals(realm)) {
                AdminUser adminUser = AdminUser.dao.findById(userId);
                for (Record r : userRoleList) {//如果有多个权限 验证当前用户是否对当前管理 是否为本人操作
                    if (r.getStr("realm").equals(realm) && "1".equals(r.getStr("data_type"))) {//当前操作的管理链接地址
                        adminUsers.add(userId);
                        HashSet<Long> hashSet = new HashSet<>(adminUsers);
                        adminUsers.clear();
                        adminUsers.addAll(hashSet);
                        return adminUsers;
                    } else if (r.getStr("realm").equals(realm) && "2".equals(r.getStr("data_type"))) {//本人及其
                        adminUsers.addAll(queryUserByParentUser(adminUser.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
                        adminUsers.add(userId);
                        HashSet<Long> hashSet = new HashSet<>(adminUsers);
                        adminUsers.clear();
                        adminUsers.addAll(hashSet);
                        return adminUsers;
                    } else if (r.getStr("realm").equals(realm) && "3".equals(r.getStr("data_type"))) {//本部门
                        queryUserByDeptId(adminUser.getDeptId()).forEach(record -> adminUsers.add(record.getLong("id")));
                        adminUsers.add(userId);
                        HashSet<Long> hashSet = new HashSet<>(adminUsers);
                        adminUsers.clear();
                        adminUsers.addAll(hashSet);
                        return adminUsers;
                    } else if (r.getStr("realm").equals(realm) && "4".equals(r.getStr("data_type"))) {//本部门及下属部门
                        List<Record> records = adminDeptService.queryDeptByParentDept(adminUser.getDeptId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                        List<Integer> deptIds = new ArrayList<>();
                        deptIds.add(adminUser.getDeptId());
                        records.forEach(record -> {
                            deptIds.add(record.getInt("id"));
                        });
                        SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIds));
                        adminUsers.addAll(Db.query(sqlPara.getSql(), sqlPara.getPara()));
                        adminUsers.add(userId);
                        HashSet<Long> hashSet = new HashSet<>(adminUsers);
                        adminUsers.clear();
                        adminUsers.addAll(hashSet);
                        return adminUsers;
                    } else if (r.getStr("realm").equals(realm) && "5".equals(r.getStr("data_type"))) {//全部
                        return null;
                    }
                }
            } else {
                if (list.contains(5)) {
                    return null;
                } else {
                    AdminUser adminUser = AdminUser.dao.findById(userId);
                    if (list.contains(4)) {
                        List<Record> records = adminDeptService.queryDeptByParentDept(adminUser.getDeptId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                        List<Integer> deptIds = new ArrayList<>();
                        deptIds.add(adminUser.getDeptId());
                        records.forEach(record -> deptIds.add(record.getInt("id")));
                        SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIds));
                        adminUsers.addAll(Db.query(sqlPara.getSql(), sqlPara.getPara()));
                    } else if (list.contains(3)) {
                        queryUserByDeptId(adminUser.getDeptId()).forEach(record -> adminUsers.add(record.getLong("id")));
                    }

                    if (list.contains(2)) {
                        adminUsers.addAll(queryUserByParentUser(adminUser.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
                    }
                    adminUsers.add(adminUser.getUserId());
                }
            }
        }

        adminUsers.add(userId);
        HashSet<Long> hashSet = new HashSet<>(adminUsers);
        adminUsers.clear();
        adminUsers.addAll(hashSet);
        return adminUsers;
    }

    public List<Long> queryUserByParentUser(Long userId, Integer deepness) {
        List<Long> recordList = new ArrayList<>();
        if (deepness > 0) {
            List<Long> records = Db.query("SELECT b.user_id FROM 72crm_admin_user AS b WHERE b.parent_id = ?", userId);
            recordList.addAll(records);
            int size = recordList.size();
            for (int i = 0; i < size; i++) {
                recordList.addAll(queryUserByParentUser(recordList.get(i), deepness - 1));
            }
        }
        return recordList;
    }

    public List<Record> queryUserByDeptId(Integer deptId) {
        return Db.find(Db.getSql("admin.user.queryUserByDeptId"), deptId);
    }

    public List<Record> queryAllUserByDeptId(Integer deptId) {
        List<Integer> deptIdList = new ArrayList<>();
        deptIdList.add(deptId);
        deptIdList.addAll(queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM));
        return Db.find(Db.getSqlPara("admin.user.queryAllUserByDeptId", Kv.by("ids", deptIdList)));
    }


    /**
     * 修改用户账号功能
     *
     * @param id       用户ID
     * @param username 新的用户名
     * @param password 新的密码
     * @return 操作状态
     */
    @Before(Tx.class)
    public R usernameEdit(Integer id, String username, String password) {
        if (!ReUtil.isMatch("^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$", password)) {
            return R.error("密码必须由 6-20位字母、数字组成");
        }
        AdminUser adminUser = AdminUser.dao.findById(id);
        if (adminUser == null) {
            return R.error("用户不存在！");
        }
        if (adminUser.getUsername().equals(username)) {
            return R.error("账号不能和原账号相同");
        }
        Integer count = Db.queryInt("select count(*) from 72crm_admin_user where username = ?", username);
        if (count > 0) {
            return R.error("手机号重复！");
        }
        adminUser.setUsername(username);
        adminUser.setMobile(username);
        adminUser.setPassword(BaseUtil.sign(username + password, adminUser.getSalt()));
        BaseUtil.userExit(adminUser.getUserId(), null);
        return R.isSuccess(adminUser.update());
    }

    /**
     * @author zxy
     * 关注和取消关注
     */
    public R attention(Long userId) {
        List<AdminAttention> attentions = AdminAttention.dao.find("select * from 72crm_admin_attention where be_user_id = ? and attention_user_id = ?", userId, BaseUtil.getUserId());
        if (attentions.size() > 0) {
            return Db.delete("delete from 72crm_admin_attention where be_user_id = ? and attention_user_id = ?"
                    , userId, BaseUtil.getUserId()) > 0 ? R.ok() : R.error();
        } else {
            AdminAttention attention = new AdminAttention();
            attention.setBeUserId(userId);
            attention.setAttentionUserId(BaseUtil.getUserId());
            return attention.save() ? R.ok() : R.error();
        }
    }

    @Before(Tx.class)
    public R userExcelImport(UploadFile file, boolean temp) {
        List<String> sqlList = new ArrayList<>();
        List<List<Object>> errList = new ArrayList<>();
        if (file.getFile() == null || !file.getFile().exists()) {
            return R.ok();
        }
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(file.getFile().getAbsolutePath(), 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
            if (rowIndex > 1) {
                num.getAndSet(num.get() + 1);
                if (StrUtil.isEmptyIfStr(rowList.get(0))) {
                    rowList.add(0, "用户名不能为空");
                    errList.add(rowList);
                    return;
                }
                if (StrUtil.isEmptyIfStr(rowList.get(1))) {
                    rowList.add(0, "密码不能为空");
                    errList.add(rowList);
                    return;
                }
                if (!ReUtil.isMatch("^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$", rowList.get(1).toString())) {
                    rowList.add(0, "密码必须由 6-20位字母、数字组成");
                    errList.add(rowList);
                    return;
                }
                if (StrUtil.isEmptyIfStr(rowList.get(2))) {
                    rowList.add(0, "姓名不能为空");
                    errList.add(rowList);
                    return;
                }
                String username = rowList.get(0).toString().trim();
                Integer count = Db.queryInt("select count(1) from `72crm_admin_user` where username = ?", username);
                if (count > 0) {
                    rowList.add(0, "手机号已存在");
                    errList.add(rowList);
                    return;
                }
                if (!ReUtil.isMatch("^[1][3,4,5,6,7,8,9][0-9]{9}$", username)) {
                    rowList.add(0, "手机号格式不正确");
                    errList.add(rowList);
                    return;
                }

                String password = rowList.get(1).toString().trim();
                String realname = rowList.get(2).toString().trim();
                String sex = null;
                String email = null;
                String post = null;
                if (null != rowList.get(3)) {
                    sex = rowList.get(3).toString().trim();
                }
                if (null != rowList.get(4)) {
                    email = rowList.get(4).toString().trim();
                }
                if (null != rowList.get(5)) {
                    post = rowList.get(5).toString().trim();
                }
                AdminUser adminUser = new AdminUser();
                String salt = IdUtil.fastSimpleUUID();
                adminUser.setUsername(username);
                adminUser.setPassword(BaseUtil.sign((adminUser.getUsername().trim() + password.trim()), salt));
                adminUser.setSalt(salt);
                adminUser.setNum(RandomUtil.randomNumbers(15));
                adminUser.setCreateTime(new Date());
                adminUser.setRealname(realname);
                adminUser.setMobile(username);
                adminUser.setEmail(email);
                adminUser.setPost(post);
                adminUser.setStatus(0);
                if (temp) {
                    adminUser.setIsResources(1);
                }
                if (StrUtil.isNotEmpty(sex)) {
                    if ("女".equals(sex)) {
                        adminUser.setSex(2);
                    } else {
                        adminUser.setSex(1);
                    }
                }
                adminUser.save();
                sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '电话无人接听', '跟进记录常用语')");
                sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户无意向', '跟进记录常用语')");
                sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语')");
                sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语')");
            }
        });
        Db.batch(sqlList, 500);
        R result = R.ok().put("totalSize", num.get()).put("errSize", 0);
        if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(file.getUploadPath() + "/" + token);


                for (int i = 0; i < 7; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                writer.writeHeadRow(Arrays.asList("错误信息", "手机号(*)", "登录密码(*)", "姓名(*)", "性别", "邮箱", "岗位"));
                writer.write(errList);
                result.put("errSize", errList.size()).put("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }


    @Before(Tx.class)
    public R excelImport(UploadFile file) {
        List<String> sqlList = new ArrayList<>();
        List<List<Object>> errList = new ArrayList<>();
        if (file.getFile() == null || !file.getFile().exists()) {
            return R.ok();
        }
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(file.getFile().getAbsolutePath(), 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
            if (rowIndex > 1) {
                num.getAndSet(num.get() + 1);
                if (StrUtil.isEmptyIfStr(rowList.get(0))) {
                    rowList.add(0, "用户名不能为空");
                    errList.add(rowList);
                    return;
                }
                if (StrUtil.isEmptyIfStr(rowList.get(1))) {
                    rowList.add(0, "密码不能为空");
                    errList.add(rowList);
                    return;
                }
                if (!ReUtil.isMatch("^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$", rowList.get(1).toString())) {
                    rowList.add(0, "密码必须由 6-20位字母、数字组成");
                    errList.add(rowList);
                    return;
                }
                if (StrUtil.isEmptyIfStr(rowList.get(2))) {
                    rowList.add(0, "姓名不能为空");
                    errList.add(rowList);
                    return;
                }
                String username = rowList.get(0).toString().trim();
                Integer count = Db.queryInt("select count(1) from `72crm_admin_user` where username = ?", username);
                if (count > 0) {
                    rowList.add(0, "手机号已存在");
                    errList.add(rowList);
                    return;
                }
                if (!ReUtil.isMatch("^[1][3,4,5,6,7,8,9][0-9]{9}$", username)) {
                    rowList.add(0, "手机号格式不正确");
                    errList.add(rowList);
                    return;
                }

                String password = rowList.get(1).toString().trim();
                String realname = rowList.get(2).toString().trim();
                String sex = null;
                String email = null;
                String post = null;
                if (null != rowList.get(3)) {
                    sex = rowList.get(3).toString().trim();
                }
                if (null != rowList.get(4)) {
                    email = rowList.get(4).toString().trim();
                }
                if (null != rowList.get(5)) {
                    post = rowList.get(5).toString().trim();
                }
                AdminUser adminUser = new AdminUser();
                String salt = IdUtil.fastSimpleUUID();
                adminUser.setUsername(username);
                adminUser.setPassword(BaseUtil.sign((adminUser.getUsername().trim() + password.trim()), salt));
                adminUser.setSalt(salt);
                adminUser.setNum(RandomUtil.randomNumbers(15));
                adminUser.setCreateTime(new Date());
                adminUser.setRealname(realname);
                adminUser.setMobile(username);
                adminUser.setEmail(email);
                adminUser.setPost(post);
                adminUser.setStatus(0);
                if (StrUtil.isNotEmpty(sex)) {
                    if ("女".equals(sex)) {
                        adminUser.setSex(2);
                    } else {
                        adminUser.setSex(1);
                    }
                }
                adminUser.save();
                sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '电话无人接听', '跟进记录常用语')");
                sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户无意向', '跟进记录常用语')");
                sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语')");
                sqlList.add("INSERT INTO `72crm_admin_user_config`  VALUES (null,'" + adminUser.getUserId() + "', '1', 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语')");
            }
        });
        Db.batch(sqlList, 500);
        R result = R.ok().put("totalSize", num.get()).put("errSize", 0);
        if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(file.getUploadPath() + "/" + token);


                for (int i = 0; i < 7; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                writer.writeHeadRow(Arrays.asList("错误信息", "手机号(*)", "登录密码(*)", "姓名(*)", "性别", "邮箱", "岗位"));
                writer.write(errList);
                result.put("errSize", errList.size()).put("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }

    public AdminUser queryUserInfo(Long userId) {
        return AdminUser.dao.template("admin.user.queryUserInfo", Kv.by("userId", userId)).findFirst();
    }

    /**
     * 设置用户是否是人力资源???
     *
     * @param userIds
     * @param isResources
     * @return
     */
    @Before(Tx.class)
    public R setResources(String userIds, Integer isResources, String workId) {
        List<String> strings = Arrays.asList(userIds.split(","));
        for (String userId : strings) {
            if (isResources == 0) {
                Integer taskUserCount = Db.queryInt("SELECT COUNT(1) from 72crm_task WHERE task_id in(" +
                        "SELECT num_id FROM 72crm_work_user_record WHERE user_id=? and type=1 and work_id =?" +
                        ") and status!='已完成'", userId,workId);
                if (taskUserCount != 0) {
                    return R.error("存在没有完成的任务不能删除");
                }
                Db.delete("delete from 72crm_work_user where work_id=? and user_id=?", workId, userId);
            } else {
                Integer integer = Db.queryInt("select status from 72crm_admin_user where user_id=?", userId);
                if (integer == 0) {
                    return R.error("该用户被禁用不能添加人力资源");
                }
                WorkUser workUser = new WorkUser();
                workUser.setWorkId(Integer.parseInt(workId));
                workUser.setUserId(Long.parseLong(userId));
                workUser.save();
            }
        }
        return R.ok();
    }
}
