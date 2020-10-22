#namespace("task.file")
    #sql ("queryPageList")
    SELECT
            file_id,
            `name`,
            file_attr,
            task_id,
            subtasks_id,
            file_path,
            file_name,
            size,
            file_type,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = f.main_user_id) main_user_name,
            `status`,
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
    #if(fileAttr)
        and file_attr=#para(fileAttr)
    #end
    ORDER BY create_time desc
    #end

    #sql ("queryById")
        SELECT
            file_id,
            `name`,
            file_attr,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = f.main_user_id) main_user_id,
            `status`,
            (SELECT task_name FROM 72crm_task WHERE task_id=f.task_id) task_id,
            (SELECT task_name FROM 72crm_task WHERE task_id=f.subtasks_id) subtasks_id,
            (SELECT realname FROM 72crm_admin_user WHERE user_id = f.create_user_id) create_user_id,
            create_time,
            batch_id
        FROM 72crm_task_file f
        WHERE
        file_id = ?
    #end

    #sql("deleteFileByIds")
      DELETE FROM `72crm_task_file`  where file_id in (#fori(ids))
    #end

#end