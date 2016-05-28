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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XPath;

import java.math.BigDecimal;

/**
 * The <CODE>LoanRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for syndicated loan messages.
 * <P>
 * @author 	Goonie & BitWise
 * @version	$Id: LoanRules.java 697 2012-11-30 16:57:18Z andrew_jacobs $
 * @since	TFP 1.2
 */
public final class LoanRules extends FpMLRuleSet 
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-4 and later
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R4_4__R4_X
		= Precondition.and (
				Preconditions.R4_4__R4_X,
				Preconditions.CONFIRMATION);
	
	/**
	 * A <CODE>Rule</CODE> that ensures that the effective date of a loan contract 
	 * is not after the start date of the interest period.
	 * <P>
	 * Applies to FpML 4.4 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule RULE01 = new Rule (R4_4__R4_X, "ln-1")
		{	
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.2
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "LoanContract"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("loanContract"), errorHandler));
			}
		
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "currentInterestRatePeriod", "startDate");
					Element		effective	= XPath.path (context, "effectiveDate");
					
					if ((start == null) || (effective == null) || 
						greaterOrEqual (toDate (start), toDate (effective))) continue;
										
					errorHandler.error ("305", context,
							"The effectiveDate must not be after the currentInterestRatePeriod/startDate",
							getDisplayName (), null);
					
					result = false;
				}
			
				return (result);
			}
		};
	
	/**
	 * A <CODE>Rule</CODE> that ensures that if the floating rate index contains the string 'PRIME' 
	 * then the rate fixing date must be equal to the effective date.
	 * <P>
	 * Applies to FpML 4.4 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule RULE02 = new Rule (R4_4__R4_X, "ln-2")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.2
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "DrawdownNotice"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "DrawdownNotice"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					if (exists (XPath.path (context, "loanContract"))){
						Element		effective	= XPath.path (context, "loanContract", "effectiveDate");
						Element		fixingDate	= XPath.path (context, "loanContract", "currentInterestRatePeriod", "rateFixingDate");
						Element		rateIndex	= XPath.path (context, "loanContract", "currentInterestRatePeriod", "floatingRateIndex");
					
						if ((fixingDate != null) && (effective != null) && (DOM.getInnerText (rateIndex).contains("PRIME"))) {
							if (notEqual (toDate (fixingDate) , toDate (effective))) {
								errorHandler.error ("305", context,
									"If the floatingRateIndex contains the string 'PRIME' then the currentInterestRatePeriod/rateFixingDate must be the same as the effectiveDate",
									getDisplayName (), null);
								result = false;
							}
						}
					}
				}
				
				return (result);
			}
			
		};
	
	/**
	 * A <CODE>Rule</CODE> that ensures that the rateFixingDate must not be after the startDate, 
	 * the startDate must not be after the endDate, and the rateFixingDate must not be after the endDate.
	 * <P>
	 * Applies to FpML 4.4 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule RULE03 = new Rule (R4_4__R4_X, "ln-3")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.2
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRatePeriod"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("currentInterestRatePeriod"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		end	= XPath.path (context, "endDate");
					Element		start	= XPath.path (context, "startDate");
					Element		fixingDate	= XPath.path (context, "rateFixingDate");
				
					if ((start!= null) && (fixingDate != null) && (less (toDate (start), toDate (fixingDate)))){
						errorHandler.error ("305", context,
								"The rateFixingDate must not be after the startDate",
								getDisplayName (), null);
						result = false;
					}						
					if ((end != null) && (start !=null) && (less (toDate (end), toDate (start)))) {
					
						errorHandler.error ("305", context,
								"The startDate must not be after the endDate",
								getDisplayName (), null);
						result = false;
					}
					if ((end != null) && (fixingDate !=null) && (less (toDate (end), toDate (fixingDate)))) {
						
						errorHandler.error ("305", context,
								"The rateFixingDate must not be after the endDate",
								getDisplayName (), null);
						result = false;
					}
				}
				return (result);
			}
		};
	
	/**
	 * A <CODE>Rule</CODE> that ensures that if mandatoryCostRate doesn't exist and interestRate and margin and allInRate exist, 
	 * then allInRate = margin + interestRate 
	 * <P>
	 * Applies to FpML 4.4 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule RULE04 = new Rule (R4_4__R4_X, "ln-4")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.2
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRatePeriod"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("currentInterestRatePeriod"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		costRate	= XPath.path (context, "mandatoryCostRate");
					Element		interestRate	= XPath.path (context, "interestRate");
					Element		margin	= XPath.path (context,"margin");
					Element		allInRate	= XPath.path (context,"allInRate");
				
					if ((costRate == null) && (interestRate != null) && (margin != null) && (allInRate != null)){
						BigDecimal allInRateValue = toDecimal (allInRate);
						BigDecimal marginValue = toDecimal (margin);
						BigDecimal interestRateValue = toDecimal (interestRate);
						BigDecimal marginPlusInterest = marginValue.add (interestRateValue);
						
						if (allInRateValue.compareTo (marginPlusInterest) != 0)
							errorHandler.error ("305", context,
									"The allInRate must be equal to margin + interestRate",
									getDisplayName (), null);
							result = false;
					}						
				}
				return (result);
			}			
		};
	
	/**
	 * A <CODE>Rule</CODE> that ensures that if mandatoryCostRate and interestRate and margin and allInRate exist, 
	 * then allInRate = margin + interestRate + mandatoryCostRate
	 * <P>
	 * Applies to FpML 4.4 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule RULE05 = new Rule (R4_4__R4_X, "ln-5")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.2
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRatePeriod"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("currentInterestRatePeriod"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context 		= (Element) list.item (index);
					Element		costRate		= XPath.path (context, "mandatoryCostRate");
					Element		interestRate	= XPath.path (context, "interestRate");
					Element		margin			= XPath.path (context,"margin");
					Element		allInRate		= XPath.path (context,"allInRate");
				
					if ((costRate!= null) && (interestRate != null) && (margin != null) && (allInRate !=null)){
						
						BigDecimal allInRateValue 		= toDecimal (allInRate);
						BigDecimal marginValue 			= toDecimal (margin);
						BigDecimal interestRateValue 	= toDecimal (interestRate);
						BigDecimal costRateValue 		= toDecimal (costRate);
						BigDecimal marginPlusInterest 	= marginValue.add (interestRateValue);
						BigDecimal totalMarginPlusCost 	= marginPlusInterest.add (costRateValue);
								
						if (allInRateValue.compareTo (totalMarginPlusCost) != 0) {
							errorHandler.error ("305", context,
								"The allInRate must be equal to margin + interestRate + mandatoryCostRate",
								getDisplayName (), null);
							result = false;
						}
					}						
				}
				return (result);
			}
		};
	
	public static final Rule RULE10 = new Rule (R4_4__R4_X, "ln-10")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.2
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FacilityNotice"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("facilityNotice"), errorHandler));					
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		facilityAmount	= XPath.path (context, "facilityIdentifier", "originalCommitmentAmount");
					Element		loanAmount	= XPath.path (context, "facilityCommitmentPosition", "loanContractPosition", "loanContractIdentifier", "originalAmount");
					
					if ((facilityAmount != null) && (loanAmount != null) && (isSameCurrency(facilityAmount,loanAmount))){
						Element originalCommitment = XPath.path(facilityAmount, "amount");
						Element originalAmount = XPath.path(loanAmount, "amount");
						
						if (less(toDecimal(originalCommitment), toDecimal(originalAmount))){
							errorHandler.error ("305", context,
									"The facilityIdentifier/originalCommitmentAmount/amount must be greater than or equal to the facilityCommitmentPosition/loanContractPosition/loanContractIdentifier/originalAmount/amount",
									getDisplayName (), null);		
							result = false;
						}
						
					}
				}
				return (result);
			}
		};
	
	/**
	 * Provides access to the Loan validation rule set.
	 *
	 * @return	The Loan validation rule set.
	 * @since	TFP 1.2
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}

	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.2
	 */
	private LoanRules ()
	{ }

	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for syndicated loans.
	 * @since	TFP 1.2
	 */
	private static final RuleSet	rules = RuleSet.forName ("LoanRules");
}