package com.github.hamishmorgan.xom;

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

import nu.xom.NodeFactory;
import nu.xom.Nodes;

import javax.annotation.Nonnull;
import java.net.URI;

@Nonnull
public class XomB extends BaseXomBuilder {

    /**
     * Rather than use null to represent unset URI values, use this special
     * constant.
     */
    public static final URI NULL_URI = URI.create("");

    /**
     * Construct a new com.github.hamishmorgan.xom.XomB instance that will use the given NodeFactory
     * instance to create all XOM nodes.
     * <p/>
     *
     * @param factory used to create nodes
     * @throws NullPointerException if factory is null
     */
    public XomB(final NodeFactory factory) {
        super(factory);
    }

    /**
     * Construct a new com.github.hamishmorgan.xom.XomB instance that will use the default NodeFactory.
     */
    public XomB() {
        this(new NodeFactory());
    }

    @Nonnull
    public DocumentBuilder document() {
        return new DocumentBuilder(factory);
    }

    @Nonnull
    public DocTypeBuilder doctype(@Nonnull final String rootElementName) {
        return new DocTypeBuilder(factory, rootElementName);
    }

    @Nonnull
    public ElementBuilder root(@Nonnull final String name, final URI namespace) {
        return root(name).setNamespace(namespace);
    }

    @Nonnull
    public ElementBuilder root(@Nonnull final String name) {
        return new ElementBuilder(factory, name, true);
    }

    @Nonnull
    public ElementBuilder buildElement(@Nonnull final String name, final URI namespace) {
        return element(name).setNamespace(namespace);
    }

    @Nonnull
    public ElementBuilder element(@Nonnull final String name) {
        return new ElementBuilder(factory, name, false);
    }

}
