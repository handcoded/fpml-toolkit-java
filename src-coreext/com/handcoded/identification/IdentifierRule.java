// Copyright (C),2006-2011 HandCoded Software Ltd.
// All rights reserved.
//
// This software is the confidential and proprietary information of HandCoded
// Software Ltd. ("Confidential Information").  You shall not disclose such
// Confidential Information and shall use it only in accordance with the terms
// of the license agreement you entered into with HandCoded Software.
//
// HANDCODED SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
// SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. HANDCODED SOFTWARE SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
// OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

package com.handcoded.identification;

import java.util.HashMap;

/**
 * The <CODE>IdentifierRule</CODE> class encapsulates the logic for extracting
 * properties from an <CODE>Object</CODE> and formatting them into an
 * identifier.
 * 
 * @author	BitWise
 * @version	$Id: IdentifierRule.java 13 2011-11-07 23:38:40Z andrew $
 * @since	TFP 1.6
 */
public final class IdentifierRule
{
	/**
	 * Constructs a <CODE>IdentifierRule</CODE> with the indicated name,
	 * property set and formatter instance.
	 * 
	 * @param	name			The name of the rule.
	 * @param	properties		The <CODE>Property</CODE> set.
	 * @param	formatter		The identifier <CODE>Formatter</CODE>.
	 * @since	TFP 1.6
	 */
	public IdentifierRule (final String name, final Property [] properties, 
			final Formatter formatter)
	{
		this.name = name;
		this.properties = properties;
		this.formatter = formatter;
	}

	/**
	 * Returns the name of <CODE>IdentifierRule</CODE>.
	 * 
	 * @return	The name assigned to the rule.
	 * @since	TFP 1.6
	 */
	public String getName ()
	{
		return (name);
	}
	
	/**
	 * Derives the identifier associated with the context <CODE>Object</CODE>.
	 * 
	 * @param 	context			The context <CODE>Object</CODE>.
	 * @return	The derived identifier string or <CODE>null</CODE>.
	 * @since	TFP 1.6
	 */
	public String getIdentifier (final Object context)
	{
		HashMap<String, String>	values = new HashMap<String, String> ();
		String			data;
		
		for (Property property : properties) {
			if ((data = property.getValue (context)) != null)
				values.put (property.getName (), data);
		}		
		return (formatter.format (values));
	}
	
	/**
	 * The name of this type of identifier.
	 * @since	TFP 1.6
	 */
	private final String		name;
	
	/**
	 * The set of <CODE>Property</CODE> instances that define it.
	 * @since	TFP 1.6
	 */
	private final Property [] 	properties;
	
	/**
	 * The <CODE>Formatter</CODE> used to combine the properties.
	 * @since	TFP 1.6
	 */
	private final Formatter		formatter;
}