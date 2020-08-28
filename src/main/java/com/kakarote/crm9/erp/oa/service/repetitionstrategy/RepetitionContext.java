package com.kakarote.crm9.erp.oa.service.repetitionstrategy;

import com.kakarote.crm9.erp.oa.common.RepetitionType;

/**
 * 重复策略处理器
 * @author hmb
 */
public class RepetitionContext {

    public static AbstractRepetitionStrategy getRepetitionStrategy(Integer type){
        RepetitionType repetitionType = RepetitionType.valueOf(type);
        AbstractRepetitionStrategy strategy;
        switch (repetitionType){
            case DAILY:strategy = new DailyStrategy();break;
            case WEEKLY:strategy = new WeeklyStrategy();break;
            case MONTHLY:strategy = new MonthlyStrategy();break;
            case ANNUALLY:strategy = new AnnuallyStrategy();break;
            default:strategy = new NoRepetitionStrategy();break;
        }
        return strategy;
    }
}
