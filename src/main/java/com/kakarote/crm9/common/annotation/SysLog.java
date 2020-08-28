package com.kakarote.crm9.common.annotation;

import com.kakarote.crm9.common.constant.LogEnum;

import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Inherited
@Documented
/**
 * 系统日志注解
 */
public @interface SysLog {
    LogEnum[] value();
}
