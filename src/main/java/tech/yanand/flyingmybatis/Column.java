package tech.yanand.flyingmybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mapping a class's field to a database column is often necessary to
 * specify the name of the database column.
 *
 * @author Richard Zhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * Specify the name of the table column.
     *
     * @return Column name of the table.
     */
    String value() default "";
}