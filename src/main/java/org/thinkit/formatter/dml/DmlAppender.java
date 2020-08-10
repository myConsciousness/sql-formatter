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
     * {@link DmlAppender} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link DmlAppender} クラスの新しいインスタンス
     */
    private static DmlAppender of() {
        return new DmlAppender();
    }

    /**
     * {@link DmlAppender} クラスのインスタンスを生成する {@link Builder} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link Builder} クラスの新しいインスタンス
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link DmlAppender} クラスのインスタンスを生成する処理を定義したビルダークラスです。
     *
     * @author Kato Shinya
     * @since 1.0
     * @version 1.0
     */
    public static class Builder {

        /**
         * DMLトークナイザ
         */
        private DmlTokenizer dmlTokenizer;

        /**
         * インデント数
         */
        private int indent = 4;

        /**
         * デフォルトコンストラクタ
         */
        private Builder() {
        }

        /**
         * 連動する {@link DmlTokenizer} クラスを登録した {@link DmlAppender} クラスに登録します。
         *
         * @param dmlTokenizer {@link DmlAppender} クラスと連動するDMLのトークナイザー
         *
         * @exception NullPointerException 引数として {@code null} が渡された場合
         */
        public Builder register(@NonNull DmlTokenizer dmlTokenizer) {
            this.dmlTokenizer = dmlTokenizer;
            return this;
        }

        /**
         * インデント数を設定します。
         *
         * @param indent インデント数
         *
         * @exception NullPointerException 引数として {@code null} が渡された場合
         */
        public Builder withIndent(int indent) {
            this.indent = indent;
            return this;
        }

        public DmlAppender build() {
            final DmlAppender appender = DmlAppender.of();
            appender.sql = new StringBuilder();
            appender.dmlTokenizer = this.dmlTokenizer;
            appender.dmlIndenter = DmlIndenter.of(this.indent);
            appender.beginLine = false;
            return appender;
        }
    }

    /**
     * 登録したトークナイザーから現在位置のトークンを取得し文字列へ追加します。
     * <p>
     * この {@link DmlAppender#appendToken()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DmlAppender appendToken() {
        this.sql.append(this.dmlTokenizer.getToken());
        return this;
    }

    /**
     * {@link DmlIndenter} クラスから改行コードを取得し文字列へ追加します。
     * <p>
     * この {@link DmlAppender#appendNewLine()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DmlAppender appendNewLine() {
        this.sql.append(this.dmlIndenter.newline());
        return this;
    }

    /**
     * {@link DmlIndenter} クラスをインクリメントします。
     * <p>
     * この {@link DmlAppender#increment()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DmlAppender increment() {
        this.dmlIndenter.increment();
        return this;
    }

    /**
     * {@link DmlIndenter} クラスをデクリメントします。
     * <p>
     * この {@link DmlAppender#decrement()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DmlAppender decrement() {
        this.dmlIndenter.decrement();
        return this;
    }

    /**
     * 開始ライン可否を {@code true} へ上書きします。
     * <p>
     * この {@link DmlAppender#toBeginLine()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DmlAppender toBeginLine() {
        this.beginLine = true;
        return this;
    }

    /**
     * 開始ライン可否を {@code false} へ上書きします。
     * <p>
     * この {@link DmlAppender#toBegitoNotBeginLinenLine()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DmlAppender toNotBeginLine() {
        this.beginLine = false;
        return this;
    }

    @Override
    public String toString() {
        return this.sql.toString();
    }
}