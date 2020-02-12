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

package com.handcoded.xpath;

import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;

/**
 * Instances of the <CODE>Function</CODE> represent user defined functions
 * used in a data view XPath expressions.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.9
 */
public final class Function
{
	/**
	 * Constructs a <CODE>Function</CODE> instance with the given name and
	 * class name strings then attempts to load the associated Java class and
	 * create an instance.
	 * 
	 * @param	name		The name of function used in a XPath.
	 * @param	className	The name of the implementing Java class.
	 * @since	TFP 1.9
	 */
	public Function (QName name, String className)
	{
		this.name = name;
		this.className = className;
		
		try {
			instance = (XPathFunction) Class.forName (className).newInstance();
		}
		catch (Exception error) {
			logger.severe("Could not dynamically created an instance of " + className);
		}
	}
	
	/**
	 * Provides access to the function name.
	 * 
	 * @return	The function name.
	 * @since	TFP 1.9
	 */
	public QName getName ()
	{
		return (name);
	}
	
	/**
	 * Provides access to the class name.
	 * 
	 * @return	The Java class name.
	 * @since	TFP 1.9
	 */
	public String getClassName ()
	{
		return (className);
	}
	
	/**
	 * Returns the <CODE>XPathFunction</CODE> instance created for this
	 * <CODE>Function</CODE> object.
	 * 
	 * @return	The <CODE>XPathFunction</CODE> instance.
	 * @since	TFP 1.9
	 */
	public XPathFunction getInstance ()
	{
		return (instance);
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.9
	 */
	private static Logger	logger
		= Logger.getLogger ("com.handcoded.view.Function");

	/**
	 * The name associated with this XPath function.
	 * @since	TFP 1.9
	 */
	private final QName 	name;
	
	/**
	 * The name of the class that implements the function.
	 * @since	TFP 1.9
	 */
	private final String	className;
	
	/**
	 * An instance of the target class or <CODE>null</CODE> if it could not
	 * be created.
	 * @since	TFP 1.9
	 */
	private XPathFunction 	instance = null;
}