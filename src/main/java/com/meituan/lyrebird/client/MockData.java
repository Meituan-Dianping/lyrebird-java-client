package com.meituan.lyrebird.client;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to mark a class or a method on test case to declare which mock data will be used
 */
@Retention(value = RUNTIME)
@Target(value = {METHOD, TYPE})
public @interface MockData {
    /**
     * Group ID of mock data
     *
     * @return group ID of mock data
     */
    String groupID() default "";

    /**
     * Group name of mock data
     *
     * @return group name of mock data
     */
    String groupName() default "";
}
