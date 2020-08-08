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
        final FieldFixer field = FieldFixer.of();
        final ParenthesisFixer startParenthesis = ParenthesisFixer.of();

        boolean inClauses = false;

        final DmlTokenizer tokenizer = DmlTokenizer.of(sql);
        final DmlAppender appender = DmlAppender.register(tokenizer);

        while (tokenizer.next()) {
            String token = tokenizer.getToken();
            String lowercaseToken = tokenizer.getLowercaseToken();
            String lastToken = tokenizer.getLastToken();

            if (DmlStatement.contains(lowercaseToken)) {
                this.dmlStatement(appender, tokenizer, startParenthesis, field);
            } else if (StartClause.contains(lowercaseToken)) {
                this.startClause(appender, tokenizer, inClauses);
                inClauses = true;
            } else if (EndClause.contains(lowercaseToken)) {
                this.endClause(appender, tokenizer, field, inClauses);
                inClauses = false;
            } else if (field.isNewline() && Delimiter.comma().equals(token)) {
                appender.toBeginLine().appendToken().appendNewLine();
            } else if (MiscStatement.ON.getStatement().equals(lastToken) & Delimiter.comma().equals(token)) {
                this.afterOnStatement(appender, field);
            } else if (MiscStatement.ON.getStatement().equals(lowercaseToken)) {
                this.onStatement(appender);
            } else if ("(".equals(token)) {
                this.startParenthesis(appender, tokenizer, function, field, startParenthesis);
            } else if (")".equals(token)) {

                startParenthesis.decrement();

                if (startParenthesis.hasParenthesis()) {
                    appender.decrement();
                    startParenthesis.pop();
                    field.pop();
                }

                if (function.isInFunction()) {
                    appender.decrement().appendToken();
                } else {
                    if (!field.isNewline()) {
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

    private void dmlStatement(@NonNull DmlAppender appender, @NonNull DmlTokenizer tokenizer,
            @NonNull ParenthesisFixer startParenthesis, @NonNull FieldFixer field) {

        appender.appendToken();

        if (DmlStatement.SELECT.getStatement().equals(tokenizer.getLowercaseToken())) {
            appender.toBeginLine().increment().appendNewLine();
            startParenthesis.push();
            field.push().toNewline();
        } else {
            appender.toNotBeginLine().increment();

            if (DmlStatement.UPDATE.getStatement().equals(tokenizer.getLowercaseToken())) {
                appender.toBeginLine().appendNewLine();
            }
        }
    }

    private void startClause(@NonNull DmlAppender appender, @NonNull DmlTokenizer tokenizer, boolean inClauses) {

        if (!inClauses) {
            if (MiscStatement.ON.getStatement().equals(tokenizer.getLastToken())) {
                appender.decrement();
            }

            appender.decrement().appendNewLine();
        }

        appender.toNotBeginLine().appendToken();
    }

    private void endClause(@NonNull DmlAppender appender, @NonNull DmlTokenizer tokenizer, @NonNull FieldFixer field,
            boolean inClauses) {

        if (!inClauses) {
            if (MiscStatement.ON.getStatement().equals(tokenizer.getLastToken())) {
                appender.decrement();
            }

            appender.decrement().appendNewLine();
        }

        final String lowercaseToken = tokenizer.getLowercaseToken();

        if (!EndClause.UNION.getClause().equals(lowercaseToken)) {
            appender.increment();
        }

        appender.toBeginLine().appendToken().appendNewLine();

        if (EndClause.BY.getClause().equals(lowercaseToken) || EndClause.SET.getClause().equals(lowercaseToken)
                || EndClause.FROM.getClause().equals(lowercaseToken)) {
            field.toNewline();
        } else {
            field.toNotNewline();
        }
    }

    private void onStatement(@NonNull DmlAppender appender) {
        appender.toNotBeginLine().increment().appendNewLine().appendToken();
    }

    private void afterOnStatement(@NonNull DmlAppender appender, @NonNull FieldFixer field) {
        appender.toBeginLine().appendToken().decrement().appendNewLine();
        field.toNewline();
    }

    private void startParenthesis(@NonNull DmlAppender appender, @NonNull DmlTokenizer tokenizer,
            @NonNull FunctionFixer function, @NonNull FieldFixer field, @NonNull ParenthesisFixer startParenthesis) {

        startParenthesis.increment();

        if (this.isFunctionName(tokenizer.getLastToken()) || function.isInFunction()) {
            function.increment();
        }

        if (function.isInFunction()) {
            appender.toNotBeginLine().appendToken();
        } else {
            appender.appendToken();

            if (!field.isNewline()) {
                appender.toBeginLine().increment().appendNewLine();
            }
        }
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