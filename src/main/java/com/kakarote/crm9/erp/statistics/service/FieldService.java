package com.kakarote.crm9.erp.statistics.service;

import com.kakarote.crm9.erp.statistics.common.StatisticsFieldEnum;
import com.kakarote.crm9.utils.R;

/**
 * @author zyy
 */
public class FieldService {


    public R getField(Integer type) {

        if(StatisticsFieldEnum.DEPT.getType() == type){

        }else if(StatisticsFieldEnum.TIME.getType() == type){

        }else if(StatisticsFieldEnum.WORKPHASE.getType() == type){

        }else if(StatisticsFieldEnum.WORKTYPE.getType() == type){

        }else {

        }
        return R.ok();
    }
}
