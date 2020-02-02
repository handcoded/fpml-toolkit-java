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

import java.util.HashMap;

/**
 * Instances of the <CODE>DateRoll</CODE> class carry out financial date
 * adjustments. A <CODE>DateRoll</CODE> instance will apply a particualar
 * adjustment rule to a given <CODE>Date</CODE> using a business day
 * <CODE>Calendar</CODE> to skip non-working days.
 *  
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
public abstract class DateRoll
{
	/**
	 * The set of all existing <CODE>DateRoll</CODE> instances.
	 * @since	TFP 1.0
	 */
	private static HashMap<String, DateRoll> extent
		= new HashMap<> ();
	
	/**
	 * A <CODE>DateRoll</CODE> that performs no adjustment.
	 * @since	TFP 1.0
	 */
	public static final DateRoll	NONE
		= new DateRoll ("NONE")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			@Override
			public Date adjust (Calendar calendar, Date date)
			{
				return (date);
			}
		};
	
	/**
	 * A <CODE>DateRoll</CODE> that adjusts a date to the next business day if
	 * it falls on a holiday.
	 * @since	TFP 1.0
	 */
	public static final DateRoll	FOLLOWING
		= new DateRoll ("FOLLOWING")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			@Override
			public Date adjust (Calendar calendar, Date date)
			{
				Date				result = date;
				
				while (!calendar.isBusinessDay (result))
					result = result.plusDays (1);
				return (result);
			}
		};
		
	/**
	 * A <CODE>DateRoll</CODE> that adjusts a date to the preceding business
	 * day if it falls on a holiday.
	 * @since	TFP 1.0
	 */
	public static final DateRoll	PRECEDING
		= new DateRoll ("PRECEDING")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			@Override
			public Date adjust (Calendar calendar, Date date)
			{
				Date				result = date;
				
				while (!calendar.isBusinessDay (result))
					result = result.plusDays (-1);
				return (result);
			}
		};
		
	/**
	 * A <CODE>DateRoll</CODE> that adjusts a date to the next business day if
	 * it falls on a holiday unless that would move the date in the next month
	 * in which case it is rolled to a preceding date.
	 * @since	TFP 1.0
	 */
	public static final DateRoll	MODFOLLOWING
		= new DateRoll ("MODFOLLOWING")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			@Override
			public Date adjust (Calendar calendar, Date date)
			{
				Date				result = FOLLOWING.adjust (calendar, date);
				
				if (date.month () != result.month ())
					result = PRECEDING.adjust (calendar, date);
				return (result);
			}
		};
		
	/**
	 * A <CODE>DateRoll</CODE> that adjusts a date to the previous business
	 * day if it falls on a holiday unless that would move the date in the
	 * previous month in which case it is rolled to a following date.
	 * @since	TFP 1.0
	 */
	public static final DateRoll	MODPRECEDING
		= new DateRoll ("MODPRECEDING")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			@Override
			public Date adjust (Calendar calendar, Date date)
			{
				Date				result = PRECEDING.adjust (calendar, date);
				
				if (date.month () != result.month ())
					result = FOLLOWING.adjust (calendar, date);	
				return (result);
			}
		};
		
	/**
	 * A <CODE>DateRoll</CODE> that adjusts dates which fall on a Saturday to
	 * the preceding Friday, and those falling on a Sunday to the following
	 * Monday. This convention is used by some national holidays, for example
	 * Christmas Day in the USA.
	 * @since	TFP 1.0
	 */
	public static final DateRoll	WEEKEND
		= new DateRoll ("WEEKEND")
		{
			/**
			 * {@inheritDoc}
			 * @since	TFP 1.0
			 */
			@Override
			public Date adjust (Calendar calendar, Date date)
			{
				switch (date.weekday ()) {
				case TemporalDate.SATURDAY:		return (date.plusDays (-1));
				case TemporalDate.SUNDAY:		return (date.plusDays (+1));
				default:						return (date);
				}
			}
		};

	/**
	 * Attempts to find a <CODE>DateRoll</CODE> instance in the extent set with
	 * the given reference name.
	 * 
	 * @param	name			The reference name for the required instance.
	 * @return	The matching <CODE>DateRoll</CODE> instance or <CODE>null</CODE>
	 * 			if no match could be found.
	 * @since	TFP 1.0
	 */
	public static DateRoll forName (final String name)
	{
		return (extent.get (name));
	}

	/**
	 * Provides access to the symbolic name for this <CODE>DateRoll</CODE>
	 * instance.
	 * 
	 * @return	The symbolic name for the <CODE>DateRoll</CODE>.
	 * @since	TFP 1.0
	 */
	public final String getName ()
	{
		return (name);
	}
		
	/**
	 * Adjusts a <CODE>Date</CODE> which falls on a holiday within the
	 * indicated <CODE>Calendar</CODE> to an appropriate business day.
	 *  
	 * @param 	calendar		The <CODE>Calendar</CODE> to be used.
	 * @param 	date			The <CODE>Date</CODE> to adjust.
	 * @return	A (possibly) adjusted <CODE>Date</CODE> instance.
	 * @since	TFP 1.0
	 */
	public abstract Date adjust (Calendar calendar, Date date);
	
	/**
	 * Constructs a <CODE>DateRoll</CODE> instance and adds it to the
	 * extent set indexed by its symbolic name.
	 * 
	 * @param 	name			The symbolic name for this instance.
	 * @since	TFP 1.0
	 */
	protected DateRoll (final String name)
	{
		extent.put (this.name = name, this);
	}

	/**
	 * The symbolic name for this instance.
	 * @since	TFP 1.0
	 */
	private final String		name;
}