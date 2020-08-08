package org.thinkit.formatter;

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
     * デフォルトコンストラクタ
     */
    private FieldFixer() {
        this.deque = new ArrayDeque<>();
        this.newline = false;
    }

    /**
     * {@link FieldFixer} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link FieldFixer} クラスの新しいインスタンス
     */
    public static FieldFixer of() {
        return new FieldFixer();
    }

    public FieldFixer push() {
        this.deque.push(this.newline);
        this.newline = false;
        return this;
    }

    public FieldFixer pop() {
        this.newline = this.deque.pop();
        return this;
    }

    public FieldFixer toNewline() {
        this.newline = true;
        return this;
    }

    public FieldFixer toNotNewline() {
        this.newline = false;
        return this;
    }
}