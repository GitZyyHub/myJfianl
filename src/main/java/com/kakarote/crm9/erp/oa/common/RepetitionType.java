package com.kakarote.crm9.erp.oa.common;

public enum RepetitionType {
    /**
     * RepetitionType
     */
    NO_REPETITION( 1,"从不重复"),
    DAILY ( 2,"每天" ),
    WEEKLY ( 3,"每周"),
    MONTHLY ( 4 ,"每月"),
    ANNUALLY ( 5,"每年" );

    private Integer type;
    private String remark;

    RepetitionType(Integer type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public static RepetitionType valueOf(Integer type) {
        for (RepetitionType c : RepetitionType.values ()) {
            if ( c.getType ().equals ( type ) ) {
                return c;
            }
        }
        return RepetitionType.NO_REPETITION;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
