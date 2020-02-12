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

package com.handcoded.finance;

/**
 * The <CODE>Temporal</CODE> class provides storage for the optional timezone
 * offset portion of <CODE>Date</CODE>, <CODE>DateTime</CODE> and <CODE>Time</CODE>
 * instances.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.1
 */
public abstract class Temporal
{
	/**
	 * Provides access to the implicit <CODE>TimeZone</CODE>.
	 * 
	 * @return	The implicit <CODE>TimeZone</CODE> instance.
	 * @since	TFP 1.1
	 */
	public static TimeZone getImplicitTimeZone ()
	{
		return (implicitTimeZone);
	}
	
	/**
	 * Changes the value of the implicit <CODE>TimeZone</CODE>.
	 * 
	 * @param 	value		The <CODE>TimeZone</CODE> to be used.
	 * @since	TFP 1.1
	 */
	public static void setImplicitTimeZone (TimeZone value)
	{
		implicitTimeZone = value;
	}
	
	/**
	 * Provides access to the timezone offset if any was specified.
	 * 
	 * @return	The time zone offset in minutes.
	 * @since	TFP 1.1
	 */
	public final TimeZone getTimeZone ()
	{
		return (timeZone);
	}
	
	/**
	 * Constructs a <CODE>Temporal</CODE> instance having either a UTC
	 * time or no timezone depending on the argument.
	 * 
	 * @param	timeZone	The <CODE>TimeZone</CODE> value or <CODE>null</CODE>.	
	 * @since	TFP 1.1
	 */
	protected Temporal (TimeZone timeZone)
	{
		this.timeZone = timeZone;
	}
		
	/**
	 * Determines if a character is a digit (e.g. 0..9). Used by parsing
	 * methods in derived classes.
	 * 
	 * @param 	ch				The character to be tested
	 * @return	<CODE>true</CODE> if the character is a decimal digit.
	 * @since	TFP 1.1
	 */
	protected static boolean isDigit (char ch)
	{
		return (('0' <= ch) && (ch <= '9'));
	}
	
	/**
	 * Holds the default <CODE>TimeZone</CODE> for the executing application.
	 * @since	TFP 1.1
	 */
	private static TimeZone		implicitTimeZone = new TimeZone ();
					
	/**
	 * Holds the <CODE>TimeZone</CODE> if present.
	 * @since	TFP 1.1
	 */
	protected final TimeZone	timeZone;
}