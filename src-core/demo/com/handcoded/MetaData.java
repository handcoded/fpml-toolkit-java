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

package demo.com.handcoded;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import com.handcoded.framework.Application;
import com.handcoded.meta.DTDRelease;
import com.handcoded.meta.Release;
import com.handcoded.meta.SchemaRelease;
import com.handcoded.meta.Specification;
import com.handcoded.xml.XmlUtility;
import com.handcoded.xml.resolver.CatalogManager;

/**
 * A simple application that dumps out a list of specifications and releases
 * registered as meta-data when the Toolkit is initialised.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class MetaData extends Application
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
		new MetaData ().run (arguments);
	}

	/**
	 * Constructs a <CODE>MetaData</CODE> application instance.
	 * @since	TFP 1.6
	 */
	protected MetaData ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected void startUp ()
	{
		super.startUp();

		// Initialise the default catalog
		try {
			XmlUtility.setDefaultCatalog (CatalogManager.find ("files-core/catalog-toolkit.xml"));
		}
		catch (SAXException error) {
			logger.log (Level.SEVERE, "Failed to parse XML catalog");
			System.exit (1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected void execute ()
	{
		for (Specification specification : Specification.specifications ()) {
			
			System.out.println (">> " + specification.getName ());

			for (Release release : specification.releases ()) {
				if (release instanceof DTDRelease)
					System.out.println ("DTD " + release.getVersion ()
							+ " " + ((DTDRelease) release).getPublicId ());
				else if (release instanceof SchemaRelease)
					System.out.println ("XSD " + release.getVersion ()
							+ " " + ((SchemaRelease) release).getNamespaceUri ());
				else
					System.out.println ("??? " + release.getVersion ());
			}
		}
		setFinished (true);
	}
	
	/**
	 * Logging instance used for error reporting.
	 * @since	TFP 1.6
	 */
	private static Logger		logger
		= Logger.getLogger ("demo.com.handcoded.MetaData");
}