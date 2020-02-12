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
 * An instance implementing the <CODE>SchemaRecognise</CODE> interface is used
 * to compare an XML <CODE>Document</CODE> to a <CODE>SchemaRelease</CODE> to
 * see if it could be an instance of it.
 *   
 * @author	Andrew Jacobs
 * @since	TFP 1.1
 */
public interface SchemaRecogniser
{
	/**
	 * Determines if the XML <CODE>Document</CODE> could be an instance of the
	 * indicated <CODE>SchemaRelease</CODE>.
	 *
	 * @param 	release			The potential <CODE>SchemaRelease</CODE>.
	 * @param 	document		The <CODE>Document</CODE> to be tested.
	 * @return	<CODE>true</CODE> if the <CODE>Document</CODE> could be an
	 * 			instance of the indicated <CODE>SchemaRelease</CODE>.
	 * @since	TFP 1.1
	 */
	public boolean recognises (SchemaRelease release, Document document);
}