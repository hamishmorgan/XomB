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

import com.github.hamishmorgan.xom.XomB;
import com.github.hamishmorgan.xom.api.ElementBuilder;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import nu.xom.*;

import javax.annotation.Nonnull;
import java.net.URI;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

class ElementBuilderImpl extends AbstractParentNodeBuilder<Nodes, ElementBuilder> implements ElementBuilder {

    /**
     * Whether or not the element being built is expected to be root element.
     *
     * Since the XOM NodeFactory provides separate methods for the construction of normal elements vs. root
     * elements, we need to insure the correct method is called. Most of the time this will make no difference,
     * but it still needs to happen.
     */
    private final boolean isRootElement;
    /**
     * Construct a list of the attributes of this element.
     */
    private final ImmutableList.Builder<Attribute> attributes;
    /**
     * The elements unqualified name.
     */
    private String localName;
    /**
     * The namespace prefix for the given name.
     */
    private Optional<String> prefix = Optional.absent();
    /**
     * The elements name-space.
     */
    private Optional<URI> namespace;

    /**
     * Constructor should not be called directly. Instead use {@link com.github.hamishmorgan.xom.XomB }
     * nodeFactory methods: {@link com.github.hamishmorgan.xom.XomB#createElement(String) }
     * {@link com.github.hamishmorgan.xom.XomB#createElement(String)},
     * {@link com.github.hamishmorgan.xom.XomB#createRoot(String) }, and
     * {@link com.github.hamishmorgan.xom.XomB#root(String, java.net.URI) }.
     *
     * @param nodeFactory use to instantiate xom nodes
     * @param name        the qualified element name
     * @param rootElement whether or not this element is expected to be a
     *                    createRoot element.
     * @throws NullPointerException     if name is null
     * @throws IllegalArgumentException if name is empty
     */
    ElementBuilderImpl(NodeFactory nodeFactory, @Nonnull final String name, final boolean rootElement) {
        super(nodeFactory);
        checkNotNull(name, "name");
        checkArgument(!name.isEmpty(), "argument name is empty");

        final int colon = name.indexOf(':');
        if (colon > 0) {
            withPrefix(name.substring(0, colon));
            withLocalName(name.substring(colon + 1));
        } else {
            withLocalName(name);
        }

        this.isRootElement = rootElement;
        this.attributes = ImmutableList.builder();
        this.namespace = Optional.absent();
    }

    @Override
    public String getLocalName() {
        return localName;
    }

    @Override
    @Nonnull
    public ElementBuilder withNamespace(final URI namespace) {
        this.namespace = Optional.of(namespace);
        return this;
    }

    @Override
    @Nonnull
    public ElementBuilder clearNamespace() {
        this.namespace = Optional.absent();
        return this;
    }

    @Override
    @Nonnull
    public final ElementBuilder withPrefix(@Nonnull String prefix) {
        checkArgument(!prefix.isEmpty(), "prefix is empty");

        this.prefix = Optional.of(prefix);
        return this;
    }

    @Override
    @Nonnull
    public final ElementBuilder clearPrefix() {
        this.prefix = Optional.absent();
        return this;
    }

    @Override
    @Nonnull
    public final ElementBuilder withLocalName(@Nonnull String localName) {
        checkArgument(!localName.isEmpty(), "argument localName is empty");

        this.localName = localName;
        return this;
    }

    @Override
    @Nonnull
    public ElementBuilder add(final String data) {
        checkNotNull(data, "data");
//	    checkArgument(!data.isEmpty(), "argument data is empty");

        _addChildren(factory.makeText(data));
        return this;
    }

    @Override
    @Nonnull
    public ElementBuilder add(@Nonnull final ElementBuilder elBuilder) {
        checkNotNull(elBuilder, "elBuilder");

        _addChildren(elBuilder.build());
        return this;
    }

    @Override
    @Nonnull
    public ElementBuilder add(@Nonnull final Element element) {
        checkNotNull(element, "element");

        _addChild(element);
        return this;
    }

    @Override
    @Nonnull
    public ElementBuilder addAttribute(@Nonnull Attribute attribute) {
        checkNotNull(attribute, "attribute");
        checkArgument(attribute.getParent() == null,
                "Argument attribute already has a parent node.");

        attributes.add(attribute);
        return this;
    }

    @Override
    @Nonnull
    public ElementBuilder addAttribute(@Nonnull final String name,
                                       final String value) {
        return addAttribute(name, XomB.NULL_URI, value, Attribute.Type.CDATA);
    }

    @Override
    @Nonnull
    public ElementBuilder addAttribute(
            @Nonnull final String name, @Nonnull final URI namespace,
            final String value, final Attribute.Type type) {
        checkNotNull(name, "name");
        checkArgument(!name.isEmpty(), "argument name is empty");
        checkNotNull(namespace, "namespaceURI");
        checkNotNull(value, "name");
        checkNotNull(type, "type");

        Nodes nodes = factory.makeAttribute(name, namespace.toString(),
                value, type);
        for (int i = 0; i < nodes.size(); i++) {
            add(nodes.get(i));
        }
        return this;
    }

    @Override
    @Nonnull
    public ElementBuilder add(final Node node) {
        checkNotNull(node, "node");
        if (node instanceof Namespace || node instanceof DocType
                || node instanceof Document) {
            throw new IllegalArgumentException(
                    "element node can not have child notes of type "
                            + node.getClass().getSimpleName());

        } else if (node instanceof Attribute) {
            attributes.add((Attribute) node);
        } else { // Element, Comment, Text, ProcessingInstructiona
            _addChild(node);
        }
        return this;
    }

    @Override
    public Nodes build() {

        final String qualifiedName = prefix.isPresent()
                ? prefix.get() + ":" + localName
                : localName;

        final String namespaceStr = namespace.isPresent()
                ? namespace.get().toString()
                : XomB.NULL_URI.toString();

        final Element element;
        if (isRootElement)
            element = (Element) factory.makeRootElement(
                    qualifiedName, namespaceStr).copy();
        else
            element = (Element) factory.startMakingElement(
                    qualifiedName, namespaceStr).copy();

        if (_getBaseURI().isPresent()) {
            element.setBaseURI(_getBaseURI().get().toString());
        }

        for (final Attribute attribute : attributes.build()) {
            element.addAttribute((Attribute) attribute.copy());
        }

        for (final Node node : _getChildren()) {
            element.appendChild(node.copy());
        }

        /*
         * XXX: Not sure if finishMarkingElement is supposed to be called on
         * root elements or not. It seems reasonable that someone might want
         * produce extract comments or processing instructions for a root
         * element, in which case finish' MUST be called.
         */
        return factory.finishMakingElement(element);
    }
}
