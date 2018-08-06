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

import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.finance.Calendar;
import com.handcoded.finance.Date;
import com.handcoded.finance.DateRoll;
import com.handcoded.finance.Interval;
import com.handcoded.finance.Period;
import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XPath;

/**
 * The <CODE>FxRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for foreign exchange products.
 * <P>
 * These rules cope with DTD based instances where no type information is
 * available and schema based instances where it is (including where FpML renamed
 * the types between 4.0 and 4.1).
 *
 * @author 	BitWise
 * @version	$Id: FxRules.java 744 2013-07-02 21:54:56Z andrew_jacobs $
 * @since	TFP 1.2
 */
public final class FxRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 3-0 thru
	 * 4-* confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R3_0__R4_X
		= Precondition.and (
				Preconditions.R3_0__R4_X,
				Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 3-0 or later
	 * confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R3_0__LATER
		= Precondition.and (
				Preconditions.R3_0__LATER,
				Preconditions.CONFIRMATION);
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-*
	 * confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R4_0__R4_X
		= Precondition.and (
				Preconditions.R4_0__R4_X,
				Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-0 or later
	 * confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R4_0__LATER
		= Precondition.and (
				Preconditions.R4_0__LATER,
				Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-2 thru 4.*
	 * confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R4_2__R4_X
		= Precondition.and (
				Preconditions.R4_2__R4_X,
				Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-2 or later
	 * confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R4_2__LATER
		= Precondition.and (
				Preconditions.R4_2__LATER,
				Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 5-1 or later
	 * confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R5_1__LATER
		= Precondition.and (
				Preconditions.R5_1__LATER,
				Preconditions.CONFIRMATION);
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects documents containing
	 * at least one FX single leg.
	 * @since	TFP 1.6
	 */
	private static final Precondition	FX_SINGLE_LEG
		= new ContentPrecondition (
				new String [] { "fxSingleLeg", },
				new String [] { "FxLeg", "FXLeg", "FxSingleLeg" }
				);
				
	/**
	 * A <CODE>Precondition</CODE> instance that detects documents containing
	 * at least one FX swap leg.
	 * @since	TFP 1.6
	 */
	private static final Precondition	FX_SWAP_LEG
		= new ContentPrecondition (
				new String [] { "nearLeg", "farLeg"},
				new String [] { "FxSwapLeg" }
				);
				
	/**
	 * A <CODE>Precondition</CODE> instance that detects documents containing
	 * at least one trade.
	 * @since	TFP 1.6
	 */
	private static final Precondition	TRADE
		= new ContentPrecondition (
				new String [] { "trade" },
				new String [] { "Trade" }
				);
				
	/**
	 * A <CODE>Precondition</CODE> instance that detects documents containing
	 * at least one contract.
	 * @since	TFP 1.6
	 */
	private static final Precondition	CONTRACT
		= new ContentPrecondition (
				new String [] { "contract" },
				new String [] { "Contract" }
				);
	
	/**
	 * A <CODE>Rule</CODE> that ensures that the rate is positive.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE01_OLD
		= new Rule (R3_0__R4_X, "fx-1[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ExchangeRate"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("exchangeRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element			context = (Element) list.item (index);
					Element			rate	= XPath.path (context, "rate");
					
					if ((rate == null) || isPositive (rate)) continue;
					
					errorHandler.error ("305", context,
							"The rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};
	
	/**
	 * A <CODE>Rule</CODE> that ensures that if forwardPoints exists then
	 * spotRate should also exist.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE02_OLD
		= new Rule (R3_0__R4_X, "fx-2[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ExchangeRate"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("exchangeRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element 	forward = XPath.path (context, "forwardPoints");
					Element		spot	= XPath.path (context, "spotRate");
					
					if (!((forward != null) && (spot == null))) continue;
					
					errorHandler.error ("305", context,
							"If forwardPoints exists then spotRate should also exist.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures that if both forwardPoints and spotRate
	 * exist then rate equals 'spotRate + forwardRate'.
	 * <P>
	 * Applies to FpML 3.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE03
		= new Rule (R3_0__LATER, "fx-3")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ExchangeRate"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("exchangeRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element 	forward = XPath.path (context, "forwardPoints");
					Element		spot	= XPath.path (context, "spotRate");
					Element		rate	= XPath.path (context, "rate");
					
					if ((rate == null) || (forward == null) || (spot == null)) continue;
					
					if (toDecimal (rate).compareTo (toDecimal (spot).add (toDecimal (forward))) == 0)
						continue;
					
					errorHandler.error ("305", context,
							"Sum of spotRate and forwardPoints does not equal rate.",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures sideRates/baseCurrency must be neither
	 * quotedCurrencyPair/currency1 nor quotedCurrencyPair/currency2.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE04_OLD
		= new Rule (R3_0__R4_X, "fx-4[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ExchangeRate"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("exchangeRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element 	baseCcy = XPath.path (context, "sideRates", "baseCurrency");
					Element		ccy1	= XPath.path (context, "quotedCurrencyPair", "currency1");
					Element		ccy2	= XPath.path (context, "quotedCurrencyPair", "currency2");
					
					if ((baseCcy == null) || (ccy2 == null) || (ccy2 == null)) continue;
					
					if (equal (baseCcy, ccy1) || equal (baseCcy, ccy2)) {
						errorHandler.error ("305", context,
								"The side rate base currency must not be one of the trade currencies.",
								getDisplayName (), toToken (baseCcy));
					
						result = false;
					}
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures sideRates/currency1SideRate/currency
	 * must be the same as quotedCurrencyPair/currency1.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE05_OLD
		= new Rule (R3_0__R4_X, "fx-5[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ExchangeRate"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("exchangeRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy		= XPath.path (context, "quotedCurrencyPair", "currency1");
					Element 	ccy1 	= XPath.path (context, "sideRates", "currency1SideRate", "currency");
					
					if ((ccy == null) || (ccy1 == null) || equal (ccy, ccy1)) continue;
					
					errorHandler.error ("305", context,
							"The side rate currency1 '" + toToken (ccy1) +
							"' must be the same as trade currency1 '" + toToken (ccy) + "'.",
							getDisplayName (), null);
				
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures sideRates/currency2SideRate/currency
	 * must be the same as quotedCurrencyPair/currency2.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE06_OLD
		= new Rule (R3_0__R4_X, "fx-6[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ExchangeRate"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("exchangeRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy		= XPath.path (context, "quotedCurrencyPair", "currency2");
					Element 	ccy1 	= XPath.path (context, "sideRates", "currency2SideRate", "currency");
					
					if ((ccy == null) || (ccy1 == null) || equal (ccy, ccy1)) continue;
					
					errorHandler.error ("305", context,
							"The side rate currency2 '" + toToken (ccy1) +
							"' must be the same as trade currency2 '" + toToken (ccy) + "'.",
							getDisplayName (), null);
				
					result = false;
				}
				
				return (result);
			}
		};					

	/**
	 * A <CODE>Rule</CODE> that ensures triggerRate is positive.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE07_OLD
		= new Rule (R3_0__R4_X, "fx-7[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAmericanTrigger"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAmericanTrigger"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxAmericanTrigger"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		rate	= XPath.path (context, "triggerRate");
					
					if ((rate == null) || isPositive (rate)) continue;
										
					errorHandler.error ("305", context,
							"The trigger rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if observationStartDate and observationEndDate
	 * both exist then observationStartDate &lt;= observationEndDate.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE08_OLD
		= new Rule (R3_0__R4_X, "fx-8[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAmericanTrigger"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAmericanTrigger"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxAmericanTrigger"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "observationStartDate");
					Element		end		= XPath.path (context, "observationEndDate");
					
					if ((start == null) || (end == null) || 
						lessOrEqual (toDate (start), toDate (end))) continue;
										
					errorHandler.error ("305", context,
							"The observationStartDate must not be after the observationEndDate",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures if observationStartDate and observationEndDate
	 * both exist then observationStartDate &lt;= observationEndDate.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.5
	 */
	public static final Rule 	RULE08
		= new Rule (R5_1__LATER, "fx-8")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxTouch"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("touch"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "observationStartDate");
					Element		end		= XPath.path (context, "observationEndDate");
					
					if ((start == null) || (end == null) || 
						lessOrEqual (toDate (start), toDate (end))) continue;
										
					errorHandler.error ("305", context,
							"The observationStartDate must not be after the observationEndDate",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures if observationStartDate and observationEndDate
	 * both exist then observationStartDate &lt;= observationEndDate.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE09_OLD
		= new Rule (R3_0__R4_X, "fx-9[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAverageRateObservationSchedule"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateObservationSchedule"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("averageRateObservationSchedule"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "observationStartDate");
					Element		end		= XPath.path (context, "observationEndDate");
					
					if ((start == null) || (end == null) || 
						lessOrEqual (toDate (start), toDate (end))) continue;
										
					errorHandler.error ("305", context,
							"The observationStartDate must not be after the observationEndDate",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if ostartDate and endDate
	 * both exist then startDate &lt;= endDate.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE09
		= new Rule (R5_1__LATER, "fx-9")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateObservationSchedule"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("averageRateObservationSchedule"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "startDate");
					Element		end		= XPath.path (context, "endDate");
					
					if ((start == null) || (end == null) || 
						lessOrEqual (toDate (start), toDate (end))) continue;
										
					errorHandler.error ("305", context,
							"The startDate must not be after the endDate",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures the observation period defined by
	 * observationStartDate and observationEndDate should be an integer
	 * multiple of the calculationPeriodFrequency.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE10_OLD
		= new Rule (R3_0__R4_X, "fx-10[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAverageRateObservationSchedule"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateObservationSchedule"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("averageRateObservationSchedule"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "observationStartDate");
					Element		end		= XPath.path (context, "observationEndDate");
					Element		period	= XPath.path (context, "calculationPeriodFrequency");
					
					if ((start == null) || (end == null) || (period == null) ||
							toInterval (period).dividesDates (toDate (start), toDate (end))) continue;
								
					errorHandler.error ("305", context,
							"The observation period is not a multiple of the calculationPeriodFrequency",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the observation period defined by
	 * startDate and endDate should be an integer multiple of the calculationPeriodFrequency.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE10
		= new Rule (R5_1__LATER, "fx-10")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateObservationSchedule"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("averageRateObservationSchedule"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "startDate");
					Element		end		= XPath.path (context, "endDate");
					Element		period	= XPath.path (context, "calculationPeriodFrequency");
					
					if ((start == null) || (end == null) || (period == null) ||
							toInterval (period).dividesDates (toDate (start), toDate (end))) continue;
								
					errorHandler.error ("305", context,
							"The observation period is not a multiple of the calculationPeriodFrequency",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures each observationDate is unique.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE11_OLD
		= new Rule (R3_0__R4_X, "fx-11[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAverageRateOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxAverageRateOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	nodes	= XPath.paths (context, "observedRates", "observationDate");
					
					int			limit	= nodes.getLength ();
					Date []		dates	= new Date [limit];
					
					for (int count = 0; count < limit; ++count)
						dates [count] = toDate (nodes.item(count));					
					
					for (int outer = 0; outer < (limit - 1); ++outer) {
						for (int inner = outer + 1; inner < limit; ++inner) {
							if (equal (dates [outer], dates [inner]))
								errorHandler.error ("305", nodes.item (inner),
										"Duplicate observation date",
										getDisplayName (), toToken (nodes.item (inner)));
							
							result = false;
						}
					}
					dates = null;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures each observationDate is unique.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.5
	 */
	public static final Rule 	RULE11
		= new Rule (R5_1__LATER, "fx-11")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAsianFeature"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("asian"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	nodes	= XPath.paths (context, "rateObservation", "date");
					
					int			limit	= nodes.getLength ();
					Date []		dates	= new Date [limit];
					
					for (int count = 0; count < limit; ++count)
						dates [count] = toDate (nodes.item(count));					
					
					for (int outer = 0; outer < (limit - 1); ++outer) {
						for (int inner = outer + 1; inner < limit; ++inner) {
							if (equal (dates [outer], dates [inner]))
								errorHandler.error ("305", nodes.item (inner),
										"Duplicate observation date",
										getDisplayName (), toToken (nodes.item (inner)));
							
							result = false;
						}
					}
					dates = null;
				}
				
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures each observationDate matches one defined
	 * by the schedule.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE12_OLD
		= new Rule (R3_0__R4_X, "fx-12[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAverageRateOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxAverageRateOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context 	= (Element) list.item (index);
					Element		schedule	= XPath.path (context, "averageRateObservationSchedule");
					
					if (schedule == null) continue;
					
					Element		start	= XPath.path (schedule, "observationStartDate");
					Element		end		= XPath.path (schedule, "observationEndDate");
					Element		freq	= XPath.path (schedule, "calculationPeriodFrequency");
					Element		roll	= XPath.path (freq, "rollConvention");
					
					if ((start == null) || (end == null) || (freq == null) || (roll == null)) continue;
					
					Date [] 	dates	= generateSchedule (toDate (start), toDate (end),
							toInterval (freq), DateRoll.forName (toToken (roll)), null);
					
					NodeList	nodes	= XPath.paths (context, "observedRates", "observationDate");
										
					for (int count = 0; count < nodes.getLength(); ++count) {
						Element 	observed = (Element) nodes.item (count);
						Date		date 	 = toDate (observed);
						
						boolean		found = false;
						for (int match = 0; match < dates.length; ++match) {
							if (equal (date, dates [match])) {
								found = true;
								break;
							}
						}
						
						if (!found) {
							errorHandler.error ("305", observed,
									"Observation date '" + toToken (observed) +
									"' does not match with the schedule.",
									getDisplayName (), toToken(observed));
							
							result = false;
						}
					}
					dates = null;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures each observationDate matches one defined
	 * by the schedule.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE12
		= new Rule (R5_1__LATER, "fx-12")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAsianFeature"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("asian"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context 	= (Element) list.item (index);
					Element		schedule	= XPath.path (context, "observationSchedule");
					
					if (schedule == null) continue;
					
					Element		start	= XPath.path (schedule, "startDate");
					Element		end		= XPath.path (schedule, "endDate");
					Element		freq	= XPath.path (schedule, "calculationPeriodFrequency");
					Element		roll	= XPath.path (freq, "rollConvention");
					
					if ((start == null) || (end == null) || (freq == null) || (roll == null)) continue;
					
					Date [] 	dates	= generateSchedule (toDate (start), toDate (end),
							toInterval (freq), DateRoll.forName (toToken (roll)), null);
					
					NodeList	nodes	= XPath.paths (context, "rateObservation", "date");
										
					for (int count = 0; count < nodes.getLength(); ++count) {
						Element 	observed = (Element) nodes.item (count);
						Date		date 	 = toDate (observed);
						
						boolean		found = false;
						for (int match = 0; match < dates.length; ++match) {
							if (equal (date, dates [match])) {
								found = true;
								break;
							}
						}
						
						if (!found) {
							errorHandler.error ("305", observed,
									"Observation date '" + toToken (observed) +
									"' does not match with the schedule.",
									getDisplayName (), toToken(observed));
							
							result = false;
						}
					}
					dates = null;
				}
				
				return (result);
			}
		};
	/**
	 * A <CODE>Rule</CODE> that if explicit observation dates have been given
	 * then the observed rates must correspond to them.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE13_OLD
		= new Rule (R3_0__R4_X, "fx-13[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAverageRateOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxAverageRateOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context 	= (Element) list.item (index);
					NodeList	schedule	= XPath.paths (context, "averageRateObservationDate", "observationDate");
					int			limit		= (schedule != null) ? schedule.getLength () : 0;
					
					if (limit == 0) continue;
					
					Date []		dates	= new Date [limit];
					
					for (int count = 0; count < limit; ++count)
						dates [count] = toDate (schedule.item (count));					
					
					NodeList	nodes	= XPath.paths (context, "observedRates", "observationDate");
										
					for (int count = 0; count < nodes.getLength(); ++count) {
						Element 	observed = (Element) nodes.item (count);
						Date		date 	 = toDate (observed);
						
						boolean		found = false;
						for (int match = 0; match < dates.length; ++match) {
							if (equal (date, dates [match])) {
								found = true;
								break;
							}
						}
						
						if (!found) {
							errorHandler.error ("305", observed,
									"Observation date '" + toToken (observed) +
									"' does not match with a defined observationDate.",
									getDisplayName (), toToken(observed));
							
							result = false;
						}
					}
					dates = null;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures observationStartDate and observationEndDate
	 * both exist then observationStartDate &lt;= observationEndDate.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE14_OLD
		= new Rule (R3_0__R4_X, "fx-14[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXBarrier"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxBarrier"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxBarrier"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "observationStartDate");
					Element		end		= XPath.path (context, "observationEndDate");
					
					if ((start == null) || (end == null) || 
						lessOrEqual (toDate (start), toDate (end))) continue;
										
					errorHandler.error ("305", context,
							"The observationStartDate must not be after the observationEndDate",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures observationStartDate and observationEndDate
	 * both exist then observationStartDate &lt;= observationEndDate.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.5
	 */
	public static final Rule 	RULE14
		= new Rule (R5_1__LATER, "fx-14")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxBarrierFeature"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("barrier"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	= XPath.path (context, "observationStartDate");
					Element		end		= XPath.path (context, "observationEndDate");
					
					if ((start == null) || (end == null) || 
						lessOrEqual (toDate (start), toDate (end))) continue;
										
					errorHandler.error ("305", context,
							"The observationStartDate must not be after the observationEndDate",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures spotRate is positive if it exists.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE15_OLD
		= new Rule (R3_0__R4_X, "fx-15[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXBarrierOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxBarrierOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxBarrierOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		rate	= XPath.path (context, "spotRate");
					
					if ((rate == null) || isPositive (rate)) continue;
										
					errorHandler.error ("305", context,
							"The spot rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};
				
	/**
	 * Context: FxDigitalOption (Complex Type)
	 * <P>
	 * A <CODE>Rule</CODE> that ensures spotRate is positive if it exists.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE16_OLD
		= new Rule (R3_0__R4_X, "fx-16[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXDigitalOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxDigitalOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxDigitalOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		rate	= XPath.path (context, "spotRate");
					
					if ((rate == null) || isPositive (rate)) continue;
										
					errorHandler.error ("305", context,
							"The spot rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures triggerRate is positive.
	 * <P>
	 * Applies to FpML 3.0 up to 4.9.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE17_OLD
		= new Rule (R3_0__R4_X, "fx-17[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXEuropeanTrigger"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxEuropeanTrigger"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxEuropeanTrigger"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		rate	= XPath.path (context, "triggerRate");
					
					if ((rate == null) || isPositive (rate)) continue;
										
					errorHandler.error ("305", context,
							"The trigger rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * Context: FxLeg (Complex Type)
	 * <P>
	 * A <CODE>Rule</CODE> that ensures payer and receiver are correct.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE18_OLD
		= new Rule (R3_0__R4_X, "fx-18[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXLeg"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSingleLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy1Pay	= XPath.path (context, "exchangedCurrency1", "payerPartyReference");
					Element		ccy1Rec	= XPath.path (context, "exchangedCurrency1", "receiverPartyReference");
					Element		ccy2Pay	= XPath.path (context, "exchangedCurrency2", "payerPartyReference");
					Element		ccy2Rec	= XPath.path (context, "exchangedCurrency2", "receiverPartyReference");
					
					if ((ccy1Pay == null) || (ccy1Rec == null) ||
						(ccy2Pay == null) || (ccy2Rec == null)) continue;
					
					if (equal (DOM.getAttribute(ccy1Pay, "href"), DOM.getAttribute(ccy2Rec, "href")) &&
						equal (DOM.getAttribute(ccy2Pay, "href"), DOM.getAttribute(ccy1Rec, "href"))) continue;
										
					errorHandler.error ("305", context,
							"Exchanged currency payers and receivers don't match.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
	
	/**
	 * Context: FxLeg (Complex Type)
	 * <P>
	 * A <CODE>Rule</CODE> that ensures payer and receiver are correct.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE18
		= new Rule (Precondition.and (FX_SINGLE_LEG, R5_1__LATER), "fx-18")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSingleLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSingleLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy1PayPty	= XPath.path (context, "exchangedCurrency1", "payerPartyReference");
					Element		ccy1RecPty	= XPath.path (context, "exchangedCurrency1", "receiverPartyReference");
					Element		ccy2PayPty	= XPath.path (context, "exchangedCurrency2", "payerPartyReference");
					Element		ccy2RecPty	= XPath.path (context, "exchangedCurrency2", "receiverPartyReference");
					
					if ((ccy1PayPty == null) || (ccy1RecPty == null) ||
						(ccy2PayPty == null) || (ccy2RecPty == null)) continue;

					Element		ccy1PayAcc	= XPath.path (context, "exchangedCurrency1", "payerAccountReference");
					Element		ccy1RecAcc	= XPath.path (context, "exchangedCurrency1", "receiverAccountReference");
					Element		ccy2PayAcc	= XPath.path (context, "exchangedCurrency2", "payerAccountReference");
					Element		ccy2RecAcc	= XPath.path (context, "exchangedCurrency2", "receiverAccountReference");
					
					if (equal (DOM.getAttribute(ccy1PayPty, "href"), DOM.getAttribute(ccy2RecPty, "href")) &&
						equal (DOM.getAttribute(ccy2PayPty, "href"), DOM.getAttribute(ccy1RecPty, "href")) &&
						((!exists (ccy1PayAcc) && !exists (ccy2RecAcc)) || equal (DOM.getAttribute (ccy1PayAcc, "href"), DOM.getAttribute (ccy2RecAcc, "href"))) &&
						((!exists (ccy2PayAcc) && !exists (ccy1RecAcc)) || equal (DOM.getAttribute (ccy2PayAcc, "href"), DOM.getAttribute (ccy1RecAcc, "href"))))
						continue;
										
					errorHandler.error ("305", context,
							"Exchanged currency payers and receivers don't match.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
		
	/**
	 * Context: FxLeg (Complex Type)
	 * <P>
	 * A <CODE>Rule</CODE> that ensures exchanged currencies are different.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE19_OLD
		= new Rule (Precondition.and (FX_SINGLE_LEG, R3_0__R4_X), "fx-19[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXLeg"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSingleLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy1	= XPath.path (context, "exchangedCurrency1", "paymentAmount", "currency");
					Element		ccy2	= XPath.path (context, "exchangedCurrency2", "paymentAmount", "currency");
					
					if ((ccy1 == null) || (ccy2 == null) || !isSameCurrency (ccy1, ccy2)) continue;
					
					errorHandler.error ("305", context,
							"Exchanged currencies must be different.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
		
	/**
	 * Context: FxLeg (Complex Type)
	 * <P>
	 * A <CODE>Rule</CODE> that ensures exchanged currencies are different.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE19
		= new Rule (Precondition.and (FX_SINGLE_LEG, R5_1__LATER), "fx-19")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSingleLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSingleLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy1	= XPath.path (context, "exchangedCurrency1", "paymentAmount", "currency");
					Element		ccy2	= XPath.path (context, "exchangedCurrency2", "paymentAmount", "currency");
					
					if ((ccy1 == null) || (ccy2 == null) || !isSameCurrency (ccy1, ccy2)) continue;
					
					errorHandler.error ("305", context,
							"Exchanged currencies must be different.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures split settlement dates are different.
	 * <P>
	 * Applies to FpML 3.0 up tp 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE20_OLD
		= new Rule (Precondition.and (FX_SINGLE_LEG, R3_0__R4_X), "fx-20[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXLeg"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSingleLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		date1	= XPath.path (context, "currency1ValueDate");
					Element		date2	= XPath.path (context, "currency2ValueDate");
					
					if ((date1 == null) || (date2 == null)) continue;
					
					if (notEqual (toDate (date1), toDate (date2))) continue;
										
					errorHandler.error ("305", context,
							"currency1ValueDate and currency2ValueDate must be different.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures split settlement dates are different.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE20
		= new Rule (Precondition.and (FX_SINGLE_LEG, R5_1__LATER), "fx-20")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSingleLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSingleLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		date1	= XPath.path (context, "currency1ValueDate");
					Element		date2	= XPath.path (context, "currency2ValueDate");
					
					if ((date1 == null) || (date2 == null)) continue;
					
					if (notEqual (toDate (date1), toDate (date2))) continue;
										
					errorHandler.error ("305", context,
							"currency1ValueDate and currency2ValueDate must be different.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures non-deliverable forwards contains
	 * a specification of the forward points.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE21_OLD
		= new Rule (Precondition.and (FX_SINGLE_LEG, R3_0__R4_X), "fx-21[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXLeg"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSingleLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ndf		= XPath.path (context, "nonDeliverableForward");
					Element		fwd		= XPath.path (context, "exchangeRate", "forwardPoints");
					
					if ((ndf == null) || (fwd != null)) continue;
					
					errorHandler.error ("305", context,
							"Non-deliverable forward does not specify forwardPoints.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures non-deliverable forwards contains
	 * a specification of the forward points.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE21
		= new Rule (Precondition.and (FX_SINGLE_LEG, R5_1__LATER), "fx-21")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSingleLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSingleLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ndf		= XPath.path (context, "nonDeliverableSettlement");
					Element		fwd		= XPath.path (context, "exchangeRate", "forwardPoints");
					
					if ((ndf == null) || (fwd != null)) continue;
					
					errorHandler.error ("305", context,
							"Non-deliverable forward does not specify forwardPoints.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures buyer, seller, payer and receiver are correct.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE22_OLD
		= new Rule (R3_0__R4_X, "fx-22[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXOptionLeg"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxOptionLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSimpleOption"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fxBarrierOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		buyer	 = XPath.path (context, "buyerPartyReference");
					Element		seller	 = XPath.path (context, "sellerPartyReference");
					Element		payer	 = XPath.path (context, "fxOptionPremium", "payerPartyReference");
					Element		receiver = XPath.path (context, "fxOptionPremium", "receiverPartyReference");
					
					if ((buyer == null) || (seller == null) ||
						(payer == null) || (receiver == null)) continue;
					
					if (equal (DOM.getAttribute(buyer, "href"), DOM.getAttribute(payer, "href")) &&
						equal (DOM.getAttribute(seller, "href"), DOM.getAttribute(receiver, "href"))) continue;
										
					errorHandler.error ("305", context,
							"Premium payer and receiver don't match with option buyer and seller.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures buyer, seller, payer and receiver are correct.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE22
		= new Rule (R5_1__LATER, "fx-22")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		buyer	 		= XPath.path (context, "buyerPartyReference");
					Element		seller	 		= XPath.path (context, "sellerPartyReference");
					Element		payer	 		= XPath.path (context, "premium", "payerPartyReference");
					Element		receiver 		= XPath.path (context, "premium", "receiverPartyReference");
					
					if ((buyer != null) && (seller != null) ||
						(payer != null) && (receiver != null)) {
					
						if (equal (DOM.getAttribute(buyer, "href"), DOM.getAttribute(seller, "href"))) {
							Element		buyerAccount	= XPath.path (context, "buyerAccountReference");
							Element		sellerAccount	= XPath.path (context, "sellerAccountReference");
							Element		payerAccount	= XPath.path (context, "premium", "payerAccountReference");
							Element		receiverAccount = XPath.path (context, "premium", "receiverAccountReference");
							
							if ((buyerAccount != null) && (sellerAccount != null) &&
								equal (DOM.getAttribute(buyer, "href"), DOM.getAttribute(payer, "href")) &&
								equal (DOM.getAttribute(seller, "href"), DOM.getAttribute(receiver, "href")) &&
								equal (DOM.getAttribute(buyerAccount, "href"), DOM.getAttribute(payerAccount, "href")) &&
								equal (DOM.getAttribute(sellerAccount, "href"), DOM.getAttribute(receiverAccount, "href"))) continue;
						}
						else
							if (equal (DOM.getAttribute(buyer, "href"), DOM.getAttribute(payer, "href")) &&
								equal (DOM.getAttribute(seller, "href"), DOM.getAttribute(receiver, "href"))) continue;
					}
										
					errorHandler.error ("305", context,
							"Premium payer and receiver don't match with option buyer and seller.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures the put and call currencies are different.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE23_OLD
		= new Rule (R3_0__R4_X, "fx-23[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXOptionLeg"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxOptionLeg"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSimpleOption"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fxBarrierOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy1	= XPath.path (context, "putCurrencyAmount", "currency");
					Element		ccy2	= XPath.path (context, "callCurrencyAmount", "currency");
					
					if ((ccy1 == null) || (ccy2 == null) || !isSameCurrency (ccy1, ccy2)) continue;
					
					errorHandler.error ("305", context,
							"Put and call currencies must be different.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures the put and call currencies are different.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE23
		= new Rule (R5_1__LATER, "fx-23")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy1	= XPath.path (context, "putCurrencyAmount", "currency");
					Element		ccy2	= XPath.path (context, "callCurrencyAmount", "currency");
					
					if ((ccy1 == null) || (ccy2 == null) || !isSameCurrency (ccy1, ccy2)) continue;
					
					errorHandler.error ("305", context,
							"Put and call currencies must be different.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
				
	/**
	 * A <CODE>Rule</CODE> that ensures rate is positive.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE24_OLD
		= new Rule (R3_0__R4_X, "fx-24[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXStrikePrice"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxStrikePrice"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxStrikePrice"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		rate	= XPath.path (context, "rate");
					
					if ((rate == null) || isPositive (rate)) continue;
										
					errorHandler.error ("305", context,
							"The rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * Context: FxSwap (Complex Type)
	 * <P>
	 * A <CODE>Rule</CODE> that ensures two or more legs are present.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE25_OLD
		= new Rule (R3_0__R4_X, "fx-25[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXSwap"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSwap"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	legs	= XPath.paths (context, "fxSingleLeg");
					
					if (count (legs) >= 2) continue;
										
					errorHandler.error ("305", context,
							"FX swaps must have at least two legs.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if two legs are present they must have
	 * different value dates.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE26_OLD
		= new Rule (R3_0__R4_X, "fx-26[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXSwap"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSwap"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	legs	= XPath.paths (context, "fxSingleLeg");
					
					if (count (legs) != 2) continue;
					
					Element 	date1	= XPath.path ((Element) legs.item (0), "valueDate");
					Element 	date2	= XPath.path ((Element) legs.item (1), "valueDate");
										
					if (notEqual (toDate (date1), toDate (date2))) continue;
					
					errorHandler.error ("305", context,
							"FX swaps legs must settle on different days.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that value date of the near leg of an
	 * FX swap is before that of the far leg.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE26
		= new Rule (R5_1__LATER, "fx-26")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSwap"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element 	nearDate = XPath.path (context, "nearLeg", "valueDate");
					Element 	farDate  = XPath.path (context, "farLeg", "valueDate");
					
					if ((nearDate == null) || (farDate == null) ||
						less (toDate (nearDate), toDate (farDate))) continue;
					
					errorHandler.error ("305", context,
							"The value date of the near leg must be before that of the far leg.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures two different currencies are specified.
	 * <P>
	 * Applies to FpML 3.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE27
		= new Rule (R3_0__LATER, "fx-27")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "QuotedCurrencyPair"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("quotedCurrencyPair"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy1	= XPath.path (context, "currency1");
					Element		ccy2	= XPath.path (context, "currency2");
					
					if ((ccy1 == null) || (ccy2 == null) || !isSameCurrency (ccy1, ccy2)) continue;
					
					errorHandler.error ("305", context,
							"Currencies must be different.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
				
	/**
	 * A <CODE>Rule</CODE> that ensures triggerRate is positive.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE28_OLD
		= new Rule (R3_0__R4_X, "fx-28[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "SideRate"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("currency1SideRate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("currency2SideRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		rate	= XPath.path (context, "rate");
					
					if ((rate == null) || isPositive (rate)) continue;
										
					errorHandler.error ("305", context,
							"The rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that if forwardPoints exists then
	 * spotRate should also exist.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE29_OLD
		= new Rule (R3_0__R4_X, "fx-29[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "SideRate"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("currency1SideRate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("currency2SideRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element 	forward = XPath.path (context, "forwardPoints");
					Element		spot	= XPath.path (context, "spotRate");
					
					if (!((forward != null) && (spot == null))) continue;
					
					errorHandler.error ("305", context,
							"If forwardPoints exists then spotRate should also exist.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
				
	/**
	 * A <CODE>Rule</CODE> that ensures that if both forwardPoints and spotRate
	 * exist then rate equals 'spotRate + forwardRate'.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE30_OLD
		= new Rule (R3_0__R4_X, "fx-30[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "SideRate"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("currency1SideRate"), errorHandler)
						& validate (nodeIndex.getElementsByName ("currency2SideRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element 	forward = XPath.path (context, "forwardPoints");
					Element		spot	= XPath.path (context, "spotRate");
					Element		rate	= XPath.path (context, "rate");
					
					if ((rate == null) || (forward == null) || (spot == null)) continue;
					
					if (toDecimal (rate).equals(toDecimal (spot).add (toDecimal (forward))))
						continue;
					
					errorHandler.error ("305", context,
							"Sum of spotRate and forwardPoints does not equal rate.",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that if both forwardPoints and spotRate
	 * exist then rate equals 'spotRate + forwardRate'.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE30
		= new Rule (R5_1__LATER, "fx-30")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CrossRate"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("crossRate"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element 	forward = XPath.path (context, "forwardPoints");
					Element		spot	= XPath.path (context, "spotRate");
					Element		rate	= XPath.path (context, "rate");
					
					if ((rate == null) || (forward == null) || (spot == null)) continue;
					
					if (toDecimal (rate).compareTo(toDecimal (spot).add (toDecimal (forward))) == 0)
						continue;
					
					errorHandler.error ("305", context,
							"Sum of spotRate and forwardPoints does not equal rate.",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that side rates are obtained relative
	 * to another currency.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE31_OLD
		= new Rule (R3_0__R4_X, "fx-31[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "SideRates"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("sideRates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element 	base 	= XPath.path (context, "baseCurrency");
					Element		ccy1	= XPath.path (context, "currency1SideRate", "currency");
					Element		ccy2	= XPath.path (context, "currency2SideRate", "currency");
					
					if ((base == null) || (ccy1 == null) || (ccy2 == null)) continue;
					if (!isSameCurrency (base, ccy1) && !isSameCurrency (base, ccy2)) continue;
					
					errorHandler.error ("305", context,
							"The base currency must be different from the side rate currencies.",
							getDisplayName (), toToken (base));
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures the initial payer and receiver
	 * are different.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE32_OLD
		= new Rule (R3_0__R4_X, "fx-32[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "TermDeposit"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("termDeposit"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		payer	 = XPath.path (context, "initialPayerReference");
					Element		receiver = XPath.path (context, "initialReceiverReference");
					
					if ((payer == null) || (receiver == null)) continue;

					if (notEqual (DOM.getAttribute(payer, "href"),
								  DOM.getAttribute(receiver, "href"))) continue;
					
					errorHandler.error ("305", context,
							"The initial payer and receiver must be different",
							getDisplayName (), DOM.getAttribute (payer, "href"));
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures the initial payer and receiver
	 * are different.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE32
		= new Rule (R5_1__LATER, "fx-32")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "TermDeposit"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("termDeposit"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context 	= (Element) list.item (index);
					Element		payerParty	  = XPath.path (context, "payerPartyReference");
					Element		receiverParty = XPath.path (context, "receiverPartyReference");
					
					if ((payerParty == null) || (receiverParty == null) ||
							notEqual (DOM.getAttribute(payerParty, "href"),
									DOM.getAttribute(receiverParty, "href"))) continue;
					
					Element		payerAccount	= XPath.path (context, "payerAccountReference");
					Element		receiverAccount = XPath.path (context, "receiverAccountReference");
					
					if ((payerAccount != null) && (receiverAccount != null) &&
							notEqual (DOM.getAttribute (payerAccount, "href"),
									DOM.getAttribute (receiverAccount, "href"))) continue;
					
					errorHandler.error ("305", context,
							"The payer and receiver must be different",
							getDisplayName (), DOM.getAttribute (payerParty, "href"));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the maturity date is after the start date.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE33
		= new Rule (R4_0__LATER, "fx-33")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "TermDeposit"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("termDeposit"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		start	 = XPath.path (context, "startDate");
					Element		maturity = XPath.path (context, "maturityDate");
					
					if ((start == null) || (maturity == null) ||
						less (toDate (start), toDate (maturity))) continue;
					
					errorHandler.error ("305", context,
							"The maturity date must be after the start date",
							getDisplayName (), toToken (maturity));
					
					result = false;
				}
				
				return (result);
			}
		};
				
	/**
	 * A <CODE>Rule</CODE> that ensures the principal amount is positive.
	 * <P>
	 * Applies to FpML 4.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE34
		= new Rule (R4_0__R4_X, "fx-34[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "TermDeposit"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("termDeposit"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		amount	= XPath.path (context, "principal", "amount");
					
					if ((amount == null) || isPositive (amount)) continue;

					errorHandler.error ("305", context,
							"The principal amount must be positive",
							getDisplayName (), toToken (amount));
					
					result = false;
				}
				
				return (result);
			}
		};
				
	/**
	 * A <CODE>Rule</CODE> that ensures the fixed rate is positive.
	 * <P>
	 * Applies to FpML 4.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE35
		= new Rule (R4_0__R4_X, "fx-35[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "TermDeposit"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("termDeposit"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		rate	= XPath.path (context, "fixedRate");
					
					if ((rate == null) || isPositive (rate)) continue;

					errorHandler.error ("305", context,
							"The fixed rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures expiry date is after trade date.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE36_OLD
		= new Rule (Precondition.and (TRADE, R3_0__R4_X), "fx-36[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "tradeHeader", "tradeDate");
					Element		expiryDate	 = XPath.path (context, "fxAverageRateOption", "expiryDateTime", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null)) continue;
					
					if (less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};
					
	/**
	 * A <CODE>Rule</CODE> that ensures expiry date is after trade date.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE36
		= new Rule (Precondition.and (TRADE, R5_1__LATER), "fx-36")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "tradeHeader", "tradeDate");
					Element		expiryDate	 = XPath.path (context, "fxOption", "europeanExercise", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null)) continue;
					
					if (less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures expiry date is after contract trade date.
	 * <P>
	 * Applies to FpML 4.2 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE36B_OLD
		= new Rule (Precondition.and (CONTRACT, R4_2__R4_X), "fx-36b[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "header", "contractDate");
					Element		expiryDate	 = XPath.path (context, "fxAverageRateOption", "expiryDateTime", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null)) continue;
					
					if (less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after contract trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};
						
	/**
	 * A <CODE>Rule</CODE> that ensures expiry date is after trade date.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE37_OLD
		= new Rule (Precondition.and (TRADE, R3_0__R4_X), "fx-37[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "tradeHeader", "tradeDate");
					Element		expiryDate	 = XPath.path (context, "fxBarrierOption", "expiryDateTime", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null)) continue;
					
					if (less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures expiry date is after contract trade date.
	 * <P>
	 * Applies to FpML 4.2 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE37B_OLD
		= new Rule (Precondition.and (CONTRACT, R4_2__R4_X), "fx-37b[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "header", "contractDate");
					Element		expiryDate	 = XPath.path (context, "fxBarrierOption", "expiryDateTime", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null)) continue;
					
					if (less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after contract trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures expiry date is after trade date.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE38_OLD
		= new Rule (Precondition.and (TRADE, R3_0__R4_X), "fx-38[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "tradeHeader", "tradeDate");
					Element		expiryDate	 = XPath.path (context, "fxDigitalOption", "expiryDateTime", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null)) continue;
					
					if (less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};
						
	/**
	 * A <CODE>Rule</CODE> that ensures expiry date is after trade date.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE38
		= new Rule (Precondition.and (TRADE, R5_1__LATER), "fx-38")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "tradeHeader", "tradeDate");
					Element		expiryDate	 = XPath.path (context, "fxDigitalOption", "europeanExercise", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null)) continue;
					
					if (less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};
							
	/**
	 * A <CODE>Rule</CODE> that ensures expiry date is after contract trade date.
	 * <P>
	 * Applies to FpML 4.2 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE38B_OLD
		= new Rule (Precondition.and (CONTRACT, R4_2__R4_X), "fx-38b[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "header", "contractDate");
					Element		expiryDate	 = XPath.path (context, "fxDigitalOption", "expiryDateTime", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null)) continue;
					
					if (less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after contract trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};
							
	/**
	 * A <CODE>Rule</CODE> that ensures value date is after trade date.
	 * <P>
	 * Applies to FpML 3.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE39
		= new Rule (Precondition.and (TRADE, R3_0__LATER), "fx-39")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate = XPath.path (context, "tradeHeader", "tradeDate");
					Element		valueDate = XPath.path (context, "fxSingleLeg", "valueDate");
					Element		value1Date = XPath.path (context, "fxSingleLeg", "currency1ValueDate");
					Element		value2Date = XPath.path (context, "fxSingleLeg", "currency2ValueDate");
					
					if (tradeDate != null) {
						if (valueDate != null) {
							if (lessOrEqual (toDate (tradeDate), toDate (valueDate))) continue;
							
							errorHandler.error ("305", context,
									"value date must be after trade date.",
									getDisplayName (), toToken (valueDate));
							
							result = false;
						}
						
						if (value1Date != null) {
							if (lessOrEqual (toDate (tradeDate), toDate (value1Date))) continue;
							
							errorHandler.error ("305", context,
									"value1date must be after trade date.",
									getDisplayName (), toToken (value1Date));
							
							result = false;
						}

						if (value2Date != null) {
							if (lessOrEqual (toDate (tradeDate), toDate (value2Date))) continue;
							
							errorHandler.error ("305", context,
									"value2date must be after trade date.",
									getDisplayName (), toToken (value2Date));
							
							result = false;
						}
					}
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures value date is after contract trade date.
	 * <P>
	 * Applies to FpML 3.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE39B_OLD
		= new Rule (Precondition.and (CONTRACT, R4_2__LATER), "fx-39b[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate = XPath.path (context, "header", "contractDate");
					Element		valueDate = XPath.path (context, "fxSingleLeg", "valueDate");
					Element		value1Date = XPath.path (context, "fxSingleLeg", "currency1ValueDate");
					Element		value2Date = XPath.path (context, "fxSingleLeg", "currency2ValueDate");
					
					if (tradeDate != null) {
						if (valueDate != null) {
							if (less (toDate (tradeDate), toDate (valueDate))) continue;
							
							errorHandler.error ("305", context,
									"value date must be after contract trade date.",
									getDisplayName (), toToken (valueDate));
							
							result = false;
						}
						
						if (value1Date != null) {
							if (less (toDate (tradeDate), toDate (value1Date))) continue;
							
							errorHandler.error ("305", context,
									"value1date must be after contract trade date.",
									getDisplayName (), toToken (value1Date));
							
							result = false;
						}

						if (value2Date != null) {
							if (less (toDate (tradeDate), toDate (value2Date))) continue;
							
							errorHandler.error ("305", context,
									"value2date must be after contract trade date.",
									getDisplayName (), toToken (value2Date));
							
							result = false;
						}
					}
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures all FX swap value dates are after the
	 * trade date.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE40_OLD
		= new Rule (Precondition.and (TRADE, R3_0__R4_X), "fx-40[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  	= (Element) list.item (index);
					Element		tradeDate 	= XPath.path (context, "tradeHeader", "tradeDate");
					NodeList	legs	  	= XPath.paths (context, "fxSwap", "fxSingleLeg");
					
					for (int count = 0; count < legs.getLength(); ++count) {
						Element		leg			= (Element) legs.item (count);
						Element		valueDate 	= XPath.path (leg, "valueDate");
						Element		value1Date 	= XPath.path (leg, "currency1ValueDate");
						Element		value2Date 	= XPath.path (leg, "currency2ValueDate");
						
						if (tradeDate != null) {
							if (valueDate != null) {
								if (less (toDate (tradeDate), toDate (valueDate))) continue;
								
								errorHandler.error ("305", leg,
										"value date must be after trade date.",
										getDisplayName (), toToken (valueDate));
								
								result = false;
							}
							
							if (value1Date != null) {
								if (less (toDate (tradeDate), toDate (value1Date))) continue;
								
								errorHandler.error ("305", leg,
										"value1date must be after trade date.",
										getDisplayName (), toToken (value1Date));
								
								result = false;
							}

							if (value2Date != null) {
								if (less (toDate (tradeDate), toDate (value2Date))) continue;
								
								errorHandler.error ("305", leg,
										"value2date must be after trade date.",
										getDisplayName (), toToken (value2Date));
								
								result = false;
							}
						}
					}
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures all FX swap value dates are after the
	 * trade date.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE40
		= new Rule (Precondition.and (TRADE, R5_1__LATER), "fx-40")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  	= (Element) list.item (index);
					Element		tradeDate 	= XPath.path (context, "tradeHeader", "tradeDate");
					Element		nearLeg  	= XPath.path (context, "fxSwap", "nearLeg");
					Element		farLeg  	= XPath.path (context, "fxSwap", "nearLeg");
					
					{
						Element		valueDate 	= XPath.path (nearLeg, "valueDate");
						Element		value1Date 	= XPath.path (nearLeg, "currency1ValueDate");
						Element		value2Date 	= XPath.path (nearLeg, "currency2ValueDate");
						
						if (tradeDate != null) {
							if (valueDate != null) {
								if (less (toDate (tradeDate), toDate (valueDate))) continue;
								
								errorHandler.error ("305", nearLeg,
										"value date must be after trade date.",
										getDisplayName (), toToken (valueDate));
								
								result = false;
							}
							
							if (value1Date != null) {
								if (less (toDate (tradeDate), toDate (value1Date))) continue;
								
								errorHandler.error ("305", nearLeg,
										"value1date must be after trade date.",
										getDisplayName (), toToken (value1Date));
								
								result = false;
							}

							if (value2Date != null) {
								if (less (toDate (tradeDate), toDate (value2Date))) continue;
								
								errorHandler.error ("305", nearLeg,
										"value2date must be after trade date.",
										getDisplayName (), toToken (value2Date));
								
								result = false;
							}
						}
					}
						
					{
						Element		valueDate 	= XPath.path (farLeg, "valueDate");
						Element		value1Date 	= XPath.path (farLeg, "currency1ValueDate");
						Element		value2Date 	= XPath.path (farLeg, "currency2ValueDate");
						
						if (tradeDate != null) {
							if (valueDate != null) {
								if (less (toDate (tradeDate), toDate (valueDate))) continue;
								
								errorHandler.error ("305", farLeg,
										"value date must be after trade date.",
										getDisplayName (), toToken (valueDate));
								
								result = false;
							}
							
							if (value1Date != null) {
								if (less (toDate (tradeDate), toDate (value1Date))) continue;
								
								errorHandler.error ("305", farLeg,
										"value1date must be after trade date.",
										getDisplayName (), toToken (value1Date));
								
								result = false;
							}

							if (value2Date != null) {
								if (less (toDate (tradeDate), toDate (value2Date))) continue;
								
								errorHandler.error ("305", farLeg,
										"value2date must be after trade date.",
										getDisplayName (), toToken (value2Date));
								
								result = false;
							}
						}
					}
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures all FX swap value dates are after the
	 * contract trade date.
	 * <P>
	 * Applies to FpML 4.2 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE40B
		= new Rule (Precondition.and (CONTRACT, R4_2__R4_X), "fx-40b[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  	= (Element) list.item (index);
					Element		tradeDate 	= XPath.path (context, "header", "contractDate");
					NodeList	legs	  	= XPath.paths (context, "fxSwap", "fxSingleLeg");
					
					for (int count = 0; count < legs.getLength(); ++count) {
						Element		leg			= (Element) legs.item (count);
						Element		valueDate 	= XPath.path (leg, "valueDate");
						Element		value1Date 	= XPath.path (leg, "currency1ValueDate");
						Element		value2Date 	= XPath.path (leg, "currency2ValueDate");
						
						if (tradeDate != null) {
							if (valueDate != null) {
								if (less (toDate (tradeDate), toDate (valueDate))) continue;
								
								errorHandler.error ("305", leg,
										"value date must be after contract trade date.",
										getDisplayName (), toToken (valueDate));
								
								result = false;
							}
							
							if (value1Date != null) {
								if (less (toDate (tradeDate), toDate (value1Date))) continue;
								
								errorHandler.error ("305", leg,
										"value1date must be after contract trade date.",
										getDisplayName (), toToken (value1Date));
								
								result = false;
							}

							if (value2Date != null) {
								if (less (toDate (tradeDate), toDate (value2Date))) continue;
								
								errorHandler.error ("305", leg,
										"value2date must be after contract trade date.",
										getDisplayName (), toToken (value2Date));
								
								result = false;
							}
						}
					}
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures triggerRate is positive.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE41_OLD
		= new Rule (R3_0__R4_X, "fx-41[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXBarrier"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxBarrier"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxBarrier"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		rate	= XPath.path (context, "triggerRate");
					
					if ((rate == null) || isPositive (rate)) continue;
										
					errorHandler.error ("305", context,
							"The trigger rate must be positive",
							getDisplayName (), toToken (rate));
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures each averageRateObservationDate/observationDate
	 * is unique.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE42_OLD
		= new Rule (R3_0__R4_X, "fx-42[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAverageRateOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxAverageRateOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	nodes	= XPath.paths (context, "averageRateObservationDate", "observationDate");
					
					int			limit	= nodes.getLength ();
					Date []		dates	= new Date [limit];
					
					for (int count = 0; count < limit; ++count)
						dates [count] = toDate (nodes.item(count));					
					
					for (int outer = 0; outer < (limit - 1); ++outer) {
						for (int inner = outer + 1; inner < limit; ++inner) {
							if (equal (dates [outer], dates [inner]))
								errorHandler.error ("305", nodes.item (inner),
										"Duplicate observation date",
										getDisplayName (), toToken(nodes.item (inner)));
							
							result = false;
						}
					}
					dates = null;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the put and call currencies are different.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE43_OLD
		= new Rule (R3_0__R4_X, "fx-43[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAverageRateOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxAverageRateOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		ccy1	= XPath.path (context, "putCurrencyAmount", "currency");
					Element		ccy2	= XPath.path (context, "callCurrencyAmount", "currency");
					
					if ((ccy1 == null) || (ccy2 == null) || !isSameCurrency (ccy1, ccy2)) continue;
													
					errorHandler.error ("305", context,
							"Put and call currencies must be different.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures buyer, seller, payer and receiver are correct.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE44
		= new Rule (R3_0__R4_X, "fx-44[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXAverageRateOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAverageRateOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxAverageRateOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		buyer	 = XPath.path (context, "buyerPartyReference");
					Element		seller	 = XPath.path (context, "sellerPartyReference");
					Element		payer	 = XPath.path (context, "fxOptionPremium", "payerPartyReference");
					Element		receiver = XPath.path (context, "fxOptionPremium", "receiverPartyReference");
					
					if ((buyer == null) || (seller == null) ||
						(payer == null) || (receiver == null)) continue;
					
					if (equal (DOM.getAttribute(buyer, "href"), DOM.getAttribute(payer, "href")) &&
						equal (DOM.getAttribute(seller, "href"), DOM.getAttribute(receiver, "href"))) continue;
										
					errorHandler.error ("305", context,
							"Premium payer and receiver don't match with option buyer and seller.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures buyer, seller, payer and receiver are correct.
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE45_OLD
		= new Rule (R3_0__R4_X, "fx-45[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (
						  validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FXDigitalOption"), errorHandler)
						& validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxDigitalOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxDigitalOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		buyer	 = XPath.path (context, "buyerPartyReference");
					Element		seller	 = XPath.path (context, "sellerPartyReference");
					Element		payer	 = XPath.path (context, "fxOptionPremium", "payerPartyReference");
					Element		receiver = XPath.path (context, "fxOptionPremium", "receiverPartyReference");
					
					if ((buyer == null) || (seller == null) ||
						(payer == null) || (receiver == null)) continue;
					
					if (equal (DOM.getAttribute(buyer, "href"), DOM.getAttribute(payer, "href")) &&
						equal (DOM.getAttribute(seller, "href"), DOM.getAttribute(receiver, "href"))) continue;
										
					errorHandler.error ("305", context,
							"Premium payer and receiver don't match with option buyer and seller.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures buyer, seller, payer and receiver are correct.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE45
		= new Rule (R5_1__LATER, "fx-45")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxDigitalOption"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("fxDigitalOption"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		buyerParty	 = XPath.path (context, "buyerPartyReference");
					Element		sellerParty	 = XPath.path (context, "sellerPartyReference");
					Element		payerParty	 = XPath.path (context, "premium", "payerPartyReference");
					Element		receiverParty = XPath.path (context, "premium", "receiverPartyReference");
					
					if ((buyerParty == null) || (sellerParty == null) ||
						(payerParty == null) || (receiverParty == null)) continue;
					
					if (equal (buyerParty.getAttribute ("href"), sellerParty.getAttribute ("href"))) {
						Element		buyerAccount	 = XPath.path (context, "buyerAccountReference");
						Element		sellerAccount	 = XPath.path (context, "sellerAccountReference");
						Element		payerAccount	 = XPath.path (context, "premium", "payerAccountReference");
						Element		receiverAccount = XPath.path (context, "premium", "receiverAccountReference");
					
	                    if ((buyerAccount != null) && (sellerAccount != null) &&
	                        (payerAccount != null) && (receiverAccount != null) &&
	                        equal (buyerParty.getAttribute("href"), payerParty.getAttribute("href")) &&
	    					equal (sellerParty.getAttribute("href"), receiverParty.getAttribute("href")) &&
	                        equal (buyerAccount.getAttribute("href"), payerAccount.getAttribute("href")) &&
	    					equal (sellerAccount.getAttribute("href"), receiverAccount.getAttribute("href"))) continue;
					}
					else
						if (equal (DOM.getAttribute(buyerParty, "href"), DOM.getAttribute(payerParty, "href")) &&
							equal (DOM.getAttribute(sellerParty, "href"), DOM.getAttribute(receiverParty, "href"))) continue;
										
					errorHandler.error ("305", context,
							"Premium payer and receiver don't match with option buyer and seller.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the side rates definition for currency1
	 * uses an appropriate basis. 
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE46_OLD
		= new Rule (R3_0__R4_X, "fx-46[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "SideRates"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("sideRates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		basis	= XPath.path (context, "currency1SideRate", "sideRateBasis");
					
					if ((basis == null) ||
						toToken (basis).equalsIgnoreCase ("Currency1PerBaseCurrency") || 
						toToken (basis).equalsIgnoreCase ("BaseCurrencyPerCurrency1")) continue;
					
					errorHandler.error ("305", context,
							"Side rate basis for currency1 should not be expressed in terms of currency2.",
							getDisplayName (), toToken (basis));
					
					result = false;
				}
				
				return (result);
			}
		};
				
	/**
	 * A <CODE>Rule</CODE> that ensures the side rates definition for currency2
	 * uses an appropriate basis. 
	 * <P>
	 * Applies to FpML 3.0 up to 4.x.
	 * @since	TFP 1.2
	 */
	public static final Rule 	RULE47_OLD
		= new Rule (R3_0__R4_X, "fx-47[OLD]")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "SideRates"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("sideRates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		basis	= XPath.path (context, "currency2SideRate", "sideRateBasis");
					
					if ((basis == null) ||
						toToken (basis).equalsIgnoreCase ("Currency2PerBaseCurrency") || 
						toToken (basis).equalsIgnoreCase ("BaseCurrencyPerCurrency2")) continue;
					
					errorHandler.error ("305", context,
							"Side rate basis for currency2 should not be expressed in terms of currency1.",
							getDisplayName (), toToken (basis));
					
					result = false;
				}
				
				return (result);
			}
		};
					
	/**
	 * A <CODE>Rule</CODE> that ensures if one rateObservation/rate exists,
	 * then rateObservationQuoteBasis must exist.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE48
		= new Rule (R5_1__LATER, "fx-48")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxAsianFeature"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("asian"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	rates	= XPath.paths (context, "rateObservation", "rate");
					Element		basis	= XPath.path (context, "rateObservationQuoteBasis");
					
					if ((rates.getLength () == 0) ||
						(rates.getLength () > 0) && (basis != null)) continue;
					
					errorHandler.error ("305", context,
							"If one rateObservation/rate exists, then rateObservationQuoteBasis must exist.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that two different currencies are used
	 * in each FxSwapLeg.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE49
		= new Rule (Precondition.and (FX_SWAP_LEG, R5_1__LATER), "fx-49")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSwapLeg"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("nearLeg"), errorHandler)
						& validate (nodeIndex.getElementsByName ("farLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element	context = (Element) list.item (index);
					Element	ccy1	= XPath.path (context, "exchangedCurrency1", "paymentAmount", "currency");
					Element	ccy2	= XPath.path (context, "exchangedCurrency2", "paymentAmount", "currency");
					
					if ((ccy1 == null) || (ccy2 == null) ||
	                    notEqual (ccy1.getAttribute ("currencyScheme"), ccy2.getAttribute ("currencyScheme")) ||
	                    notEqual (ccy1, ccy2)) continue;
					
					errorHandler.error ("305", context,
							"The two currencies must be different",
							getDisplayName (), toToken (ccy1));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if split settlement is specified then
	 * the settlement dates must be different.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE50
		= new Rule (Precondition.and (FX_SWAP_LEG, R5_1__LATER), "fx-50")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSwapLeg"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("nearLeg"), errorHandler)
						& validate (nodeIndex.getElementsByName ("farLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		date1	= XPath.path (context, "currency1ValueDate");
					Element		date2	= XPath.path (context, "currency2ValueDate");
					
					if ((date1 == null) || (date2 == null) ||
						notEqual (toDate (date1), toDate (date2))) continue;
					
					errorHandler.error ("305", context,
							"currency1ValueDate and currency2ValueDate must be different.",
							getDisplayName (), toToken (date1));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if cash settlement is specified the
	 * deal must be a forward.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE51
		= new Rule (Precondition.and (FX_SWAP_LEG, R5_1__LATER), "fx-51")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxSwapLeg"), errorHandler));					
					
				return (
						  validate (nodeIndex.getElementsByName ("nearLeg"), errorHandler)
						& validate (nodeIndex.getElementsByName ("farLeg"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context = (Element) list.item (index);
					Element		cash	= XPath.path (context, "nonDeliverableSettlement");
					Element		forward	= XPath.path (context, "exchangeRate", "forwardPoints");
					
					if ((cash == null) || (forward != null)) continue;
					
					errorHandler.error ("305", context,
							"If nonDeliverableSettlement is specified then forwardPoints must be present.",
							getDisplayName (), null);
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the expiry date of an American option
	 * falls after the trade date.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE52
		= new Rule (Precondition.and (TRADE, R5_1__LATER), "fx-52")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "tradeHeader", "tradeDate");
					Element		expiryDate	 = XPath.path (context, "fxOption", "americanExercise", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null) ||
						less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the expiry date of an American option
	 * falls after the trade date.
	 * <P>
	 * Applies to FpML 5.1 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule 	RULE53
		= new Rule (Precondition.and (TRADE, R5_1__LATER), "fx-53")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));					
					
				return (validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength(); ++index) {
					Element		context  = (Element) list.item (index);
					Element		tradeDate	 = XPath.path (context, "tradeHeader", "tradeDate");
					Element		expiryDate	 = XPath.path (context, "fxDigitalOption", "americanExercise", "expiryDate");
					
					if ((tradeDate == null) || (expiryDate == null) ||
						less (toDate (tradeDate), toDate (expiryDate))) continue;
										
					errorHandler.error ("305", context,
							"Expiry date must be after trade date.",
							getDisplayName (), toToken (expiryDate));
					
					result = false;
				}
				
				return (result);
			}
		};

	/**
	 * Provides access to the FX validation rule set.
	 *
	 * @return	The FX validation rule set.
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
	private FxRules ()
	{ }

	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for interest rate products.
	 * @since	TFP 1.2
	 */
	private static final RuleSet	rules = RuleSet.forName ("FxRules");
	
	/**
	 * Generates a set of dates according to schedule defined by a start date,
	 * an end date, an interval, roll convention and a calendar.
	 * 
	 * @param	start		The start date.
	 * @param	end			The end date.
	 * @param	frequency	The frequency of the schedule (e.g. 6M).
	 * @param	roll		The date roll convention or <CODE>null</CODE>.
	 * @param	calendar	The holiday calendar or <CODE>null</CODE>.
	 * @return	An array of calculated and adjusted dates.
	 * @since	TFP 1.2
	 */
	protected static Date [] generateSchedule (final Date start, final Date end,
			final Interval frequency, final DateRoll roll, final Calendar calendar)
	{
		Date		current = start;
		Vector<Date> found	= new Vector<Date> ();
		Date []		dates;
		
		while (less (current, end)) {
			Date		adjusted;
			
			if (roll != null)
				adjusted = roll.adjust(calendar, current);
			else
				adjusted = current;
			
			if (!found.contains (adjusted))
				found.add (adjusted);
			
			if (frequency.getPeriod () == Period.TERM) {
				if (equal (current, start))
					current = end;
				else
					break;				
			}
			else
				current = current.plus (frequency);
		}
		
		found.copyInto (dates  = new Date [found.size ()]);
		return (dates);
	}
}