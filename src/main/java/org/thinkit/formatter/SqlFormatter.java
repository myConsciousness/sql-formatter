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

import java.util.LinkedList;

import org.thinkit.common.catalog.Delimiter;
import org.thinkit.formatter.catalog.DmlStatement;
import org.thinkit.formatter.catalog.EndClause;
import org.thinkit.formatter.catalog.LogicalExpression;
import org.thinkit.formatter.catalog.MiscStatement;
import org.thinkit.formatter.catalog.Quantifier;
import org.thinkit.formatter.catalog.StartClause;
import org.thinkit.formatter.common.Formatter;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * SQLにおけるDMLクエリを整形する処理を定義したフォーマッタクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
public class SqlFormatter implements Formatter {

    /**
     * 空白
     */
    private static final String WHITESPACES = " \n\r\f\t";

    boolean afterByOrSetOrFromOrSelect;
    private LinkedList<Boolean> afterByOrFromOrSelects = new LinkedList<>();

    /**
     * デフォルトコンストラクタ
     */
    private SqlFormatter() {
    }

    /**
     * {@link SqlFormatter} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link SqlFormatter} クラスの新しいインスタンス
     */
    public static Formatter of() {
        return new SqlFormatter();
    }

    @Override
    public String format(@NonNull final String sql) {

        final StringBuilder formatted = new StringBuilder();

        final DmlIndenter indenter = DmlIndenter.of();
        final FunctionFixer function = FunctionFixer.of();
        final ParenthesisFixer startParenthesis = ParenthesisFixer.of();

        boolean beginLine = false;
        boolean inClauses = false;

        final DmlTokenizer tokenizer = DmlTokenizer.of(sql);
        final DmlAppender appender = DmlAppender.register(tokenizer);

        while (tokenizer.next()) {
            String token = tokenizer.getToken();
            String lowercaseToken = tokenizer.getLowercaseToken();
            String lastToken = tokenizer.getLastToken();

            if (DmlStatement.contains(lowercaseToken)) {

                formatted.append(token);

                if (DmlStatement.SELECT.getStatement().equals(lowercaseToken)) {
                    formatted.append(indenter.increment().newline());
                    startParenthesis.push();
                    afterByOrFromOrSelects.addLast(afterByOrSetOrFromOrSelect);

                    beginLine = true;
                    afterByOrSetOrFromOrSelect = true;
                } else {
                    indenter.increment();
                    beginLine = false;

                    if (DmlStatement.UPDATE.getStatement().equals(lowercaseToken)) {
                        formatted.append(indenter.newline());
                        beginLine = true;
                    }
                }
            } else if (StartClause.contains(lowercaseToken)) {
                if (!inClauses) {
                    if (MiscStatement.ON.getStatement().equals(lastToken)) {
                        indenter.decrement();
                    }

                    formatted.append(indenter.decrement().newline());
                }

                inClauses = true;

                formatted.append(token);
                beginLine = false;

            } else if (EndClause.contains(lowercaseToken)) {
                if (!inClauses) {
                    if (MiscStatement.ON.getStatement().equals(lastToken)) {
                        indenter.decrement();
                    }

                    formatted.append(indenter.decrement().newline());
                }

                if (!EndClause.UNION.getClause().equals(lowercaseToken)) {
                    indenter.increment();
                }

                formatted.append(token);
                formatted.append(indenter.newline());
                beginLine = true;
                inClauses = false;

                afterByOrSetOrFromOrSelect = EndClause.BY.getClause().equals(lowercaseToken)
                        || EndClause.SET.getClause().equals(lowercaseToken)
                        || EndClause.FROM.getClause().equals(lowercaseToken);

            } else if (afterByOrSetOrFromOrSelect && Delimiter.comma().equals(token)) {
                formatted.append(token);
                formatted.append(indenter.newline());
                beginLine = true;
            } else if (MiscStatement.ON.getStatement().equals(lowercaseToken)) {
                formatted.append(indenter.increment().newline());
                formatted.append(token);
                beginLine = false;
            } else if (MiscStatement.ON.getStatement().equals(lastToken) & Delimiter.comma().equals(token)) {
                formatted.append(token);
                formatted.append(indenter.decrement().newline());
                beginLine = true;
                afterByOrSetOrFromOrSelect = true;
            } else if ("(".equals(token)) {

                startParenthesis.increment();

                if (this.isFunctionName(lastToken) || function.isInFunction()) {
                    function.increment();
                }

                if (function.isInFunction()) {
                    formatted.append(token);
                    beginLine = false;
                } else {
                    formatted.append(token);

                    if (!afterByOrSetOrFromOrSelect) {
                        formatted.append(indenter.increment().newline());
                        beginLine = true;
                    }
                }
            } else if (")".equals(token)) {

                startParenthesis.decrement();

                if (startParenthesis.hasParenthesis()) {
                    indenter.decrement();
                    startParenthesis.pop();
                    afterByOrSetOrFromOrSelect = afterByOrFromOrSelects.removeLast();
                }

                if (function.isInFunction()) {
                    function.decrement();
                    formatted.append(token);
                } else {
                    if (!afterByOrSetOrFromOrSelect) {
                        formatted.append(indenter.decrement().newline());
                    }

                    formatted.append(token);
                }

                beginLine = false;

            } else if (EndClause.VALUES.getClause().equals(lowercaseToken)) {
                formatted.append(indenter.decrement().newline());
                formatted.append(token);
                formatted.append(indenter.increment().newline());
                beginLine = true;
            } else if (LogicalExpression.contains(lowercaseToken)
                    && !LogicalExpression.CASE.getExpression().equals(lowercaseToken)) {

                if (LogicalExpression.END.getExpression().equals(lowercaseToken)) {
                    indenter.decrement();
                }

                formatted.append(indenter.newline());
                formatted.append(token);

                beginLine = false;
            } else if (Quantifier.BETWEEN.getQuantifier().equals(lastToken)
                    && LogicalExpression.AND.getExpression().equals(lowercaseToken)) {

                formatted.append(token);
                beginLine = false;

            } else if (this.isWhitespace(token)) {
                if (!beginLine) {
                    formatted.append(token);
                }
            } else {
                formatted.append(token);

                if (DmlStatement.INSERT.getStatement().equals(lastToken)) {
                    formatted.append(indenter.newline());
                    beginLine = true;
                } else {
                    beginLine = false;
                    if (LogicalExpression.CASE.getExpression().equals(lowercaseToken)) {
                        indenter.increment();
                    }
                }
            }
        }

        return formatted.toString();
    }

    private boolean isFunctionName(@NonNull String token) {

        final char begin = token.charAt(0);
        final boolean isIdentifier = Character.isJavaIdentifierStart(begin) || '"' == begin;

        return isIdentifier && !LogicalExpression.contains(token) && !EndClause.contains(token)
                && !Quantifier.contains(token) && !DmlStatement.contains(token) && !MiscStatement.contains(token);
    }

    private boolean isWhitespace(@NonNull String token) {
        return WHITESPACES.contains(token);
    }
}