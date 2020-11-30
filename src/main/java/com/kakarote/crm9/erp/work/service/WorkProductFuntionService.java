package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.utils.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.entity.WorkProductFunction;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.Date;
import java.util.List;

/**
 * @author zyy
 */
public class WorkProductFuntionService  extends AbstractBasePageList {

    /**
     * 分页查询产品功能
     */
    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth){
        CrmEnum productFunctione = CrmEnum.PRODUCT_FUNCTIONE;
        Kv kv = getKv(basePageRequest, productFunctione, isDataAuth);
        kv.set("_productId",basePageRequest.getJsonObject().getInteger("productId"));
        List<Record> records = Db.find(Db.getSqlPara("work.product.queryFuntionPageList", kv));
        Integer integer = Db.template("work.product.queryFuntionPageListCount", kv).queryInt();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        jsonObject.put("totalRow",integer);
        return R.ok().put("data",jsonObject);
    }

    /**
     * 设置产品功能
     */
    public R setProductFunction(JSONObject jsonObject){
        WorkProductFunction entity = jsonObject.getObject("entity", WorkProductFunction.class);
        boolean temp = false;
        if(entity.getFunctionId() == null){
            entity.setCreateTime(new Date());
            entity.setCreateUserId(BaseUtil.getUserId());
            entity.setUpdateTime(new Date());
            temp = entity.save();
        }else {
            temp = true;
            entity.setUpdateTime(new Date());
            entity.update();
        }
        return temp?R.ok():R.error("设置失败");
    }

    /**
     * 根据ID查询产品功能
     */
    public Record queryById(Integer id){
        return  Db.findFirst("select * from 72crm_work_product_function where function_id=?",id);
    }
    /**
     * 批量删除
     * @return
     */
    public R delProductFuntion(String ids){
        String[] split = ids.split(",");
        for (String s : split) {
            Integer integer = Db.queryInt("select count(1) from 72crm_work_demand_function where function_id=?", s);
            if(integer >0){
                return R.error("产品功能已经对标不能删除");
            }
            Db.delete("DELETE FROM 72crm_work_product_function where function_id =?",s);
            Db.delete("delete from 72crm_work_demand_function where function_id =?",s);
        }
        return R.ok();
    }

    public R queryFuntionByProductId(String productIds) {

        return R.ok();
    }

    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (function_name like '%{search}%' or function_number like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }


}
