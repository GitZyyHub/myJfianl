#namespace("admin.field")
    #sql ("queryField")
        SELECT `field_id`,`field_name`,`name`,`type`,`label`,`remark`,
        `input_tips`,`default_value`,`is_unique`,`is_null`,`sorting`,`options`,`field_type`
        FROM `72crm_admin_field` WHERE `label` = ? and `is_hidden`=0 order by sorting
    #end

    #sql("querySysInformationById")
        SELECT create_time,create_user_id,update_time FROM #para(tableName) WHERE #para(tableId)=#para(id)
    #end









#sql ("queryAddField")
    select field_id,field_name,name,type,input_tips,options,is_unique,is_null,"" as value,field_type,label,default_value
    from 72crm_admin_field where label = ? and is_hidden = 0 order by sorting
    #end
    #sql ("queryInformationField")
    select field_name,name,type,field_type from 72crm_admin_field where label = ? and is_hidden = 0 order by sorting
    #end

    #sql("queryFields")
      SELECT IFNULL(label,'1') as label,IFNULL(MAX(update_time),'2000-01-01 00:00:00') as update_time,'线索管理' as name,'crm_leads' as types FROM 72crm_admin_field WHERE label='1'
      union all
      SELECT IFNULL(label,'2') as label,IFNULL(MAX(update_time),'2000-01-01 00:00:00') as update_time,'客户管理' as name,'crm_customer' as types FROM 72crm_admin_field WHERE label='2'
      union all
      SELECT IFNULL(label,'3') as label,IFNULL(MAX(update_time),'2000-01-01 00:00:00') as update_time,'联系人管理' as name,'crm_contacts' as types FROM 72crm_admin_field WHERE label='3'
      union all
      SELECT IFNULL(label,'4') as label,IFNULL(MAX(update_time),'2000-01-01 00:00:00') as update_time,'产品管理' as name,'crm_product' as types FROM 72crm_admin_field WHERE label='4'
      union all
      SELECT IFNULL(label,'5') as label,IFNULL(MAX(update_time),'2000-01-01 00:00:00') as update_time,'商机管理' as name,'crm_business' as types FROM 72crm_admin_field WHERE label='5'
      union all
      SELECT IFNULL(label,'6') as label,IFNULL(MAX(update_time),'2000-01-01 00:00:00') as update_time,'合同管理' as name,'crm_contract' as types FROM 72crm_admin_field WHERE label='6'
      union all
      SELECT IFNULL(label,'7') as label,IFNULL(MAX(update_time),'2000-01-01 00:00:00') as update_time,'回款管理' as name,'crm_receivables' as types FROM 72crm_admin_field WHERE label='7'
      union all
      SELECT IFNULL(label,'17') as label,IFNULL(MAX(update_time),'2000-01-01 00:00:00') as update_time,'客户回访管理' as name,'crm_return_visit' as types FROM 72crm_admin_field WHERE label='17'
    #end
    #sql("deleteByChooseId")
      DELETE FROM 72crm_admin_field WHERE field_id not in
      (
        #for(i:ids)
           #(for.index > 0 ? "," : "") #para(i)
        #end
      )
      and (operating = '0' or operating = '2')
      and label=#para(label)
      #if(label==10)
        and examine_category_id=#para(categoryId)
      #end
    #end
    #sql("deleteByFieldValue")
      DELETE FROM #(dataTableName) WHERE field_id in
      (
      SELECT field_id FROM 72crm_admin_field WHERE field_id not in (#fori(ids))
      and (operating = '0' or operating = '2')
      and label=#para(label)
      #if(label==10)
        and examine_category_id=#para(categoryId)
      #end
      )
    #end

    #sql ("updateFieldSort")
    update 72crm_admin_field_sort set #if(field_type == 0)field_name = #para(name),#end name = #para(name),type = #para(type)
    where field_id = #para(field_id)
    #end

    #sql ("updatePoolFieldSetting")
    update 72crm_crm_customer_pool_field_setting set #if(field_type == 0)field_name = #para(name),#end name = #para(name),type = #para(type)
    where field_id = #para(field_id)
    #end

    #sql ("deleteFieldSort")
    delete from 72crm_admin_field_sort where label = #para(label) and field_name in (#fori(names))
    #end

    #sql ("deleteCustomerPoolFieldSetting")
    delete from 72crm_crm_customer_pool_field_setting where field_name in (#fori(names))
    #end

    #sql ("deleteCustomerPoolField")
    delete from 72crm_crm_customer_pool_field_sort where field_name in (#fori(names))
    #end

    #sql ("updateRelateFieldName")
    update 72crm_admin_field set name = #para(name)
    where field_name = #para(fieldName)  and label = #para(label) and field_type = 1
    #end
    #sql ("updateRelateSortFieldName")
        update `72crm_admin_field_sort` set name = #para(name)
        where field_name = #para(fieldName) and label = #para(label);
    #end

    #sql ("customerFieldList")
    select field_id,field_name,name,type,options from 72crm_admin_field where field_type != 1 and label = ? and is_hidden = 0
    #end

    #sql("list")
      SELECT field_id,field_name,name,type,label,remark,input_tips,max_length,default_value,is_unique,is_null,options,is_hidden,operating,update_time,examine_category_id,field_type,relevant FROM 72crm_admin_field WHERE label=#para(label)
      #if(categoryId)
        and examine_category_id=#para(categoryId)
      #end
      #if(isHidden)
        and is_hidden = #para(isHidden)
      #end
      ORDER BY sorting asc
    #end
    #sql("queryFieldStyle")
      SELECT * FROM 72crm_admin_field_style WHERE type=? and field_name =? and user_id=? limit 1
    #end
    #sql("queryPoolFieldStyle")
      SELECT * FROM 72crm_crm_customer_pool_field_style WHERE pool_id=? and field_name =? and user_id=? limit 1
    #end
    #sql("queryFieldIsExist")
      SELECT COUNT(*) FROM #(dataTableName) WHERE field_id=#para(field_id)  and value=#para(value)
      #if(batchId)
        and batch_id!=#para(batchId)
      #end
    #end
    #sql("queryFixedIsExist")
      SELECT COUNT(*) FROM 72crm_#(tableName) WHERE #(field_name)=#para(value)
      #if(label==2||label==5||label==4)
        and status != 3
      #elseif(label==6||label==7)
        and check_status != 7
      #end
      #if(batchId)
        and batch_id!=#para(batchId)
      #end
    #end
    #sql("queryFieldValue")
      #if(field_type==1)
        SELECT #(primaryKey),batch_id FROM 72crm_#(tableName) where #(field_name) = #para(value)
      #elseif(field_type==0)
        SELECT #(primaryKey),batch_id FROM 72crm_#(tableName) WHERE batch_id=(SELECT batch_id FROM #(dataTableName) WHERE field_id=#para(field_id) and value=#para(value) LIMIT 0,1)
      #end
    #end
    #sql("queryFieldValueNoDelete")
      SELECT #(primaryKey),batch_id FROM  #(tableName) where 1=1
      #if(label==2||label==4)
        and status != 3
      #elseif(label==6||label==7)
        and check_status != 7
      #end
      and (
        #for(record : uniqueList)
          #if(record.get("value")&&record.get("field_type")==1)
              #(for.index > 0 ? "or" : "") #(record.get("field_name")) = #para(record.get("value"))
          #elseif(record.get("value"))
              #(for.index > 0 ? "or" : "") batch_id in (SELECT batch_id FROM #(dataTableName) WHERE field_id= #para(record.get("field_id")) and value= #para(record.get("value")))
          #end
        #end
      )
    #end
    #sql ("queryListHead")
    select field_name as fieldName,name,type,field_id from 72crm_admin_field_sort where is_hide = 0 and label = ? and user_id = ? order by sort asc
    #end

    #sql ("queryDefaultPool")
    select pool_id from 72crm_crm_customer_pool
    where status = 1 and (find_in_set(#para(userId),admin_user_id) or find_in_set(#para(userId),member_user_id)
    or find_in_set(#para(deptId),member_dept_id)) order by create_time desc limit 1
    #end

    #sql ("queryDefaultPoolField")
    select pool_id,field_id,field_name,name,type from 72crm_crm_customer_pool_field_setting where pool_id = ? and is_hidden = 0
    #end

    #sql ("queryPoolListHead")
    select field_id,field_name,name,type from 72crm_crm_customer_pool_field_sort
    where pool_id = ? and user_id = ? and is_hidden = 0 order by sort
    #end

    #sql ("queryDefaultPoolListHead")
    select field_name,name,type from 72crm_crm_customer_pool_field_setting where pool_id = ?
    #end

    #sql ("queryFieldConfig")
    select id,name,field_name from 72crm_admin_field_sort where is_hide = ? and label = ? and user_id = ? order by sort asc
    #end

    #sql ("queryPoolFieldConfig")
    select id,name,field_name from 72crm_crm_customer_pool_field_sort where is_hidden = ? and pool_id = ? and user_id = ? order by sort asc
    #end

    #sql ("sort")
    update 72crm_admin_field_sort set is_hide = 0,sort = ? where label = ? and user_id = ? and id = ?
    #end

    #sql ("isHide")
    update 72crm_admin_field_sort set is_hide = 1,sort = 0
    where id in (
       #for(i : ids)
           #(for.index > 0 ? "," : "")#para(i)
       #end
   ) and label = #para(label) and user_id = #para(userId)
   #end

    #sql ("poolSort")
    update 72crm_crm_customer_pool_field_sort set is_hidden = 0,sort = ? where pool_id = ? and user_id = ? and id = ?
    #end

    #sql ("poolIsHide")
    update 72crm_crm_customer_pool_field_sort set is_hidden = 1,sort = 0
    where id in (
       #for(i : ids)
           #(for.index > 0 ? "," : "")#para(i)
       #end
   ) and pool_id = #para(poolId) and user_id = #para(userId)
   #end

   #sql ("deleteFieldv")
    delete from #(dataTableName) WHERE `batch_id` in (#fori(batchList))
   #end
    #sql ("searchPhone")
    SELECT
        af.field_id,
        af.label,
        adv.`value`,
        adv.batch_id
    FROM
        72crm_admin_field af
    LEFT JOIN #(dataTableName) adv ON (af.field_id = adv.field_id)
    WHERE
        af.type = 7
    AND af.field_type = 0
    AND (
        af.label = 1
        OR af.label = 2
        OR af.label = 3
    )
    and (adv.value like CONCAT('%',#para(search),'%') or adv.value like CONCAT('%',#para(search),'%')  )
    limit 0,1
   #end
   #sql("updateDataName")
    update #(tableName) set name = #para(name) where field_id = #para(fieldId)
   #end
#end
