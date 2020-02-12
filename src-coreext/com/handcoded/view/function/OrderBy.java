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

package com.handcoded.view.function;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.handcoded.xml.MutableNodeList;
import com.handcoded.xml.Types;

/**
 * The <CODE>OrderBy</CODE> XPath extension function orders a set of nodes
 * indicated by the first argument using set of values indicated by the second
 * (e.g. order-by (nodes, values)) for example to order FX legs by currency.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.9
 */
public final class OrderBy implements XPathFunction
{
	/**
	 * Constructions an <CODE>OrderBy</CODE> XPath function instance.
	 * @since	TFP 1.9
	 */
	public OrderBy ()
	{ }

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@SuppressWarnings("rawtypes")
	public Object evaluate(List args) throws XPathFunctionException
	{
		// Check parameters
		if (args.size () != 2)
			throw new XPathFunctionException ("The OrderBy function requires two arguments");
		
		if (!(args.get (0) instanceof NodeList))
			throw new XPathFunctionException ("The first argument to OrderBy MUST be a NodeList");
		if (!(args.get (1) instanceof NodeList))
			throw new XPathFunctionException ("The second argument to OrderBy MUST be a NodeList");

		ArrayList<Node> nodes  = expand ((NodeList) args.get (0));
		ArrayList<Node> values = expand ((NodeList) args.get (1));
		
		if (nodes.size() != values.size ())
			throw new XPathFunctionException ("The nodes and values arguments MUST contain the same number of Nodes");
		
		// Sort the nodes
		boolean		sorted;
		do {
			sorted = true;
			
			for (int index = 0; index < (nodes.size () - 1); index++) {
				String lhs = Types.toString (values.get (index));
				String rhs = Types.toString (values.get (index + 1));
				
				if (lhs.compareTo (rhs) > 0) {
					Node 		temp;
					
					temp = nodes.get (index);
					nodes.set(index, nodes.get(index + 1));
					nodes.set(index + 1,  temp);
					
					temp = values.get (index);
					values.set(index, values.get(index + 1));
					values.set(index + 1,  temp);
					
					sorted = false;
				}
			}
		} while (!sorted);
		
		// Build the result
		MutableNodeList	list = new MutableNodeList ();
		for (int index = 0; index < nodes.size(); ++index)
			list.add(nodes.get (index));
		
		return (list);
	}
	
	/**
	 * Expands a <CODE>NodeList</CODE> into an <CODE>ArrayList</CODE> of
	 * <CODE>Node</CODE> instances.
	 * 
	 * @param	list		The <CODE>NodeList</CODE> to expand
	 * @return	The resulting <CODE>ArrayList</CODE>.
	 * @since	TFP 1.9
	 */
	private ArrayList<Node> expand (NodeList list)
	{
		ArrayList<Node> nodes = new ArrayList<> ();
		
		for (int index = 0; index < list.getLength (); ++index)
			nodes.add (list.item (index));
		
		return (nodes);
	}
}