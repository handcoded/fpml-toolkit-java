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

package com.handcoded.view.function;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import com.handcoded.view.Value;

/**
 * The <CODE>Conditional</CODE> class implements an XPath extension function
 * that returns either the second or third argument based on the value of the
 * first (e.g. 
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.9
 */
public final class Conditional implements XPathFunction
{
	/**
	 * Constructs a <CODE>Conditional</CODE> instance.
	 * @since	TFP 1.9
	 */
	public Conditional ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.9
	 */
	@SuppressWarnings("rawtypes")
	public Object evaluate(List args) throws XPathFunctionException
	{
		if (args.size () != 3)
			throw new XPathFunctionException ("The conditional function requires three arguments");
		
		return (Value.toBoolean (args.get (0)) ? args.get (1) : args.get(2));
	}
}