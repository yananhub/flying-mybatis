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

/**
 * Case format utilities
 */
final class CaseFormatUtils {

    private CaseFormatUtils() {
        // Instantiation is not allowed
    }

    /**
     * Field name to column name
     */
    static String toColumnName(String fieldName) {
        StringBuilder sb = new StringBuilder();
        appendChar(sb, fieldName, 0);
        return sb.toString();
    }

    /**
     * Type name to table name
     */
    static String toTableName(String typeName) {
        StringBuilder sb = new StringBuilder();
        // Append first char
        sb.append((char) (typeName.charAt(0) + 32));
        appendChar(sb, typeName, 1);
        return sb.toString();
    }

    private static void appendChar(StringBuilder sb, String name, int startIndex) {
        for (int i = startIndex; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (ch > 64 && ch < 91) {
                sb.append('_').append((char) (name.charAt(i) + 32));
            } else {
                sb.append(ch);
            }
        }
    }
}
