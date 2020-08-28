#namespace("crm.returnVisit")
    #sql ("queryReturnVisitPageListByFieldType1")
    #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          (SELECT customer_name FROM 72crm_crm_customer WHERE customer_id=`a`.`customer_id`) AS `customer_name`,
          (SELECT name FROM 72crm_crm_contacts WHERE contacts_id=`a`.`contacts_id`) AS `contacts_name`,
          (SELECT num FROM 72crm_crm_contract WHERE contract_id=`a`.`contract_id`) AS `contract_num`,
          (SELECT name FROM 72crm_crm_contract WHERE contract_id=`a`.`contract_id`) AS `contract_name`,
          `z`.*
        FROM
          `72crm_crm_return_visit` as `a`
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
            FROM 72crm_crm_return_visit_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_return_visit` as a
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

    #sql ("queryReturnVisitPageListByFieldType2")
      #(select) FROM (
        SELECT
           `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          (SELECT customer_name FROM 72crm_crm_customer WHERE customer_id=`a`.`customer_id`) AS `customer_name`,
          (SELECT name FROM 72crm_crm_contacts WHERE contacts_id=`a`.`contacts_id`) AS `contacts_name`,
          (SELECT num FROM 72crm_crm_contract WHERE contract_id=`a`.`contract_id`) AS `contract_num`,
          (SELECT name FROM 72crm_crm_contract WHERE contract_id=`a`.`contract_id`) AS `contract_name`,
          `z`.*
        FROM
          `72crm_crm_return_visit` as `a`
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
            72crm_crm_return_visit_data as a
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

    #sql ("queryReturnVisitPageListCount")
      SELECT count(1) FROM `72crm_crm_return_visit` as a
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end

    #sql ("queryById")
    select a.*,b.user_id,b.realname as owner_user_name,c.customer_name,d.name as contacts_name,e.num as contract_num
    from 72crm_crm_return_visit as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
    left join 72crm_crm_customer as c on a.customer_id = c.customer_id
    left join 72crm_crm_contacts as d on a.contacts_id = d.contacts_id
    left join 72crm_crm_contract as e on a.contract_id = e.contract_id
    where visit_id = ?
    #end

    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a inner join 72crm_crm_return_visit as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.visit_id = #para(visitId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'回访详情' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_return_visit_data as b on a.batch_id = b.value
    inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
    inner join 72crm_crm_return_visit as d on d.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where d.visit_id = #para(visitId)
    #end

    #sql ("queryReturnVisitBatchId")
      select batch_id from 72crm_crm_return_visit where visit_id in(#fori(ids))
    #end

    #sql ("deleteByIds")
      delete from `72crm_crm_return_visit` WHERE `visit_id` in (#fori(ids))
    #end
#end
