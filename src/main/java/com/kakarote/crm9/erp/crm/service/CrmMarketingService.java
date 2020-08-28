package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.kakarote.crm9.common.constant.BehaviorEnum;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmMarketing;
import com.kakarote.crm9.erp.crm.entity.CrmMarketingInfo;
import com.kakarote.crm9.utils.ActionRecordUtil;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;

import java.util.*;

public class CrmMarketingService {

    public static byte[] bytes = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

    @Inject
    private CrmLeadsService leadsService;

    @Inject
    private CrmCustomerService customerService;

    public R addOrUpdate(CrmMarketing crmMarketing) {
        boolean bol;
        Long userId = BaseUtil.getUserId();
        if (crmMarketing.getStartTime().compareTo(crmMarketing.getEndTime()) > 0) {
            return R.error("创建时间应早于结束时间");
        }
        if (crmMarketing.getMarketingId() == null){
            Set<Long> relationIds = TagUtil.toLongSet(crmMarketing.getRelationUserId());
            crmMarketing.setRelationUserId(TagUtil.fromLongSet(relationIds));
            crmMarketing.setCreateUserId(userId);
            crmMarketing.setCreateTime(new Date());
            crmMarketing.setUpdateTime(new Date());
            bol = crmMarketing.save();
            Aop.get(ActionRecordUtil.class).addObjectActionRecord(CrmEnum.CRM_MARKETING, crmMarketing.getMarketingId(), BehaviorEnum.SAVE);
        }else {
            crmMarketing.setUpdateTime(new Date());
            Aop.get(ActionRecordUtil.class).updateRecord(new CrmMarketing().dao().findById(crmMarketing.getMarketingId()), crmMarketing, CrmEnum.CRM_MARKETING);
            bol = crmMarketing.update();
        }
        return bol?R.ok():R.error();
    }

    public R queryPageList(BasePageRequest<CrmMarketing> basePageRequest,Integer status) {
        List<Long> userIds = Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
        userIds.add(BaseUtil.getUserId());
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Page<CrmMarketing> paginate =CrmMarketing.dao.template("crm.marketing.queryPageList",
                Kv.by("path", BaseConstant.UPLOAD_PATH)
                        .set("address", BaseUtil.getIpAddress())
                        .set("userIds", userIds).set("crmType", jsonObject.getInteger("crmType")).
                        set("search", jsonObject.getString("search")).
                        set("isAdmin",BaseUtil.isAdmin()).
                        set("timeType",basePageRequest.getJsonObject().get("timeType")).
                        set("status",status)).
                paginate(basePageRequest.getPage(), basePageRequest.getLimit());
        List<CrmMarketing> list = paginate.getList();
        for (CrmMarketing crmMarketing: list) {
            if (crmMarketing.getMainFileIds() != null) {
                SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", crmMarketing.getMainFileIds()).set("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()));
                List<Record> recordList=updateUrl(sqlPara);
                crmMarketing.put("mainFileList",recordList);
                crmMarketing.put("mainFile",  recordList.size()>0?recordList.get(0):null);
            }else {
                crmMarketing.put("mainFileList",  null);
            }
        }
        return R.ok().put("data",paginate);
    }



    public R queryById(Integer marketingId) {
        return queryById(marketingId,null);
    }

    public R queryById(Integer marketingId,String device) {
        AES aes = SecureUtil.aes(bytes);
        CrmMarketing crmMarketing = CrmMarketing.dao.template("crm.marketing.queryById", Kv.by("marketingId",marketingId)).findFirst();
        crmMarketing.put("enMarketingId",aes.encryptHex(crmMarketing.getMarketingId().toString()));
        crmMarketing.put("currentUserId",aes.encryptHex(BaseUtil.getUserId().toString()));
        crmMarketing.put("relationUserInfo", Db.find(Db.getSqlPara("admin.user.queryByIds", Kv.by("ids", TagUtil.toLongSet(crmMarketing.getRelationUserId())))));
        crmMarketing.put("createUserInfo", Db.findFirst("select user_id,realname,img from 72crm_admin_user where user_id = ?",crmMarketing.getCreateUserId()));
        if (StrUtil.isNotEmpty(crmMarketing.getDetailFileIds())) {
            SqlPara sqlPara=Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", crmMarketing.getDetailFileIds()).set("path", BaseConstant.UPLOAD_PATH).set("address",BaseUtil.getIpAddress()));
            crmMarketing.put("detailFileList", updateUrl(sqlPara));
        }else {
            crmMarketing.put("detailFileList", new ArrayList<>());
        }
        if (crmMarketing.getMainFileIds() != null) {
            SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", crmMarketing.getMainFileIds()).set("path",  BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()));
            crmMarketing.put("mainFileList",  updateUrl(sqlPara));
        }else {
            crmMarketing.put("mainFileList",  new ArrayList<>());
        }
        Integer second = crmMarketing.getSecond();
        Integer count = Db.queryInt("select count(1) from `72crm_crm_marketing_info` where marketing_id = ? and device = ?", marketingId, device);
        if (second == 1 && count > 0 ){
            crmMarketing.put("isAdd",0);
        }else {
            crmMarketing.put("isAdd",1);
        }
        if (crmMarketing.getStatus() == 0 || System.currentTimeMillis() > crmMarketing.getEndTime().getTime()){
            crmMarketing.put("isEnd",1);
        }else {
            crmMarketing.put("isEnd",0);
        }
        return R.ok().put("data",crmMarketing);
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
    @Before(Tx.class)
    public R deleteByIds(String marketingIds) {
        for (Integer marketingId : TagUtil.toSet(marketingIds)) {
            Integer count = Db.queryInt("select count(1) from `72crm_crm_marketing_info` where marketing_id = ? and status = 0", marketingId);
            if (count > 0){
                return R.error("有未同步数据，不能删除！");
            }
            CrmMarketing.dao.deleteById(marketingId);
            Db.delete("delete from `72crm_crm_marketing` where marketing_id = ?",marketingId);
        }
        return R.ok();
    }


    public List<Record> queryField(Integer marketingId) {
        CrmMarketing crmMarketing = CrmMarketing.dao.findById(marketingId);
        List<Record> recordList = Db.template("crm.marketing.queryFieldByIds", Kv.by("fieldIds", TagUtil.toSet(crmMarketing.getFieldDataId()))).find();
        Aop.get(AdminFieldService.class).recordToFormType(recordList);
        return recordList;
    }

    public R queryAddField(String marketingId) {
        AES aes = SecureUtil.aes(bytes);
        Integer marketingIdInt = Integer.valueOf(aes.decryptStr(marketingId));
        CrmMarketing crmMarketing = CrmMarketing.dao.findById(marketingIdInt);
        if (crmMarketing.getStatus() == 0){
            return R.error("该推广停止使用！");
        }
        if (System.currentTimeMillis() > crmMarketing.getEndTime().getTime()){
            return R.error("二维码过期！");
        }
        Db.update("update `72crm_crm_marketing` set browse = browse + 1 where marketing_id = ?",marketingIdInt);
        List<Record> recordList = Db.template("crm.marketing.queryFieldByIds", Kv.by("fieldIds",TagUtil.toSet(crmMarketing.getFieldDataId()))).find();
        Aop.get(AdminFieldService.class).recordToFormType(recordList);
        Kv kv = Kv.by("marketingName", crmMarketing.getMarketingName()).set("list", recordList);
        if (crmMarketing.getMainFileIds() != null) {
            SqlPara sqlPara = Db.getSqlPara("admin.file.queryByIds", Kv.by("fileIds", crmMarketing.getMainFileIds()).set("path", BaseConstant.UPLOAD_PATH).set("address", BaseUtil.getIpAddress()));
            kv.set("mainFileList",  updateUrl(sqlPara));
        }else {
            kv.set("mainFileList",  new ArrayList<>());
        }
        return R.ok().put("data",kv);
    }


    public Page<Record> census(BasePageRequest basePageRequest) {
        JSONObject data = basePageRequest.getJsonObject();
        Integer marketingId = data.getInteger("marketingId");
        Long userId = BaseUtil.getUserId();
        Integer status = data.getInteger("status");
        List<Long> userIds = Aop.get(AdminUserService.class).queryChileUserIds(userId, BaseConstant.AUTH_DATA_RECURSION_NUM);
        userIds.add(BaseUtil.getUserId());
        Page<Record> paginate = Db.template("crm.marketing.census", Kv.by("marketingId", marketingId).set("userIds", userIds).set("status",status)).paginate(basePageRequest.getPage(),basePageRequest.getLimit());
        List<Record> recordList = new ArrayList<>();
        paginate.getList().forEach(record -> {
            String fieldInfo = record.getStr("field_info");
            record.remove("field_info");
            JSONObject jsonObject = JSON.parseObject(fieldInfo);
            JSONObject entity = jsonObject.getJSONObject("entity");
            jsonObject.getJSONArray("field").forEach(field->{
                JSONObject adminFieldv = (JSONObject)field;
                entity.put(adminFieldv.getString("name"),adminFieldv.getString("value"));
            });
            entity.put("status",record.getInt("status"));
            entity.put("r_id",record.getInt("r_id"));
            entity.put("owner_user_name",record.getStr("owner_user_name"));
            record.set("entity",entity);
            record.remove("status","r_id");
            recordList.add(new Record().setColumns(entity.getInnerMap()));
        });
        paginate.setList(recordList);
        return paginate;
    }

    public R saveMarketingInfo(JSONObject data) {
        AES aes = SecureUtil.aes(bytes);
        Integer marketingId = Integer.valueOf(aes.decryptStr(data.getString("marketingId")));
        Long currentUserId = Long.valueOf(aes.decryptStr(data.getString("currentUserId")));
        CrmMarketing crmMarketing = CrmMarketing.dao.findById(marketingId);
        CrmMarketingInfo marketingInfo = new CrmMarketingInfo();
        marketingInfo.setFieldInfo(data.getJSONObject("fieldInfo").toJSONString());
        marketingInfo.setDevice(data.getString("device"));
        marketingInfo.setMarketingId(marketingId);
        marketingInfo.setOwnerUserId(currentUserId);
        Integer second = crmMarketing.getSecond();
        Integer count = Db.queryInt("select count(1) from `72crm_crm_marketing_info` where marketing_id = ? and device = ?", marketingInfo.getMarketingId(), marketingInfo.getDevice());
        if (second == 1 && count > 0 ){
            return R.error("只能填写一次！");
        }
        Db.update("update 72crm_crm_marketing set submit_num = submit_num + 1 where marketing_id = ?",marketingId);
        marketingInfo.setCreateTime(new Date());
        return marketingInfo.save()?R.ok():R.error();
    }

    @Before(Tx.class)
    public R syncData(Integer marketingId,String rIds) {
        Set<Integer> ids;
        if (StrUtil.isEmpty(rIds)){
            ids = new HashSet<>(Db.query("select r_id from `72crm_crm_marketing_info` where marketing_id = ? and owner_user_id = ?",marketingId,BaseUtil.getUserId()));
        }else {
            ids = TagUtil.toSet(rIds);
        }
        for (Integer id : ids) {
            CrmMarketingInfo marketingInfo = CrmMarketingInfo.dao.findById(id);
            CrmMarketing crmMarketing = CrmMarketing.dao.findById(marketingInfo.getMarketingId());
            Integer crmType = crmMarketing.getCrmType();
            marketingInfo.getFieldInfo();
            Integer status = marketingInfo.getStatus();
            if (status == 0){
                marketingInfo.setStatus(1);
                marketingInfo.update();
            }else if (status == 1){
                return R.error("数据已同步！");
            }
            JSONObject jsonObject = JSON.parseObject(marketingInfo.getFieldInfo());
            R r = new R();
            if (crmType.equals(CrmEnum.CRM_LEADS.getType())){
                r = leadsService.addOrUpdate(jsonObject);
            }else if (crmType.equals(CrmEnum.CRM_CUSTOMER.getType())){
                r = customerService.addOrUpdate(jsonObject,"noImport");
            }
            if ("500".equals(r.get("code"))){
                marketingInfo.setStatus(2);
                marketingInfo.update();
                return R.error("同步失败,"+r.get("msg"));
            }
        }
        return R.ok();
    }


    public List<Record> allExportExcel() {
        List<Long> userIds = Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
        userIds.add(BaseUtil.getUserId());
        return Db.template("crm.marketing.queryPageList", Kv.by("userIds", userIds)).find();
    }


    @Before(Tx.class)
    public R updateStatus(String marketingIds, Integer status) {
        List<CrmMarketing> crmMarketingList = new ArrayList<>();
        TagUtil.toSet(marketingIds).forEach(id->{
            CrmMarketing marketing = new CrmMarketing();
            marketing.setMarketingId(id);
            marketing.setStatus(status);
            crmMarketingList.add(marketing);
        });
        Aop.get(ActionRecordUtil.class).addMarketingUpdateStatusRecord(marketingIds.split(","), CrmEnum.CRM_MARKETING, status);
        Db.batchUpdate(crmMarketingList,100);
        return R.ok();
    }

    public R updateShareNum(Long marketingId,Integer num){
        if (num == null){
            Db.update("update 72crm_crm_marketing set share_num = share_num + 1 where marketing_id = ?",marketingId);
        }else {
            Db.update("update 72crm_crm_marketing set share_num = ? where marketing_id = ?",num,marketingId);
        }
        return R.ok();

    }
}
