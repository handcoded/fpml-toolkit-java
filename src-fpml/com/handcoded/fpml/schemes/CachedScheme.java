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

import java.util.Hashtable;

/**
 * The <CODE>CacheScheme</CODE> provides in memory storage for the codes
 * defined for a domain and their associated descriptions.
 *
 * @author	BitWise
 * @version	$Id: CachedScheme.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
public class CachedScheme extends Scheme
{
	/**
	 * Constructs a <CODE>CachedScheme</CODE> instance for the indicated
	 * scheme URI.
	 *
	 * @param	uri				The URI used to reference the scheme.
	 * @since	TFP 1.0
	 */
	public CachedScheme (final String uri)
	{
		super (uri);
	}
		
	/**
	 * Constructs a <CODE>CachedScheme</CODE> instance for the indicated
	 * scheme URI.
	 *
	 * @param	uri				The URI used to reference the scheme.
	 * @param	canonicalUri	The Canonical scheme URI.
	 * @since	TFP 1.2
	 */
	public CachedScheme (final String uri, final String canonicalUri)
	{
		super (uri, canonicalUri);
	}
		
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	public boolean isValid (final String code)
	{
		return (values.containsKey (code));
	}
	
	/**
	 * Provides the underlying storage for the code values.
	 * @since	TFP 1.0
	 */
	protected Hashtable<String, Value>	values
		= new Hashtable<String, Value> ();

	/**
	 * Adds a <CODE>Value</CODE> instance to the extent set managed by this
	 * instance. If the new value has the same code as an existing entry it
	 * will replace it and a reference to the old instance will be returned
	 * to the caller.
	 *
	 * @param	value		The scheme <CODE>Value</CODE> to be added.
	 * @return	The old <CODE>Value</CODE> instance having the same code as
	 *			the new one, otherwise <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
	protected final Value add (Value value)
	{
		return (values.put (value.getCode (), value));
	}
}