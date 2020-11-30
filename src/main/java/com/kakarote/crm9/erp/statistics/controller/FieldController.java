package com.kakarote.crm9.erp.statistics.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.statistics.service.FieldService;

/**
 * @author zyy
 */
public class FieldController extends Controller {

    @Inject
    FieldService fieldService;

    public void gerField(@Para("")Integer type){
        renderJson(fieldService.getField(type));
    }

}
