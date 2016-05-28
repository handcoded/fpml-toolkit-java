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
 * The <CODE>DelegateUriEntry</CODE> class implements URI delegation.
 * 
 * @author 	BitWise
 * @version	$Id: DelegateUriEntry.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.1
 */
final class DelegateUriEntry extends RelativeEntry implements UriRule
{
	public DelegateUriEntry (final GroupEntry parent, final String startString,
			final String catalog, final String xmlbase)
	{
		super (parent, xmlbase);
		
		this.startString  = startString;
		this.catalog = catalog;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public String applyTo (final String uri, Stack<GroupEntry> catalogs)
		throws SAXException
	{
		if (uri.startsWith (startString))
			return (CatalogManager.find (catalog).getDefinition ().applyRules (uri, catalogs));

		return (null);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected String toDebug ()
	{
		return ("startString=\"" + startString + "\",catalog=\"" + catalog + "\"," + super.toDebug ());
	}

	/**
	 * The uri prefix to match with.
	 * @since	TFP 1.0
	 */
	private final String	startString;
	
	/**
	 * The catalog to delegate to.
	 * @since	TFP 1.0
	 */
	private final String	catalog;
}