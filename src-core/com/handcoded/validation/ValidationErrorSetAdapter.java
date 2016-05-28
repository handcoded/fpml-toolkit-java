// Copyright (C),2007 HandCoded Software Ltd.
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
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * The <CODE>ValidationErrorSetAdapter</CODE> provides an implementation of both
 * the SAX <CODE>ErrorHandler</CODE> and the business rule <CODE>ValidationErrorHandler</CODE>
 * interfaces which captures errors and records them in a <CODE>ValidationErrorSet</CODE>.
 * 
 * @author	BitWise
 * @version	$Id: ValidationErrorSetAdapter.java 157 2007-05-31 22:24:41Z andrew_jacobs $
 * @since	TFP 1.1
 */
public final class ValidationErrorSetAdapter implements ErrorHandler, ValidationErrorHandler
{
	/**
	 * Construct a <CODE>ValidationErrorSetAdapter</CODE> which will capture
	 * errors and store them in the indicated <CODE>ValidationErrorSet</CODE>.
	 * 
	 * @param 	errorSet		The <CODE>ValidationErrorSet</CODE> to populate with errors.
	 * @since	TFP 1.1
	 */
	public ValidationErrorSetAdapter (ValidationErrorSet errorSet)
	{
		this.errorSet = errorSet;
	}
	
	/**
	 * Capture warnings generated during XML parsing.
	 * 
	 * @param	error			The <CODE>SAXParseException</CODE> raised by the parser.
	 * @since	TFP 1.1
	 */
	public void warning (SAXParseException error)
	{
		errorSet.addError ("200", error.getLineNumber (), error.getColumnNumber (),
			error.getMessage (), "xml-warning", null);
	}	
	
	/**
	 * Capture warnings generated during XML parsing.
	 * 
	 * @param	error			The <CODE>SAXParseException</CODE> raised by the parser.
	 * @since	TFP 1.1
	 */
	public void error (SAXParseException error)
	{
		errorSet.addError ("200", error.getLineNumber (), error.getColumnNumber (),
				error.getMessage (), "xml-error", null);
	}	

	/**
	 * Capture warnings generated during XML parsing.
	 * 
	 * @param	error			The <CODE>SAXParseException</CODE> raised by the parser.
	 * @since	TFP 1.1
	 */
	public void fatalError (SAXParseException error)
	{
		errorSet.addError ("200", error.getLineNumber (), error.getColumnNumber (),
				error.getMessage (), "xml-fatal", null);
	}	

	/**
	 * Capture business rule errors generated during validation.
	 * 
	 * @param	code			The FpML defined reason code associated with the error.
	 * @param 	context			The DOM <CODE>Node</CODE> containing the erroneous data.
	 * @param 	description		A textual description of the problem detected.
	 * @param 	ruleName		The code for the FpML validation rule that has failed.
	 * @param 	additionalData	Any additional data that may assist in problem solving.
	 * @since	TFP 1.1
	 */
	public void error (String code, Node context, String description, String ruleName, String additionalData)
	{
		errorSet.addError (code, context, description, ruleName, additionalData);
	}
	
	/**
	 * The <CODE>ValidationErrorSet</CODE> to be populated with errors.
	 * @since	TFP 1.1
	 */
	private final ValidationErrorSet	errorSet;
}