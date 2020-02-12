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

import com.handcoded.fpml.schemes.SchemeCollection;

/**
 * The <CODE>SchemeAccess</CODE> interface provides access to an instances
 * <CODE>SchemeDefaults</CODE> meta-description and <CODE>SchemeCollection</CODE>
 * instance.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
public interface SchemeAccess
{
	/**
	 * Provides access to a meta-description of scheme defaults values.
	 * 
	 * @return	A <CODE>SchemeDefaults</CODE> instance.
	 */
	public SchemeDefaults getSchemeDefaults ();
	
	/**
	 * Provides access to the scheme collection for the release.
	 * 
	 * @return A <CODE>SchemeCollection</CODE> instance.
	 */
	public SchemeCollection getSchemeCollection ();
}