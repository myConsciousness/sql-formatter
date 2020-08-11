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
import lombok.RequiredArgsConstructor;

/**
 * DDL命令の句を管理するカタログです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum Clause implements Catalog<Clause> {

    /**
     * {@code add} 句
     */
    ADD(0, "add"),

    /**
     * {@code on} 句
     */
    ON(1, "on"),

    /**
     * {@code column} 句
     */
    COLUMN(2, "column");

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
}