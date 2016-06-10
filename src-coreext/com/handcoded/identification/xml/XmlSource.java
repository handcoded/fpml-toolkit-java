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

package com.handcoded.identification.xml;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;

import com.handcoded.identification.Source;
import com.handcoded.xml.namespace.DynamicNamespaceContext;

/**
 * An instance of the <CODE>XmlSource</CODE> instance can be used to extract
 * the string value of an XPath expression from an DOM tree.
 * 
 * @author	BitWise
 * @version	$Id: XmlSource.java 11 2011-11-04 23:16:14Z andrew $
 * @since	TFP 1.6
 */
final class XmlSource implements Source
{
	/**
	 * Constructs a <CODE>XmlSource</CODE> for the indicated XPath expression.
	 * 
	 * @param 	expr			The XPath expression
	 * @since	TFP 1.6
	 */
	public XmlSource (final String expr)
	{
		this.expr = expr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public Object findSource (final Object context)
	{
		synchronized (xpath) {
			try {
				if (((Element) context).getNamespaceURI () != null)
					xpath.setNamespaceContext (new DynamicNamespaceContext ((Element) context));
				
				Object result = xpath.evaluate (expr, context);
				return (result);
			}
			catch (XPathExpressionException error) {
				logger.log (Level.SEVERE, "Failed to evaluate XPath (" + expr + ")", error);
			}
			return (null);
		}
	}

	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.6
	 */
	private static Logger	logger
		= Logger.getLogger ("com.handcoded.identification.xml.XmlSource");

	/**
	 * The <CODE>XPathFactory</CODE> used to create <CODE>XPath</CODE> instances.
	 * @since	TFP 1.6
	 */
	private static XPathFactory	factory = XPathFactory.newInstance ();
	
	/**
	 * The XPath expression
	 * @since	TFP 1.6
	 */
	private final String	expr;
	
	/**
	 * The <CODE>XPath</CODE> instance used to evaluate the expression.
	 * One instance is shared by all executing threads and is locked before
	 * use.
	 * @since	TFP 1.6
	 */
	private XPath			xpath	= factory.newXPath ();
}