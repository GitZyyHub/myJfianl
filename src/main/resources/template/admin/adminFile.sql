#namespace("admin.file")


    #sql ("queryById")
        SELECT a.file_id,a.`name`,a.size,a.create_user_id,b.realname AS create_user_name,a.create_time,a.file_path,a.file_type,a.batch_id FROM
        72crm_admin_file a INNER JOIN 72crm_admin_user b ON a.create_user_id=b.user_id WHERE a.file_id=?
    #end


    #sql("saveFile")
      INSERT INTO `72crm_admin_file`
      (`types`, `name`, `size`, `create_user_id`, `create_time`, `file_path`, `batch_id`)
       VALUES (?,?,?,?,?,?,?);
    #end
    #sql("queryByBatchId")
      SELECT a.file_id,a.name,a.size,a.create_user_id,b.realname as create_user_name,a.create_time,a.file_path,a.file_type,a.batch_id
      FROM `72crm_admin_file` as a inner join `72crm_admin_user` as b on a.create_user_id = b.user_id
      where a.batch_id=?
    #end
    #sql("queryPathByBatchId")
      SELECT path FROM `72crm_admin_file` as a where a.batch_id in (#fori(batchId))
      #if(type)
        and file_type=#para(type)
      #end
    #end
    #sql("deleteFileByBatchId")
      DELETE FROM `72crm_admin_file` WHERE batch_id in (#fori(batchId))
      #if(type)
        and file_type=#para(type)
      #end
    #end
    #sql("queryByIds")
      select file_path,file_id,name,size,REPLACE(path,#para(path),#para(address)) as url from 72crm_admin_file
      where file_id in (#fori(fileIds))
    #end
    #sql("queryByPath")
      select name,size,file_id,REPLACE(path,#para(path),#para(address)) as file_path from 72crm_admin_file
      where file_path = #para(filePath)
    #end
    #sql("queryAllByIds")
      select file_type as file_type, name as name from 72crm_admin_file
      where 1 = 1
      #if(ids)
       and file_id in (#fori(ids))
      #end
       #if(batchId)
       and batch_id in (#fori(batchId))
      #end
    #end
     #sql("deleteFileByIds")
      DELETE FROM `72crm_task_file`  where file_id in (#fori(ids))
    #end
#end
