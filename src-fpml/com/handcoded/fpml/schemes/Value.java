// Copyright (C),2005-2013 HandCoded Software Ltd.
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

package com.handcoded.fpml.schemes;

/**
 * The <CODE>Value</CODE> class is used to represent code values within their
 * corresponding <CODE>Scheme</CODE> instance's extent set.
 *
 * @author	BitWise
 * @version	$Id: Value.java 733 2013-05-31 21:28:22Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class Value
{
	/**
	 * Constructs a <CODE>Value</CODE> given a code, its source (e.g. who
	 * defines it - FpML or a someone else) and a description.
	 *
	 * @param	code		The code string.
	 * @param	source		The source identifier.
	 * @param	description	Some descriptive text.
	 * @since	TFP 1.0
	 */
	public Value (final String code, final String source, final String description)
	{
		this.code		 = code;
		this.source 	 = source;
		this.description = description;
	}
	
	/**
	 * Provides access to the code string.
	 *
	 * @return 	The code string.
	 * @since	TFP 1.0
	 */
	public String getCode ()
	{
		return (code);
	}
	
	/**
	 * Provides access to the source identifier.
	 *
	 * @return 	The source identifier.
	 * @since	TFP 1.0
	 */
	public String getSource ()
	{
		return (source);
	}
	
	/**
	 * Provides access to the description.
	 *
	 * @return	The description string.
	 * @since	TFP 1.0
	 */
	public String getDescription ()
	{
		return (description);
	}

	/**
	 * Returns the hash code for the instance based on the value of the code
	 * string.
	 *
	 * @return 	The hash code. 
	 * @since	TFP 1.0
	 */
	@Override
	public int hashCode ()
	{
		return (code.hashCode());
	}
	
	/**
	 * Converts the instance data into a displayable string.
	 *
	 * @return	A displayable string.
	 * @since	TFP 1.0
	 */
	@Override
	public String toString ()
	{
		return (getClass ().getName () + "{code=" + code + ",source=" + source + ",description=" + description);
	}
	
	/**
	 * The code value string.
	 * @since	TFP 1.0
	 */
	private final String	code;
	
	/**
	 * The source identifier string.
	 * @since	TFP 1.0
	 */
	private final String	source;
	
	/**
	 * The description string.
	 * @since	TFP 1.0
	 */
	private final String	description;
}