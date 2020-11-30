package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.common.constant.WorkRecordEnum;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.common.WorkPermissions;
import com.kakarote.crm9.erp.work.common.WorkRendAuthEnum;
import com.kakarote.crm9.erp.work.common.WorkUserEnum;
import com.kakarote.crm9.erp.work.entity.*;
import com.kakarote.crm9.utils.AbstractBasePageList;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zyy
 */
public class WorkDemandService extends AbstractBasePageList {

    @Inject
    WorkHistoricalRecordService workHistoricalRecordService;
    @Inject
    AdminFieldService adminFieldService;

    /**
     * 分页查询需求
     */
    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        basePageRequest.setJsonObject(jsonObject);
        CrmEnum demand = CrmEnum.DEMAND;
        Kv kv = getKv(basePageRequest, demand, isDataAuth);
        kv.set("_workId", jsonObject.getInteger("workId"));
        //queryDataAuth(WorkUserEnum.DEMAND,kv,CrmEnum.DEMAND,null, WorkRendAuthEnum.DEMAND,null);
        /*if(kv==null){
            return R.error("无权限");
        }*/
        List<Record> records = Db.find(Db.getSqlPara("work.demand.queryPageList", kv));
        Integer demand_id ;
        for (Record record : records) {
            demand_id = record.getInt("demand_id");
            setRecord(record, demand_id);
            demand_id=null;
        }
        Integer integer = Db.template("work.demand.queryPageListCount", kv).queryInt();
        JSONObject resultJsonObject = new JSONObject();
        resultJsonObject.put("list", records);
        resultJsonObject.put("totalRow", integer);
        return R.ok().put("data", resultJsonObject);
    }

    /**
     * 根据id查询
     */
    public Record queryById(Integer id) {
        Record first = Db.findFirst(Db.getSql("work.demand.queryById"), id);
        if (first == null) return null;
        setRecord(first, first.getInt("demand_id"));
        return first;
    }

    public void setRecord(Record record, Integer demand_id) {
        //查询关联的产品
        List<Record> products = Db.find(Db.getSql("work.demand.queryAssociatedProduct"), demand_id);
        
        record.set("product_name", products.stream().map(product -> StrUtil.isEmpty(product.getStr("product_name"))?null:product.getStr("product_name") + ",").collect(Collectors.toList()));
        record.set("product_version", products.stream().map(product -> StrUtil.isEmpty(product.getStr("product_version"))?null:product.getStr("product_version")+",").collect(Collectors.toList()));
        record.set("product_number", products.stream().map(product -> StrUtil.isEmpty(product.getStr("product_number"))?null:product.getStr("product_number")+",").collect(Collectors.toList()));
        try {
            record.set("product_percentage",products.stream().map(product -> StrUtil.isEmpty(product.getStr("product_percentage"))?null:product.getStr("product_percentage")+",").collect(Collectors.toList()));
        } catch (NullPointerException e) {}
        try {
            record.set("product_describe",products.stream().map(product -> StrUtil.isEmpty(product.getStr("product_describe"))?null:product.getStr("product_describe")+",").collect(Collectors.toList()));
        } catch (NullPointerException e) {}

        //查询关联的产品功能
        List<Record> functions = Db.find(Db.getSql("work.demand.queryAssociatedFunction"), demand_id);
        record.set("function_name", functions.stream().map(function -> StrUtil.isEmpty(function.getStr("function_name"))?null:function.getStr("function_name")+",").collect(Collectors.toList()));
        record.set("function_number", functions.stream().map(function ->  StrUtil.isEmpty(function.getStr("function_number"))?null:function.getStr("function_number")+",").collect(Collectors.toList()));
    }


    /**
     * 设置需求
     *
     * @param jsonObject
     * @return
     */
    @Before(Tx.class)
    public R setDemand(JSONObject jsonObject) {
        JSONObject data = jsonObject.getJSONObject("entity");
        WorkDemand entity = jsonObject.getObject("entity", WorkDemand.class);
        boolean temp = false;
        if (entity.getDemandId() == null) {
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setCreateUserId(BaseUtil.getUserId());
            temp = entity.save();
            workHistoricalRecordService.add(entity.getWorkId(),WorkRecordEnum.DEMAND, BehaviorEnum.SAVE,entity.getDemandId(), null, null,entity.getDemandId());
        } else {
            temp = true;
            Record first = Db.findFirst(Db.getSql("work.demand.queryDemandById"), entity.getDemandId());
            if (first == null) {
                return R.error("需求不存在");
            }
            entity.setUpdateTime(new Date());
            entity.update();
            Record update = Db.findFirst(Db.getSql("work.demand.queryDemandById"), entity.getDemandId());
            //添加变更记录
            workHistoricalRecordService.add(entity.getWorkId(),WorkRecordEnum.DEMAND, BehaviorEnum.UPDATE, entity.getDemandId(), update,first, entity.getDemandId());
        }
        WorkDemand byId = Aop.get(WorkDemand.class).dao().findById(entity.getDemandId());
        HashSet<Long> set = new HashSet<>();
        set.add(byId.getCreateUserId());
        WorkService.setWorkRelation(set,entity.getDemandId(), WorkUserEnum.DEMAND.getType(),entity.getWorkId());
        return temp ? R.ok() : R.error("更新失败");
    }

    /**
     * 设置需求完成进度
     */
    public R setDemandStatus(String demand, String status) {
        Db.update("update set 72crm_work_demand demand_status=? where demand_id=?",demand,status);
        return R.ok();
    }

    /**
     * 查询需求对标的产品
     */
    public R queryDemandProduct(Integer demandId) {
        return R.ok().put("data",Db.find(Db.getSqlPara("work.demand.queryDemandProduct", Kv.by("demandId", demandId))));
    }

    /**
     * 查询需求对标的产品功能
     */
    public R queryDemandFunction(Integer demandId,String productIds) {
        List<Integer> functionIds = Db.query("select function_id from 72crm_work_demand_function where demand_id=?", demandId);
        if(StrUtil.isEmpty(productIds)){
            if(functionIds == null||functionIds.size()==0){
                return R.ok();
            }
        }

        List<String> ids = new ArrayList<>();
        if(StrUtil.isEmpty(productIds)) {
            ids = Db.template("work.demand.groupProductIdByFunction", Kv.by("functionIds", functionIds)).query();
        }else {
            ids = Arrays.asList(productIds.split(","));
        }
        /*List<String> ids = Db.template("work.demand.groupProductIdByFunction", Kv.by("functionIds", functionIds)).query();;
        if(!StrUtil.isEmpty(productIds)) {
            ids.addAll(Arrays.asList(productIds.split(",")));
        }
        HashSet<String> strings = new HashSet<>(ids);*/
        List<Record> records = null;
        if(!StrUtil.isEmpty(productIds)){
            records = Db.find(Db.getSqlPara("work.demand.queryDemandFunction", Kv.by("productIds", ids).set("functionIds", functionIds)));
            records.addAll(Db.find(Db.getSqlPara("work.demand.queryDemandFunction", Kv.by("productIds", ids).set("functionIdsNot", functionIds))));
        }
        return R.ok().put("data",records);
    }

    /**
     * 设置关联产品列表
     */
    @Before(Tx.class)
    public R setDemandProduct(JSONObject jsonObject) {
        Integer demandId = jsonObject.getInteger("demandId");
        Db.delete("delete from 72crm_work_demand_product where demand_id=?",demandId);
        Db.delete("delete from 72crm_work_demand_function where demand_id=?",demandId);
        String functionIds = jsonObject.getString("functionIds");
        List<Map<String,Object>> products = (List<Map<String,Object>>)jsonObject.get("products");
        boolean temp = false;
        if (StrUtil.isEmpty(demandId + "") || CollectionUtils.isEmpty(products) || StrUtil.isEmpty(functionIds)) {
            return R.error("数据不完整");
        }else {

            for (Map<String, Object> product : products) {
                WorkDemandProduct workDemandProduct = new WorkDemandProduct();
                workDemandProduct.setDemandId(demandId);
                workDemandProduct.setProductId( (int)product.get("productId"));
                workDemandProduct.setProductDescribe((String) product.get("productDescribe"));
                workDemandProduct.setProductPercentage((String) product.get("productPercentage"));
                temp = workDemandProduct.save();
            }

            String[] functionId = functionIds.split(",");
            for (String s : functionId) {
                WorkDemandFunction workDemandFunction = new WorkDemandFunction();
                workDemandFunction.setDemandId(demandId);
                workDemandFunction.setFunctionId(Integer.parseInt(s));
                temp = workDemandFunction.save();
            }
            Db.update("update 72crm_work_demand set is_associated=1 where demand_id =?",demandId);
        }

        return temp ? R.ok() : R.error("关联失败");
    }

    @Before(Tx.class)
    public R excelImport(UploadFile file,String workId) {
        if (file.getFile() == null || !file.getFile().exists()) return R.ok();

        //错误集合
        ArrayList<Object> errList = new ArrayList<>();
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(file.getFile().getAbsolutePath(), 0, (int sheetIndex, int rowIndex, List<Object> rowList) ->{

            if(rowIndex>1){

                num.getAndSet(num.get() + 1);
                WorkDemand workDemand = new WorkDemand();
                workDemand.setWorkId(Integer.parseInt(workId));
                if(StrUtil.isEmptyIfStr(rowList.get(0))){
                    rowList.add(0,"子系统不能为空");
                    errList.add(rowList);
                    return;
                }
                workDemand.setSubsystem(rowList.get(0).toString().trim());
                if(StrUtil.isEmptyIfStr(rowList.get(1))){
                    rowList.add(0,"子模块不能为空");
                    errList.add(rowList);
                    return;
                }
                workDemand.setChildModule(rowList.get(1).toString().trim());
                if(StrUtil.isEmptyIfStr(rowList.get(2))){
                    rowList.add(0,"一级功能不能为空");
                    errList.add(rowList);
                    return;
                }
                workDemand.setFunction1(rowList.get(2).toString().trim());
                workDemand.setFunction2(rowList.get(3).toString().trim());
                if(StrUtil.isEmptyIfStr(rowList.get(4))){
                    rowList.add(0,"需求描述不能为空");
                    errList.add(rowList);
                    return;
                }
                workDemand.setDemandDescribe(rowList.get(4).toString().trim());
                workDemand.setDemandStatus(rowList.get(5).toString().trim());
                workDemand.setDemandVersion(rowList.get(6).toString().trim());
                workDemand.setPlan(rowList.get(7).toString().trim());
                workDemand.setDemandNumber(rowList.get(8).toString().trim());

                if(StrUtil.isEmptyIfStr(rowList.get(11))){
                    rowList.add(0,"需求描述不能为空");
                    errList.add(rowList);
                    return;
                }
                workDemand.setDemandType(rowList.get(11).toString().trim());

                workDemand.save();
            }
        });
        R result = R.ok().put("totalSize",num.get()).put("errSize",0);
        if(errList.size()>0){
            BigExcelWriter writer=null;
            try {
                String token= IdUtil.simpleUUID();
                writer= ExcelUtil.getBigWriter(file.getUploadPath()+"/"+token);


                for (int i = 0; i < 7; i++) {
                    writer.setColumnWidth(i,20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);

                List<Record> fields = adminFieldService.getFields(CrmEnum.DEMAND.getType(), null);

                writer.writeHeadRow(Arrays.asList("错误信息","手机号(*)","登录密码(*)","姓名(*)","性别","邮箱","岗位"));
                writer.write(errList);
                result.put("errSize",errList.size()).put("token",token);
            }finally {
                if(writer!=null){
                    writer.close();
                }
            }
        }
        return result;

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public R delDemand(String ids) {
        List<String> strings = Arrays.asList(ids.split(","));
        for (String string : strings) {
//            if(!WorkPermissions.isPermissions(CrmEnum.DEMAND.getTableName(),CrmEnum.DEMAND.getTableId(),string)) return R.error("无权操作");
        }
        String substring = strings.toString();
        substring = substring.substring(1, substring.length() - 1);
        Db.delete("delete from 72crm_work_demand where demand_id in(" + substring + ")");
        Db.delete("delete from 72crm_work_demand_product where demand_id in(" + substring + ")");
        return R.ok();
    }




    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (subsystem like '%{search}%' or child_module like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }



}
