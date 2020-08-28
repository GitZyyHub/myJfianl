#namespace("crm.business")
    #sql("queryInformationById")
        select a.business_name,c.name as status_name,d.name as type_name ,a.deal_date,b.customer_id,b.customer_name,a.money,a.remark,a.batch_id
        from `72crm_crm_business` as a
                 left join `72crm_crm_customer` b on a.customer_id = b.customer_id
                 left join `72crm_crm_business_status` c on c.status_id = a.status_id
                 left join `72crm_crm_business_type` d on d.type_id = a.type_id
        where business_id = ?;
    #end

    #sql ("queryById")
    select a.*,b.customer_id,b.customer_name,c.name as typeName,d.name as statusName,e.realname as owner_user_name,
    (select count(1) from 72crm_crm_business_user_star where business_id = a.business_id and user_id = ?) as star
    from 72crm_crm_business as a left join 72crm_crm_customer as b on a.customer_id = b.customer_id
    left join 72crm_crm_business_type as c on a.type_id = c.type_id
    left join 72crm_crm_business_status as d on a.status_id = d.status_id
    left join 72crm_admin_user as e on a.owner_user_id = e.user_id
    where a.business_id = ?
    #end

    #sql ("transfer")
    update 72crm_crm_business
    set owner_user_id = #para(ownerUserId)
    where business_id in (
        #for(i : ids)
            #(for.index > 0 ? "," : "")#para(i)
        #end
    )
    #end

    #sql ("queryProduct")
        select b.product_id,b.name,b.name as productName,c.name as category_name,a.price,a.sales_price,a.num,a.discount,a.subtotal,(select value from `72crm_crm_product_data` as d where d.batch_id = b.batch_id and d.name = '单位') as 'unit',(select value from `72crm_crm_product_data` as d where d.batch_id = b.batch_id and d.name = '是否上下架') as '是否上下架'
        from 72crm_crm_business_product as a inner join 72crm_crm_product as b on a.product_id = b.product_id
        left join 72crm_crm_product_category as c on b.category_id = c.category_id
        where a.business_id = ?
    #end

    #sql ("queryContract")
    select a.contract_id,a.num,a.name as contract_name,b.customer_name,a.money,a.start_time,a.end_time,a.check_status
    from 72crm_crm_contract as a left join 72crm_crm_customer as b on a.customer_id = b.customer_id
    where a.business_id = #para(businessId) and a.check_status != 7
    #(conditions)
    #end

    #sql ("queryContacts")
    select a.contacts_id,a.name,a.mobile,a.post
    from 72crm_crm_contacts as a inner join 72crm_crm_contacts_business as b on a.contacts_id = b.contacts_id
    where b.business_id = #para(businessId) #(conditions)
    #end

    #sql ("queryContractNumber")
    select count(*)
    from 72crm_crm_contract as a left join 72crm_crm_customer as b on a.customer_id = b.customer_id
    where a.business_id in (?) and a.check_status != 7
    #end

    #sql ("queryBusinessStatusList")
    select b.status_id,b.name,b.rate,b.order_num
    from 72crm_crm_business as a left join 72crm_crm_business_status as b on a.type_id = b.type_id
    where a.business_id = ?
    #end

    #sql ("queryBusinessStatus")
    select business_id,status_id,is_end,status_remark from 72crm_crm_business where business_id = ?
    #end

    #sql ("queryOrderId")
    select b.order_num
    from 72crm_crm_business as a left join 72crm_crm_business_status as b on a.status_id = b.status_id
    where a.business_id = ?
    #end

    #sql ("queryByIds")
      select * from 72crm_crm_business
      where business_id in (#fori(ids))
    #end

    #sql ("queryBusinessProduct")
    select b.product_id,c.name as name,d.name as category_name,b.unit,b.price,b.sales_price,b.num,b.discount,b.subtotal
    from 72crm_crm_business as a inner join 72crm_crm_business_product as b on a.business_id = b.business_id
    inner join 72crm_crm_product as c on b.product_id = c.product_id  and c.status != 3
    inner join 72crm_crm_product_category as d on c.category_id = d.category_id
    where a.business_id = ?
    #end

    #sql ("deleteMember")
    update 72crm_crm_business set rw_user_id = replace(rw_user_id,?,','),ro_user_id = replace(ro_user_id,?,',') where business_id = ?
    #end

    #sql ("unrelateContacts")
    delete from 72crm_crm_contacts_business where business_id = #para(businessId) and contacts_id in (
        #for(i:ids)
          #(for.index > 0 ? "," : "")#para(i)
        #end
    )
    #end
    #sql ("queryBusinessBatchId")
      select batch_id from 72crm_crm_business where business_id in(#fori(ids))
    #end

    #sql ("clearBusinessProduct")
      delete from 72crm_crm_business_product where business_id in (#fori(ids))
    #end

    #sql("deleteBusiness")
      update 72crm_crm_business set status ='3'  where business_id in (#fori(ids))
    #end

    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_business as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.business_id = #para(businessId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'商机详情' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_business_data as b on a.batch_id = b.value
    inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
    inner join 72crm_crm_business as d on d.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where d.business_id = #para(businessId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'跟进记录' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.activity_type_id = #para(businessId) and b.type = 1 and b.activity_type = 5
    #end

    #sql ("queryNum")
    select (
        (select count(*) from 72crm_admin_file as a join 72crm_crm_business as b on a.batch_id = b.batch_id
         where b.business_id = #para(businessId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_business_data as b on a.batch_id = b.value
         inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
         inner join 72crm_crm_business as d on d.batch_id = b.batch_id
         where d.business_id = #para(businessId) ) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
         where b.activity_type_id = #para(businessId) and b.type = 5 and b.activity_type = 1)
    ) as fileCount,
        (select count(a.contacts_id) from 72crm_crm_contacts_business a left join `72crm_crm_contacts` b on a.contacts_id = b.contacts_id where a.business_id = #para(businessId) #(contactsConditions)
    ) as contactCount,
        (select (select count(*) from 72crm_admin_user
                 where FIND_IN_SET(user_id,(a.rw_user_id)) or FIND_IN_SET(user_id,(a.ro_user_id)) or user_id = a.owner_user_id)
         from 72crm_crm_business as a where business_id = #para(businessId)
    ) as memberCount,
        (select count(*) from 72crm_crm_contract where business_id = #para(businessId) and check_status != 7 #(conditions)
    ) as contractCount,
        (select count(*) from 72crm_crm_business_product where business_id = #para(businessId)
    ) as productCount
    #end

    #sql ("queryBusinessPageListByFieldType1")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`customer_name` AS `customer_name`,
	        `e`.`name` AS `type_name`,
	        `f`.`name` AS `status_name`,
          `z`.*
        FROM
          `72crm_crm_business` as `a`
          LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id`
          LEFT JOIN `72crm_crm_business_type` `e` ON `a`.`type_id` = `e`.`type_id`
          LEFT JOIN `72crm_crm_business_status` `f` ON `a`.`status_id` = `f`.`status_id`
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
            FROM 72crm_crm_business_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_business` as a
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
    #sql ("queryBusinessPageListByFieldType2")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`customer_name` AS `customer_name`,
	        `e`.`name` AS `type_name`,
	        `f`.`name` AS `status_name`,
          `z`.*
        FROM
          `72crm_crm_business` as `a`
          LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id`
          LEFT JOIN `72crm_crm_business_type` `e` ON `a`.`type_id` = `e`.`type_id`
          LEFT JOIN `72crm_crm_business_status` `f` ON `a`.`status_id` = `f`.`status_id`
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
            72crm_crm_business_data as a
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
    #sql ("queryBusinessPageListCount")
      SELECT count(1) FROM `72crm_crm_business` as a
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end
    #sql ("queryBusinessMoney")
      SELECT sum(money) FROM 72crm_crm_business
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end

    #sql ("querySelectSql")
    select * ,(select rate from `72crm_crm_business_status` where status_id = views.status_id ) as rate,
    #end
#end
