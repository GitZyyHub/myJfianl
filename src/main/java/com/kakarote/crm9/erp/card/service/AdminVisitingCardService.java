package com.kakarote.crm9.erp.card.service;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.config.Constants;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.entity.AdminUserHisTable;
import com.kakarote.crm9.erp.card.entity.AdminVisitingCard;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * @author zxy
 * 名片表
 */
public class AdminVisitingCardService {
    /**
     * @author zxy
     * 设为员工默认访问认名片或者关联员工
     */
    public R setSaveCard(JSONObject jsonObject){
        AdminUserHisTable table = jsonObject.toJavaObject(AdminUserHisTable.class);
        AdminUserHisTable userHisTable = AdminUserHisTable.dao.findFirst("select * from 72crm_admin_user_his_table where user_id = ? and type = 2",table.getUserId());
        table.setType(2);
        table.setHisTable(1);
        Boolean flag = false;
        if (userHisTable != null){
            table.setHisTableId(userHisTable.getHisTableId());
            flag = table.update();
        }else {
            flag = table.save();
        }
        return  flag? R.ok(): R.error();
    }
    /**
     * @author zxy
     * 移除员工默认访问认名片或者关联员工
     */
    public R deleteByUserId(Long userId){
        AdminUserHisTable userHisTable = AdminUserHisTable.dao.findFirst("select * from 72crm_admin_user_his_table where user_id = ? and type = 2",userId);
        userHisTable.setHisTable(0);
        return userHisTable.update()? R.ok(): R.error();
    }
    /**
     * @author zxy
     * 根据员工id，获取名片
     */
    public R queryByUserId(Long userId){
        Record record = Db.findFirst(Db.getSql("card.visiting.queryByUserId") , BaseConstant.UPLOAD_PATH, BaseUtil.getIpAddress(),userId);
        if (StrUtil.isNotEmpty(record.get("img"))) {
            record.set("img", record.getStr("img").replace("\\", "/"));
        }
        return R.ok().put("data",record);
    }
    /**
     * @author zxy
     * 保存个人信息
     */
    public R save(JSONObject jsonObject){
        AdminUser adminUser = jsonObject.toJavaObject(AdminUser.class);
        adminUser.setUserId(BaseUtil.getUserId());
        adminUser.update();
        AdminVisitingCard card = jsonObject.toJavaObject(AdminVisitingCard.class);
        card.setUserId(BaseUtil.getUserId());
        AdminVisitingCard adminVisitingCard = AdminVisitingCard.dao.findFirst("select * from 72crm_admin_visiting_card where user_id = ? ",card.getUserId());
        Boolean flag = false;
        if (adminVisitingCard != null){
            card.setCardId(adminVisitingCard.getCardId());
            flag = card.update();
        }else {
            flag = card.save();
        }
        return flag? R.ok(): R.error();
    }
    /**
     * @author zxy
     * 保存海报
     */
    public R upload(UploadFile file, String prefix, String userId){
        AdminVisitingCard adminVisitingCard = AdminVisitingCard.dao.findFirst("select * from 72crm_admin_visiting_card where user_id = ?",userId);
        if (ClassLoaderUtil.isPresent("com.jfinal.server.undertow.UndertowServer")) {
            adminVisitingCard.setOfficialImg(BaseUtil.getIpAddress() + prefix + "/" + file.getFileName());
        } else {
            adminVisitingCard.setOfficialImg(BaseUtil.getIpAddress() + new Constants().getBaseUploadPath() + "/" + prefix + "/" + file.getFileName());
        }

        return adminVisitingCard.update()? R.ok(): R.error();
    }
    /**
     * @author zxy
     * 保存海报
     */
    public R checkAuth(){

        Long userId= BaseUtil.getUserId();

        AdminUser adminUser= AdminUser.dao.findById(userId);
        if(adminUser==null){
            return R.error().put("error","查无当前员工");
        }
      /* AdminCompanyMiniapp adminCompanyMiniapp = AdminCompanyMiniapp.queryApp(userId);
        if (adminCompanyMiniapp == null){*/
            return R.ok().put("auth",true);
        /*}else {
            return R.ok().put("auth",true);
        }
       return null;*/
    }
    /**
     * @author zxy
     * 设为关联人员
     */
    public R relevance(List<Long> userIds){
        for (Long userId: userIds) {
            AdminUserHisTable userHisTable = AdminUserHisTable.dao.findFirst("select * from 72crm_admin_user_his_table where user_id = ? and type = 3",userId);
            if (userHisTable == null){
                userHisTable = new AdminUserHisTable();
            }
            userHisTable.setUserId(userId);
            userHisTable.setType(3);
            userHisTable.setHisTable(1);
            if (userHisTable.getHisTableId() != null){
                userHisTable.setHisTableId(userHisTable.getHisTableId());
                userHisTable.update();
            }else {
                userHisTable.save();
            }
        }
        return R.ok();
    }
    /**
     * @author zxy
     * 解除关联人员
     */
    public R relieve(List<Long> userIds){
        SqlPara sqlPara = Db.getSqlPara("admin.user.updateHisTable", Kv.by("userIds",userIds));
        SqlPara cardSqlPara = Db.getSqlPara("card.visiting.deleteByIds", Kv.by("userIds",userIds));
        Db.delete(cardSqlPara.getSql(),cardSqlPara.getPara());
        return Db.update(sqlPara.getSql(),sqlPara.getPara())>0? R.ok(): R.error();
    }
}
