// Copyright (C),2005-2011 HandCoded Software Ltd.
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
 * Instances of the <CODE>TimeZone</CODE> represents offsets from UTC expressed
 * as a number of hours and minutes.
 * 
 * @author 	BitWise
 * @version	$Id: TimeZone.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.1
 */
public final class TimeZone implements Comparable<TimeZone>, Serializable
{
	/**
	 * A <CODE>TimeZone</CODE> instance representing UTC.
	 * @since	TFP 1.1
	 */
	public static final TimeZone UTC = new TimeZone (0);
	
	/**
	 * The smallest allowed timezone offset value.
	 * @since	TFP 1.1
	 */
	public static final int 	MIN_TIMEZONE_OFFSET	= -14 * 60;
	
	/**
	 * The largest allowed timezone offset value.
	 * @since	TFP 1.1
	 */
	public static final int 	MAX_TIMEZONE_OFFSET =  14 * 60;
	
	/**
	 * Constructs a <CODE>TimeZone</CODE> instance initialised with the
	 * offset for the default time zone where the application is executing.
	 * 
	 * @since	TFP 1.1
	 */
	public TimeZone ()
	{
		this (java.util.TimeZone.getDefault ().getRawOffset () / (60 * 1000));
	}

	/**
	 * Constructs a <CODE>TimeZone</CODE> instance having a specified
	 * offset value.
	 * 
	 * @param   offset			The timezone offset in minutes.
	 * @since	TFP 1.1
	 */
	public TimeZone (int offset)
	{
		if ((offset < MIN_TIMEZONE_OFFSET) || (offset > MAX_TIMEZONE_OFFSET))
			throw new IllegalArgumentException ("Invalue TimeZone offset value");
		
		this.offset = offset;
	}
	
	/**
	 * Provides access to the <CODE>TimeZone</CODE> offset value.
	 * 
	 * @return	The <CODE>TimeZone</CODE> offset in minutes.
	 * @since	TFP 1.1
	 */
	public int getOffset ()
	{
		return (offset);
	}
	
	/**
	 * Determines if the <CODE>TimeZone</CODE> is UTC.
	 * 
	 * @return <CODE>true</CODE> if the <CODE>TimeZone</CODE> is UTC,
	 * 		   <CODE>false</CODE> otherwise.
	 * @since TFP 1.1
	 */
	public boolean isUTC ()
	{
		return (equals (UTC));
	}

	/**
	 * Return a formatted represetation of the <CODE>TimeZone</CODE>. If the
	 * offset is zero this will be a 'Z' otherwise it will be '+HH:MM' or
	 * '-HH:MM'.
	 * 
	 * @return	The formatted value of the <CODE>TimeZone</CODE>.
	 * @since	TFP 1.1
	 */
	public String toString ()
	{
		if (offset == 0) return ("Z");
		
		synchronized (buffer) {
			int 				value;
			
			buffer.setLength (0);
			
			if (offset < 0) {
				buffer.append ('-');
				value = -offset;
			}
			else {
				buffer.append ('+');
				value =  offset;
			}
		
			int hours 	= value / 60;
			int minutes = value % 60;
			
			buffer.append ((char)('0' + hours / 10));
			buffer.append ((char)('0' + hours % 10));
			buffer.append (':');
			buffer.append ((char)('0' + minutes / 10));
			buffer.append ((char)('0' + minutes % 10));
			
			return (buffer.toString ());
		}
	}
	
	/**
	 * Returns the hash value of the instance for hash based data structures
	 * and algorithms.
	 *
	 * @return	The hash value for the <CODE>TimeZone</CODE>.
	 * @since	TFP 1.1
	 */
	public int hashCode ()
	{
		return (offset);
	}

	/**
	 * Determines if this <CODE>TimeZone</CODE> instance and another object
	 * hold the same time value.
	 *
	 * @param	other			The other <CODE>Object</CODE> to compare with.
	 * @return	<CODE>true</CODE> if both instances are <CODE>TimeZone</CODE>
	 * 			instances and represent the same value, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.1
	 */
	public boolean equals (Object other)
	{
		return ((other instanceof TimeZone) && equals ((TimeZone) other));
	}
	
	/**
	 * Determines if this instance and another <CODE>TimeZone</CODE> hold
	 * the same value.
	 * 
	 * @param 	other			The other <CODE>TimeZone</CODE>.
	 * @return	<CODE>true</CODE> if both instances represent the same value,
	 * 			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.1
	 */
	public boolean equals (TimeZone other)
	{
		return (offset == other.offset);
	}

	/**
	 * Returns the result of comparing this instance to another <CODE>TimeZone
	 * </CODE>.
	 *
	 * @param	other			The <CODE>TimeZone</CODE> instance to compare with.
	 * @return	An integer value indicating the relative ordering.
	 * @since	TFP 1.1
	 */
	public int compareTo (TimeZone other)
	{
		return (offset - other.offset);
	}
	
	/**
	 * Serialization UID
	 * @since	TFP 1.1
	 */
	private static final long 	serialVersionUID = -4159662197500707717L;

	/**
	 * Buffer area used for formatting.
	 * @since	TFP 1.1
	 */
	private static StringBuffer	buffer	= new StringBuffer ();
	
	/**
	 * Represents an offset in minutes from UTC.
	 * @since	TFP 1.1
	 */
	private final int			offset;
}