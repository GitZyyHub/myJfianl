#namespace("crm.Instrument")
   #sql("queryBulletinInfo")
      select * from #(viewName) as a where 1=1 and (a.create_time between #para(beginDate) and  #para(endDate))
      and create_user_id in ( #for(x:userIds) #(for.index == 0 ? "" : ",") #para(x) #end)
      #if(tn)
        and (SELECT COUNT(*) FROM 72crm_crm_business_change as b WHERE b.business_id = a.business_id and (b.create_time between #para(beginDate) and  #para(endDate)))>0
      #end
      order by #(sortField) #(orderType)
   #end
   #sql("queryBulletin")
       select
      (SELECT COUNT(*) FROM 72crm_crm_business as c WHERE c.status = 1 and DATE_FORMAT(c.create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end AND owner_user_id in (#fori(userIds))) as businessCount,
      COUNT(*) as contractCount,
      IFNULL(SUM(CASE WHEN a.check_status = 1 THEN a.money END),0) as contractMoney,
      (SELECT COUNT(*) FROM  72crm_crm_contacts as c WHERE DATE_FORMAT(c.create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end AND owner_user_id in (#fori(userIds))) as contactsCount,
      (SELECT COUNT(*) FROM 72crm_crm_customer as c WHERE status != 3 and DATE_FORMAT(c.create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end AND owner_user_id in (#fori(userIds))) as customerCount,
      (SELECT IFNULL(SUM(money),0) FROM 72crm_crm_receivables as c WHERE check_status='1' and DATE_FORMAT(c.return_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end AND owner_user_id in (#fori(userIds))) as receivablesMoney,
      (SELECT COUNT(*) FROM 72crm_crm_activity as c WHERE type in (1,4) and DATE_FORMAT(c.create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end AND create_user_id in (#fori(userIds))) as recordCount,
      (SELECT IFNULL(SUM(c.money),0) FROM 72crm_crm_business as c WHERE c.status = 1 and DATE_FORMAT(c.create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end AND owner_user_id in (#fori(userIds))) as businessMoney
      from 72crm_crm_contract as a
      WHERE a.check_status != 7 and DATE_FORMAT(a.order_date,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end AND owner_user_id in (#fori(userIds)) and check_status != 7
   #end

   #sql("dataInfoCostumer")
    SELECT COUNT(a.customer_id) allCustomer,COUNT(CASE WHEN a.deal_status = '1' THEN 1 ELSE NULL END) AS dealCustomer FROM 72crm_crm_customer AS a
    where a.status != 3
    and DATE_FORMAT(a.create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end  AND owner_user_id in (#fori(userIds))
   #end
   #sql("dataInfoBusiness")
    SELECT COUNT(*) as allBusiness,IFNULL(SUM(money),0) as businessMoney,COUNT(CASE WHEN is_end='1' THEN 1 ELSE null END) as endBusiness FROM 72crm_crm_business as a
    where a.status = 1
    and DATE_FORMAT(a.create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end  AND owner_user_id in (#fori(userIds))
   #end
   #sql("dataInfoContract")
      SELECT COUNT(*) as allContract,IFNULL(SUM(CASE WHEN a.check_status = 1 THEN a.money END),0) as contractMoney,IFNULL(SUM(a.planMoney),0) as planMoney FROM (
        select a.money,a.check_status,
        (SELECT IFNULL(SUM(b.money),0) FROM 72crm_crm_receivables_plan AS b WHERE b.contract_id = a.contract_id and a.check_status='1') AS planMoney
        FROM 72crm_crm_contract as a
         WHERE a.check_status != 7 and DATE_FORMAT(a.order_date,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end  AND a.owner_user_id in (#fori(userIds))
		  ) as a
   #end
   #sql("dataInfoReceivables")
    SELECT IFNULL(SUM(CASE WHEN check_status='1' THEN money ELSE 0 END),0) as receivablesMoney
    FROM 72crm_crm_receivables as a
    where DATE_FORMAT(a.return_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end  AND owner_user_id in (#fori(userIds))
   #end
   #sql("dataInfoActivity")
    SELECT COUNT(*) as activityNum,COUNT(CASE WHEN b.activity_type_id in
    (SELECT customer_id FROM 72crm_crm_customer where DATE_FORMAT(create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end  AND owner_user_id in (#fori(userIds)))
    THEN 1 ELSE NULL END ) as activityRealNum
    FROM (
      SELECT
        b.activity_type_id
      FROM
        72crm_crm_activity AS b
      WHERE
        b.type = '1'
        AND b.activity_type = '2'
        AND DATE_FORMAT(b.create_time,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime)  #end
        AND create_user_id in (#fori(userIds))
        GROUP BY b.activity_type_id
    ) as b
   #end
   #sql ("queryRecordCount")
        SELECT activity_type as crmType,COUNT(activity_type) as count FROM 72crm_crm_activity
        WHERE
        type in (1,4) and create_user_id in ( #fori(userIds)) and status = 1
        #if(type == 1)
          AND TO_DAYS(create_time) = TO_DAYS(now())
        #end
        #if(type == 2)
        AND to_days(NOW()) - TO_DAYS(create_time) = 1
        #end
        #if(type == 3)
        AND   YEARWEEK(date_format(create_time,'%Y-%m-%d')) = YEARWEEK(now())
        #end
        #if(type == 4)
        AND  YEARWEEK(date_format(create_time,'%Y-%m-%d')) = YEARWEEK(now()) -1
        #end
        #if(type == 5)
        AND  date_format(create_time,'%Y-%m')=date_format(now(),'%Y-%m')
        #end
        #if(type == 6)
        AND  date_format(create_time,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
        #end
        #if(type == 7)
        AND  QUARTER(create_time)=QUARTER(now())
        #end
        #if(type == 8)
        AND  QUARTER(create_time)=QUARTER(DATE_SUB(now(),interval 1 QUARTER))
        #end
        #if(type == 9)
        AND   YEAR(create_time)=YEAR(NOW())
        #end
        #if(type == 10)
        AND  YEAR(create_time)=YEAR(date_sub(now(),interval 1 year))
        #end
        #if(type == 11)
        AND  TO_DAYS(create_time) >= TO_DAYS(#para(startTime)) and  TO_DAYS(create_time) <= TO_DAYS(#para(endTime))
        #end
        group by activity_type
    #end
    #sql ("queryRecordList")
      select a.activity_id,a.type,b.img as userImg,b.realname,a.create_time,a.content,a.category,a.next_time,a.batch_id,a.business_ids,a.contacts_ids,a.activity_type,a.activity_type_id,a.status,
      a.lng,a.lat,a.address,
      (case a.activity_type
         when 2 then (select customer_name from `72crm_crm_customer` where customer_id = a.activity_type_id)
         when 1 then (select leads_name from `72crm_crm_leads` where leads_id = a.activity_type_id)
         when 5 then (select business_name from `72crm_crm_business` where business_id = a.activity_type_id)
         when 6 then (select name from `72crm_crm_contract` where contract_id = a.activity_type_id)
         when 3 then (select name from `72crm_crm_contacts` where contacts_id = a.activity_type_id)
      end) as 'crmTypeName'
      from 72crm_crm_activity as a inner join 72crm_admin_user as b on a.create_user_id = b.user_id
      where a.create_user_id in (#fori(userIds)) and a.status = 1
      #if(queryType == 1)
        and a.type = 1
      #elseif(queryType == 4)
        and a.type = 4
      #elseif(queryType == 0)
        and a.type in (1,4)
      #end
      #if(crmType)
        and a.activity_type = #para(crmType)
      #end
      #if(type == 1)
      AND TO_DAYS(a.create_time) = TO_DAYS(now())
      #end
      #if(type == 2)
      AND to_days(NOW()) - TO_DAYS(a.create_time) = 1
      #end
      #if(type == 3)
      AND   YEARWEEK(date_format(a.create_time,'%Y-%m-%d')) = YEARWEEK(now())
      #end
      #if(type == 4)
      AND  YEARWEEK(date_format(a.create_time,'%Y-%m-%d')) = YEARWEEK(now()) -1
      #end
      #if(type == 5)
      AND  date_format(a.create_time,'%Y-%m')=date_format(now(),'%Y-%m')
      #end
      #if(type == 6)
      AND  date_format(a.create_time,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
      #end
      #if(type == 7)
      AND  QUARTER(a.create_time)=QUARTER(now())
      #end
      #if(type == 8)
      AND  QUARTER(a.create_time)=QUARTER(DATE_SUB(now(),interval 1 QUARTER))
      #end
      #if(type == 9)
      AND   YEAR(a.create_time)=YEAR(NOW())
      #end
      #if(type == 10)
      AND  YEAR(a.create_time)=YEAR(date_sub(now(),interval 1 year))
      #end
      #if(type == 11)
      AND  TO_DAYS(a.create_time) >= TO_DAYS(#para(startTime)) and  TO_DAYS(a.create_time) <= TO_DAYS(#para(endTime))
      #end
      order by a.create_time desc
    #end
    #sql("queryPerformance")
      select
      #if(queryItem)
       (SELECT (#for(month1:monthList) #(for.index > 0 ? "+" : "")IFNULL(SUM(#(month1)),0) #end) FROM 72crm_crm_achievement WHERE year = #(year) and status=#para(label) and #if(dept)(type=2 and obj_id in (#para(dept))) #else(type=3 and obj_id in (#fori(userIds)))#end) as achievementMoneys,
      #end
      IFNULL(SUM(money),0) as money
      FROM #if(label==1) 72crm_crm_contract #elseif(label==2) 72crm_crm_receivables #end
      where check_status='1' and DATE_FORMAT(#if(label==1) create_time #elseif(label==2) return_time #end,#para(sqlDateFormat)) #if(cycleNum==1) = #para(beginTime) #else BETWEEN #para(beginTime) and #para(finalTime) #end and owner_user_id in (#fori(userIds))
    #end

   #sql("queryBulletinByLog")
       select
      (SELECT COUNT(*) FROM 72crm_crm_business as c WHERE TO_DAYS(create_time)=TO_DAYS(NOW()) AND owner_user_id in (#fori(userIds))) as business_num,
      COUNT(*) as contract_num,
      (SELECT COUNT(*) FROM 72crm_crm_customer as c WHERE TO_DAYS(create_time)=TO_DAYS(NOW()) AND owner_user_id in (#fori(userIds))) as customer_num,
      (SELECT IFNULL(SUM(money),0) FROM 72crm_crm_receivables as c WHERE check_status='1' and TO_DAYS(create_time)=TO_DAYS(NOW()) AND owner_user_id in (#fori(userIds))) as receivables_money,
      (SELECT COUNT(*) FROM 72crm_crm_activity as c WHERE type = 1 and TO_DAYS(create_time)=TO_DAYS(NOW()) AND create_user_id in (#fori(userIds))) as activity_num
      from 72crm_crm_contract as a
      WHERE TO_DAYS(create_time)=TO_DAYS(NOW()) AND owner_user_id in (#fori(userIds)) and check_status='1'
   #end
   #sql("queryBulletinByLogInfo")
      SELECT business_id as type_id,2 as type FROM 72crm_crm_business WHERE TO_DAYS(create_time) = TO_DAYS(NOW()) AND owner_user_id IN (#fori(userIds))
      union all
      SELECT contract_id as type_id,3 as type FROM 72crm_crm_contract WHERE TO_DAYS(create_time) = TO_DAYS(NOW()) AND owner_user_id IN (#fori(userIds)) and check_status != 7
      union all
      SELECT customer_id as type_id,1 as type FROM 72crm_crm_customer WHERE TO_DAYS(create_time) = TO_DAYS(NOW()) AND owner_user_id IN (#fori(userIds))
      union all
      SELECT receivables_id as type_id,4 as type FROM 72crm_crm_receivables WHERE check_status = '1' AND TO_DAYS(create_time) = TO_DAYS(NOW()) AND owner_user_id IN (#fori(userIds))
      union all
      SELECT activity_id as type_id,5 as type FROM 72crm_crm_activity WHERE type in (1,4) AND TO_DAYS(create_time) = TO_DAYS(NOW()) AND create_user_id IN (#fori(userIds))
   #end
   #sql ("forgottenCustomerCount")
        select count(customer_id)
        from `72crm_crm_customer`  where  deal_status = 0  and status != 3 and (to_days(now()) - ifnull(to_days(last_time),to_days(create_time)))> #para(day)
        AND owner_user_id in (#fori(userIds))
   #end
   #sql ("forgottenCustomer")
        select customer_id
        from `72crm_crm_customer`  where  deal_status = 0  and status != 3 and (to_days(now()) - ifnull(to_days(last_time),to_days(create_time)))> #para(day)
        AND owner_user_id in (#fori(userIds))
   #end
   #sql ("unContactCustomerCount")
   select count(*) from 72crm_crm_customer a
   where  status != 3 and to_days(last_time) < to_days(next_time) and to_days(next_time) < to_days(now()) AND owner_user_id in (#fori(userIds))
   #end
   #sql ("unContactCustomer")
   select customer_id from 72crm_crm_customer
   where to_days(last_time) < to_days(next_time) and to_days(next_time) < to_days(now())  AND owner_user_id in (#fori(userIds))
       #if(search)
           and (customer_name like concat('%',#para(search),'%') or
           telephone like concat('%',#para(search),'%') or
           mobile like concat('%',#para(search),'%'))
       #end
   #end

  #sql ("contractRanKing")
      SELECT IFNULL(SUM(cct.money), 0) as money,(TRUNCATE ( IFNULL((IFNULL(SUM(cct.money), 0)/SUM(achievement)),0)*100 ,2)) as rate,
      cau.realname,cau.user_id,cau.img,cct.owner_user_id,cad.name as structureName
      FROM
        (select sum(money) as money, owner_user_id from 72crm_crm_contract where check_status = 1
        and  owner_user_id in (
         #fori(userIds)
         )
         and DATE_FORMAT(order_date,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime) group by owner_user_id)as cct
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.owner_user_id
       left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
       left join ( select sum(x.achievement) as achievement,x.user_id from (
       #for(x : monthMap)
       #(for.index == 0 ? "" : "union all")
       select #(x.month) as achievement,obj_id as user_id from 72crm_crm_achievement where obj_id in (
         #fori(userIds)
         ) and year = #(x.year) and type = 3 and status = 1
       #end
       ) as x group by user_id) as y ON y.user_id = cct.owner_user_id
        GROUP BY cct.owner_user_id
        ORDER BY money DESC
  #end

  #sql ("receivablesRanKing")
      SELECT IFNULL(SUM(cct.money), 0) as money,(TRUNCATE ( IFNULL((IFNULL(SUM(cct.money), 0)/SUM(achievement)),0)*100 ,2)) as rate,
      cau.realname,cau.user_id,cau.img,cad.name as structureName
      FROM
      (select sum(money) as money, owner_user_id from 72crm_crm_receivables where check_status = 1
        and  owner_user_id in (
         #fori(userIds)
         )
         and DATE_FORMAT(return_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime) group by owner_user_id) as cct
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.owner_user_id
      left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
       left join ( select sum(x.achievement) as achievement,x.user_id from (
       #for(x : monthMap)
       #(for.index == 0 ? "" : "union all")
       select #(x.month) as achievement,obj_id as user_id from 72crm_crm_achievement where obj_id in (
         #fori(userIds)
         ) and year = #(x.year) and type = 3 and status = 1
       #end
       ) as x group by user_id) as y ON y.user_id = cct.owner_user_id
        GROUP BY cct.owner_user_id
        ORDER BY money DESC
  #end
#end
