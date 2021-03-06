package top.hyizhou.framework.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于拦截过量请求
 * @author hyizhou
 * @date 2021/11/25 10:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    /**
     * 秒数，与最大请求数一起使用，表示指定秒数内运行通过最大请求数
     */
    int seconds();

    /**
     * 最大请求数，表示指定秒数内最大请求数
     */
    int maxCount();

    /**
     * 是在登录后请求也会被限制。默认是false，不做限制
     */
    boolean asBeforeLogin() default false;
}
