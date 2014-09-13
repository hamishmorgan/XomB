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

import nu.xom.Nodes;

import javax.annotation.Nonnull;
import java.net.URI;

public interface DocTypeBuilder extends NodeBuilder<Nodes, DocTypeBuilder> {

    @Nonnull
    DocTypeBuilder withSystemID(URI systemID);

    @Nonnull
    DocTypeBuilder clearSystemID();

    @Nonnull
    DocTypeBuilder withPublicID(@Nonnull String publicID);

    @Nonnull
    DocTypeBuilder clearPublicID();

    @Nonnull
    DocTypeBuilder withInternalDTDSubset(@Nonnull String internalDTDSubset);

    @Nonnull
    DocTypeBuilder clearInternalDTDSubset();

}
