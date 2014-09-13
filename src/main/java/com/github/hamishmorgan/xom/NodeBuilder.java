package com.github.hamishmorgan.xom;

/**
 * Created by hamish on 13/09/14.
 */
interface NodeBuilder<P, B extends NodeBuilder<P, B>> {

    /**
     * Construct an XOM node for the current state of the builder.
     *
     * @return newly constructed XOM node
     * @throws IllegalStateException if the build cannot complete because a
     *                               require argument is un/miss-configured
     */
    public abstract P build();
}
