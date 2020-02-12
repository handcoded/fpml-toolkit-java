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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * A <CODE>RuleBasedCalendar</CODE> uses a set of <CODE>CalendarRule</CODE>
 * instance to derive the dates on which holidays will occur either in the
 * past or future.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class RuleBasedCalendar extends Calendar
{
	/**
	 * Constructs a <CODE>RuleBasedCalendar</CODE> with the given name and
	 * weekend rule.
	 * 
	 * @param 	name			The name used to identify the <CODE>Calendar</CODE>.
	 * @param 	weekend			A <CODE>Weekend</CODE> defining instance.
	 * @since	TFP 1.0	
	 */
	public RuleBasedCalendar (final String name, final Weekend weekend)
	{
		super (name);
		
		this.weekend = weekend;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	public boolean isBusinessDay (final Date date)
	{
		if (weekend.isWeekend (date)) return (false);
		
		int year = date.year ();
		
		if ((holidays == null) || (year < minYear) || (year > maxYear))
			synchronized (this) {
				if (holidays == null) {
					holidays = new HashMap<Date, CalendarRule> ();
				
					generate (minYear = maxYear = year, year);
				}
				else {
					if (year < minYear) {
						int oldLimit = minYear;
						generate (minYear = year, oldLimit - 1);
					}
					else if (year > maxYear) {
						int oldLimit = maxYear;
						generate (oldLimit + 1, maxYear = year);
					}
				}
			}
	
		return (!holidays.containsKey(date));
	}
	
	/**
	 * Adds a <CODE>CalendarRule</CODE> instance to the set maintained by the
	 * current instance.
	 * 
	 * @param 	rule			The <CODE>CalendarRule</CODE> to be added.
	 * @since	TFP 1.0
	 */
	public void addRule (final CalendarRule rule)
	{
		rules.add (rule);
	}
	
	/**
	 * The <CODE>Weekend</CODE> instance to use for recurring weekly
	 * non-business days.
	 * @since	TFP 1.0
	 */
	private final Weekend		weekend;
	
	/**
	 * The set of <CODE>Rule</CODE> instance used to define holidays.
	 * @since	TFP 1.0
	 */
	private ArrayList<CalendarRule> rules
		= new ArrayList<> ();
	
	/**
	 * The oldest year for which holiday dates have been calculated.
	 * @since	TFP 1.0
	 */
	private int					minYear;
	
	/**
	 * The most future year for which holiday dates have been calculated.
	 * @since	TFP 1.0
	 */
	private int					maxYear;
	
	/**
	 * The set of all holiday dates determined so far.
	 * @since	TFP 1.0
	 */
	private HashMap<Date, CalendarRule> holidays = null;

	/**
	 * Uses the <CODE>CalendarRule</CODE> instances to extend the holiday
	 * set for the years specified.
	 *  
	 * @param 	min				The first year in the period required.
	 * @param 	max				The last year in the period required.
	 * @since	TFP 1.0
	 */
	private void generate (int min, int max)
	{
		for (CalendarRule rule : rules) {
			int		from  = Math.max (min, rule.getFrom ());
			int		until = Math.min (max, rule.getUntil ());

			for (int year = from; year <= until; ++year)
				holidays.put (rule.generate (this, year), rule);
		}
	}
}