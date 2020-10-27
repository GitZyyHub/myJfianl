package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.entity.WorkProduct;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.ToolUtils;

import java.util.Date;
import java.util.List;

/**
 * @author zyy
 */
public class WorkProductService extends AbstractBasePageList {


    /**
     * 分页查询产品
     */
    public R queryPageList(BasePageRequest basePageRequest,boolean isDataAuth){
        CrmEnum crmEnum = CrmEnum.PRODUCT;
        Kv kv = getKv(basePageRequest, crmEnum, isDataAuth);
        List<Record> records = Db.find(Db.getSqlPara("work.product.queryPageList", kv));
        Integer integer = Db.template("work.product.queryPageListCount", kv).queryInt();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",records);
        jsonObject.put("totalRow",integer);
        return R.ok().put("data",jsonObject);
    }


    /**
     * 设置产品
     * @param jsonObject
     * @return
     */
    public R setWorkProduct(JSONObject jsonObject) {
        WorkProduct entity = jsonObject.getObject("entity", WorkProduct.class);
        boolean temp = false;
        if(entity.getProductId() == null){
            if(StrUtil.isEmpty(entity.getProductNumber())){
                AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 23");
                String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
                entity.setProductNumber(result);
            }
            entity.setCreateTime(new Date());
            entity.setCreateUserId(BaseUtil.getUserId());
            entity.setUpdateTime(new Date());
            temp = entity.save();
        }else {
            temp = true;
            WorkProduct byId = entity.findById(entity.getProductId());
            if(byId == null){
                return R.error("产品不存在");
            }
            entity.update();
        }
        return temp?R.ok():R.error("更新失败");
    }

    /**
     * 设置产品状态
     */
    public R setWorkProductStatus(Integer status,String ids){
        Db.update("update 72crm_work_product set product_status="+status+" where product_id in ("+ids+")");
        return R.ok();
    }

    /**
     * 根据ID查询产品状态
     */
    public Record queryById(Integer id){
        return Db.findFirst("select * from 72crm_work_product where product_id=?",id);
    }

    /**
     * 查询产品使用统计
     * @param basePageRequest
     * @return
     */
    public R queryStatistics(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        if(jsonObject.get("excel") != null){
            List<Record> records = Db.find(Db.getSqlPara("work.product.queryStatistics", Kv.by("productId", jsonObject.get("productId"))));
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("list",records);
            return R.ok().put("data",jsonObject1);
        }
        Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit()
                , Db.getSqlPara("work.product.queryStatistics", Kv.by("productId", jsonObject.get("productId"))));
        return R.ok().put("data",paginate);
    }

    @Override
    public JSONObject appendSqlSearch(String search) {
        return null;
    }

}
