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
             #if(deptIds)
                and (dept_id = #(x.dept_id) or area = #(x.dept_id))
             #end
             and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) w
        #end
    #end

    #sql("workDeptStatistics2")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select (select name from 72crm_admin_dept where dept_id='#(x.dept)') name, sum(counts) counts from (
            #for(i : x.below)
            #(for.index == 0 ? "" : "union all")
                    select ifnull(count(1),0) counts from 72crm_work a where 1=1
                    and (dept_id in (#fori(i)) or area in (#fori(i)))
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #end
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
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) w
        #end
    #end

    #sql("workTimeStatistics")
        #for(x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(count(1),0) counts from 72crm_work where
            (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime))
                    and (#(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
        #end
    #end

-- 项目盈利

    #sql("profitDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from (
            select ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from 72crm_work where 1=1
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
            #for(i : x.below)
            #(for.index == 0 ? "" : "union all")
                    select ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from 72crm_work a where 1=1
                    and (dept_id in (#fori(i)) or area in (#fori(i)))
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #end
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
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
             and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            ) w
        #end
    #end

    #sql("profitTime")
        #for(x:timeList)
            #(for.index == 0 ? "":"union all")
            select '#(x.startTime)' as name ,ifnull(sum(budget),0) budget,ifnull(sum(effective_amount),0) effective_amount,ifnull(sum(practical_cost),0) practical_cost from 72crm_work where
            (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime))
                    and (#(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
        #end
    #end

-- 项目成本

    #sql("costDept1")
        #for(x : deptIds)
        #(for.index == 0 ? "" : "union all")
            select '#(x.name)' as name ,ifnull(count(1),0) counts from 72crm_work where 1=1
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
            #for(i : x.below)
            #(for.index == 0 ? "" : "union all")
                select ifnull(count(1),0) counts from 72crm_work where 1=1
                    #if(isAll==2)
                        #if(rate)
                            and implement_cost<=(effective_amount * #para(rate))
                        #end
                    #end
                    and (dept_id in (#fori(i)) or and area in (#fori(i)))
                    and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                    and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            #end
            ) w
        #end
    #end

    #sql("costWorkphase")
        #for(x : search)
        #(for.index == 0 ? "" : "union all")
            select '#(x)' as name ,count(1) counts from 72crm_work where 1=1
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
            #if(isAll==2)
                #if(rate)
                    and implement_cost<=(effective_amount * #para(rate))
                #end
            #end
            and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime))
            and (#(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
        #end
    #end

-- 项目预警

    #sql("queryWorkIdByDeptId")
            select work_id,DATE_FORMAT(start_time, '%Y%m%d' ) start_time,DATE_FORMAT(end_time, '%Y%m%d' ) end_time from 72crm_work a where 1=1
                and (dept_id in (#fori(below)) or area in (#fori(below)))
                and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
    #end

    #sql("settingWarningNum")
        select sum(counts) counts from(
        #for(x:works)
        #(for.index == 0 ? "" : "union all")
            select count(1) counts from 72crm_work_resources_setting where 1=1 and actual_time is not null
            and work_id = #(x.work_id)
            and ((DATE_FORMAT(actual_time, '%Y%m%d') - DATE_FORMAT(now(), '%Y%m%d')) / (#(x.end_time)-#(x.start_time)) ) < 0.1
        #end
        ) w
    #end

    #sql("settingAlarmNum")
    select sum(counts) counts from(
        #for(x:works)
        #(for.index == 0 ? "" : "union all")
            select count(1) counts from 72crm_work_resources_setting where 1=1 and actual_time is not null
            and work_id = #(x.work_id)
            and (DATE_FORMAT(actual_time, '%Y%m%d') - DATE_FORMAT(now(), '%Y%m%d'))  < 0
        #end
        ) w
    #end

        #sql("warningTime")
        select work_id,DATE_FORMAT(start_time, '%Y%m%d' ) start_time,DATE_FORMAT(end_time, '%Y%m%d' ) end_time from 72crm_work where
        (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #(startTime) and #para(endTime))
        and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #(startTime) and #para(endTime))
    #end


-----------------风险-------------------------

    #sql("queryWorkIdByDeptId2")
    select work_id,manager_id from 72crm_work a where 1=1 and (dept_id=#para(dept) or area=#para(dept))
    #end

    #sql("riskNum")
        SELECT COUNT(1) from 72crm_work_risk where work_id=#para(workId) and create_user_id =#para(userId)
        #if(isComplete)
            and solve_time is not null and solve_user_id is not null
        #end
    #end

    #sql("queryWorkState")
    select work_id from 72crm_work where 1=1
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
         select count(1) counts from 72crm_work_risk where risk_level=#para(search)
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
        and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
        )
    #end

    #sql("usageAll")
        select count(1) counts ,(select product_name from 72crm_work_product where product_id =dp.product_id) name  from 72crm_work_demand_product dp where demand_id in (#fori(demandIds))
        and product_id in (select product_id from 72crm_work_product) group by product_id
    #end

    #sql("usageArea2")
    select sum(counts) counts,name from (
        #for(x :deptIds)
        #(for.index == 0 ? "" : "union all")
         select count(1) counts,(select product_name from 72crm_work_product where product_id = dp.product_id) name from 72crm_work_demand_product dp where demand_id in (
                select demand_id from 72crm_work_demand where work_id in (
                #for(i : x.below)
                #(for.index == 0 ? "" : "union all")
                    select work_id from 72crm_work where 1=1
                    and (dept_id in (#fori(i)) or area in (#fori(i)))
                    and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                    and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
                #end
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
                    and (#(x.startTime)<= DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(x.endTime))
                    and (#(x.startTime)<= DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(x.endTime))
                )
            ) group by product_id
        #end
         ) w group by name
    #end

    #sql("queryDemandIdsByWorkId")
        select demand_id from 72crm_work_demand where work_id=?
    #end

    #sql("queryProductByWorkId")
        SELECT product_id,(select product_name from 72crm_work_product where product_id=dp.product_id) product_name,sum(product_percentage) product_percentage from 72crm_work_demand_product dp WHERE
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
        LEFT JOIN 72crm_work_demand_product dp ON dp.product_id = wp.product_id
    GROUP BY
        wp.product_id) p
    LEFT JOIN 72crm_work_demand_function f on p.product_id = f.product_id GROUP BY p.product_id
    #end

    #sql("queryWorkInDept2")
        select work_id from 72crm_work a where 1=1
            and (dept_id in (#fori(i)) or area in (#fori(i)))
            and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
            and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
    #end

    #sql("queryWorkInDept1")
        select work_id from 72crm_work a where 1=1
        and (dept_id = #para(deptId) or area = #para(deptId))
         and (DATE_FORMAT(start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
         and (DATE_FORMAT(end_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime))
    #end

    #sql("sumWorkPercentage")
        SELECT IFNULL(SUM(dp.product_percentage),0) counts from 72crm_work_demand_product dp where demand_id in (
        SELECT demand_id from 72crm_work_demand where work_id in (#fori(i))
        )
    #end

    #sql("countWorkFunction")
        SELECT COUNT(1) from 72crm_work_demand_function dp LEFT JOIN (
        SELECT demand_id from 72crm_work_demand where work_id in (#fori(i))) wd on dp.demand_id=wd.demand_id
    #end

    #sql("queryWorkInTime")
        select work_id counts from 72crm_work where
        (#para(startTime)<=DATE_FORMAT(start_time,'#(sqlDateFormat)') and DATE_FORMAT(start_time,'#(sqlDateFormat)')<#para(endTime))
        and (#para(startTime)<=DATE_FORMAT(end_time,'#(sqlDateFormat)') and DATE_FORMAT(end_time,'#(sqlDateFormat)')<#para(endTime))
    #end
#end