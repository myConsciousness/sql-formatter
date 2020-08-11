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
 * SQLの命令におけるDDLステートメントを管理するカタログです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum DdlStatement implements Catalog<DdlStatement> {

    /**
     * {@code create table} ステートメント
     */
    CREATE_TABLE(0, "create table"),

    /**
     * {@code alter table} ステートメント
     */
    ALTER_TABLE(1, "alter table"),

    /**
     * {@code comment on} ステートメント
     */
    COMMENT_ON(2, "comment on"),

    /**
     * {@code create database} ステートメント
     */
    CREATE_DATABASE(3, "create database"),

    /**
     * {@code create drop} ステートメント
     */
    DROP(4, "drop");
    ;

    /**
     * コード値
     */
    @Getter
    private final int code;

    /**
     * ステートメント
     */
    @Getter
    private final String statement;
}