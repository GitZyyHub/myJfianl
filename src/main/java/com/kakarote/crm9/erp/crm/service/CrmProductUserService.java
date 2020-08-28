package com.kakarote.crm9.erp.crm.service;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.crm.entity.CrmProduct;
import com.kakarote.crm9.erp.crm.entity.CrmProductUser;
import com.kakarote.crm9.utils.R;

import java.util.List;


public class CrmProductUserService {
    /**
     * 设置员工关联的产品
     */
    public R save(JSONObject jsonObject){
        CrmProductUser productUser = jsonObject.toJavaObject(CrmProductUser.class);
        CrmProductUser crmProductUser = CrmProductUser.dao.findFirst(Db.getSqlPara("crm.product.queryByUserId", Kv.by("userId",productUser.getUserId())));
        if (crmProductUser != null){
            productUser.setProductUserId(crmProductUser.getProductUserId());
        }
        if (productUser.getProductUserId() == null){
           return productUser.save()? R.ok(): R.error();
        }else {
          return   productUser.update()? R.ok(): R.error();
        }
    }
    /**
     * 查询员工关联产品
     */
    public R queryByUserId(Long userId){
        CrmProductUser crmProductUser = CrmProductUser.dao.findFirst(Db.getSqlPara("crm.product.queryByUserId", Kv.by("userId",userId)));
        if (crmProductUser != null && StrUtil.isNotEmpty(crmProductUser.getProductIds())){
            List<CrmProduct> crmProductList = CrmProduct.dao.find(Db.getSqlPara("crm.product.getProductByIds", Kv.by("ids",crmProductUser.getProductIds())));
            crmProductUser.put("crmProductList",crmProductList);
        }
        return R.ok().put("data",crmProductUser);
    }
}
