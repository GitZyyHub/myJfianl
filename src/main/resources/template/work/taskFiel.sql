#namespace("task.file")
    #sql ("queryPageList")
    SELECT
            file_id,
            `name`,
            file_attr,
            task_id,
            (select task_name from 72crm_task where task_id=f.task_id) task_name,
            subtasks_id,
            (select task_name from 72crm_task where task_id=f.subtasks_id) subtasks_name,
            file_path,
            file_name,
            size,
            file_type,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = f.main_user_id) main_user_name,
            (case (select check_status from 72crm_task_hours where subtasks_id = f.subtasks_id) when 0 then '待审核' when 1 then '通过' when 2 then '未通过' when 3 then '审核中' end) status,
            (SELECT task_name FROM 72crm_task WHERE task_id=f.task_id) task_name,
            (SELECT task_name FROM 72crm_task WHERE task_id=f.subtasks_id) subtasks_name,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = f.create_user_id) create_user_name,
            create_time,
            batch_id
    FROM 72crm_task_file f
    where 1=1
    #if(taskId)
        and (subtasks_id=#para(taskId) or task_id=#para(taskId))
    #end
    #if(search)
        and ( f.name LIKE concat('%', #para(search), '%')  or f.file_attr LIKE concat('%', #para(search), '%'))
    #end
    #if(fileAttr)
        and file_attr=#para(fileAttr)
    #end
    #if(workId)
        and work_id =#para(workId)
    #end
    #if(type)
        and `type` =#para(type)
    #end
    ORDER BY create_time desc
    #end

    #sql ("queryById")
        SELECT
            f.*,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = f.main_user_id) main_user_name,
            (SELECT task_name FROM 72crm_task WHERE task_id=f.task_id) task_name,
            (SELECT task_name FROM 72crm_task WHERE task_id=f.subtasks_id) subtasks_name,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = f.create_user_id) create_user_name
        FROM 72crm_task_file f
        WHERE
        file_id = ?
    #end

    #sql("deleteFileByIds")
      DELETE FROM `72crm_task_file`  where file_id in (#fori(ids))
    #end

    #sql("deleteFileByTaskIds")
        delete from 72crm_task_file where subtasks_id in (#fori(ids))
    #end

#end