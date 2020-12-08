package com.kakarote.crm9.erp.work.common;

/**
 * @author zyy
 */
public enum  WorkUserEnum {

    TYPE_1(1,"任务"),
    TYPE_2(2,"变更参与人"),
    TYPE_3(3,"变更提出人"),
    WORK(4,"项目"),
    TASK_DETAILS(5,"子任务管理"),
    TASK_HOURS(6,"工时提报"),
    DEMAND(7,"需求"),
    MILESTONE(8,"里程碑"),
    CONFIGURATION(9,"配置管理"),
    TASKCONFIG(10,"配置计划"),
    HOURS(11,"工时"),
    RISK(12,"风险"),
    TASK_O(13,"任务"),
    ;

    private Integer type;

    private String value;


    WorkUserEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
