#namespace("work.demand")
    #sql ("queryDemandById")
        SELECT work_id,subsystem,demand_type,child_module,function1,function2,demand_id,demand_describe,demand_status,demand_version,plan,demand_number FROM 72crm_work_demand WHERE demand_id=?
    #end

    #sql("queryPageList")
        SELECT
            demand_id,
            work_id,
            subsystem,
            child_module,
            function1,
            function2,
            demand_describe,
            demand_type,
            demand_status,
            demand_version,
            plan,
            demand_number,
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

    #end

    #sql ("queryDemandByIdExport")
         SELECT
                    (select work_name from 72crm_work where work_id = d.work_id)
                    subsystem,
                    child_module,
                    function1,
                    function2,
                    demand_describe,
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
        SELECT work_id,subsystem,child_module,function1,function2,demand_describe,demand_status,demand_version,plan,demand_type,
        demand_number,demand_id,update_time FROM 72crm_work_demand WHERE demand_id=?
    #end

    #sql("queryAssociatedProduct")
        SELECT p.product_name,p.product_version,p.product_number,d.product_percentage,d.product_describe FROM 72crm_work_product p
        left join 72crm_work_demand_product d on p.product_id=d.product_id
        where d.demand_id =?
    #end

    #sql ("queryAssociatedFunction")
        SELECT function_name,function_number FROM 72crm_work_product_function WHERE function_id in (
            SELECT function_id FROM 72crm_work_demand_function WHERE demand_id =?)
    #end

    #sql ("queryDemandProduct")
    SELECT
        d.demand_id,
        d.subsystem,
        d.child_module,
        d.function1,
        d.function2,
        a.*
    FROM
        72crm_work_demand d
        JOIN (
        SELECT
            p.product_id,
            p.product_name,
            p.product_version,
            c.product_percentage,
            c.product_describe
        FROM
            72crm_work_product p
            INNER JOIN (SELECT product_id, product_percentage, product_describe FROM 72crm_work_demand_product e WHERE e.demand_id = #para(demandId) ) c ON p.product_id = c.product_id
        ) a
    WHERE
        d.demand_id = #para(demandId)
    #end

    #sql ("queryDemandFunction")
    SELECT
    function_id,
        product_id,
        product_name,
        product_version,
        main_user_id,
        (SELECT realname FROM 72crm_admin_user WHERE user_id=f.main_user_id) main_user_name,
        (SELECT realname FROM 72crm_admin_user WHERE user_id=f.exploit_user_id) exploit_user_name,
        exploit_user_id,
        function_name,
        function_number,
        product_status,
        #if(functionIds)
            1 as tag,
        #end
        function_describe
    FROM
        72crm_work_product_function f
    WHERE 1=1
        and product_id in (#fori(productIds))
        #if(functionIdsNot)
            AND function_id NOT IN (#fori(functionIdsNot))
        #end
        #if(functionIds)
            AND function_id IN (#fori(functionIds))
        #end
    #end

    #sql("groupProductIdByFunction")
    select product_id from 72crm_work_product_function where function_id in (#fori(functionIds)) group by product_id
    #end

#end