#namespace("card.weixinLeads")
  #sql ("queryPage")
    select a.* ,b.realname as owner_user_name
    from 72crm_card_weixin_leads as a
    LEFT JOIN 72crm_admin_user as b on b.user_id = a.owner_user_id
    where 1 = 1
    and weixin_img is not null
    and weixin_name is not null
     #if(search)
        and (weixin_name like concat('%',#para(search),'%') or a.mobile like concat('%',#para(search),'%'))
    #end
     #if(userIds)
        and a.owner_user_id in (
          #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
        #end
        )
    #end
     order by a.create_time desc
  #end
   #sql ("deleteByIds")
   delete from 72crm_card_weixin_leads
   where weixin_leads_id in (
     #for(i : ids)
            #(for.index > 0 ? "," : "")#para(i)
        #end
   )
  #end
   #sql ("changeLeads")
  update 72crm_card_weixin_leads set is_transform = 1
   where weixin_leads_id in (
     #for(i : ids)
            #(for.index > 0 ? "," : "")#para(i)
        #end
   )
  #end
   #sql ("queryListByIds")
    select a.weixin_name,a.mobile,b.realname as owner_user_name,a.create_time
    from 72crm_card_weixin_leads as a
    LEFT JOIN 72crm_admin_user as b on b.user_id = a.owner_user_id
    where 1 = 1
     #if(search)
        and (weixin_name like concat('%',#para(search),'%') or mobile like concat('%',#para(search),'%'))
    #end
     #if(ids)
        and a.weixin_leads_id in (
          #for(i : ids)
            #(for.index > 0 ? "," : "")#para(i)
        #end
        )
    #end
     #if(userIds)
        and a.owner_user_id in (
          #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
        #end
        )
    #end
  #end
   #sql ("queryByLeadsIds")
  select  * from 72crm_card_weixin_leads
   where is_transform = 0
   and weixin_leads_id in (
     #for(i : ids)
            #(for.index > 0 ? "," : "")#para(i)
        #end
   )
  #end
#end
