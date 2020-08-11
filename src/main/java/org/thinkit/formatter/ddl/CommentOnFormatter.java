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
import org.thinkit.common.exception.IllegalNumberFoundException;
import org.thinkit.formatter.catalog.ddl.Clause;
import org.thinkit.formatter.catalog.ddl.LogicalExpression;
import org.thinkit.formatter.common.Formatter;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * DDL命令におけるCOMMENT ON文を整形する処理を定義したフォーマッタークラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
final class CommentOnFormatter implements Formatter {

    /**
     * 空白
     */
    private static final String WHITESPACES = " \n\r\f\t";

    /**
     * インデント数
     */
    private int indent;

    /**
     * デフォルトコンストラクタ
     */
    private CommentOnFormatter() {
        this.indent = 4;
    }

    /**
     * コンストラクタ
     *
     * @param indent インデント数
     *
     * @throws IllegalNumberFoundException 引数として指定された {@code indent} の数値が負数の場合
     */
    private CommentOnFormatter(int indent) {
        Precondition.requirePositive(indent);
        this.indent = indent;
    }

    /**
     * {@link CommentOnFormatter} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link CommentOnFormatter} クラスの新しいインスタンス
     */
    public static Formatter of() {
        return new CommentOnFormatter();
    }

    /**
     * 引数として指定された {@code indent} の数値に応じた {@link CommentOnFormatter}
     * クラスの新しいインスタンスを生成し返却します。
     *
     * @param indent インデント数
     * @return {@code indent} の数値に応じた {@link CommentOnFormatter} クラスの新しいインスタンス
     *
     * @throws IllegalNumberFoundException 引数として指定された {@code indent} の数値が負数の場合
     */
    public static Formatter withIndent(int indent) {
        return new CommentOnFormatter(indent);
    }

    @Override
    public String format(@NonNull final String sql) {

        final DdlTokenizer tokenizer = DdlTokenizer.of(sql);
        final DdlAppender appender = DdlAppender.builder().register(tokenizer).withIndent(this.indent).build();

        boolean startLine = false;
        boolean newline = false;

        while (tokenizer.next()) {

            if (Clause.COLUMN.getClause().equals(tokenizer.getLowercaseToken())
                    || LogicalExpression.IS.getExpression().equals(tokenizer.getLowercaseToken())) {
                appender.appendNewline().appendToken();
                startLine = true;
                newline = true;
            } else if (tokenizer.isQuote()) {

                appender.incrementIndent().appendNewline().appendToken().decrementIndent();

                while (tokenizer.next()) {
                    appender.appendToken();

                    if (tokenizer.isQuote()) {
                        appender.decrementIndent();
                        break;
                    }
                }
            } else if (this.isWhitespace(tokenizer.getToken())) {
                if (!startLine) {
                    appender.appendToken();
                }
            } else {
                if (newline) {
                    appender.incrementIndent().appendNewline().decrementIndent();
                    newline = false;
                }

                appender.appendToken();
                startLine = false;
            }
        }

        return appender.toString();
    }

    /**
     * 引数として渡された {@code token} の文字列が空白であるか判定します。
     *
     * @param token 判定対象のトークン
     * @return {@code token} の文字列が空白である場合は {@code true} 、それ以外は {@code false}
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private boolean isWhitespace(@NonNull String token) {
        return WHITESPACES.contains(token);
    }
}