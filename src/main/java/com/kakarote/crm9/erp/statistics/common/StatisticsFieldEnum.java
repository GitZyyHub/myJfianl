package com.kakarote.crm9.erp.statistics.common;

import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.common.WorkTypeEnum;
import org.omg.CORBA.TIMEOUT;

/**
 * @author zyy
 */
public enum StatisticsFieldEnum {

    AREA(1,"区域"),
    DEPT(2,"部门"),
    TIME(3,"时间"),
    WORKPHASE(4,"项目阶段"),
    WORKTYPE(5,"项目类型"),
    RISKLEVEL(6,"风险等级"),
    MILESTONE_TYPE(7,"里程碑状态"),
    TASK_TYPE(8,"任务状态"),
    DEMAND_STATUS(9,"需求状态"),
    DEMAND_TYPE(10,"需求类型"),
    RESOURCES_TYPE(11,"资源状态"),
    HOURS_TYPE(12,"工时"),

    CRM_NULL(0,"全部");
    ;


    Integer type;
    String content;

    public static StatisticsFieldEnum parse(int type) {
        for (StatisticsFieldEnum Enum : StatisticsFieldEnum.values()) {
            if (Enum.getType()==type) {
                return Enum;
            }
        }
        return CRM_NULL;
    }


    StatisticsFieldEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
