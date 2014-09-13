package com.github.hamishmorgan.xomb.api;

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

import nu.xom.Comment;
import nu.xom.ProcessingInstruction;

import javax.annotation.Nonnull;
import java.net.URI;

public interface ParentNodeBuilder<P, B extends ParentNodeBuilder<P, B>> extends NodeBuilder<P, B> {
    /**
     * @param baseURI
     * @return
     * @throws NullPointerException if baseURI is null
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    B withBaseURI(URI baseURI);

    @Nonnull
    @SuppressWarnings("unchecked")
    B clearBaseURI();

    /**
     * @param target
     * @param data
     * @return
     * @throws NullPointerException if target or data is null
     */
    @Nonnull
    B addPI(@Nonnull String target, String data);

    /**
     * @param pi
     * @return
     * @throws NullPointerException if pi is null
     */
    @Nonnull
    B addPI(@Nonnull ProcessingInstruction pi);

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
    B addComment(String data);

    /**
     * @param comment
     * @return
     * @throws NullPointerException if data is null
     */
    @Nonnull
    B addComment(@Nonnull Comment comment);
}