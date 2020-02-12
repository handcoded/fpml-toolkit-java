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

package com.handcoded.validation;

import org.w3c.dom.Node;

/**
 * The <CODE>ErrorHandler</CODE> interface provides a standard way to report
 * semantic errors to the application during the validation process.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public interface ValidationErrorHandler
{
	/**
	 * Informs the <CODE>ErrorHandler</CODE> of a validation rule failure and
	 * describes the location and nature of the error to allow correction.
	 * All of the arguments except the reason code are optional and a <CODE>
	 * null</CODE> value may be supplied.
	 *
	 * @param 	code			The FpML defined reason code associated with the error.
	 * @param 	context			The DOM <CODE>Node</CODE> containing the erroneous data.
	 * @param 	description		A textual description of the problem detected.
	 * @param 	ruleName		The code for the FpML validation rule that has failed.
	 * @param 	additionalData 	Any additional data that may assist in problem solving.
	 * @since	TFP 1.0
	 */
	public void error (String code, Node context, String description,
			String ruleName, String additionalData);
}