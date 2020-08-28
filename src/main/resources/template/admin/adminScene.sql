#namespace("admin.scene")
    #sql ("queryPoolField")
    select a.field_name,a.name,a.type,b.options
    from 72crm_crm_customer_pool_field_setting as a left join 72crm_admin_field as b on a.field_id = b.field_id
    where pool_id = ? and a.is_hidden = 0
    #end

    #sql ("queryScene")
    select a.scene_id,a.data,a.name,if(b.default_id is null,0,1) as is_default,a.is_system,a.bydata
    from 72crm_admin_scene as a left join 72crm_admin_scene_default as b on a.scene_id = b.scene_id
    where a.type = ? and a.user_id = ? and is_hide = 0 order by a.sort asc
    #end
    #sql ("queryHideScene")
    select scene_id,name,data from 72crm_admin_scene where type = ? and user_id = ? and is_hide = 1
    #end

    #sql ("querySystemNumber")
    select count(*) from 72crm_admin_scene where is_system = 1 and type = ? and user_id = ?
    #end

    #sql ("sort")
    update 72crm_admin_scene set is_hide = 0,sort = ? where type = ? and user_id = ? and scene_id = ?
    #end

    #sql ("queryIsHideSystem")
    select count(scene_id) as number from 72crm_admin_scene where scene_id in (
       #for(i : ids)
           #(for.index > 0 ? "," : "")#para(i)
       #end
    ) and is_system = 1
    #end

    #sql ("isHide")
    update 72crm_admin_scene set is_hide = 1,sort = 0
    where scene_id in (
       #for(i : ids)
           #(for.index > 0 ? "," : "")#para(i)
       #end
    ) and type = #para(type) and user_id = #para(userId)
    #end

    #sql("queryPutInPoolToday")
    select IFNULL(GROUP_CONCAT(customer_id),0)
    from 72crm_crm_customer as a left join 72crm_crm_owner_record as b on a.customer_id = b.type_id
    where b.type = 9 and TO_DAYS(b.create_time) = TO_DAYS(NOW())
    #end

    #sql ("queryPutInPoolTodayNum")
    select count(*)
    from 72crm_crm_customer as a left join 72crm_crm_owner_record as b on a.customer_id = b.type_id
    where b.type = 9 and TO_DAYS(b.create_time) = TO_DAYS(NOW())
    #end

    #sql ("queryBatchId1")
      select batch_id from #(dataTableName) where name=#para(name) and `value` #(connector) #(value)
      #if(batchList&&batchList.size()>0)
        and batch_id in ( #fori(batchList))
      #end
    #end

    #sql ("queryBatchId2")
      select batch_id from #(dataTableName)
      where name = (select name from 72crm_admin_field where field_name = #para(name) and label = #para(label))
      and `value` #(connector) #(value)
      #if(batchList&&batchList.size()>0)
        and batch_id in ( #fori(batchList))
      #end
    #end

#end
