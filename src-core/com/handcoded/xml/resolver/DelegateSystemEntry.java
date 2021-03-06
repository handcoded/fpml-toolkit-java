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

package com.handcoded.xml.resolver;

import java.util.Stack;

import org.xml.sax.SAXException;

/**
 * The <CODE>DelegateSystemEntry</CODE> class implements system identifier
 * delegation.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
final class DelegateSystemEntry extends RelativeEntry implements EntityRule
{
	/**
	 * Constructs a <CODE>DelegateSystem</CODE> instance that will direct
	 * matching system identifier searches to a sub-catalog.
	 *
	 * @param	parent			The containing catalog element.
	 * @param	prefix			The system identifier to be matched.
	 * @param	catalog			The URI of the sub-catalog.
	 * @param	xmlbase			The optional xml:base URI
	 * @since	TFP 1.0
	 */
	public DelegateSystemEntry (final GroupEntry parent, final String prefix,
		final String catalog, final String xmlbase)
	{
		super (parent, xmlbase);
		
		this.prefix  = prefix;
		this.catalog = catalog;
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public String applyTo (final String	publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		if (systemId.startsWith (prefix))
			return (CatalogManager.find (catalog).getDefinition ().applyRules (publicId, systemId, catalogs));

		return (null);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected String toDebug ()
	{
		return ("prefix=\"" + prefix + "\",catalog=\"" + catalog + "\"," + super.toDebug ());
	}

	/**
	 * The prefix to match against.
	 * @since	TFP 1.0
	 */
	private final String	prefix;

	/**
	 * The URI of the sub-catalog.
	 * @since	TFP 1.0
	 */
	private final String	catalog;
}