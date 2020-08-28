package com.kakarote.crm9.erp.email.controller;

import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.annotation.RequestBody;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.email.common.EmailInterceptor;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.erp.email.service.EmailAccountService;

/**
 * @author fff  邮箱
 */
public class EmailAccountController extends Controller {

    @Inject
    private EmailAccountService emailAccountService;

    /**
     * 验证当前 账户是否已经绑定邮箱
     * @return
     */
    public void validationAccount(){
        renderJson(emailAccountService.validationAccount());
    }

    /**
     * 绑定邮箱
     * @param emailAccount
     */
    @Clear(EmailInterceptor.class)
    public void saveOrUpdateEmailAccount(@RequestBody EmailAccount emailAccount){
        renderJson(emailAccountService.saveOrUpdateEmailAccount(emailAccount));
    }

    /**
     * 查询当前邮箱详细信息
     */
    public void queryAccountById(){
        renderJson(emailAccountService.queryAccountById());
    }

    /**
     * 解绑邮箱
     */
    public void unbundleEmailAccount(){
        renderJson(emailAccountService.unbundleEmailAccount());
    }

    /**
     * 编辑个性签名
     */
    public void updateSignature(@Para("signature") String signature){
        renderJson(emailAccountService.updateSignature(signature));
    }

    /**
     * 修改昵称
     * @param sendNick
     */
    public void updateSendNick(@Para("sendNick") String sendNick){
        renderJson(emailAccountService.updateSendNick(sendNick));
    }

    /***
     * 快速检索邮箱
     * @param email
     */
    public void searchEmail(@Para("email") String email){
        renderJson(emailAccountService.searchEmail(email));
    }

    /**
     * 查询最近用的联系人
     */
    public void queryLately(BasePageRequest basePageRequest){
        renderJson(emailAccountService.queryLately(basePageRequest));
    }
}
