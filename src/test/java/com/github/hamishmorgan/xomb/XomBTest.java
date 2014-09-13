package com.github.hamishmorgan.xomb;

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

import com.github.hamishmorgan.xomb.api.ElementBuilder;
import nu.xom.Document;
import nu.xom.NodeFactory;
import org.junit.Test;

import java.net.URI;
import java.nio.charset.Charset;

public class XomBTest {

    @Test
    public void testHTMLGen() {

        final XomB x = new XomB(new NodeFactory());

        Document doc = x.createDocument()
                .withDocType("html")
                .withBaseURI(URI.create("http://localhost/"))
                .withRoot(x.createRoot("html")
                        .addComment("Root comment!")
                        .add(x.createElement("head")
                                .add(x.createElement("title")
                                        .add("404 Not Found")))
                        .add(x.createElement("body")
                                .addComment("Body comment!")
                                .withBaseURI(URI.create("http://example.com/"))
                                .addAttribute("id", "mc body")
                                .add(x.createElement("h1")
                                        .add("Not Found"))
                                .add(x.createElement("p")
                                        .add("Abject failure."))
                                .add(x.createElement("hr"))
                                .add(x.createElement("address")
                                        .add("Unicorn powered."))))
                .addPI("php", "run_finalizer();")
                .build();


        System.out.println(XomUtil.toString(doc, Charset.forName("ASCII")));

    }

    @Test
    public void testRepeatedBuild() {
	
	XomB x = new XomB();
        ElementBuilder p = x.createElement("p");

        Document doc = x.createDocument()
                .withRoot(
                        x.createElement("root")
                                .add(p.add("A"))
                                .add(p.addComment("win"))
                                .add(p.add("B"))
                                .add(p.add("C"))
                                .add(p.add("D"))
                ).build();

        System.out.println(XomUtil.toString(doc, Charset.forName("ASCII")));
    }
   
    
    @Test
    public void testNamespaces() {

        final XomB x = new XomB(new NodeFactory());

        URI h = URI.create("http://www.w3.org/TR/html4/");
        URI f = URI.create("http://www.w3schools.com/furniture");
        Document doc = x.createDocument().withRoot(
                x.createElement("root")
                        .add(x.createElement("table")
                                .withNamespace(h).withPrefix("h")
                                .add(x.createElement("tr")
                                        .withNamespace(h).withPrefix("h")
                                        .add(x.createElement("td")
                                                .withNamespace(h).withPrefix("h")
                                                .add("Apples"))
                                        .add(x.createElement("td")
                                                .withNamespace(h).withPrefix("h")
                                                .add("Bananas"))))
                        .add(x.createElement("table")
                                .withNamespace(f).withPrefix("f")
                                .add(x.createElement("name")
                                        .withNamespace(f).withPrefix("f")
                                        .add("African Coffee Table"))
                                .add(x.createElement("width")
                                        .withNamespace(f).withPrefix("f")
                                        .add("80"))
                                .add(x.createElement("length")
                                        .withNamespace(f).withPrefix("f")
                                        .add("120")))
        ).build();

        System.out.println(XomUtil.toString(doc, Charset.forName("ASCII")));


//        <createRoot>
//
//<h:table xmlns:h="http://www.w3.org/TR/html4/">
//  <h:tr>
//    <h:td>Apples</h:td>
//    <h:td>Bananas</h:td>
//  </h:tr>
//</h:table>
//
//<f:table xmlns:f="http://www.w3schools.com/furniture">
//  <f:name>African Coffee Table</f:name>
//  <f:width>80</f:width>
//  <f:length>120</f:length>
//</f:table>
//
//</createRoot>
//        
//        

    }
    
//     @Test
//    public void testCommentEscaping() {
//
//        final XomB x = new XomB(new NodeFactory());
//
//        Document doc = x.createDocument()
//                .withDocType("html")
//                .withRoot(x.createRoot("html")
//		.addComment("Root  comment!")
//                .add(x.createElement("head")
//                /**/.add(x.createElement("title")
//                /**/.add("404 Not Found")))
//                .add(x.createElement("body")
//		/**/.addComment("Body comment!")
//                /**/.add(x.createElement("h1")
//                /*    */.add("Unicorn powered."))))
//                .build();
//
//
//        System.out.println(XomUtil.toString(doc, Charset.forName("ASCII")));
//
//    }

//        XomUtil.DocumentBuilder builder = XomUtil.documentBuilder().withDocType("html");
//
//        ElementBuilder createRoot = builder.createRootElement("html");
//
//        createRoot.createChildElement("head")
//                .createChildElement("title").add("404 Not Found");
//
//
//        ElementBuilder body = createRoot.createChildElement("body");
//        body.addAttribute("id", "mc body");
//        
//        body.createChildElement("h1").add("Not Found");
//        body.createChildElement("p").add("Abject failure and stuff.");
//        body.createChildElement("hr");
//        body.createChildElement("address").add("Unicorn powered magic webserver.");
//
//
//        Document doc = builder.build();
//
//        
//    
//                "<!DOCTYPE html>\n"
//            + "<html>"
//            + "  <head>\n"
//            + "    <title>{0,number,integer} {1}</title>\n"
//            + "  </head>"
//            + "  <body>\n"
//            + "    <h1>{1}</h1>\n"
//            + "    <p>{2}</p>\n"
//            + "    <hr>\n"
//            + "    <address>Unicorn powered magic webserver.</address>\n"
//            + "  </body>"
//            + "</html>");
//    /**
//     * Test of getFactory method, of class XomB.
//     */
//    @Test
//    public void testGetFactory() {
//	System.out.println("getFactory");
//	XomB instance = new XomB();
//	NodeFactory expResult = null;
//	NodeFactory result = instance.getFactory();
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of newDocument method, of class XomB.
//     */
//    @Test
//    public void testNewDocument() {
//	System.out.println("newDocument");
//	XomB instance = new XomB();
//	DocumentBuilder expResult = null;
//	DocumentBuilder result = instance.newDocument();
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of newRoot method, of class XomB.
//     */
//    @Test
//    public void testNewRoot_String_URI() {
//	System.out.println("newRoot");
//	String name = "";
//	URI namespace = null;
//	XomB instance = new XomB();
//	ElementBuilder expResult = null;
//	ElementBuilder result = instance.newRoot(name, namespace);
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of newRoot method, of class XomB.
//     */
//    @Test
//    public void testNewRoot_String() {
//	System.out.println("newRoot");
//	String name = "";
//	XomB instance = new XomB();
//	ElementBuilder expResult = null;
//	ElementBuilder result = instance.newRoot(name);
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of newElement method, of class XomB.
//     */
//    @Test
//    public void testNewElement_String_URI() {
//	System.out.println("newElement");
//	String name = "";
//	URI namespace = null;
//	XomB instance = new XomB();
//	ElementBuilder expResult = null;
//	ElementBuilder result = instance.newElement(name, namespace);
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of newElement method, of class XomB.
//     */
//    @Test
//    public void testNewElement_String() {
//	System.out.println("newElement");
//	String name = "";
//	XomB instance = new XomB();
//	ElementBuilder expResult = null;
//	ElementBuilder result = instance.newElement(name);
//	assertEquals(expResult, result);
//	// TODO review the generated test code and remove the default call to fail.
//	fail("The test case is a prototype.");
//    }
}
