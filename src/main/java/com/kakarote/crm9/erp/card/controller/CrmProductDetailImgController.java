package com.kakarote.crm9.erp.card.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.card.entity.CrmProductDetailImg;
import com.kakarote.crm9.erp.card.service.CrmProductDetailImgService;

/**
 * @author zxy
 * 产品详情图片
 */
public class CrmProductDetailImgController extends Controller {
    @Inject
    private CrmProductDetailImgService service;

    /**
     * @author zxy
     * 添加
     */
    public void save(){
        CrmProductDetailImg img =   JSON.parseObject(getRawData(), CrmProductDetailImg.class);
        renderJson(service.save(img));
    }
    /**
     * @author zxy
     * 查询产品详情 type 1.首图 2.详情图
     */
    public void queryListByType(@Para("productId") Long productId){
        renderJson(service.queryListByType(productId));
    }
    /**
     * @author zxy
     * 删除产品详情
     */
    public void delete(@Para("imgId") Integer imgId){
        renderJson(service.delete(imgId));
    }

}
