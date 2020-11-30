package com.kakarote.crm9.erp.admin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.common.AdminMessageEnum;
import com.kakarote.crm9.erp.admin.entity.AdminExamineLog;
import com.kakarote.crm9.erp.admin.entity.AdminExamineRecord;
import com.kakarote.crm9.erp.admin.entity.AdminMessage;
import com.kakarote.crm9.erp.crm.entity.*;
import com.kakarote.crm9.erp.oa.entity.*;
import com.kakarote.crm9.erp.work.entity.Task;
import com.kakarote.crm9.erp.work.entity.TaskComment;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.kakarote.crm9.utils.TagUtil;
import com.kakarote.crm9.utils.XingePushUtil;

import java.util.*;

public class AdminMessageService {
    XingePushUtil xingePushUtil = new XingePushUtil();

    public void addMessage(AdminMessageEnum messageEnum, Model model, Long userId) {
        switch (messageEnum) {
            case OA_TASK_ALLOCATION:
            case OA_TASK_JOIN:
            case OA_TASK_OVER:
                addOaTaskMessage(messageEnum, (Task) model, userId);
                break;
            case OA_LOG_SEND:
                addOaLogSendMessage(messageEnum, (OaLog) model, userId);
                break;
            case OA_LOG_REPLY:
                addOaLogReplyMessage(messageEnum, (TaskComment) model, userId);
                break;
            case OA_COMMENT_REPLY:
                addOaCommentReplyMessage(messageEnum, (TaskComment) model, userId);
                break;
            case OA_EXAMINE_REJECT:
            case OA_EXAMINE_PASS:
                addOaExamineMessage(messageEnum, (OaExamineRecord) model, userId);
                break;
            case OA_NOTICE_MESSAGE:
                addOaNoticeMessage(messageEnum, (OaAnnouncement) model, userId);
                break;
            case OA_EVENT_MESSAGE:
                addOaEventMessage(messageEnum, (OaEvent) model, userId);
                break;
            case CRM_CONTRACT_PASS:
            case CRM_CONTRACT_REJECT:
            case CRM_RECEIVABLES_PASS:
            case CRM_RECEIVABLES_REJECT:
            case CRM_INVOICE_PASS:
            case CRM_INVOICE_REJECT:
                addCrmExamineMessage(messageEnum, (AdminExamineRecord) model, userId);
                break;
            case CRM_BUSINESS_USER:
            case CRM_CONTRACT_USER:
            case CRM_CUSTOMER_USER:
            case CRM_BUSINESS_TEAM_EXIT:
            case CRM_CUSTOMER_TEAM_EXIT:
            case CRM_CONTRACT_TEAM_EXIT:
            case CRM_BUSINESS_REMOVE_TEAM:
            case CRM_CUSTOMER_REMOVE_TEAM:
            case CRM_CONTRACT_REMOVE_TEAM:
                addCrmTeamMessage(messageEnum, model, userId);
                break;
            case OA_EXAMINE_NOTICE:
                addOaExamineNotice(messageEnum, (OaExamineLog) model, userId);
                break;
            case CRM_CONTRACT_EXAMINE:
            case CRM_RECEIVABLES_EXAMINE:
            case CRM_INVOICE_EXAMINE:
                addCrmExamineNotice(messageEnum, (AdminExamineLog) model, userId);
                break;
            default:
                break;
        }
    }

    public Page<AdminMessage> queryList(BasePageRequest<AdminMessage> request) {
        Page<AdminMessage> page= AdminMessage.dao.template("admin.message.queryList", request.getData().put("key", new ArrayList<>()).put("userId", BaseUtil.getUserId())).paginate(request.getPage(), request.getLimit());
        if(Arrays.asList(14,16,18,20,38,39,40,41,42,43,44,45).contains(request.getData().getType())){
            page.getList().forEach(data->{
                data.setContent(data.getContent().split(",")[0]);
            });
        }
        return page;
    }

    public R queryUnreadCount() {
        return R.ok().put("data", Db.template("admin.message.queryUnreadCount", Kv.by("userId", BaseUtil.getUserId())).findFirst());
    }

    /**
     * @author wyq
     * 批量标记为已读
     */
    public R readMessage(Long messageId) {
        Db.update(Db.getSql("admin.message.readMessage"), messageId);
        return R.ok();
    }

    /**
     * @author wyq
     * 全部标记为已读
     */
    public R readAllMessage(Integer label) {
        Db.template("admin.message.readAllMessage", Kv.by("userId", BaseUtil.getUserId()).set("label", label)).update();
        return R.ok();
    }

    /**
     * 添加任务消息
     *
     * @author zhangzhiwei
     */
    private void addOaTaskMessage(AdminMessageEnum messageEnum, Task taskOld, Long userId) {
        Set<Long> ownerUserId = new HashSet<>();
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        Task task = Task.dao.findById(taskOld.getTaskId());
        if (!messageEnum.equals(AdminMessageEnum.OA_TASK_JOIN) && task.getMainUserId() != null) {
            ownerUserId.add(task.getMainUserId());
        }
        if (!messageEnum.equals(AdminMessageEnum.OA_TASK_ALLOCATION)) {
            ownerUserId.addAll(TagUtil.toLongSet(task.getOwnerUserId()));
        }
        List<AdminMessage> messageList = new ArrayList<>();
        ownerUserId.forEach(id -> {
            if (userId.equals(id)) {
                return;
            }
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            //adminMessage.setTitle(task.getName());
            adminMessage.setRecipientUser(id);
            //adminMessage.setTypeId(task.getTaskId());
            messageList.add(adminMessage);
            String content = "";
            if (messageEnum.equals(AdminMessageEnum.OA_TASK_ALLOCATION)){
                content = realname + "将《" + adminMessage.getTitle() + "》任务分配给您，请及时查看";
            }else if (messageEnum.equals(AdminMessageEnum.OA_TASK_JOIN)){
                content = realname + "邀请您参与《" + adminMessage.getTitle() + "》任务，请及时查看";
            }else if (messageEnum.equals(AdminMessageEnum.OA_TASK_OVER)){
                content = realname + "将《" + adminMessage.getTitle() + "》任务标记结束";
            }
            xingePushUtil.sendMessage("消息提醒", content, id);
        });
        Db.batchSave(messageList, 100);
    }

    /**
     * 添加日志发送消息
     *
     * @author zhangzhiwei
     */
    private void addOaLogSendMessage(AdminMessageEnum messageEnum, OaLog log, Long userId) {
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        Set<Long> integerSet = TagUtil.toLongSet(log.getSendUserIds());
        List<String> deptIds = StrUtil.split(log.getSendDeptIds(), ",", 0, true, true);
        if (deptIds.size() > 0) {
            integerSet.addAll(Db.template("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIds)).query());
        }
        List<AdminMessage> messageList = new ArrayList<>();
        integerSet.forEach(id -> {
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            adminMessage.setTitle(DateUtil.today());
            adminMessage.setRecipientUser(id);
            adminMessage.setTypeId(log.getLogId());
            messageList.add(adminMessage);
            String content = realname + "将日志《" + adminMessage.getTitle() + "》发送给您，请及时查看";
            xingePushUtil.sendMessage("消息提醒", content, id);
        });
        Db.batchSave(messageList, 100);
    }

    /**
     * 添加日志回复消息，暂未添加针对评论的回复
     *
     * @author zhangzhiwei
     */
    private void addOaLogReplyMessage(AdminMessageEnum messageEnum, TaskComment taskComment, Long userId) {
        OaLog log = OaLog.dao.findById(taskComment.getTypeId());
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setContent(taskComment.getContent());
        adminMessage.setTitle(DateUtil.formatDate(log.getCreateTime()));
        adminMessage.setRecipientUser(log.getCreateUserId());
        adminMessage.setTypeId(log.getLogId());
        adminMessage.save();
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        String content = realname + "回复了您的《" + adminMessage.getTitle() + "》日志，请及时查看";
        xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
    }

    /**
     * 评论回复提醒
     */
    private void addOaCommentReplyMessage(AdminMessageEnum messageEnum, TaskComment taskComment, Long userId) {
        OaLog log = OaLog.dao.findById(taskComment.getTypeId());
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setContent(taskComment.getContent());
        adminMessage.setTitle(DateUtil.formatDate(log.getCreateTime()));
        adminMessage.setRecipientUser(taskComment.getPid());
        adminMessage.setTypeId(log.getLogId());
        adminMessage.save();
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        String content = realname + "回复了您评论的日志《" + adminMessage.getTitle() + "》，请及时查看";
        xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
    }

    /**
     * 添加OA审批消息
     *
     * @author zhangzhiwei
     */
    private void addOaExamineMessage(AdminMessageEnum messageEnum, OaExamineRecord examineRecord, Long userId) {
        OaExamine examine = OaExamine.dao.findById(examineRecord.getExamineId());
        OaExamineCategory category = new OaExamineCategory().findById(examine.getCategoryId());
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setContent(examineRecord.getRemarks());
        adminMessage.setTitle(category.getTitle());
        adminMessage.setRecipientUser(examine.getCreateUserId());
        adminMessage.setTypeId(examine.getExamineId());
        adminMessage.save();
        String content = "";
        if (messageEnum.equals(AdminMessageEnum.OA_EXAMINE_REJECT)){
            String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
            content = realname + "拒绝您的《" + adminMessage.getTitle() + "》，请及时处理";
        }else if(messageEnum.equals(AdminMessageEnum.OA_EXAMINE_PASS)){
            content = "您的《" + adminMessage.getTitle() + "》已经审核通过，请及时查看";
        }
        xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
    }

    /**
     * 添加oa待审批提醒
     *
     * @author wyq
     */
    private void addOaExamineNotice(AdminMessageEnum messageEnum, OaExamineLog oaExamineLog, Long userId) {
        OaExamineRecord oaExamineRecord = OaExamineRecord.dao.findById(oaExamineLog.getRecordId());
        OaExamine examine = OaExamine.dao.findById(oaExamineRecord.getExamineId());
        OaExamineCategory category = new OaExamineCategory().findById(examine.getCategoryId());
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setTitle(category.getTitle());
        adminMessage.setRecipientUser(oaExamineLog.getExamineUser());
        adminMessage.setTypeId(examine.getExamineId());
        adminMessage.save();
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        String content = realname + "提交了《" + adminMessage.getTitle() + "》，请及时处理";
        xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
    }

    /**
     * 添加OA公告消息
     *
     * @author zhangzhiwei
     */
    private void addOaNoticeMessage(AdminMessageEnum messageEnum, OaAnnouncement announcement, Long userId) {
        Set<Long> userIds = TagUtil.toLongSet(announcement.getOwnerUserIds());
        userIds.add(BaseUtil.getUserId());
        List<String> deptIds = StrUtil.split(announcement.getDeptIds(), ",", 0, true, true);
        if (deptIds.size() > 0) {
            userIds.addAll(Db.template("admin.user.queryUserIdByDeptId", Kv.by("deptIds", deptIds)).query());
        }
        if (StrUtil.isAllEmpty(announcement.getOwnerUserIds(), announcement.getDeptIds())) {
            userIds.addAll(Db.query("SELECT user_id FROM `72crm_admin_user`"));
        }
        List<AdminMessage> messageList = new ArrayList<>();
        userIds.forEach(id -> {
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            adminMessage.setTitle(announcement.getTitle());
            adminMessage.setRecipientUser(id);
            adminMessage.setTypeId(announcement.getAnnouncementId());
            messageList.add(adminMessage);
            String content = "您有一个新公告《" + adminMessage.getTitle() + "》，请及时查看";
            xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
        });
        Db.batchSave(messageList, 200);
    }

    /**
     * 添加OA日程消息
     *
     * @author zhangzhiwei
     */
    private void addOaEventMessage(AdminMessageEnum messageEnum, OaEvent event, Long userId) {
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        List<String> userIds = StrUtil.split(event.getOwnerUserIds(), ",", 0, true, true);
        List<AdminMessage> messageList = new ArrayList<>();
        userIds.forEach(id -> {
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            adminMessage.setTitle(event.getTitle());
            adminMessage.setRecipientUser(Long.valueOf(id));
            adminMessage.setTypeId(event.getEventId());
            messageList.add(adminMessage);
            String content = realname + "邀请您参与《" + adminMessage.getTitle() + "》的日程，请及时查看";
            xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
        });
        Db.batchSave(messageList, 200);
    }

    /**
     * 添加CRM审批消息
     *
     * @author zhangzhiwei
     */
    private void addCrmExamineMessage(AdminMessageEnum messageEnum, AdminExamineRecord examineRecord, Long userId) {
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setContent(examineRecord.getRemarks());
        if (Arrays.asList(AdminMessageEnum.CRM_CONTRACT_PASS, AdminMessageEnum.CRM_CONTRACT_REJECT).contains(messageEnum)) {
            CrmContract crmContract = CrmContract.dao.findFirst(Db.getSql("crm.contract.queryContractByRecordId"), examineRecord.getRecordId());
            adminMessage.setTitle(crmContract.getName());
            adminMessage.setRecipientUser(crmContract.getOwnerUserId());
            adminMessage.setTypeId(crmContract.getContractId());
        } else if (Arrays.asList(AdminMessageEnum.CRM_RECEIVABLES_PASS, AdminMessageEnum.CRM_RECEIVABLES_REJECT).contains(messageEnum)) {
            CrmReceivables receivables = CrmReceivables.dao.findFirst(Db.getSql("crm.receivables.queryReceivablesByRecordId"), examineRecord.getRecordId());
            adminMessage.setTitle(receivables.getNumber());
            adminMessage.setRecipientUser(receivables.getOwnerUserId());
            adminMessage.setTypeId(receivables.getReceivablesId());
        } else {
            CrmInvoice crmInvoice = CrmInvoice.dao.findFirst(Db.getSql("crm.invoice.queryInvoiceByRecordId"), examineRecord.getRecordId());
            adminMessage.setTitle(crmInvoice.getInvoiceApplyNumber());
            adminMessage.setRecipientUser(crmInvoice.getOwnerUserId());
            adminMessage.setTypeId(crmInvoice.getInvoiceId());
        }
        adminMessage.save();
        String content = "";
        if (messageEnum.equals(AdminMessageEnum.CRM_CONTRACT_REJECT)){
            content = realname + "拒绝您的《" + adminMessage.getTitle() + "》合同审批，请及时处理";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_CONTRACT_PASS)){
            content = realname + "您的《" + adminMessage.getTitle() + "》合同已经审核通过，请及时查看";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_RECEIVABLES_REJECT)){
            content = realname + "拒绝您的《" + adminMessage.getTitle() + "》回款审批，请及时处理";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_RECEIVABLES_PASS)){
            content = realname + "您的《" + adminMessage.getTitle() + "》回款已经审核通过，请及时查看";
        } else if (messageEnum.equals(AdminMessageEnum.CRM_INVOICE_REJECT)) {
            content = realname + "拒绝您的《" + adminMessage.getTitle() + "》发票审批，请及时查看";
        } else if (messageEnum.equals(AdminMessageEnum.CRM_INVOICE_PASS)) {
            content = realname + "您的《" + adminMessage.getTitle() + "》发票已经审核通过，请及时查看";
        }
        xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
    }

    private void addCrmExamineNotice(AdminMessageEnum messageEnum, AdminExamineLog examineLog, Long userId) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        AdminExamineRecord examineRecord = AdminExamineRecord.dao.findById(examineLog.getRecordId());
        if (messageEnum.equals(AdminMessageEnum.CRM_CONTRACT_EXAMINE)) {
            CrmContract crmContract = CrmContract.dao.findFirst(Db.getSql("crm.contract.queryContractByRecordId"), examineRecord.getRecordId());
            adminMessage.setTitle(crmContract.getName());
            adminMessage.setRecipientUser(examineLog.getExamineUser());
            adminMessage.setTypeId(crmContract.getContractId());
        } else if (messageEnum.equals(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE)) {
            CrmReceivables receivables = CrmReceivables.dao.findFirst(Db.getSql("crm.receivables.queryReceivablesByRecordId"), examineRecord.getRecordId());
            adminMessage.setTitle(receivables.getNumber());
            adminMessage.setRecipientUser(examineLog.getExamineUser());
            adminMessage.setTypeId(receivables.getReceivablesId());
        } else {
            CrmInvoice crmInvoice = CrmInvoice.dao.findFirst(Db.getSql("crm.invoice.queryInvoiceByRecordId"), examineRecord.getRecordId());
            adminMessage.setTitle(crmInvoice.getInvoiceApplyNumber());
            adminMessage.setRecipientUser(examineLog.getExamineUser());
            adminMessage.setTypeId(crmInvoice.getInvoiceId());
        }
        adminMessage.save();
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        String content = "";
        if (messageEnum.equals(AdminMessageEnum.CRM_CONTRACT_EXAMINE)){
            content = realname + "提交了《" + adminMessage.getTitle() + "》合同审批，请及时处理";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE)){
            content = realname + "提交了《" + adminMessage.getTitle() + "》回款审批，请及时处理";
        } else if (messageEnum.equals(AdminMessageEnum.CRM_INVOICE_EXAMINE)) {
            content = realname + "提交了《" + adminMessage.getTitle() + "》发票审批，请及时处理";
        }
        xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
    }

    /**
     * 团队成员提醒
     */
    private void addCrmTeamMessage(AdminMessageEnum messageEnum, Model model, Long userId) {
        String realname = Db.queryStr("select realname from 72crm_admin_user where user_id = ?",userId);
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(BaseUtil.getUserId());
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setRecipientUser(userId);
        adminMessage.setTypeId(model.getInt(TableMapping.me().getTable(model.getClass()).getPrimaryKey()[0]));
        if (model instanceof CrmCustomer) {
            adminMessage.setTitle(((CrmCustomer) model).getCustomerName());
        } else if (model instanceof CrmContract) {
            adminMessage.setTitle(((CrmContract) model).getName());
        } else if (model instanceof CrmBusiness) {
            adminMessage.setTitle(((CrmBusiness) model).getBusinessName());
        }
        adminMessage.save();
        String content = "";
        if (messageEnum.equals(AdminMessageEnum.CRM_BUSINESS_USER)){
            content = realname + "将您添加为商机《" + adminMessage.getTitle() + "》的成员";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_CUSTOMER_USER)){
            content = realname + "将您添加为客户《" + adminMessage.getTitle() + "》的成员";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_CONTRACT_USER)){
            content = realname + "将您添加为合同《" + adminMessage.getTitle() + "》的成员";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_BUSINESS_TEAM_EXIT)){
            content = realname + "退出了您商机《" + adminMessage.getTitle() + "》的团队";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_CUSTOMER_TEAM_EXIT)){
            content = realname + "退出了您客户《" + adminMessage.getTitle() + "》的团队";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_CONTRACT_TEAM_EXIT)){
            content = realname + "退出了您合同《" + adminMessage.getTitle() + "》的团队";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_BUSINESS_REMOVE_TEAM)){
            content = realname + "将您移出了商机《" + adminMessage.getTitle() + "》的团队";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_CUSTOMER_REMOVE_TEAM)){
            content = realname + "将您移出了客户《" + adminMessage.getTitle() + "》的团队";
        }else if (messageEnum.equals(AdminMessageEnum.CRM_CONTRACT_REMOVE_TEAM)){
            content = realname + "将您移出了合同《" + adminMessage.getTitle() + "》的团队";
        }
        xingePushUtil.sendMessage("消息提醒", content, adminMessage.getRecipientUser());
    }

    /**
     * 清空通知
     *
     * @author wyq
     */
    public R clear(Integer label) {
        Db.template("admin.message.clear", Kv.by("label", label).set("userId", BaseUtil.getUserId())).delete();
        return R.ok();
    }

    /**
     * 删除通知
     *
     * @author wyq
     */
    public R deleteById(Integer messageId) {
        return AdminMessage.dao.deleteById(messageId) ? R.ok() : R.error();
    }


}
