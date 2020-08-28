#namespace("card.weixinBrowse")
  #sql ("queryByWeixinLeadsId")
    select * from 72crm_card_weixin_browse where weixin_leads_id = ? and user_id = ?
  #end
  #sql ("updateNumByWeixinLeadsId")
    update 72crm_card_weixin_browse set num = num + 1 where  weixin_leads_id = ?
  #end
  #sql ("queryList")
    select a.* ,b.weixin_img,b.weixin_name
    from 72crm_card_weixin_browse as a
    LEFT JOIN 72crm_card_weixin_leads as b on b.weixin_leads_id = a.weixin_leads_id
    where 1 = 1
     and b.weixin_img is not null
    and b.weixin_name is not null
     #if(userId)
      and a.user_id = #para(userId)
     #end
      #if(weixinName)
      and b.weixin_name = #para(weixinName)
     #end
  #end
#end
