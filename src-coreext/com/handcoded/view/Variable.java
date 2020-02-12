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

package com.handcoded.view;

import javax.xml.namespace.QName;

/**
 * A <CODE>Variable</CODE> defines a XPath extractable value which can be
 * referenced in <CODE>Facet</CODE> expressions. Their key use is to hold
 * references to important sub-blocks within XML documents.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.9
 */
public final class Variable
{
	/**
	 * Constructs a <CODE>Variable</CODE> instance with the indicated name,
	 * type and XPath expression.
	 * 
	 * @param	name		The name of the variable.
	 * @param	type		The type of node the variable should hold.
	 * @param	expr		The XPath expression to extract it.
	 * @since	TFP 1.9
	 */
	public Variable (String name, QName type, String expr)
	{
		this.name = name;
		this.type = type;
		this.expr = expr;
	}
	
	/**
	 * Provides access to the name.
	 * 
	 * @return	The variable's name.
	 * @since	TFP 1.9
	 */
	public String getName ()
	{
		return (name);
	}
	
	/**
	 * Provides access to the type
	 * 
	 * @return	The variable's type.
	 * @since	TFP 1.9
	 */
	public QName getType ()
	{
		return (type);
	}
	
	/**
	 * Provides access to the variable's XPath expression.
	 * 
	 * @return	The variable's XPath expression.
	 * @since	TFP 1.9
	 */
	public String getExpr ()
	{
		return (expr);
	}
	
	/**
	 * The variable name.
	 * @since	TFP 1.9
	 */
	private final String	name;
	
	/**
	 * The variable type.
	 * @since	TFP 1.9
	 */
	private final QName		type;
	
	/**
	 * The variable XPath expression.
	 * @since	TFP 1.9
	 */
	private final String	expr;
}