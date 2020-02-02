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

import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XPath;

/**
 * The <CODE>PricingAndRiskRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for pricing and risk documents.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.1
 */
public final class PricingAndRiskRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Rule</CODE> that ensures the generic/@href attribute must match the @id
	 * attribute of an element of type Asset.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * 
	 * @since		TFP 1.2
	 * @deprecated
	 */
	@Deprecated
	public static final Rule	RULE01
		= new Rule (Preconditions.R4_0__LATER, "pr-1")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex, nodeIndex.getElementsByType (determineNamespace (nodeIndex), "PricingDataPointCoordinate"), errorHandler));

				return (validate (nodeIndex, nodeIndex.getElementsByName ("coordinate"), errorHandler));
			}
			
			public boolean validate (NodeIndex nodeIndex, NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					Element		generic	= XPath.path (context, "generic");
					Attr		href;
					Element		target;
					
					if ((generic == null) ||
						((href = generic.getAttributeNode ("href")) == null) ||
						((target = nodeIndex.getElementById (href.getValue ())) == null)) continue;
					
					String targetName = target.getLocalName ();
					
					if (targetName.equals ("basket") ||
						targetName.equals ("cash") ||
						targetName.equals ("commodity") ||
						targetName.equals ("deposit") ||
						targetName.equals ("bond") ||
						targetName.equals ("convertibleBond") ||		
						targetName.equals ("equity") ||
						targetName.equals ("exchangeTradedFund") ||
						targetName.equals ("index") ||
						targetName.equals ("future") ||
						targetName.equals ("fxRate") ||
						targetName.equals ("loan") ||
						targetName.equals ("mortgage") ||
						targetName.equals ("mutualFund") ||
						targetName.equals ("rateIndex") ||
						targetName.equals ("simpleCreditDefautSwap") ||
						targetName.equals ("simpleFra") ||
						targetName.equals ("simpleIrSwap") ||
						targetName.equals ("dealSummary") ||
						targetName.equals ("facilitySummary")) continue;
					
					errorHandler.error ("305", context,
						"generic/@href must match the @id attribute of an element of type Asset",
						getDisplayName (), targetName);
					
					result = false;
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures the @href attribute must match the @id
	 * attribute of an element of type PricingStructure.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * 
	 * @since		TFP 1.0
	 * @deprecated
	 */
	@Deprecated
	public static final Rule	RULE02
		= new Rule (Preconditions.R4_0__LATER, "pr-2")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex, nodeIndex.getElementsByType (determineNamespace (nodeIndex), "PaymentCalculationPeriod"), errorHandler));

				return (validate (nodeIndex, nodeIndex.getElementsByName ("paymentCalculationPeriod"), errorHandler));
			}
			
			public boolean validate (NodeIndex nodeIndex, NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					Attr		href;
					Element		target;
					
					if (((href = context.getAttributeNode ("href")) == null) ||
						((target = nodeIndex.getElementById (href.getValue ())) == null)) continue;
					
					String targetName = target.getLocalName ();
					
					if (targetName.equals ("creditCurve") ||
						targetName.equals ("fxCurve") ||
						targetName.equals ("volatilityRepresentation") ||
						targetName.equals ("yieldCurve")) continue;
					
					errorHandler.error ("305", context,
						"@href must match the @id attribute of an element of type PricingStructure",
						getDisplayName (), targetName);
					
					result = false;
				}
				return (result);
			}
		};

	/**
	 * Provides access to the pricing and risk validation rule set.
	 * 
	 * @return	The pricing and risk validation rule set.
	 * @since	TFP 1.1
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}
	
	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for pricing and risk documents.
	 * @since	TFP 1.1
	 */
	private static final RuleSet	rules = RuleSet.forName ("PricingAndRiskRules");
	
	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.1
	 */
	private PricingAndRiskRules ()
	{ }
}