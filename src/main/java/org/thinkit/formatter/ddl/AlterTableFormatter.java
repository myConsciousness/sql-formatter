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

import org.thinkit.common.catalog.Delimiter;
import org.thinkit.common.exception.IllegalNumberFoundException;
import org.thinkit.formatter.catalog.ddl.DdlStatement;
import org.thinkit.formatter.catalog.ddl.StartClause;
import org.thinkit.formatter.common.Formatter;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * DDL命令におけるALTER TABLE文を整形する処理を定義したフォーマッタークラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
final class AlterTableFormatter implements Formatter {

    /**
     * インデント数
     */
    private int indent;

    /**
     * デフォルトコンストラクタ
     */
    private AlterTableFormatter() {
        this.indent = 4;
    }

    /**
     * コンストラクタ
     *
     * @param indent インデント数
     */
    private AlterTableFormatter(int indent) {
        this.indent = indent;
    }

    /**
     * {@link AlterTableFormatter} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link AlterTableFormatter} クラスの新しいインスタンス
     */
    public static Formatter of() {
        return new AlterTableFormatter();
    }

    /**
     * 引数として指定された {@code indent} の数値に応じた {@link AlterTableFormatter}
     * クラスの新しいインスタンスを生成し返却します。
     *
     * @param indent インデント数
     * @return {@code indent} の数値に応じた {@link AlterTableFormatter} クラスの新しいインスタンス
     *
     * @throws IllegalNumberFoundException 引数として指定された {@code indent} の数値が負数の場合
     */
    public static Formatter withIndent(int indent) {
        return new AlterTableFormatter(indent);
    }

    @Override
    public String format(@NonNull final String sql) {
        final DdlTokenizer tokenizer = DdlTokenizer.of(sql);
        final DdlAppender appender = DdlAppender.builder().register(tokenizer).withIndent(this.indent).build();

        while (tokenizer.next()) {

            if (tokenizer.isQuote()) {
                appender.appendToken();

                while (tokenizer.next()) {
                    appender.appendToken();
                    if (tokenizer.isQuote()) {
                        break;
                    }
                }
            } else if (tokenizer.isBreak()) {

                if (!StartClause.COLUMN.getClause().equals(tokenizer.getLowercaseToken())) {
                    appender.appendNewline();
                }

                appender.appendToken();

                if (!StartClause.RENAME.getClause().equals(tokenizer.getLowercaseToken())
                        && !DdlStatement.DROP.getStatement().equals(tokenizer.getLowercaseToken())) {
                    appender.incrementIndent().appendNewline().decrementIndent();
                }
            } else {
                if (Delimiter.semicolon().equals(tokenizer.getToken())) {
                    appender.appendNewline();
                }

                appender.appendToken();
            }
        }

        return appender.toString();
    }
}