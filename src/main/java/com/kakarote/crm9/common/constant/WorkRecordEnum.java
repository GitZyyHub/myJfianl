package com.kakarote.crm9.common.constant;

import static com.kakarote.crm9.common.constant.BehaviorEnum.NULL;

/**
 * @author zyy
 */
public enum   WorkRecordEnum {

    WORK(1,"项目模块"),
    MILESTONE(2,"里程碑模块"),
    DEMAND(3,"需求模块"),
    TASK_CONFIG(4,"配置计划模块"),
    WORK_CONFIG(5,"配置管理模块"),
    RESOURCE(6,"资源配置模块"),
    TASK(7,"任务模块"),
    RISK(8,"风险模块"),
    TASKFILE(9,"任务资料"),
    WORKFILE(10,"资料成果"),
    SUBTASKS(11,"子任务模块"),
    HOURS(12,"工时提报"),
    WORK_DETAILS(13,"项目详情模块"),
    NULL(0, "null");
    ;


    private Integer type;

    private String typeName;

    public static WorkRecordEnum parse(int type) {
        for (WorkRecordEnum Enum : WorkRecordEnum.values()) {
            if (Enum.getType() == type) {
                return Enum;
            }
        }
        return NULL;
    }


    WorkRecordEnum(Integer type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
