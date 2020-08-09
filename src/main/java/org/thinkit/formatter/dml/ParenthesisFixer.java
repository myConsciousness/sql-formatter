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
import lombok.ToString;

/**
 * 括弧を管理する処理や状態を定義したクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
final class ParenthesisFixer {

    /**
     * 括弧数のデック
     */
    private Deque<Integer> deque;

    /**
     * 括弧数
     */
    private int count;

    /**
     * デフォルトコンストラクタ
     */
    private ParenthesisFixer() {
        this.deque = new ArrayDeque<>();
        this.count = 0;
    }

    /**
     * {@link ParenthesisFixer} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link ParenthesisFixer} クラスの新しいインスタンス
     */
    public static ParenthesisFixer of() {
        return new ParenthesisFixer();
    }

    /**
     * 括弧数をインクリメントします。
     * <p>
     * この {@link ParenthesisFixer#increment()}
     * メソッドは自分自身のインスタンスを返却するため後続処理をメソッドチェーンの形式で実行することができます。
     *
     * @return 自分自身のインスタンス
     */
    public ParenthesisFixer increment() {
        this.count++;
        return this;
    }

    /**
     * 括弧数をデクリメントします。
     * <p>
     * この {@link ParenthesisFixer#decrement()}
     * メソッドは自分自身のインスタンスを返却するため後続処理をメソッドチェーンの形式で実行することができます。
     *
     * @return 自分自身のインスタンス
     */
    public ParenthesisFixer decrement() {
        this.count--;
        return this;
    }

    /**
     * 括弧数が {@code 0} よりも大きいか判定します。
     *
     * @return 括弧数が {@code 0} よりも大きい場合は {@code true} 、それ以外は {@code false}
     */
    public boolean hasParenthesis() {
        return this.count > 0;
    }

    /**
     * 現在の括弧数を {@code Deque} 構造の配列へ追加します。 現在の括弧数は {@link ParenthesisFixer#push()}
     * メソッドの呼び出し時に {@code 0} へ初期化されます。
     * <p>
     * {@link ParenthesisFixer#push()}
     * メソッドは自分自身のインスタンスを返却するため、メソッドチェーンの形式で後続処理を行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public ParenthesisFixer push() {
        this.deque.push(this.count);
        this.count = 0;
        return this;
    }

    /**
     * {@link ParenthesisFixer#push()} メソッドで {@code Deque}
     * 構造の配列へ追加された括弧数を最後尾から取り出します。 {@link ParenthesisFixer#pop()}
     * メソッドで取り出された括弧数は現在の括弧数に上書きされます。
     * <p>
     * {@link ParenthesisFixer#pop()}
     * メソッドは自分自身のインスタンスを返却するため、メソッドチェーンの形式で後続処理を行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public ParenthesisFixer pop() {
        this.count = this.deque.pop();
        return this;
    }
}