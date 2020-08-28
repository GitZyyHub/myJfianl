#namespace("oa.log")
  #sql("queryList")
    SELECT
	    a.*, b.dept_id,
      b.realname,
      b.img as userImg,
      soal.customer_ids,
      soal.contacts_ids,
      soal.business_ids,
      soal.contract_ids,
      (CASE WHEN c.log_id is null THEN 0 ELSE 1 END) as getBulletin,
      c.customer_num as customerCount,
      c.business_num as businessCount,
      c.contract_num as contractCount,
      c.receivables_money as receivablesMoney,
      c.activity_num as recordCount,
      (SELECT COUNT(*) FROM 72crm_task_comment WHERE type_id=a.log_id and type='2') as replyNum
    FROM
	    72crm_oa_log as a
    LEFT JOIN 72crm_admin_user as b on a.create_user_id=b.user_id
    LEFT JOIN 72crm_oa_log_record as c on a.log_id=c.log_id
    LEFT JOIN 72crm_oa_log_relation as soal on soal.log_id=a.log_id
    WHERE 1 = 1
    #if(by==1 )
      and a.create_user_id = #fori(userIds)
    #else if(by==2)
      and FIND_IN_SET(#para(send_user_ids),a.send_user_ids)
      #if(userIds&&userIds.size()>0)
        and a.create_user_id in (#fori(userIds))
      #end
    #elseif(by==0)
        #if(!isAdmin)
        and (
        #if(userIds1&&userIds1.size() > 0)
        (a.create_user_id in (#fori(userIds1))) or
        #end
        (FIND_IN_SET(#para(send_user_ids),a.send_user_ids)
             #if(userIds2&&userIds2.size() > 0)
             and a.create_user_id in (#fori(userIds2))
             #end
        ))
        #end
    #end
    #if(beginTime)
      and date_format(a.create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
    #end
    #if(categoryId)
      and category_id = #para(categoryId)
    #end
    #if(logId)
      and a.log_id = #para(logId)
    #end
    #if(search)
     and (
        a.content like CONCAT('%',#para(search),'%')
        or a.tomorrow like CONCAT('%',#para(search),'%')
        or a.question like CONCAT('%',#para(search),'%')
     )
    #end
    order by log_id desc
  #end

  #sql ("queryLogRelation")
  SELECT
	    a.*, b.dept_id,
      b.realname,
      b.img as userImg,
      soal.customer_ids,
      soal.contacts_ids,
      soal.business_ids,
      soal.contract_ids
    FROM 72crm_oa_log as a left join 72crm_oa_log_relation as d on a.log_id = d.log_id
    LEFT JOIN 72crm_admin_user as b on a.create_user_id=b.user_id
     LEFT JOIN 72crm_oa_log_relation as soal on soal.log_id=a.log_id
    WHERE 1 = 2
    #if(businessIds)
      or d.business_ids like concat('%,',#para(businessIds),',%')
    #end
    #if(contactsIds)
      or d.contacts_ids like concat('%,',#para(contactsIds),',%')
    #end
    #if(contractIds)
      or d.contract_ids like concat('%,',#para(contractIds),',%')
    #end
    #if(customerIds)
      or d.customer_ids like concat('%,',#para(customerIds),',%')
    #end
  #end
  #sql("queryLogBulletin")
    SELECT
      COUNT(*) allNum,
	    COUNT(CASE WHEN create_user_id=#para(userId) and to_days(create_time) = to_days(now()) THEN 1 ELSE null END) as nowNum
    FROM
      72crm_oa_log
    WHERE
      date_format( create_time, '%Y-%m-%d' ) between #para(beginTime) and #para(endTime)
    AND create_user_id=#para(userId)
  #end
  #sql ("queryLogBulletinByType")
    #if(type == 1)
        select a.customer_id, a.customer_name, a.deal_status, a.create_time, a.owner_user_id,b.realname as owner_user_name,a.last_time
        from `72crm_crm_customer` a left join `72crm_admin_user` b on a.owner_user_id = b.user_id
        where a.customer_id in (#fori(typeIds))
        #if(search)
        and a.customer_name like concat('%',#para(search),'%')
        #end
    #else if(type == 2)
        select a.business_id,a.business_name,a.create_time, a.owner_user_id,b.realname as owner_user_name,a.last_time,c.name as statusName
        from `72crm_crm_business`  a
        left join `72crm_admin_user` b on a.owner_user_id = b.user_id
        left join 72crm_crm_business_status c on a.status_id = c.status_id
        where a.business_id in (#fori(typeIds))
        #if(search)
        and a.business_name like concat('%',#para(search),'%')
        #end
    #else if(type == 3)
        select a.contract_id,a.check_status,a.create_time,a.name,a.last_time,
        (select group_concat(realname) from `72crm_admin_user` where find_in_set(user_id,a.company_user_id)) as company_user_name
        from `72crm_crm_contract` a left join `72crm_crm_contacts` b on a.contacts_id = b.contacts_id
        where a.contract_id in (#fori(typeIds))
        #if(search)
        and a.name like concat('%',#para(search),'%')
        #end
    #else if(type == 4)
        select a.receivables_id,a.number,a.return_time, a.owner_user_id,b.realname as owner_user_name
        from `72crm_crm_receivables` a left join `72crm_admin_user` b on a.owner_user_id = b.user_id
        where a.receivables_id in (#fori(typeIds))
        #if(search)
        and a.number like concat('%',#para(search),'%')
        #end
    #else if(type == 5)
        select a.activity_id,b.img as userImg,b.realname,a.create_time,a.content,a.category,a.next_time,a.batch_id,a.business_ids,a.contacts_ids,a.activity_type,a.activity_type_id,a.status,
               (case a.activity_type
                    when 2 then (select customer_name from `72crm_crm_customer` where customer_id = a.activity_type_id)
                    when 1 then (select leads_name from `72crm_crm_leads` where leads_id = a.activity_type_id)
                    when 5 then (select business_name from `72crm_crm_business` where business_id = a.activity_type_id)
                    when 6 then (select name from `72crm_crm_contract` where contract_id = a.activity_type_id)
                    when 3 then (select name from `72crm_crm_contacts` where contacts_id = a.activity_type_id)
                   end) as crm_type_name
        from 72crm_crm_activity as a inner join 72crm_admin_user as b on a.create_user_id = b.user_id
        where  a.activity_id in (#fori(typeIds)) and a.activity_type = #para(crmType)
    #end
    #if(sortField)
        order by #(sortField) #(orderType)
    #end
  #end
  #sql ("queryLogRecordCount")
    SELECT a.activity_type as crmType,COUNT(a.activity_type) as count FROM 72crm_crm_activity a
    WHERE a.activity_id in (#fori(typeIds))
    group by a.activity_type
  #end

  #sql("queryExportList")
    SELECT
	    a.log_id,a.content,a.tomorrow,a.question,a.create_time,
	    (CASE WHEN a.category_id = 1 THEN '日报' WHEN a.category_id = 2 THEN '周报' WHEN a.category_id = 3 THEN '月报' END) as category,
	    (select GROUP_CONCAT(realname) from 72crm_admin_user where find_in_set(user_id,a.send_user_ids)) as send_user_name,
	    (select GROUP_CONCAT(name) from 72crm_admin_dept where find_in_set(dept_id,a.send_dept_ids)) as send_dept_name,
	  b.dept_id,
      b.realname as create_user_name,
      soal.customer_ids,
      soal.contacts_ids,
      soal.business_ids,
      soal.contract_ids,
      if(soal.customer_ids is null,null,(select GROUP_CONCAT(customer_name SEPARATOR'】、【') from 72crm_crm_customer where find_in_set(customer_id,soal.customer_ids) )) as customer_name,
      if(soal.contacts_ids is null,null,(select GROUP_CONCAT(name SEPARATOR'】、【') from 72crm_crm_contacts where find_in_set(contacts_id,soal.contacts_ids))) as contacts_name,
      if(soal.business_ids is null,null,(select GROUP_CONCAT(business_name SEPARATOR'】、【') from 72crm_crm_business where find_in_set(business_id,soal.business_ids))) as business_name,
      if(soal.contract_ids is null,null,(select GROUP_CONCAT(name SEPARATOR'】、【') from 72crm_crm_contract where find_in_set(contract_id,soal.contract_ids))) as contract_name,
      (SELECT COUNT(*) FROM 72crm_task_comment WHERE type_id=a.log_id and type='2') as replyNum
    FROM
	    72crm_oa_log as a
    LEFT JOIN 72crm_admin_user as b on a.create_user_id=b.user_id
    LEFT JOIN 72crm_oa_log_relation as soal on soal.log_id=a.log_id
    WHERE 1 = 1
    #if(by==1 )
      and a.create_user_id = #fori(userIds)
    #else if(by==2)
      and FIND_IN_SET(#para(send_user_ids),a.send_user_ids)
      #if(userIds&&userIds.size()>0)
        and a.create_user_id in (#fori(userIds))
      #end
    #elseif(by==0)
        #if(!isAdmin)
        and (
        #if(userIds1&&userIds1.size() > 0)
        (a.create_user_id in (#fori(userIds1))) or
        #end
        (FIND_IN_SET(#para(send_user_ids),a.send_user_ids)
             #if(userIds2&&userIds2.size() > 0)
             and a.create_user_id in (#fori(userIds2))
             #end
        ))
        #end
    #end
    #if(beginTime)
      and date_format(a.create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
    #end
    #if(categoryId)
      and category_id = #para(categoryId)
    #end
    #if(logId)
      and a.log_id = #para(logId)
    #end
    #if(search)
     and (
        a.content like CONCAT('%',#para(search),'%')
        or a.tomorrow like CONCAT('%',#para(search),'%')
        or a.question like CONCAT('%',#para(search),'%')
     )
    #end
    order by log_id desc
  #end

  #sql ("queryCommentList")
  select a.comment_id,a.content,a.main_id,b.realname as create_user_name,c.realname as reply_name
  from 72crm_task_comment as a left join 72crm_admin_user as b on a.user_id = b.user_id
  left join 72crm_admin_user as c on a.pid = c.user_id
  where type_id = ? and type = 2
  #end

  #sql ("queryCompleteStats")
  select count(distinct create_user_id) from 72crm_oa_log
  where create_time between #para(start) and #para(end) and create_user_id in (#fori(userIds)) and category_id = #para(type)
  #end

  #sql ("queryCompleteOaLogList")
  SELECT
	  a.*, b.dept_id,
      b.realname,
      b.img as userImg,
      soal.customer_ids,
      soal.contacts_ids,
      soal.business_ids,
      soal.contract_ids,
      (CASE WHEN c.log_id is null THEN 0 ELSE 1 END) as getBulletin,
      c.customer_num as customerCount,
      c.business_num as businessCount,
      c.contract_num as contractCount,
      c.receivables_money as receivablesMoney,
      c.activity_num as recordCount,
      (SELECT COUNT(*) FROM 72crm_task_comment WHERE type_id=a.log_id and type='2') as replyNum
    FROM
	    72crm_oa_log as a
    LEFT JOIN 72crm_admin_user as b on a.create_user_id=b.user_id
    LEFT JOIN 72crm_oa_log_record as c on a.log_id=c.log_id
    LEFT JOIN 72crm_oa_log_relation as soal on soal.log_id=a.log_id
    WHERE a.create_time between #para(start) and #para(end) and a.create_user_id in (#fori(userIds)) and a.category_id = #para(type)
    #if(search)
    and b.realname like CONCAT('%',#para(search),'%')
    #end
    #if(!isAdmin)
        and (
        #if(userIds1&&userIds1.size() > 0)
        (a.create_user_id in (#fori(userIds1))) or
        #end
        (FIND_IN_SET(#para(send_user_ids),a.send_user_ids)
             #if(userIds2&&userIds2.size() > 0)
             and a.create_user_id in (#fori(userIds2))
             #end
        ))
    #end
    order by a.create_time
  #end

  #sql ("queryIncompleteOaLogList")
  select user_id,realname from 72crm_admin_user
  where user_id not in (select create_user_id from 72crm_oa_log
                        where category_id = #para(type) and create_time between #para(start) and #para(end) and create_user_id in (#fori(userIds)))
  and user_id in (#fori(userIds))
  #if(search)
  and b.realname like CONCAT('%',#para(search),'%')
  #end
  #end
#end
