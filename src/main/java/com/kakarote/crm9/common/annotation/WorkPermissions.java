package com.kakarote.crm9.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE,ElementType.PARAMETER })
@Inherited
@Documented
public @interface WorkPermissions {
    String[] value();
}
