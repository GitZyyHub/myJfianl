package com.kakarote.crm9.erp.crm.common;

public enum InstrumentModelEnum {

    /**
     * 仪表盘模块
     */
    SELL_MONTH(1, "合同/回款金额目标及完成情况"),
    DATA_INFO(2, "数据汇总"),
    PERFORMANCE(4, "业绩指标完成率"),
    SELL_FUNNEL(5, "销售漏斗"),
    REMIND(6, "遗忘提醒"),
    RANKING(7, "排行榜");

    InstrumentModelEnum(Integer modelId, String modelName) {
        this.modelId = modelId;
        this.modelName = modelName;
    }

    private Integer modelId;
    private String modelName;


    public Integer getModelId() {
        return modelId;
    }

    public String getModelName() {
        return modelName;
    }
}
