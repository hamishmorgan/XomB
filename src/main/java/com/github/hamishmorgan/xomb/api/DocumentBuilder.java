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

import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;

import javax.annotation.Nonnull;
import java.net.URI;

public interface DocumentBuilder extends ParentNodeBuilder<Document, DocumentBuilder> {

    @Nonnull
    DocumentBuilder withDocType(@Nonnull String rootElementName,
                                @Nonnull String publicID,
                                URI systemID);

    @Nonnull
    DocumentBuilder withDocType(@Nonnull String rootElementName);

    @Nonnull
    DocumentBuilder withDocType(@Nonnull DocType docType);

    @Nonnull
    DocumentBuilder withDocType(@Nonnull DocTypeBuilder docTypeBuilder);

    @Nonnull
    DocumentBuilder withRoot(@Nonnull ElementBuilder rootElement);

    @Nonnull
    DocumentBuilder withRoot(@Nonnull Element rootElement);

}
