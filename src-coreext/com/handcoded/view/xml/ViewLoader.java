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

package com.handcoded.view.xml;

import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.handcoded.framework.Application;
import com.handcoded.view.DataView;
import com.handcoded.view.Facet;
import com.handcoded.view.FacetSet;
import com.handcoded.view.Variable;
import com.handcoded.view.View;
import com.handcoded.xml.DOM;
import com.handcoded.xml.XPath;
import com.handcoded.xml.XmlUtility;
import com.handcoded.xpath.Function;

/**
 * The <CODE>ViewLoader</CODE> class provides a means to load an XML file
 * containing the definition of a <CODE>DataView</CODE> and create its in
 * memory representation.
 * 
 * @author 	Bitwise
 * @since	TFP 1.9
 */
public final class ViewLoader
{
	/**
	 * Loads the definition of a <CODE>DataView</CODE> from an XML file and
	 * constructs its in memory representation.
	 * 
	 * @param	filename		The name of the file containing the definition
	 * @return	A new <CODE>DataView</CODE> instance configured from the file
	 * 			contents.
	 * @since	TFP 1.9
	 */
	public static DataView load (String filename)
	{
		DataView			view = null;
		
		Document document = XmlUtility.nonValidatingParse (
				new InputSource (Application.openStream (filename)));
		
		if (document != null) {
			view = new DataView ();
			
			processView (view, (Element) document.getDocumentElement ());
		}
		else
			logger.severe ("Failed to load data view configuration: " + filename);

		return (view);
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.9
	 */
	private static Logger	logger
		= Logger.getLogger ("com.handcoded.view.xml.ViewLoader");

	/**
	 * Processes the definition of a dataView or facetSet element.
	 * 
	 * @param	view			The <CODE>View</CODE> instance to populate.
	 * @param	context			The context <CODE>Element</CODE>.
	 * @since	TFP 1.9
	 */
	private static void processView (View view, Element context)
	{
		NodeList	list;
		
		NamedNodeMap map = context.getAttributes ();
		for (int index = 0; index < map.getLength(); ++index) {
			Node node = map.item (index);
			String name = node.getNodeName ();
			
			if (name.startsWith("xmlns:"))
				view.addNamespace(name.substring(6), node.getNodeValue ());
		}
		
		list = XPath.paths (context, "function");
		for (int index = 0; index < list.getLength (); ++index)
			processFunction (view, (Element) list.item (index));
		
		list = XPath.paths (context, "variable");
		for (int index = 0; index < list.getLength (); ++index)
			processVariable (view, (Element) list.item (index));
		
		list = XPath.paths (context, "facet");
		for (int index = 0; index < list.getLength (); ++index)
			processFacet (view, (Element) list.item (index));
		
		list = XPath.paths (context, "facetSet");
		for (int index = 0; index < list.getLength (); ++index)
			processFacetSet (view, (Element) list.item (index));
	}
		
	/**
	 * Processes the definition of a function element.
	 * 
	 * @param	view			The <CODE>View</CODE> instance to populate.
	 * @param	context			The context <CODE>Element</CODE>.
	 * @since	TFP 1.9
	 */
	private static void processFunction (View view, Element context)
	{
		String platform		= DOM.getAttribute(context, "platform");
		
		if (platform.equals("Java")) {
			String name			= trim (DOM.getAttribute(context, "name"));
			String className	= trim (DOM.getAttribute(context, "class"));
		
			if ((name != null) && (className != null)) {
				int			index = name.indexOf(':');
				String		ns = null;
				
				if (index != -1) {
					String 		prefix = name.substring(0, index);
					
					name = name.substring(index + 1);
					ns = view.findNamespace (prefix);
				}
				
				view.addFunction (new Function (new QName (ns, name), className));
			}
			else
				logger.severe ("Invalid data view function declaration: "
					+ XPath.forNode (context));
		}
	}
	
	/**
	 * Processes the definition of a variable element.
	 * 
	 * @param	view			The <CODE>View</CODE> instance to populate.
	 * @param	context			The context <CODE>Element</CODE>.
	 * @since	TFP 1.9
	 */
	private static void processVariable (View view, Element context)
	{
		String name			= trim (DOM.getAttribute(context, "name"));
		String type			= trim (DOM.getAttribute(context, "type"));
		String expr			= trim (DOM.getAttribute(context, "select"));
		
		if ((name != null) && (expr != null) && (type != null))
			view.addVariable (new Variable (name, mapType (type), expr));
		else
			logger.severe ("Invalid data view variable declaration: "
					+ XPath.forNode (context));
	}
	
	/**
	 * Processes the definition of a facet element.
	 * 
	 * @param	view			The <CODE>View</CODE> instance to populate.
	 * @param	context			The context <CODE>Element</CODE>.
	 * @since	TFP 1.9
	 */
	private static void processFacet (View view, Element context)
	{
		String name			= DOM.getAttribute(context, "name");
		String type			= DOM.getAttribute(context, "type");
		String expr			= DOM.getAttribute(context, "select");
		
		if ((name != null) && (expr != null) && (type != null))
			view.addFacet (new Facet (name, mapType (type), expr));
		else
			logger.severe ("Invalid data view facet declaration: "
					+ XPath.forNode (context));
	}
	
	/**
	 * Processes the definition of a facetSet element.
	 * 
	 * @param	view			The <CODE>View</CODE> instance to populate.
	 * @param	context			The context <CODE>Element</CODE>.
	 * @since	TFP 1.9
	 */
	private static void processFacetSet (View view, Element context)
	{
		String test			= DOM.getAttribute(context, "test");

		if (test != null) {
			FacetSet facetSet = new FacetSet (view, test);
			
			view.addFacetSet (facetSet);
			processView (facetSet, context);
		}
		else
			logger.severe ("Invalid data view facetSet declaration: "
					+ XPath.forNode (context));
	}
	
	/**
	 * Maps the type code used in the definition to the value used by the
	 * XPath evaluator.
	 * 
	 * @param	type			The code to be mapped.
	 * @return	The corresponding value from <CODE>XPathConstants</CODE>.
	 * @since	TFP 1.9
	 */
	private static QName mapType (String type)
	{
		if (type.equals("boolean")) return (XPathConstants.BOOLEAN);
		if (type.equals("node")) 	return (XPathConstants.NODE);
		if (type.equals("nodeset")) return (XPathConstants.NODESET);
		if (type.equals("number")) 	return (XPathConstants.NUMBER);
		if (type.equals("string")) 	return (XPathConstants.STRING);
		
		return (null);
	}
	
	/**
	 * Trim a <CODE>String</CODE> value if it is not <CODE>null</CODE>.
	 * 
	 * @param	value			The value to be trimmed.
	 * @return	A trimmed <CODE>String</CODE> or <CODE>null</CODE>.
	 * @since	TFP 1.9
	 */
	private static String trim (String value)
	{
		return ((value != null) ? value.trim () : null);
	}
}