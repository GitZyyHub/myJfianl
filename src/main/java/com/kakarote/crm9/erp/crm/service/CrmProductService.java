package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.card.entity.CrmProductDetailImg;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmProduct;
import com.kakarote.crm9.erp.crm.entity.CrmProductData;
import com.kakarote.crm9.erp.crm.entity.CrmProductUser;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.ParamsUtil;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CrmProductService extends AbstractBasePageList {

    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmProductCategoryService crmProductCategoryService;

    @Inject
    private CrmRecordService crmRecordService;

    @Inject
    private ActionRecordUtil actionRecordUtil;


    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        Kv kv = getKv(basePageRequest, CrmEnum.CRM_PRODUCT, isDataAuth);
        if (kv.containsKey("error")){
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.product.queryProductPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.product.queryProductPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        resultJsonObject.put("list", recordPage);
        Integer count = Db.template("crm.product.queryProductPageListCount", kv).queryInt();
        resultJsonObject.put("totalRow", count);
        if (recordPage.size() > 0) {
            List<Integer> receivablesIds = recordPage.stream().map(record -> record.getInt("receivables_id")).collect(Collectors.toList());
            Record record = Db.findFirst("SELECT IFNULL(SUM(money),0) AS receivablesMoney FROM 72crm_crm_receivables AS a WHERE a.check_status = '1' AND a.receivables_id IN (" + StrUtil.join(",", receivablesIds) + ")");
            resultJsonObject.put("money", record);
        }
        return R.ok().put("data", resultJsonObject);
    }


    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (name like '%{search}%')";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }

    @Override
    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject jsonObject = super.appendOtherSql(isDataAuth, kv, crmEnum);
        String conditions = " and status != 3 ";
        jsonObject.put("sql", jsonObject.getString("sql") + conditions);
        return jsonObject;
    }

    /**
     * 分页条件查询产品
     */
    public Page<CrmProduct> queryPage(BasePageRequest<CrmProduct> basePageRequest) {
        CrmProductUser crmProductUser = CrmProductUser.dao.findFirst(Db.getSqlPara("crm.product.queryByUserId", Kv.by("userId",BaseUtil.getUserId())));
        String ids = null;
        if (crmProductUser != null && StrUtil.isNotEmpty(crmProductUser.getProductIds())){
            ids = crmProductUser.getProductIds();
        }else {
            return new Page<>();
        }
        Page<CrmProduct> page = CrmProduct.dao.paginate(basePageRequest.getPage(), basePageRequest.getLimit(),
                Db.getSqlPara("crm.product.getProductPageList"
                        ,Kv.by("categoryId",basePageRequest.getJsonObject().get("categoryId"))
                                .set("search",basePageRequest.getJsonObject().get("search")).set("ids",ids)));
        List<CrmProduct> list = page.getList();
        CrmProductDetailImg detailImg;
        for (CrmProduct crmProduct: list) {
            detailImg = CrmProductDetailImg.dao.findFirst("select * from 72crm_crm_product_detail_img where  product_id = ?",crmProduct.getProductId());
            if (detailImg != null && detailImg.getMainFileIds() != null) {
                SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", detailImg.getMainFileIds()).set("path", BaseConstant.UPLOAD_PATH).set("address",BaseUtil.getIpAddress()));
                crmProduct.put("mainFileList",  updateUrl(sqlPara));
            }else {
                crmProduct.put("mainFileList",  null);
            }
        }
        return page;
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
     * 微信小程序产品详情
     */
    public R queryWeiXinById(Integer id){
        CrmProduct crmProduct = CrmProduct.dao.findFirst(Db.getSql("crm.product.queryWeiXinById"),id);
        CrmProductDetailImg detailImg = CrmProductDetailImg.dao.findFirst("select * from 72crm_crm_product_detail_img where  product_id = ?",crmProduct.getProductId());
        if (detailImg != null) {
            if (detailImg.getMainFileIds() != null) {
                SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", detailImg.getMainFileIds()).set("path",  BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()));
                crmProduct.put("mainFileList", updateUrl(sqlPara));
            } else {
                crmProduct.put("mainFileList", null);
            }

            if (detailImg.getDetailFileIds() != null) {
                SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", detailImg.getDetailFileIds()).set("path",  BaseConstant.UPLOAD_PATH).set("address",BaseUtil.getIpAddress()));
                List<Record> list = updateUrl(sqlPara);
                crmProduct.put("detailFileList", list);
                crmProduct.put("detailFile",list);
            } else {
                crmProduct.put("detailFileList", null);
                crmProduct.put("detailFile", null);
            }
        }else {
            crmProduct.put("mainFileList", null);
            crmProduct.put("detailFileList", null);
            crmProduct.put("detailFile", null);
        }
        return R.ok().put("data",crmProduct);
    }
    /**
     * 添加或修改产品
     *
     * @param jsonObject
     */
    @Before(Tx.class)
    public R saveAndUpdate(JSONObject jsonObject) {
        CrmProduct crmProduct = jsonObject.getObject("entity", CrmProduct.class);
        CrmProductDetailImg img = jsonObject.getObject("entity", CrmProductDetailImg.class);
        if (ParamsUtil.isOutMoney(crmProduct.getPrice())){
            return R.outMaxMoney();
        }
        String batchId = StrUtil.isNotEmpty(crmProduct.getBatchId()) ? crmProduct.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(jsonObject.getJSONArray("field"), Kv.by("batchId", batchId).set("dataTableName", "72crm_crm_product_data"));
        save(jsonObject.getJSONArray("field"), batchId);
        boolean flag;
        if (crmProduct.getProductId() == null) {
            crmProduct.setStatus(1);
            crmProduct.setCreateUserId(BaseUtil.getUser().getUserId());
            crmProduct.setCreateTime(DateUtil.date());
            crmProduct.setUpdateTime(DateUtil.date());
            crmProduct.setOwnerUserId(BaseUtil.getUser().getUserId());
            crmProduct.setBatchId(batchId);
            flag = crmProduct.save();
            actionRecordUtil.addRecord(crmProduct.getProductId(), CrmEnum.CRM_PRODUCT);
        } else {
            CrmProduct oldCrmProduct = new CrmProduct().dao().findById(crmProduct.getProductId());
            actionRecordUtil.updateRecord(oldCrmProduct, crmProduct, CrmEnum.CRM_PRODUCT);
            crmProduct.setUpdateTime(DateUtil.date());
            flag = crmProduct.update();
        }
        CrmProductDetailImg   detailImg = CrmProductDetailImg.dao.findFirst("select * from 72crm_crm_product_detail_img where  product_id = ?",crmProduct.getProductId());
        if (detailImg != null){
            img.setImgId(detailImg.getImgId());
            img.update();
        }else {
            img.setProductId(crmProduct.getProductId());
            img.save();
        }
        return flag ? R.ok() : R.error();
    }

    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_product_data", "batch_id", batchId);
        List<CrmProductData> Fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            CrmProductData fieldv = TypeUtils.castToJavaBean(obj, CrmProductData.class);
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            Fieldvs.add(fieldv);
        });
        Db.batchSave(Fieldvs, 100);
        return true;
    }

    /**
     * 根据id查询产品
     */
    public Record queryById(Integer id) {
        Record crmProduct = Db.findFirst(Db.getSql("crm.product.queryById"),id);
        List<Record> recordList = Db.find("select name,value from `72crm_crm_product_data` where batch_id = ?", crmProduct.getStr("batch_id"));
        recordList.forEach(field->crmProduct.set(field.getStr("name"),field.getStr("value")));
        CrmProductDetailImg   detailImg = CrmProductDetailImg.dao.findFirst("select * from 72crm_crm_product_detail_img where  product_id = ?",id);
        if (detailImg != null) {
            if (detailImg.getMainFileIds() != null) {
                SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", detailImg.getMainFileIds()).set("path",  BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()));
                crmProduct.set("mainFileList", updateUrl(sqlPara));
            } else {
                crmProduct.set("mainFileList", null);
            }

            if (detailImg.getDetailFileIds() != null) {
                SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", detailImg.getDetailFileIds()).set("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()));
                crmProduct.set("detailFileList", updateUrl(sqlPara));
            } else {
                crmProduct.set("detailFileList", null);
            }
        }else {
            crmProduct.set("mainFileList", null);
            crmProduct.set("detailFileList", null);
        }
        return crmProduct;
    }

    /**
     * 根据id查询产品基本信息
     */
    public List<Record> information(Integer id) {
        Record record = queryById(id);
        List<String> keyList = Arrays.asList("name", "num","price","description");
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间","更新时间");
        List<Record> recordList = adminFieldService.queryInformation(CrmEnum.CRM_PRODUCT,record, keyList);
        recordList.add(new Record().set("name","产品类型").set("value",new Record().set("categoryId",record.getInt("category_id")).set("categoryName",record.getStr("categoryName"))).set("formType","category").set("field_type",1));
        return recordList.stream().sorted(Comparator.comparingInt(r->-r.getInt("field_type"))).peek(r-> {
            r.remove("field_type", "setting", "type");
            if (systemFieldList.contains(r.getStr("name"))){
                r.set("sysInformation",1);
            }else {
                r.set("sysInformation",0);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 根据id删除产品
     */
    public R deleteByIds(String productIds) {
        int update = Db.template("crm.product.updateStatus", Kv.by("ids", productIds).set("status",3)).update();
        actionRecordUtil.addDeleteActionRecord(CrmEnum.CRM_PRODUCT, productIds.split(","));
        return update>0 ? R.ok() : R.error();
    }

    /**
     * 上架或者下架
     */
    public R updateStatus(String ids, Integer status) {
        List<Record> recordList = Db.find("select batch_id from 72crm_crm_product where  product_id in (" + ids + ")");
        StringBuilder batchIds = new StringBuilder();
        for (Record record : recordList) {
            if (batchIds.length() == 0) {
                batchIds.append("'").append(record.getStr("batch_id")).append("'");
            } else {
                batchIds.append(",'").append(record.getStr("batch_id")).append("'");
            }
        }
        String a;
        if (status == 0) {
            a = "下架";
        } else {
            a = "上架";
        }
        StringBuilder sqlfield = new StringBuilder("update 72crm_crm_product_data set value = '" + a + "' where name = '是否上下架' and batch_id in ( ");
        sqlfield.append(batchIds.toString());
        sqlfield.append(" )");
        int f = Db.update(sqlfield.toString());
        return R.isSuccess(f > 0);
    }

    /**
     * @author wyq
     * 查询编辑字段
     */
    public List<Record> queryField(Integer productId) {
        Record product = queryById(productId);
        List<Integer> list = crmProductCategoryService.queryId(null, product.getInt("category_id"));
        Integer[] categoryIds = new Integer[list.size()];
        categoryIds = list.toArray(categoryIds);
        product.set("category_id",categoryIds);
        return adminFieldService.queryUpdateField(CrmEnum.CRM_PRODUCT.getType(),product);
    }

    /**
     * @author wyq
     * 获取产品导入查重字段
     */
    public R getCheckingField(){
        return R.ok().put("data","产品名称");
    }


    /**
     * @author wyq
     * 附件列表
     */
    public R queryFileList(Integer productId) {
        SqlPara sqlPara=Db.getSqlPara("crm.product.queryFileList", Kv.by("productId", productId));
        return R.ok().put("data", Db.find(sqlPara));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public R num(Integer productId) {
        Kv kv=Kv.by("productId", productId);
        Record record = Db.findFirst(Db.getSqlPara("crm.product.queryNum", kv));
        return R.ok().put("data", record);
    }

    /**
     * @author wyq
     * 转移
     */
    @Before(Tx.class)
    public R updateOwnerUserId(String productIds, Long ownerUserId) {
        String[] ids = productIds.split(",");
        Db.update(Db.getSqlPara("crm.product.updateOwnerUserId", Kv.by("ownerUserId", ownerUserId).set("ids", ids)));
        for (String id : ids) {
            actionRecordUtil.addConversionRecord(Integer.valueOf(id), CrmEnum.CRM_PRODUCT, ownerUserId);
        }
        return R.ok();
    }
}
