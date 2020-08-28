#namespace("crm.customer")
    #sql ("queryIsRoUser")
      select count(*) from 72crm_crm_customer where find_in_set(?,ro_user_id) and customer_id = ? and owner_user_id is not null
    #end

    #sql("count")
      select count(*) as customerNum from 72crm_crm_customer where customer_name = ?
    #end

    #sql("queryById")
      select a.*,b.realname as ownerUserName,(IF(a.owner_user_id is null,1,0)) as is_pool,
      (select count(1) from 72crm_crm_customer_user_star where customer_id = a.customer_id and user_id = ?) as star
      from `72crm_crm_customer` as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
      where customer_id = ?
    #end

    #sql("queryBusiness")
      select a.business_id,a.business_name,a.money,a.is_end,a.type_id,a.status_id,b.customer_name,c.name as type_name,d.name as status_name
      from 72crm_crm_business as a
      left join 72crm_crm_customer as b on a.customer_id = b.customer_id
      left join 72crm_crm_business_type as c on a.type_id = c.type_id
      left join 72crm_crm_business_status as d on a.status_id = d.status_id
      where a.status = 1 and a.customer_id = #para(customerId)
      #if(businessName)
        and a.business_name like concat('%',#para(businessName),'%')
      #end
      #(conditions)
    #end

    #sql ("queryBusinessNumber")
      select count(*) from 72crm_crm_business where customer_id = ? and status = 1
    #end
    #sql ("queryContactsNumber")
      select count(*) from 72crm_crm_contacts where customer_id = ?
    #end
    #sql ("queryCustomerBatchId")
      select batch_id from 72crm_crm_customer where customer_id in(#fori(ids))
    #end
    #sql("queryContacts")
      select a.contacts_id,a.name,a.mobile,a.post,a.telephone,
      (select value from `72crm_crm_contacts_data` as b where b.batch_id = a.batch_id and b.name = '是否关键决策人' limit 1) as '是否关键决策人'
      from 72crm_crm_contacts as a where a.customer_id = #para(customerId) #(conditions)
      #if(contactsName)
        and a.name like CONCAT('%',#para(contactsName),'%')
      #end
    #end
    #sql("queryReceivablesPlan")
      select a.plan_id,a.num,b.customer_name,c.num as contract_num,a.money,a.return_date,a.return_type,a.remind,a.remark
      from 72crm_crm_receivables_plan as a inner join 72crm_crm_customer as b on a.customer_id = b.customer_id
      inner join 72crm_crm_contract as c on a.contract_id = c.contract_id
      where b.customer_id = ?
    #end

    #sql("queryReceivablesPlanPageSelect")
      select a.plan_id,a.num,b.customer_name,c.num as contract_num,a.money,a.return_date,a.return_type,a.remind,a.remark
    #end

    #sql("queryReceivables")
      select a.receivables_id,a.number as receivables_num,b.name as contract_name,b.money as contract_money,a.money as receivables_money,c.realname as owner_user_name,
      a.check_status,a.return_time,d.num as planNum
      from 72crm_crm_receivables as a inner join 72crm_crm_contract as b on a.contract_id = b.contract_id
      inner join 72crm_admin_user as c on a.owner_user_id = c.user_id
      left join 72crm_crm_receivables_plan as d on a.plan_id = d.plan_id
      where a.customer_id = #para(customerId) and a.check_status != 7 #(conditions)
    #end

    #sql ("queryReturnVisit")
      select a.visit_id,a.visit_number,a.visit_time,b.realname as owner_user_name,c.num as contract_num,
             (select value from 72crm_crm_return_visit_data  where name = '回访形式' and batch_id = a.batch_id limit 1) as '回访形式',
             (select value from 72crm_crm_return_visit_data  where name = '客户满意度' and batch_id = a.batch_id limit 1) as '客户满意度'
      from 72crm_crm_return_visit as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
      left join 72crm_crm_contract as c on a.contract_id = c.contract_id
      where a.customer_id = #para(customerId) #(conditions)
    #end

    #sql ("queryInvoice")
      select a.invoice_id,a.invoice_apply_number,a.invoice_money,a.invoice_date,a.real_invoice_date,a.invoice_type,a.invoice_number,
             a.logistics_number,a.telephone,a.remark,a.check_status,a.invoice_status,a.create_time,
             b.realname as owner_user_name,c.num as contract_num,c.money as contract_money
      from 72crm_crm_invoice as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
      left join 72crm_crm_contract as c on a.contract_id = c.contract_id
      where a.customer_id = #para(customerId) #(conditions)
    #end

    #sql ("queryInvoiceInfo")
      select a.info_id,a.title_type,a.invoice_title,a.tax_number,a.deposit_bank,a.deposit_account,a.deposit_address,
             a.telephone,a.remark,a.create_time,b.realname as owner_user_name
      from 72crm_crm_invoice_info as a left join 72crm_admin_user as b on a.create_user_id = b.user_id
      where a.customer_id = #para(customerId)
    #end

    #sql ("queryCallRecord")
      select a.call_record_id,a.number,a.create_time,a.talk_time,a.dial_time,a.state,a.type,a.file_path,a.file_name,b.realname as owner_user_name
      from 72crm_call_record as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
      where a.model = 'customer' and a.model_id = #para(customerId)
    #end

    #sql ("queryContract")
      select a.contract_id,a.num,a.name as contract_name,b.customer_name,a.money,a.start_time,a.end_time,a.check_status,
      ifnull((select sum(c.money) from `72crm_crm_receivables` c where c.contract_id = a.contract_id and c.check_status = 1),0) as receivablesMoneyCount
      from 72crm_crm_contract as a inner join 72crm_crm_customer as b on a.customer_id = b.customer_id
      where a.customer_id = #para(customerId) and a.check_status != 7
      #if(contractName)
      and a.name like CONCAT('%',#para(contractName),'%')
      #end
      #(conditions)
    #end

    #sql ("queryPassContract")
      select a.contract_id,a.num,a.name as contract_name,b.customer_name,a.money,a.start_time,a.end_time,a.check_status
      from 72crm_crm_contract as a inner join 72crm_crm_customer as b on a.customer_id = b.customer_id
      where a.customer_id = #para(customerId) and a.check_status = #para(checkStatus) and a.check_status != 7
      #(conditions)
      #if(contractName)
      and a.name like CONCAT('%',#para(contractName),'%')
      #end
    #end

    #sql ("getMembers")
    select a.user_id as id,a.realname,b.name
    from 72crm_admin_user as a left join 72crm_admin_dept as b on a.dept_id = b.dept_id
    where a.user_id = ?
    #end

    #sql ("queryDealNum")
      select count(*) from 72crm_crm_customer
      where deal_status = 1 and customer_id in (#fori(ids))
    #end

    #sql ("setDealStatus")
      update 72crm_crm_customer set status = 1,deal_status = 1,deal_time = #para(time)
      where customer_id in (#fori(ids))
    #end

    #sql ("updateCustomer")
      update 72crm_crm_customer set
      update_time=now()
      #if(status)
        ,status=#para(status)
      #end
      #if(dealStatus)
        ,deal_status=#para(dealStatus)
      #end
      #if(dealTime)
        ,deal_time=#para(dealTime)
      #end
      #if(ownerUserId)
        ,owner_user_id=#para(ownerUserId)
      #end
      #if(putInPool)
        ,owner_user_id=null,pool_time = now(),is_receive = null,ro_user_id=',',ro_user_id=','
      #end
      #if(roUserId)
        ,ro_user_id=#para(roUserId)
      #end
      #if(rwUserId)
        ,ro_user_id=#para(roUserId)
      #end
      where customer_id in (#fori(ids))
    #end

    #sql ("lockNum")
    SELECT count(owner_user_id) as num,owner_user_id as userId FROM 72crm_crm_customer
    where owner_user_id is not null and status = 1
    and customer_id in (#fori(ids)) group by owner_user_id
    #end

    #sql ("ownerNum")
    SELECT count(customer_id) FROM 72crm_crm_customer
    where (owner_user_id != #para(ownerUserId) or owner_user_id is null)
    and customer_id in (
        #for(i : ids)
            #(for.index > 0 ? "," : "")#para(i)
        #end
    )
    #end

    #sql ("updateContacts")
      UPDATE 72crm_crm_contacts SET owner_user_id = null
      WHERE customer_id in (#fori(ids))
    #end
    #sql ("updateContacts1")
      UPDATE 72crm_crm_contacts SET owner_user_id = #para(userId)
      WHERE customer_id in (#fori(ids))
    #end

    #sql ("selectOwnerUserId")
      SELECT
        customer_id
      FROM
        (
        SELECT
          customer_id,
          create_time,
          owner_user_id
        FROM
          72crm_crm_customer
        WHERE
          owner_user_id != 0
          AND owner_user_id IS NOT NULL
          AND deal_status = 0
          AND STATUS = 1
        ) AS ccc
        LEFT JOIN (
        SELECT
          activity_type_id,
          type,
          activity_type,
          max( create_time ) AS create_time
        FROM
          72crm_crm_activity
        WHERE
          activity_type = 2
          AND type = 1
        GROUP BY
          activity_type_id
        ) AS car ON car.activity_type_id = ccc.customer_id
      WHERE
          (
            unix_timestamp( now( ) ) - unix_timestamp( IFNULL( car.create_time, ccc.create_time ) ) > #para(followupDay)
            AND IFNULL(car.create_time,ccc.create_time) >= ccc.create_time
          )
          or (unix_timestamp(now())-unix_timestamp(ccc.create_time)) >  #para(dealDay)
    #end

    #sql ("queryByIds")
    select * from 72crm_crm_customer
    where customer_id in (
        #for(i:ids)
          #(for.index > 0 ? "," : "")#para(i)
        #end
    )
    #end
    #sql ("deleteMember")
      update 72crm_crm_customer set rw_user_id = replace(rw_user_id,?,','),ro_user_id = replace(ro_user_id,?,',') where customer_id = ?
    #end
    #sql ("updateDealStatusById")
      update 72crm_crm_customer set deal_status = ?,deal_time = ?,status = 1 where customer_id = ?
    #end

    #sql ("getCustomersByIds")
    update 72crm_crm_customer set owner_user_id = #para(userId),followup = 0,receive_time = #para(now),update_time = #para(now),is_receive = #para(isReceive)
     where customer_id in (
        #for(i:ids)
          #(for.index > 0 ? "," : "")#para(i)
        #end
    )
    #end
    #sql ("queryCustomerSetting")
      SELECT a.setting_id,a.setting_name,a.customer_num,a.customer_deal,CONCAT(ifnull(GROUP_CONCAT(d.realname),''),',',ifnull(GROUP_CONCAT(c.name),'')) as `range`,
       ifnull(GROUP_CONCAT(d.user_id),'') as userIds,ifnull(GROUP_CONCAT(c.dept_id),'') as deptIds
    FROM 72crm_crm_customer_setting as a
           LEFT JOIN 72crm_crm_customer_settinguser as b on a.setting_id=b.setting_id
           LEFT JOIN 72crm_admin_dept as c on b.dept_id=c.dept_id and b.type='2'
           LEFT JOIN 72crm_admin_user as d on b.user_id=d.user_id and b.type='1'
    WHERE a.type=#para(type) GROUP BY a.setting_id
    #end
    #sql("queryCustomerSettingByUserId")
      SELECT a.customer_num,a.customer_deal,b.type FROM 72crm_crm_customer_setting  as a
                                                          LEFT JOIN 72crm_crm_customer_settinguser as b on a.setting_id=b.setting_id and b.type='1'
      WHERE  b.user_id=#para(userId) and a.type=#para(type)
      union all
      SELECT a.customer_num,a.customer_deal,b.type FROM 72crm_crm_customer_setting  as a
                                                          LEFT JOIN 72crm_crm_customer_settinguser as b on a.setting_id=b.setting_id and b.type='2'
                                                          LEFT JOIN 72crm_admin_user as c on b.dept_id=c.dept_id
      WHERE c.user_id=#para(userId) and a.type=#para(type)
    #end
    #sql("queryCustomerSettingNum")
      SELECT COUNT(*) FROM 72crm_crm_customer WHERE status !='3'
      #if(deal==0)
        and deal_status=0
      #end
      #if(type==2)
        and status='2'
      #end
    #end
    #sql ("getCustomerSettingCount")
      select count(*)
      from `72crm_crm_customer_setting` a left join `72crm_crm_customer_settinguser` b on a.setting_id = b.setting_id
      where a.type = #para(type)
        #if(userId)
        and user_id = #para(userId)
        #end
        #if(deptId)
        and dept_id = #para(deptId)
        #end
    #end
    #sql ("queryFieldDuplicate")
       select count(1) from `72crm_crm_customer` where #(key) = '#(value)' and status != 3
    #end
    #sql ("queryFieldDuplicate1")
       select count(*) from `72crm_admin_field` a left join `72crm_crm_customer_data` b on a.field_id = b.field_id where label = 2  and field_name = #para(key) and value = #para(value);
    #end
    #sql ("dataCheck")
        select a.* from (
          select a.customer_id as id,a.customer_name as name, a.create_time, b.realname as ownerUserName,2 as type,a.last_time,''as poolName
          from `72crm_crm_customer` a
          join `72crm_admin_user` b on a.owner_user_id = b.user_id
          where a.owner_user_id is not null and a.status != 3
          #if(name)
            and a.customer_name like concat('%', #para(name), '%')
          #end
          #if(phone)
            and (a.telephone = #para(phone) or a.mobile = #para(phone))
          #end
          union all
          select a.customer_id as id,a.customer_name as name, a.create_time, null as ownerUserName,9 as type,a.last_time,
          (select GROUP_CONCAT(y.pool_name) from 72crm_crm_customer_pool_relation as x join 72crm_crm_customer_pool as y on x.pool_id = y.pool_id where x.customer_id = a.customer_id) as poolName
          from `72crm_crm_customer` a
          where a.owner_user_id is null and a.status != 3
          #if(name)
            and a.customer_name like concat('%', #para(name), '%')
          #end
          #if(phone)
            and (a.telephone = #para(phone) or a.mobile = #para(phone))
          #end
          union all
          select a.leads_id as id,a.leads_name as name, a.create_time, b.realname as ownerUserName,1 as type,a.last_time,''as poolName
          from `72crm_crm_leads` a
          join `72crm_admin_user` b on a.owner_user_id = b.user_id
          where a.is_transform = 0
          #if(name)
            and a.leads_name like concat('%', #para(name), '%')
          #end
          #if(phone)
            and (a.telephone = #para(phone) or a.mobile = #para(phone))
          #end
        ) a limit 0,10
    #end

    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a inner join 72crm_crm_customer as b on a.batch_id = b.batch_id
    left join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.customer_id = #para(customerId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'客户详情' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_customer_data as b on a.batch_id = b.value
    inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
    inner join 72crm_crm_customer as d on d.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where d.customer_id = #para(customerId)
    union all
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'跟进记录' as source,1 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a
    inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
    inner join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.activity_type_id = #para(customerId) and b.type = 1 and b.activity_type = 2
    #end

    #sql ("queryNum")
    select (
        (select count(*) from 72crm_admin_file as a left join 72crm_crm_customer as b on a.batch_id = b.batch_id
         where b.customer_id = #para(customerId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_customer_data as b on a.batch_id = b.value
         inner join 72crm_admin_field as c on c.field_id = b.field_id and c.type = 8
         inner join 72crm_crm_customer as d on d.batch_id = b.batch_id
         where d.customer_id = #para(customerId)) +
        (select count(*) from 72crm_admin_file as a inner join 72crm_crm_activity as b on a.batch_id = b.batch_id
         where b.activity_type_id = #para(customerId) and b.type = 1 and b.activity_type = 2 )
    ) as fileCount,
    (select count(contacts_id) from 72crm_crm_contacts where customer_id = #para(customerId) #(contactsCon)) as contactCount,
    (select (select count(*) from 72crm_admin_user
                 where FIND_IN_SET(user_id,(a.rw_user_id)) or FIND_IN_SET(user_id,(a.ro_user_id)) or user_id = a.owner_user_id)
         from 72crm_crm_customer as a where customer_id = #para(customerId)
    ) as memberCount,
    (select count(*) from 72crm_crm_business where customer_id = #para(customerId) and status = 1 #(businessCon)
    ) as businessCount,
    (select count(*) from 72crm_crm_contract where customer_id = #para(customerId) and check_status != 7 #(contractCon)
    ) as contractCount,
    (select count(*) from 72crm_crm_return_visit where customer_id = #para(customerId) #(returnVisitCon)
    ) as returnVisitCount,
    (select count(*) from 72crm_crm_invoice where customer_id = #para(customerId) #(invoiceCon)
    ) as invoiceCount,
    (select count(*) from 72crm_call_record where model = 'customer' and model_id = #para(customerId)
    ) as callRecordCount,
    count(*) as receivablesCount
    from
    72crm_crm_receivables
    where customer_id = #para(customerId) and check_status != 7 #(receivablesCon)
    #end
      #sql("searchPhone")
        SELECT * FROM 72crm_crm_customer where 1=1
        and (mobile like CONCAT('%',#para(search),'%') or telephone like CONCAT('%',#para(search),'%')  )
        limit 0,1
    #end

    #sql("searchFieldValue")
       SELECT
            cc.customer_id,
            cc.customer_name,
            afd.`value`,
            afd.batch_id
        FROM
            (
                72crm_admin_field af
                LEFT JOIN 72crm_crm_customer_data afd ON (af.field_id = afd.field_id)
            )
        LEFT JOIN 72crm_crm_customer cc ON (afd.batch_id = cc.batch_id)
        WHERE
            af.type = 7
        AND af.label = 2
        AND cc.customer_id = #para(customerId)
    #end

    #sql ("queryCustomerPageListByFieldType1")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          d.name as contacts_name,d.mobile as contacts_mobile,d.address as contacts_address,
          `z`.*
        FROM
          `72crm_crm_customer` as `a`
          LEFT JOIN `72crm_crm_contacts` `d` ON `a`.`contacts_id` = `d`.`contacts_id`
        JOIN (
          SELECT
            b.batch_id as field_batch_id
            #for(field : fieldMap)
              #if(field.value&&field.value.get("field_type")!=1)
                #if(field.value.get("type")==12)
                  ,GROUP_CONCAT(if(a.name = '#(field.key)',c.name,null)) AS '#(field.key)'
                #elseif(field.value.get("type")==10)
                  ,GROUP_CONCAT(if(a.name = '#(field.key)',d.realname,null)) AS '#(field.key)'
                #else
                   #if(field.value.get("field_type")==2)
                    ,max(CASE WHEN `a`.`name` = '#(field.value.get("name"))' THEN `a`.`value` END) AS '#(field.value.get("field_name"))'
                   #else
                   ,max(CASE WHEN `a`.`name` = '#(field.key)'  THEN `a`.`value` END) AS '#(field.key)'
                   #end
                #end
              #end
            #end
            FROM 72crm_crm_customer_data AS a RIGHT JOIN (
              SELECT batch_id FROM `72crm_crm_customer` as a
              WHERE 1=1
              #if(batchList&&batchList.size()>0)
                and batch_id in ( #fori(batchList))
              #end
              #for(query : queryList) #if(query.get("type")==1) #(query.get("sql")) #end #end #if(orderByKey!="create_user_name"&&orderByKey!="owner_user_name") ORDER BY `#(orderByKey)` #(orderByType) #end #if(!field) #if(page&&limit) LIMIT #(page),#(limit) #end #end
            ) AS b ON a.batch_id = b.batch_id
            #if(fieldMap.containsKey("user"))
              left join 72crm_admin_user d on find_in_set(d.user_id,ifnull(a.value,0))
            #end
            #if(fieldMap.containsKey("dept"))
              left join 72crm_admin_dept c on find_in_set(c.dept_id,ifnull(a.value,0))
            #end
            GROUP BY b.batch_id
        ) `z` ON `a`.`batch_id` = `z`.`field_batch_id`
      ) as views
        where 1=1
         #for(query : queryList) #if(query.get("type")==3) #(query.get("sql")) #end #end
        ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
        #if(field) #if(page&&limit) LIMIT #(page),#(limit) #end #end
    #end
    #sql ("queryCustomerPageListByFieldType2")
      #(select) FROM (
        SELECT
            `a`.*,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`create_user_id`) AS `create_user_name`,
          (SELECT realname FROM 72crm_admin_user WHERE user_id=`a`.`owner_user_id`) AS `owner_user_name`,
          `z`.*,
          d.name as contacts_name,d.mobile as contacts_mobile,d.address as contacts_address
        FROM
          `72crm_crm_customer` as `a`
          LEFT JOIN `72crm_crm_contacts` `d` ON `a`.`contacts_id` = `d`.`contacts_id`
        LEFT JOIN (
          SELECT
            a.batch_id as field_batch_id
            #for(field : fieldMap)
              #if(field.value&&field.value.get("field_type")!=1)
                #if(field.value.get("type")==12)
                  ,GROUP_CONCAT(if(a.name = '#(field.key)',c.name,null)) AS '#(field.key)'
                #elseif(field.value.get("type")==10)
                  ,GROUP_CONCAT(if(a.name = '#(field.key)',d.realname,null)) AS '#(field.key)'
                #else
                   #if(field.value.get("field_type")==2)
                    ,max(CASE WHEN `a`.`name` = '#(field.value.get("name"))' THEN `a`.`value` END) AS '#(field.value.get("field_name"))'
                   #else
                   ,max(CASE WHEN `a`.`name` = '#(field.key)'  THEN `a`.`value` END) AS '#(field.key)'
                   #end
                #end
              #end
            #end
          FROM
            72crm_crm_customer_data as a
          #if(fieldMap.containsKey("user"))
            left join 72crm_admin_user d on find_in_set(d.user_id,ifnull(a.value,0))
          #end
          #if(fieldMap.containsKey("dept"))
            left join 72crm_admin_dept c on find_in_set(c.dept_id,ifnull(a.value,0))
          #end
          where 1=1
          #if(batchList&&batchList.size()>0)
                and batch_id in ( #fori(batchList))
          #end
          GROUP BY a.batch_id
        ) `z` ON `a`.`batch_id` = `z`.`field_batch_id`
      ) as views
      WHERE 1=1
      #for(query : queryList) #(query.get("sql")) #end
      ORDER BY CONVERT(`#(orderByKey)` using gbk) #(orderByType)
      #if(page&&limit)
          LIMIT #(page),#(limit)
      #end
    #end
    #sql ("queryCustomerPageListCount")
      SELECT count(1) FROM `72crm_crm_customer` as a
      WHERE 1=1
      #if(batchList&&batchList.size()>0)
          and batch_id in ( #for(str:batchList) #(for.index == 0 ? "" : ",") #para(str) #end)
      #end
      #for(query : queryList)
        #if(query.get("type")==1) #(query.get("sql")) #end
      #end
    #end

    #sql ("getCustomerPageList")
    select *,(select count(*) from 72crm_crm_business as a where a.customer_id = views.customer_id and a.status = 1) as business_count,
    #end

    #sql ("queryOutDays")
    select TO_DAYS(now()) - TO_DAYS(a.create_time) as days from
        (select pre_owner_user_id,create_time from 72crm_crm_owner_record
         where type_id = #para(customerId) and type = 9 and post_owner_user_id is null order by create_time desc limit 1) as a
    where a.pre_owner_user_id = #para(userId)
    #end

    #sql ("queryPoolIdByUserId")
    select pool_id from 72crm_crm_customer_pool
    where find_in_set(#para(userId),admin_user_id) or find_in_set(#para(userId),member_user_id)
    or find_in_set(#para(deptId),member_dept_id)
    #end

    #sql ("queryNearbyCustomer")
    select
    a.customer_id,a.customer_name,a.last_time,a.location,a.detail_address,a.mobile,a.telephone,
    b.realname as owner_user_name,a.lng,a.lat,
    (CASE WHEN a.owner_user_id is null THEN '公海' ELSE '客户' END ) as model,
    ROUND(
        6378.138 * 2 * ASIN(
            SQRT(
                POW(
                    SIN(
                        (
                            #para(lat) * PI() / 180 - lat * PI() / 180
                        ) / 2
                    ),
                    2
                ) + COS(#para(lat) * PI() / 180) * COS(lat * PI() / 180) * POW(
                    SIN(
                        (
                            #para(lng) * PI() / 180 - lng * PI() / 180
                        ) / 2
                    ),
                    2
                )
            )
        ) * 1000
    ) as distance
    from 72crm_crm_customer as a left join 72crm_admin_user as b on a.owner_user_id = b.user_id
    where a.status != 3 and ROUND(
        6378.138 * 2 * ASIN(
            SQRT(
                POW(
                    SIN(
                        (
                            #para(lat) * PI() / 180 - lat * PI() / 180
                        ) / 2
                    ),
                    2
                ) + COS(#para(lat) * PI() / 180) * COS(lat * PI() / 180) * POW(
                    SIN(
                        (
                            #para(lng) * PI() / 180 - lng * PI() / 180
                        ) / 2
                    ),
                    2
                )
            )
        ) * 1000
    ) < #para(radius)
    #if(ownerUserId)
    and owner_user_id = #para(ownerUserId)
    #end
    #if(type == 2)
    and owner_user_id in (#fori(userIds))
    #elseif(type == 9)
    and customer_id in (select customer_id from 72crm_crm_customer_pool_relation where pool_id in (#fori(poolIds)))
    #else
    and (owner_user_id in (#fori(userIds)) or customer_id in (select customer_id from 72crm_crm_customer_pool_relation where pool_id in (#fori(poolIds))))
    #end
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
    where a.status = 1 and a.customer_id in (#fori(customerIds))
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
    where a.status = 1 and a.customer_id in (#fori(customerIds)) and b.business_id is null
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
    where a.status = 1 and a.deal_status = 0 and a.customer_id in (#fori(customerIds))
    #if(ids)
       and a.owner_user_id in (#fori(ids))
    #end
    #if(customer_level_setting == 2)
       and c.value = #para(level)
    #end
    #end
#end
