#namespace("test.adminUser")
    #sql ("findBy")
    select username,password,salt,mobile,dept_id,parent_id FROM 72crm_admin_user WHERE
    #if(username)
    username=#para(username)
    #end
    #if(mobile)
    and mobile=#para(mobile)
    #end
    and status=1
    #end
#end