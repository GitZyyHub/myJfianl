#namespace("admin.user")
    #sql("queryByIds")
      select realname from 72crm_admin_user where user_id in (
        #fori(ids)
      )
    #end
    #sql("queryUserIdAndRealNameByIds")
      select user_id,realname from 72crm_admin_user where user_id in (
        #fori(ids)
      )
    #end

    #sql ("queryIdByName")
        select user_id from 72crm_admin_user where realname=?
    #end

    #sql("queryCountByNames")
      select user_id from 72crm_admin_user where realname in (
        #fori(names)
      )
    #end

    #sql ("queryUserByDeptIds")
        select user_id from 72crm_admin_user where dept_id in (#fori(deptIds))
    #end















    #sql("queryByUserName")
      select a.*,
      (SELECT username FROM 72crm_admin_dept WHERE dept_id = a.dept_id) as deptName,
      (SELECT realname FROM 72crm_admin_user WHERE user_id=a.parent_id) as parentName
      from 72crm_admin_user as a
      where a.username = #para(username)
    #end
    #sql("queryUserList")

    select
        a.job_number,a.rank,a.condition,
        (select name from 72crm_admin_dept where dept_id = a.area) area,
        a.realname,a.username,a.user_id,a.sex,a.mobile,a.email,e.name as deptName,a.status,a.create_time,a.dept_id,
        a.post,a.parent_id,a.img,
        (select b.realname from 72crm_admin_user b where b.user_id = a.parent_id) as parentName,
        a.post,a.parent_id,a.img,
        (select b.realname from 72crm_admin_user as b where b.user_id = a.parent_id) as parentName,
        (select group_concat(d.role_id) from 72crm_admin_user_role as c left join 72crm_admin_role as d on c.role_id = d.role_id where c.user_id = a.user_id) as roleId,
        (select group_concat(d.role_name) from 72crm_admin_user_role as c left join 72crm_admin_role as d on c.role_id = d.role_id where c.user_id = a.user_id) as roleName
        from 72crm_admin_user a
        left join 72crm_admin_dept e on a.dept_id = e.dept_id
        #if(roleId)
        left join 72crm_admin_user_role  g on a.user_id = g.user_id
        #end
        where  1=1
        #if(name)
          and (a.realname like concat('%', #para(name), '%') or a.mobile like concat('%', #para(name), '%'))
        #end
        #if(deptId.size()>0)
          and a.dept_id in (#fori(deptId))
        #end
        #if(status)
          and a.status = #para(status)
        #end
        #if(label==1)
          AND DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(create_time)
        #elseif(label==2)
          and a.status='2'
        #elseif(label==3)
          and a.status = '0'
        #end
        #if(roleId)
          and g.role_id = #para(roleId)
        #end
        #if(isResources)
            and is_resources=#para(isResources)
        #end
        #if(search)
          and (a.realname like concat('%', #para(search), '%')
          #if(pinyinSearch)
            or (
            #for(str : pinyinSearch)
                #(for.index == 0 ? "" : "and") #(str)
            #end
            )
          #end
          )
        #end
        order by status desc
    #end
    #sql("querySuperior")
      select id,realname from 72crm_admin_user where 1 = 1
      #if(name)
        and realname like concat('%', #para(name), '%')
      #end
    #end

    #sql("queryUserByRealName")
      select   au.realname,au.mobile,au.post as postName ,ad.name as deptName from 72crm_admin_user as au
      LEFT JOIN 72crm_admin_dept as ad on au.dept_id = ad.dept_id
      where 1=1
      #if(deptId)
        and au.dept_id = #para(deptId)
      #end
      #if(name)
        and au.realname like concat('%', #para(name), '%')
      #end
    #end
     #sql("queryUserByRealNameAndDeptId")
     select a.*,
         case
    when LENGTH( tmp_char) > 1 && tmp_loc > 0 && tmp_loc<24
    THEN (ELT(tmp_loc,'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','W','X','Y','Z'))
    ELSE  UPPER(tmp_char) end
    as initial
     from (
      select   au.realname,au.username,au.mobile,au.post as postName ,ad.name as deptName ,au.dept_id,au.user_id,au.status as user_status,
      case
      when (select  count(1) from 72crm_admin_attention where be_user_id = au.user_id and attention_user_id =  #para(userId)) > 0 then 1 ELSE  0 end as status,
      INTERVAL(CONV(HEX(CONVERT(LEFT(realname,1) USING GBK)),16,10),
      0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA ,0xCEF4,0xD1B9,0xD4D1) as tmp_loc,
      LEFT(realname,1) as tmp_char
      from 72crm_admin_user as au
      LEFT JOIN 72crm_admin_dept as ad on au.dept_id = ad.dept_id
      ) as a
      where 1=1
      #if(deptId)
        and dept_id = #para(deptId)
      #end
      #if(name)
        and (realname like concat('%', #para(name), '%') or username like concat('%', #para(name), '%'))
      #end
      #if(status)
        and user_status = #para(status)
      #end
      ORDER BY initial #(order)
    #end
    #sql("queryUserByUserId")
      select a.*,(SELECT name FROM 72crm_admin_dept WHERE dept_id = a.dept_id) as deptName,(SELECT realname FROM 72crm_admin_user WHERE user_id=a.parent_id) as parentName from 72crm_admin_user as a
      where a.user_id = ?
    #end
    #sql("queryUserIdByDeptId")
      select DISTINCT user_id from 72crm_admin_user where dept_id in (#fori(deptIds))
    #end
    #sql("queryUserByDeptId")
      select user_id as id,realname from 72crm_admin_user where dept_id =?
    #end
    #sql ("queryMiNi")
      select a.realname,a.username,a.user_id,a.sex,a.mobile,a.email,e.name as deptName,a.status,a.create_time,a.dept_id,
       a.post,a.parent_id,a.img,
        IFNULL((select  his_table from 72crm_admin_user_his_table where user_id = a.user_id  and type = 2),0) as card ,
       (select b.realname from 72crm_admin_user b where b.user_id = a.parent_id) as parentName,
       a.post,a.parent_id,a.img, ifnull(f.his_table,0) as his_table,
       (select b.realname from 72crm_admin_user as b where b.user_id = a.parent_id) as parentName,
       (select group_concat(d.role_id) from 72crm_admin_user_role as c left join 72crm_admin_role as d on c.role_id = d.role_id where c.user_id = a.user_id) as roleId,
       (select group_concat(d.role_name) from 72crm_admin_user_role as c left join 72crm_admin_role as d on c.role_id = d.role_id where c.user_id = a.user_id) as roleName
        from 72crm_admin_user a
        left join 72crm_admin_dept e on a.dept_id = e.dept_id
         left join 72crm_admin_user_his_table f on f.user_id = a.user_id and f.type = 1
          left join 72crm_admin_user_his_table g on g.user_id = a.user_id and g.type = 3
        where  1=1
        and g.his_table = 1
        #if(name)
          and (a.realname like concat('%', #para(name), '%') or a.mobile like concat('%', #para(name), '%'))
        #end
    #end
     #sql ("updateHisTable")
      update 72crm_admin_user_his_table set his_table = 0 where
      ( type = 3 or type = 2) and
      user_id in (
         #for(i:userIds)
          #(for.index == 0 ? "" : ",")
              #para(i)
        #end
      )
    #end
    #sql("queryAllUserByDeptId")
        select user_id as id,realname  from 72crm_admin_user where dept_id in (
            #for(i : ids)
                #(for.index > 0 ? "," : "")#para(i)
            #end
        )
    #end

    #sql ("updateScene")
    delete from 72crm_admin_scene where user_id in (
        #for(i:ids)
          #(for.index == 0 ? "" : ",")
              #para(i)
        #end
    )
    #end

    #sql ("querySubUserList")
    select user_id,realname,img from 72crm_admin_user where user_id in (#fori(ids)) and status = 1
    #end
    #sql("queryUserInfo")
      SELECT
        a.realname,a.img,c.`name` as dept_name,a.post,a.mobile,a.email,a.sex
      FROM
        72crm_admin_user as a
      LEFT JOIN 72crm_admin_dept as c on a.dept_id=c.dept_id
      WHERE a.user_id = #para(userId)
    #end
#end
