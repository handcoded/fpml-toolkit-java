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

package demo.com.handcoded.fpml;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.fpml.infoset.ProductInfoset;
import com.handcoded.xml.NodeIndex;
import com.handcoded.xml.XmlUtility;
import com.handcoded.xml.writer.NestedWriter;
import com.handcoded.xml.writer.XmlWriter;

/**
 * This application demonstrates how to use the <CODE>ProductInfoset</CODE>
 * class to extract the core product definition from an FpML trade document.
 * 
 * @author	BitWise
 * @version	$Id$
 * @since	TFP 1.6
 */
public class Infoset extends Application
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
		new Infoset ().run (arguments);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected void startUp ()
	{
		super.startUp ();
		
		if (getArguments ().length == 0) {
			logger.severe ("No files are present on the command line");
			System.exit (1);
		}
				
		XmlUtility.getDefaultSchemaSet ().getSchema ();
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected void execute ()
	{
		String []		arguments 	= getArguments ();
		Document		document;
		NodeIndex		nodeIndex;
		
		arguments = findFiles (arguments);

		try {
			for (int index = 0; index < arguments.length; ++index) {
				String filename = arguments [index];
				document = XmlUtility.nonValidatingParse (new File (filename));
				
				System.out.println (">> " + filename);
								
				nodeIndex = new NodeIndex (document);
				doInfoset (nodeIndex.getElementsByName ("trade"));
				doInfoset (nodeIndex.getElementsByName ("contract"));
			}
		}
		catch (Exception error) {
			logger.log (Level.SEVERE, "Unexpected exception during processing", error);
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
		return (" files ...");
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.6
	 */
	private static Logger	logger
		= Logger.getLogger ("demo.com.handcoded.fpml.Classify");
	
	/**
	 * The <CODE>XmlWriter</CODE> used to format the output document.
	 * @since	TFP 1.6
	 */
	private XmlWriter 		writer;
	
	/**
	 * Constructs a <CODE>Infoset</CODE> instance.
	 * @since	TFP 1.6
	 */
	private Infoset ()
	{
		try {
			writer = new NestedWriter (System.out);
		} catch (UnsupportedEncodingException error) {
			logger.log (Level.SEVERE, "Failed to create XmlWriter", error);
			System.exit (2);
		}
	}

	/**
	 * Create the infoset for each context <CODE>Element</CODE> in the
	 * provided <CODE>NodeList</CODE> and write it to the output stream.
	 * 
	 * @param	list			The context <CODE>NodeList</CODE>.
	 * @since	TFP 1.6
	 */
	private void doInfoset (NodeList list)
	{
		for (int index = 0; index < list.getLength (); ++index) {
			Element		context = (Element) list.item (index);
			Document 	document = ProductInfoset.createInfoset (context);
			
			writer.write (document);
		}
	}
}