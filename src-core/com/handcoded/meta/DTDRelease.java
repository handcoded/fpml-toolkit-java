// Copyright (C),2005-2010 HandCoded Software Ltd.
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

package com.handcoded.meta;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * The <CODE>DTDRelease</CODE> class adds support for the <CODE>DTD</CODE>
 * interface to the base <CODE>Release</CODE> class.
 *
 * @author 	BitWise
 * @version	$Id: DTDRelease.java 463 2010-11-08 23:37:44Z andrew_jacobs $
 * @since	TFP 1.0
 */
public class DTDRelease extends Release implements DTD
{
	/**
	 * Constructs a <CODE>DTDRelease</CODE> instance describing a DTD based
	 * release of a particular <CODE>Specification</CODE>.
	 *
	 * @param 	specification	A reference to the owning specification.
	 * @param 	version			The version identifier for this release.
	 * @param 	publicId		The public identifier URI.
	 * @param 	systemId		The system identifier.
	 * @param 	rootElement		The normal root element.
	 * @since 	TFP 1.0
	 */
	public DTDRelease (Specification specification, String version,
		final String publicId, final String systemId, final String rootElement)
	{
		super (specification, version, new String [] { rootElement });

		this.publicId 	 = publicId;
		this.systemId 	 = systemId;
	}

	/**
	 * Constructs a <CODE>DTDRelease</CODE> instance describing a DTD based
	 * release of a particular <CODE>Specification</CODE>.
	 *
	 * @param 	specification	A reference to the owning specification.
	 * @param 	version			The version identifier for this release.
	 * @param 	publicId		The public identifier URI.
	 * @param 	systemId		The system identifier.
	 * @param 	rootElements	The list of possible root elements.
	 * @since 	TFP 1.0
	 */
	public DTDRelease (Specification specification, String version,
		final String publicId, final String systemId, final String [] rootElements)
	{
		super (specification, version, rootElements);

		this.publicId 	 = publicId;
		this.systemId 	 = systemId;
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public final String getPublicId ()
	{
		return (publicId);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public final String getSystemId ()
	{
		return (systemId);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public Document newInstance (final String rootElement)
	{
		DOMImplementation	impl = builder.getDOMImplementation ();

		return (impl.createDocument (null, rootElement,
				impl.createDocumentType (rootElement, getPublicId (), getSystemId ())));
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.4
	 */
	public Document newFragment (final String rootElement)
	{
		DOMImplementation	impl = builder.getDOMImplementation ();

		return (impl.createDocument (null, rootElement, null));
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public boolean isInstance (Document document)
	{
		DocumentType	doctype = document.getDoctype ();

		if (doctype != null) {
			String	publicId = doctype.getPublicId ();
			Element root = document.getDocumentElement ();

			if ((publicId != null) && publicId.equals (this.publicId)
					&& hasRootElement (root.getLocalName ()))
				return (true);
		}
		return (false);
	}

	/**
	 * Java logging instance.
	 * @since 	TFP 1.0
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.xml.DTDRelease");

	/**
	 * The <CODE>DocumentBuilder</CODE> configured to create DTD based
	 * <CODE>Document</CODE> instances.
	 * @since	TFP 1.0
	 */
	private static DocumentBuilder builder	= null;

	/**
	 * The public identifier URI for this DTD.
	 * @since	TFP 1.0
	 */
	private final String		publicId;

	/**
	 * The system identifier for this DTD.
	 * @since	TFP 1.0
	 */
	private final String		systemId;

	/**
	 *
	 */
	static {
		DocumentBuilderFactory	factory = DocumentBuilderFactory.newInstance ();

		factory.setNamespaceAware (false);
		factory.setValidating (false);

		try {
			builder = factory.newDocumentBuilder ();
		}
		catch (ParserConfigurationException error) {
			logger.log(Level.SEVERE, "No suitable JAXP DOM implementation for DTD construction", error);
		}
	}
}