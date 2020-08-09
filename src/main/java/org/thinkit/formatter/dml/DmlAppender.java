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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * {@link DmlTokenizer} クラスと連動してDMLクエリを生成するアペンダークラスです。
 * <p>
 * 生成した文字列は {@link #toString()} メソッドを使用することで取得することができます。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@EqualsAndHashCode
final class DmlAppender {

    /**
     * 整形済みのSQL
     */
    private StringBuilder sql;

    /**
     * DMLトークナイザ
     */
    private DmlTokenizer dmlTokenizer;

    /**
     * DMLインデンター
     */
    private DmlIndenter dmlIndenter;

    /**
     * 開始ライン
     */
    @Getter
    private boolean beginLine;

    /**
     * デフォルトコンストラクタ
     */
    private DmlAppender() {
    }

    /**
     * コンストラクタ
     *
     * @param dmlTokenizer DMLのトークナイザー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private DmlAppender(@NonNull DmlTokenizer dmlTokenizer) {
        this.sql = new StringBuilder();
        this.dmlTokenizer = dmlTokenizer;
        this.dmlIndenter = DmlIndenter.of();
    }

    public static DmlAppender register(@NonNull DmlTokenizer dmlTokenizer) {
        return new DmlAppender(dmlTokenizer);
    }

    public DmlAppender appendToken() {
        this.sql.append(this.dmlTokenizer.getToken());
        return this;
    }

    public DmlAppender appendNewLine() {
        this.sql.append(this.dmlIndenter.newline());
        return this;
    }

    public DmlAppender increment() {
        this.dmlIndenter.increment();
        return this;
    }

    public DmlAppender decrement() {
        this.dmlIndenter.decrement();
        return this;
    }

    public DmlAppender toBeginLine() {
        this.beginLine = true;
        return this;
    }

    public DmlAppender toNotBeginLine() {
        this.beginLine = false;
        return this;
    }

    @Override
    public String toString() {
        return this.sql.toString();
    }
}