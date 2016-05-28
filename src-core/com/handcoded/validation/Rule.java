// Copyright (C),2005-2012 HandCoded Software Ltd.
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

package com.handcoded.validation;

import java.util.Enumeration;
import java.util.Hashtable;

import com.handcoded.xml.NodeIndex;

/**
 * A <CODE>Rule</CODE> instance encapsulates a validation rule that can be
 * tested against a DOM <CODE>Document</CODE>.
 * <P>
 * Anonymous inheritance is the easiest way to implement a specific rule with
 * the minimum of Java code. 
 *
 * @author	BitWise
 * @version	$Id: Rule.java 651 2012-08-14 22:04:52Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class Rule extends Validator
{
	/**
	 * Returns a reference to the named <CODE>Rule</CODE> instance if it exists.
	 * 
	 * @param 	name		The name of the required <CODE>Rule</CODE>.
	 * @return	The corresponding <CODE>Rule</CODE> instance or <CODE>null</CODE>.
	 * @since	TFP 1.2
	 */
	public static Rule forName (final String name)
	{
		return (extent.get (name));
	}
	
	/**
	 * Returns an <CODE>Enumeration</CODE> that can be used to iterate over
	 * the <CODE>Rule</CODE> instances that have been constructed.
	 * 
	 * @return	An <CODE>Enumeration</CODE> of <CODE>Rule</CODE> instances.
	 * @since	TFP 1.6
	 */
	public static Enumeration<Rule> Rules ()
	{
		return (extent.elements ());
	}
	
	/**
	 * Provides access to the <CODE>Precondition</CODE>
	 * 
	 * @return	The <CODE>Precondition</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final Precondition getPrecondition ()
	{
		return (precondition);
	}
	
	/**
	 * Provides access to the unique rule name.
	 *
	 * @return	The rule name.
	 * @since	TFP 1.0
	 */
	public final String getName ()
	{
		return (name);
	}
	
	/**
	 * Provides access to the rule alias.
	 *
	 * @return	The rule alias.
	 * @since	TFP 1.6
	 */
	public final String getAlias ()
	{
		return (alias);
	}
	
	/**
	 * Changes the alias name value.
	 * 
	 * @param 	value		The new alias name.
	 * @since	TFP 1.6
	 */
	public final void setAlias (final String value)
	{
		alias = value;
	}
	
	/**
	 * Provides access to the display name.
	 *
	 * @return	The display name.
	 * @since	TFP 1.6
	 */
	public final String getDisplayName ()
	{
		return ((alias != null) ? alias : name);
	}

	/**
	 * Evaluates the <CODE>Precondition</CODE> against an indexed <CODE>Document
	 * </CODE> to see if it applies.
	 * 
	 * @param 	nodeIndex		The <CODE>NodeIndex</CODE> for the <CODE>Document</CODE>.
	 * @return	A <CODE>boolean</CODE> indicating if the <CODE>Rule</CODE> is
	 * 			applicable.
	 * @since	TFP 1.0
	 */
	public final boolean appliesTo (final NodeIndex nodeIndex)
	{
		return (precondition.evaluate (nodeIndex, new Hashtable<Precondition, Boolean> ()));
	}
	
	/**
	 * Converts the <CODE>Rule</CODE> to a string for debugging.
	 *
	 * @return 	A text description of the instance.
	 * @since	TFP 1.0
	 */
	@Override
	public String toString ()
	{
		return (getClass ().getName () + " [" + toDebug () + "]");
	}

	/**
	 * Constructs a <CODE>Rule</CODE> with the given name and which applies
	 * to all documents.
	 *
	 * @param 	name			The unique rule name for this instance.
	 * @since	TFP 1.0
	 */
	protected Rule (final String name)
	{
		this (Precondition.ALWAYS, name);
	}
		
	/**
	 * Constructs a <CODE>Rule</CODE> with the given name and which applies
	 * in the circumstances defined by its <CODE>Precondition</CODE>.
	 *
	 * @param	precondition	A <CODE>Precondition</CODE> instance.
	 * @param 	name			The unique rule name for this instance.
	 * @since	TFP 1.0
	 */
	protected Rule (final Precondition precondition, final String name)
	{
		this.precondition 	= precondition;
		this.name			= name;
		
		extent.put (name, this);
	}
		
	/**
	 * Produces a debugging string describing the state of the instance.
	 *
	 * @return 	The debugging string.
	 * @since	TFP 1.0
	 */
	protected String toDebug ()
	{
		return ("precondition=\"" + precondition + "\" name=\"" + name + "\"");
	}
	
	/**
	 * The set of all <CODE>Rule</CODE> instances indexed by name.
	 * @since	TFP 1.2
	 */
	private static Hashtable<String, Rule> extent = new Hashtable<String, Rule> ();
	
	/**
	 * The <CODE>Precondition</CODE> for this rule.
	 * @since	TFP 1.0
	 */
	private final Precondition	precondition;
	
	/**
	 * The unique name for this <CODE>Rule</CODE>.
	 * @since	TFP 1.0
	 */
	private final String		name;
	
	/**
	 * The alias for this <CODE>Rule</CODE>.
	 * @since	TFP 1.6
	 */
	private String				alias = null;
}