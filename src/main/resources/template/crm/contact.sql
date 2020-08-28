#namespace("crm.contact")
    #sql("getContactsPageList")
    select a.contacts_id,name,a.customer_id,c.customer_name,b.realname as ownerUserName
    from 72crm_crm_contacts as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
    left join 72crm_crm_customer as c on a.customer_id = c.customer_id
    where 1=1
      #if(contactsName)
      and a.name like CONCAT('%',#para(contactsName),'%')
      #end
      #if(customerName)
      and c.customer_name like CONCAT('%',#para(customerName),'%')
      #end
      #if(telephone)
      and a.telephone = #para(telephone)
      #end
      #if(mobile)
      and a.mobile = #para(mobile)
      #end
       #if(customerId)
      and a.customer_id = #para(customerId)
      #end
    #end

  #sql ("queryInformationById")
    select a.name,
           b.customer_id,
           b.customer_name,
           a.next_time,
           a.post,
           a.mobile,
           a.telephone,
           a.email,
           a.address,
           a.remark,
           a.batch_id
    from `72crm_crm_contacts` as a
           left join `72crm_crm_customer` b on a.customer_id = b.customer_id
    where a.contacts_id = ?
  #end

  #sql ("queryById")
  select a.*,b.customer_id,b.customer_name,
  (select count(1) from 72crm_crm_contacts_user_star where contacts_id = a.contacts_id and user_id = ?) as star
  from 72crm_crm_contacts as a left join 72crm_crm_customer as b on a.customer_id = b.customer_id
  where a.contacts_id = ?
  #end

  #sql ("transfer")
    update 72crm_crm_contacts
    set owner_user_id = #para(ownerUserId)
    where contacts_id in (
        #for(i : ids)
            #(for.index > 0 ? "," : "")#para(i)
        #end
    )
    #end

    #sql("queryBusiness")
    select a.business_id,a.business_name,a.money,f.customer_name,d.name as type_name,e.name as status_name,a.is_end,a.status_id,a.type_id
    from 72crm_crm_business as a inner join 72crm_crm_contacts_business as b on a.business_id = b.business_id
    inner join 72crm_crm_contacts as c on b.contacts_id = c.contacts_id
    left join 72crm_crm_business_type as d on a.type_id = d.type_id
    left join 72crm_crm_business_status as e on a.status_id = e.status_id
    inner join 72crm_crm_customer as f on a.customer_id = f.customer_id
    where c.contacts_id = ?
    #end

    #sql ("queryBusinessNumber")
    select count(*)
    from 72crm_crm_business as a inner join 72crm_crm_customer as b on a.customer_id = b.customer_id
    inner join 72crm_crm_contacts as c on b.customer_id = c.customer_id
    where c.contacts_id in (?)
    #end

    #sql ("queryByIds")
    select * from 72crm_crm_contacts
    where contacts_id in (
        #for(i:ids)
          #(for.index > 0 ? "," : "")#para(i)
        #end
    )
    #end

    #sql ("queryRepeatFieldNumber")
    select count(*) as number from 72crm_crm_contacts where 1=2
      #if(contactsName)
      or name = #para(contactsName)
      #end
      #if(telephone)
      or telephone = #para(telephone)
      #end
      #if(mobile)
      or mobile = #para(mobile)
      #end
    #end

    #sql ("queryRepeatField")
    select contacts_id,batch_id from 72crm_crm_contacts where 1=2
      #if(contactsName)
      or name = #para(contactsName)
      #end
      #if(telephone)
      or telephone = #para(telephone)
      #end
      #if(mobile)
      or mobile = #para(mobile)
      #end
    #end

    #sql ("unrelateBusiness")
      delete from 72crm_crm_contacts_business where contacts_id = #para(contactsId) and business_id in (#fori(ids))
    #end
    #sql("unrelateBusinessContacts")
      UPDATE `72crm_crm_business` SET `contacts_id` = NULL WHERE contacts_id = #para(contactsId) and `business_id`  in (#fori(ids))
    #end

    #sql("updateCustomerContacts")
      UPDATE `72crm_crm_customer` as a SET `contacts_id` = null
      WHERE a.`contacts_id` = #para(0)
    #end
    #sql("updateBusinessContacts")
      UPDATE `72crm_crm_business` as a SET `contacts_id` = null
      WHERE a.`contacts_id` = #para(0)
    #end

    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name from 72crm_admin_file as a inner join 72crm_crm_contacts as b on a.batch_id = b.batch_id
    left join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.contacts_id = #para(contactsId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'联系人详情' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_contacts_data as b on a.batch_id = b.value
    inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
    inner join 72crm_crm_contacts as d on d.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where d.contacts_id = #para(contactsId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'跟进记录' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.activity_type_id = #para(contactsId) and b.type = 1 and b.activity_type = 3
    #end

    #sql ("queryNum")
    select (
        (select count(*) from 72crm_admin_file as a join 72crm_crm_contacts as b on a.batch_id = b.batch_id
         where b.contacts_id = #para(contactsId) ) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_contacts_data as b on a.batch_id = b.value
         inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
         inner join 72crm_crm_contacts as d on d.batch_id = b.batch_id
         where d.contacts_id = #para(contactsId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
         where b.activity_type_id = #para(contactsId)  and b.type = 1 and b.activity_type = 3)
    ) as fileCount,
        (select count(*) from 72crm_crm_contacts_business as a inner join 72crm_crm_business as b on a.business_id = b.business_id
         where a.contacts_id = #para(contactsId) and b.status = 1 #(conditions)
    ) as businessCount
    #end
    #sql("searchPhone")
        SELECT * FROM 72crm_crm_contacts where 1=1
        and (mobile like CONCAT('%',#para(search),'%') or telephone like CONCAT('%',#para(search),'%')  )
        limit 0,1
    #end

    #sql("searchFieldValue")
        SELECT
            cc.contacts_id,
            cc.`name`,
            afd.`value`,
            afd.batch_id
        FROM
            (
                72crm_admin_field af
                LEFT JOIN 72crm_crm_contacts_data afd ON (af.field_id = afd.field_id)
            )
        LEFT JOIN 72crm_crm_contacts cc ON (afd.batch_id = cc.batch_id)
        WHERE
            af.type = 7
        AND af.label = 3
        AND cc.contacts_id = #para(contactsId)
    #end
    #sql ("queryContactsPageListByFieldType1")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`customer_name` AS `customer_name`,
          `z`.*
        FROM
          `72crm_crm_contacts` as `a`
          LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id` and `d`.owner_user_id is not null
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
                    #if(field.value.get("field_type")==2)
                    ,max(CASE WHEN `a`.`name` = '#(field.value.get("name"))' THEN `a`.`value` END) AS '#(field.value.get("field_name"))'
                   #else
                   ,max(CASE WHEN `a`.`name` = '#(field.key)'  THEN `a`.`value` END) AS '#(field.key)'
                   #end
                #end
              #end
            #end
            FROM 72crm_crm_contacts_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_contacts` as a
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
    #sql ("queryContactsPageListByFieldType2")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`customer_name` AS `customer_name`,
          `z`.*
        FROM
          `72crm_crm_contacts` as `a`
          LEFT JOIN `72crm_crm_customer` `d` ON `a`.`customer_id` = `d`.`customer_id` and `d`.owner_user_id is not null
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
                   #if(field.value.get("field_type")==2)
                    ,max(CASE WHEN `a`.`name` = '#(field.value.get("name"))' THEN `a`.`value` END) AS '#(field.value.get("field_name"))'
                   #else
                   ,max(CASE WHEN `a`.`name` = '#(field.key)'  THEN `a`.`value` END) AS '#(field.key)'
                   #end
                #end
              #end
            #end
          FROM
            72crm_crm_contacts_data as a
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
    #sql ("queryContactsPageListCount")
      SELECT count(1) FROM `72crm_crm_contacts` as a
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end
#end
