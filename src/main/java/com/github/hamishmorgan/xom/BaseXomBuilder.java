package com.github.hamishmorgan.xom;
/*
 * Copyright (c) 2010, Hamish Morgan.
 * All Rights Reserved.
 */

import nu.xom.NodeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

@Nonnull
class BaseXomBuilder {

    protected final Log LOG = LogFactory.getLog(getClass());

    final NodeFactory factory;

    public BaseXomBuilder(final NodeFactory nodeFactory) {
        this.factory = checkNotNull(nodeFactory, "nodeFactory");
    }

    public NodeFactory getFactory() {
        return factory;
    }


}
