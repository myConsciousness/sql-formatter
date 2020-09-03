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

package org.thinkit.formatter.content.dml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.thinkit.api.catalog.Catalog;
import org.thinkit.formatter.common.catalog.IndentType;
import org.thinkit.formatter.content.dml.entity.DmlDefaultIndentItem;
import org.thinkit.framework.content.Attribute;
import org.thinkit.framework.content.Condition;
import org.thinkit.framework.content.Content;
import org.thinkit.framework.content.annotation.ContentMapping;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * コンテンツ「DML既定インデント項目」の情報をロードするコンテンツクラスです。
 *
 * @author Kato Shinya
 * @since 1.0
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
@ContentMapping(content = "formatter/sql/DmlDefaultIndentItem")
public final class DmlDefaultIndentItemLoader implements Content<DmlDefaultIndentItem> {

    /**
     * デフォルトコンストラクタ
     */
    private DmlDefaultIndentItemLoader() {
    }

    /**
     * {@link DmlDefaultIndentItemLoader} クラスの新しいインスタンスを生成し返却します。
     *
     * @return {@link DmlDefaultIndentItemLoader} クラスの新しいインスタンス
     */
    public static Content<DmlDefaultIndentItem> of() {
        return new DmlDefaultIndentItemLoader();
    }

    /**
     * コンテンツ要素定数
     */
    @RequiredArgsConstructor
    private enum ContentAttribute implements Attribute {

        /**
         * インデントタイプ
         */
        INDENT_TYPE(Key.indentType),

        /**
         * インデント数
         */
        INDENT(Key.indent);

        /**
         * キー
         */
        private final Key key;

        /**
         * キー要素
         */
        private enum Key {
            indentType, indent;
        }

        @Override
        public String getString() {
            return this.key.name();
        }
    }

    @Override
    public DmlDefaultIndentItem execute() {
        final Map<String, String> content = this.loadContent(this.getClass()).get(0);

        return DmlDefaultIndentItem.of(
                Catalog.getEnum(IndentType.class,
                        Integer.parseInt(content.get(ContentAttribute.INDENT_TYPE.getString()))),
                Integer.parseInt(content.get(ContentAttribute.INDENT.getString())));
    }

    @Override
    public List<Attribute> getAttributes() {

        final List<Attribute> attributes = new ArrayList<>();

        for (Attribute attribute : ContentAttribute.values()) {
            attributes.add(attribute);
        }

        return attributes;
    }

    @Override
    public Map<Condition, String> getConditions() {
        return null;
    }
}