// Copyright (C),2005-2012 HandCoded Software Ltd.
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

import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.NodeIndex;

/**
 * The <CODE>BusinessProcessRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for business process messages.
 *
 * @author	BitWise
 * @version	$Id: BusinessProcessRules.java 610 2012-04-01 22:08:07Z andrew_jacobs $
 * @since	TFP 1.1
 */
public final class BusinessProcessRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Rule</CODE> that ensures The @href attribute on a firstPeriodStartDate
	 * must match the @id attribute of an element of type Party.
	 * <P>
	 * Applies to FpML 4.1 and later.
	 * @since	TFP 1.2
	 * @deprecated
	 */
	public static final Rule	RULE01
		= new Rule (Preconditions.R4_1__LATER, "bp-1")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex, nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FirstPeriodStartDate"), errorHandler));
				return (validate (nodeIndex, nodeIndex.getElementsByName ("firstPeriodStartDate"), errorHandler));
			}
			
			private boolean validate (NodeIndex nodeIndex, NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context 	= (Element) list.item (index);
					Attr		href;
					
					if ((context == null) || (href = context.getAttributeNode ("href"))== null) continue;
					
					Element		target	= nodeIndex.getElementById (href.getValue ());
						
					if ((target == null) || !target.getLocalName().equals("party")) {
						errorHandler.error ("305", context,
							"The @href attribute on the firstPeriodStartDate must reference a party",
							getDisplayName (), href.getValue () );
						
						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures The @href attribute on a firstPeriodStartDate
	 * must match the @id attribute of an element of type Party.
	 * <P>
	 * Applies to FpML 4.1 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE02
		= new Rule (Preconditions.R4_1__LATER, "bp-2")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex, nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FirstPeriodStartDate"), errorHandler));
				return (validate (nodeIndex, nodeIndex.getElementsByName ("firstPeriodStartDate"), errorHandler));
			}
			
			private boolean validate (NodeIndex nodeIndex, NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context 	= (Element) list.item (index);
					Attr		href;
					
					if ((context == null) || (href = context.getAttributeNode ("href"))== null) continue;
					
					Element		target	= nodeIndex.getElementById (href.getValue ());
						
					if ((target == null) || !target.getLocalName().equals("party")) {
						errorHandler.error ("305", context,
							"The @href attribute on the firstPeriodStartDate must reference a party",
							getDisplayName (), href.getValue () );
						
						result = false;
					}
				}
				return (result);
			}
		};
		
	/**
	 * Provides access to the business process validation rule set.
	 * 
	 * @return	The business process validation rule set.
	 * @since	TFP 1.1
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}
	
	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for business process messages.
	 * @since	TFP 1.1
	 */
	private static final RuleSet	rules = RuleSet.forName ("BusinessProcessRules");
	
	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.1
	 */
	private BusinessProcessRules ()
	{ }
}