package com.github.hamishmorgan.xomb;

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

import com.github.hamishmorgan.xomb.api.DocTypeBuilder;
import com.github.hamishmorgan.xomb.api.DocumentBuilder;
import com.github.hamishmorgan.xomb.api.ElementBuilder;
import com.github.hamishmorgan.xomb.impl.XomBuilderFactoryImpl;
import com.github.hamishmorgan.xomb.spi.XomBuilderFactory;
import nu.xom.NodeFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.net.URI;

@Nonnull
public class XomB implements XomBuilderFactory {

    @Nonnull
    private final XomBuilderFactory xomBuilderFactory;

    /**
     * Construct a new com.github.hamishmorgan.xomb.XomB instance that will use the given NodeFactory
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
     * Construct a new com.github.hamishmorgan.xomb.XomB instance that will use the default NodeFactory.
     */
    public XomB() {
        this(new NodeFactory());
    }


    @Nonnull
    @CheckReturnValue
    public ElementBuilder root(@Nonnull final String name, final URI namespace) {
        return createRoot(name).withNamespace(namespace);
    }

    @Nonnull
    @CheckReturnValue
    public ElementBuilder buildElement(@Nonnull final String name, final URI namespace) {
        return createElement(name).withNamespace(namespace);
    }

    @Override
    @Nonnull
    @CheckReturnValue
    public NodeFactory getNodeFactory() {
        return xomBuilderFactory.getNodeFactory();
    }

    @Override
    @Nonnull
    @CheckReturnValue
    public DocumentBuilder createDocument() {
        return xomBuilderFactory.createDocument();
    }

    @Override
    @Nonnull
    @CheckReturnValue
    public DocTypeBuilder createDocType(@Nonnull String rootElementName) {
        return xomBuilderFactory.createDocType(rootElementName);
    }

    @Override
    @Nonnull
    @CheckReturnValue
    public ElementBuilder createRoot(@Nonnull String name) {
        return xomBuilderFactory.createRoot(name);
    }

    @Override
    @Nonnull
    @CheckReturnValue
    public ElementBuilder createElement(@Nonnull String name) {
        return xomBuilderFactory.createElement(name);
    }
}
