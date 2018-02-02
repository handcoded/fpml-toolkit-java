// Copyright (C),2005-2017 HandCoded Software Ltd.
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

package com.handcoded.view;

/**
 * A <CODE>FacetSet</CODE> is a view containing namespaces, variables and
 * facets which is conditionally evaluated.
 * 
 * @author	BitWise
 * @since	TFP 1.9
 */
public final class FacetSet extends View
{
	/**
	 * Constructs a <CODE>FacetSet</CODE> instance with the indicated parent
	 * <CODE>View</CODE> and conditional text string.
	 * 
	 * @param	parent		The parent <CODE>View</CODE>
	 * @param	test		An XPath expression string.
	 * @since	TFP 1.9	
	 */
	public FacetSet (View parent, String test)
	{
		super (parent);
		
		this.test = test;
	}
	
	/**
	 * Provides access to the XPath used to determine if the <CODE>FacetSet</CODE<
	 * will be evaluated.
	 * 
	 * @return	The XPath expression string.
	 * @since	TFP 1.9
	 */
	public String getTest ()
	{
		return (test);
	}

	/**
	 * The XPath expression defined applicability.
	 * @since	TFP 1.9
	 */
	private final String	test;
}