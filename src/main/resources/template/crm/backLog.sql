#namespace("crm.backLog")
  #sql ("todayCustomerNum")
  select count(*) from 72crm_crm_customer
  where to_days(next_time) = to_days(now()) and last_time < next_time and owner_user_id = ? and status != 3
  #end

  #sql ("setLeadsFollowup")
    update 72crm_crm_leads set followup = 1 where leads_id in (#fori(ids))
  #end

  #sql ("followLeadsNum")
  select count(*) from 72crm_crm_leads as a
  where followup = 0
  and is_transform = 0  and owner_user_id = ?
  #end

  #sql ("setCustomerFollowup")
  update 72crm_crm_customer set followup = 1 where customer_id in (
    #for(i : ids)
      #(for.index > 0 ? "," : "")#para(i)
    #end
  )
  #end

  #sql ("followCustomerNum")
  select count(*) from 72crm_crm_customer as a
  where is_receive = 1 and followup = 0 and status = 1
  and owner_user_id = ?
  #end

  #sql ("checkContractNum")
  select count(*)
  from 72crm_crm_contract as a inner join 72crm_admin_examine_record as b on a.examine_record_id = b.record_id
  left join 72crm_admin_examine_log as c on b.record_id = c.record_id
  where c.examine_user = ? and a.check_status in (0,3) and c.examine_status in (0,3) and ifnull(b.examine_step_id, 1) = ifnull(c.examine_step_id, 1) and c.is_recheck != 1
  #end

  #sql ("checkReceivablesNum")
  select count(*)
  from 72crm_crm_receivables as a inner join 72crm_admin_examine_record as b on a.examine_record_id = b.record_id
  left join 72crm_admin_examine_log as c on b.record_id = c.record_id
  where c.examine_user = ? and a.check_status in (0,3) and ifnull(b.examine_step_id, 1) = ifnull(c.examine_step_id, 1) and c.is_recheck != 1
  #end

  #sql ("remindReceivablesPlanNum")
  select count(*)
  from 72crm_crm_receivables_plan as a inner join 72crm_crm_customer as b on a.customer_id = b.customer_id
  inner join 72crm_crm_contract as c on a.contract_id = c.contract_id
  where to_days(a.return_date) >= to_days(now()) and to_days(a.return_date) <= to_days(now())+a.remind
  and receivables_id is null and c.owner_user_id = ?
  #end

  #sql ("endContractNum")
  select count(*) from 72crm_crm_contract as a inner join 72crm_crm_customer as b on a.customer_id = b.customer_id
  where check_status = 1 and to_days(a.end_time) >= to_days(now()) and to_days(a.end_time) <= to_days(now()) + IFNULL(?,0) and a.owner_user_id = ?
  #end
  #sql("queryPutInPoolCustomer")
    SELECT
      customer_id
    FROM
      (SELECT customer_id,create_time,owner_user_id FROM 72crm_crm_customer WHERE owner_user_id = #para(userId) AND deal_status = 0 AND status = 1) AS a
      LEFT JOIN
      (SELECT activity_type_id,type,activity_type,max( create_time ) AS create_time FROM 72crm_crm_activity WHERE activity_type = 2 AND type = 1
        GROUP BY activity_type_id) AS b
      ON b.activity_type_id = a.customer_id
      LEFT JOIN (
      SELECT
        max( create_time ) AS create_time,
        type_id
      FROM
        72crm_crm_owner_record
      WHERE
        type = 9
        AND post_owner_user_id = #para(userId)
      GROUP BY
        type_id
      ) AS c ON c.type_id = a.customer_id
    WHERE
        to_days(now()) - to_days( IFNULL( b.create_time, a.create_time ) )
        BETWEEN #para(startFollowUpDays) AND #para(followUpDays)
        OR to_days(now()) - to_days( IFNULL( c.create_time, a.create_time ) )
        BETWEEN #para(startDealDays) AND #para(dealDays)
  #end
  #sql ("putInPoolRemindNum")
    SELECT
      COUNT( 1 )
    FROM
      (SELECT customer_id,create_time,owner_user_id FROM 72crm_crm_customer WHERE owner_user_id = #para(userId) AND deal_status = 0 AND status = 1) AS a
      LEFT JOIN
      (SELECT activity_type_id,type,activity_type,max( create_time ) AS create_time FROM 72crm_crm_activity WHERE activity_type = 2 AND type = 1
        GROUP BY activity_type_id) AS b
      ON b.activity_type_id = a.customer_id
      LEFT JOIN (
      SELECT
        max( create_time ) AS create_time,
        type_id
      FROM
        72crm_crm_owner_record
      WHERE
        type = 9
        AND post_owner_user_id = #para(userId)
      GROUP BY
        type_id
      ) AS c ON c.type_id = a.customer_id
    WHERE
        to_days(now()) - to_days( IFNULL( b.create_time, a.create_time ) )
        BETWEEN #para(startFollowUpDays) AND #para(followUpDays)
        OR to_days(now()) - to_days( IFNULL( c.create_time, a.create_time ) )
        BETWEEN #para(startDealDays) AND #para(dealDays)
  #end

  #sql ("putInPoolByRecord")
  select a.customer_id,(#para(limit_day) - (to_days(now()) - to_days(if(a.last_time > a.receive_time,a.last_time,a.receive_time)))) as poolDay
    from 72crm_crm_customer as a
    #if(customer_level_setting == 2)
       left join 72crm_crm_customer_data as c on a.batch_id = c.batch_id and c.name = '客户级别'
    #end
    #if(business_handle == 0)
       inner join 72crm_crm_business as d on a.customer_id = d.customer_id and d.status = 1
    #end
    where (to_days(now()) - to_days(if(a.last_time > a.receive_time,a.last_time,a.receive_time))) between (#para(limit_day) - #para(remindDay)) and #para(limit_day)
    and a.status = 1 and a.owner_user_id in (#fori(userIds))
    #if(ids)
       and a.owner_user_id in (#fori(ids))
    #end
    #if(deal_handle == 0)
       and a.deal_status = 0
    #end
    #if(customer_level_setting == 2)
       and c.value = #para(level)
    #end
  #end

  #sql ("putInPoolByBusiness")
    select a.customer_id,(#para(limit_day) - (to_days(now()) - to_days(a.receive_time))) as poolDay
    from 72crm_crm_customer as a left join 72crm_crm_business as b on a.customer_id = b.customer_id and b.status = 1
    #if(level != 1)
       left join 72crm_crm_customer_data as c on a.batch_id = c.batch_id and c.name = '客户级别'
    #end
    where (to_days(now()) - to_days(if(b.create_time > a.receive_time,b.create_time,a.receive_time))) between (#para(limit_day) - #para(remindDay)) and #para(limit_day)
    and a.status = 1 and a.owner_user_id in (#fori(userIds)) and b.business_id is null
    #if(ids)
       and a.owner_user_id in (#fori(ids))
    #end
    #if(deal_handle == 0)
       and a.deal_status = 0
    #end
    #if(customer_level_setting == 2)
       and c.value = #para(level)
    #end
    #end

    #sql ("putInPoolByDealStatus")
    select customer_id,(#para(limit_day) - (to_days(now()) - to_days(a.receive_time))) as poolDay
    from 72crm_crm_customer as a
    #if(level != 1)
       left join 72crm_crm_customer_data as c on a.batch_id = c.batch_id and c.name = '客户级别'
    #end
    where (to_days(now()) - to_days(a.receive_time)) between (#para(limit_day) - #para(remindDay)) and #para(limit_day)
    and a.status = 1 and a.deal_status = 0 and a.owner_user_id in (#fori(userIds))
    #if(ids)
       and a.owner_user_id in (#fori(ids))
    #end
    #if(customer_level_setting == 2)
       and c.value = #para(level)
    #end
    #end

    #sql ("returnVisitRemind")
    select contract_id from 72crm_crm_contract
    where check_status = 1 and owner_user_id in (#fori(userIds)) and to_days(now()) - to_days(start_time) >= #para(remindDay)
    #end

    #sql ("returnVisitRemindNum")
    select count(1) from 72crm_crm_contract
    where check_status = 1 and owner_user_id = #para(userId) and to_days(now()) - to_days(start_time) >= #para(remindDay)
    #end
#end
