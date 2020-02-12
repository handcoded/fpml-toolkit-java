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

/**
 * The <CODE>DTD</CODE> interface provides access to information that describes
 * an XML DTD based grammar.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
public interface DTD extends Grammar
{
	/**
	 * Returns the public identifier associated with this <CODE>DTD</CODE>
	 * instance.
	 * 
	 * @return 	The public identifier string.
	 * @since	TFP 1.0
	 */
	public String getPublicId ();
	
	/**
	 * Returns the system identifier associated with this <CODE>DTD</CODE>
	 * instance.
	 * 
	 * @return 	The system identifier string.
	 * @since	TFP 1.0
	 */
	public String getSystemId ();
}