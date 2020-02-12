// Copyright (C),2006-2020 HandCoded Software Ltd.
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
 * A <CODE>RuleBook</CODE> instance holds a set of <CODE>IdentifierRule</CODE>
 * instances.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class RuleBook
{
	/**
	 * Constructs an empty <CODE>RuleBook</CODE>.
	 * @since	TFP 1.6
	 */
	public RuleBook ()
	{ }
	
	/**
	 * Adds an <CODE>IdentifierRule</CODE> to the <CODE>RuleBook</CODE>
	 * rule collection.
	 * 
	 * @param	identifier		The <CODE>IdentifierRule</CODE> to be added.
	 * @since	TFP 1.6
	 */
	public void add (final IdentifierRule identifier)
	{
		identifiers.put (identifier.getName (), identifier);
	}
	
	/**
	 * Attempts to locates an <CODE>IdentifierRule</CODE> with the indicated
	 * name.
	 * 
	 * @param	name			The name of the required rule.
	 * @return	An <CODE>IdentifierRule</CODE> if a match is found, <CODE>null</CODE>
	 * 			otherwise.
	 * @since	TFP 1.6
	 */
	public IdentifierRule find (final String name)
	{
		return (identifiers.get (name));
	}
	
	/**
	 * The extent set of <CODE>IdentifierRule</CODE> instances.
	 * @since	TFP 1.6
	 */
	private HashMap<String, IdentifierRule> identifiers
		= new HashMap<> ();
}