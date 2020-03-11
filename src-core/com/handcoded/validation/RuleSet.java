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

package com.handcoded.validation;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.handcoded.framework.Application;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.parser.SAXParser;

/**
 * A <CODE>RuleSet</CODE> instance contains a collection of validation rules
 * that can be tested against a DOM <CODE>Document</CODE> in a single
 * operation. 
 *
 * @author	Andrew Jacobs
 * @since 	TFP 1.0
 */
public final class RuleSet extends Validator
{
	/**
	 * Constructs an unnamed empty <CODE>RuleSet</CODE>.
	 * @since 	TFP 1.0
	 * @deprecated
	 */
	@Deprecated
	public RuleSet ()
	{
		this (null);
	}
	
	/**
	 * Constructs a named empty <CODE>RuleSet</CODE>.
	 * 
	 * @param	name		The name of the <CODE>RuleSet</CODE>.
	 * @since 	TFP 1.2
	 */
	public RuleSet (final String name)
	{
		if ((this.name = name) != null) extent.put (name, this);
	}
	
	/**
	 * Returns a reference to the named <CODE>RuleSet</CODE> instance if it exists.
	 * 
	 * @param 	name		The name of the required <CODE>RuleSet</CODE>.
	 * @return	The corresponding <CODE>RuleSet</CODE> instance.
	 * @since	TFP 1.2
	 */
	public static RuleSet forName (final String name)
	{
		synchronized (extent) {
			return (extent.computeIfAbsent (name, key -> new RuleSet (name)));
		}
	}
	
	/**
	 * Provides access to the <CODE>RuleSet</CODE> name.
	 * 
	 * @return	The name of the <CODE>RuleSet</CODE> or <CODE>null</CODE> if
	 *			unnamed.
	 * @since	TFP 1.2
	 */
	public final String getName ()
	{
		return (name);
	}
	
	/**
	 * Adds the indicated <CODE>Rule</CODE> instance to the <CODE>RuleSet
	 * </CODE>. A <CODE>Rule</CODE> may be referenced by several <CODE>
	 * RuleSet</CODE> instances simultaneously.
	 * <P>
	 * If the <CODE>Rule</CODE> has the same name as a previously added
	 * one then it will replace it. This feature can be used to overwrite
	 * standard rules with customised ones.
	 *
	 * @param 	rule			The <CODE>Rule</CODE> to be added.
	 * @since 	TFP 1.0
	 */
	public void add (Rule rule)
	{
		rules.put (rule.getName (), rule);
	}
	
	/**
	 * Adds the <CODE>Rule</CODE> instances that comprise another <CODE>
	 * RuleSet</CODE> to this one.
	 *
	 * @param 	ruleSet			The <CODE>RuleSet</CODE> to be added.
	 * @since 	TFP 1.0
	 */
	public void add (RuleSet ruleSet)
	{
		for (Rule rule : ruleSet.rules.values ())
			add (rule);
	}
	
	/**
	 * Attempts to remove a <CODE>Rule</CODE> with the given name from the
	 * collection held by the <CODE>RuleSet</CODE>.
	 *
	 * @param 	name			The name of the <CODE>Rule</CODE> to be removed.
	 * @return 	The <CODE>Rule</CODE> instance removed from the collection
	 *			or <CODE>null</CODE> if there was no match.
	 * @since 	TFP 1.0
	 */
	public Rule remove (String name)
	{
		return (rules.remove (name));
	}
	
	/**
	 * Attempts to remove a given <CODE>Rule</CODE> from the collection held
	 * by the <CODE>RuleSet</CODE>.
	 *
	 * @param 	rule			The <CODE>Rule</CODE> to be removed.
	 * @return 	The <CODE>Rule</CODE> instance removed from the collection
	 *			or <CODE>null</CODE> if there was no match.
	 * @since 	TFP 1.0
	 */
	public Rule remove (Rule rule)
	{
		return (remove (rule.getName ()));
	}
	
	/**
	 * Returns the current number of rules in the <CODE>RuleSet</CODE>
	 *
	 * @return	The number of rules in the <CODE>RuleSet</CODE>
	 * @since 	TFP 1.0
	 */
	public int size ()
	{
		return (rules.size ());
	}
	
	/**
	 * Converts the <CODE>RuleSet</CODE> to a string for debugging.
	 *
	 * @return 	A text description of the instance.
	 * @since 	TFP 1.0
	 */
	@Override
	public String toString ()
	{
		return (getClass ().getName () + " [" + toDebug () + "]");
	}

	/**
	 * {@inheritDoc}
	 * @since 	TFP 1.0
	 */
	@Override
	protected boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler)
	{
		boolean			result = true;
		HashMap<Precondition, Boolean> cache = new HashMap<> ();
		
		for (Rule rule : rules.values ()) {
			Precondition 	condition = rule.getPrecondition ();
			
			if (cache.computeIfAbsent (condition, pre -> pre.evaluate (nodeIndex, cache)).booleanValue ())
				result &= rule.validate (nodeIndex, errorHandler);
		}
			
		return (result);
	}
		
	/**
	 * Produces a debugging string describing the state of the rule
	 * collection.
	 *
	 * @return 	The debugging string.
	 * @since 	TFP 1.0
	 */
	protected String toDebug ()
	{
		StringBuilder		buffer = new StringBuilder ();
		
		for (Rule rule : rules.values ()) {
			if (buffer.length () > 0) buffer.append (',');
			buffer.append ('\"');
			buffer.append (rule.getName ());
			buffer.append ('\"');
		}
		return ("rules=" + buffer);
	}
	
	/**
	 * The <CODE>BootStrap</CODE> class provides a simple extension of the
	 * SAX <CODE>DefaultHandler</CODE> that captures the key values that
	 * define calendars from an XML initialisation file.
	 * 
	 * @since	TFP 1.0
	 */
	private static final class BootStrap extends DefaultHandler
	{
		/**
		 * This method is called when the XML parser has just processed the
		 * start of an XML element. 
		 * @since 	TFP 1.2 
		 */
		@Override
		public void startElement (String ns, String localName, String qName, Attributes attributes) // NOSONAR
		{
			if (localName.equals ("forceLoad")) {
				String		platform		= attributes.getValue ("platform");
				String		implementation	= attributes.getValue ("class");

				if (platform.equals ("Java")) {
					try {
						Class.forName (implementation);
					}
					catch (ClassNotFoundException error) {
						logger.severe ("Could not force load rule class '" + implementation + "'");
					}
				}
			}
			else if (localName.equals ("ruleSet")) {
				String		name	= attributes.getValue ("name");

				ruleSet = new RuleSet (name);
			}
			else if (localName.equals ("addRule")) {
				String		name	= attributes.getValue ("name");
				String		alias	= attributes.getValue ("alias");
				Rule		rule	= Rule.forName (name);
				
				if (rule != null) {
					if (ruleSet != null)
						ruleSet.add (rule);
					else
						logger.severe ("Syntax error in rule file - addRule outside of RuleSet");
					
					if (alias != null) rule.setAlias (alias);
				}
				else
					logger.severe ("Reference to undefined rule '" + name + "' in addRule");
			}
			else if (localName.equals ("removeRule")) {
				String		name	= attributes.getValue ("name");
				Rule		rule	= Rule.forName (name);
				
				if (rule != null) {
					if (ruleSet != null)
						ruleSet.remove (rule);
					else
						logger.severe ("Syntax error in rule file - removeRule outside of RuleSet");
				}
				else
					logger.severe ("Reference to undefined rule '" + name + "' in addRule");
			}
			else if (localName.equals ("addRuleSet")) {
				String		name	= attributes.getValue ("name");
				RuleSet		rules	= RuleSet.forName (name);
				
				if (rules != null) {
					if (ruleSet != null) {
						if (rules != ruleSet)
							ruleSet.add (rules);
						else
							logger.severe ("Attempt to recursively define ruleset '" + name + "'");
					}
					else
						logger.severe ("Syntax error in rule file - addRuleSet outside of RuleSet");
				}
				else
					logger.severe ("Reference to undefined rule '" + name + "' in addRule");
			}
		}
		
		/**
		 * This method is called when the XML parser has just processed the
		 * end of an XML element. 
		 * @since 	TFP 1.2 
		 */
		@Override
		public void endElement (String ns, String localName, String qName)
		{
			if (localName.equals ("ruleSet")) {
				ruleSet = null;
			}
		}
		
		/**
		 * The <CODE>RuleSet</CODE> instance being constructed.
		 * @since	TFP 1.2
		 */
		private RuleSet		ruleSet = null;
	}

	/**
	 * A <CODE>Logger</CODE> instance used to report run-time problems.
	 * @since	TFP 1.0
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.validation.RuleSet");
	
	/**
	 * The set of all named <CODE>RuleSet</CODE> instances.
	 * @since	TFP 1.2
	 */
	private static HashMap<String, RuleSet> extent
		= new HashMap<> ();
	
	/**
	 * The name of the <CODE>RuleSet</CODE> or <CODE>null</CODE> if not named
	 * (for backwards compatibility).
	 * @since	TFP 1.2
	 */
	private final String	name;
	
	/**
	 * The underlying collection of rules indexed by name.
	 * @since 	TFP 1.0
	 */
	private HashMap<String, Rule>	rules
		= new HashMap<> ();

	/**
	 * Causes the <CODE>RuleSet</CODE> class to try and bootstrap the business
	 * rules from a configuration file.
	 * @since	TFP 1.2
	 */
	static {
		try {
			SAXParser parser = new SAXParser (false, true, false, false, null, null);
			
			try {
				logger.info ("Bootstrapping business rules");
				parser.parse (new InputSource (Application.openStream ("files-core/business-rules.xml")),	new BootStrap ());
				logger.info ("Completed.");
			}
			catch (Exception error) {
				logger.log (Level.SEVERE, "Unable to load rule set definitions", error);	
			}
		}
		catch (Exception error) {
			logger.log (Level.SEVERE, "No suitable JAXP implementation installed", error);
		}
	}
}