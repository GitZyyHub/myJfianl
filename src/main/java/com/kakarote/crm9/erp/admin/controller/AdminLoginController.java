package com.kakarote.crm9.erp.admin.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.cache.CaffeineCache;
import com.kakarote.crm9.common.config.redis.Redis;
import com.kakarote.crm9.common.config.redis.RedisManager;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminLoginLog;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.entity.AdminUserHisTable;
import com.kakarote.crm9.erp.admin.service.AdminConfigService;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.utils.AdminLogUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户登录
 *
 * @author zhangzhiwei
 */
@Clear
public class AdminLoginController extends Controller {


    @Inject
    private AdminRoleService adminRoleService;

    @Inject
    private AdminConfigService adminConfigService;

    public final static Prop prop = PropKit.use("config/crm9-config.txt");

    public void index() {
        redirect("/index.html");
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @author zhangzhiwei
     * 用户登录
     */
    public void login(@Para("username") String username, @Para("password") String password, @Para("") AdminLoginLog adminLoginLog) {
        String ipAddress = ServletUtil.getClientIP(getRequest());
        adminLoginLog.setIpAddress(ipAddress);
        String ipCityInfo = AdminLogUtil.getIpCityInfo(ipAddress);
        if (ipCityInfo != null) {
            if (ipCityInfo.contains("内网IP")) {
                adminLoginLog.setLoginAddress("内网");
            } else {
                try {
                    String[] addressArr = ipCityInfo.split("\\|");
                    adminLoginLog.setLoginAddress(addressArr[0] + addressArr[2] + addressArr[3] + addressArr[4]);
                } catch (Exception e) {
                    Log.getLog(getClass()).error("e", e);
                }
            }
        }
        UserAgent userAgent = UserAgentUtil.parse(getRequest().getHeader("user-agent"));
        adminLoginLog.setDeviceType(userAgent.isMobile() ? "移动端" : "网页");
        adminLoginLog.setCore(userAgent.getBrowser().toString() + userAgent.getVersion());
        adminLoginLog.setPlatform(userAgent.getOs().toString());
        String key = BaseConstant.USER_LOGIN_ERROR_KEY + username;
        Redis redis = RedisManager.getRedis();
        long beforeTime = System.currentTimeMillis() - 60 * 5 * 1000;
        if (redis.exists(key)) {
            if (redis.zcount(key, beforeTime, System.currentTimeMillis()) >= 5) {
                Set zrevrange = redis.zrevrange(key, 4, 5);
                Long time = (Long) zrevrange.iterator().next() + 60 * 5 * 1000;
                long expire = (time - System.currentTimeMillis()) / 1000;
                renderJson(R.error("密码错误次数过多，请等" + expire + "秒后在重试！"));
                return;
            }
        }
        redis.zadd(key, System.currentTimeMillis(), System.currentTimeMillis());
        redis.expire(key, 3600);
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            renderJson(R.error("请输入用户名和密码！"));
            return;
        }
        AdminUser adminUser = AdminUser.dao.findFirst(Db.getSqlPara("admin.user.queryByUserName", Kv.by("username", username.trim())));
        if (adminUser == null) {
            //TODO 用户不存在时密码登录提示密码错误，验证码提示请先注册
            renderJson(R.error("用户不存在，请先注册！"));
            return;
        }
        if ((!BaseUtil.verify(username + password, adminUser.getSalt(), adminUser.getPassword()))) {
            addLoginLog(adminLoginLog, adminUser.getUserId(), 2);
            Log.getLog(getClass()).warn("用户登录失败");
            renderJson(R.error("用户名或密码错误！"));
            return;
        }
        adminLoginLog.setId(null);
        login(adminUser, adminLoginLog);

    }

    /**
     * 添加登录日志
     */
    private void addLoginLog(AdminLoginLog adminLoginLog, Long userId, int authResult) {
        adminLoginLog.setCreateUserId(userId);
        adminLoginLog.setAuthResult(authResult);
        adminLoginLog.setLoginTime(new Date());
        adminLoginLog.save();
    }

    /**
     * 单用户登录的逻辑
     */
    @NotAction
    private void login(AdminUser user, AdminLoginLog adminLoginLog) {
        if (user.getStatus() == 0) {
            addLoginLog(adminLoginLog, user.getUserId(), 2);
            renderJson(R.error("账户被禁用！"));
            return;
        }
        if (user.getStatus() == 2) {
            user.setStatus(1);
        }
        addLoginLog(adminLoginLog, user.getUserId(), 1);
        BaseUtil.setUser(user);
        String token = IdUtil.simpleUUID();
        user.setLastLoginIp(ServletUtil.getClientIP(getRequest()));
        user.setLastLoginTime(new Date());
        user.update();
        user.setRoles(adminRoleService.queryRoleIdsByUserId(user.getUserId()));
        RedisManager.getRedis().setex(token, 3600 * 24 * 7, user);
        Integer type = getParaToInt("type", 1);
        BaseUtil.setToken(user.getUserId(), token, type);
        AdminUserHisTable userHisTable = AdminUserHisTable.dao.findFirst("select * from 72crm_admin_user_his_table where  type = ? and user_id = ? and his_table = 1", 3, user.getUserId());
        boolean flag = false;
        if (userHisTable != null) {
            flag = true;
        }
        Integer isReadNotice = Db.queryInt("select status from 72crm_admin_user_config where user_id = ? and name = 'readNotice'", BaseUtil.getUserId());
        EmailAccount account = EmailAccount.getEmailAccount(BaseUtil.getUserId());
        setCookie("Admin-Token", token, 3600 * 24 * 30, true);
        renderJson(R.ok().put("Admin-Token", token).put("user", user.remove("password", "salt"))
                .put("auth", adminRoleService.auth(user.getUserId()))
                .put("isAdmin", user.getUserId().equals(BaseUtil.getSuperUser()))
                .put("cardAuth", flag)
                .put("emailId", account != null ? account.getId() : null)
                .put("isReadNotice", isReadNotice)
                .put("firstModel", adminConfigService.firstModel())
        );
    }

    /**
     * @author zhangzhiwei
     * 退出登录
     */
    public void logout() {
        String token = BaseUtil.getToken(getRequest());
        if (!StrUtil.isEmpty(token)) {
            AdminUser adminUser = RedisManager.getRedis().get(token);
            if (adminUser != null && adminUser.getUserId() != null) {
                CaffeineCache.ME.remove("role:permissions:", adminUser.getUserId().toString());
            }
            RedisManager.getRedis().del(token);
            removeCookie("Admin-Token");
        }
        renderJson(R.ok());
    }

    public void version() {
        renderJson(R.ok().put("name", BaseConstant.NAME).put("version", BaseConstant.VERSION));
    }

    public void ping() {
        System.out.println(ServletUtil.getClientIP(getRequest()));
        List<String> arrays = new ArrayList<>();
        Connection connection = null;
        try {
            connection = Db.use().getConfig().getConnection();
            if (connection != null) {
                arrays.add("数据库连接成功");
            }
        } catch (Exception e) {
            arrays.add("数据库连接异常");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    Log.getLog(getClass()).error("", e);
                }
            }

        }
        try {
            if (RedisManager.getRedis() != null) {
                arrays.add("Redis配置成功");
            } else {
                arrays.add("Redis配置失败");
            }
        } catch (Exception e) {
            arrays.add("Redis配置失败");
        }
        renderJson(R.ok().put("data", arrays));
    }

    /**
     * @author wyq
     * 接入钉钉
     */
    public void dingLogin(String code) {
        String appkey = prop.get("appkey");
        String appSecret = prop.get("appSecret");
        String tokenJson = HttpUtil.get("https://oapi.dingtalk.com/gettoken?appkey=" + appkey + "&appsecret=" + appSecret);
        String accessToken = JSONObject.parseObject(tokenJson).getString("access_token");
        String userJson = HttpUtil.get("https://oapi.dingtalk.com/user/getuserinfo?access_token=" + accessToken + "&code=" + code);
        System.out.println("userJson:" + userJson);
        String userId = JSONObject.parseObject(userJson).getString("userid");
        String userInfo = HttpUtil.get("https://oapi.dingtalk.com/user/get?access_token=" + accessToken + "&userid=" + userId);
        String mobile = JSONObject.parseObject(userInfo).getString("mobile");
        Integer isUser = Db.queryInt("select count(*) from 72crm_admin_user where mobile = ?", mobile);
        if (isUser > 0) {
            AdminUser user = AdminUser.dao.findFirst(Db.getSqlPara("admin.user.queryByUserName", Kv.by("username", mobile.trim())));
            if (user.getStatus() == 2) {
                user.setStatus(1);
            }
            String key = BaseConstant.USER_LOGIN_ERROR_KEY + mobile;
            Redis redis = RedisManager.getRedis();
            redis.del(key);
            String token = IdUtil.simpleUUID();
            user.setLastLoginIp(ServletUtil.getClientIP(getRequest()));
            user.setLastLoginTime(new Date());
            user.update();
            user.setRoles(adminRoleService.queryRoleIdsByUserId(user.getUserId()));
            redis.setex(token, 3600, user);
            Integer type = getParaToInt("type", 1);
            BaseUtil.setToken(user.getUserId(), token, type);
            user.remove("password", "salt");
            System.out.println("wqe" + user);
            BaseUtil.setUser(user);
            renderJson(R.ok().put("Admin-Token", token).put("user", user).put("auth", adminRoleService.auth(user.getUserId())));
            BaseUtil.removeThreadLocal();
        } else {
            renderJson(R.error("账户不存在"));
        }
    }
}
