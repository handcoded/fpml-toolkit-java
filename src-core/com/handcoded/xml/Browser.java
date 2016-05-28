// Copyright (C),2005-2006 HandCoded Software Ltd.
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The <CODE>Browser</CODE> class tracks a position of interest within a
 * DOM <CODE>Document</CODE> which can be moved around as the document is
 * explored. During traversal the <CODE>Browser</CODE> automatically skips
 * over any non-element nodes in the DOM tree.
 * 
 * @author	BitWise
 * @version	$Id: Browser.java 44 2006-10-02 10:20:59Z andrew_jacobs $
 * @since	TFP 1.0
 */
public class Browser
{
	/**
	 * Constructs a <CODE>Browser</CODE> that is attached to the root
	 * element of the given <CODE>Document</CODE>.
	 * 
	 * @param	document		The <CODE>Document</CODE> to explore.
	 * @since	TFP 1.0
	 */
	public Browser (Document document)
	{
		this (document, document.getDocumentElement ());
	}
	
	/**
	 * Constructs a <CODE>Browser</CODE> that is attached to a specific
	 * initial context <CODE>Element</CODE>.
	 * 
	 * @param 	document		The <CODE>Document</CODE> to explore.
	 * @param 	context			The intial context <CODE>Element</CODE>.
	 * @throws	IllegalArgumentException If the context <CODE>Element</CODE>
	 * 			does not belong to the <CODE>Document</CODE>.
	 * @since	TFP 1.0
	 */
	public Browser (Document document, Element context)
	{
		if (context.getOwnerDocument () != document)
			throw new IllegalArgumentException ("Invalid context element");
		
		this.document = document;
		this.context  = context;
	}
	
	/**
	 * Provides access to the underlying <CODE>Document</CODE> instance.
	 * 
	 * @return	The underlying <CODE>Document</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final Document getDocument ()
	{
		return (document);
	}
	
	/**
	 * Provides access to the current context <CODE>Element</CODE>.
	 * 
	 * @return	The current context <CODE>Element</CODE>.
	 * @since	TFP 1.0
	 */
	public final Element getContext ()
	{
		return (context);
	}
	
	/**
	 * Moves the context to the first child <CODE>Element</CODE> if one
	 * exists.
	 * 
	 * @return	<CODE>true</CODE> if the context <CODE>Element</CODE> was
	 * 			changed, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public final boolean moveToFirstChild ()
	{
		Element		child = DOM.getFirstChild (context);

		if (child != null) {
			context = child;
			return (true);
		}
		return (false);
	}

	/**
	 * Moves the context to the last child <CODE>Element</CODE> if one
	 * exists.
	 * 
	 * @return	<CODE>true</CODE> if the context <CODE>Element</CODE> was
	 * 			changed, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public final boolean moveToLastChild ()
	{
		Element		child = DOM.getLastChild (context);

		if (child != null) {
			context = child;
			return (true);
		}
		return (false);
	}

	/**
	 * Moves the context to the next sibling <CODE>Element</CODE> if one
	 * exists.
	 * 
	 * @return	<CODE>true</CODE> if the context <CODE>Element</CODE> was
	 * 			changed, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public final boolean moveToNextSibling ()
	{
		Element		element = DOM.getNextSibling (context);

		if (element != null) {
			context = element;
			return (true);
		}
		return (false);
	}

	/**
	 * Moves the context to the previous sibling <CODE>Element</CODE> if one
	 * exists.
	 * 
	 * @return	<CODE>true</CODE> if the context <CODE>Element</CODE> was
	 * 			changed, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public final boolean moveToPreviousSibling ()
	{
		Element		element = DOM.getPreviousSibling (context);

		if (element != null) {
			context = element;
			return (true);
		}
		return (false);
	}

	/**
	 * Moves the context to the parent <CODE>Element</CODE>.
	 * 
	 * @return	<CODE>true</CODE> if the context <CODE>Element</CODE> was
	 * 			changed, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public final boolean moveToParent ()
	{
		Element		parent = DOM.getParent (context);

		if (parent != null){
			context = parent;
			return (true);
		}
		return (false);
	}
	
	/**
	 * Determines if the context <CODE>Element</CODE> has at least one child
	 * <CODE>Element</CODE>.
	 * 
	 * @return	<CODE>true</CODE> if the context <CODE>Element</CODE> has at
	 * 			least one <CODE>Element</CODE>, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public final boolean hasChildNodes ()
	{
		return (DOM.hasChildNodes (context));
	}
	
	/**
	 * Converts the <CODE>Browser</CODE> to a string for debugging.
	 *
	 * @return 	A text description of the instance.
	 * @since	TFP 1.0
	 */
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
		return ("document=" + document + " context=" + context);
	}
	
	/**
	 * The <CODE>Document</CODE> that is being constructed.
	 * @since	TFP 1.0
	 */
	protected Document		document;
	
	/**
	 * The <CODE>Element</CODE> that is the context for the next operation.
	 * @since	TFP 1.0
	 */
	protected Element			context;
}