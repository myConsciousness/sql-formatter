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

import org.thinkit.api.catalog.BiCatalog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * SQLの命令における終了句を管理するカタログです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum EndClause implements BiCatalog<EndClause, String> {

    /**
     * {@code from} 句
     */
    FROM(0, "from"),

    /**
     * {@code where} 句
     */
    WHERE(1, "where"),

    /**
     * {@code having} 句
     */
    HAVING(2, "having"),

    /**
     * {@code set} 句
     */
    SET(3, "set"),

    /**
     * {@code by} 句
     */
    BY(4, "by"),

    /**
     * {@code union} 句
     */
    UNION(5, "union"),

    /**
     * {@code join} 句
     */
    JOIN(6, "join"),

    /**
     * {@code into} 句
     */
    INTO(7, "into"),

    /**
     * {@code values} 句
     */
    VALUES(8, "values"),

    /**
     * {@code on} 句
     */
    ON(9, "on");

    /**
     * コード値
     */
    @Getter
    private final int code;

    /**
     * タグ
     */
    @Getter
    private final String tag;
}