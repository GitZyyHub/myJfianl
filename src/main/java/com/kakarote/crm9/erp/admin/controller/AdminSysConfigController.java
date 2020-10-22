package com.kakarote.crm9.erp.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.cache.CaffeineCache;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminNumberSetting;
import com.kakarote.crm9.erp.admin.entity.AdminUserConfig;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统配置
 * @author hmb
 */
public class AdminSysConfigController extends Controller {

    /**
     * 系统设置KEY
     */
    private static final String SYS_CONFIG_KEY = "SYS_CONFIG";

    @Inject
    private AdminFileService adminFileService;

    /**
     * 设置系统配置
     * @author zhangzhiwei
     */
    @Permissions("manage:system:update")
    @Before(Tx.class)
    public void setSysConfig(){
        Db.delete("DELETE FROM `72crm_admin_config` WHERE `name` = ?", SYS_CONFIG_KEY);
        String prefix = BaseUtil.getDate();
        UploadFile file = getFile("file", prefix);
        Kv kv = getKv();
        if (file != null) {
            R r = adminFileService.upload(file, null, "file", "/" + prefix);
            kv.set("logo", r.get("url"));
        }
        AdminConfig adminConfig = new AdminConfig();
        adminConfig.setStatus(1);
        adminConfig.setName(SYS_CONFIG_KEY);
        adminConfig.setValue(JSON.toJSONString(kv));
        adminConfig.setDescription("系统LOGO配置");
        adminConfig.save();
        renderJson(R.ok());
    }

    /**
     * 查询系统配置
     * @author zhangzhiwei
     */
    public void querySysConfig(){
        AdminConfig adminConfig = AdminConfig.dao.findFirst("SELECT * FROM `72crm_admin_config` where name=?", SYS_CONFIG_KEY);
        if (adminConfig == null) {
            renderJson(R.ok().put("data", Kv.by("logo", "").set("name", "")));
            return;
        }
        String data = adminConfig.getValue();
        Map map = JSON.parseObject(data, Map.class);
        renderJson(R.ok().put("data", map));

    }

    /**
     * 查询模块启用设置
     * @author wyq
     */
    @Permissions("manage:configSet:read")
    public void queryModuleSetting(){
        List<Record> recordList = Db.find("select setting_id,name as module,status,value as type,description as name from 72crm_admin_config where name in ('taskExamine','project','log','book','crm','project','bi','hrm','jxc','call','applet','calendar','email') ");
        renderJson(R.ok().put("data",recordList));
    }

    /**
     * 设置模块启用
     * @author wyq
     */
    @Permissions("manage:configSet:update")
    public void setModuleSetting(@Para("") AdminConfig adminConfig){
        if ("crm".equals(AdminConfig.dao.findById(adminConfig.getSettingId()).getName())){
            renderJson(R.error("客户管理不能关闭"));
            return;
        }
        //修改模块后清除企业用户缓存
        List<Long> userIds=Db.query("SELECT user_id FROM `72crm_admin_user` ");
        userIds.forEach(userId-> CaffeineCache.ME.remove("role:permissions:", userId.toString()));
        renderJson(R.isSuccess(adminConfig.update()));
    }

    /**
     * 日志欢迎语配置
     */
    @Before(Tx.class)
    public void setLogWelcomeSpeech(){
        List<String> values = JSON.parseArray(getRawData(), String.class);
        List<AdminConfig> configList = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).length()>100){
                renderJson(R.error("第"+(i+1)+"行错误，欢迎语不能超过100字！"));
                return;
            }
            AdminConfig config = new AdminConfig();
            config.setName("logWelcomeSpeech");
            config.setValue(values.get(i));
            configList.add(config);
        }
        Db.delete("delete from `72crm_admin_config` where name = 'logWelcomeSpeech'");
        Db.batchSave(configList,50);
        renderJson(R.ok());
    }

    /**
     * 获取日志欢迎语列表
     */
    public void getLogWelcomeSpeechList(){
        List<AdminConfig> adminConfigs = AdminConfig.dao.find("select setting_id,value from `72crm_admin_config` where name = 'logWelcomeSpeech'");
        renderJson(R.ok().put("data",adminConfigs));
    }

    /**
     * 获取日志欢迎语列表
     */
    public void deleteConfigById(@Para("settingId") Integer settingId){
        boolean b = AdminConfig.dao.deleteById(settingId);
        renderJson(b? R.ok(): R.error());
    }

    /**
     * 查询手机端模块设置
     */
    public void queryMobileModuleSetting(){
        AdminUserConfig userConfig= AdminUserConfig.getConfig("MobileModuleSetting");
        if(userConfig==null){
            renderJson(R.ok().put("data",new JSONArray()));
            return;
        }
        renderJson(R.ok().put("data",JSON.parseArray(userConfig.getValue())));
    }

    /**
     * 修改手机端模块设置
     */
    public void setMobileModuleSetting(){
        AdminUserConfig userConfig = AdminUserConfig.getConfig("MobileModuleSetting");
        if(userConfig!=null){
            userConfig.setValue(getRawData());
            userConfig.update();
        }else {
            userConfig=new AdminUserConfig();
            userConfig.setStatus(1);
            userConfig.setName("MobileModuleSetting");
            userConfig.setValue(getRawData());
            userConfig.setUserId(BaseUtil.getUserId());
            userConfig.setDescription("手机端模块设置");
            userConfig.save();
        }
        renderJson(R.ok());
    }

    /**
     * 设置跟进记录常用语
     */
    @Before(Tx.class)
    public void setActivityPhrase(){
        List<String> stringList = JSON.parseArray(getRawData(), String.class);
        String name = "ActivityPhrase";
        Db.delete("delete from `72crm_admin_user_config` where user_id = ? and name = ?", BaseUtil.getUserId(),name);
        List<AdminUserConfig> adminUserConfigList = new ArrayList<>();
        stringList.forEach(s->{
            AdminUserConfig userConfig = new AdminUserConfig();
            userConfig.setStatus(1);
            userConfig.setName(name);
            userConfig.setValue(s);
            userConfig.setUserId(BaseUtil.getUserId());
            userConfig.setDescription("跟进记录常用语");
            adminUserConfigList.add(userConfig);
        });
        Db.batchSave(adminUserConfigList,20);
        renderJson(R.ok());
    }

    /**
     * 查询跟进记录常用语
     */
    public void queryActivityPhrase(){
        List<String> activityPhrase = Db.query("select value from `72crm_admin_user_config` where user_id = ? and  name = ?", BaseUtil.getUserId(), "ActivityPhrase");
        renderJson(R.ok().put("data",activityPhrase));
    }

    public void queryNumberSetting() {
        List<Record> recordList = Db.find("select setting_id,status,value as label from 72crm_admin_config where name = 'numberSetting'");
        recordList.forEach(record -> {
            List<Record> settingList = Db.find("select * from 72crm_admin_number_setting where pid = ?", record.getInt("setting_id"));
            record.set("setting", settingList);
        });
        renderJson(R.ok().put("data", recordList));
    }

    public void queryInvoiceNumberSetting() {
        Record record = Db.findFirst("select status,'invoiceNumberSetting' as name from 72crm_admin_config where name = 'numberSetting' and value = 18");
        renderJson(R.ok().put("data", record));
    }

    @Before(Tx.class)
    public void setNumberSetting() {
        JSONArray jsonArray = JSONArray.parseArray(getRawData());
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            Db.update("update 72crm_admin_config set status = ? where name = 'numberSetting' and value = ?", jsonObject.getInteger("status"), jsonObject.getString("label"));
            Db.delete("delete from 72crm_admin_number_setting where pid = ?", jsonObject.getInteger("settingId"));
            JSONArray settingArray = jsonObject.getJSONArray("setting");
            if (jsonObject.getInteger("status") == 1 && settingArray.size() < 2) {
                renderJson(R.error("编号规则不得少于两级"));
                return;
            }
            if (settingArray.size() > 0) {
                List<AdminNumberSetting> settingList = settingArray.toJavaList(AdminNumberSetting.class);
                List<Integer> typeList = settingList.stream().map(AdminNumberSetting::getType).collect(Collectors.toList());
                for (int i = 0; i < settingList.size(); i++) {
                    AdminNumberSetting setting = settingList.get(i);
                    setting.setPid(jsonObject.getInteger("settingId")).setSort(i);
                    if (setting.getType() == 3 && setting.getResetType() < 4 && !typeList.contains(2)) {
                        renderJson(R.error("启用重新开始编号需要有日期编号规则"));
                        return;
                    }
                    setting.save();
                }
            }
        }
        renderJson(R.ok());
    }
}
