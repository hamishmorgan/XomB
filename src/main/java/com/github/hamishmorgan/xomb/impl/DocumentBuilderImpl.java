package com.github.hamishmorgan.xomb.impl;

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
import nu.xom.Comment;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.NodeFactory;
import nu.xom.Nodes;
import nu.xom.ProcessingInstruction;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

class DocumentBuilderImpl
        extends AbstractParentNodeBuilder<Document, DocumentBuilder>
        implements DocumentBuilder {

    /**
     * Store whether or not the DocType element has been set. DocType element can only be set once, and cannot be unset.
     */
    private boolean docTypeSet;

    /**
     * Store whether or not the root element has been set. Root element can only be set once, and cannot be unset.
     */
    private boolean rootElementSet;

    /**
     * Constructor should not be called directly. Instead use {@link com.github.hamishmorgan.xomb.XomB#createDocument()}
     * nodeFactory method.
     */
    DocumentBuilderImpl(@Nonnull NodeFactory nodeFactory) {
        super(nodeFactory);
        docTypeSet = false;
        rootElementSet = false;
    }

    @Override
    @Nonnull
    public DocumentBuilder withDocType(@Nonnull final String rootElementName,
                                       @Nonnull final String publicID,
                                       @Nonnull final URI systemID) {
        return withDocType(new DocTypeBuilderImpl(factory, rootElementName)
                .withPublicID(publicID)
                .withSystemID(systemID));
    }

    @Override
    @Nonnull
    public DocumentBuilder withDocType(@Nonnull String rootElementName) {
        return withDocType(new DocTypeBuilderImpl(factory, rootElementName));
    }

    @Override
    @Nonnull
    public DocumentBuilder withDocType(@Nonnull DocType docType) {
        checkState(!docTypeSet, "DocType has already been set.");
        docTypeSet = true;
        return _addChild(docType);
    }

    @Override
    @Nonnull
    public DocumentBuilder withDocType(@Nonnull DocTypeBuilder docTypeBuilder) {
        checkState(!docTypeSet, "DocType has already been set.");
        docTypeSet = true;
        return _addChildren(docTypeBuilder.build());
    }

    @Override
    @Nonnull
    public DocumentBuilderImpl withRoot(@Nonnull ElementBuilder rootElement) {
        checkNotNull(rootElement, "rootElement");

        //   Can contain any number of PIs and comments, but exactly 1 createRoot node
        final Nodes nodes = rootElement.build();
        for (int i = 0; i < nodes.size(); i++) {
            final Node node = nodes.get(i);
            if (node instanceof Element) {
                withRoot((Element) node);
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

    @Override
    @Nonnull
    public DocumentBuilder withRoot(@Nonnull Element rootElement) {
        checkNotNull(rootElement, "rootElement");
        checkState(!rootElementSet, "Root element has already been set.");

        _addChild(rootElement);
        rootElementSet = true;
        return this;
    }

    @Override
    @Nonnull
    @CheckReturnValue
    public Document build() {
        final Document document = factory.startMakingDocument();

        if (_getBaseURI().isPresent()) {
            document.setBaseURI(_getBaseURI().get().toString());
        }

        /*
         * Rather than just appending all the child elements, we need to
         * explicitly call the setRootElement method on the document exactly
         * once. Any nodes before or after must be inserted before and after
         * the root createElement.
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
