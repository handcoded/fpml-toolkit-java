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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * The abstract <CODE>RelativeEntry</CODE> class is the common base
 * of all entries that resolve relative to xml:base.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.1
 */
abstract class RelativeEntry extends CatalogComponent
{
	/**
	 * Provides the xml:base value, possibly obtaining it from an
	 * enclosing element.
	 *  
	 * @return 	The value of the xml:base for this entry.
	 * @since	TFP 1.1
	 */
	public String getXmlBase ()
	{
		if (xmlbase != null)
			return (xmlbase);
		else {
			if (getParent () != null)
				return (getParent ().getXmlBase ());
			else
				return (null);
		}
	}
	
	/**
	 * Constructs a <CODE>RelativeEntry</CODE>.
	 * 
	 * @param 	parent			The containing element.
	 * @param 	xmlbase			Optional <CODE>xml:base</CODE> value.
	 * @since	TFP 1.0
	 */
	protected RelativeEntry (final GroupEntry parent, final String xmlbase)
	{
		super (parent);
		
		this.xmlbase = xmlbase;
	}
	
	/**
	 * Converts the <CODE>xml:base</CODE> into a <CODE>URI</CODE>
	 * for resolution operations.
	 *
	 * @return	The base <CODE>URI</CODE> for resolution.
	 * @throws 	URISyntaxException if the xml:base or the catalog URL is
	 *			invalid.
	 * @since	TFP 1.0
	 */
	protected URI baseAsUri ()
		throws URISyntaxException
	{
		return (new URI (getXmlBase ()));
	}
	
	/**
	 * Converts the instance's member values to <CODE>String</CODE> representations
	 * and concatenates them all together. This function is used by toString and
	 * may be overriden in derived classes.
	 *
	 * @return	The object's <CODE>String</CODE> representation.
	 * @since	TFP 1.0
	 */
	protected String toDebug ()
	{
		return ("xml:base=" + ((xmlbase == null) ? "null" : ("\"" + xmlbase + "\"")));
	}

	/**
	 * The xml:base value for this component or <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
	private String			xmlbase;
}