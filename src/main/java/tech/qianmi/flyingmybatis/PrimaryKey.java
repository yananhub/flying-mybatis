package tech.qianmi.flyingmybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the field of the class as the primary key field.
 *
 * @author yanan.zhang
 * @since 2021/2/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {

    /**
     * Configuring the primary key is auto-increment.
     */
    KeyType keyType() default KeyType.AUTO;

    /**
     * The type of primary key
     *
     * @author yanan
     * @since 2018/10/16
     */
    enum KeyType {
        AUTO, UUID, INPUT
    }
}
