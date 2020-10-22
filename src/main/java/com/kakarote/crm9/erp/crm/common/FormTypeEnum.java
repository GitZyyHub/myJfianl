package com.kakarote.crm9.erp.crm.common;

/**
 * @author hmb
 */

public enum FormTypeEnum{
    /**
     * 自定义字段类型
     */
    TEXT(1,"text"),
    TEXTAREA(2,"textarea"),
    SELECT(3,"select"),
    DATE(4,"date"),
    NUMBER(5,"number"),
    FLOATNUMBER(6,"floatnumber"),
    MOBILE(7,"mobile"),
    FILE(8,"file"),
    CHECKBOX(9,"checkbox"),
    USER(10,"user"),        //选择系统人员
    STRUCTURE(12,"structure"),
    DATETIME(13,"datetime"),    //开始时间
    EMAIL(14,"email"),
    CUSTOMER(15,"customer"),    //选择客户名称
    BUSINESS(16,"business"),
    CONTACTS(17,"contacts"),
    MAP_ADDRESS(18,"map_address"),
    CATEGORY(19,"category"),
    CONTRACT(20,"contract"),
    RECEIVABLES_PLAN(21,"receivables_plan"),
    BUSINESS_CAUSE(22,"business_cause"),
    EXAMINE_CAUSE(23,"examine_cause"),
    ADDRESS(24,"address"),
    WEBSITE(25,"website"),
    RETURN_VISIT(26, "return_visit"),
    RETURN_VISIT_CONTRACT(27, "return_visit_contract"),
    SINGLE_USER(28, "single_user"),
    TASK(29,"task"),    //任务
    MILESTONE(30,"milestone"), //里程碑
    WORK(31,"work"),        //项目
    SUBTASK(32,"subTask"),  //子任务
    NULL(0,"null")
    ;

    FormTypeEnum(int type, String formType){
        this.type = type;
        this.formType = formType;
    }

    private int type;
    private String formType;

    public static FormTypeEnum parse(int type) {
        for (FormTypeEnum Enum : FormTypeEnum.values()) {
            if (Enum.getType()==type) {
                return Enum;
            }
        }
        return NULL;
    }

    public static FormTypeEnum parse(String type) {
        for (FormTypeEnum Enum : FormTypeEnum.values()) {
            if (Enum.getFormType().equals(type)) {
                return Enum;
            }
        }
        return NULL;
    }

    public int getType(){
        return type;
    }

    public String getFormType(){
        return formType;
    }}
