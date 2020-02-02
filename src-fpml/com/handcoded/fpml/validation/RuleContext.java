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
 * The <CODE>RuleContext</CODE> interface defines a standard API for locating
 * the document <CODE>Element</CODE> instances that are contexts for a FpML
 * rule.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.6
 */
public interface RuleContext
{
	/**
	 * Returns a <CODE>NodeList</CODE> containing all the elements defined in
	 * the <CODE>NodeIndex</CODE> that match the context specification.
	 * 
	 * @param 	nodeIndex		A <CODE>NodeIndex</CODE> for the test document.
	 * @return	A <CODE>NodeList</CODE> containing all the matching <CODE>Element
	 * 			</CODE> instances, if any.
	 * @since	TFP 1.6
	 */
	public NodeList getMatchingElements (NodeIndex nodeIndex);
}