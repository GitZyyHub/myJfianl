package com.kakarote.crm9;


import cn.hutool.core.util.ReUtil;
import com.alibaba.druid.filter.config.ConfigTools;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.utils.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        String str = "10/11/1 10:20:44";
        Date parse = sdf.parse(str);
        System.out.println(parse.toString());
    }

}
