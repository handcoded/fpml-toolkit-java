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
 * The <CODE>Time</CODE> class provides an immutable representation for a
 * an ISO time value.
 *
 * @author	BitWise
 * @version	$Id: Time.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class Time extends Temporal
	implements ImmutableTime, Comparable<Time>, Serializable
{
	/**
	 * A constant <CODE>Time</CODE> instance representing the first instant of
	 * the day.
	 * @since	TFP 1.1
	 */
	public static final Time	START_OF_DAY
		= new Time (TimeValue.START_OF_DAY, null);
	
	/**
	 * A constant <CODE>Time</CODE> instance representing the last instant of
	 * the day.
	 * @since	TFP 1.1
	 */
	public static final Time	END_OF_DAY
		= new Time (TimeValue.END_OF_DAY, null);
	
	/**
	 * Constructs a <CODE>Time</CODE> instance based on the supplied hour and
	 * minute values.
	 * <P>
	 * A special value of 24:00:00 is allowed which is considered to the the
	 * same as 00:00:00.
	 *
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @since	TFP 1.0
	 */
	public Time (int hours, int minutes)
	{
		this (new TimeValue (hours, minutes, BigDecimal.ZERO), null);	
	}
	
	/**
	 * Constructs a <CODE>Time</CODE> instance based on the supplied hour,
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
	public Time (int hours, int minutes, int seconds)
	{
		this (new TimeValue (hours, minutes, seconds), null);	
	}
	
	/**
	 * Constructs a <CODE>Time</CODE> instance based on the supplied hour,
	 * minute and seconds values.
	 * <P>
	 * A special value of 24:00:00 is allowed which is considered to the the
	 * same as 00:00:00.
	 *
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59).
	 * @param	utc				Indicates UTC time zone.
	 * @since	TFP 1.0
	 */
	public Time (int hours, int minutes, int seconds, boolean utc)
	{
		this (new TimeValue (hours, minutes, seconds), utc ? TimeZone.UTC : null);	
	}
	
	/**
	 * Constructs a <CODE>Time</CODE> instance based on the supplied hour,
	 * minute and seconds values.
	 * <P>
	 * A special value of 24:00:00 is allowed which is considered to the the
	 * same as 00:00:00.
	 *
	 * @param	hours			The number of hours (0-24)
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59.99).
	 * @param	utc				Indicates UTC time zone.
	 * @since	TFP 1.1
	 */
	public Time (int hours, int minutes, final BigDecimal seconds, boolean utc)
	{
		this (new TimeValue (hours, minutes, seconds), utc ? TimeZone.UTC : null);
	}
	
	/**
	 * Constructs a <CODE>Time</CODE> instance based on the supplied hour,
	 * minute and seconds values.
	 *
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59).
	 * @param	offset			The time zone offset.
	 * @since	TFP 1.1
	 */
	public Time (int hours, int minutes, int seconds, int offset)
	{
		this (new TimeValue (hours, minutes, seconds), new TimeZone (offset));
	}
	
	/**
	 * Constructs a <CODE>Time</CODE> instance based on the supplied hour,
	 * minute and seconds values.
	 *
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59.99).
	 * @param	offset			The time zone offset.
	 * @since	TFP 1.1
	 */
	public Time (int hours, int minutes, final BigDecimal seconds, int offset)
	{
		this (new TimeValue (hours, minutes, seconds), new TimeZone (offset));
	}
	
	/**
	 * Parses a character string in the ISO time format and uses the extracted
	 * values to construct a <CODE>Time</CODE> instance.
	 *
	 * @param	text			The character string to be parsed.
	 * @return	A <CODE>Time</CODE> instance constructed from the parsed data.
	 *
	 * @throws	IllegalArgumentException If the character string is not in the
	 *			correct format. 
	 * @since	TFP 1.0
	 */
	public static Time parse (String text)
	{
		char		ch;
		
		if (text != null) text = text.trim();
		
		int			limit = text.length ();
		int			index = 0;
		
		while (true) {
			// Extract time components
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			int hours = (ch - '0') * 10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			hours += ch - '0'; ++index;
			
			if ((index >= limit) || (text.charAt (index++) != ':')) break;
			
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			int minutes = (ch - '0') * 10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			minutes += ch - '0'; ++index;
			
			if ((index >= limit) || (text.charAt (index++) != ':')) break;
			
			int start = index;
			if ((index >= limit) || !isDigit (text.charAt (index++))) break;
			if ((index >= limit) || !isDigit (text.charAt (index++))) break;
		
			if ((index < limit) && (text.charAt (index) == '.')) {
				do {
					++index;
				} while ((index < limit) && isDigit (text.charAt (index)));
			}
			BigDecimal seconds = new BigDecimal (text.substring (start, index));
			
			// Detect UTC time zone
			if ((index < limit) && (text.charAt (index) == 'Z')) {
				return (new Time (hours, minutes, seconds, true));
			}
			
			// Detect time offsets
			if ((index < limit) && (text.charAt (index) == '+')) {
				++index;
				if ((index >= limit) || !isDigit (ch = text.charAt (index))) break; 
				int offset = (ch - '0') * 600; ++index;
				if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
				offset += (ch - '0') * 60; ++index;
				
				if ((index >= limit) || (text.charAt (index++) != ':')) break;
				
				if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
				offset += (ch - '0') * 10; ++index;
				if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
				offset += (ch - '0'); ++index;

				return (new Time (hours, minutes, seconds, offset));
			}
				
			if ((index < limit)&& (text.charAt (index) == '-')) {
				++index;
				if ((index >= limit) || !isDigit (ch = text.charAt (index))) break; 
				int offset = (ch - '0') * 600; ++index;
				if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
				offset += (ch - '0') * 60; ++index;
				
				if ((index >= limit) || (text.charAt (index++) != ':')) break;
				
				if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
				offset += (ch - '0') * 10; ++index;
				if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
				offset += (ch - '0'); ++index;

				return (new Time (hours, minutes, seconds, -offset));
			}
			
			return (new Time (hours, minutes, seconds, false));
		}

		throw new IllegalArgumentException ("Value is not in ISO time format");
	}
		
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int hours ()
	{
		return (timeValue.hours ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int minutes ()
	{
		return (timeValue.minutes ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public BigDecimal seconds ()
	{
		return (timeValue.seconds ());
	}
	
	/**
	 * Returns the hash value of the date for hash based data structures and
	 * algorithms.
	 *
	 * @return	The hash value for the time.
	 * @since	TFP 1.0
	 */
	public int hashCode ()
	{
		if (timeZone != null)
			return (timeValue.hashCode () ^ timeZone.hashCode ());
		else
			return (timeValue.hashCode ());
	}
	
	/**
	 * Determines if this <CODE>Time</CODE> instance and another object hold
	 * the same time value.
	 *
	 * @param	other			The other <CODE>Object</CODE> to compare with.
	 * @return	<CODE>true</CODE> if both instances are times and represent the
	 * 			same value, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean equals (final Object other)
	{
		return ((other instanceof Time) && equals ((Time) other));
	}

	/**
	 * Determines if this <CODE>Time</CODE> instance and another hold the same
	 * time value.
	 * @param 	other			The other <CODE>Time</CODE> to compare with.
	 * @return	<CODE>true</CODE> if both itimes represent the same value,
	 * 			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.1
	 */
	public boolean equals (final Time other)
	{
		if ((timeZone == null) && (other.timeZone == null))
			return (timeValue.equals (other.timeValue));
		else if ((timeZone != null) && (other.timeZone != null) && timeZone.equals (other.timeZone))
			return (timeValue.equals (other.timeValue));
		else
			return (toDateTime ().equals (other.toDateTime ()));
	}	
	
	/**
	 * Returns the result of comparing this instance to another <CODE>Time
	 * </CODE>.
	 *
	 * @param	other			The <CODE>Time</CODE> instance to compare with.
	 * @return	An integer value indicating the relative ordering.
	 * @since	TFP 1.0
	 */
	public int compareTo (final Time other)
	{
		if ((timeZone == null) && (other.timeZone == null))
			return (timeValue.compareTo (other.timeValue));
		else if ((timeZone != null) && (other.timeZone != null) && timeZone.equals (other.timeZone))
			return (timeValue.compareTo (other.timeValue));
		else
		return (toDateTime ().compareTo (other.toDateTime ()));
	}
	
	/**
	 * Converts the value of this <CODE>Time</CODE> instance into a
	 * <CODE>String</CODE> for display.
	 * <P>
	 * The full ISO format looks like this:
	 * <P>
	 * <CODE>HH:MM:SS[.S+][[Z|+HH:MM|-HH:MM]]</CODE>
	 *
	 * @return	A <CODE>String</CODE> in the ISO time format representing the
	 *			<CODE>Time</CODE> value.
	 * @since	TFP 1.0
	 */
	public String toString ()
	{
		if (timeZone != null)
			return (timeValue.toString () + timeZone.toString ());
		else
			return (timeValue.toString ());
	}
	
	/**
	 * Constructs a <CODE>Time</CODE> from its time and time zone
	 * components.
	 * 
	 * @param 	timeValue		The <CODE>TimeValue</CODE> component.
	 * @param 	timeZone		The <CODE>TimeZone</CODE> component or <CODE>null</CODE>.
	 * @since	TFP 1.1
	 */
	protected Time (TimeValue timeValue, TimeZone timeZone)
	{
		super (timeZone);
		
		this.timeValue = timeValue;
	}
	
	/**
	 * Creates a <CODE>DateTime</CODE> instance from the current time.
	 * 
	 * @return	A <CODE>DateTime</CODE> instance.
	 * @since	TFP 1.1
	 */
	protected DateTime toDateTime ()
	{
		return (new DateTime (EPOCH, timeValue, timeZone));
	}
	
	/**
	 * Serialization UID
	 * @since	TFP 1.0
	 */
	private static final long serialVersionUID = -1184154490188640058L;

	/**
	 * The date epoch used when converting to a <CODE>DateTime</CODE>.
	 * @since	TFP 1.1
	 */
	private static final DateValue 	EPOCH = new DateValue (31, 12, 1971);
	
	/**
	 * The time part of the <CODE>Time</CODE>
	 * @since	TFP 1.1
	 */
	private final TimeValue		timeValue;
}