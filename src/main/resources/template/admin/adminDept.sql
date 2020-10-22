#namespace("admin.dept")

    #sql("queryDeptById")
        select a.dept_id as id,a.name,
		(case a.pid when 0 then a.dept_id else a.pid end) pid,
        (case a.pid when 0 then a.name else b.name end) p_name
        from 72crm_admin_dept a left join  72crm_admin_dept b
        on a.pid=b.dept_id
    #end









    #sql("queryByIds")
      select dept_id as id,name from 72crm_admin_dept where dept_id in (
        #for(x:ids)
          #(for.index == 0 ? "" : ",")
              #para(x)
        #end
      )
    #end
    #sql("deptSql")
     select name,dept_id from 72crm_admin_dept ORDER BY num
    #end

    #sql ("queryDeptByParentUser")
    SELECT a.dept_id AS id,a.name,a.pid,b.user_id
    FROM 72crm_admin_dept as a LEFT JOIN 72crm_admin_user as b on a.dept_id=b.dept_id
    WHERE b.parent_id = ?
    #end


    #sql ("queryDeptIdByName")
        select dept_id from 72crm_admin_dept where name=?
    #end


#end
