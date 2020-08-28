package com.kakarote.crm9.erp.card.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.card.entity.CardWeixinBrowse;
import com.kakarote.crm9.utils.R;

/**
 * @author zxy
 * 微信浏览名片表
 */
public class CardWeixinBrowseService {
    /**
     * @author zxy
     * 添加微信浏览
     */
    public R save(JSONObject jsonObject){
        CardWeixinBrowse weixinBrowse = jsonObject.toJavaObject(CardWeixinBrowse.class);
        CardWeixinBrowse browse = CardWeixinBrowse.dao.findFirst(Db.getSql("card.weixinBrowse.queryByWeixinLeadsId"),
                weixinBrowse.getWeixinLeadsId(),weixinBrowse.getUserId());
        if (browse != null){
            browse.setNum(browse.getNum() + 1 );
            browse.setUpdateTime(DateUtil.date());
            return browse.update()? R.ok(): R.error();
        }
        weixinBrowse.setUpdateTime(DateUtil.date());
        weixinBrowse.setNum(0);
        weixinBrowse.setCreateTime(DateUtil.date());
        return weixinBrowse.save()? R.ok(): R.error();
    }
    /**
     * @author zxy
     * 根据员工id分页查询微信浏览人
     */
    public R queryPage(BasePageRequest<CardWeixinBrowse> base){
        AdminUser user = AdminUser.dao.findById(base.getData().getUserId());
        Page<Record> page = Db.paginate(base.getPage(),base.getLimit(),
                Db.getSqlPara("card.weixinBrowse.queryList",
                        Kv.by("userId",base.getData().getUserId())
                .set("weixinName",base.getJsonObject().get("search"))));
        return R.ok().put("data",page).put("count",page.getList().size());
    }
    /**
     * @author zxy
     * 根据员工id分页查询微信浏览人个数
     */
    public R queryCount(Long id){
        AdminUser user = AdminUser.dao.findById(id);
       Integer count = Db.queryInt("select count(1)  from 72crm_card_weixin_browse where user_id = ? ",id);
        return R.ok().put("data",count);
    }
}
