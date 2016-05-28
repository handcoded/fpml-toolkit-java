// Copyright (C),2005-2006 HandCoded Software Ltd.
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

import java.io.Serializable;

/**
 * <CODE>Period</CODE> defines a type-safe enumeration of time units (day,
 * week, month, etc.).
 *
 * @author	BitWise
 * @version	$Id: Period.java 36 2006-08-31 20:27:56Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class Period implements Serializable
{
	/**
	 * A <CODE>Period</CODE> instance representing a day.
	 * @since	TFP 1.0
	 */
	public static final Period	DAY			= new Period ("D");
	
	/**
	 * A <CODE>Period</CODE> instance representing a week.
	 * @since	TFP 1.0
	 */
	public static final Period	WEEK		= new Period ("W");
	
	/**
	 * A <CODE>Period</CODE> instance representing a month.
	 * @since	TFP 1.0
	 */
	public static final Period	MONTH		= new Period ("M");
		
	/**
	 * A <CODE>Period</CODE> instance representing a year.
	 * @since	TFP 1.0
	 */
	public static final Period	YEAR		= new Period ("Y");
	
	/**
	 * A <CODE>Period</CODE> instance representing a term.
	 * @since	TFP 1.0
	 */
	public static final Period	TERM		= new Period ("T");

	/**
	 * Returns the <CODE>Period</CODE> that matches the given code.
	 *
	 * @param	code			The period code (e.g. D,W,M,Y).
	 * @return	The corresponding <CODE>Period</CODE> instance or <CODE>null
	 *			</CODE> if the code is invalid.
	 * @since	TFP 1.0
	 */
	public static Period forCode (final String code)
	{
		if (code.length () == 1) {
			switch (code.charAt (0)) {
			case 'D':	return (DAY);
			case 'W':	return (WEEK);
			case 'M':	return (MONTH);
			case 'Y':	return (YEAR);
			case 'T':	return (TERM);
			}
		}
		return (null);
	}
	
	/**
	 * Returns the hash value of the <CODE>Period</CODE> for hash based data
	 * structures and algorithms.
	 *
	 * @return	The hash value for the <CODE>Period</CODE>.
	 * @since	TFP 1.0
	 */
	public int hashCode ()
	{
		return (code.hashCode ());
	}
	
	/**
	 * Determines if this <CODE>Period</CODE> instance and another hold the same
	 * date.
	 *
	 * @param	other		The <CODE>Date</CODE> instance to compare with.
	 * @return	<CODE>true</CODE> if both instances represent the same date,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean equals (final Object other)
	{
		return ((other instanceof Period) && code.equals (((Period) other).code));
	}
	
	/**
	 * Converts the instance data members to a <CODE>String</CODE> representation
	 * that can be displayed for debugging purposes.
	 *
	 * @return 	The object's <CODE>String</CODE> representation.
	 * @since	TFP 1.0
	 */
	public final String toString ()
	{
		return (code);
	}
	
	/**
	 * Constructs a <CODE>Period</CODE> using the provided code string.
	 *
	 * @param	code			The period code (e.g. M)
	 * @since	TFP 1.0
	 */
	protected Period (final String code)
	{
		this.code = code;
	}
	
	/**
	 * Serialization UID
	 * @since	TFP 1.0
	 */
	private static final long serialVersionUID = 1385837233562531444L;

	/**
	 * The period code.
	 * @since	TFP 1.0
	 */
	private final String	code;
}