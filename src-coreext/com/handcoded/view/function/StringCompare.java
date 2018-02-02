// Copyright (C),2005-2017 HandCoded Software Ltd.
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
 * The <CODE>StringCompare</CODE> function implements a C style string
 * comparison. XPath 1.0 only supports = and != comparisions for string
 * values.
 * 
 * @author	BitWise
 * @since	TFP 1.9
 */
public final class StringCompare implements XPathFunction
{
	/**
	 * Constructs a <CODE>StringCompare</CODE> instance.
	 * @since	TFP 1.9
	 */
	public StringCompare ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.9
	 */
	@SuppressWarnings("rawtypes")
	public Object evaluate (List args) throws XPathFunctionException
	{
		if (args.size () != 2)
			throw new XPathFunctionException ("The StringCompare function requires two arguments");

		String lhs = Value.toString (args.get (0));
		String rhs = Value.toString (args.get (1));
		
		return (((lhs == null) || (rhs == null)) ? Boolean.FALSE : lhs.compareTo (rhs));
	}
}