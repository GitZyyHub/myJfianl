#namespace("crm.activity")
  #sql ("getCrmActivityPageList")
    select a.*,b.realname,b.img as userImg,(case a.activity_type when 1 then (select leads_name from `72crm_crm_leads` where leads_id = a.activity_type_id)
                                                 when 2 then (select customer_name from `72crm_crm_customer` where customer_id = a.activity_type_id)
                                                 when 3 then (select name from `72crm_crm_contacts` where contacts_id = a.activity_type_id)
                                                 when 5 then (select business_name from `72crm_crm_business` where business_id = a.activity_type_id)
                                                 when 6 then (select name from `72crm_crm_contract` where contract_id = a.activity_type_id)
                                                 when 7 then (select number from `72crm_crm_receivables` where receivables_id = a.activity_type_id)
                                  end ) as activityTypeName
    from `72crm_crm_activity` as a left join `72crm_admin_user` b on a.create_user_id = b.user_id
    where  date_format(a.create_time,'%Y-%m-%d') = #para(time) and a.status = 1
    #if(crmType == 2)
      and (
        #if(activityType == 2)
          a.activity_type = 2 and a.activity_type_id = #para(activityTypeId)
        #else if(activityType == 3)
          a.activity_type = 3 and a.activity_type_id in (select contacts_id from `72crm_crm_contacts` where customer_id = #para(activityTypeId))
        #else if(activityType == 5)
          a.activity_type = 5 and a.activity_type_id in (select business_id from `72crm_crm_business` where customer_id = #para(activityTypeId))
        #else if(activityType == 6)
          a.activity_type = 6 and a.activity_type_id in (select contract_id from `72crm_crm_contract` where customer_id = #para(activityTypeId)) and a.type = 2
        #else if(activityType == 7)
          a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where customer_id = #para(activityTypeId)) and a.type = 2
        #else if(activityType == 8)
          a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),customer_ids))
        #else if(activityType == 9)
          a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),customer_ids))
        #else if(activityType == 10)
          a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),customer_ids))
        #else if(activityType == 11)
          a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),customer_ids))
        #else
          (a.activity_type = 2 and a.activity_type_id = #para(activityTypeId)) or
          (a.activity_type = 3 and a.activity_type_id in (select contacts_id from `72crm_crm_contacts` where customer_id = #para(activityTypeId))) or
          (a.activity_type = 5 and a.activity_type_id in (select business_id from `72crm_crm_business` where customer_id = #para(activityTypeId))) or
          (a.activity_type = 6 and a.activity_type_id in (select contract_id from `72crm_crm_contract` where customer_id = #para(activityTypeId)) and a.type = 2) or
          (a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where customer_id = #para(activityTypeId)) and a.type = 2) or
          (a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),customer_ids))) or
          (a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),customer_ids))) or
          (a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),customer_ids))) or
          (a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),customer_ids)))
        #end
             )
    #else if(crmType == 3)
      and (
        #if(activityType == 2)
          a.activity_type = 2 and a.activity_id in (select activity_id from `72crm_crm_activity_relation` where type = 3 and type_id = #para(activityTypeId))
        #else if(activityType == 3)
          a.activity_type = 3 and a.activity_type_id = #para(activityTypeId)
        #else if(activityType == 8)
          a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),contacts_ids))
        #else if(activityType == 9)
          a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),contacts_ids))
        #else if(activityType == 10)
          a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),contacts_ids))
        #else if(activityType == 11)
          a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),contacts_ids))
        #else
          (a.activity_type = 2 and a.activity_id in (select activity_id from `72crm_crm_activity_relation` where type = 3 and type_id = #para(activityTypeId))) or
          (a.activity_type = 3 and a.activity_type_id = #para(activityTypeId)) or
          (a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),contacts_ids))) or
          (a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),contacts_ids))) or
          (a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),contacts_ids))) or
          (a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),contacts_ids)))
        #end
        )
    #else if(crmType == 5)
     and (
        #if(activityType == 2)
          a.activity_type = 2 and a.activity_id in (select activity_id from `72crm_crm_activity_relation` where type = 5 and type_id = #para(activityTypeId))
        #else if(activityType == 5)
          a.activity_type = 5 and a.activity_type_id = #para(activityTypeId)
        #else if(activityType == 6)
          a.activity_type = 6 and a.activity_type_id in (select contract_id from `72crm_crm_contract` where business_id = #para(activityTypeId) ) and a.type = 2
        #else if(activityType == 7)
          a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where contract_id in (select contract_id from `72crm_crm_contract` where business_id = #para(activityTypeId))) and a.type = 2
        #else if(activityType == 8)
          a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else if(activityType == 9)
          a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else if(activityType == 10)
          a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else if(activityType == 11)
          a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else
          (a.activity_type = 2 and a.activity_id in (select activity_id from `72crm_crm_activity_relation` where type = 5 and type_id = #para(activityTypeId))) or
          (a.activity_type = 5 and a.activity_type_id = #para(activityTypeId)) or
          (a.activity_type = 6 and a.activity_type_id in (select contract_id from `72crm_crm_contract` where business_id = #para(activityTypeId) ) and a.type = 2) or
          (a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where contract_id in (select contract_id from `72crm_crm_contract` where business_id = #para(activityTypeId))) and a.type = 2) or
          (a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),business_ids))) or
          (a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),business_ids))) or
          (a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),business_ids))) or
          (a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),business_ids)))
        #end
    )
    #else if(crmType == 6)
      and (
        #if(activityType == 6)
          a.activity_type = 6 and a.activity_type_id = #para(activityTypeId)
        #else if(activityType == 7)
          a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where contract_id  = #para(activityTypeId)) and a.type = 2
        #else if(activityType == 8)
          a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),contract_ids))
        #else if(activityType == 9)
          a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else if(activityType == 10)
          a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),contract_ids))
        #else if(activityType == 11)
          a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),contract_ids))
        #else
          (a.activity_type = 6 and a.activity_type_id = #para(activityTypeId)) or
          (a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where contract_id  = #para(activityTypeId)) and a.type = 2) or
          (a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),contract_ids))) or
          (a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),contract_ids))) or
          (a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),contract_ids))) or
          (a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),contract_ids)))
        #end
         )
    #else
      and a.activity_type = #para(crmType) and a.activity_type_id = #para(activityTypeId)
    #end
          order by a.create_time desc
  #end
  #sql ("getActivityCountByTime")
    SELECT date_format(create_time,'%Y-%m-%d') FROM 72crm_crm_activity as a where a.status = 1
    #if(crmType == 2)
      and (
        #if(activityType == 1)
          a.activity_type = 1 and a.activity_type_id in (select leads_id from `72crm_crm_leads` where customer_id = #para(activityTypeId))
        #else if(activityType == 2)
          a.activity_type = 2 and a.activity_type_id = #para(activityTypeId)
        #else if(activityType == 3)
          a.activity_type = 3 and a.activity_type_id in (select contacts_id from `72crm_crm_contacts` where customer_id = #para(activityTypeId))
        #else if(activityType == 5)
          a.activity_type = 5 and a.activity_type_id in (select business_id from `72crm_crm_business` where customer_id = #para(activityTypeId))
        #else if(activityType == 6)
          a.activity_type = 6 and a.activity_type_id in (select contract_id from `72crm_crm_contract` where customer_id = #para(activityTypeId)) and a.type = 2
        #else if(activityType == 7)
          a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where customer_id = #para(activityTypeId)) and a.type = 2
        #else if(activityType == 8)
          a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),customer_ids))
        #else if(activityType == 9)
          a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),customer_ids))
        #else if(activityType == 10)
          a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),customer_ids))
        #else if(activityType == 11)
          a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),customer_ids))
        #else
          (a.activity_type = 1 and a.activity_type_id in (select leads_id from `72crm_crm_leads` where customer_id = #para(activityTypeId))) or
          (a.activity_type = 2 and a.activity_type_id = #para(activityTypeId)) or
          (a.activity_type = 3 and a.activity_type_id in (select contacts_id from `72crm_crm_contacts` where customer_id = #para(activityTypeId))) or
          (a.activity_type = 5 and a.activity_type_id in (select business_id from `72crm_crm_business` where customer_id = #para(activityTypeId))) or
          (a.activity_type = 6 and a.activity_type_id in (select contract_id from `72crm_crm_contract` where customer_id = #para(activityTypeId)) and a.type = 2) or
          (a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where customer_id = #para(activityTypeId)) and a.type = 2) or
          (a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),customer_ids))) or
          (a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),customer_ids))) or
          (a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),customer_ids))) or
          (a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),customer_ids)))
        #end
             )
    #else if(crmType == 3)
      and (
        #if(activityType == 2)
          a.activity_type = 2 and a.activity_id in (select activity_id from `72crm_crm_activity_relation` where type = 3 and type_id = #para(activityTypeId))
        #else if(activityType == 3)
          a.activity_type = 3 and a.activity_type_id = #para(activityTypeId)
        #else if(activityType == 8)
          a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),contacts_ids))
        #else if(activityType == 9)
          a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),contacts_ids))
        #else if(activityType == 10)
          a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),contacts_ids))
        #else if(activityType == 11)
          a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),contacts_ids))
        #else
          (a.activity_type = 2 and a.activity_id in (select activity_id from `72crm_crm_activity_relation` where type = 3 and type_id = #para(activityTypeId))) or
          (a.activity_type = 3 and a.activity_type_id = #para(activityTypeId)) or
          (a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),contacts_ids))) or
          (a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),contacts_ids))) or
          (a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),contacts_ids))) or
          (a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),contacts_ids)))
        #end
        )
    #else if(crmType == 5)
     and (
        #if(activityType == 2)
          a.activity_type = 2 and a.activity_id in (select activity_id from `72crm_crm_activity_relation` where type = 5 and type_id = #para(activityTypeId))
        #else if(activityType == 5)
          a.activity_type = 5 and a.activity_type_id = #para(activityTypeId)
        #else if(activityType == 6)
          a.activity_type = 6 and a.activity_type_id in (select contract_id from `72crm_crm_contract` where business_id = #para(activityTypeId) ) and a.type = 2
        #else if(activityType == 7)
          a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where contract_id in (select contract_id from `72crm_crm_contract` where business_id = #para(activityTypeId))) and a.type = 2
        #else if(activityType == 8)
          a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else if(activityType == 9)
          a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else if(activityType == 10)
          a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else if(activityType == 11)
          a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else
          (a.activity_type = 2 and a.activity_id in (select activity_id from `72crm_crm_activity_relation` where type = 5 and type_id = #para(activityTypeId))) or
          (a.activity_type = 5 and a.activity_type_id = #para(activityTypeId)) or
          (a.activity_type = 6 and a.activity_type_id in (select contract_id from `72crm_crm_contract` where business_id = #para(activityTypeId) ) and a.type = 2) or
          (a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where contract_id in (select contract_id from `72crm_crm_contract` where business_id = #para(activityTypeId))) and a.type = 2) or
          (a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),business_ids))) or
          (a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),business_ids))) or
          (a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),business_ids))) or
          (a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),business_ids)))
        #end
    )
    #else if(crmType == 6)
      and (
        #if(activityType == 6)
          a.activity_type = 6 and a.activity_type_id = #para(activityTypeId)
        #else if(activityType == 7)
          a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where contract_id  = #para(activityTypeId)) and a.type = 2
        #else if(activityType == 8)
          a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),contract_ids))
        #else if(activityType == 9)
          a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),business_ids))
        #else if(activityType == 10)
          a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),contract_ids))
        #else if(activityType == 11)
          a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),contract_ids))
        #else
          (a.activity_type = 6 and a.activity_type_id = #para(activityTypeId)) or
          (a.activity_type = 7 and a.activity_type_id in (select receivables_id from `72crm_crm_receivables` where contract_id  = #para(activityTypeId)) and a.type = 2) or
          (a.activity_type = 8 and a.activity_type_id in (select log_id from `72crm_oa_log_relation` where find_in_set(#para(activityTypeId),contract_ids))) or
          (a.activity_type = 9 and a.activity_type_id in (select examine_id from `72crm_oa_examine_relation` where find_in_set(#para(activityTypeId),contract_ids))) or
          (a.activity_type = 10 and a.activity_type_id in (select event_id from `72crm_oa_event_relation` where find_in_set(#para(activityTypeId),contract_ids))) or
          (a.activity_type = 11 and a.activity_type_id in (select task_id from `72crm_task_relation` where find_in_set(#para(activityTypeId),contract_ids)))
        #end
         )
    #else
      and a.activity_type = #para(crmType) and a.activity_type_id = #para(activityTypeId)
    #end
    GROUP BY date_format(create_time,'%Y-%m-%d')  ORDER BY create_time DESC LIMIT #para(page),2
  #end

  #sql ("queryLastRecord")
  select * from 72crm_crm_activity
  where type = 1 and activity_type = ? and activity_type_id = ? and activity_id != ? order by create_time desc limit 1
  #end

  #sql ("updateLastRecord")
  update #(tableName) set last_time = #para(lastTime)#if(type == 1 || type == 2),last_content = #para(lastContent)#end
  where #(tableId) = #para(id)
  #end

  #sql("delete")
    update `72crm_crm_activity` set status = 0 where activity_type = #para(type) and activity_type_id in (#fori(ids))
  #end

  #sql ("queryOutworkStats")
  select b.user_id,b.realname,b.img,count(1) as count
  from 72crm_crm_activity as a inner join (select user_id,realname,img
                                           from 72crm_admin_user
                                           where user_id in (#fori(userId))) as b on a.create_user_id = b.user_id
  where a.type = 4 and a.create_time between #para(startTime) and #para(endTime) group by create_user_id
  #end

  #sql ("queryOutworkList")
  select b.user_id,b.realname,b.img as userImg,a.activity_id,a.create_time,a.address,a.content,a.activity_type,a.status,a.batch_id,c.customer_id,c.customer_name
  from 72crm_crm_activity as a left join 72crm_admin_user as b on a.create_user_id = b.user_id
  left join 72crm_crm_customer as c on a.activity_type_id = c.customer_id
  where a.type = 4 and a.create_user_id = #para(userId) #if(startTime)and a.create_time between #para(startTime) and #para(endTime)#end
  order by create_time desc
  #end
#end
