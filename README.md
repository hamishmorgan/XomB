
**XomB** (XML Object Model Builder; pronounced zombie!) is construction tools for [nu.xom](http://www.xom.nu/) XML 
documents and elements. 

## Motivation 

The motivation for this tools was a number of perceived problems with the standard method 
of documents construction using the XOM library. These include:

 * The factory methods provided by NodeFactory include parameters that rarely used, resulting in large amounts 
   unnecessary boiler plate code. For example consider 
   `NodeFactory.makeAttribute(String, String, String, nu.xom.Attribute.Type)`, the `namespaceURI` parameter is usually 
   `null`, and the type parameter is usually `CDATA`. XomB mitigates this problem by 
   implemented highly ubiquitous alternatives to constructors.

 * A number of factory methods return type `nu.xom.Nodes`. This is because the `NodeFactory` is permitted to replace 
   expected node types with *any* number of other types. For example a `NodeFactory.makeAttribute` could return 
   elements instead. Sadly the `nu.xom.Nodes` object is a collection without any of normal niceties (such as 
   implementing `java.langIterable`); consequently it introduces yet more pointless boiler plate code. XomB handles 
   `Nodes` internally so the application is simplified considerably.
 
 * XOM library makes frequent use of `null`s to indicate the absence of a property, or that the property should have 
   some default value. This is generally bad design and results in errors not being detected until much later, so 
   this builder interface is non-nullable. (For previously nullable strings use the empty string instead.)
 
 * XOM is unnecessarily weakly typed, with frequent use of String names in place of proper typed instances. For example 
   URIs and charsets where encoded as Strings rather than the classes Java provides. This practice increases the 
   likelihood of hard to diagnose run time errors. This problem has been reduces by demanding the proper classes 
   where appropriate.


## Example

```java
XomB x = new XomB(new NodeFactory());
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
```

to produce

```html
<?xml version="1.0" encoding="US-ASCII"?>
<!DOCTYPE html>
<html xml:base="http://localhost/">
  <!--Root comment!-->
  <head>
    <title>404 Not Found</title>
  </head>
  <body xml:base="http://example.com/" id="mc body">
    <!--Body comment!-->
    <h1>Not Found</h1>
    <p>Abject failure.</p>
    <hr/>
    <address>Unicorn powered.</address>
  </body>
</html>
<?php run_finalizer();?>
```

## Notes

Instances of `com.github.hamishmorgan.xom.XomB` are thread-safe if and only if the supplied `nu.xom.NodeFactory` is 
also the thread safe. The default factory implementation *is* thread safe, so the supplying this object (either 
explicitly or implicitly using the default constructor) results in the XomB instance being thread-safe.

Builders instances can be used repeatedly to generate multiple independent instances. Builder state is not changed 
during building so subsequent nodes constructed will inherit properties set on previous ones.

## Todo:

 * Addition build() methods, such as build to string, and build DTD.
 
 * Not sure the semantics of the API are quite right yet.
