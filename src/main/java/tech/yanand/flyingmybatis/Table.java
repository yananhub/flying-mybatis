package tech.yanand.flyingmybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark the Domain object as a database table.
 *
 * @author Richard Zhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * The name of the database table.
     *
     * @return The table name.
     */
    String value();
}