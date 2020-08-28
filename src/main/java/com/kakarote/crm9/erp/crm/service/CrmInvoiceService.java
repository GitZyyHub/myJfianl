package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminConfig;
import com.kakarote.crm9.erp.admin.entity.AdminExamineLog;
import com.kakarote.crm9.erp.admin.service.AdminExamineRecordService;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminMessageService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmInvoice;
import com.kakarote.crm9.erp.crm.entity.CrmInvoiceInfo;
import com.kakarote.crm9.utils.*;

import java.util.*;

/**
 * @author wyq
 */
public class CrmInvoiceService {
    @Inject
    private AdminExamineRecordService examineRecordService;

    @Inject
    private ActionRecordUtil actionRecordUtil;

    public R queryPageList(BasePageRequest basePageRequest) {
        StringBuilder conditions = new StringBuilder(" 1=1 ");
        Integer sceneId = basePageRequest.getJsonObject().getInteger("sceneId");
        if (sceneId != null) {
            if (sceneId == 1) {
                conditions.append(" and a.owner_user_id = ").append(BaseUtil.getUserId());
            } else if (sceneId == 2) {
                List<Long> userIdList = Aop.get(AdminUserService.class).queryChileUserIds(BaseUtil.getUserId(), BaseConstant.AUTH_DATA_RECURSION_NUM);
                if (userIdList.size() > 0) {
                    conditions.append(" and a.owner_user_id in (").append(CollectionUtil.join(userIdList, ",")).append(")");
                } else {
                    Page page = new Page<Record>();
                    page.setPageNumber(basePageRequest.getPage());
                    page.setPageSize(basePageRequest.getLimit());
                    return R.ok().put("data", page);
                }
            }
        }
        JSONObject data = basePageRequest.getJsonObject().getJSONObject("data");
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                JSONObject jsonObject = JSON.parseObject(entry.getValue().toString());
                String condition = jsonObject.getString("condition");
                String value = jsonObject.getString("value");
                String name = jsonObject.getString("name");
                String formType = jsonObject.getString("formType");
                if (!ParamsUtil.isValid(name)) {
                    return R.error("参数包含非法字段");
                }
                if (StrUtil.isNotEmpty(value) && !ParamsUtil.isValid(value)) {
                    return R.error("参数包含非法字段");
                }
                if (StrUtil.isNotEmpty(jsonObject.getString("start")) && !ParamsUtil.isValid(jsonObject.getString("start"))) {
                    return R.error("参数包含非法字段");
                }
                if (StrUtil.isNotEmpty(jsonObject.getString("end")) && !ParamsUtil.isValid(jsonObject.getString("end"))) {
                    return R.error("参数包含非法字段");
                }
                boolean isNull = Arrays.asList("isNull", "isNotNull").contains(condition);
                boolean isTime = StrUtil.isEmpty(jsonObject.getString("start")) || StrUtil.isEmpty(jsonObject.getString("end"));
                if (formType.equals("date") && isTime) {
                    return R.error("需选择起始和结束时间");
                } else if (!formType.equals("date") && !isNull && StrUtil.isEmpty(value)) {
                    return R.error("value不能为空");
                }
                if (name.equals("customerName")) {
                    conditions.append(" and b.").append(StrUtil.toUnderlineCase(name));
                } else {
                    conditions.append(" and a.").append(StrUtil.toUnderlineCase(name));
                }
                if ("date".equals(formType)) {
                    conditions.append(" between '").append(jsonObject.getString("start")).append("' and '").append(jsonObject.getString("end")).append("'");
                } else if ("is".equals(condition)) {
                    conditions.append(" = '").append(value).append("'");
                } else if ("isNot".equals(condition)) {
                    conditions.append(" != '").append(value).append("'");
                } else if ("contains".equals(condition)) {
                    conditions.append(" like '%").append(value).append("%'");
                } else if ("notContains".equals(condition)) {
                    conditions.append(" not like '%").append(value).append("%'");
                } else if ("isNull".equals(condition)) {
                    conditions.append(" is null");
                } else if ("isNotNull".equals(condition)) {
                    conditions.append(" is not null");
                } else if ("gt".equals(condition)) {
                    conditions.append(" > ").append(value);
                } else if ("egt".equals(condition)) {
                    conditions.append(" >= ").append(value);
                } else if ("lt".equals(condition)) {
                    conditions.append(" < ").append(value);
                } else if ("elt".equals(condition)) {
                    conditions.append(" <= ").append(value);
                } else if ("in".equals(condition)) {
                    conditions.append(" in (").append(value).append(")");
                } else if ("not in".equals(condition)) {
                    conditions.append(" not in (").append(value).append(")");
                }
            }
        }
        return R.ok().put("data", Db.template("crm.invoice.queryPageList", Kv.by("conditions", conditions.toString())).paginate(basePageRequest.getPage(), basePageRequest.getLimit()));
    }

    public R save(CrmInvoice crmInvoice, Long checkUserId) {
        AdminConfig adminConfig = AdminConfig.dao.findFirst("select setting_id,status,value from 72crm_admin_config where name = 'numberSetting' and value = 18");
        if (adminConfig.getStatus() == 1 && StrUtil.isEmpty(crmInvoice.getInvoiceApplyNumber())) {
            String result = Aop.get(ToolUtils.class).generateNumber(adminConfig, null);
            crmInvoice.setInvoiceApplyNumber(result);
        }
        Integer examineCount = Db.queryInt("select count(1) from 72crm_admin_examine where category_type = 3 and status = 1");
        Record examineData = new Record();
        if (examineCount > 0) {
            examineData = examineRecordService.saveExamineRecord(3, checkUserId, crmInvoice.getOwnerUserId(), null, crmInvoice.getCheckStatus());
            if (examineData.getInt("status") == 0) {
                return R.error("没有启动的审核步骤，不能添加！");
            } else if (examineData.getInt("status") == 2) {
                return R.error("没有审批人，不能添加！");
            } else {
                crmInvoice.setExamineRecordId(examineData.getInt("recordId"));
            }
            crmInvoice.setCheckStatus(0);
        } else {
            if (crmInvoice.getCheckStatus() == null) {
                crmInvoice.setCheckStatus(1);
            }
        }
        String batchId = StrUtil.isNotEmpty(crmInvoice.getBatchId()) ? crmInvoice.getBatchId() : IdUtil.simpleUUID();
        crmInvoice.setOwnerUserId(BaseUtil.getUserId()).setCreateUserId(BaseUtil.getUserId()).setCreateTime(new Date()).setUpdateTime(new Date()).setBatchId(batchId).save();
        actionRecordUtil.addRecord(crmInvoice.getInvoiceId(), CrmEnum.CRM_INVOICE);
        addExamineNotice(crmInvoice, examineData);
        return R.ok();
    }

    public R update(CrmInvoice crmInvoice, Long checkUserId) {
        CrmInvoice oldInvoice = CrmInvoice.dao.findById(crmInvoice.getInvoiceId());
        if (oldInvoice.getCheckStatus() == 1) {
            return R.error("已通过的合同不能编辑");
        }
        if (oldInvoice.getCheckStatus() != 4 && oldInvoice.getCheckStatus() != 2) {
            return R.error("不能编辑，请先撤回再编辑！");
        }
        Integer examineCount = Db.queryInt("select count(1) from 72crm_admin_examine where category_type = 3 and status = 1");
        Record examineData = new Record();
        if (examineCount > 0) {
            examineData = examineRecordService.saveExamineRecord(3,
                    checkUserId, oldInvoice.getOwnerUserId(),
                    oldInvoice.getExamineRecordId(), crmInvoice.getCheckStatus());
            if (examineData.getInt("status") == 0) {
                return R.error("没有启动的审核步骤，不能添加！");
            } else if (examineData.getInt("status") == 2) {
                return R.error("没有审批人，不能添加！");
            } else {
                crmInvoice.setExamineRecordId(examineData.getInt("recordId"));
            }
            crmInvoice.setCheckStatus(0);
        } else {
            if (crmInvoice.getCheckStatus() == null) {
                crmInvoice.setCheckStatus(1);
            }
            if (oldInvoice.getExamineRecordId() != null) {
                Db.update("update 72crm_admin_examine_log set is_recheck = 1 where record_id = ?", oldInvoice.getExamineRecordId());
            }
        }
        actionRecordUtil.updateRecord(oldInvoice, crmInvoice, CrmEnum.CRM_INVOICE);
        crmInvoice.setUpdateTime(new Date()).update();
        addExamineNotice(crmInvoice, examineData);
        return R.ok();
    }

    private void addExamineNotice(CrmInvoice crmInvoice, Record examineData) {
        //判断当前是否提交审核，提交审核需要生成一条系统通知
        if (ObjectUtil.equal(crmInvoice.getCheckStatus(), 0)) {
            List<Long> examineLogIdList = examineData.get("examineLogIds");
            examineLogIdList.forEach(examineLogId -> {
                AdminExamineLog examineLog = AdminExamineLog.dao.findById(examineLogId);
                Aop.get(AdminMessageService.class).addMessage(AdminMessageEnum.CRM_INVOICE_EXAMINE, examineLog, BaseUtil.getUserId());
            });
        }
    }

    public R updateInvoiceStatus(CrmInvoice crmInvoice) {
        Db.update(Db.getSql("crm.invoice.updateInvoiceStatus"), crmInvoice.getInvoiceNumber(), crmInvoice.getRealInvoiceDate(), crmInvoice.getLogisticsNumber(), crmInvoice.getInvoiceId());
        return R.ok();
    }

    public Record queryById(Integer invoiceId) {
        return Db.findFirst(Db.getSql("crm.invoice.queryById"), invoiceId);
    }

    @Before(Tx.class)
    public R deleteById(String invoiceIds) {
        String[] idsArr = invoiceIds.split(",");
        for (String id : idsArr) {
            CrmInvoice crmInvoice = CrmInvoice.dao.findById(id);
            boolean bol = (crmInvoice.getCheckStatus() != 4 && crmInvoice.getCheckStatus() != 5) && (!BaseUtil.getUserId().equals(BaseUtil.getSuperUser()) && !BaseUtil.getUser().getRoles().contains(BaseUtil.getSuperRole()));
            if (bol) {
                return R.noAuth();
            }
            Aop.get(AdminFileService.class).removeByBatchId(crmInvoice.getBatchId());
            crmInvoice.delete();
        }
        return R.ok();
    }

    public R transfer(String invoiceIds, Long ownerUserId) {
        Db.template("crm.invoice.transfer", Kv.by("ids", invoiceIds).set("ownerUserId", ownerUserId)).update();
        for (String id : invoiceIds.split(",")) {
            actionRecordUtil.addConversionRecord(Integer.valueOf(id), CrmEnum.CRM_INVOICE, ownerUserId);
        }
        return R.ok();
    }

    public R queryFileList(Integer invoiceId) {
        return R.ok().put("data", Db.template("crm.invoice.queryFileList", Kv.by("invoiceId", invoiceId)).find());
    }

    public R saveInvoiceInfo(CrmInvoiceInfo crmInvoiceInfo) {
        crmInvoiceInfo.setCreateUserId(BaseUtil.getUserId()).setCreateTime(new Date()).save();
        return R.ok();
    }

    public R updateInvoiceInfo(CrmInvoiceInfo crmInvoiceInfo) {
        crmInvoiceInfo.update();
        return R.ok();
    }

    public R deleteInvoiceInfo(Integer infoId) {
        CrmInvoiceInfo.dao.deleteById(infoId);
        return R.ok();
    }
}
