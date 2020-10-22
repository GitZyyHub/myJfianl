#namespace("work.product")

    #sql ("queryPageList")
        SELECT
            p.*,
            (select realname FROM 72crm_admin_user WHERE user_id=p.main_user_id) main_user_name,
            (select realname FROM 72crm_admin_user WHERE user_id=p.exploit_user_id) exploit_user_name,
            (select realname FROM 72crm_admin_user WHERE user_id=p.create_user_id) create_user_name
        FROM 72crm_work_product p
        where 1=1
        #for(query:queryList)
                #(query.get("sql"))
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
          LIMIT #(page),#(limit)
        #end
    #end

    #sql ("queryPageListCount")
    SELECT
         count(1)
        FROM 72crm_work_product
        where 1=1
        #for(query:queryList)
                #(query.get("sql"))
        #end
    #end


    /*----------产品功能-----------*/
    #sql ("queryFuntionPageList")
        SELECT
            function_id,
            product_id,
            function_number,
            function_name,
            function_describe,
            product_name,
            product_version,
            product_number,
            product_status,
            create_time,
            update_time,
            (SELECT realname FROM 72crm_admin_user where user_id=f.main_user_id) main_user_name,
            (SELECT realname FROM 72crm_admin_user where user_id=f.exploit_user_id) exploit_user_name,
            (SELECT realname FROM 72crm_admin_user where user_id=f.create_user_id) create_user_name
        FROM 72crm_work_product_function f
        where 1=1
        #if(_productId)
            and f.product_id=#para(_productId)
        #end
        #for(query:queryList)
            #(query.get("sql"))
        #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(page&&limit)
            LIMIT #(page),#(limit)
        #end
    #end


    #sql("queryFuntionPageListCount")
        SELECT
            count(1)
        FROM 72crm_work_product_function
        where 1=1
        #if(_productId)
            and product_id=#para(_productId)
        #end
        #for(query:queryList)
            #(query.get("sql"))
        #end
    #end

    #sql("queryStatistics")
        SELECT work_name,work_type
        (SELECT realname FROM 72crm_admin_user where user_id=w.sales_id) sales_name,
        ,work_phase from 72crm_work w
        WHERE work_id in (
            SELECT work_id from 72crm_work_demand WHERE demand_id in (
                SELECT demand_id FROM 72crm_work_demand_product WHERE product_id =#para(productId)
            )
        )
    #end
#end