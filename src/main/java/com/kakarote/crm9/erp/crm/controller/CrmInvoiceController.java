package com.kakarote.crm9.erp.crm.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.annotation.RequestBody;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.crm.entity.CrmInvoice;
import com.kakarote.crm9.erp.crm.entity.CrmInvoiceInfo;
import com.kakarote.crm9.erp.crm.service.CrmInvoiceService;
import com.kakarote.crm9.utils.R;

/**
 * @author wyq
 */
public class CrmInvoiceController extends Controller {
    @Inject
    private CrmInvoiceService crmInvoiceService;

    /**
     * 查询列表页
     */
    @Permissions({"crm:invoice:index"})
    public void queryPageList(BasePageRequest basePageRequest) {
        renderJson(crmInvoiceService.queryPageList(basePageRequest));
    }

    /**
     * 新建
     */
    @Permissions({"crm:invoice:save"})
    public void save(@RequestBody CrmInvoice crmInvoice, @RequestBody Long checkUserId) {
        renderJson(crmInvoiceService.save(crmInvoice, checkUserId));
    }

    /**
     * 编辑
     */
    @Permissions({"crm:invoice:update"})
    public void update(@RequestBody CrmInvoice crmInvoice, @RequestBody Long checkUserId) {
        renderJson(crmInvoiceService.update(crmInvoice, checkUserId));
    }

    /**
     * 修改开票状态
     */
    @Permissions({"crm:invoice:updateInvoiceStatus"})
    public void updateInvoiceStatus(@Para("") CrmInvoice crmInvoice) {
        renderJson(crmInvoiceService.updateInvoiceStatus(crmInvoice));
    }

    /**
     * 重置开票状态
     */
    @Permissions({"crm:invoice:resetInvoiceStatus"})
    public void resetInvoiceStatus(@Para("") CrmInvoice crmInvoice) {
        renderJson(crmInvoiceService.updateInvoiceStatus(crmInvoice));
    }

    /**
     * 查询详情
     */
    @Permissions({"crm:invoice:read"})
    public void queryById(@Para("invoiceId") Integer invoiceId) {
        renderJson(R.ok().put("data", crmInvoiceService.queryById(invoiceId)));
    }

    /**
     * 删除
     */
    @Permissions({"crm:invoice:delete"})
    public void deleteById(@Para("invoiceIds") String invoiceIds) {
        renderJson(crmInvoiceService.deleteById(invoiceIds));
    }

    /**
     * 转移
     */
    @Permissions({"crm:invoice:transfer"})
    public void transfer(@Para("invoiceIds") String invoiceIds, @Para("newOwnerUserId") Long ownerUserId) {
        renderJson(crmInvoiceService.transfer(invoiceIds, ownerUserId));
    }

    /**
     * 查询附件列表
     */
    public void queryFileList(@Para("invoiceId") Integer invoiceId) {
        renderJson(crmInvoiceService.queryFileList(invoiceId));
    }

    /**
     * 新建发票信息
     */
    public void saveInvoiceInfo(@Para("") CrmInvoiceInfo crmInvoiceInfo) {
        renderJson(crmInvoiceService.saveInvoiceInfo(crmInvoiceInfo));
    }

    /**
     * 编辑发票信息
     */
    public void updateInvoiceInfo(@Para("") CrmInvoiceInfo crmInvoiceInfo) {
        renderJson(crmInvoiceService.updateInvoiceInfo(crmInvoiceInfo));
    }

    /**
     * 删除发票信息
     */
    public void deleteInvoiceInfo(@Para("infoId") Integer infoId) {
        renderJson(crmInvoiceService.deleteInvoiceInfo(infoId));
    }
}
