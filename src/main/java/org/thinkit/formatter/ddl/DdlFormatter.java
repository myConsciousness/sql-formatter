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
import org.thinkit.formatter.catalog.ddl.DdlStatement;
import org.thinkit.formatter.common.Formatter;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * SQLにおけるDDLクエリを整形する処理を定義したフォーマッタクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
public final class DdlFormatter implements Formatter {

    /**
     * インデント数
     */
    private int indent;

    /**
     * デフォルトコンストラクタ
     */
    private DdlFormatter() {
        this.indent = 4;
    }

    /**
     * コンストラクタ
     *
     * @param indent インデント数
     *
     * @throws IllegalNumberFoundException 引数として指定された {@code indent} の数値が負数の場合
     */
    private DdlFormatter(int indent) {
        Precondition.requirePositive(indent);
        this.indent = indent;
    }

    /**
     * {@link DdlFormatter} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link DdlFormatter} クラスの新しいインスタンス
     */
    public static Formatter of() {
        return new DdlFormatter();
    }

    /**
     * 引数として指定された {@code indent} の数値に応じた {@link DdlFormatter}
     * クラスの新しいインスタンスを生成し返却します。
     *
     * @param indent インデント数
     * @return {@code indent} の数値に応じた {@link DdlFormatter} クラスの新しいインスタンス
     *
     * @throws IllegalNumberFoundException 引数として指定された {@code indent} の数値が負数の場合
     */
    public static Formatter withIndent(int indent) {
        return new DdlFormatter(indent);
    }

    @Override
    public String format(@NonNull final String sql) {

        final String trimmedSql = sql.trim();
        final String lowercaseSql = sql.toLowerCase();

        if (lowercaseSql.startsWith(DdlStatement.CREATE_TABLE.getStatement())) {
            return CreateTableFormatter.withIndent(this.indent).format(sql);
        } else if (lowercaseSql.startsWith(DdlStatement.ALTER_TABLE.getStatement())) {
            return AlterTableFormatter.withIndent(this.indent).format(sql);
        } else if (lowercaseSql.startsWith(DdlStatement.COMMENT_ON.getStatement())) {
            return CommentOnFormatter.withIndent(this.indent).format(sql);
        }

        return trimmedSql;
    }
}