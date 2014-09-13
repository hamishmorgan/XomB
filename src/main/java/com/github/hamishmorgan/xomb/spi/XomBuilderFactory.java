package com.github.hamishmorgan.xomb.spi;

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
import nu.xom.NodeFactory;

import javax.annotation.Nonnull;

public interface XomBuilderFactory {

    NodeFactory getNodeFactory();

    @Nonnull
    DocumentBuilder createDocument();

    @Nonnull
    DocTypeBuilder createDocType(@Nonnull String rootElementName);

    @Nonnull
    ElementBuilder createRoot(@Nonnull String name);

    @Nonnull
    ElementBuilder createElement(@Nonnull String name);
}
