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
 * The <CODE>UriEntry</CODE> class implements simple URI matching.
 * 
 * @author 	BitWise
 * @version	$Id: UriEntry.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
final class UriEntry extends RelativeEntry implements UriRule
{
	/**
	 * Constructs a <CODE>UriEntry</CODE> instance that will replace
	 * one URI with another.
	 * 
	 * @param	parent			The containing element.
	 * @param 	name			URI to be matched.
	 * @param	uri				The replacement URI.
	 * @param	xmlbase			Optional <CODE>xml:base</CODE> value.
	 * @since	TFP 1.0
	 */
	public UriEntry (final GroupEntry parent, final String name, final String uri,
			final String xmlbase)
	{
		super (parent, xmlbase);
		
		this.name = name;
		this.uri  = uri;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public String applyTo (final String uri, Stack<GroupEntry> catalogs)
		throws SAXException
	{
		URI					targetUri;
		URI					systemUri;
		
		// Convert the target uri value to a URI
		try {
			if (uri.startsWith ("file:"))
				targetUri = new File (uri.substring (5)).toURI ();
			else
				targetUri = baseAsUri ().resolve (new URI (uri));
		}
		catch (URISyntaxException error) {
			throw new SAXException ("Failed to normalise target URI", error);
		}
	
		// Convert the catalog name value to a URI
		try {
			systemUri = baseAsUri ().resolve (new URI (name));
		}
		catch (URISyntaxException error) {
			throw new SAXException ("Failed to normalise name", error);
		}
		
		// If they match then replace with the catalog URI
		if (systemUri.equals (targetUri)) {
			try {
				URI base = baseAsUri ();
				
				// Resolve JAR URI references the hard way 
				if (base.getScheme () != null && base.getScheme ().equals ("jar")) {
					String [] parts = base.toString().split ("!");
					return (parts [0] + "!" + new URI (parts [1]).resolve (new URI (this.uri)).toString ());
				}
				else
					return (base.resolve (new URI (this.uri)).toString ());
			}
			catch (URISyntaxException error) {
				throw new SAXException ("Failed to resolve new URI", error);
			}
		}

		return (null);
	}

	/**
	 * The URI to be matched against.
	 * @since	TFP 1.0
	 */
	private final String	name;
	
	/**
	 * The URI to map to.
	 * @since	TFP 1.0
	 */
	private final String	uri;
}