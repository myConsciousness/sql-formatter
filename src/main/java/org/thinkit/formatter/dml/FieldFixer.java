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

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * フィールドを管理する処理や状態を定義したクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
final class FieldFixer {

    /**
     * 改行可否のデック
     */
    private Deque<Boolean> deque;

    /**
     * 改行可否
     */
    @Getter
    private boolean newline;

    /**
     * 開始ライン
     */
    @Getter
    private boolean startLine;

    /**
     * デフォルトコンストラクタ
     */
    private FieldFixer() {
        this.deque = new ArrayDeque<>();
        this.newline = false;
        this.startLine = false;
    }

    /**
     * {@link FieldFixer} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link FieldFixer} クラスの新しいインスタンス
     */
    public static FieldFixer of() {
        return new FieldFixer();
    }

    /**
     * 現在の改行可否を {@code Deque} 構造の配列へ追加します。 現在の改行可否は {@link FieldFixer#push()}
     * メソッドの呼び出し時に {@code false} へ初期化されます。
     * <p>
     * {@link FieldFixer#push()} メソッドは自分自身のインスタンスを返却するため、メソッドチェーンの形式で後続処理を行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public FieldFixer push() {
        this.deque.push(this.newline);
        this.newline = false;
        return this;
    }

    /**
     * {@link FieldFixer#push()} メソッドで {@code Deque} 構造の配列へ追加された改行可否を最後尾から取り出します。
     * {@link FieldFixer#pop()} メソッドで取り出された改行可否は現在の改行可否に上書きされます。
     * <p>
     * {@link FieldFixer#pop()} メソッドは自分自身のインスタンスを返却するため、メソッドチェーンの形式で後続処理を行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public FieldFixer pop() {
        this.newline = this.deque.pop();
        return this;
    }

    /**
     * 改行可否を {@code true} で上書きします。
     * <p>
     * {@link FieldFixer#toNewline()}
     * メソッドは自分自身のインスタンスを返却するため、メソッドチェーンの形式で後続処理を行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public FieldFixer toNewline() {
        this.newline = true;
        return this;
    }

    /**
     * 改行可否を {@code false} で上書きします。
     * <p>
     * {@link FieldFixer#toNotNewline()}
     * メソッドは自分自身のインスタンスを返却するため、メソッドチェーンの形式で後続処理を行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public FieldFixer toNotNewline() {
        this.newline = false;
        return this;
    }

    /**
     * 開始ライン可否を {@code true} へ上書きします。
     * <p>
     * この {@link FieldFixer#toStartLine()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public FieldFixer toStartLine() {
        this.startLine = true;
        return this;
    }

    /**
     * 開始ライン可否を {@code false} へ上書きします。
     * <p>
     * この {@link FieldFixer#toNotStartLinenLine()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public FieldFixer toNotStartLine() {
        this.startLine = false;
        return this;
    }
}