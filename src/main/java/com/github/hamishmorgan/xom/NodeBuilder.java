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
