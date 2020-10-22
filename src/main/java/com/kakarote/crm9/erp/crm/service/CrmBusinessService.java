package com.kakarote.crm9.erp.crm.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.crm.common.AbstractBasePageList;
import com.kakarote.crm9.erp.crm.common.CrmActivityEnum;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.utils.*;

import java.util.*;
import java.util.stream.Collectors;

public class CrmBusinessService extends AbstractBasePageList {
    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmRecordService crmRecordService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    @Inject
    private CrmActivityService crmActivityService;

    public R queryPageList(BasePageRequest basePageRequest, boolean isDataAuth) {
        Kv kv = getKv(basePageRequest, CrmEnum.CRM_BUSINESS, isDataAuth);
        if (kv.containsKey("error")){
            return R.error(kv.getStr("error"));
        }
        JSONObject resultJsonObject = new JSONObject();
        SqlPara sqlPara;
        if (kv.getInt("fieldType") == 1) {
            sqlPara = Db.getSqlPara("crm.business.queryBusinessPageListByFieldType1", kv);
        } else {
            sqlPara = Db.getSqlPara("crm.business.queryBusinessPageListByFieldType2", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        Integer count = Db.template("crm.business.queryBusinessPageListCount", kv).queryInt();
        resultJsonObject.put("totalRow", count);
        String businessSumMoney = Db.template("crm.business.queryBusinessMoney", kv).queryStr();
        resultJsonObject.put("money", new Record().set("businessSumMoney", businessSumMoney));
        JSONObject data = (JSONObject) kv.get("data");
        if (data != null && data.containsKey("type_id")) {
            String statusId = data.getJSONObject("type_id").getString("statusId");
            if(StrUtil.isNotEmpty(statusId)){
                if("win".equals(statusId)){
                    resultJsonObject.put("rate",100);
                }else if("lose".equals(statusId)){
                    resultJsonObject.put("rate",0);
                }else if("invalid".equals(statusId)){
                    resultJsonObject.put("rate",0);
                }else{
                    resultJsonObject.put("rate",Db.queryStr("select rate from `72crm_crm_business_status` where status_id = ? ",Integer.valueOf(statusId)));
                }
            }
        }
        recordPage.forEach(record -> {
            if(record.getInt("is_end") == 1){
                record.set("status_name", "赢单");
            }else if(record.getInt("is_end") == 2){
                record.set("status_name", "输单");
            }else if(record.getInt("is_end") == 3){
                record.set("status_name", "无效");
            }
        });
        setBusinessStatus(recordPage);
        resultJsonObject.put("list", recordPage);
        return R.ok().put("data", resultJsonObject);
    }

    @Override
    public JSONObject appendOtherSql(boolean isDataAuth, Kv kv, CrmEnum crmEnum) {
        JSONObject jsonObject = super.appendOtherSql(isDataAuth, kv, crmEnum);
        String conditions = " and status != 3";
        jsonObject.put("sql", jsonObject.getString("sql") + conditions);
        return jsonObject;
    }

    @Override
    public JSONObject appendSqlSearch(String search) {
        JSONObject sqlObject = new JSONObject();
        String conditions = " and (business_name like '%{search}%' )";
        sqlObject.put("sql", StrUtil.format(conditions, Kv.by("search", search)));
        sqlObject.put("type", 1);
        return sqlObject;
    }

    @Override
    public String querySelectSql() {
        return Db.getSql("crm.business.querySelectSql") +
                "(select count(*) from 72crm_crm_business_user_star where user_id = " + BaseUtil.getUserId() +
                " and business_id = views.business_id) as star";
    }

    /**
     * @author wyq
     * 新增或更新商机
     */
    @Before(Tx.class)
    public R addOrUpdate(JSONObject jsonObject) {
        CrmBusiness crmBusiness = jsonObject.getObject("entity", CrmBusiness.class);
        if (crmBusiness.getBusinessId() != null && AuthUtil.isCrmOperateAuth(CrmEnum.CRM_BUSINESS, crmBusiness.getBusinessId())) {
            return R.error("没有权限");
        }
        if (ParamsUtil.isOutMoney(crmBusiness.getMoney()) || ParamsUtil.isOutMoney(crmBusiness.getTotalPrice())){
            return R.outMaxMoney();
        }
        JSONArray jsonArray = jsonObject.getJSONArray("product");
        List<CrmBusinessProduct> businessProductList = jsonArray.toJavaList(CrmBusinessProduct.class);
        Db.template("crm.business.clearBusinessProduct",Kv.by("ids",crmBusiness.getBusinessId())).delete();
        String batchId = StrUtil.isNotEmpty(crmBusiness.getBatchId()) ? crmBusiness.getBatchId() : IdUtil.simpleUUID();
        actionRecordUtil.updateRecord(jsonObject.getJSONArray("field"), Kv.by("batchId", batchId).set("dataTableName", "72crm_crm_business_data"));
        save(jsonObject.getJSONArray("field"), batchId);
        boolean saveOrUpdate;
        if (crmBusiness.getBusinessId() != null) {
            crmBusiness.setUpdateTime(DateUtil.date());
            actionRecordUtil.updateRecord(new CrmBusiness().dao().findById(crmBusiness.getBusinessId()), crmBusiness, CrmEnum.CRM_BUSINESS);
            CrmBusiness oldBusiness = CrmBusiness.dao.findById(crmBusiness.getBusinessId());
            if (!oldBusiness.getStatusId().equals(crmBusiness.getStatusId())){
                CrmBusinessChange change = new CrmBusinessChange();
                change.setBusinessId(crmBusiness.getBusinessId());
                change.setStatusId(crmBusiness.getStatusId());
                change.setCreateTime(DateUtil.date());
                change.setCreateUserId(BaseUtil.getUserId());
                change.save();
            }
            saveOrUpdate = crmBusiness.update();
        } else {
            crmBusiness.setCreateTime(DateUtil.date());
            crmBusiness.setUpdateTime(DateUtil.date());
            crmBusiness.setCreateUserId(BaseUtil.getUser().getUserId());
            crmBusiness.setOwnerUserId(BaseUtil.getUser().getUserId());
            crmBusiness.setBatchId(batchId);
            crmBusiness.setRwUserId(",");
            crmBusiness.setRoUserId(",");
            saveOrUpdate = crmBusiness.save();
            if (jsonObject.getInteger("contactsId") != null){
                CrmContactsBusiness crmContactsBusiness = new CrmContactsBusiness();
                crmContactsBusiness.setBusinessId(crmBusiness.getBusinessId());
                crmContactsBusiness.setContactsId(jsonObject.getInteger("contactsId"));
                crmContactsBusiness.save();
            }
            crmActivityService.addActivity(2, CrmActivityEnum.BUSINESS,crmBusiness.getBusinessId(),"");
            actionRecordUtil.addRecord(crmBusiness.getBusinessId(), CrmEnum.CRM_BUSINESS);
        }
        if (businessProductList != null) {
            for (CrmBusinessProduct crmBusinessProduct : businessProductList) {
                crmBusinessProduct.setBusinessId(crmBusiness.getBusinessId());
                crmBusinessProduct.save();
            }
        }
        return saveOrUpdate ? R.ok() : R.error();
    }

    private boolean save(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return false;
        }
        Db.deleteById("72crm_crm_business_data", "batch_id", batchId);
        List<CrmBusinessData> Fieldvs = new ArrayList<>();
        array.forEach(obj -> {
            CrmBusinessData fieldv = TypeUtils.castToJavaBean(obj, CrmBusinessData.class);
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            Fieldvs.add(fieldv);
        });
        Db.batchSave(Fieldvs, 100);
        return true;
    }

    /**
     * @author wyq
     * 根据商机id查询
     */
    public Record queryById(Integer businessId) {
        Record crmBusiness = Db.findFirst(Db.getSql("crm.business.queryById"), BaseUtil.getUserId(), businessId);
        List<Record> recordList = Db.find("select name,value from `72crm_crm_business_data` where batch_id = ?", crmBusiness.getStr("batch_id"));
        recordList.forEach(field->crmBusiness.set(field.getStr("name"),field.getStr("value")));
        return crmBusiness;
    }

    /**
     * @author wyq
     * 基本信息
     */
    public List<Record> information(Integer busienssId) {
        Record record = queryById(busienssId);
        List<String> keyList = Arrays.asList("business_name","deal_date","money","remark");
        List<Record> recordList = adminFieldService.queryInformation(CrmEnum.CRM_BUSINESS,record, keyList);
        List<String> systemFieldList = Arrays.asList("创建人", "创建时间","更新时间","下次联系时间","最后跟进时间");
        recordList.add(new Record().set("name","商机阶段").set("value",new Record().set("statusId",record.getInt("status_id")).set("statusName",record.getStr("statusName"))).set("formType","statusName").set("field_type",1));
        recordList.add(new Record().set("name","商机状态组").set("value",new Record().set("typeId",record.getInt("type_id")).set("typeName",record.getStr("typeName"))).set("formType","typeName").set("field_type",1));
        recordList.add(new Record().set("name","客户名称").set("value",new Record().set("customerId",record.getInt("customer_id")).set("customerName",record.getStr("customer_name"))).set("formType","customer").set("field_type",1));
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
     * @author wyq
     * 根据商机id查询产品
     */
    public R queryProduct(BasePageRequest<CrmBusiness> basePageRequest) {
        Integer businessId = basePageRequest.getData().getBusinessId();
        Integer pageType = basePageRequest.getPageType();
        Record record = Db.findFirst(Db.getSql("crm.product.querySubtotalByBusinessId"), businessId);
        if (record.getStr("money") == null){
            record.set("money",0);
        }
        if (0 == pageType) {
            record.set("list", Db.find(Db.getSql("crm.business.queryProduct"), businessId));
        } else {
            record.set("list", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), new SqlPara().setSql(Db.getSql("crm.business.queryProduct")).addPara(businessId)));
        }
        return R.ok().put("data", record);
    }

    /**
     * @author wyq
     * 根据商机id查询合同
     */
    public R queryContract(BasePageRequest<CrmBusiness> basePageRequest) {
        Integer businessId = basePageRequest.getData().getBusinessId();
        Integer pageType = basePageRequest.getPageType();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_BUSINESS, "a", 1);
        if (0 == pageType) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.business.queryContract", Kv.by("businessId", businessId).set("conditions", conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.business.queryContract", Kv.by("businessId", businessId).set("conditions", conditions))));
        }
    }

    /**
     * @author wyq
     * 根据商机id查询联系人
     */
    public R queryContacts(BasePageRequest<CrmBusiness> basePageRequest){
        Integer businessId = basePageRequest.getData().getBusinessId();
        Integer pageType = basePageRequest.getPageType();
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_CONTACTS, 1);
        if (0 == pageType) {
            return R.ok().put("data", Db.find(Db.getSqlPara("crm.business.queryContacts",Kv.by("businessId",businessId).set("conditions",conditions))));
        } else {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.business.queryContacts",Kv.by("businessId",businessId).set("conditions",conditions))));
        }
    }

    /**
     * @author wyq
     * 商机关联联系人
     */
    public R relateContacts(Integer businessId, String contactsIds){
        String[] contactsIdsArr = contactsIds.split(",");
        Db.delete("delete from 72crm_crm_contacts_business where business_id = ? ",businessId);
        for (String id:contactsIdsArr){
            CrmContactsBusiness crmContactsBusiness = new CrmContactsBusiness();
            crmContactsBusiness.setContactsId(Integer.valueOf(id));
            crmContactsBusiness.setBusinessId(businessId);
            crmContactsBusiness.save();
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 商机解除关联联系人
     */
    @Before(Tx.class)
    public R unrelateContacts(Integer businessId, String contactsIds){
        List<String> strings = StrUtil.splitTrim(contactsIds,",");
        CrmBusiness crmBusiness=CrmBusiness.dao.findById(businessId);
        //如果解除关联的联系人包含首要联系人则将首要联系人清空
        if(strings.contains(crmBusiness.getContactsId().toString())){
            crmBusiness.setContactsId(null);
            crmBusiness.update();
        }
        SqlPara sqlPara = Db.getSqlPara("crm.business.unrelateContacts",Kv.by("businessId",businessId).set("ids",strings));
        Db.delete(sqlPara.getSql(),sqlPara.getPara());
        return R.ok();
    }

    /**
     * @author wyq
     * 根据id删除商机
     */
    @Before(Tx.class)
    public R deleteByIds(String businessIds) {
        String[] idsList = StrUtil.split(businessIds,",");
        Integer number = Db.queryInt(Db.getSql("crm.business.queryContractNumber"), businessIds);
        if (number > 0) {
            return R.error("该条数据与其他数据有必要关联，请勿删除");
        }
        Kv kv=Kv.by("ids",idsList);
        Db.template("crm.business.deleteBusiness",kv).delete();

        List<String> batchList = Db.template("crm.business.queryBusinessBatchId",kv).query();

        //删除文件
        Aop.get(AdminFileService.class).removeByBatchId(batchList.toArray(new String[0]));
        //删除跟进记录
        Aop.get(CrmActivityService.class).deleteActivityRecord(CrmActivityEnum.BUSINESS,idsList);
        //删除字段操作记录
        Aop.get(CrmRecordService.class).deleteActionRecord(CrmEnum.CRM_BUSINESS,idsList);
        actionRecordUtil.addDeleteActionRecord(CrmEnum.CRM_BUSINESS, idsList);
        //删除商机产品关联
        Db.template("crm.business.clearBusinessProduct",kv).delete();
        //删除自定义字段
        Db.template("admin.field.deleteFieldv", Kv.by("batchList", batchList).set("dataTableName", "72crm_crm_business_data")).delete();
        return R.ok();
    }

    /**
     * 根据客户id变更负责人
     *
     * @author wyq
     */
    public R updateOwnerUserId(CrmCustomer crmCustomer) {
        CrmBusiness crmBusiness = new CrmBusiness();
        crmBusiness.setNewOwnerUserId(crmCustomer.getNewOwnerUserId());
        crmBusiness.setTransferType(crmCustomer.getTransferType());
        crmBusiness.setPower(crmCustomer.getPower());
        String businessIds = Db.queryStr("select GROUP_CONCAT(business_id) from 72crm_crm_business where customer_id in ("+crmCustomer.getCustomerIds()+")");
        if (StrUtil.isEmpty(businessIds)){
            return R.ok();
        }
        crmBusiness.setBusinessIds(businessIds);
        return transfer(crmBusiness);
    }

    /**
     * @author wyq
     * 根据商机id变更负责人
     */
    public R transfer(CrmBusiness crmBusiness) {
        String[] businessIdsArr = crmBusiness.getBusinessIds().split(",");
        for (String businessId : businessIdsArr) {
            if (AuthUtil.isRwAuth(Integer.parseInt(businessId), CrmEnum.CRM_BUSINESS)) {
                return R.error("无权限转移");
            }
            String memberId = "," + crmBusiness.getNewOwnerUserId() + ",";
            Db.update(Db.getSql("crm.business.deleteMember"), memberId, memberId, Integer.valueOf(businessId));
            CrmBusiness oldBusiness = CrmBusiness.dao.findById(Integer.valueOf(businessId));
            crmBusiness.setBusinessId(Integer.valueOf(businessId));
            crmBusiness.setOwnerUserId(crmBusiness.getNewOwnerUserId());
            if (2 == crmBusiness.getTransferType() && !crmBusiness.getOwnerUserId().equals(oldBusiness.getOwnerUserId())) {
                if (1 == crmBusiness.getPower()) {
                    crmBusiness.setRoUserId(oldBusiness.getRoUserId() + oldBusiness.getOwnerUserId() + ",");
                }
                if (2 == crmBusiness.getPower()) {
                    crmBusiness.setRwUserId(oldBusiness.getRwUserId() + oldBusiness.getOwnerUserId() + ",");
                }
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_BUSINESS_USER,crmBusiness,oldBusiness.getOwnerUserId());
            }
            crmBusiness.update();
            actionRecordUtil.addConversionRecord(Integer.valueOf(businessId), CrmEnum.CRM_BUSINESS, crmBusiness.getNewOwnerUserId());
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 查询团队成员
     */
    public List<Record> getMembers(Integer businessId) {
        CrmBusiness crmBusiness = CrmBusiness.dao.findById(businessId);
        List<Record> recordList = new ArrayList<>();
        if (crmBusiness.getOwnerUserId() != null) {
            Record ownerUser = Db.findFirst(Db.getSql("crm.customer.getMembers"), crmBusiness.getOwnerUserId());
            recordList.add(ownerUser.set("power", "负责人权限").set("groupRole", "负责人"));
        }
        String roUserId = crmBusiness.getRoUserId();
        String rwUserId = crmBusiness.getRwUserId();
        String memberIds = roUserId + rwUserId.substring(1);
        if (",".equals(memberIds)) {
            return recordList;
        }
        String[] memberIdsArr = memberIds.substring(1, memberIds.length() - 1).split(",");
        Set<String> memberIdsSet = new HashSet<>(Arrays.asList(memberIdsArr));
        for (String memberId : memberIdsSet) {
            Record record = Db.findFirst(Db.getSql("crm.customer.getMembers"), memberId);
            if (roUserId.contains(memberId)) {
                record.set("power", "只读").set("groupRole", "普通成员");
            }
            if (rwUserId.contains(memberId)) {
                record.set("power", "读写").set("groupRole", "普通成员");
            }
            recordList.add(record);
        }
        return recordList;
    }

    /**
     * @author wyq
     * 添加团队成员
     */
    @Before(Tx.class)
    public R addMember(CrmBusiness crmBusiness) {
        Set<Integer> businessIdsArr = TagUtil.toSet(crmBusiness.getIds());
        Set<Long> memberArr = TagUtil.toLongSet(crmBusiness.getMemberIds());
        StringBuilder stringBuffer = new StringBuilder();
        for (Integer id : businessIdsArr) {
            if (AuthUtil.isCrmOperateAuth(CrmEnum.CRM_BUSINESS, id)) {
                return R.error("没有权限");
            }
            CrmBusiness oldBusiness = CrmBusiness.dao.findById(id);
            Long ownerUserId = oldBusiness.getOwnerUserId();
            for (Long memberId : memberArr) {
                if (ownerUserId.equals(memberId)) {
                    return R.error("负责人不能重复选为团队成员");
                }
                Db.update(Db.getSql("crm.business.deleteMember"), "," + memberId + ",", "," + memberId + ",", id);
            }
            if (1 == crmBusiness.getPower()) {
                stringBuffer.setLength(0);
                String roUserId = stringBuffer.append(CrmBusiness.dao.findById(id).getRoUserId()).append(crmBusiness.getMemberIds()).append(",").toString();
                Db.update("update 72crm_crm_business set ro_user_id = ? where business_id = ?", roUserId, id);
            }
            if (2 == crmBusiness.getPower()) {
                stringBuffer.setLength(0);
                String rwUserId = stringBuffer.append(CrmBusiness.dao.findById(id).getRwUserId()).append(crmBusiness.getMemberIds()).append(",").toString();
                Db.update("update 72crm_crm_business set rw_user_id = ? where business_id = ?", rwUserId, id);
            }
            List<Long> roUserIdList = StrUtil.splitTrim(oldBusiness.getRoUserId(),",").stream().map(Long::parseLong).collect(Collectors.toList());
            List<Long> rwUserIdList = StrUtil.splitTrim(oldBusiness.getRwUserId(),",").stream().map(Long::parseLong).collect(Collectors.toList());
            roUserIdList.addAll(rwUserIdList);
            memberArr.removeAll(roUserIdList);
            memberArr.forEach(memberId -> {
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_BUSINESS_USER, oldBusiness, memberId);
                actionRecordUtil.addMemberActionRecord(CrmEnum.CRM_BUSINESS, id, memberId);
            });
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 删除团队成员
     */
    @Before(Tx.class)
    public R deleteMembers(CrmBusiness crmBusiness) {
        String[] businessIdsArr = crmBusiness.getIds().split(",");
        String[] memberArr = crmBusiness.getMemberIds().split(",");
        for (String id : businessIdsArr) {
            if (AuthUtil.isCrmOperateAuth(CrmEnum.CRM_BUSINESS, Integer.valueOf(id))) {
                return R.error("没有权限");
            }
            CrmBusiness business = CrmBusiness.dao.findById(Integer.valueOf(id));
            for (String memberId : memberArr) {
                Db.update(Db.getSql("crm.business.deleteMember"), "," + memberId + ",", "," + memberId + ",", Integer.valueOf(id));
                if (!Long.valueOf(memberId).equals(BaseUtil.getUserId())){
                    Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_BUSINESS_REMOVE_TEAM, business, Long.parseLong(memberId));
                    actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.CRM_BUSINESS, Integer.valueOf(id), Long.parseLong(memberId), false);
                }else {
                    Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_BUSINESS_TEAM_EXIT, business, business.getOwnerUserId());
                    actionRecordUtil.addDeleteMemberActionRecord(CrmEnum.CRM_BUSINESS, Integer.valueOf(id), Long.parseLong(memberId), true);
                }
            }
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 自助退出团队成员
     */
    @Before(Tx.class)
    public R exitTeam(Integer businessId) {
        Long userId = BaseUtil.getUserId();
        CrmBusiness business = CrmBusiness.dao.findById(businessId);
        if (business.getOwnerUserId().equals(userId)){
            return R.error("负责人不能退出团队！");
        }
        Db.update(Db.getSql("crm.business.deleteMember"), "," + userId + ",", "," + userId + ",", businessId);
        Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_BUSINESS_TEAM_EXIT, business, business.getOwnerUserId());
        return R.ok();
    }

    /**
     * @author wyq
     * 商机状态组展示
     */
    public Record queryBusinessStatus(Integer businessId) {
        List<Record> statusList = Db.find(Db.getSql("crm.business.queryBusinessStatusList"), businessId);
        Record business = Db.findFirst(Db.getSql("crm.business.queryBusinessStatus"),businessId);
        return business.set("statusList",statusList);
    }

    /**
     * @author wyq
     * 商机状态组推进
     */
    @Before(Tx.class)
    public R boostBusinessStatus(CrmBusiness crmBusiness) {
        String businessStatus = "";
        if (crmBusiness.getStatusId() != null) {
            CrmBusinessChange change = new CrmBusinessChange();
            change.setBusinessId(crmBusiness.getBusinessId());
            change.setStatusId(crmBusiness.getStatusId());
            change.setCreateTime(DateUtil.date());
            change.setCreateUserId(BaseUtil.getUserId());
            change.save();
            businessStatus = Db.queryStr("select name from `72crm_crm_business_status` where status_id = ?", crmBusiness.getStatusId());
        }else {
            if(crmBusiness.getIsEnd() == 1){
                businessStatus = "赢单";
            }else if(crmBusiness.getIsEnd() == 2){
                businessStatus = "输单";
            }else if(crmBusiness.getIsEnd() == 3){
                businessStatus = "无效";
            }
        }
        crmActivityService.addActivity(3, CrmActivityEnum.BUSINESS,crmBusiness.getBusinessId(),businessStatus);
        return crmBusiness.update() ? R.ok() : R.error();
    }

    /**
     * @author wyq
     * 查询编辑字段
     */
    public List<Record> queryField(Integer businessId) {
        Record business = queryById(businessId);
        List<Record> customerList = new ArrayList<>();
        Record customer = new Record();
        customerList.add(customer.set("customer_id",business.getInt("customer_id")).set("customer_name",business.getStr("customer_name")));
        business.set("customer_id",customerList);
        List<Record> fieldList = adminFieldService.queryUpdateField(CrmEnum.CRM_BUSINESS.getType(),business);
        Optional<Record> optional=fieldList.stream().filter(record -> "remark".equals(record.getStr("field_name"))).findFirst();
        optional.ifPresent(fieldList::remove);
        fieldList.add(new Record().set("field_name","type_id").set("name","商机状态组").set("value",business.getInt("type_id")).set("form_type","business_type").set("setting",new String[0]).set("is_null",1).set("field_type",1));
        fieldList.add(new Record().set("field_name","status_id").set("name","商机阶段").set("value",business.getInt("status_id")).set("form_type","business_status").set("setting",new String[0]).set("is_null",1).set("field_type",1));
        optional.ifPresent(fieldList::add);
        List<Record> productList = Db.find(Db.getSql("crm.business.queryBusinessProduct"), businessId);
        Kv kv = Kv.by("discount_rate", business.getBigDecimal("discount_rate")).set("product", productList).set("total_price", business.getStr("total_price"));
        fieldList.add(new Record().set("field_name","product").set("name","产品").set("value",kv).set("form_type","product").set("setting",new String[]{}).set("is_null",0).set("field_type",1));
        return fieldList;
    }

    /**
     * @author wyq
     * 查询商机状态组及商机状态
     */
    public List<Record> queryBusinessStatusOptions(String type) {
        List<Record> businessTypeList = Db.find("select * from 72crm_crm_business_type where status = 1");
        for (Record record : businessTypeList) {
            Integer typeId = record.getInt("type_id");
            List<Record> businessStatusList = Db.find("select * from 72crm_crm_business_status where type_id = ?", typeId);
            if ("condition".equals(type)){
                Record win = new Record();
                win.set("name","赢单").set("typeId",typeId).set("statusId","win");
                businessStatusList.add(win);
                Record lose = new Record();
                lose.set("name","输单").set("typeId",typeId).set("statusId","lose");
                businessStatusList.add(lose);
                Record invalid = new Record();
                invalid.set("name","无效").set("typeId",typeId).set("statusId","invalid");
                businessStatusList.add(invalid);
            }
            record.set("statusList", businessStatusList);
        }
        return businessTypeList;
    }

    /**
     * 设置首要联系人
     * @return
     */
    public R setContacts(Integer businessId, Integer contactsId){
        if(businessId==null||contactsId==null){
            return R.error("参数错误");
        }
        CrmBusiness crmBusiness=CrmBusiness.dao.findById(businessId);
        crmBusiness.setContactsId(contactsId);
        return R.isSuccess(crmBusiness.update());
    }

    /**
     * @author wyq
     * 附件列表
     */
    public R queryFileList(Integer businessId) {
        SqlPara sqlPara=Db.getSqlPara("crm.business.queryFileList", Kv.by("businessId", businessId));
        return R.ok().put("data", Db.find(sqlPara));
    }

    /**
     * @author wyq
     * 详情页数量展示
     */
    public R num(Integer businessId) {
        String conditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_CONTRACT, 1);
        String contactsConditions = AuthUtil.getCrmAuthSql(CrmEnum.CRM_CONTACTS, "b",1);
        Kv kv=Kv.by("businessId", businessId).set("conditions", conditions).set("contactsConditions",contactsConditions);
        Record record = Db.findFirst(Db.getSqlPara("crm.business.queryNum", kv));
        return R.ok().put("data", record);
    }

    /**
     * 拼装商机数据
     * @param list
     */
    public void setBusinessStatus(List<Record> list){
        list.forEach(record -> {
            if(record.getInt("is_end") == 0){
                Integer sortNum = Db.queryInt("select order_num from 72crm_crm_business_status where status_id = ?", record.getInt("status_id"));
                int totalStatsNum = Db.queryInt("select count(*) from 72crm_crm_business_status where type_id = ?", record.getInt("type_id")) + 1;
                if(sortNum == null){
                    sortNum = 0;
                }
                record.set("progressBar", sortNum + "/" + totalStatsNum);
            }else if(record.getInt("is_end") == 1){
                int totalStatsNum = Db.queryInt("select count(*) from 72crm_crm_business_status where type_id = ?", record.getInt("type_id")) + 1;
                record.set("status_name", "赢单").set("progressBar", totalStatsNum + "/" + totalStatsNum);
            }else if(record.getInt("is_end") == 2){
                int totalStatsNum = Db.queryInt("select count(*) from 72crm_crm_business_status where type_id = ?", record.getInt("type_id")) + 1;
                record.set("status_name", "输单").set("progressBar", "0/" + totalStatsNum);
            }else if(record.getInt("is_end") == 3){
                record.set("status_name", "无效").set("progressBar", "0/0");
            }
        });
    }

    /**
     * 标星（关注）
     */
    public R star(Integer businessId) {
        Long userId = BaseUtil.getUserId();
        CrmBusinessUserStar userStar = CrmBusinessUserStar.dao.findFirst("select * from 72crm_crm_business_user_star where user_id = ? and business_id = ?", userId, businessId);
        List<Integer> businessIdList = Db.query("select business_id from 72crm_crm_business_user_star where user_id = ?", userId);
        if (ObjectUtil.isEmpty(userStar)) {
            new CrmBusinessUserStar().setUserId(userId).setBusinessId(businessId).save();
            businessIdList.add(businessId);
        } else {
            userStar.delete();
            businessIdList.remove(businessId);
        }
        String businessIds = CollectionUtil.isEmpty(businessIdList) ? "0" : CollectionUtil.join(businessIdList, ",");
        JSONObject data = new JSONObject().fluentPut("business_id", new JSONObject().fluentPut("name", "business_id").fluentPut("condition", "in").fluentPut("value", businessIds));
        Db.update("update 72crm_admin_scene set data = ? where type = 5 and user_id = ? and bydata = 'star'", data.toString(), userId);
        return R.ok();
    }
}
