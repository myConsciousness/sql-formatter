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

package org.thinkit.formatter.catalog.ddl;

import org.thinkit.common.catalog.Catalog;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * DDL命令における開始句を管理するカタログです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum StartClause implements Catalog<StartClause> {
    /**
     * {@code add} 句
     */
    ADD(0, "add"),

    /**
     * {@code column} 句
     */
    COLUMN(1, "column"),

    /**
     * {@code modify} 句
     */
    MODIFY(2, "modify"),
    /**
     * {@code rename} 句
     */
    RENAME(3, "rename");

    /**
     * コード値
     */
    @Getter
    private final int code;

    /**
     * 句
     */
    @Getter
    private final String clause;

    /**
     * 引数として渡された {@code token} に格納された文字列が {@link StartClause} に定義されているか判定します。
     *
     * @param token 判定対象のトークン
     * @return 引数として渡された {@code token} に格納された文字列が {@link StartClause} に定義されている場合は
     *         {@code true} 、それ以外は {@code false}
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    public static boolean contains(@NonNull String token) {

        for (StartClause clause : StartClause.values()) {
            if (clause.getClause().equals(token)) {
                return true;
            }
        }

        return false;
    }
}