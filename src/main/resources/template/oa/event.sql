#namespace("oa.event")
    #sql("queryEventId")
        select event_id from `72crm_oa_event` where batch_id = #para(batchId)
    #end
    #sql("deleteEventByBatchId")
        delete from `72crm_oa_event` where batch_id = #para(batchId)
    #end
    #sql("deleteEventRelation")
        delete from `72crm_oa_event_relation` where event_id in (#fori(eventIds))
    #end
    #sql("deleteEventByIds")
        delete from `72crm_oa_event` where event_id in (#fori(eventIds))
    #end
    #sql("queryList")
    select a.*,b.color
    from `72crm_oa_event` a left join `72crm_oa_calendar_type` b on a.type_id = b.type_id
        where 1 = 1
        #if(userId)
        and find_in_set(#para(userId),owner_user_ids)
        #end
        #if(startTime)
          and (repeat_end_time is null or
                  case
                   when repetition_type = 1 then unix_timestamp(repeat_start_time) * 1000  >= (#para(startTime) - (unix_timestamp(end_time) * 1000 - unix_timestamp(start_time) * 1000)) and
                                                 unix_timestamp(repeat_end_time) * 1000 <= #para(endTime) + (unix_timestamp(end_time) * 1000 - unix_timestamp(start_time) * 1000)
                   else
                       unix_timestamp(repeat_end_time)*1000 >= #para(startTime)
                   end
                )
        #end
        #if(typeIds)
        and a.type_id in (#fori(typeIds))
        #end
    #end
    #sql("queryEventRecordList")
        select * from `72crm_oa_event_update_record` where batch_id in (#fori(batchIds))
    #end
    #sql("queryById")
        select a.*,b.type_name,b.color,c.realname as createUserName from `72crm_oa_event` a left join `72crm_oa_calendar_type` b on a.type_id = b.type_id
        left join `72crm_admin_user` c on a.create_user_id = c.user_id where a.event_id = #para(eventId)
    #end
    #sql("queryNoticeById")
        select type,value from `72crm_oa_event_notice` where event_id = #para(eventId)
    #end
    #sql("deleteNoticeMessage")
        delete from `72crm_admin_message` where label = 5 and type = 9 and create_time > now() and type_id = #para(eventId)
    #end
#end
