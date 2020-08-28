#namespace("admin.achievement")
  #sql("queryAchievementInfoByDept")
    select a.*,b.dept_id,b.name from 72crm_crm_achievement a inner join 72crm_admin_dept b on a.obj_id = b.dept_id
    where a.year = #para(year) and a.type = '2' and b.dept_id in(#fori(deptIds)) and a.status = #para(status)
  #end
  #sql("queryAchievementInfoByUser")
    select a.*,b.user_id,b.realname as name from 72crm_crm_achievement a inner join 72crm_admin_user b on a.obj_id = b.user_id
    where a.year = #para(year) and a.type = '3' and b.user_id in(#fori(userIds)) and a.status = #para(status)
  #end
  #sql("deleteAchievement")
    delete 72crm_crm_achievement from 72crm_crm_achievement
    where obj_id in(#fori(obj_id)) and type = #para(type) and year = #para(year) and status = #para(status)
  #end
#end
