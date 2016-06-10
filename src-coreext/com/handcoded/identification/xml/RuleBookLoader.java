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

package com.handcoded.identification.xml;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.handcoded.framework.Application;
import com.handcoded.identification.Extractor;
import com.handcoded.identification.Formatter;
import com.handcoded.identification.IdentifierRule;
import com.handcoded.identification.Property;
import com.handcoded.identification.RuleBook;
import com.handcoded.identification.Source;
import com.handcoded.xml.DOM;
import com.handcoded.xml.XPath;
import com.handcoded.xml.XmlUtility;

/**
 * Creates a <CODE>RuleBook</CODE> by parsing the contents of an XML
 * configuration file and constructing the appropriate objects.
 * 
 * @author 	BitWise
 * @version	$Id: RuleBookLoader.java 11 2011-11-04 23:16:14Z andrew $
 * @since	TFP 1.6
 */
public final class RuleBookLoader
{
	/**
	 * Creates a <CODE>RuleBook</CODE> by parsing the content of the
	 * indicated configuration file.
	 * 
	 * @param	filename		The configuration filename.
	 * @return	A <CODE>RuleBook</CODE> instance created using the
	 * 			details in the configuration file.
	 * @since	TFP 1.6
	 */
	public static RuleBook load (final String filename)
	{
		RuleBook			ruleBook = new RuleBook ();
		
		Document document = XmlUtility.nonValidatingParse (
				new InputSource (Application.openStream (filename)));

		NodeList list = DOM.getChildElements (document.getDocumentElement ());
		for (int index = 0; index < list.getLength (); ++index) {
			Element context = (Element) list.item (index);
			
			if (context.getLocalName ().equals ("identifier")) {
				String	name 	= context.getAttribute ("name");
				
				Property [] properties  = loadProperties (XPath.paths (context, "property"));
				Formatter formatter = (Formatter) loadClass (XPath.paths (context, "formatter"));
				
				ruleBook.add (new IdentifierRule (name, properties, formatter));
			}
			else
				logger.log (Level.WARNING, "Unexpected element '" + context.getLocalName () + "'");
		}
		
		return (ruleBook);
	}

	/**
	 * Logging instance used for error reporting.
	 * @since	TFP 1.6
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.identification.xml.RuleBookLoader");

	/**
	 * A hash table of already created instances by class name.
	 * @since	TFP 1.6
	 */
	private static Hashtable<String, Object> instances
		= new Hashtable<String, Object> ();
	
	/**
	 * The default extractor used when none is explicitly specified.
	 * @since	TFP 1.6
	 */
	public static Extractor		defaultExtractor
		= (Extractor) loadClass ("com.handcoded.identification.xml.StringExtractor");
		
	/**
	 * Ensures no instances can be constructed.
	 * @since	TFP 1.6
	 */
	private RuleBookLoader ()
	{ }
	
	/**
	 * Creates an array of <CODE>Property</CODE> instances from the
	 * configuration file details.
	 * 
	 * @param	list			A <CODE>NodeList</CODE> of all the property elements.	
	 * @return	An array of the constructed instances.
	 * @since	TFP 1.6
	 */
	private static Property [] loadProperties (NodeList list)
	{
		Property [] result = new Property [list.getLength ()];
		
		for (int index = 0; index < list.getLength (); ++index)
			result [index] = loadProperty ((Element) list.item (index));
		return (result);
	}
	
	/**
	 * Creates a <CODE>Property</CODE> instance from the configuration
     * file details.
     * 
	 * @param	context			The <CODE>XmlElement</CODE> for the property.
	 * @return	A constructed <CODE>Property</CODE> instance.
	 * @since	TFP 1.6
	 */
	private static Property loadProperty (Element context)
	{
		String		name 		= context.getAttribute ("name");
		Extractor 	extractor 	= loadExtractor (XPath.paths (context, "extractor"));
		NodeList	list		= XPath.paths (context, "source");
				
		Source [] sources = new Source [list.getLength ()];
		for (int index = 0; index < list.getLength (); ++index)
			sources [index] = loadSource ((Element) list.item (index));
			
		return (new Property (name, extractor, sources));	
	}
	
	/**
	 * Creates an <CODE>XmlSource</CODE> that will extract data from the
	 * target source location.
	 * 
	 * @param	context			The <CODE>Element</CODE> describing the source.
	 * @return	A new <CODE>Source</CODE> instance that will be used to 
	 * 			extract data from a document.
	 * @since	TFP 1.6
	 */
	private static Source loadSource (Element context)
	{
		return (new XmlSource (context.getAttribute ("xpath")));
	}
	
	/**
	 * Finds a class to use to control data extraction. If none is specified
	 * then a default instance is used.
	 * 
	 * @param	list			The <CODE>NodeList</CODE> of class references.
	 * @return	An instance of the created class or <CODE>null</CODE>.
	 * @since	TFP 1.6
	 */
	private static Extractor loadExtractor (NodeList list)
	{
		Extractor		result = (Extractor) loadClass (list);
		
		return ((result != null) ? result : defaultExtractor);
	}
	
	/**
	 * Attempts to load a class dynamically, selecting the appropriate
	 * one for the execution platform.
	 * 
	 * @param	list			The <CODE>NodeList</CODE> of class references.
	 * @return	An instance of the created class or <CODE>null</CODE>.
	 * @since	TFP 1.6
	 */
	private static Object loadClass (NodeList list)
	{
		for (int index = 0; index < list.getLength (); ++index) {
			Element		element		= (Element) list.item (index);
			
			if (element.getAttribute ("platform").equals ("Java"))
				return (loadClass (element.getAttribute ("class")));
		}
		return (null);
	}	
	
	/**
	 * If an instance of the given class has already been constructed then
	 * re-use it, otherwise dynamically construct a new instance.
	 * 
	 * @param	className		The target class name.
	 * @return	An instance of the target class or <CODE>null</CODE> if
	 * 			one could not be constructed.
	 * @since	TFP 1.6
	 */
	private static Object loadClass (final String className)
	{
		Object		result;
		
		if ((result = instances.get (className)) == null) {
			try {
				result = Class.forName (className).newInstance ();
				instances.put (className, result);
			}
			catch (Exception error) {
				logger.log (Level.SEVERE, "Failed to create instance of class '" + className + "'", error);
			}
		}
		return (result);
	}
}