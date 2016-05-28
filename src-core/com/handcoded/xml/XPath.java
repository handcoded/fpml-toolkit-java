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

package com.handcoded.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides utility functions for creating XPath location  strings and
 * applying simple XPath like operations to a DOM tree.
 *
 * @author	BitWise
 * @version	$Id: XPath.java 659 2012-08-20 16:33:32Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class XPath
{
	/**
	 * Recursively determines the XPath expression for a given <CODE>Node</CODE>
	 * in a DOM tree.
	 *
	 * @param	node		The DOM <CODE>Node</CODE> to be described.
	 * @return	A string in XPath format describing the <CODE>Node</CODE>.
	 * @since	TFP 1.0
	 */
	public static String forNode (Node node)
	{
		return (forNode (node, null));
	}
	
	/**
	 * Recursively determines the XPath expression for a given <CODE>Node</CODE>
	 * in a DOM tree until the indicated root point is reached. If the root
	 * point is <CODE>null</CODE> the algorithm recurses to the top of the
	 * DOM tree.
	 *
	 * @param	node		The DOM <CODE>Node</CODE> to be described.
	 * @param	root		The root <CODE>Node</CODE> or <CODE>null</CODE>.
	 * @return	A string in XPath format describing the <CODE>Node</CODE>.
	 * @since	TFP 1.1
	 */
	public static String forNode (Node node, Node root)
	{
		if (node != root) {	
			switch (node.getNodeType ()) {
			case Node.ATTRIBUTE_NODE:
				return (forNode (((Attr) node).getOwnerElement ()) + "/@" + node.getLocalName ());
				
			case Node.ELEMENT_NODE:
				{
					int		succ = 0;
					int		pred = 0;
				
					for (Element temp = DOM.getPreviousSibling ((Element) node); temp != null;) {
						if (!node.getLocalName ().equals (temp.getLocalName ()))
							break;
							
						temp = DOM.getPreviousSibling (temp);
						++succ;
					}
					
					for (Element temp = DOM.getNextSibling ((Element) node); temp != null;) {
						if (!node.getLocalName ().equals (temp.getLocalName ()))
							break;
							
						temp = DOM.getNextSibling (temp);
						++pred;
					}
					
					if ((succ + pred) > 0)
						return (forNode (node.getParentNode ())
							+ "/" + node.getLocalName () + "[" + (succ + 1) + "]");
					else
						return (forNode (node.getParentNode ())
							+ "/" + node.getLocalName ());
				}
			}
		}
		return ("");	
	}	

	// --------------------------------------------------------------------

	/**
	 * Determines the common ancestor of two <CODE>Element</CODE> instances.
	 *
	 * @param 	elementA		The first element.
	 * @param 	elementB		The second element.
	 * @return	The common ancestor <CODE>Node</CODE> or <CODE>>null</CODE> if the
	 *			two <CODE>Element</CODE> arguments are in different documents.
	 * @since	TFP 1.0
	 */
	public static Node commonAncestor (final Element elementA, final Element elementB)
	{
		if ((elementA != null) && (elementB != null)) {
			if (elementA.getOwnerDocument () == elementB.getOwnerDocument ()) {
				for (Node nodeA = elementA; nodeA != null; nodeA = nodeA.getParentNode ())
					for (Node nodeB = elementB; nodeB != null; nodeB = nodeB.getParentNode ())
						if (nodeA == nodeB) return (nodeA);
			}
		}
		return (null);
	}

	// --------------------------------------------------------------------

	/**
	 * Evaluates a simple single valued path access from the given context
	 * node to the named child element. The '.' and '..' specifiers are
	 * supported.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name			The name of the required child.
	 * @return	The child <CODE>Element</CODE> or <CODE>null</CODE> if no
	 *			matching element exists.
	 * @since	TFP 1.0
	 */
	public static Element path (final Element context, final String name)
	{
		if (context != null) {
			if (name.equals ("."))
				return (context);
			
			if (name.equals (".."))
				return ((Element) context.getParentNode ());
		
			return (DOM.getElementByLocalName (context, name));
		}
		return (null);
	}

	/**
	 * Evaluates a simple single valued path access from the given context
	 * node to the named grandchild element.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @return	The child <CODE>Element</CODE> or <CODE>null</CODE> if no
	 * 			matching element exists.
	 * @since	TFP 1.0
	 */
	public static Element path (final Element context, final String name1, final String name2)
	{
		return (path (path (context, name1), name2));
	}

	/**
	 * Evaluates a simple single valued path access from the given context
	 * node to the named great-grandchild element.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @return	The child <CODE>Element</CODE> or <CODE>null</CODE> if no
	 *			matching element exists.
	 * @since	TFP 1.0
	 */
	public static Element path (final Element context, final String name1, final String name2, final String name3)
	{
		return (path (path (path (context, name1), name2), name3));
	}

	/**
	 * Evaluates a simple single valued path access from the given context
	 * node to the named great-great-grandchild element.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param 	name4			The name of the required great-great-grandchild.
	 * @return	The child <CODE>Element</CODE> or <CODE>null</CODE> if no
	 * 			matching element exists.
	 * @since	TFP 1.0
	 */
	public static Element path (final Element context, final String name1, final String name2, final String name3, final String name4)
	{
		return (path (path (path (path (context, name1), name2), name3), name4));
	}

	/**
	 * Evaluates a simple single valued path access from the given context
	 * node to the named great-great-great-grandchild element.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param 	name4			The name of the required great-great-grandchild.
	 * @param 	name5			The name of the required great-great-great-grandchild.
	 * @return	The child <CODE>Element</CODE> or <CODE>null</CODE> if no
	 * 			matching element exists.
	 * @since	TFP 1.0
	 */
	public static Element path (final Element context, final String name1, final String name2, final String name3, final String name4, final String name5)
	{
		return (path (path (path (path (path (context, name1), name2), name3), name4), name5));
	}

	/**
	 * Evaluates a simple single valued path access from the given context
	 * node to the named great-great-great-grandchild element.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param 	name4			The name of the required great-great-grandchild.
	 * @param 	name5			The name of the required great-great-great-grandchild.
	 * @param 	name6			The name of the required great-great-great-great-grandchild.
	 * @return	The child <CODE>Element</CODE> or <CODE>null</CODE> if no
	 * 			matching element exists.
	 * @since	TFP 1.0
	 */
	public static Element path (final Element context, final String name1, final String name2, final String name3, final String name4, final String name5, final String name6)
	{
		return (path (path (path (path (path (path (context, name1), name2), name3), name4), name5), name6));
	}

	/**
	 * Evaluates a simple single valued path access from the given context
	 * node to the named great-great-great-grandchild element.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param 	name4			The name of the required great-great-grandchild.
	 * @param 	name5			The name of the required great-great-great-grandchild.
	 * @param 	name6			The name of the required great-great-great-great-grandchild.
	 * @param 	name7			The name of the required great-great-great-great-great-grandchild.
	 * @return	The child <CODE>Element</CODE> or <CODE>null</CODE> if no
	 * 			matching element exists.
	 * @since	TFP 1.0
	 */
	public static Element path (final Element context, final String name1, final String name2, final String name3, final String name4, final String name5, final String name6, final String name7)
	{
		return (path (path (path (path (path (path (path (context, name1), name2), name3), name4), name5), name6), name7));
	}

	/**
	 * Evaluates a simple single valued path access from the given context
	 * node to the named great-great-great-grandchild element.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param 	name4			The name of the required great-great-grandchild.
	 * @param 	name5			The name of the required great-great-great-grandchild.
	 * @param 	name6			The name of the required great-great-great-great-grandchild.
	 * @param 	name7			The name of the required great-great-great-great-great-grandchild.
	 * @param 	name8			The name of the required great-great-great-great-great-great-grandchild.
	 * @return	The child <CODE>Element</CODE> or <CODE>null</CODE> if no
	 * 			matching element exists.
	 * @since	TFP 1.0
	 */
	public static Element path (final Element context, final String name1, final String name2, final String name3, final String name4, final String name5, final String name6, final String name7, final String name8)
	{
		return (path (path (path (path (path (path (path (path (context, name1), name2), name3), name4), name5), name6), name7), name8));
	}

	//---------------------------------------------------------------------------

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named child elements. The '*', '.' and '..' specifiers are
	 * supported.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name			The name of the required child.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 * 			child elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final Element context, final String name)
	{
		if (context != null) {
			if (name.equals ("*"))
				return (DOM.getChildElements (context));
			
			if (name.equals (".")) {
				MutableNodeList	list = new MutableNodeList ();

				list.add (context);
				return (list);
			}
			
			if (name.equals ("..")) {
				if (context.getParentNode () != null) {
					MutableNodeList	list = new MutableNodeList ();
	
					list.add (context.getParentNode ());
					return (list);
				}
				else
					return (MutableNodeList.EMPTY);
			}
		
			return (DOM.getElementsByLocalName (context, name));
		}
		return (MutableNodeList.EMPTY);
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named child elements.
	 *
	 * @param 	contexts		The context <CODE>Element</CODE>.
	 * @param 	name			The name of the required child.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 * 			child elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final NodeList contexts, final String name)
	{
		MutableNodeList		list = new MutableNodeList ();

		for (int index = 0; index < contexts.getLength (); ++index)
			list.addAll (paths ((Element) contexts.item (index), name));

		return (list);
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named grandchild elements.
	 *
	 * @param 	contexts		The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 * 			child elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final NodeList contexts, final String name1, final String name2)
	{
		MutableNodeList		list = new MutableNodeList ();

		for (int index = 0; index < contexts.getLength (); ++index)
			list.addAll (paths (paths ((Element) contexts.item (index), name1), name2));

		return (list);
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named grandchild elements.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 * 			grandchild elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final Element context, final String name1, final String name2)
	{
		return (paths (paths (context, name1), name2));
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named great-grandchild elements.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 * 			great-grandchild elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final Element context, final String name1, final String name2, final String name3)
	{
		return (paths (paths (paths (context, name1), name2), name3));
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named great-great-grandchild elements.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param 	name4			The name of the required great-great-grandchild.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 * 			great-great-grandchild elements
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final Element context, final String name1, final String name2, final String name3, final String name4)
	{
		return (paths (paths (paths (paths (context, name1), name2), name3), name4));
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named great-great-great-grandchild elements.
	 *
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @param 	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param 	name4			The name of the required great-great-grandchild.
	 * @param 	name5			The name of the required great-great-great-grandchild.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 * 			great-great-great-grandchild elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final Element context, final String name1, final String name2, final String name3, final String name4, final String name5)
	{
		return (paths (paths (paths (paths (paths (context, name1), name2), name3), name4), name5));
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named great-great-great-great-grandchild elements.
	 *
	 * @param	context			The context <CODE>Element</CODE>.
	 * @param	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param	name4			The name of the required great-great-grandchild.
	 * @param	name5			The name of the required great-great-great-grandchild.
	 * @param	name6			The name of the required great-great-great-great-grandchild.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 *			great-great-great-great-grandchild elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final Element context, final String name1, final String name2, final String name3, final String name4, final String name5, final String name6)
	{
		return (paths (paths (paths (paths (paths (paths (context, name1), name2), name3), name4), name5), name6));
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named great-great-great-great-grandchild elements.
	 *
	 * @param	context			The context <CODE>Element</CODE>.
	 * @param	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param	name4			The name of the required great-great-grandchild.
	 * @param	name5			The name of the required great-great-great-grandchild.
	 * @param	name6			The name of the required great-great-great-great-grandchild.
	 * @param	name7			The name of the required great-great-great-great-great-grandchild.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 *			great-great-great-great-great-grandchild elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final Element context, final String name1, final String name2, final String name3, final String name4, final String name5, final String name6, final String name7)
	{
		return (paths (paths (paths (paths (paths (paths (paths (context, name1), name2), name3), name4), name5), name6), name7));
	}

	/**
	 * Evaluates a simple multiple valued path access from the given context
	 * node to the named great-great-great-great-grandchild elements.
	 *
	 * @param	context			The context <CODE>Element</CODE>.
	 * @param	name1			The name of the required child.
	 * @param 	name2			The name of the required grandchild.
	 * @param 	name3			The name of the required great-grandchild.
	 * @param	name4			The name of the required great-great-grandchild.
	 * @param	name5			The name of the required great-great-great-grandchild.
	 * @param	name6			The name of the required great-great-great-great-grandchild.
	 * @param	name7			The name of the required great-great-great-great-great-grandchild.
	 * @param	name8			The name of the required great-great-great-great-great-great-grandchild.
	 * @return	A possibly empty <CODE>NodeList</CODE> of matching
	 *			great-great-great-great-great-great-grandchild elements.
	 * @since	TFP 1.0
	 */
	public static NodeList paths (final Element context, final String name1, final String name2, final String name3, final String name4, final String name5, final String name6, final String name7, final String name8)
	{
		return (paths (paths (paths (paths (paths (paths (paths (paths (context, name1), name2), name3), name4), name5), name6), name7), name8));
	}
	
	//---------------------------------------------------------------------------
	
	/**
	 * Determines of the context <CODE>Attr</CODE> matches the indicated
     * named attribute.
     * 
     * @param	context		The context <CODE>Attr</CODE>.
     * @param	attrName	The target attribute name.
     * @return	<CODE>true</CODE> if the match is successfully made.
	 * @since	TFP 1.6
	 */
    public static boolean match (Attr context, String attrName)
    {
        return (context.getLocalName ().equals (attrName));
    }

    /**
     * Determines of the context <CODE>Attr</CODE> matches the indicated
     * named attribute and parent element name.
     * 
     * @param	context		The context <CODE>Attr</CODE>.
     * @param	name1		The name of the parent element.
     * @param	attrName	The target attribute name.
     * @return	<CODE>true</CODE> if the match is successfully made.
	 * @since	TFP 1.6
     */
    public static boolean match (Attr context, String name1, String attrName)
    {
        return (match (context, attrName) && match (context.getOwnerElement (), name1));
    }

    /**
     * Determines of the context <CODE>Attribute</CODE> matches the indicated
     * named attribute and ancestor element names.
     * 
     * @param	context		The context <CODE>Attr</CODE>.
     * @param	name1		The name of the grand-parent element.
     * @param	name2		The name of the parent element.
     * @param	attrName	The target attribute name.
     * @return	<CODE>true</CODE> if the match is successfully made.
	 * @since	TFP 1.6
     */
    public static boolean match (Attr context, String name1, String name2, String attrName)
    {
        return (match (context, attrName) && match (context.getOwnerElement (), name1, name2));
    }

    /**
     * Determines of the context <CODE>Element</CODE> matches the indicated
     * element name.
     * 
     * @param	context		The context <CODE>Element</CODE>.
     * @param	name1		The target element name.
     * @return	<CODE>true</CODE> if the match is successfully made.
	 * @since	TFP 1.6
     */
    public static boolean match (Element context, String name1)
    {
	    return (context.getLocalName ().equals (name1));
    }

    /**
     * Determines of the context <CODE>Element</CODE> matches the indicated
     * element and parent.
     * 
     * @param	context		The context <CODE>Element</CODE>.
     * @param	name1		The parent element name.
     * @param	name2		The target element name.
     * @return	<CODE>true</CODE> if the match is successfully made.
	 * @since	TFP 1.6
     */
    public static boolean match (Element context, String name1, String name2)
    {
	    return (match (context, name2) && match ((Element) context.getParentNode (), name1));
    }

	//---------------------------------------------------------------------------
	
	/**
	 * Ensures no instances can be constructed.
	 * @since	TFP 1.0
	 */
	private XPath ()
	{ }
}