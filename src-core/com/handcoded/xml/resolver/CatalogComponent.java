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
 * The abstract <CODE>CatalogComponent</CODE> class defines the standard API
 * provided by all catalog entry components.
 * 
 * @author 	BitWise
 * @version	$Id$
 * @since	TFP 1.1
 */
abstract class CatalogComponent
{
	/**
	 * Provides access to the parent <CODE>GroupEntry</CODE>.
	 * 
	 * @return 	The parent <CODE>GroupEntry</CODE>.
	 * @since	TFP 1.1
	 */
	public final GroupEntry getParent ()
	{
		return (parent);
	}
	
	/**
	 * Converts the instance data members to a <CODE>String</CODE> representation
	 * that can be displayed for debugging purposes.
	 *
	 * @return 	The object's <CODE>String</CODE> representation.
	 * @since	TFP 1.0
	 */ 
	public String toString ()
	{
		return (getClass ().getName () + "[" + toDebug () + "]");
	}

	/**
	 * Constructs a <CODE>CatalogComponent</CODE> instance.
	 * 
	 * @param	parent			The containing catalog element.
	 * @since	TFP 1.0
	 */
	protected CatalogComponent (final GroupEntry parent)
	{
		this.parent  = parent;
	}

	/**
	 * Returns the parent entry that represents the catalog as a whole.
	 * 
	 * @return	The entry representing the catalog.
	 * @since	TFP 1.1
	 */
	protected CatalogEntry getCatalog ()
	{
		for (GroupEntry entry = parent; entry != null; entry = entry.getParent())
			if (entry instanceof CatalogEntry) return ((CatalogEntry) entry);
		
		return (null);
	}
	
	/**
	 * Converts the instance's member values to <CODE>String</CODE> representations
	 * and concatenates them all together. This function is used by toString and
	 * may be overridden in derived classes.
	 *
	 * @return	The object's <CODE>String</CODE> representation.
	 * @since	TFP 1.0
	 */
	protected abstract String toDebug ();
	
	/**
	 * The containing catalog element or <CODE>null</CODE>.
	 */
	private final GroupEntry	parent;
}