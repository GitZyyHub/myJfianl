#namespace("work.user")
    #sql ("findUserId")
        select user_id from 72crm_work_user where work_id = ?
    #end

    #sql ("insertOrUpdate")
        replace into 72crm_work_user(work_id, user_id) select ?, ?;
    #end

    #sql ("findWorkIdByUserId")
        SELECT work_id from 72crm_work_use WHERE user_id=?
    #end

    #sql ("fingWorkField")
        SELECT field_name FROM `72crm_admin_field` WHERE type = ? and is_hidden=0 and field_type=0
    #end








#end