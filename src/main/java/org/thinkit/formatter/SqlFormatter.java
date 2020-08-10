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

package org.thinkit.formatter;

import org.thinkit.common.Precondition;
import org.thinkit.formatter.catalog.dml.DmlStatement;
import org.thinkit.formatter.common.Formatter;
import org.thinkit.formatter.dml.DmlFormatter;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * SQLクエリを整形する処理を定義したフォーマッタクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
public class SqlFormatter implements Formatter {

    /**
     * インデント数
     */
    private int indent;

    /**
     * デフォルトコンストラクタ
     */
    private SqlFormatter() {
        indent = 4;
    }

    /**
     * コンストラクタ
     *
     * @param indent インデント数
     *
     * @throws IllegalArgumentException 引数として渡された {@code indent} の数値が負数の場合
     */
    private SqlFormatter(int indent) {
        Precondition.requirePositive(indent);
        this.indent = indent;
    }

    /**
     * {@link SqlFormatter} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link SqlFormatter} クラスの新しいインスタンス
     */
    public static Formatter of() {
        return new SqlFormatter();
    }

    /**
     * 引数として指定されたインデント数に応じた {@link SqlFormatter} クラスの新しいインスタンスを生成し返却します。
     *
     * @param indent インデント数
     * @return インデント数に応じた {@link SqlFormatter} クラスの新しいインスタンス
     *
     * @throws IllegalArgumentException 引数として渡された {@code indent} の数値が負数の場合
     */
    public static Formatter withIndent(int indent) {
        return new SqlFormatter(indent);
    }

    @Override
    public String format(@NonNull final String sql) {

        final String trimmedSql = sql.trim().toLowerCase();

        if (trimmedSql.startsWith(DmlStatement.SELECT.getStatement())
                || trimmedSql.startsWith(DmlStatement.INSERT.getStatement())
                || trimmedSql.startsWith(DmlStatement.UPDATE.getStatement())
                || trimmedSql.startsWith(DmlStatement.DELETE.getStatement())) {
            return DmlFormatter.withIndent(this.indent).format(sql);
        }

        return "";
    }
}