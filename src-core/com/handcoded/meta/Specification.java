// Copyright (C),2005-2019 HandCoded Software Ltd.
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

package com.handcoded.meta;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.framework.Application;
import com.handcoded.xml.DOM;
import com.handcoded.xml.Types;
import com.handcoded.xml.XPath;
import com.handcoded.xml.XmlUtility;

/**
 * Instances of the <CODE>Specification</CODE> class represent XML based
 * data models such as those for the standards FpML and FixML.
 * 
 * @author	BitWise
 * @since 	TFP 1.0
 */
public final class Specification
{
	/**
	 * Constructs a <CODE>Specification</CODE> with the given name.
	 * 
	 * @param 	name			The specification name.
	 * @since	TFP 1.0
	 */
	public Specification (final String name)
	{
		extent.put (this.name = name, this);
	}
	
	/**
	 * Attempts to locate a <CODE>Specification</CODE> instance with the given
	 * name.
	 * 
	 * @param 	name			The target <CODE>Specification</CODE> name.
	 * @return	The corresponding <CODE>Specification</CODE> or <CODE>null
	 * 			</CODE> if no matching instance exists.
	 * @since	TFP 1.0
	 */
	public static Specification forName (final String name)
	{
		return ((Specification) extent.get (name));
	}
	
	/**
	 * Attempts to locate the <CODE>Specification</CODE> instance corresponding
	 * to the given <CODE>Document</CODE>.
	 * 
	 * @param	document			The <CODE>Document</CODE> to examine.
	 * @return	The <CODE>Specification</CODE> instance corresponding to the
	 * 			<CODE>Document</CODE> or <CODE>null</CODE> if its not
	 * 			recognised.
	 * @since	TFP 1.0
	 */
	public static Specification forDocument (Document document)
	{
		Release			release = releaseForDocument (document);
		
		return ((release != null) ? release.getSpecification () : null);
	}
	
	/**
	 * Attempts to locate the <CODE>Release</CODE> instance corresponding
	 * to the given <CODE>Document</CODE>.
	 * 
	 * @param	document			The <CODE>Document</CODE> to examine.
	 * @return	The <CODE>Release</CODE> instance corresponding to the
	 * 			<CODE>Document</CODE> or <CODE>null</CODE> if its not
	 * 			recognised.
	 * @since	TFP 1.0
	 */
	public static Release releaseForDocument (Document document)
	{
		Enumeration<String>	cursor	= extent.keys ();
		
		while (cursor.hasMoreElements ()) {
			Release	release = extent.get (cursor.nextElement ()).getReleaseForDocument (document);
			
			if (release != null) return (release);
		}
		return (null);
	}
	
	/**
	 * Returns an array containing all the <CODE>Specification</CODE>
	 * instances in the class extent.
	 * 
	 * @return	An array of all the instances.
	 * @since	TFP 1.6
	 */
	public static Enumeration<Specification> specifications ()
	{
		return (extent.elements ());
	}
		
	/**
	 * Provides access to the specification name.
	 * 
	 * @return	The specification name.
	 * @since	TFP 1.0
	 */
	public String getName ()
	{
		return (name);
	}
	
	/**
	 * Determines if the given <CODE>Document</CODE> is an instance of some
	 * <CODE>Release</CODE> of this <CODE>Specification</CODE>.
	 * 
	 * @param	document		The <CODE>Document</CODE> to be examined.
	 * @return	<CODE>true</CODE> if this document is an instance of the
	 * 			<CODE>Specification</CODE>, <CODE>false</CODE> otherwise.
	 * @since 	TFP 1.0
	 */
	public boolean isInstance (Document document)
	{
		return (getReleaseForDocument (document) != null);
	}
	
	/**
	 * Attempts to locate a <CODE>Release</CODE> of the current
	 * <CODE>Specification</CODE> that is compatible with the given
	 * <CODE>Document</CODE>.
	 * 
	 * @param 	document		The <CODE>Document</CODE> to be examined.
	 * @return	The corresponding <CODE>Release</CODE> or <CODE>null</CODE>
	 * 			if a match could not be found.
	 * @since	TFP 1.0
	 */
	public Release getReleaseForDocument (Document document)
	{
		Enumeration<Release> cursor = releases.elements ();
		
		while (cursor.hasMoreElements ()) {
			Release	release = cursor.nextElement ();
			if (release.isInstance (document)) return (release);
		}
		return (null);
	}

	/**
	 * Attempts to locate a <CODE>Release</CODE> associated with this
	 * <CODE>Specification</CODE> with the indicated version identifier.
	 * 
	 * @param 	version			The target version identifier.
	 * @return	The corresponding <CODE>Release</CODE> instance or <CODE>null
	 * 			</CODE> if none exists.
	 * @since	TFP 1.0
	 */
	public Release getReleaseForVersion (final String version)
	{
		Enumeration<Release> cursor = releases.elements ();
		
		while (cursor.hasMoreElements ()) {
			Release	release = cursor.nextElement ();
			if (release.getVersion ().equals (version)) return (release);
		}
		return (null);
	}
	
	/**
	 * Attempts to locate a <CODE>SchemaRelease</CODE> associated with this
	 * <CODE>Specification</CODE> with the indicated version identifier and
	 * namespace URI.
	 * 
	 * @param 	version			The target version identifier.
	 * @param 	namespaceUri	The target namespace URI.
	 * @return	The corresponding <CODE>SchemaRelease</CODE> instance or
	 * 			<CODE>null</CODE> if none exists.
	 * @since	TFP 1.5
	 */
	public SchemaRelease getReleaseForVersionAndNamespace (final String version, final String namespaceUri)
	{
		Enumeration<Release> cursor = releases.elements ();
		
		while (cursor.hasMoreElements ()) {
			Release release = cursor.nextElement ();
			if (release instanceof SchemaRelease) {
				SchemaRelease schemaRelease = (SchemaRelease) release;
				if (schemaRelease.getVersion ().equals (version) &&
					schemaRelease.getNamespaceUri ().equals (namespaceUri))
					return (schemaRelease);
			}
		}
		return (null);
	}
	
	/**
	 * Adds the indicated <CODE>Release</CODE> instance to the set managed
	 * by the specification.
	 * 
	 * @param 	release			The <CODE>Release</CODE> instance to add.
	 * @since	TFP 1.0 
	 */
	public void add (Release release)
	{
		if (release.getSpecification () != this)
			throw new AssertionError ("The provided release is for a different specification");
				
		releases.add (release);
	}
	
	/**
	 * Removes the indicated <CODE>Release</CODE> instance from the set managed
	 * by the <CODE>Specification</CODE>.
	 * 
	 * @param 	release			The <CODE>Release</CODE> to be removed.
	 * @since	TFP 1.0
	 */
	public void remove (Release release)
	{
		if (release.getSpecification () != this)
			throw new AssertionError ("The provided release is for a different specification");

		releases.remove (release);
	}
	
	/**
	 * Returns an <CODE>Enumeration</CODE> that can be used to iterate through
	 * the <CODE>Release</CODE> instances for this <CODE>Specification</CODE>.
	 * 
	 * @return	An <CODE>Enumeration</CODE> instance.
	 * @since	TFP 1.0
	 */
	public Enumeration<Release> releases ()
	{
		return (releases.elements ());
	}
	
	/**
	 * Returns a hash code for this instance based on its name.
	 * 
	 * @return	The hash code for this instance.
	 * @since	TFP 1.0 
	 */
	@Override
	public int hashCode ()
	{
		return (name.hashCode ());
	}
	
	/**
	 * Converts the <CODE>Specification</CODE> to a string for debugging.
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
	 * Produces a debugging string describing the state of the instance.
	 *
	 * @return 	The debugging string.
	 * @since	TFP 1.0
	 */
	protected String toDebug ()
	{
		StringBuffer		buffer = new StringBuffer ();
		
		buffer.append ("name=\"");
		buffer.append (name);
		buffer.append ("\", releases={");
		
		Enumeration<Release> cursor = releases.elements ();
		boolean		first  = true;
		
		while (cursor.hasMoreElements()) {
			if (!first) buffer.append (',');

			buffer.append ('"');
			buffer.append (cursor.nextElement ().getVersion ());
			buffer.append ('"');
			first = false;
		}
		buffer.append ("}");
		
		return (buffer.toString ());
	}
		                              
	/**
	 * A <CODE>Logger</CODE> instance used to report run-time problems.
	 * @since	TFP 1.4
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.meta.Specification");
	
	/**
	 * The extent set of all <CODE>Specification</CODE> instances.
	 * @since	TFP 1.0
	 */
	private static Hashtable<String, Specification>	extent
		= new Hashtable<String, Specification> ();
	
	/**
	 * The unique name of this <CODE>Specification</CODE>.
	 * @since 	TFP 1.0
	 */
	private final String 		name;
	
	/**
	 * The set of <CODE>Release</CODE> instances associated with this
	 * <CODE>Specification</CODE>.
	 * @since	TFP 1.0
	 */
	private Vector<Release>		releases	= new Vector<Release> ();
	
	/**
	 * If the releases file defines a custom class loader to be used the process
	 * the data block identified by the context element then return its name,
	 * otherwise return the indicated default class name.
	 * 
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	defaultClass	The name of the default class loader if not overridden.
	 * @return	The name of the class loader to be instantiated.
	 * @since	TFP 1.5
	 */
	private static String getClassLoader (Element context, final String defaultClass)
	{
		NodeList	list = XPath.paths (context, "classLoader");
		for (int index = 0; index < list.getLength (); ++index) {
			Element element = (Element) list.item (index);
			String platform = DOM.getAttribute (element, "platform");
			
			if ((platform != null) && platform.equals ("Java"))
				return (DOM.getAttribute (element, "class"));
		}
		return (defaultClass);
	}
	
	/**
	 * Creates a <CODE>ReleaseLoader</CODE> that can process a DTD meta
	 * definition.
	 * 
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @return	An instance of the <CODE>ReleaseLoader</CODE> interface.
	 * @throws 	ClassNotFoundException
	 * 			If the default or overriding class name is not valid.
	 * @throws 	InstantiationException
	 * 			If an instance of the <CODE>ReleaseLoader</CODE> could not
	 * 			be created.
	 * @throws 	IllegalAccessException
	 * 			If the class being dynamically created can not be publically
	 * 			accessed.
	 * @since	TFP 1.5
	 */
	private static ReleaseLoader getDtdReleaseLoader (Element context)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		String targetName = getClassLoader (context, "com.handcoded.meta.DefaultDTDReleaseLoader");
		
		try {
			Class<?> targetClass = Class.forName (targetName);
		
			return ((ReleaseLoader) targetClass.newInstance ());
		}
		catch (ClassNotFoundException error) {
			logger.log (Level.SEVERE, "Reference to undefined class loader type: " + targetName);
			throw error;
		}
	}
	
	/**
	 * Creates a <CODE>ReleaseLoader</CODE> that can process a schema meta
	 * definition.
	 * 
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @return	An instance of the <CODE>ReleaseLoader</CODE> interface.
	 * @throws 	ClassNotFoundException
	 * 			If the default or overriding class name is not valid.
	 * @throws 	InstantiationException
	 * 			If an instance of the <CODE>ReleaseLoader</CODE> could not
	 * 			be created.
	 * @throws 	IllegalAccessException
	 * 			If the class being dynamically created can not be publicly
	 * 			accessed.
	 * @since	TFP 1.5
	 */
	private static ReleaseLoader getSchemaReleaseLoader (Element context)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		String targetName = getClassLoader (context, "com.handcoded.meta.DefaultSchemaReleaseLoader");
		
		try {
			Class<?> targetClass = Class.forName (targetName);
		
			return ((ReleaseLoader) targetClass.newInstance ());
		}
		catch (ClassNotFoundException error) {
			logger.log (Level.SEVERE, "Reference to undefined class loader type: " + targetName);
			throw error;
		}
	}
	
	/**
	 * Bootstrap the entire collection of specifications by processing the
	 * contents of the 'files/releases.xml' file.
	 * @since	TFP 1.5
	 */
	static {
		Hashtable<String, SchemaRelease> loadedSchemas = new Hashtable<String, SchemaRelease> ();
		
		logger.log (Level.INFO, "Bootstrapping Specifications");
		
		try {
			Document document = XmlUtility.nonValidatingParseWithXInclude (
					Application.openInputSource ("files-core/releases.xml"));
			
			NodeList specifications = XPath.paths (document.getDocumentElement (), "specification");
			for (int index = 0; index < specifications.getLength (); ++index) {
				Element context = (Element) specifications.item (index);
				Element name = XPath.path (context, "name");
				
				Specification specification = new Specification (Types.toToken (name));
				
				NodeList dtds = XPath.paths (context, "dtdRelease");
				for (int count = 0; count < dtds.getLength (); ++count) {
					Element node = (Element) dtds.item (count);
					getDtdReleaseLoader (node).loadData (specification, node, loadedSchemas);
				}
				
				NodeList schemas = XPath.paths (context, "schemaRelease");
				for (int count = 0; count < schemas.getLength (); ++count) {
					Element node = (Element) schemas.item (count);
					getSchemaReleaseLoader (node).loadData (specification, node, loadedSchemas);
				}
			}
		}
		catch (Exception error) {
			logger.log (Level.SEVERE, "Unable to load specifications", error);	
		}
		
		logger.log (Level.INFO, "Completed");
	}
}