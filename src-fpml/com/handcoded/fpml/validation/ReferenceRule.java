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

package com.handcoded.fpml.validation;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.NodeIndex;

/**
 * The <CODE>ReferenceRule</CODE> class validates an intra-document reference
 * recorded using 'ID' and 'IDREF' based attributes. When possible type
 * information is used to locate possible matching elements otherwise element
 * names are used instead.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.2
 */
public class ReferenceRule extends Rule
{
	/**
	 * Construct a <CODE>ReferenceRule</CODE> instance that will locate
	 * context and target elements based on the data provided.
	 * 
	 * @param	precondition	A <CODE>Precondition</CODE> instance.
	 * @param 	name			The unique name for the rule.
	 * @param	contextType		The schema type for the context element.
	 * @param	contextElements	An array of potential context element names.
	 * @param	targetType		The schema type for the target element.
	 * @param	targetElements	An array of potential target element names.
	 * @param	referenceAttribute The name of the attribute containing the reference.
	 * @since	TFP 1.4
	 */
	public ReferenceRule (final Precondition precondition, final String name,
			final String contextType, final String [] contextElements,
			final String targetType, final String [] targetElements,
			final String referenceAttribute)
	{
		super (precondition, name);	 
		
		this.contextType 		= contextType;
		this.contextElements 	= contextElements;
		this.targetType			= targetType;
		this.targetElements 	= targetElements;
		this.referenceAttribute = referenceAttribute;
	}
	
	/**
	 * Construct a <CODE>ReferenceRule</CODE> instance that will locate
	 * context and target elements based on the data provided.
	 * 
	 * @param	precondition	A <CODE>Precondition</CODE> instance.
	 * @param 	name			The unique name for the rule.
	 * @param	contextType		The schema type for the context element.
	 * @param	contextElements	An array of potential context element names.
	 * @param	targetType		The schema type for the target element.
	 * @param	targetElements	An array of potential target element names.
	 * @since	TFP 1.2
	 */
	public ReferenceRule (final Precondition precondition, final String name,
			final String contextType, final String [] contextElements,
			final String targetType, final String [] targetElements)
	{
		this (precondition, name, contextType, contextElements, targetType, targetElements, "href");	 
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0	
	 */
	public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
	{
		if (nodeIndex.hasTypeInformation ()) {
			String namespace = FpMLRuleSet.determineNamespace (nodeIndex);
			
			return (validate (
						nodeIndex.getElementsByType (namespace, contextType),
						nodeIndex.getElementsByType (namespace, targetType), errorHandler));
		}
	
		return (validate (
					nodeIndex.getElementsByName (contextElements),
					nodeIndex.getElementsByName (targetElements), errorHandler));
	}
	
	/**
	 * Checks the elements in context <CODE>NodeList</CODE> to see if they
	 * reference elements in the target <CODE>NodeList</CODE>.
	 *  
	 * @param 	contexts		A <CODE>NodeList</CODE> of context <CODE>Element</CODE> instances.
	 * @param 	targets			A <CODE>NodeList</CODE> of target <CODE>Element</CODE> instances.
	 * @param 	errorHandler	The <CODE>ValidationErrorHandler</CODE> used to report errors.
	 * @return	<CODE>true</CODE> if the validation succeeded.
	 * @since	TFP 1.2
	 */
	protected boolean validate (NodeList contexts, NodeList targets, ValidationErrorHandler errorHandler)
	{
		boolean		result = true;
		
		for (int index = 0, length = contexts.getLength (); index < length; ++index) {
			Element 	context	= (Element) contexts.item (index);
			Attr		href	= context.getAttributeNode (referenceAttribute);
			
			if (href == null) continue;
			
			String		hrefValue = href.getValue ();
			boolean		found	= false;
			
			for (int count = 0, limit = targets.getLength (); count < limit; ++count) {
				Element		target = (Element) targets.item (count);
				Attr		id	   = target.getAttributeNode ("id");
				
				if (id == null)  continue;
				
				if (id.getValue ().equals (hrefValue)) {
					found = true;
					break;
				}
			}
			
			if (found) continue;
			
			errorHandler.error ("305", context,
					"The @" + referenceAttribute + " attribute of a '" + contextType
					+ "' element should match with an @id attribute on a '" + targetType
					+ "' element.",
					getDisplayName (), hrefValue);
			
			result = false;
		}
		
		return (result);
	}
	
	/**
	 * Contains the name of context element's schema type.
	 * @since	TFP 1.2
	 */
	private final String	contextType;
	
	/**
	 * Contains an array of potential context element names.
	 * @since	TFP 1.2
	 */
	private final String []	contextElements;
	
	/**
	 * Contains the name of target element's schema type.
	 * @since	TFP 1.2
	 */
	private final String	targetType;
	
	/**
	 * Contains an array of potential target element names.
	 * @since	TFP 1.2
	 */
	private final String [] targetElements;
	
	/**
	 * Contains the name of the attribute used to create the reference.
	 * Normally this is 'href' but a few rules use special names.
	 * @since	TFP 1.4
	 */
	private final String	referenceAttribute;
}