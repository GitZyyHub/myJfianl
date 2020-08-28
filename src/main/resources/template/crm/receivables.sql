#namespace("crm.receivables")
     #sql("queryById")
        select  scr.* ,scc.customer_name as customerName,scco.num as contractNum,sccp.num as planNum
         from 72crm_crm_receivables as scr
        LEFT JOIN 72crm_crm_customer as scc on scc.customer_id = scr.customer_id
         LEFT JOIN 72crm_crm_contract as scco on scco.contract_id = scr.contract_id
         LEFT JOIN 72crm_crm_receivables_plan as sccp on sccp.receivables_id = scr.receivables_id
        where scr.receivables_id = ?
     #end
     #sql("queryReceivablesPageList")
        select  rec.receivables_id,rec.number as receivables_num,scco.name contract_name,scco.money as contract_money
            ,b.realname as owner_user_name,
            rec.check_status,rec.return_time,rec.money as receivables_money,a.num as plan_num
        FROM `72crm_crm_receivables` as rec left join `72crm_crm_receivables_plan` a on rec.receivables_id = a.receivables_id
                                            left join 72crm_crm_contract as scco on scco.contract_id = rec.contract_id
                                            left join `72crm_admin_user` b on rec.owner_user_id = b.user_id
        where rec.contract_id = #para(contractId) and rec.check_status != 7 #(conditions)
     #end
     #sql("queryReceivablesByContractIds")
       select * from 72crm_crm_receivables where contract_id in (#fori(contractIds)) and check_status != 7
     #end
     #sql("queryReceivablesById")
      SELECT
        a.*,
        b.customer_id,
        b.customer_name,
        c.contract_id,
        c.num AS contract_num,
        c.name AS contract_name,
        c.money AS contract_money,
        d.realname AS owner_user_name,
        e.num as plan_num
      FROM
        72crm_crm_receivables AS a
        LEFT JOIN 72crm_crm_customer AS b ON a.customer_id = b.customer_id
        LEFT JOIN 72crm_crm_contract AS c ON a.contract_id = c.contract_id
        LEFT JOIN 72crm_admin_user AS d ON a.owner_user_id = d.user_id
        LEFT JOIN 72crm_crm_receivables_plan as e on a.plan_id=e.plan_id
      WHERE
        a.receivables_id = ?
     #end
     #sql ("queryByNumber")
       select count(*) from 72crm_crm_receivables where number = ? and check_status!=7
     #end
     #sql ("updateCheckStatusById")
      update 72crm_crm_receivables set check_status = ? where receivables_id = ?
     #end
     #sql("queryReceivablesByRecordId")
      SELECT number,receivables_id,owner_user_id FROM 72crm_crm_receivables WHERE examine_record_id=? limit 0,1
     #end

    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_receivables as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.receivables_id = #para(receivablesId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'回款详情' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_receivables_data as b on a.batch_id = b.value
    inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
    inner join 72crm_crm_receivables as d on d.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where d.receivables_id = #para(receivablesId)
    #end

    #sql ("queryNum")
    select (
        (select count(*) from 72crm_admin_file as a join 72crm_crm_receivables as b on a.batch_id = b.batch_id
         where b.receivables_id = #para(receivablesId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_receivables_data as b on a.batch_id = b.value
         inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
         inner join 72crm_crm_receivables as d on d.batch_id = b.batch_id
         where d.receivables_id = #para(receivablesId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
         where b.activity_type_id = #para(receivablesId) and b.type = 1 and b.activity_type = 1)
    ) as fileCount
    #end
    #sql ("queryReceivablesPageListByFieldType1")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`customer_name` AS `customer_name`,
          `e`.`name` AS `contract_name`,
          `e`.`num` AS `contract_num`,
          `e`.`money` AS `contract_money`,
          `f`.`num` AS `plan_num`,
          `z`.*
        FROM
          `72crm_crm_receivables` as `a`
          LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id`
          LEFT JOIN `72crm_crm_contract` `e` ON `a`.`contract_id` = `e`.`contract_id`
          LEFT JOIN `72crm_crm_receivables_plan` `f` ON `a`.`plan_id` = `f`.`plan_id`
        JOIN (
          SELECT
            b.batch_id as field_batch_id
            #for(field : fieldMap)
              #if(field.value&&field.value.get("field_type")!=1)
                #if(field.value.get("type")==12)
                  ,GROUP_CONCAT(if(a.name = '#(field.key)',c.name,null)) AS '#(field.key)'
                #elseif(field.value.get("type")==10)
                  ,GROUP_CONCAT(if(a.name = '#(field.key)',d.realname,null)) AS '#(field.key)'
                #else
                  ,max(CASE WHEN `a`.`name` = '#(field.key)' THEN `a`.`value` END) AS '#(field.key)'
                #end
              #end
            #end
            FROM 72crm_crm_receivables_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_receivables` as a
              WHERE 1=1
              #if(batchList&&batchList.size()>0)
                and batch_id in ( #fori(batchList))
              #end
              #for(query : queryList) #if(query.get("type")==1) #(query.get("sql")) #end #end #if(orderByKey!="create_user_name"&&orderByKey!="owner_user_name") ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType) #end #if(!field) #if(page&&limit) LIMIT #(page),#(limit) #end #end
            ) AS b ON a.batch_id = b.batch_id
            #if(fieldMap.containsKey("user"))
              left join 72crm_admin_user d on find_in_set(d.user_id,ifnull(a.value,0))
            #end
            #if(fieldMap.containsKey("dept"))
              left join 72crm_admin_dept c on find_in_set(c.dept_id,ifnull(a.value,0))
            #end
            GROUP BY b.batch_id
        ) `z` ON `a`.`batch_id` = `z`.`field_batch_id`
      ) as views
        where 1=1
         #for(query : queryList) #if(query.get("type")==3) #(query.get("sql")) #end #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(field) #if(page&&limit) LIMIT #(page),#(limit) #end #end
    #end
    #sql ("queryReceivablesPageListByFieldType2")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`customer_name` AS `customer_name`,
          `e`.`name` AS `contract_name`,
          `e`.`num` AS `contract_num`,
          `e`.`money` AS `contract_money`,
          `f`.`num` AS `plan_num`,
          `z`.*
        FROM
          `72crm_crm_receivables` as `a`
          LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id`
          LEFT JOIN `72crm_crm_contract` `e` ON `a`.`contract_id` = `e`.`contract_id`
          LEFT JOIN `72crm_crm_receivables_plan` `f` ON `a`.`plan_id` = `f`.`plan_id`
        LEFT JOIN (
          SELECT
            a.batch_id as field_batch_id
            #for(field : fieldMap)
              #if(field.value&&field.value.get("field_type")!=1)
                #if(field.value.get("type")==12)
                  ,GROUP_CONCAT(if(a.name = '#(field.key)',c.name,null)) AS '#(field.key)'
                #elseif(field.value.get("type")==10)
                  ,GROUP_CONCAT(if(a.name = '#(field.key)',d.realname,null)) AS '#(field.key)'
                #else
                  ,max(CASE WHEN `a`.`name` = '#(field.key)'  THEN `a`.`value` END) AS '#(field.key)'
                #end
              #end
            #end
          FROM
            72crm_crm_receivables_data as a
          #if(fieldMap.containsKey("user"))
            left join 72crm_admin_user d on find_in_set(d.user_id,ifnull(a.value,0))
          #end
          #if(fieldMap.containsKey("dept"))
            left join 72crm_admin_dept c on find_in_set(c.dept_id,ifnull(a.value,0))
          #end
          where 1=1
          #if(batchList&&batchList.size()>0)
                and batch_id in ( #fori(batchList))
          #end
          GROUP BY a.batch_id
        ) `z` ON `a`.`batch_id` = `z`.`field_batch_id`
      ) as views
      WHERE 1=1
      #for(query : queryList) #(query.get("sql")) #end
      ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
      #if(page&&limit)
          LIMIT #(page),#(limit)
      #end
    #end
    #sql ("queryReceivablesPageListCount")
      SELECT count(1) as count,IFNULL(SUM(CASE WHEN a.check_status = 1 THEN money
                                               WHEN a.check_status != 1 THEN 0 END
                               ),0) AS receivablesMoney FROM `72crm_crm_receivables` as a
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end

    #sql ("excelExport")
    SELECT
      `a`.*,
      `b`.`realname` AS `create_user_name`,
      `c`.`realname` AS `owner_user_name`,
      `d`.`customer_name`,
      IFNULL(`e`.`business_name`,'') AS `business_name`,
      `f`.`name` AS `contacts_name`,
      `z`.*
    FROM
      `72crm_crm_receivables` as `a`
    left JOIN `72crm_admin_user` `b` ON `a`.`create_user_id` = `b`.`user_id`
    left JOIN `72crm_admin_user` `c` ON `a`.`owner_user_id` = `c`.`user_id`
    LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id`
    LEFT JOIN `72crm_crm_contract` `e` ON `a`.`contract_id` = `e`.`contract_id`
    LEFT JOIN `72crm_crm_receivables_plan` `f` ON `a`.`plan_id` = `f`.`plan_id`
    INNER JOIN (
      SELECT
        b.batch_id as field_batch_id
        #for(field : fieldMap)
          #if(field.value&&field.value.get("field_type")!=1)
            #if(field.value.get("type")==12)
              ,GROUP_CONCAT(if(a.name = #para(field.key),c.name,null)) AS '#(field.key)'
            #elseif(field.value.get("type")==10)
              ,GROUP_CONCAT(if(a.name = #para(field.key),d.realname,null)) AS '#(field.key)'
            #else
              ,max(CASE WHEN `a`.`name` = #para(field.key) THEN `a`.`value` END) AS '#(field.key)'
            #end
          #end
        #end
        FROM 72crm_crm_receivables_data AS a RIGHT JOIN (SELECT batch_id FROM 72crm_crm_receivables WHERE receivables_id in (#for(i:ids) #(for.index > 0 ? "," : "")#para(i) #end)) AS b ON a.batch_id = b.batch_id
        #if(fieldMap.containsKey("user"))
          left join 72crm_admin_user d on find_in_set(d.user_id,ifnull(a.value,0))
        #end
        #if(fieldMap.containsKey("dept"))
          left join 72crm_admin_dept c on find_in_set(c.dept_id,ifnull(a.value,0))
        #end
        GROUP BY b.batch_id
    ) `z` ON `a`.`batch_id` = `z`.`field_batch_id`
    order by update_time desc
    #end
#end
