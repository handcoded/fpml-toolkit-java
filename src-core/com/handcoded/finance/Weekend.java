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

import java.util.Hashtable;

/**
 * The <CODE>Weekend</CODE> class provides a mechanism to test if a <CODE>Date
 * </CODE> falls on a weekend (e.g. non-working day). In traditionally Christian
 * countries Saturday and Sunday are non-working days but other religions have
 * selected other days.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
public abstract class Weekend
{
	/**
	 * The extent set of all <CODE>Weekend</CODE> instances.
	 * @since	TFP 1.0
	 */
	private static Hashtable<String, Weekend> extent
		= new Hashtable<String, Weekend> ();
	
	/**
	 * A <CODE>Weekend</CODE> instance that detects normal Western-European
	 * (Saturday &amp; Sunday) weekends.
	 * @since	TFP 1.0
	 */
	public static final Weekend		SAT_SUN
		= new Weekend ("SAT/SUN")
	{
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		@Override
		public boolean isWeekend (final Date date)
		{
			int day = date.weekday ();
			
			return ((day == Date.SATURDAY) || (day == Date.SUNDAY));
		}
	};

	/**
	 * A <CODE>Weekend</CODE> instance that defines Friday and Saturday
	 * as non-working days.
	 * @since	TFP 1.1
	 */
	public static final Weekend		FRI_SAT
		= new Weekend ("FRI/SAT")
	{
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		@Override
		public boolean isWeekend (final Date date)
		{
			int day = date.weekday ();
			
			return ((day == Date.FRIDAY) || (day == Date.SATURDAY));
		}
	};
	
	/**
	 * A <CODE>Weekend</CODE> instance that defines Thursday and Friday
	 * as non-working days.
	 * @since	TFP 1.1
	 */
	public static final Weekend		THU_FRI
		= new Weekend ("THU/FRI")
	{
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		@Override
		public boolean isWeekend (final Date date)
		{
			int day = date.weekday ();
			
			return ((day == Date.THURSDAY) || (day == Date.FRIDAY));
		}
	};

	/**
	 * Attempts to find a <CODE>Weekend</CODE> instance in the extent set with
	 * the given reference name.
	 * 
	 * @param	name			The reference name for the required instance.
	 * @return	The matching <CODE>Weekend</CODE> instance or <CODE>null</CODE>
	 * 			if no match could be found.
	 * @since	TFP 1.0
	 */
	public static Weekend forName (final String name)
	{
		return (extent.get (name));
	}
	
	/**
	 * Provides access to the reference name for a <CODE>Weekend</CODE>
	 * instance.
	 * 
	 * @return	The reference name string for the instance.
	 * @since	TFP 1.0
	 */
	public final String getName ()
	{
		return (name);
	}
	
	/**
	 * Determines if the given <CODE>Date</CODE> falls on a weekend.
	 * 
	 * @param 	date			The <CODE>Date</CODE> to check.
	 * @return	<CODE>true</CODE> if the date falls on a weekend,
	 * 			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public abstract boolean isWeekend (final Date date);
	
	/**
	 * Constructs a <CODE>Weekend</CODE> instance with a given reference name.
	 * 
	 * @param	name			The name used to reference the instance.
	 * @since	TFP 1.0
	 */
	protected Weekend (final String name)
	{
		extent.put ((this.name = name), this);
	}
	
	/**
	 * The name of this instance.
	 * @since	TFP 1.0
	 */
	private final String 		name;
}