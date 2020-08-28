#namespace("card.weixinLeadsUser")
  #sql("queryPage")
    SELECT a.* ,b.post,b.realname,b.mobile,b.img
    from 72crm_card_weixin_leads_user as a
    LEFT JOIN 72crm_admin_user as b on b.user_id = a.user_id
    where 1 = 1
     #if(weixinLeadsId)
      and weixin_leads_id = #para(weixinLeadsId)
     #end
      #if(relevanceUserId)
      and relevance_user_id = #para(relevanceUserId)
     #end
     group by a.user_id
     order by a.user_id desc
  #end
  #sql("deleteById")
    delete from 72crm_card_weixin_leads_user where weixin_user_id = ?
  #end
   #sql("queryByWeixinLeadsId")
    SELECT  a.*,b.realname,b.img
    from 72crm_card_weixin_leads_user as a
    LEFT JOIN 72crm_admin_user as b on b.user_id = a.user_id)
    where weixin_leads_id = #para(weixinLeadsId)
  #end
#end
