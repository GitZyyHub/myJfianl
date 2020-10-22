package com.kakarote.crm9.erp.admin.service;

import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.CrmAchievement;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminAchievementService {

    /**
     * 设置业绩目标
     * @author zhangzhiwei
     */
    @Before(Tx.class)
    public R setAchievement(List<CrmAchievement> achievementList) {
        achievementList.forEach(achievement -> {
            if(achievement.getObjId()==null){
                return;
            }
            Db.template("admin.achievement.deleteAchievement",achievement.toRecord().getColumns()).delete();
            achievement.save();
        });
        return R.ok();
    }
    /**
     * 新增业绩目标
     * @author zhangzhiwei
     */
    @Before(Tx.class)
    public R addAchievement(CrmAchievement achievement) {
        List<CrmAchievement> crmAchievements=new ArrayList<>();
        Map<String,Object> map=achievement.toRecord().getColumns();
        map.put("obj_id",achievement.getObjIds());
        Db.template("admin.achievement.deleteAchievement",map).delete();
        achievement.getObjIds().forEach(obj->{
            CrmAchievement crmAchievement=new CrmAchievement()._setAttrs(achievement.toRecord().getColumns());
            crmAchievement.setAchievementId(null);
            crmAchievement.setObjIds(null);
            crmAchievement.setObjId(obj);
            crmAchievements.add(crmAchievement);
        });
        Db.batchSave(crmAchievements,200);
        return R.ok();
    }

    /**
     * 删除业绩目标
     * @param achievementId 业绩目标ID
     * @author zhangzhiwei
     */
    @Before(Tx.class)
    public R deleteAchievement(Integer achievementId) {
        CrmAchievement achievement= CrmAchievement.dao.findById(achievementId);
        if(achievement!=null){
            achievement.delete();
        }
        return R.ok();
    }


    /**
     * 查询业绩目标列表
     */
    public R queryAchievementList(CrmAchievement achievement, Long userId, Integer deptId) {
        if (achievement.getType() == null) {
            achievement.setType(2);
        }
        if (achievement.getType() == 2) {
            List<Record> recordList= Aop.get(AdminDeptService.class).queryDeptByParentDept(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
            List<Integer> deptList=recordList.stream().map(record->record.getInt("id")).collect(Collectors.toList());
            deptList.add(deptId);
            achievement.put("deptIds",deptList);
            List<CrmAchievement> achievementList=achievement.template("admin.achievement.queryAchievementInfoByDept",achievement).find();
            return R.ok().put("data", achievementList);
        } else if (achievement.getType() == 3) {
            List<Long> userList = new ArrayList<>();
            if (userId==null) {
                List<Record> recordList = Aop.get(AdminUserService.class).queryAllUserByDeptId(deptId);
                userList.addAll(recordList.stream().map(record->record.getLong("id")).collect(Collectors.toList()));
            } else {
                userList.add(userId);
            }
            achievement.put("userIds",userList);
            List<CrmAchievement> achievementList=achievement.template("admin.achievement.queryAchievementInfoByUser",achievement).find();
            return R.ok().put("data", achievementList);
        }
        return null;
    }

}
