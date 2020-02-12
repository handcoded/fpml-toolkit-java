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

package com.handcoded.fpml.meta;

import org.w3c.dom.Element;

import com.handcoded.meta.DefaultInstanceInitialiser;
import com.handcoded.meta.SchemaRelease;

/**
 * The <CODE>FpMLInstanceInitialiser</CODE> class provides additional
 * initialisation logic for FpML schema based instances, in particular
 * it ensures that the FpML version attribute is set to match the
 * referenced schema.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.1
 */
public final class FpMLInstanceInitialiser extends DefaultInstanceInitialiser
{
	/**
	 * Constructs a <CODE>FpMLInstanceInitialiser</CODE> that performs
	 * additional initialisation for FpML documents.
	 * @since	TFP 1.1
	 */
	public FpMLInstanceInitialiser ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * <P>
	 * This derived implementation also sets the FpML version number attribute.
	 * 
	 * @since	TFP 1.1
	 */
	public void initialise (SchemaRelease release, Element root, boolean isDefaultNamespace)
	{
		super.initialise (release, root, isDefaultNamespace);

		int majorVersion = Integer.parseInt (release.getVersion().split("-")[0]);
		
		if (majorVersion <= 4)
			root.setAttribute ("version", release.getVersion ());
		else
			root.setAttribute("fpmlVersion", release.getVersion ());
	}
}