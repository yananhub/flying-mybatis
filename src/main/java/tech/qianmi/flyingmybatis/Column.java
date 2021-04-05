package tech.qianmi.flyingmybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mapping a class's field to a database column is often necessary to
 * specify the name of the database column.
 *
 * @author yanan.zhang
 * @since 2021/2/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * Specify the name of the database column.
     */
    String value() default "";
}