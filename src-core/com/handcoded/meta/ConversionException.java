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

/**
 * Instance of <CODE>ConversionException</CODE> are thrown when irrecoverable
 * problems are detected during document conversion.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.3
 */
public final class ConversionException extends Exception
{
	/**
	 * Serialization UID.
	 */
	private static final long serialVersionUID = -5088338511162618659L;

	/**
	 * Constructs a <CODE>ConversionException</CODE> with a given message
	 * string.
	 * 
	 * @param	message		The exception detail string.
	 */
	public ConversionException (final String message)
	{
		super (message);
	}
}