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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The <CODE>DateTime</CODE> class provides an immutable representation for a
 * an ISO datetime value.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.1
 */
public final class DateTime extends TemporalDate
	implements ImmutableDate, ImmutableTime, Comparable<DateTime>, Serializable
{
	/**
	 * The earliest possible date that can be correctly represented,
	 * @since	TFP 1.1
	 */
	public static final DateTime	MIN_VALUE
		= new DateTime (DateValue.MIN_VALUE, TimeValue.START_OF_DAY, null);
	
	/**
	 * The latest possible date that can be correctly represented.
	 * @since	TFP 1.1
	 */
	public static final DateTime	MAX_VALUE
		= new DateTime (DateValue.MAX_VALUE, TimeValue.END_OF_DAY, null);

	/**
	 * Constructs a <CODE>DateTime</CODE> instance given all the date and
	 * time components.
	 *
	 * @param	day				The day of the month (1-31).
	 * @param	month			The month of the year (1-12).
	 * @param	year			The year (1900-2099).
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59).
	 * @since	TFP 1.0
	 */
	public DateTime (final int day, final int month, final int year,
			final int hours, final int minutes, final int seconds)
	{
		this (new DateValue (day, month, year),
			  new TimeValue (hours, minutes, seconds), null);
	}

	/**
	 * Constructs a <CODE>DateTime</CODE> instance given all the date and
	 * time components and a flag indicating UTC time or not. 
	 *
	 * @param	day				The day of the month (1-31).
	 * @param	month			The month of the year (1-12).
	 * @param	year			The year (1900-2099).
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59).
	 * @param	utc				Indicates UTC time zone.
	 * @since	TFP 1.0
	 */
	public DateTime (final int day, final int month, final int year,
			final int hours, final int minutes, final int seconds, final boolean utc)
	{
		this (new DateValue (day, month, year),
			  new TimeValue (hours, minutes, seconds),
			  utc ? TimeZone.UTC : null);
	}

	/**
	 * Constructs a <CODE>DateTime</CODE> instance given all the date and
	 * time components plus a time zone offset.
	 * 
	 * @param	day				The day of the month (1-31).
	 * @param	month			The month of the year (1-12).
	 * @param	year			The year (1900-2099).
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59).
	 * @param	offset			The time zone offset.
	 * @since	TFP 1.0
	 */
	public DateTime (final int day, final int month, final int year,
			final int hours, final int minutes, final int seconds, final int offset)
	{
		this (new DateValue (day, month, year),
			  new TimeValue (hours, minutes, seconds),
			  new TimeZone (offset));
	}

	/**
	 * Constructs a <CODE>DateTime</CODE> instance given all the date and
	 * time components and a flag indicating UTC time or not. 
	 *
	 * @param	day				The day of the month (1-31).
	 * @param	month			The month of the year (1-12).
	 * @param	year			The year (1900-2099).
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59.99).
	 * @param	utc				Indicates UTC time zone.
	 * @since	TFP 1.0
	 */
	public DateTime (final int day, final int month, final int year,
			final int hours, final int minutes, final BigDecimal seconds, final boolean utc)
	{
		this (new DateValue (day, month, year),
				  new TimeValue (hours, minutes, seconds),
				  utc ? TimeZone.UTC : null);
	}

	/**
	 * Constructs a <CODE>DateTime</CODE> instance given all the date and
	 * time components plus a time zone offset.
	 *
	 * @param	day				The day of the month (1-31).
	 * @param	month			The month of the year (1-12).
	 * @param	year			The year (1900-2099).
	 * @param	hours			The number of hours (0-24).
	 * @param	minutes			The number of minutes (0-59).
	 * @param 	seconds			The number of seconds (0-59.99).
	 * @param	offset			The time zone offset.
	 * @since	TFP 1.0
	 */
	public DateTime (final int day, final int month, final int year,
			final int hours, final int minutes, final BigDecimal seconds, final int offset)
	{
		this (new DateValue (day, month, year),
				  new TimeValue (hours, minutes, seconds),
				  new TimeZone (offset));
	}

	/**
	 * Parses a <CODE>DateTime</CODE> instance from a character string in the
	 * ISO date format (as produced by <CODE>toString</CODE>).
	 *
	 * @param	text			The text string to be parsed.
	 * @return	A new <CODE>DateTime</CODE> instance containing the parsed data.
	 *
	 * @throws	IllegalArgumentException If the character string is not in the
	 *			correct format.
	 * @since	TFP 1.0
	 */
	public static DateTime parse (String text)
	{
		char		ch;
		
		if (text != null) text = text.trim();
		
		int			limit = text.length ();
		int			index = 0;
		
		while (true) {		// NOSONAR
			// Extract date components
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			int year = (ch - '0') * 1000; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			year += (ch - '0') * 100; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			year += (ch - '0') *10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			year += (ch - '0'); ++index;
			
			if ((index >= limit) || (text.charAt (index++) != '-')) break;
			
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			int month = (ch - '0') * 10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			month += (ch - '0'); ++index;
			
			if ((index >= limit) || (text.charAt (index++) != '-')) break;
			
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			int day = (ch - '0') * 10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			day += (ch - '0'); ++index;

			if ((index >= limit) || (text.charAt (index++) != 'T')) break;

			// Extract time components
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			int hours = (ch - '0') * 10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			hours += (ch - '0'); ++index;
			
			if ((index >= limit) || (text.charAt (index++) != ':')) break;
			
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			int minutes = (ch - '0') * 10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;		// NOSONAR
			minutes += (ch - '0'); ++index;
			
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

			// Detect zulu time zone
			if ((index < limit)&& (text.charAt (index) == 'Z')) {
				return (new DateTime (day, month, year, hours, minutes, seconds, true));
			}
			
			// Detect time offsets
			if ((index < limit)&& (text.charAt (index) == '+')) {
				++index;
				if ((index >= limit) && !isDigit (ch = text.charAt (index++))) break; 	// NOSONAR
				int offset = (ch - '0') * 600;
				if ((index >= limit) && !isDigit (ch = text.charAt (index++))) break;	// NOSONAR
				offset += (ch - '0') * 60;
				
				if ((index >= limit) && (text.charAt (index++) != ':')) break;
				
				if ((index >= limit) && !isDigit (ch = text.charAt (index++))) break;	// NOSONAR
				offset += (ch - '0') * 10;
				if ((index >= limit) && !isDigit (ch = text.charAt (index++))) break;	// NOSONAR
				offset += (ch - '0');

				return (new DateTime (day, month, year, hours, minutes, seconds, offset));
			}
				
			if ((index < limit)&& (text.charAt (index) == '-')) {
				++index;
				if ((index >= limit) && !isDigit (ch = text.charAt (index++))) break; 	// NOSONAR
				int offset = (ch - '0') * 600;
				if ((index >= limit) && !isDigit (ch = text.charAt (index++))) break;	// NOSONAR
				offset += (ch - '0') * 60;
				
				if ((index >= limit) && (text.charAt (index++) != ':')) break;
				
				if ((index >= limit) && !isDigit (ch = text.charAt (index++))) break;	// NOSONAR
				offset += (ch - '0') * 10;
				if ((index >= limit) && !isDigit (ch = text.charAt (index++))) break;	// NOSONAR
				offset += (ch - '0');

				return (new DateTime (day, month, year, hours, minutes, seconds, -offset));
			}
			
			return (new DateTime (day, month, year, hours, minutes, seconds, false));
		}

		throw new IllegalArgumentException ("Value is not in ISO date & time format");
	}
	
	/**
	 * Returns the current UTC or local time as <CODE>DateTime</CODE> value.
	 * 
	 * @param 	utc			Indicates if the UTC or local time is required
	 * @return	The current UTC or local time as <CODE>DateTime</CODE> value.
	 * @since	TFP 1.8
	 */
	public static DateTime now (boolean utc)
	{
		java.util.TimeZone tz = utc ? java.util.TimeZone.getTimeZone ("UTC")
									: java.util.TimeZone.getDefault ();
		java.util.Calendar cal = new GregorianCalendar (tz);
		
		return (new DateTime (
					new DateValue (cal.get (java.util.Calendar.DAY_OF_MONTH),
							cal.get (java.util.Calendar.MONTH) - Calendar.JANUARY + 1,
							cal.get(java.util.Calendar.YEAR)),
					new TimeValue (cal.get (java.util.Calendar.HOUR_OF_DAY),
							cal.get (java.util.Calendar.MINUTE),
							new BigDecimal (cal.get (java.util.Calendar.SECOND) * 1000
									+ cal.get (java.util.Calendar.MILLISECOND)).divide (THOUSAND)),
					utc ? TimeZone.UTC : null));
	}

	/**
	 * Determines if a year is a leap year.
	 *
	 * @param	year			The year to test (1900-2099)
	 * @return	<CODE>true</CODE> if the year is a leap year, <CODE>false</CODE>
	 *			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean isLeapYear (final int	year)
	{
		return (DateValue.isLeapYear (year));
	}
	
	/**
	 * Returns the number of days in the given month in a leap or non-leap
	 * year.
	 *
	 * @param	month			The month number (1-12).
	 * @param	leapYear		Flag to indicate a leap year.
	 * @return	The length of the given month in the indicated type of year.
	 * @since	TFP 1.0
	 */
	public static int monthLength (final int month, final boolean leapYear)
	{
		return (DateValue.monthLength (month, leapYear));
	}
	
	/**
	 * Returns the number of days in the given month for the specified year.
	 *
	 * @param	month			The month number (1-12).
	 * @param	year			The year (1900-2099).
	 * @return	The length of the given month in the specified year.
	 * @since	TFP 1.0
	 */
	public static int monthLength (final int month, final int year)
	{
		return (DateValue.monthLength (month, year));
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int weekday ()
	{
		return (dateValue.weekday ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int dayOfMonth ()
	{
		return (dateValue.dayOfMonth ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int lastDayOfMonth ()
	{
		return (dateValue.lastDayOfMonth ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public boolean isEndOfMonth ()
	{
		return (dateValue.isEndOfMonth ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int dayOfYear ()
	{
		return (dateValue.dayOfYear ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int month ()
	{
		return (dateValue.month ());
	}	
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int year ()
	{
		return (dateValue.year ());
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
	 * Creates a new <CODE>Date</CODE> based on the current instance and a given
	 * number of days adjustment.
	 *
	 * @param	days			The number of days to adjust by.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public DateTime plusDays (final int days)
	{
		return (new DateTime (dateValue.plusDays(days), timeValue, timeZone));
	}
	
	/**
	 * Creates a new <CODE>Date</CODE> based on the current instance and a given
	 * number of weeks adjustment.
	 *
	 * @param	weeks			The number of weeks to adjust by.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public DateTime plusWeeks (final int weeks)
	{
		return (new DateTime (dateValue.plusWeeks (weeks), timeValue, timeZone));
	}
	
	/**
	 * Creates a new <CODE>DateTime</CODE> based on the current instance and a given
	 * number of months adjustment.
	 *
	 * @param	months			The number of months to adjust by.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public DateTime plusMonths (final int months)
	{
		return (new DateTime (dateValue.plusMonths (months), timeValue, timeZone));
	}
	
	/**
	 * Creates a new <CODE>DateTime</CODE> based on the current instance and a given
	 * number of years adjustment.
	 *
	 * @param	years 			The number of years to adjust by.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public DateTime plusYears (final int years)
	{
		return (new DateTime (dateValue.plusYears (years), timeValue, timeZone));
	}
	
	/**
	 * Creates a new <CODE>DateTime</CODE> based on the current instance and a given
	 * <CODE>Interval</CODE>.
	 *
	 * @param	interval		The time <CODE>Interval</CODE>.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public DateTime plus (Interval interval)
	{
		return (new DateTime (dateValue.plus (interval), timeValue, timeZone));
	}
	
	/**
	 * Creates a <CODE>Date</CODE> instance based in the date component values
	 * of the current instance.
	 * 
	 * @return	A <CODE>Date</CODE> instance.
	 * @since	TFP 1.1
	 */
	public Date toDate ()
	{
		return (new Date (dateValue, timeZone));
	}
	
	/**
	 * Creates a <CODE>Time</CODE> instance based in the time component values
	 * of the current instance.
	 * 
	 * @return	A <CODE>Time</CODE> instance.
	 * @since	TFP 1.1
	 */
	public Time toTime ()
	{
		return (new Time (timeValue, timeZone));
	}
	
	/**
	 * Uses the timezone information to create a UTC normalised <CODE>DateTime
	 * </CODE> from the current instance.
	 * 
	 * @return	The normalised <CODE>DateTime</CODE> instance.
	 * @since	TFP 1.1
	 */
	public DateTime normalize ()
	{
		// Already in UTC?
		if ((timeZone != null) && timeZone.isUTC ()) return (this);
		
		int offset = ((timeZone != null) ? timeZone : getImplicitTimeZone ()).getOffset ();
		
		int dt = dayOfMonth ();
		int mo = month ();
		int yr = year ();
		int hr = hours ()   - offset / 60;
		int mn = minutes () - offset % 60;
		
		// Rolled into previous day?
		while (mn < 0) {
			mn += 60;
			if (--hr < 0) {
				hr = 23;
				if (--dt < 1) {
					if (--mo < 1) {
						mo = 12;
						--yr;
					}
					dt = monthLength (mo, yr);
				}
			}
		}
		while (hr < 0) {
			hr += 24;
			if (--dt < 1) {
				if (--mo < 1) {
					mo = 12;
					--yr;
				}
				dt = monthLength (mo, yr);
			}
		}
		
		// Rolled into next day?
		while (mn > 59) {
			mn -= 60;
			if (++hr > 23) {
				hr = 0;
				if (++dt > monthLength (mo, yr)) {
					if (++mo > 12) {
						mo = 1;
						++yr;
					}
					dt = 1;
				}
			}
		}
		while (hr > 23) {
			hr -= 24;
			if (++dt > monthLength (mo, yr)) {
				if (++mo > 12) {
					mo = 1;
					++yr;
				}
				dt = monthLength (mo, yr);
			}
		}
		
		return (new DateTime (dt, mo, yr, hr, mn, seconds (), true));
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
		if (timeZone != null)
			return (dateValue.hashCode() ^ timeValue.hashCode() ^ timeZone.hashCode ());
		else
			return (dateValue.hashCode() ^ timeValue.hashCode());
	}
	
	/**
	 * Determines if this <CODE>DateTime</CODE> instance and another object hold
	 * the same value.
	 *
	 * @param	other			The other <CODE>Object</CODE> to compare with.
	 * @return	<CODE>true</CODE> if both instances are datetimes and represent the
	 * 			same value, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean equals (final Object other)
	{
		return ((other instanceof DateTime) && (equals ((DateTime) other)));
	}

	/**
	 * Determines if this <CODE>Time</CODE> instance and another hold the same
	 * time value.
	 * @param 	other			The other <CODE>Time</CODE> to compare with.
	 * @return	<CODE>true</CODE> if both itimes represent the same value,
	 * 			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.1
	 */
	public boolean equals (final DateTime other)
	{
		DateTime	lhs = normalize ();
		DateTime	rhs = other.normalize ();
		
		return (lhs.dateValue.equals (rhs.dateValue) && lhs.timeValue.equals (rhs.timeValue));
	}
	
	/**
	 * Returns the result of comparing this instance to another <CODE>Time
	 * </CODE>.
	 *
	 * @param	other			The <CODE>Time</CODE> instance to compare with.
	 * @return	An integer value indicating the relative ordering.
	 * @since	TFP 1.0
	 */
	public int compareTo (final DateTime other)
	{
		DateTime	lhs = normalize ();
		DateTime	rhs = other.normalize ();
		
		int	result = lhs.dateValue.compareTo (rhs.dateValue);
		if (result != 0) return (result);
		return (lhs.timeValue.compareTo (rhs.timeValue));
	}
	
	/**
	 * Converts the value of this <CODE>Time</CODE> instance into a
	 * <CODE>String</CODE> for display.
	 *
	 * @return	A <CODE>String</CODE> in the format HH:MM:SS representing the
	 *			<CODE>Time</CODE> value.
	 * @since	TFP 1.0
	 */
	public String toString ()
	{
		if (timeZone != null)
			return (dateValue.toString() + "T" + timeValue.toString() + timeZone.toString());
		else
			return (dateValue.toString() + "T" + timeValue.toString());
	}
	
	/**
	 * Constructs a <CODE>DateTime</CODE> instance from its date, time and
	 * time zone components.
	 * 
	 * @param 	dateValue		The <CODE>DateValue</CODE> component.
	 * @param 	timeValue		The <CODE>TimeValue</CODE> component.
	 * @param 	timeZone		The <CODE>TimeZone</CODE> component or <CODE>null</CODE>.
	 * @since	TFP 1.1
	 */
	protected DateTime (DateValue dateValue, TimeValue timeValue, TimeZone timeZone)
	{
		super (timeZone);
		
		this.dateValue = dateValue;
		this.timeValue = timeValue;
	}
	
	/**
	 * Serialization UID
	 * @since	TFP 1.1
	 */
	private static final long serialVersionUID = 2153258690295593096L;
	
	/**
	 * A constant used to create seconds values.
	 * @since	TFP 1.8
	 */
	private static final BigDecimal	THOUSAND = new BigDecimal (1000);
	
	/**
	 * The date part of the <CODE>DateTime</CODE>
	 * @since	TFP 1.1
	 */
	private final DateValue		dateValue;
	
	/**
	 * The time part of the <CODE>DateTime</CODE>
	 * @since	TFP 1.1
	 */
	private final TimeValue		timeValue;
}