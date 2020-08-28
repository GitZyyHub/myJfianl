package com.kakarote.crm9.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.SecureUtil;
import com.jfinal.aop.Aop;
import com.kakarote.crm9.common.config.JfinalConfig;
import com.kakarote.crm9.common.config.redis.Redis;
import com.kakarote.crm9.common.config.redis.RedisManager;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminRoleService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;

public class BaseUtil {

    private static ThreadLocal<AdminUser> threadLocal = new ThreadLocal<>();

    private static final String USER_ADMIN_TOKEN = "72CRM_USER_ADMIN_TOKEN";

    private static final String USER_MOBILE_TOKEN = "72CRM_USER_MOBILE_TOKEN";

    /**
     *
     * 获取当前系统是开发开始正式
     * @return true代表为真
     */
    public static boolean isDevelop() {
        return JfinalConfig.prop.getBoolean("jfinal.devMode",Boolean.TRUE);
    }

    /**
     * 获取当前是否是windows系统
     * @return true代表为真
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 签名数据
     *
     * @param key  key
     * @param salt 盐
     * @return 加密后的字符串
     */
    public static String sign(String key, String salt) {
        return SecureUtil.md5(key.concat("erp").concat(salt));
    }

    /**
     * 验证签名是否正确
     *
     * @param key  key
     * @param salt 盐
     * @param sign 签名
     * @return 是否正确 true为正确
     */
    public static boolean verify(String key, String salt, String sign) {
        return sign.equals(sign(key, salt));
    }

    /**
     * 获取当前年月的字符串
     *
     * @return yyyyMMdd
     */
    public static String getDate() {
        return DateUtil.format(new Date(), "yyyyMMdd");
    }

    //FIXME 如果获取的地址不正确,直接返回一个固定地址也可以
    public static String getIpAddress() {
        return "https://crm.food-chain.com/";

    }

    public static AdminUser getUser() {
        return threadLocal.get();
    }

    public static void setUser(AdminUser adminUser){
        threadLocal.set(adminUser);
    }

    public static void setUser(Long userId){
        AdminUser adminUser=AdminUser.dao.findById(userId);
        adminUser.setRoles(Aop.get(AdminRoleService.class).queryRoleIdsByUserId(userId));
        setUser(adminUser);
    }
    public static Long getUserId(){
        return getUser().getUserId();
    }

    public static void removeThreadLocal(){
        threadLocal.remove();
    }

    public static String getToken(HttpServletRequest request){
        return request.getHeader("Admin-Token") != null ? request.getHeader("Admin-Token") : "";
    }

    public static void userExit(Long userId,Integer type){
        userExit(userId,type,null);
    }

    public static boolean isTime(String str){
        if (Validator.isMactchRegex(Validator.BIRTHDAY, str)) {
            Matcher matcher = Validator.BIRTHDAY.matcher(str);
            if (matcher.find()) {
                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(3));
                int day = Integer.parseInt(matcher.group(5));
                // 验证月
                if (month < 1 || month > 12) {
                    return false;
                }

                // 验证日
                if (day < 1 || day > 31) {
                    return false;
                }
                // 检查几个特殊月的最大天数
                if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
                    return false;
                }
                if (month == 2) {
                    // 在2月，非闰年最大28，闰年最大29
                    return day < 29 || (day == 29 && DateUtil.isLeapYear(year));
                }
                return true;
            }
        }
        return false;
    }


    /**
     * 新增一个退出备注，前端可以给用户展示
     * @param userId 用户ID
     * @param type 登录类型 null为全部
     * @param extra 额外提示
     */
    public static void userExit(Long userId,Integer type,Integer extra){
        Redis redis = RedisManager.getRedis();
        String token = null;
        if(type==null||type==1){
            if(redis.exists(USER_ADMIN_TOKEN+userId)){
                token=redis.get(USER_ADMIN_TOKEN+userId);
                redis.del(USER_ADMIN_TOKEN+userId);
            }
        }
        if(type==null||type==2){
            if(redis.exists(USER_MOBILE_TOKEN+userId)){
                token=redis.get(USER_MOBILE_TOKEN+userId);
                redis.del(USER_MOBILE_TOKEN+userId);
            }
        }
        //1代表被挤掉提示
        if(Objects.equals(1,extra) && token!=null){
            Long time=redis.ttl(token);
            AdminUser adminUser=new AdminUser();
            adminUser.put("extra",extra);
            adminUser.put("extraTime",new Date());
            if(time>1){
                redis.setex(token,time.intValue(),adminUser);
            }
        }else {
            if(token!=null){
                redis.del(token);
            }

        }
    }
    public static void setToken(Long userId,String token,Integer type){
        //用户被挤掉给予用户提示
        userExit(userId,type,1);
        Redis redis = RedisManager.getRedis();
        if(redis.exists(token)){
            if(type==1){
                redis.setex(USER_ADMIN_TOKEN+userId,redis.ttl(token).intValue(),token);
            }else if(type==2){
                redis.setex(USER_MOBILE_TOKEN+userId,redis.ttl(token).intValue(),token);
            }
        }

    }

    public static Long getSuperUser(){
        return 16360L;
    }

    public static Integer getSuperRole(){
        return 198324;
    }

    public static boolean isAdmin(){
        return getUserId().equals(getSuperUser()) || getUser().getRoles().contains(getSuperRole());
    }

    public static Integer getWorkAdminRole(){
        return 198333;
    }

    public static Integer getSmallAdminRole(){
        return 198334;
    }

    public static Integer getSmallEditRole(){
        return 198335;
    }

    public static String getIp() {
        return getUser().get("ip");
    }
}
