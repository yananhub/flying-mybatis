/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.yanand.flyingmybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the field of the class as the primary key field.
 *
 * @author Richard Zhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {

    /**
     * Configuring the primary key is auto-increment, UUID or input.
     *
     * @return Key type.
     */
    KeyType keyType() default KeyType.AUTO;

    /**
     * The type of primary key.
     *
     * @author Richard Zhang
     */
    enum KeyType {

        /**
         * Auto-increment by DB.
         */
        AUTO,

        /**
         * Generated by Java {@link java.util.UUID}
         */
        UUID,

        /**
         * Custom input key.
         */
        INPUT
    }
}
