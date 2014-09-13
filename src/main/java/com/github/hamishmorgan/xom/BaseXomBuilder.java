package com.github.hamishmorgan.xom;
/*
 * Copyright (c) 2010, Hamish Morgan.
 * All Rights Reserved.
 */

import nu.xom.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

@Nonnull
class BaseXomBuilder {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseXomBuilder.class);

    final NodeFactory factory;

    public BaseXomBuilder(final NodeFactory nodeFactory) {
        this.factory = checkNotNull(nodeFactory, "nodeFactory");
    }

    public NodeFactory getFactory() {
        return factory;
    }


}
