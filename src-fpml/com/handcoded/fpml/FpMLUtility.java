// Copyright (C),2005-2007 HandCoded Software Ltd.
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

package com.handcoded.fpml;

import java.io.File;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;

import com.handcoded.fpml.validation.AllRules;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.XmlUtility;

/**
 * The <CODE>FpMLUtility</CODE> class contains a set of methods for performing 
 * common operations on FpML document instances.
 * 
 * @author	BitWise
 * @version	$Id: FpMLUtility.java 120 2007-03-21 21:53:07Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class FpMLUtility
{
	/**
	 * Parses the XML string provided passing any validation problems to
	 * the indicated <CODE>ErrorHandler</CODE>.
	 *
	 * @param	xml				The XML string to be parsed.
	 * @param	errorHandler	The <CODE>ErrorHandler</CODE> instance or <CODE>null</CODE>
	 * @return	A <CODE>Document</CODE> instance constructed from the XML document.
	 * @since	TFP 1.0
	 */
	public static Document parse (final String xml, ErrorHandler errorHandler)
	{
		return (parse (false, xml, errorHandler));
	}
	
	/**
	 * Parses an XML document from the given <CODE>File</CODE> passing
	 * any reported errors to the <CODE>EerorHandler</CODE> instance.
	 *
	 * @param	file			The <CODE>File</CODE> to process XML from.
	 * @param	errorHandler	The <CODE>ErrorHandler</CODE> instance or <CODE>null</CODE>
	 * @return	A <CODE>Document</CODE> instance constructed from the XML document.
	 * @since	TFP 1.0
	 */
	public static Document parse (File file, ErrorHandler errorHandler)
	{
		return (parse (false, file, errorHandler));
	}
	
	/**
	 * Parses the XML string provided passing any validation problems to
	 * the indicated <CODE>ErrorHandler</CODE>. The <CODE>schemaOnly</CODE>
	 * argument indicates if both DTD and schema, or just schema documents
	 * should be supported.
	 *
	 * @param	schemaOnly		Indicates only schema based documents to be processed. 
	 * @param	xml				The XML string to be parsed.
	 * @param	errorHandler	The <CODE>ErrorHandler</CODE> instance or <CODE>null</CODE>
	 * @return	A <CODE>Document</CODE> instance constructed from the XML document.
	 * @since	TFP 1.0
	 */
	public static Document parse (boolean schemaOnly, final String xml, ErrorHandler errorHandler)
	{
		return (
			XmlUtility.validatingParse (
				(schemaOnly ? XmlUtility.SCHEMA_ONLY : XmlUtility.DTD_OR_SCHEMA), xml,
				XmlUtility.getDefaultSchemaSet ().getSchema (),
				XmlUtility.getDefaultCatalog (), errorHandler));
	}

	/**
	 * Parses an XML document from the given <CODE>File</CODE> passing
	 * any reported errors to the <CODE>EerorHandler</CODE> instance. The
	 * <CODE>schemaOnly</CODE> argument indicates if both DTD and schema,
	 * or just schema documents should be supported.
	 *
	 * @param	schemaOnly		Indicates only schema based documents to be processed. 
	 * @param	file			The <CODE>File</CODE> to process XML from.
	 * @param	errorHandler	The <CODE>ErrorHandler</CODE> instance or <CODE>null</CODE>
	 * @return	A <CODE>Document</CODE> instance constructed from the XML document.
	 * @since	TFP 1.0
	 */
	public static Document parse (boolean schemaOnly, File file, ErrorHandler errorHandler)
	{
		return (
			XmlUtility.validatingParse (
				(schemaOnly ? XmlUtility.SCHEMA_ONLY : XmlUtility.DTD_OR_SCHEMA), file,
				XmlUtility.getDefaultSchemaSet ().getSchema (),
				XmlUtility.getDefaultCatalog (), errorHandler));
	}

	/**
	 * Uses the given <CODE>RuleSet</CODE> to perform a semantic validation of
	 * the DOM <CODE>Document</CODE> and reports errors (if any).
	 *
	 * @param 	document		The <CODE>Document</CODE> to be validated.
	 * @param 	rules			The <CODE>RuleSet</CODE> to use.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to report issues.
	 * @return	<CODE>true</CODE> if the <CODE>Document</CODE> successfully passed
	 * 			all applicable rules, <CODE>false</CODE> if one or more rules
	 * 			failed.
	 * @since	TFP 1.0
	 */
	public static boolean validate (final Document document, final RuleSet rules, final ValidationErrorHandler validationErrorHandler)
	{
		return (rules.validate (document, validationErrorHandler));
	}

	/**
	 * Uses the default FpML  <CODE>RuleSet</CODE> to perform a semantic validation of
	 * the DOM <CODE>Document</CODE> and reports errors (if any).
	 *
	 * @param 	document		The <CODE>Document</CODE> to be validated.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the <CODE>Document</CODE> successfully passed
	 * 			all applicable rules, <CODE>false</CODE> if one or more rules
	 * 			failed.
	 * @since	TFP 1.0
	 */
	public static boolean validate (final Document document, final ValidationErrorHandler validationErrorHandler)
	{
		return (validate (document, AllRules.getRules (), validationErrorHandler));
	}

	/**
	 * Attempts to parse an XML document from a string and then pass it through
	 * the specified validation rule set.
	 * <P>
	 * This function does not provide any access to the DOM <CODE>Document</CODE>
	 * created during the parsing process.
	 * 
	 * @param	xml				The XML string to be parsed.
	 * @param 	rules			The <CODE>RuleSet</CODE> used for validation.
	 * @param 	errorHandler	The <CODE>ErrorHandler</CODE> used to report parser errors.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the parse was successful, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean parseAndValidate (final String xml, RuleSet rules, ErrorHandler errorHandler, ValidationErrorHandler validationErrorHandler)
	{
		return (parseAndValidate (false, xml, rules, errorHandler, validationErrorHandler));
	}
	
	/**
	 * Attempts to parse an XML document from the indicated <CODE>File</CODE>
	 * and then pass it through the specified validation rule set.
	 * <P>
	 * This function does not provide any access to the DOM <CODE>Document</CODE>
	 * created during the parsing process.
	 * 
	 * @param	file			The <CODE>File</CODE> to be parsed.
	 * @param 	rules			The <CODE>RuleSet</CODE> used for validation.
	 * @param 	errorHandler	The <CODE>ErrorHandler</CODE> used to report parser errors.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the parse was successful, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean parseAndValidate (File file, RuleSet rules, ErrorHandler errorHandler, ValidationErrorHandler validationErrorHandler)
	{
		return (parseAndValidate (false, file, rules, errorHandler, validationErrorHandler));
	}
	
	/**
	 * Attempts to parse an XML document from a string and then pass it through
	 * the default FpML validation rule set.
	 * <P>
	 * This function does not provide any access to the DOM <CODE>Document</CODE>
	 * created during the parsing process.
	 * 
	 * @param	xml				The XML string to be parsed.
	 * @param 	errorHandler	The <CODE>ErrorHandler</CODE> used to report parser errors.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the parse was successful, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean parseAndValidate (final String xml, ErrorHandler errorHandler, ValidationErrorHandler validationErrorHandler)
	{
		return (parseAndValidate (xml, AllRules.getRules (), errorHandler, validationErrorHandler));
	}

	/**
	 * Attempts to parse an XML document from the indicated <CODE>File</CODE>
	 * and then pass it through the default FpML validation rule set.
	 * <P>
	 * This function does not provide any access to the DOM <CODE>Document</CODE>
	 * created during the parsing process.
	 * 
	 * @param	file			The <CODE>File</CODE> to be parsed.
	 * @param 	errorHandler	The <CODE>ErrorHandler</CODE> used to report parser errors.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the parse was successful, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean parseAndValidate (File file, ErrorHandler errorHandler, ValidationErrorHandler validationErrorHandler)
	{
		return (parseAndValidate (file, AllRules.getRules (), errorHandler, validationErrorHandler));
	}

	/**
	 * Attempts to parse an XML document from a string and then pass it through
	 * the specified validation rule set.
	 * <P>
	 * This function does not provide any access to the DOM <CODE>Document</CODE>
	 * created during the parsing process.
	 * 
	 * @param	schemaOnly		Indicates only schema based documents to be processed. 
	 * @param	xml				The XML string to be parsed.
	 * @param 	rules			The <CODE>RuleSet</CODE> used for validation.
	 * @param 	errorHandler	The <CODE>ErrorHandler</CODE> used to report parser errors.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the parse was successful, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean parseAndValidate (boolean schemaOnly, final String xml,
			RuleSet rules, ErrorHandler errorHandler, ValidationErrorHandler validationErrorHandler)
	{
		Document		document = parse (schemaOnly, xml, errorHandler);

		return ((document != null) ? rules.validate (document, validationErrorHandler) : false);
	}
	
	/**
	 * Attempts to parse an XML document from the indicated <CODE>File</CODE>
	 * and then pass it through the specified validation rule set.
	 * <P>
	 * This function does not provide any access to the DOM <CODE>Document</CODE>
	 * created during the parsing process.
	 * 
	 * @param	schemaOnly		Indicates only schema based documents to be processed. 
	 * @param	file			The <CODE>File</CODE> to be parsed.
	 * @param 	rules			The <CODE>RuleSet</CODE> used for validation.
	 * @param 	errorHandler	The <CODE>ErrorHandler</CODE> used to report parser errors.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the parse was successful, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean parseAndValidate (boolean schemaOnly, File file,
			RuleSet rules, ErrorHandler errorHandler, ValidationErrorHandler validationErrorHandler)
	{
		Document		document = parse (schemaOnly, file, errorHandler);

		return ((document != null) ? rules.validate (document, validationErrorHandler) : false);
	}
	
	/**
	 * Attempts to parse an XML document from a string and then pass it through
	 * the default FpML validation rule set.
	 * <P>
	 * This function does not provide any access to the DOM <CODE>Document</CODE>
	 * created during the parsing process.
	 * 
	 * @param	schemaOnly		Indicates only schema based documents to be processed. 
	 * @param	xml				The XML string to be parsed.
	 * @param 	errorHandler	The <CODE>ErrorHandler</CODE> used to report parser errors.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the parse was successful, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean parseAndValidate (boolean schemaOnly, final String xml,
			ErrorHandler errorHandler, ValidationErrorHandler validationErrorHandler)
	{
		return (parseAndValidate (schemaOnly, xml, AllRules.getRules (), errorHandler, validationErrorHandler));
	}

	/**
	 * Attempts to parse an XML document from the indicated <CODE>File</CODE>
	 * and then pass it through the default FpML validation rule set.
	 * <P>
	 * This function does not provide any access to the DOM <CODE>Document</CODE>
	 * created during the parsing process.
	 * 
	 * @param	schemaOnly		Indicates only schema based documents to be processed. 
	 * @param	file			The <CODE>File</CODE> to be parsed.
	 * @param 	errorHandler	The <CODE>ErrorHandler</CODE> used to report parser errors.
	 * @param 	validationErrorHandler	The <CODE>ValidationErrorHandler</CODE> used to semantic report issues.
	 * @return	<CODE>true</CODE> if the parse was successful, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.0
	 */
	public static boolean parseAndValidate (boolean schemaOnly, File file,
			ErrorHandler errorHandler, ValidationErrorHandler validationErrorHandler)
	{
		return (parseAndValidate (schemaOnly, file, AllRules.getRules (), errorHandler, validationErrorHandler));
	}

	/**
	 * Ensures no instances can be created
	 * @since	TFP 1.0
	 */
	private FpMLUtility()
	{ }
}