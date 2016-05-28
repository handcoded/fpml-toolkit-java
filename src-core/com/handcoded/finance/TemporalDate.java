// Copyright (C),2005-2007 HandCoded Software Ltd.
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
 * The <CODE>TemporalDate</CODE> class defines a set of weekday constants
 * needed by both the <CODE>Date</CODE> and <CODE>DateTime</CODE> classes.
 * 
 * @author 	BitWise
 * @version	$Id: TemporalDate.java 209 2007-10-29 22:32:02Z andrew_jacobs $
 * @since	TFP 1.1
 */
public abstract class TemporalDate extends Temporal
{
	/**
	 * A constant value indicating the weekday Sunday.
 	 * @since	TFP 1.0
	 */
	public static final int		SUNDAY		= 1;
	
	/**
	 * A constant value indicating the weekday Monday.
	 * @since	TFP 1.0
	 */
	public static final int		MONDAY		= 2;
	
	/**
	 * A constant value indicating the weekday Tuesday.
	 * @since	TFP 1.0
	 */
	public static final int		TUESDAY		= 3;
	
	/**
	 * A constant value indicating the weekday Wednesday.
	 * @since	TFP 1.0
	 */
	public static final int		WEDNESDAY	= 4;
	
	/**
	 * A constant value indicating the weekday Thursday.
	 * @since	TFP 1.0
	 */
	public static final int		THURSDAY	= 5;
	
	/**
	 * A constant value indicating the weekday Friday.
	 * @since	TFP 1.0
	 */
	public static final int		FRIDAY		= 6;
	
	/**
	 * A constant value indicating the weekday Saturday.
	 * @since	TFP 1.0
	 */
	public static final int		SATURDAY	= 7;
		
	/**
	 * Constructs a <CODE>TemporalDate</CODE> instance having either a UTC
	 * time or no timezone depending on the argument.
	 * 
	 * @param	timeZone	The <CODE>TimeZone</CODE> value or <CODE>null</CODE>.	
	 * @since	TFP 1.1
	 */
	protected TemporalDate (TimeZone timeZone)
	{
		super (timeZone);
	}
}
