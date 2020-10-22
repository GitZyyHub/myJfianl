package com.kakarote.crm9.common.enums;

public enum CrmEnum {
    /**
     * CRM Enum
     */

    WORK("work", 19, "项目", "72crm_work", "work_id", "72crm_work_data", "name"),
    MILESTONE("milestone", 20, "里程碑", "72crm_milestone", "milestone_id", null, null),
    TASK("task",21,"任务","72crm_task","task_id",null,null),
    CRM_NULL("NULL", 0, "NULL", "NULL", "NULL", "NULL", "NULL");

    private final String name;  //
    private final int type;     //字段自定义类型
    private final String remarks;   //解释
    private final String tableName; //主表明
    private final String tableId;   //主键
    private final String dataTableName; //自定义字段表
    private final String tableNameField; //自定义字段表对应id

    CrmEnum(String name, int type, String remarks, String tableName, String tableId, String dataTableName, String tableNameField) {
        this.name = name;
        this.type = type;
        this.remarks = remarks;
        this.tableName = tableName;
        this.tableId = tableId;
        this.dataTableName = dataTableName;
        this.tableNameField = tableNameField;
    }

    public static CrmEnum parse(int type) {
        for (CrmEnum Enum : CrmEnum.values()) {
            if (Enum.getType()==type) {
                return Enum;
            }
        }
        return CRM_NULL;
    }

    public static CrmEnum parse(String name) {
        for (CrmEnum Enum : CrmEnum.values()) {
            if (Enum.getName().equals(name)) {
                return Enum;
            }
        }
        return CRM_NULL;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableId() {
        return tableId;
    }

    public String getDataTableName() {
        return dataTableName;
    }

    public String getTableNameField() {
        return tableNameField;
    }
}







/*CRM_LEADS("crm_leads", 1, "线索", "72crm_crm_leads", "leads_id", "72crm_crm_leads_data", "leads_name"),
    CRM_CUSTOMER("crm_customer", 2, "客户", "72crm_crm_customer", "customer_id", "72crm_crm_customer_data", "customer_name"),
    CRM_CONTACTS("crm_contacts", 3, "联系人", "72crm_crm_contacts", "contacts_id", "72crm_crm_contacts_data", "name"),
    CRM_PRODUCT("crm_product", 4, "产品", "72crm_crm_product", "product_id", "72crm_crm_product_data", "name"),
    CRM_BUSINESS("crm_business", 5, "商机", "72crm_crm_business", "business_id", "72crm_crm_business_data", "business_name"),
    CRM_CONTRACT("crm_contract", 6, "合同", "72crm_crm_contract", "contract_id", "72crm_crm_contract_data", "num"),
    CRM_RECEIVABLES("crm_receivables", 7, "回款", "72crm_crm_receivables", "receivables_id", "72crm_crm_receivables_data", "number"),
    CRM_RECEIVABLES_PLAN("crm_receivables_plan", 8, "回款计划", "72crm_crm_receivables_plan", "plan_id", "NULL", "num"),
    CRM_CUSTOMER_POOL("crm_customer_pool", 9, "客户公海", "72crm_crm_customer", "customer_id", "72crm_crm_customer_data", "customer_name"),
    CRM_MARKETING("crm_marketing", 10, "推广", "72crm_crm_marketing", "marketing_id", null, "marketing_name"),
    OA_LOG("oa_log", 11, "日志", "72crm_oa_log", "log_id", null, null),
    OA_EXAMINE("oa_examine", 12, "办公审批", "72crm_oa_examine", "examine_id", null, "name"),
    TASK("task", 13, "任务", "72crm_task", "task_id", null, "name"),
    WORK_LABEL("work_label", 15, "标签", "72crm_work_task_label", "label_id", null, "name"),
    OA_CALENDAR("oa_calendar", 16, "日历", "72crm_oa_event", "event_id", null, "title"),
    CRM_RETURN_VISIT("crm_return_visit", 17, "客户回访", "72crm_crm_return_visit", "visit_id", "72crm_crm_return_visit_data", "visit_number"),
    CRM_INVOICE("crm_invoice", 18, "发票", "72crm_crm_invoice", "invoice_id", null, "invoice_apply_number"),*/