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

package com.handcoded.fpml.validation;

import com.handcoded.validation.RuleSet;

/**
 * The <CODE>AllRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with every FpML defined validation rule.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class FpMLRules
{
	/**
	 * Provides access to the official FpML validation rule set.
	 * 
	 * @return	The data type validation rule set.
	 * @since	TFP 1.0
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}
	
	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules.
	 */
	private static final RuleSet	rules = RuleSet.forName ("FpMLRules");
	
	/**
	 * Ensures no instances can be created.
	 */
	private FpMLRules ()
	{ }
}