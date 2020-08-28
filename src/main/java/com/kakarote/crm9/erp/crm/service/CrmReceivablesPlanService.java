package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.service.AdminFieldService;
import com.kakarote.crm9.erp.crm.entity.CrmContract;
import com.kakarote.crm9.erp.crm.entity.CrmReceivables;
import com.kakarote.crm9.erp.crm.entity.CrmReceivablesPlan;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.FieldUtil;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CrmReceivablesPlanService {
    @Inject
    private FieldUtil fieldUtil;
    @Inject
    private AdminFieldService adminFieldService;

    /**
     * 添加或修改回款计划
     */
    public R saveAndUpdate(JSONObject jsonObject) {
        CrmReceivablesPlan crmReceivablesPlan = jsonObject.getObject("entity", CrmReceivablesPlan.class);
        CrmContract crmContract=CrmContract.dao.findById(crmReceivablesPlan.getContractId());
        if(crmContract==null||!crmContract.getCheckStatus().equals(1)){
            return R.error("当前合同未审核通过，不能添加回款");
        }
        String batchId = StrUtil.isNotEmpty(crmReceivablesPlan.getFileBatch()) ? crmReceivablesPlan.getFileBatch() : IdUtil.simpleUUID();
        if (null == crmReceivablesPlan.getPlanId()) {
            crmReceivablesPlan.setCreateTime(DateUtil.date());
            crmReceivablesPlan.setCreateUserId(BaseUtil.getUser().getUserId());
            crmReceivablesPlan.setFileBatch(batchId);
            CrmReceivablesPlan receivablesPlan = CrmReceivablesPlan.dao.findFirst(Db.getSql("crm.receivablesplan.queryByContractId"), crmReceivablesPlan.getContractId());
            if (receivablesPlan == null) {
                crmReceivablesPlan.setNum("1");
            } else {
                crmReceivablesPlan.setNum(Integer.valueOf(receivablesPlan.getNum()) + 1 + "");
            }
            return crmReceivablesPlan.save() ? R.ok() : R.error();
        } else {
            Integer number = Db.queryInt("select count(*) from 72crm_crm_receivables where plan_id = ?",crmReceivablesPlan.getPlanId());
            if (number > 0 ){
                return R.error("该回款计划已收到回款，请勿编辑");
            }
            crmReceivablesPlan.setUpdateTime(DateUtil.date());
            return crmReceivablesPlan.update() ? R.ok() : R.error();
        }
    }

    /**
     * @author wyq
     * 删除回款计划
     */
    public R deleteByIds(String planIds){
        String[] idsArr = planIds.split(",");
        List<Record> idsList = Arrays.stream(idsArr).map(id -> new Record().set("plan_id", id)).collect(Collectors.toList());
        return Db.tx(() -> {
            Db.batch(Db.getSql("crm.receivablesplan.deleteByIds"), "plan_id", idsList, 100);
            Db.template("crm.receivablesplan.updatePlanId", Kv.by("ids",idsArr)).update();
            Db.template("crm.receivablesplan.updateReceivablesByPlanId",Kv.by("ids",idsArr)).update();
            return true;
        }) ? R.ok() : R.error();
    }

    /**
     * @author zxy
     * 查询回款自定义字段（新增）
     */
    public List<Record> queryField() {
        List<Record> fieldList = new ArrayList<>();
        String[] settingArr = new String[]{};
        String[] returnTypeArr = new String[]{"支票","现金","邮政汇款","电汇","网上转账","支付宝","微信支付","其他"};
        fieldUtil.getFixedField(fieldList, "customer_id", "客户名称", "", "customer", settingArr, 1);
        fieldUtil.getFixedField(fieldList, "contract_id", "合同编号", "", "contract", settingArr, 1);
        fieldUtil.getFixedField(fieldList, "money", "计划回款金额", "", "number", settingArr, 1);
        fieldUtil.getFixedField(fieldList, "return_date", "计划回款日期", "", "date", settingArr, 1);
        fieldUtil.getFixedField(fieldList, "return_type", "计划回款方式", "", "select", returnTypeArr, 1);
        fieldUtil.getFixedField(fieldList, "remind", "提前几天提醒", "", "number", settingArr, 0);
        fieldUtil.getFixedField(fieldList, "remark", "备注", "", "textarea", settingArr, 0);
        return fieldList;
    }

    /**
     * @author wyq
     * 查询回款自定义字段（编辑）
     */
    public List<Record> queryField(Integer id) {
        Record receivablesPlan = Db.findFirst(Db.getSql("crm.receivablesplan.queryUpdateField"),id);
        List<Record> fieldList = new ArrayList<>();
        String[] settingArr = new String[]{};
        String[] returnTypeArr = new String[]{"支票","现金","邮政汇款","电汇","网上转账","支付宝","微信支付","其他"};
        List<Record> customerList = new ArrayList<>();
        customerList.add(new Record().set("customer_id", receivablesPlan.getInt("customer_id")).set("customer_name", receivablesPlan.getStr("customer_name")));
        fieldUtil.getFixedField(fieldList, "customer_id", "客户名称", customerList, "customer", settingArr, 1);
        List<Record> contractList = new ArrayList<>();
        contractList.add(new Record().set("contract_id", receivablesPlan.getInt("contract_id")).set("num", receivablesPlan.getStr("num")));
        fieldUtil.getFixedField(fieldList, "contract_id", "合同编号", contractList, "contract", settingArr, 1);
        fieldUtil.getFixedField(fieldList, "money", "计划回款金额", receivablesPlan.getStr("money"), "number", settingArr, 1);
        fieldUtil.getFixedField(fieldList, "return_date", "计划回款日期", receivablesPlan.getStr("return_date"), "date", settingArr, 1);
        fieldUtil.getFixedField(fieldList, "return_type", "计划回款方式", receivablesPlan.getStr("return_type"), "select", returnTypeArr, 1);
        fieldUtil.getFixedField(fieldList, "remind", "提前几天提醒", receivablesPlan.getStr("remind"), "number", settingArr, 0);
        fieldUtil.getFixedField(fieldList, "remark", "备注", receivablesPlan.getStr("remark"), "textarea", settingArr, 0);
        return fieldList;
    }

    /**
     * 根据合同查询回款计划
     */
    public R qureyListByContractId(BasePageRequest<CrmReceivables> basePageRequest) {

        Integer pageType = basePageRequest.getPageType();
        if (pageType == null || 0 == pageType) {
            return R.ok().put("data", Db.find(Db.getSql("crm.receivablesplan.queryListByContractId"), basePageRequest.getData().getContractId()));
        }
        if (1 == pageType) {
            return R.ok().put("data", Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("crm.receivablesplan.queryListByContractId", basePageRequest.getData().getContractId())));
        }
        return R.error();
    }

    /**
     * 新建汇款时根据合同ID查询回款计划
     * @author wyq
     */
    public R queryReceivablesPlansByContractId(Integer contractId, Integer receivablesId){
        List<Record> recordList = Db.find(Db.getSql("crm.receivablesplan.queryReceivablesPlansByContractId"),contractId);
        if (receivablesId != null){
            Record receivables = Db.findFirst("select plan_id,num,money,return_type,date(return_date) as return_date from 72crm_crm_receivables_plan where receivables_id = ?", receivablesId);
            if (receivables != null){
                recordList.add(receivables);
            }
        }
        return R.ok().put("data",recordList);
    }

    /**
     * 根据合同id和客户id查询未使用的回款计划
     */
    public R queryByContractAndCustomer(CrmReceivablesPlan receivablesPlan) {
        List<CrmReceivablesPlan> plans = CrmReceivablesPlan.dao.find(Db.getSql("crm.receivablesplan.queryByCustomerIdContractId"), receivablesPlan.getContractId(), receivablesPlan.getCustomerId());
        return R.ok().put("data", plans);
    }
}
