// Copyright (C),2006 HandCoded Software Ltd.
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

import org.w3c.dom.Node;

import com.handcoded.xml.XPath;

/**
 * Instances of the <CODE>ValidationError</CODE> class are used to hold the
 * details of previous validation failure events.
 * <P>
 * <CODE>ValidationError</CODE> instances cannot be modified once they have
 * been constructed.
 * 
 * @author	BitWise
 * @version	$Id: ValidationError.java 157 2007-05-31 22:24:41Z andrew_jacobs $
 * @since	TFP 1.0
 */
public class ValidationError
{
	/**
	 * Constructs a <CODE>ValidationError</CODE> instance from the details provided
	 * to a syntax error handler.
	 *
	 * @param	code			The FpML defined reason code associated with the error.
	 * @param	line			The line number where the problem was detected.
	 * @param	column			The column number where the problem was detected.
	 * @param	description		A textual description of the problem detected.
	 * @param	ruleName		The code for the FpML validation rule that has failed.
	 * @param	additionalData	Any additional data that may assist in problem solving.
	 * @since	TFP 1.0
	 */
	public ValidationError (final String code, int line, int column, final String description,
		final String ruleName, final String additionalData)
	{
		this (code, line + "," + column, true, description, ruleName, additionalData);
	}

	/**
	 * Constructs a <CODE>ValidationError</CODE> instance from the details provided
	 * to a semantic error handler.
	 *
	 * @param	code			The FpML defined reason code associated with the error.
	 * @param 	context			The DOM <CODE>Node</CODE> containing the erroneous data.
	 * @param 	description		A textual description of the problem detected.
	 * @param 	ruleName		The code for the FpML validation rule that has failed.
	 * @param 	additionalData	Any additional data that may assist in problem solving.
	 * @since	TFP 1.0
	 */
	public ValidationError (final String code, Node context, final String description,
		final String ruleName, final String additionalData)
	{
		this (code, XPath.forNode (context), false, description, ruleName, additionalData);
	}
	
	/**
	 * Access the error code classifier.
	 * 
	 * @return	The error code classifier.
	 * @since	TFP 1.0
	 */
	public String getCode ()
	{
		return (code);
	}
	
	/**
	 * Access the error context, either an XPath expression or a lexical (e.g.
	 * line and column) location.
	 * 
	 * @return	The error context.
	 * @since	TFP 1.0
	 */
	public String getContext ()
	{
		return (context);
	}

	/**
	 * Indicates if the context is expressed lexically.
	 * 
	 * @return	The lexical context indicator.
	 * @since	TFP 1.0
	 */
	public boolean isLexical ()
	{
		return (lexical);
	}
	
	/**
	 * Access the error description text.
	 * 
	 * @return	The error description.
	 * @since	TFP 1.0
	 */
	public String getDescription ()
	{
		return (description);
	}
	
	/**
	 * Access the rule name (if any).
	 * 
	 * @return	The rule name or <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
	public String getRuleName ()
	{
		return (ruleName);
	}
	
	/**
	 * Access the additional data (if any).
	 * 
	 * @return	The additional data or <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
	public String getAdditionalData ()
	{
		return (additionalData);
	}
	
	/**
	 * The number code classifier for the error.
	 * @since	TFP 1.0
	 */
	private final String		code;
	
	/**
	 * The context for the error, either an XPath or 'line,column' pair.
	 * @since	TFP 1.0
	 */
	private final String		context;
	
	/**
	 * <CODE>true</CODE> if the context is lexical.
	 * @since	TFP 1.0
	 */
	private final boolean		lexical;
	
	/**
	 * A description of the error.
	 * @since	TFP 1.0
	 */
	private final String		description;
	
	/**
	 * The rule which detected the problem or <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
	private final String		ruleName;

	/**
	 * Additional data provided with the error or <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
	private final String		additionalData;
	
	/**
	 * Constructs a <CODE>ValidationError</CODE> instance from the details
	 * provided.
	 * 
	 * @param code
	 * @param context
	 * @param lexical
	 * @param description
	 * @param ruleName
	 * @param additionalData
	 * @since	TFP 1.0
	 */
	private ValidationError (final String code, final String context, boolean lexical,
			final String description,	final String ruleName, final String additionalData)
	{
		this.code 			= code;
		this.context 		= context;
		this.lexical 		= lexical;
		this.description 	= description;
		this.ruleName 		= ruleName;
		this.additionalData	= additionalData;
	}
}