package com.github.hamishmorgan.xomb.impl;

import com.github.hamishmorgan.xomb.api.DocTypeBuilder;
import com.github.hamishmorgan.xomb.api.ElementBuilder;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.NodeFactory;
import nu.xom.Nodes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentBuilderImplTest {

    private static final String VALID_ROOT_ELEMENT_NAME = "stuff";
    private static final String VALID_PUBLIC_ID = "-//OASIS//DTD DocBook V4.1//EN";
    private static final URI VALID_SYSTEM_ID = URI.create("customdocbook.dtd");

    @Mock
    private NodeFactory nodeFactoryMock;

    @InjectMocks
    private DocumentBuilderImpl instance;

    @Mock
    private Document document;

    @Before
    public void initializeMocks() {
        when(nodeFactoryMock.startMakingDocument()).thenReturn(document);
        when(nodeFactoryMock.makeDocType(anyString(), anyString(), anyString())).thenReturn(new Nodes());
    }

    @Test
    public void givenNoParameters_whenBuild_thenReturnNonNull() {
        Document result = instance.build();
        assertThat(result, equalTo(document));
    }

    @Test(expected = NullPointerException.class)
    public void givenNullDocType_whenWithDocType_thenThrowsNPE() {
        instance.withDocType((DocType) null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullDocTypeBuilder_whenWithDocType_thenThrowsNPE() {
        instance.withDocType((DocTypeBuilder) null);
    }

    @Test(expected = NullPointerException.class)
    public void givenValidDocTypeBuilder_whenWithDocType_thenBuildsDocType() {
        DocTypeBuilder docTypeBuilder = mock(DocTypeBuilder.class);
        instance.withDocType(docTypeBuilder);
        verify(docTypeBuilder).build();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullRootElementName_whenWithDocType_thenThrowsNPE() {
        instance.withDocType((String) null);
    }

    @Test
    public void givenValidRootElementName_whenBuild_thenSetsDocType() {
        instance.withDocType(VALID_ROOT_ELEMENT_NAME);
        instance.build();
        verify(nodeFactoryMock).makeDocType(eq(VALID_ROOT_ELEMENT_NAME), anyString(), anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyRootElementName_whenWithDocType_thenThrowsIAE() {
        instance.withDocType("");
    }

    @Test(expected = NullPointerException.class)
    public void givenNullPublicId_whenWithDocType_thenThrowsNPE() {
        instance.withDocType(VALID_ROOT_ELEMENT_NAME, null, VALID_SYSTEM_ID);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullSystemId_whenWithDocType_thenThrowsNPE() {
        instance.withDocType(VALID_ROOT_ELEMENT_NAME, VALID_PUBLIC_ID, null);
    }

    @Test
    public void givenValidDocType_whenBuild_thenSetsDocType() {
        DocType docType = mock(DocType.class);
        when(docType.copy()).thenReturn(docType);
        instance.withDocType(docType);
        instance.build();
        verify(document).setDocType(docType);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullElementBuilder_whenWithRoot_thenThrowsNPE() {
        instance.withRoot((ElementBuilder) null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullElement_whenWithRoot_thenThrowsNPE() {
        instance.withRoot((Element) null);
    }

    @Test
    public void givenValidRootElement_whenBuild_thenElementIsRoot() {
        Element root = mock(Element.class);
        when(root.copy()).thenReturn(root);
        instance.withRoot(root);
        instance.build();
        verify(document).setRootElement(root);
    }

    @Test
    public void givenValidRootElementBuilder_whenBuild_thenElementIsRoot() {
        Element root = mock(Element.class);
        when(root.copy()).thenReturn(root);
        ElementBuilder rootBuilder = mock(ElementBuilder.class);
        when(rootBuilder.build()).thenReturn(new Nodes(root));
        instance.withRoot(root);

        instance.build();

        verify(document).setRootElement(root);
    }

}
