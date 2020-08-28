#namespace("card.visiting")
  #sql ("queryByUserId")
      select b.weixin_img,b.intro,b.address,
      b.card_name ,a.mobile ,a.post,a.realname,
      a.email,b.wechat_number,
      b.url,REPLACE((select path from 72crm_admin_file where file_path = a.img),?,?) as img
      from 72crm_admin_user  as a
      left join 72crm_admin_visiting_card as b on b.user_id = a.user_id
      where  a.user_id = ?
      GROUP BY a.user_id
  #end
   #sql ("deleteByIds")
      delete from 72crm_admin_visiting_card
      where user_id in (
         #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
        #end
      )
  #end
#end