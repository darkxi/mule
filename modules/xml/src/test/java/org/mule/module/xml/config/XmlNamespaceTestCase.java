/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.xml.config;

import org.mule.api.endpoint.EndpointBuilder;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.jaxb.model.Person;
import org.mule.module.xml.filters.JXPathFilter;
import org.mule.module.xml.filters.JaxenFilter;
import org.mule.module.xml.transformer.JXPathExtractor;
import org.mule.module.xml.transformer.jaxb.JAXBMarshallerTransformer;
import org.mule.module.xml.transformer.jaxb.JAXBUnmarshallerTransformer;
import org.mule.module.xml.util.NamespaceManager;
import org.mule.tck.FunctionalTestCase;

public class XmlNamespaceTestCase extends FunctionalTestCase
{
    public XmlNamespaceTestCase()
    {
        setDisposeManagerPerSuite(true);
    }

    protected String getConfigResources()
    {
        return "xml-namespace-config.xml";
    }

    public void testGlobalNamespaces() throws Exception
    {
        NamespaceManager manager = muleContext.getRegistry().lookupObject(NamespaceManager.class);
        assertNotNull(manager);
        assertTrue(manager.isIncludeConfigNamespaces());
        assertEquals(5, manager.getNamespaces().size());
    }

    public void testJXPathFilterConfig() throws Exception
    {
        EndpointBuilder epb = muleContext.getRegistry().lookupEndpointBuilder("test.ep1");

        InboundEndpoint ep = epb.buildInboundEndpoint();
        assertNotNull(ep.getFilter());
        assertTrue(ep.getFilter() instanceof JXPathFilter);
        JXPathFilter filter = (JXPathFilter)ep.getFilter();
        assertEquals("/bar:foo/bar:bar", filter.getPattern());
        assertEquals(6, filter.getNamespaces().size());
        assertEquals("http://bar.com", filter.getNamespaces().get("bar"));
    }

    public void testJaxenFilterConfig() throws Exception
    {
        EndpointBuilder epb = muleContext.getRegistry().lookupEndpointBuilder("test.ep2");

        InboundEndpoint ep = epb.buildInboundEndpoint();
        assertNotNull(ep.getFilter());
        assertTrue(ep.getFilter() instanceof JaxenFilter);
        JaxenFilter filter = (JaxenFilter)ep.getFilter();
        assertEquals("/car:foo/car:bar", filter.getPattern());
        assertEquals(6, filter.getNamespaces().size());
        assertEquals("http://car.com", filter.getNamespaces().get("car"));
    }

    public void testJXPathExtractor() throws Exception
    {
        JXPathExtractor transformer = (JXPathExtractor)muleContext.getRegistry().lookupTransformer("jxpath-extractor");
        assertNotNull(transformer);
        assertNotNull(transformer.getNamespaces());
        assertEquals(6, transformer.getNamespaces().size());
        assertNotNull(transformer.getNamespaces().get("foo"));
        assertNotNull(transformer.getNamespaces().get("bar"));
    }

    public void testJaxbConfig() throws Exception
    {
        JAXBMarshallerTransformer t = (JAXBMarshallerTransformer)muleContext.getRegistry().lookupTransformer("ObjectToXml");
        assertNotNull(t);
        assertNotNull(t.getJaxbContext());

        JAXBUnmarshallerTransformer t2 = (JAXBUnmarshallerTransformer)muleContext.getRegistry().lookupTransformer("XmlToObject");
        assertNotNull(t2);
        assertEquals(Person.class, t2.getReturnDataType().getType());
        assertNotNull(t2.getJaxbContext());
    }
}
