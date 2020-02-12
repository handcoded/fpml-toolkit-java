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

package com.handcoded.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Provides utility functions for navigating a DOM tree, often combining
 * several simpler operations or returning a more useful type.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class DOM
{
	/**
	 * Finds the first child <CODE>Element</CODE> of the given parent
	 * <CODE>Element</CODE> skipping any intermediate non-element <CODE>Node
	 * </CODE> instances.
	 * 
	 * @param 	parent		The parent <CODE>Element</CODE>.
	 * @return	The first child <CODE>Element</CODE> or <CODE>null</CODE>
	 * 			if none exists.
	 * @since	TFP 1.0
	 */
	public static Element getFirstChild (final Element parent)
	{
		Node		node = parent.getFirstChild ();
		
		while ((node != null) && (node.getNodeType () != Node.ELEMENT_NODE))
			node = node.getNextSibling ();
		
		return ((Element) node);
	}
	
	/**
	 * Finds the last child <CODE>Element</CODE> of the given parent
	 * <CODE>Element</CODE> skipping any intermediate non-element <CODE>Node
	 * </CODE> instances.
	 * 
	 * @param 	parent		The parent <CODE>Element</CODE>.
	 * @return	The first child <CODE>Element</CODE> or <CODE>null</CODE>
	 * 			if none exists.
	 * @since	TFP 1.0
	 */
	public static Element getLastChild (final Element parent)
	{
		Node		node = parent.getLastChild ();
		
		while ((node != null) && (node.getNodeType () != Node.ELEMENT_NODE))
			node = node.getPreviousSibling ();
		
		return ((Element) node);
	}
	
	/**
	 * Finds the next sibling <CODE>Element</CODE> of the given context
	 * <CODE>Element</CODE> skipping any intermediate non-element <CODE>Node
	 * </CODE> instances.
	 * 
	 * @param 	element		The context <CODE>Element</CODE>.
	 * @return	The next sibling <CODE>Element</CODE> or <CODE>null</CODE>
	 * 			if none exists.
	 * @since	TFP 1.0
	 */
	public static Element getNextSibling (final Element element)
	{
		Node		node = element.getNextSibling ();
		
		while ((node != null) && (node.getNodeType () != Node.ELEMENT_NODE))
			node = node.getNextSibling ();
		
		return ((Element) node);
	}
	
	/**
	 * Finds the previous sibling <CODE>Element</CODE> of the given context
	 * <CODE>Element</CODE> skipping any intermediate non-element <CODE>Node
	 * </CODE> instances.
	 * 
	 * @param 	element		The context <CODE>Element</CODE>.
	 * @return	The previous sibling <CODE>Element</CODE> or <CODE>null</CODE>
	 * 			if none exists.
	 * @since	TFP 1.0
	 */
	public static Element getPreviousSibling (final Element element)
	{
		Node		node = element.getPreviousSibling ();
		
		while ((node != null) && (node.getNodeType () != Node.ELEMENT_NODE))
			node = node.getPreviousSibling ();
		
		return ((Element) node);
	}
	
	/**
	 * Finds the parent <CODE>Element</CODE> of the given context <CODE>Element
	 * </CODE>.
	 * @param 	element		The context <CODE>Element</CODE>.
	 * @return	The parent <CODE>Element</CODE> or <CODE>null</CODE> if the
	 * 			context <CODE>Element</CODE> was the root element.
	 * @since	TFP 1.0
	 */
	public static Element getParent (final Element element)
	{
		if (element != null) {
			Node	parent  = element.getParentNode ();
			
			if ((parent != null) && (parent.getNodeType () == Node.ELEMENT_NODE))
				return ((Element) parent);
		}
		return (null);
	}
	
	/**
	 * Finds the grand parent <CODE>Element</CODE> of the given context
	 * <CODE>Element</CODE>.
	 * 
	 * @param 	context		The context <CODE>Element</CODE>.
	 * @return	The grand parent <CODE>Element</CODE> or <CODE>null</CODE> if
	 * 			the context <CODE>Element</CODE> was the near the root element.
	 * @since	TFP 1.0
	 */
	public static Element getGrandParent (Element context)
	{
		return (getParent (getParent (context)));
	}
	
	/**
	 * Finds the great grand parent <CODE>Element</CODE> of the given context
	 * <CODE>Element</CODE>.
	 * 
	 * @param 	context		The context <CODE>Element</CODE>.
	 * @return	The parent <CODE>Element</CODE> or <CODE>null</CODE> if the
	 * 			context <CODE>Element</CODE> was the root element.
	 * @since	TFP 1.0
	 */
	public static Element getGreatGrandParent (Element context)
	{
		return (getParent (getParent (getParent (context))));
	}

	/**
	 * Determines if the context <CODE>Element</CODE> has at least one
	 * child <CODE>Element</CODE>.
	 * 
	 * @param 	element		The context <CODE>Element</CODE>.
	 * @return	<CODE>true</CODE> if at least one child <CODE>Element</CODE>
	 * 			exists, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean hasChildNodes (final Element element)
	{
		for (Node node = element.getFirstChild (); node != null;) {
			if (node.getNodeType () == Node.ELEMENT_NODE) return (true);
			node = node.getNextSibling ();
		}
		return (false);
	}
	
	/**
	 * Returns a set of child <CODE>Element</CODE> instances that have the
	 * given local name string.
	 * 
	 * @param 	context			The context <CODE>Element</CODE> for the operation.
	 * @param 	name			The local name of the requires element.
	 * @return	A possibly empty <CODE>NodeList</CODE> containing each of the
	 * 			matching child elements.
	 * @since	TFP 1.5
	 */
	public static NodeList getElementsByLocalName (Element context, final String name)
	{
		if (context == null) return (MutableNodeList.EMPTY);
		
		MutableNodeList result = new MutableNodeList ();
		
		for (Node node = context.getFirstChild (); node != null;) {
			if (node.getNodeType () == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getLocalName ().equals (name))
					result.add (element);
			}
			node = node.getNextSibling ();
		}
		return (result);
	}
	
	/**
	 * Searches for the first child <CODE>Element</CODE> of the given context
	 * <CODE>Element</CODE> that matchs the required local name.
	 *
	 * @param	context			The context <CODE>Element</CODE> for the operation.
	 * @param	name			The local name of the requires element.
	 * @return	The corresponding child <CODE>Element</CODE> node or <CODE>null
	 * 			</CODE> if no match could be found.
	 * @since	TFP 1.0
	 */
	public static Element getElementByLocalName (Element context, final String name)
	{
		if (context == null) return (null);
		
		for (Node node = context.getFirstChild (); node != null;) {
			if (node.getNodeType () == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getLocalName ().equals (name))
					return (element);
			}
			node = node.getNextSibling ();
		}
		return (null);
	}
	
	/**
	 * Searches for grand-child elements of the given context that would match
	 * an XPath expression of the form <CODE>name1/name2</CODE>.
	 *
	 * @param	context			The context <CODE>Element</CODE> for the operation
	 * @param	name1			The name of the first level element.
	 * @param	name2			The name of the second level element.
	 * @return The corresponding grand-child <CODE>Element</CODE> node or
	 *			<CODE>null</CODE> if no match could be found.
	 * @since	TFP 1.0
	 */
	public static Element getElementByLocalName (Element context, final String name1, final String name2)
	{
		return (
			getElementByLocalName (
				getElementByLocalName (context, name1), name2));
	}

	/**
	 * Searches for great grand-child elements of the given context that would
	 * match an XPath expression of the form <CODE>name1/name2/name3</CODE>.
	 *
	 * @param	context			The context <CODE>Element</CODE> for the operation
	 * @param	name1			The name of the first level element.
	 * @param	name2			The name of the second level element.
	 * @param	name3			The name of the third level element.
	 * @return 	The corresponding grand-child <CODE>Element</CODE> node or
	 *			<CODE>null</CODE> if no match could be found.
	 * @since	TFP 1.0
	 */
	public static Element getElementByLocalName (Element context, final String name1, final String name2, final String name3)
	{
		return (
			getElementByLocalName (
				getElementByLocalName (
					getElementByLocalName (context, name1), name2), name3));
	}

	/**
	 * Locates all the child elements of the given parent <CODE>Element</CODE>
	 * and returns them in a (possibly empty) <CODE>NodeList</CODE>.
	 * 
	 * @param 	parent			The parent <CODE>Element</CODE>.
	 * @return	A possibly empty <CODE>NodeList</CODE> of child elements.
	 */
	public static NodeList getChildElements (Element parent)
	{
		MutableNodeList	list	= new MutableNodeList ();
		
		if (parent != null) {
			for (Node node = parent.getFirstChild (); node != null;) {
				if (node.getNodeType() == Node.ELEMENT_NODE)
					list.add (node);
				
				node = node.getNextSibling ();
			}
		}
		return (list);
	}
	
	/**
	 * Retrieves the value of the first DOM <CODE>Text</CODE> node located 
	 * under the indicated <CODE>Element</CODE>.
	 *
	 * @param	context			The context <CODE>Element</CODE> for the operation.
	 * @return	The value of the <CODE>Text</CODE> node.
	 * @since	TFP 1.0
	 */
	public static String getInnerText (Element context)
	{
		String		text = "";
		
		if (context != null) {
			for (Node node = context.getFirstChild (); node != null;) {
				if (node.getNodeType () == Node.TEXT_NODE) {
					text = ((Text) node).getData ();
					break;
				}
				node = node.getNextSibling ();
			}
		}
		return (text);
	}	
	
	/**
	 * Sets the value of the first DOM <CODE>Text</CODE> node located under
	 * the given context <CODE>Element</CODE> to the given string. Any
	 * existing <CODE>Text</CODE> nodes are removed first.
	 *   
	 * @param 	context		The context <CODE>Element</CODE>
	 * @param 	text		The text value to store.
	 * @since	TFP 1.0
	 */
	public static void setInnerText (Element context, final String text)
	{
		for (Node node = context.getFirstChild (); node != null;) {
			Node next = node.getNextSibling ();
			
			if (node.getNodeType () == Node.TEXT_NODE)
				context.removeChild (node);
			
			node = next;
		}
		
		context.appendChild (context.getOwnerDocument ().createTextNode (text));
	}

	/**
	 * Gets the value of the specified attribute on the indicated context
	 * <CODE>Element</CODE>. Note that this method returns <CODE>null</CODE>
	 * if the attribute is not present compared to the DOM function which
	 * returns a empty string.
	 *  
	 * @param 	context		The context <CODE>Element</CODE>.
	 * @param 	name		The attribute name.
	 * @return	The value of the attribute or <CODE>null</CODE> if it was
	 * 			not present.
	 * @since	TFP 1.8
	 */
	public static String getAttribute (Element context, final String name)
	{
		if (context != null) {
			Attr		attr = context.getAttributeNode (name);
			
			return ((attr != null) ? attr.getValue () : null);
		}
		return (null);
	}
	
	/**
	 * Sets the value of specified attribute on the indicated context
	 * <CODE>Element</CODE>. If the value is <CODE>null</CODE> then the
	 * attribute is removed.
	 * 
	 * @param 	context		The context <CODE>Element</CODE>.
	 * @param 	name		The attribute name.
	 * @param	value		The new value or <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
	public static void setAttribute (Element context, final String name, final String value)
	{
		if (value != null)
			context.setAttribute (name, value);
		else
			context.removeAttribute (name);
	}
	
	/**
	 * Ensures no instances can be constructed. 
	 */
	private DOM ()
	{ }
}