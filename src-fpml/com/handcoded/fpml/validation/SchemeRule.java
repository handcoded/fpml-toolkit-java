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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.fpml.Releases;
import com.handcoded.fpml.meta.SchemeAccess;
import com.handcoded.fpml.schemes.Scheme;
import com.handcoded.fpml.schemes.SchemeCollection;
import com.handcoded.meta.Release;
import com.handcoded.meta.Specification;
import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;

/**
 * The <CODE>SchemeRule</CODE> class provides the logic to attempt the 
 * validation of a scheme value against a set of acceptable values. The class
 * understands the FpML conventions for locally overriding a default scheme
 * URI.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.6
 */
public class SchemeRule extends Rule
{
	/**
	 * Constructs a <CODE>SchemeRule</CODE> that tests the value of the scheme
	 * code at the indicated context points.
	 * 
	 * @param	precondition	A <CODE>Precondition</CODE> instance.
	 * @param	name			The unique name for the rule.
	 * @param 	elementContext	The element name based context.
	 * @param 	typeContext		The type name based context (or <CODE>null</CODE>).
	 * @param	attributeName 	The name of attribute containing any overriding URI.
	 * @since	TFP 1.6	
	 */
	public SchemeRule (final Precondition precondition, final String name,
			ElementContext elementContext, TypeContext typeContext, final String attributeName)
	{
		super (precondition, name);
		
		this.elementContext = elementContext;
		this.typeContext = typeContext;
		this.attributeName = attributeName;
	}
	
	/**
	 * Constructs a <CODE>SchemeRule</CODE> that tests the value of the scheme
	 * code at the indicated context point.
	 * 
	 * @param	precondition	A <CODE>Precondition</CODE> instance.
	 * @param	name			The unique name for the rule.
	 * @param 	elementContext	The element name based context.
	 * @param	attributeName 	The name of attribute containing any overriding URI.
	 * @since	TFP 1.6
	 */
	public SchemeRule (final Precondition precondition, final String name,
			ElementContext elementContext, final String attributeName)
	{
		this (precondition, name, elementContext, null, attributeName);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
	{
		if (nodeIndex.hasTypeInformation () && (typeContext != null))
			return (validate (typeContext.getMatchingElements (nodeIndex), errorHandler));
		
		return (validate (elementContext.getMatchingElements (nodeIndex), errorHandler));
	}
	
	/**
	 * Performs the validation of all the elements in the provided <CODE>NodeList
	 * </CODE>. 
	 * 
	 * @param	list		The <CODE>NodeList</CODE> of candidate elements.
	 * @param	errorHandler The <CODE>ValidationErrorHandler</CODE> to use to report errors.
	 * @return	<CODE>true</CODE> if the scheme value is valid, <CODE>false</CODE>
	 * 			otherwise.
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
				if (fpml.getAttributeNode("fpmlVersion") != null) {
					version = fpml.getAttribute ("fpmlVersion");
					break;
				}
				fpml = DOM.getParent (fpml);
			}
			
			if (version == null)
				errorHandler.error ("999", list.item (0), "", "", "");
			
			Release release = Releases.FPML.getReleaseForVersion (version);
			if (release == null) {
				errorHandler.error ("305", null,
						"The document release is not in the schema set -- Check configuration",
						getDisplayName (), null);
				return (false);
			}
			
			SchemeCollection 	schemes = ((SchemeAccess) release).getSchemeCollection ();
			if (schemes == null) {
				errorHandler.error ("305", null,
						"No schemes data is available for this FpML version -- Check configuration",
						getDisplayName (), null);
				return (false);				
			}
		
			for (int index = 0, length = list.getLength (); index < length; ++index) {
				Element	context = (Element) list.item (index);
	
				// If there is no local override then look for a default on the FpML
				// element in pre 3-0 versions.
				String uri = context.getAttribute (attributeName);
				if (((uri == null) || (uri.length () == 0)) && (version != null)) {
					String [] components = version.split ("-");
					if ((components.length > 1) && (components [0].compareTo ("4") < 0)) {
						SchemeAccess provider
							= (SchemeAccess) Specification.releaseForDocument (context.getOwnerDocument ());
	
						String name = provider.getSchemeDefaults ().getDefaultAttributeForScheme (attributeName);
						if (name != null) uri = fpml.getAttribute (name);
					}
				}
	
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
	
				result = false;
			}
		}
		return (result);
	}
	
	/**
	 * The <CODE>ElementContext</CODE> that lists all the context points by name.
	 * @since	TFP 1.6
	 */
	private final ElementContext elementContext;
	
	/**
	 * The <CODE>TypeContext</CODE> that defines the context points by type.
	 * @since	TFP 1.6
	 */
	private final TypeContext	typeContext;
	
	/**
	 * The name of the attribute containing the scheme URI.
	 * @since	TFP 1.0	
	 */
	private final String	attributeName;
}