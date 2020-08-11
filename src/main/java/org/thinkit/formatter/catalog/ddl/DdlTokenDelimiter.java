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
 * DDL命令におけるトークンの区切り文字を管理するカタログです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum DdlTokenDelimiter implements Catalog<DdlTokenDelimiter> {

    /**
     * {@code create table} のトークン区切り文字
     */
    CREATE_TABLE(0, " ;(,)'[]\""),

    /**
     * {@code alter table} のトークン区切り文字
     */
    ALTER_TABLE(1, " ;(,)'[]\""),

    /**
     * {@code comment on} のトークン区切り文字
     */
    COMMENT_ON(2, " ;'[]\"");

    /**
     * コード値
     */
    @Getter
    private final int code;

    /**
     * 区切り文字
     */
    @Getter
    private final String delimiter;
}