// Copyright (C),2005-2006 HandCoded Software Ltd.
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
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * The <CODE>SaxParser</CODE> class provides a wrapper around the JAXP interface
 * allowing various types of SAX parser instances to be created with less code.
 * 
 * @author	BitWise
 * @version	$Id: SAXParser.java 46 2006-10-02 10:23:18Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class SAXParser
{
	/**
	 * Constructs a <CODE>SaxParser</CODE> configured to match the supplied
	 * arguments.
	 *
	 * @param	validating			Determines if validation is required. 
	 * @param	namespaceAware		Determines if namespace processing required.
	 * @param	schemaValidating	Determines if schema support is required.
	 * @param	fullSchemaChecks	Determines if full schema checks are required.
	 * @param	entityResolver		<CODE>EntityResolver</CODE> instance or <CODE>null</CODE>.
	 * @param	errorHandler		<CODE>ErrorHandler</CODE> instance of <CODE>null</CODE>.
	 * @throws	ParserConfigurationException If JAXP cannot provide a suitable parser.	
	 * @throws	SAXException If a problem occurs configuring the SAX parser.
	 * @since	TFP 1.0	
	 */
	public SAXParser (
	final boolean	validating,
	final boolean	namespaceAware,
	final boolean 	schemaValidating,
	final boolean	fullSchemaChecks,
	EntityResolver	entityResolver,
	ErrorHandler	errorHandler)
		throws ParserConfigurationException, SAXException
	{
		synchronized (factory) {
			try {
				factory.setFeature (Feature.NAMESPACES_FEATURE_ID, namespaceAware);
			}
			catch (Exception error) {
				factory.setNamespaceAware (namespaceAware);
			}
			
			try {
				factory.setFeature (Feature.VALIDATION_FEATURE_ID, validating);
			}
			catch (Exception error) {
				factory.setValidating (validating);
			}
			
			try {
				factory.setFeature (Feature.SCHEMA_VALIDATION_FEATURE_ID, schemaValidating);
			}
			catch (Exception error) {
				logger.severe ("JAXP implementation does not support schema validation");
			}

			try {
				factory.setFeature (Feature.SCHEMA_FULL_CHECKING_FEATURE_ID, fullSchemaChecks);
			}
			catch (IllegalArgumentException error) {
				logger.severe ("JAXP implementation does not suppoty full schema checking");
			}

			reader = factory.newSAXParser ().getXMLReader ();
		}

		if (entityResolver != null)
			reader.setEntityResolver (entityResolver);

		if (errorHandler != null)
			reader.setErrorHandler (errorHandler);
	}

	/**
	 * Parses the XML document indicated by the given <CODE>InputSource</CODE>.
	 * 
	 * @param	source			The <CODE>InputSource</CODE> to parse.
	 * @param	contentHandler	The <CODE>ContentHandler</CODE> instance.
	 * @return	A DOM <CODE>Document</CODE> the parse was successful,
	 * 			<CODE>null</CODE> otherwise.
	 * @throws	IOException	If an unexpected I/O error occurs.
	 * @since	TFP 1.0	
	 */
	public boolean parse (
	InputSource		source,
	ContentHandler	contentHandler)
		throws IOException
	{
		try {
			reader.setContentHandler (contentHandler);
			reader.parse (source);
			return (true);
		}
		catch (SAXException error) {
			System.out.println ("Error: "+error);
			return (false);
		}
	}

	/**
	 * Parses the XML document indicated by the given <CODE>File</CODE>.
	 * 
	 * @param	file			The <CODE>File</CODE> to parse.
	 * @param	contentHandler	The <CODE>ContentHandler</CODE> instance.
	 * @return	A DOM <CODE>Document</CODE> the parse was successful,
	 * 			<CODE>null</CODE> otherwise.
	 * @throws	IOException	If an unexpected I/O error occurs.
	 * @since	TFP 1.0	
	 */
	public boolean parse (
	File			file,
	ContentHandler	contentHandler)
		throws IOException
	{
		return (parse (new InputSource (new FileReader (file)), contentHandler));
	}

	/**
	 * Parses the XML document indicated by the given <CODE>String</CODE>.
	 * 
	 * @param	xml				The <CODE>String</CODE> to parse.
	 * @param	contentHandler	The <CODE>ContentHandler</CODE> instance.
	 * @return	A DOM <CODE>Document</CODE> the parse was successful,
	 * 			<CODE>null</CODE> otherwise.
	 * @throws	IOException	If an unexpected I/O error occurs.
	 * @since	TFP 1.0	
	 */
	public boolean parse (
	final String	xml,
	ContentHandler	contentHandler)
		throws IOException
	{
		return (parse (new InputSource (new StringReader (xml)), contentHandler));
	}

	/**
	 * Logging instance used to record runtime problems.
	 * @since	TFP 1.0
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.xml.parser.SaxParser");
	
	/**
	 * The <CODE>SAXParserFactory</CODE> used to build parsers.
	 * @since	TFP 1.0	
	 */
	private static SAXParserFactory	factory
		= SAXParserFactory.newInstance ();

	/**
	 * The <CODE>XMLReader</CODE> associated with this instance.
	 * @since	TFP 1.0	
	 */
	private XMLReader			reader;
}