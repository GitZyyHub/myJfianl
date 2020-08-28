#namespace("crm.record")
  #sql("deleteRecord")
    delete from `72crm_crm_action_record` where types = #para(type) and action_id in (#fori(ids))
  #end

  #sql ("queryRecordList")
  select a.*,b.realname,b.img
  from 72crm_crm_action_record a left join 72crm_admin_user b on a.create_user_id = b.user_id
  where action_id = ? and types = ? and content is not null and content != '' order by create_time desc
  #end

  #sql ("queryFieldValue")
  select * from #(dataTableName) where batch_id = #para(batchId)
  #end
#end
