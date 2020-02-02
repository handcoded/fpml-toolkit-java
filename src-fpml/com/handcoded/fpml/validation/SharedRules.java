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

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.handcoded.finance.Date;
import com.handcoded.finance.Time;
import com.handcoded.fpml.util.Identifier;
import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.DOM;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.Types;
import com.handcoded.xml.XPath;

/**
 * The <CODE>SharedRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for shared components.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class SharedRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 1-0 or
	 * later confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R1_0__LATER
		= Precondition.and (
				Preconditions.R1_0__LATER,
				Preconditions.CONFIRMATION);
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 1-0 thru
	 * 2-0 confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R1_0__R2_0
	= Precondition.and (
			Preconditions.R1_0__R2_0,
			Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 1-0 thru
	 * 3-0 confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R1_0__R3_0
	= Precondition.and (
			Preconditions.R1_0__R3_0,
			Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 3-0 or
	 * later confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R3_0__LATER
	= Precondition.and (
			Preconditions.R3_0__LATER,
			Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-2 thru
	 * 4-* confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R4_2__R4_X
	= Precondition.and (
			Preconditions.R4_2__R4_X,
			Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 5-0 or
	 * later confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R5_0__LATER
	= Precondition.and (
			Preconditions.R5_0__LATER,
			Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 5-0 thru
	 * 5-3 confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R5_0__R5_3
	= Precondition.and (
			Preconditions.R5_0__R5_3,
			Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 5-4 or
	 * later confirmation view document.
	 * @since	TFP 1.7
	 */
	private static final Precondition R5_4__LATER
	= Precondition.and (
			Preconditions.R5_4__LATER,
			Preconditions.CONFIRMATION);

	/**
	 * A <CODE>Rule</CODE> instance that ensures that business centers are
	 * only present if the date adjustment convention allows them.
	 * <P>
	 * Applies to all FpML versions.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE01 = new Rule (R1_0__LATER, "shared-1")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation()) {
					String ns = nodeIndex.getDocument ().getDocumentElement ().getNamespaceURI ();
					return (validate (nodeIndex.getElementsByType (ns, "BusinessDayAdjustments"), errorHandler));					
				}
					
				return (
					  validate (nodeIndex.getElementsByName ("dateAdjustments"), errorHandler)
					& validate (nodeIndex.getElementsByName ("calculationPeriodDatesAdjustments"), errorHandler)
					& validate (nodeIndex.getElementsByName ("paymentDatesAdjustments"), errorHandler)
					& validate (nodeIndex.getElementsByName ("resetDatesAdjustments"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)			
			{
				boolean		result = true;

				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					
					String text = DOM.getInnerText (DOM.getElementByLocalName (context, "businessDayConvention"));
												
					NodeList defs = context.getElementsByTagName ("businessCenters");
					NodeList refs = context.getElementsByTagName ("businessCentersReference");
					
					if (text.equals ("NONE") || text.equals ("NotApplicable")) {
						if ((defs.getLength () + refs.getLength ()) != 0) {
							errorHandler.error ("305", context,
								"business center definitions or references should not be present", getDisplayName (), null);
							result = false;
						}
					}
					else {
						if ((defs.getLength () + refs.getLength ()) == 0) {
							errorHandler.error ("305", context,
								"business center definitions or references should be present", getDisplayName (), null);
							result = false;
						}
					}
				}
				return (result);
			}			
		};
		
	/**
	 *  A <CODE>Rule</CODE> that ensures that period multiplier is 'D' if the
	 * &lt;dayType&gt; element is present.
	 * <P>
	 * Applies to FpML 1.0, 2.0 and 3.0.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE02 = new Rule (R1_0__R3_0, "shared-2")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
					  validate (nodeIndex.getElementsByName ("cashSettlementValuationDate"), errorHandler)
				    & validate (nodeIndex.getElementsByName ("feePaymentDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDateOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("initialFixingDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("paymentDaysOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("rateCutOffDaysOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("relativeDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("varyingNotionalInterimExchangePaymentDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("varyingNotionalFixingDates"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					
					if (implies (
							exists (XPath.path (context, "dayType")),
							equal (XPath.path (context, "period"), "D")))
						continue;
					
					errorHandler.error ("305", context,
						"Offset contains a day type by the the period is '" +
						DOM.getInnerText (XPath.path (context, "period")) + "', not 'D'",
						getDisplayName (), null);
					
					result = false;
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures that period multiplier is not zero when
	 * the day type is 'Business
	 * <P>
	 * Applies to FpML 1.0, 2.0 and 3.0.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE03 = new Rule (R1_0__R3_0, "shared-3")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return ( 
					  validate (nodeIndex.getElementsByName ("cashSettlementValuationDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("feePaymentDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDateOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("initialFixingDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("paymentDaysOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("rateCutOffDaysOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("relativeDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("varyingNotionalInterimExchangePaymentDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("varyingNotionalFixingDates"), errorHandler));
			}

			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result		= true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);

					if (implies (
							equal (XPath.path (context, "dayType"), "Business"),
							notEqual (XPath.path (context, "periodMultiplier"), 0)))
						continue;

					errorHandler.error ("305", context,
						"Offset has day type set to 'Business' but the period " +
						"multiplier is set to zero.",
						getDisplayName (), Types.toToken (XPath.path (context, "periodMultiplier")));

					result = false;
				}				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that the businessDayConvention is
	 * NONE when the day type is Business.
	 * <P>
	 * Applies to all FpML versions.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE04 = new Rule (R1_0__LATER, "shared-4")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
					  validate (nodeIndex.getElementsByName ("relativeDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDateOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("initialFixingDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("cashSettlementValuationDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("varyingNotionalFixingDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("varyingNotionalInterimExchangePaymentDates"), errorHandler));
			}
			 
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
								
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					Element		dayType = DOM.getElementByLocalName (context, "dayType");
					
					if ((dayType != null) && DOM.getInnerText (dayType).equals ("Business")) {
						String value = DOM.getInnerText (DOM.getElementByLocalName (context, "businessDayConvention"));
						if (!value.equals ("NONE")) {
							errorHandler.error ("305", context,
								"businessDayConvention should be NONE", getDisplayName (), value);
							result = false;
						}
					}
				}
				return (result);
			}			
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures the payer and receivers are different.
	 * <P>
	 * Applies to all FpML versions.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE05 = new Rule (R1_0__LATER, "shared-5")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("payerPartyReference");

				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element payer  = (Element) list.item (index);
					
					if (payer == null) continue;
					
					Element parent = (Element) payer.getParentNode ();
 					Element receiver = DOM.getElementByLocalName (parent, "receiverPartyReference");
					
 					if (receiver == null) continue;
 					
					if (payer.getAttribute ("href").equals (receiver.getAttribute ("href"))) {
						errorHandler.error ("305", parent,
							"payer and receiver party references must be different",
							getDisplayName (), payer.getAttribute ("href"));
						result = false;
					}
				}

				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures latestExerciseTime is after the
	 * earliestExerciseTime for American exercises.
	 * <P>
	 * Applies to FpML 3-0 and later.
	 * @since	TFP 1.0	
	 */
	public static final Rule 	RULE06	= new Rule (R3_0__LATER, "shared-6")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("americanExercise");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					Element		latest  = XPath.path (context, "latestExerciseTime", "hourMinuteTime");
					Element		earliest = XPath.path (context, "earliestExerciseTime", "hourMinuteTime");
					
					if ((latest == null) || (earliest == null) ||
						less (toTime (earliest), toTime (latest))) continue;
						
					errorHandler.error ("305", context,
						"The latest exercise time must be after the earliest",
						getDisplayName (), null);
					result = false;
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures latestExerciseTime is after the
	 * earliestExerciseTime for American exercises.
	 * <P>
	 * Applies to FpML 3-0 and later.
	 * @since	TFP 1.0	
	 */
	public static final Rule 	RULE07 	= new Rule (R3_0__LATER, "shared-7")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (validate (nodeIndex.getElementsByName ("bermudaExercise"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					Element		latest   = XPath.path (context, "latestExerciseTime", "hourMinuteTime");
					Element		earliest = XPath.path (context, "earliestExerciseTime", "hourMinuteTime");

					if ((latest == null) || (earliest == null)) continue;
					
					Time 	t1 = toTime (earliest);
					Time	t2 = toTime (latest);
					
					if ((t1 == null) || (t2 == null) || less (t1, t2))
						continue;
						
					errorHandler.error ("305", context,
						"The latest exercise time must be after the earliest",
						getDisplayName (), null);
					
					result = false;
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures unadjustedFirstDate is before
	 * unadjustedLastDate.
	 * <P>
	 * Applies to FpML 3-0 and later.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE08 	= new Rule (R3_0__LATER, "shared-8")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
					  validate (nodeIndex.getElementsByName ("scheduleBounds"), errorHandler)
					& validate (nodeIndex.getElementsByName ("businessDateRange"), errorHandler));
			}
			
			/**
			 * Validate all of the elements identified by the given
			 * <CODE>NodeList</CODE>.
			 *
			 * @param	list		The <CODE>NodeList</CODE> of candidate elements.
			 * @param	errorHandler The <CODE>ErrorHandler</CODE> to report
			 *						semantic errors through.
			 * @return	<CODE>false</CODE> of the RULE failed, <CODE>true
			 *			</CODE> otherwise.
			 */
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean		result 	= true;
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					
					try {
						Date firstDate = Date.parse (DOM.getInnerText (DOM.getElementByLocalName (context, "unadjustedFirstDate")));
						Date lastDate  = Date.parse (DOM.getInnerText (DOM.getElementByLocalName (context, "unadjustedLastDate")));
						
						if (firstDate.compareTo (lastDate) >= 0) {
							errorHandler.error ("305", context,
									"The unadjusted last date must be after the unadjusted first date",
									getDisplayName (), null);
											  	
							result = false;
						}
					}
					catch (IllegalArgumentException error) {
						// Syntax errors handled elsewhere
					}
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures business centers are not defined
	 * or referenced if the businessDayConvention is NONE or NotApplicable.
	 * <P>
	 * Applies to FpML 3-0 and later.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE09 	= new Rule (R3_0__LATER, "shared-9")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (validate (nodeIndex.getElementsByName ("businessDateRange"), errorHandler));
			} 

			/**
			 * Validate all of the elements identified by the given
			 * <CODE>NodeList</CODE>.
			 *
			 * @param	list		The <CODE>NodeList</CODE> of candidate elements.
			 * @param	errorHandler The <CODE>ErrorHandler</CODE> to report
			 *						semantic errors through.
			 * @return	<CODE>false</CODE> of the RULE failed, <CODE>true
			 *			</CODE> otherwise.
			 */
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)			
			{
				boolean		result = true;

				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					
					String text = DOM.getInnerText (DOM.getElementByLocalName (context, "businessDayConvention"));
												
					NodeList defs = context.getElementsByTagName ("businessCenters");
					NodeList refs = context.getElementsByTagName ("businessCentersReference");
					
					if (text.equals ("NONE") || text.equals ("NotApplicable")) {
						if ((defs.getLength () + refs.getLength ()) != 0) {
							errorHandler.error ("305", context,
								"business center definitions or references should not be present", getDisplayName (), null);
							result = false;
						}
					}
					else {
						if ((defs.getLength () + refs.getLength ()) == 0) {
							errorHandler.error ("305", context,
								"business center definitions or references should be present", getDisplayName (), null);
							result = false;
						}
					}
				}
				return (result);
			}			
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures calculationAgentPartyReference/@href
	 * attributes are unique.
	 * <P>
	 * Applies to all FpML versions.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE10 = new Rule (R1_0__LATER, "shared-10")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list   = nodeIndex.getElementsByName ("calculationAgent");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					NodeList refs 	= context.getElementsByTagName ("calculationAgentPartyReference");
					ArrayList<String>	values 	= new ArrayList<> ();
					
					for (int count = 0; count < refs.getLength(); ++count) {
						String href = ((Element) refs.item (count)).getAttribute ("href");
						
						if (values.contains (href)) {
							errorHandler.error ("305", context,
								"Duplicate calculation agent party reference", getDisplayName (), href);
							result = false;
						}
						else
							values.add (href);
					}	
					values.clear ();
				}

				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures businessDateRange references to
	 * business centers are within the same trade.
	 * <P>
	 * Applies to FpML 3-0 and later.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE11 	= new Rule (R3_0__LATER, "shared-11")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("businessDateRange");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element	context = (Element) list.item (index);
					Element ref = DOM.getElementByLocalName (context, "businessCentersReference");
		
					if (ref != null) {
						Element def = nodeIndex.getDocument ().getElementById (ref.getAttribute ("href"));
						
						if (!def.getLocalName ().equals ("businessCenters")) {
							errorHandler.error ("305", context,
								"Reference does not match with a businessCenters element",
								getDisplayName (), null);
							result = false;
						}
						else {
							// Walk up to <trade> node	
							do {
								Node node = ref.getParentNode ();
								
								if (node.getNodeType () == Node.ELEMENT_NODE)
									ref = (Element) node;
								else
									ref = null;
							} while ((ref != null) && !ref.getLocalName ().equals ("trade"));
							
							// Walk up to <trade> node
							do {
								Node node = def.getParentNode ();
								
								if (node.getNodeType () == Node.ELEMENT_NODE)
									def = (Element) node;
								else
									def = null;
							} while ((def != null) && !def.getLocalName ().equals ("trade"));
							
							if (def != ref) {
								errorHandler.error ("305", context,
									"The referenced business centers are not in the same trade",
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
	 * A <CODE>Rule</CODE> that ensures the referential integrity of
	 * buyerPartyReference/@href instances.
	 * <P>
	 * Applies to FpML 1-0 and 2-0.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE12_XLINK = new Rule (R1_0__R2_0, "shared-12[XLINK]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("buyerPartyReference");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  href	= context.getAttribute ("href");
					
					if ((href == null) || (href.length() < 2) || (href.charAt(0) != '#')) {
						errorHandler.error ("305", context,
							"The @href attribute is not a valid XPointer",
							getDisplayName (), href);
						result = false;
						continue;
					}
					
					Element target	= nodeIndex.getElementById (href.substring (1));
					
					if ((target == null) || !(target.getLocalName ().equals ("party") || target.getLocalName ().equals ("tradeSide"))) {
						errorHandler.error ("305", context,
							"The @href attribute does not reference a party element",
							getDisplayName (), href);
						result = false;
					}
				}
				
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures the referential integrity of
	 * buyerPartyReference/@href instances.
	 * <P>
	 * Applies to FpML 3-0 and later.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE12	= new Rule (R3_0__LATER, "shared-12")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("buyerPartyReference");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  href	= context.getAttribute ("href");
					Element target	= nodeIndex.getElementById (href);
					
					if ((target == null) || !(target.getLocalName ().equals ("party")  || target.getLocalName ().equals ("tradeSide"))) {
						errorHandler.error ("305", context,
							"The @href attribute does not reference a party element",
							getDisplayName (), href);
						result = false;
					}
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures the referential integrity of
	 * sellerPartyReference/@href instances.
	 * <P>
	 * Applies to FpML 1-0 and 2-0.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE13_XLINK	= new Rule (R1_0__R2_0, "shared-13[XLINK]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("sellerPartyReference");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  href	= context.getAttribute ("href");
					
					if ((href == null) || (href.length() < 2) || (href.charAt(0) != '#')) {
						errorHandler.error ("305", context,
							"The @href attribute is not a valid XPointer",
							getDisplayName (), href);
						result = false;
						continue;
					}
					
					Element target	= nodeIndex.getElementById (href.substring (1));
					
					if ((target == null) || !(target.getLocalName ().equals ("party"))) {
						errorHandler.error ("305", context,
							"The @href attribute does not reference a party element",
							getDisplayName (), href);
						result = false;
					}
				}
				
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures the referential integrity of
	 * sellerPartyReference/@href instances.
	 * <P>
	 * Applies to FpML 3-0 and later. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE13	= new Rule (R3_0__LATER, "shared-13")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("sellerPartyReference");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  href	= context.getAttribute ("href");
					Element target	= nodeIndex.getElementById (href);
					
					if ((target == null) || !(target.getLocalName ().equals ("party") || target.getLocalName ().equals ("tradeSide"))) {
						errorHandler.error ("305", context,
							"The @href attribute does not reference a party element",
							getDisplayName (), href);
						result = false;
					}
				}
				
				return (result);
			}
		};
			
	/**
	 * A <CODE>Rule</CODE> that ensures the referential integrity of
	 * calculationAgentPartyReference/@href instances.
	 * <P>
	 * Applies to FpML 1-0 and 2-0. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE14_XLINK	= new Rule (R1_0__R2_0, "shared-14[XLINK]")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("calculationAgentPartyReference");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  href	= context.getAttribute ("href");
					
					if ((href == null) || (href.length() < 2) || (href.charAt(0) != '#')) {
						errorHandler.error ("305", context,
							"The @href attribute is not a valid XPointer",
							getDisplayName (), href);
						result = false;
						continue;
					}
					
					Element target	= nodeIndex.getElementById (href.substring (1));
					
					if ((target == null) || !(target.getLocalName ().equals ("party"))) {
						errorHandler.error ("305", context,
							"The @href attribute does not reference a party element",
							getDisplayName (), href);
						result = false;
					}
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the referential integrity of
	 * calculationAgentPartyReference/@href instances.
	 * <P>
	 * Applies to FpML 3-0 and later. 
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE14	= new Rule (R3_0__LATER, "shared-14")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("calculationAgentPartyReference");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					String  href	= context.getAttribute ("href");
					Element target	= nodeIndex.getElementById (href);
					
					if ((target == null) || !(target.getLocalName ().equals ("party"))) {
						errorHandler.error ("305", context,
							"The @href attribute does not reference a party element",
							getDisplayName (), href);
						result = false;
					}
				}
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures that period multiplier is 'D' if the
	 * &lt;dayType&gt; element is present.
	 * <P>
	 * Applies to all FpML versions.
	 * @since	TFP 1.0	
	 */
	public static final Rule	RULE15 = new Rule (R1_0__LATER, "shared-15")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				return (
					  validate (nodeIndex.getElementsByName ("gracePeriod"), errorHandler)
					& validate (nodeIndex.getElementsByName ("paymentDaysOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("rateCutOffDaysOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("relativeDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDateOffset"), errorHandler)
					& validate (nodeIndex.getElementsByName ("initialFixingDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("cashSettlementValuationDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("varyingNotionalFixingDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("varyingNotionalInterimExchangePaymentDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("feePaymentDate"), errorHandler)
					& validate (nodeIndex.getElementsByName ("relativeDates"), errorHandler));
			}
			
			/**
			 * Validate all of the elements identified by the given
			 * <CODE>NodeList</CODE>.
			 *
			 * @param	list		The <CODE>NodeList</CODE> of candidate elements.
			 * @param	errorHandler The <CODE>ErrorHandler</CODE> to report
			 *						semantic errors through.
			 * @return	<CODE>false</CODE> if the RULE failed, <CODE>true
			 *			</CODE> otherwise.
			 */
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{			
				boolean		result = true;

				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element context = (Element) list.item (index);
					Element period  = DOM.getElementByLocalName (context, "period");
					Element dayType = DOM.getElementByLocalName (context, "dayType");
					Element factor  = DOM.getElementByLocalName (context, "periodMultiplier");
					
					if (dayType != null) {
						if (!DOM.getInnerText (period).equals ("D")
							|| Integer.parseInt (DOM.getInnerText (factor)) == 0) {
							errorHandler.error ("305", context,
									"daytype must only be present if and only if the period " +
									"is 'D' and the periodMultiplier is non-zero",
								getDisplayName (), null);
							result = false;
						}
					}
					else {
						if (DOM.getInnerText (period).equals ("D")
							&& Integer.parseInt (DOM.getInnerText (factor)) != 0) {
							errorHandler.error ("305", context,
									"daytype must only be present if and only if the period " +
									"is 'D' and the periodMultiplier is non-zero",
								getDisplayName (), null);
							result = false;
						}
					}			
				}

				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures the reference integrity of trade side
	 * party references.
	 * <P>
	 * Applies to FpML 4.2 until 5.0.
	 * @since	TFP 1.0
	 */
	public static final Rule RULE16	= new Rule (R4_2__R4_X, "shared-16")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = XPath.paths (nodeIndex.getElementsByName ("tradeSide"), "*", "party");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					String		href	= context.getAttribute ("href");
					Element		target	= nodeIndex.getElementById (href);
					
					if (target.getLocalName ().equals ("party")) continue;
					
					errorHandler.error ("305", context,
						"The value of the href attribute does not refere to a party structure",
						getDisplayName (), href);
					
					result = false;
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures the reference integrity of trade side
	 * account references.
	 * <P>
	 * Applies to FpML 4.2 until 5.0.
	 * @since	TFP 1.0
	 */
	public static final Rule RULE17	= new Rule (R4_2__R4_X, "shared-17")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = XPath.paths (nodeIndex.getElementsByName ("tradeSide"), "*", "account");
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context = (Element) list.item (index);
					String		href	= context.getAttribute ("href");
					Element		target	= nodeIndex.getElementById (href);
					
					if (target.getLocalName ().equals ("account")) continue;
					
					errorHandler.error ("305", context,
						"The value of the href attribute does not refere to an account structure",
						getDisplayName (), href);
					
					result = false;
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures all party instances represent unique
	 * entities.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE18 = new Rule (R5_0__LATER, "shared-18")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Party"), errorHandler));
				return (validate (nodeIndex.getElementsByName ("party"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean			result	= true;
				ArrayList<Identifier> identifiers = new ArrayList<> ();
				ArrayList<String>	names = new ArrayList<String> ();
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context 	= (Element) list.item (index);
					Element 	partyId 	= XPath.path (context, "partyId");
					Element		partyName	= XPath.path (context, "partyName");
					
					if (partyId != null) {
						Identifier	identifier = new Identifier (partyId, "partyIdScheme");
						
						if (identifiers.contains (identifier)) {
							errorHandler.error ("305", context,
								"Party identifiers must be unique",
								getDisplayName (), identifier.toString ());
							result = false;
						}
						else
							identifiers.add (identifier);
					}
					
					// The FpML rule should not include this part
					if (partyName != null) {
						String		name	= DOM.getInnerText (partyName);
						
						if (names.contains (name)) {
							errorHandler.error ("305", context,
								"Party names must be unique",
								getDisplayName (), name);
							result = false;
						}
						else
							names.add (name);
					}
				}
				
				identifiers.clear ();
				names.clear ();
				
				return (result);
			}
		};
	
	/**
	 * A <CODE>Rule</CODE> that ensures all account instances represent unique
	 * entities.
	 * <P>
	 * Applies to FpML 5.0 up to 5.3.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE19A = new Rule (R5_0__R5_3, "shared-19a")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Account"), errorHandler));
				return (validate (nodeIndex.getElementsByName ("account"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean			result	= true;
				ArrayList<Identifier> identifiers = new ArrayList<> ();
				ArrayList<String>	names = new ArrayList<> ();
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context 	= (Element) list.item (index);
					Element 	accountId 	= XPath.path (context, "accountId");
					Element		accountName	= XPath.path (context, "accountName");
					
					if (accountId != null) {
						Identifier	identifier = new Identifier (accountId, "accountIdScheme");
						
						if (identifiers.contains (identifier)) {
							errorHandler.error ("305", context,
								"Account identifiers must be unique",
								getDisplayName (), identifier.toString ());
							result = false;
						}
						else
							identifiers.add (identifier);
					}
					
					// The FpML rule should not include this part
					if (accountName != null) {
						String		name	= DOM.getInnerText (accountName);
						
						if (names.contains (name)) {
							errorHandler.error ("305", context,
								"Account names must be unique",
								getDisplayName (), name);
							result = false;
						}
						else
							names.add (name);
					}
				}

				identifiers.clear ();
				names.clear ();
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures all account instances represent unique
	 * entities.
	 * <P>
	 * Applies to FpML 5.4 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE19B = new Rule (R5_4__LATER, "shared-19b")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Account"), errorHandler));
				return (validate (nodeIndex.getElementsByName ("account"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean			result	= true;
				ArrayList<Identifier> identifiers = new ArrayList<> ();
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context 	= (Element) list.item (index);
					Element 	accountId 	= XPath.path (context, "accountId");
					
					if (accountId != null) {
						Identifier	identifier = new Identifier (accountId, "accountIdScheme");
						
						if (identifiers.contains (identifier)) {
							errorHandler.error ("305", context,
								"Account identifiers must be unique",
								getDisplayName (), identifier.toString ());
							result = false;
						}
						else
							identifiers.add (identifier);
					}
				}
				
				identifiers.clear ();
				
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures all adjustable dates in a set are unique.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE20 = new Rule (R5_0__LATER, "shared-20")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "AdjustableDates"), errorHandler));
				return (
					  validate (nodeIndex.getElementsByName ("adjustableDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("calculationPeriods"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fixingDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fxFixingSchedule"), errorHandler)
					& validate (nodeIndex.getElementsByName ("fxObservationDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("paymentDates"), errorHandler)
					& validate (nodeIndex.getElementsByName ("periods"), errorHandler)
					& validate (nodeIndex.getElementsByName ("pricingDates"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean			result	= true;
				ArrayList<Date>	dates	= new ArrayList<> ();
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element			context	= (Element) list.item (index);
					NodeList		nodes	= XPath.paths (context, "unadjustedDate");
					
					for (int count = 0; count < nodes.getLength (); ++count) {
						Element			node	= (Element) nodes.item (count);
						Date			date 	= Types.toDate (node);
						
						if (dates.contains (date)) {
							errorHandler.error ("305", node,
								"Duplicate unadjustedDate",
								getDisplayName (), date.toString ());
							result = false;
						}
						else
							dates.add (date);
					}
					
					dates.clear ();
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures all account instances represent unique
	 * entities.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE21 = new Rule (R5_0__LATER, "shared-21")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "BusinessCenters"), errorHandler));
				return (validate (nodeIndex.getElementsByName ("businessCenters"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean			result	= true;
				ArrayList<Identifier> identifiers = new ArrayList<> ();
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context 	= (Element) list.item (index);
					NodeList	nodes		= XPath.paths (context, "businessCenter");
					
					for (int count = 0; count < nodes.getLength (); ++count) {
						Element		node		= (Element) nodes.item (count);
						Identifier	identifier  = new Identifier (node, "businessCenterScheme");
						
						if (identifiers.contains (identifier)) {
							errorHandler.error ("305", node,
								"Duplicate business center",
								getDisplayName (), identifier.toString ());
							result = false;
						}
						else
							identifiers.add (identifier);
					}
					identifiers.clear ();
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures calculation agent references are unique.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 * @deprecated
	 */
	@Deprecated
	public static final Rule RULE22 = new Rule (R5_0__LATER, "shared-22")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CalculationAgent"), errorHandler));
				return (validate (nodeIndex.getElementsByName ("calculationAgent"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean			result	= true;
				ArrayList<String>	idrefs	= new ArrayList<> ();

				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element			context = (Element) list.item (index);
					NodeList		nodes	= XPath.paths (context, "calculationAgentPartyReference");
					
					for (int count = 0; count < nodes.getLength (); ++count) {
						Element 		node	= (Element) nodes.item (count);
						String			idref	= node.getAttribute ("href");
						
						if (idrefs.contains (idref)) {
							errorHandler.error ("305", node,
								"Duplicate calculationAgentPartyReference",
								getDisplayName (), idref);
							result = false;
						}
						else
							idrefs.add (idref);
					}
					idrefs.clear ();
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures all reference bank references are unique.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE23 = new Rule (R5_0__LATER, "shared-23")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "CashSettlementReferenceBanks"), errorHandler));
				return (validate (nodeIndex.getElementsByName ("cashSettlementReferenceBanks"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean			result	= true;
				ArrayList<Identifier> identifiers = new ArrayList<> ();
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context 	= (Element) list.item (index);
					NodeList	nodes		= XPath.paths (context, "referenceBank", "referenceBankId");
					
					for (int count = 0; count < nodes.getLength (); ++count) {
						Element		node		= (Element) nodes.item (count);
						Identifier	identifier  = new Identifier (node, "referenceBankIdScheme");
						
						if (identifiers.contains (identifier)) {
							errorHandler.error ("305", node,
								"Duplicate reference bank Id",
								getDisplayName (), identifier.toString ());
							result = false;
						}
						else
							identifiers.add (identifier);
					}
					identifiers.clear ();
				}
				return (result);
			}
		};
		
	/**
	 * A <CODE>Rule</CODE> that ensures all routing identifers are unique.
	 * entities.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE24 = new Rule (R5_0__LATER, "shared-24")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
					return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "RoutingIds"), errorHandler));
				return (validate (nodeIndex.getElementsByName ("routingIds"), errorHandler));
			}
			
			private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
			{
				boolean			result	= true;
				ArrayList<Identifier> identifiers = new ArrayList<> ();
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context 	= (Element) list.item (index);
					NodeList	nodes		= XPath.paths (context, "routingId");
					
					for (int count = 0; count < nodes.getLength (); ++count) {
						Element		node		= (Element) nodes.item (count);
						Identifier	identifier  = new Identifier (node, "routingIdCodeScheme");
						
						if (identifiers.contains (identifier)) {
							errorHandler.error ("305", node,
								"Duplicate routing Id",
								getDisplayName (), identifier.toString ());
							result = false;
						}
						else
							identifiers.add (identifier);
					}
					identifiers.clear ();
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures all step dates in a schedule are unique.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE25 = new Rule (R5_0__LATER, "shared-25")
		{
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				if (nodeIndex.hasTypeInformation ())
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
				boolean			result	= true;
				ArrayList<Date> 	dates = new ArrayList<> ();
				
				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element		context		= (Element) list.item (index);
					NodeList	nodes		= XPath.paths (context, "step", "stepDate");
					
					for (int count = 0; count < nodes.getLength (); ++count) {
						Element		node		= (Element) nodes.item (count);
						Date		date	  	= Types.toDate (node);
						
						if (dates.contains (date)) {
							errorHandler.error ("305", node,
								"Duplicate step date",
								getDisplayName (), date.toString ());
							result = false;
						}
						else
							dates.add (date);
					}
					dates.clear ();
				}
				return (result);
			}
		};

	/**
	 * A <CODE>Rule</CODE> that ensures a currency is specified when a bond
	 * has a par value.
	 * <P>
	 * Applies to FpML 5.0 and later.
	 * @since	TFP 1.6
	 */
	public static final Rule RULE26 = new Rule (R5_0__LATER, "shared-26")
	{
		public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
		{
			if (nodeIndex.hasTypeInformation ())
				return (validate (nodeIndex.getElementsByType (determineNamespace (nodeIndex), "Bond"), errorHandler));
			return (validate (nodeIndex.getElementsByName ("bond"), errorHandler));
		}
		
		private boolean validate (NodeList list, ValidationErrorHandler errorHandler)
		{
			boolean		result	= true;
			
			for (int index = 0, length = list.getLength (); index < length; ++index) {
				Element		context	= (Element) list.item (index);
				Element		parValue = XPath.path (context, "parValue");
				Element		currency = XPath.path (context, "currency");
				
				if ((parValue == null) || (currency != null)) continue;
				
				errorHandler.error ("305", context,
					"If parValue is present then the currency must be specified",
					getDisplayName (), null);
				result = false;				
			}
			
			return (result);
		}
	};

	/**
	 * A <CODE>Rule</CODE> that ensures the buyers and sellers are different.
	 * <P>
	 * Applies to all FpML versions.
	 * @since	TFP 1.6	
	 */
	public static final Rule	RULE29 = new Rule (R1_0__LATER, "shared-29")
		{
			/**
			 * {@inheritDoc}
			 */
			public boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
			{
				boolean		result = true;
				NodeList	list = nodeIndex.getElementsByName ("buyerPartyReference");

				for (int index = 0, length = list.getLength (); index < length; ++index) {
					Element buyer  = (Element) list.item (index);
					
					if (buyer == null) continue;
					
					Element parent = (Element) buyer.getParentNode ();
 					Element seller = DOM.getElementByLocalName (parent, "sellerPartyReference");
					
 					if (seller == null) continue;
 					
					if (buyer.getAttribute ("href").equals (seller.getAttribute ("href"))) {
						errorHandler.error ("305", parent,
							"buyer and seller party references must be different",
							getDisplayName (), buyer.getAttribute ("href"));
						result = false;
					}
				}

				return (result);
			}
		};
		
	/**
	 * Provides access to the shared components validation rule set.
	 * 
	 * @return	The shared validation rule set.
	 * @since	TFP 1.0
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}
		
	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for interest rate products.
	 * @since	TFP 1.0	
	 */
	private static final RuleSet	rules = RuleSet.forName ("SharedRules");
		
	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.0	
	 */
	private SharedRules ()
	{ }
}