// Copyright (C),2005-2008 HandCoded Software Ltd.
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

import com.handcoded.finance.Interval;
import com.handcoded.finance.Period;
import com.handcoded.xml.DOM;
import com.handcoded.xml.Logic;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XPath;

/**
 * The <CODE>FpMLRuleSet</CODE> is the abstract base class used by all other
 * FpML rule sets.
 * 
 * @author 	BitWise
 * @version	$Id: FpMLRuleSet.java 619 2012-04-02 21:20:52Z andrew_jacobs $
 * @since	TFP 1.1
 */
public abstract class FpMLRuleSet extends Logic
{
	/**
	 * Constructs a <CODE>FpMLRuleSet</CODE>
	 * @since	TFP 1.1
	 */
	protected FpMLRuleSet ()
	{ }
	
	/**
	 * Determine if two <CODE>Element</CODE> structures containing
	 * <B>currency</B> instances have the same currency codes belonging to
	 * the same currency code scheme.
	 * 
	 * @param	ccy1			The first currency <CODE>Element</CODE>.
	 * @param	ccy2			The second currency <CODE>Element</CODE>.
	 * @return	<CODE>true</CODE> if both <B>currency</B> structures have the same code.
	 * @since	TFP 1.2
	 */
	protected static boolean isSameCurrency (final Element ccy1, final Element ccy2)
	{
		String		uri1	= DOM.getAttribute (ccy1, "currencyScheme");
		String		uri2	= DOM.getAttribute (ccy2, "currencyScheme");
		
        if ((uri1 == null) && (uri2 == null))
            return (equal (ccy1, ccy2));

        if ((uri1 != null) && (uri2 != null) && uri1.equals (uri2))
			return (equal (ccy1, ccy2));
		
		return (false);
	}
	
	/**
	 * Extracts an <CODE>Interval</CODE> from the data stored below the
	 * given context node.
	 *
	 * @param	context		The context <CODE>Element</CODE>.
	 * @return	An <CODE>Interval</CODE> constructed from the stored data.
	 * @since	TFP 1.0
	 */
	protected static Interval toInterval (Element context)
	{
		if (context != null) {
			try {
				return (new Interval (
					toInteger (XPath.path (context, "periodMultiplier")),
					Period.forCode (toToken (XPath.path (context, "period")))));
			}
			catch (Exception error) {
				return (null);
			}
		}
		return (null);
	}

	/**
	 * Determines the namespace URI of the FpML document.
	 * 
	 * @param 	nodeIndex		A <CODE>NodeIndex</CODE> of the entire document.
	 * @return	A <CODE>String</CODE> containing the namespace URI.
	 * @since	TFP 1.1
	 */
	protected static String determineNamespace (final NodeIndex nodeIndex)
	{
		return (nodeIndex.getDocument ().getDocumentElement ().getNamespaceURI ());
	}
}