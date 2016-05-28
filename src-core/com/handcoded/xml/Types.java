// Copyright (C),2005-2015 HandCoded Software Ltd.
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

package com.handcoded.xml;

import java.math.BigDecimal;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.handcoded.finance.Date;
import com.handcoded.finance.DateTime;
import com.handcoded.finance.Time;

/**
 * The <CODE>Types</CODE> class contains a set of functions for extracting
 * Java type values from XML <CODE>Element</CODE> content text strings.
 * <P>
 * Much of this code has been refactored from the <CODE>Logic</CODE> class.
 * 
 * @author 	BitWise
 * @version	$Id: Types.java 812 2015-06-10 15:38:09Z andrew_jacobs $
 * @since	TFP 1.1
 */
public abstract class Types
{
	/**
	 * Returns the value of the given <CODE>Node</CODE> as a string.
	 *
	 * @param 	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.0
	 */
	public static String toString (final Node node)
	{
		return (DOM.getInnerText ((Element) node));
	}

	/**
	 * Returns the value of the given <CODE>Node</CODE> as a token string
	 * with all extra white space removed.
	 *
	 * @param 	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.2
	 */
	public static String toToken (final Node node)
	{
		if (node != null) {
			if (node.getNodeType () == Node.ELEMENT_NODE)
				return (DOM.getInnerText ((Element) node).trim ());
			if (node.getNodeType () == Node.ATTRIBUTE_NODE)
				return (((Attr) node).getValue ());
		}
		return (null);
	}

	/**
	 * Returns the value of the given <CODE>Node</CODE> as an boolean.
	 * 
	 * @param 	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.0
	 */
	public static boolean toBool (final Node node)
	{
		if (node != null) {
			String text = toToken (node);
			
			if (text.equalsIgnoreCase("true") || text.equals ("1"))
				return (true); 
		}
		return (false);
	}
	
	/**
	 * Returns the value of the given <CODE>Node</CODE> as an integer.
	 * 
	 * @param 	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.0
	 */
	public static int toInteger (final Node node)
	{
		if (node != null) {
			try {
				return (Integer.parseInt (toToken (node)));
			}
			catch (Exception error) {
				;
			}
		}
		return (0);
	}

	/**
	 * Returns the value of the given <CODE>Node</CODE> as an double.
	 * 
	 * @param 	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.0
	 */
	public static double toDouble (final Node node)
	{
		if (node != null) {
			try {
				return (Double.parseDouble (toToken (node)));
			}
			catch (Exception error) {
				;
			}
		}
		return (0.0);
	}

	/**
	 * Returns the value of the given <CODE>Node</CODE> as a decimal.
	 * 
	 * @param	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.0
	 */
	public static BigDecimal toDecimal (final Node node)
	{
		if (node != null) {
			try {
	            return (new BigDecimal (toToken (node)));
			}
			catch (Exception error) {
				;
			}
		}
		return (null);
	}
	
	/**
	 * Returns the value of the given <CODE>Node</CODE> as an <CODE>Date</CODE>.
	 * 
	 * @param 	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.1
	 */
	public static Date toDate (final Node node)
	{
		if (node != null) {
			try {
				return (Date.parse (toToken (node)));
			}
			catch (Exception error) {
				;
			}
		}
		return (null);
	}

	/**
	 * Returns the value of the given <CODE>Node</CODE> as an <CODE>DateTime</CODE>.
	 * 
	 * @param 	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.1
	 */
	public static DateTime toDateTime (final Node node)
	{
		if (node != null) {
			try {
				return (DateTime.parse (toToken (node)));
			}
			catch (Exception error) {
				;
			}
		}
		return (null);
	}

	/**
	 * Returns the value of the given <CODE>Node</CODE> as an <CODE>Time</CODE>.
	 * 
	 * @param 	node			The <CODE>Node</CODE> containing the value.
	 * @return	The value of the node as a Java datatype.
	 * @since	TFP 1.1
	 */
	public static Time toTime (final Node node)
	{
		if (node != null) {
			try {
				return (Time.parse (toToken (node)));
			}
			catch (Exception error) {
				;
			}
		}
		return (null);
	}

	/**
	 * Rounds a monetary decimal value to a given number of places. 
	 *  
	 * @param 	value			The <CODE>BigDecimal</CODE> to round.
	 * @param	places			The number of places required.
	 * @return	The rounded value.
	 * @since	TFP 1.0
	 */
	public static BigDecimal round (final BigDecimal value, int places)
	{
		return (new BigDecimal (value.movePointRight (places).toBigInteger ()).movePointLeft (places));
	}
	
	/**
	 * Converts the value of the indicated <CODE>Node</CODE> into a <CODE>BigDecimal
	 * </CODE> value and then determines if it is positive.
	 * 
	 * @param 	node			The <CODE>Node</CODE> to be tested.
	 * @return	<CODE>true</CODE> if the value is positive, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.2
	 */
	public static boolean isPositive (final Node node)
	{
		return (isPositive (toDecimal (node)));
	}
	
	/**
	 * Determines if the supplied <CODE>BigDecimal</CODE> value is positive.
	 * 
	 * @param	value		The <CODE>BigDecimal</CODE> to test.
	 * @return	<CODE>true</CODE> if the value is positive, <CODE>false</CODE>
	 * 			otherwise
	 * @since	TFP 1.2
	 */
	public static boolean isPositive (final BigDecimal value)
	{
		return (value.compareTo (BigDecimal.ZERO) > 0);
	}
	
	/**
	 * Converts the value of the indicated <CODE>Node</CODE> into a <CODE>BigDecimal
	 * </CODE> value and then determines if it is negative.
	 * 
	 * @param 	node			The <CODE>Node</CODE> to be tested.
	 * @return	<CODE>true</CODE> if the value is negative, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.2
	 */
	public static boolean isNegative (final Node node)
	{
		return (isNegative (toDecimal (node)));
	}
	
	/**
	 * Determines if the supplied <CODE>BigDecimal</CODE> value is negative.
	 * 
	 * @param	value		The <CODE>BigDecimal</CODE> to test.
	 * @return	<CODE>true</CODE> if the value is negative, <CODE>false</CODE>
	 * 			otherwise
	 * @since	TFP 1.2
	 */
	public static boolean isNegative (final BigDecimal value)
	{
		return (value.compareTo (BigDecimal.ZERO) < 0);
	}
	
	/**
	 * Converts the value of the indicated <CODE>Node</CODE> into a <CODE>BigDecimal
	 * </CODE> value and then determines if it is non-negative.
	 * 
	 * @param 	node			The <CODE>Node</CODE> to be tested.
	 * @return	<CODE>true</CODE> if the value is non-negative, <CODE>false</CODE>
	 * 			otherwise.
	 * @since	TFP 1.2
	 */
	public static boolean isNonNegative (final Node node)
	{
		return (isNonNegative (toDecimal (node)));
	}
	
	/**
	 * Determines if the supplied <CODE>BigDecimal</CODE> value is non-negative.
	 * 
	 * @param	value		The <CODE>BigDecimal</CODE> to test.
	 * @return	<CODE>true</CODE> if the value is non-negative, <CODE>false</CODE>
	 * 			otherwise
	 * @since	TFP 1.2
	 */
	public static boolean isNonNegative (final BigDecimal value)
	{
		return (value.compareTo (BigDecimal.ZERO) >= 0);
	}
	
	/**
	 * Constructs a <CODE>Types</CODE> instance.
	 * @since	TFP 1.1
	 */
	protected Types ()
	{ }
}