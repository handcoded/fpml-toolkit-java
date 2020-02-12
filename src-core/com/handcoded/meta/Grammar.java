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

package com.handcoded.meta;

import org.w3c.dom.Document;

/**
 * The <CODE>Grammar</CODE> interface defines methods common to both the derived
 * <CODE>DTD</CODE> and <CODE>Schema</CODE> interfaces.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
public interface Grammar
{
	/**
	 * Returns a list of possible root element names for this <CODE>Grammar</CODE>.
	 * 
	 * @return	The root element names.
	 * @since	TFP 1.0
	 */
	public String [] getRootElements ();
	
	/**
	 * Indicates that the schema is a pure extension (e.g. has no root elements).
	 *
	 * @return	<CODE>true</CODE> if the schema is an extension.
	 * @since	TFP 1.0
	 */
	public boolean isExtensionOnly ();
	
	/**
	 * Determines if the provided <CODE>Document</CODE> instance uses this
	 * <CODE>Grammar</CODE>.
	 * 
	 * @param 	document			The <CODE>Document</CODE> to be examined.
	 * @return	<CODE>true</CODE> if the <CODE>Document</CODE> uses this
	 * 			<CODE>Grammar</CODE>, <CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public boolean isInstance (Document document);
	
	/**
	 * Creates a new instance the XML grammar represented by this instance
	 * using the indicated element name as the root element for the document.
	 * 
	 * @param	rootElement			The name of the root element.
	 * @return	A new <CODE>Document</CODE> instance.
	 * @since	TFP 1.0
	 */
	public Document newInstance (final String rootElement);
	
	/**
	 * Create a new <CODE>Document</CODE> instance which will hold a fragment
	 * of a document based on the grammar represented by this release.
	 * 
	 * @param 	rootElement			The name of the root element.
	 * @return	A new <CODE>Document</CODE> instance.
	 * @since	TFP 1.4
	 */
	public Document newFragment (final String rootElement);
}