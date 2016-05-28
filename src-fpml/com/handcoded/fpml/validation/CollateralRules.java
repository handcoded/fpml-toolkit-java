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

import java.math.BigDecimal;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.finance.Date;
import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XPath;

/**
 * The <CODE>CollateralRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for the collateral
 * business process.
 *
 * @author	BitWise
 * @version	$Id: CollateralRules.java 728 2013-05-10 10:54:08Z andrew_jacobs $
 * @since	TFP 1.7
 */
public final class CollateralRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 5-1 or later
	 * reporting view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R5_1__LATER
		= Precondition.and (Preconditions.R5_1__LATER, Preconditions.REPORTING);
	
	/**
	 * A rule instance that ensures that the variation margin amounts
	 * agree if both are given.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE01 = new Rule (R5_1__LATER, "col-1")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "RequestMargin"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("requestMargin"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		callResult	= XPath.path (context, "marginCallResult", "variationMargin");
					Element		requirement	= XPath.path (context, "marginRequirement", "variationMargin");
					
					if (!exists (callResult) || !exists (requirement)) continue;
					
					Element		callCurrency = XPath.path (callResult, "marginCallAmount", "currency");
					Element		reqdCurrency = XPath.path (requirement, "deliver", "currency");
					
					if ((callCurrency == null) || (reqdCurrency == null) ||
							!isSameCurrency (callCurrency, reqdCurrency)) continue;
					
					Element		callAmount = XPath.path (callResult, "marginCallAmount", "amount");
					Element		deliverAmount = XPath.path (requirement, "deliver", "amount");
					Element		returnAmount = XPath.path (requirement, "return", "amount");
					
					BigDecimal	callValue = toDecimal (callAmount);
					BigDecimal	reqdValue = sum (toDecimal (deliverAmount), toDecimal (returnAmount));
					
					if ((callValue == null) || (reqdValue == null) || (callValue.compareTo (reqdValue) == 0))
						continue;
					
					errorHandler.error ("305", context,
							"The value of the variationMargin amounts are not equal",
							getDisplayName (), null);
					result = false;
				}
				return (result);
			}
		};
	
	/**
	 * A rule instance that ensures that the segregated independent amounts
	 * agree if both are given.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE02 = new Rule (R5_1__LATER, "col-2")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "RequestMargin"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("requestMargin"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		callResult	= XPath.path (context, "marginCallResult", "segregatedIndependentAmount");
					Element		requirement	= XPath.path (context, "marginRequirement", "segregatedIndependentAmount");
		
					if (!exists (callResult) || !exists (requirement)) continue;
		
					Element		callCurrency = XPath.path (callResult, "marginCallAmount", "currency");
					Element		reqdCurrency = XPath.path (requirement, "deliver", "currency");
					
					if ((callCurrency == null) || (reqdCurrency == null) ||
							!isSameCurrency (callCurrency, reqdCurrency)) continue;
					
					Element		callAmount = XPath.path (callResult, "marginCallAmount", "amount");
					Element		deliverAmount = XPath.path (requirement, "deliver", "amount");
					Element		returnAmount = XPath.path (requirement, "return", "amount");
					
					BigDecimal	callValue = toDecimal (callAmount);
					BigDecimal	reqdValue = sum (toDecimal (deliverAmount), toDecimal (returnAmount));
					
					if ((callValue == null) || (reqdValue == null) || (callValue.compareTo (reqdValue) == 0))
						continue;
					
					errorHandler.error ("305", context,
							"The value of the segregatedIndependentAmounts are not equal",
							getDisplayName (), null);
					
					result = false;
				}
				return (result);
			}
		};

	/**
	 * A rule instance that ensures that if two exposures exists they must
	 * have different directions.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE03 = new Rule (R5_1__LATER, "col-3")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "MarkToMarket"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("markToMarket"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	exposures = XPath.paths (context, "exposure");
					
					if (exposures.getLength () != 2) continue;
					
					Element		exposed1 = XPath.path ((Element) exposures.item (0), "exposedPartyReference");
					Element		obligated1 = XPath.path ((Element) exposures.item (0), "obligatedPartyReference");
					Element		exposed2 = XPath.path ((Element) exposures.item (1), "exposedPartyReference");
					Element		obligated2 = XPath.path ((Element) exposures.item (1), "obligatedPartyReference");
					
					if ((exposed1 == null) || (exposed2 == null) ||
							(obligated1 == null) || (obligated2 == null) ||
							((exposed1.getAttribute ("href").compareTo (exposed2.getAttribute ("href")) != 0) &&
							 (obligated1.getAttribute ("href").compareTo (obligated2.getAttribute ("href")) != 0)))
						continue;
					
					errorHandler.error ("305", context,
							"The exposure elements must be in different directions",
							getDisplayName (), null);
					
					result = false;
				}
				return (result);
			}
		};

	/**
	 * A rule instance that ensures that pending collateral giver and taker
	 * roles are reversed if there is more than one exchange.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE04 = new Rule (R5_1__LATER, "col-4")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CollateralBalance"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("collateral"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					
					NodeList	pending	= XPath.paths (context, "variationMargin", "pendingCollateral");
					
					if (pending.getLength () != 2) continue;
					
					Element		giver1 = XPath.path ((Element) pending.item (0), "giverPartyReference");
					Element		taker1 = XPath.path ((Element) pending.item (0), "takerPartyReference");
					Element		giver2 = XPath.path ((Element) pending.item (1), "giverPartyReference");
					Element		taker2 = XPath.path ((Element) pending.item (1), "takerPartyReference");
					
					if ((giver1 == null) || (giver2 == null) ||	(taker1 == null) || (taker2 == null) ||
							((giver1.getAttribute ("href").compareTo (giver2.getAttribute ("href")) != 0) &&
							 (taker1.getAttribute ("href").compareTo (taker2.getAttribute ("href")) != 0)))
						continue;
					
					errorHandler.error ("305", context,
							"The pending collateral elements must be in different directions",
							getDisplayName (), null);
					
					result = false;
				}
				return (result);
			}
		};
	
	/**
	 * A rule instance that ensures that the margin caller and receiver are
	 * not the same party.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE05 = new Rule (R5_1__LATER, "col-5")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "RequestMargin"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("requestMargin"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					
					Element		issuer	= XPath.path (context, "marginCallIssuerPartyReference");
					Element		receiver = XPath.path (context, "marginCallReceiverPartyReference");
			
					if ((issuer == null) || (receiver == null)) continue;
					
					if (issuer.getAttribute ("href").compareTo (receiver.getAttribute ("href")) != 0)
						continue;
					
					errorHandler.error ("305", context,
							"The call issuer and call receiver can not be the same party",
							getDisplayName (), issuer.getAttribute ("href"));
					
					result = false;
				}
				
				return (result);
			}
		};
		
	/**
	 * A rule instance that ensures that the held collateral holder and poster are
	 * have reversed roles if there are two variation margin structures.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE08 = new Rule (R5_1__LATER, "col-8")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CollateralBalance"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("collateral"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					
					NodeList	held	= XPath.paths (context, "variationMargin", "heldCollateral");
					
					if (held.getLength () != 2) continue;
					
					Element		holder1 = XPath.path ((Element) held.item (0), "holdingPartyReference");
					Element		poster1 = XPath.path ((Element) held.item (0), "postingPartyReference");
					Element		holder2 = XPath.path ((Element) held.item (1), "holdingPartyReference");
					Element		poster2 = XPath.path ((Element) held.item (1), "postingPartyReference");
					
					if ((holder1 == null) || (holder2 == null) ||	(poster1 == null) || (poster2 == null) ||
							((holder1.getAttribute ("href").compareTo (holder2.getAttribute ("href")) != 0) &&
							 (poster1.getAttribute ("href").compareTo (poster2.getAttribute ("href")) != 0)))
						continue;
					
					errorHandler.error ("305", context,
							"The held collateral elements must be in different directions",
							getDisplayName (), null);
					
					result = false;
				}
				return (result);
			}
		};
	
	/**
	 * A rule instance that ensures that the substitution issuer and receiver
	 * are different parties.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */	
	public static final Rule	RULE09 = new Rule (R5_1__LATER, "col-9")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "RequestSubstitution"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "RequestSubstitutionRetracted"), errorHandler));					
					
				return (
					    validate (nodeIndex.getElementsByName ("requestSubstitution"), errorHandler)
					  & validate (nodeIndex.getElementsByName ("requestSubstitutionRetracted"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		issuer   = XPath.path (context, "substitutionIssuerPartyReference");
					Element		receiver = XPath.path (context, "substitutionReceiverPartyReference");

					if ((issuer == null) || (receiver == null) ||
							(issuer.getAttribute ("href").compareTo (receiver.getAttribute ("href")) != 0)) continue;
						
					errorHandler.error ("305", context,
							"The substitution issuer and reciever must be different parties",
							getDisplayName (), issuer.getAttribute ("href"));
					
					result = false;
				}
				return (result);
			}
		};
		
	/**
	 * A rule instance that ensures that the interest period start date falls
	 * before the end date.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE10 = new Rule (R5_1__LATER, "col-10")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestPeriod"), errorHandler));
					
				return (validate (nodeIndex.getElementsByName ("interestPeriod"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		startDate = XPath.path (context, "startDate");
					Element		endDate   = XPath.path (context, "endDate");

					if ((startDate == null) || (endDate == null) ||
							lessOrEqual (toDate (startDate), toDate (endDate))) continue;
						
					errorHandler.error ("305", context,
							"The interest period start date must be before the end date",
							getDisplayName (), null);
					
					result = false;
				}
				return (result);
			}
		};
	
	/**
	 * A rule instance that ensures that the daily interest calculation dates
	 * are unique.
	 * <p>
	 * Applies to FpML 5-1 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE11 = new Rule (R5_1__LATER, "col-11")
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean validate (NodeIndex nodeIndex,
					ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestCalculationDetails"), errorHandler));
					
				return (validate (nodeIndex.getElementsByName ("interestCalculationDetails"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result  =	true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	dateList = XPath.paths (context, "dailyInterestCalculation", "calculationDate");

					Date		dates [] = new Date [dateList.getLength ()];
					for (int count = 0; count < dates.length; ++count)
						dates [count] = toDate (dateList.item (count));
					
					for (int outer = 0; outer < (dates.length - 1); ++outer) {
						for (int inner = outer + 1; inner < dates.length; ++inner) {
							if (dates [outer].compareTo (dates [inner]) == 0) {
								errorHandler.error ("305", context,
										"The daily interest calculation dates must be unique",
										getDisplayName (), dates [outer].toString ());
								
								result = false;
							}
						}
					}
				}
				return (result);
			}
		};		
	
	/**
	 * Provides access to the collateral validation rule set.
	 *
	 * @return	The collateral validation rule set.
	 * @since	TFP 1.7
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}

	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for the collateral process.
	 * @since	TFP 1.7
	 */
	private static final RuleSet	rules = RuleSet.forName ("CollateralRules");

	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.7
	 */
	private CollateralRules ()
	{ }
	
	/**
	 * Adds two <CODE>BigDecimal</CODE> values either of which may be null.
	 * 
	 * @param 	lhs				The left hand side.
	 * @param 	rhs				The right hand side.
	 * @return	The sum of the two values.
	 * @since	TFP 1.7
	 */
	private static BigDecimal sum (BigDecimal lhs, BigDecimal rhs)
	{
		if (lhs == null) lhs = BigDecimal.ZERO;
		if (rhs == null) rhs = BigDecimal.ZERO;
		
		return (lhs.add (rhs));
	}
}