package com.kakarote.crm9.erp.card.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.card.entity.CardWeixinLeadsUser;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * @author zxy
 * 名片夹
 */
@Clear
public class CardWeixinLeadsUserService {
    /**
     * @author zxy
     * 添加名片夹
     */
    public R save(JSONObject jsonObject){
        CardWeixinLeadsUser leadsUser = jsonObject.toJavaObject(CardWeixinLeadsUser.class);
        leadsUser.setCreateTime(DateUtil.date());
        return leadsUser.save()? R.ok(): R.error();
    }
    /**
     * @author zxy
     * 查询名片夹，根据微信线索查询我扫的员工
     */
    public R queryPage(BasePageRequest<CardWeixinLeadsUser> base){
        Page<Record> page =
                Db.paginate(base.getPage(),base.getLimit(),
                        Db.getSqlPara("card.weixinLeadsUser.queryPage",
                                Kv.by("weixinLeadsId",base.getData().getWeixinLeadsId())
                                        .set("relevanceUserId",base.getData().getRelevanceUserId())));
        List<Record> list = page.getList();
        for (Record record: list) {
            if (StrUtil.isNotEmpty(record.get("img"))) {
                AdminFile adminFile = AdminFile.dao.findFirst(Db.getSqlPara("admin.file.queryByPath",
                        Kv.by("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()).set("filePath",record.get("img"))));
                if (adminFile != null) {
                    record.set("img", adminFile.getFilePath().replace("\\", "/"));
                }
            }
        }
        return R.ok().put("data",page);
    }
    /**
     * @author zxy
     * 删除名片夹
     */
    public R deleteById(Integer id){
        return CardWeixinLeadsUser.dao.deleteById(id)? R.ok(): R.error();
    }
}
