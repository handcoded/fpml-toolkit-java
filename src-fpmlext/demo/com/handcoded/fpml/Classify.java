// Copyright (C),2006-2012 HandCoded Software Ltd.
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

package demo.com.handcoded.fpml;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.classification.Category;
import com.handcoded.fpml.DefaultHelper;
import com.handcoded.fpml.Releases;
import com.handcoded.fpml.classification.FpMLTaxonomy;
import com.handcoded.fpml.classification.ISDATaxonomy;
import com.handcoded.fpml.identification.UPI;
import com.handcoded.fpml.infoset.ProductInfoset;
import com.handcoded.framework.Option;
import com.handcoded.meta.Conversion;
import com.handcoded.meta.DTDRelease;
import com.handcoded.meta.Release;
import com.handcoded.meta.SchemaRelease;
import com.handcoded.meta.Specification;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XmlUtility;

/**
 * This application demonstrates the classification components being used to
 * identify the type of product within an FpML document based on its structure.
 * 
 * @author	BitWise
 * @version	$Id: Classify.java 525 2011-09-02 16:52:24Z andrew_jacobs $
 * @since	TFP 1.0
 */
public class Classify extends Application
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
		new Classify ().run (arguments);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected void startUp ()
	{
		super.startUp ();
		
		if (getArguments ().length == 0) {
			logger.severe ("No files are present on the command line");
			System.exit (1);
		}
		
		if (hkmaOption.isPresent () && isdaOption.isPresent ()) {
			logger.severe ("Only one of -isda or -hkma can be specified");
			System.exit (1);
		}
				
		XmlUtility.getDefaultSchemaSet ().getSchema ();
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected void execute ()
	{
		String []		arguments 	= findFiles (getArguments ());
		
		try {
			for (String filename : arguments) {
				Document 	document = XmlUtility.nonValidatingParse (new File (filename));
				NodeIndex	nodeIndex;
				
				System.out.println (">> " + filename);
				
				Release release = Specification.releaseForDocument (document);
				
				if (release != null) {
					if (release instanceof DTDRelease)
						System.out.println ("= " + release
							+ " {" + ((DTDRelease) release).getPublicId () + "}");
					else if (release instanceof SchemaRelease)
						System.out.println ("= " + release
							+ " {" + ((SchemaRelease) release).getNamespaceUri () + "}");
					else
						System.out.println ("= " + release);
				}
				
                if (isdaOption.isPresent()) {
                    if (release != Releases.R5_3_CONFIRMATION) {
                        Conversion conversion = Conversion.conversionFor (release, Releases.R5_3_CONFIRMATION);

                        if (conversion == null) {
                            System.out.println ("!! The contents of the file can not be converted to FpML 5.3 (Confirmation)");
                            continue;
                        }
                        document = conversion.convert (document, new DefaultHelper ());

                        if (document == null) {
                        	System.out.println ("!! Automatic conversion to FpML 5.3 (Confirmation) failed");
                            continue;
                        }
                    }

				    nodeIndex = new NodeIndex (document);

				    doIsdaClassify (nodeIndex.getElementsByName ("trade"));
                }
                else {
				    nodeIndex = new NodeIndex (document);

				    doClassify (nodeIndex.getElementsByName ("trade"), "Trade");
				    doClassify (nodeIndex.getElementsByName ("contract"), "Contract");
                }			
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
		return (" files ...");
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.0
	 */
	private static Logger	logger
		= Logger.getLogger ("demo.com.handcoded.fpml.Classify");

	/**
	 * A command line option that allows the default taxonomy to be overridden.
	 * @since	TFP 1.6
	 */
	private Option				isdaOption
		= new Option ("-isda", "Use the ISDA taxonomy");
	
	/**
	 * A command line option that allows the default taxonomy to be overridden.
	 * @since	TFP 1.6
	 */
	private Option				hkmaOption
		= new Option ("-hkma", "Use the HKMA taxonomy");
	
	/**
	 * Constructs a <CODE>Classify</CODE> instance.
	 * @since	TFP 1.6
	 */
	private Classify ()
	{ }
	
	/**
	 * Uses the predefined FpML product types to attempt to classify a
	 * product within the document.
	 * 
	 * @param 	list		A set of context elements to analyse.
	 * @param 	container	The type of product container for display.
	 * @since	TFP 1.1
	 */
	private void doClassify (NodeList list, String container)
	{
		for (int index = 0; index < list.getLength (); ++index) {
			Element 	element	= (Element) list.item (index);
		    Category	category = FpMLTaxonomy.FPML.classify (element);

		    System.out.print (": " + container + "(");
		    System.out.print ((category != null) ? category.toString () : "UNKNOWN");
		    System.out.println (")");
		}
	}

	/**
	 * Attempts to classify the trades within the document using the ISDA
	 * taxonomy defined for regulatory reporting and generate an example
	 * UPI.
	 * 
	 * @param 	list		A set of context elements to analyse.
	 * @since	TFP 1.6
	 */
    private void doIsdaClassify (NodeList list)
    {
		for (int index = 0; index < list.getLength (); ++index) {
			Element 	element	= (Element) list.item (index);
		    Document	infoset		= ProductInfoset.createInfoset (element);
		    Element		infosetRoot	= infoset.getDocumentElement ();
						
		    Category	assetClass  = ISDATaxonomy.assetClassForInfoset (infosetRoot);
		    Category	productType = ISDATaxonomy.productTypeForInfoset (infosetRoot);
		    UPI			upi = UPI.forProductInfoset (infosetRoot, productType);

		    System.out.print (": Trade (");
		    System.out.print ((assetClass != null) ? assetClass.toString () : "UNKNOWN");
		    System.out.print (" / ");
		    System.out.print ((productType != null) ? productType.toString () : "UNKNOWN");
		    System.out.print (" / ");
		    System.out.print ((upi != null) ? upi.toString () : "UNKNOWN");
		    System.out.println (")");
        }
	}
}