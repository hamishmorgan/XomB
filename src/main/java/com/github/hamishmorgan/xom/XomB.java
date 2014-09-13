package com.github.hamishmorgan.xom;/*
 * Copyright (c) 2010, Hamish Morgan.
 * All Rights Reserved.
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
