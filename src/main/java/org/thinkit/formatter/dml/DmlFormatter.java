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

import org.thinkit.common.catalog.Delimiter;
import org.thinkit.common.catalog.Parenthesis;
import org.thinkit.formatter.catalog.EndClause;
import org.thinkit.formatter.catalog.LogicalExpression;
import org.thinkit.formatter.catalog.MiscStatement;
import org.thinkit.formatter.catalog.Quantifier;
import org.thinkit.formatter.catalog.StartClause;
import org.thinkit.formatter.catalog.dml.DmlStatement;
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
public final class DmlFormatter implements Formatter {

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
    private DmlFormatter() {
        indent = 4;
    }

    /**
     * コンストラクタ
     *
     * @param indent インデント数
     *
     * @throws IllegalArgumentException 引数として渡された {@code indent} の数値が負数の場合
     */
    private DmlFormatter(int indent) {

        if (indent < 0) {
            throw new IllegalArgumentException("Indent must be positive.");
        }

        this.indent = indent;
    }

    /**
     * {@link SqlFormatter} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link SqlFormatter} クラスの新しいインスタンス
     */
    public static Formatter of() {
        return new DmlFormatter();
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
        return new DmlFormatter(indent);
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
                this.fieldItem(appender);
            } else if (MiscStatement.ON.getStatement().equals(lastToken) & Delimiter.comma().equals(token)) {
                this.afterOnStatement(appender, field);
            } else if (MiscStatement.ON.getStatement().equals(lowercaseToken)) {
                this.onStatement(appender);
            } else if (Parenthesis.start().equals(token)) {
                this.startParenthesis(appender, tokenizer, function, field, startParenthesis);
            } else if (Parenthesis.end().equals(token)) {
                this.endParenthesis(appender, function, field, startParenthesis);
            } else if (EndClause.VALUES.getClause().equals(lowercaseToken)) {
                this.valuesClause(appender);
            } else if (LogicalExpression.contains(lowercaseToken)
                    && !LogicalExpression.CASE.getExpression().equals(lowercaseToken)) {
                this.logicalExceptCase(appender, tokenizer);
            } else if (Quantifier.BETWEEN.getQuantifier().equals(lastToken)
                    && LogicalExpression.AND.getExpression().equals(lowercaseToken)) {
                this.logicalAfterBetween(appender);
            } else if (this.isWhitespace(token)) {
                this.whitespace(appender);
            } else {
                this.otherStatements(appender, tokenizer);
            }
        }

        return appender.toString();
    }

    /**
     * トークンがDML命令の場合の処理を定義したメソッドです。
     *
     * @param appender         DML命令のアペンダー
     * @param tokenizer        DML命令のトークナイザ
     * @param startParenthesis 括弧の調整オブジェクト
     * @param field            フィールドの調整オブジェクト
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
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

    /**
     * トークンが開始句の場合の処理を定義したメソッドです。
     *
     * @param appender  DML命令のアペンダー
     * @param tokenizer DML命令のトークナイザー
     * @param inClauses 開始句以降かつ終了句までにあるトークンかの可否。トークンの登場位置が {@link StartClause}
     *                  クラスに定義されている要素の後で、かつ {@link EndClause} クラスに定義された要素よりも前の場合は
     *                  {@code true} を指定し、 そうでない場合は {@code false} を指定する。
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void startClause(@NonNull DmlAppender appender, @NonNull DmlTokenizer tokenizer, boolean inClauses) {

        if (!inClauses) {
            if (MiscStatement.ON.getStatement().equals(tokenizer.getLastToken())) {
                appender.decrement();
            }

            appender.decrement().appendNewLine();
        }

        appender.toNotBeginLine().appendToken();
    }

    /**
     * トークンが終了句の場合の処理を定義したメソッドです。
     *
     * @param appender  DML命令のアペンダー
     * @param tokenizer DML命令のトークナイザー
     * @param field     フィールドの調整オブジェクト
     * @param inClauses 開始句以降かつ終了句までにあるトークンかの可否。トークンの登場位置が {@link StartClause}
     *                  クラスに定義されている要素の後で、かつ {@link EndClause} クラスに定義された要素よりも前の場合は
     *                  {@code true} を指定し、 そうでない場合は {@code false} を指定する。
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
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

    /**
     * トークンが {@code "on"} 句の場合の処理を定義したメソッドです。
     *
     * @param appender DMLのアペンダー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void onStatement(@NonNull DmlAppender appender) {
        appender.toNotBeginLine().increment().appendNewLine().appendToken();
    }

    /**
     * トークンの登場位置が {@code "on"} 句以降の場合の処理を定義したメソッドです。
     *
     * @param appender DMLのアペンダー
     * @param field    フィールドの調整オブジェクト
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void afterOnStatement(@NonNull DmlAppender appender, @NonNull FieldFixer field) {
        appender.toBeginLine().appendToken().decrement().appendNewLine();
        field.toNewline();
    }

    /**
     * トークンが開始括弧の場合の処理を定義したメソッドです。
     *
     * @param appender         DMLのアペンダー
     * @param tokenizer        DMLのトークナイザー
     * @param function         関数の調整オブジェクト
     * @param field            フィールドの調整オブジェクト
     * @param startParenthesis 括弧の調整オブジェクト
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void startParenthesis(@NonNull DmlAppender appender, @NonNull DmlTokenizer tokenizer,
            @NonNull FunctionFixer function, @NonNull FieldFixer field, @NonNull ParenthesisFixer startParenthesis) {

        startParenthesis.increment();

        if (this.isFunction(tokenizer.getLastToken()) || function.isInFunction()) {
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

    /**
     * トークンが終了括弧の場合の処理を定義したメソッドです。
     *
     * @param appender         DMLのアペンダー
     * @param function         関数の調整オブジェクト
     * @param field            フィールドの調整オブジェクト
     * @param startParenthesis 括弧の調整オブジェクト
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void endParenthesis(@NonNull DmlAppender appender, @NonNull FunctionFixer function,
            @NonNull FieldFixer field, @NonNull ParenthesisFixer startParenthesis) {

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
    }

    /**
     * トークンが {@code "values"} 句の場合の処理を定義したメソッドです。
     *
     * @param appender DMLアペンダー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void valuesClause(@NonNull DmlAppender appender) {
        appender.decrement().appendNewLine();
        appender.appendToken();
        appender.increment().appendNewLine();
        appender.toBeginLine();
    }

    /**
     * トークンが {@code "case"} 以外の論理式である場合の処理を定義したメソッドです。
     *
     * @param appender  DMLのアペンダー
     * @param tokenizer DMLのトークナイザー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void logicalExceptCase(@NonNull DmlAppender appender, @NonNull DmlTokenizer tokenizer) {

        if (LogicalExpression.END.getExpression().equals(tokenizer.getLowercaseToken())) {
            appender.decrement();
        }

        appender.toNotBeginLine().appendNewLine().appendToken();
    }

    /**
     * トークンの登場位置が {@code "between"} 句よりも後である場合の処理を定義したメソッドです。
     *
     * @param appender DMLのアペンダー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void logicalAfterBetween(@NonNull DmlAppender appender) {
        appender.toNotBeginLine().appendToken();
    }

    /**
     * トークンが空白である場合の処理を定義したメソッドです。
     *
     * @param appender DMLのアペンダー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void whitespace(@NonNull DmlAppender appender) {
        if (!appender.isBeginLine()) {
            appender.appendToken();
        }
    }

    /**
     * トークンがその他のステートメントであった場合の所為を定義したメソッドです。
     *
     * @param appender  DMLのアペンダー
     * @param tokenizer DMLのトークナイザー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void otherStatements(@NonNull DmlAppender appender, @NonNull DmlTokenizer tokenizer) {

        appender.appendToken();

        if (DmlStatement.INSERT.getStatement().equals(tokenizer.getLastToken())) {
            appender.toBeginLine().appendNewLine();
        } else {
            appender.toNotBeginLine();
            if (LogicalExpression.CASE.getExpression().equals(tokenizer.getLowercaseToken())) {
                appender.increment();
            }
        }
    }

    /**
     * {@code ","} までのフィールド項目に対する処理を定義したメソッドです。
     *
     * @param appender DMLのアペンダー
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private void fieldItem(@NonNull DmlAppender appender) {
        appender.toBeginLine().appendToken().appendNewLine();
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

    /**
     * 引数として渡された {@code token} の文字列が関数名であるか判定します。
     *
     * @param token 判定対象のトークン
     * @return {@code token} の文字列が関数名である場合は {@code true} 、それ以外は {@code false}
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private boolean isFunction(@NonNull String token) {

        final char start = token.charAt(0);
        final boolean isIdentifier = Character.isJavaIdentifierStart(start) || '"' == start;

        return isIdentifier && !LogicalExpression.contains(token) && !EndClause.contains(token)
                && !Quantifier.contains(token) && !DmlStatement.contains(token) && !MiscStatement.contains(token);
    }
}