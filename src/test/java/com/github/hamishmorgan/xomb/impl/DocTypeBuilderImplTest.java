package com.github.hamishmorgan.xomb.impl;

import com.github.hamishmorgan.xomb.api.DocTypeBuilder;
import com.github.hamishmorgan.xomb.api.MissingRequiredPropertyException;
import nu.xom.DocType;
import nu.xom.NodeFactory;
import nu.xom.Nodes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocTypeBuilderImplTest {

    private static final String VALID_ROOT_ELEMENT_NAME = "stuff";
    private static final String VALID_PUBLIC_ID = "-//OASIS//DTD DocBook V4.1//EN";
    private static final URI VALID_SYSTEM_ID = URI.create("customdocbook.dtd");
    private static final String VALID_INTERNAL_DTD_SUBSET = " <!ELEMENT foo (#PCDATA)>  ";

    @Mock
    private NodeFactory nodeFactoryMock;

    @InjectMocks
    private DocTypeBuilderImpl instance;

    @Test(expected = MissingRequiredPropertyException.class)
    public void givenRootElementNameIsUnset_whenBuild_thenThrowsException() {
        instance.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyRootElementName_whenWithRootElementName_thenThrowsIAE() {
        instance.withRootElementName("");
    }

    @Test(expected = NullPointerException.class)
    public void givenNullRootElementName_whenWithRootElementName_thenThrowsNPE() {
        instance.withRootElementName(null);
    }

    @Test
    public void givenValidRootElementName_whenWithRootElementName_thenReturnsBuilder() {
        DocTypeBuilder result = instance.withRootElementName(VALID_ROOT_ELEMENT_NAME);
        assertThat(result, equalTo((DocTypeBuilder) instance));
    }

    @Test
    public void givenValidRootElementName_whenBuild_thenInvokesFactoryWithSameName() {
        instance.withRootElementName(VALID_ROOT_ELEMENT_NAME);
        instance.build();
        verify(nodeFactoryMock).makeDocType(eq(VALID_ROOT_ELEMENT_NAME), anyString(), anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyPublicID_whenWithPublicID_thenThrowsIAE() {
        instance.withPublicID("");
    }

    @Test(expected = NullPointerException.class)
    public void givenNullPublicID_whenWithPublicID_thenThrowsNPE() {
        instance.withPublicID(null);
    }

    @Test
    public void givenValidPublicID_whenWithPublicID_thenReturnsBuilder() {
        DocTypeBuilder result = instance.withPublicID(VALID_PUBLIC_ID);
        assertThat(result, equalTo((DocTypeBuilder) instance));
    }

    @Test
    public void givenValidPublicID_whenBuild_thenInvokesFactoryWithSamePublicID() {
        instance.withRootElementName(VALID_ROOT_ELEMENT_NAME);
        instance.withPublicID(VALID_PUBLIC_ID);
        instance.build();
        verify(nodeFactoryMock).makeDocType(anyString(), eq(VALID_PUBLIC_ID), anyString());
    }

    @Test(expected = NullPointerException.class)
    public void givenNullSystemId_whenWithSystemId_thenThrowsNPE() {
        instance.withSystemID(null);
    }

    @Test
    public void givenValidSystemId_whenWithSystemId_thenReturnsBuilder() {
        DocTypeBuilder result = instance.withSystemID(VALID_SYSTEM_ID);
        assertThat(result, equalTo((DocTypeBuilder) instance));
    }

    @Test
    public void givenValidSystemId_whenBuild_thenInvokesFactoryWithSameSystemId() {
        instance.withRootElementName(VALID_ROOT_ELEMENT_NAME);
        instance.withSystemID(VALID_SYSTEM_ID);
        instance.build();
        verify(nodeFactoryMock).makeDocType(anyString(), anyString(), eq(VALID_SYSTEM_ID.toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyInternalDTDSubset_whenWithInternalDTDSubset_thenThrowsIAE() {
        instance.withInternalDTDSubset("");
    }

    @Test(expected = NullPointerException.class)
    public void givenNullInternalDTDSubset_whenWithInternalDTDSubset_thenThrowsNPE() {
        instance.withInternalDTDSubset(null);
    }

    @Test
    public void givenValidInternalDTDSubset_whenWithInternalDTDSubset_thenReturnsBuilder() {
        DocTypeBuilder result = instance.withInternalDTDSubset(VALID_INTERNAL_DTD_SUBSET);
        assertThat(result, equalTo((DocTypeBuilder) instance));
    }

    @Test
    public void givenValidInternalDTDSubset_whenBuild_thenReturnsNodes() {
        DocType docTypeNode = mock(DocType.class);
        Nodes nodes = new Nodes();
        nodes.append(docTypeNode);
        when(nodeFactoryMock.makeDocType(anyString(), anyString(), anyString())).thenReturn(nodes);

        instance.withRootElementName(VALID_ROOT_ELEMENT_NAME);
        instance.withInternalDTDSubset(VALID_INTERNAL_DTD_SUBSET);
        Nodes result = instance.build();

        assertThat(result, equalTo(nodes));
    }

    @Test
    public void givenAllValidArguments_whenBuild_thenReturnsNodes() {
        DocType docTypeNode = mock(DocType.class);
        Nodes nodes = new Nodes();
        nodes.append(docTypeNode);
        when(nodeFactoryMock.makeDocType(anyString(), anyString(), anyString())).thenReturn(nodes);

        instance.withRootElementName(VALID_ROOT_ELEMENT_NAME);
        instance.withSystemID(VALID_SYSTEM_ID);
        instance.withPublicID(VALID_PUBLIC_ID);
        instance.withRootElementName(VALID_ROOT_ELEMENT_NAME);
        instance.withInternalDTDSubset(VALID_INTERNAL_DTD_SUBSET);
        Nodes result = instance.build();

        assertThat(result, equalTo(nodes));
    }

}
