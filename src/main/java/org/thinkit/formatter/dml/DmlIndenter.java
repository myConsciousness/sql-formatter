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

package org.thinkit.formatter.dml;

import org.thinkit.common.catalog.Indentation;
import org.thinkit.formatter.common.Indentable;

/**
 * DML命令のインデント操作を管理するクラスです。
 * <p>
 * この {@link DmlIndenter} クラスは {@code formatter-commons} プロジェクトでより汎用的な
 * {@link Indentable} インターフェースおよび {@link Indent} クラスの実装により非推奨となりました。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 *
 * @deprecated
 */
@Deprecated
final class DmlIndenter {

    /**
     * 改行コード
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * インデントスペース
     */
    private String indentSpaces;

    /**
     * インデントファクター
     */
    private int indentFactor;

    /**
     * デフォルトコンストラクタ
     */
    private DmlIndenter() {
        this.indentSpaces = Indentation.getIndentSpaces();
        this.indentFactor = 0;
    }

    /**
     * コンストラクタ
     *
     * @param indent インデント数
     */
    private DmlIndenter(int indent) {
        this.indentSpaces = Indentation.getIndentSpaces(indent);
        this.indentFactor = 0;
    }

    /**
     * {@link DmlIndenter} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link DmlIndenter} クラスの新しいインスタンス
     */
    public static DmlIndenter of() {
        return new DmlIndenter();
    }

    /**
     * 引数として渡された {@code indent} の数値に基づいて {@link DmlIndenter} クラスの新しいインスタンスを生成し返却します。
     *
     * @param indent インデント数
     * @return {@link DmlIndenter} クラスの新しいインスタンス
     */
    public static DmlIndenter of(int indent) {
        return new DmlIndenter(indent);
    }

    public DmlIndenter increment() {
        this.indentFactor++;
        return this;
    }

    public DmlIndenter decrement() {
        this.indentFactor--;
        return this;
    }

    public String newline() {

        final StringBuilder newline = new StringBuilder();
        newline.append(LINE_SEPARATOR);

        for (int i = 0; i < indentFactor; i++) {
            newline.append(this.indentSpaces);
        }

        return newline.toString();
    }
}