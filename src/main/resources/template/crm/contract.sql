#namespace("crm.contract")
   #sql("queryById")
    select cc.* ,ccu.customer_name as customerName, cb.business_name as businessName
    from 72crm_crm_contract as cc left join 72crm_crm_customer as ccu on ccu.customer_id = cc.customer_id
    left join 72crm_crm_business as cb on cb.business_id = cc.business_id
    where cc.contract_id = ?
   #end

   #sql ("transfer")
   update 72crm_crm_contract
   set owner_user_id = #para(ownerUserId)
   where contract_id in (#fori(ids))
   #end

   #sql ("queryByIds")
      select * from  72crm_crm_contract
       where contract_id in (
            #for(i:ids)
              #(for.index > 0 ? "," : "")#para(i)
            #end
       )
   #end
    #sql ("queryBusinessProduct")
      select c.product_id , c.name as name,d.name as category_name,b.unit,b.price,b.sales_price,b.num,b.discount,b.subtotal
      from 72crm_crm_contract as a inner join 72crm_crm_contract_product as b on a.contract_id = b.contract_id
      inner join 72crm_crm_product as c on b.product_id = c.product_id  and a.check_status != 3
      inner join 72crm_crm_product_category as d on c.category_id = d.category_id
      where a.contract_id = ?
    #end

    #sql ("queryReturnVisit")
      select a.visit_id,a.visit_number,a.visit_time,b.realname as owner_user_name,c.num as contract_num,
             (select value from 72crm_crm_return_visit_data  where name = '回访形式' and batch_id = a.batch_id limit 1) as '回访形式',
             (select value from 72crm_crm_return_visit_data  where name = '客户满意度' and batch_id = a.batch_id limit 1) as '客户满意度'
      from 72crm_crm_return_visit as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
      left join 72crm_crm_contract as c on a.contract_id = c.contract_id
      where a.contract_id = #para(contractId)
    #end

    #sql ("deleteMember")
    update 72crm_crm_contract set rw_user_id = replace(rw_user_id,?,','),ro_user_id = replace(ro_user_id,?,',') where contract_id = ?
    #end
    #sql ("queryByContractId")
      select crt.*,ccc.customer_id,ccc.customer_name,cau.realname as ownerUserName,ccb.business_name,a.name as contacts_name,
      (select group_concat(realname) from `72crm_admin_user` where find_in_set(user_id,crt.company_user_id)) as company_user_name,
      ( select count(1) from 72crm_crm_receivables where contract_id =  crt.contract_id ) as receivablesCount,
      ( select IFNULL(sum(money),0) from 72crm_crm_receivables where contract_id =  crt.contract_id and check_status = 1) as receivablesMoney
      from 72crm_crm_contract as crt left join 72crm_admin_user as cau on crt.owner_user_id = cau.user_id
      left join 72crm_crm_customer as ccc on crt.customer_id = ccc.customer_id
      left join 72crm_crm_business as ccb on crt.business_id = ccb.business_id
      left join 72crm_crm_contacts as a on crt.contacts_id = a.contacts_id
      where crt.contract_id = ?
    #end
    #sql ("deleteByContractId")
    delete from 72crm_crm_contract_product where contract_id = ?
    #end
    #sql ("queryByNum")
    select count(*) from 72crm_crm_contract where num = ? and check_status!=7
    #end
    #sql ("updateCheckStatusById")
      update 72crm_crm_contract set check_status = ? where contract_id = ?
    #end
    #sql ("updateReceivedMoney")
      update `72crm_crm_contract` set received_money = received_money + #para(money),unreceived_money = unreceived_money - #para(money) where contract_id = #para(contractId) and unreceived_money = #para(unreceivedMoney)
    #end
    #sql ("updateUnreceivedMoney")
      update `72crm_crm_contract` set received_money = received_money - #para(money),unreceived_money = unreceived_money + #para(money) where contract_id = #para(contractId)
    #end

    #sql ("queryContractConfig")
    select status,value as contractDay from 72crm_admin_config where name = 'expiringContractDays' limit 1
    #end

    #sql ("setContractConfig")
    update 72crm_admin_config set status = #para(status)
    #if(contractDay)
    ,value = #para(contractDay)
    #end
     where name = 'expiringContractDays'
    #end
    #sql("queryContractByRecordId")
      SELECT contract_id,name,owner_user_id FROM 72crm_crm_contract WHERE examine_record_id = ? limit 0,1
    #end
    #sql("contractDiscard")
      update `72crm_crm_contract` set check_status = 8,update_time = now() where contract_id = #para(contractId)
    #end

    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a inner join 72crm_crm_contract as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.contract_id = #para(contractId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'合同详情' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_contract_data as b on a.batch_id = b.value
    inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
    inner join 72crm_crm_contract as d on d.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where d.contract_id = #para(contractId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'跟进记录' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.activity_type_id = #para(contractId) and b.type = 1 and b.activity_type = 6
    #end

    #sql ("queryNum")
    select (
        (select count(*) from 72crm_admin_file as a join 72crm_crm_contract as b on a.batch_id = b.batch_id
         where b.contract_id = #para(contractId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_contract_data as b on a.batch_id = b.value
         inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
         inner join 72crm_crm_contract as d on d.batch_id = b.batch_id
         where d.contract_id = #para(contractId) ) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
         where b.activity_type_id = #para(contractId) and b.type = 1 and b.activity_type = 6)
    ) as fileCount,
        (select (select count(*) from 72crm_admin_user
                 where FIND_IN_SET(user_id,(a.rw_user_id)) or FIND_IN_SET(user_id,(a.ro_user_id)) or user_id = a.owner_user_id)
         from 72crm_crm_contract as a where contract_id = #para(contractId)
    ) as memberCount,
        (select count(*) from 72crm_crm_contract_product where contract_id = #para(contractId)
    ) as productCount,
        (select count(*) from 72crm_crm_receivables where contract_id = #para(contractId)  and check_status != 7 #(conditions)
    ) as receivablesCount,
        (select count(*) from 72crm_crm_return_visit where contract_id = #para(contractId) #(returnVisitCon)
    ) as returnVisitCount,
        (select count(*) from 72crm_crm_invoice where contract_id = #para(contractId) #(invoiceCon)
    ) as invoiceCount
    #end

    #sql ("queryContractPageListByFieldType1")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`customer_name` AS `customer_name`,
          IFNULL(`e`.`business_name`,'') AS `business_name`,
          `f`.`name` AS `contacts_name`,
          `z`.*
        FROM
          `72crm_crm_contract` as `a`
          LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id`
					LEFT JOIN `72crm_crm_business` `e` ON `a`.`business_id` = `e`.`business_id`
					LEFT JOIN `72crm_crm_contacts` `f` ON `a`.`contacts_id` = `f`.`contacts_id`
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
            FROM 72crm_crm_contract_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_contract` as a
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
    #sql ("queryContractPageListByFieldType2")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`customer_name` AS `customer_name`,
          IFNULL(`e`.`business_name`,'') AS `business_name`,
          `f`.`name` AS `contacts_name`,
          `z`.*
        FROM
          `72crm_crm_contract` as `a`
          LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id`
					LEFT JOIN `72crm_crm_business` `e` ON `a`.`business_id` = `e`.`business_id`
					LEFT JOIN `72crm_crm_contacts` `f` ON `a`.`contacts_id` = `f`.`contacts_id`
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
            72crm_crm_contract_data as a
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
    #sql ("queryContractPageListCount")
      SELECT count(1)as count,IFNULL(SUM(CASE WHEN a.check_status = 1 THEN money
                                              WHEN a.check_status = 1 THEN 0 END
                              ),0) AS contractMoney,
            IFNULL(SUM(received_money),0) AS receivedMoney,IFNULL(SUM(unreceived_money),0) AS unReceivedMoney
      FROM `72crm_crm_contract` as a
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end
#end
