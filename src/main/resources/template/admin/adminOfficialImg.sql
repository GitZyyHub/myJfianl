#namespace("admin.offical")
  #sql("queryListByType")
    select * from 72crm_admin_official_img
    where
     type = #para(type)
    order by tactic
  #end
   #sql("queryImgTactic")
    select tactic from 72crm_admin_official_img
    where type = ?
    order by tactic desc LIMIT 0,1
  #end
#end
