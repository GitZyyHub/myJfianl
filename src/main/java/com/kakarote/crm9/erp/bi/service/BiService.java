package com.kakarote.crm9.erp.bi.service;

import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.erp.bi.common.MonthEnum;
import com.kakarote.crm9.erp.bi.entity.BiParams;
import com.kakarote.crm9.utils.BaseUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BiService{

    @Inject
    private BiTimeUtil biTimeUtil;


    /**
     * 产品销售情况统计
     * startTime 开始时间 endTime 结束时间 userId用户ID deptId部门ID
     */
    public List<Record> queryProductSell(BiParams biParams) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "product", "read");
        biParams.setMenuId(menuId);
        Record record = biTimeUtil.analyzeType(biParams);
        String userIds = record.getStr("userIds");
        List<Record> recordList = Db.find(Db.getSqlPara("bi.base.queryProductSell", Kv.by("beginTime", record.getStr("beginTime")).set("finalTime", record.getStr("finalTime")).set("userId", userIds).set("sqlDateFormat","'"+record.getStr("sqlDateFormat")+"'")));
        return recordList;
    }

    public List<Map<String, Object>> productSellExport(BiParams biParams) {
        List<Record> recordList = queryProductSell(biParams);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Record productTotal = new Record().set("categoryName", "").set("productName", "").set("contracNum", "").set("ownerUserName", "").set("customerName", "").set("productPrice", "");
            Record record = recordList.get(i);
            Integer categoryId = record.getInt("category_id");
            Integer productId = record.getInt("product_id");
            if (i > 0) {
                Record lastRecord = recordList.get(i - 1);
                Integer lastCategoryId = lastRecord.getInt("category_id");
                Integer lastProductId = lastRecord.getInt("product_id");
                //每种产品总计
                if (!productId.equals(lastProductId)) {
                    List<Record> productTotalList = recordList.stream().filter(r -> r.getInt("product_id").equals(lastProductId) && r.getInt("category_id").equals(lastCategoryId)).collect(Collectors.toList());
                    String productTotalNum = String.format("%.2f", productTotalList.stream().collect(Collectors.summarizingDouble(r -> r.getDouble("productNum"))).getSum());
                    String productTotalMoney = String.format("%.2f", productTotalList.stream().collect(Collectors.summarizingDouble(r -> r.getDouble("productPrice"))).getSum());
                    list.add(productTotal.set("productPrice", "合计").set("categoryName", lastRecord.getStr("categoryName")).set("productName", "").set("productNum", productTotalNum).set("productSubtotal", productTotalMoney).getColumns());
                    //每种分类总计
                    if (!categoryId.equals(lastCategoryId)) {
                        List<Record> categoryTotalList = recordList.stream().filter(r -> r.getInt("category_id").equals(lastCategoryId)).collect(Collectors.toList());
                        String categoryTotalNum = String.format("%.2f", categoryTotalList.stream().collect(Collectors.summarizingDouble(r -> r.getDouble("productNum"))).getSum());
                        String categoryTotalMoney = String.format("%.2f", categoryTotalList.stream().collect(Collectors.summarizingDouble(r -> r.getDouble("productPrice"))).getSum());
                        Record categoryTotal = new Record().setColumns(productTotal.getColumns());
                        list.add(categoryTotal.set("productPrice", "总计").set("productName", "").set("categoryName", "").set("productNum", categoryTotalNum).set("productSubtotal", categoryTotalMoney).getColumns());
                    }
                }
            }
            list.add(record.getColumns());
            if (i == recordList.size() - 1) {
                //每种产品总计
                List<Record> productTotalList = recordList.stream().filter(r -> r.getInt("product_id").equals(productId) && r.getInt("category_id").equals(categoryId)).collect(Collectors.toList());
                String productTotalNum = String.format("%.2f", productTotalList.stream().collect(Collectors.summarizingDouble(r -> r.getDouble("productNum"))).getSum());
                String productTotalMoney = String.format("%.2f", productTotalList.stream().collect(Collectors.summarizingDouble(r -> r.getDouble("productPrice"))).getSum());
                Record lastProductTotal = new Record().setColumns(productTotal.getColumns());
                list.add(lastProductTotal.set("productPrice", "合计").set("categoryName", record.getStr("categoryName")).set("productName", "").set("productNum", productTotalNum).set("productSubtotal", productTotalMoney).getColumns());
                //每种分类总计
                List<Record> categoryTotalList = recordList.stream().filter(r -> r.getInt("category_id").equals(categoryId)).collect(Collectors.toList());
                String categoryTotalNum = String.format("%.2f", categoryTotalList.stream().collect(Collectors.summarizingDouble(r -> r.getDouble("productNum"))).getSum());
                String categoryTotalMoney = String.format("%.2f", categoryTotalList.stream().collect(Collectors.summarizingDouble(r -> r.getDouble("productPrice"))).getSum());
                Record lastCategoryTotal = new Record().setColumns(productTotal.getColumns());
                list.add(lastCategoryTotal.set("productPrice", "总计").set("productName", "").set("productNum", categoryTotalNum).set("categoryName", "").set("productSubtotal", categoryTotalMoney).getColumns());
            }
        }
        return list;
    }

    /**
     * 获取商业智能业绩目标完成情况
     *
     * @author wyq
     */
    public List<Record> taskCompleteStatistics(String year, Integer status, Integer deptId, Long userId, Integer isUser) {
        Integer menuId = Db.queryInt(Db.getSql("admin.menu.queryMenuId"), "bi", "achievement", "read");
        List<Record> resultList = new ArrayList<>();
        Kv kv = Kv.by("map", MonthEnum.values()).set("year", year);
        if (isUser == 1){
            if (userId != null){
                //根据数据权限过滤userId
                userId = biTimeUtil.dataFilter(menuId, userId);
                kv.set("userId", userId).set("status", status).set("type", 3).set("objId", userId);
                List<Record>  recordList = Db.find(Db.getSqlPara("bi.base.taskCompleteStatistics", kv));
                if(recordList.size() == 0){
                    return new ArrayList<>();
                }
                resultList.add(new Record().set("name",Db.queryStr("select realname from `72crm_admin_user` where user_id = ?",userId)).set("list",recordList));
            }else {
                List<Record> userList = Db.find("select b.user_id,b.realname from `72crm_crm_achievement` a left join `72crm_admin_user` b on a.obj_id = b.user_id where a.type = 3 and a.status =? and a.year = ?", status, year);
                //根据数据权限过滤userId
                List<Long> userIdList = userList.stream().map(record -> record.getLong("user_id")).collect(Collectors.toList());
                biTimeUtil.dataFilter(menuId, userIdList);
                userList.removeIf(user -> !userIdList.contains(user.getLong("user_id")));
                for (Record record : userList) {
                    Long user = record.getLong("user_id");
                    String name = record.getStr("realname");
                    kv.set("userId", user).set("status", status).set("type", 3).set("objId", user);
                    List<Record>  recordList = Db.find(Db.getSqlPara("bi.base.taskCompleteStatistics", kv));
                    resultList.add(new Record().set("name",name).set("list",recordList));
                }
            }
        }else {
            List<Integer> deptIdList;
            if(deptId != null){
                deptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(deptId, BaseConstant.AUTH_DATA_RECURSION_NUM);
                deptIdList.add(deptId);
            }else {
                deptIdList = Db.query("select dept_id from `72crm_crm_achievement` a left join `72crm_admin_dept` b on a.obj_id = b.dept_id where a.type = 2 and a.status =? and a.year = ?", status, year);
            }
            //根据数据权限过滤deptId
            if (!Objects.equals(BaseUtil.getUserId(), BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole())) {
                List<Integer> authDeptIdList = new ArrayList<>();
                Integer dataType = Db.queryInt(Db.getSql("bi.base.queryDataTypeByUserId"), BaseUtil.getUserId(), menuId);
                if (dataType == null || dataType == 1 || dataType == 2) {
                    deptIdList.clear();
                } else if (dataType != 5) {
                    if (dataType == 3) {
                        authDeptIdList.add(BaseUtil.getUser().getDeptId());
                    } else if (dataType == 4) {
                        authDeptIdList = Aop.get(AdminUserService.class).queryChileDeptIds(BaseUtil.getUser().getDeptId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                        authDeptIdList.add(BaseUtil.getUser().getDeptId());
                    }
                    deptIdList.retainAll(authDeptIdList);
                }
            }
            List<Record> deptList = Db.find(Db.getSqlPara("bi.base.queryDept", Kv.by("status", status).set("deptIds", deptIdList)));
            for (Record record : deptList) {
                Integer dept = record.getInt("dept_id");
                String name = record.getStr("name");
                kv.set("deptId", dept).set("status", status).set("type", 2).set("objId", dept);
                List<Record> recordList = Db.find(Db.getSqlPara("bi.base.taskCompleteStatistics", kv));
                resultList.add(new Record().set("name",name).set("list",recordList));
            }
        }
        return resultList;
    }

    public List<Map<String, Object>> taskCompleteStatisticsExport(String year, Integer status, Integer deptId, Long userId, Integer isUser) {
        List<Record> recordList = taskCompleteStatistics(year, status, deptId, userId, isUser);
        List<Map<String, Object>> list = new ArrayList<>();
        recordList.forEach(record -> {
            String name = record.getStr("name");
            List<Record> taskList = record.get("list");
            BigDecimal quarterMoney = new BigDecimal(0.00);
            BigDecimal quarterAchievement = new BigDecimal(0.00);
            BigDecimal totalMoney = new BigDecimal(0.00);
            BigDecimal totalAchievement = new BigDecimal(0.00);
            for (int i = 0; i < taskList.size(); i++) {
                Record task = taskList.get(i);
                quarterMoney = quarterMoney.add(task.getBigDecimal("money"));
                totalMoney = totalMoney.add(task.getBigDecimal("money"));
                quarterAchievement = quarterAchievement.add(task.getBigDecimal("achievement"));
                totalAchievement = totalAchievement.add(task.getBigDecimal("achievement"));
                task.set("name", name);
                list.add(task.getColumns());
                if (i == 2) {
                    BigDecimal rate = quarterMoney.divide(quarterAchievement, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    Record r = new Record().set("name", "").set("month", "第一季度").set("money", quarterMoney).set("achievement", quarterAchievement).set("rate", rate);
                    list.add(r.getColumns());
                    quarterMoney = new BigDecimal(0.00);
                    quarterAchievement = new BigDecimal(0.00);
                } else if (i == 5) {
                    BigDecimal rate = quarterMoney.divide(quarterAchievement, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    Record r = new Record().set("name", "").set("month", "第二季度").set("money", quarterMoney).set("achievement", quarterAchievement).set("rate", rate);
                    list.add(r.getColumns());
                    quarterMoney = new BigDecimal(0.00);
                    quarterAchievement = new BigDecimal(0.00);
                } else if (i == 8) {
                    BigDecimal rate = quarterMoney.divide(quarterAchievement, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    Record r = new Record().set("name", "").set("month", "第三季度").set("money", quarterMoney).set("achievement", quarterAchievement).set("rate", rate);
                    list.add(r.getColumns());
                    quarterMoney = new BigDecimal(0.00);
                    quarterAchievement = new BigDecimal(0.00);
                } else if (i == 11) {
                    BigDecimal rate = quarterMoney.divide(quarterAchievement, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    Record r = new Record().set("name", "").set("month", "第四季度").set("money", quarterMoney).set("achievement", quarterAchievement).set("rate", rate);
                    list.add(r.getColumns());
                    quarterMoney = new BigDecimal(0.00);
                    quarterAchievement = new BigDecimal(0.00);
                    BigDecimal totalRate = totalMoney.divide(totalAchievement, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    Record total = new Record().set("name", "").set("month", "全年").set("money", totalMoney).set("achievement", totalAchievement).set("rate", totalRate);
                    list.add(total.getColumns());
                }
            }
        });
        return list;
    }
}
