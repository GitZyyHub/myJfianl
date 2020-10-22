#namespace("work.task.historical")

    #sql("queryPageList")
    SELECT
        (select realname from 72crm_admin_user where h.operation_user_id=user_id) operation_user_name,
        change_time,
        module_one,
        module_two,
        behavior,
        field,
        content_before,
        content_after,
        number
    FROM
    72crm_work_task_historical h
    where 1=1
    #if(taskId)
        and task_id = #para(taskId)
    #end

    #end
#end