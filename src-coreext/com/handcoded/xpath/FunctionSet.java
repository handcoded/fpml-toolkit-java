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

package com.handcoded.xpath;

import java.util.HashMap;

import javax.xml.namespace.QName;

/**
 * Instances of <CODE>FunctionSet</CODE> are used to hold the details of user
 * defined XPath functions used by the classification and view utilities to
 * allow custom run-time functions to be applied to XML data.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.11
 */
public class FunctionSet
{
	/**
	 * Constructs an empty <CODE>FunctionSet</CODE> instance.
	 * @since	TFP 1.11
	 */
	public FunctionSet ()
	{ }
	
	public Function put (QName name, Function function)
	{
		return (functions.put (name, function));
	}
	
	public Function get (QName name)
	{
		return (functions.get (name));
	}

	private HashMap<QName, Function>	functions
		= new HashMap<> ();
}