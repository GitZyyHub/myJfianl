package com.kakarote.crm9.erp.bi.common;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminDeptService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.utils.BaseUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author wyq
 */
public class BiTimeUtil {


    public Record analyzeTypeOa(BiParams biParams){
        Record record = analyzeTime(biParams);
        List<Long> userIdList = new ArrayList<>();
        Integer deptId = biParams.getDeptId();
        Long userId = biParams.getUserId();
        Integer isUser = biParams.getIsUser();
        if (isUser == 0){
            if (deptId == null) {
                deptId = Db.queryInt("select dept_id from `72crm_admin_dept` where pid = 0 ");
            }
            List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
            deptIdList.add(deptId);
            SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIdList));
            userIdList = Db.query(sqlPara.getSql(), sqlPara.getPara());
        }else {
            if (userId == null){
                userIdList = Db.query("select user_id from `72crm_admin_user` where status != 0");
            }else {
                userIdList.add(userId);
            }
        }
        return record.set("userIds", CollectionUtil.join(userIdList, ","));
    }

    public Record analyzeType(BiParams biParams){
        Record record = analyzeTime(biParams);
        record.set("userIds", CollectionUtil.join(analyzeAuth(biParams), ","));
        return record;
    }

    public List<Long> analyzeAuth(BiParams biParams) {
        List<Long> userIdList = new ArrayList<>();
        Integer deptId = biParams.getDeptId();
        Long userId = biParams.getUserId();
        Integer isUser = biParams.getIsUser();
        if (isUser == 0){
            if (deptId == null) {
                deptId = BaseUtil.getUser().getDeptId();
            }
            List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
            deptIdList.add(deptId);
            SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIdList));
            userIdList = Db.query(sqlPara.getSql(), sqlPara.getPara());
        }else {
            if (userId == null){
                userIdList = Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                userIdList.add(BaseUtil.getUserId());
            }else {
                userIdList.add(userId);
            }
        }
        Integer menuId = biParams.getMenuId();
        //获取数据权限范围内的userId，取交集
        dataFilter(menuId, userIdList);
        return userIdList;
    }

    public Long dataFilter(Integer menuId, Long userId) {
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(userId);
        dataFilter(menuId, userIdList);
        if (CollectionUtil.isEmpty(userIdList)) {
            return 0L;
        }
        return userId;
    }

    /**
     * 数据权限过滤
     *
     * @param menuId
     * @param userIdList
     * @return
     */
    public void dataFilter(Integer menuId, List<Long> userIdList) {
        if (!Objects.equals(BaseUtil.getUserId(), BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole())) {
            List<Long> authUserIdList = new ArrayList<>();
            Integer dataType = Db.queryInt(Db.getSql("bi.base.queryDataTypeByUserId"), BaseUtil.getUserId(), menuId);
            if (dataType == null) {
                userIdList.clear();
            } else if (dataType != 5) {
                if (dataType == 1) {
                    authUserIdList.add(BaseUtil.getUserId());
                } else if (dataType == 2) {
                    authUserIdList = Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                    authUserIdList.add(BaseUtil.getUserId());
                } else if (dataType == 3) {
                    authUserIdList = Db.query("select user_id from 72crm_admin_user where dept_id = ?", BaseUtil.getUser().getDeptId());
                } else if (dataType == 4) {
                    List<Integer> deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(BaseUtil.getUser().getDeptId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                    deptIdList.add(BaseUtil.getUser().getDeptId());
                    SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIdList));
                    authUserIdList = Db.query(sqlPara.getSql(), sqlPara.getPara());
                }
                userIdList.retainAll(authUserIdList);
            }
        }
    }

    public Record analyzeTime(BiParams biParams){
        Date beginDate = DateUtil.date();
        Date endDate = DateUtil.date();
        Integer cycleNum = 12;
        String sqlDateFormat = "%Y%m";
        String dateFormat = "yyyyMM";
        String type = biParams.getType();
        String startTime = biParams.getStartTime();
        String endTime = biParams.getEndTime();
        if (StrUtil.isNotEmpty(type)){
            if ("year".equals(type)){
                beginDate = DateUtil.beginOfYear(DateUtil.date());
                endDate = DateUtil.endOfYear(DateUtil.date());
            }else if ("lastYear".equals(type)){
                beginDate = DateUtil.beginOfYear(DateUtil.offsetMonth(DateUtil.date(),-12));
                endDate = DateUtil.endOfYear(DateUtil.offsetMonth(DateUtil.date(),-12));
            }else if ("quarter".equals(type)){
                beginDate = DateUtil.beginOfQuarter(DateUtil.date());
                endDate = DateUtil.endOfQuarter(DateUtil.date());
                cycleNum = 3;
            }else if ("lastQuarter".equals(type)){
                beginDate = DateUtil.beginOfQuarter(DateUtil.offsetMonth(DateUtil.date(),-3));
                endDate = DateUtil.endOfQuarter(DateUtil.offsetMonth(DateUtil.date(),-3));
                cycleNum = 3;
            }else if ("month".equals(type)){
                beginDate = DateUtil.beginOfMonth(DateUtil.date());
                endDate = DateUtil.endOfMonth(DateUtil.date());
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = (int)DateUtil.between(beginDate,endDate,DateUnit.DAY)+1;
            }else if ("lastMonth".equals(type)){
                beginDate = DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(),-1));
                endDate = DateUtil.endOfMonth(DateUtil.offsetMonth(DateUtil.date(),-1));
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = (int)DateUtil.between(beginDate,endDate,DateUnit.DAY)+1;
            }else if ("week".equals(type)){
                beginDate = DateUtil.beginOfWeek(DateUtil.date());
                endDate = DateUtil.endOfWeek(DateUtil.date());
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 7;
            }else if ("lastWeek".equals(type)){
                beginDate = DateUtil.beginOfWeek(DateUtil.offsetWeek(DateUtil.date(),-1));
                endDate = DateUtil.endOfWeek(DateUtil.offsetWeek(DateUtil.date(),-1));
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 7;
            }else if ("today".equals(type)){
                beginDate = DateUtil.beginOfDay(DateUtil.date());
                endDate = DateUtil.endOfDay(DateUtil.date());
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 1;
            }else if ("yesterday".equals(type)){
                beginDate = DateUtil.offsetDay(DateUtil.date(),-1);
                endDate = DateUtil.offsetDay(DateUtil.date(),-1);
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 1;
            }
        }else if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)){
            Date start;
            Date end;
            if (startTime.length() == 6) {
                start = DateUtil.parse(startTime, "yyyyMM");
                end = DateUtil.endOfMonth(DateUtil.parse(endTime, "yyyyMM"));
            } else {
                start = DateUtil.parse(startTime);
                end = DateUtil.parse(endTime);
            }
            Integer startMonth = Integer.valueOf(DateUtil.format(start,"yyyyMM"));
            Integer endMonth = Integer.valueOf(DateUtil.format(end,"yyyyMM"));
            if (startMonth.equals(endMonth)){
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                Long diffDay = DateUtil.between(start,end,DateUnit.DAY);
                cycleNum = diffDay.intValue() + 1;
            }else {
                sqlDateFormat = "%Y%m";
                dateFormat = "yyyyMM";
                Integer diffYear = Integer.valueOf(endMonth.toString().substring(0,4)) - Integer.valueOf(startMonth.toString().substring(0,4));
                Integer diffMonth = endMonth%100 - startMonth%100 + 1;
                cycleNum = diffYear*12 + diffMonth;
            }
            beginDate = start;
            endDate = end;
        }
        Integer beginTime = Integer.valueOf(DateUtil.format(beginDate,dateFormat));
        Integer finalTime = Integer.valueOf(DateUtil.format(endDate,dateFormat));
        return new Record().set("sqlDateFormat",sqlDateFormat).set("dateFormat",dateFormat).set("beginDate",beginDate).set("endDate",endDate).set("cycleNum",cycleNum).set("beginTime",beginTime).set("finalTime",finalTime);
    }

    public Integer estimateTime(Integer beginTime){
        if (beginTime < 1000000 && beginTime%100 == 12){
            beginTime = beginTime + 89;
        }else if (beginTime > 1000000){
            int month = DateUtil.month(DateUtil.parse(beginTime.toString(),"yyyyMMdd"))+1;
            if (month == 1||month == 3||month == 5||month == 7||month == 8||month == 10){
                if (beginTime%100 == 31){
                    beginTime = beginTime + 70;
                }else {
                    beginTime++;
                }
            }else if (month == 4||month == 6||month == 9||month == 11){
                if (beginTime%100 == 30){
                    beginTime = beginTime + 71;
                }else {
                    beginTime++;
                }
            }else if (month == 2){
                int year = DateUtil.year(DateUtil.parse(beginTime.toString(),"yyyyMMdd"));
                if (DateUtil.isLeapYear(year) && beginTime%100 == 29){
                    beginTime = beginTime + 72;
                }else if (!DateUtil.isLeapYear(year) && beginTime%100 == 28){
                    beginTime = beginTime + 73;
                }else {
                    beginTime++;
                }
            }else if (month == 12){
                if (beginTime%100 == 31){
                    beginTime = beginTime + 8870;
                }else{
                    beginTime++;
                }
            }
        }else {
            beginTime++;
        }
        return beginTime;
    }

    public Integer analyzeType(String type){
        int status;
        if (type == null){
            return 11;
        }
        switch(type){
            case "year":
                status = 9;
                break;
            case "lastYear":
                status = 10;
                break;
            case "quarter":
                status = 7;
                break;
            case "lastQuarter":
                status = 8;
                break;
            case "month":
                status = 5;
                break;
            case "lastMonth":
                status = 6;
                break;
            case "week":
                status = 3;
                break;
            case "lastWeek":
                status = 4;
                break;
            case "today":
                status = 1;
                break;
            case "yesterday":
                status = 2;
                break;
            default:
                status = 11;
                break;
        }
        return status;
    }
    public String[] getAddress(){
        return new String[]{"北京","上海","天津","广东","浙江",
                "海南","福建","湖南","湖北","重庆","辽宁","吉林",
                "黑龙江","河南","河北","陕西","甘肃","宁夏","西藏",
                "新疆","青海","四川","广西","贵州","江西","江苏",
                "云南","内蒙古","山东","山西","安徽"};
    }

    /**
     * 计算与上次的比较
     * @param record 类型
     */
    public void prevAnalyzeType(Record record){
        Date beginDate = DateUtil.date();
        Date endDate = DateUtil.date();
        int cycleNum = 12;
        String sqlDateFormat = "%Y%m";
        String dateFormat = "yyyyMM";
        String userIds;
        Integer deptId = record.getInt("deptId");
        Long userId = record.getLong("userId");
        String type = record.getStr("type");
        if (StrUtil.isNotEmpty(type)){
            if ("year".equals(type)){
                beginDate = DateUtil.beginOfYear(DateUtil.offsetMonth(DateUtil.date(),-12));
                endDate = DateUtil.endOfYear(DateUtil.offsetMonth(DateUtil.date(),-12));
            }else if ("lastYear".equals(type)){
                beginDate = DateUtil.beginOfYear(DateUtil.offsetMonth(DateUtil.date(),-24));
                endDate = DateUtil.endOfYear(DateUtil.offsetMonth(DateUtil.date(),-24));
            }else if ("quarter".equals(type)){
                beginDate = DateUtil.beginOfQuarter(DateUtil.offsetMonth(DateUtil.date(),-3));
                endDate = DateUtil.endOfQuarter(DateUtil.offsetMonth(DateUtil.date(),-3));
                cycleNum = 3;
            }else if ("lastQuarter".equals(type)){
                beginDate = DateUtil.beginOfQuarter(DateUtil.offsetMonth(DateUtil.date(),-6));
                endDate = DateUtil.endOfQuarter(DateUtil.offsetMonth(DateUtil.date(),-6));
                cycleNum = 3;
            }else if ("month".equals(type)){
                beginDate = DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(),-1));
                endDate = DateUtil.endOfMonth(DateUtil.offsetMonth(DateUtil.date(),-1));
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = (int)DateUtil.between(beginDate,endDate,DateUnit.DAY)+1;
            }else if ("lastMonth".equals(type)){
                beginDate = DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(),-2));
                endDate = DateUtil.endOfMonth(DateUtil.offsetMonth(DateUtil.date(),-2));
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = (int)DateUtil.between(beginDate,endDate,DateUnit.DAY)+1;
            }else if ("week".equals(type)){
                beginDate = DateUtil.beginOfWeek(DateUtil.offsetWeek(DateUtil.date(),-1));
                endDate = DateUtil.endOfWeek(DateUtil.offsetWeek(DateUtil.date(),-1));
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 7;
            }else if ("lastWeek".equals(type)){
                beginDate = DateUtil.beginOfWeek(DateUtil.offsetWeek(DateUtil.date(),-2));
                endDate = DateUtil.endOfWeek(DateUtil.offsetWeek(DateUtil.date(),-2));
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 7;
            }else if ("today".equals(type)){
                beginDate = DateUtil.offsetDay(DateUtil.date(),-1);
                endDate = DateUtil.offsetDay(DateUtil.date(),-1);
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 1;
            }else if ("yesterday".equals(type)){
                beginDate = DateUtil.offsetDay(DateUtil.date(),-2);
                endDate = DateUtil.offsetDay(DateUtil.date(),-2);
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
                cycleNum = 1;
            }
        }

        if (userId != null){
            userIds = userId.toString();
        }else if(deptId!=null){
            List<Record> records = Aop.get(AdminDeptService.class).queryDeptByParentDept(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
            List<Integer> deptIds = new ArrayList<>();
            deptIds.add(deptId);
            records.forEach(dept ->deptIds.add(dept.getInt("id")));
            SqlPara sqlPara = Db.getSqlPara("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIds));
            List<Long> userIdList =  Db.query(sqlPara.getSql(), sqlPara.getPara());
            userIds = CollectionUtil.join(userIdList,",");
        }else {
            userIds="";
        }
        Integer beginTime = Integer.valueOf(DateUtil.format(beginDate,dateFormat));
        Integer finalTime = Integer.valueOf(DateUtil.format(endDate,dateFormat));
        record.set("sqlDateFormat",sqlDateFormat).set("dateFormat",dateFormat).set("beginDate",beginDate).set("endDate",endDate).set("cycleNum",cycleNum).set("userIds",userIds).set("beginTime",beginTime).set("finalTime",finalTime);
    }
}
