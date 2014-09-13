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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import nu.xom.*;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract super class to the node build classes.
 *
 * Handles a collection of child nodes, but does not implement any public API because the
 * constraints for subclasses are quite different.
 *
 * @param <P> product type (the object constructed by this builder)
 * @param <B> builder type (the subclass type of this builder)
 */
abstract class ParentNodeBuilder<P, B extends ParentNodeBuilder<P, B>>
        extends BaseXomBuilder
        implements NodeBuilder<P, B> {

    /**
     * Defines a names-space from which all URIs inside are considered to be
     * relative to.
     * <p/>
     * For example, consider the following XHTML snippets, which are
     * equivalent:
     * <pre>
     *      &lt;a href="http://example.org/test#foo"/&gt;
     *      &lt;a xml:base="http://example.org/test" href="#foo"/&gt;
     * </pre>
     */
    private Optional<URI> baseURI;

    /**
     * Build an immutable list of childrenOf.
     */
    private final ImmutableList.Builder<Node> children;

    /**
     * Constructor
     */
    ParentNodeBuilder(NodeFactory nodeFactory) {
        super(nodeFactory);
        baseURI = Optional.absent();
        children = ImmutableList.builder();
    }

    /**
     * @param baseURI
     * @return
     * @throws NullPointerException if baseURI is null
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public B setBaseURI(final URI baseURI) {
        this.baseURI = Optional.of(baseURI);
        return (B) this;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public B clearBaseURI() {
        this.baseURI = Optional.absent();
        return (B) this;
    }

    /**
     * @param target
     * @param data
     * @return
     * @throws NullPointerException if target or data is null
     */
    @Nonnull
    public B addPI(@Nonnull final String target, final String data) {
        checkArgument(!target.isEmpty(), "argument target is empty");
        return _addChildren(factory.makeProcessingInstruction(
                checkNotNull(target, "target"),
                checkNotNull(data, "data")));
    }

    /**
     * @param pi
     * @return
     * @throws NullPointerException if pi is null
     */
    @Nonnull
    public B addPI(@Nonnull final ProcessingInstruction pi) {
        return _addChild(pi);
    }

    /**
     * @param data
     * @return
     * @throws NullPointerException                 if data is null
     * @throws nu.xom.IllegalDataException          if data contains a double-hyphen (--) or
     *                                              a carriage return, or data ends with a hyphen.
     * @throws nu.xom.IllegalCharacterDataException if data contains corrupt or
     *                                              unsupported characters.
     */
    @Nonnull
    public B addComment(final String data) {
        return _addChildren(factory.makeComment(checkNotNull(data, "data")));
    }

    /**
     * @param comment
     * @return
     * @throws NullPointerException if data is null
     */
    @Nonnull
    public B addComment(@Nonnull final Comment comment) {
        return _addChild(comment);
    }

    /**
     * @return
     */
    protected Optional<URI> _getBaseURI() {
        return baseURI;
    }

    /**
     * @param nodes
     * @return
     * @throws NullPointerException     if nodes is null, or if any element of
     *                                  nodes is null.
     * @throws IllegalArgumentException if any node already has a parent
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    protected B _addChildren(@Nonnull final Nodes nodes) {
        for (int i = 0; i < nodes.size(); i++)
            _addChild(nodes.get(i));
        return (B) this;
    }

    /**
     * @param node
     * @return
     * @throws NullPointerException     if node is null
     * @throws IllegalArgumentException if node already has a parent
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    protected B _addChild(@Nonnull final Node node) {
        checkArgument(node.getParent() == null, "node argument already has a parent");
        children.add(node);
        return (B) this;
    }

    protected List<Node> _getChildren() {
        return children.build();
    }
}
