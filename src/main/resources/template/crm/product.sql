#namespace("crm.product")
    #sql("getProductPageList")
      select a.*, d.`value`
      from 72crm_crm_product as a
      join 72crm_crm_product_data d on d.name = '是否上下架' and d.batch_id = a.batch_id
      where  d.`value` = '上架'
      and status is not null  and status != 3
       #if(categoryId)
        and a.category_id = #para(categoryId)
        #end
         #if(search)
        and a.name like concat('%',#para(search),'%')
        #end
         #if(ids)
         and product_id in (#fori(ids))
        #end
    #end
    #sql("totalRowSql")
     select count(*) from productview
    #end
    #sql("queryById")
       select ccp.*, sc.name as categoryName
       from 72crm_crm_product as ccp
       LEFT JOIN 72crm_crm_product_category as sc on sc.category_id = ccp.category_id
       where ccp.product_id = ?  and ccp.status != 3
    #end
    #sql("queryProductPageList")
        SELECT *,
             (select value from `72crm_crm_product_data` a  where a.batch_id = c.batch_id and a.name = '单位') as unit,
             (select value from `72crm_crm_product_data` b  where b.batch_id = c.batch_id and b.name = '是否上下架') as `是否上下架`
        from
             (select sccp.product_id,scp.name as product_name,sccp.price,sccp.sales_price,sccp.num,sccp.discount,sccp.subtotal,c.name as category_name,scp.batch_id
             FROM 72crm_crm_contract_product sccp
                 LEFT JOIN 72crm_crm_product as scp on scp.product_id = sccp.product_id
                 left join `72crm_crm_product_category` as c on scp.category_id = c.category_id
             where sccp.contract_id = ?) as c
    #end
     #sql("queryByCategoryId")
      select count(*) from 72crm_crm_product where category_id = ?
    #end
    #sql ("queryCategoryByParentId")
      select count(*) from 72crm_crm_product_category where pid = ?
    #end
    #sql("getByNum")
      select COUNT(*) from 72crm_crm_product where num = ? and status != 3
    #end
    #sql("querySubtotalByContractId")
      select distinct ccc.discount_rate,
      round(((select SUM(subtotal) from 72crm_crm_contract_product WHERE contract_id = ccc.contract_id)/100*(100 - ccc.discount_rate) ),2)as money
      from 72crm_crm_contract as ccc
      where ccc.contract_id = ?
    #end
    #sql("querySubtotalByBusinessId")
      select distinct ccc.discount_rate,
      round((( select SUM(subtotal) from 72crm_crm_business_product WHERE business_id = ccc.business_id ) /100*(100 - ccc.discount_rate) ),2)as money
      from 72crm_crm_business as ccc
      where ccc.business_id = ?
    #end

    #sql ("queryInformationById")
      select a.name,b.name as category_name,a.num,a.price,a.description,a.batch_id
      from `72crm_crm_product` a
             left join `72crm_crm_product_category` b on a.category_id = b.category_id
      where product_id = ?
    #end

    #sql ("queryAllCategory")
      select  * from 72crm_crm_product_category
      where 1 = 1
    #end
    #sql("queryWeiXinById")
       select ccp.*, sc.name as categoryName,b.remarks
       from 72crm_crm_product as ccp
       LEFT JOIN 72crm_crm_product_category as sc on sc.category_id = ccp.category_id
        LEFT JOIN 72crm_crm_product_detail_img as b on b.product_id = ccp.product_id
       where ccp.product_id = ?
    #end
    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a inner join 72crm_crm_product as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.product_id = #para(productId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'产品详情' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_product_data as b on a.batch_id = b.value
    inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
    inner join 72crm_crm_product as d on d.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where d.product_id = #para(productId)
    #end

    #sql ("queryNum")
    select (
        (select count(*) from 72crm_admin_file as a join 72crm_crm_product as b on a.batch_id = b.batch_id
         where b.product_id = #para(productId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_product_data as b on a.batch_id = b.value
         inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
         inner join 72crm_crm_product as d on d.batch_id = b.batch_id
         where d.product_id = #para(productId))
    ) as fileCount
    #end

    #sql ("queryProductPageListByFieldType1")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`name` AS `category_name`,
          `z`.*
        FROM
          `72crm_crm_product` as `a`
          LEFT JOIN `72crm_crm_product_category` `d` ON `a`.`category_id` = `d`.`category_id`
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
            FROM 72crm_crm_product_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_product` as a
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
    #sql ("queryProductPageListByFieldType2")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `d`.`name` AS `category_name`,
          `z`.*
        FROM
          `72crm_crm_product` as `a`
          LEFT JOIN `72crm_crm_product_category` `d` ON `a`.`category_id` = `d`.`category_id`
        LEFT JOIN (
          SELECT
            a.batch_id as field_batch_id
            #for(field : fieldMap)
              #if(field.value&&field.value.get("field_type")==0||field.value.get("field_type")==2)
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
            72crm_crm_product_data as a
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
    #sql ("queryProductPageListCount")
      SELECT count(1) FROM `72crm_crm_product` as a
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end

    #sql ("updateOwnerUserId")
    update 72crm_crm_product set owner_user_id = #para(ownerUserId) where product_id in (#fori(ids))
    #end

    #sql ("updateStatus")
    update 72crm_crm_product set status = #para(status) where product_id in (#fori(ids))
    #end
    #sql ("queryDetailImg")
      select * from 72crm_crm_product_detail_img where product_id = #para(productId) and type = #para(type)
    #end
     #sql ("queryByUserId")
    select * from 72crm_crm_product_user where user_id = #para(userId)
    #end
     #sql("getProductByIds")
      select a.*,d.`value`
      from 72crm_crm_product as a
       join 72crm_admin_field c on c.name = '是否上下架'
      join 72crm_admin_fieldv d on d.name = '是否上下架' and d.batch_id = a.batch_id
      where  1 = 1
        and product_id in (#fori(ids))
    #end
#end
