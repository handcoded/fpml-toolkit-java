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

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.finance.Time;
import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;

/**
 * This class provides datatype validation rules for the DTD based FpML
 * grammars used in FpML 1-0, 2-0 and 3-0. For later FpML versions this
 * function is performed by the XML parser.
 *  
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class DataTypeRules
{
	/**
	 * Rule 1: Datatype (HourMinuteType) -
	 *
	 * A rule instance that ensures that HourMinuteTime values match a
	 * HH:MM:SS pattern where the SS portion is always '00'.
	 * <P>
	 * Applies to FpML 1-0, 2-0 and 3-0. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE01
		= new Rule (Preconditions.R1_0__R3_0, "datatype-1")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (validate (nodeIndex.getElementsByName ("hourMinuteTime"), errorHandler));
			}

			/**
			 * Applies validation to each context <CODE>Element</CODE> in the
			 * provided <CODE>NodeList</CODE>.
			 * 
			 * @param	list			A (possibly empty) <CODE>NodeList</CODE>.
			 * @param	errorHandler	Used to report validation failures.
			 * @return	A boolean value indicating it the validation was successful.
			 * @since	TFP 1.0
			 */
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  value	= DOM.getInnerText (context);
					
					try {
						if (!Time.parse (value).seconds ().equals (BigDecimal.ZERO)) {
							errorHandler.error ("305", context,
								"The seconds component of the time must be zeroes",
								getDisplayName (), value);
							result = false;
						}
					}
					catch (IllegalArgumentException error) {
						errorHandler.error ("305", context,
							"The time value is not in HH:MM:SS format",
							getDisplayName (), value);
						result = false;
					}
				}
				
				return (result);
			}			
		};
		
	/**
	 * Rule 2: Datatype (date) -
	 *
	 * A rule instance that ensures that date values match a
	 * YYYY-MM-DD pattern.
	 * <P>
	 * Applies to FpML 1-0, 2-0 and 3-0. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE02
		= new Rule (Preconditions.R1_0__R3_0, "datatype-2")
		{			
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
				    validate (nodeIndex.getElementsByName ("adjustedEarlyTerminationDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedEffectiveDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedEndDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedExerciseDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedExerciseFeePaymentDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedExtendedTerminationDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedFixingDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedFxSpotFixingDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedPrincipalExchangeDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedRelevantSwapEffectiveDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedStartDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedTerminationDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("adjustedPaymentDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("currency1ValueDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("currency2ValueDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("expiryDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("firstNotionalStepDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("firstPaymentDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("firstRegularPeriodStartDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("fixingDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("lastNotionalStepDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("lastRegularPaymentDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("lastRegularPeriodEndDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("masterAgreementDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("observationDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("observationEndDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("observationStartDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("resetDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("stepDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("premiumSettlementDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("tradeDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("unadjustedDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("unadjustedEndDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("unadjustedFirstDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("unadjustedLastDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("unadjustedPaymentDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("unadjustedPrincipalExchangeDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("unadjustedStartDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("valuationDate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("valueDate"), errorHandler));
			}
			
			/**
			 * The regular expression used to detect valid instances.
			 * @since	TFP 1.0
			 */
			private Pattern			pattern = Pattern.compile ("[1-2]\\d{3}-[0-1]\\d-[0-3]\\d");
			
			/**
			 * The pattern matcher used to compare a string to the pattern.
			 * @since	TFP 1.0
			 */
			private Matcher			matcher = null;

			/**
			 * Applies validation to each context <CODE>Element</CODE> in the
			 * provided <CODE>NodeList</CODE>.
			 * 
			 * @param	list			A (possibly empty) <CODE>NodeList</CODE>.
			 * @param	errorHandler	Used to report validation failures.
			 * @return	A boolean value indicating it the validation was successful.
			 * @since	TFP 1.0
			 */
			private synchronized boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  value	= DOM.getInnerText (context).trim ();
					
					if (matcher == null)
						matcher = pattern.matcher (value);
					else
						matcher.reset (value);
					
					if (!matcher.matches ()) {
						errorHandler.error ("305", context,
								"The date value is not in YYYY-MM-DD format",
								getDisplayName (), value);
						result = false;
					}
				}
				
				return (result);
			}
		};

	/**
	 * Rule 3: Datatype (decimal) -
	 *
	 * A rule instance that ensures that decimal values match a
	 * (+/-)&lt;digits&gt;(.&lt;digits&gt;) pattern.
	 * <P>
	 * Applies to FpML 1-0, 2-0 and 3-0. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE03
		= new Rule (Preconditions.R1_0__R3_0, "datatype-3")
		{			
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
				  	validate (nodeIndex.getElementsByName ("amount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("averageRateWeightingFactor"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("calculatedRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("discountRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("feeAmount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("feeRate"), errorHandler)	
				  & validate (nodeIndex.getElementsByName ("fixedPaymentAmount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("fixedRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("floatingRateMultiplier"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("forwardPoints"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("initialRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("initialValue"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("integralMultipleAmount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("integralMultipleExercise"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("maximumNotionalAmount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("maximumNumberOfOptions"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("minimumNotionalAmount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("minimumNumberOfOptions"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("notionalAmount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("notionalStepAmount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("notionalStepRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("numberOfOptions"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("observedFxSpotRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("observedRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("optionEntitlement"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("percentageOfNotional"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("premiumValue"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("pricePerOption"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("principalExchangeAmount"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("rate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("spotPrice"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("spotRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("spread"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("stepValue"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("strikePrice"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("strikeRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("stubRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("thresholdRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("treatedRate"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("triggerRate"), errorHandler));
			}
			
			/**
			 * The regular expression used to detect valid instances.
			 * @since	TFP 1.0
			 */
			private Pattern			pattern = Pattern.compile ("(\\+|-)?(0|[1-9][0-9]*)(\\.[0-9]+)?");
			
			/**
			 * The pattern matcher used to compare a string to the pattern.
			 * @since	TFP 1.0
			 */
			private Matcher			matcher = null;
			
			/**
			 * Applies validation to each context <CODE>Element</CODE> in the
			 * provided <CODE>NodeList</CODE>.
			 * 
			 * @param	list			A (possibly empty) <CODE>NodeList</CODE>.
			 * @param	errorHandler	Used to report validation failures.
			 * @return	A boolean value indicating it the validation was successful.
			 * @since	TFP 1.0
			 */
			private synchronized boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  value	= DOM.getInnerText (context).trim ();
			
					if (matcher == null)
						matcher = pattern.matcher (value);
					else
						matcher.reset (value);
					
					if (!matcher.matches ()) {
						errorHandler.error ("305", context,
							"Invalid decimal value",
							getDisplayName (), value);
						result = false;
					}
				}
				
				return (result);
			}
		};
			
	/**
	 * Rule 4: Datatype (integer) -
	 *
	 * A rule instance that ensures that integer values match a
	 * (+/-)&lt;digits&gt; pattern.
	 * <P>
	 * Applies to FpML 1-0, 2-0 and 3-0. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE04
		= new Rule (Preconditions.R1_0__R3_0, "datatype-4")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
				  	validate (nodeIndex.getElementsByName ("intermediarySequenceNumber"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("periodMultiplier"), errorHandler));
			}
			
			/**
			 * The regular expression used to detect valid instances.
			 * @since	TFP 1.0
			 */
			private Pattern			pattern = Pattern.compile ("(\\+|-)?(0|[1-9][0-9]*)");
			
			/**
			 * The pattern matcher used to compare a string to the pattern.
			 * @since	TFP 1.0
			 */
			private Matcher			matcher = null;

			/**
			 * Applies validation to each context <CODE>Element</CODE> in the
			 * provided <CODE>NodeList</CODE>.
			 * 
			 * @param	list			A (possibly empty) <CODE>NodeList</CODE>.
			 * @param	errorHandler	Used to report validation failures.
			 * @return	A boolean value indicating it the validation was successful.
			 * @since	TFP 1.0
			 */
			private synchronized boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  value	= DOM.getInnerText (context).trim ();
			
					if (matcher == null)
						matcher = pattern.matcher (value);
					else
						matcher.reset (value);
					
					if (!matcher.matches ()) {
						errorHandler.error ("305", context,
							"Invalid integer value",
							getDisplayName (), value);
						result = false;
					}
				}
				
				return (result);
			}
		};
				
	/**
	 * Rule 5: Datatype (positiveInteger) -
	 *
	 * A rule instance that ensures that integer values match a
	 * (+)&lt;digits&gt; pattern.
	 * <P>
	 * Applies to FpML 1-0, 2-0 and 3-0. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE05
		= new Rule (Preconditions.R1_0__R3_0, "datatype-5")
		{			
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
				  	validate (nodeIndex.getElementsByName ("calculationPeriodNumberOfDays"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("observationWeight"), errorHandler)
				  & validate (nodeIndex.getElementsByName ("periodSkip"), errorHandler));
			}
			
			/**
			 * The regular expression used to detect valid instances.
			 * @since	TFP 1.0
			 */
			private Pattern			pattern = Pattern.compile ("(\\+)?[1-9][0-9]*");
			
			/**
			 * The pattern matcher used to compare a string to the pattern.
			 * @since	TFP 1.0
			 */
			private Matcher			matcher = null;

			/**
			 * Applies validation to each context <CODE>Element</CODE> in the
			 * provided <CODE>NodeList</CODE>.
			 * 
			 * @param	list			A (possibly empty) <CODE>NodeList</CODE>.
			 * @param	errorHandler	Used to report validation failures.
			 * @return	A boolean value indicating it the validation was successful.
			 * @since	TFP 1.0
			 */
			private synchronized boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  value	= DOM.getInnerText (context).trim ();
			
					if (matcher == null)
						matcher = pattern.matcher (value);
					else
						matcher.reset (value);
					
					if (!matcher.matches ()) {
						errorHandler.error ("305", context,
							"Invalid positive integer value",
							getDisplayName (), value);
						result = false;
					}
				}
				
				return (result);
			}
		};
					
	/**
	 * Rule 6: Datatype (nonNegativeInteger) -
	 *
	 * A rule instance that ensures that integer values match a
	 * (+)&lt;digits&gt; pattern.
	 * <P>
	 * Applies to FpML 1-0, 2-0 and 3-0. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE06
		= new Rule (Preconditions.R1_0__R3_0, "datatype-6")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
				  	validate (nodeIndex.getElementsByName ("precision"), errorHandler));
			}
			
			/**
			 * The regular expression used to detect valid instances.
			 * @since	TFP 1.0
			 */
			private Pattern			pattern = Pattern.compile ("(\\+)?(0|[1-9][0-9]*)");
			
			/**
			 * The pattern matcher used to compare a string to the pattern.
			 * @since	TFP 1.0
			 */
			private Matcher			matcher = null;

			/**
			 * Applies validation to each context <CODE>Element</CODE> in the
			 * provided <CODE>NodeList</CODE>.
			 * 
			 * @param	list			A (possibly empty) <CODE>NodeList</CODE>.
			 * @param	errorHandler	Used to report validation failures.
			 * @return	A boolean value indicating it the validation was successful.
			 * @since	TFP 1.0
			 */
			private synchronized boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  value	= DOM.getInnerText (context).trim ();
			
					if (matcher == null)
						matcher = pattern.matcher (value);
					else
						matcher.reset (value);
					
					if (!matcher.matches ()) {
						errorHandler.error ("305", context,
							"Invalid non-negative integer value",
							getDisplayName (), value);
						result = false;
					}
				}
				
				return (result);
			}
		};
						
	/**
	 * Rule 7: Datatype (boolean) -
	 *
	 * A rule instance that ensures that boolean values match either
	 * 'true' or 'false'.
	 * <P>
	 * Applies to FpML 1-0, 2-0 and 3-0. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE07
		= new Rule (Preconditions.R1_0__R3_0, "datatype-7")
		{			
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (	
					validate (nodeIndex.getElementsByName ("automaticExerciseApplicable"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("cashflowsMatchParameters"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("failureToDeliverApplicable"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("fallbackExercise"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("finalExchange"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("followUpConfirmation"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("fraDiscounting"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("initialExchange"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("intermediateExchange"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("swapPremium"), errorHandler)
				  &	validate (nodeIndex.getElementsByName ("swaptionStraddle"), errorHandler));
			}
			
			/**
			 * The regular expression used to detect valid instances.
			 * @since	TFP 1.0
			 */
			private Pattern			pattern = Pattern.compile ("(true)|(false)");
			
			/**
			 * The pattern matcher used to compare a string to the pattern.
			 * @since	TFP 1.0
			 */
			private Matcher			matcher = null;

			/**
			 * Applies validation to each context <CODE>Element</CODE> in the
			 * provided <CODE>NodeList</CODE>.
			 * 
			 * @param	list			A (possibly empty) <CODE>NodeList</CODE>.
			 * @param	errorHandler	Used to report validation failures.
			 * @return	A boolean value indicating it the validation was successful.
			 * @since	TFP 1.0
			 */
			private synchronized boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  value	= DOM.getInnerText (context).trim ();
			
					if (matcher == null)
						matcher = pattern.matcher (value);
					else
						matcher.reset (value);
					
					if (!matcher.matches ()) {
						errorHandler.error ("305", context,
							"Invalid boolean value",
							getDisplayName (), value);
						result = false;
					}
				}
				
				return (result);
			}
		};
	
	/**
	 * Provides access to the data type validation rule set.
	 * 
	 * @return	The data type validation rule set.
	 * @since	TFP 1.0
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}
	
	/**
	 * A <CODE>RuleSet</CODE> containing data type validation rules for
	 * DTD based releases.
	 * 
	 * @since	TFP 1.0
	 */
	private static final RuleSet	rules = RuleSet.forName ("DataTypeRules");
}