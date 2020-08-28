#namespace("work.task")
  #sql("myWorkLog")
    SELECT wtl.log_id, wtl.content, wtl.create_time ,au.img,au.realname
      FROM 72crm_work_task_log as wtl
      LEFT JOIN 72crm_admin_user as au on au.user_id = wtl.user_id
      where wtl.task_id = #para(taskId) and wtl.status != 4
      order by wtl.create_time desc
  #end
   #sql("queryTaskRelation")
     SELECT st.*,
      (select count(*) from 72crm_task_comment where type_id = st.task_id and type = 1) as commentCount,
      (select count(*) from 72crm_task where pid = st.task_id and status = 5) as childWCCount,
      (select count(*) from 72crm_task where pid = st.task_id) as childAllCount,
      (select count(*) from 72crm_admin_file where batch_id = st.batch_id) as fileCount
      FROM 72crm_task as st
      LEFT JOIN 72crm_task_relation as str on str.task_id = st.task_id
      where 1 = 2
       #if(businessIds)
        or str.business_ids like concat('%,',#para(businessIds),',%')
      #end
      #if(contactsIds)
        or str.contacts_ids like concat('%,',#para(contactsIds),',%')
      #end
      #if(contractIds)
       or str.contract_ids like concat('%,',#para(contractIds),',%')
      #end
      #if(customerIds)
        or str.customer_ids like concat('%,',#para(customerIds),',%')
      #end
  #end
  #sql("getTaskList")
    select a.*,
      (select count(*) from 72crm_task_comment where type_id = a.task_id and type = 1) as commentCount,
      (select count(*) from 72crm_task where pid = a.task_id and status = 5) as childWCCount,
      (select count(*) from 72crm_task where pid = a.task_id) as childAllCount,
      (select count(*) from 72crm_admin_file where batch_id = a.batch_id) as fileCount
      from 72crm_task a
      where a.pid = 0 and a.ishidden = 0
      #if(type == null  || type == 0)
        and (
          a.main_user_id in ( #for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end)
          or a.create_user_id in (#for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end )
          or (#for(i:userIds) #(for.index > 0 ? "or" : "") FIND_IN_SET(#para(i),a.owner_user_id) #end)
        )
      #end
      #if(type == 1)
       and  a.main_user_id in ( #for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end)
      #end
      #if(type == 2)
       and  a.create_user_id in (#for(i : userIds) #(for.index > 0 ? "," : "")#para(i) #end )
      #end
      #if(type == 3)
       and (#for(i:userIds) #(for.index > 0 ? "or" : "") FIND_IN_SET(#para(i),a.owner_user_id) #end)
      #end
      #if(status&&status==1)
       and a.status in(1,2)
      #elseif(status)
       and a.status = #para(status)
      #end
      #if(priority)
       and a.priority = #para(priority)
      #end
      #if(date)
       and date_format(a.stop_time,'%Y-%m-%d') = #para(date)
      #end
      #if(taskName)
       and a.name like concat('%', #para(taskName),'%')
      #end
      order by a.create_time desc
  #end

  #sql("getTaskListExport")
    select a.task_id,a.name,a.description,a.start_time,a.stop_time,a.create_time,b.realname as create_user_name,c.realname as main_user_name,
        (CASE WHEN a.priority = 0 THEN '无'
              WHEN a.priority = 1 THEN '低'
              WHEN a.priority = 2 THEN '中'
              WHEN a.priority = 3 THEN '高' END ) as priority,
        (select GROUP_CONCAT(realname) from 72crm_admin_user where find_in_set(user_id,a.owner_user_id)) as owner_user_name,
        (select GROUP_CONCAT(name) from 72crm_work_task_label where find_in_set(label_id,a.label_id)) as label_name,
      d.customer_ids,
      d.contacts_ids,
      d.business_ids,
      d.contract_ids,
      if(d.customer_ids is null,null,(select GROUP_CONCAT(customer_name SEPARATOR'】、【') from 72crm_crm_customer where find_in_set(customer_id,d.customer_ids))) as customer_name,
      if(d.contacts_ids is null,null,(select GROUP_CONCAT(name SEPARATOR'】、【') from 72crm_crm_contacts where find_in_set(contacts_id,d.contacts_ids))) as contacts_name,
      if(d.business_ids is null,null,(select GROUP_CONCAT(business_name SEPARATOR'】、【') from 72crm_crm_business where find_in_set(business_id,d.business_ids))) as business_name,
      if(d.contract_ids is null,null,(select GROUP_CONCAT(name SEPARATOR'】、【') from 72crm_crm_contract where find_in_set(contract_id,d.contract_ids))) as contract_name
      from 72crm_task as a left join 72crm_admin_user as b on a.create_user_id = b.user_id
      left join 72crm_admin_user as c on a.main_user_id = c.user_id
      left join 72crm_task_relation as d on a.task_id = d.task_id
      where a.pid = 0 and a.ishidden = 0
      #if(type == null  || type == 0)
        and (
          a.main_user_id in ( #for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end)
          or a.create_user_id in (#for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end )
          or (#for(i:userIds) #(for.index > 0 ? "or" : "") FIND_IN_SET(#para(i),a.owner_user_id) #end)
        )
      #end
      #if(type == 1)
       and  a.main_user_id in ( #for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end)
      #end
      #if(type == 2)
       and  a.create_user_id in (#for(i : userIds) #(for.index > 0 ? "," : "")#para(i) #end )
      #end
      #if(type == 3)
       and (#for(i:userIds) #(for.index > 0 ? "or" : "") FIND_IN_SET(#para(i),a.owner_user_id) #end)
      #end
      #if(status&&status==1)
       and a.status in(1,2)
      #elseif(status)
       and a.status = #para(status)
      #end
      #if(priority)
       and a.priority = #para(priority)
      #end
      #if(date)
       and date_format(a.stop_time,'%Y-%m-%d') = #para(date)
      #end
      #if(taskName)
       and a.name like concat('%', #para(taskName),'%')
      #end
      order by a.create_time desc
  #end
  #sql("queryTaskCount")
    SELECT
      COUNT(CASE WHEN a.`status` = '5' THEN 1 ELSE null END) as stopTask,
      COUNT(*) as allTask
    FROM
      72crm_task as  a
    where ishidden = 0 and a.pid = 0
      #if(type == null  || type == 0)
        and (
          a.main_user_id in ( #for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end)
          or a.create_user_id in (#for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end )
          or (#for(i:userIds) #(for.index > 0 ? "or" : "") FIND_IN_SET(#para(i),a.owner_user_id) #end)
        )
      #end
      #if(type == 1)
       and  a.main_user_id in ( #for(i : userIds) #(for.index > 0 ? "," : "") #para(i) #end)
      #end
      #if(type == 2)
       and  a.create_user_id in (#for(i : userIds) #(for.index > 0 ? "," : "")#para(i) #end )
      #end
      #if(type == 3)
       and (#for(i:userIds) #(for.index > 0 ? "or" : "") FIND_IN_SET(#para(i),a.owner_user_id) #end)
      #end
      #if(status&&status==1)
       and a.status in(1,2)
      #elseif(status)
       and a.status = #para(status)
      #end
      #if(priority)
       and a.priority = #para(priority)
      #end
      #if(date)
       and date_format(a.stop_time,'%Y-%m-%d') = #para(date)
      #end
      #if(taskName)
       and a.name like concat('%', #para(taskName),'%')
      #end
  #end
#end
