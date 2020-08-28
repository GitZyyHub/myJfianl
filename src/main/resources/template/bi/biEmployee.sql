#namespace("bi.employee")
  #sql ("contractNum")
  #for(x : timeList)
  #(for.index == 0 ? "" : "union all")
  select #(x) as month,
         count(contract_id) as thisMonth,
         (select count(contract_id)
          from 72crm_crm_contract
          where DATE_FORMAT(order_date,'%Y%m') = #if(for.index == 0) #(x-89) #else #(x-1) #end and check_status = 1 and owner_user_id in (#fori(userIds))) as lastMonth,
         (select count(contract_id)
          from 72crm_crm_contract
          where DATE_FORMAt(order_date,'%Y%m') = #(x-100) and check_status = 1  and owner_user_id in (#fori(userIds))) as lastYear
  from 72crm_crm_contract
  where DATE_FORMAT(order_date,'%Y%m') = #(x) and check_status = 1 and owner_user_id in (#fori(userIds))
  #end
  #end

  #sql ("contractMoney")
  #for(x : timeList)
  #(for.index == 0 ? "" : "union all")
  select #(x) as month,
         IFNULL(SUM(money),0) as thisMonth,
         (select IFNULL(SUM(money),0)
          from 72crm_crm_contract
          where DATE_FORMAT(order_date,'%Y%m') = #if(for.index == 0) #(x-89) #else #(x-1) #end and check_status = 1 and owner_user_id in (#fori(userIds))) as lastMonth,
         (select IFNULL(SUM(money),0)
          from 72crm_crm_contract
          where DATE_FORMAt(order_date,'%Y%m') = #(x-100) and check_status = 1  and owner_user_id in (#fori(userIds))) as lastYear
  from 72crm_crm_contract
  where DATE_FORMAT(order_date,'%Y%m') = #(x) and check_status = 1 and owner_user_id in (#fori(userIds))
  #end
  #end

  #sql ("receivables")
  #for(x : timeList)
  #(for.index == 0 ? "" : "union all")
  select #(x) as month,
        IFNULL(SUM(money),0) as thisMonth,
         (select IFNULL(SUM(money),0)
          from 72crm_crm_receivables
          where DATE_FORMAT(return_time,'%Y%m') = #if(for.index == 0) #(x-89) #else #(x-1) #end and check_status = 1 and owner_user_id in (#fori(userIds))) as lastMonth,
         (select IFNULL(SUM(money),0)
          from 72crm_crm_receivables
          where DATE_FORMAt(return_time,'%Y%m') = #(x-100) and check_status = 1  and owner_user_id in (#fori(userIds))) as lastYear
  from 72crm_crm_receivables
  where DATE_FORMAT(return_time,'%Y%m') = #(x) and check_status = 1 and owner_user_id in (#fori(userIds))
  #end
  #end

  #sql ("contractByMonth")
  select contract_id
  from 72crm_crm_contract
  where DATE_FORMAT(order_date,'%Y%m') = #(month) and check_status = 1 and owner_user_id in (#fori(userIds))
  #end

  #sql ("receivablesByMonth")
  select receivables_id
  from 72crm_crm_receivables
  where DATE_FORMAT(return_time,'%Y%m') = #(month) and check_status = 1 and owner_user_id in (#fori(userIds))
  #end

  #sql ("totalContract")
  select count(contract_id) as contractNum,
        IFNULL(sum(money),0) as contractMoney,
        IFNULL(sum(receivablesMoney),0) as receivablesMoney,
        (IFNULL(sum(money),0) - IFNULL(sum(receivablesMoney),0)) as unreceivedMoney
  from (select a.money,a.contract_id,
               (select SUM(IFNULL(money,0)) from 72crm_crm_receivables as b
                where b.contract_id = a.contract_id and b.check_status = 1) as receivablesMoney
        from 72crm_crm_contract as a
        where DATE_FORMAT(a.order_date,#para(sqlDateFormat)) between #para(beginTime) and #para(finalTime) and a.check_status = 1
            #if(userIds)
            and a.owner_user_id in (#fori(userIds))
            #end
  ) as x
  #end

  #sql ("totalContractTable")
  #for(x : timeList)
  #(for.index == 0 ? "" : "union all")
  select #(x) as type,
         count(contract_id) as contractNum,
         IFNULL(SUM(IFNULL(money,0)),0) as contractMoney,
         IFNULL(SUM(receivablesMoney),0) as receivablesMoney
  from (select a.contract_id,a.money,
               (select SUM(money)
                from 72crm_crm_receivables as b
                where b.contract_id = a.contract_id and b.check_status = 1) as receivablesMoney
        from 72crm_crm_contract as a
        where DATE_FORMAT(a.order_date,'#(sqlDateFormat)') = #(x) and a.check_status = 1 and a.owner_user_id in (#fori(userIds))
  ) as x
  #end
  #end

  #sql ("receivablesByContract")
  select receivables_id from 72crm_crm_receivables as a left join 72crm_crm_contract as b on b.contract_id = a.contract_id
  where DATE_FORMAT(b.order_date,'#(sqlDateFormat)') = #(month) and b.check_status = 1 and b.owner_user_id in (#fori(userIds))
  and a.check_status = 1
  #end
#end
