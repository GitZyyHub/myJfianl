package com.kakarote.crm9.erp.work.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.entity.Work;
import com.kakarote.crm9.erp.work.entity.WorkProduct;
import com.kakarote.crm9.erp.work.entity.WorkProductHistory;
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
    @Before(Tx.class)
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
            if((byId.getProductVersion()==null&&entity.getProductVersion()!=null) ||
                    (byId.getProductVersion()!=null&&entity.getProductVersion()==null) ||
                    (!byId.getProductVersion().equals(entity.getProductVersion()))
            ){
                setVersionHistorical(byId.getProductVersion(),entity.getProductVersion(),entity.getVersionDescribe(),entity.getProductId());
            }
            entity.update();
        }
        return temp?R.ok():R.error("更新失败");
    }

    /**
     * 删除产品
     */
    public R delWorkProduct(String ids) {
        Db.find("");
        return R.ok();
    }

    /**
     * 添加产品修改历史记录
     */
    public void setVersionHistorical(String oldVersion,String newVersion,String describe,Integer productId){
        WorkProductHistory workProductHistory = new WorkProductHistory();
        workProductHistory.setOldVersion(oldVersion);
        workProductHistory.setNewVersion(newVersion);
        workProductHistory.setDescribe(describe);
        workProductHistory.setProductId(productId);
        workProductHistory.setCreateTime(new Date());
        workProductHistory.save();
    }

    /**
     * 查询产品版本历史记录
     * @param basePageRequest
     * @return
     */
    public R queryVersionHistorical(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("work.product.queryVersionHistorical", Kv.by("productId", jsonObject.get("productId"))));
        return R.ok().put("data",paginate);
    }

    /**
     * 设置产品状态
     */
    public R setWorkProductStatus(String status,String ids){
        String[] split = ids.split(",");
        for (String s : split) {
            Db.update("update 72crm_work_product set product_status=? where product_id =?",status,s);
        }
        return R.ok();
    }

    /**
     * 根据ID查询产品状态
     */
    public Record queryById(Integer id){
        return Db.findFirst("select p.*,(select realname from 72crm_admin_user where user_id=p.main_user_id) main_user_name," +
                "(select realname from 72crm_admin_user where user_id=p.exploit_user_id) exploit_user_name" +
                " from 72crm_work_product p where product_id=?",id);
    }

    /**
     * 查询产品使用统计
     * @param basePageRequest
     * @return
     */
    public R queryStatistics(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv by = Kv.by("productId", jsonObject.get("productId"));
        if(jsonObject.get("excel") != null){
            by.set("page",basePageRequest.getPage()).set("limit",basePageRequest.getLimit());
        }
        List<Record> records = Db.find(Db.getSqlPara("work.product.queryStatistics",by));
        Integer integer = Db.template("work.product.queryStatisticsCount", by).queryInt();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("list",records);
        jsonObject1.put("totalRow",integer);
        return R.ok().put("data",jsonObject1);
/*        Page<Record> paginate = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit()
                , Db.getSqlPara("work.product.queryStatistics", Kv.by("productId", jsonObject.get("productId"))));*/
        //return R.ok().put("data",paginate1);
    }


    @Override
    public JSONObject appendSqlSearch(String search) {
        return null;
    }



}
