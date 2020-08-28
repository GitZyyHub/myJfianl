#namespace("work.trash")
  #sql ("queryList")
  select a.task_id,a.name,a.stop_time,a.priority,a.status,
      c.realname as main_user_name,c.img as main_user_img,d.customer_ids,d.contacts_ids,d.business_ids,d.contract_ids,
  (select count(*) from 72crm_admin_file where batch_id = a.batch_id) as file_num,
  (select count(*) from 72crm_task_comment where type_id = a.task_id and type = 1) as comment_num,
  (select count(*) from 72crm_task where pid = a.task_id) as child_all_count,
  (select count(*) from 72crm_task where pid = a.task_id and status = 5) as child_finish_count
  from 72crm_task as a left join 72crm_admin_user as c on a.main_user_id = c.user_id
  left join 72crm_task_relation as d on a.task_id = d.task_id
  where pid = 0 and ishidden = 1
    #if(userId)
    and (a.main_user_id = #para(userId) or a.owner_user_id like concat('%,',#para(userId),',%'))
    #end
     order by a.hidden_time desc
  #end
#end
