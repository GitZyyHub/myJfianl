#namespace("bi.work")
  #sql ("queryLogByUser")
    SELECT b.realname,b.username,b.img,b.user_id,(SELECT COUNT(1) FROM 72crm_task_comment WHERE type='2' and type_id=a.log_id) as sum,a.send_user_ids,a.read_user_ids
    FROM 72crm_oa_log as a LEFT JOIN 72crm_admin_user as b on a.create_user_id=b.user_id
    WHERE DATE_FORMAT(a.create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime) and a.create_user_id =#para(userId)
  #end

  #sql ("logList")
  SELECT
	    a.*, b.dept_id,
      b.realname,
      b.img as userImg,
      soal.customer_ids,
      soal.contacts_ids,
      soal.business_ids,
      soal.contract_ids,
      (CASE WHEN c.log_id is null THEN 0 ELSE 1 END) as getBulletin,
      c.customer_num as customerCount,
      c.business_num as businessCount,
      c.contract_num as contractCount,
      c.receivables_money as receivablesMoney,
      c.activity_num as recordCount,
      (SELECT COUNT(*) FROM 72crm_task_comment WHERE type_id=a.log_id and type='2') as replyNum
    FROM
	    72crm_oa_log as a
    LEFT JOIN 72crm_admin_user as b on a.create_user_id=b.user_id
    LEFT JOIN 72crm_oa_log_record as c on a.log_id=c.log_id
    LEFT JOIN 72crm_oa_log_relation as soal on soal.log_id=a.log_id
    left join 72crm_task_comment as d on type='2' and d.type_id=a.log_id
    WHERE date_format(a.create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
    and a.create_user_id = #para(userId)
    #if(queryType == 1)
       d.type_id is not null
    #else if(queryType == 2)
       d.type is null
    #end
    order by a.create_time desc
  #end

  #sql ("queryExamineCategory")
    SELECT category_id,title,type FROM 72crm_oa_examine_category WHERE is_deleted = 0
  #end
  #sql ("queryExamineCount")
    SELECT IFNULL(SUM(money), 0) as money,IFNULL(SUM(duration), 0) as duration
    FROM 72crm_oa_examine as a LEFT JOIN 72crm_oa_examine_record as b on a.examine_id=b.examine_id and b.examine_status='1'
    WHERE a.category_id = #para(categoryId)
    and (a.create_time between #para(startTime) and  #para(endTime))
    and a.create_user_id = #para(userId)
  #end
  #sql ("examineStatistics")
    SELECT
      a.user_id,a.username,a.realname,a.img
      #for(x:categorys) ,COUNT(case when b.category_id=#para(x.category_id) then 1 end) as count_#(x.category_id) #end
    FROM
      72crm_admin_user AS a
    LEFT JOIN 72crm_oa_examine AS b ON a.user_id = b.create_user_id and (DATE_FORMAT(b.create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime))
    left join 72crm_oa_examine_record as c on b.examine_id = c.examine_id
    left join 72crm_oa_examine_category as d on b.category_id = d.category_id
    where a.user_id in (
     #fori(userList)
    ) and c.examine_status != 4  and d.is_deleted = 0
    GROUP BY a.user_id
  #end
#end
