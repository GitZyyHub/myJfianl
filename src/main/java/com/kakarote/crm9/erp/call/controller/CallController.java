package com.kakarote.crm9.erp.call.controller;

import com.jfinal.core.Controller;
import com.kakarote.crm9.utils.R;

/**
 * 通话记录
 */
public class CallController extends Controller {

    /**
     * 搜索呼入电话
     */
    public void checkAuth(){
        renderJson(R.ok().put("auth",false));
    }
}
