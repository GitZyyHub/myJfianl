package com.kakarote.crm9.erp.admin.common;

public enum AdminModelEnum {
    /**
     * 系统后台模块枚举
     */
    COMPANY_HOME_PAGE("企业首页",1),
    APPLICATION_MANAGE("应用管理",2),
    USER_AND_DEPT("员工和部门管理",3),
    CARD_APP_MANAGE("名片小程序管理",4),
    ROLE_MANAGE("角色权限管理",5),
    CRM_EXAMINE("审批流（合同/回款）",6),
    OA_EXAMINE("审批流（办公）",7),
    PROJECT_MANAGE("项目管理",8),
    CRM_SETTING("客户管理",9),
    ADMIN_LOG_MANAGE("系统日志管理",10),
    OTHER_SETTING("其他设置",11);

    private final String modelName;
    private final Integer modelNumber;

    public String getModelName(){
        return modelName;
    }

    public Integer getModelNumber(){
        return modelNumber;
    }

    AdminModelEnum(String modelName, Integer modelNumber) {
        this.modelName = modelName;
        this.modelNumber = modelNumber;
    }
}
