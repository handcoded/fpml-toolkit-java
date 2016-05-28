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

package com.handcoded.fpml.util;

import org.w3c.dom.Element;

import com.handcoded.xml.DOM;

/**
 * Instances of the <CODE>Identifier</CODE> class are used to represent
 * scheme based identifiers like those for parties, trades and messages.
 * 
 * @author	BitWise
 * @version	$Id: Identifier.java 698 2012-11-30 18:15:39Z andrew_jacobs $
 * @since	TFP 1.6
 */
public final class Identifier
{
	/**
	 * Constructs an <CODE>Identifier</CODE> from the data contained in
	 * the indicated DOM <CODE>Element</CODE>.
	 * 
	 * @param	context			The DOM <CODE>Element</CODE>.
	 * @param	attributeName	The name of the scheme attribute.
	 * @since	TFP 1.6
	 */
	public Identifier (Element context, String attributeName)
	{
		this (context.getAttribute (attributeName), DOM.getInnerText (context));
	}
	
	/**
	 * Constructs an <CODE>Identifier</CODE> from the given data values.
	 * 
	 * @param	schemeUri		The qualifying scheme URI.
	 * @param	codeValue		The actual code value.
	 * @since	TFP 1.6
	 */
	public Identifier (final String schemeUri, final String codeValue)
	{
		this.schemeUri = schemeUri;
		this.codeValue = codeValue;
	}
	
	/**
	 * Returns the scheme URI.
	 * 
	 * @return	The scheme URI.
	 * @since	TFP 1.6
	 */
	public String getSchemeUri ()
	{
		return (schemeUri);
	}
	
	/**
	 * Returns the code value.
	 * 
	 * @return	The code value.
	 * @since	TFP 1.6
	 */
	public String getCodeValue ()
	{
		return (codeValue);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	public String toString ()
	{
		synchronized (buffer) {
			buffer.setLength (0);
			if (schemeUri != null) buffer.append (schemeUri);
			buffer.append (':');
			if (codeValue != null) buffer.append (codeValue);
			
			return (buffer.toString ());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	public int hashCode ()
	{
		return (toString ().hashCode ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	public boolean equals (final Object other)
	{
		return ((other instanceof Identifier) && equals ((Identifier) other));
	}
	
	/**
	 * Determines if two <CODE>Identifier</CODE> instance represent the same
	 * qualified value.
	 * 
	 * @param	other			The instance to compare with.
	 * @return	<CODE>true</CODE> if the two instances contain the same value.
	 * @since	TFP 1.6
	 */
	public boolean equals (final Identifier other)
	{
		return (equals (schemeUri, other.schemeUri) && equals (codeValue, other.codeValue));
	}
	
	/**
	 * A static buffer used to format the string value.
	 * @since	TFP 1.6
	 */
	private static StringBuffer	buffer		= new StringBuffer ();
	
	/**
	 * The qualifying scheme URI.
	 * @since	TFP 1.6
	 */
	private final String		schemeUri;

	/**
	 * The actual code value.
	 * @since	TFP 1.6
	 */
	private final String		codeValue;
	
	/**
	 * Compares to string or null values to determine if they have the same
	 * value.
	 * 
	 * @param	lhs			The left hand string or <CODE>null</CODE>.
	 * @param	rhs			The right hand string or <CODE>null</CODE>.
	 * @return 	<CODE>true</CODE> if both strings are <CODE>null</CODE>
	 * 			or have the same value.
	 * @since	TFP 1.6
	 */
	private static boolean equals (final String lhs, final String rhs)
	{
		return ((lhs == rhs) || ((lhs != null) && (rhs != null) && lhs.equals (rhs)));
	}
}