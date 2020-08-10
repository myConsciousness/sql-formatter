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
     * デフォルトコンストラクタ
     */
    private DdlAppender() {
    }

    /**
     * コンストラクタ
     *
     * @param ddlTokenizer DDL命令のトークナイザー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private DdlAppender(@NonNull DdlTokenizer ddlTokenizer) {
        this.sql = new StringBuilder();
        this.ddlTokenizer = ddlTokenizer;
    }

    /**
     * {@link DdlAppender} クラスと連動する {@code ddlTokenizer} オブジェクトを登録して
     * {@link DdlAppender} クラスの新しいインスタンスを生成し返却します。
     *
     * @param ddlTokenizer {@link DdlAppender} クラスと連動する {@code ddlTokenizer} オブジェクト
     * @return {@link DdlAppender} クラスの新しいインスタンス
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    public static DdlAppender register(@NonNull DdlTokenizer ddlTokenizer) {
        return new DdlAppender(ddlTokenizer);
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

    @Override
    public String toString() {
        return this.ddlTokenizer.toString();
    }
}