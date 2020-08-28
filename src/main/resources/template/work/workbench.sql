#namespace("work.workbench")
  #sql("myTask")
    select a.*,b.name as workName,
      c.realname as main_user_name,c.img as main_user_img,d.customer_ids,d.contacts_ids,d.business_ids,d.contract_ids,
      (select count(*) from 72crm_task_comment where type_id = a.task_id and type = 1) as commentCount,
      (select count(*) from 72crm_task where pid = a.task_id and status = 5) as childWCCount,
      (select count(*) from 72crm_task where pid = a.task_id) as childAllCount,
      (select count(*) from 72crm_admin_file where batch_id = a.batch_id) as fileCount
    from 72crm_task a left join 72crm_work b on a.work_id = b.work_id
      left join 72crm_admin_user as c on a.main_user_id = c.user_id
      left join 72crm_task_relation as d on a.task_id = d.task_id
    where (a.owner_user_id like concat('%,',#para(userId),',%') or a.main_user_id = #para(userId)) and a.pid = 0
    and a.ishidden = 0 and a.is_top = #para(isTop) and (a.status = 1 or a.status = 2) and a.is_archive = 0
  #end

  #sql ("myTaskExport")
  select a.task_id,a.name,a.description,a.start_time,a.stop_time,a.create_time,b.realname as create_user_name,c.realname as main_user_name,
        (CASE WHEN a.priority = 0 THEN '无'
              WHEN a.priority = 1 THEN '低'
              WHEN a.priority = 2 THEN '中'
              WHEN a.priority = 3 THEN '高' END ) as priority,
        (CASE WHEN a.is_top = 0 THEN '收件箱'
              WHEN a.is_top = 1 THEN '今天要做'
              WHEN a.is_top = 2 THEN '下一步要做'
              WHEN a.is_top = 3 THEN '以后要做' END ) as is_top,
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
  where (a.owner_user_id like concat('%,',#para(userId),',%') or a.main_user_id = #para(userId)) and a.pid = 0
  and a.ishidden = 0 and (a.status = 1 or a.status = 2) and a.is_archive = 0
  order by a.is_top
  #end

  #sql("dateList")
    select task_id,name,date_format(start_time,'%Y-%m-%d') as start_time,date_format(stop_time,'%Y-%m-%d') as stop_time,priority,create_time,update_time
    from 72crm_task a
    where (a.main_user_id = #para(userId) or a.owner_user_id like concat('%,',#para(userId),',%')) and ( a.stop_time between #para(startTime) and #para(endTime) or a.stop_time is null ) and a.pid = 0 and (a.status = 1 or a.status = 2) and a.is_archive = 0  and a.ishidden = 0
  #end

  #sql ("queryTaskLabel")
  select b.task_id,a.label_id,a.name as label_name,a.color
  from 72crm_work_task_label as a left join 72crm_task as b on find_in_set(a.label_id,b.label_id)
  where b.task_id in (#fori(taskIds))
  #end
#end
