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

package com.handcoded.meta;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The <CODE>DefaultSpecificationHandler</CODE> captures information from a
 * specification releases XML file and builds the objects needed to represent
 * it in memory.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.4
 */
public class DefaultSpecificationHandler extends DefaultHandler
{
	/**
	 * This method is called when the XML parser has just processed the
	 * start of an XML element. For some elements this triggers an
	 * immediate response, for others it saves information is temporary
	 * variables to be acted upon later.
	 * @since 	TFP 1.4 
	 */
	public void startElement (String ns, String localName, String qName, Attributes attributes)
	{
		if (localName.equals ("dtdRelease")) {
			version = null;
			publicId = null;
			systemId = null;
			
			rootElements.clear ();
		}
		else if (localName.equals ("schemaRelease")) {
			version = null;
			namespaceUri = null;
			schemaLocation = null;
			preferredPrefix = null;
			alternatePrefix = null;
			
			rootElements.clear ();
		}
		text.setLength (0);
	}
	
	/**
	 * This method is called when the XML parser detects that end of an
	 * element definition. It is used to trigger the processing of data
	 * cached during the elements processing.
	 * @since	TFP 1.4
	 */
	public void endElement (String ns, String localName, String qName)
	{
		if (localName.equals ("name"))
			specification = new Specification (text.toString ());
		else if (localName.equals ("version"))
			version = text.toString ();
		else if (localName.equals ("publicId"))
			publicId = text.toString ();
		else if (localName.equals ("systemId"))
			systemId = text.toString ();
		else if (localName.equals ("namespaceUri"))
			namespaceUri = text.toString ();
		else if (localName.equals ("schemaLocation"))
			schemaLocation = text.toString ();
		else if (localName.equals ("preferredPrefix"))
			preferredPrefix = text.toString ();
		else if (localName.equals ("alternatePrefix"))
			alternatePrefix = text.toString ();
		else if (localName.equals ("rootElement"))
			rootElements.add (text.toString ());
		else if (localName.equals ("dtdRelease"))
			createDTDRelease ();
		else if (localName.equals ("schemaRelease"))
			createSchemaRelease ();
	}
	
	/**
	 * Captures the characters that comprise the body of the current
	 * element.
	 * 
	 * @since	TFP 1.4
	 */
	public void characters (char [] ch, int start, int length)
	{
		text.append (ch, start, length);	
	}

	/**
	 * A text buffer used to cache element content.
	 * @since	TFP 1.4
	 */
	protected StringBuilder	text	= new StringBuilder ();
	
	/**
	 * The <CODE>Specification</CODE> instance for the file being read.
	 * @since	TFP 1.4
	 */
	protected Specification	specification = null;
	
	/**
	 * A text buffer used to cache the release version number.
	 * @since	TFP 1.4
	 */
	protected String		version;
	
	/**
	 * A text buffer used to cache a DTD's public name.
	 * @since	TFP 1.4
	 */
	protected String		publicId;
	
	/**
	 * A text buffer used to cache a DTD's system identifier.
	 * @since	TFP 1.4
	 */
	protected String		systemId;
	
	/**
	 * A text buffer used to cache a schema's namespace URI.
	 * @since	TFP 1.4
	 */
	protected String		namespaceUri;
	
	/**
	 * A text buffer used to cache a schema's default location path.
	 * @since	TFP 1.4
	 */
	protected String		schemaLocation;
	
	/**
	 * A text buffer used to cache a schema's preferred prefix.
	 * @since	TFP 1.4
	 */
	protected String		preferredPrefix;
	
	/**
	 * A text buffer used to cache a schema's alternate prefix.
	 * @since	TFP 1.4
	 */
	protected String		alternatePrefix;
	
	/**
	 * A <CODE>ArrayList</CODE> of <CODE>String</CODE> instance containing the
	 * root element names.
	 * @since	TFP 1.4
	 */
	protected ArrayList<String> rootElements = new ArrayList<> ();	
	
	/**
	 * Creates a <CODE>DTDRelease</CODE> instance from the cached information.
	 * @since	TFP 1.4
	 */
	protected void createDTDRelease ()
	{
		new DTDRelease (specification, version, publicId, systemId, (String) rootElements.get (0));
	}
	
	/**
	 * Creates a <CODE>SchemaRelease</CODE> instance from the cached information.
	 * @since	TFP 1.4
	 */
	protected void createSchemaRelease ()
	{
		String [] roots		= new String [rootElements.size ()];
		
		new SchemaRelease (specification, version, namespaceUri, schemaLocation,
				preferredPrefix, alternatePrefix, rootElements.toArray (roots));
	}
}
