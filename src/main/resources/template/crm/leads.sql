#namespace("crm.leads")
    #sql("getLeadsPageList")
    select a.leads_id,a.leads_name,b.realname as ownerUserName
    from 72crm_crm_leads as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
    where 1=1
      #if(leadsName)
      and a.leads_name like CONCAT('%',#para(leadsName),'%')
      #end
      #if(telephone)
      and a.telephone = #para(telephone)
      #end
      #if(mobile)
      and a.mobile = #para(mobile)
      #end
    #end

    #sql("count")
    select count(*) as nameNum from leadsview where leads_name = #para(leadsName)
    #if (leadsId)
    and leads_id != #para(leadsId)
    #end
    #end

    #sql("queryById")
    select a.*,a.leads_name as name,b.realname as ownerUserName,
    (select count(1) from 72crm_crm_leads_user_star where leads_id = a.leads_id and user_id = ?) as star
    from 72crm_crm_leads as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
    where leads_id = ?
    #end

    #sql ("updateOwnerUserId")
    update 72crm_crm_leads
    set owner_user_id = #para(ownerUserId),followup = 0
    where leads_id in (
        #for(i:ids)
          #(for.index > 0 ? "," : "")#para(i)
        #end
    )
    #end

    #sql ("updateIsTransform")
    update 72crm_crm_leads set is_transform = 1 where leads_id = ?
    #end

    #sql ("queryLeadsBatchId")
      select batch_id from 72crm_crm_leads where leads_id in(#fori(ids))
    #end
    #sql ("deleteByIds")
      delete from `72crm_crm_leads` WHERE `leads_id` in (#fori(ids))
    #end

    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_leads as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.leads_id = #para(leadsId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'线索详情' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_leads_data as b on a.batch_id = b.value
    inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
    inner join 72crm_crm_leads as d on d.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where d.leads_id = #para(leadsId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'跟进记录' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
    left join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.activity_type_id = #para(leadsId) and b.type = 1 and b.activity_type = 1
    #end

    #sql ("queryNum")
    select (
        (select count(*) from 72crm_admin_file as a left join 72crm_crm_leads as b on a.batch_id = b.batch_id
         where b.leads_id = #para(leadsId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_leads_data as b on a.batch_id = b.value
         inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
         inner join 72crm_crm_leads as d on d.batch_id = b.batch_id
         where d.leads_id = #para(leadsId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
         where b.activity_type_id = #para(leadsId) and b.type = 1 and b.activity_type = 1)
    ) as fileCount
    #end
     #sql("searchPhone")
        SELECT * FROM 72crm_crm_leads where 1=1
        and (mobile like CONCAT('%',#para(search),'%') or telephone like CONCAT('%',#para(search),'%')  )
        limit 0,1
    #end

    #sql("searchFieldValue")
        SELECT
            cl.leads_id,
            cl.leads_name,
            afd.`value`,
            afd.batch_id
        FROM
            (
                72crm_admin_field af
                LEFT JOIN 72crm_crm_leads_data afd ON (af.field_id = afd.field_id)
            )
        LEFT JOIN 72crm_crm_leads cl ON (afd.batch_id = cl.batch_id)
        WHERE
            af.type = 7
        AND af.label = 1
        AND cl.leads_id = #para(leadsId)
    #end
    #sql ("queryLeadPageListByFieldType1")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `z`.*
        FROM
          `72crm_crm_leads` as `a`
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
            FROM 72crm_crm_leads_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_leads` as a
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
    #sql ("queryLeadPageListByFieldType2")
      #(select) FROM (
        SELECT
           `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `z`.*
        FROM
          `72crm_crm_leads` as `a`
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
            72crm_crm_leads_data as a
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
    #sql ("queryLeadPageListCount")
      SELECT count(1) FROM `72crm_crm_leads` as a
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end
#end
