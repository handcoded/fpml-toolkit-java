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
 * The <CODE>RewriteSystemEntry</CODE> class implements system identifier
 * rewriting.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
final class RewriteSystemEntry extends CatalogComponent implements EntityRule
{
	/**
	 * Constructs a <CODE>RewriteSystem</CODE> instance that will replace
	 * the prefix of system identifier with another.
	 *
	 * @param	parent			The containing catalog element.
	 * @param	oldPrefix		The system identifier to be matched.
	 * @param	newPrefix		The replacement prefix.
	 * @since	TFP 1.0
	 */
	public RewriteSystemEntry (final GroupEntry parent, final String oldPrefix,
			final String newPrefix)
	{
		super (parent);
		
		this.oldPrefix = oldPrefix;
		this.newPrefix = newPrefix;;
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public String applyTo (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		if (systemId.startsWith (oldPrefix))
			return (newPrefix + systemId.substring (oldPrefix.length ()));

		return (null);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected String toDebug ()
	{
		return ("oldPrefix=\"" + oldPrefix + "\",newPrefix=\"" + newPrefix + "\"");
	}

	/**
	 * The URI prefix to match against.
	 * @since	TFP 1.0
	 */
	private final String	oldPrefix;

	/**
	 * The URI prefix to replace with.
	 * @since	TFP 1.0
	 */
	private final String	newPrefix;
}