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

import java.util.Stack;

import org.xml.sax.SAXException;

/**
 * The <CODE>RewriteUriEntry</CODE> class implements URI rewriting.
 * 
 * @author	BitWise
 * @version	$Id: RewriteUriEntry.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
final class RewriteUriEntry extends CatalogComponent implements UriRule
{
	/**
	 * Constructs a <CODE>RewriteUriEntry</CODE>.
	 * 
	 * @param 	parent			The parent <CODE>GroupEntry</CODE>.
	 * @param 	startString		The starting string to match.
	 * @param 	rewritePrefix	The replacement prefix string.
	 * @since	TFP1.0
	 */
	public RewriteUriEntry (final GroupEntry parent, final String startString, final String rewritePrefix)
	{
		super (parent);
		
		this.startString   = startString;
		this.rewritePrefix = rewritePrefix;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public String applyTo (final String uri, Stack<GroupEntry> catalogs)
		throws SAXException
	{
		if (uri.startsWith (startString))
			return (rewritePrefix + uri.substring (startString.length ()));

		return (null);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected String toDebug ()
	{
		return ("startString=\"" + startString + "\",rewritePrefix=\"" + rewritePrefix + "\"");
	}
	
	/**
	 * The prefix to match with.
	 * @since	TFP 1.0
	 */
	private final String		startString;
	
	/**
	 * The replacement prefix.
	 * @since	TFP 1.0
	 */
	private final String		rewritePrefix;
}