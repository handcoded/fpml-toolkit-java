// Copyright (C),2005-2020 HandCoded Software Ltd.
// All rights reserved.
//
// This software is licensed in accordance with the terms of the 'Open Source
// License (OSL) Version 3.0'. Please see 'license.txt' for the details.
//
// HANDCODED SOFTWARE LTD MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
// SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. HANDCODED SOFTWARE LTD SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
// OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

package com.handcoded.xml.parser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The <CODE>DOMParser</CODE> class provides a wrapper around the JAXP interface
 * allowing various types of DOM parser instances to be created with less code.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class DOMParser
{
	/**
	 * Constructs a <CODE>DomParser</CODE> configured to match the supplied
	 * arguments.
	 *
	 * @param	validating			Determines if validation is required. 
	 * @param	namespaceAware		Determines if namespace processing required.
	 * @param	schemaAware			Determines if schema processing required. 
	 * @param	xincludeAware		Determines if XInclude processing required.
	 * @param	schema				<CODE>Schema</CODE> used for validation.
	 * @param	entityResolver		<CODE>EntityResolver</CODE> instance or <CODE>null</CODE>.
	 * @param	errorHandler		<CODE>ErrorHandler</CODE> instance or <CODE>null</CODE>.
	 * @throws	ParserConfigurationException If JAXP cannot provide a suitable parser.	
	 * @since	TFP 1.8	
	 */
	public DOMParser (final boolean	validating, final boolean namespaceAware,
			final boolean schemaAware, final boolean xincludeAware, Schema schema,
			EntityResolver entityResolver, ErrorHandler	errorHandler)
		throws ParserConfigurationException
	{
		DocumentBuilderFactory		factory	= DocumentBuilderFactory.newInstance ();
		
		factory.setAttribute("http://apache.org/xml/features/validation/schema", 
			    (schemaAware || (schema != null) ? Boolean.TRUE : Boolean.FALSE));
		factory.setValidating (validating && (schema == null));	
		factory.setNamespaceAware (namespaceAware || schemaAware || xincludeAware || (schema != null));
		factory.setXIncludeAware (xincludeAware);
		factory.setSchema (schema);
		
		try {
			factory.setFeature(Feature.DEFER_NODE_EXPANSION_FEATURE_ID, false);
		}
		catch (ParserConfigurationException error) {
			logger.info ("Installed XML Parser does not support deferred node expansion feature.");
		}

		builder = factory.newDocumentBuilder ();

		if (entityResolver != null)
			builder.setEntityResolver (entityResolver);
		
		if (errorHandler != null)
			builder.setErrorHandler (errorHandler);
	}
	
	/**
	 * Constructs a <CODE>DomParser</CODE> configured to match the supplied
	 * arguments.
	 *
	 * @param	validating			Determines if validation is required. 
	 * @param	namespaceAware		Determines if namespace processing required.
	 * @param	schemaAware			Determines if schema processing required. 
	 * @param	schema				<CODE>Schema</CODE> used for validation.
	 * @param	entityResolver		<CODE>EntityResolver</CODE> instance or <CODE>null</CODE>.
	 * @param	errorHandler		<CODE>ErrorHandler</CODE> instance or <CODE>null</CODE>.
	 * @throws	ParserConfigurationException If JAXP cannot provide a suitable parser.	
	 * @since	TFP 1.0	
	 */
	public DOMParser (final boolean	validating, final boolean namespaceAware,
			final boolean schemaAware, Schema schema,
			EntityResolver entityResolver, ErrorHandler	errorHandler)
		throws ParserConfigurationException
	{
		this (validating, namespaceAware, schemaAware, false, schema, entityResolver, errorHandler);
	}
	
	/**
	 * Constructs a <CODE>DomParser</CODE> configured to match the supplied
	 * arguments.
	 *
	 * @param	validating			Determines if validation is required. 
	 * @param	namespaceAware		Determines if namespace processing required.
	 * @param	schemaAware			Determines if schema processing required. 
	 * @param	entityResolver		<CODE>EntityResolver</CODE> instance or <CODE>null</CODE>.
	 * @param	errorHandler		<CODE>ErrorHandler</CODE> instance or <CODE>null</CODE>.
	 * @throws	ParserConfigurationException If JAXP cannot provide a suitable parser.	
	 * @since	TFP 1.0	
	 */
	public DOMParser (final boolean	validating, final boolean namespaceAware,
			final boolean schemaAware, EntityResolver entityResolver, ErrorHandler	errorHandler)
		throws ParserConfigurationException
	{
		this (validating, namespaceAware, schemaAware, null, entityResolver, errorHandler);
	}
	
	/**
	 * Parses the XML document indicated by the given <CODE>InputSource</CODE>.
	 * 
	 * @param	source			The <CODE>InputSource</CODE> to parse.
	 * @return	A DOM <CODE>Document</CODE> the parse was successful,
	 * 			<CODE>null</CODE> otherwise.
	 * @throws	IOException	If an unexpected I/O error occurs.
	 * @since	TFP 1.0	
	 */
	public Document parse (
	InputSource		source)
		throws IOException
	{
		try {
			return (builder.parse (source));
		}
		catch (SAXParseException error) {
			return (null);
		}
		catch (SAXException error) {
			logger.log (Level.WARNING, "Unhandled SAX Exception", error);
			return (null);
		}
	}

	/**
	 * Parses the XML document indicated by the given <CODE>File</CODE>.
	 * 
	 * @param	file			The <CODE>File</CODE> to parse.
	 * @return	A DOM <CODE>Document</CODE> the parse was successful,
	 * 			<CODE>null</CODE> otherwise.
	 * @throws	IOException	If an unexpected I/O error occurs.
	 * @since	TFP 1.0	
	 */
	public Document parse (
	File			file)
		throws IOException
	{
		try {
			return (builder.parse (file));
		}
		catch (SAXParseException error) {
			return (null);
		}
		catch (SAXException error) {
			logger.log (Level.WARNING, "Unhandled SAX Exception", error);
			return (null);
		}		
	}

	/**
	 * Parses the XML document indicated by the given <CODE>String</CODE>.
	 * 
	 * @param	xml			The <CODE>String</CODE> to parse.
	 * @return	A DOM <CODE>Document</CODE> the parse was successful,
	 * 			<CODE>null</CODE> otherwise.
	 * @throws	IOException	If an unexpected I/O error occurs.
	 * @since	TFP 1.0	
	 */
	public Document parse (
	final String	xml)
		throws IOException
	{
		return (parse (new InputSource (new StringReader (xml))));
	}

	/**
	 * Creates a new DOM <CODE>Document</CODE> instance.
	 * 
	 * @return	A new (and empty) DOM <CODE>Document</CODE>. 
	 * @since	TFP 1.0	
	 */
	public Document newDocument ()
	{
		return (builder.newDocument ());
	}

	/**
	 * Logging instance used to record runtime problems.
	 * @since	TFP 1.0
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.xml.parser.DOMParser");
	
	/**
	 * The <CODE>DocumentBuilder</CODE> associated with this instance.
	 * @since	TFP 1.0	
	 */
	private DocumentBuilder			builder;
	
	/**
	 * Encourage Xerces to cache grammars as it parses.
	 */
	static {
		 System.setProperty (
				"org.apache.xerces.xni.parser.XMLParserConfiguration",
		    	"org.apache.xerces.parsers.XMLGrammarCachingConfiguration");
	}
}