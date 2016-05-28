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
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XPath;

/**
 * The <CODE>ReferencesRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for ID/IDREF references.
 *
 * @author	BitWise
 * @version	$Id: ReferenceRules.java 619 2012-04-02 21:20:52Z andrew_jacobs $
 * @since	TFP 1.1
 */
public final class ReferenceRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Rule</CODE> that ensures an <CODE>AssetReference</CODE> correctly
	 * refers to an <CODE>Asset</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE01
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-1",
				"AssetReference", new String [] {
					"assetReference", "definition", "underlyingAssetReference",
					"underlyerReference" },
				"Asset", new String [] {
					"basket", "cash", "commodity", "deposit", "bond",
					"convertibleBond", "equity", "exchangeTradedFund",
					"index", "future", "fxRate", "loan", "mortgage",
					"mutualFund", "rateIndex", "simpleCreditDefautSwap",
					"simpleFra", "simpleIrSwap", "dealSummary", "facilitySummary" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>FixedRateReference</CODE> correctly
	 * refers to a <CODE>FixedRate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE02
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-2",
				"FixedRateReference", new String [] {
					"strikeReference" },
				"FixedRate", new String [] {
					"fixedRate" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>ProtectionTermsReference</CODE> correctly
	 * refers to a <CODE>ProtectionTerms</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE03
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-3",
				"ProtectionTermsReference", new String [] {
					"protectionTermsReference" },
				"ProtectionTerms", new String [] {
					"protectionTerms" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>SettlementTermsReference</CODE> correctly
	 * refers to a <CODE>SettlementTerms</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE04
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-4",
				"SettlementTermsReference", new String [] {
					"settlementTermsReference" },
				"SettlementTerms", new String [] {
					"settlementTerms" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures the <CODE>@href</CODE> attribute of a
	 * <CODE>firstPeriodStartDate</CODE> must match the @id attribute of an
	 * element of type <CODE>Party</CODE>.
	 * <P>
	 * Applies to FpML 4.1 and later.
	 * @since	TFP 1.2
	 * @deprecated
	 */
	public static final Rule	RULE05
		= new Rule (Preconditions.R4_1__LATER, "ref-5")
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
	 * A <CODE>Rule</CODE> that ensures an <CODE>InterestCalculationReference</CODE> correctly
	 * refers to an <CODE>Rate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE06
		= new ReferenceRule (Preconditions.R4_2__R4_4, "ref-6",
				"InterestCalculationReference", new String [] {
					"interestLegRate" },
				"Rate", new String [] {
				"rateCalculation", "floatingRate", "floatingRateCalculation",
				"inflationRateCalculation" });
			
	/**
	 * A <CODE>Rule</CODE> that ensures an <CODE>InterestLegCalculationPeriodDatesReference</CODE> correctly
	 * refers to an <CODE>InterestLegCalculationPeriodDates</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE07
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-7",
				"InterestLegCalculationPeriodDatesReference", new String [] {
					"calculationPeriodDatesReference" },
				"InterestLegCalculationPeriodDates", new String [] {
					"interestLegCalculationPeriodDates" });
			
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>CalculationPeriodDatesReference</CODE> correctly
	 * refers to an <CODE>CalculationPeriodDates</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE08
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-8",
				"CalculationPeriodDatesReference", new String [] {
					"calculationPeriodDatesReference" },
				"CalculationPeriodDates", new String [] {
					"calculationPeriodDates" });
			
	/**
	 * A <CODE>Rule</CODE> that ensures an <CODE>InterestRateStreamReference</CODE> correctly
	 * refers to an <CODE>InterestRateStream</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE09
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-9",
				"InterestRateStreamReference", new String [] {
					"swapStreamReference" },
				"InterestRateStream", new String [] {
					"capFloorStream", "swapStream" });
			
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PaymentCalculationPeriod</CODE> correctly
	 * refers to a <CODE>PricingStructure</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE10
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-10",
				"PaymentCalculationPeriod", new String [] {
					"paymentCalculationPeriod" },
				"PricingStructure", new String [] {
					"creditCurve", "fxCurve", "volatilityRepresentation",
					"yieldCurve" });
					
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PaymentDatesReference</CODE> correctly
	 * refers to a <CODE>PaymentDates</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE11
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-11",
				"PaymentDatesReference", new String [] {
					"paymentDatesReference" },
				"PaymentDates", new String [] {
					"paymentDates" });
	
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PaymentCalculationPeriod</CODE> correctly
	 * refers to a <CODE>PricingStructure</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE12
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-12",
				"ResetDatesReference", new String [] {
					"resetDatesReference" },
				"ResetDates", new String [] {
					"resetDates" });
	
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>CreditEventsReference</CODE> correctly
	 * refers to a <CODE>CreditEvents</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE13
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-13",
				"CreditEventsReference", new String [] {
					"creditEventsReference" },
				"CreditEvents", new String [] {
					"creditEvents" });
	
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>CreditEventsReference</CODE> correctly
	 * refers to a <CODE>CreditEvents</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE14
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-14",
				"CashflowFixingReference", new String [] {
					"calculatedRateReference" },
				"CashflowFixing", new String [] {
					"calculatedRate" });
	
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>CashflowObservationReference</CODE> correctly
	 * refers to a <CODE>CreditEvents</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE15
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-15",
				"CashflowObservationReference", new String [] {
					"observationReference" },
				"CashflowObservation", new String [] {
					"observationElements" });
	
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>StepReference</CODE> correctly
	 * refers to a <CODE>Step</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE16
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-16",
				"StepReference", new String [] {
					"fixedRateStepReference" },
				"Step", new String [] {
					"step" });
	
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>TradeUnderlyerReference</CODE> correctly
	 * refers to a <CODE>Step</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE17
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-17",
				"TradeUnderlyerReference", new String [] {
					"underlyerReference" },
				"TradeUnderlyer", new String [] {
					"underlyer" });
	
	/**
	 * A <CODE>Rule</CODE> that ensures the generic/@href attribute must match the @id
	 * attribute of an element of type Asset.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE18
		= new Rule (Preconditions.R4_0__LATER, "ref-18")
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
				
				for (int index = 0; index < list.getLength (); ++index) {
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
	 * A <CODE>Rule</CODE> that ensures a <CODE>TradeUnderlyerReference</CODE> correctly
	 * refers to a <CODE>Step</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE19
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-19",
				"MarketReference", new String [] {
					"marketReference" },
				"Market", new String [] {
					"market" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PricingDataPointCoordinateReference</CODE> correctly
	 * refers to a <CODE>PricingDataPointCoordinate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE20
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-20",
				"PricingDataPointCoordinateReference", new String [] {
					"coordinateReference" },
				"PricingDataPointCoordinate", new String [] {
					"coordinate" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PricingParameterDerivativeReference</CODE> correctly
	 * refers to a <CODE>PricingParameterDerivative</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE21
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-21",
				"PricingParameterDerivativeReference", new String [] {
					"partialDerivativeReference" },
				"PricingParameterDerivative", new String [] {
					"partialDerivative" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PricingParameterDerivativeReference</CODE> correctly
	 * refers to a <CODE>PricingParameterDerivative</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.4
	 */
	public static final Rule	RULE22
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-22",
				"Valuation", new String [] {
					"valuation", "assetValuation", "associatedValue",
					"assetQuote", "pricingStructureValuation", "creditCurveValuation",
					"defaultProbabilityCurve", "fxCurveValuation",
					"volatilityMatrixValuation", "yieldCurveValuation" },
				"ValuationScenario", new String [] {
					"valuationScenario" },
				"definitionRef");
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>ValuationReference</CODE> correctly
	 * refers to a <CODE>Valuation</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE23
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-23",
				"ValuationReference", new String [] {
					"valuationReference", "associatedValueReference",
					"inputDateReference" },
				"Valuation", new String [] {
					"valuation", "assetValuation", "associatedValue", "assetQuote",
					"pricingStructionValuation", "creditCurveValuation",
					"defaultProbabilityCurve", "fxCurveValuation",
					"volatilityMatrixValuation", "yieldCurveValuation" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>ValuationScenarioReference</CODE> correctly
	 * refers to a <CODE>ValuationScenario</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE24
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-24",
				"ValuationScenarioReference", new String [] {
					"valuationScenarioReference", "baseValuationScenario" },
				"ValuationScenario", new String [] {
					"valuationScenario" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures an <CODE>AccountReference</CODE> correctly
	 * refers to an <CODE>Account</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE25
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-25",
				"AccountReference", new String [] {
					"accountReference", "account" },
				"Account", new String [] {
					"account" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>BusinessCentersReference</CODE> correctly
	 * refers to a <CODE>BusinessCenters</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE26
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-26",
				"BusinessCentersReference", new String [] {
					"businessCentersReference" },
				"BusinessCenters", new String [] {
					"businessCenters" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>BusinessDayAdjustmentsReference</CODE> correctly
	 * refers to a <CODE>BusinessDayAdjustments</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE27
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-27",
				"BusinessDayAdjustmentsReference", new String [] {
					"dateAdjustmentsReference" },
				"BusinessDayAdjustments", new String [] {
					"dateAdjustments", "calculationPeriodDatesAdjustments",
					"paymentDatesAdjustments", "resetDatesAdjustments",
					"relativeDateAdjustments" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>LegalEntityReference</CODE> correctly
	 * refers to a <CODE>LegalEntity</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE28
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-28",
				"LegalEntityReference", new String [] {
					"primaryObligorReference", "guarantorReference",
					"borrowerReference", "insurerReference",
					"creditEntityReference"},
				"LegalEntity", new String [] {
					"referenceEntity", "excludedReferenceEntity",
					"primaryObligor", "guarantor", "borrower",
					"insurer" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PartyReference</CODE> correctly
	 * refers to a <CODE>Party</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE29
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-29",
				"PartyReference", new String [] {
					"intermediaryPartyReference", "depositoryPartyReference",
					"notifyingPartyReference", "notifiedPartyReference",
					"transferor", "transferee", "remainingParty", "otherRemainingParty",
					"definingParty", "matchingParty", "baseParty",
					"activityProvider",	"positionProvider", "valuationProvider",
					"partyReference", "party", "buyerPartyReference", "sellerPartyReference",
					"payerPartyReference", "receiverPartyReference", "issuerPartyReference",
					"accountBeneficiary", "beneficiaryPartyReference",
					"calculationAgentPartyReference", "correspondentPartyReference",
					"extraOrdinaryDividends", "exerciseNoticePartyReference",
					"issuingBankPartyReference", "borrowerPartyReference",
					"agentBankPartyReference" },
				"Party", new String [] {
					"party" });

	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>Payment</CODE> correctly
	 * refers to a <CODE>PricingStructure</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE30
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-30",
				"Payment", new String [] {
					"payment", "otherPartyPayment", "premium",
					"additionalPayment", "exchangedCurrency1",
					"exchangedCurrency2" },
				"PricingStructure", new String [] {
					"creditCurve", "fxCurve", "volatilityRepresentation",
					"yieldCurve" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PricingStructureReference</CODE> correctly
	 * refers to a <CODE>PricingStructure</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE31
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-31",
				"PricingStructureReference", new String [] {
					"baseYieldCurve", "settlementCurrencyYieldCurve",
					"forecastCurrencyYieldCurve", "originalInputReference",
					"replacementInputReference", "pricingInputReference",
					"partialDerivativeReference", "replacementMarketInput" },
				"PricingStructure", new String [] {
					"creditCurve", "fxCurve", "volatilityRepresentation",
					"yieldCurve" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>PricingStructureReference</CODE> correctly
	 * refers to a <CODE>PricingStructure</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE32
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-32",
				"ProductReference", new String [] {
					"premiumProductReference", "definition" },
				"Product", new String [] {
					"swap", "fra", "capFloor", "swaption", "strategy",
					"commoditySwap", "bulletPayment", "creditDefaultSwap",
					"dividendSwapTransactionSupplement", "equityForward",
					"equityOption", "equityOptionTransactionSupplement",
					"equitySwapTransactionSupplement", "fxAverageRateOption",
					"fxBarrierOption", "fxDigitalOption", "fxSwap",
					"fxSingleLeg", "equitySwap", "returnSwap",
					"termDeposit", "varianceSwap" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>RateReference</CODE> correctly
	 * refers to a <CODE>Rate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE33
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-33",
				"RateReference", new String [] {
					"rateReference" },
				"Rate", new String [] {
					"rateCalculation", "floatingRate", "floatingRateCalculation",
					"inflationRateCalculation" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>ScheduleReference</CODE> correctly
	 * refers to a <CODE>Schedule</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE34
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-34",
				"ScheduleReference", new String [] {
					"notionalReference", "constantNotionalScheduleReference" },
				"Schedule", new String [] {
					"fixedRate", "feeRateSchedule", "floatingRateMultiplierSchedule",
					"fixedRateSchedule", "knownAmountSchedule", "notionalStepSchedule",
					"feeAmountSchedule", "spreadSchedule", "capRateSchedule",
					"floorRateSchedule" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>SpreadScheduleReference</CODE> correctly
	 * refers to a <CODE>SpreadSchedule</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE35
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-35",
				"SpreadScheduleReference", new String [] {
					"underlyerSpread" },
				"SpreadSchedule", new String [] {
					"spreadSchedule" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures a <CODE>SensitivitySetDefinitionReference</CODE>
	 * correctly refers to a <CODE>SensitivitySetDefinition</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.2
	 */
	public static final Rule	RULE36
		= new ReferenceRule (Preconditions.R4_0__LATER, "ref-36",
				"SensitivitySetDefinitionReference", new String [] {
					"definitionReference" },
				"SensitivitySetDefinition", new String [] {
					"sensitivitySetDefinition" });
		
	/**
	 * A <CODE>Rule</CODE> that ensures the cash settlement date for a
	 * mandatory early termination matches the termination date.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE38
		= new Rule (Preconditions.R4_0__LATER, "ref-38")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "MandatoryEarlyTermination"), errorHandler));
				return (validate (nodeIndex.getElementsByName ("mandatoryEarlyTermination"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result	= true;
				
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context	= (Element) list.item (index);
					Element		dateRef  = XPath.path (context, "cashSettlement", "cashSettlementValuationDate", "dateRelativeTo");
					Element		termDate = XPath.path (context, "mandatoryEarlyTerminationDate");
					
					if ((dateRef == null) || (termDate == null) ||
							DOM.getAttribute (dateRef, "href").equals (DOM.getAttribute (termDate, "id"))) continue;
					
					errorHandler.error ("305", context,
							"The cash settlement valuation date should reference the" +
							"mandatory termination date", 
							getDisplayName (), DOM.getAttribute (dateRef, "href"));
					result = false;
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
	 * validation rules for intra-document references.
	 * @since	TFP 1.1
	 */
	private static final RuleSet	rules = RuleSet.forName ("ReferenceRules");
	
	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.1
	 */
	private ReferenceRules ()
	{ }
}