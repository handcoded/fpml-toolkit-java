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
 * Defines a standard API implemented by catalog entry types that map
 * entities.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.1
 */
interface EntityRule
{
	/**
	 * Applies the rule instance to the public or system identifier in an
	 * attempt to locate the URI of a resource with can provide the required
	 * information.
	 *
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		The stack of catalogs being processed
	 * @return	A new URI if the rule was successfully applied, otherwise
	 *			<CODE>null</CODE>.
	 * @throws	SAXException If an occur was detected during processing.
	 * @since	TFP 1.0
	 */
	public abstract String applyTo (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException;	
}