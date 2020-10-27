package com.kakarote.crm9.erp.test.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminUserRole;
import com.kakarote.crm9.erp.test.entity.TestAdminUser;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;


/**
 * 测试用户登录
 *
 * @author zyy
 */
public class TestAdminLoginController extends Controller {


    /*@Inject
    TestAdminLoginService testAdminLoginService;*/

    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
    /**
     * 测试注册
     */
    public void register(BasePageRequest<TestAdminUser> basePageRequest) {
        TestAdminUser testAdminUser = basePageRequest.getData();

        String username = testAdminUser.getUsername();
        String password = testAdminUser.getPassword();
        String mobile = testAdminUser.getMobile();

        //查看用户名和密码是否为空
        if (!StringUtils.isNotBlank(username)) {
            renderJson(R.error("请输入用户名"));
            return;
        }
        if (!StringUtils.isNotBlank(password)) {
            renderJson(R.error("请输入密码"));
            return;
        }
        if (!StringUtils.isNotBlank(mobile) || mobile.matches(REGEX_MOBILE)) {
            renderJson(R.error("请输入正确手机号"));
            return;
        }

        //查询手机号是否存在

        String sql = Db.getSql("test.adminUser.findBy");
        SqlPara sqlPara = Db.getSqlPara("test.adminUser.findBy", Kv.by("username", username).set("mobile", mobile));
        Record record = Db.findFirst(sqlPara);
        if(record != null){
            renderJson(R.error("用户已存在"));
            return;
        }

        //生成盐密码加密
        String salt = UUID.randomUUID().toString().replaceAll("-","");
        String sign = BaseUtil.sign(username + password, salt);

        //补充数据
        testAdminUser.setUsername(username).setPassword(sign).setMobile(mobile).setSalt(salt).setCreateTime(new Date()).setStatus(1);

        renderJson(testAdminUser.save()? R.ok(): R.error("添加失败"));
        //添加权限表
        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setUserId(testAdminUser.getUserId());

        testAdminUser.getRoles().forEach(list -> {
            adminUserRole.setRoleId(list);
            adminUserRole.save();
        }
        );


    }

}
