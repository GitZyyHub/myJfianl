package com.kakarote.crm9.erp.email.common;

import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.email.entity.EmailAccount;
import com.kakarote.crm9.erp.email.service.EmailRecordService;
import com.sun.mail.pop3.POP3Folder;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class Pop3EmailTask {

    public static final Pop3EmailTask ME = new Pop3EmailTask();
    //超出的线程直接抛出异常
    private static final ExecutorService threadPool = new ThreadPoolExecutor(10, 200, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2048), new ThreadPoolExecutor.AbortPolicy());

    private static final ConcurrentHashMap<Long, Task> threadMap = new ConcurrentHashMap<>(32,0.75f);

    private Pop3EmailTask(){

    }

    public static void remove(Long userId){
        Task task = threadMap.remove(userId);
        if(task!=null){
            task.close();
        }
    }
    public void receivingEmail(){
        List<EmailAccount> emailAccounts=EmailAccount.dao.findAll();
        emailAccounts.forEach(this::addTask);
    }

    public void addTask(EmailAccount e){
        Task task=new Task(e);
        threadMap.put(e.getCreateUserId(),task);
        threadPool.execute(task);
    }

    private class Task extends Thread {
        private EmailAccount emailAccount;
        private Integer num = 0;
        private Integer errNum = 0;
        private boolean interrupted = true;
        private AdminUser adminUser;

        public Task(EmailAccount emailAccount){
            this.emailAccount = emailAccount;
            this.adminUser = AdminUser.dao.findById(emailAccount.getCreateUserId());
        }
        @Override
        public void run() {
            while (interrupted) {
                System.out.println("---------开始同步-----------");
                try {
                    POP3Folder folder=getFolder();
                    if(folder==null){
                        errNum++;
                    }else {
                        if(!Objects.equals(num,folder.getMessageCount())){
                            System.out.println("------有新邮件-------");
                            num=folder.getMessageCount();
                            Aop.get(EmailRecordService.class).receivingEmail(emailAccount,adminUser,EmailEnum.RECEIPT_EMAIL);
                        }
                        folder.close(false);
                    }
                    sleep(60000);
                } catch (Exception e) {
                    e.printStackTrace();
                    errNum++;
                }
                if (errNum >= 3) {
                    close();
                }
            }
        }

        public void close() {
            interrupted = false;
            threadMap.remove(adminUser.getUserId());
        }

        private POP3Folder getFolder() throws MessagingException {
            POP3Folder folder=emailAccount.getPoP3Folder();
            folder.open(Folder.READ_ONLY);
            return folder;
        }
    }
}
