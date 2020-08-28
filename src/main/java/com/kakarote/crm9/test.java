package com.kakarote.crm9;


import cn.hutool.core.util.ReUtil;
import com.alibaba.druid.filter.config.ConfigTools;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.utils.R;

public class test {
    public static void main(String[] args) throws Exception {
        if (!ReUtil.isMatch("^(\\+?0?\\d{2,4}\\-?)?\\d{6,11}$", "18670280079")) {
            System.out.println("-----------------");
        }
        if (!ReUtil.isMatch("^(\\+?0?\\d{2,4}\\-?)?\\d{6,11}$", "18670280079")) {
            System.out.println("---------1111111111111--------");
        }
    }

}
