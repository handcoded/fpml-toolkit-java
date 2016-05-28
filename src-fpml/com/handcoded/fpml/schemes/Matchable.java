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

package com.handcoded.fpml.schemes;

/**
 * The <CODE>Matchable</CODE> interface defines an API for locating values
 * in a <CODE>Scheme</CODE> that have codes that match a given regular
 * expression.
 * <P>
 * <CODE>Matchable</CODE> can be supported by either closed or open domains.
 * The implementation of this function for an open domain is likely to require
 * that a query is executed against an external data source, such as a
 * relational database.
 *
 * @author	BitWise
 * @version	$Id: Matchable.java 39 2006-08-31 20:41:11Z andrew_jacobs $
 * @since	TFP 1.0
 */
public interface Matchable
{
	/**
	 * Finds the subset of scheme values than match the provided regular
	 * expression. No particular orderining of the values is enforced.
	 *
	 * @param	pattern			A regular expression.
	 * @return	An array containing the matching <CODE>Value</CODE> instances.
	 */
	public Value [] values (final String pattern);
}