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

package com.handcoded.acme;

import com.handcoded.meta.SchemaRelease;
import com.handcoded.meta.Specification;

/**
 * The <CODE>Releases</CODE> class contains a set of static objects describing
 * the HandCoded Acme extension schemas.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class Releases
{
	/**
	 * A <CODE>Specification</CODE> instance representing Acme extension schemas
	 * as a whole.
	 * @since	TFP 1.0
	 */
	public static Specification	ACME
		= Specification.forName ("Acme");

	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for the
	 * Acme 1-0 schema.
	 * @since	TFP 1.0
	 */
	public static SchemaRelease	R1_0
		= (SchemaRelease) ACME.getReleaseForVersion ("1-0");

	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for the
	 * Acme 2-0 schema.
	 * @since	TFP 1.0
	 */
	public static SchemaRelease	R2_0
		= (SchemaRelease) ACME.getReleaseForVersion ("2-0");

	/**
	 * Ensures no instances can be constructed.
	 * @since	TFP 1.0
	 */
	private Releases ()
	{ }
}