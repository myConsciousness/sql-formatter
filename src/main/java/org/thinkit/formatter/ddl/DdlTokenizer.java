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

import java.util.Locale;
import java.util.StringTokenizer;

import org.thinkit.formatter.catalog.ddl.DdlStatement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * SQLにおけるDDLのクエリトークンを管理する処理と状態を定義したクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
final class DdlTokenizer {

    /**
     * トークナイザー
     */
    private StringTokenizer tokenizer;

    /**
     * トークン
     */
    @Getter
    private String token;

    /**
     * 小文字のトークン
     */
    @Getter
    private String lowercaseToken;

    /**
     * デフォルトコンストラクタ
     */
    private DdlTokenizer() {
    }

    /**
     * 引数として渡された {@code sql} を基に {@link DdlTokenizer} クラスの新しいインスタンスを生成します。
     * <p>
     * 以下のDDLクエリをサポートしています。サポート対象外のDDLクエリが渡された場合は {@link IllegalArgumentException}
     * が必ず実行時に発生します。
     *
     * @param sql 処理対象のSQL
     *
     * @throws IllegalArgumentException サポート対象外のDDLクエリが渡された場合
     */
    private DdlTokenizer(@NonNull String sql) {

        final String lowercaseSql = sql.toLowerCase();

        if (lowercaseSql.startsWith(DdlStatement.CREATE_TABLE.getStatement())) {
            this.tokenizer = new StringTokenizer(sql, "(,)'[]\"", true);
        } else if (lowercaseSql.startsWith(DdlStatement.ALTER_TABLE.getStatement())) {
            this.tokenizer = new StringTokenizer(sql, " (,)'[]\"", true);
        } else if (lowercaseSql.startsWith(DdlStatement.COMMENT_ON.getStatement())) {
            this.tokenizer = new StringTokenizer(sql, " '[]\"", true);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported DDL query was given: %s", sql));
        }
    }

    /**
     * {@link DdlTokenizer} クラスのインスタンス生成時に渡した {@code sql}
     * を基に生成されたトークナイザからトークンを取得し、トークナイザの位置をインクリメントします。
     * <p>
     * {@link DdlTokenizer#next()} メソッドを実行した際にトークナイザから取得できるトークンが存在しない場合は
     * {@code false} を返却します。取得できるトークンが存在する場合は {@code true} を返却します。
     * <p>
     * {@link DdlTokenizer#next()} メソッドの実行後は以下の {@code Getter}
     * メソッドを使用することで現在位置のトークンを取得することができます。
     * <p>
     * {@link DdlTokenizer#getToken()} <br>
     * {@link DdlTokenizer#getLowercaseToken()}
     *
     * @return {@link DdlTokenizer#next()} メソッドを実行した際にトークナイザから取得できるトークンが存在しない場合は
     *         {@code false} 、取得できるトークンが存在する場合は {@code true}
     */
    public boolean next() {

        if (!this.tokenizer.hasMoreTokens()) {
            return false;
        }

        this.token = this.tokenizer.nextToken();
        this.lowercaseToken = this.token.toLowerCase(Locale.ROOT);

        return true;
    }
}