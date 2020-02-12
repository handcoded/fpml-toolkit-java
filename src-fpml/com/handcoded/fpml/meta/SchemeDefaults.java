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

package com.handcoded.fpml.meta;

import java.util.HashMap;

/**
 * The <CODE>SchemeDefaults</CODE> class provides a way to find out the URI associated
 * with a particular type of coded data. An instance of <CODE>SchemeDefaults</CODE>
 * exists for each supported release of FpML and is customised to return the
 * appropriate URI for that version.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class SchemeDefaults
{	
	/**
	 * Constructs a <CODE>SchemeDefaults</CODE> instance for an FpML 4-0 or
	 * later based instance in which scheme URI references can have schema
	 * defined default values.
	 * 
	 * @param	values			An array of scheme names and default URIs
	 * @since	TFP 1.0
	 */
	public SchemeDefaults (final String values [][])
	{
		for (int index = 0; index < values.length; ++index)
			defaultValues.put (values [index][0], values [index][1]);
	}
	
	/**
	 * Constructs a <CODE>SchemeDefaults</CODE> instance.
	 * 
	 * @param	values			An array of scheme names and default URIs
	 * @param	names			An array of attribute names and default URIs
	 * @since	TFP 1.0
	 */
	public SchemeDefaults (final String values [][], final String names [][])
	{
		this (values);

		for (int index = 0; index < names.length; ++index)
			defaultAttrs.put (names [index][0], names [index][1]);
	}
		
	/**
	 * Returns the URI normally associated with the given scheme default
	 * attribute.
	 * 
	 * @param 	name			The name of the scheme default attribute
	 * @return	The default URI associated with this scheme default, or
	 * 			<CODE>null</CODE> if none.
	 * @since	TFP 1.0
	 */
	public String getDefaultUriForAttribute (String name)
	{
		return (defaultValues.get (name));
	}
	
	/**
	 * Returns the name of the scheme default attribute that may provide
	 * the default value of a given scheme attribute. For example the
	 * <CODE>currencyScheme</CODE> attribute will default to the value of
	 * the <CODE>currencySchemeDefault</CODE> attribute.
	 *  
	 * @param 	name			The name of the scheme attribute
	 * @return	The name of the scheme default attribute associated with
	 * 			this scheme attribute.
	 * @since	TFP 1.0
	 */
	public String getDefaultAttributeForScheme (String name)
	{
		return (defaultAttrs.get (name));
	}
	
	/**
	 * A <CODE>HashMap</CODE> containing the scheme URI associated with each
	 * scheme default attribute.
	 * @since	TFP 1.0
	 */
	private HashMap<String, String> defaultValues
		= new HashMap<> ();

	/**
	 * A <CODE>HashMap</CODE> relating a scheme attribute name to its
	 * corresponding scheme default attribute (e.g. partyIdScheme uses
	 * partyIdSchemeDefault).
	 * @since	TFP 1.0
	 */
	private HashMap<String, String> defaultAttrs
		= new HashMap<> ();
}