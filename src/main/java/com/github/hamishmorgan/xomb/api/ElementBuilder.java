package com.github.hamishmorgan.xomb.api;

/*
 * #%L
 * XomB XML Object Model Builder
 * %%
 * Copyright (C) 2012 - 2014 Hamish Morgan
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.common.base.Optional;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.net.URI;

public interface ElementBuilder extends ParentNodeBuilder<Nodes, ElementBuilder> {
    /**
     * I didn't want to make this (or indeed any accessors) public, but
     * certain problems require knowing the name of the parent element from
     * that parents builder.
     *
     * @return
     */
    @Nonnull
    @CheckReturnValue
    String getLocalName();

    /**
     * @param localName
     * @return ElementBuilder instance of method chaining
     * @throws NullPointerException     if localName is null
     * @throws IllegalArgumentException if localName is empty
     */
    @Nonnull
    ElementBuilder withLocalName(@Nonnull String localName);

    /**
     * @param namespace
     * @return ElementBuilder instance of method chaining
     * @throws NullPointerException if namespace is null
     */
    @Nonnull
    ElementBuilder withNamespace(@Nonnull URI namespace);

    /**
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    ElementBuilder clearNamespace();

    /**
     * @param prefix
     * @return ElementBuilder instance of method chaining
     * @throws NullPointerException     if prefix is null
     * @throws IllegalArgumentException if prefix is empty
     */
    @Nonnull
    ElementBuilder withPrefix(String prefix);

    @Nonnull
    ElementBuilder clearPrefix();

    /**
     * @param data
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    ElementBuilder add(@Nonnull String data);

    /**
     * @param elBuilder
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    ElementBuilder add(@Nonnull ElementBuilder elBuilder);

    /**
     * @param element
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    ElementBuilder add(@Nonnull Element element);

    /**
     * @param attribute
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    ElementBuilder addAttribute(@Nonnull Attribute attribute);

    /**
     * @param name
     * @param value
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    ElementBuilder addAttribute(@Nonnull String name, @Nonnull String value);

    /**
     * @param name
     * @param namespace
     * @param value
     * @param type
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    ElementBuilder addAttribute(@Nonnull String name, @Nonnull Optional<URI> namespace, @Nonnull String value, @Nonnull Attribute.Type type);

    /**
     * @param node
     * @return ElementBuilder instance of method chaining
     * @throws NullPointerException     if node is null
     * @throws IllegalArgumentException if node is a Namespace, DocType or
     *                                  Document, or node already has a parent.
     */
    @Nonnull
    ElementBuilder add(@Nonnull Node node);
}
