package com.tibbers.zhonghui.annotation;

import java.lang.annotation.*;

/**
 * @author Tibbers
 * @create 2017-05-27 18:43
 */

@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenListen {
    boolean isListen() default true;
}
