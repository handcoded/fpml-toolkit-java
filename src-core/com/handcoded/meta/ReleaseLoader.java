// Copyright (C),2005-2011 HandCoded Software Ltd.
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

import org.w3c.dom.Element;
import java.util.Hashtable;

/**
 * Instances implementing the <CODE>ReleaseLoader</CODE> are created and used
 * during the processing of the releases meta data file to load the data into
 * an appropriate internal structure.
 * 
 * @author	BitWise
 * @version	$Id: ReleaseLoader.java 698 2012-11-30 18:15:39Z andrew_jacobs $
 * @since	TFP 1.5
 */
public interface ReleaseLoader
{
	/**
	 * Extracts the data from the DOM tree below the indicated context <CODE>Element
	 * </CODE> and create a suitable <CODE>Release</CODE> structure to add to the
	 * indicated <CODE>Specification</CODE>.
	 * 
	 * @param	specification	The owning <CODE>Specification</CODE>.
	 * @param	context			The context <CODE>Element</CODE> containing data.
	 * @param	loadedSchemas	A <CODE>Hashtable</CODE> of all ready loaded schemas.
	 * @since	TFP 1.5
	 */
	public void loadData (Specification specification, Element context,
			Hashtable<String, SchemaRelease> loadedSchemas);
}