package com.kakarote.crm9.erp.admin.common;


/**
 * 消息通知枚举类
 */
public enum AdminMessageEnum {

    /**
     * 消息通知枚举类
     */
    NULL(0,0,"NULL"),
    OA_TASK_ALLOCATION(1,1,"分配给我的任务"),
    OA_TASK_JOIN(2,1,"我参与的任务"),
    OA_TASK_OVER(3,1,"任务结束通知"),
    OA_LOG_REPLY(4,2,"日志回复提醒"),
    OA_LOG_SEND(5,2,"日志发送提醒"),
    OA_EXAMINE_REJECT(6,3,"OA审批拒绝提醒"),
    OA_EXAMINE_PASS(7,3,"OA审批通过提醒"),
    OA_NOTICE_MESSAGE(8,4,"公告通知提醒"),
    OA_EVENT_MESSAGE(9,5,"日程通知"),
    CRM_CONTRACT_REJECT(10,6,"合同拒绝通知"),
    CRM_CONTRACT_PASS(11,6,"合同全部通过通知"),
    CRM_RECEIVABLES_REJECT(12,6,"回款拒绝通知"),
    CRM_RECEIVABLES_PASS(13,6,"回款通过通知"),
    CRM_CUSTOMER_IMPORT(14,6,"导入客户通知"),
    CRM_CUSTOMER_IMPORT_CANCEL(15,6,"导入客户取消通知"),
    CRM_CONTACTS_IMPORT(16,6,"导入联系人通知"),
    CRM_CONTACTS_IMPORT_CANCEL(17,6,"导入联系人取消通知"),
    CRM_LEADS_IMPORT(18,6,"导入线索通知"),
    CRM_LEADS_IMPORT_CANCEL(19,6,"导入线索取消通知"),

    CRM_PRODUCT_IMPORT_CANCEL(21,6,"导入产品取消通知"),
    CRM_BUSINESS_USER(22,6,"商机团队成员通知"),
    CRM_CUSTOMER_USER(23,6,"客户团队成员通知"),
    CRM_CONTRACT_USER(24,6,"合同团队成员通知"),
    OA_EXAMINE_NOTICE(25,3,"OA待审核审批提醒"),
    CRM_CONTRACT_EXAMINE(26,6,"合同待审核审批提醒"),
    CRM_RECEIVABLES_EXAMINE(27,6,"回款待审核审批提醒"),
    CRM_BUSINESS_TEAM_EXIT(28, 6, "商机团队成员退出提醒"),
    CRM_CUSTOMER_TEAM_EXIT(29, 6, "客户团队成员退出提醒"),
    CRM_CONTRACT_TEAM_EXIT(30, 6, "合同团队成员退出提醒"),
    CRM_BUSINESS_REMOVE_TEAM(31, 6, "移除商机团队成员提醒"),
    CRM_CUSTOMER_REMOVE_TEAM(32, 6, "移除客户团队成员提醒"),
    CRM_CONTRACT_REMOVE_TEAM(33, 6, "移除合同团队成员提醒"),
    OA_COMMENT_REPLY(34, 2, "评论回复提醒"),
    CRM_INVOICE_REJECT(35, 6, "发票拒绝通知"),
    CRM_INVOICE_PASS(36, 6, "发票通过通知"),
    CRM_INVOICE_EXAMINE(37, 6, "发票待审核审批提醒"),



    CRM_WORK(38,6,"导入项目通知"),
    CRM_DEMAND_IMPORT(39,6,"导入需求通知"),
    WORK_RISK_IMPORT(40,6,"导入风险通知"),
    WORK_TASK_IMPORT(41,6,"导入任务通知"),
    WORK_RESOURCES(42,6,"导入资源配置通知"),
    WORK_PRODUCT(43,6,"导入产品通知"),
    WORK_PRODUCT_FUNCTIONE(44,6,"导入产品功能通知"),
    WORK_WORKCONFIG(45,6,"导入配置管理通知"),

    ;

    AdminMessageEnum(Integer type,Integer label,String remarks){
        this.type=type;
        this.label=label;
        this.remarks=remarks;
    }
    private final int  type;
    private final int  label;
    private final String remarks;

    public int getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getLabel() {
        return label;
    }

    public static AdminMessageEnum parse(int type) {
        for (AdminMessageEnum Enum : AdminMessageEnum.values()) {
            if (Enum.getType()==type) {
                return Enum;
            }
        }
        return NULL;
    }
}
