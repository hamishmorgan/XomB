package com.github.hamishmorgan.xom;/*
 * Copyright (c) 2010, Hamish Morgan.
 * All Rights Reserved.
 */

import nu.xom.NodeFactory;
import nu.xom.Nodes;

import javax.annotation.Nonnull;
import java.net.URI;

/**
 * <code>com.github.hamishmorgan.xom.XomB</code> (XML Object Model Builder, pronounced xombie :-) is
 * construction tools for {@link nu.xom} XML documents and elements.
 * <p/>
 * The motivation for this tools was a number of perceived problems with the
 * standard method of documents construction using the XOM library. These
 * include:
 * <p/>
 * <ul>
 * <p/>
 * <li>The factory methods provided by {@link NodeFactory} include parameters
 * that rarely used, resulting in large amounts unnecessary boiler plate code.
 * For example consider {@link NodeFactory#makeAttribute(String,
 * String, String, nu.xom.Attribute.Type) }, the
 * namespaceURI parameter is usually
 * <code>null</code>, and the type parameter is usually
 * <code>CDATA</code>. The
 * <code>com.github.hamishmorgan.xom.XomB</code> mitigates this problem by implemented highly ubiquitous
 * alternatives to constructors.</li>
 * <p/>
 * <li>A number of factory methods return type {@link Nodes}. This is because
 * the factory is permitted to replace expected node types with any number of
 * other types. For example a
 * <code>makeAttribute</code> could return elements instead. The
 * <code>Nodes</code> object is a collection without any of normal niceties
 * (such as implementing
 * <code>Iterable</code>); consequently it introduces yet more pointless boiler
 * plate code.
 * <code>com.github.hamishmorgan.xom.XomB</code> handles
 * <code>Nodes</code> internally so the application is simplified
 * considerably.</li>
 * <p/>
 * <li>XOM library makes frequent use of
 * <code>null</code>s to indicate the absence of a property, or that the
 * property should have some default value. This is generally bad design and
 * results in errors not being detected until much later, so this builder
 * interface is non-nullable. (For previously nullable strings use the empty
 * string instead.)</li>
 * <p/>
 * <li>XOM is unnecessarily weakly typed, with frequent use of String names in
 * place of proper typed instances. For example URIs and charsets where encoded
 * as Strings rather than the classes Java provides. This practice increases the
 * likelihood of hard to diagnose run time errors. This problem has been reduces
 * by demanding the proper classes where appropriate.</li>
 * <p/>
 * <p/>
 * </ul>
 * <p/>
 * <p/>
 * Instances of
 * <code>com.github.hamishmorgan.xom.XomB</code> are thread-safe if and only if the supplied
 * <code>NodeFactory</code> is also the thread safe. The default factory
 * implementation ({@link nu.xom.NodeFactory}) IS thread safe, so the supplying
 * this object (either explicitly or implicitly using the default constructor)
 * results in the
 * <code>com.github.hamishmorgan.xom.XomB</code> instance being thread-safe.
 * <p/>
 * <p/>
 * <p/>
 * Builders instances can be used repeatedly to generate multiple independent
 * instances. Builder state is not changed during building so subsequent nodes
 * constructed will inherit properties set on previous ones.
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * Todo:
 * <p/>
 * <p/>
 * <ul>
 * <p/>
 * <li>Addition build() methods, such as build to string, and build DTD.</li>
 * <p/>
 * <li>Not sure the semantics of the API are quite right yet.</li>
 * <p/>
 * </ul>
 *
 * @author hamish
 */
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
