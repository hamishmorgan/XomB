package com.github.hamishmorgan.xom;

import com.google.common.base.Optional;
import nu.xom.DocType;
import nu.xom.NodeFactory;
import nu.xom.Nodes;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.net.URI;

import static com.google.common.base.Preconditions.checkArgument;

@ThreadSafe
public class DocTypeBuilder extends BaseXomBuilder implements NodeBuilder<Nodes, DocTypeBuilder> {

    @Nonnull
    private final String rootElementName;

    private Optional<String> publicID;

    private Optional<URI> systemID;

    private Optional<String> internalDTDSubset;

    DocTypeBuilder(NodeFactory factory, @Nonnull final String rootElementName) {
        super(factory);
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
            if (!foundDT && LOGGER.isWarnEnabled()) {
                LOGGER.warn("Failed to set internal DT subset property on "
                        + "DocType node because no DocType was produced by "
                        + "the NodeFactory.");
            }
        }
        return doctypeNodes;
    }
}
