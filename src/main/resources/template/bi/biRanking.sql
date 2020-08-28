#namespace("bi.ranking")
  #sql ("contractRanKing")
      SELECT IFNULL(SUM(cct.money), 0) as money,cau.realname,cau.user_id,cau.img,cct.owner_user_id,cad.name as structureName
      FROM
        72crm_crm_contract as cct
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.owner_user_id
       left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
      WHERE
        check_status = 1
        and  cct.owner_user_id in (
         #fori(userIds)
         )
         and DATE_FORMAT(order_date,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        GROUP BY cct.owner_user_id
        ORDER BY money DESC
  #end
  #sql ("receivablesRanKing")
      SELECT IFNULL(SUM(cct.money), 0) as money,cau.realname,cau.user_id,cau.img,cad.name as structureName
      FROM
        72crm_crm_receivables as cct
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.owner_user_id
      left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
      WHERE
        check_status = 1
        and  cct.owner_user_id in (
         #fori(userIds)
         )
        and DATE_FORMAT(return_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        GROUP BY cct.owner_user_id
        ORDER BY money DESC
  #end
  #sql ("contractCountRanKing")
      SELECT count(1) as `count`,cau.realname,cau.user_id,cau.img,cct.owner_user_id,cad.name as structureName
      FROM
        72crm_crm_contract as cct
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.owner_user_id
       left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
      WHERE
        check_status != 7
        and  cct.owner_user_id in (
         #fori(userIds)
         )
         and DATE_FORMAT(order_date,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        GROUP BY cct.owner_user_id
        ORDER BY count DESC
  #end
   #sql ("productCountRanKing")
     SELECT count(1) as `count`,cau.realname,cct.owner_user_id,cad.name as structureName
      FROM
        72crm_crm_contract_product as ccp
      LEFT JOIN 72crm_crm_contract as cct on ccp.contract_id = cct.contract_id
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.owner_user_id
       left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
      WHERE
        check_status = 1
        and  cct.owner_user_id in (
         #fori(userIds)
         )
         and DATE_FORMAT(order_date,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        GROUP BY cct.owner_user_id
        ORDER BY count DESC
  #end
  #sql ("customerCountRanKing")
      SELECT count(1) as `count`,cau.realname,cau.user_id,cau.img,cct.owner_user_id,cad.name as structureName
      FROM
        72crm_crm_customer as cct
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.owner_user_id
       left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
      WHERE cct.status != 3 and
         cct.owner_user_id in (
         #fori(userIds)
         )
         and DATE_FORMAT(cct.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        GROUP BY cct.owner_user_id
        ORDER BY count DESC
  #end
  #sql ("contactsCountRanKing")
      SELECT count(1) as `count`,cau.realname,cau.user_id,cau.img,cct.create_user_id,cad.name as structureName
      FROM
        72crm_crm_contacts as cct
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.create_user_id
       left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
      WHERE
         cct.create_user_id in (
         #fori(userIds)
         )
         and DATE_FORMAT(cct.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        GROUP BY cct.create_user_id
        ORDER BY count DESC
  #end
  #sql("customerGenjinCountRanKing")
      select
        cau.realname,
        cau.user_id,
        cad.name AS structureName,
        count(v.customer_id) as count
    from (SELECT a.activity_type_id as customer_id,a.create_user_id
                   FROM 72crm_crm_activity a
                   WHERE a.activity_type = 2
                     AND a.type = 1
                     AND DATE_FORMAT(a.create_time, '#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
                   union
                   SELECT customer_id,b.create_user_id
                   FROM 72crm_crm_leads a
                            LEFT JOIN 72crm_crm_activity b ON a.leads_id = b.activity_type_id
                       AND activity_type = 1
                       AND type = 1
                   WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
                   union
                   SELECT customer_id,b.create_user_id
                   FROM 72crm_crm_contacts a
                            LEFT JOIN 72crm_crm_activity b ON a.contacts_id = b.activity_type_id
                       AND activity_type = 3
                       AND type = 1
                   WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
                   union
                   SELECT customer_id,b.create_user_id
                   FROM 72crm_crm_business a
                            LEFT JOIN 72crm_crm_activity b ON a.business_id = b.activity_type_id
                       AND activity_type = 5
                       AND type = 1
                   WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
                     ) as v
    left JOIN 72crm_admin_user cau on cau.user_id = v.create_user_id
    inner JOIN 72crm_admin_dept cad ON cad.dept_id = cau.dept_id
    WHERE cau.user_id in (#fori(userIds))
    GROUP BY cau.user_id
    order by count desc
  #end
  #sql ("recordCountRanKing")
      SELECT count(1)  as `count`,cau.realname,cau.user_id,cau.img,ccr.create_user_id,cad.name as structureName
      FROM
        72crm_crm_activity as ccr
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = ccr.create_user_id
       left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
      WHERE
        ccr.type = 1  and
         ccr.create_user_id in (#fori(userIds))
        and DATE_FORMAT(ccr.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        GROUP BY ccr.create_user_id
        ORDER BY count DESC
  #end
   #sql ("contractProductRanKing")
     select IFNULL(SUM(c.num), 0) AS num, a.`name` AS categoryName, b.`name` AS productName, a.category_id, b.product_id from 72crm_crm_product_category as a
 LEFT JOIN 72crm_crm_product as b ON a.category_id = b.category_id
 LEFT JOIN 72crm_crm_contract_product as c ON b.product_id = c.product_id
 LEFT JOIN 72crm_crm_contract as d ON c.contract_id = d.contract_id
      WHERE
        d.check_status = 1
        and  d.owner_user_id in (
         #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
         #end
         )
        #if(type == 1)
          and to_days(NOW()) = TO_DAYS(d.order_date)
          #end
           #if(type == 2)
          and to_days(NOW()) - TO_DAYS(d.order_date) = 1
          #end
           #if(type == 3)
          and YEARWEEK(date_format(d.order_date,'%Y-%m-%d')) = YEARWEEK(now())
          #end
           #if(type == 4)
          and YEARWEEK(date_format(d.order_date,'%Y-%m-%d')) = YEARWEEK(now()) -1
          #end
           #if(type == 5)
          and date_format(d.order_date,'%Y-%m')=date_format(now(),'%Y-%m')
          #end
           #if(type == 6)
          and date_format(d.order_date,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
          #end
           #if(type == 7)
          and QUARTER(d.order_date)=QUARTER(now()) AND YEAR(cct.order_date)=YEAR(NOW())
          #end
           #if(type == 8)
          and QUARTER(d.order_date)=QUARTER(DATE_SUB(now(),interval 1 QUARTER)) and YEAR(DATE_SUB(d.order_date,interval 1 QUARTER)) = YEAR(DATE_SUB(NOW(),interval 1 QUARTER))
          #end
           #if(type == 9)
          and YEAR(d.order_date)=YEAR(NOW())
          #end
           #if(type == 10)
          and YEAR(d.order_date)=YEAR(date_sub(now(),interval 1 year))
          #end
           #if(type == 11)
            and  TO_DAYS(d.order_date) >= TO_DAYS(#para(startTime))
            and  TO_DAYS(d.order_date) <= TO_DAYS(#para(endTime))
          #end
        GROUP BY a.category_id
  #end
  #sql ("travelCountRanKing")
      SELECT count(1) as `count`,cau.realname,coe.create_user_id,cad.name as structureName
      FROM
        72crm_oa_examine_travel as coet
      LEFT JOIN 72crm_oa_examine as coe on coe.examine_id = coet.examine_id
      left join `72crm_oa_examine_category` a on coe.category_id = a.category_id
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = coe.create_user_id
       left join 72crm_admin_dept as cad on cad.dept_id = cau.dept_id
       left join 72crm_oa_examine_record as c on coe.examine_id = c.examine_id
      WHERE a.type = 3 and
         coe.create_user_id in (
         #fori(userIds)
         ) and c.examine_status = 1
         and DATE_FORMAT(coet.start_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        GROUP BY coe.create_user_id
        ORDER BY count DESC
  #end
  #sql("productSellRanKing")
    SELECT cct.contract_id,ccs.customer_id,ccp.product_id,ccpc.category_id,cct.`name` as contractName,
      ccpc.`name` as categoryName,ccs.customer_name as customerName,cccp.num as num,cccp.subtotal ,cccp.sales_price as salesPrice,
      cct.num as contractNum,cct.owner_user_id  ,cau.realname,cccp.unit,cccp.discount,cccp.price
      FROM 72crm_crm_contract as cct
      LEFT JOIN 72crm_crm_contract_product as cccp ON cccp.contract_id = cct.contract_id
      LEFT JOIN 72crm_crm_customer as ccs on ccs.customer_id = cct.customer_id
      LEFT JOIN 72crm_crm_product as ccp on ccp.product_id = cccp.product_id
      LEFT JOIN 72crm_crm_product_category as ccpc ON ccp.category_id = ccpc.category_id
      LEFT JOIN 72crm_admin_user as cau on cau.user_id = cct.owner_user_id
      where
          cct.check_status = 1
        and  cct.owner_user_id in (
         #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
         #end
         )
        #if(type == 1)
          and to_days(NOW()) = TO_DAYS(cct.order_date)
          #end
           #if(type == 2)
          and to_days(NOW()) - TO_DAYS(cct.order_date) = 1
          #end
           #if(type == 3)
          and YEARWEEK(date_format(cct.order_date,'%Y-%m-%d')) = YEARWEEK(now())
          #end
           #if(type == 4)
          and YEARWEEK(date_format(cct.order_date,'%Y-%m-%d')) = YEARWEEK(now()) -1
          #end
           #if(type == 5)
          and date_format(cct.order_date,'%Y-%m')=date_format(now(),'%Y-%m')
          #end
           #if(type == 6)
          and date_format(cct.order_date,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
          #end
           #if(type == 7)
          and QUARTER(cct.order_date)=QUARTER(now()) AND YEAR(cct.order_date)=YEAR(NOW())
          #end
           #if(type == 8)
          and QUARTER(cct.order_date)=QUARTER(DATE_SUB(now(),interval 1 QUARTER)) and YEAR(DATE_SUB(cct.order_date,interval 1 QUARTER)) = YEAR(DATE_SUB(NOW(),interval 1 QUARTER))
          #end
           #if(type == 9)
          and YEAR(cct.order_date)=YEAR(NOW())
          #end
           #if(type == 10)
          and YEAR(cct.order_date)=YEAR(date_sub(now(),interval 1 year))
          #end
           #if(type == 11)
            and  TO_DAYS(cct.order_date) >= TO_DAYS(#para(startTime))
            and  TO_DAYS(cct.order_date) <= TO_DAYS(#para(endTime))
          #end
      #end

      #sql ("businessRanKing")
      select b.realname,b.user_id,b.img,count(business_id) as count
      from 72crm_crm_business as a left join 72crm_admin_user as b on a.create_user_id = b.user_id
      where create_user_id in (#fori(userIds)) and DATE_FORMAT(a.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
      group by a.create_user_id order by count desc
      #end

      #sql ("businessMoneyRanKing")
      select b.realname,b.user_id,b.img,sum(a.money) as money
      from 72crm_crm_business as a left join 72crm_admin_user as b on a.create_user_id = b.user_id
      where create_user_id in (#fori(userIds)) and DATE_FORMAT(a.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
      group by a.create_user_id order by money desc
      #end

      #sql("addressAnalyse")
        #for(x : address)
        #(for.index == 0 ? "" : "union all")
        SELECT '#(x)' as address,
               COUNT(1) as allCustomer,
               sum(deal_status) as dealCustomer
        FROM 72crm_crm_customer as ccc
        where left(ccc.address,INSTR(ccc.address, ',') - 1) like concat('%','#(x)','%') and ccc.owner_user_id in (
         #fori(userIds))
        and DATE_FORMAT(ccc.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
        #end
      #end
       #sql("portrait")
        SELECT count(*) as allCustomer,
        sum(a.deal_status) as dealCustomer,
        (case when (IFNULL(b.`value`,'') = '') then '未知' else b.`value` end) as industry
        FROM 72crm_crm_customer as a
        LEFT JOIN 72crm_crm_customer_data as b on a.batch_id=b.batch_id and b.`name`= (select name from 72crm_admin_field where label = 2 and field_name = '客户行业')
         where   a.owner_user_id in (
         #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
         #end
         )
          #if(type == 1)
          and to_days(NOW()) = TO_DAYS(a.create_time)
          #end
           #if(type == 2)
          and to_days(NOW()) - TO_DAYS(a.create_time) = 1
          #end
           #if(type == 3)
          and YEARWEEK(date_format(a.create_time,'%Y-%m-%d')) = YEARWEEK(now())
          #end
           #if(type == 4)
          and YEARWEEK(date_format(a.create_time,'%Y-%m-%d')) = YEARWEEK(now()) -1
          #end
           #if(type == 5)
          and date_format(a.create_time,'%Y-%m')=date_format(now(),'%Y-%m')
          #end
           #if(type == 6)
          and date_format(a.create_time,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
          #end
           #if(type == 7)
          and QUARTER(a.create_time)=QUARTER(now()) AND YEAR(a.create_time)=YEAR(NOW())
          #end
           #if(type == 8)
          and QUARTER(a.create_time)=QUARTER(DATE_SUB(now(),interval 1 QUARTER)) and YEAR(DATE_SUB(a.create_time,interval 1 QUARTER)) = YEAR(DATE_SUB(NOW(),interval 1 QUARTER))
          #end
           #if(type == 9)
          and YEAR(a.create_time)=YEAR(NOW())
          #end
           #if(type == 10)
          and YEAR(a.create_time)=YEAR(date_sub(now(),interval 1 year))
          #end
           #if(type == 11)
            and  TO_DAYS(a.create_time) >= TO_DAYS(#para(startTime))
            and  TO_DAYS(a.create_time) <= TO_DAYS(#para(endTime))
          #end
        GROUP BY IFNULL(b.value,'')
       #end
       #sql("portraitLevel")
         SELECT count(*) as allCustomer,
        sum(a.deal_status) as dealCustomer,
        (case when (IFNULL(b.`value`,'') = '') then '未知' else b.`value` end) as level
        FROM 72crm_crm_customer as a
        LEFT JOIN 72crm_crm_customer_data as b on a.batch_id=b.batch_id and b.`name`= (select name from 72crm_admin_field where label = 2 and field_name = '客户级别')
        where   a.owner_user_id in (
         #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
         #end
         )
          #if(type == 1)
          and to_days(NOW()) = TO_DAYS(a.create_time)
          #end
           #if(type == 2)
          and to_days(NOW()) - TO_DAYS(a.create_time) = 1
          #end
           #if(type == 3)
          and YEARWEEK(date_format(a.create_time,'%Y-%m-%d')) = YEARWEEK(now())
          #end
           #if(type == 4)
          and YEARWEEK(date_format(a.create_time,'%Y-%m-%d')) = YEARWEEK(now()) -1
          #end
           #if(type == 5)
          and date_format(a.create_time,'%Y-%m')=date_format(now(),'%Y-%m')
          #end
           #if(type == 6)
          and date_format(a.create_time,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
          #end
           #if(type == 7)
          and QUARTER(a.create_time)=QUARTER(now()) AND YEAR(a.create_time)=YEAR(NOW())
          #end
           #if(type == 8)
          and QUARTER(a.create_time)=QUARTER(DATE_SUB(now(),interval 1 QUARTER)) and YEAR(DATE_SUB(a.create_time,interval 1 QUARTER)) = YEAR(DATE_SUB(NOW(),interval 1 QUARTER))
          #end
           #if(type == 9)
          and YEAR(a.create_time)=YEAR(NOW())
          #end
           #if(type == 10)
          and YEAR(a.create_time)=YEAR(date_sub(now(),interval 1 year))
          #end
           #if(type == 11)
            and  TO_DAYS(a.create_time) >= TO_DAYS(#para(startTime))
            and  TO_DAYS(a.create_time) <= TO_DAYS(#para(endTime))
          #end
        GROUP BY IFNULL(b.value,'')
       #end
       #sql("portraitSource")
        SELECT count(*) as allCustomer,
        sum(a.deal_status) as dealCustomer,
        (case when (IFNULL(b.`value`,'') = '') then '未知' else b.`value` end) as source
        FROM 72crm_crm_customer as a
        LEFT JOIN 72crm_crm_customer_data as b on a.batch_id=b.batch_id and b.`name`=(select name from 72crm_admin_field where label = 2 and field_name = '客户来源')
          where   a.owner_user_id in (
         #for(i : userIds)
            #(for.index > 0 ? "," : "")#para(i)
         #end
         )
          #if(type == 1)
          and to_days(NOW()) = TO_DAYS(a.create_time)
          #end
           #if(type == 2)
          and to_days(NOW()) - TO_DAYS(a.create_time) = 1
          #end
           #if(type == 3)
          and YEARWEEK(date_format(a.create_time,'%Y-%m-%d')) = YEARWEEK(now())
          #end
           #if(type == 4)
          and YEARWEEK(date_format(a.create_time,'%Y-%m-%d')) = YEARWEEK(now()) -1
          #end
           #if(type == 5)
          and date_format(a.create_time,'%Y-%m')=date_format(now(),'%Y-%m')
          #end
           #if(type == 6)
          and date_format(a.create_time,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m')
          #end
           #if(type == 7)
          and QUARTER(a.create_time)=QUARTER(now()) AND YEAR(a.create_time)=YEAR(NOW())
          #end
           #if(type == 8)
          and QUARTER(a.create_time)=QUARTER(DATE_SUB(now(),interval 1 QUARTER)) and YEAR(DATE_SUB(a.create_time,interval 1 QUARTER)) = YEAR(DATE_SUB(NOW(),interval 1 QUARTER))
          #end
           #if(type == 9)
          and YEAR(a.create_time)=YEAR(NOW())
          #end
           #if(type == 10)
          and YEAR(a.create_time)=YEAR(date_sub(now(),interval 1 year))
          #end
           #if(type == 11)
            and  TO_DAYS(a.create_time) >= TO_DAYS(#para(startTime))
            and  TO_DAYS(a.create_time) <= TO_DAYS(#para(endTime))
          #end
        GROUP BY IFNULL(b.value,'')
       #end
#end
