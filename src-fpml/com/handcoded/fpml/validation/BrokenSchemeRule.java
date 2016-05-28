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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.fpml.Releases;
import com.handcoded.fpml.meta.SchemeAccess;
import com.handcoded.fpml.schemes.Scheme;
import com.handcoded.fpml.schemes.SchemeCollection;
import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;

/**
 * The <CODE>BrokenSchemeRule</CODE> class provided the logic to validate FpML
 * scheme code values where the default attribute on the root node was omitted
 * by mistake.
 * 
 * @author	BitWise
 * @version	$Id: BrokenSchemeRule.java 734 2013-05-31 21:28:59Z andrew_jacobs $
 * @since	TFP 1.0
 */
public class BrokenSchemeRule extends Rule
{
	/**
	 * Constructs a <CODE>BrokenSchemeRule</CODE> that validates a single element
	 *
	 * @param	precondition	A <CODE>Precondition</CODE> instance.
	 * @param	name			The rule name.
	 * @param	elementName		The local name of the <CODE>Element</CODE> to test.
	 * @param	attributeName 	The name of attribute containing any overriding URI.
	 * @since	TFP 1.0	
	 */
	public BrokenSchemeRule (final Precondition precondition, final String name, final String elementName, final String attributeName)
	{
		this (precondition, name, new String [] { elementName }, attributeName);
	}
	
	/**
	 * Constructs a <CODE>BrokenSchemeRule</CODE> that validates any of the listed
	 * elements
	 *
	 * @param	precondition	A <CODE>Precondition</CODE> instance.
	 * @param	name			The rule name.
	 * @param	elementNames  	The local names of the <CODE>Element</CODE> instances to test.
	 * @param	attributeName 	The name of attribute containing any overriding URI.
	 * @since	TFP 1.0	
	 */
	public BrokenSchemeRule (final Precondition precondition, final String name, final String [] elementNames, final String attributeName)
	{
		super (precondition, name);	 
		
		this.elementNames 	= elementNames;
		this.attributeName 	= attributeName;
	}
	
	/**
	 * Constructs a <CODE>BrokenSchemeRule</CODE> that validates a single element
	 *
	 * @param	name			The rule name.
	 * @param	elementName		The local name of the <CODE>Element</CODE> to test.
	 * @param	attributeName 	The name of attribute containing any overriding URI.
	 * @since	TFP 1.0	
	 */
	public BrokenSchemeRule (final String name, final String elementName, final String attributeName)
	{
		this (Precondition.ALWAYS, name, new String [] { elementName }, attributeName);
	}
	
	/**
	 * Constructs a <CODE>BrokenSchemeRule</CODE> that validates any of the listed
	 * elements
	 *
	 * @param	name			The rule name.
	 * @param	elementNames  	The local names of the <CODE>Element</CODE> instances to test.
	 * @param	attributeName 	The name of attribute containing any overriding URI.
	 * @since	TFP 1.0	
	 */
	public BrokenSchemeRule (final String name, final String [] elementNames, final String attributeName)
	{
		this (Precondition.ALWAYS, name, elementNames, attributeName);	 
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0	
	 */
	public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
	{
		boolean		result = true;
		
		for (int index = 0; index < elementNames.length; ++index)
			result &= validate (nodeIndex.getElementsByName (elementNames [index]), errorHandler);	
	
		return (result);
	}	 
	
	/**
	 * Validates the data content of a set of elements by locating the scheme
	 * identified by the scheme attribute.
	 * 
	 * @param	list			A <CODE>NodeList</CODE> of elements to check.
	 * @param	errorHandler	The <CODE>ErrorHandler</CODE> used to report issues.
	 * @return	<CODE>true</CODE> if the code values pass the checks,
	 * 			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.0	
	 */
	protected boolean validate (NodeList list, ValidationErrorHandler errorHandler)
	{
		boolean		result 	= true;
		
		if (list.getLength () > 0) {
			Element fpml	= DOM.getParent ((Element) list.item(0));
			String version	= null;
			
			// Find the FpML root node
			while (fpml != null) {
				if (fpml.getLocalName().equals("FpML")) {
					version = fpml.getAttribute ("version");
					break;
				}
				if (fpml.hasAttribute ("fpmlVersion")) {
					version = fpml.getAttribute ("fpmlVersion");
					break;
				}
				fpml = DOM.getParent (fpml);
			}
						
			SchemeCollection 	schemes =
				((SchemeAccess) (Releases.FPML.getReleaseForVersion (version))).getSchemeCollection ();
				
			for (int index = 0; index < list.getLength (); ++index) {
				Element	context = (Element) list.item (index);
				String	uri		= context.getAttribute (attributeName);
				
				if ((uri == null) || (uri.length () == 0)) {
					errorHandler.error ("305", context,
						"A qualifying scheme URI has not been defined for this element",
						getDisplayName (), context.getLocalName ());
					
					result = false;
					continue;
				}
				
				Scheme scheme = schemes.findSchemeForUri (uri);
				if (scheme == null) {
					errorHandler.error ("305", context,
						"An unrecognized scheme URI has been used as a qualifier",
						getDisplayName (), uri);
					
					result = false;
					continue;
				}
				
				String value = DOM.getInnerText (context).trim ();
				if (scheme.isValid (value)) continue;
				
				errorHandler.error ("305", context,
					"The code value '" + value + "' is not valid in scheme '" + scheme.getUri () + "'",
					getDisplayName (), value);	
			}
		}
		return (result);
	}

	/**
	 * A list of local element names that this rule will validate.
	 * @since	TFP 1.0	
	 */
	private final String []	elementNames;
	
	/**
	 * The name of the attribute containing the scheme URI.
	 * @since	TFP 1.0	
	 */
	private final String	attributeName;
}
