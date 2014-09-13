package com.github.hamishmorgan.xom;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import nu.xom.*;

import javax.annotation.Nonnull;
import java.net.URI;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class ElementBuilder extends ParentNodeBuilder<Nodes, ElementBuilder> {

    /**
     * Whether or not the element being built is expected to be root
     * element.
     * <p/>
     * Since the XOM NodeFactory provides separate methods for the
     * construction of normal elements vs. root elements, we need to insure
     * the correct method is called. Most of the time this will make no
     * difference, but it still needs to happen.
     */
    private final boolean isRootElement;

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
     * Construct a list of the attributes of this element.
     */
    private final ImmutableList.Builder<Attribute> attributes;

    /**
     * Constructor should not be called directly. Instead use {@link com.github.hamishmorgan.xom.XomB }
     * factory methods: {@link com.github.hamishmorgan.xom.XomB#element(String) }
     * {@link com.github.hamishmorgan.xom.XomB#element(String)},
     * {@link com.github.hamishmorgan.xom.XomB#root(String) }, and
     * {@link com.github.hamishmorgan.xom.XomB#root(String, java.net.URI) }.
     *
     * @param name        the qualified element name
     * @param rootElement whether or not this element is expected to be a
     *                    root element.
     * @throws NullPointerException     if name is null
     * @throws IllegalArgumentException if name is empty
     */
    ElementBuilder(NodeFactory nodeFactory, @Nonnull final String name, final boolean rootElement) {
        super(nodeFactory);
        checkNotNull(name, "name");
        checkArgument(!name.isEmpty(), "argument name is empty");

        final int colon = name.indexOf(':');
        if (colon > 0) {
            setPrefix(name.substring(0, colon));
            setLocalName(name.substring(colon + 1));
        } else {
            setLocalName(name);
        }

        this.isRootElement = rootElement;
        this.attributes = ImmutableList.builder();
        this.namespace = Optional.absent();
    }

    /**
     * I didn't want to make this (or indeed any accessors) public, but
     * certain problems require knowing the name of the parent element from
     * that parents builder.
     *
     * @return
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * @param namespace
     * @return ElementBuilder instance of method chaining
     * @throws NullPointerException if namespace is null
     */
    @Nonnull
    public ElementBuilder setNamespace(final URI namespace) {
        this.namespace = Optional.of(namespace);
        return this;
    }

    /**
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    public ElementBuilder clearNamespace() {
        this.namespace = Optional.absent();
        return this;
    }

    /**
     * @param prefix
     * @return ElementBuilder instance of method chaining
     * @throws NullPointerException     if prefix is null
     * @throws IllegalArgumentException if prefix is empty
     */
    @Nonnull
    public final ElementBuilder setPrefix(@Nonnull String prefix) {
        checkArgument(!prefix.isEmpty(), "prefix is empty");

        this.prefix = Optional.of(prefix);
        return this;
    }

    @Nonnull
    public final ElementBuilder clearPrefix() {
        this.prefix = Optional.absent();
        return this;
    }

    /**
     * @param localName
     * @return ElementBuilder instance of method chaining
     * @throws NullPointerException     if localName is null
     * @throws IllegalArgumentException if localName is empty
     */
    @Nonnull
    public final ElementBuilder setLocalName(@Nonnull String localName) {
        checkArgument(!localName.isEmpty(), "argument localName is empty");

        this.localName = localName;
        return this;
    }

    /**
     * @param data
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    public ElementBuilder add(final String data) {
        checkNotNull(data, "data");
//	    checkArgument(!data.isEmpty(), "argument data is empty");

        _addChildren(factory.makeText(data));
        return this;
    }

    /**
     * @param elBuilder
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    public ElementBuilder add(@Nonnull final ElementBuilder elBuilder) {
        checkNotNull(elBuilder, "elBuilder");

        _addChildren(elBuilder.build());
        return this;
    }

    /**
     * @param element
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    public ElementBuilder add(@Nonnull final Element element) {
        checkNotNull(element, "element");

        _addChild(element);
        return this;
    }

    /**
     * @param attribute
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    public ElementBuilder addAttribute(@Nonnull Attribute attribute) {
        checkNotNull(attribute, "attribute");
        checkArgument(attribute.getParent() == null,
                "Argument attribute already has a parent node.");

        attributes.add(attribute);
        return this;
    }

    /**
     * @param name
     * @param value
     * @return ElementBuilder instance of method chaining
     */
    @Nonnull
    public ElementBuilder addAttribute(@Nonnull final String name,
                                       final String value) {
        return addAttribute(name, XomB.NULL_URI, value, Attribute.Type.CDATA);
    }

    /**
     * @param name
     * @param namespace
     * @param value
     * @param type
     * @return ElementBuilder instance of method chaining
     */
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

    /**
     * @param node
     * @return ElementBuilder instance of method chaining
     * @throws NullPointerException     if node is null
     * @throws IllegalArgumentException if node is a Namespace, DocType or
     *                                  Document, or node already has a parent.
     */
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
