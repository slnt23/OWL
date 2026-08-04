package xyz.nanian.owl.log.annotation;

import xyz.nanian.owl.log.constant.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Unified operation log annotation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    LogType type() default LogType.BIZ;

    String module();

    String action();

    boolean persist() default false;
}
