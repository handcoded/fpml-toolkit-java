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

import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XPath;

import java.math.BigDecimal;
import java.util.*;

/**
 * The <CODE>LoanRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for syndicated loan messages.
 * <P>
 * @author 	Marc Gratacos
 * @author 	Andrew Jacobs
 * @since	TFP 1.2
 */
public final class LoanRules extends FpMLRuleSet 
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-4 and later
	 * confirmation view documents.
	 * @since	TFP 1.7
	 */
	/*private static final Precondition	R4_4__R4_X
		= Precondition.and (
				Preconditions.R4_4__R4_X,
				Preconditions.CONFIRMATION);*/
				
	private static final Precondition R5_0__R5_10 = new VersionRangePrecondition("5-0","5-10");

    private static final Precondition R4_4__R5_10 = Precondition.or(
            Preconditions.R4_4__R4_X,
            R5_0__R5_10
    );

    private static final Precondition R4_4__R5_10_CONFIRMATION = Precondition.and(
            R4_4__R5_10,
            Preconditions.CONFIRMATION
    );
				
	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 5-11 or later
	 * confirmation view document.
	 * @since	TFP 1.7
	 */
	public static final Precondition	R5_11__LATER_CONFIRMATION
			= Precondition.and (
			Preconditions.R5_11__LATER,
			Preconditions.CONFIRMATION);
			
	private static final Precondition LOAN_SERVICING_NOTIFICATION = new ContentPrecondition(
            new String[]{"loanServicingNotification"},
            new String[]{"LoanServicingNotification"}
    );

    private static final Precondition LOAN_TRADE_NOTIFICATION = new ContentPrecondition(
            new String[]{"loanTradeNotification"},
            new String[]{"LoanTradeNotification"}
    );

    private static final Precondition LOAN_ALLOCATION_NOTIFICATION = new ContentPrecondition(
            new String[]{"loanAllocationNotification"},
            new String[]{"LoanAllocationNotification"}
    );
	
	/**
     * A <CODE>Rule</CODE> that ensures that the sum of all accrual amount must equal the total loan
     * interest payment amount, assuming the same currency.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE01 = new Rule(R5_11__LATER_CONFIRMATION, "ln-1") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanInterestPayment"), errorHandler);
            return validate(nodeIndex.getElementsByName("interestPayment"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            Double interestSum = 0.0;
            Double accrualSum = 0.0;

            for (int index = 0, length = nodes.getLength (); index < length; ++index) {
                Element context = (Element) nodes.item(index);
                Element amount = XPath.path(context, "amount", "amount");
                Element currency = XPath.path(context, "amount", "currency");

                if ((amount == null) || (currency == null)) continue;

                interestSum += toDouble(amount);

                NodeList accrList = XPath.paths(context, "accrualSchedule");
                if ((accrList == null) || (accrList.getLength() == 0)) continue;

                for (int indexAccr = 0; indexAccr < accrList.getLength(); ++indexAccr) {
                    Element accrContext = (Element) accrList.item(indexAccr);
                    Element accrAmount = XPath.path(accrContext, "accrualAmount", "amount");
                    Element accrCurrency = XPath.path(accrContext, "accrualAmount", "currency");

                    if ((accrAmount == null) || (accrCurrency == null)) continue;
                    if (notEqual(currency, accrCurrency)) continue;

                    accrualSum += toDouble(accrAmount);
                }

                if (Double.compare(interestSum, accrualSum) != 0) {
                    errorHandler.error("305", context,
                            "The total loan interest payment amount must equal the total accrual amounts",
                            getDisplayName(), String.format("Interest amount of %s not equal to accrual amount of %s", interestSum, accrualSum));
                    result = false;
                }

            }

            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the sum of all accrual amount must equal the total letter
     * of credit issuance fee payment amount, assuming the same currency.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE02 = new Rule(R5_11__LATER_CONFIRMATION, "ln-2") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LcIssuanceFeePayment"), errorHandler);
            return validate(nodeIndex.getElementsByName("lcIssuanceFeePayment"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            Double interestSum = 0.0;
            Double accrualSum = 0.0;

            for (int index = 0, length = nodes.getLength (); index < length; ++index) {
                Element context = (Element) nodes.item(index);
                Element amount = XPath.path(context, "amount", "amount");
                Element currency = XPath.path(context, "amount", "currency");

                if ((amount == null) || (currency == null)) continue;

                interestSum += toDouble(amount);

                NodeList accrList = XPath.paths(context, "accrualSchedule");
                if ((accrList == null) || (accrList.getLength() == 0)) continue;

                for (int indexAccr = 0; indexAccr < accrList.getLength(); ++indexAccr) {
                    Element accrContext = (Element) accrList.item(indexAccr);
                    Element accrAmount = XPath.path(accrContext, "accrualAmount", "amount");
                    Element accrCurrency = XPath.path(accrContext, "accrualAmount", "currency");

                    if ((accrAmount == null) || (accrCurrency == null)) continue;
                    if (notEqual(currency, accrCurrency)) continue;

                    accrualSum += toDouble(accrAmount);
                }

                if (Double.compare(interestSum, accrualSum) != 0) {
                    errorHandler.error("305", context,
                            "The total letter of credit issuance fee payment amount must equal the total accrual amounts",
                            getDisplayName(), String.format("Fee amount of %s not equal to accrual amount of %s", interestSum, accrualSum));
                    result = false;
                }

            }

            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the sum of all accrual amount must equal the total
     * accruing fee payment amount, assuming the same currency.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE03 = new Rule(R5_11__LATER_CONFIRMATION, "ln-3") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "AccruingFeePayment"), errorHandler);
            return validate(nodeIndex.getElementsByName("accruingFeePayment"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            Double interestSum = 0.0;
            Double accrualSum = 0.0;

            for (int index = 0, length = nodes.getLength (); index < length; ++index) {
                Element context = (Element) nodes.item(index);
                Element amount = XPath.path(context, "amount", "amount");
                Element currency = XPath.path(context, "amount", "currency");

                if ((amount == null) || (currency == null)) continue;

                interestSum += toDouble(amount);

                NodeList accrList = XPath.paths(context, "accrualSchedule");
                if ((accrList == null) || (accrList.getLength() == 0)) continue;

                for (int indexAccr = 0; indexAccr < accrList.getLength(); ++indexAccr) {
                    Element accrContext = (Element) accrList.item(indexAccr);
                    Element accrAmount = XPath.path(accrContext, "accrualAmount", "amount");
                    Element accrCurrency = XPath.path(accrContext, "accrualAmount", "currency");

                    if ((accrAmount == null) || (accrCurrency == null)) continue;
                    if (notEqual(currency, accrCurrency)) continue;

                    accrualSum += toDouble(accrAmount);
                }

                if (Double.compare(interestSum, accrualSum) != 0) {
                    errorHandler.error("305", context,
                            "The total accruing fee payment amount must equal the total accrual amounts",
                            getDisplayName(), String.format("Fee amount of %s not equal to accrual amount of %s", interestSum, accrualSum));
                    result = false;
                }

            }

            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the sum of all accrual amount must equal the total
     * accruing PIK payment amount, assuming the same currency.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE04 = new Rule(R5_11__LATER_CONFIRMATION, "ln-4") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "AccruingPikPayment"), errorHandler);
            return validate(nodeIndex.getElementsByName("accruingPikPayment"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            Double interestSum = 0.0;
            Double accrualSum = 0.0;

            for (int index = 0, length = nodes.getLength (); index < length; ++index) {
                Element context = (Element) nodes.item(index);
                Element amount = XPath.path(context, "amount", "amount");
                Element currency = XPath.path(context, "amount", "currency");

                if ((amount == null) || (currency == null)) continue;

                interestSum += toDouble(amount);

                NodeList accrList = XPath.paths(context, "accrualSchedule");
                if ((accrList == null) || (accrList.getLength() == 0)) continue;

                for (int indexAccr = 0; indexAccr < accrList.getLength(); ++indexAccr) {
                    Element accrContext = (Element) accrList.item(indexAccr);
                    Element accrAmount = XPath.path(accrContext, "accrualAmount", "amount");
                    Element accrCurrency = XPath.path(accrContext, "accrualAmount", "currency");

                    if ((accrAmount == null) || (accrCurrency == null)) continue;
                    if (notEqual(currency, accrCurrency)) continue;

                    accrualSum += toDouble(accrAmount);
                }

                if (Double.compare(interestSum, accrualSum) != 0) {
                    errorHandler.error("305", context,
                            "The total accruing PIK payment amount must equal the total accrual amounts",
                            getDisplayName(), String.format("PIK amount of %s not equal to accrual amount of %s", interestSum, accrualSum));
                    result = false;
                }

            }

            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the start date of the facilities of a deal is the same or after the credit agreement
     * date of the deal.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE05 = new Rule(R5_11__LATER_CONFIRMATION, "ln-5") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "Deal"), errorHandler);
            return validate(nodeIndex.getElementsByName("deal"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {
                Element context = (Element) nodes.item(0);
                Element creditAgreementDate = XPath.path(context, "creditAgreementDate");
                NodeList startDates = XPath.paths(context, "*", "startDate");

                for (int index = 0; index < startDates.getLength(); ++index) {

                    Element startDate = (Element) startDates.item(index);

                    if ((startDate == null) || (creditAgreementDate == null) || greaterOrEqual(toDate(startDate), toDate(creditAgreementDate)))
                        continue;

                    errorHandler.error("305", startDate,
                            "The start date of a facility must be equal or after the credit agreement date of a deal",
                            getDisplayName(), String.format("%s start date of a facility not the same or after the credit agreement date of the deal %s", toToken(startDate), toToken(creditAgreementDate)));
                    result = false;
                }
            }
            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the start date of the facilities of an accrual calculation is the same or after the effective
     * date of its contract.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE06 = new Rule(R5_11__LATER_CONFIRMATION, "ln-6") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanContract"), errorHandler);
            return (
                    validate(nodeIndex.getElementsByName("contract"), errorHandler)
                            & validate(nodeIndex.getElementsByName("loanContract"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            for (int index = 0, length = nodes.getLength (); index < length; ++index) {

                Element context = (Element) nodes.item(index);
                Element effectiveDate = XPath.path(context, "effectiveDate");
                NodeList startDates = XPath.paths(context, "*", "startDate");

                if ((startDates == null) || (startDates.getLength() == 0)) continue;

                Element startDate = (Element) startDates.item(0);

                if ((startDate == null) || (effectiveDate == null) || greaterOrEqual(toDate(startDate), toDate(effectiveDate)))
                    continue;

                errorHandler.error("305", startDate,
                        "The start date of an accrual calculation must be equal or after the effective date of the contract",
                        getDisplayName(), String.format("%s start date of an accrual calculation not the same or after the effective date of the contract %s", toToken(startDate), toToken(effectiveDate)));
                result = false;
            }
            return result;
        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that the rate fixing date of a PRIME base rate loan contract is equal to the
     * effective date of that contract.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE07 = new Rule(R5_11__LATER_CONFIRMATION, "ln-7") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanContract"), errorHandler);
            return (
                    validate(nodeIndex.getElementsByName("contract"), errorHandler)
                            & validate(nodeIndex.getElementsByName("loanContract"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            for (int index = 0, length = nodes.getLength (); index < length; ++index) {

                Element context = (Element) nodes.item(index);
                Element effectiveDate = XPath.path(context, "effectiveDate");
                Element rateFixingDate = XPath.path(context, "floatingRateAccrual", "rateFixingDate");
                Element floatingRateIndex = XPath.path(context, "floatingRateAccrual", "floatingRateIndex");

                if ((rateFixingDate == null) || (effectiveDate == null)) continue;

                if (DOM.getInnerText(floatingRateIndex).toUpperCase().contains("PRIME")) {

                    if (notEqual(toDate(rateFixingDate), toDate(effectiveDate))) {

                        errorHandler.error("305", rateFixingDate,
                                "The rate fixing date of a PRIME base rate loan contract must be equal to the effective date of the contract",
                                getDisplayName(), String.format("%s rate fixing date of a PRIME base rate loan contract must be the same that the effective date of the contract %s", toToken(rateFixingDate), toToken(effectiveDate)));
                        result = false;

                    }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that for a floating rate loan accrual period, the end date must be greater than
     * or equal to the start date, which must be greater than or equal to the rate fixing date.  In other words, it is
     * not possible to have a period end date that is earlier than the start date.  And it is impossible to have an
     * accrual period start date that is earlier than its rate fixing date.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE08 = new Rule(R5_11__LATER_CONFIRMATION, "ln-8") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "FloatingRateAccrual"), errorHandler);
            return validate(nodeIndex.getElementsByName("floatingRateAccrual"), errorHandler);
        }

        private boolean validate(NodeList list, ValidationErrorHandler errorHandler) {
            boolean result = true;

            for (int index = 0, length = list.getLength (); index < length; ++index) {
                Element context = (Element) list.item(index);
                Element end = XPath.path(context, "endDate");
                Element start = XPath.path(context, "startDate");
                Element fixingDate = XPath.path(context, "rateFixingDate");

                if ((start != null) && (fixingDate != null) && (less(toDate(start), toDate(fixingDate)))) {
                    errorHandler.error("305", context,
                            "The rate fixing date must not be after the start date",
                            getDisplayName(), String.format("%s rate fixing date of an accrual calculation must not be after its start date %s", toToken(fixingDate), toToken(start)));
                    result = false;
                }
                if ((end != null) && (start != null) && (less(toDate(end), toDate(start)))) {

                    errorHandler.error("305", context,
                            "The start date must not be after the end date",
                            getDisplayName(), String.format("%s start date of an accrual calculation must not be after its end date %s", toToken(start), toToken(end)));
                    result = false;
                }
                if ((end != null) && (fixingDate != null) && (less(toDate(end), toDate(fixingDate)))) {

                    errorHandler.error("305", context,
                            "The rate fixing date must not be after the end date",
                            getDisplayName(), String.format("%s rate fixing date of an accrual calculation must not be after its end date %s", toToken(fixingDate), toToken(end)));
                    result = false;
                }
            }
            return (result);
        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that for a floating rate loan accrual, the all-in rate (which is the actual
     * percentage rate charged to the borrower) must be equal to the sum of the base rate, the spread and, as
     * applicable, the mandatory cost rate, the default spread, and the penalty spread.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE09 = new Rule(R5_11__LATER_CONFIRMATION, "ln-9") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "FloatingRateAccrual"), errorHandler);
            return validate(nodeIndex.getElementsByName("floatingRateAccrual"), errorHandler);
        }

        private boolean validate(NodeList list, ValidationErrorHandler errorHandler) {
            boolean result = true;

            for (int index = 0, length = list.getLength (); index < length; ++index) {
                Element context = (Element) list.item(index);
                Element costRate = XPath.path(context, "mandatoryCostRate");
                Element baseRate = XPath.path(context, "baseRate");
                Element spread = XPath.path(context, "spread");
                Element defaultSpread = XPath.path(context, "defaultSpread");
                Element penaltySpread = XPath.path(context, "penaltySpread");
                Element allInRate = XPath.path(context, "allInRate");

                if ((allInRate != null) && (spread != null)) {
                    BigDecimal allInRateValue = toDecimal(allInRate);
                    BigDecimal costRateValue = toDecimal(costRate);
                    BigDecimal spreadValue = toDecimal(spread);
                    BigDecimal baseRateValue = toDecimal(baseRate);
                    BigDecimal defaultSpreadValue = toDecimal(defaultSpread);
                    BigDecimal penaltySpreadValue = toDecimal(penaltySpread);
                    BigDecimal spreadsValue;
                    BigDecimal spreadsPlusBase;
                    BigDecimal total;
                    if (penaltySpread == null) {
                        if (defaultSpread == null) {
                            spreadsValue = spreadValue;
                        } else {
                            spreadsValue = spreadValue.add(defaultSpreadValue);
                        }

                    } else {
                        if (defaultSpread == null) {
                            spreadsValue = spreadValue.add(penaltySpreadValue);
                        } else {
                            spreadsValue = spreadValue.add(penaltySpreadValue.add(defaultSpreadValue));
                        }

                    }
                    if (baseRateValue == null) {
                        spreadsPlusBase = spreadsValue;
                    } else {
                        spreadsPlusBase = baseRateValue.add(spreadsValue);
                    }
                    if (costRateValue == null) {
                        total = spreadsPlusBase;
                    } else {
                        total = spreadsPlusBase.add(costRateValue);
                    }

                    if (allInRateValue.compareTo(total) != 0) {
                        errorHandler.error("305", context,
                                "The all-in rate must be equal to the sum of the base rate, the spread and, as applicable, the mandatory cost rate, the default spread, and the penalty spread.",
                                getDisplayName(), String.format("The all-in rate %s must be equal to the sum of the base rate (%s), the spread (%s) and, as applicable, the mandatory cost rate (%s), the default spread (%s), and the penalty spread (%s).", allInRateValue, baseRateValue, spreadValue, costRateValue, defaultSpreadValue, penaltySpreadValue));
                        result = false;
                    }
                }
            }
            return (result);
        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that within the MoneyWithParticipantShare complex type, wherever used, if the
     * shareAmount is included it should never exceed the amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE10 = new Rule(R5_11__LATER_CONFIRMATION, "ln-10") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "MoneyWithParticipantShare"), errorHandler);

            class Contexts {
                private String firstElement;
                private String secondElement;

                public Contexts(String firstElement, String secondElement) {
                    this.firstElement = firstElement;
                    this.secondElement = secondElement;
                }

                public String getFirstElement() {
                    return firstElement;
                }

                public void setFirstElement(String firstElement) {
                    this.firstElement = firstElement;
                }

                public String getSecondElement() {
                    return secondElement;
                }

                public void setSecondElement(String secondElement) {
                    this.secondElement = secondElement;
                }
            }

            final List<String> firstContextList = new ArrayList<>(Arrays.asList(
                    "accrualAmount",
                    "amount",
                    "currentDealAmount",
                    "minLcIssuanceFeeAmount",
                    "originalCommitment",
                    "priorAmount",
                    "projectedAmount",
                    "referenceAmount",
                    "remaining",
                    "totalCommitmentAmount",
                    "unavailableToUtilizeAmount",
                    "requiredFundedAmount",
                    "requiredUnfundedAmount"
            ));
            final List<Contexts> secondContextList = new ArrayList<>(Arrays.asList(
                    new Contexts("facilityCommitment","unfundedAmount"),
                    new Contexts("facilityCommitment","fundedAmount"),
                    new Contexts("commitment","unfundedAmount"),
                    new Contexts("commitment","fundedAmount"),
                    new Contexts("priorCommitment","unfundedAmount"),
                    new Contexts("priorCommitment","fundedAmount")
            ));
            Element documentElement = nodeIndex.getDocument().getDocumentElement();

            return firstContextList.stream().map(x -> validate(nodeIndex.getElementsByName(x),errorHandler)).allMatch(x-> x) &&
                    secondContextList.stream().map(x -> validate(XPath.paths(documentElement,x.getFirstElement(),x.getSecondElement()),errorHandler)).allMatch(x -> x);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            for (int index = 0, length = nodes.getLength (); index < length; ++index) {

                Element context = (Element) nodes.item(index);
                Element amount = XPath.path(context, "amount");
                Element shareAmount = XPath.path(context, "shareAmount");

                if ((amount != null) && (shareAmount != null) && (less(toDecimal(amount), toDecimal(shareAmount)))) {

                    errorHandler.error("305", context,
                            "Within the MoneyWithParticipantShare complex type, wherever used, if the shareAmount is included it should never exceed the amount.",
                            getDisplayName(), String.format("%s share amount must be equal or lower than amount %s", toToken(shareAmount), toToken(amount)));
                    result = false;
                }

            }

            return result;

        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the loan contract amount (if in the same currency as the facility) may not
     * exceed the facility commitment amount under which the loan contract is housed.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE11 = new Rule(R5_11__LATER_CONFIRMATION, "ln-11") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            Double contractSum = 0.0;
            Double facilitySum = 0.0;

            if (nodes != null && nodes.getLength() > 0) {
                Element context = (Element) nodes.item(0);
                Element facilitySummary = XPath.path(context, "facilitySummary");
                NodeList contractSummaryList = XPath.paths(context, "contractSummary");

                if ((facilitySummary != null) && (contractSummaryList != null) && (contractSummaryList.getLength() != 0)) {

                    Element facilityAmount = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "amount");
                    Element facilityCurrency = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "currency");
                    facilitySum += toDouble(facilityAmount);

                    if ((facilityAmount != null) && (facilityCurrency != null)) {

                        for (int index = 0; index < contractSummaryList.getLength(); ++index) {

                            Element contractSummary = (Element) contractSummaryList.item(index);
                            Element contractAmount = XPath.path(contractSummary, "amount", "amount");
                            Element contractCurrency = XPath.path(contractSummary, "amount", "currency");

                            if ((contractAmount != null) && (contractCurrency != null) && equal(facilityCurrency, contractCurrency)) {
                                contractSum += toDouble(contractAmount);
                            }

                        }

                        if (facilitySum < contractSum) {
                            errorHandler.error("305", context,
                                    "The loan contract summary amount (if in the same currency as the facility) may not exceed the facility commitment amount under which the loan contract is housed.",
                                    getDisplayName(), String.format("Loan contract summary amount of %s must not exceed facility commitment amount %s", contractSum, facilitySum));
                            result = false;
                        }

                    }

                }
            }

            return result;

        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the loan contract amount (if in the same currency as the facility) may not
     * exceed the facility commitment amount under which the loan contract is housed.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE11b = new Rule(R5_11__LATER_CONFIRMATION, "ln-11b") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            Double contractSum = 0.0;
            Double facilitySum = 0.0;

            if (nodes != null && nodes.getLength() > 0) {
                Element context = (Element) nodes.item(0);
                Element facilitySummary = XPath.path(context, "facilitySummary");
                NodeList contractList = XPath.paths(context, "contract");

                if ((facilitySummary != null) && (contractList != null) && (contractList.getLength() != 0)) {

                    Element facilityAmount = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "amount");
                    Element facilityCurrency = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "currency");
                    facilitySum += toDouble(facilityAmount);

                    if ((facilityAmount != null) && (facilityCurrency != null)) {

                        for (int index = 0; index < contractList.getLength(); ++index) {

                            Element contract = (Element) contractList.item(index);
                            Element contractAmount = XPath.path(contract, "amount", "amount");
                            Element contractCurrency = XPath.path(contract, "amount", "currency");

                            if ((contractAmount != null) && (contractCurrency != null) && equal(facilityCurrency, contractCurrency)) {
                                contractSum += toDouble(contractAmount);
                            }

                        }

                        if (facilitySum < contractSum) {
                            errorHandler.error("305", context,
                                    "The loan contract amount (if in the same currency as the facility) may not exceed the facility commitment amount under which the loan contract is housed.",
                                    getDisplayName(), String.format("Loan contracts amount of %s must not exceed facility commitment amount %s", contractSum, facilitySum));
                            result = false;
                        }

                    }

                }
            }

            return result;

        }
    };


    /**
     * A <CODE>Rule</CODE> that ensures that the letter of credit amount (if in the same currency as the facility) may
     * not exceed the facility commitment amount under which the letter of credit is housed.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE12 = new Rule(R5_11__LATER_CONFIRMATION, "ln-12") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            Double letterOfCreditSum = 0.0;
            Double facilitySum = 0.0;

            if (nodes != null && nodes.getLength() > 0) {
                Element context = (Element) nodes.item(0);
                Element facilitySummary = XPath.path(context, "facilitySummary");
                NodeList letterOfCreditSummaryList = XPath.paths(context, "letterOfCreditSummary");

                if ((facilitySummary != null) && (letterOfCreditSummaryList != null) && (letterOfCreditSummaryList.getLength() != 0)) {

                    Element facilityAmount = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "amount");
                    Element facilityCurrency = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "currency");
                    facilitySum += toDouble(facilityAmount);

                    if ((facilityAmount != null) && (facilityCurrency != null)) {

                        for (int index = 0; index < letterOfCreditSummaryList.getLength(); ++index) {

                            Element letterOfCreditSummary = (Element) letterOfCreditSummaryList.item(index);
                            Element letterOfCreditAmount = XPath.path(letterOfCreditSummary, "amount", "amount");
                            Element letterOfCreditCurrency = XPath.path(letterOfCreditSummary, "amount", "currency");

                            if ((letterOfCreditAmount != null) && (letterOfCreditCurrency != null) && equal(facilityCurrency, letterOfCreditCurrency)) {
                                letterOfCreditSum += toDouble(letterOfCreditAmount);
                            }

                        }

                        if (facilitySum < letterOfCreditSum) {
                            errorHandler.error("305", context,
                                    "The letter of credit summary amount (if in the same currency as the facility) may not exceed the facility commitment amount under which the letter of credit is housed.",
                                    getDisplayName(), String.format("Letter of credit summary amount of %s must not exceed facility commitment amount %s", letterOfCreditSum, facilitySum));
                            result = false;
                        }

                    }

                }
            }

            return result;

        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the letter of credit amount (if in the same currency as the facility) may
     * not exceed the facility commitment amount under which the letter of credit is housed.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE12b = new Rule(R5_11__LATER_CONFIRMATION, "ln-12b") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            Double letterOfCreditSum = 0.0;
            Double facilitySum = 0.0;

            if (nodes != null && nodes.getLength() > 0) {
                Element context = (Element) nodes.item(0);
                Element facilitySummary = XPath.path(context, "facilitySummary");
                NodeList letterOfCreditList = XPath.paths(context, "letterOfCredit");

                if ((facilitySummary != null) && (letterOfCreditList != null) && (letterOfCreditList.getLength() != 0)) {

                    Element facilityAmount = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "amount");
                    Element facilityCurrency = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "currency");
                    facilitySum += toDouble(facilityAmount);

                    if ((facilityAmount != null) && (facilityCurrency != null)) {

                        for (int index = 0; index < letterOfCreditList.getLength(); ++index) {

                            Element letterOfCredit = (Element) letterOfCreditList.item(index);
                            Element letterOfCreditAmount = XPath.path(letterOfCredit, "amount", "amount");
                            Element letterOfCreditCurrency = XPath.path(letterOfCredit, "amount", "currency");

                            if ((letterOfCreditAmount != null) && (letterOfCreditCurrency != null) && equal(facilityCurrency, letterOfCreditCurrency)) {
                                letterOfCreditSum += toDouble(letterOfCreditAmount);
                            }

                        }

                        if (facilitySum < letterOfCreditSum) {
                            errorHandler.error("305", context,
                                    "The letter of credit amount (if in the same currency as the facility) may not exceed the facility commitment amount under which the letter of credit is housed.",
                                    getDisplayName(), String.format("Letter of credits amount of %s must not exceed facility commitment amount %s", letterOfCreditSum, facilitySum));
                            result = false;
                        }

                    }

                }
            }

            return result;

        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the event payment date of a loan bulk servicing notification must be
     * the same or later than the start date of the corresponding facility, loan contract, and/or letter of credit
     * (as applicable).
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE13 = new Rule(R5_11__LATER_CONFIRMATION, "ln-13") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;
            for (int index = 0, length = nodes.getLength (); index < length; ++index) {
                Element context = (Element) nodes.item(index);

                NodeList paymentDateNodeList = XPath.paths(context, "eventPayment", "paymentDate");
                NodeList facilitySummaryNodeList = XPath.paths(context, "facilitySummary");
                NodeList contractNodeList = XPath.paths(context, "contract");
                NodeList contractSummaryNodeList = XPath.paths(context, "contractSummary");
                NodeList letterOfCreditNodeList = XPath.paths(context, "letterOfCredit");
                NodeList letterOfCreditSummaryNodeList = XPath.paths(context, "letterOfCreditSummary");

                if (paymentDateNodeList != null && (facilitySummaryNodeList != null || contractNodeList != null || contractSummaryNodeList != null || letterOfCreditNodeList != null || letterOfCreditSummaryNodeList != null)) {

                    for (int index1 = 0, length1 = paymentDateNodeList.getLength (); index1 < length1; ++index1) {

                        Element paymentDate = (Element) paymentDateNodeList.item(index1);
                        Element paymentDate_adjustedOrUnadjustedDate = null;
                        Element adjustedDate = XPath.path(paymentDate, "adjustedDate");
                        if (adjustedDate != null) paymentDate_adjustedOrUnadjustedDate = adjustedDate;
                        else {
                            Element unadjusted = XPath.path(paymentDate, "unadjustedDate");
                            if (unadjusted != null) paymentDate_adjustedOrUnadjustedDate = unadjusted;
                        }

                        for (int index2 = 0, length2 = facilitySummaryNodeList.getLength (); index2 < length2; ++index2) {

                            Element facilitySummary = (Element) facilitySummaryNodeList.item(index2);
                            Element facilitySummary_startDate = XPath.path(facilitySummary, "startDate");

                            if (facilitySummary_startDate != null) {
                                if (!less(toDate(paymentDate_adjustedOrUnadjustedDate), toDate(facilitySummary_startDate))) continue;
                                errorHandler.error("305", paymentDate_adjustedOrUnadjustedDate,
                                        "The event payment date of a loan contract (or contracts) event must be the same or later than the effective date of that loan contract (or contracts).",
                                        getDisplayName(), String.format("%s event payment date must not be before the facility start date %s", toToken(paymentDate_adjustedOrUnadjustedDate), toToken(facilitySummary_startDate)));
                                result = false;
                            }

                        }

                        for (int index2 = 0, length2 = contractNodeList.getLength (); index2 < length2; ++index2) {

                            Element contract = (Element) contractNodeList.item(index2);
                            Element contract_effectiveDate = XPath.path(contract, "effectiveDate");

                            if (contract_effectiveDate != null) {
                                if (!less(toDate(paymentDate_adjustedOrUnadjustedDate), toDate(contract_effectiveDate))) continue;
                                errorHandler.error("305", paymentDate_adjustedOrUnadjustedDate,
                                        "The event payment date of a loan contract (or contracts) event must be the same or later than the effective date of that loan contract (or contracts).",
                                        getDisplayName(), String.format("%s event payment date must not be before the contract effective date %s", toToken(paymentDate_adjustedOrUnadjustedDate), toToken(contract_effectiveDate)));
                                result = false;
                            }

                        }

                        for (int index2 = 0, length2 = contractSummaryNodeList.getLength (); index2 < length2; ++index2) {

                            Element contractSummary = (Element) contractSummaryNodeList.item(index2);
                            Element contractSummary_effectiveDate = XPath.path(contractSummary, "effectiveDate");

                            if (contractSummary_effectiveDate != null) {
                                if (!less(toDate(paymentDate_adjustedOrUnadjustedDate), toDate(contractSummary_effectiveDate))) continue;
                                errorHandler.error("305", paymentDate_adjustedOrUnadjustedDate,
                                        "The event payment date of a loan contract (or contracts) event must be the same or later than the effective date of that loan contract (or contracts).",
                                        getDisplayName(), String.format("%s event payment date must not be before the contract effective date %s", toToken(paymentDate_adjustedOrUnadjustedDate), toToken(contractSummary_effectiveDate)));
                                result = false;
                            }

                        }

                        for (int index2 = 0, length2 = letterOfCreditNodeList.getLength (); index2 < length2; ++index2) {

                            Element letterOfCredit = (Element) letterOfCreditNodeList.item(index2);
                            Element letterOfCredit_effectiveDate = XPath.path(letterOfCredit, "effectiveDate");

                            if (letterOfCredit_effectiveDate != null) {
                                if (!less(toDate(paymentDate_adjustedOrUnadjustedDate), toDate(letterOfCredit_effectiveDate))) continue;
                                errorHandler.error("305", paymentDate_adjustedOrUnadjustedDate,
                                        "The event payment date of a loan contract (or contracts) event must be the same or later than the effective date of that loan contract (or contracts).",
                                        getDisplayName(), String.format("%s event payment date must not be before the contract effective date %s", toToken(paymentDate_adjustedOrUnadjustedDate), toToken(letterOfCredit_effectiveDate)));
                                result = false;
                            }

                        }

                        for (int index2 = 0, length2 = letterOfCreditSummaryNodeList.getLength (); index2 < length2; ++index2) {

                            Element letterOfCreditSummary = (Element) letterOfCreditSummaryNodeList.item(index2);
                            Element letterOfCreditSummary_effectiveDate = XPath.path(letterOfCreditSummary, "effectiveDate");

                            if (letterOfCreditSummary_effectiveDate != null) {
                                if (!less(toDate(paymentDate_adjustedOrUnadjustedDate), toDate(letterOfCreditSummary_effectiveDate))) continue;
                                errorHandler.error("305", paymentDate_adjustedOrUnadjustedDate,
                                        "The event payment date of a loan contract (or contracts) event must be the same or later than the effective date of that loan contract (or contracts).",
                                        getDisplayName(), String.format("%s event payment date must not be before the facility start date %s", toToken(paymentDate_adjustedOrUnadjustedDate), toToken(letterOfCreditSummary_effectiveDate)));
                                result = false;
                            }
                        }
                    }
                }
            }
            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, the commitment adjustment (if a commitment
     * reduction) cannot exceed the total commitment amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE14 = new Rule(R5_11__LATER_CONFIRMATION, "ln-14") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element context = (Element) nodes.item(0);
                Element commitmentAdjustment = XPath.path(context, "commitmentAdjustment");
                Element facilitySummary = XPath.path(context, "facilitySummary");

                if (commitmentAdjustment != null && facilitySummary != null) {

                    Element adjustmentType = XPath.path(commitmentAdjustment, "adjustment", "adjustmentType");

                    if (adjustmentType != null && DOM.getInnerText(adjustmentType).equals("Decrease")) {

                        Element commitmentAdjustmentCurrency = XPath.path(commitmentAdjustment, "adjustment", "amount", "currency");
                        Element facilityCommitmentCurrency = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "currency");

                        if (commitmentAdjustmentCurrency != null && facilityCommitmentCurrency != null && equal(commitmentAdjustmentCurrency, facilityCommitmentCurrency)) {

                            Element commitmentAdjustmentAmount = XPath.path(commitmentAdjustment, "adjustment", "amount", "amount");
                            Element facilityCommitmentAmount = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "amount");

                            if (commitmentAdjustmentAmount != null && facilityCommitmentAmount != null && less(toDecimal(facilityCommitmentAmount), toDecimal(commitmentAdjustmentAmount))) {


                                errorHandler.error("305", commitmentAdjustment,
                                        "Assuming the same currency, the commitment adjustment (if a commitment reduction) cannot exceed the total commitment amount.",
                                        getDisplayName(), String.format("Commitment adjustment amount of %s cannot exceed the total commitment amount of %s", toDecimal(commitmentAdjustmentAmount), toDecimal(facilityCommitmentAmount)));
                                result = false;

                            }

                        }

                    }

                }

            }
            return (result);

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, the loan contract adjustment (if a reduction)
     * cannot exceed the loan contract amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE15 = new Rule(R5_11__LATER_CONFIRMATION, "ln-15") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element context = (Element) nodes.item(0);
                Element loanContractAdjustment = XPath.path(context, "loanContractAdjustment");

                if (loanContractAdjustment != null) {

                    Element adjustmentType = XPath.path(loanContractAdjustment, "adjustment", "adjustmentType");

                    if (adjustmentType != null && DOM.getInnerText(adjustmentType).equals("Decrease")) {

                        Element adjustmentAmount = XPath.path(loanContractAdjustment, "adjustment", "amount", "amount");
                        Element adjustmentCurrency = XPath.path(loanContractAdjustment, "adjustment", "amount", "currency");

                        if (adjustmentAmount != null && adjustmentCurrency != null) {

                            NodeList contractSummaryList = XPath.paths(context, "contractSummary");

                            if (contractSummaryList != null && contractSummaryList.getLength() > 0) {

                                for (int contractSummaryIndex = 0; contractSummaryIndex < contractSummaryList.getLength(); ++contractSummaryIndex) {

                                    Element contractSummary = (Element) contractSummaryList.item(contractSummaryIndex);
                                    Element contractSummaryCurrency = XPath.path(contractSummary, "amount", "currency");

                                    if (contractSummaryCurrency == null || !equal(adjustmentCurrency, contractSummaryCurrency))
                                        continue;

                                    Element contractSummaryAmount = XPath.path(contractSummary, "amount", "amount");

                                    if (contractSummaryAmount == null || !less(toDecimal(contractSummaryAmount), toDecimal(adjustmentAmount)))
                                        continue;

                                    errorHandler.error("305", loanContractAdjustment,
                                            "Assuming the same currency, the loan contract adjustment (if a reduction) cannot exceed the loan contract amount.",
                                            getDisplayName(), String.format("Contract adjustment amount of %s cannot exceed the total contract amount of %s", toDecimal(adjustmentAmount), toDecimal(contractSummaryAmount)));
                                    result = false;

                                }

                            }

                            NodeList contractList = XPath.paths(context, "contract");

                            if (contractList != null && contractList.getLength() > 0) {

                                for (int contractIndex = 0; contractIndex < contractList.getLength(); ++contractIndex) {

                                    Element contract = (Element) contractList.item(contractIndex);
                                    Element contractCurrency = XPath.path(contract, "amount", "currency");

                                    if (contractCurrency == null || !equal(adjustmentCurrency, contractCurrency))
                                        continue;

                                    Element contractAmount = XPath.path(contract, "amount", "amount");

                                    if (contractAmount == null || !less(toDecimal(contractAmount), toDecimal(adjustmentAmount)))
                                        continue;

                                    errorHandler.error("305", loanContractAdjustment,
                                            "Assuming the same currency, the loan contract adjustment (if a reduction) cannot exceed the loan contract amount.",
                                            getDisplayName(), String.format("Contract adjustment amount of %s cannot exceed the total contract amount of %s", toDecimal(adjustmentAmount), toDecimal(contractAmount)));
                                    result = false;

                                }

                            }

                        }

                    }

                }

            }
            return (result);

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, the LC adjustment (if a reduction) cannot
     * exceed the LC amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE16 = new Rule(R5_11__LATER_CONFIRMATION, "ln-16") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element context = (Element) nodes.item(0);
                Element lcAdjustment = XPath.path(context, "lcAdjustment");

                if (lcAdjustment != null) {

                    Element adjustmentType = XPath.path(lcAdjustment, "adjustment", "adjustmentType");

                    if (adjustmentType != null && DOM.getInnerText(adjustmentType).equals("Decrease")) {

                        Element adjustmentAmount = XPath.path(lcAdjustment, "adjustment", "amount", "amount");
                        Element adjustmentCurrency = XPath.path(lcAdjustment, "adjustment", "amount", "currency");

                        if (adjustmentAmount != null && adjustmentCurrency != null) {

                            NodeList letterOfCreditSummaryList = XPath.paths(context, "letterOfCreditSummary");

                            if (letterOfCreditSummaryList != null && letterOfCreditSummaryList.getLength() > 0) {

                                for (int letterOfCreditSummaryIndex = 0; letterOfCreditSummaryIndex < letterOfCreditSummaryList.getLength(); ++letterOfCreditSummaryIndex) {

                                    Element letterOfCreditSummary = (Element) letterOfCreditSummaryList.item(letterOfCreditSummaryIndex);
                                    Element letterOfCreditSummaryCurrency = XPath.path(letterOfCreditSummary, "amount", "currency");

                                    if (letterOfCreditSummaryCurrency == null || !equal(adjustmentCurrency, letterOfCreditSummaryCurrency))
                                        continue;

                                    Element letterOfCreditSummaryAmount = XPath.path(letterOfCreditSummary, "amount", "amount");

                                    if (letterOfCreditSummaryAmount == null || !less(toDecimal(letterOfCreditSummaryAmount), toDecimal(adjustmentAmount)))
                                        continue;

                                    errorHandler.error("305", lcAdjustment,
                                            "Assuming the same currency, the LC adjustment (if a reduction) cannot exceed the LC amount.",
                                            getDisplayName(), String.format("LC adjustment amount of %s cannot exceed the total LC amount of %s", toDecimal(adjustmentAmount), toDecimal(letterOfCreditSummaryAmount)));
                                    result = false;

                                }

                            }

                            NodeList letterOfCreditList = XPath.paths(context, "letterOfCredit");

                            if (letterOfCreditList != null && letterOfCreditList.getLength() > 0) {

                                for (int letterOfCreditIndex = 0; letterOfCreditIndex < letterOfCreditList.getLength(); ++letterOfCreditIndex) {

                                    Element letterOfCredit = (Element) letterOfCreditList.item(letterOfCreditIndex);
                                    Element letterOfCreditCurrency = XPath.path(letterOfCredit, "amount", "currency");

                                    if (letterOfCreditCurrency == null || !equal(adjustmentCurrency, letterOfCreditCurrency))
                                        continue;

                                    Element letterOfCreditAmount = XPath.path(letterOfCredit, "amount", "amount");

                                    if (letterOfCreditAmount == null || !less(toDecimal(letterOfCreditAmount), toDecimal(adjustmentAmount)))
                                        continue;

                                    errorHandler.error("305", lcAdjustment,
                                            "Assuming the same currency, the LC adjustment (if a reduction) cannot exceed the LC amount.",
                                            getDisplayName(), String.format("Contract adjustment amount of %s cannot exceed the total LC amount of %s", toDecimal(adjustmentAmount), toDecimal(letterOfCreditAmount)));
                                    result = false;

                                }

                            }

                        }

                    }

                }

            }
            return (result);

        }

    };


    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, the repayment amount cannot exceed the loan
     * contract amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE17 = new Rule(R5_11__LATER_CONFIRMATION, "ln-17") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element context = (Element) nodes.item(0);
                Element repayment = XPath.path(context, "repayment");

                if (repayment != null) {

                    Element repaymentAmount = XPath.path(repayment, "amount", "amount");
                    Element repaymentCurrency = XPath.path(repayment, "amount", "currency");

                    if (repaymentAmount != null && repaymentCurrency != null) {

                        NodeList contractSummaryList = XPath.paths(context, "contractSummary");

                        if (contractSummaryList != null && contractSummaryList.getLength() > 0) {

                            for (int contractSummaryIndex = 0; contractSummaryIndex < contractSummaryList.getLength(); ++contractSummaryIndex) {

                                Element contractSummary = (Element) contractSummaryList.item(contractSummaryIndex);
                                Element contractSummaryCurrency = XPath.path(contractSummary, "amount", "currency");

                                if (contractSummaryCurrency == null || !equal(repaymentCurrency, contractSummaryCurrency))
                                    continue;

                                Element contractSummaryAmount = XPath.path(contractSummary, "amount", "amount");

                                if (contractSummaryAmount == null || !less(toDecimal(contractSummaryAmount), toDecimal(repaymentAmount)))
                                    continue;

                                errorHandler.error("305", repayment,
                                        "Assuming the same currency, the repayment amount cannot exceed the loan contract amount.",
                                        getDisplayName(), String.format("Repayment amount of %s cannot exceed the total contract amount of %s", toDecimal(repaymentAmount), toDecimal(contractSummaryAmount)));
                                result = false;

                            }

                        }

                        NodeList contractList = XPath.paths(context, "contract");

                        if (contractList != null && contractList.getLength() > 0) {

                            for (int contractIndex = 0; contractIndex < contractList.getLength(); ++contractIndex) {

                                Element contract = (Element) contractList.item(contractIndex);
                                Element contractCurrency = XPath.path(contract, "amount", "currency");

                                if (contractCurrency == null || !equal(repaymentCurrency, contractCurrency)) continue;

                                Element contractAmount = XPath.path(contract, "amount", "amount");

                                if (contractAmount == null || !less(toDecimal(contractAmount), toDecimal(repaymentAmount)))
                                    continue;

                                errorHandler.error("305", repayment,
                                        "Assuming the same currency, the repayment amount cannot exceed the loan contract amount.",
                                        getDisplayName(), String.format("Repayment amount of %s cannot exceed the total contract amount of %s", toDecimal(repaymentAmount), toDecimal(contractAmount)));
                                result = false;

                            }

                        }

                    }

                }

            }

            return (result);

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, the share amount of a repayment cannot exceed
     * the share amount of the total current commitment of the facility.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE18 = new Rule(R5_11__LATER_CONFIRMATION, "ln-18") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanServicingNotification"), errorHandler);
            return validate(nodeIndex.getElementsByName("loanServicingNotification"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element context = (Element) nodes.item(0);
                Element repayment = XPath.path(context, "repayment");

                if (repayment != null) {

                    Element repaymentShareAmount = XPath.path(repayment, "amount", "shareAmount");
                    Element repaymentCurrency = XPath.path(repayment, "amount", "currency");

                    if (repaymentShareAmount != null && repaymentCurrency != null) {

                        NodeList facilitySummaryList = XPath.paths(context, "facilitySummary");

                        if (facilitySummaryList != null && facilitySummaryList.getLength() > 0) {

                            for (int facilitySummaryIndex = 0; facilitySummaryIndex < facilitySummaryList.getLength(); ++facilitySummaryIndex) {

                                Element facilitySummary = (Element) facilitySummaryList.item(facilitySummaryIndex);
                                Element facilitySummaryCurrency = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "currency");

                                if (facilitySummaryCurrency == null || !equal(repaymentCurrency, facilitySummaryCurrency))
                                    continue;

                                Element facilitySummaryShareAmount = XPath.path(facilitySummary, "currentCommitment", "totalCommitmentAmount", "shareAmount");

                                if (facilitySummaryShareAmount == null || !less(toDecimal(facilitySummaryShareAmount), toDecimal(repaymentShareAmount)))
                                    continue;

                                errorHandler.error("305", repayment,
                                        "Assuming the same currency, the share amount of a repayment cannot exceed the share amount of the total current commitment of the facility.",
                                        getDisplayName(), String.format("Lender share of the repayment amount of %s cannot exceed the lender share of the total current facility commitment of %s", toDecimal(repaymentShareAmount), toDecimal(facilitySummaryShareAmount)));
                                result = false;

                            }

                        }

                    }

                }

            }

            return (result);

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that a fee payment date cannot be before the effective date of the payment
     * event.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE19 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION, LOAN_SERVICING_NOTIFICATION), "ln-19") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation()) {
                String firstContext = "EventPayment";
                final List<String> secondContextList = new ArrayList<>(Arrays.asList(
                        "AccruingFeePayment",
                        "AmendmentFeePayment",
                        "FacilityExtensionFeePayment",
                        "FundingFeePayment",
                        "MiscFeePayment",
                        "FacilityPrepaymentFeePayment",
                        "UpfrontFeePayment",
                        "WaiverFeePayment",
                        "LcIssuanceFeePayment",
                        "BreakageFeePayment",
                        "LoanInterestPayment",
                        "Repayment"
                ));
                String namespace = determineFpMLNamespace(nodeIndex);
                return secondContextList.stream().map(x -> validate(nodeIndex.getElementsByType(namespace,firstContext),nodeIndex.getElementsByType(namespace,x),errorHandler)).allMatch(x-> x);
            }
            else {
                String firstContext = "eventPayment";
                final List<String> secondContextList = new ArrayList<>(Arrays.asList(
                        "accruingFeePayment",
                        "amendmentFeePayment",
                        "facilityExtensionFeePayment",
                        "fundingFeePayment",
                        "miscFeePayment",
                        "facilityPrepaymentFeePayment",
                        "upfrontFeePayment",
                        "waiverFeePayment",
                        "lcIssuanceFeePayment",
                        "breakageFeePayment",
                        "interestPayment",
                        "repayment"
                ));
                return secondContextList.stream().map(x -> validate(nodeIndex.getElementsByName(firstContext),nodeIndex.getElementsByName(x),errorHandler)).allMatch(x-> x);
            }
        }

        private boolean validate(NodeList eventPayments, NodeList events, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (eventPayments != null && eventPayments.getLength() > 0) {

                for (int eventPaymentIndex = 0; eventPaymentIndex < eventPayments.getLength(); ++eventPaymentIndex) {

                    Element payment = (Element) eventPayments.item(eventPaymentIndex);
                    Element adjusted = XPath.path(payment, "paymentDate", "adjustedDate");

                    if (events != null && events.getLength() > 0) {

                        for (int eventsIndex = 0; eventsIndex < events.getLength(); ++eventsIndex) {

                            Element event = (Element) events.item(eventsIndex);
                            Element eventEffective = XPath.path(event, "effectiveDate");

                            if (eventEffective != null) {

                                if (adjusted != null) {

                                    if (!less(toDate(adjusted), toDate(eventEffective))) continue;
                                    errorHandler.error("305", payment,
                                            "A fee payment date cannot be before the effective date of the payment event.",
                                            getDisplayName(), String.format("%s fee payment date must not be before the payment event effective date %s", toToken(adjusted), toToken(eventEffective)));
                                    result = false;

                                } else if (adjusted == null) {

                                    Element unadjusted = XPath.path(payment, "paymentDate", "unadjustedDate");

                                    if (unadjusted != null) {

                                        if (!less(toDate(unadjusted), toDate(eventEffective))) continue;
                                        errorHandler.error("305", payment,
                                                "A fee payment date cannot be before the effective date of the payment event.",
                                                getDisplayName(), String.format("%s fee payment date must not be before the payment event effective date %s", toToken(unadjusted), toToken(eventEffective)));
                                        result = false;
                                    }

                                }

                            }

                        }

                    }

                }

            }
            return (result);

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, the share amount of payments made in
     * association with an event may not exceed the 'eventPayment' amount conveyed.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE20 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION, LOAN_SERVICING_NOTIFICATION), "ln-20") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation()) {
                String firstContext = "EventPayment";
                final List<String> secondContextList = new ArrayList<>(Arrays.asList(
                        "AccruingFeePayment",
                        "AmendmentFeePayment",
                        "FacilityExtensionFeePayment",
                        "FundingFeePayment",
                        "MiscFeePayment",
                        "FacilityPrepaymentFeePayment",
                        "UpfrontFeePayment",
                        "WaiverFeePayment",
                        "LcIssuanceFeePayment",
                        "BreakageFeePayment",
                        "LoanInterestPayment",
                        "Repayment"
                ));
                String namespace = determineFpMLNamespace(nodeIndex);

                return secondContextList.stream().map(x -> validate(nodeIndex.getElementsByType(namespace,firstContext),nodeIndex.getElementsByType(namespace,x),errorHandler)).allMatch(x-> x);
            }
            else {
                String firstContext = "eventPayment";
                final List<String> secondContextList = new ArrayList<>(Arrays.asList(
                        "accruingFeePayment",
                        "amendmentFeePayment",
                        "facilityExtensionFeePayment",
                        "fundingFeePayment",
                        "miscFeePayment",
                        "facilityPrepaymentFeePayment",
                        "upfrontFeePayment",
                        "waiverFeePayment",
                        "lcIssuanceFeePayment",
                        "breakageFeePayment",
                        "interestPayment",
                        "repayment"
                ));

                return secondContextList.stream().map(x -> validate(nodeIndex.getElementsByName(firstContext),nodeIndex.getElementsByName(x),errorHandler)).allMatch(x-> x);
            }
        }

        private boolean validate(NodeList eventPayments, NodeList events, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (eventPayments != null && eventPayments.getLength() > 0) {

                for (int eventPaymentIndex = 0; eventPaymentIndex < eventPayments.getLength(); ++eventPaymentIndex) {

                    Element payment = (Element) eventPayments.item(eventPaymentIndex);
                    Element paymentAmount = XPath.path(payment, "paymentAmount", "amount");
                    Element paymentCurrency = XPath.path(payment, "paymentAmount", "currency");

                    if (events != null && events.getLength() > 0) {

                        for (int eventsIndex = 0; eventsIndex < events.getLength(); ++eventsIndex) {

                            Element event = (Element) events.item(eventsIndex);
                            Element eventCurrency = XPath.path(event, "amount", "currency");

                            if (eventCurrency == null || !equal(paymentCurrency, eventCurrency)) continue;

                            Element eventShareAmount = XPath.path(event, "amount", "shareAmount");

                            if (eventShareAmount == null || !less(toDecimal(paymentAmount), toDecimal(eventShareAmount)))
                                continue;

                            errorHandler.error("305", payment,
                                    "Assuming the same currency, the share amount of payments made in association with an event may not exceed the 'eventPayment' amount conveyed.",
                                    getDisplayName(), String.format("Lender share of the event payment amount of %s cannot exceed the total event payment amount of %s", toToken(eventShareAmount), toToken(paymentAmount)));
                            result = false;

                        }

                    }

                }

            }

            return (result);

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that if there are childEventIdentifer elements included, then there must be a
     * corresponding event for each identifier within the notification.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE21 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION, LOAN_SERVICING_NOTIFICATION), "ln-21") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(
                        nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "AbstractLoanServicingEvent"),
                        errorHandler
                );
            else {
                final List<String> contextList = new ArrayList<>(Arrays.asList(
                        "accrualOptionChange",
                        "accruingFeeChange",
                        "accruingFeeExpiry",
                        "accruingFeePayment",
                        "accruingPikPayment",
                        "amendmentFeePayment",
                        "commitmentAdjustment",
                        "defaultRateChange",
                        "defaultRateExpiry",
                        "facilityExtensionFeePayment",
                        "facilityPrepayment",
                        "facilityPrepaymentFeePayment",
                        "facilityTermination",
                        "fundingFeePayment",
                        "mandatoryCostRateChange",
                        "mandatoryCostRateExpiry",
                        "miscFeePayment",
                        "penaltyRateChange",
                        "penaltyRateExpiry",
                        "upfrontFeePayment",
                        "waiverFeePayment",
                        "lcAdjustment",
                        "lcFxRevaluation",
                        "lcIssuance",
                        "lcIssuanceFeePayment",
                        "lcRateChange",
                        "lcRenewal",
                        "lcTermination",
                        "baseRateSet",
                        "borrowing",
                        "breakageFeePayment",
                        "interestCapitalization",
                        "interestPayment",
                        "loanContractAdjustment",
                        "maturityChange",
                        "repayment",
                        "rollover"
                ));

                return contextList.stream().map(x -> nodeIndex.getElementsByName(x)).map(x -> validate(x,errorHandler)).allMatch(x-> x);
            }
        }

        private boolean validate(NodeList events, ValidationErrorHandler errorHandler) {

            boolean result = true;

            class EventIdentifier {

                String href;
                String scheme;
                String value;

                private EventIdentifier(){}

                public EventIdentifier(String href, String scheme, String value) {
                    this.href = href;
                    this.scheme = scheme;
                    this.value = value;
                }

                public String getHref() {
                    return href;
                }

                public void setHref(String href) {
                    this.href = href;
                }

                public String getScheme() {
                    return scheme;
                }

                public void setScheme(String scheme) {
                    this.scheme = scheme;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public Boolean equals(EventIdentifier eventIdentifier) {
                    return (this.href.equals(eventIdentifier.getHref()) &&
                            this.scheme.equals(eventIdentifier.getScheme()) &&
                            this.value.equals(eventIdentifier.getValue()));
                }
            }

            if (events != null) {

                ArrayList<EventIdentifier> childEventIdentifierPopulatedList = new ArrayList<>();
                ArrayList<EventIdentifier> eventIdentifierPopulatedList = new ArrayList<>();

                for (int eventIndex = 0; eventIndex < events.getLength(); ++eventIndex) {

                    Element event = (Element) events.item(eventIndex);
                    Element parentEventIdentifier = XPath.path(event, "parentEventIdentifier");

                    if (parentEventIdentifier != null) {
                        NodeList childEventIdentifierList = XPath.paths(parentEventIdentifier, "childEventIdentifier");

                        if (childEventIdentifierList != null && childEventIdentifierList.getLength() > 0) {

                            //loop to get children events
                            for (int childEventsIndex = 0; childEventsIndex < childEventIdentifierList.getLength(); ++childEventsIndex) {

                                Element childEvent = (Element) childEventIdentifierList.item(childEventsIndex);
                                Element childEventPartyRef = XPath.path(childEvent, "partyReference");
                                String childEventPartyHref = childEventPartyRef.getAttribute("href");
                                Element childEventId = XPath.path(childEvent, "eventId");
                                String childEventIdScheme = childEventId.getAttribute("eventIdScheme");

                                childEventIdentifierPopulatedList.add(new EventIdentifier(toToken(childEventId),childEventIdScheme,childEventPartyHref));
                            }
                        }
                    }

                    NodeList eventIdentifierList = XPath.paths(event, "eventIdentifier");

                    if (eventIdentifierList != null) {

                        //loop for eventIdenfier's
                        for (int eventIdsIndex = 0; eventIdsIndex < eventIdentifierList.getLength(); ++eventIdsIndex) {

                            Element eventIdentifier = (Element) eventIdentifierList.item(eventIdsIndex);
                            Element eventPartyRef = XPath.path(eventIdentifier, "partyReference");
                            String eventPartyHref = eventPartyRef.getAttribute("href");
                            Element eventId = XPath.path(eventIdentifier, "eventId");
                            String eventIdScheme = eventId.getAttribute("eventIdScheme");

                            eventIdentifierPopulatedList.add(new EventIdentifier(toToken(eventId),eventIdScheme,eventPartyHref));
                        }
                    }
                }

                for (EventIdentifier eventIdentifier : childEventIdentifierPopulatedList) {
                    Boolean found = false;

                    for (EventIdentifier identifier : eventIdentifierPopulatedList) {
                        if (eventIdentifier.equals(identifier)) {
                            found = true;
                            continue;
                        }
                    }

                    if (!found) {
                        errorHandler.error("305", null,
                                "If there are childEventIdentifier elements included, then there must be a corresponding event for each identifier within the notification.",
                                getDisplayName(), String.format("Child event identifier of %s not found corresponding event within the notification", null));
                        result = false;
                    }
                }
            }
            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the sum of all share amounts of outstandings positions must be less than or
     * equal to the total commitment share amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE22 = new Rule(R5_11__LATER_CONFIRMATION, "ln-22") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "FacilityPosition"), errorHandler);
            return validate(nodeIndex.getElementsByName("facilityPosition"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            for (int index = 0; index < nodes.getLength(); ++index) {
                Double outstandingsSum = 0.0;
                Element context = (Element) nodes.item(index);
                Element comAmount = XPath.path(context, "commitment", "totalCommitmentAmount", "shareAmount");
                Element comCurrency = XPath.path(context, "commitment", "totalCommitmentAmount", "currency");

                if ((comAmount == null) || (comCurrency == null)) continue;

                NodeList outstandingsList = XPath.paths(context, "outstandingsPosition");
                if ((outstandingsList == null) || (outstandingsList.getLength() == 0)) continue;

                for (int outstandingsIndex = 0; outstandingsIndex < outstandingsList.getLength(); ++outstandingsIndex) {
                    Element outstandingsContext = (Element) outstandingsList.item(outstandingsIndex);
                    Element outstandingsAmount = XPath.path(outstandingsContext, "amount", "shareAmount");
                    Element outstandingsCurrency = XPath.path(outstandingsContext, "amount", "currency");

                    if ((outstandingsAmount == null) || (outstandingsCurrency == null)) continue;
                    if (notEqual(comCurrency, outstandingsCurrency)) continue;

                    outstandingsSum += toDouble(outstandingsAmount);
                }

                if (Double.compare(toDouble(comAmount), outstandingsSum) < 0) {
                    errorHandler.error("305", context,
                            "The sum of all share amounts of outstandings positions must be less than or equal to the total commitment share amount.",
                            getDisplayName(), String.format("Sum of all share amounts of outstandings of %s not less or equal to total commitment share amount of %s", outstandingsSum, toDouble(comAmount)));
                    result = false;
                }

            }

            return (result);
        }
    };


    /**
     * A <CODE>Rule</CODE> that ensures that the sum amount of all loans outstanding may not exceed the facility
     * commitment amount under which all loans outstanding are housed.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE23 = new Rule(R5_11__LATER_CONFIRMATION, "ln-23") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "FacilityPosition"), errorHandler);
            return validate(nodeIndex.getElementsByName("facilityPosition"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            for (int index = 0; index < nodes.getLength(); ++index) {
                Double outstandingsSum = 0.0;
                Element context = (Element) nodes.item(index);
                Element comAmount = XPath.path(context, "commitment", "totalCommitmentAmount", "amount");
                Element comCurrency = XPath.path(context, "commitment", "totalCommitmentAmount", "currency");

                if ((comAmount == null) || (comCurrency == null)) continue;

                NodeList outstandingsList = XPath.paths(context, "outstandingsPosition");
                if ((outstandingsList == null) || (outstandingsList.getLength() == 0)) continue;

                for (int outstandingsIndex = 0; outstandingsIndex < outstandingsList.getLength(); ++outstandingsIndex) {
                    Element outstandingsContext = (Element) outstandingsList.item(outstandingsIndex);
                    Element outstandingsAmount = XPath.path(outstandingsContext, "amount", "amount");
                    Element outstandingsCurrency = XPath.path(outstandingsContext, "amount", "currency");

                    if ((outstandingsAmount == null) || (outstandingsCurrency == null)) continue;
                    if (notEqual(comCurrency, outstandingsCurrency)) continue;

                    outstandingsSum += toDouble(outstandingsAmount);
                }

                if (Double.compare(toDouble(comAmount), outstandingsSum) < 0) {
                    errorHandler.error("305", context,
                            "The sum amount of all loans outstanding may not exceed the facility commitment amount under which all loans outstanding are housed.",
                            getDisplayName(), String.format("Sum of all amounts of outstandings of %s not less or equal to total commitment amount of %s", outstandingsSum, toDouble(comAmount)));
                    result = false;
                }

            }

            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that the sum of all prior amounts of outstandings positions must be less than
     * or equal to the prior total commitment amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE24 = new Rule(R5_11__LATER_CONFIRMATION, "ln-24") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "FacilityPosition"), errorHandler);
            return validate(nodeIndex.getElementsByName("facilityPosition"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            for (int index = 0; index < nodes.getLength(); ++index) {
                Double outstandingsSum = 0.0;
                Element context = (Element) nodes.item(index);
                Element comAmount = XPath.path(context, "priorCommitment", "totalCommitmentAmount", "shareAmount");
                Element comCurrency = XPath.path(context, "priorCommitment", "totalCommitmentAmount", "currency");

                if ((comAmount == null) || (comCurrency == null)) continue;

                NodeList outstandingsList = XPath.paths(context, "outstandingsPosition");
                if ((outstandingsList == null) || (outstandingsList.getLength() == 0)) continue;

                for (int outstandingsIndex = 0; outstandingsIndex < outstandingsList.getLength(); ++outstandingsIndex) {
                    Element outstandingsContext = (Element) outstandingsList.item(outstandingsIndex);
                    Element outstandingsAmount = XPath.path(outstandingsContext, "priorAmount", "shareAmount");
                    Element outstandingsCurrency = XPath.path(outstandingsContext, "priorAmount", "currency");

                    if ((outstandingsAmount == null) || (outstandingsCurrency == null)) continue;
                    if (notEqual(comCurrency, outstandingsCurrency)) continue;

                    outstandingsSum += toDouble(outstandingsAmount);
                }

                if (Double.compare(toDouble(comAmount), outstandingsSum) < 0) {
                    errorHandler.error("305", context,
                            "The sum of all prior amounts of outstandings positions must be less than or equal to the prior total commitment amount.",
                            getDisplayName(), String.format("Sum of all prior share amounts of outstandings of %s not less or equal to prior total commitment share amount of %s", outstandingsSum, toDouble(comAmount)));
                    result = false;
                }

            }

            return (result);
        }
    };


    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, a loan contract prior global amount may not
     * exceed the facility commitment prior global amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE25 = new Rule(R5_11__LATER_CONFIRMATION, "ln-25") {
        /**
         * {@inheritDoc}
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "FacilityPosition"), errorHandler);
            return validate(nodeIndex.getElementsByName("facilityPosition"), errorHandler);
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            for (int index = 0; index < nodes.getLength(); ++index) {
//                Double outstandingsSum  = 0.0;
                Element context = (Element) nodes.item(index);
                Element comAmount = XPath.path(context, "priorCommitment", "totalCommitmentAmount", "amount");
                Element comCurrency = XPath.path(context, "priorCommitment", "totalCommitmentAmount", "currency");

                if ((comAmount == null) || (comCurrency == null)) continue;

                NodeList outstandingsList = XPath.paths(context, "outstandingsPosition");
                if ((outstandingsList == null) || (outstandingsList.getLength() == 0)) continue;

                for (int outstandingsIndex = 0; outstandingsIndex < outstandingsList.getLength(); ++outstandingsIndex) {
                    Element outstandingsContext = (Element) outstandingsList.item(outstandingsIndex);
                    Element outstandingsAmount = XPath.path(outstandingsContext, "priorAmount", "amount");
                    Element outstandingsCurrency = XPath.path(outstandingsContext, "priorAmount", "currency");

                    if ((outstandingsAmount == null) || (outstandingsCurrency == null)) continue;
                    if (notEqual(comCurrency, outstandingsCurrency)) continue;

                    if (Double.compare(toDouble(comAmount), toDouble(outstandingsAmount)) < 0) {
                        errorHandler.error("305", context,
                                "Assuming the same currency, a loan contract prior global amount may not exceed the facility commitment prior global amount.",
                                getDisplayName(), String.format("Prior amount of outstanding of %s not less or equal to prior total commitment amount of %s", toDouble(outstandingsAmount), toDouble(comAmount)));
                        result = false;
                    }

//                    outstandingsSum += toDouble(outstandingsAmount);
                }


            }

            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that if a trade is designated as a 'primary' type, it cannot also be flagged as
     * a when-issued trade.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE26 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION,LOAN_TRADE_NOTIFICATION), "ln-26") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanTrade"), errorHandler);
            return (validate(nodeIndex.getElementsByName("trade"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {
                Element trade           = (Element) nodes.item(0);
                Element type            = XPath.path(trade, "marketType");
                Element whenIssuedFlag  = XPath.path(trade, "whenIssuedFlag");

                if (type != null) {

                    if (DOM.getInnerText(type).toUpperCase().equals("PRIMARY")) {

                        if (toBool(whenIssuedFlag) != false) {

                            errorHandler.error("305", whenIssuedFlag,
                                    "If a trade is designated as a 'primary' market type, it cannot also be flagged as a when-issued trade.",
                                    getDisplayName(), String.format("If market type equals %s then whenIssuedFlag mustn't be %s", toToken(type), toToken(whenIssuedFlag)));
                            result = false;

                        }

                    }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that for any trade settlement task, the date the task was raised must be on or
     * before the date the task is expected to be cleared.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE27 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION,LOAN_TRADE_NOTIFICATION), "ln-27") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanTradeSettlementTask"), errorHandler);
            return (validate(nodeIndex.getElementsByName("settlementTask"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element task        = (Element) nodes.item(0);
                Element expected    = XPath.path(task, "dates","expectedDate");

                if (expected != null) {

                    Element raised  = XPath.path(task, "dates", "raisedDate");

                    if (less(toDate(expected), toDate(raised))) {

                        errorHandler.error("305", task,
                                "For any trade settlement task, the date the task was raised must be on or before the date the task is expected to be cleared.",
                                getDisplayName(), String.format("%s task expected to be cleared date must not be before the raised date %s", toToken(expected), toToken(raised)));
                        result = false;

                    }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that for any allocation settlement task, the date the task was raised must be on
     * or before the date the task is expected to be cleared.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE28 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION,LOAN_ALLOCATION_NOTIFICATION), "ln-28") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanAllocationSettlementTask"), errorHandler);
            return (validate(nodeIndex.getElementsByName("settlementTask"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                for (int index = 0; index < nodes.getLength(); ++index) {

                    Element task        = (Element) nodes.item(index);
                    Element expected    = XPath.path(task, "dates","expectedDate");

                    if (expected != null) {

                        Element raised  = XPath.path(task, "dates", "raisedDate");

                        if (less(toDate(expected), toDate(raised))) {

                            errorHandler.error("305", task,
                                    "For any allocation settlement task, the date the task was raised must be on or before the date the task is expected to be cleared.",
                                    getDisplayName(), String.format("%s task expected to be cleared date must not be before the raised date %s", toToken(expected), toToken(raised)));
                            result = false;

                        }

                    }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that for Secondary Market trades, if facility summary structure is conveyed,
     * the trade date cannot be earlier than the facility start date.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE29 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION,LOAN_TRADE_NOTIFICATION), "ln-29") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanTradeNotification"), errorHandler);
            return (validate(nodeIndex.getElementsByName("loanTradeNotification"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {
                Element context         = (Element) nodes.item(0);
                Element trade           = XPath.path(context, "trade");
                Element facilitySummary = XPath.path(context, "facilitySummary");

                if (trade != null && facilitySummary != null) {

                    Element type = XPath.path(trade, "marketType");

                    if (DOM.getInnerText(type).toUpperCase().equals("SECONDARY")) {

                        Element tradeDate = XPath.path(trade, "tradeDate");
                        Element startDate = XPath.path(facilitySummary, "startDate");

                        if (less(toDate(tradeDate), toDate(startDate))) {

                            errorHandler.error("305", context,
                                    "For Secondary Market trades, if facility summary structure is conveyed, the trade date cannot be earlier than the facility start date.",
                                    getDisplayName(), String.format("Secondary Market Trade date of %s mustn't be before the facility start date %s", toToken(tradeDate), toToken(startDate)));
                            result = false;

                        }

                    }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that if the facility summary structure is conveyed and maturity date is
     * included, the trade date must be earlier than the facility maturity date.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE30 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION,LOAN_TRADE_NOTIFICATION), "ln-30") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanTradeNotification"), errorHandler);
            return (validate(nodeIndex.getElementsByName("loanTradeNotification"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {
                Element context         = (Element) nodes.item(0);
                Element trade           = XPath.path(context, "trade");
                Element maturityDate    = XPath.path(context, "facilitySummary", "maturityDate");

                if (trade != null && maturityDate != null) {

                        Element tradeDate = XPath.path(trade, "tradeDate");

                        if (less(toDate(maturityDate), toDate(tradeDate))) {

                            errorHandler.error("305", context,
                                    "If the facility summary structure is conveyed and maturity date is included, the trade date must be earlier than the facility maturity date.",
                                    getDisplayName(), String.format("Trade date of %s mustn't be after the facility maturity date %s", toToken(tradeDate), toToken(maturityDate)));
                            result = false;

                        }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, the loan trade amount may not exceed the
     * current global facility commitment amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE31 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION,LOAN_TRADE_NOTIFICATION), "ln-31") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanTradeNotification"), errorHandler);
            return (validate(nodeIndex.getElementsByName("loanTradeNotification"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {
                Element context         = (Element) nodes.item(0);
                Element trade           = XPath.path(context, "trade");
                Element facilitySummary = XPath.path(context, "facilitySummary");

                if (trade != null && facilitySummary != null) {

                    Element tradeAmount         = XPath.path(trade, "amount", "amount");
                    Element tradeCurrency       = XPath.path(trade, "amount", "currency");
                    Element facilityAmount      = XPath.path(facilitySummary, "currentCommitment","totalCommitmentAmount", "amount");
                    Element facilityCurrency    = XPath.path(facilitySummary, "currentCommitment","totalCommitmentAmount", "currency");

                    if ((tradeAmount != null) && (tradeCurrency != null) && (facilityAmount != null) && (facilityCurrency != null)) {

                        if (equal(tradeCurrency, facilityCurrency)) {

                            if (Double.compare(toDouble(facilityAmount), toDouble(tradeAmount)) < 0) {
                                errorHandler.error("305", context,
                                        "Assuming the same currency, the loan trade amount may not exceed the current global facility commitment amount.",
                                        getDisplayName(), String.format("Loan trade amount of %s not less or equal to facility total commitment amount of %s", toDouble(tradeAmount), toDouble(facilityAmount)));
                                result = false;
                            }

                        }

                    }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that for Secondary Market trades, if facility summary structure is conveyed,
     * the allocation of a trade cannot occur earlier than the facility start date.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE32 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION,LOAN_ALLOCATION_NOTIFICATION), "ln-32") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanAllocationNotification"), errorHandler);
            return (validate(nodeIndex.getElementsByName("loanAllocationNotification"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element context = (Element) nodes.item(0);
                Element facilitySummary = XPath.path(context, "facilitySummary");

                if (facilitySummary != null) {

                    Element notice      = XPath.path(context, "noticeDate");
                    Element startDate   = XPath.path(facilitySummary, "startDate");

                    if (less(toDate(notice), toDate(startDate))) {

                        errorHandler.error("305", context,
                                "If facility summary structure is conveyed, the allocation of a trade cannot occur earlier than the facility start date.",
                                getDisplayName(), String.format("%s allocation notice date must not be before the facility start date %s", toToken(notice), toToken(startDate)));
                        result = false;

                    }

                }

             }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, any single loan allocation amount may not
     * exceed the loan trade amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE33 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION, LOAN_ALLOCATION_NOTIFICATION), "ln-33") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanAllocationNotification"), errorHandler);
            return (validate(nodeIndex.getElementsByName("loanAllocationNotification"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element context = (Element) nodes.item(0);
                Element tradeSummary = XPath.path(context, "tradeSummary");

                if (tradeSummary != null) {

                    Element tradeAmount = XPath.path(tradeSummary, "amount", "amount");
                    Element tradeCurrency = XPath.path(tradeSummary, "amount", "currency");

                    NodeList allocationSummaryList = XPath.paths(context, "allocationSummary");

                    if ((allocationSummaryList != null) && (allocationSummaryList.getLength() != 0)) {

                        for (int allocationSummaryIndex = 0; allocationSummaryIndex < allocationSummaryList.getLength(); ++allocationSummaryIndex) {

                            Element allocationSummary = (Element) allocationSummaryList.item(allocationSummaryIndex);
                            Element allocationSummaryAmount = XPath.path(allocationSummary, "amount", "amount");
                            Element allocationSummaryCurrency = XPath.path(allocationSummary, "amount", "currency");

                            if ((allocationSummaryAmount == null) || (allocationSummaryCurrency == null) || (tradeAmount == null) || (tradeCurrency == null))
                                continue;
                            if (notEqual(allocationSummaryCurrency, tradeCurrency)) continue;

                            if (Double.compare(toDouble(tradeAmount), toDouble(allocationSummaryAmount)) < 0) {
                                errorHandler.error("305", context,
                                        "Assuming the same currency, any single loan allocation amount may not exceed the loan trade amount.",
                                        getDisplayName(), String.format("Loan allocation amount of %s not less or equal to loan trade amount of %s", toDouble(allocationSummaryAmount), toDouble(tradeAmount)));
                                result = false;
                            }

                        }

                    }

                    NodeList allocationList = XPath.paths(context, "allocation");

                    if ((allocationList != null) && (allocationList.getLength() != 0)) {

                        for (int allocationIndex = 0; allocationIndex < allocationList.getLength(); ++allocationIndex) {

                            Element allocation = (Element) allocationList.item(allocationIndex);
                            Element allocationAmount = XPath.path(allocation, "amount", "amount");
                            Element allocationCurrency = XPath.path(allocation, "amount", "currency");

                            if ((allocationAmount == null) || (allocationCurrency == null) || (tradeAmount == null) || (tradeCurrency == null))
                                continue;
                            if (notEqual(allocationCurrency, tradeCurrency)) continue;

                            if (Double.compare(toDouble(tradeAmount), toDouble(allocationAmount)) < 0) {
                                errorHandler.error("305", context,
                                        "Assuming the same currency, any single loan allocation amount may not exceed the loan trade amount.",
                                        getDisplayName(), String.format("Loan allocation amount of %s not less or equal to loan trade amount of %s", toDouble(allocationAmount), toDouble(tradeAmount)));
                                result = false;
                            }

                        }

                    }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that assuming the same currency, the sum of all loan allocation amounts must
     * equal the loan trade amount.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE34 = new Rule(Precondition.and(R5_11__LATER_CONFIRMATION,LOAN_ALLOCATION_NOTIFICATION), "ln-34") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanAllocationNotification"), errorHandler);
            return (validate(nodeIndex.getElementsByName("loanAllocationNotification"), errorHandler));
        }
        

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element     context         = (Element) nodes.item(0);
                Element     tradeSummary    = XPath.path(context, "tradeSummary");

                if (tradeSummary != null) {

                    Element tradeAmount     = XPath.path(tradeSummary, "amount", "amount");
                    Element tradeCurrency   = XPath.path(tradeSummary, "amount", "currency");

                    if ((tradeAmount != null) && (tradeCurrency != null)) {

                        Double      allocationsSum          = 0.0;
                        NodeList    allocationSummaryList   = XPath.paths(context, "allocationSummary");

                        if ((allocationSummaryList != null) && (allocationSummaryList.getLength() != 0)) {

                            for (int allocationSummaryIndex = 0; allocationSummaryIndex < allocationSummaryList.getLength(); ++allocationSummaryIndex) {

                                Element allocationSummary           = (Element) allocationSummaryList.item(allocationSummaryIndex);
                                Element allocationSummaryAmount     = XPath.path(allocationSummary, "amount", "amount");
                                Element allocationSummaryCurrency   = XPath.path(allocationSummary, "amount", "currency");

                                if ((allocationSummaryAmount == null) || (allocationSummaryCurrency == null)) continue;
                                if (notEqual(allocationSummaryCurrency, tradeCurrency)) continue;
                                allocationsSum += toDouble(allocationSummaryAmount);

                            }

                        }

                        NodeList allocationList = XPath.paths(context, "allocation");

                        if ((allocationList != null) && (allocationList.getLength() != 0)) {

                            for (int allocationIndex = 0; allocationIndex < allocationList.getLength(); ++allocationIndex) {

                                Element allocation = (Element) allocationList.item(allocationIndex);
                                Element allocationAmount = XPath.path(allocation, "amount", "amount");
                                Element allocationCurrency = XPath.path(allocation, "amount", "currency");

                                if ((allocationAmount == null) || (allocationCurrency == null)) continue;
                                if (notEqual(allocationCurrency, tradeCurrency)) continue;
                                allocationsSum += toDouble(allocationAmount);

                            }

                        }

                        if (allocationsSum.compareTo(toDouble(tradeAmount)) != 0) {
                            errorHandler.error("305", context,
                                    "Assuming the same currency, the sum of all loan allocation amounts must equal the loan trade amount.",
                                    getDisplayName(), String.format("The sum of loan allocation amounts of %s not equal to loan trade amount of %s", allocationsSum, toDouble(tradeAmount)));
                            result = false;
                        }

                    }

                }

            }

            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that the effectiveDate element must represent a date on or after the
     * notification date (i.e. header creation timestamp). Loan Party Profile Notifications can be future-dated, but
     * never backdated.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE35 = new Rule(R5_11__LATER_CONFIRMATION, "ln-35") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanPartyProfileStatement"), errorHandler);
            return (validate(nodeIndex.getElementsByName("loanPartyProfileStatement"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element context = (Element) nodes.item(0);
                Element effectiveDate = XPath.path(context, "partyProfile", "effectiveDate");
                Element creation = XPath.path(context, "header", "creationTimestamp");

                if (effectiveDate != null && creation != null) {

                    if (less(toDate(effectiveDate), toDate(creation))) {

                        errorHandler.error("305", context,
                                "The effectiveDate element must represent a date on or after the notification date (i.e. header creation timestamp). Loan Party Profile Notifications can be future-dated, but never backdated.",
                                getDisplayName(), String.format("%s effective date cannot be before the notification date %s", toToken(effectiveDate), toToken(creation)));
                        result = false;

                    }

                }

            }
            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that if multiple instances of settlementDetails are included in the Loan Party
     * Profile Statement, then each settlementDetails must have a distinct currency type.
     * <p>
     * Applies to FpML 5.11 and later.
     *
     * @since TH 0.4.6 (2018-12-20)
     */
    public static final Rule RULE36 = new Rule(R5_11__LATER_CONFIRMATION, "ln-36") {
        /**
         * {@inheritDoc}
         *
         * @since TH 0.4.6 (2018-12-20)
         */
        protected boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanPartyProfileStatement"), errorHandler);
            return (validate(nodeIndex.getElementsByName("loanPartyProfileStatement"), errorHandler));
        }

        private boolean validate(NodeList nodes, ValidationErrorHandler errorHandler) {

            boolean result = true;

            if (nodes != null && nodes.getLength() > 0) {

                Element     context         = (Element) nodes.item(0);
                NodeList    instructionList = XPath.paths(context, "partyProfile", "settlementInstructionDetails");

                if (instructionList != null && instructionList.getLength() != 0) {

                    for (int instructionIndex = 0; instructionIndex < instructionList.getLength(); ++instructionIndex) {

                        Element         instruction             = (Element) instructionList.item(instructionIndex);
                        NodeList        settlementDetailsList   =  XPath.paths(instruction, "settlementDetails");
                        List<String>    includedCcys            = new ArrayList<>();

                        if (settlementDetailsList != null && settlementDetailsList.getLength() >= 1) {

                            for (int settlementDetailsIndex = 0; settlementDetailsIndex < settlementDetailsList.getLength(); ++settlementDetailsIndex) {

                                Element settlementDetails       = (Element) settlementDetailsList.item(settlementDetailsIndex);
                                Element currency                = XPath.path(settlementDetails, "currency");
                                String  ccy                     = toToken(currency);

                                if (includedCcys.contains(ccy)) {
                                    errorHandler.error("305", settlementDetails,
                                            "If multiple instances of settlementDetails are included in the Loan Party Profile Statement, then each settlementDetails must have a distinct currency type.",
                                            getDisplayName(), String.format("settlementDetails currency of %s is repeated", ccy));
                                    result = false;
                                }

                                includedCcys.add(ccy);

                            }

                        }

                    }

                }

            }
            return result;

        }

    };

    /**
     * A <CODE>Rule</CODE> that ensures that the effective date of a loan contract
     * is not after the start date of the interest period.
     * <p>
     * Applies to FpML 4.4 and later.
     *
     * @since TFP 1.2
     */
    public static final Rule RULE01OLD = new Rule(R4_4__R5_10_CONFIRMATION, "ln-1[OLD]") {
        /**
         * {@inheritDoc}
         * @since TFP 1.2
         */
        public boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return (
                        validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "LoanContract"), errorHandler));

            return (
                    validate(nodeIndex.getElementsByName("loanContract"), errorHandler));
        }

        private boolean validate(NodeList list, ValidationErrorHandler errorHandler) {
            boolean result = true;

            for (int index = 0; index < list.getLength(); ++index) {
                Element context = (Element) list.item(index);
                Element start = XPath.path(context, "currentInterestRatePeriod", "startDate");
                Element effective = XPath.path(context, "effectiveDate");

                if ((start == null) || (effective == null) ||
                        greaterOrEqual(toDate(start), toDate(effective))) continue;

                errorHandler.error("305", context,
                        "The effectiveDate must not be after the currentInterestRatePeriod/startDate",
                        getDisplayName(), null);

                result = false;
            }

            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that if the floating rate index contains the string 'PRIME'
     * then the rate fixing date must be equal to the effective date.
     * <p>
     * Applies to FpML 4.4 and later.
     *
     * @since TFP 1.2
     */
    public static final Rule RULE02OLD = new Rule(R4_4__R5_10_CONFIRMATION, "ln-2[OLD]") {
        /**
         * {@inheritDoc}
         * @since TFP 1.2
         */
        public boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return (
                        validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "DrawdownNotice"), errorHandler));

            return (
                    validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "DrawdownNotice"), errorHandler));
        }

        private boolean validate(NodeList list, ValidationErrorHandler errorHandler) {
            boolean result = true;

            for (int index = 0; index < list.getLength(); ++index) {
                Element context = (Element) list.item(index);
                if (exists(XPath.path(context, "loanContract"))) {
                    Element effective = XPath.path(context, "loanContract", "effectiveDate");
                    Element fixingDate = XPath.path(context, "loanContract", "currentInterestRatePeriod", "rateFixingDate");
                    Element rateIndex = XPath.path(context, "loanContract", "currentInterestRatePeriod", "floatingRateIndex");

                    if ((fixingDate != null) && (effective != null) && (DOM.getInnerText(rateIndex).contains("PRIME"))) {
                        if (notEqual(toDate(fixingDate), toDate(effective))) {
                            errorHandler.error("305", context,
                                    "If the floatingRateIndex contains the string 'PRIME' then the currentInterestRatePeriod/rateFixingDate must be the same as the effectiveDate",
                                    getDisplayName(), null);
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
     * <p>
     * Applies to FpML 4.4 and later.
     *
     * @since TFP 1.2
     */
    public static final Rule RULE03OLD = new Rule(R4_4__R5_10_CONFIRMATION, "ln-3[OLD]") {
        /**
         * {@inheritDoc}
         * @since TFP 1.2
         */
        public boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return (
                        validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "InterestRatePeriod"), errorHandler));

            return (
                    validate(nodeIndex.getElementsByName("currentInterestRatePeriod"), errorHandler));
        }

        private boolean validate(NodeList list, ValidationErrorHandler errorHandler) {
            boolean result = true;

            for (int index = 0; index < list.getLength(); ++index) {
                Element context = (Element) list.item(index);
                Element end = XPath.path(context, "endDate");
                Element start = XPath.path(context, "startDate");
                Element fixingDate = XPath.path(context, "rateFixingDate");

                if ((start != null) && (fixingDate != null) && (less(toDate(start), toDate(fixingDate)))) {
                    errorHandler.error("305", context,
                            "The rateFixingDate must not be after the startDate",
                            getDisplayName(), null);
                    result = false;
                }
                if ((end != null) && (start != null) && (less(toDate(end), toDate(start)))) {

                    errorHandler.error("305", context,
                            "The startDate must not be after the endDate",
                            getDisplayName(), null);
                    result = false;
                }
                if ((end != null) && (fixingDate != null) && (less(toDate(end), toDate(fixingDate)))) {

                    errorHandler.error("305", context,
                            "The rateFixingDate must not be after the endDate",
                            getDisplayName(), null);
                    result = false;
                }
            }
            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that if mandatoryCostRate doesn't exist and interestRate and margin and allInRate exist,
     * then allInRate = margin + interestRate
     * <p>
     * Applies to FpML 4.4 and later.
     *
     * @since TFP 1.2
     */
    public static final Rule RULE04OLD = new Rule(R4_4__R5_10_CONFIRMATION, "ln-4[OLD]") {
        /**
         * {@inheritDoc}
         * @since TFP 1.2
         */
        public boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return (
                        validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "InterestRatePeriod"), errorHandler));

            return (
                    validate(nodeIndex.getElementsByName("currentInterestRatePeriod"), errorHandler));
        }

        private boolean validate(NodeList list, ValidationErrorHandler errorHandler) {
            boolean result = true;

            for (int index = 0; index < list.getLength(); ++index) {
                Element context = (Element) list.item(index);
                Element costRate = XPath.path(context, "mandatoryCostRate");
                Element interestRate = XPath.path(context, "interestRate");
                Element margin = XPath.path(context, "margin");
                Element allInRate = XPath.path(context, "allInRate");

                if ((costRate == null) && (interestRate != null) && (margin != null) && (allInRate != null)) {
                    BigDecimal allInRateValue = toDecimal(allInRate);
                    BigDecimal marginValue = toDecimal(margin);
                    BigDecimal interestRateValue = toDecimal(interestRate);
                    BigDecimal marginPlusInterest = marginValue.add(interestRateValue);

                    if (allInRateValue.compareTo(marginPlusInterest) != 0)
                        errorHandler.error("305", context,
                                "The allInRate must be equal to margin + interestRate",
                                getDisplayName(), null);
                    result = false;
                }
            }
            return (result);
        }
    };

    /**
     * A <CODE>Rule</CODE> that ensures that if mandatoryCostRate and interestRate and margin and allInRate exist,
     * then allInRate = margin + interestRate + mandatoryCostRate
     * <p>
     * Applies to FpML 4.4 and later.
     *
     * @since TFP 1.2
     */
    public static final Rule RULE05OLD = new Rule(R4_4__R5_10_CONFIRMATION, "ln-5[OLD]") {
        /**
         * {@inheritDoc}
         * @since TFP 1.2
         */
        public boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return (
                        validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "InterestRatePeriod"), errorHandler));

            return (
                    validate(nodeIndex.getElementsByName("currentInterestRatePeriod"), errorHandler));
        }

        private boolean validate(NodeList list, ValidationErrorHandler errorHandler) {
            boolean result = true;

            for (int index = 0; index < list.getLength(); ++index) {
                Element context = (Element) list.item(index);
                Element costRate = XPath.path(context, "mandatoryCostRate");
                Element interestRate = XPath.path(context, "interestRate");
                Element margin = XPath.path(context, "margin");
                Element allInRate = XPath.path(context, "allInRate");

                if ((costRate != null) && (interestRate != null) && (margin != null) && (allInRate != null)) {

                    BigDecimal allInRateValue = toDecimal(allInRate);
                    BigDecimal marginValue = toDecimal(margin);
                    BigDecimal interestRateValue = toDecimal(interestRate);
                    BigDecimal costRateValue = toDecimal(costRate);
                    BigDecimal marginPlusInterest = marginValue.add(interestRateValue);
                    BigDecimal totalMarginPlusCost = marginPlusInterest.add(costRateValue);

                    if (allInRateValue.compareTo(totalMarginPlusCost) != 0) {
                        errorHandler.error("305", context,
                                "The allInRate must be equal to margin + interestRate + mandatoryCostRate",
                                getDisplayName(), null);
                        result = false;
                    }
                }
            }
            return (result);
        }
    };

    public static final Rule RULE10OLD = new Rule(R4_4__R5_10_CONFIRMATION, "ln-10[OLD]") {
        /**
         * {@inheritDoc}
         * @since TFP 1.2
         */
        public boolean validate(NodeIndex nodeIndex, ValidationErrorHandler errorHandler) {
            if (nodeIndex.hasTypeInformation())
                return (
                        validate(nodeIndex.getElementsByType(determineFpMLNamespace(nodeIndex), "FacilityNotice"), errorHandler));

            return (
                    validate(nodeIndex.getElementsByName("facilityNotice"), errorHandler));
        }

        private boolean validate(NodeList list, ValidationErrorHandler errorHandler) {
            boolean result = true;

            for (int index = 0; index < list.getLength(); ++index) {
                Element context = (Element) list.item(index);
                Element facilityAmount = XPath.path(context, "facilityIdentifier", "originalCommitmentAmount");
                Element loanAmount = XPath.path(context, "facilityCommitmentPosition", "loanContractPosition", "loanContractIdentifier", "originalAmount");

                if ((facilityAmount != null) && (loanAmount != null) && (isSameCurrency(facilityAmount, loanAmount))) {
                    Element originalCommitment = XPath.path(facilityAmount, "amount");
                    Element originalAmount = XPath.path(loanAmount, "amount");

                    if (less(toDecimal(originalCommitment), toDecimal(originalAmount))) {
                        errorHandler.error("305", context,
                                "The facilityIdentifier/originalCommitmentAmount/amount must be greater than or equal to the facilityCommitmentPosition/loanContractPosition/loanContractIdentifier/originalAmount/amount",
                                getDisplayName(), null);
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