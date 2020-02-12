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

package com.handcoded.fpml.classification.function;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handcoded.fpml.Releases;
import com.handcoded.fpml.meta.SchemeAccess;
import com.handcoded.fpml.schemes.Scheme;
import com.handcoded.meta.Release;
import com.handcoded.xml.DOM;

/**
 * Implements an XPath extension function that checks of the text value of an
 * element is the name of an OIS index.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.11
 */
public class IsOisIndex implements XPathFunction
{
	/**
	 * Constructs an <CODE>IsOisIndex</CODE> instance.
	 * @since	TFP 1.11
	 */
	public IsOisIndex ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * @since 	TFP 1.11
	 */
	@SuppressWarnings ("rawtypes")
	public Object evaluate (List args)
			throws XPathFunctionException
	{
		// Check parameters	
		if (args.size () != 1)
			throw new XPathFunctionException ("The IsOisIndex function requires one argument");
		
		if (!(args.get (0) instanceof Element))
			throw new XPathFunctionException ("The argument MUST be an element");
		
		Element	element = (Element) args.get (0);
		String value = DOM.getInnerText (element);
		
		if (value == null) return (Boolean.FALSE);
		
		// Check against the list of OIS indexes
		Document document = element.getOwnerDocument ();
		Release release	= Releases.releaseFor (document);
		
		if ((release == null) || !(release instanceof SchemeAccess))
			throw new XPathFunctionException ("The document is not a known FpML release");
		
		Scheme scheme = ((SchemeAccess) release).getSchemeCollection ()
				.findSchemeForUri ("urn:handcoded:floating-rate-index:ois");
		
		return (scheme.isValid (value.trim ()) ? Boolean.TRUE : Boolean.FALSE);
	}
}