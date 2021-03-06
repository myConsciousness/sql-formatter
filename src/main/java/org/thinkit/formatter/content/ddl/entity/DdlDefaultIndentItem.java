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

package org.thinkit.formatter.content.ddl.entity;

import java.io.Serializable;

import org.thinkit.common.Precondition;
import org.thinkit.common.exception.IllegalNumberFoundException;
import org.thinkit.formatter.common.catalog.IndentType;
import org.thinkit.framework.content.entity.ContentEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * コンテンツ「DDL既定インデント項目」の値を管理するデータクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
public final class DdlDefaultIndentItem implements ContentEntity, Serializable {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = -7855565808139022777L;

    /**
     * インデントタイプ
     */
    @Getter
    private IndentType indentType;

    /**
     * インデント数
     */
    @Getter
    private int indent;

    /**
     * デフォルトコンストラクタ
     */
    private DdlDefaultIndentItem() {
    }

    /**
     * コンストラクタ
     *
     * @param indentType インデントタイプ
     * @param indent     インデント数
     *
     * @exception NullPointerException        引数として {@code null} が渡された場合
     * @exception IllegalNumberFoundException 引数として渡された {@code indent} が負数の場合
     */
    private DdlDefaultIndentItem(@NonNull IndentType indentType, int indent) {
        Precondition.requirePositive(indent);
        this.indentType = indentType;
        this.indent = indent;
    }

    /**
     * コピーコンストラクタ
     *
     * @param ddlDefaultIndentItem DDL既定インデント項目
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    private DdlDefaultIndentItem(@NonNull DdlDefaultIndentItem ddlDefaultIndentItem) {
        this.indentType = ddlDefaultIndentItem.getIndentType();
        this.indent = ddlDefaultIndentItem.getIndent();
    }

    /**
     * 引数として渡された値を基に {@link DdlDefaultIndentItem} の新しいインスタンスを生成し返却します。
     *
     * @param indentType インデントタイプ
     * @param indent     インデント
     * @return {@link DdlDefaultIndentItem} の新しいインスタンス
     *
     * @exception NullPointerException        引数として {@code null} が渡された場合
     * @exception IllegalNumberFoundException 引数として渡された {@code indent} が負数の場合
     */
    public static DdlDefaultIndentItem of(@NonNull IndentType indentType, int indent) {
        return new DdlDefaultIndentItem(indentType, indent);
    }

    /**
     * 引数として渡された {@code ddlDefaultIndentItem} オブジェクトの値を基に
     * {@link DdlDefaultIndentItem} の新しいインスタンスを生成し返却します。
     *
     * @param ddlDefaultIndentItem DDL既定インデント項目
     * @return {@link DdlDefaultIndentItem} の新しいインスタンス
     *
     * @exception NullPointerException 引数として {@code null} が渡された場合
     */
    public static DdlDefaultIndentItem of(@NonNull DdlDefaultIndentItem ddlDefaultIndentItem) {
        return new DdlDefaultIndentItem(ddlDefaultIndentItem);
    }
}