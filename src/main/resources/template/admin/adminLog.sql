#namespace("admin.log")
  #sql ("queryLoginLogList")
  select a.id,a.login_time,a.ip_address,a.login_address,a.device_type,a.core,a.platform,a.imei,b.realname,
      (CASE WHEN a.auth_result = 1 THEN '成功' WHEN a.auth_result = 2 THEN '失败' END )as auth_result
  from 72crm_admin_login_log as a left join 72crm_admin_user as b on a.create_user_id = b.user_id
  where 1=1
  #if(startTime&&endTime)
  and a.login_time between #para(startTime) and #para(endTime)
  #end
  #if(userIds)
  and a.create_user_id in (#fori(userIds))
  #end
  order by a.login_time desc
  #end

  #sql ("querySystemLogList")
  select a.id,a.create_time,a.ip_address,a.types,a.behavior,a.object,a.detail,b.realname
  from 72crm_admin_system_log as a left join 72crm_admin_user as b on a.create_user_id = b.user_id
  where 1=1
  #if(startTime&&endTime)
  and a.loginTime between #para(startTime) and #para(endTime)
  #end
  #if(userIds)
  and a.create_user_id in (#fori(userIds))
  #end
  #if(model)
  and a.types = #para(types)
  #end
  #if(behavior)
  and a.behavior like concat('%', #para(behavior), '%')
  #end
  order by a.create_time desc
  #end

  #sql ("queryActionRecordList")
  select a.id,a.create_time,a.ip_address,a.types,a.behavior,a.object,a.detail,b.realname
  from 72crm_crm_action_record as a left join 72crm_admin_user as b on a.create_user_id = b.user_id
  where 1=1
  #if(startTime&&endTime)
  and a.create_time between #para(startTime) and #para(endTime)
  #end
  #if(userIds)
  and a.create_user_id in (#fori(userIds))
  #end
  #if(types)
  and a.types in (#fori(types))
  #end
  #if(behavior)
  and a.behavior in (#fori(behavior))
  #end
  order by a.create_time desc
  #end
#end
