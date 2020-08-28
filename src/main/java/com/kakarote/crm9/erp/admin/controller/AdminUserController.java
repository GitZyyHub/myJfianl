package com.kakarote.crm9.erp.admin.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.SysLog;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.common.constant.LogEnum;
import com.kakarote.crm9.erp.admin.common.AdminModelEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.entity.AdminUserConfig;
import com.kakarote.crm9.erp.admin.entity.AdminUserHisTable;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.utils.AdminLogUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.io.File;
import java.util.List;

/**
 * @author hmb
 */
public class AdminUserController extends Controller {

    @Inject
    private AdminUserService adminUserService;

    @Inject
    private AdminFileService adminFileService;

    /**
     * @author hmb
     * 设置系统用户
     * @param adminUser
     */
    @Permissions("manage:users:userSave")
    public void addUser(@Para("") AdminUser adminUser){
        renderJson(adminUserService.setUser(adminUser,getPara("roleIds")));
    }

    @Permissions("manage:users:userUpdate")
    public void setUser(@Para("") AdminUser adminUser){
        renderJson(adminUserService.setUser(adminUser,getPara("roleIds")));
    }

    /**
     * @author hmb
     * 更新状态
     */
    @Permissions("manage:users:userEnables")
    public void setUserStatus(){
        String ids = getPara("userIds");
        String status = getPara("status");
        renderJson(adminUserService.setUserStatus(ids,status));
    }

    /**
     * @author hmb
     * 查询系统用户列表
     * @param basePageRequest 分页对象
     */
    public void queryUserList(BasePageRequest<AdminUser> basePageRequest){
        String roleId = getPara("roleId");
        String search = getPara("search");
        renderJson(adminUserService.queryUserList(basePageRequest,roleId,search));
    }

    /**
     * @author zxy
     * 查询系统用户列表
     * @param basePageRequest 分页对象
     */
    public void queryUserMiNiList(BasePageRequest<AdminUser> basePageRequest){
        renderJson(adminUserService.queryUserMiNiList(basePageRequest));
    }

    /**
     * @author hmb
     * 重置密码
     */
    @Permissions("manage:users:userEnables")
    public void resetPassword(){
        String ids = getPara("userIds");
        String pwd = getPara("password");
        renderJson(adminUserService.resetPassword(ids,pwd));
    }

    /**
     * @author hmb
     * 查询上级列表
     */
    @Permissions("manage:users:read")
    public void querySuperior(){
        String realName = getPara("realName");
        renderJson(adminUserService.querySuperior(realName));
    }

    /**
     * 查询所有用户列表
     * @author hmb
     */
    @Permissions("manage:users:read")
    public void queryAllUserList(){
        renderJson(adminUserService.queryAllUserList());
    }
    /**
     * @author zxy
     * 查询系统所有用户名称
     */
    @SysLog({LogEnum.ALL_ADDRESS_BOOK,LogEnum.MY_FOLLOW_ADDRESS_BOOK})
    public void queryListName(BasePageRequest<AdminUser> basePageRequest){
        renderJson(adminUserService.queryListName(basePageRequest));
    }

    /**
     * 查询当前登录的用户
     * @author zhangzhiwei
     */
    public void queryLoginUser(){

        AdminUserHisTable userHisTable = AdminUserHisTable.dao.findFirst("select * from 72crm_admin_user_his_table where  type = ? and user_id = ? and his_table = 1",3,BaseUtil.getUserId());
        boolean flag = false;
        if (userHisTable != null){
            flag = true;
        }
        EmailAccount account= EmailAccount.getEmailAccount(BaseUtil.getUserId());
        Integer isReadNotice = Db.queryInt("select status from 72crm_admin_user_config where user_id = ? and name = 'readNotice'", BaseUtil.getUserId());
        AdminUser user = adminUserService.resetUser(getRequest());
        renderJson(R.ok().put("data",user)
                .put("isAdmin",user.getUserId().equals(BaseUtil.getSuperUser()))
                .put("isReadNotice", isReadNotice).put("cardAuth", flag)
                .put("emailId", account != null ? account.getId() : null)
        );
    }
    @Clear
    public void updateImg(){
        String prefix= BaseUtil.getDate();
        UploadFile uploadFile=getFile("file",prefix);
        BaseUtil.setUser(getParaToLong("userId"));
        R r=adminFileService.upload(uploadFile,null,"file","/"+prefix);
        if(r.isSuccess()){
            String url= (String) r.get("url");
            if(adminUserService.updateImg(url,getParaToLong("userId"))){
                renderJson(R.ok());
                return;
            }
        }
        renderJson(R.error("修改头像失败"));
    }
    public void updatePassword(){
        String oldPass=getPara("oldPwd");
        String newPass=getPara("newPwd");
        AdminUser adminUser=BaseUtil.getUser();
        if(!BaseUtil.verify(adminUser.getUsername()+oldPass,adminUser.getSalt(),adminUser.getPassword())){
            renderJson(R.error("密码输入错误"));
            return;
        }
        adminUser.setPassword(newPass);
        renderJson(adminUserService.updateUser(adminUser));
    }

    @NotNullValidate(value = "realname",message = "姓名不能为空")
    @NotNullValidate(value = "username",message = "用户名不能为空")
    public void updateUser(@Para("")AdminUser adminUser){
        renderJson(adminUserService.updateUser(adminUser));
    }

    /**
     *
     * @author zhangzhiwei
     * @param id 用户ID
     * @param username 用户新账号
     * @param password 用户新密码
     */
    @Permissions("manage:users:userUpdate")
    @NotNullValidate(value = "username",message = "账号不能为空")
    @NotNullValidate(value = "password",message = "密码不能为空")
    @NotNullValidate("id")
    public void usernameEdit(@Para("id")Integer id,@Para("username")String username,@Para("password")String password){
        renderJson(adminUserService.usernameEdit(id,username,password));
    }

    public void queryUserByDeptId(@Para("deptId")Integer deptId){
        renderJson(R.ok().put("data",adminUserService.queryAllUserByDeptId(deptId)));
    }
    /**
     * @author zxy
     * 关注和取消关注
     */
    public void attention(@Para("userId")Long userId){
        renderJson(adminUserService.attention(userId));
    }

    /**
     * excel导入员工
     */
    public void excelImport(){
        UploadFile file = getFile("file", "/excel/");
        renderJson(adminUserService.excelImport(file));
    }

    /**
     * 下载错误模板
     * @author zhangzhiwei
     */

    public void downExcel(){
        String token=get("token");
        File file=new File(BaseConstant.UPLOAD_PATH+"excel/"+token);
        if(file.exists()){
            renderFile(file,"import_error.xlsx");
        }else {
            renderNull();
        }
    }

    /**
     * @author wyq
     * 查询下属user
     */
    public void querySubUserList(){
        renderJson(R.ok().put("data",adminUserService.querySubUserList(BaseUtil.getUserId())));
    }

    /**
     * 查询用户信息，无需任何权限
     * @param userId 用户ID
     */
    public void queryUserInfo(@Para("userId") Long userId){
        AdminUser adminUser=adminUserService.queryUserInfo(userId);
        renderJson(R.ok().put("data",adminUser));
    }

    /**
     * 如果是超级管理员查询全部用户
     * 若不是查询下级用户
     */
    public void queryAuthUserList(){
        List<Record> recordList;
        if (BaseUtil.isAdmin()){
            recordList = Db.find("select user_id,realname,img from `72crm_admin_user` where status = 1");
        }else {
            recordList = adminUserService.querySubUserList(BaseUtil.getUserId());
        }
        renderJson(R.ok().put("data",recordList));
    }

    public void readNotice() {
        Integer count = Db.queryInt("select count(1) from 72crm_admin_user_config where user_id = ? and name = 'readNotice'", BaseUtil.getUserId());
        if (count == 0) {
            AdminUserConfig adminUserConfig = new AdminUserConfig();
            adminUserConfig.setUserId(BaseUtil.getUserId()).setStatus(1).setName("readNotice").setDescription("升级日志阅读状态").save();
        } else {
            Db.update("update 72crm_admin_user_config set status = 1 where user_id = ? and name = 'readNotice'", BaseUtil.getUserId());
        }
        renderJson(R.ok());
    }
}
