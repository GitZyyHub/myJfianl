#namespace("email.record")

    #sql ("getPageList")
        select a.*,(SELECT COUNT(1) FROM 72crm_email_file WHERE batch_id=a.batch_id) as fileNum from 72crm_email_record as a
        where 1=1 and is_del=0
        #if(userId)
            and create_user_id=#para(userId)
        #end
        #if(emailType)
            and email_type=#para(emailType)
        #end
        #if(search)
            and (sender like  CONCAT('%',#para(search),'%') or sender_email like  CONCAT('%',#para(search),'%') or theme like  CONCAT('%',#para(search),'%'))
        #end
        #if(star)
            and is_start=#para(star)
        #end
        #if(read)
            and is_read= #para(read)
        #end
        #if(email)
            and (sender_email=#para(email) or receipt_emails like  concat('%',#para(email),'%'))
        #end
        #if(sorting)
            order by create_time desc
        #end
    #end
    #sql("queryEmailRecordByUid")
      SELECT email_uid FROM 72crm_email_record WHERE create_user_id = #para(userId) and email_type=#para(emailType) and email_uid is not null
    #end
    #sql("deleteEmailByUid")
      DELETE FROM `72crm_email_record` WHERE create_user_id = #para(userId) and email_type=#para(emailType) and (email_uid not in (#fori(uids)) or email_uid is null)
    #end
    #sql("queryLately")
        select id,email as value,name from 72crm_email_lately
        where 1=1
        #if(userId)
            and create_user_id=#para(userId)
        #end
        order by update_time desc
    #end
    #sql("queryLatelyById")
        select * from 72crm_email_lately where 1=1 and create_user_id=#para(userId) and email=#para(email)
    #end
    #sql("queryEmailFileByBatchId")
        SELECT * FROM `72crm_email_file` where batch_id=#para(batchId)
    #end
#end
