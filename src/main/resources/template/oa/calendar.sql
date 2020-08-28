#namespace("oa.calendar")
    #sql("queryTypeList")
        select type_id,type_name,color from `72crm_oa_calendar_type` where type  = 2
    #end
    #sql("deleteEventRecordByEventId")
        delete from `72crm_oa_event_update_record` where event_id in (#fori(eventIds))
    #end
    #sql("eventTask")
        select task_id, name, unix_timestamp(start_time) * 1000 as startTime, unix_timestamp(stop_time) * 1000 as endTime,2 as eventType
        from `72crm_task`
        where unix_timestamp(start_time) * 1000 >= (#para(startTime) - (unix_timestamp(stop_time) * 1000 - unix_timestamp(start_time) * 1000))
          and unix_timestamp(stop_time) * 1000 <= #para(endTime) + (unix_timestamp(stop_time) * 1000 - unix_timestamp(start_time) * 1000)
          and   pid = 0 and ishidden = 0
                        and (
                      main_user_id = #para(userId)
                      or FIND_IN_SET(#para(userId),owner_user_id)
                        )
    #end
    #sql("eventCustomer")
      select date_format(next_time,'%Y-%m-%d') as time
      from `72crm_crm_customer`
      where status != 3 and  owner_user_id = #para(userId) and unix_timestamp(next_time)*1000 between #para(startTime) and #para(endTime)
      group by date_format(next_time,'%Y-%m-%d')
    #end
    #sql("endContract")
      select date_format(date_sub(end_time,interval #para(expiringDay) day),'%Y-%m-%d')  as time
      from `72crm_crm_contract`
      where check_status = 1 and owner_user_id = #para(userId)
      and (to_days(end_time) - #para(expiringDay)) between to_days(#para(startTime)) and to_days(#para(endTime))
      group by date_format(date_sub(end_time,interval #para(expiringDay) day),'%Y-%m-%d')
    #end
    #sql("receiveContract")
        select date_format(a.return_date,'%Y-%m-%d')  as time
         from `72crm_crm_receivables_plan` a
             left join `72crm_crm_contract` c on a.contract_id = c.contract_id
        where c.check_status = 1
          and to_days(a.return_date) between to_days(#para(startTime)) and to_days(#para(endTime))
          and  a.create_user_id = #para(userId)
          and a.return_date is not null
          group by date_format(a.return_date,'%Y-%m-%d')
    #end
    #sql("eventCustomerList")
      select customer_id
      from `72crm_crm_customer`
      where status != 3 and owner_user_id = #para(userId) and to_days(next_time) = to_days(#para(time))
    #end
    #sql("endContractList")
      select contract_id
      from `72crm_crm_contract`
      where check_status = 1 and owner_user_id = #para(userId)
      and (to_days(end_time) - #para(expiringDay)) = to_days(#para(time))
    #end
    #sql("receiveContractList")
        select c.contract_id,a.return_type,a.return_date,a.num as planNum
         from `72crm_crm_receivables_plan` a
         left join `72crm_crm_contract` c on a.contract_id = c.contract_id
        where c.check_status = 1
        and to_days(a.return_date)  = to_days(#para(time))
          and  a.create_user_id = #para(userId)
          and a.return_date is not null
    #end
    #sql("queryTypeListByUser")
        select type_id from `72crm_oa_calendar_type_user`
        where user_id = #para(userId)
    #end
    #sql("queryUserType")
        select color
        from `72crm_oa_calendar_type_user` a
                 left join `72crm_oa_calendar_type` `b` on a.type_id = b.type_id
        where b.color in (1, 2, 3, 4)
          and a.user_id = #para(userId)
    #end
#end