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

import com.github.hamishmorgan.xom.api.DocTypeBuilder;
import com.github.hamishmorgan.xom.api.DocumentBuilder;
import com.github.hamishmorgan.xom.api.ElementBuilder;
import com.github.hamishmorgan.xom.api.XomBuilderFactory;
import com.github.hamishmorgan.xom.impl.XomBuilderFactoryImpl;
import nu.xom.NodeFactory;

import javax.annotation.Nonnull;
import java.net.URI;

@Nonnull
public class XomB implements XomBuilderFactory {

    /**
     * Rather than use null to represent unset URI values, use this special
     * constant.
     */
    public static final URI NULL_URI = URI.create("");

    private final XomBuilderFactory xomBuilderFactory;

    /**
     * Construct a new com.github.hamishmorgan.xom.XomB instance that will use the given NodeFactory
     * instance to create all XOM nodes.
     * <p/>
     *
     * @param factory used to create nodes
     * @throws NullPointerException if nodeFactory is null
     */
    public XomB(final NodeFactory factory) {
        xomBuilderFactory = new XomBuilderFactoryImpl(factory);
    }

    /**
     * Construct a new com.github.hamishmorgan.xom.XomB instance that will use the default NodeFactory.
     */
    public XomB() {
        this(new NodeFactory());
    }


    @Nonnull
    public ElementBuilder root(@Nonnull final String name, final URI namespace) {
        return root(name).setNamespace(namespace);
    }

    @Nonnull
    public ElementBuilder buildElement(@Nonnull final String name, final URI namespace) {
        return element(name).setNamespace(namespace);
    }

    @Override
    public NodeFactory getNodeFactory() {
        return xomBuilderFactory.getNodeFactory();
    }

    @Override
    @Nonnull
    public DocumentBuilder document() {
        return xomBuilderFactory.document();
    }

    @Override
    @Nonnull
    public DocTypeBuilder doctype(@Nonnull String rootElementName) {
        return xomBuilderFactory.doctype(rootElementName);
    }

    @Override
    @Nonnull
    public ElementBuilder root(@Nonnull String name) {
        return xomBuilderFactory.root(name);
    }

    @Override
    @Nonnull
    public ElementBuilder element(@Nonnull String name) {
        return xomBuilderFactory.element(name);
    }
}
