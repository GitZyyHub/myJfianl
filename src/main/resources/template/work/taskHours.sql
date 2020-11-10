#namespace("task.hours")

    #sql("queryPageList")
        SELECT
            hours_id,
            task_id,
            (select task_name from 72crm_task where task_id=h.task_id) task_name,
            main_user_id,
            (select realname from 72crm_admin_user where user_id=h.main_user_id) main_user_name,
            subtasks_id,
            (select task_name from 72crm_task where task_id=h.subtasks_id) subtasks_name,
            start_time,
            end_time,
            working_hours,
            hours_type,
            progress_content,
            check_user_id,
            (select realname from 72crm_admin_user where user_id=h.check_user_id) check_user_name,
            create_user_id,
            (select realname from 72crm_admin_user where user_id=h.create_user_id) create_user_name,
            h.create_time,
            h.examine_time,
            (case check_status when 0 then '待审核' when 1 then '通过' when 2 then '未通过' end) check_status,
            create_time
        FROM
        72crm_task_hours h
        where 1=1
        #if(taskId)
            and task_id = #para(taskId)
        #end
        #if(workId)
        and work_id = #para(workId)
        #end
        #if(task)
            and subtasks_id in #fori(task)
        #end
        #for(query:queryList)
            #(query.get("sql"))
        #end

        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
            LIMIT #(page),#(limit)
        #end
    #end
    #sql("queryHoursByUserId")
    SELECT
            hours_id,
            task_id,
            (select task_name from 72crm_task where task_id=h.task_id) task_name,
            main_user_id,
            (select realname from 72crm_admin_user where user_id=h.main_user_id) main_user_name,
            subtasks_id,
            (select task_name from 72crm_task where task_id=h.subtasks_id) subtasks_name,
            start_time,
            end_time,
            working_hours,
            hours_type,
            progress_content,
            check_user_id,
            (select realname from 72crm_admin_user where user_id=h.check_user_id) check_user_name,
            create_user_id,
            (select realname from 72crm_admin_user where user_id=h.create_user_id) create_user_name,
            h.create_time,
            h.examine_time,
            (case check_status when 0 then '待审核' when 1 then '通过' when 2 then '未通过' end) check_status,
            create_time
        FROM
        72crm_task_hours h
        where 1=1
        and create_user_id = #para(userId)
        ORDER BY update_time desc
    #end

    #sql ("queryPageListCount")
        SELECT count(1) FROM 72crm_task_hours h where 1=1
        #if(taskId)
            and task_id = #para(taskId)
        #end
        #if(workId)
        and work_id = #para(workId)
        #end
        #if(task)
            and subtasks_id in #fori(task)
        #end
        #for(query:queryList)
            #(query.get("sql"))
        #end
    #end

    #sql ("queryHoursByIdExport")
        SELECT
            h.hours_id,
            h.create_time,
            h.examine_time,
            (case h.check_status when 0 then '待审核' when 1 then '通过' when 2 then '未通过' end) check_status,
            (select realname from 72crm_admin_user where user_id=h.main_user_id) main_user_name,
            (select d.name from 72crm_admin_user u join 72crm_admin_dept d on u.dept_id=d.dept_id where u.user_id=h.main_user_id) main_user_dept,
            w.work_name,
            w.work_num,
            (select name from 72crm_admin_dept where dept_id=w.dept_id) work_area,
            (select task_name from 72crm_task where task_id=h.task_id) task_name,
            (select task_num from 72crm_task where task_id=h.task_id) task_num,
            (select task_name from 72crm_task where task_id=h.subtasks_id) subtasks_name,
            (select task_num from 72crm_task where task_id=h.subtasks_id) subtasks_num,
            working_hours
        FROM
        72crm_task_hours h
        left join 72crm_work w on w.work_id=h.work_id
        where 1=1
        #if(taskId)
            and subtasks_id = #para(taskId)
        #end
        #if(hoursIds)
            and hours_id in (#fori(hoursIds))
        #end
    #end

    #sql ("queryHoursRecord")
        select r.*,
         (select realname from 72crm_admin_user where user_id=r.create_user_id) create_user_name
         from 72crm_task_hours_record r where 1=1
        #if(taskId)
            and r.task_id = #para(taskId)
        #end
        #if(workId)
        and r.work_id=#para(workId)
        #end
         ORDER BY create_time desc
    #end

    #sql("updateCheckStatusById")
        update 72crm_task_hours set check_status = ? where hours_id = ?
    #end
    #sql("updateCheckStatusByIdThrough")
        update 72crm_task_hours set check_status = ?,examine_time=? where hours_id = ?
    #end

    #sql ("hoursRecordList")
        select r.*,(select realname from 72crm_admin_user where user_id=r.create_user_id) from 72crm_task_hours_record r where 1=1
        #if(taskId)
         and r.task_id=#para(taskId)
        #end
        #if(workId)
        and  r.work_id = #para(workId)
        #end
    #end
    #sql ("hoursRecordExport")
        select r.file_name,
        (select realname from 72crm_admin_user where user_id=r.create_user_id) create_name,
        r.create_time
        from 72crm_task_hours_record r where 1=1
        #if(workId)
            and r.work_id = #para(workId)
        #end
    #end


#end