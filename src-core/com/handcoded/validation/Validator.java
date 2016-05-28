// Copyright (C),2005-2006 HandCoded Software Ltd.
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

import org.w3c.dom.Document;

import com.handcoded.xml.NodeIndex;

/**
 * The <CODE>Validator</CODE> class defines a standard API for requesting
 * the validation of the business data content of an XML instance document.
 *
 * @author	BitWise
 * @version	$Id: Validator.java 283 2008-04-04 20:24:54Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class Validator
{
	/**
	 * Determines if the given DOM <CODE>Document</CODE> instance's business
	 * data content passes a validation test. If errors are detected these will
	 * be reported through the <CODE>ErrorHandler</CODE> instance passed as an
	 * argument.
	 * <P>
	 * Note that test returns <CODE>true</CODE> if it does not fail, including
	 * circumstances when the test is inapplicable to the <CODE>Document</CODE>
	 * under examination.
	 *
	 * @param 	document		The DOM <CODE>Document</CODE> instance to examine.
	 * @param 	errorHandler	An <CODE>ErrorHandler</CODE> instance used to report
	 *							validation failures.
	 * @return 	<CODE>false</CODE> if the validation test failed, <CODE>true
	 *			</CODE> otherwise. 
	 * @since	TFP 1.0
	 */
	public boolean validate (Document document, ValidationErrorHandler errorHandler)
	{
		return (validate (new NodeIndex (document), errorHandler));
	}
	
	/**
	 * Constructs a <CODE>Validator</CODE> instance.
	 * 
	 * @since	TFP 1.0
	 */
	protected Validator ()
	{ }
	
	/**
	 * Determines if the DOM <CODE>Document</CODE> instance indexed by the
	 * provided <CODE>NodeIndex</CODE> has business data content that passes a
	 * validation test. If errors are detected they will be reported through
	 * the <CODE>ErrorHandler</CODE> instance passed as an argument.
	 * <P>
	 * Note that test returns <CODE>true</CODE> if it does not fail, including
	 * circumstances when the test is inapplicable to the <CODE>Document</CODE>
	 * under examination.
	 *
	 * @param 	nodeIndex		The <CODE>NodeIndex</CODE> instance to examine.
	 * @param 	errorHandler	An <CODE>ErrorHandler</CODE> instance used to report
	 *							validation failures.
	 * @return 	<CODE>false</CODE> if the validation test failed, <CODE>true
	 *			</CODE> otherwise. 
	 * @since	TFP 1.0
	 */
	protected abstract boolean validate (NodeIndex nodeIndex, ValidationErrorHandler errorHandler);
}