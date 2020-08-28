#namespace("work")
  #sql("queryWorkNameList")
    select  a.work_id,a.name,a.owner_user_id,a.color,0 as collect,999 as orderNum,a.is_system_cover,a.is_open,a.create_user_id,a.owner_role,
    (CASE WHEN is_system_cover = 0 THEN b.file_path
          WHEN is_system_cover = 1 THEN a.cover_url END ) as cover_url
    from 72crm_work as a left join 72crm_admin_file as b on ifnull(a.batch_id,0) = b.batch_id
    where a.status = 1
      #if(userId)
      and (a.owner_user_id like concat('%,',#para(userId),',%') and a.is_open = 0) or a.is_open = 1
      #end
  #end
  #sql("queryWorkOrderList")
    select  work_id,order_num
    from `72crm_work_order`
    where 1 = 1 and user_id = #para(userId)
  #end
  #sql("queryWorkCollectWorkId")
  select work_id from `72crm_work_collect` where user_id = #para(userId)
  #end
  #sql("queryOwnerWorkIdList")
      select  a.work_id
      from 72crm_work a
      where 1 = 1
        #if(userId)
      and (owner_user_id like concat('%,',#para(userId),',%') and is_open = 0) or is_open = 1
        #end
  #end
  #sql("queryTaskByWorkId")
    select a.*,ifnull(b.name,'未分组') as className,ifnull(b.class_id,0) as classId,b.order_num as classOrder,
      c.realname as main_user_name,c.img as main_user_img,d.customer_ids,d.contacts_ids,d.business_ids,d.contract_ids,
      (select count(*) from 72crm_task_comment where type_id = a.task_id and type = 1) as commentCount,
      e.childWCCount,e.childAllCount,
      (select count(*) from 72crm_admin_file where batch_id = a.batch_id) as fileCount
      from 72crm_task a left join 72crm_work_task_class b on a.class_id = b.class_id
      left join 72crm_admin_user as c on a.main_user_id = c.user_id
      left join 72crm_task_relation as d on a.task_id = d.task_id
      LEFT JOIN (
		SELECT COUNT(*) as childAllCount,COUNT(status=5 or null) as childWCCount,pid FROM 72crm_task GROUP BY pid
	  ) as e on e.pid = a.task_id
      where a.work_id = #para(workId) and a.status != 3  and a.ishidden = 0 and is_archive = 0
      #if(classId)
        and a.class_id = #para(classId)
      #end
      #if(userIds && userIds.size()>0)
        and (
        #for(i : userIds)
        #(for.index > 0 ? " or " : "")
        a.owner_user_id like concat('%,',#para(i),',%') or  a.main_user_id = #para(i)
        #end
          )
      #end
      #if(labelIds && labelIds.size()>0)
        and (
        #for(i : labelIds)
          #(for.index > 0 ? " or " : "")
            a.label_id like  concat('%,',#para(i),',%')
        #end
        )
      #end
      #if(stopTimeType)
        and
        case #para(stopTimeType)  when 1 then  date_format(a.stop_time,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d')
                                  when 2 then  date_format(a.stop_time,'%Y-%m-%d') = date_format(DATE_ADD(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')
                                  when 3 then  YEARWEEK(date_format(a.stop_time,'%Y-%m-%d')) = YEARWEEK(now())
                                  when 4 then  date_format(a.stop_time,'%Y%m') = DATE_FORMAT( CURDATE( ) , '%Y%m' )
                                  when 5 then  a.stop_time is null
                                  when 6 then  a.stop_time < now()
                                  when 7 then  date_format(a.update_time,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d') end
      #end
      #if(taskName)
      and a.name like concat('%',#para(taskName),'%')
      #end

      order by b.order_num
  #end

  #sql("queryOwnerTaskListByWorkId")
    select a.*,c.realname as main_user_name,c.img as main_user_img,d.customer_ids,d.contacts_ids,d.business_ids,d.contract_ids,
      (select count(*) from 72crm_task_comment where type_id = a.task_id and type = 1) as commentCount,
      e.childWCCount,e.childAllCount,
      (select count(*) from 72crm_admin_file where batch_id = a.batch_id) as fileCount
      from 72crm_task a left join 72crm_work_task_class b on a.class_id = b.class_id
      left join 72crm_admin_user as c on a.main_user_id = c.user_id
      left join 72crm_task_relation as d on a.task_id = d.task_id
      LEFT JOIN (
		SELECT COUNT(*) as childAllCount,COUNT(status=5 or null) as childWCCount,pid FROM 72crm_task GROUP BY pid
	  ) as e on e.pid = a.task_id
      where a.work_id = #para(workId) and a.status != 3  and a.ishidden = 0 and is_archive = 0
      #if(userIds && userIds.size()>0)
        and (
        #for(i : userIds)
        #(for.index > 0 ? " or " : "")
        a.owner_user_id like concat('%,',#para(i),',%') or  a.main_user_id = #para(i)
        #end
          )
      #end
      #if(labelIds && labelIds.size()>0)
        and (
        #for(i : labelIds)
          #(for.index > 0 ? " or " : "")
            a.label_id like  concat('%,',#para(i),',%')
        #end
        )
      #end
      #if(stopTimeType)
        and
        case #para(stopTimeType)  when 1 then  date_format(a.stop_time,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d')
                                  when 2 then  date_format(a.stop_time,'%Y-%m-%d') = date_format(DATE_ADD(NOW(),INTERVAL 1 DAY),'%Y-%m-%d')
                                  when 3 then  YEARWEEK(date_format(a.stop_time,'%Y-%m-%d')) = YEARWEEK(now())
                                  when 4 then  date_format(a.stop_time,'%Y%m') = DATE_FORMAT( CURDATE( ) , '%Y%m' )
                                  when 5 then  a.stop_time is null
                                  when 6 then  a.stop_time < now()
                                  when 7 then  date_format(a.update_time,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d') end
      #end
      #if(taskName)
      and a.name like concat('%',#para(taskName),'%')
      #end
  #end

  #sql ("queryOwnerUser")
  select user_id,realname from 72crm_admin_user where user_id in (#fori(userIds))
  #end

  #sql("workStatistics")
    select count(*) as allCount,
      count((status=1 and is_archive=0)or null) as unfinished,
      count((status=2 and is_archive=0) or null) as overdue,
      count((status=5 and is_archive=0) or null) as complete,
      count(is_archive=1 or null) as archive,
      ifnull(ROUND((count((status=5 and is_archive=0) or null)/count(is_archive=0 or null))*100,2),0) as completionRate,
      ifnull(ROUND((count((status=2 and is_archive=0) or null)/count(is_archive=0 or null))*100,2),0) as overdueRate
      from 72crm_task where 1 = 1 and ishidden = 0
      #if(workId)
        and work_id = #para(workId)
      #end
      #if(workIds)
        and work_id in (#para(workIds))
      #end
      #if(userId)
        and (main_user_id = #para(userId) or owner_user_id like concat('%,',#para(userId),',%'))  and (is_archive = 0 or (is_archive = 1 and status = 5))
      #end
      #if(mainUserId)
        and main_user_id = #para(mainUserId)
      #end
  #end

  #sql("queryRoleList")
  select role_id,role_name,remark from 72crm_admin_role where role_type in (5,6) and status = 1
  #end

  #sql ("queryOwnerRoleList")
  select a.user_id,b.realname,a.role_id,c.role_name,b.img
  from 72crm_work_user as a left join 72crm_admin_user as b on a.user_id = b.user_id
  left join 72crm_admin_role as c on a.role_id = c.role_id
  where a.work_id = ?
  #end
  #sql ("queryRealmByRoleId")
    select concat((select realm from `72crm_admin_menu` where menu_id = b.parent_id),':',b.realm) from `72crm_admin_role_menu` a left join `72crm_admin_menu` b on a.menu_id = b.menu_id where a.role_id = ? and b.menu_type = 3
  #end
  #sql("archList")
  select a.*,b.name as workName,
      c.realname as main_user_name,c.img as main_user_img,d.customer_ids,d.contacts_ids,d.business_ids,d.contract_ids,
        (select count(*) from 72crm_task_comment where type_id = a.task_id and type = 1) as commentCount,
         (select count(*) from 72crm_task where pid = a.task_id and status = 5) as childWCCount,
         (select count(*) from 72crm_task where pid = a.task_id) as childAllCount,
         (select count(*) from 72crm_admin_file where batch_id = a.batch_id) as fileCount
  from 72crm_task a left join 72crm_work b on a.work_id = b.work_id
  left join 72crm_admin_user as c on a.main_user_id = c.user_id
  left join 72crm_task_relation as d on a.task_id = d.task_id
  where a.work_id = ? and a.is_archive = 1 and a.ishidden = 0
  #end
  #sql("queryTaskFileByWorkId")
    SELECT a.file_id,a.name, a.size,a.create_user_id,b.realname as create_user_name,a.create_time,a.file_path,a.file_type,a.batch_id
    FROM `72crm_admin_file` as a inner join `72crm_admin_user` as b on a.create_user_id = b.user_id
    where a.batch_id in (select batch_id from `72crm_task` where work_id = #para(workId) and ishidden = 0)
  #end
  #sql("leave")
    update `72crm_task` set main_user_id = null where work_id =  #para(workId) and main_user_id = #para(userId);
  #end
  #sql("leave1")
    update `72crm_task` set owner_user_id = replace(owner_user_id,concat(',',#para(userId),','),',') where work_id = #para(workId) and  owner_user_id like concat(',',#para(userId),',');
  #end

  #sql ("queryMenuByRoleId")
  SELECT a.realm,a.menu_id,a.parent_id FROM 72crm_admin_menu as a
      LEFT JOIN 72crm_admin_role_menu as b on a.menu_id=b.menu_id
  where b.role_id = ?
  #end

  #sql("getTaskOwnerOnWork")
    select user_id,img,realname from `72crm_admin_user` where user_id in (select main_user_id from `72crm_task` where ishidden = 0 and work_id in (?))
  #end
  #sql("queryWorkAuth")
    SELECT COUNT(1) FROM 72crm_work WHERE FIND_IN_SET(#para(userId),owner_user_id)
  #end
  #sql ("queryProjectUser")
  select user_id,realname from 72crm_admin_user as a left join 72crm_admin_user_role as b on a.user_id = b.user_id
  left join 72crm_admin_role_menu as c on b.role_id = c.role_id
  where d.menu_id = ?
  #end

  #sql ("workTaskExport")
  select a.task_id,a.name,a.description,a.start_time,a.stop_time,a.create_time,b.realname as create_user_name,c.realname as main_user_name,
        (CASE WHEN a.priority = 0 THEN '无'
              WHEN a.priority = 1 THEN '低'
              WHEN a.priority = 2 THEN '中'
              WHEN a.priority = 3 THEN '高' END ) as priority,
         e.name as class_name,
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
  left join 72crm_work_task_class as e on a.class_id = e.class_id
  left join 72crm_work as f on a.work_id = f.work_id
  where a.work_id = #para(workId) and a.pid = 0 and a.ishidden = 0 and (a.status = 1 or a.status = 2 or a.status = 5) and a.is_archive = 0
  order by e.order_num,a.order_num
  #end

  #sql ("queryWorkOwnerUser")
  select realname,user_id from 72crm_admin_user
  where FIND_IN_SET(user_id,(select owner_user_id from 72crm_work where work_id = ?))
  #end

  #sql ("queryWorkAuthByUserId")
  select b.realm from 72crm_admin_role_menu as a left join 72crm_admin_menu as b on a.menu_id = b.menu_id
  where a.role_id = ? and b.parent_id = ?
  #end
#end
