package com.kakarote.crm9.erp.oa.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.oa.entity.OaAnnouncement;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;

import java.util.HashSet;
import java.util.List;

/**
 * 公告
 */
public class OaAnnouncementService {


    /**
     * 添加或修改
     */
    public R saveAndUpdate(OaAnnouncement oaAnnouncement) {
        boolean flag;
        oaAnnouncement.setDeptIds(TagUtil.fromString(oaAnnouncement.getDeptIds()));
        oaAnnouncement.setOwnerUserIds(TagUtil.fromString(oaAnnouncement.getOwnerUserIds()));
        if (oaAnnouncement.getAnnouncementId() == null) {
            oaAnnouncement.setCreateTime(DateUtil.date());
            oaAnnouncement.setUpdateTime(DateUtil.date());
            flag = oaAnnouncement.save();
            Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.OA_NOTICE_MESSAGE,oaAnnouncement,0L);
        } else {
            oaAnnouncement.setUpdateTime(DateUtil.date());
            flag = oaAnnouncement.update();
        }
        return R.isSuccess(flag);
    }

    /**
     * 删除
     */
    @Before(Tx.class)
    public R delete(Integer id) {
        return R.ok().put("status", OaAnnouncement.dao.deleteById(id) ? 1 : 0);
    }

    /**
     * 根据ID查询详情
     */
    public R queryById(Integer id) {
        Record record = Db.findFirst(Db.getSql("oa.announcement.queryById"), id);
        if (record == null){
            return R.error("公告已被删除");
        }
        List<Record> ownerUserIdList = Db.find("select user_id,realname from 72crm_admin_user where find_in_set(user_id,?)",record.getStr("owner_user_ids"));
        List<Record> deptIdList = Db.find("select dept_id as id,name from 72crm_admin_dept where find_in_set(dept_id,?)",record.getStr("dept_ids"));
        record.set("owner_user_ids",ownerUserIdList).set("dept_ids",deptIdList);
        return R.ok().put("data", record);
    }

    /**
     * 倒叙查询公告集合
     * @author zhangzhiwei
     */
    public R queryList(BasePageRequest<OaAnnouncement> page, Integer type) {
        AdminUser adminUser=BaseUtil.getUser();
        Kv kv=Kv.by("type",type).set("deptId",adminUser.getDeptId()).set("userId",adminUser.getUserId());
        Page<Record> recordPage=Db.paginate(page.getPage(),page.getLimit(),Db.getSqlPara("oa.announcement.queryList",kv));
        return R.ok().put("data", recordPage);
    }

    public void readAnnouncement(Integer announcementId) {
        OaAnnouncement oaAnnouncement = OaAnnouncement.dao.findById(announcementId);
        HashSet<String> hashSet = new HashSet<>(StrUtil.splitTrim(oaAnnouncement.getReadUserIds(), ","));
        hashSet.add(BaseUtil.getUser().getUserId().toString());
        oaAnnouncement.setReadUserIds("," + String.join(",", hashSet) + ",");
        oaAnnouncement.update();
    }
}
