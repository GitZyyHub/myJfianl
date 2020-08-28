package com.kakarote.crm9.erp.card.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.card.entity.AdminOfficialImg;
import com.kakarote.crm9.erp.card.service.AdminOfficialImgService;
import com.kakarote.crm9.utils.BaseUtil;

import java.util.List;

/**
 * @author zxy
 * 名片海报和官网设置
 */
public class AdminOfficialImgController extends Controller {
    @Inject
    private AdminOfficialImgService service;
    /**
     * @author zxy
     * 添加
     */
    public void save(){
        String prefix= BaseUtil.getDate();
        renderJson(service.save(getFile("file",prefix),getPara("type"),prefix));
    }
    /**
     * @author zxy
     * 查询官网图片 type 1.官网设置 2.名片海报
     */
    public void queryListByType(@Para("type") Integer type){
        renderJson(service.queryListByType(type));
    }
    /**
     * @author zxy
     * 删除官网图片
     */
    public void delete(@Para("officialImgId") Integer officialImgId){
        renderJson(service.delete(officialImgId));
    }
    /**
     * @author zxy
     * 图片排序
     */
    public void sortImg(){
        List<AdminOfficialImg> imgs = JSON.parseArray(getRawData(), AdminOfficialImg.class);
        renderJson(service.sortImg(imgs));
    }
    /**
     * @author zxy
     * 小程序查询官网图片 type 1.官网设置 2.名片海报
     */
    @Clear
    public void queryMiNiListByType(@Para("type") Integer type){
        renderJson(service.queryListByType(type));
    }
}
