package com.kakarote.crm9.erp.email.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.config.redis.RedisManager;
import com.kakarote.crm9.erp.admin.entity.AdminFile;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.email.common.EmailEnum;
import com.kakarote.crm9.erp.email.common.EmailTask;
import com.kakarote.crm9.erp.email.common.EmailUtil;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.erp.email.entity.EmailFile;
import com.kakarote.crm9.erp.email.entity.EmailLately;
import com.kakarote.crm9.erp.email.entity.EmailRecord;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author fff 邮件操作记录
 */
public class EmailRecordService {

    /**
     * @return r
     * @author zhangzhiwei
     * 查询邮箱里面的数量
     */
    public R queryEmailNumber() {
        Record data = Db.template("email.account.queryEmailNumber", Kv.by("userId", BaseUtil.getUserId())).findFirst();
        return R.ok().put("data", data);
    }

    /**
     * 发送邮件
     */
    @Before(Tx.class)
    public R sendEmails(EmailRecord emailRecord) {
        if (emailRecord == null) {
            return R.error("操作有误");
        }
        //邮件附件
        String batchId = emailRecord.getBatchId();
        //查询邮箱信息
        EmailAccount emailAccount = EmailAccount.get();

        emailRecord.setEmailAccountId(emailAccount.getId());
        if (emailAccount.getSendNick() != null) {
            emailRecord.setSender(emailAccount.getSendNick());
        } else {
            emailRecord.setSender(emailAccount.getEmailAccount());
        }

        emailRecord.setSenderEmail(emailAccount.getEmailAccount());

        List<String> ccMails = new ArrayList<>();
        //抄送人
        JSONArray jsonArray = JSONArray.parseArray(emailRecord.getCcEmails());
        int t = jsonArray.size();

        jsonArray.forEach(obj -> {
            JSONObject object = (JSONObject) obj;
            String email = object.getString("email");
            ccMails.add(email);
            saveEmailLately(email, object.getString("name"));
        });
        emailRecord.setCcEmails(String.join(",", ccMails));
        //普通收件
        List<String> emails = new ArrayList<>();
        JSONArray mailArray = JSONArray.parseArray(emailRecord.getReceiptEmails());
        t += mailArray.size();
        if (t > 40) {
            return R.error("单次最多给40人发送邮件");
        }
        mailArray.forEach(obj -> {
            JSONObject object = (JSONObject) obj;
            String email = object.getString("email");
            saveEmailLately(email, object.getString("name"));
            emails.add(email);
        });
        emailRecord.setReceiptEmails(String.join(",", emails));
        //附件
        List<File> fileList = new ArrayList<>();
        List<Record> recordList = Db.find("SELECT * FROM `72crm_admin_file` where batch_id=?", batchId);
        for (Record record : recordList) {
            if (record != null) {
                File file = new File(record.getStr("path"));
                fileList.add(file);
            }
        }
        //2是分别发送
        String type = emailRecord.getType();

        //发送邮件
        boolean isSend = false;
        if ("2".equals(type)) {
            for (String mail : emails) {
                emailRecord.setReceiptEmails(mail);
                boolean b = EmailUtil.sendEmail(emailAccount, emailRecord, fileList);
                if (b && !isSend) {
                    isSend = true;
                }
            }
        } else {
            isSend = EmailUtil.sendEmail(emailAccount, emailRecord, fileList);
        }
        return R.isSuccess(isSend, "发送邮件失败，请稍候再试");
    }

    private void saveEmailLately(String email, String name) {
        Kv kv = Kv.by("userId", BaseUtil.getUserId());
        kv.set("email", email);
        EmailLately emailLately = EmailLately.dao.template("email.record.queryLatelyById", kv).findFirst();
        if (emailLately != null) {
            if (name == null) {
                name = email.split("@")[0];
            }
            emailLately.setName(name);
            emailLately.setUpdateTime(new Date());
            emailLately.update();
        } else {
            emailLately = new EmailLately();
            if (name == null) {
                name = email.split("@")[0];
            }
            emailLately.setName(name);
            emailLately.setCreateUserId(BaseUtil.getUserId());
            emailLately.setCreateTime(new Date());
            emailLately.setEmail(email);
            emailLately.save();
        }
    }

    /**
     * 保存为草稿箱
     *
     * @param emailRecord 邮箱记录
     * @return R
     */
    public R saveDraftsBox(EmailRecord emailRecord) {

        if (Objects.isNull(emailRecord)) {
            return R.error("操作有误");
        }
        //查询邮箱信息
        EmailAccount emailAccount = EmailAccount.get();
        emailRecord.setEmailAccountId(emailAccount.getId());
        emailRecord.setSender(emailAccount.getEmailAccount());
        emailRecord.setSenderEmail(emailAccount.getEmailAccount());
        emailRecord.setIsRead(EmailEnum.HAVE_READ.getTypes());
        emailRecord.setEmailType(EmailEnum.RECORD_DRAFT.getTypes());
        emailRecord.setRecordType(EmailEnum.RECORD_DRAFT.getTypes());
        emailRecord.setCreateTime(DateUtil.date());
        emailRecord.setCreateUserId(BaseUtil.getUserId());

        //抄送人
        JSONArray jsonArray = JSONArray.parseArray(emailRecord.getCcEmails());
        int t = jsonArray.size();
        String[] ccName = new String[jsonArray.size()], ccMail = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String email = object.getString("email");
            String name = object.getString("name");
            if (name == null) {
                name = email.split("@")[0];
            }
            ccName[i] = name;
            ccMail[i] = email;
        }
        ;
        emailRecord.setCcEmails(String.join(",", ccMail));
        emailRecord.setCcName(String.join(",", ccName));

        JSONArray mailArray = JSONArray.parseArray(emailRecord.getReceiptEmails());
        t += mailArray.size();
        if (t > 40) {
            return R.error("单次最多给40人发送邮件");
        }
        String[] names = new String[mailArray.size()], mails = new String[mailArray.size()];

        for (int k = 0; k < mailArray.size(); k++) {
            JSONObject object = mailArray.getJSONObject(k);
            String email = object.getString("email");
            String name = object.getString("name");
            if (StrUtil.isEmpty(name)) {
                name = email.split("@")[0];
            }
            names[k] = name;
            mails[k] = email;
        }
        emailRecord.setReceiptEmails(String.join(",", mails));
        emailRecord.setReceiptName(String.join(",", names));
        //附件
        List<File> fileList = new ArrayList<>();
        List<Record> recordList = Db.find("SELECT * FROM `72crm_admin_file` where batch_id=?", emailRecord.getBatchId());
        for (Record record : recordList) {
            if (record != null) {
                File file = new File(record.getStr("path"));
                fileList.add(file);
            }
        }

        Long uid = EmailUtil.saveTheBox(emailAccount, emailRecord, fileList);
        emailRecord.setEmailUid(uid);
        if (emailRecord.getId() != null) {
            emailRecord.update();
        } else {
            emailRecord.save();
        }
        return R.ok();
    }

    /**
     * 移动邮件
     *
     * @param emailIds  需要移动的ID列表
     * @param emailType INBOX :收件箱 Sent Messages：发件箱 other：其他文件夹
     * @return E
     */
    public R shiftEmail(String emailIds, String emailType) {
        EmailAccount emailAccount = EmailAccount.get();
        String[] ids = StrUtil.split(emailIds, ",");
        for (String id : ids) {
            //查询邮件
            EmailRecord emailRecord = EmailRecord.dao.findById(id);
            //查询不到不处理
            if (emailRecord == null) {
                continue;
            }

            if (emailRecord.getEmailType().equals(EmailEnum.RECEIPT_EMAIL.getTypes()) && emailType.equals("INBOX")) {
                return R.error("不能移动到相同的类目中");
            } else if (emailRecord.getEmailType().equals(EmailEnum.RECORD_SEND.getTypes()) && emailType.equals("Sent Messages")) {
                return R.error("不能移动到相同的类目中");
            }
            EmailEnum emailEnum = EmailEnum.valueOf(emailType, true);
            emailRecord.setEmailType(emailEnum.getTypes());
            emailRecord.update();
        }
        EmailTask.ShiftEmailTask shiftEmailTask = new EmailTask.ShiftEmailTask(emailAccount, EmailEnum.valueOf(emailType, true), ids);
        EmailUtil.addTask(shiftEmailTask);
        return R.ok();
    }

    /**
     * @param basePageRequest
     * @author 查询邮箱邮件列表
     */
    public R queryEmailList(BasePageRequest basePageRequest) {
        String type = basePageRequest.getJsonObject().getString("type");
        String email = basePageRequest.getJsonObject().getString("email");
        String read = basePageRequest.getJsonObject().getString("read");
        String search = basePageRequest.getJsonObject().getString("search");
        String emaliType = EmailEnum.valueOf(type, true).getTypes().toString();

        //邮箱
        Kv kv = new Kv();
        kv.set("sorting", "desc");
        if (StrUtil.isEmpty(type)) {
            kv.set("emailType", null);
        } else if ("STAR".equals(type)) {
            kv.set("star", 1);
        } else {
            kv.set("emailType", emaliType);
        }
        if (StrUtil.isNotEmpty(read)) {
            kv.set("read", 0);
        }
        kv.set("userId", BaseUtil.getUserId());
        kv.set("email", email);
        kv.set("search", search);

        //查询数据
        Page<Record> page = Db.paginate(basePageRequest.getPage(), basePageRequest.getLimit(), Db.getSqlPara("email.record.getPageList", kv));

        Record emailList = new Record();
        emailList.set("list", page.getList());
        emailList.set("totalRow", page.getTotalRow());
        return R.ok().put("data", emailList);
    }

    /**
     * 更改邮件状态
     *
     * @param emailIds 邮件id集合
     * @param state    状态 1：已读 2：未读 3：标星 4：取消标星
     * @return r
     */
    public R changeEmailState(String emailIds, Integer state) {

        EmailAccount emailAccount = EmailAccount.get();
        String[] ids = StrUtil.split(emailIds, ",");

        for (String id : ids) {
            //邮件
            EmailRecord emailRecord = EmailRecord.dao.findById(id);
            //查询不到不处理
            if (emailRecord == null) {
                continue;
            }
            if (state.equals(1)) {
                emailRecord.setIsRead(1);
            } else if (state.equals(2)) {//未读
                emailRecord.setIsRead(0);
            } else if (state.equals(3)) {//标星
                emailRecord.setIsStart(1);
            } else if (state.equals(4)) {//取消标星
                emailRecord.setIsStart(0);
            }
            emailRecord.update();
        }

        EmailTask.UpdateStateTask updateStateTask = new EmailTask.UpdateStateTask(emailAccount, state, ids);
        EmailUtil.addTask(updateStateTask);
        return R.ok();
    }

    /**
     * 普通删除
     *
     * @param emailIds 邮件id
     * @return r
     */
    public R logicDelete(String emailIds) {
        shiftEmail(emailIds, "Deleted Messages");
        return R.ok();
    }

    /**
     * 彻底删除
     */
    public R deleteByEmailId(String emailIds) {
        EmailAccount emailAccount = EmailAccount.get();
        //查询邮箱信息
        String[] ids = StrUtil.split(emailIds, ",");
        List<Integer> emailTypes = new ArrayList<>();
        List<Long> emailUids = new ArrayList<>();
        for (String id : ids) {
            //查询邮件
            EmailRecord emailRecord = EmailRecord.dao.findById(id);
            if (emailRecord == null) {
                continue;
            }
            emailTypes.add(emailRecord.getEmailType());
            emailUids.add(emailRecord.getEmailUid());
            emailRecord.delete();
        }
        EmailTask.DeleteEmailTask deleteEmailTask = new EmailTask.DeleteEmailTask(emailAccount, emailTypes, emailUids);
        EmailUtil.addTask(deleteEmailTask);
        return R.ok();

    }

    /**
     * 收信
     *
     * @return e
     * @author zhangzhiwei
     */
    public R receivingEmail() {
        EmailEnum[] emailEnums = new EmailEnum[]{EmailEnum.RECEIPT_EMAIL, EmailEnum.RECORD_SEND, EmailEnum.RECORD_DRAFT, EmailEnum.RECORD_DELETE, EmailEnum.RECORD_GARBAGE};
        return receivingEmail(null,null,emailEnums);
    }

    public R receivingEmail(EmailAccount emailAccount, AdminUser adminUser, EmailEnum... emailEnums) {
        if (emailAccount == null) {
            //查询邮箱信息
            emailAccount = EmailAccount.get();
        }

        for (EmailEnum anEnum : emailEnums) {
            String redisCacheKey = "EMAIL:CACHE:KEY:";
            String key = redisCacheKey.concat(anEnum.getKey()).concat(emailAccount.getCreateUserId().toString());
            Integer k = RedisManager.getRedis().get(key);
            if (k == null) {
                RedisManager.getRedis().setex(key, 3600, 1);
            } else {
                return R.error("数据正在同步中，请稍后再试");
            }
            if (adminUser == null) {
                adminUser = BaseUtil.getUser();
            }
            adminUser.put("emailAccount", emailAccount.getId());
            //使用线程单独保存到数据库
            EmailUtil.saveEmail(emailAccount, adminUser, anEnum);
        }
        return R.ok();
    }

    /**
     * 查询邮件详细信息
     */
    public R queryEmailById(String id) {
        if (id == null || "".equals(id)) {
            return R.error("条件不可为空");
        }

        EmailRecord emailRecord = EmailRecord.dao.findById(id);

        return R.ok().put("data", emailRecord);
    }

    /**
     * @param file    文件
     * @param batchId 批次ID
     */
    public R upload(UploadFile file, String batchId, String fileType, String prefix) {
        if (batchId == null || "".equals(batchId)) {
            batchId = IdUtil.simpleUUID();
        }
        AdminFile adminFile = new AdminFile();
        adminFile.setBatchId(batchId);
        adminFile.setCreateTime(new Date());
        adminFile.setCreateUserId(BaseUtil.getUser().getUserId());
        adminFile.setPath(file.getFile().getAbsolutePath());
        adminFile.setFilePath(BaseUtil.getIpAddress() + prefix + file.getFileName());
        adminFile.setName(file.getFileName());
        if (StrUtil.isNotBlank(fileType)) {
            adminFile.setFileType(fileType);
        }
        adminFile.setSize(file.getFile().length());
        return adminFile.save() ? R.ok().put("batchId", batchId).put("name", file.getFileName()).put("url", adminFile.getFilePath()).put("size", file.getFile().length() / 1000 + "KB").put("file_id", adminFile.getFileId()) : R.error();
    }

    public List<EmailFile> queryEmailFileByBatchId(String batchId) {
        return EmailFile.dao.template("email.record.queryEmailFileByBatchId", Kv.by("batchId", batchId)).find();
    }

    public R downEmailFile(String id, String recordId, HttpServletResponse response) {
        EmailFile emailFile = EmailFile.dao.findById(id);
        EmailRecord emailRecord = EmailRecord.dao.findById(recordId);
        if (emailFile != null && emailRecord != null) {
            EmailAccount emailAccount = EmailAccount.get();
            Store store = emailAccount.getEmailStore();
            try (IMAPFolder folder = emailAccount.getIMAPFolder(store, EmailEnum.valueOf(emailRecord.getEmailType()))) {
                folder.open(Folder.READ_WRITE);
                Message message = folder.getMessageByUID(emailRecord.getEmailUid());
                if (emailAccount.isSohu()) {
                    message = new MimeMessage((IMAPMessage) message);
                }
                InputStream inputStream = EmailUtil.getFile(message, emailFile.getFileName(), emailFile.getFileSize());
                if (inputStream != null) {
                    response.setContentType("application/octet-stream;charset=utf-8");
                    response.setCharacterEncoding("UTF-8");
                    //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
                    response.setHeader("Content-Disposition", "attachment;filename=" + emailFile.getFileName());
                    ServletUtil.write(response, inputStream);
                }
            } catch (Exception e) {
                Log.getLog(getClass()).error("下载邮件错误", e);
            }
        }
        return R.error("下载文件失败");
    }
}
