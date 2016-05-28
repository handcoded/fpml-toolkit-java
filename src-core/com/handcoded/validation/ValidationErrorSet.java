// Copyright (C),2006-2011 HandCoded Software Ltd.
// All rights reserved.
//
// This software is the confidential and proprietary information of HandCoded
// Software Ltd. ("Confidential Information").  You shall not disclose such
// Confidential Information and shall use it only in accordance with the terms
// of the license agreement you entered into with HandCoded Software.
//
// HANDCODED SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
// SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. HANDCODED SOFTWARE SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
// OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

package com.handcoded.validation;

import java.util.Vector;

import org.w3c.dom.Node;

/**
 * A <CODE>ValidationErrorSet</CODE> instance can be used to capture syntax or
 * semantic validation errors generated during XML parsing or business rule
 * evaluation.
 * 
 * @author	BitWise
 * @version	$Id: ValidationErrorSet.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class ValidationErrorSet
{
	/**
	 * Constructs an empty <CODE>ValidationErrorSet</CODE>.
	 * @since	TFP 1.0
	 */
	public ValidationErrorSet ()
	{ }
	
	/**
	 * Returns the number of <CODE>ValidationError</CODE> instances in the set.
	 * 
	 * @return	The number of <CODE>ValidationError</CODE> instances held.
	 * @since	TFP 1.0
	 */
	public int count ()
	{
		return (errors.size ());
	}
	
	/**
	 * Access the specified <CODE>ValidationError</CODE> within the set.
	 * 
	 * @param 	index			The required item index.
	 * @return	The corresponding <CODE>ValidationError</CODE> instance.
	 */
	public ValidationError getItem (int index)
	{
		return (errors.elementAt (index));
	}

	/**
	 * Clears the set so that it can be reused.
	 * @since	TFP 1.0
	 */
	public void clear ()
	{
		errors.clear ();
	}
	
	/**
	 * Adds the indicated <CODE>ValidationError</CODE> instance to the set.
	 * 
	 * @param 	error			The <CODE>ValidationError</CODE> to store.
	 * @since	TFP 1.0
	 */
	public void addError (ValidationError error)
	{
		errors.add (error);
	}

	/**
	 * Creates a <CODE>ValidationError</CODE> from the parameters and adds it
	 * to the set.
	 * 
	 * @param	code			The FpML defined reason code associated with the error.
	 * @param	line			The line number where the problem was detected.
	 * @param	column			The column number where the problem was detected.
	 * @param 	description		A textual description of the problem detected.
	 * @param 	ruleName		The code for the FpML validation rule that has failed.
	 * @param 	additionalData	Any additional data that may assist in problem solving.
	 * @since	TFP 1.0
	 */
	public void addError (final String code, int line, int column, final String description,
			final String ruleName, final String additionalData)
	{
		addError (new ValidationError (code, line, column, description, ruleName, additionalData));
	}

	/**
	 * Creates a <CODE>ValidationError</CODE> from the parameters and adds it
	 * to the set.
	 * 
	 * @param	code			The FpML defined reason code associated with the error.
	 * @param 	context			The DOM <CODE>Node</CODE> containing the erroneous data.
	 * @param 	description		A textual description of the problem detected.
	 * @param 	ruleName		The code for the FpML validation rule that has failed.
	 * @param 	additionalData	Any additional data that may assist in problem solving.
	 * @since	TFP 1.0
	 */
	public void addError (final String code, Node context, final String description,
			final String ruleName, final String additionalData)
	{
		addError (new ValidationError (code, context, description, ruleName, additionalData));
	}

	/**
	 * The <CODE>Vector</CODE> used to the hold the errors.
	 * @since	TFP 1.0
	 */
	private Vector<ValidationError> errors
		= new Vector<ValidationError> ();
}
