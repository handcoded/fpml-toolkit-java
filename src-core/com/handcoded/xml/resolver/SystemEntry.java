// Copyright (C),2005-2011 HandCoded Software Ltd.
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

package com.handcoded.xml.resolver;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

import org.xml.sax.SAXException;

/**
 * The <CODE>SystemEntry</CODE> class implements simple system identifier
 * matching.
 * 
 * @author 	BitWise
 * @version	$Id: SystemEntry.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
final class SystemEntry extends RelativeEntry implements EntityRule
{
	/**
	 * Constructs a <CODE>SystemEntry</CODE> instance that will replace
	 * a system identifier with a URI.
	 *
	 * @param	parent			The containing element.
	 * @param	systemId		The system identifier to be matched.
	 * @param	uri				The replacement URI.
	 * @param	xmlbase			Optional <CODE>xml:base</CODE> value.
	 * @since	TFP 1.0
	 */
	public SystemEntry (final GroupEntry parent, final String systemId,
			final String uri, final String xmlbase)
	{
		super (parent, xmlbase);
		
		this.systemId = systemId;
		this.uri      = uri;
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public String applyTo (final String	publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		URI					targetUri;
		URI					systemUri;
		
		// Convert the target SystemId value to a URI
		try {
			if (systemId.startsWith ("file:"))
				targetUri = new File (systemId.substring (5)).toURI ();
			else
				targetUri = new URI (systemId);
		}
		catch (URISyntaxException error) {
			throw new SAXException ("Failed to normalise targetId", error);
		}
	
		// Convert the catalog SystemId value to a URI
		try {
			systemUri = new URI (this.systemId);
		}
		catch (URISyntaxException error) {
			throw new SAXException ("Failed to normalise systemId", error);
		}
		
		// If they match then replace with the catalog URI
		if (systemUri.equals (targetUri)) {
			try {
				return (baseAsUri ().resolve (new URI (uri)).toString ());
			}
			catch (URISyntaxException error) {
				throw new SAXException ("Failed to resolve target URI", error);
			}
		}

		return (null);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected String toDebug ()
	{
		return ("systemId=\"" + systemId + "\",uri=\"" + uri + "\"," + super.toDebug ());
	}

	/**
	 * The systemId to match against.
	 * @since	TFP 1.0
	 */
	private final String	systemId;

	/**
	 * The URI to replace with.
	 * @since	TFP 1.0
	 */
	private final String	uri;
}