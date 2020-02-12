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

package com.handcoded.xsl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.handcoded.framework.Application;

/**
 * The <CODE>Transformation</CODE> class is a wrapper around the JAXP APIs that
 * allows a <CODE>javax.xml.transform.Transformer</CODE> to be created from a
 * file based source and applied to DOM <CODE>Document</CODE>.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class Transformation
{
	/**
	 * Constructs a <CODE>Transformation</CODE> instance using the XSL source
	 * in the specified file.
	 * 
	 * @param	filename		The XSL filename
	 * @since	TFP 1.6
	 */
	public Transformation (final String filename)
	{
		try {
			transformer = factory.newTransformer (new StreamSource (Application.openStream (filename)));
		}
		catch (TransformerConfigurationException error) {
			logger.log(Level.SEVERE, "Failed to create an XSL transformation", error);
		}
	}
	
	/**
	 * Applies the XSL transformation to the indicated DOM <CODE>Node</CODE>
	 * and returns the resulting document.
	 * 
	 * @param	node			The source DOM <CODE>Node</CODE>.
	 * @return	A new DOM <CODE>Document</CODE> containing the result of the
	 * 			XSL transformation.
	 * @since	TFP 1.6
	 */
	public synchronized Document transform (final Node node)
	{
		DOMResult	result	= new DOMResult ();
		
		try {
			transformer.transform (new DOMSource (node), result);
		}
		catch (TransformerException error) {
			logger.log (Level.SEVERE, "Unexpected exception during XSL transformation", error);
		}
		return ((Document) result.getNode ());
	}
	
	/**
	 * Logging instance used to record runtime problems.
	 * @since	TFP 1.6
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.xsl.Transformation");
	
	/**
	 * The <CODE>TransformerFactory</CODE> instance used to create XSL
	 * transformation instances.
	 * @since	TFP 1.6
	 */
	private static TransformerFactory	factory
		= TransformerFactory.newInstance ();

	/**
	 * The underlying <CODE>Transformer</CODE> that will be applied to
	 * a DOM <CODE>Document</CODE> instance.
	 * @since	TFP 1.6 
	 */
	private Transformer		transformer	= null;
}