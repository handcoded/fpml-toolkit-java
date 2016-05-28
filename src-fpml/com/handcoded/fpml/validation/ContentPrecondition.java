// Copyright (C),2005-2013 HandCoded Software Ltd.
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

package com.handcoded.fpml.validation;

import java.util.Hashtable;

import org.w3c.dom.NodeList;

import com.handcoded.validation.Precondition;
import com.handcoded.xml.NodeIndex;

/**
 * The <CODE>ContentPrecondition</CODE> checks if the document being processed
 * contains specific elements determined from either their XML Schema type or
 * from a list (if type information is not available).
 * 
 * @author	BitWise
 * @version	$Id: ContentPrecondition.java 734 2013-05-31 21:28:59Z andrew_jacobs $
 * @since	TFP 1.6
 */
public class ContentPrecondition extends Precondition
{
	/**
	 * Constructions a <CODE>ContentPrecondition</CODE> instance that checks
	 * documents containing the indicated elements or types.
	 * 
	 * @param	elements		The names of elements to check for.
	 * @param	types			The names of types to check for.
	 * @since	TFP 1.6
	 */
	public ContentPrecondition (final String [] elements, final String [] types)
	{
		this.elements = elements;
		this.types = types;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.5
	 */
	@Override
	public boolean evaluate (final NodeIndex nodeIndex, Hashtable<Precondition, Boolean> cache)
	{
		if (nodeIndex.hasTypeInformation ()) {
			String ns = FpMLRuleSet.determineNamespace (nodeIndex);
			
			for (String type : types) {
				NodeList list = nodeIndex.getElementsByType (ns, type);
				
				if ((list != null) && (list.getLength () > 0)) return (true);
			}
		}
		else {
			for (String element : elements) {
				NodeList list = nodeIndex.getElementsByName (element);
				
				if ((list != null) && (list.getLength () > 0)) return (true);
			}
		}
		return (false);
	}
	
	/**
	 * A list of element names to check for.
	 * @since	TFP 1.6
	 */
	private final String []		elements;
	
	/**
	 * A list of type names to check for.
	 * @since	TFP 1.6
	 */
	private final String []		types;
}