/*
 * Copyright 2020 Kato Shinya.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.thinkit.formatter.catalog.dml;

import org.thinkit.common.catalog.Catalog;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * SQLの命令における論理式を管理するカタログです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum LogicalExpression implements Catalog<LogicalExpression> {

    /**
     * {@code when} 式
     */
    WHEN(0, "when"),

    /**
     * {@code else} 式
     */
    ELSE(1, "else"),

    /**
     * {@code and} 式
     */
    AND(2, "and"),

    /**
     * {@code or} 式
     */
    OR(3, "or"),

    /**
     * {@code end} 式
     */
    END(4, "end"),

    /**
     * {@code case} 式
     */
    CASE(5, "case");

    /**
     * コード値
     */
    @Getter
    private final int code;

    /**
     * 式
     */
    @Getter
    private final String expression;

    /**
     * 引数として渡された {@code token} に格納された文字列が {@link LogicalExpression} に定義されているか判定します。
     *
     * @param token 判定対象のトークン
     * @return 引数として渡された {@code token} に格納された文字列が {@link LogicalExpression}
     *         に定義されている場合は {@code true} 、それ以外は {@code false}
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    public static boolean contains(@NonNull String token) {

        for (LogicalExpression expression : LogicalExpression.values()) {
            if (expression.getExpression().equals(token)) {
                return true;
            }
        }

        return false;
    }
}