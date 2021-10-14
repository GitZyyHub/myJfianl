#namespace("statistics")

    #sql("queryDept")
        select dept_id,name from 72crm_admin_dept where 1=1
        #if(deptIds)
            and dept_id in (#fori(deptIds))
        #end
        order by num desc
    #end



    #sql("workDeptStatistics1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name , counts,'#(x.dept_id)' as deptId from (
            select ifnull(count(1),0) counts from 72crm_work a where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
             #if(deptIds)
                and (dept_id = #(x.dept_id) or area = #(x.dept_id))
             #end
             and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             or (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) w
        #end
    #end

    #sql("workDeptStatistics2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name, sum(counts) counts from (
                    select work_id,ifnull(count(1),0) counts from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                or (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            )w
        #end

    #end

    #sql("workWorkphaseStatistics")
        #for(x : search)
        #(for.index == 0 ? "" : "union all")
            select '#(x)' as name , counts from(
            #if(type==4)
                select ifnull(count(1),0) counts from 72crm_work where work_phase = '#(x)'
            #else if(type==5)
                select ifnull(count(1),0) counts from 72crm_work where
                #if(x == '已结项')
                    work_phase='结项'
                #else if(x == '未结项')
                    work_phase!='结项'
                #else
                    work_phase = '#(x)'
                #end
            #end
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) w
        #end
    #end

    #sql("workTimeStatistics")
        #for(x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(count(1),0) counts from 72crm_work where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            #if(sqlDateFormat == '%Y%m%d')
             and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
                and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
                or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
        #end
    #end

-- 项目盈利

    #sql("profitDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from (
            select ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from 72crm_work where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
             #if(deptIds)
                and (dept_id = #(x.dept_id) or area = #(x.dept_id))
             #end
             and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) w
        #end
    #end

    #sql("profitDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from (

                    select ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))

            )w
        #end
    #end


        #sql("profitWorkphase")
        #for(x : search)
        #(for.index == 0 ? "" : "union all")
            select '#(x)' as name , ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from(
            #if(type==4)
                select ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from 72crm_work where work_phase = '#(x)'
            #else if(type==5)
                select ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from 72crm_work where
                #if(x == '已结项')
                    work_phase='结项'
                #else if(x == '未结项')
                    work_phase!='结项'
                #else
                    work_phase = '#(x)'
                #end
            #end
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) w
        #end
    #end

    #sql("profitTime")
        #for(x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from 72crm_work where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            #if(sqlDateFormat == '%Y%m%d')
             and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
                and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
                or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
        #end
    #end

-- 项目成本

    #sql("costDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_work where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(isAll==2)
                    and implement_cost<=(effective_amount * #para(rate))
                #end
                and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        #end
    #end

    #sql("costDept2")
        #for(x : deptIds)
         #(for.index == 0 ? "" : "union all")
        select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,sum(counts) counts from (

                select ifnull(count(1),0) counts from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    #if(isAll==2)
                        #if(rate)
                            and implement_cost<=(effective_amount * #para(rate))
                        #end
                    #end
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
                    and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                    and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))

            ) w
        #end
    #end

    #sql("costWorkphase")
        #for(x : search)
        #(for.index == 0 ? "" : "union all")
            select '#(x)' as name ,count(1) counts from 72crm_work where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            #if(isAll==2)
                #if(rate)
                    and implement_cost<=(effective_amount * #para(rate))
                #end
            #end
            #if(x == '已结项')
                and work_phase='结项'
            #else if(x == '未结项')
                and work_phase!='结项'
            #else
                and work_phase = '#(x)'
            #end
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        #end
    #end

    #sql("costTime")
        #for(x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,count(1) counts from 72crm_work where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            #if(isAll==2)
                #if(rate)
                    and implement_cost<=(effective_amount * #para(rate))
                #end
            #end
            #if(sqlDateFormat == '%Y%m%d')
             and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
                and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
                or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
        #end
    #end

-- 项目预警

    #sql("queryWorkIdByDeptId")
            select work_id,DATE_FORMAT(start_time, '%Y%m%d' ) start_time,DATE_FORMAT(end_time, '%Y%m%d' ) end_time from 72crm_work a where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                and (dept_id in (#fori(below)) or area in (#fori(below)))
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
    #end

    #sql("settingWarningNum")
        select sum(counts) counts from(
        #for(x:works)
        #(for.index == 0 ? "" : "union all")
            select count(1) counts from 72crm_work_resources_setting where 1=1 and resources_status ='未到位'
            and work_id = #(x.work_id)
            and ((DATE_FORMAT(plan_time, '%Y%m%d') - DATE_FORMAT(now(), '%Y%m%d')) / (#(x.end_time)-#(x.start_time)) ) < 0.1
        #end
        ) w
    #end

    #sql("settingAlarmNum")
    select sum(counts) counts from(
        #for(x:works)
        #(for.index == 0 ? "" : "union all")
            select count(1) counts from 72crm_work_resources_setting where 1=1 and resources_status ='未到位'
            and work_id = #(x.work_id)
            and (DATE_FORMAT(plan_time, '%Y%m%d') - DATE_FORMAT(now(), '%Y%m%d'))  < 0
        #end
        ) w
    #end

        #sql("warningTime")
        select work_id,DATE_FORMAT(start_time, '%Y%m%d' ) start_time,DATE_FORMAT(end_time, '%Y%m%d' ) end_time from 72crm_work where 1=1
        and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
        #if(sqlDateFormat == '%Y%m%d')
             and (#(startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
                and (#(startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(endTime)
                or #(startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(endTime))
            #end
    #end


-----------------风险-------------------------

    #sql("queryWorkIdByDeptId2")
    select work_id,manager_id from 72crm_work a where 1=1
    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
    and (dept_id=#para(dept) or area=#para(dept))
    #end

    #sql("riskNum")
        SELECT COUNT(1) from 72crm_work_risk where work_id=#para(workId)
        #if(isComplete)
            and solve_time is not null and solve_user_id is not null
        #end
    #end

    #sql("queryWorkState")
    select work_id from 72crm_work where 1=1
        and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
        #if(x == '已结项')
            and work_phase='结项'
        #else if(x == '未结项')
            and work_phase!='结项'
        #end
    #end

    #sql("riskWorkType")
        select count(1) from 72crm_work_risk where work_id in (#fori(works))
        #if(state == 1)
            and risk_status='空'
        #else
            and risk_status='关闭'
        #end
    #end

    #sql("riskLevel")
         select count(1) counts from 72crm_work_risk where risk_level=#para(search) and work_id in (#fori(workIds))
         #if(state == 1)
            and risk_status='空'
         #else
            and risk_status='关闭'
         #end
    #end

-------------------产品------------------------

    #sql("queryDemandByWork")
        select demand_id from 72crm_work_demand where work_id in (
        select work_id from 72crm_work where 1=1
        and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
        and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        )
    #end

    #sql("usageAll")
        select count(1) counts ,(select product_name from 72crm_work_product where product_id =dp.product_id) name  from 72crm_work_demand_product dp where demand_id in (#fori(demandIds))
        and product_id in (select product_id from 72crm_work_product) group by product_id
    #end

    #sql("usageArea2")
    select ifnull(sum(counts),0) counts,name from (
        #for(x :deptIds)
        #(for.index == 0 ? "" : "union all")
         select count(1) counts,(select product_name from 72crm_work_product where product_id = dp.product_id) name from 72crm_work_demand_product dp where demand_id in (
                select demand_id from 72crm_work_demand where work_id in (
                    select work_id from 72crm_work where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
                    and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                    and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))

                )
            ) group by product_id
        #end
        ) w group by name
    #end

    #sql("usageArea1")
    select sum(counts) counts,name from (
        #for(x :deptIds)
        #(for.index == 0 ? "" : "union all")
         select count(1) counts,(select product_name from 72crm_work_product where product_id = dp.product_id) name from 72crm_work_demand_product dp where demand_id in (
                select demand_id from 72crm_work_demand where work_id in (
                    select work_id from 72crm_work where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                    and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                    and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                )
            ) group by product_id
        #end
        ) w group by name
    #end

    #sql("usageTime")
    select sum(counts) counts,name from (
        #for(x:timeList)
            #(for.index == 0 ? "":"union all")
            select ifnull(count(1),0) counts,(select product_name from 72crm_work_product where product_id = dp.product_id) name from 72crm_work_demand_product dp where demand_id in (
                select demand_id from 72crm_work_demand where work_id in (
                    select work_id from 72crm_work where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    #if(sqlDateFormat == '%Y%m%d')
                     and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
                    #else
                        and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
                        or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
                    #end
                )
            ) group by product_id
        #end
         ) w group by name
    #end

    #sql("queryDemandIdsByWorkId")
        select demand_id from 72crm_work_demand where work_id=?
    #end

    #sql("queryProductByWorkId")
        SELECT product_id,(select product_name from 72crm_work_product where product_id=dp.product_id) product_name,ifnull(sum(product_percentage),0) product_percentage from 72crm_work_demand_product dp WHERE
        demand_id in (#fori(demandIds))
        GROUP BY product_id
    #end

    #sql("queryFunctionByDemandIdsAndProductIdCount")
        select count(1) from 72crm_work_demand_function where demand_id in (#fori(demandIds))
        #if(multi)
        and product_id in (#fori(productIds))
        #else
        and product_id=#para(productId)
        #end
    #end

    #sql("queryProductDetails")
    SELECT COUNT(f.function_id) counts,p.* from
    (SELECT
        wp.product_id,
        wp.product_name,
		ifnull(sum(dp.product_percentage),0) product_percentage
        from
        `72crm_work_product` wp
        LEFT JOIN
        (select product_id,product_percentage from 72crm_work_demand_product where demand_id in (#fori(demandIds)))
        dp  ON dp.product_id = wp.product_id
    GROUP BY
        wp.product_id) p
    LEFT JOIN 72crm_work_demand_function f on p.product_id = f.product_id
    where f.demand_id in (#fori(demandIds))
    GROUP BY p.product_id
    #end

    #sql("queryWorkInDept2")
        select work_id from 72crm_work a where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            and (dept_id in (#fori(i)) or area in (#fori(i)))
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
    #end

    #sql("queryWorkInDept1")
        select work_id from 72crm_work a where 1=1
        and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
        and (dept_id = #para(deptId) or area = #para(deptId))
         and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
         and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
    #end

    #sql("sumWorkPercentage")
        SELECT IFNULL(SUM(dp.product_percentage),0) counts from 72crm_work_demand_product dp where demand_id in (
        SELECT demand_id from 72crm_work_demand where work_id in (#fori(i))
        )
        #if(productId)
        and product_id = #para(productId)
        #end
    #end

    #sql("countWorkFunction")
         SELECT COUNT(1) from 72crm_work_demand_function dp where demand_id in (
        SELECT demand_id from 72crm_work_demand where work_id in (#fori(i))
        )
        #if(productId)
        and product_id = #para(productId)
        #end
    #end

    #sql("queryWorkInTime")
        select work_id counts from 72crm_work where 1=1
        and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            #if(sqlDateFormat == '%Y%m%d')
             and (#(startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
                and (#(startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(endTime)
                or #(startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(endTime))
            #end
    #end

    #sql("sumHours")
        SELECT ifnull(SUM(working_hours),0) hours from 72crm_task_hours where check_status=1 and work_id in (#fori(workIds))
        #if(userIds)
        and create_user_id in (#fori(userIds))
        #end
    #end


--------------人力资源-------------------

    #sql("queryUserByDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
        select '#(x.name)' as name ,count(1) counts from (
            select user_id from 72crm_work_user
            where work_id in (
            select work_id from 72crm_work where 1=1
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            #if(deptIds)
                and (dept_id = #(x.dept_id) or area = #(x.dept_id))
             #end
             and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            )
            group by user_id) a
        #end
    #end

    #sql("queryUserByDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,count(1) counts from (
            select user_id from 72crm_work_user
            where work_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
           ) group by user_id ) a
        #end
    #end

    #sql("queryUserByTime")
        #for(x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,count(1) counts from 72crm_work_user
            where work_id in (
            select work_id from 72crm_work where
            and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            #if(sqlDateFormat == '%Y%m%d')
             and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
                and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
                or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
        #end
    #end


--------------------里程碑-----------------------

    #sql("distributionDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_milestone where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                 #end
            )
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            or (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #if(!isAll)
            and (SELECT CURDATE())>end_time and degree != 100
            #end
        #end

    #end

    #sql("distributionDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(count(1),0) counts from (
            select work_id from 72crm_milestone
            where work_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
            )
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            or (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #if(!isAll)
            and (select CURDATE())>end_time and degree != 100
            #end
            ) a
        #end
    #end

    #sql("distributionType")
            select count(1) from 72crm_milestone
            where work_id in (
                    select work_id from 72crm_work a where
                    (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #if(type == 1)
                and degree = 100
            #else if(type == 2)
                and (select CURDATE())<end_time and degree != 100
            #else if(type == 3)
                and degree != 100
                and (select CURDATE()) > end_time

            #end
    #end

    #sql("distributionDeptStatisticsTime")
        #for (x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(count(1),0) counts from 72crm_milestone
            where work_id in (
                select work_id from 72crm_work where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            #if(sqlDateFormat == '%Y%m%d')
            and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
            and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
            or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
            #if(isAll)
            and (select CURDATE())>end_time and degree != 100
            #end
        #end
    #end

    #sql("warningDept1")
        #for(x : deptIds)
            #(for.index == 0 ? "" : "union all")
                select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,count(1) counts from (
                select work_id from 72crm_milestone
                where work_id in (
                        select work_id from 72crm_work a where 1=1
                        and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                        and (dept_id in (#(x.below)) or area in (#(x.below)))
                )
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                 and degree != 100
                #if(warning)
                and (select CURDATE())<end_time
                and (timestampdiff( DAY ,now() ,end_time))/(timestampdiff(DAY,start_time,end_time)) < 0.1
                #else
                and (timestampdiff( DAY ,now() ,end_time)) < 0
                #end
                ) a
        #end

    #end

    #sql("warningDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,count(1) counts from 72crm_milestone where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                 #end
            )
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and degree != 100
            #if(warning)
            and (select CURDATE())<end_time
            and (timestampdiff( DAY ,now() ,end_time))/(timestampdiff(DAY,start_time,end_time)) < 0.1
            #else
            and (timestampdiff( DAY ,now() ,end_time)) < 0
            #end
        #end
    #end

    #sql("warningStatisticsTime")
        #for (x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,count(1) counts from 72crm_milestone
            where work_id in (
                select work_id from 72crm_work where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            #if(sqlDateFormat == '%Y%m%d')
            and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
            and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
            or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
             and degree != 100
            #if(warning)
            and (select CURDATE())<end_time
            and (timestampdiff( DAY ,now() ,end_time))/(timestampdiff(DAY,start_time,end_time)) < 0.1
            #else
            and (timestampdiff( DAY ,now() ,end_time)) < 0
            #end
        #end
    #end

-----------------任务------------------

    #sql("taskDistributionDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_task where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                 #end
            )
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            or (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #if(!isAll)
            and (SELECT CURDATE())>end_time and completeness != 100
            #end
            #if(isParent)
            and pid is null
            #else
            and pid is not null
            #end
        #end
    #end

    #sql("taskDistributionDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(count(1),0) counts from (
            select work_id from 72crm_task
            where work_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
            )
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            or (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #if(!isAll)
            and (select CURDATE())>end_time and completeness != 100
            #end
            #if(isParent)
            and pid is null
            #else
            and pid is not null
            #end
            ) a
        #end
    #end

    #sql("taskDistributionType")
            select count(1) from 72crm_task
            where work_id in (
                    select work_id from 72crm_work a where
                    (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #if(type == 1)
                and completeness = 100
            #else if(type == 2)
                and (select CURDATE())<end_time and completeness != 100
            #else if(type == 3)
                and completeness != 100
                and (select CURDATE()) > end_time
            #end

            #if(isParent)
            and pid is null
            #else
            and pid is not null
            #end
    #end

    #sql("taskDistributionTime")
        #for (x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,count(1) counts from 72crm_task
            where work_id in (
                select work_id from 72crm_work where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            #if(sqlDateFormat == '%Y%m%d')
            and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
            and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
            or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
            #if(!isAll)
            and (select CURDATE())>end_time and completeness != 100
            #end

            #if(isParent)
            and pid is null
            #else
            and pid is not null
            #end
        #end
    #end

    #sql("taskWarningDept1")
        #for(x : deptIds)
            #(for.index == 0 ? "" : "union all")
                select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,count(1) counts from (
                select work_id from 72crm_task
                where work_id in (
                        select work_id from 72crm_work a where 1=1
                        and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                        and (dept_id in (#(x.below)) or area in (#(x.below)))
                )
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                 and completeness != 100
                #if(warning)
                and (select CURDATE())<end_time
                and (timestampdiff( DAY ,now() ,end_time))/(timestampdiff(DAY,start_time,end_time)) < 0.1
                #else
                and (timestampdiff( DAY ,now() ,end_time)) < 0
                #end

                #if(isParent)
                and pid is null
                #else
                and pid is not null
                #end
                ) a
        #end
    #end

    #sql("taskWarningDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,count(1) counts from 72crm_task where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                 #end
            )
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and completeness != 100
            #if(warning)
            and (select CURDATE())<end_time
            and (timestampdiff( DAY ,now() ,end_time))/(timestampdiff(DAY,start_time,end_time)) < 0.1
            #else
            and (timestampdiff( DAY ,now() ,end_time)) < 0
            #end

            #if(isParent)
            and pid is null
            #else
            and pid is not null
            #end
        #end
    #end

        #sql("taskWarningStatisticsTime")
        #for (x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,count(1) counts from 72crm_task
            where work_id in (
                select work_id from 72crm_work where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            #if(sqlDateFormat == '%Y%m%d')
            and (#(x.startTime)  between DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)'))
            #else
            and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime)
            or #(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
            and completeness != 100
            #if(warning)
            and (select CURDATE())<end_time
            and (timestampdiff( DAY ,now() ,end_time))/(timestampdiff(DAY,start_time,end_time)) < 0.1
            #else
            and (timestampdiff( DAY ,now() ,end_time)) < 0
            #end
            #if(isParent)
            and pid is null
            #else
            and pid is not null
            #end
        #end
    #end

----------------需求-------------------

    #sql("demandDistributionDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_work_demand where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                #end
            )
            and (DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        #end
    #end


    #sql("demandDistributionDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(count(1),0) counts from (
            select work_id from 72crm_work_demand
            where work_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
            )
            and (DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) a
        #end
    #end

    #sql("demandDistributionTime")
        #for (x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(count(1),0) counts from 72crm_work_demand
            where work_id in (
                select work_id from 72crm_work where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            #if(sqlDateFormat == '%Y%m%d')
            and (#(x.startTime)  between DATE_FORMAT(create_time,'#(sqlDateFormat)') and DATE_FORMAT(create_time,'#(sqlDateFormat)'))
            #else
            and (#(x.startTime)<= DATE_FORMAT(create_time,'#(sqlDateFormat)') and DATE_FORMAT(create_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
        #end
    #end

    #sql("demandDistributionStatus")
        select count(1) counts from 72crm_work_demand
        where work_id in (
                select work_id from 72crm_work a where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(type == 2)
                    and (select CURDATE())<end_time
                #else if(type == 3)
                    and (select CURDATE()) > end_time
                #end
        )
        #if(type == 1)
            and demand_status = 100
        #else if(type == 2)
            and demand_status != 100
        #else if(type == 3)
            and demand_status != 100
        #end
        and (DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
    #end

    #sql("demandDistributionType")
        #for(x : search)
        #(for.index == 0 ? "" : "union all")
        select '#(x)' as name , ifnull(count(1),0) counts from 72crm_work_demand where work_id in (
        select work_id from 72crm_work where (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
        )
        and demand_type = '#(x)'
        and (DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        #end
    #end

    #sql("productCount1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(count(1),0) counts from 72crm_work_demand_product
            where demand_id in (
                select  demand_id from 72crm_work_demand where work_id in (
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
                #end

                )
            )
            #if(!isAll)
                #if(product)
                and product_percentage != '100'
                #else
                and product_percentage = '100'
                #end
            #end
        #end
    #end

    #sql("productCount2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts  from 72crm_work_demand_product
            where demand_id in (
                select  demand_id from 72crm_work_demand where work_id in (
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                #end
                )
            )
            #if(!isAll)
                #if(product)
                and product_percentage != '100'
                #else
                and product_percentage = '100'
                #end
            #end
        #end
    #end

    #sql("queryDemdand2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name, demand_id from 72crm_work_demand d
            left join 72crm_work w on d.work_id = w.work_id
            where (w.work_type ='自主实施（有合同）' or w.work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (w.dept_id = #(x.dept_id) or w.area = #(x.dept_id))
                #end
        #end
    #end

    #sql("queryDemdand1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,demand_id  from
            72crm_work_demand d left join 72crm_work w on d.work_id = w.work_id
                where (w.work_type ='自主实施（有合同）' or w.work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (w.dept_id in (#(x.below)) or w.area in (#(x.below)))
                #end
        #end
    #end

    #sql("queryDemandRelationCount")
        select count(1) counts from 72crm_work_demand_product where demand_id in(#fori(demandIds)) group by demand_id
    #end

    #sql("individuationCount")
        SELECT COUNT(1) from (
        select count(1) from 72crm_work_demand_product where demand_id in (#fori(demandIds)) and product_percentage = '100' GROUP BY demand_id) w
    #end

------------------资源------------------

    #sql("resourcesDistributionDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_work_resources_setting where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                #end
            )
            and (DATE_FORMAT(plan_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        #end
    #end

    #sql("resourcesDistributionDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(count(1),0) counts from (
            select work_id from 72crm_work_resources_setting
            where work_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
            )
            and (DATE_FORMAT(plan_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) a
        #end
    #end


    #sql("resourcesDistributionTime")
        #for (x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(count(1),0) counts from 72crm_work_resources_setting
            where work_id in (
                select work_id from 72crm_work where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            #if(sqlDateFormat == '%Y%m%d')
            and (#(x.startTime)  between DATE_FORMAT(plan_time,'#(sqlDateFormat)') and DATE_FORMAT(plan_time,'#(sqlDateFormat)'))
            #else
            and (#(x.startTime)<= DATE_FORMAT(plan_time,'#(sqlDateFormat)') and DATE_FORMAT(plan_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
        #end
    #end


    #sql("resourcesDistributionType")
        #for(x : search)
        #(for.index == 0 ? "" : "union all")
        select '#(x)' as name , ifnull(count(1),0) counts from 72crm_work_resources_setting where work_id in (
        select work_id from 72crm_work where (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
        )
        and resources_status = '#(x)'
        and (DATE_FORMAT(plan_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        #end
    #end

--------------工时-----------------

    #sql("hoursDistributionDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_task_hours where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                #end
            )
            and (DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        #end
    #end

    #sql("hoursDistributionDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(count(1),0) counts from (
            select work_id from 72crm_task_hours
            where work_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
            )
            and (DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) a
        #end
    #end


    #sql("hoursDistributionTime")
        #for (x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(count(1),0) counts from 72crm_task_hours
            where work_id in (
                select work_id from 72crm_work where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            #if(sqlDateFormat == '%Y%m%d')
            and (#(x.startTime)  between DATE_FORMAT(create_time,'#(sqlDateFormat)') and DATE_FORMAT(create_time,'#(sqlDateFormat)'))
            #else
            and (#(x.startTime)<= DATE_FORMAT(create_time,'#(sqlDateFormat)') and DATE_FORMAT(create_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
        #end
    #end


    #sql("hoursDistributionType")
        #for(x : search)
        #(for.index == 0 ? "" : "union all")
        select '#(x)' as name , ifnull(count(1),0) counts from 72crm_task_hours where work_id in (
        select work_id from 72crm_work where (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
        )
        and hours_type = '#(x)'
        and (DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        #end
    #end


    #sql("rankingDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(sum(working_hours),0) counts from 72crm_task_hours where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                #end
            )
            and check_status = 1
        #end
    #end

    #sql("rankingDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(sum(working_hours),0) counts from (
            select working_hours from 72crm_task_hours
            where work_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
            )
            and check_status = 1
            ) a
        #end
    #end

    #sql("rankingDeptResources2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_work_user where work_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                #end
            )
        #end
    #end

    #sql("rankingDeptResources1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(count(1),0) counts from (
            select user_id from 72crm_work_user
            where work_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
            )
            ) a
        #end
    #end


-------------------配置管理----------------


    #sql("configDistributionDept2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_work_config where associated_id in(
                select work_id from 72crm_work where 1=1
                and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                #if(deptIds)
                    and (dept_id = #(x.dept_id) or area = #(x.dept_id))
                #end
            )
            and (DATE_FORMAT(plan_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and type =2
        #end
    #end

    #sql("configDistributionDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name,ifnull(count(1),0) counts from (
            select associated_id from 72crm_work_config
            where associated_id in (
                    select work_id from 72crm_work a where 1=1
                    and (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
                    and (dept_id in (#(x.below)) or area in (#(x.below)))
            )
            and (DATE_FORMAT(plan_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and type =2
            ) a
        #end
    #end


    #sql("configDistributionTime")
        #for (x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(count(1),0) counts from 72crm_work_config
            where associated_id in (
                select work_id from 72crm_work where
                (work_type ='自主实施（有合同）' or work_type ='自主实施（无合同）')
            )
            and type =2
            #if(sqlDateFormat == '%Y%m%d')
            and (#(x.startTime)  between DATE_FORMAT(plan_time,'#(sqlDateFormat)') and DATE_FORMAT(plan_time,'#(sqlDateFormat)'))
            #else
            and (#(x.startTime)<= DATE_FORMAT(plan_time,'#(sqlDateFormat)') and DATE_FORMAT(plan_time,'#(sqlDateFormat)')<#para(x.endTime))
            #end
        #end
    #end



#end