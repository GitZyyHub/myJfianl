#namespace("admin.menu")
    #sql("queryMenuList")
      SELECT  c.realm,c.menu_id,c.parent_id,c.menu_type,menu_name FROM 72crm_admin_menu as c
      WHERE 1=1
      #if(parentId)
        AND c.parent_id = #para(parentId)
      #end
      #if(realm)
        AND c.realm = #para(realm)
      #end
      #if(remarks)
        AND c.remarks = #para(remarks)
      #end
      #if(notRealm)
        AND c.realm not in(
        #for(x:notRealm)
          #(for.index == 0 ? "" : ",") #para(x)
        #end
      )
      #end
    #end
    #sql("queryMenuByAuth")
      SELECT a.realm,a.menu_id,a.parent_id FROM 72crm_admin_menu as a
      LEFT JOIN 72crm_admin_role_menu as b on a.menu_id=b.menu_id
      LEFT JOIN 72crm_admin_user_role as c on b.role_id=c.role_id
      WHERE 1=1
      #if(userId)
        AND c.user_id=#para(userId)
      #end
      #if(roleId)
        AND c.role_id=#para(roleId)
      #end
    #end

    #sql ("queryMenuId")
    select a.menu_id from 72crm_admin_menu as a inner join 72crm_admin_menu as b on a.parent_id = b.menu_id
    inner join 72crm_admin_menu as c on b.parent_id = c.menu_id
    where c.realm = ? and b.realm = ? and a.realm = ?
    #end
#end
