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

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <CODE>MutableNodeList</CODE> implements a DOM <CODE>NodeList</CODE> that
 * can have its contents changed.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class MutableNodeList implements NodeList, Cloneable
{
	/**
	 * An empty <CODE>NodeList</CODE>
	 * @since	TFP 1.0
	 */
	public static final NodeList	EMPTY	= new MutableNodeList ();
	
	/**
	 * Constructs an empty <CODE>MutableNodeList</CODE> instance.
	 * @since	TFP 1.0
	 */
	public MutableNodeList ()
	{
		nodes = null;
	}
	
	/**
	 * Constructs a <CODE>MutableNodeList</CODE> instance containing a single
	 * <CODE>Node</CODE>.
	 * 
	 * @param	value		The initial <CODE>Node</CODE> to add to the list.
	 * @since	TFP 1.6
	 */
	public MutableNodeList (Node value)
	{
		this ();
		add (value);
	}
	
	/**
	 * Adds the indicated <CODE>Node</CODE> instance to the current
	 * <CODE>MutableNodeList</CODE>.
	 * 
	 * @param	node			The <CODE>Node</CODE> to be added.
	 * @since	TFP 1.0
	 */
	public void add (Node node)
	{
		if (node != null) {
			if (nodes == null) nodes = new ArrayList<> ();
				
			nodes.add (node);
		}
	}
	
	/**
	 * Adds the contents of the given <CODE>NodeList</CODE> into the current
	 * <CODE>MutableNodeList</CODE>.
	 *
	 * @param	list			The <CODE>NodeList</CODE> to be added.
	 * @since	TFP 1.0
	 */
	public void addAll (NodeList list)
	{
		int				length = list.getLength ();
		
		if (length > 0) {
			if (nodes == null)
				nodes = new ArrayList<> ();	
		
			for (int index = 0; index < length; ++index)
				nodes.add (list.item (index));
		}
	}
	
	/**
	 * Removes the indicated <CODE>Node</CODE> from the collection maintained
	 * within the <CODE>MutableNodeList</CODE>.
	 * 
	 * @param 	node			The <CODE>Node</CODE> to be removed.
	 * @since	TFP 1.0
	 */
	public void remove (Node node)
	{
		if (nodes != null) nodes.remove (node);
	}
	
	/**
	 * Removes each of the <CODE>Node</CODE> instances in the given <CODE>NodeList
	 * </CODE> from the collection maintained within the <CODE>MutableNodeList
	 * </CODE>.
	 * 
	 * @param 	list			The <CODE>NodeList</CODE> of instances to remove.
	 * @since	TFP 1.0
	 */
	public void removeAll (NodeList list)
	{
		for (int index = 0; index < list.getLength (); ++index)
			remove (list.item (index));
	}	
	
	/**
	 * Returns the number of <CODE>Node</CODE> instances contained in the list.
	 * 
	 * @return	The number of <CODE>Node</CODE> instance in the list.
	 * @since	TFP 1.0
	 */
	public int getLength ()
	{
		return ((nodes != null) ? nodes.size () : 0);
	}
	
	/**
	 * Returns the indicated item from underlying list.
	 * 
	 * @param	index			The position of the required item.
	 * @return	The <CODE>Node</CODE> in the indicated position.
	 * @since	TFP 1.0
	 */
	public Node item (int index)
	{
		return (nodes.get (index));
	}
	
	/**
	 * Removes all the <CODE>Node</CODE> instances from the underlying
	 * collections.
	 * 
	 * @since	TFP 1.0
	 */
	public void clear ()
	{
		if (nodes != null)
			nodes.clear ();
	}
	
	/**
	 * Returns a clone of this <CODE>MutableNodeList</CODE>.
	 * 
	 * @return	A clone of this <CODE>MutableNodeList</CODE>.
	 * @since	TFP 1.0
	 */
	@Override
	public Object clone ()
	{
		if (nodes != null)
			return (new MutableNodeList (new ArrayList<Node> (nodes)));
		else
			return (new MutableNodeList ());
	}
	
	/**
	 * Converts the <CODE>MutableNodeList</CODE> to a string for debugging.
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
	 * Constructs a <CODE>MutableNodeList</CODE> which will use the given
	 * <CODE>ArrayList</CODE> for storage.
	 * 
	 * @param 	nodes			The <CODE>ArrayList</CODE> to use for storage.
	 * @since	TFP 1.0				
	 */
	protected MutableNodeList (ArrayList<Node> nodes)
	{
		this.nodes = nodes;
	}
	
	/**
	 * Produces a debugging string describing the state of the instance.
	 *
	 * @return 	The debugging string.
	 * @since	TFP 1.0
	 */
	protected String toDebug ()
	{
		StringBuilder		buffer = new StringBuilder ();
		
		buffer.append ("nodes={");
		
		for (int index = 0; index < nodes.size (); ++index) {
			if (index != 0) buffer.append (',');
			buffer.append (nodes.get (index));
		}
		buffer.append ("}");
		
		return (buffer.toString ());
	}

	/**
	 * The underlying storage for the DOM <CODE>Node</CODE> instances. The
	 * <CODE>ArrayList</CODE> is created when the first <CODE>Node</CODE> is
	 * added.
	 * @since	TFP 1.0
	 */
	private ArrayList<Node>	nodes;
}