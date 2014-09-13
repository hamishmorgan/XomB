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

import com.github.hamishmorgan.xomb.api.MissingRequiredPropertyException;
import com.github.hamishmorgan.xomb.api.DocTypeBuilder;
import com.google.common.base.Optional;
import nu.xom.DocType;
import nu.xom.NodeFactory;
import nu.xom.Nodes;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.net.URI;

import static com.google.common.base.Preconditions.checkArgument;

@ThreadSafe
class DocTypeBuilderImpl extends AbstractXomBuilder implements DocTypeBuilder {

    @Nonnull
    private Optional<String> rootElementName;

    @Nonnull
    private Optional<String> publicID;

    @Nonnull
    private Optional<URI> systemID;

    @Nonnull
    private Optional<String> internalDTDSubset;

    DocTypeBuilderImpl(@Nonnull NodeFactory factory) {
        super(factory);
        rootElementName = Optional.absent();
        systemID = Optional.absent();
        publicID = Optional.absent();
        internalDTDSubset = Optional.absent();
    }

    @Override
    @Nonnull
    public DocTypeBuilder withRootElementName(@Nonnull final String rootElementName) {
        checkArgument(!rootElementName.isEmpty(),
                "argument rootElementName is empty");
        this.rootElementName = Optional.of(rootElementName);
        return this;
    }

    @Override
    @Nonnull
    public DocTypeBuilder withSystemID(@Nonnull final URI systemID) {
        this.systemID = Optional.of(systemID);
        return this;
    }

    @Override
    @Nonnull
    public DocTypeBuilder clearSystemID() {
        this.systemID = Optional.absent();
        return this;
    }

    @Override
    @Nonnull
    public DocTypeBuilder withPublicID(@Nonnull final String publicID) {
        checkArgument(!publicID.isEmpty(),
                "argument publicID is empty");
        this.publicID = Optional.of(publicID);
        return this;
    }

    @Override
    @Nonnull
    public DocTypeBuilder clearPublicID() {
        this.publicID = Optional.absent();
        return this;
    }

    @Override
    @Nonnull
    public DocTypeBuilder withInternalDTDSubset(@Nonnull final String internalDTDSubset) {
        checkArgument(!internalDTDSubset.isEmpty(),
                "argument internalDTDSubset is empty");
        this.internalDTDSubset = Optional.of(internalDTDSubset);
        return this;
    }

    @Override
    @Nonnull
    public DocTypeBuilder clearInternalDTDSubset() {
        this.internalDTDSubset = Optional.absent();
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public Nodes build() {

        if(!rootElementName.isPresent()) {
            throw new MissingRequiredPropertyException("rootElementName");
        }

        final Nodes doctypeNodes = factory.makeDocType(
                rootElementName.get(),
                publicID.orNull(),
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
            if (!foundDT && LOGGER.isWarnEnabled()) {
                LOGGER.warn("Failed to set internal DT subset property on "
                        + "DocType node because no DocType was produced by "
                        + "the NodeFactory.");
            }
        }
        return doctypeNodes;
    }
}
