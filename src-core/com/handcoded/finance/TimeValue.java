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
import java.math.BigDecimal;

/**
 * Instances of the <CODE>TimeValue</CODE> are used to hold the time portion
 * of <CODE>Time</CODE> and <CODE>DateTime</CODE> values.
 * 
 * @author	BitWise
 * @version	$Id: TimeValue.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.1
 */
final class TimeValue implements ImmutableTime, Comparable<TimeValue>, Serializable
{
	/**
	 * Constant value used to range check seconds values.
	 * @since	TFP 1.1
	 */
	private static final BigDecimal 	SIXTY = new BigDecimal (60);
	
	/**
	 * A time value representing the first instant of the day.
	 * @since	TFP 1.1
	 */
	public static final TimeValue	START_OF_DAY
		= new TimeValue (0,0);
	
	/**
	 * A time value representing the last instant of the day.
	 * @since	TFP 1.1
	 */
	public static final TimeValue	END_OF_DAY
		= new TimeValue (24,0);
	
	/**
	 * Constructs a <CODE>TimeValue</CODE> instance based on the supplied hour
	 * and minute values.
	 * <P>
	 * A special value of 24:00:00 is allowed which is considered to the
	 * same as 00:00:00.
	 *
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @since	TFP 1.0
	 */
	public TimeValue (int hours, int minutes)
	{
		this (hours, minutes, BigDecimal.ZERO);	
	}
	
	/**
	 * Constructs a <CODE>TimeValue</CODE> instance based on the supplied hour,
	 * minute and seconds values.
	 * <P>
	 * A special value of 24:00:00 is allowed which is considered to the the
	 * same as 00:00:00.
	 *
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59).
	 * @since	TFP 1.0
	 */
	public TimeValue (int hours, int minutes, int seconds)
	{
		this (hours, minutes, new BigDecimal (seconds));	
	}

	/**
	 * Constructs a <CODE>TimeValue</CODE> instance based on the supplied hour,
	 * minute and seconds values.
	 * <P>
	 * A special value of 24:00:00 is allowed which is considered to the the
	 * same as 00:00:00.
	 *
	 * @param	hours			The number of hours (0-24)
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59.99).
	 * @since	TFP 1.1
	 */
	public TimeValue (int hours, int minutes, final BigDecimal seconds)
	{
		if (hours == 24) {
			if ((minutes != 0) || !seconds.equals(BigDecimal.ZERO))
				throw new IllegalArgumentException ("Minutes and seconds must be zero in end-of-day value");
		}
		else {
			if ((hours < 0) || (hours > 23))
				throw new IllegalArgumentException ("Invalid hours value");
			
			if ((minutes < 0) || (minutes > 59))
				throw new IllegalArgumentException ("Invalid minutes value");
			
			if ((seconds.compareTo(BigDecimal.ZERO) < 0) || (seconds.compareTo (SIXTY) >= 0))
				throw new IllegalArgumentException ("Invalid seconds value");
		}

		this.hours 	 = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	/**
	 * Provides access to the hours component of the time value.
	 *
	 * @return	The hours component of the time.
	 * @since	TFP 1.0
	 */
	public int hours ()
	{
		return (hours);
	}
	
	/**
	 * Provides access to the minutes component of the time value.
	 *
	 * @return	The minutes component of the time.
	 * @since	TFP 1.0
	 */
	public int minutes ()
	{
		return (minutes);
	}
	
	/**
	 * Provides access to the seconds component of the time value.
	 *
	 * @return	The seconds component of the time.
	 * @since	TFP 1.0
	 */
	public BigDecimal seconds ()
	{
		return (seconds);
	}
	
	/**
	 * Returns the hash value of the date for hash based data structures and
	 * algorithms.
	 *
	 * @return	The hash value for the date.
	 * @since	TFP 1.0
	 */
	public int hashCode ()
	{
		return ((hours * 60 + minutes) * 60 ^ seconds.hashCode ());
	}
	
	/**
	 * Determines if this <CODE>TimeValue</CODE> instance and another object hold
	 * the same date.
	 *
	 * @param	other			The <CODE>Object</CODE> instance to compare with.
	 * @return	<CODE>true</CODE> if both instances represent the same date,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.1
	 */
	public boolean equals (Object other)
	{
		return ((other instanceof TimeValue) && equals ((TimeValue) other));
	}
	
	/**
	 * Determines if this <CODE>TimeValue</CODE> instance and another hold the
	 * same date.
	 *
	 * @param	other			The <CODE>TimeValue</CODE> instance to compare with.
	 * @return	<CODE>true</CODE> if both instances represent the same date,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.1
	 */
	public boolean equals (TimeValue other)
	{
		return ((hours == other.hours) && (minutes == other.minutes)
					&& seconds.equals (other.seconds));
	}
	
	/**
	 * Returns the result of comparing this instance to another <CODE>TimeValue
	 * </CODE>.
	 *
	 * @param	other			The <CODE>TimeValue</CODE> instance to compare with.
	 * @return	An integer value indicating the relative ordering.
	 * @since	TFP 1.1
	 */
	public int compareTo (TimeValue other)
	{
		if (hours 	!= other.hours)   return (hours - other.hours);
		if (minutes != other.minutes) return (minutes - other.minutes);
		return (seconds.compareTo(other.seconds));
	}
	
	/**
	 * Converts the value of this <CODE>TimeValue</CODE> instance into a
	 * <CODE>String</CODE> for display in ISO format (e.g. HH:MM:SS[.S+]).
	 *
	 * @return	A <CODE>String</CODE> in the partial ISO time format
	 * 			representing the time
	 * @since	TFP 1.0
	 */
	public String toString ()
	{
		int			secs = seconds.intValue ();
		BigDecimal	fraction = seconds.remainder (BigDecimal.ONE);
		
		synchronized (buffer) {
			buffer.setLength (0);
			
			buffer.append ((char)('0' + hours / 10));
			buffer.append ((char)('0' + hours % 10));
			buffer.append (':');
			buffer.append ((char)('0' + minutes / 10));
			buffer.append ((char)('0' + minutes % 10));
			buffer.append (':');
			buffer.append ((char)('0' + secs / 10));
			buffer.append ((char)('0' + secs % 10));
			
			if (!fraction.equals (BigDecimal.ZERO)) {
				buffer.append ('.');
				do {
					fraction = fraction.movePointRight (1);
					buffer.append ((char)('0' + fraction.intValue ()));
					fraction = fraction.remainder(BigDecimal.ONE);
				} while (!fraction.equals(BigDecimal.ZERO));
			}
			
			return (buffer.toString ());
		}
	}
	
	/**
	 * Serialization UID
	 * @since	TFP 1.1
	 */
	private static final long serialVersionUID = -1173800701254321048L;

	/**
	 * A static buffer used to produce the <CODE>toString</CODE> value. The
	 * buffer must be synchronized before use to ensure thread safety.
	 * @since	TFP 1.0
	 */
	private static final StringBuffer	buffer = new StringBuffer ();
	
	/**
	 * The number of hours.
	 * @since	TFP 1.1
	 */
	private final int		hours;
	
	/**
	 * The number of minutes.
	 * @since	TFP 1.1
	 */
	private final int		minutes;
	
	/**
	 * The number of seconds
	 * @since	TFP 1.1
	 */
	private final BigDecimal seconds;
}
