package com.kakarote.crm9.erp.statistics.common;

import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.work.common.WorkTypeEnum;
import org.omg.CORBA.TIMEOUT;

/**
 * @author zyy
 */
public enum StatisticsFieldEnum {

    DEPT(1,"区域"),
    TIME(2,"时间"),
    WORKPHASE(3,"项目阶段"),
    WORKTYPE(4,"项目类型"),

    CRM_NULL(0,null);
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
}
