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

package com.handcoded.fpml.schemes;

import java.util.Vector;
import java.util.Enumeration;

/**
 * The <CODE>ClosedScheme</CODE> class provides an extended implementation of
 * <CODE>CachedScheme</CODE> which implements the <CODE>Enumerable</CODE> and
 * <CODE>Matchable</CODE> interfaces.
 * 
 * @author	BitWise
 * @version	$Id: ClosedScheme.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
public class ClosedScheme extends CachedScheme implements Enumerable, Matchable
{
	/**
	 * Constructs a <CODE>ClosedScheme</CODE> instance for the indicated
	 * scheme URI.
	 *
	 * @param	uri				The URI used to reference the scheme.
	 * @since	TFP 1.0
	 */
	public ClosedScheme (final String uri)
	{
		super (uri);
	}
	
	/**
	 * Constructs a <CODE>ClosedScheme</CODE> instance for the indicated
	 * scheme URI and Canonical scheme URI.
	 *
	 * @param	uri				The URI used to reference the scheme.
	 * @param	canonicalUri	The Canonical scheme URI.
	 * @since	TFP 1.2
	 */
	public ClosedScheme (final String uri, final String canonicalUri)
	{
		super (uri, canonicalUri);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public final Value [] values ()
	{
		Value 			result [] = new Value [values.size ()];
		
		values.values ().toArray (result);
		return (result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public final Value [] values (final String pattern)
	{
		Vector<Value>	matches = new Vector<Value> ();
		
		for (Enumeration<Value> cursor = values.elements (); cursor.hasMoreElements ();) {
			Value		value = cursor.nextElement ();
			
			if (value.getCode ().matches (pattern)) matches.add (value);
		}
		
		Value			result [] = new Value [matches.size ()];
		
		matches.copyInto (result);
		return (result);
	}
}