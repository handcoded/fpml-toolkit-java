// Copyright (C),2005-2012 HandCoded Software Ltd.
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

import java.util.Vector;

/**
 * An instance of the <CODE>CalendarSet</CODE> class holds a collection of
 * <CODE>Calendar</CODE> instances used to define business days for date rolls.
 * <P>
 * A date is only considered a business day if it is business day in all the
 * underlying <CODE>Calendar</CODE> instances.
 * 
 * @author 	BitWise
 * @version	$Id$
 * @since	TFP 1.7
 */
public final class CalendarSet extends Calendar
{
	/**
	 * Constructs an empty <CODE>CalendarSet</CODE> instance.
	 * @since	TFP 1.7
	 */
	public CalendarSet ()
	{
		super (null);
	}

	/**
	 * Adds the indicated <CODE>Calendar</CODE> to the underlying collection.
	 * 
	 * @param	calendar		The <CODE>Calendar</CODE> to be added.
	 * @since	TFP 1.7
	 */
	public void add (Calendar calendar)
	{
		calendars.add (calendar);
	}
	
	/**
	 * Determines if the <CODE>Date</CODE> provided falls on a business day
	 * in all the <CODE>Calendar</CODE> instances in this set.
	 *
	 * @param	date			The <CODE>Date</CODE> to be tested.
	 * @return	<CODE>true</CODE> if the date is a business day,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.6
	 */
	@Override
	public boolean isBusinessDay (final Date date)
	{
		for (Calendar calendar : calendars)
			if (!calendar.isBusinessDay (date))
				return (false);
		
		return (true);
	}

	/**
	 * The underlying set of <CODE>Calendar</CODE> instances.
	 * @since	TFP 1.7
	 */
	private Vector<Calendar>	calendars = new Vector<Calendar> ();
}