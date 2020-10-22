package com.kakarote.crm9.erp.email.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.email.common.Pop3EmailTask;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.sun.mail.util.MailConnectException;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author fff  邮箱
 */
public class EmailAccountService {

    /**
     * 验证当前 账户是否已经绑定邮箱
     *
     * @return R
     */
    public R validationAccount() {
        EmailAccount emailAccount = EmailAccount.get();
        return R.ok().put("data", emailAccount);
    }

    /**
     * 绑定或者设置邮箱
     *
     * @param emailAccount 邮箱账户
     * @return R
     */
    public R saveOrUpdateEmailAccount(EmailAccount emailAccount) {
        if (Objects.isNull(emailAccount)) {
            return R.error("添加信息不可为空");
        }

        EmailAccount account = EmailAccount.dao.findFirst("SELECT * FROM `72crm_email_account` where create_user_id=?", BaseUtil.getUserId());

        if (account != null) {
            return R.error("当前用户已绑定邮箱");
        }
        if (Objects.equals(1, emailAccount.getConfigurationMode()) && StrUtil.isEmpty(emailAccount.getReceivingServer())) {
            emailAccount.checkEmailConfig();
        }

        emailAccount.setCreateTime(DateUtil.date());
        emailAccount.setUpdateTime(DateUtil.date());
        emailAccount.setServiceType("IMAP");
        //暂时没保存总数量
        emailAccount.setEmailCount(0);
        emailAccount.setCreateUserId(BaseUtil.getUserId());
        Properties props = new Properties();
        //TODO 暂时只处理imap
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", emailAccount.getReceivingServer());
        props.setProperty("mail.imap.port", emailAccount.getReceivingSsl());//根据邮件服务器情况设定
        props.setProperty("mail.imap.ssl.enable", emailAccount.getReceivingSsl().equals("993")?"true":"false");
        props.setProperty("mail.imap.partialfetch", "false");
        props.setProperty("mail.imaps.partialfetch", "false");
        props.put("mail.imap.auth", "true");
        Session session = Session.getInstance(props);
        try (Store store = session.getStore("imap")) {
            store.connect(emailAccount.getEmailAccount(), emailAccount.getEmailPassword());  //密码需要邮箱授权的验证码
        } catch (MailConnectException e) {
            return R.error("未找到对应邮箱服务器，请检查域名或手动配置");
        } catch (AuthenticationFailedException e) {
            return R.error("账号或密码错误，请检查");
        } catch (MessagingException e) {
            return R.error("配置出错，请稍后再试");
        }

        emailAccount.save();
        EmailAccount.set(emailAccount);
        Aop.get(EmailRecordService.class).receivingEmail();
        Pop3EmailTask.ME.addTask(emailAccount);
        EmailAccount.remove();
        return R.ok();
    }

    /**
     * 查询当前邮箱详细信息
     */
    public R queryAccountById() {
        EmailAccount emailAccount = EmailAccount.get();
        return R.ok().put("data", emailAccount);
    }

    /**
     * 解绑邮箱
     *
     * @return R
     */
    @Before(Tx.class)
    public R unbundleEmailAccount() {
        EmailAccount emailAccount = EmailAccount.get();
        //删除记录
        Db.delete("delete from 72crm_email_record where create_user_id=? ", emailAccount.getCreateUserId());
        //删除附件记录
        Db.delete("delete from 72crm_email_file where create_user_id=? ", emailAccount.getCreateUserId());
        //删除联系人
        Db.delete("delete from 72crm_email_lately where create_user_id=? ", emailAccount.getCreateUserId());
        emailAccount.delete();
        Pop3EmailTask.remove(emailAccount.getCreateUserId());
        return R.ok();
    }

    /**
     * 编辑个性签名
     *
     * @param signature 个性签名
     * @return R r
     */
    public R updateSignature(String signature) {
        if (StrUtil.isEmpty(signature)) {
            return R.error("签名不可为空");
        }
        EmailAccount emailAccount = EmailAccount.get();
        emailAccount.setSignature(signature);
        emailAccount.update();
        return R.ok();
    }

    /**
     * 编辑个性签名
     *
     * @param sendNick 个性签名
     * @return R r
     */
    public R updateSendNick(String sendNick) {

        if (StrUtil.isEmpty(sendNick)) {
            return R.error("昵称不可为空");
        }
        EmailAccount emailAccount = EmailAccount.get();
        emailAccount.setSendNick(sendNick);
        emailAccount.update();
        return R.ok();

    }

    /**
     * 快速搜索邮箱
     */
    public R searchEmail(String email) {
        List<Record> recordList = Db.find(Db.getSqlPara("email.account.selectEmail", Kv.by("email", email)));
        return R.ok().put("data", recordList);
    }

    /**
     * 查询最近使用的联系人
     *
     * @return R
     */
    public R queryLately(BasePageRequest basePageRequest) {
        Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("email.record.queryLately", Kv.by("userId", BaseUtil.getUserId())));
        return R.ok().put("data", page);
    }

}
