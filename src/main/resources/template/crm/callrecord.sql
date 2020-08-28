#namespace("call.record")
    #sql("pageCallRecordList")
        SELECT
            cr.*, au.realname AS createUserName,
            hu.realname AS ownerUserName,
            (
                CASE cr.model
                WHEN 'leads' THEN
                    (
                        SELECT
                            cl.leads_name
                        FROM
                            72crm_crm_leads cl
                        WHERE
                            cl.leads_id = cr.model_id
                    )
                WHEN 'customer' THEN
                    (
                        SELECT
                            cc.customer_name
                        FROM
                            72crm_crm_customer cc
                        WHERE
                            cc.customer_id = cr.model_id
                    )
                WHEN 'contacts' THEN
                    (
                        SELECT
                            cl. NAME
                        FROM
                            72crm_crm_contacts cl
                        WHERE
                            cl.contacts_id = cr.model_id
                    )
                END
            ) AS model_name
        FROM
            (
                72crm_call_record cr
                LEFT JOIN 72crm_admin_user au ON (
                    cr.create_user_id = au.user_id
                )
            )
        LEFT JOIN 72crm_admin_user hu ON (
            cr.owner_user_id = hu.user_id
        )
        WHERE owner_user_id in (
           #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
          #end
        )
          #if(status == 1)
          and to_days(NOW()) = TO_DAYS(cr.create_time)
          #end
           #if(status == 2)
          and to_days(NOW()) - TO_DAYS(cr.create_time) = 1
          #end
           #if(status == 3)
          and YEARWEEK(date_format(cr.create_time,'%Y-%m-%d')) = YEARWEEK(now())
          #end
           #if(status == 4)
          and YEARWEEK(date_format(cr.create_time,'%Y-%m-%d')) = YEARWEEK(now()) -1
          #end
           #if(status == 5)
          and date_format(cr.create_time,'%Y-%m')=date_format(now(),'%Y-%m')
          #end
           #if(status == 6)
          and date_format(cr.create_time,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
          #end
           #if(status == 7)
          and QUARTER(cr.create_time)=QUARTER(now()) AND YEAR(cr.create_time)=YEAR(NOW())
          #end
           #if(status == 8)
          and QUARTER(cr.create_time)=QUARTER(DATE_SUB(now(),interval 1 QUARTER)) and YEAR(DATE_SUB(cr.create_time,interval 1 QUARTER)) = YEAR(DATE_SUB(NOW(),interval 1 QUARTER))
          #end
           #if(status == 9)
          and YEAR(cr.create_time)=YEAR(NOW())
          #end
           #if(status == 10)
          and YEAR(cr.create_time)=YEAR(date_sub(now(),interval 1 year))
          #end
           #if(status == 11)
            and  TO_DAYS(cr.create_time) >= TO_DAYS(#para(startTime))
            and  TO_DAYS(cr.create_time) <= TO_DAYS(#para(endTime))
          #end
          #if(talkTime)
          and talk_time #(talkTimeCondition) #para(talkTime)
          #end
        order by cr.call_record_id DESC
    #end
     #sql("queryRecord")
       select * from 72crm_call_record  where number = ? and start_time = ? and owner_user_id = ?
    #end
#end
