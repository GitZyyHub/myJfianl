#namespace("work.task")

    #sql ("advancedSearch")

        SELECT
            t1.task_id,
            t1.class_id,
            t1.task_name,
            t1.milestone_id,
            t1.task_num,
            t1.completeness,
            t1.start_time,
            t1.end_time,
            t1.description,
            t1.create_time,
            t1.update_time,
            t1.before_id,
            t1.status,
            t1.priority,
            t1.difficulty,
            t1.degree,
            t1.batch_id,
            t1.main_user_id,
            t1.pid,
            (select task_name from 72crm_task where t1.pid=task_id) p_name,
            (select work_name from 72crm_work where t1.work_id=work_id) work_name,
            (select task_name from 72crm_task where t1.before_id=task_id) before_name,
            (select milestone_name from 72crm_milestone where t1.milestone_id=milestone_id) milestone_name,
            (select realname from 72crm_admin_user where t1.main_user_id=user_id) main_user_name,
            (select realname from 72crm_admin_user where t1.create_user_id=user_id) create_user_name
        FROM
            72crm_task AS t1
            #if(_status)
                 left join 72crm_work_user_record u on u.num_id=t1.task_id and type=1
            #end
            where 1=1
            #if(dataAuth)
            and task_id in (#fori(dataAuth))
        #end
        #if(_status)
            and status!=#para(_status) and ((main_user_id=#para(_userId) or create_user_id=#para(_userId)) or u.user_id=#para(_userId))
        #end
        #if(_milestone_id)
            and t1.milestone_id=#para(_milestone_id)
        #end
        #if(_pid)
         and t1.pid=#para(_pid)
        #end
        #if(_work_id)
            and t1.work_id=#para(_work_id)
        #end
        #if(_before_id)
            and t1.before_id=#para(_before_id)
        #end
        #for(query:queryList)
                    #(query.get("sql"))
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
            LIMIT #(page),#(limit)
        #end
    #end

    #sql("waiting")

        SELECT
            t1.task_id,
            t1.class_id,
            t1.task_name,
            t1.milestone_id,
            t1.task_num,
            t1.completeness,
            t1.start_time,
            t1.end_time,
            t1.description,
            t1.create_time,
            t1.update_time,
            t1.before_id,
            t1.status,
            t1.priority,
            t1.difficulty,
            t1.degree,
            t1.batch_id,
            t1.main_user_id,
            t1.pid,
            (select task_name from 72crm_task where t1.pid=task_id) p_name,
            (select work_name from 72crm_work where t1.work_id=work_id) work_name,
            (select task_name from 72crm_task where t1.before_id=task_id) before_name,
            (select milestone_name from 72crm_milestone where t1.milestone_id=milestone_id) milestone_name,
            (select realname from 72crm_admin_user where t1.main_user_id=user_id) main_user_name,
            (select realname from 72crm_admin_user where t1.create_user_id=user_id) create_user_name
        FROM
            72crm_task AS t1 where 1=1
        #for(query:queryList)
            #(query.get("sql"))
        #end
        #if(dataAuth)
            and task_id in (#fori(dataAuth))
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
    #end

    #sql ("queryTaskPageListCount")
    select count(1) from 72crm_task AS t1
         #if(_status)
                 left join 72crm_work_user_record u on u.num_id=t1.task_id and type=1
            #end
            where 1=1
        #if(_status)
            and status!=#para(_status) and ((main_user_id=#para(_userId) or create_user_id=#para(_userId)) or u.user_id=#para(_userId))
        #end
        #if(_milestone_id)
            and t1.milestone_id=#para(_milestone_id)
        #end
        #if(_work_id)
            and t1.work_id=#para(_work_id)
        #end
        #if(_before_id)
            and t1.before_id=#para(_before_id)
        #else
            and t1.before_id=0
        #end
        #for(query:queryList)
                    #(query.get("sql"))
        #end
    #end


    #sql ("queryById")
        SELECT
            t1.task_id,
            t1.work_id,
            (select work_name from 72crm_work where work_id=t1.work_id) work_name,
            t1.task_num,
            t1.class_id,
            t1.owner_user_id,
            t1.pid,
            t1.task_name,
            t1.description,
            t1.`status`,
            t1.start_time,
            t1.end_time,
            t1.completeness,
            t1.priority,
            t1.difficulty,
            t1.degree,
            t1.create_time,
            t1.update_time,
            t1.before_id,
            main_user_id,
            (select realname from 72crm_admin_user where user_id=t1.main_user_id) main_user_name,
            milestone_id,
            (select milestone_name from 72crm_milestone where milestone_id=t1.milestone_id) milestone_name,
            create_user_id,
            (select realname from 72crm_admin_user where user_id=t1.main_user_id) create_user_name
        FROM
            72crm_task AS t1
        WHERE t1.task_id = ?
    #end

    #sql ("countTaskBySubtasksId")
        select count(1) from 72crm_task_hours where subtasks_id in (#fori(ids))
    #end

    #sql("countTaskByBeforeId")
        select count(1) from 72crm_task where before_id in (#fori(ids))
    #end

    #sql ("deleteTask")
        delete from 72crm_task where task_id in (#fori(ids))
    #end
    #sql ("deleteTaskByPid")
        delete from 72crm_task where pid in (#fori(ids))
    #end

    #sql("findOwnerUser")
        select user_id,realname from 72crm_admin_user where user_id in (select user_id from 72crm_work_user_record where num_id=? and type=1)
    #end

    #sql ("queryTaskByIdExport")
         SELECT
            t1.class_id,
            t1.task_name,
            t1.task_num,
            t1.completeness,
            t1.start_time,
            t1.end_time,
            t1.description,
            t1.create_time,
            t1.status,
            t1.priority,
            t1.difficulty,
            t1.degree,
            (select work_name from 72crm_work where t1.work_id=work_id) work_name,
            (select task_name from 72crm_task where t1.before_id=task_id) before_name,
            (select milestone_name from 72crm_milestone where t1.milestone_id=milestone_id) milestone_name,
            (select realname from 72crm_admin_user where t1.main_user_id=user_id) main_user_name,
            (select realname from 72crm_admin_user where t1.create_user_id=user_id) create_user_name
        FROM
            72crm_task AS t1
        where 1=1
        and work_id=#para(workId) and pid is null
    #end

     #sql ("querySubtasksExport")
         SELECT
            t1.class_id,
            t1.task_name,
            t1.task_num,
            t1.completeness,
            t1.start_time,
            t1.end_time,
            t1.description,
            t1.create_time,
            t1.status,
            t1.priority,
            t1.difficulty,
            t1.degree,
            (select task_name from 72crm_task where t1.pid=task_id) pid,
            (select work_name from 72crm_work where t1.work_id=work_id) work_name,
            (select task_name from 72crm_task where t1.before_id=task_id) before_name,
            (select milestone_name from 72crm_milestone where t1.milestone_id=milestone_id) milestone_name,
            (select realname from 72crm_admin_user where t1.main_user_id=user_id) main_user_name,
            (select realname from 72crm_admin_user where t1.create_user_id=user_id) create_user_name
        FROM
            72crm_task AS t1
        where 1=1
        #if(taskIds)
            and pid in (#fori(taskIds))
        #end
    #end


    #sql ("findTaskByFileId")
        select user_id from 72crm_task_relation where task_id = (select task_id from 72crm_task where batch_id=?) and user_id = ?
    #end

    #sql ("findTaskFileByBatchId")
        select * from 72crm_admin_file where batch_id = #para(batchId)
    #end

    #sql ("queryResourceById")
        SELECT r.*,u.username AS mainUserName  FROM `72crm_work_resources_setting` AS r
        INNER JOIN 72crm_admin_user AS u ON r.main_user_id = u.user_id
        WHERE r.resources_id =?
    #end



    #sql("queryTaskHoursLockPageList")
        select * from 72crm_task_hours_lock
    #end










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













