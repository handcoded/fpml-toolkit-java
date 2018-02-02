// Copyright (C),2005-2017 HandCoded Software Ltd.
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

package com.handcoded.view;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.handcoded.xml.XPath;

/**
 * Instances of the <CODE>Value</CODE> class hold the results of XPath
 * evaluation against the input document.
 * 
 * @author	Bitwise
 * @since	TFP 1.9
 */
public final class Value
{
	/**
	 * Constructs a <CODE>Value</CODE> instance.
	 * 
	 * @param	object			The result object from the XPath.
	 * @since	1.9
	 */
	public Value (Object object, QName type)
	{
		this.object = object;
		this.type = type;
	}
	
	/**
	 * Provides access to the raw object.
	 * 
	 * @return	The underlying <CODE>Object</CODE> value.
	 * @since	TFP 1.9
	 */
	public Object getObject ()
	{
		return (object);
	}
	
	/**
	 * Returns the value expressed as <CODE>boolean</CODE>. Non-boolean
	 * results are converted as per the XPath 2.0 rules.
	 * 
	 * @return	The value expressed as a <CODE>boolean</CODE>.
	 * @since	TFP 1.9
	 */
	public boolean toBoolean ()
	{
		return (toBoolean (object));
	}
	
	/**
	 * Returns an object expressed as <CODE>boolean</CODE>. Non-boolean
	 * results are converted as per the XPath 2.0 rules.
	 * 
	 * @param	object		The <CODE>Object</CODE> to be converted. 
	 * @return	The value expressed as a <CODE>boolean</CODE>.
	 * @since	TFP 1.9
	 */
	public static boolean toBoolean (Object object)
	{
		if (object != null) {
			if (object instanceof Boolean)
				return (((Boolean) object).booleanValue ());
			
			if (object instanceof Double) {
				Double		value = (Double) object;
				
				return (!value.isNaN() && (value.doubleValue () != 0.0));
			}
			
			if (object instanceof String)
				return (((String) object).length() != 0);
			
			if (object instanceof NodeList)
				return (((NodeList) object).getLength () != 0);
		}
		return (false);
	}
	
	/**
	 * Returns the value expressed as <CODE>double</CODE>. Non-numeric
	 * results are converted as per the XPath 2.0 rules.
	 * 
	 * @param	object		The <CODE>Object</CODE> to be converted. 
	 * @return	The value expressed as a <CODE>double</CODE>.
	 * @since	TFP 1.9
	 */
	public double toNumber ()
	{
		return (toNumber (object));
	}
	
	/**
	 * Returns an object expressed as <CODE>double</CODE>. Non-numeric
	 * results are converted as per the XPath 2.0 rules.
	 * 
	 * @return	The value expressed as a <CODE>double</CODE>.
	 * @since	TFP 1.9
	 */
	public static double toNumber (Object object)
	{
		if (object != null) {
			if (object instanceof Boolean)
				return (((Boolean) object).booleanValue () ? 1.0 : 0.0);
			
			if (object instanceof Double)
				return (((Double) object).doubleValue ());
			
			if (object instanceof String) {
				try {
					return (Double.parseDouble((String) object));
				}
				catch (NumberFormatException error) {
					;
				}
				return (0.0);
			}
			
			if (object instanceof NodeList)
				return (0.0);
		}
		return (0.0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.9
	 */
	@Override
	public String toString ()
	{
		return (toString (object));
	}
	
	/**
	 * Returns an object expressed as <CODE>String</CODE>. Non-numeric
	 * results are converted as per the XPath 2.0 rules.
	 * 
	 * @param	object		The <CODE>Object</CODE> to be converted. 
	 * @return	The value expressed as a <CODE>double</CODE>.
	 * @since	TFP 1.9
	 */
	public static String toString (Object object)
	{
		if (object != null) {
			if (object instanceof String)
				return ((String) object);
		
			return (object.getClass ().getName () + ":" + object.toString( ));
		}
		return ("NULL");
	}
	
	/**
	 * Returns a string containing debugging information.
	 *  
	 * @return	Debugging information.
	 * @since	TFP 1.9
	 */
	public String toDebug ()
	{
		if (object != null) {
			StringBuffer	buffer = new StringBuffer ();
			
			if (type == XPathConstants.NODE) {
				buffer.append("NODE: " + XPath.forNode ((Node) object));
				
				return (buffer.toString ());
			}
			
			if (type == XPathConstants.NODESET) {
				NodeList 		list = (NodeList) object;
				buffer.append ("NODESET: { ");
				for (int index = 0; index < list.getLength(); ++index) {
					if (index != 0) buffer.append(", ");
					buffer.append (XPath.forNode (list.item (index)));
				}
				buffer.append(" }");
				
				return (buffer.toString ());
			}
			if (type == XPathConstants.STRING) {
				return ("STRING: '" + (String) object + "'");
			}
			if (type == XPathConstants.NUMBER) {
				return ("NUMBER: " + (Double) object);
			}
			if (type == XPathConstants.BOOLEAN) {
				return ("BOOLEAN: " + (Boolean) object);
			}
			
			return (type + " : " + object);
		}
		return ("NULL");
	}
			
	/**
	 * The raw result returned by the XPath evaluation.
	 * @since	TFP 1.9
	 */
	private final Object	object;
	
	/**
	 * The type associated with the result.
	 * @since	TFP 1.9
	 */
	private final QName		type;
}