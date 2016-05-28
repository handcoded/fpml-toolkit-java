// Copyright (C),2005-2006 HandCoded Software Ltd.
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
 * A <CODE>CalendarRule</CODE> instance provides an algorithmic means to
 * determine when a holiday will fall in a given year.
 * 
 * @author	BitWise
 * @version	$Id: CalendarRule.java 36 2006-08-31 20:27:56Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class CalendarRule
{
	/**
	 * The <CODE>Fixed</CODE> class describes a holiday that falls on a
	 * fixed day, but for which the actual public holiday may be rolled to
	 * close business day. For example in the UK when Christmas falls on
	 * a weekend the following Monday is a bank holiday (and the Tuesday
	 * Boxing day).
	 * @since	TFP 1.0
	 */
	public static final class Fixed extends CalendarRule
	{
		/**
		 * Constructs a <CODE>Fixed</CODE> calendar rule instance from the
		 * provided values.
		 * 
		 * @param 	name			The name of the rule.
		 * @param 	from			The first year the rule applies.
		 * @param 	until			The last year the rule applies.
		 * @param 	month			The month in which the holiday falls.
		 * @param 	dayOfMonth		The unadjusted date of the holiday.
		 * @param 	dateRoll		The <CODE>DateRoll</CODE> to use.
		 * @since	TFP 1.0
		 */
		public Fixed (final String name, int from, int until, int month, int dayOfMonth, DateRoll dateRoll)
		{
			super (name, from, until);
			
			this.month 	  	= month;
			this.dayOfMonth = dayOfMonth;
			this.dateRoll 	= dateRoll;
		}
		
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		public Date generate (Calendar calendar, int year)
		{
			return (dateRoll.adjust (calendar, new Date (dayOfMonth, month, year)));
		}
		
		/**
		 * Determines if date for this holiday has been adjusted.
		 * 
		 * @param	date				The (possibly) adjusted date to check.
		 * @return	<CODE>true</CODE> if the holiday was rolled otherwise
		 * 			<CODE>false</CODE>.
		 * @since	TFP 1.0
		 */
		public boolean isAdjusted (Date date)
		{
			return ((date.month () != month) || (date.dayOfMonth() != dayOfMonth));
		}
		
		/**
		 * The month in which the holiday falls.
		 * @since	TFP 1.0
		 */
		private final int			month;
		
		/**
		 * The unadjusted date of the holiday.
		 * @since	TFP 1.0
		 */
		private final int			dayOfMonth;
		
		/**
		 * The <CODE>DateRoll</CODE> to be applied to the unadjusted date.
		 * @since	TFP 1.0
		 */
		private final DateRoll		dateRoll;
	}
	
	/**
	 * The <CODE>Offset</CODE> class describes a holiday that occurs on
	 * a particular instance of a day within a month. In the USA for
	 * example Thanks Giving is the fourth Thursday in November.  
	 * @since	TFP 1.0
	 */
	public static final class Offset extends CalendarRule
	{
		/**
		 * Constant value indicating the first occurance.
		 * @since	TFP 1.0
		 */
		public static final int		FIRST	=  1;
		
		/**
		 * Constant value indicating the second occurance.
		 * @since	TFP 1.0
		 */
		public static final int		SECOND	=  2;
		
		/**
		 * Constant value indicating the third occurance.
		 * @since	TFP 1.0
		 */
		public static final int		THIRD	=  3;
		
		/**
		 * Constant value indicating the fourth occurance.
		 * @since	TFP 1.0
		 */
		public static final int 	FOURTH	=  4;
		
		/**
		 * Constant value indicating the last occurance.
		 * @since	TFP 1.0
		 */
		public static final int		LAST	= -1;
		
		/**
		 * 
		 * @param 	name			The name of the rule.
		 * @param 	from			The first year the rule applies.
		 * @param 	until			The last year the rule applies.
		 * @param 	when			Specifies the occurance.
		 * @param 	day				Specifies the day of the week.
		 * @param 	month			The month in which the holiday falls.
		 */
		public Offset (final String name, int from, int until, int when, int day, int month)
		{
			super (name, from, until);
			
			this.when  = when;
			this.day   = day;
			this.month = month;
		}
		
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		public Date generate (Calendar calendar, int year)
		{
			int 	limit = Date.monthLength (month, year);
			
			switch (when) {			
			case LAST:
				{
					for (int index = limit - 6; index <= limit; ++index) {
						Date date = new Date (index, month, year);
					
						if (date.weekday () == day) return (date);
					}
					break;
				}
				
			default:
				{
					int count = 0;
		
					for (int index = 1; index <= limit; ++index) {
						Date date = new Date (index, month, year);
						
						if ((date.weekday () == day) && (++count == when))
							return (date);
					}
					break;
				}
			}
			return (null);
		}
	
		/**
		 * The occurance of day when the holiday falls.
		 * @since	TFP 1.0
		 */
		private final int			when;
		
		/**
		 * The day of the week when the holiday falls.
		 * @since	TFP 1.0
		 */
		private final int			day;
		
		/**
		 * The month in which the holiday falls.
		 * @since	TFP 1.0
		 */
		private final int			month;
	}
	
	/**
	 * The <CODE>Easter</CODE> class can calculate the date of a holiday
	 * which is defines relative to Easter Monday (e.g. Good Friday).
	 * @since	TFP 1.0
	 */
	public static final class Easter extends CalendarRule
	{
		/**
		 * Constructs a <CODE>Easter</CODE> rule instance.
		 * 
		 * @param 	name			The name of the holiday
		 * @param 	from			The first year it applies
		 * @param 	until			The last year it applies
		 * @param 	offset			The offset from Easter Monday.
		 * @since	TFP 1.0
		 */
		public Easter (final String name, int from, int until, int offset)
		{
			super (name, from, until);
			
			this.offset = offset;
		}
		
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		public Date generate (Calendar calendar, final int year)
		{
			return (new Date (1, 1, year).plusDays (easterMonday [year - 1900] + offset - 1));
		}

		/**
		 * Lookup table for calculating Easter Monday in any year between 1900 and
		 * 2099.
		 * @since	TFP 1.0
		 */
		private static int			easterMonday [] = {
	            107,  98,  90, 103,  95, 114, 106,  91, 111, 102,   // 1900-1909
	             87, 107,  99,  83, 103,  95, 115,  99,  91, 111,   // 1910-1919
	             96,  87, 107,  92, 112, 103,  95, 108, 100,  91,   // 1920-1929
	            111,  96,  88, 107,  92, 112, 104,  88, 108, 100,   // 1930-1939
	             85, 104,  96, 116, 101,  92, 112,  97,  89, 108,   // 1940-1949
	            100,  85, 105,  96, 109, 101,  93, 112,  97,  89,   // 1950-1959
	            109,  93, 113, 105,  90, 109, 101,  86, 106,  97,   // 1960-1969
	             89, 102,  94, 113, 105,  90, 110, 101,  86, 106,   // 1970-1979
	             98, 110, 102,  94, 114,  98,  90, 110,  95,  86,   // 1980-1989
	            106,  91, 111, 102,  94, 107,  99,  90, 103,  95,   // 1990-1999
	            115, 106,  91, 111, 103,  87, 107,  99,  84, 103,   // 2000-2009
	             95, 115, 100,  91, 111,  96,  88, 107,  92, 112,   // 2010-2019
	            104,  95, 108, 100,  92, 111,  96,  88, 108,  92,   // 2020-2029
	            112, 104,  89, 108, 100,  85, 105,  96, 116, 101,   // 2030-2039
	             93, 112,  97,  89, 109, 100,  85, 105,  97, 109,   // 2040-2049
	            101,  93, 113,  97,  89, 109,  94, 113, 105,  90,   // 2050-2059
	            110, 101,  86, 106,  98,  89, 102,  94, 114, 105,   // 2060-2069
	             90, 110, 102,  86, 106,  98, 111, 102,  94, 107,   // 2070-2079
	             99,  90, 110,  95,  87, 106,  91, 111, 103,  94,   // 2080-2089
	            107,  99,  91, 103,  95, 115, 107,  91, 111, 103    // 2090-2099
	    	};
		
		/**
		 * The offset (in days) from Easter Monday to the holiday.
		 * @since	TFP 1.0
		 */
		private final int			offset;
	}

	/**
	 * Provides access to the name of the <CODE>CalendarRule</CODE>
	 * 
	 * @return	The name of the rule.
	 * @since	TFP 1.0
	 */
	public final String getName ()
	{
		return (name);
	}
	
	/**
	 * Provides access to the first year that the rule applies to.
	 * 
	 * @return	The first applicable year.
	 * @since	TFP 1.0
	 */
	public final int getFrom ()
	{
		return (from);
	}
	
	/**
	 * Provides access to the last year that the rule applies to.
	 * 
	 * @return	The last applicable year.
	 * @since	TFP 1.0
	 */
	public final int getUntil ()
	{
		return (until);
	}
	
	/**
	 * Determines if a <CODE>CalendarRule</CODE> is applicable to a given
	 * year.
	 * 
	 * @param 	year			The year to be tested.
	 * @return	The applicability of the <CODE>CalendarRule</CODE>.
	 * @since	TFP 1.0
	 */
	public final boolean isApplicable (int year)
	{
		return ((from <= year) && (year <= until));
	}
	
	/**
	 * Calculates the <CODE>Date</CODE> that this holiday would fall in the
	 * given year, if necessary adjusting for other holidays or weekends.
	 * 
	 * @param 	calendar		The <CODE>Calendar</CODE> for adjustments.
	 * @param 	year			The year to generate for.
	 * @return	The <CODE>Date</CODE> the holiday should fall on.
	 */
	public abstract Date generate (Calendar calendar, int year);
	
	/**
	 * Constructs a <CODE>CalendarRule</CODE> with in the given name and
	 * applicable year range.
	 * 
	 * @param 	name			The name of the rule.
	 * @param 	from			The first year the rule applies.
	 * @param 	until			The last year the rule applies.
	 * @since	TFP 1.0
	 */
	protected CalendarRule (final String name, int from, int until)
	{
		this.name  = name;
		this.from  = from;
		this.until = until;
	}
	
	/**
	 * The name of the <CODE>CalendarRule</CODE>.
	 * @since	TFP 1.0
	 */
	private final String		name;
	
	/**
	 * The first year this rule applies.
	 * @since	TFP 1.0
	 */
	private final int			from;
	
	/**
	 * The last year this rule applies.
	 * @since	TFP 1.0
	 */
	private final int			until;
}