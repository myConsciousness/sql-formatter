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

import java.util.Locale;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.thinkit.api.catalog.BiCatalog;
import org.thinkit.formatter.common.Tokenizable;
import org.thinkit.formatter.common.catalog.Whitespace;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * SQLにおけるDMLのクエリトークンを管理する処理と状態を定義したクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
final class DmlTokenizer implements Tokenizable {

    /**
     * 区切り文字
     */
    private static final String TOKEN_DELIMITER = ";()+*/-=<>'`\"[]," + getWhitespaces();

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
     * 最後に取得した空白以外のトークン
     */
    @Getter
    private String lastToken;

    /**
     * デフォルトコンストラクタ
     */
    private DmlTokenizer() {
    }

    private static String getWhitespaces() {
        return BiCatalog.stream(Whitespace.class).collect(Collectors.toList()).toString();
    }

    /**
     * コンストラクタ
     *
     * @param sql SQL
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private DmlTokenizer(@NonNull String sql) {
        this.tokenizer = new StringTokenizer(sql.trim(), TOKEN_DELIMITER, true);
    }

    /**
     * 引数として渡された {@code sql} に格納された文字列を基に {@link DmlTokenizer}
     * クラスの新しいインスタンスを生成し返却します。
     *
     * @param sql 処理対象のSQL
     * @return {@link DmlTokenizer} クラスの新しいインスタンス
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    public static Tokenizable of(@NonNull String sql) {
        return new DmlTokenizer(sql);
    }

    @Override
    public boolean next() {

        if (!this.tokenizer.hasMoreTokens()) {
            return false;
        }

        final String token = this.tokenizer.nextToken();
        final StringBuilder sb = new StringBuilder(token);

        if ("'".equals(token) || "\"".equals(token) || "[".equals(token)) {

            final String closeSymbol = "'".equals(token) || "\"".equals(token) ? token : "]";

            while (this.tokenizer.hasMoreTokens()) {
                String symbol = this.tokenizer.nextToken();
                sb.append(symbol);

                if (closeSymbol.equals(symbol)) {
                    break;
                }
            }
        }

        this.token = sb.toString();
        this.lowercaseToken = token.toLowerCase(Locale.ROOT);

        if (!this.isWhitespace(this.lowercaseToken)) {
            this.lastToken = this.lowercaseToken;
        }

        return true;
    }
}