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

package com.handcoded.dsig;

import com.handcoded.meta.SchemaRelease;
import com.handcoded.meta.Specification;

/**
 * The <CODE>Releases</CODE> class contains a set of static objects describing
 * the W3C Digital Signature specification.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class Releases
{
	/**
	 * A <CODE>Specification</CODE> instance representing Digital Signatures
	 * as a whole.
	 * @since	TFP 1.0
	 */
	public static Specification	DSIG
		= Specification.forName ("DSig");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * DSIG recommendation.
	 * @since	TFP 1.0
	 */
	public static SchemaRelease	R1_0
		= (SchemaRelease) DSIG.getReleaseForVersion ("1-0");
	
	/**
	 * Ensures no instances can be constructed.
	 * @since	TFP 1.0
	 */
	private Releases ()
	{ }
}