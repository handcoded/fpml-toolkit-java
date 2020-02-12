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
 * Each <CODE>Facet</CODE> instance represents a value that can be extracted
 * from an XML document using an XPath expression.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.9
 */
public final class Facet
{
	/**
	 * Constructs a new <CODE>Facet</CODE> instance with the given name,
	 * target type and XPath expression.
	 * 
	 * @param	name		The name of <CODE>Facet</CODE> in the result set.
	 * @param	type		The target type of the <CODE>Facet</CODE> value.
	 * @param	expr		The XPath expression string.
	 * @since	TFP 1.9
	 */
	public Facet (String name, QName type, String expr)
	{
		this.name = name;
		this.type = type;
		this.expr = expr;
	}
	
	/**
	 * Provides access the <CODE>Facet</CODE> name.
	 * 
	 * @return	The <CODE>Facet</CODE> name.
	 * @since	TFP 1.9
	 */
	public String getName ()
	{
		return (name);
	}
	
	/**
	 * Provides access the <CODE>Facet</CODE> type.
	 * 
	 * @return	The <CODE>Facet</CODE> type.
	 * @since	TFP 1.9
	 */
	public QName getType ()
	{
		return (type);
	}
	
	/**
	 * Provides access the <CODE>Facet</CODE> XPath expression.
	 * 
	 * @return	The <CODE>Facet</CODE> XPath expression.
	 * @since	TFP 1.9
	 */
	public String getExpr ()
	{
		return (expr);
	}
	
	/**
	 * The name of the <CODE>Facet</CODE>.
	 * @since	TFP 1.9
	 */
	private final String 	name;
	
	/**
	 * The type of the <CODE>Facet</CODE>.
	 * @since	TFP 1.9
	 */
	private final QName		type;
	
	/**
	 * The XPath used to obtain the <CODE>Facet</CODE> value.
	 * @since	TFP 1.9
	 */
	private final String	expr;
}