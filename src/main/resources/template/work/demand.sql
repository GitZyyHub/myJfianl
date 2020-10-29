#namespace("work.demand")
    #sql ("queryDemandById")
        SELECT work_id,subsystem,demand_type,child_module,function1,function2,demand_id,`describe`,demand_status,demand_version,plan,demand_number FROM 72crm_work_demand WHERE demand_id=?
    #end

    #sql("queryPageList")
        SELECT
            demand_id,
            work_id,
            subsystem,
            child_module,
            function1,
            function2,
            `describe`,
            demand_type,
            demand_status,
            demand_version,
            plan,
            demand_number,
            product_percentage,
            product_describe,
            is_associated
            FROM
            72crm_work_demand

        where 1=1
        #if(_workId)
            and work_id = #para(_workId)
        #end
        #for(query:queryList)
                #(query.get("sql"))
        #end
        #if(dataAuth)
            and demand_id in (#fori(dataAuth))
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
          LIMIT #(page),#(limit)
        #end

    #end

    #sql("queryPageListCount")
    select count(1) from 72crm_work_demand where 1=1
        #if(_workId)
            and work_id = #para(_workId)
        #end
        #for(query:queryList)
                #(query.get("sql"))
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
          LIMIT #(page),#(limit)
        #end
    #end

    #sql ("queryDemandByIdExport")
         SELECT
                    (select work_name from 72crm_work where work_id = d.work_id)
                    subsystem,
                    child_module,
                    function1,
                    function2,
                    `describe`,
                    demand_type,
                    demand_status,
                    demand_version,
                    plan,
                    demand_number,
                    demand_id,
                    update_time,
                    product_percentage,
                    product_describe
         FROM
         72crm_work_demand d
         where 1=1
         #if(demandIds)
            and demand_id in (#fori(demandIds))
         #end

    #end

    #sql ("queryById")
        SELECT work_id,subsystem,child_module,function1,function2,`describe`,demand_status,demand_version,plan,demand_type,
        demand_number,demand_id,update_time,product_percentage,product_describe FROM 72crm_work_demand WHERE demand_id=?
    #end

    #sql("queryAssociatedProduct")
        SELECT product_name,product_version,product_number FROM 72crm_work_product WHERE product_id in (
            SELECT product_id FROM 72crm_work_demand_product WHERE demand_id =?)
    #end

    #sql ("queryAssociatedFunction")
        SELECT function_name,function_number FROM 72crm_work_product_function WHERE function_id in (
            SELECT function_id FROM 72crm_work_demand_function WHERE demand_id =?)
    #end

#end