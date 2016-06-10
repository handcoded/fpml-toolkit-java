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

package com.handcoded.classification.xml;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;

import com.handcoded.xml.namespace.DynamicNamespaceContext;

/**
 * The <CODE>XPathNode</CODE> class implements an <CODE>ExprNode</CODE> that
 * evaluates an XPath expression against a DOM tree to determine a matching
 * classification has been found.
 * 
 * @author 	BitWise
 * @version	$Id: XPathNode.java 9 2011-10-19 20:52:20Z andrew $
 * @since	TFP 1.6
 */
final class XPathNode extends ExprNode
{
	/**
	 * Constructs an <CODE>XPathNode</CODE> that will execute the indicated
	 * XPath expression on test <CODE>Element</CODE> instances. The path should
	 * use the prefix 'dyn' for elements that will be dynamically associated
	 * with a namespace derived from the context.
	 * 
	 * @param 	test			The XPath expression
	 * @since	TFP 1.6
	 */
	public XPathNode (final String test)
	{
		this.test = test;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	public boolean evaluate (final Object context)
	{
		synchronized (xpath) {
			try {
				if (((Element) context).getNamespaceURI () != null)
					xpath.setNamespaceContext (new DynamicNamespaceContext ((Element) context));
				
				Object result = xpath.evaluate (test, context, XPathConstants.BOOLEAN);
				return (((Boolean) result).booleanValue ());
			}
			catch (XPathExpressionException error) {
				logger.log (Level.SEVERE, "Failed to evaluate XPath (" + test + ")", error);
			}
			return (false);
		}
	}
		
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.6
	 */
	private static Logger	logger
		= Logger.getLogger ("com.handcoded.classification.xml.XPathNode");

	/**
	 * The <CODE>XPathFactory</CODE> used to create <CODE>XPath</CODE> instances.
	 * @since	TFP 1.6
	 */
	private static XPathFactory	factory = XPathFactory.newInstance ();
	
	/**
	 * The XPath expression that will be evaluated against the context element.
	 * @since	TFP 1.6
	 */
	private final String		test;
	
	/**
	 * The <CODE>XPath</CODE> instance used to evaluate the expression.
	 * One instance is shared by all executing threads and is locked before
	 * use.
	 * @since	TFP 1.6
	 */
	private XPath				xpath	= factory.newXPath ();
}