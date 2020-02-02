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

package com.handcoded.fpml.validation;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.handcoded.xml.MutableNodeList;
import com.handcoded.xml.NodeIndex;

/**
 * An instance of <CODE>ElementContext</CODE> defines the context for a
 * <CODE>SchemeRule</CODE> based on a set of element names optionally qualified
 * by the parent element name.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class ElementContext implements RuleContext
{
	/**
	 * Constructs an <CODE>ElementContext</CODE> given an array of parent
	 * element names (or <CODE>null</CODE>) and an array of element names.
	 * <P>
	 * If both arrays are provided them they must be the same length.
	 * 
	 * @param 	parentNames		An array of parent element names (or <CODE>null</CODE>).
	 * @param 	elementNames	An array of context element names.
	 * @since	TFP 1.6
	 */
	public ElementContext (final String [] parentNames, final String [] elementNames)
	{
		this.parentNames  = parentNames;
		this.elementNames = elementNames;
	}

	/**
	 * Constructs an <CODE>ElementContext</CODE> given an array of element
	 * names.
	 * 
	 * @param 	elementNames	An array of context element names.
	 * @since	TFP 1.6
	 */
	public ElementContext (final String [] elementNames)
	{
		this (null, elementNames);
	}
	
	/**
	 * Constructs an <CODE>ElementContext</CODE> for a given qualified
	 * element name.
	 * 
	 * @param 	parentName		The name of the parent element.
	 * @param 	elementName		The context element name.
	 * @since	TFP 1.6
	 */
	public ElementContext (final String parentName, final String elementName)
	{
		this (new String [] { parentName }, new String [] { elementName });
	}
	
	/**
	 * Constructs an <CODE>ElementContext</CODE> for a given element name.
	 * 
	 * @param 	elementName		The context element name.
	 * @since	TFP 1.6
	 */
	public ElementContext (final String elementName)
	{
		this (null, new String [] { elementName });
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public NodeList getMatchingElements (NodeIndex nodeIndex)
	{
		if (parentNames == null)
			return (nodeIndex.getElementsByName (elementNames));

		MutableNodeList		result = new MutableNodeList ();
		
		for (int index = 0; index < elementNames.length; ++index) {
			NodeList matches = nodeIndex.getElementsByName (elementNames [index]);
			
			if (parentNames [index] == null)
				result.addAll (matches);
			else {
				for (int count = 0, length = matches.getLength (); count < length; ++count) {
					Element	element = (Element) matches.item (count);
					Node	parent	= element.getParentNode ();
					
					if (parent.getNodeType () == Node.ELEMENT_NODE) {
						if (parent.getLocalName ().equals (parentNames [index]))
							result.add (element);
					}
				}
			}
		}
		return (result);
	}
	
	/**
	 * A list of the local parent element names corresponding to the
	 * <CODE>elementNames</CODE>. If the array has a <CODE>null</CODE> value
	 * then no parent element qualification is performed.
	 * @since	TFP 1.6 
	 */
	private final String []	parentNames;
	
	/**
	 * A list of local element names that this rule will validate.
	 * @since	TFP 1.6	
	 */
	private final String []	elementNames;
}