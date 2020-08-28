package com.kakarote.crm9.erp.card.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.card.entity.CrmProductDetailImg;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * @author zxy
 * 产品详情图片
 */
public class CrmProductDetailImgService {
    /**
     * @author zxy
     * 产品详情添加
     */
    public R save(CrmProductDetailImg img){
        CrmProductDetailImg   detailImg = CrmProductDetailImg.dao.findFirst("select * from 72crm_crm_product_detail_img where  product_id = ?",img.getProductId());
            if (detailImg != null) {
               img.setImgId(detailImg.getImgId());
            }
        Boolean flag ;
        if (img.getImgId() == null){
            flag = img.save();
        }else {
            flag = img.update();
        }
        return flag? R.ok().put("data",img): R.error();
    }
    /**
     * @author zxy
     * 产品详情查询
     */
    public R queryListByType(Long productId){
        CrmProductDetailImg   detailImg = CrmProductDetailImg.dao.findFirst("select * from 72crm_crm_product_detail_img where  product_id = ?",productId);
        if (detailImg != null) {
            if (detailImg.getMainFileIds() != null) {
                SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", detailImg.getMainFileIds()).set("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()));
                detailImg.put("mainFileList", updateUrl(sqlPara));
            } else {
                detailImg.put("mainFileList", null);
            }

            if (detailImg.getDetailFileIds() != null) {
                SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", detailImg.getDetailFileIds()).set("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()));
                List<Record> list = updateUrl(sqlPara);
                detailImg.put("detailFileList", list);
                detailImg.put("detailFile",list);
            } else {
                detailImg.put("detailFileList", null);
                detailImg.put("detailFile", null);
            }
        }else {
            detailImg = new CrmProductDetailImg();
            detailImg.put("mainFileList", null);
            detailImg.put("detailFileList", null);
            detailImg.put("detailFile", null);
        }
        return R.ok().put("data",detailImg);
    }
    private List<Record> updateUrl(SqlPara sqlPara){
        List<Record> list = Db.find(sqlPara);
        for (Record record: list) {
            if (record != null) {
                record.set("url", record.getStr("url").replace("\\", "/"));
            }
        }
        return list;
    }
    /**
     * @author zxy
     * 产品详情删除
     */
    public R delete(Integer id){
       return CrmProductDetailImg.dao.deleteById(id)? R.ok(): R.error();
    }

}
