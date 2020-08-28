#namespace("crm.invoice")
    #sql ("queryPageList")
    select a.*,b.customer_name,c.num as contract_num,c.money as contract_money,d.realname as owner_user_name
    from 72crm_crm_invoice as a left join 72crm_crm_customer as b on a.customer_id = b.customer_id
    left join 72crm_crm_contract as c on a.contract_id = c.contract_id
    left join 72crm_admin_user as d on a.owner_user_id = d.user_id
    where #(conditions) order by a.update_time desc
    #end

    #sql ("updateInvoiceStatus")
    update 72crm_crm_invoice set invoice_status = 1,invoice_number = ?,real_invoice_date = ?,logistics_number = ?
    where invoice_id = ?
    #end

    #sql ("queryById")
    select a.*,b.customer_name,c.num as contract_num,c.money as contract_money,d.realname as owner_user_name,e.realname as create_user_name
    from 72crm_crm_invoice as a left join 72crm_crm_customer as b on a.customer_id = b.customer_id
    left join 72crm_crm_contract as c on a.contract_id = c.contract_id
    left join 72crm_admin_user as d on a.owner_user_id = d.user_id
    left join 72crm_admin_user as e on a.create_user_id = e.user_id
    where invoice_id = ?
    #end

    #sql("transfer")
    update 72crm_crm_invoice set owner_user_id = #para(ownerUserId) where invoice_id in (#fori(ids))
    #end

    #sql ("queryFileList")
    select a.file_id,a.file_path,a.name,a.file_type,a.size,a.create_time,'附件上传' as source,0 as readOnly,x.realname as create_user_name
    from 72crm_admin_file as a inner join 72crm_crm_invoice as b on a.batch_id = b.batch_id
    left join 72crm_admin_user as x on a.create_user_id = x.user_id
    where b.invoice_id = #para(invoiceId)
    #end

    #sql ("updateCheckStatusById")
      update 72crm_crm_invoice set check_status = ? where invoice_id = ?
    #end

    #sql("queryInvoiceByRecordId")
      SELECT invoice_apply_number,invoice_id,owner_user_id FROM 72crm_crm_invoice WHERE examine_record_id=? limit 0,1
    #end
#end
