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
 * The <CODE>Date</CODE> class provides an immutable representation for a
 * an ISO date value.
 *
 * @author	BitWise
 * @version	$Id: Date.java 581 2012-01-09 20:08:00Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class Date extends TemporalDate
	implements ImmutableDate, Comparable<Date>, Serializable
{
	/**
	 * The earliest possible date that can be correctly represented,
	 * @since	TFP 1.0
	 */
	public static final Date	MIN_VALUE
		= new Date (DateValue.MIN_VALUE, null);
	
	/**
	 * The latest possible date that can be correctly represented.
	 * @since	TFP 1.0
	 */
	public static final Date	MAX_VALUE
		= new Date (DateValue.MAX_VALUE, null);
	
	/**
	 * Constructs a <CODE>Date</CODE> instance given a day, month and year.
	 *
	 * @param	day				The day of the month (1-31).
	 * @param	month			The month of the year (1-12).
	 * @param	year			The year (1900-2099).
	 * @since	TFP 1.0
	 */
	public Date (final int day, final int month, final int year)
	{
		this (new DateValue (day, month, year), null);
	}

	/**
	 * Constructs a <CODE>Date</CODE> instance given a day, month and year.
	 *
	 * @param	day				The day of the month (1-31).
	 * @param	month			The month of the year (1-12).
	 * @param	year			The year (1900-2099).
	 * @param	utc				Indicates UTC time zone.
	 * @since	TFP 1.0
	 */
	public Date (final int day, final int month, final int year, final boolean utc)
	{
		this (new DateValue (day, month, year), utc ? TimeZone.UTC : null);
	}

	/**
	 * Constructs a <CODE>Date</CODE> instance given a day, month and year.
	 *
	 * @param	day				The day of the month (1-31).
	 * @param	month			The month of the year (1-12).
	 * @param	year			The year (1900-2099).
	 * @param	offset			The time zone offset.
	 * @since	TFP 1.0
	 */
	public Date (final int day, final int month, final int year, final int offset)
	{
		this (new DateValue (day, month, year), new TimeZone (offset));
	}

	/**
	 * Parses a <CODE>Date</CODE> instance from a character string in the
	 * ISO date format (as produced by <CODE>toString</CODE>).
	 *
	 * @param	text			The text string to be parsed.
	 * @return	A new <CODE>Date</CODE> instance containing the parsed data.
	 * @throws	IllegalArgumentException If the character string is not in the
	 *			correct format.
	 * @since	TFP 1.0
	 */
	public static Date parse (String text)
	{
		char		ch;
		
		if (text != null) text = text.trim();
		
		int			limit = text.length ();
		int			index = 0;
		
		while (true) {
			// Extract date components
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			int year = (ch - '0') * 1000; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			year += (ch - '0') * 100; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			year += (ch - '0') *10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			year += ch - '0'; ++index;
			
			if ((index >= limit) || (text.charAt (index++) != '-')) break;
			
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			int month = (ch - '0') * 10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			month += ch - '0'; ++index;
			
			if ((index >= limit) || (text.charAt (index++) != '-')) break;
			
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			int day = (ch - '0') * 10; ++index;
			if ((index >= limit) || !isDigit (ch = text.charAt (index))) break;
			day += ch - '0'; ++index;
		
			// Detect UTC time zone
			if ((index < limit)&& (text.charAt (index) == 'Z')) {
				return (new Date (day, month, year, true));
			}
			
			// Detect time offsets
			if ((index < limit)&& (text.charAt (index) == '+')) {
				++index;
				if ((index >= limit) && !isDigit (ch = text.charAt (index))) break; 
				int offset = (ch - '0') * 600; ++index;
				if ((index >= limit) && !isDigit (ch = text.charAt (index))) break;
				offset += (ch - '0') * 60; ++index;
				
				if ((index >= limit)&& (text.charAt (index++) != ':')) break;
				
				if ((index >= limit) && !isDigit (ch = text.charAt (index))) break;
				offset = (ch - '0') * 10; ++index;
				if ((index >= limit) && !isDigit (ch = text.charAt (index))) break;
				offset += ch - '0'; ++index;

				return (new Date (day, month, year, offset));
			}
				
			if ((index < limit)&& (text.charAt (index) == '-')) {
				++index;
				if ((index >= limit) && !isDigit (ch = text.charAt (index))) break; 
				int offset = (ch - '0') * 600; ++index;
				if ((index >= limit) && !isDigit (ch = text.charAt (index))) break;
				offset += (ch - '0') * 60; ++index;
				
				if ((index >= limit)&& (text.charAt (index++) != ':')) break;
				
				if ((index >= limit) && !isDigit (ch = text.charAt (index))) break;
				offset = (ch - '0') * 10; ++index;
				if ((index >= limit) && !isDigit (ch = text.charAt (index))) break;
				offset += ch - '0'; ++index;

				return (new Date (day, month, year, -offset));
			}
			
			return (new Date (day, month, year));
		}

		throw new IllegalArgumentException ("Value is not in ISO date format (" + text +")");
	}
	
	/**
	 * Creates a <CODE>Date</CODE> instance initialised with the current date.
	 * 
	 * @return	The current date including <CODE>TimeZone</CODE> details.
	 * @since	TFP 1-0
	 */
	public static Date now ()
	{
		return (new Date (DateValue.now (), getImplicitTimeZone ()));
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
	 * Contains the day number.
	 * @return	The day number.
	 * @since	TFP 1.6
	 */
	public int dayNumber ()
	{
		return (dateValue.dayNumber ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public int dayOfMonth ()
	{
		return (dateValue.dayOfMonth());
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
	 * Creates a new <CODE>Date</CODE> based on the current instance and a given
	 * number of days adjustment.
	 *
	 * @param	days			The number of days to adjust by.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public Date plusDays (final int days)
	{
		return (new Date (dateValue.plusDays(days), timeZone));
	}
	
	/**
	 * Creates a new <CODE>Date</CODE> based on the current instance and a given
	 * number of weeks adjustment.
	 *
	 * @param	weeks			The number of weeks to adjust by.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public Date plusWeeks (final int weeks)
	{
		return (new Date (dateValue.plusWeeks (weeks), timeZone));
	}
	
	/**
	 * Creates a new <CODE>Date</CODE> based on the current instance and a given
	 * number of months adjustment.
	 *
	 * @param	months			The number of months to adjust by.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public Date plusMonths (final int months)
	{
		return (new Date (dateValue.plusMonths (months), timeZone));
	}
	
	/**
	 * Creates a new <CODE>Date</CODE> based on the current instance and a given
	 * number of years adjustment.
	 *
	 * @param	years 			The number of years to adjust by.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public Date plusYears (final int years)
	{
		return (new Date (dateValue.plusYears (years), timeZone));
	}
	
	/**
	 * Creates a new <CODE>Date</CODE> based on the current instance and a given
	 * <CODE>Interval</CODE>.
	 *
	 * @param	interval		The time <CODE>Interval</CODE>.
	 * @return	The adjusted date.
	 * @since	TFP 1.0
	 */
	public Date plus (Interval interval)
	{
		return (new Date (dateValue.plus (interval), timeZone));
	}
	
	/**
	 * Creates a <CODE>DateTime</CODE> instance representing midnight on the
	 * morning of the current date.
	 * 
	 * @return	The <CODE>DateTime</CODE> instance.
	 * @since	TFP 1.1
	 */
	public DateTime toDateTime ()
	{
		return (new DateTime (dateValue, TimeValue.START_OF_DAY, timeZone));
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
			return (dateValue.hashCode () ^ timeZone.hashCode ());
		else
			return (dateValue.hashCode ());
	}
	
	/**
	 * Determines if this <CODE>Date</CODE> instance and another object hold
	 * the same date.
	 *
	 * @param	other			The <CODE>Date</CODE> instance to compare with.
	 * @return	<CODE>true</CODE> if both instances represent the same date,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean equals (final Object	other)
	{
		return ((other instanceof Date) && equals ((Date) other));
	}

	/**
	 * Determines if this <CODE>Date</CODE> instance and another hold the same
	 * date.
	 *
	 * @param	other			The <CODE>Date</CODE> instance to compare with.
	 * @return	<CODE>true</CODE> if both instances represent the same date,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean equals (final Date other)
	{
		if ((timeZone == null) && (other.timeZone == null))
			return (dateValue.equals (other.dateValue));
		else if ((timeZone != null) && (other.timeZone != null) && timeZone.equals (other.timeZone))
			return (dateValue.equals (other.dateValue));
		else
			return (toDateTime ().equals (other.toDateTime ()));
	}

	/**
	 * Returns the result of comparing this instance to another <CODE>Date
	 * </CODE>.
	 *
	 * @param	other			The <CODE>Date</CODE> instance to compare with.
	 * @return	An integer value indicating the relative ordering.
	 * @since	TFP 1.0
	 */
	public int compareTo (final Date other)
	{
		if ((timeZone == null) && (other.timeZone == null))
			return (dateValue.compareTo (other.dateValue));
		else if ((timeZone != null) && (other.timeZone != null) && timeZone.equals (other.timeZone))
			return (dateValue.compareTo (other.dateValue));
		else
			return (toDateTime ().compareTo (other.toDateTime ()));
	}
	
	/**
	 * Converts the instance data members to a <CODE>String</CODE> representation
	 * that can be displayed for debugging purposes.
	 *
	 * @return 	The object's <CODE>String</CODE> representation.
	 * @since	TFP 1.0
	 */
	public String toString ()
	{
		if (timeZone != null)
			return (dateValue.toString () + timeZone.toString ());
		else
			return (dateValue.toString ());
	}
	
	/**
	 * Constructs a <CODE>Date</CODE> using its <CODE>DateValue</CODE> and
	 * <CODE>TimeZone</CODE> components.
	 * 
	 * @param 	dateValue		The <CODE>DateValue</CODE> component.		
	 * @param 	timeZone		The <CODE>TimeZone</CODE> component or <CODE>null</CODE>.
	 * @since	TFP 1.1
	 */
	protected Date (DateValue dateValue, TimeZone timeZone)
	{
		super (timeZone);
		
		this.dateValue = dateValue;
	}
	
	/**
	 * Serialization UID
	 * @since	TFP 1.0
	 */
	private static final long serialVersionUID = -5790893719145834093L;
		
	/**
	 * 
	 * @since	TFP 1.1
	 */
	private final DateValue	dateValue;
}