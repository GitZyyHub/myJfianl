#namespace("crm.customerPool")
    #sql ("queryCustomerPoolPageListByFieldType1")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`pre_owner_user_id`) AS `pre_owner_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          d.name as contacts_name,
          `z`.*
        FROM
          `72crm_crm_customer` as `a`
          LEFT JOIN `72crm_crm_contacts` `d` ON `a`.`contacts_id` = `d`.`contacts_id`
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
            FROM 72crm_crm_customer_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_customer` as a
              WHERE a.customer_id in (select customer_id from 72crm_crm_customer_pool_relation where pool_id = #para(poolId))
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
    #sql ("queryCustomerPoolPageListByFieldType2")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`pre_owner_user_id`) AS `pre_owner_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          d.name as contacts_name,
          `z`.*
        FROM
          `72crm_crm_customer` as `a`
          LEFT JOIN `72crm_crm_contacts` `d` ON `a`.`contacts_id` = `d`.`contacts_id`
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
            72crm_crm_customer_data as a
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
      WHERE views.customer_id in (select customer_id from 72crm_crm_customer_pool_relation where pool_id = #para(poolId))
      #for(query : queryList) #(query.get("sql")) #end
      ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
      #if(page&&limit)
          LIMIT #(page),#(limit)
      #end
    #end
    #sql ("queryCustomerPoolPageListCount")
      SELECT count(1) FROM `72crm_crm_customer` as a
      WHERE a.customer_id in (select customer_id from 72crm_crm_customer_pool_relation where pool_id = #para(poolId))
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end

    #sql ("queryUser")
    select user_id,realname,img from 72crm_admin_user where user_id in (#fori(ids))
    #end

    #sql ("queryMemberDept")
    select dept_id as id,name from 72crm_admin_dept where dept_id in (#fori(ids))
    #end

    #sql ("queryPoolNameListByAuth")
    select pool_id,pool_name from 72crm_crm_customer_pool
    where status = 1
    #if(noAdmin)
    and (find_in_set(#para(userId),admin_user_id) or find_in_set(#para(userId),member_user_id)
    or find_in_set(#para(deptId),member_dept_id))
    #end
    order by create_time desc
    #end

    #sql ("queryPoolReadAuth")
    select count(1) from 72crm_crm_customer_pool
    where status = 1 and (find_in_set(#para(userId),admin_user_id) or find_in_set(#para(userId),member_user_id)
    or find_in_set(#para(deptId),member_dept_id))
    #end

    #sql ("queryPoolAuth")
    select count(1) from 72crm_crm_customer_pool
    where status = 1 and (find_in_set(#para(userId),admin_user_id))
    #end

    #sql ("queryCustomerField")
    select field_id,(CASE WHEN field_name = 'customer_id' THEN field_name = 'customer_name' ELSE field_name END) as field_name,name,type
    from 72crm_admin_field where label = ? and is_hidden = 0
    #end

    #sql ("updateFieldSort")
    update 72crm_crm_customer_pool_field_sort set field_name = #para(field_name),name = #para(name)
    where field_id = #para(field_id) and pool_id = #para(poolId)
    #end

    #sql ("putInPoolByRecord")
    select a.customer_id
    from 72crm_crm_customer as a
    #if(customer_level_setting == 2)
       left join 72crm_crm_customer_data as c on a.batch_id = c.batch_id and c.name = '客户级别'
    #end
    #if(business_handle == 0)
       left join 72crm_crm_business as d on a.customer_id = d.customer_id and d.status = 1
    #end
    where (to_days(now()) - to_days(if(a.last_time > a.receive_time,a.last_time,a.receive_time))) > #para(limit_day) and a.status = 1
    #if(ids)
       and a.owner_user_id in (#fori(ids))
    #else
       and a.owner_user_id is not null
    #end
    #if(business_handle == 0)
       and d.business_id is null
    #end
    #if(deal_handle == 0)
       and a.deal_status = 0
    #end
    #if(customer_level_setting == 2)
       and c.value = #para(level)
    #end
    #end

    #sql ("putInPoolByBusiness")
    select a.customer_id
    from 72crm_crm_customer as a left join 72crm_crm_business as b on a.customer_id = b.customer_id and b.status = 1
    #if(level != 1)
       left join 72crm_crm_customer_data as c on a.batch_id = c.batch_id and c.name = '客户级别'
    #end
    where (to_days(now()) - to_days(a.receive_time)) > #para(limit_day) and a.status = 1 and b.business_id is null
    #if(ids)
       and a.owner_user_id in (#fori(ids))
    #else
       and a.owner_user_id is not null
    #end
    #if(deal_handle == 0)
       and a.deal_status = 0
    #end
    #if(customer_level_setting == 2)
       and c.value = #para(level)
    #end
    #end

    #sql ("putInPoolByDealStatus")
    select a.customer_id
    from 72crm_crm_customer as a
    #if(level != 1)
       left join 72crm_crm_customer_data as c on a.batch_id = c.batch_id and c.name = '客户级别'
    #end
    where (to_days(now()) - to_days(a.receive_time)) > #para(limit_day) and a.status = 1 and a.deal_status = 0
    #if(ids)
       and a.owner_user_id in (#fori(ids))
    #else
       and a.owner_user_id is not null
    #end
    #if(customer_level_setting == 2)
       and c.value = #para(level)
    #end
    #end

    #sql ("putInInternational")
      update 72crm_crm_customer set owner_user_id = null where customer_id in (#fori(ids))
    #end

    #sql ("queryDeleteAuth")
      select count(1) from 72crm_crm_customer_pool where pool_id = ? and find_in_set(?,admin_user_id)
    #end

    #sql ("deletePoolRelation")
      delete from 72crm_crm_customer_pool_relation where customer_id in (#fori(ids))
    #end
#end
