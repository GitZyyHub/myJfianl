#namespace("crm.marketing")
  #sql("queryPageList")
    select a.marketing_id,
       a.marketing_name,
       a.marketing_type,
       a.marketing_money,
       a.main_file_ids,
       a.address,
       a.crm_type,
       a.start_time,
       a.end_time,
       a.status,
       a.second,
       a.create_time,
       a.update_time,
       a.create_user_id,
       b.realname as createUserName
    from `72crm_crm_marketing` a
         left join `72crm_admin_user` b on a.create_user_id = b.user_id
         where 1 = 1
         #if(!isAdmin)
             and (#for(x : userIds)
                #(for.index > 0 ? "or" : "")
                 a.relation_user_id like concat('%,',#para(x),',%')
            #end)
        #end
        #if(crmType)
           and a.crm_type = #para(crmType)
        #end
        #if(search)
           and a.marketing_name like concat('%',#para(search),'%')
        #end
        #if(timeType == 1)
           and to_days(NOW()) <= TO_DAYS(a.end_time) and to_days(NOW()) >= TO_DAYS(a.start_time)
        #end
        #if(timeType == 2)
           and to_days(NOW()) > TO_DAYS(a.end_time)
        #end
         #if(status)
           and a.status = #para(status)
        #end
        ORDER BY IFNUll(a.update_time,a.create_time) desc
  #end
  #sql("queryById")
    select a.*,
           (select count(1) from `72crm_crm_marketing_info` b  where b.marketing_id = a.marketing_id) as sub_count
    from `72crm_crm_marketing` a where a.marketing_id = #para(marketingId)
  #end
  #sql("queryFieldByIds")
        SELECT field_id,field_name,name,type,label,remark,input_tips,max_length,default_value,is_unique,is_null,options,operating,update_time,examine_category_id,field_type,relevant
        FROM 72crm_admin_field
        WHERE field_id in (#fori(fieldIds))
        ORDER BY sorting
  #end
  #sql("census")
        select a.r_id,a.status,a.field_info,b.realname as owner_user_name from `72crm_crm_marketing_info` a left join `72crm_admin_user` b on a.owner_user_id = b.user_id where a.marketing_id = #para(marketingId)
        and a.owner_user_id in (#fori(userIds))
        #if(status)
            and a.status = #para(status)
        #end
  #end
#end




