// Copyright (C),2005-2013 HandCoded Software Ltd.
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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import com.handcoded.fpml.DefaultHelper;
import com.handcoded.fpml.Releases;
import com.handcoded.framework.Option;
import com.handcoded.meta.Conversion;
import com.handcoded.meta.ConversionException;
import com.handcoded.meta.Release;
import com.handcoded.meta.Specification;
import com.handcoded.xml.XmlUtility;
import com.handcoded.xml.writer.NestedWriter;

/**
 * This application demonstrates the conversion components being used to
 * convert an FpML document to another version.
 * 
 * @author	BitWise
 * @version	$Id: Convert.java 766 2013-11-12 19:29:14Z andrew_jacobs $
 * @since	TFP 1.6
 */
public final class Convert extends Application
{
	/**
	 * Creates an application instance and invokes its <CODE>run</CODE>
	 * method passing the command line arguments.
	 * 
	 * @param 	arguments		The command line arguments
	 * @since	TFP 1.6
	 */
	public static void main (String [] arguments)
	{   
		new Convert ().run (arguments);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected void startUp ()
	{
		super.startUp ();
		
		if (!targetOption.isPresent ()) {
			logger.severe ("No target version specified.");
			System.exit (1);
		}
		
		if (!outputOption.isPresent ()) {
			logger.severe ("No output directory was specified.");
			System.exit (1);
		}

		directory = new File (outputOption.getValue ());
		if (directory.exists ()) {
			if (!directory.isDirectory ()) {
				logger.severe ("The output target exists and is not a directory");
				System.exit (1);
			}
		}
		else {
			if (!directory.mkdir ()) {
				logger.severe ("Failed to create output directory");
				System.exit (1);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected void execute ()
	{
		String []	arguments = findFiles (getArguments ());
		
		for (String filename : arguments) {
			File	 	file	 = new File (filename);
			Document 	document = XmlUtility.nonValidatingParse (file);
			
			System.out.println (">> " + filename);
			
			Release source = Specification.releaseForDocument (document);
			Release target = Releases.compatibleRelease (document, targetOption.getValue ());
			
			if (target == null) {
				System.out.println ("!! No compatible target FpML release");
				continue;
			}
			
			Conversion	conversion = Conversion.conversionFor (source, target);
			
			if (conversion == null) {
				System.out.println ("!! No conversion path exists to the target version");
				continue;
			}
			
			try {
				Document newDocument = conversion.convert (document, new DefaultHelper ());
				
				try {
				OutputStream	stream = new FileOutputStream (new File (directory, file.getName ()));
				
				new NestedWriter (stream).write (newDocument);
				stream.close ();
				}
				catch (Exception error) {
					logger.log (Level.SEVERE, "Exception while writing converted XML document", error);
				}
			}
			catch (ConversionException error) {
				logger.log (Level.SEVERE, "FpML document conversion failed", error);
				continue;
			}
		}
		setFinished (true);
	}

	/**
	 * {@inheritDoc} 
	 * @since	TFP 1.6
	 */
	@Override
	protected String describeArguments ()
	{
		return (" files or directories ...");
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.6
	 */
	private static Logger	logger
		= Logger.getLogger ("demo.com.handcoded.fpml.Convert");

	/**
	 * The <CODE>Option</CODE> instance use to detect <CODE>-target version</CODE>
	 * @since	TFP 1.6
	 */
	private Option			targetOption
		= new Option ("-target", "The target version of FpML", "version");
	
	/**
	 * The <CODE>Option</CODE> instance use to detect <CODE>-output directory</CODE>
	 * @since	TFP 1.6
	 */
	private Option			outputOption
		= new Option ("-output", "The output directory", "directory");
	
	private File			directory;
	
	/**
	 * Constructs a <CODE>Validate</CODE> instance.
	 * @since	TFP 1.6
	 */
	private Convert ()
	{ }
}