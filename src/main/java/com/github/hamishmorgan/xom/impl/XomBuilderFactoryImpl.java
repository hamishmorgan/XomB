package com.github.hamishmorgan.xom.impl;

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
import nu.xom.NodeFactory;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

@Nonnull
public class XomBuilderFactoryImpl implements XomBuilderFactory {

    protected final NodeFactory nodeFactory;

    public XomBuilderFactoryImpl(final NodeFactory nodeFactory) {
        this.nodeFactory = checkNotNull(nodeFactory, "nodeFactory");
    }

    @Override
    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    @Override
    @Nonnull
    public DocumentBuilder document() {
        return new DocumentBuilderImpl(nodeFactory);
    }

    @Override
    @Nonnull
    public DocTypeBuilder doctype(@Nonnull final String rootElementName) {
        return new DocTypeBuilderImpl(nodeFactory, rootElementName);
    }

    @Override
    @Nonnull
    public ElementBuilder root(@Nonnull final String name) {
        return new ElementBuilderImpl(nodeFactory, name, true);
    }

    @Override
    @Nonnull
    public ElementBuilder element(@Nonnull final String name) {
        return new ElementBuilderImpl(nodeFactory, name, false);
    }

}
