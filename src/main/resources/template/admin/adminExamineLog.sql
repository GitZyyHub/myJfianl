#namespace("admin.examineLog")
  #sql("queryNowadayExamineLogByRecordId")
  select * from 72crm_admin_examine_log where record_id = ? and examine_user = ?
  #end
  #sql("queryExamineLogByRecordId")
  select sael.* ,(SELECT realname from 72crm_admin_user WHERE user_id = sael.create_user) createUserName,(SELECT realname from 72crm_admin_user WHERE user_id = sael.examine_user) examineUserName
  from 72crm_admin_examine_log as sael where record_id = ? order by create_time
  #end
  #sql("queryExamineLogByStepId")
  select sael.* ,(SELECT realname from 72crm_admin_user WHERE user_id = sael.create_user) createUserName,(SELECT realname from 72crm_admin_user WHERE user_id = sael.examine_user) examineUserName
  from 72crm_admin_examine_log as sael where examine_step_id = ? order by create_time
  #end
  #sql("queryNowadayExamineLogByRecordIdAndStepId")
  select * from 72crm_admin_examine_log where record_id = ? and examine_step_id = ? and examine_user = ? and is_recheck = 0
  #end
  #sql("queryExamineLogByRecordIdAndStepId")
  select sael.* ,
         (SELECT realname from 72crm_admin_user WHERE user_id = sael.create_user) createUserName,
         (SELECT realname from 72crm_admin_user WHERE user_id = sael.examine_user) examineUserName
  from 72crm_admin_examine_log as sael
  where record_id = ? and examine_step_id = ? order by create_time
  #end
  #sql("queryUserByUserId")
  SELECT DISTINCT saud.user_id, saud.realname , 0 as examine_status from 72crm_admin_user as sau
  LEFT JOIN 72crm_admin_user as saud on saud.user_id = sau.parent_id WHERE sau.user_id = ?
  #end
  #sql("queryExamineLogByRecordIdAndStepIdAndStatus")
    select sael.* ,(SELECT realname from 72crm_admin_user WHERE user_id = sael.create_user) createUserName,
    (SELECT realname from 72crm_admin_user WHERE user_id = sael.examine_user) examineUserName
    from 72crm_admin_examine_log as sael where record_id = ? and examine_step_id = ?  order by create_time
  #end
  #sql("queryUserByUserIdAndStatus")
    SELECT DISTINCT user_id, realname  , 0 as examine_status , img from 72crm_admin_user WHERE user_id = ?
  #end
  #sql("queryNowadayExamineLogByRecordIdAndStepIdAndStatus")
    select * from 72crm_admin_examine_log where record_id = ? and examine_step_id = ? and examine_user = ? and examine_status != 0
  #end
  #sql("queryUserByRecordIdAndStepIdAndStatus")
    select sael.examine_time  as examineTime , sael.examine_status,sau.realname as realname , sau.user_id , sau.img
    from 72crm_admin_examine_log as sael
    LEFT JOIN 72crm_admin_user as sau on sau.user_id = sael.examine_user
    where record_id = ? and examine_step_id = ?
    and sael.is_recheck != 1  order by sael.create_time
  #end
  #sql("queryUserByRecordId")
    select sael.examine_time  as examineTime ,
    sael.examine_status,sau.realname ,sau.user_id,sau.img
    from 72crm_admin_examine_log as sael
    LEFT JOIN 72crm_admin_user as sau on sau.user_id = sael.examine_user
    where sael.record_id = ? and  sael.examine_status = ? and sael.is_recheck != 1
  #end
  #sql("queryExamineLogAndUserByRecordId")
    select sael.examine_time  as examineTime , sael.examine_status,sau.realname ,sau.img , sael.log_id
    from 72crm_admin_examine_log as sael
    LEFT JOIN 72crm_admin_user as sau on sau.user_id = sael.examine_user
    where sael.record_id = ? and sael.is_recheck != 1 order by sael.create_time
  #end
  #sql("queryExamineLogAndUserByLogId")
    select sael.examine_time  as examineTime , sael.examine_status,sau.realname ,sau.img , sael.log_id
    from 72crm_admin_examine_log as sael
    LEFT JOIN 72crm_admin_user as sau on sau.user_id = sael.examine_user where
    sael.log_id = ? and sael.is_recheck != 1
    order by sael.create_time
  #end
  #sql("queryExamineLog")
  SELECT * FROM 72crm_admin_examine_log
  WHERE examine_status = 0 and is_recheck != 1
  and record_id = #para(recordId)
  and  examine_user = #para(auditUserId)
    #if(stepId)
    and examine_step_id = #para(stepId)
    #end
  #end
  #sql("queryRecordAndId")
    select create_user, sau.realname as examineUserName , sau.user_id , sau.img, aser.create_time as examineTime, 6 as examine_status
    from 72crm_admin_examine_record  as aser
    LEFT JOIN 72crm_admin_user as sau on sau.user_id = aser.create_user where record_id = ?
  #end
  #sql("queryUserByUserIdAnd")
    SELECT DISTINCT user_id, realname ,img, 0 as examine_status from 72crm_admin_user WHERE user_id = ?
  #end
  #sql("queryRecordByUserIdAndStatus")
    SELECT DISTINCT user_id, realname  ,img, 6 as examine_status, 5 as stepType, #para(examineTime) as examineTime
    from 72crm_admin_user WHERE user_id = #para(create_user)
  #end
  #sql ("queryExamineByRecordId")
  select category_type
  from 72crm_admin_examine_record as a left join 72crm_admin_examine as b on a.examine_id = b.examine_id where record_id = ?
  #end
  #sql ("queryContractByRecordId")
  select a.create_time,a.owner_user_id,b.realname,check_status
  from 72crm_crm_contract as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id where examine_record_id = ?
  #end
   #sql ("queryReceivablesByRecordId")
  select a.create_time,a.owner_user_id,b.realname,a.check_status
  from 72crm_crm_receivables as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id where examine_record_id = ?
  #end
   #sql ("queryInvoiceByRecordId")
  select a.create_time,a.owner_user_id,b.realname,a.check_status
  from 72crm_crm_invoice as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id where examine_record_id = ?
  #end
  #sql("queryExamineLogByRecordIdByStep")
    select sael.order_id as order_id , sau.user_id , sau.realname , sau.img ,sael.examine_status,sael.examine_time,sael.remarks
    from 72crm_admin_examine_log as sael
    LEFT JOIN 72crm_admin_user as sau on sau.user_id = sael.examine_user
    where sael.record_id = ? AND sael.examine_status not in (0,5) order by sael.create_time
  #end
  #sql("updateExamineLogIsRecheckByRecordId")
    UPDATE 72crm_admin_examine_log SET is_recheck = 1 WHERE record_id = ?
  #end
  #sql("queryNowadayExamineLogByRecordIdAndStatus")
    select * from 72crm_admin_examine_log
    where record_id = ? and examine_status = 0 and examine_user = ? and is_recheck = 0
  #end
  #sql("queryCountByStepId")
    SELECT  DISTINCT ((SELECT COUNT(log_id) FROM 72crm_admin_examine_log WHERE record_id = #para(recordId) and examine_step_id = #para(stepId))- (SELECT COUNT(log_id) FROM 72crm_admin_examine_log WHERE record_id = #para(recordId) and examine_step_id = #para(stepId) and examine_status = 2 ))
    as toCount FROM
    72crm_admin_examine_log
  #end
  #sql("myInitiate")
    SELECT
      a.*,
      a.create_user as create_user_id,
      b.`name`,
      b.examine_type,
      IFNULL(c.`name`,d.number) as catagory,
      IFNULL(c.contract_id,d.receivables_id) as catagory_id
    FROM
        72crm_admin_examine_record AS a
        LEFT JOIN 72crm_admin_examine AS b ON a.examine_id = b.examine_id
        LEFT JOIN 72crm_crm_contract AS c ON c.examine_record_id = a.record_id AND b.examine_type = '1'
        LEFT JOIN 72crm_crm_receivables AS d ON d.examine_record_id = a.record_id AND b.examine_type = '2'
    WHERE a.create_user=#para(userId)
    #if(status!=null&&status!="")
      and  a.examine_status = #para(status)
    #end
  #end
  #sql("myExamine")
    SELECT
      a.*,
      a.record_id as examineRecordId,
      a.create_user as create_user_id,
      b.`name`,
      b.examine_type,
      #if(categoryType==1)
        c.`name` AS catagory,
        c.contract_id AS catagory_id,
        c.order_date,
        d.customer_id as catagory_cite_id,
        d.customer_name as catagory_cite_name
      #else
        d.number AS catagory,
        d.receivables_id AS catagory_id,
        d.return_time,
        c.contract_id as catagory_cite_id,
        c.name as catagory_cite_name
        #end
    FROM
      72crm_admin_examine_log AS e
      LEFT JOIN 72crm_admin_examine_record AS a ON e.record_id = a.record_id
      LEFT JOIN 72crm_admin_examine AS b ON a.examine_id = b.examine_id
      #if(categoryType==1)
      LEFT JOIN 72crm_crm_contract AS c ON c.examine_record_id = a.record_id AND b.category_type = '1'
      LEFT JOIN 72crm_crm_customer as d on c.customer_id=d.customer_id
      #else
      LEFT JOIN 72crm_crm_receivables AS d ON d.examine_record_id = a.record_id AND b.category_type = '2'
      LEFT JOIN 72crm_crm_contract AS c ON d.contract_id = c.contract_id
      #end
    WHERE
    e.log_id IN (
          SELECT MAX(log_id)
            FROM 72crm_admin_examine_log
          WHERE
            #if((status==null||status=="")&&isAdmin==true)
            1=1
            #else
            examine_user = #para(userId)
            #end
            #if(status==1)
              and examine_status = 0 and is_recheck !=1
            #elseif(status==2)
              and examine_status != 0 and is_recheck = 0
            #else
               and ((examine_status = 0 and is_recheck !=1) or examine_status != 0)
            #end
          GROUP BY record_id
    )
    #if(categoryType)
      and b.category_type=#para(categoryType)
    #end
    order by create_time desc
  #end
  #sql("queryExamineUserByExamineLog")
     SELECT
      GROUP_CONCAT(b.realname)
     FROM
      72crm_admin_examine_log as a
      LEFT JOIN 72crm_admin_user as b on a.examine_user=b.user_id
     WHERE a.record_id=#para(record_id) and examine_status=#para(examine_status)
     #if(examine_step_id)
      and a.examine_step_id=#para(examine_step_id)
     #end
  #end
  #sql("queryExamineUserByExamineStepId")
    SELECT * FROM 72crm_admin_examine_log
    WHERE record_id = #para(recordId) and examine_step_id = #para(examineStepId)
    and (is_recheck = 0 or is_recheck is null)
     #if(examineStatus)
      and examine_status =  #para(examineStatus)
    #end
    ORDER BY log_id desc LIMIT 0 , 1
  #end
#end
