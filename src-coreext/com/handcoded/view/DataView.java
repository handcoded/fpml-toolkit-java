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

package com.handcoded.view;

import org.w3c.dom.Element;

/**
 * A <CODE>DataView</CODE> instance represents the root of a set of views
 * that extract data from an XML document that has been parsed into a DOM
 * tree.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.9
 */
public class DataView extends View
{
	/**
	 * Constructs a <CODE>DataView</CODE> instance.
	 * @since	TFP 1.9
	 */
	public DataView ()
	{
		super (null);
	}
	
	/**
	 * Applies the data extraction criteria defined in the <CODE>View</CODE> to
	 * the indicated XML <CODE>Element</CODE> and populates a <CODE>ResultSet</CODE>
	 * with the results.
	 *  
	 * @param	root		The root <CODE>Element</CODE> of the XML.
	 * @return	A <CODE>ValueSet</CODE> with the resulting <CODE>Facet</CODE>
	 * 			values.
	 * @since	TFP 1.9
	 */
	public ValueSet applyTo (Element root)
	{
		return (applyTo (root, new VariableSet (), new ValueSet ()));
	}
}