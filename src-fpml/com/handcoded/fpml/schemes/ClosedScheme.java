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

package com.handcoded.fpml.schemes;

import java.util.ArrayList;

/**
 * The <CODE>ClosedScheme</CODE> class provides an extended implementation of
 * <CODE>CachedScheme</CODE> which implements the <CODE>Enumerable</CODE> and
 * <CODE>Matchable</CODE> interfaces.
 * 
 * @author	Andrew Jacobs
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
		return (values.values ().toArray (new Value [values.size ()]));
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public final Value [] values (final String pattern)
	{
		ArrayList<Value>	matches = new ArrayList<> ();
		
		for (Value value : values.values ()) {
			if (value.getCode ().matches (pattern)) matches.add (value);
		}
		return (matches.toArray (new Value [matches.size ()]));
	}
}