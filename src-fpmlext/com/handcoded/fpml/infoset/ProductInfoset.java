// Copyright (C),2006-2020 HandCoded Software Ltd.
// All rights reserved.
//
// This software is the confidential and proprietary information of HandCoded
// Software Ltd. ("Confidential Information").  You shall not disclose such
// Confidential Information and shall use it only in accordance with the terms
// of the license agreement you entered into with HandCoded Software.
//
// HANDCODED SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
// SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. HANDCODED SOFTWARE SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
// OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

package com.handcoded.fpml.infoset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.handcoded.finance.Date;
import com.handcoded.finance.Interval;
import com.handcoded.finance.Period;
import com.handcoded.fpml.Releases;
import com.handcoded.meta.DirectConversion;
import com.handcoded.meta.Helper;
import com.handcoded.meta.SchemaRelease;
import com.handcoded.meta.Specification;
import com.handcoded.xml.DOM;
import com.handcoded.xml.Types;
import com.handcoded.xml.XPath;

/**
 * The <CODE>ProductInfoset</CODE> class provides utility functions that can be
 * used to convert the DOM representation of an FpML trade into a stripped down
 * product infoset. The product infoset is created by passing the document
 * through a conversion instance.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class ProductInfoset
{
	/**
	 * Creates the product infoset for the fragment of FpML indicated by the
	 * <CODE>Element</CODE> argument.
	 * 
	 * @param	element			The root <CODE>Element</CODE> of the FpML fragment.
	 * @return	A DOM <CODE>Document</CODE> containing the product infoset.
	 * @since	TFP 1.6
	 */
	public static Document createInfoset (final Element element)
	{
		return (conversion.convert (element, null));
	}
	
	private static class FPML_5_3__INFOSET_1_0 extends DirectConversion
	{
		public FPML_5_3__INFOSET_1_0 ()
		{
			super (Releases.R5_3_CONFIRMATION, R1_0);
		}
		
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.6
		 */
		@Override
		public Document convert (Document source, Helper helper)
		{
            return (convert (source.getDocumentElement (), helper));
		}
		
        public Document convert (Element oldRoot, Helper helper)
        {
			Document		target 	= getTargetRelease ().newInstance ("productInfoset");
			NodeList		list	= oldRoot.getChildNodes ();
			
			// Transcribe each of the first level child elements
			for (int index = 0; index < list.getLength (); ++index) {
				Node		node = list.item (index);
				
                if (node.getNodeType() == Node.ELEMENT_NODE)
				    traverse (node, target, target.getDocumentElement ());
			}
            return (target);
        }
        
        private static final Comparator<Node> COMPARE_FIXED_RATES
        	= new Comparator <Node> ()
        	{
        		public int compare (Node lhs, Node rhs)
        		{
        			return (compareSwapStreamFixedRates (lhs, rhs));
        		}
        	};

        private static final Comparator<Node> COMPARE_FLOAT_RATES
        	= new Comparator <Node> ()
        	{
        		public int compare (Node lhs, Node rhs)
        		{
        			return (compareSwapStreamFloatingRates (lhs, rhs));
        		}
        	};

        /**
         * Compares the fixed rate values in two streams to determine their
         * relative ordering.
         * 
         * @param 	lhs			The <CODE>Node</CODE> for the left hand stream.
         * @param 	rhs			The <CODE>Node</CODE> for the right hand stream.
         * @return	An integer value indicating the relative ordering.
         * @since	TFP 1.6
         */
        private static int compareSwapStreamFixedRates (Node lhs, Node rhs)
        {
            BigDecimal lhsRate = extractSwapStreamFixedRate ((Element) lhs);
            BigDecimal rhsRate = extractSwapStreamFixedRate ((Element) rhs);

            return (lhsRate.compareTo (rhsRate));
        }

        private static BigDecimal extractSwapStreamFixedRate (Element stream)
        {
            Element  rate = XPath.path (stream, "calculationPeriodAmount", "calculation", "fixedRateSchedule", "initialValue");

            return (Types.toDecimal (rate));
        }

        private static int compareSwapStreamFloatingRates (Node lhs, Node rhs)
        {
            String  lhsIndex = extractSwapStreamFloatingRate ((Element) lhs);
            String  rhsIndex = extractSwapStreamFloatingRate ((Element) rhs);

            return (lhsIndex.compareTo (rhsIndex));              
        }

        private static String extractSwapStreamFloatingRate (Element stream)
        {
            Element  index   = XPath.path (stream, "calculationPeriodAmount", "calculation", "floatingRateCalculation", "floatingRateIndex");
            Element  tenor   = XPath.path (stream, "calculationPeriodAmount", "calculation", "floatingRateCalculation", "indexTenor");

            if (tenor != null) {
                Element  factor  = XPath.path (tenor, "periodMultipler");
                Element  period  = XPath.path (tenor, "period");

                return (Types.toToken (index) + "/" + Types.toToken (factor) + "/" + Types.toToken (period));
            }
            return (Types.toToken (index));
        }

        private static void deriveTenor (Date start, Date end, Document document, Element parent)
        {
            int         monthDiff   = end.month () - start.month ();
            int         yearDiff    = end.year () - start.year ();
            int         daysDiff    = end.dayNumber () - start.dayNumber ();
            Interval    tenor       = null;

            if (daysDiff < 28) {
                if (daysDiff % 7 == 0)
                    tenor = new Interval (daysDiff / 7, Period.WEEK);
                else
                    tenor = new Interval (daysDiff, Period.DAY);
            }
            else {
                if (monthDiff != 0)
                    tenor = new Interval (monthDiff + 12 * yearDiff, Period.MONTH);
                else
                    tenor = new Interval (yearDiff, Period.YEAR);
            }

            // Build the XML structure
            if (tenor != null) {
                Element  element;

                element = document.createElementNS (R1_0.getNamespaceUri (), "periodMultiplier");
                DOM.setInnerText (element, Integer.toString (tenor.getMultiplier ()));
                parent.appendChild (element);

                element = document.createElementNS (R1_0.getNamespaceUri (), "period");
                DOM.setInnerText (element, tenor.getPeriod ().toString ());
                parent.appendChild (element);
            }
        }

        private static Element findTrade (Element context)
        {
            Node     parent  = context.getParentNode ();

            while ((parent != null) && (parent.getNodeType () == Node.ENTITY_NODE)) {
                if (((Element) parent).getLocalName ().equals ("trade"))
                    return ((Element) parent);

                parent = parent.getParentNode ();
            }
            return (null);
        }

        /// <summary>
		/// Recursively walk through the input document until the start of a
        /// product definition is located. At that point start transcribing
        /// the details.
		/// </summary>
		/// <param name="context">The <see cref="XmlNode"/> to be examined.</param>
		/// <param name="document">The new <see cref="XmlDocument"/> instance.</param>
		/// <param name="parent">The new parent <see cref="XmlNode"/>.</param>
		private void traverse (Node context, Document document, Element parent)
		{
			switch (context.getNodeType ()) {
			case Node.ELEMENT_NODE:
				{
					Element		element = (Element) context;

                    // Start copying when we reach a product element
                    if (XPath.match (element, "swap") ||                    // IR
                        XPath.match (element, "fra") ||
                        XPath.match (element, "bulletPayment") ||
                        XPath.match (element, "capFloor") ||
                        XPath.match (element, "swaption") ||
                        XPath.match (element, "bondOption") ||
                        XPath.match (element, "fxSingleLeg") ||             // FX
                        XPath.match (element, "fxSwap") ||
                        XPath.match (element, "fxOption") ||
                        XPath.match (element, "fxDigitalOption") ||
                        XPath.match (element, "termDeposit") ||
                        XPath.match (element, "commoditySwap") ||           // CO
                        XPath.match (element, "commoditySwaption") ||
                        XPath.match (element, "commodityOption") ||
                        XPath.match (element, "commodityForward") ||
                        XPath.match (element, "creditDefaultSwap") ||       // CD
                        XPath.match (element, "creditDefaultSwapOption") ||
                        XPath.match (element, "strategy"))
                        transcribe (element, document, parent);
                    else {
                    	NodeList	list = element.getChildNodes ();
				        for (int index = 0; index < list.getLength (); ++index) {
				        	Node 		node 	= list.item (index);
                            if (node.getNodeType () == Node.ELEMENT_NODE)
					            traverse (node, document, element);
				        }
                    }
					break;
				}
            }
        }

		/// <summary>
		/// Recursively processes an FpML 5-x document to produce a new document
        /// containin the stripped down product infosets.
		/// </summary>
		/// <param name="context">The <see cref="XmlNode"/> to be copied.</param>
		/// <param name="document">The new <see cref="XmlDocument"/> instance.</param>
		/// <param name="parent">The new parent <see cref="XmlNode"/>.</param>
		private void transcribe (Node context, Document document, Element parent)
		{
            if (context != null) {
			    switch (context.getNodeType ()) {
                case Node.ATTRIBUTE_NODE:
                    {
                        Attr	 attribute = (Attr) context;

                        // Remove unnecessary ID attributes
                        if (XPath.match (attribute, "businessCenters", "id") ||
                            XPath.match (attribute, "swapStream", "id") ||
                            XPath.match (attribute, "swapStream", "resetDates", "id") ||
                            XPath.match (attribute, "europeanExercise", "id") ||
                            XPath.match (attribute, "americanExercise", "id") ||
                            XPath.match (attribute, "bermudaExercise", "id"))
                            break;

                        if (XPath.match (attribute, "currencyScheme") &&
                            attribute.getValue ().equals ("http://www.fpml.org/ext/iso4217"))
                            break;

                        parent.setAttribute (attribute.getName (), attribute.getValue ());
                        break;
                    }

			    case Node.ELEMENT_NODE:
				    {
					    Element		element = (Element) context;

                        // Strip an existing taxonomy data and identification
                        if (XPath.match (element, "productType") ||
                            XPath.match (element, "assetClass") ||
                            XPath.match (element, "productid"))
                            break;

                        // Remove party specific references
                        if (XPath.match (element, "buyerPartyReference") ||
                            XPath.match (element, "buyerAccountReference") ||
                            XPath.match (element, "sellerPartyReference") ||
                            XPath.match (element, "sellerAccountReference") ||
                            XPath.match (element, "payerPartyReference") ||
                            XPath.match (element, "payerAccountReference") ||
                            XPath.match (element, "receiverPartyReference") ||
                            XPath.match (element, "receiverAccountReference") ||
                            XPath.match (element, "partyReference") ||
                            XPath.match (element, "calculationAgentPartyReference"))
                            break;

                        // Remove monetary amounts
                        if (XPath.match (element, "notional", "amount") ||
                            XPath.match (element, "paymentAmount", "amount") ||
                            XPath.match (element, "stubAmount", "amount"))
                            break;

                        // Remove calculation period references
                        if (XPath.match (element, "paymentDates", "calculationPeriodDatesReference") ||
                            XPath.match (element, "resetDates", "calculationPeriodDatesReference") ||
                            XPath.match (element, "stubCalculationPeriodAmount", "calculationPeriodDatesReference"))
                            break;

                        // Remove reset dates references
                        if (XPath.match (element, "fixingDates", "dateRelativeTo") ||
                            XPath.match (element, "fixingDateOffset", "dateRelativeTo") ||
                            XPath.match (element, "relativeDate", "dateRelativeTo"))
                            break;

                        // Remove dates
                        if (XPath.match (element, "paymentDate", "unadjustedDate") ||
                            XPath.match (element, "adjustableDate", "unadjustedDate") ||
                            XPath.match (element, "paymentDates", "firstPaymentDate") ||
                            XPath.match (element, "fra", "adjustedEffectiveDate") ||
                            XPath.match (element, "fra", "adjustedTerminationDate"))
                            break;

                        // Replace businessCentersReference with a copy of the target
                        if (XPath.match (element, "businessCentersReference")) {
                            transcribe (element.getOwnerDocument ().getElementById (element.getAttribute ("href")), document, parent);
                            break;
                        }

                        // Strip schedules
                        if (XPath.match (element, "notionalStepSchedule", "initialValue") ||
                            XPath.match (element, "fixedRateSchedule", "initialValue") ||
                            XPath.match (element, "spreadSchedule", "initialValue") ||
                            XPath.match (element, "capRateSchedule", "initialValue") ||
                            XPath.match (element, "floorRateSchedule", "initialValue"))
                            break;

                        // Keep an indication of step presence but remove values
                        if (XPath.match (element, "step")) {
                            Element prevElement = DOM.getPreviousSibling (element);

                            if ((prevElement != null) && XPath.match (prevElement, "step"))
                                break;
                        }
                        if (XPath.match (element, "step", "stepDate") ||
                            XPath.match (element, "step", "stepValue"))
                            break;

                        // Remove rates
                        if (XPath.match (element, "initialStub", "stubRate") ||
                            XPath.match (element, "finalStub", "stubRate") ||
                            XPath.match (element, "fra", "fixedRate"))
                            break;

                        // Remove other odd elements
                        if (XPath.match (element, "fra", "calculationPeriodNumberOfDays"))
                            break;

                        // Clone the original element
					    Element 		clone = document.createElementNS (R1_0.getNamespaceUri (), element.getLocalName ());
					    try {
				        parent.appendChild (clone);
					    }
					    catch (Exception error) {
					    	clone = null;
					    }

                        // Keep an indication of cashflows but remove values
                        if (XPath.match (element, "swapStream", "cashflows"))
                            break;

                        // Normalise swaps
                        if (XPath.match (element, "swap")) {
                            orderSwapStreams (element, document, clone);

                            transcribe (XPath.path (element, "earlyTerminationProvision"), document, clone);
                            transcribe (XPath.path (element, "cancelableProvision"), document, clone);
                            transcribe (XPath.path (element, "extendibleProvision"), document, clone);
                            break;
                        }

                        // Make swap streams relative
                        if (XPath.match (element, "swapStream", "calculationPeriodDates")) {
                            Element  effectiveDate;
                            Element  terminationDate;

                            if ((effectiveDate = XPath.path (element, "firstRegularPeriodDate", "unadjustedDate")) == null)
                                effectiveDate = XPath.path (element, "effectiveDate", "unadjustedDate");

                            if ((terminationDate = XPath.path (element, "lastRegularPeriodDate", "unadjustedDate")) == null)
                                terminationDate = XPath.path (element, "terminationDate", "unadjustedDate");

                            // Replace absolute dates with tenors
                            if ((effectiveDate != null) && (terminationDate != null)) {
                                Element tenor = document.createElementNS (R1_0.getNamespaceUri (), "relativeTerminationDate");
                                clone.appendChild (tenor);

                                deriveTenor (Types.toDate (effectiveDate), Types.toDate (terminationDate), document, tenor);
                                transcribe (XPath.path (element, "dayType"), document, tenor);

                                transcribe (XPath.path (element, "calculationPeriodDatesAdjustments"), document, clone);
                                transcribe (XPath.path (element, "stubPeriodType"), document, clone);
                                transcribe (XPath.path (element, "calculationPeriodFrequency"), document, clone);
                                break;
                            }
                        }

                        // Make FRAs relative
                        if (XPath.match (element, "fra")) {
                            Element  effectiveDate   = XPath.path (element, "adjustedEffectiveDate");
                            Element  terminationDate = XPath.path (element, "adjustedTerminationDate");

                            if ((effectiveDate != null) && (terminationDate != null)) {
                                Element tenor = document.createElementNS (R1_0.getNamespaceUri (), "relativeTerminationDate");
                                clone.appendChild (tenor);

                                deriveTenor (Types.toDate (effectiveDate), Types.toDate (terminationDate), document, tenor);
                            }
                        }

                        // Derive Expiration date tenor
                        if (XPath.match (element, "europeanExercise", "expirationDate")) {
                            Element      tradeDate  = XPath.path (findTrade (element), "tradeHeader", "tradeDate");
                            Element      expiryDate = XPath.path (element, "adjustableDate", "unadjustedDate");

                            if ((tradeDate != null) && (expiryDate != null)) {
                                Element tenor = document.createElementNS (R1_0.getNamespaceUri (), "relativeExpirationDate");
                                clone.appendChild (tenor);

                                deriveTenor (Types.toDate (tradeDate), Types.toDate (expiryDate), document, tenor);

                                Element adjustments = XPath.path (element, "adjustableDate", "dateAdjustments");
                                if (adjustments != null)
                                    transcribe (adjustments, document, clone);
                                break;
                            }
                        }

					    // Transfer and update attributes
                        NamedNodeMap	attrs	= element.getAttributes ();
					    for (int index = 0; index < attrs.getLength (); ++index)
						    transcribe (attrs.item (index), document, clone);
						
					    // Recursively copy the child node across
					    NodeList	list = element.getChildNodes ();
					    for (int index = 0; index < list.getLength (); ++index)
						    transcribe (list.item (index), document, clone);
                        break;
				    }

                case Node.TEXT_NODE:
                    {
                        parent.appendChild (document.createTextNode (((Text) context).getTextContent ()));
                        break;
                    }
                }					
			}
        }

        private void orderSwapStreams (Element context, Document document, Element parent)
        {
            List<Node> fixedStreams = new ArrayList<Node> ();
            List<Node> floatStreams = new ArrayList<Node> ();
            // TODO inflation

            NodeList	list = XPath.paths (context, "swapStream");
            for (int index = 0; index < list.getLength (); ++index) {
            	Node	node = list.item (index);
                if (XPath.path ((Element) node, "calculationPeriodAmount", "calculation", "fixedRateSchedule") != null)
                    fixedStreams.add (node);
                else if (XPath.path ((Element) node, "calculationPeriodAmount", "calculation", "floatingRateCalculation") != null)
                    floatStreams.add (node);
            }

            Collections.sort (fixedStreams, COMPARE_FIXED_RATES);
            Collections.sort (floatStreams, COMPARE_FLOAT_RATES);

            for (Node node : fixedStreams)
                transcribe (node, document, parent);

            for (Node node : floatStreams)
                transcribe (node, document, parent);
        }
	}

	/**
	 * A <CODE>Specification</CODE> instance representing the infoset
	 * specification as a whole.
	 * @since	TFP 1.6
	 */
	private static final Specification PRODUCT_INFOSET
		= new Specification ("PRODUCT_INFOSET");

	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * the version 1-0 product infoset.
	 * @since	TFP 1.6
	 */
	private static final SchemaRelease	R1_0
		= new SchemaRelease (PRODUCT_INFOSET, "1-0", "urn:handcoded:product-infoset",
                "product-infoset.xsd", "info", null, "productInfoset");

    /**
     * The <CODE>FPML_5_3__INFOSET_1_0</CODE> instance used to perform the
     * conversion.
     * @since	TFP 1.6
     */
    private static final FPML_5_3__INFOSET_1_0 conversion
        = new FPML_5_3__INFOSET_1_0 ();

	/**
	 * Prevent any instances from being created.
	 * @since	TFP 1.6
	 */
	private ProductInfoset ()
	{ }
}