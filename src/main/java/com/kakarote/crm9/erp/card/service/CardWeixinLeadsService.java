package com.kakarote.crm9.erp.card.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.card.entity.CardWeixinLeads;
import com.kakarote.crm9.erp.crm.entity.CrmLeads;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxy
 * 微信线索
 */
public class CardWeixinLeadsService {
    @Inject
    private AdminUserService userService;
    /**
     * @author zxy
     * 微信线索添加
     */
    public R save(JSONObject jsonObject){
        CardWeixinLeads weixinLeads = jsonObject.toJavaObject(CardWeixinLeads.class);
        //weixinLeads.setCreateUserId(BaseUtil.getUserId());
        weixinLeads.setCreateTime(DateUtil.date());
        if (weixinLeads.getOwnerUserId() == null){
            weixinLeads.setOwnerUserId(weixinLeads.getCreateUserId());
        }
        return weixinLeads.save()? R.ok(): R.error();
    }
    /**
     * @author zxy
     * 微信线索列表查询
     */
    public R queryPage(BasePageRequest<CardWeixinLeads> base){
        Integer type = base.getJsonObject().getInteger("type");
        List<Long> userIds = new ArrayList<>();
        if (type.equals(1)){
            userIds.add(BaseUtil.getUserId());
        }else if (type.equals(2)){
            userIds.addAll(userService.queryUserByParentUser(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
            if (userIds.size() == 0){
                return R.ok().put("data", new Page<>());
            }
        }else if (type.equals(0)){
            userIds.add(BaseUtil.getUserId());
            userIds.addAll(userService.queryUserByParentUser(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM));
            Page<Record> page = Db.paginate(base.getPage(),base.getLimit(),
                    Db.getSqlPara("card.weixinLeads.queryPage",
                            Kv.by("search",base.getJsonObject().get("search")).set("userIds",userIds).set("isAdmin", BaseUtil.isAdmin())));
            return R.ok().put("data",page);
        }else {
            Page<Record> page = Db.paginate(base.getPage(),base.getLimit(),
                    Db.getSqlPara("card.weixinLeads.queryPage",
                            Kv.by("search",base.getJsonObject().get("search"))));
            return R.ok().put("data",page);
        }
        Page<Record> page = Db.paginate(base.getPage(),base.getLimit(),
                Db.getSqlPara("card.weixinLeads.queryPage",
                        Kv.by("search",base.getJsonObject().get("search")).set("userIds",userIds)));
        return R.ok().put("data",page);
    }
    /**
     * @author zxy
     * 微信线索列表删除
     */
    public R deleteByIds(List<Integer> ids){
        SqlPara sqlPara = Db.getSqlPara("card.weixinLeads.deleteByIds", Kv.by("ids",ids));
        Db.delete(sqlPara.getSql(),sqlPara.getPara());
        return R.ok();
    }
    /**
     * @author zxy
     * 转化为线索
     */
    public R changeLeads(List<Integer> ids){
        List<CardWeixinLeads> weixinLeadsList = CardWeixinLeads.dao.find(Db.getSqlPara("card.weixinLeads.queryByLeadsIds", Kv.by("ids",ids)));
        CrmLeads leads = null;
        for (CardWeixinLeads weixinLeads: weixinLeadsList) {
            leads = new CrmLeads();
            leads.setCreateTime(DateUtil.date());
            leads.setOwnerUserId(weixinLeads.getOwnerUserId());
            leads.setLeadsName(weixinLeads.getWeixinName());
            leads.setCreateUserId(BaseUtil.getUserId());
            leads.setMobile(weixinLeads.getMobile());
            leads.setAddress(weixinLeads.getCity());
            leads.setBatchId(IdUtil.simpleUUID());
            leads.save();
        }
        SqlPara sqlPara = Db.getSqlPara("card.weixinLeads.changeLeads", Kv.by("ids",ids));
       Integer a = Db.update(sqlPara.getSql(),sqlPara.getPara());
       return a>0? R.ok(): R.error();

    }
    /**
     * @author zxy
     * 根据ids获取微信线索
     */
    public List<Record> queryListByIds(BasePageRequest<CardWeixinLeads> base){
        List<Integer> ids = null;
        if( base.getJsonObject().get("ids")!= null){
            String [] idss =  base.getJsonObject().getString("ids").split(",");
            ids = new ArrayList<>();
            for (String id: idss ) {
                ids.add(Integer.valueOf(id));
            }
        }
        List<Record> list = Db.find(Db.getSqlPara("card.weixinLeads.queryListByIds",
                Kv.by("ids",ids).set("search",base.getJsonObject().get("search"))));
        return list;
    }
    /**
     * @author zxy
     * 微信线索列表查询
     */
    public R queryMiNiPage(BasePageRequest<CardWeixinLeads> base){
        List<Long> userIds = new ArrayList<>();
        userIds.add(base.getData().getOwnerUserId());
        Page<Record> page = Db.paginate(base.getPage(),base.getLimit(),
                Db.getSqlPara("card.weixinLeads.queryPage",
                        Kv.by("search",base.getJsonObject().get("search"))
                               .set("userIds",userIds)));
        return R.ok().put("data",page);
    }
}
