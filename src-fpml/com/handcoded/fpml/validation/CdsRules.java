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

import java.math.BigDecimal;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
import com.handcoded.xml.XPath;

/**
 * The <CODE>CdsRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for credit derivative
 * products.
 *
 * @author	BitWise
 * @version	$Id: CdsRules.java 739 2013-06-13 17:14:02Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class CdsRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects documents containing
	 * at least one credit product.
	 * @since	TFP 1.6
	 */
	private static final Precondition	CDS_PRODUCT
		= new ContentPrecondition (
				new String [] { "creditDefaultSwap", "creditDefaultSwapOption" },
				new String [] { "CreditDefaultSwap", "CreditDefaultSwapOption" });
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4.0 or later
	 * documents containing at least one credit product.
	 * @since	TFP 1.6
	 */
	private static final Precondition	R4_0__LATER
		= Precondition.and (new Precondition [] {
				CDS_PRODUCT,
				Preconditions.R4_0__LATER,
				Preconditions.CONFIRMATION });
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4.x
	 * documents containing at least one credit product.
	 * @since	TFP 1.6
	 */
	private static final Precondition	R4_0__R4_X
		= Precondition.and (new Precondition [] {
				CDS_PRODUCT,
				Preconditions.R4_0__R4_X,
				Preconditions.CONFIRMATION });
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4.2 or later
	 * documents containing at least one credit product.
	 * @since	TFP 1.6
	 */
	private static final Precondition	R4_2__LATER
		= Precondition.and (new Precondition [] {
				CDS_PRODUCT,
				Preconditions.R4_2__LATER,
				Preconditions.CONFIRMATION });

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4.3 or later
	 * documents containing at least one credit product.
	 * @since	TFP 1.6
	 */
	private static final Precondition	R4_3__LATER
		= Precondition.and (new Precondition [] {
				CDS_PRODUCT,
				Preconditions.R4_3__LATER,
				Preconditions.CONFIRMATION });

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5.0 or later
	 * documents containing at least one credit product.
	 * @since	TFP 1.6
	 */
	private static final Precondition	R5_0__LATER
		= Precondition.and (new Precondition [] {
				CDS_PRODUCT,
				Preconditions.R5_0__LATER,
				Preconditions.CONFIRMATION });
	
	/**
	 * A <CODE>Rule</CODE> that ensures <CODE>tradeHeader/tradeDate</CODE> is before
	 * <CODE>creditDefaultSwap/generalTerms/effectiveDate/unadjustedDate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE01
		= new Rule (R4_0__LATER, "cd-1")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		cds	= XPath.path (context, "creditDefaultSwap");

					if ((cds == null) || !isSingleName (cds)) continue;

					try {
						Element tradeDate		= XPath.path (context, "tradeHeader", "tradeDate");
						Element effectiveDate	= XPath.path (cds, "generalTerms", "effectiveDate", "unadjustedDate");

						if ((tradeDate == null) || (effectiveDate == null) || less (toDate (tradeDate), toDate (effectiveDate))) continue;

						errorHandler.error ("305", context,
								"The tradeHeader/tradeDate must be before creditDefaultSwap/generalTerms/effectiveDate/unadjustedDate",
								getDisplayName (), null);
						result = false;
					}
					catch (IllegalArgumentException error) {
						// Syntax errors handled elsewhere
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures <CODE>tradeHeader/tradeDate</CODE> is before
	 * <CODE>creditDefaultSwap/generalTerms/effectiveDate/unadjustedDate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 * @deprecated
	 */
	public static final Rule	RULE01B
		= new Rule (R4_0__LATER, "cd-1b")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		cds		= XPath.path (context, "creditDefaultSwap");

					if ((cds == null) || !isCreditIndex (cds)) continue;

					try {
						Element tradeDate		= XPath.path (context, "tradeHeader", "tradeDate");
						Element effectiveDate	= XPath.path (cds, "generalTerms", "effectiveDate", "unadjustedDate");

						if ((tradeDate == null) || (effectiveDate == null) || !less (toDate (tradeDate), toDate (effectiveDate))) continue;

						errorHandler.error ("305", context,
								"The tradeHeader/tradeDate must not be before creditDefaultSwap/generalTerms/effectiveDate/unadjustedDate",
								getDisplayName (), null);
						result = false;
					}
					catch (IllegalArgumentException error) {
						// Syntax errors handled elsewhere
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>calculationAgent</CODE> is present,
	 * it may contain only <CODE>calculationAgentPartyReference</CODE> elements.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE02
		= new Rule (R4_0__LATER, "cd-2")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					Element		calculationAgent
						= DOM.getElementByLocalName (context, "calculationAgent");

					if (calculationAgent != null) {
						boolean		failed = false;

						for (Node node = calculationAgent.getFirstChild (); node != null; node = node.getNextSibling ()) {
							if (node.getNodeType () == Node.ELEMENT_NODE) {
								if (node.getLocalName ().equals ("calculationAgentPartyReference"))
									continue;

								if (node.getLocalName ().equals ("calculationAgentParty") &&
									DOM.getInnerText ((Element) node).trim ().equals ("AsSpecifiedInMasterAgreement"))
									continue;

								failed = true;
							}
						}

						if (failed) {
							errorHandler.error ("305", context,
									"The calculationAgent element may only contain calculationAgentPartyReferences " +
									"or a calculationAgentParty with the value 'AsSpecifiedInMasterAgreement",
									getDisplayName (), null);
							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures contracts referencing ISDA 1999 definitions
	 * do not use ISDA 2003 supplements.
	 * <P>
	 * Applies to FpML 4.0 and 4.1.
	 * @since	TFP 1.0
	 * @deprecated
	 */
	public static final Rule	RULE03
		= new Rule (R4_0__LATER, "cd-3")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (!exists (XPath.path (context, "creditDefaultSwap"))) continue;
					if (!isIsda1999 (context)) continue;

					NodeList	supplements
						= XPath.paths (context, "documentation", "contractualSupplement");

					for (int count = 0; count < supplements.getLength(); ++count) {
						Element		supplement = (Element) supplements.item (count);

						if (toToken (supplement).startsWith ("ISDA2003Credit")) {
							errorHandler.error ("305", supplement,
								"The contractualSupplement name may not begin with ISDA2003Credit",
								getDisplayName (), DOM.getInnerText (supplement));
							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures contracts referencing ISDA 1999 definitions
	 * do not use ISDA 2003 supplements.
	 * <P>
	 * Applies to FpML 4.2 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE03B
		= new Rule (R4_2__LATER, "cd-3b")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (!exists (XPath.path (context, "creditDefaultSwap"))) continue;
					if (!isIsda1999 (context)) continue;

					NodeList	types
						= XPath.paths (context, "documentation", "contractualTermsSupplement", "type");

					for (int count = 0; count < types.getLength (); ++count) {
						Element 	type = (Element) types.item (count);

						if (toToken (type).startsWith ("ISDA2003Credit")) {
							errorHandler.error ("305", type,
								"The contractualTermsSupplement/type may not begin with ISDA2003Credit",
								getDisplayName (), toToken (type));
							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures contracts referencing ISDA 2003 definitions
	 * do not use ISDA 1999 supplements.
	 * <P>
	 * Applies to FpML 4.0 and 4.1.
	 * @since	TFP 1.0
	 * @deprecated
	 */
	public static final Rule	RULE04
		= new Rule (R4_0__LATER, "cd-4")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (!exists (XPath.path (context, "creditDefaultSwap"))) continue;
					if (!isIsda2003 (context)) continue;

					NodeList	supplements
						= XPath.paths (context, "documentation", "contractualSupplement");

					for (int count = 0; count < supplements.getLength (); ++count) {
						Element		supplement = (Element) supplements.item (count);

						if (DOM.getInnerText (supplement).startsWith ("ISDA1999Credit")) {
							errorHandler.error ("305", supplement,
								"The contractualSupplement name may not begin with ISDA1999Credit",
								getDisplayName (), DOM.getInnerText (supplement));
							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures contracts referencing ISDA 2003 definitions
	 * do not use ISDA 1999 supplements.
	 * <P>
	 * Applies to FpML 4.2 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE04B
		= new Rule (R4_2__LATER, "cd-4b")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (!exists (XPath.path (context, "creditDefaultSwap"))) continue;
					if (!isIsda2003 (context)) continue;

					NodeList	types
						= XPath.paths (context, "documentation", "contractualTermsSupplement", "type");

					for (int count = 0; count < types.getLength (); ++count) {
						Element		type = (Element) types.item (count);

						if (toToken (type).startsWith ("ISDA1999Credit")) {
							errorHandler.error ("305", type,
								"The contractualTermSupplement/type name may not begin with ISDA1999Credit",
								getDisplayName (), DOM.getInnerText (type));
							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>scheduledTerminationDate/adjustableDate</CODE>
	 * exists, then <CODE>effectiveDate/unadjustedDate</CODE> &lt;
	 * <CODE>scheduledTerminationDate/adjustableDate/unadjustedDate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 thru 4.x.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE05_OLD
		= new Rule (R4_0__R4_X, "cd-5[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "GeneralTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("generalTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					Element		termination
						= DOM.getElementByLocalName (context, "scheduledTerminationDate", "adjustableDate");

					if (termination != null) {
						try {
							Date effective	= Date.parse (DOM.getInnerText (DOM.getElementByLocalName (context, "effectiveDate", "unadjustedDate")));
							Date adjustable	= Date.parse (DOM.getInnerText (DOM.getElementByLocalName (termination, "unadjustedDate")));

							if (effective.compareTo (adjustable) >= 0) {
								errorHandler.error ("305", context,
										"The scheduled termination date must be later than the effective date",
										getDisplayName (), null);
								result = false;
							}
						}
						catch (IllegalArgumentException error) {
							// Syntax errors handled elsewhere.
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>scheduledTerminationDate/adjustableDate</CODE>
	 * exists, then <CODE>effectiveDate/unadjustedDate</CODE> &lt;
	 * <CODE>scheduledTerminationDate/adjustableDate/unadjustedDate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE05
		= new Rule (R5_0__LATER, "cd-5")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "GeneralTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("generalTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					Element		effectiveDate = XPath.path (context, "effectiveDate", "unadjustedDate");
					Element		terminationDate = XPath.path (context, "scheduledTerminationDate", "unadjustedDate");
				
					if ((effectiveDate == null) || (terminationDate == null) ||
							less (toDate (effectiveDate), toDate (terminationDate)))
						continue;
					
					errorHandler.error ("305", context,
							"The scheduled termination date must be later than the effective date",
							getDisplayName (), null);
					
					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures <CODE>buyerPartyReference/@href</CODE> and
	 * <CODE>sellerPartyReference/@href</CODE> are different.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE06
		= new Rule (R4_0__LATER, "cd-6")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "GeneralTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("generalTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					Element		buyer
						= DOM.getElementByLocalName (context, "buyerPartyReference");

					Element		seller
						= DOM.getElementByLocalName (context, "sellerPartyReference");

					if ((buyer == null) || (seller == null)) continue;

					if (buyer.getAttribute ("href").equals (seller.getAttribute ("href"))) {
						errorHandler.error ("305", context,
							"The buyer and seller party references must be different",
							getDisplayName (), null);
						result = false;
					}

				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures long form contracts contain effective
	 * date adjustments.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE07
		= new Rule (R4_0__LATER, "cd-7")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "GeneralTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("generalTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (!isLongForm (DOM.getGrandParent (context)))
						continue;

					Element		effective
						= DOM.getElementByLocalName (context, "effectiveDate");
					Element		def
						= DOM.getElementByLocalName (effective, "dateAdjustments");
					Element		ref
						= DOM.getElementByLocalName (effective, "dateAdjustmentsReference");

					if (((def == null) && (ref == null)) || ((def != null) && (ref != null))) {
						errorHandler.error ("305", context,
							"A date adjustment for the effective date must either be specified or referenced",
							getDisplayName (), null);
						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures long form contracts contain termination
	 * date adjustments.
	 * <P>
	 * Applies to FpML 4.x.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE08_OLD
		= new Rule (R4_0__R4_X, "cd-8[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "GeneralTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("generalTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (!isLongForm (DOM.getGrandParent (context)))
						continue;

					if (!exists (XPath.path (context, "scheduledTerminationDate"))) continue;

					Element		def
						= XPath.path (context, "scheduledTerminationDate", "adjustableDate", "dateAdjustments");
					Element		ref
						= XPath.path (context, "scheduledTerminationDate", "adjustableDate", "dateAdjustmentsReference");

					if (((def == null) && (ref == null)) || ((def != null) && (ref != null))) {
						errorHandler.error ("305", context,
							"A date adjustment for the scheduled termination date must either be specified or referenced",
							getDisplayName (), null);
						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures long form contracts contain termination
	 * date adjustments.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE08
		= new Rule (R5_0__LATER, "cd-8")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "GeneralTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("generalTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (!isLongForm (DOM.getGrandParent (context)))
						continue;

					if (!exists (XPath.path (context, "scheduledTerminationDate"))) continue;

					Element		def
						= XPath.path (context, "scheduledTerminationDate", "dateAdjustments");
					Element		ref
						= XPath.path (context, "scheduledTerminationDate", "dateAdjustmentsReference");

					if (((def == null) && (ref == null)) || ((def != null) && (ref != null))) {
						errorHandler.error ("305", context,
							"A date adjustment for the scheduled termination date must either be specified or referenced",
							getDisplayName (), null);
						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>referenceObligation/primaryObligorReference</CODE>
	 * exists, then it must reference the <CODE>referenceEntity</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE09
		= new Rule (R4_0__LATER, "cd-9")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ReferenceInformation"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("referenceInformation"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					NodeList obligations = context.getElementsByTagName ("referenceObligation");
					for (int count = 0; count < obligations.getLength (); ++count) {
						Element		temp = (Element) obligations.item (count);
						Element		ref  = DOM.getElementByLocalName (temp, "primaryObligorReference");

						if (ref != null) {
							Element		entity = DOM.getElementByLocalName (context, "referenceEntity");

							if (!ref.getAttribute ("href").equals (entity.getAttribute ("id"))) {
								errorHandler.error ("305", context,
									"The primaryObligorReference must refer to the referenceEntity",
									getDisplayName (), ref.getAttribute ("href"));
								result = false;
							}
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>referenceObligation/guarantorReference</CODE>
	 * exists, then it must reference the <CODE>referenceEntity</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE10
		= new Rule (R4_0__LATER, "cd-10")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ReferenceInformation"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("referenceInformation"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					NodeList obligations = context.getElementsByTagName ("referenceObligation");
					for (int count = 0; count < obligations.getLength (); ++count) {
						Element		temp = (Element) obligations.item (count);
						Element		ref  = DOM.getElementByLocalName (temp, "guarantorReference");

						if (ref != null) {
							Element		entity = DOM.getElementByLocalName (context, "referenceEntity");

							if (!ref.getAttribute ("href").equals (entity.getAttribute ("id"))) {
								errorHandler.error ("305", context,
									"The primaryObligorReference must refer to the referenceEntity",
									getDisplayName (), ref.getAttribute ("href"));
								result = false;
							}
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures ISDA 2003 long form contracts contain
	 * <CODE>allGuarantees</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE11
		= new Rule (R4_0__LATER, "cd-11")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ReferenceInformation"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("referenceInformation"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		trade 	= DOM.getGreatGrandParent (context);

					if (isLongForm (trade) && isIsda2003 (trade)) {
						Element 	guarantees = DOM.getElementByLocalName (context, "allGuarantees");

						if (guarantees == null) {
							errorHandler.error ("305", context,
								"The allGuarantees element must be present",
								getDisplayName (), null);
							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>referencePrice</CODE> is present
	 * then its is not negative.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE12
		= new Rule (R4_0__LATER, "cd-12")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ReferenceInformation"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("referenceInformation"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					Element		price = DOM.getElementByLocalName (context, "referencePrice");

					if (price != null) {
						String		priceValue = DOM.getInnerText (price);

						if (Double.parseDouble (priceValue) < 0.0) {
							errorHandler.error ("305", context,
								"The reference price must be greater than or equal to zero",
								getDisplayName (), priceValue);
							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>protectionTerms/creditEvents/creditEventNotice/notifyingParty/buyerPartyReference</CODE>
	 * is present, its @href attribute must match that of <CODE>generalTerms/buyerPartyReference</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE13
		= new Rule (R4_0__LATER, "cd-13")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	buyers	= XPath.paths (context, "protectionTerms", "creditEvents", "creditEventNotice", "notifyingParty", "buyerPartyReference");
					for (int count = 0; count < buyers.getLength (); ++count) {
						Element		buyer = (Element) buyers.item (count);
						String		buyerName;
						String		referenceName;

						if (equal (
								buyerName = buyer.getAttribute ("href"),
								referenceName = XPath.path (context, "generalTerms", "buyerPartyReference").getAttribute ("href")))
							continue;

						errorHandler.error ("305", context,
							"Credit event notice references buyer party reference " + buyerName +
							" but general terms references " + referenceName,
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>protectionTerms/creditEvents/creditEventNotice/notifyingParty/sellerPartyReference</CODE>
	 * is present, its @href attribute must match that of <CODE>generalTerms/sellerPartyReference</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE14
		= new Rule (R4_0__LATER, "cd-14")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	sellers =  XPath.paths (context, "protectionTerms", "creditEvents", "creditEventNotice", "notifyingParty", "sellerPartyReference");
					for (int count = 0; count < sellers.getLength (); ++count) {
						Element		seller = (Element) sellers.item (count);
						if (equal (seller.getAttribute ("href"),
								XPath.path (context, "generalTerms", "sellerPartyReference").getAttribute ("href")))
							continue;

						errorHandler.error ("305", context,
							"If protectionTerms/creditEvents/creditEventNotice/notifyingParty/sellerPartyReference " +
							"is present, its @href attribute must match that of generalTerms/sellerPartyReference",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the valuation method is valid when
	 * there is one obligation and one valuation date.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE15
		= new Rule (R4_0__LATER, "cd-15")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
				{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (implies (
							and (
								equal (
									count (XPath.paths (context, "generalTerms", "referenceInformation", "referenceObligation")), 1),
								exists (XPath.path (context, "cashSettlementTerms", "valuationDate", "singleValuationDate"))),
							or (
								equal (
									XPath.path (context, "cashSettlementTerms", "valuationMethod"),
									"Market"),
								equal (
									XPath.path (context, "cashSettlementTerms", "valuationMethod"),
									"Highest"))))
						continue;

					errorHandler.error ("305", context,
						"If there is exactly one generalTerms/referenceInformation/referenceObligation " +
						"and cashSettlementTerms/valuationDate/singleValuationDate occurs " +
						"then the value of cashSettlementTerms/valuationMethod must be " +
						"Market or Highest",
						getDisplayName (), DOM.getInnerText (XPath.path (context, "cashSettlementTerms", "valuationMethod")));

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the valuation method is valid when
	 * there is one obligation and multiple valuation dates.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE16
		= new Rule (R4_0__LATER, "cd-16")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (implies (
							and (
								equal (
									count (XPath.paths (context, "generalTerms", "referenceInformation", "referenceObligation")), 1),
								exists (XPath.path (context, "cashSettlementTerms", "valuationDate", "multipleValuationDates"))),
							or (
								equal (
									XPath.path (context, "cashSettlementTerms", "valuationMethod"),
									"AverageMarket"),
								or (
									equal (
										XPath.path (context, "cashSettlementTerms", "valuationMethod"),
										"Highest"),
									equal (
										XPath.path (context, "cashSettlementTerms", "valuationMethod"),
										"AverageHighest")))))
						continue;

					errorHandler.error ("305", context,
						"If there is exactly one generalTerms/referenceInformation/referenceObligation " +
						"and cashSettlementTerms/valuationDate/multipleValuationDates occurs " +
						"then the value of cashSettlementTerms/valuationMethod must be " +
						"AverageMarket, Highest or AverageHighest",
						getDisplayName (), DOM.getInnerText (XPath.path (context, "cashSettlementTerms", "valuationMethod")));

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the valuation method is valid when
	 * there are multiple obligations and one valuation date.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE17
		= new Rule (R4_0__LATER, "cd-17")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (implies (
							and (
								greater (
									count (XPath.paths (context, "generalTerms", "referenceInformation", "referenceObligation")), 1),
								exists (XPath.path (context, "cashSettlementTerms", "valuationDate", "singleValuationDate"))),
							or (
								equal (
									XPath.path (context, "cashSettlementTerms", "valuationMethod"),
									"BlendedMarket"),
								equal (
									XPath.path (context, "cashSettlementTerms", "valuationMethod"),
									"BlendedHighest"))))
						continue;

					errorHandler.error ("305", context,
						"If there are more that one generalTerms/referenceInformation/referenceObligation " +
						"and cashSettlementTerms/valuationDate/singleValuationDate occurs " +
						"then the value of cashSettlementTerms/valuationMethod must be " +
						"BlendedMarket or BlendedHighest",
						getDisplayName (), DOM.getInnerText (XPath.path (context, "cashSettlementTerms", "valuationMethod")));

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the valuation method is valid when
	 * there are multiple obligations and multiple valuation dates.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE18
		= new Rule (R4_0__LATER, "cd-18")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (implies (
							and (
								greater (
									count (XPath.paths (context, "generalTerms", "referenceInformation", "referenceObligation")), 1),
								exists (XPath.path (context, "cashSettlementTerms", "valuationDate", "multipleValuationDates"))),
							or (
								equal (
									XPath.path (context, "cashSettlementTerms", "valuationMethod"),
									"AverageBlendedMarket"),
								equal (
									XPath.path (context, "cashSettlementTerms", "valuationMethod"),
									"AverageBlendedHighest"))))
						continue;

					errorHandler.error ("305", context,
						"If there are more than one generalTerms/referenceInformation/referenceObligation " +
						"and cashSettlementTerms/valuationDate/multipleValuationDates occurs " +
						"then the value of cashSettlementTerms/valuationMethod must be " +
						"AverageBlendedMarket or AverageBlendedHighest",
						getDisplayName (), DOM.getInnerText (XPath.path (context, "cashSettlementTerms", "valuationMethod")));

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures elements related to ISDA 2003 contracts
	 * are not present in ISDA 1999 contracts.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE19
		= new Rule (R4_0__LATER, "cd-19")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isIsda1999 (trade)) {
						Element	context = XPath.path (trade, "creditDefaultSwap");

						result &=
							  validate (context, XPath.path (context, "protectionTerms", "creditEvents", "creditEventNotice", "businessCenter"), errorHandler)
							& validate (context, XPath.path (context, "protectionTerms", "creditEvents", "restructuring", "multipleHolderObligation"), errorHandler)
							& validate (context, XPath.path (context, "protectionTerms", "creditEvents", "restructuring", "multipleCreditEventNotices"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "referenceInformation", "allGuarantees"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "indexReferenceInformation"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "substitution"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "modifiedEquityDelivery"), errorHandler);
					}
				}
				return (result);
			}

			private boolean validate (final Element context, final Element illegal, ValidationErrorHandler errorHandler)
			{
				if (illegal != null) {
					errorHandler.error ("305", context,
						"Illegal element found in ISDA 1999 credit default swap",
						getDisplayName (), XPath.forNode (illegal));

					return (false);
				}
				return (true);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures elements related to ISDA 1999 contracts
	 * are not present in ISDA 2003 contracts.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE20
		= new Rule (R4_0__LATER, "cd-20")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
					boolean		result 	= true;

					for (int index = 0; index < list.getLength (); ++index) {
						Element		trade = (Element) list.item (index);

						if (isIsda2003 (trade)) {
							Element	context = XPath.path (trade, "creditDefaultSwap");

							if (!exists (XPath.path (context, "protectionTerms", "obligations", "notContingent")))
								continue;

							errorHandler.error ("305", context,
								"Illegal element found in ISDA 2003 credit default swap",
								getDisplayName (), "notContingent");

							result = false;
						}
					}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures a short form contract does not
	 * contain invalid elements.
	 * <P>
	 * Applies to FpML 4.*.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE21_OLD
		= new Rule (R4_0__R4_X, "cd-21[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isShortForm (trade)) {
						Element context = XPath.path (trade, "creditDefaultSwap");

						if ((context == null) || !isSingleName (context)) continue;

						result &=
							  validate (context, XPath.path (context, "cashSettlementTerms", "settlementCurrency"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "valuationDate"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "valuationTime"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "quotationMethod"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "quotationAmount"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "minimumQuotationAmount"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "dealer"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "cashSettlementBusinessDays"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "accruedInterest"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "valuationMethod"), errorHandler)
							& validate (context, XPath.path (context, "physicalSettlementTerms"), errorHandler)
							& validate (context, XPath.path (context, "feeLeg", "periodicPayment", "fixedAmountCalculation", "calculationAmount"), errorHandler)
							& validate (context, XPath.path (context, "feeLeg", "periodicPayment", "fixedAmountCalculation", "dayCountFraction"), errorHandler)
							& validate (context, XPath.path (context, "protectionTerms", "obligations"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "referenceInformation", "allGuarantees"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "referenceInformation", "referencePrice"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "effectiveDate", "dateAdjustments"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "effectiveDate", "dateAdjustmentsReference"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "scheduledTerminationDate", "adjustableDate", "dateAdjustments"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "scheduledTerminationDate", "adjustableDate", "dateAdjustmentsReference"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "dateAdjustments"), errorHandler);
					}
				}
				return (result);
			}

			private boolean validate (final Element context, final Element illegal, ValidationErrorHandler errorHandler)
			{
				if (illegal != null) {
					errorHandler.error ("305", context,
						"Illegal element found in short form credit default swap",
						getDisplayName (), XPath.forNode (illegal));

					return (false);
				}
				return (true);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures a short form contract does not
	 * contain invalid elements.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE21
		= new Rule (R5_0__LATER, "cd-21")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isShortForm (trade)) {
						Element context = XPath.path (trade, "creditDefaultSwap");

						if ((context == null) || !isSingleName (context)) continue;

						result &=
							  validate (context, XPath.path (context, "cashSettlementTerms", "settlementCurrency"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "valuationDate"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "valuationTime"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "quotationMethod"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "quotationAmount"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "minimumQuotationAmount"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "dealer"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "cashSettlementBusinessDays"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "accruedInterest"), errorHandler)
							& validate (context, XPath.path (context, "cashSettlementTerms", "valuationMethod"), errorHandler)
							& validate (context, XPath.path (context, "physicalSettlementTerms"), errorHandler)
							& validate (context, XPath.path (context, "feeLeg", "periodicPayment", "fixedAmountCalculation", "calculationAmount"), errorHandler)
							& validate (context, XPath.path (context, "feeLeg", "periodicPayment", "fixedAmountCalculation", "dayCountFraction"), errorHandler)
							& validate (context, XPath.path (context, "protectionTerms", "obligations"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "referenceInformation", "allGuarantees"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "referenceInformation", "referencePrice"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "effectiveDate", "dateAdjustments"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "effectiveDate", "dateAdjustmentsReference"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "scheduledTerminationDate", "dateAdjustments"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "scheduledTerminationDate", "dateAdjustmentsReference"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "dateAdjustments"), errorHandler);
					}
				}
				return (result);
			}

			private boolean validate (final Element context, final Element illegal, ValidationErrorHandler errorHandler)
			{
				if (illegal != null) {
					errorHandler.error ("305", context,
						"Illegal element found in short form credit default swap",
						getDisplayName (), XPath.forNode (illegal));

					return (false);
				}
				return (true);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures a short form contract does not
	 * contain invalid elements.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE21B_OLD
		= new Rule (R4_0__R4_X, "cd-21b[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isShortForm (trade)) {
						Element context = XPath.path (trade, "creditDefaultSwap");

						if (!isCreditIndex (context)) continue;

						result &=
							  validate (context, XPath.path (context, "cashSettlementTerms"), errorHandler)
							& validate (context, XPath.path (context, "physicalSettlementTerms"), errorHandler)
							& validate (context, XPath.path (context, "feeLeg", "periodicPayment", "fixedAmountCalculation", "calculationAmount"), errorHandler)
							& validate (context, XPath.path (context, "feeLeg", "periodicPayment", "fixedAmountCalculation", "dayCountFraction"), errorHandler)
							& validate (context, XPath.path (context, "protectionTerms", "obligations"), errorHandler)
							& validate (context, XPath.path (context, "protectionTerms", "creditEvents"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "effectiveDate", "dateAdjustments"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "effectiveDate", "dateAdjustmentsReference"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "scheduledTerminationDate", "adjustableDate", "dateAdjustments"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "scheduledTerminationDate", "adjustableDate", "dateAdjustmentsReference"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "dateAdjustments"), errorHandler);
					}
				}
				return (result);
			}

			private boolean validate (final Element context, final Element illegal, ValidationErrorHandler errorHandler)
			{
				if (illegal != null) {
					errorHandler.error ("305", context,
						"Illegal element found in short form credit default swap",
						getDisplayName (), XPath.forNode (illegal));

					return (false);
				}
				return (true);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures a short form contract does not
	 * contain invalid elements.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE21B
		= new Rule (R5_0__LATER, "cd-21b")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isShortForm (trade)) {
						Element context = XPath.path (trade, "creditDefaultSwap");

						if (!isCreditIndex (context)) continue;

						result &=
							  validate (context, XPath.path (context, "cashSettlementTerms"), errorHandler)
							& validate (context, XPath.path (context, "physicalSettlementTerms"), errorHandler)
							& validate (context, XPath.path (context, "feeLeg", "periodicPayment", "fixedAmountCalculation", "calculationAmount"), errorHandler)
							& validate (context, XPath.path (context, "feeLeg", "periodicPayment", "fixedAmountCalculation", "dayCountFraction"), errorHandler)
							& validate (context, XPath.path (context, "protectionTerms", "obligations"), errorHandler)
							& validate (context, XPath.path (context, "protectionTerms", "creditEvents"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "effectiveDate", "dateAdjustments"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "effectiveDate", "dateAdjustmentsReference"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "scheduledTerminationDate", "dateAdjustments"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "scheduledTerminationDate", "dateAdjustmentsReference"), errorHandler)
							& validate (context, XPath.path (context, "generalTerms", "dateAdjustments"), errorHandler);
					}
				}
				return (result);
			}

			private boolean validate (final Element context, final Element illegal, ValidationErrorHandler errorHandler)
			{
				if (illegal != null) {
					errorHandler.error ("305", context,
						"Illegal element found in short form credit default swap",
						getDisplayName (), XPath.forNode (illegal));

					return (false);
				}
				return (true);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures short form contracts can only contain
	 * restructuring events.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE22
		= new Rule (R4_0__LATER, "cd-22")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isShortForm (trade)) {
						Element	context = XPath.path (trade, "creditDefaultSwap");
						Element	events	= XPath.path (context, "protectionTerms", "creditEvents");

						if (events != null) {
							NodeList	children = events.getChildNodes ();
							for (int count = 0; count < children.getLength (); ++count) {
								Node	node = children.item (count);
								if (node instanceof Element) {
									String localName = node.getLocalName();

									if (localName.equals ("bankruptcy") ||
										localName.equals ("failureToPay") ||
										localName.equals ("repudiationMoratorium") ||
										localName.equals ("obligationDefault") ||
										localName.equals ("obligationAcceleration")) {
										errorHandler.error ("305", context,
											"Illegal element found in short form credit default swap",
											getDisplayName (), localName);

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
	 * A <CODE>Rule</CODE> that ensures long form contracts specify the
	 * settlement terms.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE23
		= new Rule (R4_0__LATER, "cd-23")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isLongForm (trade)) {
						Element	context = XPath.path (trade, "creditDefaultSwap");

						if (or (
							exists (XPath.path (context, "cashSettlementTerms")),
							exists (XPath.path (context, "physicalSettlementTerms"))))
							continue;

						errorHandler.error ("305", context,
							"Neither cash nor physical settlement terms are present",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures long form contracts contain mandatory
	 * data values.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE24
		= new Rule (R4_0__LATER, "cd-24")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isLongForm (trade)) {
						Element	context = XPath.path (trade, "creditDefaultSwap");

						if (!exists (XPath.path (context, "protectionTerms", "creditEvents", "creditEventNotice"))) {
							errorHandler.error ("305", context,
								"Long Form credit default swap is missing a mandatory element",
								getDisplayName (), "protectionTerms/creditEvents/creditEventNotices");

							result = false;
						}

						if (!exists (XPath.path (context, "protectionTerms", "obligations"))) {
							errorHandler.error ("305", context,
								"Long Form credit default swap is missing a mandatory element",
								getDisplayName (), "protectionTerms/obligations");

							result = false;
						}

						if (!exists (XPath.path (context, "generalTerms", "referenceInformation", "referencePrice"))) {
							errorHandler.error ("305", context,
								"Long Form credit default swap is missing a mandatory element",
								getDisplayName (), "generalTerms/referenceInformation/referencePrice");

							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures long form contracts with physical
	 * settlement contain the necessary data.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE25
		= new Rule (R4_0__LATER, "cd-25")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isLongForm (trade)) {
						Element	context = XPath.path (trade, "creditDefaultSwap");

						if (exists (XPath.path (context, "physicalSettlementTerms"))) {
							if (!exists (XPath.path (context, "physicalSettlementTerms", "settlementCurrency"))) {
								errorHandler.error ("305", context,
									"A mandatory element for physical settlement is missing",
									getDisplayName (), "physicalSettlementTerms/settlementCurrency");

								result = false;
							}

							if (!exists (XPath.path (context, "physicalSettlementTerms", "physicalSettlementPeriod"))) {
								errorHandler.error ("305", context,
									"A mandatory element for physical settlement is missing",
									getDisplayName (), "physicalSettlementTerms/physicalSettlementPeriod");

								result = false;
							}

							if (!exists (XPath.path (context, "physicalSettlementTerms", "escrow"))) {
								errorHandler.error ("305", context,
									"A mandatory element for physical settlement is missing",
									getDisplayName (), "physicalSettlementTerms/escrow");

								result = false;
							}

							if (!exists (XPath.path (context, "physicalSettlementTerms", "deliverableObligations", "accruedInterest"))) {
								errorHandler.error ("305", context,
									"A mandatory element for physical settlement is missing",
									getDisplayName (), "physicalSettlementTerms/deliverableObligations/accruedInterest");

								result = false;
							}
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>feeLeg/singlePayment/adjustablePaymentDate</CODE>
	 * is present then <CODE>feeLeg/singlePayment/adjustablePaymentDate</CODE> &gt;
	 * <CODE>generalTerms/effectiveDate/unadjustedDate</CODE>.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE26
		= new Rule (R4_0__LATER, "cd-26")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		paymentDate;
					Element		effectiveDate;

					if (implies (
							exists (XPath.path (context, "feeLeg", "singlePayment", "adjustablePaymentDate")),
							greater (
								paymentDate   = XPath.path (context, "feeLeg", "singlePayment", "adjustablePaymentDate"),
								effectiveDate = XPath.path (context, "generalTerms", "effectiveDate", "unadjustedDate"))))
						continue;

					errorHandler.error ("305", context,
						"Single payment date " + DOM.getInnerText (paymentDate) + " must be " +
						"after the effective date " + DOM.getInnerText (effectiveDate),
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if a payment date is defined it falls
	 * before the termination date.
	 * <P>
	 * Applies to FpML 4.0 until 4.x.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE27_OLD
		= new Rule (R4_0__R4_X, "cd-27[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		feeDate;
					Element		termDate;

					if (and (
						exists (feeDate = XPath.path (context, "feeLeg", "singlePayment", "adjustablePaymentDate")),
						exists (termDate = XPath.path (context, "generalTerms", "scheduledTerminationDate", "adjustableDate", "unadjustedDate")))) {
						if (less (toDate (feeDate), toDate (termDate))) continue;

						errorHandler.error ("305", context,
							"Single payment date '" + DOM.getInnerText (feeDate) + "' must be " +
							"before scheduled termination date '" + DOM.getInnerText(termDate) + "'",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if a payment date is defined it falls
	 * before the termination date.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule	RULE27
		= new Rule (R5_0__LATER, "cd-27")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		feeDate;
					Element		termDate;

					if (and (
						exists (feeDate = XPath.path (context, "feeLeg", "singlePayment", "adjustablePaymentDate")),
						exists (termDate = XPath.path (context, "generalTerms", "scheduledTerminationDate", "unadjustedDate")))) {
						if (less (toDate (feeDate), toDate (termDate))) continue;

						errorHandler.error ("305", context,
							"Single payment date '" + DOM.getInnerText (feeDate) + "' must be " +
							"before scheduled termination date '" + DOM.getInnerText(termDate) + "'",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if a first payment date is present it
	 * falls after the effective date.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE28
		= new Rule (R4_0__LATER, "cd-28")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context 		= (Element) list.item (index);
					Element		paymentDate 	= XPath.path (context, "feeLeg", "periodicPayment", "firstPaymentDate");
					Element		effectiveDate	= XPath.path (context, "generalTerms", "effectiveDate", "unadjustedDate");

					if ((paymentDate == null) || (effectiveDate == null)
						|| greater (paymentDate, effectiveDate))
						continue;

					errorHandler.error ("305", context,
						"First periodic payment date '" + toToken (paymentDate) + "' " +
						"must be after the effective date '" + toToken (effectiveDate) + "'",
						getDisplayName (), null);

					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if a first payment date is present it
	 * falls before the termination date.
	 * <P>
	 * Applies to FpML 4.*.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE29_OLD
		= new Rule (R4_0__R4_X, "cd-29[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		paymentDate;
					Element		terminationDate;

					if (and (
						exists (paymentDate = XPath.path (context, "feeLeg", "periodicPayment", "firstPaymentDate")),
						exists (terminationDate = XPath.path (context, "generalTerms", "scheduledTerminationDate", "adjustableDate", "unadjustedDate")))) {
						if (less (paymentDate, terminationDate)) continue;

						errorHandler.error ("305", context,
							"First periodic payment date '" + DOM.getInnerText (paymentDate) + "' " +
							"must be before the termination date '" + DOM.getInnerText (terminationDate) + "'",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if a first payment date is present it
	 * falls before the termination date.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE29
		= new Rule (R5_0__LATER, "cd-29")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		paymentDate;
					Element		terminationDate;

					if (and (
						exists (paymentDate = XPath.path (context, "feeLeg", "periodicPayment", "firstPaymentDate")),
						exists (terminationDate = XPath.path (context, "generalTerms", "scheduledTerminationDate", "unadjustedDate")))) {
						if (less (paymentDate, terminationDate)) continue;

						errorHandler.error ("305", context,
							"First periodic payment date '" + DOM.getInnerText (paymentDate) + "' " +
							"must be before the termination date '" + DOM.getInnerText (terminationDate) + "'",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if a last regular payment date is present
	 * it falls before the termination date.
	 * <P>
	 * Applies to FpML 4.*.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE30_OLD
		= new Rule (R4_0__R4_X, "cd-30[OLD]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		paymentDate;
					Element		terminationDate;

					if (and (
						exists (paymentDate = XPath.path (context, "feeLeg", "periodicPayment", "lastRegularPaymentDate")),
						exists (terminationDate = XPath.path (context, "generalTerms", "scheduledTerminationDate", "adjustableDate", "unadjustedDate")))) {
						if (less (paymentDate, terminationDate)) continue;

						errorHandler.error ("305", context,
							"Last regular periodic payment date '" + DOM.getInnerText (paymentDate) + "' " +
							"must be before the termination date '" + DOM.getInnerText (terminationDate) + "'",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures if a last regular payment date is present
	 * it falls before the termination date.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.7
	 */
	public static final Rule	RULE30
		= new Rule (R5_0__LATER, "cd-30")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		paymentDate;
					Element		terminationDate;

					if (and (
						exists (paymentDate = XPath.path (context, "feeLeg", "periodicPayment", "lastRegularPaymentDate")),
						exists (terminationDate = XPath.path (context, "generalTerms", "scheduledTerminationDate", "unadjustedDate")))) {
						if (less (paymentDate, terminationDate)) continue;

						errorHandler.error ("305", context,
							"Last regular periodic payment date '" + DOM.getInnerText (paymentDate) + "' " +
							"must be before the termination date '" + DOM.getInnerText (terminationDate) + "'",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the first payment date falls before the
	 * last regular payment date.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE31
		= new Rule (R4_0__LATER, "cd-31")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "PeriodicPayment"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("periodicPayment"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		firstDate;
					Element		lastDate;

					if (implies (
							and (
								exists (firstDate = XPath.path (context, "firstPaymentDate")),
								exists (lastDate  = XPath.path (context, "lastRegularPaymentDate"))),
							less (firstDate, lastDate)))
						continue;

					errorHandler.error ("305", context,
						"First payment date '" + DOM.getInnerText (firstDate) + "' must be before " +
						"last payment date '" + DOM.getInnerText (lastDate) + "'",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if a long form contracts defines a feeLeg
	 * then it must contain a dayCountFraction if there is a calculationAmount.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE32
		= new Rule (R4_0__LATER, "cd-32")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (
						validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Trade"), errorHandler)
					  & validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Contract"), errorHandler));
						
				return (
					  validate (nodeIndex.getElementsByName ("trade"), errorHandler)
					& validate (nodeIndex.getElementsByName ("contract"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		trade = (Element) list.item (index);

					if (isLongForm (trade)) {
						Element	context = XPath.path (trade, "creditDefaultSwap", "feeLeg", "periodicPayment");

						if (context == null) continue;

						if (!exists (XPath.path (context, "fixedAmountCalculation", "calculationAmount")))
							continue;

						if (!exists (XPath.path (context, "fixedAmountCalculation", "dayCountFraction"))) {
							errorHandler.error ("305", context,
								"Day count fraction must be present if a periodic payment is based on " +
								"a fixed amount calculation",
								getDisplayName (), null);

							result = false;
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the interval between the first and last payment
	 * dates is a multiple of the paymentFrequency.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE33
		= new Rule (R4_0__LATER, "cd-33")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "PeriodicPayment"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("periodicPayment"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		firstDate	= XPath.path (context, "firstPaymentDate");
					Element		lastDate	= XPath.path (context, "lastRegularPaymentDate");

					if ((firstDate == null) || (lastDate == null)) continue;

					Interval	interval	= interval (XPath.path (context, "paymentFrequency"));

					if (interval.dividesDates (Date.parse (DOM.getInnerText (firstDate)), Date.parse (DOM.getInnerText (lastDate))))
						continue;

					errorHandler.error ("305", context,
						"Last regular payment date '" + DOM.getInnerText (lastDate) + "' is not " +
						"an integer multiple of the payment period after the first payment " +
						" date '" + DOM.getInnerText (firstDate) + "'",
						getDisplayName (), null);

					result = false;
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the only the allowed reference obligations
	 * are defined.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE34
		= new Rule (R4_0__LATER, "cd-34")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "DeliverableObligations"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("deliverableObligations"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (equal (XPath.path (context, "category"), "ReferenceObligationsOnly")) {
						NodeList	children = context.getChildNodes ();
						for (int count = 0; count < children.getLength(); ++count) {
							Node node	= children.item (count);
							if ((node instanceof Element) && !node.getLocalName ().equals ("category")) {
								errorHandler.error ("305", context,
									"Deliverable obligations category is set to 'Reference " +
									" Obligations Only' but further elements have been included",
									getDisplayName (), null);

								result = false;
								break;
							}
						}
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures at least on credit event type is defined.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE35
		= new Rule (R4_0__LATER, "cd-35")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditEvents"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditEvents"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (DOM.getFirstChild (context) == null) {
						errorHandler.error ("305", context,
							"No elements where found in creditEvents. The structure must " +
							"contain at least one element",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the correct number of information sources
	 * are defined.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE36
		= new Rule (R4_0__LATER, "cd-36")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "PubliclyAvailableInformation"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("publiclyAvailableInformation"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (or (
							exists (XPath.path (context, "standardPublicSources")),
							exists (XPath.path (context, "publicSource"))))
						continue;

					errorHandler.error ("305", context,
						"Either at least one public source or standard public sources " +
						"must be referred to in publiclyAvailableInformation",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the quotation amount is no smaller than
	 * the minimum quotation amount.
	 * <P>
	 * Applies to FpML 4.0 and later.
	 * @since	TFP 1.0
	 */
	public static final Rule	RULE37
		= new Rule (R4_0__LATER, "cd-37")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CashSettlementTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("cashSettlementTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					Element	ccy1	= XPath.path (context, "quotationAmount", "currency");
					Element	amount	= XPath.path (context, "quotationAmount", "amount");
					Element	ccy2 	= XPath.path (context, "minimumQuotationAmount", "currency");
					Element	minimum = XPath.path (context, "minimumQuotationAmount", "amount");

					if ((ccy1 == null) || (ccy2 == null) || (amount == null) || (minimum == null)
						|| notEqual (ccy1, ccy2)
						|| (Double.parseDouble (toToken (amount)) >= toDouble (minimum)))
						continue;

					errorHandler.error ("305", context,
						"In cash settlement terms, quotation amount " + toToken (amount) +
						" must be greater or equal to minimum quotation amount",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the if any
	 * <CODE>referencePoolItem/constituentWeight/basketPercentage</CODE> values
	 * are defined then they must sum to 1.
	 * <P>
	 * Applies to FpML 4.2 and later.
	 * @since	TFP 1.1
	 */
	public static final Rule	RULE38
		= new Rule (R4_2__LATER, "cd-38")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "ReferencePool"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("referencePool"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					NodeList	items	= XPath.paths (context, "referencePoolItem", "constituentWeight", "basketPercentage");

					if (items.getLength() == 0) continue;

					BigDecimal total = BigDecimal.ZERO;
					for (int count = 0; count < items.getLength (); ++count)
						total = total.add (toDecimal (items.item (count)));

					if (equal (total, BigDecimal.ONE)) continue;

					errorHandler.error ("305", context,
							"The sum of referencePoolItem/constituentWeight/basketPercentage should be equal to 1",
							getDisplayName (), total.toString ());

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>nthToDefault</CODE> is present
	 * and <CODE>mthToDefault</CODE> is present then <CODE>nthToDefault</CODE>
	 * must be less than <CODE>mthToDefaultM</CODE>.
	 * <P>
	 * Applies to FpML 4.2 and later.
	 * @since	TFP 1.1
	 */
	public static final Rule	RULE39
		= new Rule (R4_2__LATER, "cd-39")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "BasketReferenceInformation"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("basketReferenceInformation"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		nth		= XPath.path (context, "nthToDefault");
					Element		mth		= XPath.path (context, "mthToDefault");

					if ((nth == null) || (mth == null) || (toInteger (nth) < toInteger (mth))) continue;

					errorHandler.error ("305", context,
							"If nthToDefault is present and mthToDefault is present then nthToDefault must be less than mthToDefault.",
							getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures <CODE>attachmentPoint</CODE> must be
	 * less than or equal to <CODE>exhaustionPoint</CODE>.
	 * <P>
	 * Applies to FpML 4.2 and later.
	 * @since	TFP 1.1
	 */
	public static final Rule	RULE40
		= new Rule (R4_2__LATER, "cd-40")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Tranche"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("tranche"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
	
				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		attach	= XPath.path (context, "attachmentPoint");
					Element		exhaust	= XPath.path (context, "exhaustionPoint");
	
					if ((attach == null) || (exhaust == null) || lessOrEqual (toDecimal (attach), toDecimal (exhaust))) continue;
	
					errorHandler.error ("305", context,
							"attachmentPoint must be less than or equal to exhaustionPoint.",
							getDisplayName (), null);
	
					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if <CODE>indexReferenceInformation/tranche</CODE>
	 * is not present then <CODE>modifiedEquityDelivery</CODE> must not be present.
	 * <P>
	 * Applies to FpML 4.3 and later.
	 * @since	TFP 1.1
	 */
	public static final Rule	RULE41
		= new Rule (R4_3__LATER, "cd-41")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "GeneralTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("generalTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					Element		tranche
						= XPath.path (context, "indexReferenceInformation", "tranche");
					Element		delivery
						= XPath.path (context, "modifiedEquityDelivery");

					if ((tranche == null) && (delivery != null)) {
						errorHandler.error ("305", context,
							"If indexReferenceInformation/tranche is not present then modifiedEquityDelivery must not be present.",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * If <CODE>basketReferenceInformation</CODE> is not present then
	 * <CODE>substitution</CODE> must not be present.
	 * <P>
	 * Applies to FpML 4.3 and later.
	 * @since	TFP 1.1
	 */
	public static final Rule	RULE42
		= new Rule (R4_3__LATER, "cd-42")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "GeneralTerms"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("generalTerms"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					Element		basket
						= XPath.path (context, "basketReferenceInformation");
					Element		substitution
						= XPath.path (context, "substitution");

					if ((basket == null) && (substitution != null)) {
						errorHandler.error ("305", context,
							"If basketReferenceInformation is not present then substitution must not be present.",
							getDisplayName (), null);

						result = false;
					}
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures if the trade has an initial payment
	 * then it is paid by the protection buyer to the protection seller.
	 * <P>
	 * Applies to FpML 4.3 and later.
	 * @since	TFP 1.1
	 */
	public static final Rule	RULE43
		= new Rule (R4_3__LATER, "cd-43")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				NodeList	list 	= nodeIndex.getElementsByName ("creditDefaultSwap");

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);

					if (!isSingleName (context)) continue;

					if (!exists (XPath.path(context, "feeLeg", "initialPayment"))) continue;

					Element		payer		= XPath.path (context, "feeLeg", "initialPayment", "payerPartyReference");
					Element		receiver 	= XPath.path (context, "feeLeg", "initialPayment", "receiverPartyReference");
					Element		seller		= XPath.path (context, "generalTerms", "sellerPartyReference");
					Element		buyer		= XPath.path (context, "generalTerms", "buyerPartyReference");

					if ((payer != null) && (seller != null) && (receiver != null) && (buyer != null)) {
						if (DOM.getAttribute (payer, "href").equals(DOM.getAttribute (buyer, "href")) &&
							DOM.getAttribute (receiver, "href").equals(DOM.getAttribute (seller, "href")))
							continue;
					}

					errorHandler.error ("305", context,
						"The initial payment should be paid by the protection buyer to the protection seller",
						getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures either every <CODE>referencePoolItem</CODE>
	 * has a <CODE>basketPercentage</CODE> or that none of them have.
	 * <P>
	 * Applies to FpML 4.2 and later.
	 * @since	TFP 1.1
	 */
	public static final Rule	RULE44
		= new Rule (R4_2__LATER, "cd-44")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CreditDefaultSwap"), errorHandler));
				
				return (validate (nodeIndex.getElementsByName ("creditDefaultSwap"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;

				for (int index = 0; index < list.getLength (); ++index) {
					Element		context = (Element) list.item (index);
					Element		pool	= XPath.path (context, "generalTerms", "basketReferenceInformation", "referencePool");
					NodeList	items  	= XPath.paths (pool, "referencePoolItem");
					NodeList	weights	= XPath.paths (pool, "referencePoolItem", "constituentWeight", "basketPercentage");

					if ((weights.getLength () == 0) || (weights.getLength () == items.getLength ())) continue;

					errorHandler.error ("305", pool,
							"Either every referencePoolItem should have a basketPercentage or none should have one",
							getDisplayName (), null);

					result = false;
				}
				return (result);
			}
		};

	/**
	 * Provides access to the CDS validation rule set.
	 *
	 * @return	The CDS validation rule set.
	 * @since	TFP 1.0
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}

	/**
	 * Determines the trade under test references ISDA 1999 definitions.
	 *
	 * @param	context			The trade element.
	 * @return	<CODE>true</CODE> if the trade uses 1999 definitions.
	 * @since	TFP 1.0
	 */
	protected static boolean isIsda1999 (Element context)
	{
		Element		documentation = DOM.getElementByLocalName (context, "documentation");

		if (documentation != null) {
			Element 	definitions = DOM.getElementByLocalName (documentation, "contractualDefinitions");
			Element 	confirmType = DOM.getElementByLocalName (documentation, "masterConfirmation", "masterConfirmationType");

			return (DOM.getInnerText (definitions).equals ("ISDA1999Credit")
				 || DOM.getInnerText (confirmType).equals ("ISDA1999Credit"));
		}
		return (false);
	}

	/**
	 * Determines the trade under test references ISDA 2003 definitions.
	 *
	 * @param	trade		The trade element.
	 * @return	<CODE>true</CODE> if the trade uses 2003 definitions.
	 * @since	TFP 1.0
	 */
	protected static boolean isIsda2003 (Element trade)
	{
		if (exists (XPath.path (trade, "creditDefaultSwap"))) {
			Element		target;
			NodeList	defs	= XPath.paths (trade, "documentation", "contractualDefinitions");

			for (int index = 0; index < defs.getLength (); ++index) {
				target = (Element) defs.item (index);
				if (toToken (target).startsWith ("ISDA2003Credit"))
					return (true);
			}

			if ((target = XPath.path (trade, "documentation", "masterConfirmation", "masterConfirmationType")) != null) {
				String value = toToken (trade);

				if (value.startsWith ("ISDA2003Credit") ||
					value.startsWith ("ISDA2004Credit"))
					return (true);
			}
		}
		return (false);
	}

	/**
	 * Determines if the trade under tests contains a short form contract.
	 *
	 * @param	trade		The trade element.
	 * @return	<CODE>true</CODE> if the trade is short form.
	 * @since	TFP 1.0
	 */
	protected static boolean isShortForm (Element trade)
	{
		Element	target;

		if (exists (XPath.path (trade, "creditDefaultSwap"))) {
			if (exists (XPath.path (trade, "documentation", "masterConfirmation")))
				return (true);
			if (exists (XPath.path (trade, "documentation", "contractualMatrix")))
				return (true);

			if ((target = XPath.path (trade, "documentation", "contractualTermsSupplement", "type")) != null) {
				String	value = toToken (target);
				if (value.startsWith ("iTraxx") || value.startsWith ("CDX"))
					return (true);
			}
		}
		return (false);
	}

	/**
	 * Determines if the trade under tests contains a long form contract.
	 *
	 * @param	trade		The trade element.
	 * @return	<CODE>true</CODE> if the trade is long form.
	 * @since	TFP 1.0
	 */
	protected static boolean isLongForm (Element trade)
	{
		Element	cds;

		if (exists (cds = XPath.path (trade, "creditDefaultSwap"))) {
			if (exists (XPath.path (trade, "documentation", "masterConfirmation")))
				return (false);
			if (exists (XPath.path (trade, "documentation", "contractualMatrix")))
				return (false);

			return (isSingleName (cds));
		}
		return (false);
	}

	/**
	 * Determines if a credit default swap is on a single name.
	 *
	 * @param 	cds			The credit default swap product.
	 * @return	<CODE>true</CODE> if the product is single name.
	 * @since	TFP 1.0
	 */
	private static boolean isSingleName (Element cds)
	{
		if (exists (XPath.path (cds, "generalTerms", "referenceInformation")))
			return (true);

		return (false);
	}

	/**
	 * Determines if a credit default swap is on an index.
	 *
	 * @param 	cds			The credit default swap product.
	 * @return	<CODE>true</CODE> if the product is an index.
	 * @since	TFP 1.0
	 */
	private static boolean isCreditIndex (Element cds)
	{
		if (exists (XPath.path (cds, "generalTerms", "indexReferenceInformation")) &&
		   !exists (XPath.path (cds, "generalTerms", "indexReferenceInformation", "tranche")))
			return (true);

		return (false);
	}

	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for credit derivative swaps products.
	 * @since	TFP 1.0
	 */
	private static final RuleSet	rules = RuleSet.forName ("CdsRules");

	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.0
	 */
	private CdsRules ()
	{ }

	/**
	 * Extracts an <CODE>Interval</CODE> from the data stored below the
	 * given context node.
	 *
	 * @param 	context			The context <CODE>XmlElement</CODE>
	 * @return	An <CODE>Interval</CODE> constructed from the stored data.
	 * @since	TFP 1.0
	 */
	private static Interval interval (final Element context)
	{
		try {
			return (new Interval (
				Integer.parseInt (toToken (XPath.path (context, "periodMultiplier"))),
				Period.forCode (toToken (XPath.path (context, "period")))));
		}
		catch (Exception error) {
			return (null);
		}
	}
}