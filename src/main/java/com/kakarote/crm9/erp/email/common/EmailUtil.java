package com.kakarote.crm9.erp.email.common;

import cn.hutool.core.util.IdUtil;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.kakarote.crm9.common.config.redis.RedisManager;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.erp.email.entity.EmailFile;
import com.kakarote.crm9.erp.email.entity.EmailRecord;
import com.sun.mail.imap.AppendUID;
import com.sun.mail.imap.IMAPFolder;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EmailUtil {

    private static final ExecutorService threadPool = new ThreadPoolExecutor(1, 1000, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2048), new ThreadPoolExecutor.AbortPolicy());

    /**
     * 构造函数,初始化一个MimeMessage对象
     */
    private EmailUtil() {
    }


    /**
     * ANSWERED：是否已回复  DELETED：彻底删除   DRAFT：草稿
     * FLAGGED：星标 RECENT：新邮件标记  SEEN：已读
     * USER：底层系统是否支持用户自定义标记，应用程序只能检索这个属性，而不能设置这个属性。
     * 保存到草稿箱
     *
     * @param emailAccount 邮箱账号
     * @param emailRecord  邮件
     * @param fileList     附件
     * @return 邮件uid标识
     */
    public static Long saveTheBox(EmailAccount emailAccount, EmailRecord emailRecord, List<File> fileList) {
        Long uid = null;
        try {
            Store store = emailAccount.getEmailStore();
            IMAPFolder folder = emailAccount.getIMAPFolder(store, EmailEnum.RECORD_DRAFT);
            folder.open(Folder.READ_WRITE);
            MimeMessage message = null;
            MimeMessage mimeMessage = null;
            if (emailRecord.getId() != null) {
                EmailRecord record = EmailRecord.dao.findById(emailRecord.getId());
                if (record.getEmailUid() != null) {
                    mimeMessage = (MimeMessage) folder.getMessageByUID(record.getEmailUid());
                }
            }
            message = new MimeMessage(emailAccount.getSmtpSession());
            //设置邮箱属性
            message.setFrom(new InternetAddress(emailAccount.getEmailAccount(), emailAccount.getSendNick()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRecord.getReceiptEmails()));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailRecord.getCcEmails()));
            message.setSubject(emailRecord.getTheme());
            MimeMultipart mimeMultipart = new MimeMultipart();
            BodyPart bp = new MimeBodyPart();
            bp.setContent(emailRecord.getContent(), "text/html;charset=utf-8");
            mimeMultipart.addBodyPart(bp);
            //添加文件
            addFile(mimeMultipart, fileList);
            //设置为草稿
            message.setFlag(Flags.Flag.DRAFT, true);
            message.setContent(mimeMultipart);
            message.saveChanges();
            //追加到草稿箱

            AppendUID[] appendUIDS = folder.appendUIDMessages(new Message[]{message});
            if (appendUIDS.length > 0) {
                if (appendUIDS[0] != null) {
                    uid = appendUIDS[0].uid;
                }
            }
            if (mimeMessage != null) {
                mimeMessage.setFlag(Flags.Flag.DELETED, true);
            }
        } catch (Exception e) {
            Log.getLog(EmailUtil.class).error("保存草稿出错", e);
        }

        return uid;
    }


    /**
     * 发送邮件
     *
     * @param emailAccount 发件账号对象
     * @param emailRecord  邮件对象
     * @param fileList     附件列表
     */
    public static boolean sendEmail(EmailAccount emailAccount, EmailRecord emailRecord, List<File> fileList) {
        Session session = emailAccount.getSmtpSession();
        // 自定义发件人昵称
        Message message = new MimeMessage(session);
        boolean isSend = false;
        try {
            //设置请求内容
            message.setFrom(new InternetAddress(emailAccount.getEmailAccount(), emailAccount.getSendNick()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRecord.getReceiptEmails()));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailRecord.getCcEmails()));
            message.setSubject(emailRecord.getTheme());
            MimeMultipart mimeMultipart = new MimeMultipart();
            BodyPart bp = new MimeBodyPart();
            bp.setContent(emailRecord.getContent(), "text/html;charset=utf-8");
            mimeMultipart.addBodyPart(bp);
            //添加文件
            addFile(mimeMultipart, fileList);
            message.setContent(mimeMultipart);
            message.saveChanges();
            //获取邮差对象发送
            Transport transport = session.getTransport();
            transport.connect(emailAccount.getEmailAccount(), emailAccount.getEmailPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            isSend = true;
            if (emailRecord.getId() != null) {
                EmailRecord email = EmailRecord.dao.findById(emailRecord.getId());
                if (email != null) {
                    email.delete();
                    IMAPFolder folder = emailAccount.getIMAPFolder(emailAccount.getEmailStore(), EmailEnum.RECORD_DRAFT);
                    folder.open(Folder.READ_WRITE);
                    Message imapMessage = folder.getMessageByUID(email.getEmailUid());
                    if (imapMessage != null) {
                        imapMessage.setFlag(Flags.Flag.DELETED, true);
                    }
                    emailRecord.setId(null);
                }
            }
//            IMAPFolder folder = emailAccount.getIMAPFolder(emailAccount.getEmailStore(), EmailEnum.RECORD_SEND);
//            folder.open(Folder.READ_WRITE);
//            Long uid = folder.getUIDNext();
//            emailRecord.setEmailUid(uid);
//            emailRecord.save();
//            emailRecord.setId(null);
//            folder.appendMessages(new Message[]{message});
        } catch (Exception e) {
            Log.getLog(EmailUtil.class).error("发送邮件错误", e);
        }

        return isSend;
    }

    private static void addFile(MimeMultipart multipart, List<File> fileList) throws Exception {
        for (File file : fileList) {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(file);
            mimeBodyPart.setDataHandler(new DataHandler(fds));
            mimeBodyPart.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
            multipart.addBodyPart(mimeBodyPart);
        }
        ;
    }

    /**
     * @return true为星标邮件
     * @author zhangzhiwei
     */
    private static boolean isStar(Message message) throws MessagingException {

        Flags flags = message.getFlags();
        if (flags != null) {
            Flags.Flag[] flag = flags.getSystemFlags();
            for (Flags.Flag aFlag : flag) {
                if (aFlag.equals(Flags.Flag.FLAGGED)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获得邮件主题
     *
     * @param msg 邮件内容
     * @return 解码后的邮件主题
     */
    private static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    /**
     * 获得邮件发件人
     *
     * @param msg  邮件内容
     * @param type 1是获取发件地址
     * @return 姓名 <Email地址>
     * @throws UnsupportedEncodingException
     */
    public static String getFrom(MimeMessage msg, int type) throws MessagingException, UnsupportedEncodingException {
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from = person + "<" + address.getAddress() + ">";
        if (type == 1) {
            return address.getAddress();
        }
        return from;
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>Message.RecipientType.TO  收件人</p>
     * <p>Message.RecipientType.CC  抄送</p>
     * <p>Message.RecipientType.BCC 密送</p>
     *
     * @param msg  邮件内容
     * @param type 收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException, UnsupportedEncodingException {
        StringBuilder receiveAddress = new StringBuilder();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }
        if (addresss == null || addresss.length < 1) {
            return "";
        }
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress) address;
            receiveAddress.append(internetAddress.toUnicodeString()).append(",");
        }
        receiveAddress.deleteCharAt(receiveAddress.length() - 1); //删除最后一个逗号
        return decodeText(receiveAddress.toString());
    }

    /**
     * 获得邮件发送时间
     *
     * @param msg 邮件内容
     * @return yyyy年mm月dd日 星期X HH:mm
     * @throws MessagingException
     */
    public static Date getSentDate(MimeMessage msg) throws MessagingException {
        Date receivedDate = msg.getSentDate();
        if (receivedDate == null) {
            receivedDate = new Date();
        }
        return receivedDate;
    }

    /**
     * 判断邮件中是否包含附件
     *
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     */
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("application") || contentType.contains("name")) {
                        flag = true;
                    }
                }

                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }

    /**
     * 判断邮件是否已读
     *
     * @param msg 邮件内容
     * @return 如果邮件已读返回true, 否则返回false
     * @throws MessagingException
     */
    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }


    /**
     * 获得邮件文本内容
     *
     * @param part    邮件体
     * @param content 存储邮件文本内容的字符串
     * @throws MessagingException e
     * @throws IOException        e
     */
    public static void getMailTextContent(Part part, StringBuilder content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }

    /**
     * 文本解码
     *
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

    public static void saveEmail(EmailAccount account, AdminUser adminUser, EmailEnum emailEnum) {
        EmailImport emailImport = new EmailImport(account, emailEnum, adminUser);
        //注意执行顺序
        threadPool.execute(emailImport);
    }

    public static void addTask(Runnable runnable) {
        threadPool.execute(runnable);
    }

    public static InputStream getFile(Part part, String fileName, Long fileSize) {
        try {
            if (part.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
                //复杂体邮件包含多个邮件体
                int partCount = multipart.getCount();
                for (int i = 0; i < partCount; i++) {
                    //获得复杂体邮件中其中一个邮件体
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    //某一个邮件体也有可能是由多个邮件体组成的复杂体
                    String disp = bodyPart.getDisposition();
                    if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                        if (Objects.equals(decodeText(bodyPart.getFileName()), fileName) && Objects.equals(bodyPart.getSize(), fileSize.intValue())) {
                            return bodyPart.getInputStream();
                        }
                    } else if (bodyPart.isMimeType("multipart/*")) {
                        getFile(bodyPart, fileName, fileSize);
                    } else {
                        String contentType = bodyPart.getContentType();
                        if (contentType.contains("name") || contentType.contains("application")) {
                            if (Objects.equals(decodeText(bodyPart.getFileName()), fileName) && Objects.equals(bodyPart.getSize(), fileSize.intValue())) {
                                return bodyPart.getInputStream();
                            }
                        }
                    }
                }
            } else if (part.isMimeType("message/rfc822")) {
                getFile((Part) part.getContent(), fileName, fileSize);
            }
        } catch (IOException | MessagingException ignored) {
            ;
        }

        return null;
    }

    private static class EmailImport implements Runnable {

        private EmailAccount account;
        private AdminUser adminUser;
        private EmailEnum emailEnum;
        private List<Model> modelList = new ArrayList<>();
        private List<Model> fileList = new ArrayList<>();

        EmailImport(EmailAccount account, EmailEnum emailEnum, AdminUser adminUser) {
            this.account = account;
            this.emailEnum = emailEnum;
            this.adminUser = adminUser;
        }

        public void run() {
            Store store = account.getEmailStore();
            IMAPFolder folder = null;
            try {
                folder = account.getIMAPFolder(store, emailEnum);
                folder.open(Folder.READ_ONLY);
                int emailCount = folder.getMessageCount();
                Kv kv = Kv.by("userId", adminUser.getUserId()).set("emailType", emailEnum.getTypes());
                //获取所有邮件uid
                List<Long> uid = Db.template("email.record.queryEmailRecordByUid", kv).query();
                System.out.println(emailEnum.getName() + " 邮件数量:　" + emailCount);
                int num = 0;
                List<Long> serviceUids = new ArrayList<>(emailCount);
                Long messageUid = null;
                for (int i = emailCount; i > 0; i--) {
                    try {
                        MimeMessage mimeMessage = (MimeMessage) folder.getMessage(i);
                        messageUid = folder.getUID(mimeMessage);
                        serviceUids.add(messageUid);
                        if (num >= 200 || uid.contains(messageUid)) {
                            continue;
                        }
                        if (mimeMessage.getSubject() == null) {
                            continue;
                        }
                        parseMessage(mimeMessage, messageUid);
                        num++;
                    } catch (Exception e) {
                        if (messageUid != null) {
                            EmailRecord emailRecord = new EmailRecord();
                            emailRecord.setEmailUid(messageUid);
                            emailRecord.setIsDel(1);
                            modelList.add(emailRecord);
                        }
                        Log.getLog(EmailUtil.class).error("处理第" + i + "邮件错误：", e);
                    }
                }
                Db.batchSave(fileList, 500);
                Db.batchSave(modelList, 500);
                kv.set("uids", serviceUids);
                Db.template("email.record.deleteEmailByUid", kv).delete();
            } catch (Exception e) {
                Log.getLog(EmailUtil.class).error("导入邮件错误：", e);
            } finally {
                String key = "EMAIL:CACHE:KEY:".concat(emailEnum.getKey()).concat(account.getCreateUserId().toString());
                RedisManager.getRedis().del(key);
                try {
                    store.close();
                    if (folder != null && folder.isOpen()) {
                        folder.close();
                    }
                } catch (MessagingException ignored) {
                    ;
                }
            }

        }

        /**
         * 处理邮件
         *
         * @param msg msg
         */
        private void parseMessage(MimeMessage msg, Long messageUid) throws MessagingException, IOException {
            if (account.isSohu()) {
                msg = new MimeMessage(msg);
            }
            System.out.println("正在处理第 " + msg.getMessageNumber() + " 封邮件");
            EmailRecord emailRecord = new EmailRecord();
            String batchId = IdUtil.simpleUUID();
            emailRecord.setEmailType(emailEnum.getTypes());
            emailRecord.setBatchId(batchId);
            emailRecord.setCreateUserId(adminUser.getUserId());
            emailRecord.setEmailAccountId(adminUser.get("emailAccount"));
            emailRecord.setRecordType(emailEnum.getTypes());

            //设置主题
            emailRecord.setTheme(getSubject(msg));
            //获取发件时间
            emailRecord.setCreateTime(getSentDate(msg));
            //是否已读
            emailRecord.setIsRead(isSeen(msg) ? 1 : 0);
            //是否星标
            emailRecord.setIsStart(isStar(msg) ? 1 : 0);
            //发件人
            emailRecord.setSender(getFrom(msg, 0));
            //发件邮箱
            emailRecord.setSenderEmail(getFrom(msg, 1));
            //收件人
            emailRecord.setReceiptName(getReceiveAddress(msg, Message.RecipientType.TO));
            //收件邮箱
            emailRecord.setReceiptEmails(emailRecord.getReceiptName());
            //抄送人
            emailRecord.setCcEmails(getReceiveAddress(msg, Message.RecipientType.CC));
            //抄送名称
            emailRecord.setCcName(emailRecord.getCcEmails());
            //设置内容
            StringBuilder content = new StringBuilder();
            getMailTextContent(msg, content);
            emailRecord.setContent(content.toString());
            //设置邮件唯一标识
            emailRecord.setEmailUid(messageUid);
            //是否包涵文件
            boolean isContainerAttachment = isContainAttachment(msg);
            if (isContainerAttachment) {
                //保存附件
                saveAttachment(msg, batchId);
            }
            //加入list
            modelList.add(emailRecord);
            if (modelList.size() >= 10) {
                Db.batchSave(modelList, 100);
                Db.batchSave(fileList, 100);
                modelList.clear();
                fileList.clear();
            }
            System.out.println("第 " + msg.getMessageNumber() + " 封邮件处理结束");
        }

        /**
         * 保存附件
         *
         * @param part 邮件中多个组合体中的其中一个组合体
         * @throws UnsupportedEncodingException e
         * @throws MessagingException           e
         * @throws FileNotFoundException        e
         * @throws IOException                  e
         */
        private void saveAttachment(Part part, String batchId) throws MessagingException, IOException {
            if (part.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
                //复杂体邮件包含多个邮件体
                int partCount = multipart.getCount();
                for (int i = 0; i < partCount; i++) {
                    //获得复杂体邮件中其中一个邮件体
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    //某一个邮件体也有可能是由多个邮件体组成的复杂体
                    String disp = bodyPart.getDisposition();
                    if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                        saveFile((long) bodyPart.getSize(), decodeText(bodyPart.getFileName()), batchId);
                    } else if (bodyPart.isMimeType("multipart/*")) {
                        saveAttachment(bodyPart, batchId);
                    } else {
                        String contentType = bodyPart.getContentType();
                        if (contentType.contains("name") || contentType.contains("application")) {
                            saveFile((long) bodyPart.getSize(), decodeText(bodyPart.getFileName()), batchId);
                        }
                    }
                }
            } else if (part.isMimeType("message/rfc822")) {
                saveAttachment((Part) part.getContent(), batchId);
            }
        }

        /**
         * 保存文件名称，暂不下载文件
         *
         * @param fileSize 文件大小
         * @param fileName 文件名
         */
        private void saveFile(Long fileSize, String fileName, String batchId) {
            EmailFile emailFile = new EmailFile();
            emailFile.setFileName(fileName);
            emailFile.setCreateUserId(adminUser.getUserId());
            emailFile.setCreateTime(new Date());
            emailFile.setFileSize(fileSize);
            emailFile.setBatchId(batchId);
            fileList.add(emailFile);
        }
    }


}
