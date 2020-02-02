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

/**
 * The <CODE>ImmutableDate</CODE> interface defines methods provided by
 * both the <CODE>Date</CODE> and <CODE>DateTime</CODE> classes (and an
 * internal value representation).
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.1
 */
public interface ImmutableDate
{
	/**
	 * Calculates the day of the week on which a date falls.
	 * <P>
	 * See the constants in <CODE>Date</CODE> and <CODE>DateTime</CODE>
	 * for day values (e.g. SUNDAY, MONDAY, etc.).
	 *
	 * @return	The weekday.
	 * @since	TFP 1.0
	 */
	public int weekday ();

	/**
	 * Calculates the day of the month on which the <CODE>Date</CODE> falls.
	 *
	 * @return	The day of the month (1-31).
	 * @since	TFP 1.0
	 */
	public int dayOfMonth ();

	/**
	 * Calculates the number of the last day in the month in which the <CODE>
	 * Date</CODE> falls.
	 *
	 * @return 	The last day in the month (28-31)
	 * @since	TFP 1.0
	 */
	public int lastDayOfMonth ();

	/**
	 * Determines in the <CODE>Date</CODE> falls on the last day of the month.
	 *
	 * @return	<CODE>true</CODE> if the <CODE>Date</CODE> represents the last
	 *			day in a month, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean isEndOfMonth ();
	
	/**
	 * Calculates the day of the year represented by the current <CODE>Date
	 * </CODE>.
	 *
	 * @return 	The day of the year (1-366).
	 * @since	TFP 1.0
	 */
	public int dayOfYear ();
	
	/**
	 * Calculates the month of the year in which the current <CODE>Date</CODE>
	 * falls.
	 *
	 * @return 	The month of the year (1-12).
	 * @since	TFP 1.0
	 */
	public int month ();

	/**
	 * Calculates the year in which the current <CODE>Date</CODE> falls.
	 *
	 * @return	The year (1900 - 2099).
	 * @since	TFP 1.0
	 */
	public int year ();
}