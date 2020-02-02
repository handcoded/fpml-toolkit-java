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

import org.w3c.dom.NodeList;

import com.handcoded.xml.NodeIndex;

/**
 * An instance of <CODE>TypeContext</CODE> defines the context for a
 * <CODE>SchemeRule</CODE> based on an XML Schema type name.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class TypeContext implements RuleContext
{
	/**
	 * Constructs a <CODE>TypeContext</CODE> for a given type name.
	 * 
	 * @param 	typeName		The context type name.
	 * @since	TFP 1.6
	 */
	public TypeContext (final String typeName)
	{
		this.typeName = typeName;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public NodeList getMatchingElements (NodeIndex nodeIndex)
	{
		return (nodeIndex.getElementsByType (FpMLRuleSet.determineNamespace (nodeIndex), typeName));
	}
	
	/**
	 * The name of the type to find instances of.
	 * @since	TFP 1.6
	 */
	private final String 	typeName;
}