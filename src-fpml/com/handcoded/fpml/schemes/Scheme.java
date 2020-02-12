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

/**
 * A <CODE>Scheme</CODE> instance holds a collection of values used to validate
 * data fields that may only store a code value taken from this schemes domain.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public abstract class Scheme
{
	/**
	 * Provides access to the URI associated with this <CODE>Scheme</CODE>.
	 *
	 * @return	The URI for the FpML scheme.
	 * @since	TFP 1.0
	 */
	public final String getUri ()
	{
		return (uri);
	}	
	
	/**
	 * Provides access to the canonical URI associated with this <CODE>Scheme</CODE>.
	 *
	 * @return	The canonical URI for the FpML scheme, <CODE>null</CODE> if none.
	 * @since	TFP 1.2
	 */
	public final String getCanonicalUri ()
	{
		return (canonicalUri);
	}	
	
	/**
	 * Determines if the given code value is valid within the scheme.
	 *
	 * @param 	code			The code value to be validated.
	 * @return 	<CODE>true</CODE> if the code is valid, <CODE>false</CODE>
	 *			otherwise.
	 * @since	TFP 1.0
	 */
	public abstract boolean isValid (final String code);
	
	/**
	 * Constructs a <CODE>Scheme</CODE> instance to hold values for the
	 * domain identified by the given URI.
	 *
	 * @param	uri				The FpML URI for this domain of values.
	 * @param	canonicalUri	The FpML Canonical URI for the domain or <CODE>null</CODE>.
	 * @since	TFP 1.2
	 */
	protected Scheme (final String uri, final String canonicalUri)
	{
		this.uri 		  = uri;
		this.canonicalUri = canonicalUri;
	}
	
	/**
	 * Constructs a <CODE>Scheme</CODE> instance to hold values for the
	 * domain identified by the given URI.
	 *
	 * @param	uri				The FpML URI for this domain of values.
	 * @since	TFP 1.0
	 */
	protected Scheme (final String uri)
	{
		this (uri, null);
	}
	
	/**
	 * The URI for the FpML scheme.
	 * @since	TFP 1.0
	 */
	private final String		uri;

	/**
	 * The canonical URI for the FpML scheme.
	 * @since	TFP 1.2
	 */
	private final String		canonicalUri;
}