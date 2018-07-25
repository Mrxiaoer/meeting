package com.spider.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统日志注解
 * 
 * @author maoxinmin 1.0
 */
@Target(ElementType.METHOD) // 方法声明
@Retention(RetentionPolicy.RUNTIME) // 注释将由编译器记录在类文件中，并由VM在运行时保留，因此可以反射读取
@Documented // 表示具有类型的注释默认情况下由javadoc和类似工具记录,类型声明使用Documented进行注释，其注释将成为注释元素的公共API的一部分。
public @interface SysLog {

	String value() default "";
}
