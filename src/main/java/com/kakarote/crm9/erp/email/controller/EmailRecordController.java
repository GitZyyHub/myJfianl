package com.kakarote.crm9.erp.email.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.RequestBody;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.email.entity.EmailFile;
import com.kakarote.crm9.erp.email.entity.EmailRecord;
import com.kakarote.crm9.erp.email.service.EmailRecordService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.util.List;

/**
 * @author fff 邮件操作记录
 */
public class EmailRecordController extends Controller {

    @Inject
    private EmailRecordService emailRecordService;

    /**
     * @author 获取邮件数量 fff
     */
    public void queryEmailNumber() {
        renderJson(emailRecordService.queryEmailNumber());
    }

    /**
     * 查询邮箱邮件列表
     *
     * @author fff
     */
    public void queryEmailList(BasePageRequest basePageRequest) {
        renderJson(emailRecordService.queryEmailList(basePageRequest));
    }

    /**
     * 发送邮件
     *
     * @param emailRecord e
     * @author fff
     */
    public void sendEmail(@RequestBody EmailRecord emailRecord) {
        renderJson(emailRecordService.sendEmails(emailRecord));
    }

    /**
     * 保存为草稿
     *
     * @param emailRecord emailRecord
     * @author fff
     */
    public void saveDraftsBox(@RequestBody EmailRecord emailRecord) {
        renderJson(emailRecordService.saveDraftsBox(emailRecord));
    }

    /**
     * 移动邮件
     *
     * @param emailIds  ids
     * @param emailType INBOX :收件箱 Sent Messages：发件箱 other：其他文件夹
     */
    @NotNullValidate(value = "emailIds")
    @NotNullValidate(value = "emailType", message = "邮箱类型不可为空")
    public void shiftEmail(@Para("emailIds") String emailIds, @Para("emailType") String emailType) {
        renderJson(emailRecordService.shiftEmail(emailIds, emailType));
    }

    /**
     * 更改邮件状态
     *
     * @param emailIds 邮件id
     * @param state    状态 1：已读 2：未读 3：标星 4：取消标星
     */
    @NotNullValidate(value = "emailIds")
    @NotNullValidate(value = "state")
    public void changeEmailState(@Para("emailIds") String emailIds, @Para("state") Integer state) {
        renderJson(emailRecordService.changeEmailState(emailIds, state));
    }

    /**
     * 逻辑删除
     *
     * @param emailIds 邮件id
     */
    @NotNullValidate(value = "emailIds")
    public void logicDelete(@Para("emailIds") String emailIds) {
        renderJson(emailRecordService.logicDelete(emailIds));
    }

    /**
     * 彻底删除
     *
     * @param emailIds 邮件id集合
     */
    @NotNullValidate(value = "emailIds")
    public void deleteByEmailId(@Para("emailIds") String emailIds) {
        renderJson(emailRecordService.deleteByEmailId(emailIds));
    }

    /**
     * 手动收信
     */
    public void receivingEmail() {
        renderJson(emailRecordService.receivingEmail());
    }

    /**
     * 查询邮件详细信息
     *
     * @param id
     */
    public void queryEmailById(@Para("id") String id) {
        renderJson(emailRecordService.queryEmailById(id));
    }


    public void upload() {
        String prefix = "mail/" + BaseUtil.getDate() + "/";
        renderJson(emailRecordService.upload(getFile("file", prefix), getPara("batchId"), getPara("type"), prefix));
    }

    @NotNullValidate(value = "batchId")
    public void getEmailFileByBatchId(@Para("batchId") String batchId) {
        List<EmailFile> emailFiles = emailRecordService.queryEmailFileByBatchId(batchId);
        renderJson(R.ok().put("data",emailFiles));
    }

    public void downFile(@Para("id") String id, @Para("recordId") String recordId) {
        R r = emailRecordService.downEmailFile(id, recordId, getResponse());
        if(r!=null){
            renderJson(r);
        }else {
            renderNull();
        }
    }

}
