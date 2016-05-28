// Copyright (C),2005-2007 HandCoded Software Ltd.
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

/**
 * An instance of the <CODE>InstanceInitialiser</CODE> interface is used to
 * initialise new schema based <CODE>Document</CODE> instances.
 *  
 * @author	BitWise
 * @version	$Id: InstanceInitialiser.java 163 2007-06-19 19:27:41Z andrew_jacobs $
 * @since	TFP 1.1
 */
public interface InstanceInitialiser
{
	/**
	 * Initialises a new <CODE>Document</CODE> by adding required definitions to
	 * the structure indicated by its root <CODE>Element</CODE>.
	 * 
	 * @param 	release			The <CODE>SchemaRelease</CODE> being initialised.
	 * @param 	root			The root <CODE>Element</CODE> of the new document.
	 * @param	isDefaultNamespace <CODE>true</CODE> if the default namespace is being initialised.
	 * @since	TFP 1.1
	 */
	public void initialise (SchemaRelease release, Element root, boolean isDefaultNamespace);
}