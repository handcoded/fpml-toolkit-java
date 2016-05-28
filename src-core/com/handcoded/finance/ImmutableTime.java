// Copyright (C),2005-2007 HandCoded Software Ltd.
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

import java.math.BigDecimal;

/**
 * The <CODE>ImmutableTime</CODE> interface defines constants and methods provided
 * by both the <CODE>Time</CODE> and <CODE>DateTime</CODE> classes (and an
 * internal value representation).
 * 
 * @author	BitWise
 * @version	$Id: ImmutableTime.java 209 2007-10-29 22:32:02Z andrew_jacobs $
 * @since	TFP 1.1
 */
public interface ImmutableTime
{
	/**
	 * Provides access to the hours component of the time value.
	 *
	 * @return	The hours component of the time.
	 * @since	TFP 1.0
	 */
	public int hours ();
	
	/**
	 * Provides access to the minutes component of the time value.
	 *
	 * @return	The minutes component of the time.
	 * @since	TFP 1.0
	 */
	public int minutes ();
	
	/**
	 * Provides access to the seconds component of the time value.
	 *
	 * @return	The seconds component of the time.
	 * @since	TFP 1.0
	 */
	public BigDecimal seconds ();
}
