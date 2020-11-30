#namespace("statistics")

    #sql("queryDept")
        select dept_id from 72crm_admin_dept where 1=1
        #if(deptIds)
            and dept_id in (#fori(deptIds))
        #end
        order by num desc
    #end

    #sql("workDeptStatistics")
        SELECT d.`name`,w.* FROM 72crm_admin_dept d
        RIGHT JOIN
        #if(isDept==1)
        (select count(1) counts,dept_id dept from 72crm_work
         #if(deptIds)
           where dept_id in (#fori(deptIds))
         #end
         GROUP BY dept_id) w
        #else
        (select count(1) counts,area dept from 72crm_work
         #if(deptIds)
          where area in (#fori(deptIds))
         #end
         GROUP BY area) w
        #end
        on d.dept_id=w.dept
    #end

#end