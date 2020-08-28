#namespace("admin.message")
  #sql("queryList")
    select a.message_id, a.title, a.content, a.label, a.type, a.type_id, a.create_user, a.recipient_user, a.create_time, a.is_read, a.read_time,b.realname,(CASE WHEN  DATE_SUB(CURDATE(), INTERVAL 7 DAY) >= date(a.create_time) THEN 0 ELSE 1 END) as valid
    from 72crm_admin_message as a
    left join 72crm_admin_user as b on a.create_user=b.user_id
    where
    recipient_user = #para(userId)
    #if(is_read)
      and is_read=#para(is_read)
    #end
    #if(label)
      and label = #para(label)
    #end
    #if(type)
      and type = #para(type)
    #end
    and a.create_time < now()
    order by a.create_time desc
  #end
  #sql("queryUnreadCount")
    select count(1) as allCount,
    count(if(label = 4,1,null)) as announceCount,
    count(if(label = 3,1,null)) as examineCount,
    count(if(label = 1,1,null)) as taskCount,
    count(if(label = 2,1,null)) as logCount,
    count(if(label = 6,1,null)) as crmCount,
    count(if(label = 5 and create_time < now(),1,null)) as eventCount
    from 72crm_admin_message as a where is_read = 0 and recipient_user =  #para(userId)  and create_time < now()
  #end

  #sql ("readMessage")
  update 72crm_admin_message set is_read = 1 where message_id = ?
  #end

  #sql ("readAllMessage")
  update 72crm_admin_message set is_read = 1 where  recipient_user = #para(userId)
  #if(label)
    and label = #para(label)
  #end
  #end

  #sql ("clear")
  delete from 72crm_admin_message where recipient_user = #para(userId) and is_read = 1
   #if(label)
   and label = #para(label)
   #end
  #end
#end
