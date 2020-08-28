#namespace("email.account")
    #sql("queryListEmailAccountByConditions")
        select * from 72crm_email_account where 1=1
        #if(createUserId)
            and create_user_id=#para(createUserId)
        #end
    #end
    #sql("deleteById")
        delete from 72crm_email_account where id=? and create_user_id=?
    #end
    #sql("selectEmail")
       select customer_name as name,email as value from 72crm_crm_customer where 1=1
       and email like concat('%',#para(email),'%')
    #end

    #sql("queryEmailNumber")
      SELECT
        count(case when email_type=1 then '1' end)  as inBoxCount,
        count(case when email_type=2 then '1' end)  as sendCount,
        count(case when email_type=3 then '1' end)  as draftsCount,
        count(case when email_type=4 then '1' end)  as deleteCount,
        count(case when email_type=5 then '1' end)  as ljCount,
        count(case when is_read=0 and email_type=1 then '1' end)  as inBoxUnreadCount,
        count(case when is_read=0 and email_type=2 then '1' end)  as sendUnreadCount,
        count(case when is_read=0 and is_start=1 then '1' end)  as starUnreadCount,
        count(case when is_read=0 and email_type=4 then '1' end)  as deleteUnreadCount,
        count(case when is_read=0 and email_type=5 then '1' end)  as ljUnreadCount,
        count(case when is_start=1 then '1' end)  as starCount
      FROM
        72crm_email_record
      WHERE
        1 = 1
      AND is_del = 0
      and create_user_id=#para(userId)
    #end
#end
