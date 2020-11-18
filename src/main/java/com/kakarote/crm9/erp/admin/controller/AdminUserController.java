package com.kakarote.crm9.erp.admin.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
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
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.entity.AdminUserConfig;
import com.kakarote.crm9.erp.admin.entity.AdminUserHisTable;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.FormTypeEnum;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author hmb
 */
public class AdminUserController extends Controller {

    @Inject
    private AdminUserService adminUserService;

    @Inject
    private AdminFileService adminFileService;

    /**
     * @param adminUser
     * @author hmb
     * 设置系统用户
     */
    @Permissions("manage:users:userSave")
    public void addUser(@Para("") AdminUser adminUser) {
        renderJson(adminUserService.setUser(adminUser, getPara("roleIds")));
    }

    @Permissions("manage:users:userUpdate")
    public void setUser(@Para("") AdminUser adminUser) {
        renderJson(adminUserService.setUser(adminUser, getPara("roleIds")));
    }

    /**
     * @author hmb
     * 更新状态
     */
    @Permissions("manage:users:userEnables")
    public void setUserStatus() {
        String ids = getPara("userIds");
        String status = getPara("status");
        renderJson(adminUserService.setUserStatus(ids, status));
    }

    /**
     * @param basePageRequest 分页对象
     * @author hmb
     * 查询系统用户列表
     */
    public void queryUserList(BasePageRequest<AdminUser> basePageRequest) {
        String roleId = getPara("roleId");
        String search = getPara("search");
        String isResources = getPara("isResources");
        String workId = getPara("workId");
        renderJson(adminUserService.queryUserList(basePageRequest, roleId, search, isResources, workId));
    }

    /**
     * @param basePageRequest 分页对象
     * @author zxy
     * 查询系统用户列表
     */
    public void queryUserMiNiList(BasePageRequest<AdminUser> basePageRequest) {
        renderJson(adminUserService.queryUserMiNiList(basePageRequest));
    }

    /**
     * @author hmb
     * 重置密码
     */
    @Permissions("manage:users:userEnables")
    public void resetPassword() {
        String ids = getPara("userIds");
        String pwd = getPara("password");
        renderJson(adminUserService.resetPassword(ids, pwd));
    }

    /**
     * @author hmb
     * 查询上级列表
     */
    @Permissions("manage:users:read")
    public void querySuperior() {
        String realName = getPara("realName");
        renderJson(adminUserService.querySuperior(realName));
    }

    /**
     * 查询所有用户列表
     *
     * @author hmb
     */
    @Permissions("manage:users:read")
    public void queryAllUserList() {
        renderJson(adminUserService.queryAllUserList());
    }

    /**
     * @author zxy
     * 查询系统所有用户名称
     */
    @SysLog({LogEnum.ALL_ADDRESS_BOOK, LogEnum.MY_FOLLOW_ADDRESS_BOOK})
    public void queryListName(BasePageRequest<AdminUser> basePageRequest) {
        renderJson(adminUserService.queryListName(basePageRequest));
    }

    /**
     * 查询当前登录的用户
     *
     * @author zhangzhiwei
     */
    public void queryLoginUser() {

        AdminUserHisTable userHisTable = AdminUserHisTable.dao.findFirst("select * from 72crm_admin_user_his_table where  type = ? and user_id = ? and his_table = 1", 3, BaseUtil.getUserId());
        boolean flag = false;
        if (userHisTable != null) {
            flag = true;
        }
        EmailAccount account = EmailAccount.getEmailAccount(BaseUtil.getUserId());
        Integer isReadNotice = Db.queryInt("select status from 72crm_admin_user_config where user_id = ? and name = 'readNotice'", BaseUtil.getUserId());
        AdminUser user = adminUserService.resetUser(getRequest());
        renderJson(R.ok().put("data", user)
                .put("isAdmin", user.getUserId().equals(BaseUtil.getSuperUser()))
                .put("isReadNotice", isReadNotice).put("cardAuth", flag)
                .put("emailId", account != null ? account.getId() : null)
        );
    }

    @Clear
    public void updateImg() {
        String prefix = BaseUtil.getDate();
        UploadFile uploadFile = getFile("file", prefix);
        BaseUtil.setUser(getParaToLong("userId"));
        R r = adminFileService.upload(uploadFile, null, "file", "/" + prefix);
        if (r.isSuccess()) {
            String url = (String) r.get("url");
            if (adminUserService.updateImg(url, getParaToLong("userId"))) {
                renderJson(R.ok());
                return;
            }
        }
        renderJson(R.error("修改头像失败"));
    }

    public void updatePassword() {
        String oldPass = getPara("oldPwd");
        String newPass = getPara("newPwd");
        AdminUser adminUser = BaseUtil.getUser();
        if (!BaseUtil.verify(adminUser.getUsername() + oldPass, adminUser.getSalt(), adminUser.getPassword())) {
            renderJson(R.error("密码输入错误"));
            return;
        }
        adminUser.setPassword(newPass);
        renderJson(adminUserService.updateUser(adminUser));
    }

    @NotNullValidate(value = "realname", message = "姓名不能为空")
    @NotNullValidate(value = "username", message = "用户名不能为空")
    public void updateUser(@Para("") AdminUser adminUser) {
        renderJson(adminUserService.updateUser(adminUser));
    }

    /**
     * @param id       用户ID
     * @param username 用户新账号
     * @param password 用户新密码
     * @author zhangzhiwei
     */
    @Permissions("manage:users:userUpdate")
    @NotNullValidate(value = "username", message = "账号不能为空")
    @NotNullValidate(value = "password", message = "密码不能为空")
    @NotNullValidate("id")
    public void usernameEdit(@Para("id") Integer id, @Para("username") String username, @Para("password") String password) {
        renderJson(adminUserService.usernameEdit(id, username, password));
    }

    public void queryUserByDeptId(@Para("deptId") Integer deptId) {
        renderJson(R.ok().put("data", adminUserService.queryAllUserByDeptId(deptId)));
    }

    /**
     * @author zxy
     * 关注和取消关注
     */
    public void attention(@Para("userId") Long userId) {
        renderJson(adminUserService.attention(userId));
    }

    /**
     * excel导入员工
     */
    public void excelImport() {
        UploadFile file = getFile("file", "/excel/");
        renderJson(adminUserService.excelImport(file));
    }

    /**
     * excel导入员工
     */
    public void userExcelImport() {
        UploadFile file = getFile("file", "/excel/");
        renderJson(adminUserService.userExcelImport(file, false));
    }

    /**
     * excel导入人力资源员工
     */
    public void userImport() {
        UploadFile file = getFile("file", "/excel/");
        renderJson(adminUserService.userExcelImport(file, true));
    }

    /**
     * 下载错误模板
     *
     * @author zhangzhiwei
     */

    public void downExcel() {
        String token = get("token");
        File file = new File(BaseConstant.UPLOAD_PATH + "excel/" + token);
        if (file.exists()) {
            renderFile(file, "import_error.xlsx");
        } else {
            renderNull();
        }
    }

    /**
     * 导出
     */
    public void excelexport(BasePageRequest<AdminUser> basePageRequest) {
        String roleId = getPara("roleId");
        String search = getPara("search");
        String isResources = getPara("isResources");
        String workId = getPara("workId");
        JSONObject json = basePageRequest.getJsonObject();
        basePageRequest.setPageType(0);
        json.fluentPut("excel", "yes").fluentPut("type", CrmEnum.WORK.getType());
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            List<Record> records = (List<Record>) adminUserService.queryUserList(basePageRequest, roleId, search, isResources, workId).get("data");
            List<Map<String, Object>> list = new ArrayList<>();
            String[] s = new String[]{"username", "user_id", "status", "roleId", "parent_id", "img", "dept_id", "create_time"};
            for (Record record : records) {
                record.remove(s);
                list.add(record.getColumns());
            }
            List<Record> headList = new ArrayList<>();
            getFields(headList);
            headList.forEach(head -> {
                writer.addHeaderAlias(head.getStr("fieldName"), head.getStr("name"));
            });

            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);

            for (int i = 0; i < headList.size(); i++) {
                writer.setColumnWidth(i, 25);
            }

            HttpServletResponse response = getResponse();

            response.setCharacterEncoding("UTF-8");

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=taskFile.xls");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * 下载上传模版
     */
    public void updateTemplate() {
        List<Record> fields = new ArrayList<>();
        getFields(fields);
        //列标题
        List<String> handers = new ArrayList<>();
        //下拉框数据(数组)
        List<String[]> downData = new ArrayList<>();
        //下拉列的序号
        List<String> downRows = new ArrayList<>();

        int temp = 0;
        for (Record field : fields) {

            handers.add(field.getStr("name"));
            if (Integer.parseInt(field.getStr("type")) == FormTypeEnum.SELECT.getType()) {
                String[] options = field.getStr("options").split(",");
                downData.add(options);
                downRows.add(temp + "");
            }
            temp++;
        }
        try (HSSFWorkbook wb = com.kakarote.crm9.utils.ExcelUtil.createExcelTemplate("系统用户导入模板为必填项", handers, downData, downRows, null, null);) {

            HttpServletResponse response = getResponse();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=customer_import.xls");

            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    private void getFields(List<Record> fields) {
        List<String> names = Arrays.asList("手机号(*)", "登录密码(*)", "姓名(*)", "性别","上级人员", "邮箱", "岗位", "部门", "区域", "工号", "职级", "状态", "角色");
        List<String> fieldName = Arrays.asList("mobile", "password", "realname", "sex", "parentName","email", "post", "deptName", "area", "job_number", "rank", "condition", "roleName");
        for (int i = 0; i < names.size(); i++) {
            Record record = new Record();
            record.set("fieldName", fieldName.get(i));
            record.set("name", names.get(i));
            record.set("type", FormTypeEnum.TEXT.getType());
            if (i == 3) {
                record.set("type", FormTypeEnum.SELECT.getType());
                record.set("options", "男,女");
            }
            if (i == 10) {
                record.set("type", FormTypeEnum.SELECT.getType());
                record.set("options", "请假,空闲,任务中");
            }
            fields.add(record);
        }
    }

    /**
     * @author wyq
     * 查询下属user
     */
    public void querySubUserList() {
        renderJson(R.ok().put("data", adminUserService.querySubUserList(BaseUtil.getUserId())));
    }

    /**
     * 查询用户信息，无需任何权限
     *
     * @param userId 用户ID
     */
    public void queryUserInfo(@Para("userId") Long userId) {
        AdminUser adminUser = adminUserService.queryUserInfo(userId);
        renderJson(R.ok().put("data", adminUser));
    }

    /**
     * 如果是超级管理员查询全部用户
     * 若不是查询下级用户
     */
    public void queryAuthUserList() {
        List<Record> recordList;
        if (BaseUtil.isAdmin()) {
            recordList = Db.find("select user_id,realname,img from `72crm_admin_user` where status = 1");
        } else {
            recordList = adminUserService.querySubUserList(BaseUtil.getUserId());
        }
        renderJson(R.ok().put("data", recordList));
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

    /**
     * 标记人员为人力资源
     */
    public void setResources(@Para("userIds") String userIds, @Para("isResources") Integer isResources, @Para("workId") String workId) {
        renderJson(adminUserService.setResources(userIds, isResources, workId));
    }
}
