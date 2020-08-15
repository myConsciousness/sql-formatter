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

package org.thinkit.formatter.ddl;

import org.thinkit.common.Precondition;
import org.thinkit.common.exception.LogicException;
import org.thinkit.formatter.common.Indent;
import org.thinkit.formatter.common.Indentable;
import org.thinkit.formatter.common.Line;
import org.thinkit.formatter.common.Newline;
import org.thinkit.formatter.content.ddl.entity.DdlDefaultIndentItem;
import org.thinkit.formatter.content.ddl.rule.DdlDefaultIndentItemCollector;
import org.thinkit.framework.content.rule.RuleInvoker;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * {@link DdlTokenizer} クラスと連動してDDLクエリを生成するアペンダークラスです。
 * <p>
 * 生成した文字列は {@link #toString()} メソッドを使用することで取得することができます。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@EqualsAndHashCode
final class DdlAppender {

    /**
     * 整形済みのSQL
     */
    private StringBuilder sql;

    /**
     * DDL命令のトークナイザー
     */
    private DdlTokenizer ddlTokenizer;

    /**
     * インデント
     */
    private Indentable indent;

    /**
     * 改行
     */
    private Line newline;

    /**
     * デフォルトコンストラクタ
     */
    private DdlAppender() {
    }

    /**
     * {@link DdlAppender} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link DdlAppender} クラスの新しいインスタンス
     */
    private static DdlAppender of() {
        return new DdlAppender();
    }

    /**
     * {@link DdlAppender} クラスのインスタンスを生成する {@link Builder} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link Builder} クラスの新しいインスタンス
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link DdlAppender} クラスのインスタンスを生成する処理を定義したビルダークラスです。
     *
     * @author Kato Shinya
     * @since 1.0
     * @version 1.0
     */
    public static class Builder {

        /**
         * DDLトークナイザ
         */
        private DdlTokenizer ddlTokenizer;

        /**
         * インデント数
         */
        private int indent = -1;

        /**
         * デフォルトコンストラクタ
         */
        private Builder() {
        }

        /**
         * 連動する {@link DdlTokenizer} クラスを登録した {@link DdlAppender} クラスに登録します。
         *
         * @param ddlTokenizer {@link DdlAppender} クラスと連動するDMLのトークナイザー
         *
         * @exception NullPointerException 引数として {@code null} が渡された場合
         */
        public Builder register(@NonNull DdlTokenizer ddlTokenizer) {
            this.ddlTokenizer = ddlTokenizer;
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

        /**
         * {@link #register(DdlTokenizer)} メソッドと {@link #withIndent(int)} メソッドで設定された値を基に
         * {@link DdlAppender} クラスの新しいインスタンスを生成し返却します。
         * <p>
         * {@link #register(DdlTokenizer)} メソッドが呼び出されていない場合、または
         * {@link #register(DdlTokenizer)} メソッドで設定された値が {@code null} の場合は
         * {@link NullPointerException} が実行時に必ず発生します。
         *
         * @return {@link DdlAppender} クラスの新しいインスタンス
         *
         * @throws LogicException {@link #register(DdlTokenizer)} メソッドが呼び出されていない場合、または
         *                        {@link #register(DdlTokenizer)} メソッドで設定された値が
         *                        {@code null} の場合
         */
        public DdlAppender build() {
            Precondition.requireNonNull(this.ddlTokenizer);

            final DdlAppender appender = DdlAppender.of();
            appender.sql = new StringBuilder();
            appender.ddlTokenizer = this.ddlTokenizer;

            if (this.indent < 0) {
                final DdlDefaultIndentItem defaultIndentItem = RuleInvoker.of(DdlDefaultIndentItemCollector.of())
                        .invoke();
                appender.indent = Indent.builder().withIndent(defaultIndentItem.getIndent())
                        .withIndentType(defaultIndentItem.getIndentType()).build();
            } else {
                appender.indent = Indent.builder().withIndent(this.indent).build();
            }

            appender.newline = Newline.of(appender.indent);

            return appender;
        }
    }

    /**
     * 登録した {@link DdlTokenizer} オブジェクトの現在位置にあるトークンを取得し文字列へ追加します。
     * <p>
     * この {@link DdlAppender#appendToken()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DdlAppender appendToken() {
        this.sql.append(this.ddlTokenizer.getToken());
        return this;
    }

    /**
     * {@link Newline} クラスから改行コードを取得し文字列へ追加します。
     * <p>
     * この {@link DdlAppender#appendNewLine()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DdlAppender appendNewline() {
        this.sql.append(this.newline.create());
        return this;
    }

    /**
     * {@link Indent} クラスをインクリメントします。
     * <p>
     * この {@link DdlAppender#increment()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DdlAppender incrementIndent() {
        this.indent.increment();
        return this;
    }

    /**
     * {@link Indent} クラスをデクリメントします。
     * <p>
     * この {@link DdlAppender#increment()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public DdlAppender decrementIndent() {
        this.indent.decrement();
        return this;
    }

    @Override
    public String toString() {
        return this.sql.toString();
    }
}