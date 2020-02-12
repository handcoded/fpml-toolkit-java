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

package com.handcoded.meta;

import org.w3c.dom.Document;

/**
 * The <CODE>IndirectConversion</CODE> is used to chain multiple transformations
 * together to create a multistage transformation. <CODE>IndirectConversion
 * </CODE> instances are created during the search process to find a conversion
 * path between two releases.
 *  
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
final class IndirectConversion extends Conversion
{
	/**
	 * Constructs a <CODE>IndirectConversion</CODE> instance that connects
	 * two other <CODE>Conversion</CODE> instances.
	 * 
	 * @param 	first			The first <CODE>Conversion</CODE> to apply.
	 * @param 	second			The second <CODE>Conversion</CODE> to apply.
	 * @since	TFP 1.0
	 */
	public IndirectConversion (Conversion first, Conversion second)
	{
		this.first  = first;
		this.second = second;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public Release getSourceRelease ()
	{
		return (first.getSourceRelease ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public Release getTargetRelease ()
	{
		return (second.getTargetRelease ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public Document convert (Document document, Helper helper)
		throws ConversionException
	{
		return (second.convert (first.convert (document, helper), helper));
	}
	
	/**
	 * Creates a debugging string that shows the conversions that will become
	 * connected.
	 * 
	 * @return	A debugging string describing the instance.
	 * @since	TFP 1.0
	 */
	public String toString ()
	{
		return ("("+ first.toString () + "::" + second.toString () + ")");
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	protected final int complexity ()
	{
		return (first.complexity () + second.complexity ());
	}
	
	/**
	 * The <CODE>Conversion</CODE> to apply first.
	 * @since	TFP 1.0
	 */
	private final Conversion first;
	
	/**
	 * The <CODE>Conversion</CODE> to apply second.
	 * @since	TFP 1.0
	 */
	private final Conversion second;
}