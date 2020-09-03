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
 * SQLの命令における数量詞を管理するカタログです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum Quantifier implements BiCatalog<Quantifier, String> {

    /**
     * {@code in} 詞
     */
    IN(0, "in"),

    /**
     * {@code all} 詞
     */
    ALL(1, "all"),

    /**
     * {@code exists} 詞
     */
    EXISTS(2, "exists"),

    /**
     * {@code any} 詞
     */
    ANY(3, "any"),

    /**
     * {@code some} 詞
     */
    SOME(4, "some"),

    /**
     * {@code between} 詞
     */
    BETWEEN(5, "between");

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