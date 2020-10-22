package com.kakarote.crm9.erp.work.common;

/**
 * @author zyy
 */
public enum WorkTypeEnum {

    TYPE_1(1,"销售开拓"),
    TYPE_2(2,"自主实施(有合同)"),
    TYPE_3(3,"销售开拓(无合同)"),
    TYPE_4(4,"产品研发"),
    TYPE_5(5,"案件分析服务"),
    TYPE_6(6,"培训服务"),
    TYPE_7(7,"运维服务"),
    TYPE_8(8,"公共项目"),
    ;

    private Integer type;

    private String value;


    WorkTypeEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }



    public static String getName(Integer types) {
        for (WorkTypeEnum c : WorkTypeEnum.values()) {
            if (c.getType().equals(types)) {
                return c.value;
            }
        }
        return null;
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
