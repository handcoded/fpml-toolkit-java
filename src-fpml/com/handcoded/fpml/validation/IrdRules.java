// Copyright (C),2005-2015 HandCoded Software Ltd.
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
import com.handcoded.finance.Interval;
import com.handcoded.finance.Period;
import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.Types;
import com.handcoded.xml.XPath;

/**
 * The <CODE>IrdRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for interest rate products.
 *
 * @author	BitWise
 * @version	$Id: IrdRules.java 837 2015-11-23 20:25:15Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class IrdRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects documents containing
	 * at least one interest rate product.
	 * @since	TFP 1.7
	 */
	private static final Precondition	IRD_PRODUCT
		= new ContentPrecondition (
				new String [] { "swap", "swaption", "fra", "capFloor", "bulletPayment" },
				new String [] { "Swap", "Swaption", "Fra", "CapFloor", "BulletPayment" });
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 1-0 thru 3-0
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R1_0__R3_0
		= Precondition.and (new Precondition [] {
				IRD_PRODUCT,
				Preconditions.R1_0__R3_0,
				Preconditions.CONFIRMATION });
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 1-0 thru 4-6
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R1_0__R4_6
		= Precondition.and (new Precondition [] {
				IRD_PRODUCT,
				Preconditions.R1_0__R4_6,
				Preconditions.CONFIRMATION });

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 1-0 thru 4-9
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R1_0__R4_9
		= Precondition.and (new Precondition [] {
				IRD_PRODUCT,
				Preconditions.R1_0__R4_9,
				Preconditions.CONFIRMATION });

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 1-0 and later
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R1_0__LATER
		= Precondition.and (new Precondition [] {
				IRD_PRODUCT,
				Preconditions.R1_0__LATER,
				Preconditions.CONFIRMATION });

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-0 and later
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R4_0__LATER
		= Precondition.and (new Precondition [] {
				IRD_PRODUCT,
				Preconditions.R4_0__LATER,
				Preconditions.CONFIRMATION });
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-7 and later
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R4_7__LATER
		= Precondition.and (new Precondition [] {
				IRD_PRODUCT,
				Preconditions.R4_7__LATER,
				Preconditions.CONFIRMATION });
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-0 and later
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	private static final Precondition	R5_0__LATER
		= Precondition.and (new Precondition [] {
				IRD_PRODUCT,
				Preconditions.R5_0__LATER,
				Preconditions.CONFIRMATION });
	
	/**
	 * A <CODE>Precondition</CODE> instance that detect documents containing
	 * at least one interest rate stream in a confirmation view.
	 * @since	TFP 1.6
	 */
	private static final Precondition	INTEREST_RATE_STREAM
		= Precondition.and (
			Preconditions.CONFIRMATION,
			new ContentPrecondition (
				new String [] { "swapStream", "capFloorStream" },
				new String [] { "InterestRateStream" }));
	
	/**
	 * A <CODE>Precondition</CODE> instance that detect documents containing
	 * at least one set of calculation period dates in a confirmation view.
	 * @since	TFP 1.6
	 */
	private static final Precondition	CALCULATION_PERIOD_DATES
		= Precondition.and (
				Preconditions.CONFIRMATION,
				new ContentPrecondition (
				new String [] { "calculationPeriodDates" },
				new String [] { "CalculationPeriodDates" }));

	/**
	 * A <CODE>Rule</CODE> that ensures reset dates are present for
	 * floating rate interest streams.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE01 = new Rule (INTEREST_RATE_STREAM, "ird-1")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context	= (Element) list.item (index);

					if (iff (
							exists (XPath.path (context, "resetDates")),
							or (
									exists (XPath.path (context, "calculationPeriodAmount", "calculation", "floatingRateCalculation")),
									exists (XPath.path (context, "calculationPeriodAmount", "calculation", "inflationRateCalculation")))))
						continue;

					errorHandler.error ("305", context,
						"resetDates must be present if and only if a floatingRateCalculation " +
						"or inflationRateCalculation element is present in calculationPeriodAmount",
						getDisplayName (), null);

					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures payment frequency is a multiple
	 * of the calculation frequency.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE02 = new Rule (INTEREST_RATE_STREAM, "ird-2")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context	= (Element) list.item (index);

					if (!isParametric (context)) continue;

					Element	paymentFreq	= XPath.path (context, "paymentDates", "paymentFrequency");
					Element	calcFreq	= XPath.path (context, "calculationPeriodDates", "calculationPeriodFrequency");

					if ((paymentFreq == null) || (calcFreq == null)) continue;

					Interval payment = toInterval (paymentFreq);
					Interval calc    = toInterval (calcFreq);

					if ((payment == null) || (calc == null) || payment.isMultipleOf (calc)) continue;

					errorHandler.error ("305", context,
						"Payment frequency '" + toInterval (paymentFreq) +
						"' is not an integer multiple of calculation frequency '" +
						toInterval (calcFreq) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the first payment date matches
	 * a calculation date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE03 = new Rule (INTEREST_RATE_STREAM, "ird-3")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index){
					Element	context		= (Element) list.item (index);

					if (!isParametric (context)) continue;

					Element paymentDate = XPath.path (context, "paymentDates", "firstPaymentDate");
					Element	startDate	= XPath.path (context, "calculationPeriodDates", "firstRegularPeriodStartDate");
					Element	endDate		= XPath.path (context, "calculationPeriodDates", "lastRegularPeriodEndDate");

					if (!exists (startDate))
						startDate = XPath.path (context, "calculationPeriodDates", "effectiveDate", "unadjustedDate");

					if (!exists (endDate))
						endDate = XPath.path (context, "calculationPeriodDates", "terminationDate", "unadjustedDate");

					Interval interval	= toInterval (XPath.path (context, "calculationPeriodDates", "calculationPeriodFrequency"));

					if ((paymentDate == null) || (startDate == null) || (endDate == null) || (interval == null) ||
							isUnadjustedCalculationPeriodDate (
									toDate (paymentDate), toDate (startDate), toDate (endDate),
									interval)) continue;

					errorHandler.error ("305", context,
						"The first payment date '" + toToken (paymentDate) + "' does not " +
						"fall on one of the unadjusted calculation period dates.",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the last regular payment date
	 * matches a calculation date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE04 = new Rule (INTEREST_RATE_STREAM, "ird-4")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index){
					Element	context		= (Element) list.item (index);

					if (!isParametric (context)) continue;

					Element paymentDate = XPath.path (context, "paymentDates", "lastRegularPaymentDate");
					Element	startDate	= XPath.path (context, "calculationPeriodDates", "firstRegularPeriodStartDate");
					Element	endDate		= XPath.path (context, "calculationPeriodDates", "lastRegularPeriodEndDate");
					
					if (!exists (startDate))
						startDate = XPath.path (context, "calculationPeriodDates", "effectiveDate", "unadjustedDate");

					if (!exists (endDate))
						endDate = XPath.path (context, "calculationPeriodDates", "terminationDate", "unadjustedDate");

					Interval interval	= toInterval (XPath.path (context, "calculationPeriodDates", "calculationPeriodFrequency"));

					if ((paymentDate == null) || (startDate == null) || (endDate == null) || (interval == null) ||
							isUnadjustedCalculationPeriodDate (
									toDate (paymentDate), toDate (startDate), toDate (endDate),
									interval)) continue;

					errorHandler.error ("305", context,
						"The first payment date '" + toToken (paymentDate) + "' does not " +
						"fall on one of the unadjusted calculation period dates.",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures calculation frequency is a
	 * multiple of the reset frequency.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE05 = new Rule (INTEREST_RATE_STREAM, "ird-5")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context	= (Element) list.item (index);

					if (!isParametric (context)) continue;

					Element	calcFreq	= XPath.path (context, "calculationPeriodDates", "calculationPeriodFrequency");
					Element	resetFreq	= XPath.path (context, "resetDates", "resetFrequency");

					if ((calcFreq == null) || (resetFreq == null)) continue;

					Interval calc  = toInterval (calcFreq);
					Interval reset = toInterval (resetFreq);

					if ((calc == null) || (reset == null) || calc.isMultipleOf (reset))
						continue;

					errorHandler.error ("305", context,
						"Calculation frequency '" + toInterval (calcFreq) +
						"' is not an integer multiple of reset frequency '" +
						toInterval (resetFreq) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the first payment date is
	 * after the effective date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE06 = new Rule (INTEREST_RATE_STREAM, "ird-6")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context	= (Element) list.item (index);

					if (!isParametric (context)) continue;

					Element	payment	  = XPath.path (context, "paymentDates", "firstPaymentDate");
					Element	effective = XPath.path (context, "calculationPeriodDates", "effectiveDate", "unadjustedDate");

					if ((payment == null) || (effective == null) || greater (toDate (payment), toDate (effective)))
						continue;

					errorHandler.error ("305", context,
						"The first payment date " + toToken (payment) + " must be after " +
						"the unadjusted effective date " + toToken (effective),
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures compounding method is present
	 * when the payment frequency is less often than the calculation
	 * frequency.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE07 = new Rule (INTEREST_RATE_STREAM, "ird-7")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context	= (Element) list.item (index);

					if (!isParametric (context)) continue;

					Element	compounding	= XPath.path (context, "calculationPeriodAmount", "calculation", "compoundingMethod");
					Element	paymentFreq	= XPath.path (context, "paymentDates", "paymentFrequency");
					Element	calcFreq	= XPath.path (context, "calculationPeriodDates", "calculationPeriodFrequency");

					if ((paymentFreq == null) || (calcFreq == null)) continue;

					Interval payment = toInterval (paymentFreq);
					Interval calc    = toInterval (calcFreq);

					if ((payment == null) || (calc == null)) continue;

					if (iff (
							exists (compounding),
							not (payment.equals (calc))))
						continue;

					errorHandler.error ("305", context,
						"Compounding method must only be present when the payment frequency '" +
						toInterval (paymentFreq) + "' is different from the calculation " +
						"frequency '" + toInterval (calcFreq) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the payer and receiver are not
	 * the same party.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE08_OLD = new Rule (
			Precondition.and (R1_0__R4_9, INTEREST_RATE_STREAM), "ird-8[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context = (Element) list.item (index);
					Element payer	 = DOM.getElementByLocalName (context, "payerPartyReference");
					Element receiver = DOM.getElementByLocalName (context, "receiverPartyReference");

					if (payer.getAttribute ("href").equals (receiver.getAttribute ("href"))) {
						errorHandler.error ("305", context,
							"The payer and receiver references must not be the same",
							getDisplayName (), payer.getAttribute ("href"));
						result = false;
					}
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the payer and receiver are not
	 * the same party.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE08 = new Rule (
			Precondition.and (R5_0__LATER, INTEREST_RATE_STREAM), "ird-8")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context = (Element) list.item (index);
					Element payer	 = DOM.getElementByLocalName (context, "payerPartyReference");
					Element receiver = DOM.getElementByLocalName (context, "receiverPartyReference");

					if ((payer == null) || (receiver == null) || !payer.getAttribute ("href").equals (receiver.getAttribute ("href")))
						continue;

					payer	 = DOM.getElementByLocalName (context, "payerAccountReference");
					receiver = DOM.getElementByLocalName (context, "receiverAccountReference");
									
					if ((payer != null) && (receiver != null) && !payer.getAttribute ("href").equals (receiver.getAttribute ("href")))
						continue;
					
					errorHandler.error ("305", context,
						"The payer and receiver references must not be the same",
						getDisplayName (), null);
					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures compounding method is present
	 * only when reset dates are defined.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE09 = new Rule (INTEREST_RATE_STREAM, "ird-9")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "InterestRateStream"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("swapStream"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capFloorStream"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context = (Element) list.item (index);

					if (implies (
							exists (XPath.path (context, "calculationPeriodAmount", "calculation", "compoundingMethod")),
							exists (XPath.path (context, "resetDates"))))
						continue;

					errorHandler.error ("305", context,
						"calculationPeriodAmount/calculation/compoundingMethod can only be " +
						"present if a resetDates element is present",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the calculation period start date is
	 * consistent with the roll convention.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE10 = new Rule (CALCULATION_PERIOD_DATES, "ird-10")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context = (Element) list.item (index);
					Element rollConvention	= XPath.path (context, "calculationPeriodFrequency", "rollConvention");

					if (!isNumber (toToken (rollConvention))) continue;

					Element	startDate = XPath.path (context, "firstRegularPeriodStartDate");
					if (!exists (startDate))
						startDate = XPath.path (context, "effectiveDate", "unadjustedDate");

					if (startDate == null) continue;
					
					int		rollDate = toInteger (rollConvention);
					Date	start	 = toDate (startDate);

					if (rollDate < start.lastDayOfMonth ()) {
						if (rollDate == start.dayOfMonth ()) continue;
					}
					else
						if (start.isEndOfMonth()) continue;

					errorHandler.error ("305", context,
						"The start date of the calculation period,  '" + start + "' is not " +
						"consistent with the roll convention " + toToken (rollConvention),
						getDisplayName (), null);

					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the calculation period end date is
	 * consistent with the roll convention.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE11 = new Rule (CALCULATION_PERIOD_DATES, "ird-11")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context = (Element) list.item (index);
					Element rollConvention	= XPath.path (context, "calculationPeriodFrequency", "rollConvention");

					if (!isNumber (toToken (rollConvention))) continue;

					Element	endDate = XPath.path (context, "lastRegularPeriodEndDate");
					if (!exists (endDate))
						endDate = XPath.path (context, "terminationDate", "unadjustedDate");

					int		rollDate = toInteger (rollConvention);
					Date	end	 = toDate (endDate);
					
					if (end == null) continue;

					if (rollDate <= end.lastDayOfMonth ()) {
						if (rollDate == end.dayOfMonth ()) continue;
					}
					else
						if (end.isEndOfMonth()) continue;

					errorHandler.error ("305", context,
						"The end date of the calculation period,  '" + end + "' is not " +
						"consistent with the roll convention " + toToken (rollConvention),
						getDisplayName (), null);

					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the calculation period divides
	 * the regular period precisely.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE12 = new Rule (CALCULATION_PERIOD_DATES, "ird-12")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context	= (Element) list.item (index);
	                Element		indexName = XPath.path (context, "..", "calculationPeriodAmount", "calculation", "floatingRateCalculation", "floatingRateIndex");

	                if ((indexName != null) && equal (indexName, "CNY-CNREPOFIX=CFXS-Reuters")) continue;

					Element		start	= XPath.path (context, "firstRegularPeriodStartDate");
					Element		end		= XPath.path (context, "lastRegularPeriodEndDate");
					Element		period	= XPath.path (context, "calculationPeriodFrequency");

					if (start == null) start = XPath.path (context, "effectiveDate", "unadjustedDate");
					if (end   == null) end   = XPath.path (context, "terminationDate", "unadjustedDate");

					if ((start != null) && (end != null) && (period != null)) {
						Date		startDate = toDate (start);
						Date		endDate	  = toDate (end);
						Interval	interval  = toInterval (period);

						if ((startDate == null) || (endDate == null) || (interval == null)) continue;

						if (interval.dividesDates (startDate, endDate)) continue;

						errorHandler.error ("305", context,
							"The calculation period '" + startDate + "' to '" + endDate +
							"' is not a multiple of the frequency '" + interval + "'",
							getDisplayName (), null);

						result = false;
					}
				}

				return (result);
			}
		};

	// Rule 13 was unlucky for some.

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted termination date
	 * is after the unadjusted effective date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE14 = new Rule (CALCULATION_PERIOD_DATES, "ird-14")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	termination	= XPath.path (context, "terminationDate", "unadjustedDate");
					Element	effective	= XPath.path (context, "effectiveDate", "unadjustedDate");

					if ((termination == null) || (effective == null) || greater (termination, effective))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted termination date '" + toToken (termination) + "' should " +
						"be after unadjusted effective date '" + toToken (effective) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted termination date
	 * is after the unadjusted first period date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE15 = new Rule (CALCULATION_PERIOD_DATES, "ird-15")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context		= (Element) list.item (index);
					Element	termination	= XPath.path (context, "terminationDate", "unadjustedDate");
					Element	periodStart	= XPath.path (context, "firstPeriodStartDate", "unadjustedDate");

					if ((termination == null) || (periodStart == null) || greater (termination, periodStart))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted termination date '" + toToken (termination) + "' should " +
						"be after unadjusted first period start date '" + toToken (periodStart) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted termination date
	 * is after the unadjusted first regular period start date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE16 = new Rule (CALCULATION_PERIOD_DATES, "ird-16")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	termination	= XPath.path (context, "terminationDate", "unadjustedDate");
					Element	periodStart	= XPath.path (context, "firstRegularPeriodStartDate");

					if ((termination == null) || (periodStart == null) || greater (termination, periodStart))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted termination date '" + toToken (termination) + "' should " +
						"be after unadjusted first regular period start date '" + toToken (periodStart) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted termination date
	 * is after the unadjusted last regular period end date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE17 = new Rule (CALCULATION_PERIOD_DATES, "ird-17")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	termination	= XPath.path (context, "terminationDate", "unadjustedDate");
					Element	periodEnd	= XPath.path (context, "lastRegularPeriodEndDate");

					if ((termination == null) || (periodEnd == null) || greater (termination, periodEnd))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted termination date '" + toToken (termination) + "' should " +
						"be after unadjusted last regular period end date '" + toToken (periodEnd) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted last regular period
	 * end date is after the unadjusted first regular period start date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE18 = new Rule (CALCULATION_PERIOD_DATES, "ird-18")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	periodEnd	= XPath.path (context, "lastRegularPeriodEndDate");
					Element	periodStart	= XPath.path (context, "firstRegularPeriodStartDate");

					if ((periodEnd == null) || (periodStart == null) || greater (periodEnd, periodStart))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted last regular period end date '" + toToken (periodEnd) + "' should " +
						"be after unadjusted first regular period start date '" + toToken (periodStart) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted last regular period
	 * end date is after the unadjusted first period start date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE19 = new Rule (CALCULATION_PERIOD_DATES, "ird-19")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	periodEnd	= XPath.path (context, "lastRegularPeriodEndDate");
					Element	periodStart	= XPath.path (context, "firstPeriodStartDate", "unadjustedDate");

					if ((periodEnd == null) || (periodStart == null) || greater (periodEnd, periodStart))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted last regular period end date '" + toToken (periodEnd) + "' should " +
						"be after unadjusted first period start date '" + toToken (periodStart) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted last regular period
	 * end date is after the unadjusted effective date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE20 = new Rule (CALCULATION_PERIOD_DATES, "ird-20")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	last	  	= XPath.path (context, "lastRegularPeriodEndDate");
					Element	effective 	= XPath.path (context, "effectiveDate", "unadjustedDate");

					if ((last == null) || (effective == null) || greater (last, effective))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted last regular period end date " + toToken (last) +
						" must be after unadjusted effective date " + toToken (effective),
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted first period state
	 * date is before the unadjusted effective date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE21 = new Rule (CALCULATION_PERIOD_DATES, "ird-21")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element stream		= DOM.getParent (context);

					if (!isParametric (stream)) continue;

					Element	first	  	= XPath.path (context, "firstPeriodStartDate", "unadjustedDate");
					Element	effective 	= XPath.path (context, "effectiveDate", "unadjustedDate");

					if ((first == null) || (effective == null) || less (toDate (first), toDate (effective)))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted first period start date " + toToken (first) +
						" must be before unadjusted effective date " + toToken (effective),
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the unadjusted first period start
	 * date is before the unadjusted first regular period start date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE22 = new Rule (CALCULATION_PERIOD_DATES, "ird-22")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriodDates"), errorHandler));					
					
				return (
					  validate (nodeIndex.getElementsByName ("calculationPeriodDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	first		= XPath.path (context, "firstPeriodStartDate", "unadjustedDate");
					Element	regular 	= XPath.path (context, "firstRegularPeriodStartDate");

					if ((first == null) || (regular == null) || less (first, regular))
						continue;

					errorHandler.error ("305", context,
						"Unadjusted first period start date " + toToken (first) +
						" must be before first regular period start date " + toToken (regular),
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the initial stub is only present
	 * under the right conditions.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE23 = new Rule (R1_0__LATER, "ird-23")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex, nodeIndex.getElementsByType (determineNamespace (nodeIndex), "StubCalculationPeriodAmount"), errorHandler));					
					
				return (
					  validate (nodeIndex, nodeIndex.getElementsByName ("stubCalculationPeriodAmount"), errorHandler));
			}
			
			private boolean validate (NodeIndex nodeIndex, NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context		= (Element) list.item (index);
					Element		datesRef	= XPath.path (context, "calculationPeriodDatesReference");

					if (datesRef == null) continue;

					String		href		= datesRef.getAttribute ("href");

					// Remove leading # from XPointer type references
					if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
						href = href.substring (1);

					Element		periodDates	= nodeIndex.getElementById (href);

					if (periodDates == null) continue;

					if (implies (
							exists (XPath.path (context, "initialStub")),
							or (
								exists (XPath.path (periodDates, "firstPeriodStartDate")),
								exists (XPath.path (periodDates, "firstRegularPeriodStartDate"))))) continue;

					errorHandler.error ("305", context,
						"Initial stub is present but neither a first start date or first regular " +
						"period start date is defined in the referenced calculation period dates",
						getDisplayName (), null);

					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the final stub is only present
	 * under the right conditions.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE24 = new Rule (R1_0__LATER, "ird-24")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex, nodeIndex.getElementsByType (determineNamespace (nodeIndex), "StubCalculationPeriodAmount"), errorHandler));					
					
				return (
					  validate (nodeIndex, nodeIndex.getElementsByName ("stubCalculationPeriodAmount"), errorHandler));
			}
			
			private boolean validate (NodeIndex nodeIndex, NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context		= (Element) list.item (index);
					Element		datesRef	= XPath.path (context, "calculationPeriodDatesReference");

					if (datesRef == null) continue;

					String		href		= datesRef.getAttribute ("href");

					// Remove leading # from XPointer type references
					if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
						href = href.substring (1);

					Element	periodDates	= nodeIndex.getElementById (href);

					if (periodDates == null) continue;

					if (implies (
							exists (XPath.path (context, "finalStub")),
							exists (XPath.path (periodDates, "lastRegularPeriodEndDate")))) continue;

					errorHandler.error ("305", context,
						"Final stub is present but no last regular period end date is defined " +
						"in the referenced calculation period dates",
						getDisplayName (), null);

					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that if not steps are present
	 * the initial value is non-zero.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE25_OLD = new Rule (R1_0__R4_6, "ird-25[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Schedule"), errorHandler));					

				return (
					  validate (nodeIndex.getElementsByName ("floatingRateMultiplierSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("spreadSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixedRateSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("feeRateSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capRateSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("floorRateSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("knownAmountSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("notionalStepSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("feeAmountSchedule"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context		= (Element) list.item (index);

					if (implies (
							not (exists (XPath.path (context, "step"))),
							notEqual (
								XPath.path (context, "initialValue"),
								ZERO))) continue;

					errorHandler.error ("305", context,
						"An non-zero initial value must be provided when there are no steps " +
						"in the schedule",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that if not steps are present
	 * the initial value is non-zero.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE25 = new Rule (R4_7__LATER, "ird-25")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Schedule"), errorHandler));					

				return (
					  validate (nodeIndex.getElementsByName ("floatingRateMultiplierSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("spreadSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixedRateSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("feeRateSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("capRateSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("floorRateSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("knownAmountSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("feeAmountSchedule"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context		= (Element) list.item (index);

					if (implies (
							not (exists (XPath.path (context, "step"))),
							notEqual (
								XPath.path (context, "initialValue"),
								ZERO))) continue;

					errorHandler.error ("305", context,
						"An non-zero initial value must be provided when there are no steps " +
						"in the schedule",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the business centers reference
	 * locates a set of business centers in the document.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE26 = new Rule (R1_0__LATER, "ird-26")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex, nodeIndex.getElementsByType (determineNamespace (nodeIndex), "BusinessCentersReference"), errorHandler));					
					
				return (
					  validate (nodeIndex, nodeIndex.getElementsByName ("businessCentersReference"), errorHandler));
			}
			
			private boolean validate (NodeIndex nodeIndex, NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context		= (Element) list.item (index);
					String	href		= context.getAttribute ("href");

					// Handle XPointer syntax
					if ((href != null) && (href.length () > 1) && (href.charAt (0) == '#'))
						href = href.substring (1);

					Element	target	= nodeIndex.getElementById (href);

					if ((target == null) || (target.getLocalName ().equals ("businessCenters"))) continue;

					errorHandler.error ("305", context,
						"The businessCenterReference/@href attribute must reference a businessCenters element",
						getDisplayName (), context.getAttribute ("href"));

					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the cash settlement payment date
	 * is not present.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE27 = new Rule (R1_0__LATER, "ird-27")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "MandatoryEarlyTermination"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("mandatoryEarlyTermination"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	paymentDate	= XPath.path (context, "cashSettlement", "cashSettlementPaymentDate");

					if (not (exists (paymentDate))) continue;

					errorHandler.error ("305", context,
						"Mandatory early termination must not contain a cash settlement " +
						"payment date",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the cash settlement payment date
	 * references the early termination date.
	 * <P>
	 * Applies to FpML 1-0, 2-0 and 3-0.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE28_XLINK = new Rule (R1_0__R3_0, "ird-28[XLINK]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "MandatoryEarlyTermination"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("mandatoryEarlyTermination"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context		= (Element) list.item (index);
					Element	reference	= XPath.path (context, "cashSettlement", "cashSettlementValuationDate", "dateRelativeTo");
					Element	definition	= XPath.path (context, "mandatoryEarlyTermination");

					if ((reference != null) && (definition != null)) {
						String	href	= reference.getAttribute ("href");
						String	id		= definition.getAttribute ("id");

						// Remove leading # from XPointer type references
						if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
							href = href.substring (1);

						if ((href != null) && (id != null) && href.equals (id)) continue;

						errorHandler.error ("305", context,
							"The href of the relative cash settlement valuation date must refer to " +
							"the mandatory early termination date",
							getDisplayName (), href);

						result = false;
					}
				}
				return (result);
			}
		};

		/**
		 * A <CODE>Rule</CODE> that ensures the cash settlement payment date
		 * references the early termination date.
		 * <P>
		 * Applies to FpML 4-0 and later.
		 * @since	TFP 1.0
		 */
		public static final Rule	RULE28 = new Rule (R4_0__LATER, "ird-28")
			{
				/**
				 * {@inheritDoc}
				 */
				public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
				{
					if (nodeIndex.hasTypeInformation()) 
						return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "MandatoryEarlyTermination"), errorHandler));
					
					return (validate (nodeIndex.getElementsByName ("mandatoryEarlyTermination"), errorHandler));
				}

				private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
				{
					boolean		result = true;

					for (int index = 0; index < list.getLength (); ++index) {
						Element	context		= (Element) list.item (index);
						Element	reference	= XPath.path (context, "cashSettlement", "cashSettlementValuationDate", "dateRelativeTo");
						Element	definition	= XPath.path (context, "mandatoryEarlyTerminationDate");

						if ((reference != null) && (definition != null)) {
							String	href	= reference.getAttribute ("href");
							String	id		= definition.getAttribute ("id");

							if ((href != null) && (id != null) && href.equals (id)) continue;

							errorHandler.error ("305", context,
								"The href of the relative cash settlement valuation date must refer to " +
								"the mandatory early termination date",
								getDisplayName (), href);

							result = false;
						}
					}
					return (result);
				}
			};

	/**
	 * A <CODE>Rule</CODE> that ensures that floating rate calculations
	 * are present if there is compounding.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE29 = new Rule (R1_0__LATER, "ird-29")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Calculation"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("calculation"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	compounding	= XPath.path (context, "compoundingMethod");
					Element	floating	= XPath.path (context, "floatingRateCalculation");
					Element	inflation	= XPath.path (context, "inflationRateCalculation");

					if (implies (
							exists (compounding),
							or (
							    exists (floating),
							    exists (inflation))))
						continue;

					errorHandler.error ("305", context,
						"The calculation element contains a compounding method but " +
						"no floating rate calculation element",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that a start date is specified.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE30 = new Rule (R1_0__LATER, "ird-30")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriod"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("calculationPeriod"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	unadjusted	= XPath.path (context, "unadjustedStartDate");
					Element	adjusted	= XPath.path (context, "adjustedStartDate");

					if (or (
							exists (unadjusted),
							exists (adjusted)))
						continue;

					errorHandler.error ("305", context,
						"Calculation period contains neither an adjusted nor unadjusted " +
						"start date",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that an end date is specified.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE31 = new Rule (R1_0__LATER, "ird-31")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationPeriod"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("calculationPeriod"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	unadjusted	= XPath.path (context, "unadjustedEndDate");
					Element	adjusted	= XPath.path (context, "adjustedEndDate");

					if (or (
							exists (unadjusted),
							exists (adjusted)))
						continue;

					errorHandler.error ("305", context,
						"Calculation period contains neither an adjusted nor unadjusted " +
						"end date",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that discount rate day count
	 * fraction is only present if there is a discount rate.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE32 = new Rule (R1_0__LATER, "ird-32")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Discounting"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("discounting"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	rate		= XPath.path (context, "discountRate");
					Element	dayCount	= XPath.path (context, "discountRateDayCountFraction");

					if (implies (
							not (exists (rate)),
							not (exists (dayCount))))
						continue;

					errorHandler.error ("305", context,
						"Discount rate is missing but discount rate day fraction is present",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that the adjusted termination
	 * date is after the adjusted effective date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE33 = new Rule (R1_0__LATER, "ird-33")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Fra"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("fra"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	termination	= XPath.path (context, "adjustedTerminationDate");
					Element	effective	= XPath.path (context, "adjustedEffectiveDate");

					if ((termination == null) || (effective == null) || greater (termination, effective))
						continue;

					errorHandler.error ("305", context,
						"Adjusted termination date '" + toToken (termination) + "' must be " +
						"after adjusted effective date '" + toToken (effective) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that a payment date is specified.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE34 = new Rule (R1_0__LATER, "ird-34")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "PaymentCalculationPeriod"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("paymentCalculationPeriod"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	unadjusted	= XPath.path (context, "unadjustedPaymentDate");
					Element	adjusted	= XPath.path (context, "adjustedPaymentDate");

					if (or (
							exists (unadjusted),
							exists (adjusted)))
						continue;

					errorHandler.error ("305", context,
						"Both the unadjusted and adjusted payment date are missing from " +
						"the payment calculation period",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the first payment date is before
	 * the last regular payment date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE35 = new Rule (R1_0__LATER, "ird-35")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "PaymentDates"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("paymentDates"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	firstDate	= XPath.path (context, "firstPaymentDate");
					Element	lastDate	= XPath.path (context, "lastRegularPaymentDate");

					if ((firstDate == null) || (lastDate == null) || less (toDate (firstDate), toDate (lastDate)))
						continue;

					errorHandler.error ("305", context,
						"The first payment date '" + toToken (firstDate) + "' should be " +
						"before the last regular payment date '" + toToken (lastDate) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the interval between the first
	 * payment date and the last regular payment date is a multiple of
	 * the payment frequency.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE36 = new Rule (R1_0__LATER, "ird-36")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "PaymentDates"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("paymentDates"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context	= (Element) list.item (index);
					Element	first	= XPath.path (context, "firstPaymentDate");
					Element	last	= XPath.path (context, "lastRegularPaymentDate");
					Element	period	= XPath.path (context, "paymentFrequency");

					if ((first != null) && (last != null)) {
						Date		firstDate	= toDate (first);
						Date		lastDate	= toDate (last);
						Interval	frequency	= toInterval (period);

						if (frequency.dividesDates (firstDate, lastDate)) continue;

						errorHandler.error ("305", context,
							"The first payment date and last regular payment date are not " +
							"a multiple of the payment frequency apart", getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures one of an initial or final
	 * stud is present.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE38 = new Rule (R1_0__LATER, "ird-38")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "StubCalculationPeriodAmount"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("stubCalculationPeriodAmount"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	initial		= XPath.path (context, "initialStub");
					Element	finalStub	= XPath.path (context, "finalStub");

					if (or (
							exists (initial),
							exists (finalStub)))
						continue;

					errorHandler.error ("305", context,
						"Both the initial and final stub are missing from the stub " +
						"calculation period amount",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the adjusted exercise date is
	 * not after the adjusted termination date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE39 = new Rule (R1_0__LATER, "ird-39")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "EarlyTerminationEvent"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("earlyTerminationEvent"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	exercise	= XPath.path (context, "adjustedExerciseDate");
					Element	termination	= XPath.path (context, "adjustedEarlyTerminationDate");

					if ((exercise == null) || (termination == null) || lessOrEqual (exercise, termination))
						continue;

					errorHandler.error ("305", context,
						"The adjusted exercise date '" + toToken (exercise) + "' should be " +
						"on or before the adjusted early termination date '" + toToken (termination) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the adjusted exercise date is
	 * not after the adjusted cash settlement valuation date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE40 = new Rule (R1_0__LATER, "ird-40")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "EarlyTerminationEvent"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("earlyTerminationEvent"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	exercise	= XPath.path (context, "adjustedExerciseDate");
					Element	valuation	= XPath.path (context, "adjustedCashSettlementValuationDate");

					if ((exercise == null) || (valuation == null) || lessOrEqual (exercise, valuation))
						continue;

					errorHandler.error ("305", context,
						"The adjusted exercise date '" + toToken (exercise) + "' should be " +
						"on or before the adjusted cash settlement date '" +
						toToken (valuation) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the adjusted cash settlement
	 * valuation date is not after the adjusted cash settlement payment
	 * date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE41 = new Rule (R1_0__LATER, "ird-41")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "EarlyTerminationEvent"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("earlyTerminationEvent"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	valuation	= XPath.path (context, "adjustedCashSettlementValuationDate");
					Element	payment		= XPath.path (context, "adjustedCashSettlementPaymentDate");

					if ((payment == null) || (valuation == null) || lessOrEqual (valuation, payment))
						continue;

					errorHandler.error ("305", context,
						"The adjusted case settlement valuation date '" + toToken (valuation) +
						"' should be on or before the adjusted cash settlement payment date '" +
						toToken (payment) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the adjusted exercise date is
	 * before the adjusted extended termination date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE42 = new Rule (R1_0__LATER, "ird-42")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ExtensionEvent"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("extensionEvent"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	exercise	= XPath.path (context, "adjustedExerciseDate");
					Element	termination	= XPath.path (context, "adjustedExtendedTerminationDate");

					if ((exercise == null) || (termination == null) ||
						less (Types.toDate (exercise), Types.toDate (termination)))
						continue;

					errorHandler.error ("305", context,
						"The adjusted exercise date '" + toToken (exercise) + "' should be " +
						"before the adjusted extended termination date '" + toToken (termination) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that at least one child element is
	 * present.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE43 = new Rule (R1_0__LATER, "ird-43")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "FxLinkedNotionalAmount"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("fxLinkedNotionalAmount"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);

					if (exists (DOM.getFirstChild (context))) continue;

					errorHandler.error ("305", context,
						"fxLinkedNotionalAmount did not contain any elements",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the adjusted early termination
	 * date is not after the adjusted cash settlement date.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE44 = new Rule (R1_0__LATER, "ird-44")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "MandatoryEarlyTerminationAdjustedDates"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("mandatoryEarlyTerminationAdjustedDates"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	termination	= XPath.path (context, "adjustedEarlyTerminationDate");
					Element	valuation	= XPath.path (context, "adjustedCashSettlementValuationDate");
					Element	payment		= XPath.path (context, "adjustedCashSettlementPaymentDate");

					if ((termination == null) || (valuation == null) || (payment == null) ||
						and (
							lessOrEqual (termination, valuation),
							lessOrEqual (valuation, payment)))
						continue;

					errorHandler.error ("305", context,
						"The adjusted mandatory early termination date '" + toToken (termination) + "', " +
						"cash settlement valuation date '" + toToken (valuation) + "' and " +
						"cash settlement payment date '" + toToken (payment) + "' " +
						"are not in order",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the cash settlement valuation
	 * date is relative to the cash settlement payment date.
	 * <P>
	 * Applies to FpML 1.0, 2.0 and 3.0.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE46_XLINK
		= new Rule (R1_0__R3_0, "ird-46[XLINK]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (validate (nodeIndex.getElementsByName ("optionalEarlyTermination"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	reference	= XPath.path (context, "cashSettlement", "cashSettlementValuationDate", "dateRelativeTo");
					Element	definition	= XPath.path (context, "cashSettlement");

					if ((reference == null) || (definition == null)) continue;

					String		href	= reference.getAttribute ("href");
					String		id		= definition.getAttribute ("id");

					// Remove leading # from XPointer type references
					if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
						href = href.substring (1);

					if ((href != null) && (id != null) && equal (href, id)) continue;

					errorHandler.error ("305", context,
						"dateRelativeTo element in cash settlement valuation date must " +
						"be relative to the cash settlement payment date",
						getDisplayName (), href);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the cash settlement valuation
	 * date is relative to the cash settlement payment date.
	 * <P>
	 * Applies to all FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE46
		= new Rule (R4_0__LATER, "ird-46")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "OptionalEarlyTermination"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("optionalEarlyTermination"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	reference	= XPath.path (context, "cashSettlement", "cashSettlementValuationDate", "dateRelativeTo");
					Element	definition	= XPath.path (context, "cashSettlement", "cashSettlementPaymentDate");

					if ((reference == null) || (definition == null)) continue;

					String		href	= reference.getAttribute ("href");
					String		id		= definition.getAttribute ("id");

					if ((href != null) && (id != null) && equal (href, id)) continue;

					errorHandler.error ("305", context,
						"dateRelativeTo element in cash settlement valuation date must " +
						"be relative to the cash settlement payment date",
						getDisplayName (), href);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the cash settlement payment
	 * date for an early termination is relative to an exercise definition.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE47_XLINK = new Rule (R1_0__R3_0, "ird-47[XLINK]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (validate (nodeIndex.getElementsByName ("optionalEarlyTermination"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	reference	= XPath.path (context, "cashSettlement", "cashSettlementPaymentDate", "relativeDate", "dateRelativeTo");
					Element	exercise	= XPath.path (context, "americanExercise");

					if (exercise == null) {
						exercise = XPath.path (context, "bermudaExercise");
						if (exercise == null)
							exercise = XPath.path (context, "europeanExercise");
					}

					if ((reference == null) || (exercise == null)) continue;

					String		href	= reference.getAttribute ("href");
					String		id		= exercise.getAttribute ("id");

					// Remove leading # from XPointer type references
					if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
						href = href.substring (1);

					if ((href != null) && (id != null) && equal (href, id))
						continue;

					errorHandler.error ("305", context,
						"dateRelativeTo element in cash settlement payment date must " +
						"be relative to the exercise structure",
						getDisplayName (), href);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the cash settlement payment
	 * date for an early termination is relative to an exercise definition.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE47 = new Rule (R4_0__LATER, "ird-47")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "OptionalEarlyTermination"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("optionalEarlyTermination"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	reference	= XPath.path (context, "cashSettlement", "cashSettlementPaymentDate", "relativeDate", "dateRelativeTo");
					Element	exercise	= XPath.path (context, "americanExercise");

					if (exercise == null) {
						exercise = XPath.path (context, "bermudaExercise");
						if (exercise == null)
							exercise = XPath.path (context, "europeanExercise");
					}

					if ((reference == null) || (exercise == null)) continue;

					String		href	= reference.getAttribute ("href");
					String		id		= exercise.getAttribute ("id");

					if ((href != null) && (id != null) && equal (href, id))
						continue;

					errorHandler.error ("305", context,
						"dateRelativeTo element in cash settlement payment date must " +
						"be relative to the exercise structure",
						getDisplayName (), href);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the cash settlement payment
	 * date for a swaption is relative to an exercise definition.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE48 = new Rule (R1_0__LATER, "ird-48")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Swaption"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("swaption"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	reference	= XPath.path (context, "cashSettlement", "cashSettlementPaymentDate", "relativeDate", "dateRelativeTo");
					Element	exercise	= XPath.path (context, "americanExercise");

					if (exercise == null) {
						exercise = XPath.path (context, "bermudaExercise");
						if (exercise == null)
							exercise = XPath.path (context, "europeanExercise");
					}

					if ((reference == null) || (exercise == null)) continue;

					String		href	= reference.getAttribute ("href");
					String		id		= exercise.getAttribute ("id");

					// Remove leading # from XPointer type references
					if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
						href = href.substring (1);

					if ((href != null) && (id != null) && equal (href, id))
						continue;

					errorHandler.error ("305", context,
						"dateRelativeTo element in cash settlement payment date must " +
						"be relative to the exercise structure",
						getDisplayName (), href);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the weekly roll convention is
	 * specified for a weekly period.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE49 = new Rule (R1_0__LATER, "ird-49")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) 
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ResetFrequency"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("resetFrequency"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	period		= XPath.path (context, "period");

					if (iff (
							exists (XPath.path (context, "weeklyRollConvention")),
							equal (period, "W")))
						continue;

					errorHandler.error ("305", context,
						"weeklyRollConvention should be present if and only if the period is 'W'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the step dates are valid
	 * for a notional step schedule.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE50 = new Rule (INTEREST_RATE_STREAM, "ird-50")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				NodeList	list	= nodeIndex.getElementsByName ("notionalStepSchedule");

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context 	= (Element) list.item (index);
					NodeList dates		= XPath.paths (context, "step", "stepDate");
					Element	calculation	= XPath.path (context, "..", "..", "..", "..", "calculationPeriodDates");

					Element	firstDate	= XPath.path (calculation, "firstRegularPeriodStartDate");
					Element	lastDate	= XPath.path (calculation, "lastRegularPeriodEndDate");

					if (firstDate == null)
						firstDate = XPath.path (calculation, "effectiveDate", "unadjustedDate");

					if (lastDate == null)
						lastDate  = XPath.path (calculation, "terminationDate", "unadjustedDate");

					Element	period			= XPath.path (calculation, "calculationPeriodFrequency");
					Interval interval		= toInterval (period);

					if ((firstDate == null) || (lastDate == null) || (interval == null)) continue;

					Date		first	= toDate (firstDate);
					Date		last	= toDate (lastDate);

					if ((first == null) || (last == null)) continue;

					for (int count = 0; count < dates.getLength (); ++count) {
						Element		date 	= (Element) dates.item (count);
						Date		payment = toDate (date);

						if (isUnadjustedCalculationPeriodDate (payment, first, last, interval)) continue;

						errorHandler.error ("305", context,
							"The notional step schedule step date '" + payment + "' does not fall " +
							"on one of the calculated period dates between '" + first + "' and '" +
							last + "'",
							getDisplayName (), toToken (date));

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the step dates are valid
	 * for a fixed rate schedule.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE51 = new Rule (INTEREST_RATE_STREAM, "ird-51")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				NodeList	list 	= nodeIndex.getElementsByName ("fixedRateSchedule");

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context		= (Element) list.item (index);
					NodeList dates		= XPath.paths (context, "step", "stepDate");
					Element	calculation	= XPath.path (context, "..", "..", "..", "calculationPeriodDates");


					Element	firstDate	= XPath.path (calculation, "firstRegularPeriodStartDate");
					Element	lastDate	= XPath.path (calculation, "lastRegularPeriodEndDate");

					if (firstDate == null)
						firstDate = XPath.path (calculation, "effectiveDate", "unadjustedDate");

					if (lastDate == null)
						lastDate  = XPath.path (calculation, "terminationDate", "unadjustedDate");

					Element	period		= XPath.path (calculation, "calculationPeriodFrequency");
					Interval interval	= toInterval (period);

					if ((firstDate == null) || (lastDate == null) || (interval == null)) continue;

					Date first	= toDate (firstDate);
					Date last	= toDate (lastDate);

					if ((first == null) || (last == null)) continue;

					for (int count = 0; count < dates.getLength (); ++count) {
						Element		date	= (Element) dates.item (count);
						Date		payment = toDate (date);

						if (isUnadjustedCalculationPeriodDate (payment, first, last, interval)) continue;

						errorHandler.error ("305", context,
							"The fixed rate schedule step date '" + payment + "' does not fall " +
							"on one of the calculated period dates between '" + first + "' and '" +
							last + "'",
							getDisplayName (), toToken (date));

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the step dates are valid
	 * for a cap rate schedule.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE52 = new Rule (INTEREST_RATE_STREAM, "ird-52")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result  = true;
				NodeList	list	= nodeIndex.getElementsByName ("capRateSchedule");

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context		= (Element) list.item (index);
					NodeList dates		= XPath.paths (context, "step", "stepDate");
					Element	calculation	= XPath.path (context, "..", "..", "..", "..", "calculationPeriodDates");

					Element	firstDate	= XPath.path (calculation, "firstRegularPeriodStartDate");
					Element	lastDate	= XPath.path (calculation, "lastRegularPeriodEndDate");

					if (firstDate == null)
						firstDate = XPath.path (calculation, "effectiveDate", "unadjustedDate");

					if (lastDate == null)
						lastDate  = XPath.path (calculation, "terminationDate", "unadjustedDate");

					Element	period		= XPath.path (calculation, "calculationPeriodFrequency");
					Interval interval	= toInterval (period);

					if ((firstDate == null) || (lastDate == null) || (interval == null)) continue;

					Date		first	= toDate (firstDate);
					Date		last	= toDate (lastDate);

					if ((first == null) || (last == null)) continue;

					for (int count = 0; count < dates.getLength (); ++count) {
						Element		date	= (Element) dates.item (count);
						Date		payment = toDate (date);

						if (isUnadjustedCalculationPeriodDate (payment, first, last, interval)) continue;

						errorHandler.error ("305", context,
							"The cap rate schedule step date '" + payment + "' does not fall " +
							"on one of the calculated period dates between '" + first + "' and '" +
							last + "'",
							getDisplayName (), toToken (date));

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the step dates are valid
	 * for a floor rate schedule.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE53 = new Rule (INTEREST_RATE_STREAM, "ird-53")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				NodeList	list	= nodeIndex.getElementsByName ("floorRateSchedule");

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context		= (Element) list.item (index);
					NodeList	dates	= XPath.paths (context, "step", "stepDate");
					Element	calculation	= XPath.path (context, "..", "..", "..", "..", "calculationPeriodDates");

					Element	firstDate	= XPath.path (calculation, "firstRegularPeriodStartDate");
					Element	lastDate	= XPath.path (calculation, "lastRegularPeriodEndDate");

					if (firstDate == null)
						firstDate = XPath.path (calculation, "effectiveDate", "unadjustedDate");

					if (lastDate == null)
						lastDate  = XPath.path (calculation, "terminationDate", "unadjustedDate");

					Element	period		= XPath.path (calculation, "calculationPeriodFrequency");
					Interval interval	= toInterval (period);

					if ((firstDate == null) || (lastDate == null) || (interval == null)) continue;

					Date		first	= toDate (firstDate);
					Date		last	= toDate (lastDate);

					if ((first == null) || (last == null)) continue;

					for (int count = 0; count < dates.getLength (); ++count) {
						Element	date		= (Element) dates.item (count);
						Date	payment 	= toDate (date);

						if (isUnadjustedCalculationPeriodDate (payment, first, last, interval)) continue;

						errorHandler.error ("305", context,
							"The floor rate schedule step date '" + payment + "' does not fall " +
							"on one of the calculated period dates between '" + first + "' and '" +
							last + "'",
							getDisplayName (), toToken (date));

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the step dates are valid
	 * for a known amount schedule.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE54 = new Rule (INTEREST_RATE_STREAM, "ird-54")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				NodeList	list	= nodeIndex.getElementsByName ("knownAmountSchedule");

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context		= (Element) list.item (index);
					NodeList dates		= XPath.paths (context, "step", "stepDate");
					Element	calculation	= XPath.path (context, "..", "..", "calculationPeriodDates");

					Element	firstDate	= XPath.path (calculation, "firstRegularPeriodStartDate");
					Element	lastDate	= XPath.path (calculation, "lastRegularPeriodEndDate");

					if (firstDate == null)
						firstDate = XPath.path (calculation, "effectiveDate", "unadjustedDate");

					if (lastDate == null)
						lastDate  = XPath.path (calculation, "terminationDate", "unadjustedDate");

					Element	period		= XPath.path (calculation, "calculationPeriodFrequency");
					Interval interval	= toInterval (period);

					if ((firstDate == null) || (lastDate == null) || (interval == null)) continue;

					Date		first	= toDate (firstDate);
					Date		last	= toDate (lastDate);

					if ((first == null) || (last == null)) continue;

					for (int count = 0; count < dates.getLength(); ++count) {
						Element		date	= (Element) dates.item (count);
						Date		payment = toDate (date);

						if (isUnadjustedCalculationPeriodDate (payment, first, last, interval)) continue;

						errorHandler.error ("305", context,
							"The known amount schedule step date '" + payment + "' does not fall " +
							"on one of the calculated period dates between '" + first + "' and '" +
							last + "'",
							getDisplayName (), toToken (date));

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the calculation period dates
	 * reference matches a calculation period dates in same interest
	 * rate stream.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE55 = new Rule (R1_0__LATER, "ird-55")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result  = true;
				NodeList	list	= nodeIndex.getElementsByName ("paymentDates");

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context		= (Element) list.item (index);
					Element	reference	= XPath.path (context, "calculationPeriodDatesReference");
					Element	definition	= XPath.path (context, "..", "calculationPeriodDates");

					if ((reference != null) && (definition != null)) {
						String		href = reference.getAttribute ("href");
						String		id	 = definition.getAttribute ("id");

						// Remove leading # from XPointer type references
						if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
							href = href.substring (1);

						if ((href != null) && (id != null) && equal (href, id)) continue;

						errorHandler.error ("305", context,
							"The calculation period dates reference does not match with dates defined " +
							"in the same interest rate stream", getDisplayName (), href);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the reset dates reference
	 * matches a reset dates definition in the same interest rate
	 * stream.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE56 = new Rule (R1_0__LATER, "ird-56")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result  = true;
				NodeList 	list	= nodeIndex.getElementsByName ("paymentDates");

				for (int index = 0; index < list.getLength (); ++index) {
					Element	context		= (Element) list.item (index);
					Element	reference	= XPath.path (context, "resetDatesReference");
					Element	definition	= XPath.path (context, "..", "resetDates");

					if ((reference != null) && (definition != null)) {
						String		href = reference.getAttribute ("href");
						String		id	 = definition.getAttribute ("id");

						// Remove leading # from XPointer type references
						if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
							href = href.substring (1);

						if ((href != null) && (id != null) && equal (href, id)) continue;

						errorHandler.error ("305", context,
							"The reset dates reference does not match with dates defined " +
							"in the same interest rate stream", getDisplayName (), href);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the calculation period is consistent
	 * with the period when it is neither 'M' or 'Y'.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE57 = new Rule (R1_0__LATER, "ird-57")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				NodeList	list	= nodeIndex.getElementsByName ("calculationPeriodFrequency");

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	convention	= XPath.path (context, "rollConvention");
					Element	period		= XPath.path (context, "period");

					if ((convention == null) || (period == null)
					    || !(equal (period, "M") || equal (period, "Y"))) continue;
					    
					if (!isWeeklyRollConvention (toToken (convention))) continue;
				
					errorHandler.error ("305", context,
						"Calculation period frequency roll convention '" + toToken (convention) +
						"' is inconsistent with the calculation period '" + toToken (period) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the calculation period is weekly when
	 * the roll convention is a weekday.
	 * <P>
	 * Applies to all FpML releases.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE58 = new Rule (R1_0__LATER, "ird-58")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				NodeList	list 	= nodeIndex.getElementsByName ("calculationPeriodFrequency");

				for (int index = 0; index < list.getLength (); ++index) {
					Element context 	= (Element) list.item (index);
					Element	convention	= XPath.path (context, "rollConvention");
					Element	period		= XPath.path (context, "period");

					if ((convention == null) || (period == null) || !equal (period, "W")) continue;
					
					if (isWeeklyRollConvention (toToken (convention))) continue;
					
					errorHandler.error ("305", context,
						"Calculation period frequency roll convention '" + toToken (convention) +
						"' is inconsistent with the calculation period '" + toToken (period) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

		/**
		 * A <CODE>Rule</CODE> that ensures the calculation period dates
		 * reference matches a calculation period dates in same interest
		 * rate stream.
		 * <P>
		 * Applies to all FpML releases.
		 * @since	TFP 1.0
		 */
		public static final Rule	RULE59 = new Rule (INTEREST_RATE_STREAM, "ird-59")
			{
				/**
				 * {@inheritDoc}
				 */
				public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
				{
					boolean		result  = true;
					NodeList	list	= nodeIndex.getElementsByName ("resetDates");

					for (int index = 0; index < list.getLength (); ++index) {
						Element	context		= (Element) list.item (index);
						Element	reference	= XPath.path (context, "calculationPeriodDatesReference");
						Element	definition	= XPath.path (context, "..", "calculationPeriodDates");

						if ((reference != null) && (definition != null)) {
							String		href = reference.getAttribute ("href");
							String		id	 = definition.getAttribute ("id");

							// Remove leading # from XPointer type references
							if ((href != null) && (href.length () > 0) && (href.charAt (0) == '#'))
								href = href.substring (1);

							if ((href != null) && (id != null) && equal (href, id)) continue;

							errorHandler.error ("305", context,
								"The calculation period dates reference does not match with dates defined " +
								"in the same interest rate stream", getDisplayName (), href);

							result = false;
						}
					}
					return (result);
				}
			};

		/**
		 * A <CODE>Rule</CODE> that ensures the calculation period is a term when
		 * the roll convention is "NONE".
		 * <P>
		 * Applies to all FpML releases.
		 * @since	TFP 1.0
		 */
		public static final Rule	RULE60 = new Rule (R1_0__LATER, "ird-60")
			{
				/**
				 * {@inheritDoc}
				 */
				public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
				{
					boolean		result	= true;
					NodeList	list 	= nodeIndex.getElementsByName ("calculationPeriodFrequency");

					for (int index = 0; index < list.getLength (); ++index) {
						Element context 	= (Element) list.item (index);
						Element	convention	= XPath.path (context, "rollConvention");
						Element	period		= XPath.path (context, "period");

						if ((convention == null) || (period == null) || !equal (period, "T")) continue;
						
						if (equal (convention, "NONE")) continue;
						
						errorHandler.error ("305", context,
							"Calculation period frequency roll convention '" + toToken (convention) +
							"' is inconsistent with the calculation period '" + toToken (period) + "'",
							getDisplayName (), null);

						result = false;
					}
					return (result);
				}
			};

		/**
		 * A <CODE>Rule</CODE> that ensures that if not steps are present
		 * the initial value is non-zero.
		 * <P>
		 * Applies to FpML 4.7 and later.
		 * @since	TFP 1.6
		 */
		public static final Rule	RULE61 = new Rule (R4_7__LATER, "ird-61")
			{
				/**
				 * {@inheritDoc}
				 */
				public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
				{
					if (nodeIndex.hasTypeInformation()) 
						return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "NonNegativeSchedule"), errorHandler));					

					return (
						validate (nodeIndex.getElementsByName ("notionalStepSchedule"), errorHandler));
				}

				private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
				{
					boolean		result = true;

					for (int index = 0; index < list.getLength (); ++index) {
						Element		context		= (Element) list.item (index);

						if (implies (
								not (exists (XPath.path (context, "step"))),
								notEqual (
									XPath.path (context, "initialValue"),
									ZERO))) continue;

						errorHandler.error ("305", context,
							"An non-zero initial value must be provided when there are no steps " +
							"in the schedule",
							getDisplayName (), null);

						result = false;
					}
					return (result);
				}
			};

	/**
	 * Determines if an element of type <CODE>InterestRateStream</CODE>
	 * contains no <CODE>cashflows</CODE> element, or
	 * <CODE>cashflows/cashflowsMatchParameters</CODE> contains <CODE>true</CODE>.
	 *
	 * @param	stream	The stream <CODE>Element</CODE>.
	 * @return	<CODE>true</CODE> if the swap is parametric.
	 * @since	TFP 1.0
	 */
	protected static boolean isParametric (Element stream)
	{
		Element 	cashflows;

		if (exists (cashflows = XPath.path (stream, "cashflows")))
			return (toBool (XPath.path (cashflows, "cashflowsMatchParameters")));

		return (true);
	}

	/**
	 * Provides access to the IRD validation rule set.
	 *
	 * @return	The IRD validation rule set.
	 * @since	TFP 1.0
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}

	/**
	 * A <CODE>BigDecimal</CODE> constant with the value zero. The code
	 * used to reference the constant in <CODE>BigDecimal</CODE> class
	 * but <B>javadoc</B> kept complaining about it.
	 * @since	TFP 1.0
	 */
	private static final BigDecimal	ZERO  = new BigDecimal (0);

	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for interest rate products.
	 * @since	TFP 1.0
	 */
	private static final RuleSet	rules = RuleSet.forName ("IrdRules");

	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.0
	 */
	private IrdRules ()
	{ }

	/**
	 * Determines if a string contains a number.
	 *
	 * @param	value			The string to be tested.
	 * @return  <CODE>true</CODE> if the string just contains digits.
	 * @since	TFP 1.0
	 */
	private static boolean isNumber (final String value)
	{
		if (value != null) {
			for (int index = 0; index < value.length (); ++index) {
				char ch = value.charAt (index);
	
				if (!((ch >= '0') && (ch <= '9'))) return (false);
			}
			return (value.length () > 0);
		}
		return (false);
	}

	/**
	 * Determines if a string value contains an abbreviated English weekday
	 * name.
	 *
	 * @param 	name			The string to be tested
	 * @return	<CODE>true</CODE> if the string matches a recognised week
	 * 			day value, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	private static boolean isWeekDayName (final String name)
	{
		if (name.equals ("MON")) return (true);
		if (name.equals ("TUE")) return (true);
		if (name.equals ("WED")) return (true);
		if (name.equals ("THU")) return (true);
		if (name.equals ("FRI")) return (true);
		if (name.equals ("SAT")) return (true);
		if (name.equals ("SUN")) return (true);

		return (false);
	}
	
	/**
	 * Determines if a string value contains a code that can be used as a
	 * weekly roll convention.
	 * 
	 * @param 	code			The code value to be tested.
	 * @return	<CODE>true</CODE> if the string matches a recognised weekly
	 * 			roll convention.
	 * @since	TFP 1.2
	 */
	private static boolean isWeeklyRollConvention (final String code)
	{
		return (isWeekDayName (code) || code.equals ("NONE") || code.equals("SFE"));
	}

	/**
	 * Tests if the payment date falls on a calculated date.
	 *
	 * @param 	paymentDate		The payment date.
	 * @param 	startDate		The calculation period start date.
	 * @param 	endDate			The calculation period end date.
	 * @param 	freq			The period frequency.
	 * @return	<CODE>true</CODE> if the payment date falls on a calculated date.
	 * @since	TFP 1.0
	 */
	private static boolean isUnadjustedCalculationPeriodDate (Date paymentDate, Date startDate, Date endDate, Interval freq)
	{
		Interval	step = new Interval (0, Period.DAY);

		for (;;) {
			Date		targetDate = startDate.plus (step);

			if (targetDate.compareTo (endDate) > 0) return (false);
			if (targetDate.equals (paymentDate)) return (true);

			step = step.plus (freq);
		}
	}
}