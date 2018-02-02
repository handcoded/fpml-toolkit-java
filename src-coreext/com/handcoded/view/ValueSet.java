// Copyright (C),2005-2017 HandCoded Software Ltd.
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

package com.handcoded.view;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A <CODE>ValueSet</CODE> instance holds the results of evaluating a set of
 * <CODE>Facet</CODE> definitions against a DOM document.
 * 
 * @author 	BitWise
 * @since	TFP 1.9
 */
public class ValueSet
{
	/**
	 * Constructs a new empty <CODE>ValueSet</CODE>.
	 * @since	TFP 1.9
	 */
	public ValueSet ()
	{ }
	
	/**
	 * Adds a named <CODE>Value</CODE> to the <CODE>ValueSet</CODE>.
	 * 
	 * @param	name		The name of the value to add.
	 * @param	value		The actual <CODE>Value</CODE>.
	 * @since	TFP 1.9
	 */
	public void add (String name, Value value)
	{
		// TODO: Remove debug
		System.out.println ("VA: " + name + " -> " + value.toDebug());
		values.put (name, value);
	}
	
	/**
	 * Adds a <CODE>Value</CODE> to the <CODE>ValueSet</CODE> using
	 * the name from the indicated <CODE>Facet</CODE>.
	 * 
	 * @param	facet		The <CODE>Facet</CODE>.
	 * @param	value		The actual <CODE>Value</CODE>.
	 * @since	TFP 1.9
	 */
	public void add (Facet facet, Value value)
	{
		add (facet.getName (), value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.9
	 */
	@Override
	public String toString ()
	{
		StringBuffer	buffer = new StringBuffer ();
		Object []		keys = values.keySet ().toArray ();
		
		Arrays.sort (keys);
		
		buffer.append('{');
		for (int index = 0; index < keys.length; ++index) {
			if (index != 0) buffer.append (",");
			buffer.append ("\n\t");
			buffer.append ((String) keys [index]);
			buffer.append ("='");
			buffer.append (values.get(keys [index]));
			buffer.append ('\'');
		}
		buffer.append("\n}");
		
		return (buffer.toString());
	}

	/**
	 * The underlying storage for the values.
	 * @since	TFP 1.9
	 */
	private HashMap<String, Value> values
		= new HashMap<String, Value> ();
}