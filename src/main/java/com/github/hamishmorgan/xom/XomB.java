package com.github.hamishmorgan.xom;/*
 * Copyright (c) 2010, Hamish Morgan.
 * All Rights Reserved.
 */

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import nu.xom.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.*;

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
public class XomB {

    private static final Log LOG = LogFactory.getLog(XomB.class);

    /**
     * Rather than use null to represent unset URI values, use this special
     * constant.
     */
    public static final URI NULL_URI = URI.create("");

    /**
     * factory used to instantiate all XOM nodes.
     */
    private final NodeFactory factory;

    /**
     * Construct a new com.github.hamishmorgan.xom.XomB instance that will use the given NodeFactory
     * instance to create all XOM nodes.
     * <p/>
     *
     * @param factory used to create nodes
     * @throws NullPointerException if factory is null
     */
    public XomB(final NodeFactory factory) {
        this.factory = checkNotNull(factory);
    }

    /**
     * Construct a new com.github.hamishmorgan.xom.XomB instance that will use the default NodeFactory.
     */
    public XomB() {
        this(new NodeFactory());
    }

    /**
     * Get the node factory used by this builder.
     * <p/>
     *
     * @return node factory
     */
    public NodeFactory getFactory() {
        return factory;
    }

    @Nonnull
    public DocumentBuilder document() {
        return new DocumentBuilder();
    }

    @Nonnull
    public DocTypeBuilder doctype(@Nonnull final String rootElementName) {
        return new DocTypeBuilder(rootElementName);
    }

    @Nonnull
    public ElementBuilder root(@Nonnull final String name, final URI namespace) {
        return root(name).setNamespace(namespace);
    }

    @Nonnull
    public ElementBuilder root(@Nonnull final String name) {
        return new ElementBuilder(name, true);
    }

    @Nonnull
    public ElementBuilder buildElement(@Nonnull final String name, final URI namespace) {
        return element(name).setNamespace(namespace);
    }

    @Nonnull
    public ElementBuilder element(@Nonnull final String name) {
        return new ElementBuilder(name, false);
    }

    protected interface NodeBuilder<P, B extends NodeBuilder<P, B>> {

        /**
         * Construct an XOM node for the current state of the builder.
         *
         * @return newly constructed XOM node
         * @throws IllegalStateException if the build cannot complete because a
         *                               require argument is un/miss-configured
         */
        public abstract P build();
    }

    /**
     *
     */
    @ThreadSafe
    public class DocTypeBuilder implements NodeBuilder<Nodes, DocTypeBuilder> {

        @Nonnull
        private final String rootElementName;

        private Optional<String> publicID;

        private Optional<URI> systemID;

        private Optional<String> internalDTDSubset;

        protected DocTypeBuilder(@Nonnull final String rootElementName) {
            checkArgument(!rootElementName.isEmpty(),
                    "argument rootElementName is empty");
            this.rootElementName = rootElementName;
            systemID = Optional.absent();
            publicID = Optional.absent();
            internalDTDSubset = Optional.absent();
        }

        @Nonnull
        public DocTypeBuilder setSystemID(final URI systemID) {
            this.systemID = Optional.of(systemID);
            return this;
        }

        @Nonnull
        public DocTypeBuilder clearSystemID() {
            this.systemID = Optional.absent();
            return this;
        }

        @Nonnull
        public DocTypeBuilder setPublicID(@Nonnull final String publicID) {
            checkArgument(!publicID.isEmpty(),
                    "argument publicID is empty");
            this.publicID = Optional.of(publicID);
            return this;
        }

        @Nonnull
        public DocTypeBuilder clearPublicID() {
            this.publicID = Optional.absent();
            return this;
        }

        @Nonnull
        public DocTypeBuilder setInternalDTDSubset(
                @Nonnull final String internalDTDSubset) {
            checkArgument(!internalDTDSubset.isEmpty(),
                    "argument internalDTDSubset is empty");
            this.internalDTDSubset = Optional.of(internalDTDSubset);
            return this;
        }

        @Nonnull
        public DocTypeBuilder clearInternalDTDSubset() {
            this.internalDTDSubset = Optional.absent();
            return this;
        }

        public Nodes build() {
            final Nodes doctypeNodes = factory.makeDocType(
                    rootElementName,
                    publicID.isPresent() ? publicID.get() : null,
                    systemID.isPresent() ? systemID.get().toString() : null);

            if (internalDTDSubset.isPresent()) {
                // Find the doctype node if present
                boolean foundDT = false;
                for (int i = 0; i < doctypeNodes.size(); i++) {
                    if (doctypeNodes.get(i) instanceof DocType) {
                        ((DocType) doctypeNodes.get(i)).setInternalDTDSubset(
                                internalDTDSubset.get());
                        foundDT = true;
                        // There should only be one DocType so stop
                        break;
                    }
                }
                if (!foundDT && LOG.isWarnEnabled()) {
                    LOG.warn("Failed to set internal DT subset property on "
                            + "DocType node because no DocType was produced by "
                            + "the NodeFactory.");
                }
            }
            return doctypeNodes;
        }
    }

    /**
     * Abstract super class to the node build classes. </p> Handles a collection
     * of child nodes, but does not implement any public API because the
     * constraints for subclasses are quite different.
     *
     * @param <P> product type (the object constructed by this builder)
     * @param <B> builder type (the subclass type of this builder)
     */
    protected abstract class ParentNodeBuilder<P, B extends ParentNodeBuilder<P, B>>
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
        protected ParentNodeBuilder() {
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

        /**
         * @return
         */
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
         * @throws NullPointerException          if data is null
         * @throws IllegalDataException          if data contains a double-hyphen (--) or
         *                                       a carriage return, or data ends with a hyphen.
         * @throws IllegalCharacterDataException if data contains corrupt or
         *                                       unsupported characters.
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

        /**
         * @return
         */
        protected List<Node> _getChildren() {
            return children.build();
        }
    }

    /**
     *
     */
    public class DocumentBuilder extends ParentNodeBuilder<Document, DocumentBuilder> {

        /**
         * Store whether or not the DocType element has been set.
         * <p/>
         * DocType element can only be set once, and cannot be unset.
         */
        private boolean docTypeSet;

        /**
         * Store whether or not the root element has been set.
         * <p/>
         * Root element can only be set once, and cannot be unset.
         */
        private boolean rootElementSet;

        /**
         * Constructor should not be called directly. Instead use {@link XomB#document()
         * }
         * factory method.
         */
        DocumentBuilder() {
            docTypeSet = false;
            rootElementSet = false;
        }

        @Nonnull
        public DocumentBuilder setDocType(@Nonnull final String rootElementName,
                                          @Nonnull final String publicID,
                                          final URI systemID) {
            return setDocType(doctype(rootElementName)
                    .setPublicID(publicID)
                    .setSystemID(systemID));
        }

        @Nonnull
        public DocumentBuilder setDocType(@Nonnull String rootElementName) {
            return setDocType(doctype(rootElementName));
        }

        @Nonnull
        public DocumentBuilder setDocType(@Nonnull DocType docType) {
            checkState(!docTypeSet, "DocType has already been set.");
            docTypeSet = true;
            return _addChild(docType);
        }

        @Nonnull
        public DocumentBuilder setDocType(@Nonnull DocTypeBuilder docTypeBuilder) {
            checkState(!docTypeSet, "DocType has already been set.");
            docTypeSet = true;
            return _addChildren(docTypeBuilder.build());
        }

        /**
         * @param rootElement
         * @return
         */
        @Nonnull
        public DocumentBuilder setRoot(@Nonnull ElementBuilder rootElement) {
            checkNotNull(rootElement, "rootElement");

            //   Can contain any number of PIs and comments, but exactly 1 root node
            final Nodes nodes = rootElement.build();
            for (int i = 0; i < nodes.size(); i++) {
                final Node node = nodes.get(i);
                if (node instanceof Element) {
                    setRoot((Element) node);
                } else if (node instanceof ProcessingInstruction
                        || node instanceof Comment) {
                    _addChild(node);
                } else {
                    // should only happen if the NodeFactory is behaving badly.
                    throw new AssertionError("Document node can "
                            + "contain child nodes of type Element, Comment, or"
                            + " ProcessingInstruction.");
                }

            }
            return this;
        }

        @Nonnull
        public DocumentBuilder setRoot(@Nonnull Element rootElement) {
            checkNotNull(rootElement, "rootElement");
            checkState(!rootElementSet, "Root element has already been set.");

            _addChild(rootElement);
            rootElementSet = true;
            return this;
        }

        public Document build() {
            final Document document = factory.startMakingDocument();

            if (_getBaseURI().isPresent()) {
                document.setBaseURI(_getBaseURI().get().toString());
            }

	    /*
         * Rather than just appending all the child elements, we need to
	     * explicitly call the setRootElement method on the document exactly
	     * once. Any nodes before or after must be inserted before and after
	     * the root element.
	     */

            final List<Node> children = _getChildren();

            int i = 0;
            while (i < children.size() && !(children.get(i) instanceof Element)) {
                if (children.get(i) instanceof DocType) {
                    document.setDocType((DocType) children.get(i).copy());
                } else {
                    document.insertChild(children.get(i).copy(), i);
                }
                ++i;
            }

            if (i < children.size() && (children.get(i) instanceof Element)) {
                document.setRootElement((Element) children.get(i).copy());
                ++i;
            }

            while (i < children.size()) {
                if (children.get(i) instanceof DocType) {
                    document.setDocType((DocType) children.get(i).copy());
                } else {
                    document.insertChild(children.get(i).copy(), i);
                }
                ++i;
            }

            factory.finishMakingDocument(document);
            return document;
        }
    }

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
         * Constructor should not be called directly. Instead use {@link XomB }
         * factory methods: {@link XomB#element(String) }
         * {@link XomB#element(String)},
         * {@link XomB#root(String) }, and
         * {@link XomB#root(String, java.net.URI) }.
         *
         * @param name        the qualified element name
         * @param rootElement whether or not this element is expected to be a
         *                    root element.
         * @throws NullPointerException     if name is null
         * @throws IllegalArgumentException if name is empty
         */
        ElementBuilder(@Nonnull final String name, final boolean rootElement) {
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
            return addAttribute(name, NULL_URI, value, Attribute.Type.CDATA);
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
                    : NULL_URI.toString();

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
}
