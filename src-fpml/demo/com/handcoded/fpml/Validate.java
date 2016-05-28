// Copyright (C),2005-2011 HandCoded Software Ltd.
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

package demo.com.handcoded.fpml;

import java.io.File;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

import com.handcoded.fpml.FpMLUtility;
import com.handcoded.fpml.validation.AllRules;
import com.handcoded.fpml.validation.FpMLRules;
import com.handcoded.framework.Option;
import com.handcoded.validation.RuleSet;
import com.handcoded.xml.XPath;
import com.handcoded.xml.XmlUtility;

/**
 * This application demonstrates the validation components being used to
 * perform business level validation of an FpML document.
 * 
 * @author	BitWise
 * @version	$Id: Validate.java 592 2012-03-10 00:30:45Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class Validate extends Application
{
	/**
	 * Creates an application instance and invokes its <CODE>run</CODE>
	 * method passing the command line arguments.
	 * 
	 * @param 	arguments		The command line arguments
	 * @since	TFP 1.0
	 */
	public static void main (String [] arguments)
	{   
		new Validate ().run (arguments);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected void startUp ()
	{
		super.startUp ();
		
		if (outputOption.isPresent ()) {
			try {
				stream = new PrintStream (new File (outputOption.getValue ()));
			}
			catch (Exception error) {
				logger.severe ("Failed to create output file");
				System.exit (1);
			}
		}
		
		if (repeatOption.isPresent ()) {
			repeat = Integer.parseInt (repeatOption.getValue ());
			if (repeat <= 0) {
				logger.severe ("The repeat count must be >= 1");
				System.exit (1);
			}
		}
		random = randomOption.isPresent ();

		if (getArguments ().length == 0) {
			logger.severe ("No files are present on the command line");
			System.exit (1);
		}
		
		if (reportOption.isPresent ()) {
			stream.println ("<?xml version=\"1.0\"?>");
			stream.println ("<report>");			
		}
		
		XmlUtility.getDefaultSchemaSet ().getSchema ();
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected void cleanUp ()
	{
		super.cleanUp ();
		
		if (reportOption.isPresent ()) {
			stream.println ("</report>");			
		}
		stream.close ();
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected void execute ()
	{
		RuleSet					rules = strictOption.isPresent () ? FpMLRules.getRules() : AllRules.getRules ();
		String []				arguments = getArguments ();
		ParserErrorHandler		parserErrorHandler = new ParserErrorHandler ();
		ValidationErrorHandler	validationErrorHandler = new ValidationErrorHandler ();
		boolean					schemaOnly = schemaOnlyOption.isPresent();
		int						count = 0;
		
		arguments = findFiles (arguments);
		
		try {
			long start = System.currentTimeMillis();
	
			while (repeat-- > 0) {
				for (int index = 0; index < arguments.length; ++index) {
					int		which = random ? (int)(Math.random () * arguments.length ) : index;
					
					File file = new File (arguments [which]);
					
					if (reportOption.isPresent ())
						stream.println ("\t<file name=\"" + file.getName () + "\">");
					else
						stream.println (">> " + arguments [which]);
					
					if (noValidationOption.isPresent ()) {
						Document document = XmlUtility.nonValidatingParse (file);
						FpMLUtility.validate (document, validationErrorHandler);
					}
					else
						FpMLUtility.parseAndValidate (schemaOnly, file, rules, parserErrorHandler, validationErrorHandler);
					
					if (reportOption.isPresent ())
						stream.println ("\t</file>");

					++count;
				}
			}
			
			long end = System.currentTimeMillis ();
			
			if (reportOption.isPresent ())
				stream.println ("\t<statistics count=\"" + count
						+ "\" time=\"" + (end - start)
						+ "\" rate=\"" + ((1000.0 * count) / (end - start))
						+ "\" rules=\"" + rules.size () + "\"/>");
			else {
				stream.println ("== Processed " + count + " files in "
					+ (end - start) + " milliseconds");
				stream.println ("== " + ((1000.0 * count) / (end - start))
					+ " files/sec checking " + rules.size () + " rules");
			}
		}
		catch (Exception error) {
			logger.log (Level.SEVERE, "Unexpected exception during processing", error);
		}
		
		setFinished (true);
	}
	
	/**
	 * {@inheritDoc} 
	 * @since	TFP 1.0
	 */
	@Override
	protected String describeArguments ()
	{
		return (" files or directories ...");
	}
	
	/**
	 * The <CODE>ParserErrorHandler</CODE> provides an implementation of
	 * the SAX <CODE>ErrorHandler</CODE> interface used to report errors
	 * during XML parsing. 
	 * 
	 * @since	TFP 1.0
	 */
	private class ParserErrorHandler implements org.xml.sax.ErrorHandler
	{
		public void warning (SAXParseException error)
		{
			if (reportOption.isPresent ())
				stream.println ("\t\t<syntaxError>" + error.getMessage () + "</syntaxError>");
			else
				stream.println (error.getMessage ());
		}	
		
		public void error (SAXParseException error)
		{
			if (reportOption.isPresent ())
				stream.println ("\t\t<syntaxError>" + error.getMessage () + "</syntaxError>");
			else
				stream.println (error.getMessage ());
		}	

		public void fatalError (SAXParseException error)
		{
			if (reportOption.isPresent ())
				stream.println ("\t\t<syntaxError>" + error.getMessage () + "</syntaxError>");
			else
				stream.println (error.getMessage ());
		}	
	}
	
	/**
	 * The <CODE>ValidationErrorHandler</CODE> implements the <CODE>ErrorHandler
	 * </CODE> interface used by the validation toolkit to report semantic errors.
	 * 
	 * @since	TFP 1.0
	 */
	private class ValidationErrorHandler implements com.handcoded.validation.ValidationErrorHandler
	{
		public void error (String code, Node context, String description, String ruleName, String additionalData)
		{
			if (reportOption.isPresent ()) {
				stream.println ("\t\t<validationError rule=\"" + ruleName
						+ "\" context=\"" + XPath.forNode (context)
						+ "\"" + ((additionalData != null) ? (" additionalData=\"" + additionalData + "\"") : "")
						+ ">" + description + "</validationError>");
			}
			else {
				if (additionalData != null)
					stream.println (ruleName + " " + XPath.forNode(context) + " " + description + " [" + additionalData + "]");
				else
					stream.println (ruleName + " " + XPath.forNode(context) + " " + description);
			}
		}
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.0
	 */
	private static Logger	logger
		= Logger.getLogger ("demo.com.handcoded.fpml.Validate");

	/**
	 * The <CODE>Option</CODE> instance used to detect <CODE>-repeat count</CODE>
	 * @since	TFP 1.0
	 */
	private Option			repeatOption
		= new Option ("-repeat", "Number of times to processes the files", "count");
	
	/**
	 * The <CODE>Option</CODE> instance used to detect <CODE>-random</CODE>
	 * @since	TFP 1.0
	 */
	private Option			randomOption
		= new Option ("-random", "Pick files at random for processing");
	
	
	/**
	 * The <CODE>Option</CODE> instance used to detect <CODE>-strict</CODE>
	 * @since	TFP 1.0
	 */
	private Option			strictOption
		= new Option ("-strict", "Use only FpML defined rules (no extensions)");
	
	/**
	 * The <CODE>Option</CODE> instance used to detect <CODE>-strict</CODE>
	 * @since	TFP 1.0
	 */
	private Option			schemaOnlyOption
		= new Option ("-schemaOnly", "Only accept schema based documents");
	
	/**
	 * The <CODE>Option</CODE> instance used to detect <CODE>-report</CODE>
	 * @since	TFP 1.5
	 */
	private Option			reportOption
		= new Option ("-report", "Generate an XML report of the results");
	
	/**
	 * The <CODE>Option</CODE> instance used to detect <CODE>-output</CODE>
	 * @since	TFP 1.6
	 */
	private Option			outputOption
		= new Option ("-output", "Write output to file", "file");
	
	/**
	 * The <CODE>Option</CODE> instance use to detect <CODE>-report</CODE>
	 * @since	TFP 1.5
	 */
	private Option			noValidationOption
		= new Option ("-noValidation", "Don't perform XML validation");
	
	/**
	 * The <CODE>PrintStream</CODE> instance to output to.
	 * @since	TFP 1.6
	 */
	private PrintStream		stream = System.out;
	
	/**
	 * A counter for the number of time to re-process the files.
	 * @since	TFP 1.0
	 */
	private int				repeat = 1;
	
	/**
	 * A flag indicating whether to randomise the file list.
	 * @since	TFP 1.0
	 */
	private boolean			random = false;

	/**
	 * Constructs a <CODE>Validate</CODE> instance.
	 * @since	TFP 1.0
	 */
	private Validate ()
	{ }
}