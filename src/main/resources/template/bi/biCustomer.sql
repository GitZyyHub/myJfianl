#namespace("bi.customer")

  #sql ("totalCustomerStats")
  #for(x : timeList)
  #(for.index == 0 ? "" : "union all")
    select #(x)  as type,
           count(1) as customerNum,
           count(if(deal_status ='1',1,null)) as dealCustomerNum
    from 72crm_crm_customer as a
    where DATE_FORMAT(a.create_time,'#(sqlDateFormat)') = '#(x)' and a.owner_user_id in (#fori(userIds)) and a.status != 3
  #end
  #end

  #sql ("totalCustomerTable")
  #for(x : userIds)
  #(for.index == 0 ? "" : "union all")
    select (select realname from 72crm_admin_user where user_id = #(x)) as realname,
           count(a.customer_id) as customerNum,
           count(if(deal_status ='1',1,null)) as dealCustomerNum,
           (select IFNULL(SUM(money),0)
            from 72crm_crm_contract
            where DATE_FORMAT(order_date,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
            and owner_user_id = #(x) and check_status = 1
           ) as contractMoney,
           (select IFNULL(SUM(money),0)
            from 72crm_crm_receivables
            where DATE_FORMAT(return_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
            and owner_user_id = #(x) and check_status = 1
           ) as receivablesMoney
    from 72crm_crm_customer as a
    where DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime) and owner_user_id = #(x) and a.status != 3
  #end
  #end

  #sql ("increaseCustomer")
  select customer_id from 72crm_crm_customer
  where DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime) and owner_user_id = #(userId)
  #end

  #sql ("dealCustomer")
  select distinct b.customer_id
  from 72crm_crm_customer as b left join 72crm_crm_contract as c on b.customer_id = c.customer_id
  where c.check_status = 1 and b.owner_user_id = #(userId)
  and DATE_FORMAT(c.order_date,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  #end

  #sql ("totalContractMoney")
  select contract_id from 72crm_crm_contract
  where DATE_FORMAT(order_date,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  and owner_user_id = #(userId) and check_status = 1
  #end

  #sql ("totalReceivablesMoney")
  select receivables_id from 72crm_crm_receivables
  where DATE_FORMAT(return_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  and owner_user_id = #(userId) and check_status = 1
  #end

  #sql ("sumTotalCustomerTable")
  select '总计' as realname,
           count(a.customer_id) as customerNum,
           count(if(a.deal_status ='1',1,null)) as dealCustomerNum,
           (select IFNULL(SUM(money),0)
            from 72crm_crm_contract
            where DATE_FORMAT(order_date,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
            and owner_user_id in (#fori(userIds)) and check_status = 1
           ) as contractMoney,
           (select IFNULL(SUM(money),0)
            from 72crm_crm_receivables
            where DATE_FORMAT(return_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
            and owner_user_id in (#fori(userIds)) and check_status = 1
           ) as receivablesMoney
    from 72crm_crm_customer as a
    where DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime) and owner_user_id in (#fori(userIds)) and a.status != 3
  #end

  #sql ("customerRecordStats")
      #for(x : timeList)
      #(for.index == 0 ? "" : "union all")
        select '#(x)' as type,
               IFNULL((select count(1)
                       from 72crm_crm_activity
                       where DATE_FORMAT(create_time,'#(sqlDateFormat)') = #(x) and type = 1 and create_user_id in (#fori(userIds))
               ),0) as recordCount,
                (SELECT count(v.customer_id) from (SELECT a.activity_type_id as customer_id
                    FROM 72crm_crm_activity a
                    WHERE a.activity_type = 2
                      AND a.type = 1
                      AND DATE_FORMAT(a.create_time, '#(sqlDateFormat)') = #(x)
                      AND a.create_user_id in (#fori(userIds))
                    union
                    SELECT customer_id
                    FROM 72crm_crm_leads a
                             LEFT JOIN 72crm_crm_activity b ON a.leads_id = b.activity_type_id
                        AND activity_type = 1
                        AND type = 1
                    WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') = #(x)
                      AND b.create_user_id in (#fori(userIds))
                    union
                    SELECT customer_id
                    FROM 72crm_crm_contacts a
                             LEFT JOIN 72crm_crm_activity b ON a.contacts_id = b.activity_type_id
                        AND activity_type = 3
                        AND type = 1
                    WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') = #(x)
                      AND b.create_user_id in (#fori(userIds))
                    union
                    SELECT customer_id
                    FROM 72crm_crm_business a
                             LEFT JOIN 72crm_crm_activity b ON a.business_id = b.activity_type_id
                        AND activity_type = 5
                        AND type = 1
                        AND b.create_user_id in (#fori(userIds))
                    WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') = #(x)
                   ) as v) AS customerCount
      #end
  #end

    #sql("customerRecordInfo")
      select
        cau.realname,
        (select count(1) from `72crm_crm_activity` a where a.create_user_id = v.create_user_id  and DATE_FORMAT(a.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime) and a.type = 1) as recordCount,
        count(v.customer_id) as customerCount
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
  #end

  #sql ("followUpCustomer")
  SELECT a.activity_type_id as customer_id FROM 72crm_crm_activity a
  WHERE a.activity_type = 2 AND a.type = 1
  AND DATE_FORMAT(a.create_time, '#(sqlDateFormat)') between #para(beginTime) and #para(finalTime) AND a.create_user_id = #para(userId)
  union
  SELECT customer_id
  FROM 72crm_crm_leads a LEFT JOIN 72crm_crm_activity b ON a.leads_id = b.activity_type_id AND activity_type = 1 AND type = 1
  WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  AND b.create_user_id = #para(userId)
  union
  SELECT customer_id
  FROM 72crm_crm_contacts a LEFT JOIN 72crm_crm_activity b ON a.contacts_id = b.activity_type_id AND activity_type = 3 AND type = 1
  WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  AND b.create_user_id = #para(userId)
  union
  SELECT customer_id
  FROM 72crm_crm_business a LEFT JOIN 72crm_crm_activity b ON a.business_id = b.activity_type_id  AND activity_type = 5 AND type = 1
  WHERE DATE_FORMAT(b.create_time, '#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  AND b.create_user_id = #para(userId)
  #end

  #sql ("sumCustomerRecordInfo")
  select '总计' as realname,
         (select count(1) from `72crm_crm_activity` a
         where a.create_user_id in (#fori(userIds))
         and DATE_FORMAT(a.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime) and a.type = 1) as recordCount,
         count(v.customer_id) as customerCount
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
            ) as v inner join 72crm_admin_user as b on v.create_user_id = b.user_id
  where b.user_id in (#fori(userIds))
  #end

  #sql ("customerRecordCategoryStats")
  select category,IFNULL(count(activity_id),0) as recordNum,
         TRUNCATE(IFNULL(count(activity_id)*100/(select count(*) from 72crm_crm_activity
                                      where type = 1
                                      and DATE_FORMAT(create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
                                      and create_user_id in (
                                      #for(i:ids)
                                        #(for.index > 0 ? "," : "") #para(i)
                                      #end
                                      )),0),2) as proportion
  from 72crm_crm_activity
  where type = 1 and DATE_FORMAT(create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
  and create_user_id in (
    #for(i:ids)
        #(for.index > 0 ? "," : "") #para(i)
    #end
  ) group by category
  #end

  #sql ("customerConversionStats")
    SELECT
        #for(x : timeList)
          #(for.index == 0 ? "" : ",")
          TRUNCATE (ifnull(count(DATE_FORMAT(create_time, '%Y%m' ) = '#(x)' and deal_status = 1 or null),0)*100/ifnull(count(DATE_FORMAT(create_time, '%Y%m' ) = '#(x)' or null),0),2) as '#(x)'
        #end
    FROM
        72crm_crm_customer
    WHERE
        owner_user_id IN (#fori(userIds))
        and status != 3
  #end

  #sql ("customerConversionInfo")
  select a.customer_name,
         case when b.contract_id is NULL then null when b.contract_id is not null then b.name end as contract_name,
         case when b.contract_id is NULL then null when b.contract_id is not null then b.money end as contract_money,
         case when b.contract_id is NULL then null when b.contract_id is not null then b.order_date end as order_date,
         (select sum(c.money)
          from 72crm_crm_receivables as c
          where c.check_status =1 and c.contract_id = b.contract_id
         ) as receivables_money,
         d.realname AS owner_user_name,
         a.create_time,a.update_time
  from 72crm_crm_customer as a left join 72crm_crm_contract as b on a.customer_id = b.customer_id
  left join 72crm_admin_user as d on a.owner_user_id = d.user_id
  where DATE_FORMAT(a.create_time,#para(sqlDateFormat)) = #para(time) and check_status != 7
  and a.deal_status = 1 and a.owner_user_id in (#fori(ids))
  and a.status != 3
  #end

  #sql ("poolStats")
  #for(x : timeList)
  #(for.index == 0 ? "" : "union all")
  select '#(x)' as type,
         count(type_id) as putInNum,
         (select count(type_id)
          from 72crm_crm_owner_record
          where DATE_FORMAT(create_time,'#(sqlDateFormat)') = #(x) and type = 9 and post_owner_user_id in (#fori(userIds))) as receiveNum
  from 72crm_crm_owner_record
  where DATE_FORMAT(create_time,'#(sqlDateFormat)') = #(x) and type = 9 and pre_owner_user_id in (#fori(userIds))
  #end
  #end

  #sql ("poolTable")
  select a.realname,b.name as deptName,
         (select count(type_id)
          from 72crm_crm_owner_record
          where DATE_FORMAT(create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
          and type = 9 and pre_owner_user_id = a.user_id) as putInNum,
         (select count(type_id)
          from 72crm_crm_owner_record
          where DATE_FORMAT(create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
          and type = 9 and post_owner_user_id = a.user_id) as receiveNum
  from 72crm_admin_user as a left join 72crm_admin_dept as b on a.dept_id = b.dept_id
  where user_id in (
    #for(i:ids)
        #(for.index > 0 ? "," : "") #para(i)
    #end
  )
  #end

  #sql ("receiveFromPool")
  select type_id from 72crm_crm_owner_record
  where DATE_FORMAT(create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
  and type = 9 and pre_owner_user_id = #para(userId)
  #end

  #sql ("putInPool")
  select type_id from 72crm_crm_owner_record
  where DATE_FORMAT(create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
  and type = 9 and post_owner_user_id = #para(userId)
  #end

  #sql ("sumPoolTable")
  select '总计' as realname,'' as deptName,
         (select count(type_id)
          from 72crm_crm_owner_record
          where DATE_FORMAT(create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
          and type = 9 and pre_owner_user_id in (#fori(ids))) as putInNum,
         (select count(type_id)
          from 72crm_crm_owner_record
          where DATE_FORMAT(create_time,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime)
          and type = 9 and post_owner_user_id in (#fori(ids))) as receiveNum
  #end

  #sql ("employeeCycle")
  #for(x : timeList)
  #(for.index == 0 ? "" : "union all")
  select '#(x)' as type,
         (TRUNCATE(IF(AVG(TIMESTAMPDIFF(DAY,create_time,deal_time)) >= 0,AVG(TIMESTAMPDIFF(DAY,create_time,deal_time)),0),1)) as cycle,
         count(customer_id) as customerNum
  from 72crm_crm_customer
  where DATE_FORMAT(create_time,'#(sqlDateFormat)') = #(x) and deal_status = 1 and owner_user_id in (#fori(userIds))
  #end
  #end

  #sql ("employeeCycleInfo")
  #for(x : userIds)
  #(for.index == 0 ? "" : "union all")
  select a.realname,
         (TRUNCATE(IF(AVG(TIMESTAMPDIFF(DAY,b.create_time,b.deal_time)) >= 0,AVG(TIMESTAMPDIFF(DAY,b.create_time,b.deal_time)),0),1)) as cycle,
         count(distinct b.customer_id) as customerNum
  from 72crm_admin_user as a left join 72crm_crm_customer as b on a.user_id = b.owner_user_id
  where DATE_FORMAT(b.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  and b.deal_status = 1 and a.user_id = #(x)
  #end
  #end

  #sql ("sumEmployeeCycleInfo")
  select '总计' as realname,
         count(distinct b.customer_id) as customerNum
  from 72crm_admin_user as a left join 72crm_crm_customer as b on a.user_id = b.owner_user_id
  where DATE_FORMAT(b.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  and b.deal_status = 1 and a.user_id in (#fori(userIds))
  #end

  #sql ("districtCycle")
  #for(x : districts)
  #(for.index == 0 ? "" : "union all")
  select '#(x)' as type,
         (TRUNCATE(IF(AVG(TIMESTAMPDIFF(DAY,create_time,deal_time)) >= 0,AVG(TIMESTAMPDIFF(DAY,create_time,deal_time)),0),1)) as cycle,
         count(customer_id) as customerNum
  from 72crm_crm_customer
  where DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  and deal_status = 1 and owner_user_id in (#fori(userIds)) and address like '%#(x)%'
  #end
  #end

  #sql ("sumDistrictCycle")
  select '总计' as type,
         count(customer_id) as customerNum
  from 72crm_crm_customer
  where DATE_FORMAT(create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  and deal_status = 1 and owner_user_id in (#fori(userIds)) and address is not null and address != ',,' and address != ''
  #end

  #sql ("productCycle")
  #for(x : products)
  #(for.index == 0 ? "" : "union all")
  select '#(x.name)' as productName,
         (TRUNCATE(IF(AVG(TIMESTAMPDIFF(DAY,a.create_time,a.deal_time)) >= 0,AVG(TIMESTAMPDIFF(DAY,a.create_time,a.deal_time)),0),1)) as cycle,
         count(distinct a.customer_id) as customerNum
  from 72crm_crm_customer as a left join 72crm_crm_contract as b on a.customer_id = b.customer_id
  left join 72crm_crm_contract_product as c on b.contract_id = c.contract_id
  where DATE_FORMAT(a.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  and a.deal_status = 1 and a.owner_user_id in (#fori(userIds)) and c.product_id = #(x.product_id)
  #end
  #end

  #sql ("sumProductCycle")
  select '总计' as productName,
         count(distinct a.customer_id) as customerNum
  from 72crm_crm_customer as a inner join 72crm_crm_contract as b on a.customer_id = b.customer_id
  inner join 72crm_crm_contract_product as c on b.contract_id = c.contract_id
  inner join 72crm_crm_product as d on c.product_id = d.product_id
  where DATE_FORMAT(a.create_time,'#(sqlDateFormat)') between #para(beginTime) and #para(finalTime)
  and a.deal_status = 1 and a.owner_user_id in (#fori(userIds))
  #end
#end
