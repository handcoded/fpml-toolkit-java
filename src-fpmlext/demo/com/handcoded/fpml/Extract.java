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

package demo.com.handcoded.fpml;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import com.handcoded.framework.Option;
import com.handcoded.view.DataView;
import com.handcoded.view.ValueSet;
import com.handcoded.view.xml.ViewLoader;
import com.handcoded.xml.XmlUtility;

/**
 * This application demonstrates the data extraction facilities of the
 * DataView classes by applying them to the problem of recovering regulatory
 * reportable fields from FpML documents.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.9
 */
public class Extract extends Application
{
	/**
	 * Creates an application instance and invokes its <CODE>run</CODE>
	 * method passing the command line arguments.
	 * 
	 * @param 	arguments		The command line arguments
	 * @since	TFP 1.9
	 */
	public static void main (String [] arguments)
	{   
		new Extract ().run (arguments);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.9
	 */
	@Override
	protected void startUp ()
	{
		super.startUp ();
		
		if (getArguments ().length == 0) {
			logger.severe ("No files are present on the command line");
			System.exit (1);
		}
		
		if (!viewOption.isPresent()) {
			logger.severe ("No view definition was specified");
			System.exit (1);
		}
						
		XmlUtility.getDefaultSchemaSet ().getSchema ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute()
	{
		String []		arguments 	= findFiles (getArguments ());

		DataView		view		= ViewLoader.load (viewOption.getValue ());
		
		try {
			for (String filename : arguments) {
				Document 	document = XmlUtility.nonValidatingParse (new File (filename));
				
				System.out.println (">> " + filename);
				
				ValueSet	values = view.applyTo (document.getDocumentElement ());
				
				System.out.println (values);
			}
		}
		catch (Exception error) {
			logger.log (Level.SEVERE, "Unexpected exception during processing", error);
		}
		
		setFinished (true);
	}

	/**
	 * A command line option that allows the default taxonomy to be overridden.
	 * @since	TFP 1.9
	 */
	private Option				viewOption
		= new Option ("-view", "Defines the view to use", "view-defn");
	
	/**
	 * {@inheritDoc} 
	 * @since	TFP 1.9
	 */
	@Override
	protected String describeArguments ()
	{
		return (" files ...");
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.9
	 */
	private static Logger	logger
		= Logger.getLogger ("demo.com.handcoded.fpml.Extract");


}