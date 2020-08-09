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

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 関数を管理する処理や状態を定義したクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
final class FunctionFixer {

    /**
     * 関数の数
     */
    private int count;

    /**
     * デフォルトコンストラクタ
     */
    private FunctionFixer() {
        this.count = 0;
    }

    /**
     * {@link FunctionFixer} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link FunctionFixer} クラスの新しいインスタンス
     */
    public static FunctionFixer of() {
        return new FunctionFixer();
    }

    /**
     * 関数の数をインクリメントします。
     * <p>
     * この {@link FunctionFixer#increment()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public FunctionFixer increment() {
        this.count++;
        return this;
    }

    /**
     * 関数の数をデクリメントします。
     * <p>
     * この {@link FunctionFixer#decrement()}
     * メソッドは自分自身のインスタンスを返却するため、後続処理をメソッドチェーンの形式で行うことができます。
     *
     * @return 自分自身のインスタンス
     */
    public FunctionFixer decrement() {
        this.count--;
        return this;
    }

    /**
     * 関数の数が {@code 0} よりも大きいか判定します。
     *
     * @return 関数の数が {@code 0} よりも大きい場合は {@code true} 、それ以外は {@code false}
     */
    public boolean isInFunction() {
        return this.count > 0;
    }
}