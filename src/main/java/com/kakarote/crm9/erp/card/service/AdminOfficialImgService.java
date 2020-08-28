package com.kakarote.crm9.erp.card.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.erp.card.entity.AdminOfficialImg;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * @author zxy
 * 名片海报和官网设置
 */
public class AdminOfficialImgService {
    /**
     * @author zxy
     * 名片海报和官网设置添加
     */
    public R save(UploadFile file, String type, String prefix){
        AdminOfficialImg officialImg = null;
        if (Integer.valueOf(type).equals(2)){
            officialImg = AdminOfficialImg.dao.findFirst("select * from 72crm_admin_official_img where type = 2 ");
        }
        if (officialImg == null) {
            officialImg = new AdminOfficialImg();
        }
        officialImg.setCreateTime(DateUtil.date());
        officialImg.setPath(file.getFile().getAbsolutePath());
        officialImg.setFilePath( BaseUtil.getIpAddress() + "/" + prefix + "/" +file.getFileName());
        officialImg.setSize(file.getFile().length());
        officialImg.setName(file.getFileName());
        officialImg.setType(Integer.valueOf(type));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filePath",officialImg.getFilePath());
        jsonObject.put("name",officialImg.getName());
        Boolean flag ;
        if (officialImg.getOfficialImgId() == null){
            if (type.equals(1)){

                Integer tactic = Db.queryInt(Db.getSql("admin.offical.queryImgTactic"),1);
                if (tactic == null){
                    tactic =0;
                }
                officialImg.setTactic(tactic + 1);
            }
            flag = officialImg.save();
        }else {
            flag = officialImg.update();
        }
        jsonObject.put("officialImgId",officialImg.getOfficialImgId());
        return flag? R.ok().put("data",jsonObject): R.error() ;
    }
    /**
     * @author zxy
     * 查询官网图片 type 1.官网设置 2.名片海报
     */
    public R queryListByType(Integer type){
        List<AdminOfficialImg> list =
                AdminOfficialImg.dao.find(Db.getSqlPara("admin.offical.queryListByType",
                        Kv.by("type",type)));
        return R.ok().put("data",list);
    }
    /**
     * @author zxy
     * 删除官网图片
     */
    public R delete(Integer id){
        return AdminOfficialImg.dao.deleteById(id)? R.ok(): R.error();
    }
    /**
     * @author zxy
     * 图片排序
     */
    public R sortImg(List<AdminOfficialImg> imgs){
        imgs.forEach(img->{
            img.update();
        });
        return R.ok();
    }
}
