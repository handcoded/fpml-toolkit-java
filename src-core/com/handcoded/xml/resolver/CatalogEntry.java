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

/**
 * An instance of the <CODE>CatalogEntry</CODE> class represents the
 * &lt;catalog&gt; element within a parsed catalog file.
 * 
 * @author	BitWise
 * @version	$Id$
 * @since	TFP 1.1
 */
final class CatalogEntry extends GroupEntry
{
	/**
	 * Constructs a <CODE>CatalogEntry</CODE> given its containing entry
	 * and attribute values.
	 * 
	 * @param 	prefer			Optional <CODE>prefer</CODE> value.
	 * @param 	xmlbase			Optional <CODE>xml:base</CODE> value.
	 * @since	TFP 1.1
	 */
	public CatalogEntry (final String prefer, final String xmlbase)
	{
		super (null, prefer, xmlbase);
	}

	/**
	 * Adds a group entry to the catalog.
	 *
	 * @param	prefer			Optional <CODE>prefer</CODE> value.
	 * @param	xmlbase			Optional <CODE>xml:base</CODE> value.
	 * @return	The new <CODE>GroupEntry</CODE>
	 * @since	TFP 1.1
	 */
	public final GroupEntry addGroup (final String prefer, final String xmlbase)
	{
		GroupEntry		result = new GroupEntry (this, prefer, xmlbase);
		
		rules.add (result);
		return (result);
	}

	/**
	 * Adds a group entry to the catalog.
	 *
	 * @param	prefer			Optional <CODE>prefer</CODE> value.
	 * @return	The new <CODE>GroupEntry</CODE>
	 * @since	TFP 1.1
	 */
	public final GroupEntry addGroup (final String prefer)
	{
		return (addGroup (prefer, null));
	}
}