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

        final FunctionFixer function = FunctionFixer.of();
        final ParenthesisFixer startParenthesis = ParenthesisFixer.of();

        boolean inClauses = false;

        final DmlTokenizer tokenizer = DmlTokenizer.of(sql);
        final DmlAppender appender = DmlAppender.register(tokenizer);

        while (tokenizer.next()) {
            String token = tokenizer.getToken();
            String lowercaseToken = tokenizer.getLowercaseToken();
            String lastToken = tokenizer.getLastToken();

            if (DmlStatement.contains(lowercaseToken)) {

                appender.appendToken();

                if (DmlStatement.SELECT.getStatement().equals(lowercaseToken)) {
                    appender.toBeginLine().increment().appendNewLine();
                    startParenthesis.push();
                    afterByOrFromOrSelects.addLast(afterByOrSetOrFromOrSelect);
                    afterByOrSetOrFromOrSelect = true;
                } else {
                    appender.toNotBeginLine().increment();

                    if (DmlStatement.UPDATE.getStatement().equals(lowercaseToken)) {
                        appender.toBeginLine().appendNewLine();
                    }
                }
            } else if (StartClause.contains(lowercaseToken)) {
                if (!inClauses) {
                    if (MiscStatement.ON.getStatement().equals(lastToken)) {
                        appender.decrement();
                    }

                    appender.decrement().appendNewLine();
                }

                inClauses = true;
                appender.toNotBeginLine().appendToken();

            } else if (EndClause.contains(lowercaseToken)) {
                if (!inClauses) {
                    if (MiscStatement.ON.getStatement().equals(lastToken)) {
                        appender.decrement();
                    }

                    appender.decrement().appendNewLine();
                }

                if (!EndClause.UNION.getClause().equals(lowercaseToken)) {
                    appender.increment();
                }

                appender.toBeginLine().appendToken().appendNewLine();
                inClauses = false;

                afterByOrSetOrFromOrSelect = EndClause.BY.getClause().equals(lowercaseToken)
                        || EndClause.SET.getClause().equals(lowercaseToken)
                        || EndClause.FROM.getClause().equals(lowercaseToken);

            } else if (afterByOrSetOrFromOrSelect && Delimiter.comma().equals(token)) {
                appender.toBeginLine().appendToken().appendNewLine();
            } else if (MiscStatement.ON.getStatement().equals(lowercaseToken)) {
                appender.toNotBeginLine().increment().appendNewLine().appendToken();
            } else if (MiscStatement.ON.getStatement().equals(lastToken) & Delimiter.comma().equals(token)) {
                appender.toBeginLine().appendToken().decrement().appendNewLine();
                afterByOrSetOrFromOrSelect = true;
            } else if ("(".equals(token)) {

                startParenthesis.increment();

                if (this.isFunctionName(lastToken) || function.isInFunction()) {
                    function.increment();
                }

                if (function.isInFunction()) {
                    appender.toNotBeginLine().appendToken();
                } else {
                    appender.appendToken();

                    if (!afterByOrSetOrFromOrSelect) {
                        appender.toBeginLine().increment().appendNewLine();
                    }
                }
            } else if (")".equals(token)) {

                startParenthesis.decrement();

                if (startParenthesis.hasParenthesis()) {
                    appender.decrement();
                    startParenthesis.pop();
                    afterByOrSetOrFromOrSelect = afterByOrFromOrSelects.removeLast();
                }

                if (function.isInFunction()) {
                    appender.decrement().appendToken();
                } else {
                    if (!afterByOrSetOrFromOrSelect) {
                        appender.decrement().appendNewLine();
                    }

                    appender.appendToken();
                }

                appender.toNotBeginLine();

            } else if (EndClause.VALUES.getClause().equals(lowercaseToken)) {
                appender.decrement().appendNewLine();
                appender.appendToken();
                appender.increment().appendNewLine();
                appender.toBeginLine();
            } else if (LogicalExpression.contains(lowercaseToken)
                    && !LogicalExpression.CASE.getExpression().equals(lowercaseToken)) {

                if (LogicalExpression.END.getExpression().equals(lowercaseToken)) {
                    appender.decrement();
                }

                appender.toNotBeginLine().appendNewLine().appendToken();

            } else if (Quantifier.BETWEEN.getQuantifier().equals(lastToken)
                    && LogicalExpression.AND.getExpression().equals(lowercaseToken)) {

                appender.toNotBeginLine().appendToken();

            } else if (this.isWhitespace(token)) {
                if (!appender.isBeginLine()) {
                    appender.appendToken();
                }
            } else {
                appender.appendToken();

                if (DmlStatement.INSERT.getStatement().equals(lastToken)) {
                    appender.toBeginLine().appendNewLine();
                } else {
                    appender.toNotBeginLine();
                    if (LogicalExpression.CASE.getExpression().equals(lowercaseToken)) {
                        appender.increment();
                    }
                }
            }
        }

        return appender.toString();
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