package org.thinkit.formatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@EqualsAndHashCode
final class DmlAppender {

    /**
     * 整形済みのSQL
     */
    private StringBuilder sql;

    /**
     * DMLトークナイザ
     */
    private DmlTokenizer dmlTokenizer;

    /**
     * DMLインデンター
     */
    private DmlIndenter dmlIndenter;

    /**
     * 開始ライン
     */
    @Getter
    private boolean beginLine;

    /**
     * デフォルトコンストラクタ
     */
    private DmlAppender() {
    }

    private DmlAppender(@NonNull DmlTokenizer dmlTokenizer) {
        this.sql = new StringBuilder();
        this.dmlTokenizer = dmlTokenizer;
        this.dmlIndenter = DmlIndenter.of();
    }

    public static DmlAppender register(@NonNull DmlTokenizer dmlTokenizer) {
        return new DmlAppender(dmlTokenizer);
    }

    public DmlAppender appendToken() {
        this.sql.append(this.dmlTokenizer.getToken());
        return this;
    }

    public DmlAppender appendNewLine() {
        this.sql.append(this.dmlIndenter.newline());
        return this;
    }

    public DmlAppender increment() {
        this.dmlIndenter.increment();
        return this;
    }

    public DmlAppender decrement() {
        this.dmlIndenter.decrement();
        return this;
    }

    public DmlAppender toBeginLine() {
        this.beginLine = true;
        return this;
    }

    public DmlAppender toNotBeginLine() {
        this.beginLine = false;
        return this;
    }

    @Override
    public String toString() {
        return this.sql.toString();
    }
}