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

/**
 * An <CODE>Interval</CODE> is a length of time expressed as an integer multiple
 * of some <CODE>Period</CODE>, for example five days or three months.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class Interval implements Serializable
{
	/**
	 * Constructs an <CODE>Interval</CODE> from the provided multiplier
	 * and time unit.
	 *
	 * @param	multiplier		The time period multiplier.
	 * @param	period			The time period unit.
	 * @since	TFP 1.0
	 */
	public Interval (final int multiplier, final Period period)
	{
		this.multiplier = multiplier;
		this.period     = period;
	}
	
	/**
	 * Constructs an <CODE>Interval</CODE> from the provided multiplier
	 * and time unit code.
	 *
	 * @param	multiplier		The time period multiplier.
	 * @param	code			The time period unit code.
	 * @since	TFP 1.0
	 */
	public Interval (final int multiplier, final String code)
	{
		this (multiplier, Period.forCode (code));
	}
	
	/**
	 * Provides access to the time period multiplier.
	 *
	 * @return	The multiplier.
	 * @since	TFP 1.0
	 */
	public int getMultiplier ()
	{
		return (multiplier);
	}
	
	/**
	 * Provides access to the time period value.
	 *
	 * @return 	The time perion unit.
	 * @since	TFP 1.0
	 */
	public Period getPeriod ()
	{
		return (period);
	}

	/**
	 * Determines if the current <CODE>Interval</CODE> is an integral multiple
	 * of another instance.
	 * <P>
	 * The calculation recognises that a week is seven days and that a year is
	 * twelve months. It also allows 1T to match any time period longer than a
	 * day and for any time period to be a multiple of 1 day.
	 *
	 * @param	other			The <CODE>Interval</CODE> to compare with.
	 * @return	<CODE>true</CODE> is an integral multiple of target,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean isMultipleOf (final Interval	other)
	{
		int				value = 0;
		
		// 1T is a positive integer multiple (>= 1) of any frequency
		if ((multiplier == 1) && (period == Period.TERM) && (other.multiplier >= 1))
			return (true);
		
		// Any period > 0 is a multiple of 1D
		if ((multiplier > 0) && (other.multiplier == 1) && (other.period == Period.DAY))
			return (true);
		
		// Handle 1W = 7D and 1Y = 12M or multiples thereof
		if (period == other.period)
			value = multiplier;
		else if ((period == Period.WEEK) && (other.period == Period.DAY))
			value =  7 * multiplier;
		else if ((period == Period.YEAR) && (other.period == Period.MONTH))
			value = 12 * multiplier;
			
		return ((other.multiplier != 0) &&
				((value / other.multiplier) >= 1) &&
				((value % other.multiplier) == 0));			
	}
	
	/**
	 * Determines if an <CODE>Interval</CODE> will divide the time period delimited
	 * by two dates exactly.
	 *
	 * @param 	first			The first date.
	 * @param 	last			The last date.
	 * @return	<CODE>true</CODE> if the <CODE>Interval</CODE> exactly divides the
	 * 			two dates an integer number of times.
	 * @since	TFP 1.0
	 */
	public boolean dividesDates (Date first, Date last)
	{
		int				multiplier	= this.multiplier;
		Period			period		= this.period;
		
		if (multiplier == 0) return (false);
		
		if (period == Period.TERM)
			return (multiplier == 1);

		if (period == Period.WEEK) {
			period = Period.DAY;
			multiplier *= 7;
		}

		if (period == Period.YEAR) {
			if (first.month ()	    != last.month ())		return (false);
			if (first.dayOfMonth () != last.dayOfMonth ())	return (false);

			return (((last.year () - first.year ()) % multiplier) == 0);
		}

		if (period == Period.MONTH) {
			if (first.dayOfMonth () != last.dayOfMonth ())	return (false);

			return ((((last.year () - first.year ()) * 12 + last.month () - first.month ()) % multiplier) == 0);
		}

		return (((last.hashCode () - first.hashCode ()) % multiplier) == 0);
	}

	/**
	 * Calculates the result of adding another <CODE>Interval</CODE> to this
	 * one.
	 * 
	 * @param 	other			The <CODE>Interval</CODE> to add.
	 * @return	A new <CODE>Interval</CODE> representing the combined time
	 * 			period.
	 * @throws	IllegalArgumentException If the two time periods cannot be
	 * 			combined.
	 * @since	TFP 1.1
	 */
	public Interval plus (Interval other)
	{
		// One of the Intervals is zero length?
		if (multiplier == 0) return (other);
		if (other.multiplier == 0) return (this);

		// Both Intervals have the same unit?
		if (period == other.period)
			return (new Interval (multiplier + other.multiplier, period));
	
		// Otherwise check for equivalences
		if ((period == Period.YEAR) && (other.period == Period.MONTH))
			return (new Interval (12 * multiplier + other.multiplier, Period.MONTH));
		if ((period == Period.MONTH) && (other.period == Period.YEAR))
			return (new Interval (multiplier + 12 * other.multiplier, Period.MONTH));
		if ((period == Period.WEEK) && (other.period == Period.DAY))
			return (new Interval (7 * multiplier + other.multiplier, Period.DAY));
		if ((period == Period.DAY) && (other.period == Period.WEEK))
			return (new Interval (multiplier + 7 * other.multiplier, Period.DAY));
	
		throw new IllegalArgumentException ("Intervals cannot be combined");
	}
	
	/**
	 * Returns the hash value of the <CODE>Interval</CODE> for hash based data
	 * structures and algorithms.
	 *
	 * @return	The hash value for the <CODE>Interval</CODE>.
	 * @since	TFP 1.0
	 */
	public int hashCode ()
	{
		return (period.hashCode () + multiplier);
	}
	
	/**
	 * Determines if this <CODE>Interval</CODE> instance and another are the
	 * same. No day/week or month/year equivalences are considered.
	 *
	 * @param	other		The <CODE>Date</CODE> instance to compare with.
	 * @return	<CODE>true</CODE> if both instances represent the same date,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean equals (final Object other)
	{
		return ((other instanceof Interval) && equals ((Interval) other));
	}
	
	/**
	 * Compares two <CODE>Interval</CODE> instance to see if they contain the
	 * same information.
	 * <P>
	 * This routine takes into account the equivalence of certain
	 * time intervals (e.g. 1Y = 12M and 1W = 7D).
	 * @param 	other			The other <CODE>Interval</CODE> instance.
	 * @return	A <CODE>boolean</CODE> value indicating equality.
	 * @since	TFP 1.0
	 */
	public boolean equals (final Interval other)
	{
		if (period == other.period)
			return (multiplier == other.multiplier);

		// Handle 1Y == 12M equivalence
		if (period == Period.YEAR) {
			if (other.period == Period.MONTH)
				return ((multiplier * 12) == other.multiplier);
			return (false);
		}
        if (period == Period.MONTH) {
			if (other.period == Period.YEAR)
				return (multiplier == (other.multiplier * 12));
			return (false);
		}

		// Handle 1W == 7D equivalence
		if (period == Period.WEEK) {
			if (other.period == Period.DAY)
				return ((multiplier * 7) == other.multiplier);
			return (false);
		}
        if (period == Period.DAY) {
			if (other.period == Period.WEEK)
				return (multiplier == (other.multiplier * 7));
			return (false);
		}

		return (false);
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
		return (Integer.toString (multiplier) + period.toString ()); 
	}
	
	/**
	 * Serialization UID
	 * @since	TFP 1.0
	 */
	private static final long serialVersionUID = 3153285406096448451L;

	/**
	 * The number of units of the time period.
	 * @since	TFP 1.0
	 */
	private int			multiplier;
	
	/**
	 * The time period unit itself.
	 * @since	TFP 1.0
	 */
	private Period		period;
}