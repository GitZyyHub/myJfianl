package com.kakarote.crm9.erp.email.common;

import com.jfinal.log.Log;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.erp.email.entity.EmailRecord;
import com.sun.mail.imap.AppendUID;
import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import java.util.List;
import java.util.Objects;

public class EmailTask {

    public static class ShiftEmailTask implements Runnable {

        private EmailAccount emailAccount;
        private EmailEnum emailEnum;
        private String[] ids;

        public ShiftEmailTask(EmailAccount emailAccount, EmailEnum emailEnum, String... ids) {
            this.emailAccount = emailAccount;
            this.emailEnum = emailEnum;
            this.ids = ids;
        }

        @Override
        public void run() {
            if (ids.length == 0) {
                return;
            }
            IMAPFolder folder = null;
            try (
                    Store store = emailAccount.getEmailStore();
                    IMAPFolder moveFolder = emailAccount.getIMAPFolder(store, emailEnum);
            ) {
                moveFolder.open(Folder.READ_WRITE);
                int prevType = 0;
                for (Object id : ids) {
                    EmailRecord emailRecord = EmailRecord.dao.findById(id);
                    //查询不到不处理
                    if (emailRecord == null || emailRecord.getEmailUid() == null) {
                        continue;
                    }
                    try {
                        if (prevType != emailRecord.getEmailType()) {
                            if (folder != null) {
                                folder.close(true);
                            }
                            folder = emailAccount.getIMAPFolder(store, EmailEnum.valueOf(emailRecord.getEmailType()));
                            folder.open(Folder.READ_WRITE);
                        }
                        prevType = emailRecord.getEmailType();
                        if (folder == null) {
                            folder = emailAccount.getIMAPFolder(store, EmailEnum.valueOf(emailRecord.getEmailType()));
                        }
                        Message message = folder.getMessageByUID(emailRecord.getEmailUid());
                        if (message != null) {
                            AppendUID[] appendUID = folder.copyUIDMessages(new Message[]{message}, moveFolder);
                            if (appendUID.length > 0) {
                                message.setFlag(Flags.Flag.DELETED, true);
                                if (appendUID[0] != null) {
                                    emailRecord.setEmailUid(appendUID[0].uid);
                                    emailRecord.update();
                                }
                            }
                        }
                    } catch (MessagingException e) {
                        Log.getLog(getClass()).error("移动邮件错误", e);
                    }
                }
            } catch (MessagingException ignored) {
                ;
            } finally {
                if (folder != null && folder.isOpen()) {
                    try {
                        folder.close();
                    } catch (MessagingException ignored) {
                        ;
                    }
                }
            }
        }
    }

    public static class DeleteEmailTask implements Runnable {

        private EmailAccount emailAccount;

        private List<Integer> emailTypes;

        private List<Long> emailUids;

        public DeleteEmailTask(EmailAccount emailAccount, List<Integer> emailTypes, List<Long> emailUids) {
            this.emailAccount = emailAccount;
            this.emailTypes = emailTypes;
            this.emailUids = emailUids;
        }

        @Override
        public void run() {
            if (emailTypes.size() != emailUids.size()) {
                return;
            }
            IMAPFolder folder = null;
            Integer folderType = null;
            try (Store store = emailAccount.getEmailStore()) {

                for (int i = 0; i < emailTypes.size(); i++) {
                    try {
                        Integer emailType = emailTypes.get(i);
                        Long uid = emailUids.get(i);
                        //查询不到不处理
                        if (emailType == null || uid == null) {
                            continue;
                        }
                        if (!Objects.equals(folderType, emailType)) {
                            if (folder != null) {
                                folder.close(true);
                            }
                            folder = emailAccount.getIMAPFolder(store, EmailEnum.valueOf(emailType));
                            folder.open(Folder.READ_WRITE);
                        }
                        folderType = emailType;
                        if (folder == null) {
                            continue;
                        }
                        Message message = folder.getMessageByUID(uid);
                        message.setFlag(Flags.Flag.DELETED, true);
                    } catch (MessagingException e) {
                        Log.getLog(getClass()).error("删除邮件错误", e);
                    }
                }
            } catch (MessagingException ignored) {
                ;
            } finally {
                if (folder != null && folder.isOpen()) {
                    try {
                        folder.close();
                    } catch (MessagingException ignored) {
                        ;
                    }
                }
            }
        }
    }

    public static class UpdateStateTask implements Runnable {

        private EmailAccount emailAccount;

        private Integer state;

        private String[] ids;

        public UpdateStateTask(EmailAccount emailAccount, Integer state, String... ids) {
            this.emailAccount = emailAccount;
            this.state = state;
            this.ids = ids;
        }

        @Override
        public void run() {
            if (ids.length == 0) {
                return;
            }
            IMAPFolder folder = null;
            Integer folderType = null;
            try (Store store = emailAccount.getEmailStore()) {
                for (Object id : ids) {
                    EmailRecord record = EmailRecord.dao.findById(id);
                    //查询不到不处理
                    if (record == null || record.getEmailUid() == null) {
                        continue;
                    }

                    try {
                        if (!Objects.equals(folderType, record.getEmailType())) {
                            if (folder != null) {
                                folder.close(true);
                            }
                            folder = emailAccount.getIMAPFolder(store, EmailEnum.valueOf(record.getEmailType()));
                            folder.open(Folder.READ_WRITE);
                        }
                        folderType = record.getEmailType();
                        if (folder == null) {
                            continue;
                        }
                        Message message = folder.getMessageByUID(record.getEmailUid());
                        //为 1的情况下表示 已读
                        if (state.equals(1)) {
                            message.setFlag(Flags.Flag.SEEN, true);
                        } else if (state.equals(2)) {//未读
                            message.setFlag(Flags.Flag.SEEN, false);
                        } else if (state.equals(3)) {//标星
                            message.setFlag(Flags.Flag.FLAGGED, true);
                        } else if (state.equals(4)) {//取消标星
                            message.setFlag(Flags.Flag.FLAGGED, false);
                        }
                    } catch (MessagingException e) {
                        Log.getLog(getClass()).error("删除邮件错误", e);
                    }
                }
            } catch (MessagingException e) {
                Log.getLog(getClass()).error("更改邮件状态错误", e);
            } finally {
                if (folder != null && folder.isOpen()) {
                    try {
                        folder.close();
                    } catch (MessagingException ignored) {
                        ;
                    }
                }
            }
        }
    }
}
