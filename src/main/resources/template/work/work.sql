#namespace("work")

    #sql("queryStructure")
        select column_name,column_comment,data_type from information_schema.columns where table_name=?
    #end

    #sql ("queryDataAuth")
        select num_id from 72crm_work_user_record where user_id in (#fori(ids)) and type=#para(ty)
        #if(workId)
         and work_id = #para(workId)
        #end
    #end

    #sql ("queryDataUserId")
        select user_id from 72crm_work_user_record where 1=1
        #if(ids)
        and num_id in (#fori(ids))
        #end
        #if(ty)
        and type=#para(ty)
        #end
    #end
    #sql ("queryHistoricalRecordByType")
        SELECT * FROM 72crm_work_historical_record WHERE module=?
    #end

    #sql ("updateCheckStatusById")
        update 72crm_work set check_status=? where work_id=?
    #end

    #sql ("updateCheckStatusByIdThrough")
        update 72crm_work set check_status=?,examine_time=? where work_id=?
    #end

     #sql ("advancedSearch")
     select * from(
        SELECT
            (select realname from 72crm_admin_user where user_id=w.create_user_id) create_user_name,
            (select realname from 72crm_admin_user where user_id=w.manager_id) manager_name,
            manager_id,
            (select realname from 72crm_admin_user where user_id=w.sales_id) sales_name,
            sales_id,
            (select `name` from 72crm_admin_dept where dept_id=w.dept_id) dept_name,
            (select `name` from 72crm_admin_dept where dept_id=w.area) area,
            work_num,
            work_id,
            work_name,
            batch_id,
            create_time,
            work_phase,
            work_type,
            start_time,
            end_time,
            update_time
        from 72crm_work AS w
         where 1=1
         #if(workPhase)
            and w.work_phase != #para(workPhase)
         #end
        #if(dataAuth)
            and work_id in (#fori(dataAuth))
        #end
        ) as views where 1=1
        #for(query:queryList)
            #(query.get("sql"))
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
          LIMIT #(page),#(limit)
        #end
    #end

    #sql("queryWoekPageListCount")
        SELECT count(1) from 72crm_work AS w
        where status != 3
        #for(query : queryList)
            #if(query.get("type")==1) #(query.get("sql")) #end
        #end
        #if(workPhase)
            and w.work_phase != #para(workPhase)
         #end
         #if(dataAuth)
            and work_id in (#fori(dataAuth))
        #end
    #end

    #sql("waiting")
        SELECT
                (select realname from 72crm_admin_user where user_id=w.create_user_id) create_user_name,
                (select realname from 72crm_admin_user where user_id=w.manager_id) manager_name,
                (select realname from 72crm_admin_user where user_id=w.sales_id) sales_name,
                (select `name` from 72crm_admin_dept where dept_id=w.dept_id) dept_name,
                (select `name` from 72crm_admin_dept where dept_id=w.area) area,
                work_num,
                status,
                work_id,
                work_name,
                batch_id,
                create_time,
                description,
                work_phase,
                work_type,
                start_time,
                end_time,
                update_time
        from 72crm_work AS w
        WHERE 1=1
        and w.work_id in (SELECT work_id from 72crm_work_user_record WHERE user_id=#para(userId) and type=4)
        #for(query:queryList)
            #(query.get("sql"))
        #end
        #if(dataAuth)
            and work_id in (#fori(dataAuth))
        #end
        #if(isWaiting)
            and work_phase!='结项'
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
          LIMIT #(page),#(limit)
        #end
    #end

    #sql("waitingCount")
        SELECT count(1) from 72crm_work AS w
        where status != 3
        #for(query:queryList)
            #(query.get("sql"))
        #end
        #if(workPhase)
            and w.work_phase != #para(workPhase)
         #end
         #if(dataAuth)
            and work_id in (#fori(dataAuth))
        #end
    #end


    #sql ("queryWorkByIdExport")
    select * from (
        SELECT

            w.work_id,
            w.work_name,
            w.create_time,
            w.description,
            w.area,
            w.work_type,
            d.name AS `dept_id`,
            u2.realname AS sales_id,
            u3.realname AS create_user_id,
            u4.realname AS manager_id,
            w.start_time,
            w.end_time
        from 72crm_work AS w
        LEFT JOIN 72crm_admin_dept AS d ON d.dept_id=w.dept_id
        LEFT JOIN 72crm_admin_user AS u2 ON u2.user_id=w.sales_id
        LEFT JOIN 72crm_admin_user AS u3 ON u3.user_id=w.create_user_id
        LEFT JOIN 72crm_admin_user AS u4 ON u4.user_id=w.manager_id

        GROUP BY  w.batch_id) as a
        #if(workIds)
         where work_id in (#fori(workIds))
        #end

    #end

    #sql ("queryWorkExtensionById")
        SELECT work_name,examine,work_type,solution,risk,unscramble,target,contract_amount,effective_amount,implement_cost,
        maintenance_cost,budget,practical_cost,customer_name,customer_contact,customer_calls,ifnull(forecast_work,0) forecast_work
        FROM 72crm_work WHERE work_id=?
    #end

    #sql("queryWorkSysInformationById")
        SELECT create_time,create_user_id,update_time FROM 72crm_work WHERE work_id=?
    #end

    #sql("updateWorkPhase")
        update 72crm_work set work_phase=#para(phase) where work_id = #para(id)
    #end


    #sql("findWorkById")
        SELECT
            w.work_num,
            w.status,
            w.work_id,
            w.work_name,
            w.batch_id,
            w.create_time,
            w.description,
            w.work_phase,
            w.work_type,
            w.dept_id,
            w.area,
            w.sales_id,
            w.create_user_id,
            w.manager_id,
            w.start_time,
            w.end_time,
            w.update_time,
            w.unscramble,
            w.examine,
            w.solution,
            w.risk,
            w.target,
            w.contract_amount,
            w.effective_amount,
            w.implement_cost,
            w.maintenance_cost,
            w.budget,
            w.practical_cost,
            w.customer_name,
            w.customer_contact,
            w.forecast_work,
            w.customer_calls,
            w.label_id,
            w.check_status,
            w.examine_record_id
        FROM
            `72crm_work` w
        WHERE
            work_id = ?
    #end



    #sql ("queryWorkUserByNumId")
        select user_id,realname from 72crm_admin_user WHERE user_id in (SELECT user_id FROM `72crm_work_user_record` WHERE num_id =? and type=?)
    #end


    #sql ("findMilestoneById")
        SELECT
            m.milestone_id,
            m.milestone_num,
            m.create_time,
            m.milestone_name,
            m.degree,
            m.start_time,
            m.end_time,
            (select work_num from 72crm_work where m.work_id=work_id) work_num,
			m.update_time,
			(select realname from 72crm_admin_user where m.main_user_id = user_id) main_user_name,
            m.instructions,
            (select work_name from 72crm_work where m.work_id=work_id) work_name,
			(select task_name from 72crm_task where m.task_id=task_id) task_name
        from 72crm_milestone m
	    where 1=1
		#if(_workId)
		and m.work_id=#para(_workId)
		#end
        #for(query:queryList)
            #(query.get("sql"))
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
          LIMIT #(page),#(limit)
        #end
    #end

    #sql ("queryMilestonePageListCount")
    SELECT count(1) from 72crm_milestone
        where 1=1
		#if(_workId)
		    and work_id=#para(_workId)
		#end
        #for(query:queryList)
            #(query.get("sql"))
        #end
    #end

    #sql ("updateMilestoneDegree")
        update 72crm_milestone set degree=#para(degree) where milestone_id=#para(ids)
    #end

    #sql ("queryMilestoneById")
        SELECT m.*,w.work_name,t.task_name,u.username AS user_name from 72crm_milestone m
        left JOIN 72crm_work w ON m.work_id=w.work_id
        left JOIN 72crm_task t ON m.task_id=t.task_id
        left JOIN 72crm_admin_user u ON m.main_user_id=u.user_id
        WHERE m.milestone_id=?
    #end

    #sql("milestoneExport")

    #end

    #sql ("delRiskByids")
        DELETE FROM 72crm_work_risk where risk_id in (#para(ids))
    #end
    #sql ("queryRiskByBatchId")
        SELECT
            `risk_id`,
            `risk_type`	,
            `risk_level`,
            `scope`,
            `solve_time`,
            `risk_status`,
            `solve_advice`,
            `detailed`,
            `found_time`,
            `found_phase`,
            `found_way`,
            `direction`,
            tracking_user_id,
            solve_user_id,
            found_user_id

        FROM 72crm_work_risk r
        WHERE 1=1
        #if(workId)
            and r.work_id=#para(workId)
        #end
        #if(search)
            and found_phase like concat('%', #para(search), '%')
        #end
        order by update_time desc
    #end
    #sql ("queryRiskById")
    SELECT
        r.*,
        (SELECT realname from 72crm_admin_user WHERE user_id=r.tracking_user_id) tracking_user_name,
        (SELECT realname from 72crm_admin_user WHERE user_id=r.solve_user_id) solve_user_name,
        (SELECT realname from 72crm_admin_user WHERE user_id=r.found_user_id) found_user_name
    from 72crm_work_risk r
    WHERE r.risk_id=?
    #end
    #sql ("queryRiskByIdExport")
        SELECT
            `risk_type`,
            `risk_level`,
            `scope`,
            `solve_time`,
            `risk_status`,
            `solve_advice`,
            `detailed`,
            `found_time`,
            `found_phase`,
            `found_way`,
            `direction`,
            (SELECT realname from 72crm_admin_user WHERE user_id=r.tracking_user_id) tracking_user_name,
            (SELECT realname from 72crm_admin_user WHERE user_id=r.solve_user_id) solve_user_name,
            (SELECT realname from 72crm_admin_user WHERE user_id=r.found_user_id) found_user_name
        from 72crm_work_risk r
        #if(riskIds)
            WHERE r.risk_id in (#fori(riskIds))
        #end
    #end



    #sql ("queryChangePageList")
        SELECT c.*,
        (select realname from 72crm_admin_user where user_id=c.forward_main_user_id) forward_main_user_name
        FROM 72crm_work_change c
        WHERE 1=1
        #if(_workId)
        and c.work_id=#para(_workId)
        #end
        #if(_batch_id)
        and c.batch_id=#para(_batchId)
        #end
        #if(_search)
            and c.change_project LIKE concat('%', #para(_search), '%')
        #end
        order by update_time desc
        #if(page&&limit)
            LIMIT #(page),#(limit)
        #end

    #end
    #sql ("queryChangePageListCount")
        SELECT count(1)
        FROM 72crm_work_change c
        WHERE 1=1
        #if(_workId)
        and c.work_id=#para(_workId)
        #end
        #if(_batch_id)
        and c.batch_id=#para(_batchId)
        #end
        #if(_search)
            and c.change_project LIKE concat('%', #para(_search), '%')
        #end

    #end


    #sql ("queryChangeUserByChangeId")
        SELECT user_id,realname FROM 72crm_admin_user where user_id in (
            SELECT user_id FROM 72crm_work_change_user WHERE change_id = ?)
    #end



    #sql ("queryResourcePageList")
        SELECT s.*,u.realname as main_user_name FROM 72crm_work_resources_setting s
        LEFT JOIN 72crm_admin_user u ON s.main_user_id=u.user_id
        WHERE 1=1
        #if(_workId)
            and s.work_id=#para(_workId)
        #end
        #if(search)
            and resources_name LIKE concat('%', #para(search), '%')
        #end
        order by create_time desc
    #end

    #sql ("queryResourceById")
        SELECT s.*,
            (SELECT work_name from 72crm_work where work_id=s.work_id) work_name,
            (SELECT realname from 72crm_admin_user where user_id=s.main_user_id) main_user_name
        FROM 72crm_work_resources_setting s WHERE s.resources_id=?
    #end

    #sql ("queryHistoricalRecordList")
        SELECT * FROM 72crm_work_historical_record WHERE module=#para(type)
        #if(workId)
            and work_id=#para(workId)
        #end
        #if(operationId)
            and operation_Id=#para(operationId)
        #end

        order by create_time desc
    #end
    #sql ("queryHistoricalList")
        SELECT * FROM 72crm_work_historical WHERE 1=1
        #if(workId)
            and work_id=#para(workId)
        #end
        #if(operationId)
            and operation_id=#para(operationId)
        #end
        order by create_time desc
    #end
    #sql ("queryTaskHistoricalList")
        SELECT * FROM 72crm_work_task_historical WHERE 1=1
        #if(taskId)
        and task_id = #para(taskId)
        #end
        #if(type)
        and module_two = #para(type)
        #end
        #if(operationId)
            and operation_id=#para(operationId)
        #end
        order by create_time desc
    #end


    #sql ("queryTaskConfigByTaskId")
        SELECT
            c.*,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = c.main_user_id) main_user_name,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = c.create_user_id) create_user_name
        FROM 72crm_work_config c WHERE 1=1
        #if(type)
            and c.type=#para(type)
        #end
        #if(workId)
            and c.work_id=#para(workId)
        #end
        #if(associatedId)
            and c.associated_id=#para(associatedId)
        #end
        #for(query:queryList)
            #(query.get("sql"))
        #end
        #if(orderByKey)
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #else
        ORDER BY update_time desc
        #end
        #if(page&&limit)
            LIMIT #(page),#(limit)
        #end
    #end
    #sql ("queryConfigCount")
        SELECT
           count(1)
        FROM 72crm_work_config c WHERE 1=1
        #if(type)
            and c.type=#para(type)
        #end
        #if(workId)
            and c.work_id=#para(workId)
        #end
        #if(associatedId)
            and c.associated_id=#para(associatedId)
        #end
        #for(query:queryList)
            #(query.get("sql"))
        #end
    #end

    #sql ("queryTaskConfigById")
        SELECT config_items,plan_time,main_user_id,
        (SELECT realname FROM 72crm_admin_user WHERE user_id = c.main_user_id) main_user_name,
        (SELECT realname FROM 72crm_admin_user WHERE user_id = c.create_user_id) create_user_name,
        associated_name,associated_num,associated_id,version,version_time,version_describe FROM 72crm_work_config c WHERE config_id=?
    #end

    #sql("queryLabel")
    select label_id,name,color from 72crm_work_task_label where label_id in(#fori(ids))
    #end



  #sql ("checkWorkNum")
  select count(*)
  from 72crm_work as a inner join 72crm_admin_examine_record as b on a.examine_record_id = b.record_id
  left join 72crm_admin_examine_log as c on b.record_id = c.record_id
  where a.work_phase='结项' and c.examine_user = ? and a.check_status in (0,3) and c.examine_status in (0,3) and ifnull(b.examine_step_id, 1) = ifnull(c.examine_step_id, 1) and c.is_recheck != 1
  #end



/*------------------------------------------*/




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


   /*#sql ("find")
        SELECT w.*,field_table.*
        from 72crm_work AS w LEFT JOIN
            (
                SELECT d.batch_id  as field_batch_id
                    #for(field:fields)
                        ,max(CASE WHEN d.name = '#(field.value.get("name"))' THEN d.value END) AS '#(field.value.get("field_name"))'
                    #end
                FROM 72crm_work_data d GROUP BY d.batch_id
            ) as field_table ON w.batch_id = field_table.field_batch_id
        where work_id = ?
    #end*/



















