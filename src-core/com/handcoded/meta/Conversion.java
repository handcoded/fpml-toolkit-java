// Copyright (C),2005-2011 HandCoded Software Ltd.
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

package com.handcoded.meta;

import java.util.Enumeration;
import java.util.Stack;

import org.w3c.dom.Document;

/**
 * A <CODE>Conversion</CODE> instance encapsulates the knowledge of how to
 * transform an XML document from one <CODE>Release</CODE> of a <CODE>Specification
 * </CODE> to another.
 * <P>
 * It is expected that most conversions will be between different releases of
 * the same specification but the code allows for the discovery of
 * inter-specification conversions.
 * 
 * @author 	BitWise
 * @version	$Id: Conversion.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class Conversion
{
	/**
	 * Attempts to find a <CODE>Conversion</CODE> that will transform a
	 * <CODE>Document</CODE> between the two specified releases. The releases
	 * must be different, null transformations are not allowed.
	 * 
	 * @param	source			The source <CODE>Release</CODE> to convert from.
	 * @param 	target			The target <CODE>Release</CODE> to convert to.
	 * @return	A <CODE>Conversion</CODE> instance that implements the
	 * 			transformation or <CODE>null</CODE> if one could not be found.
	 * @since	TFP 1.0
	 */
	public static Conversion conversionFor (Release source, Release target)
	{
		if (source != target)
			return (depthFirstSearch (source, target, new Stack<Release> ()));
	
		return (null);
	}
	
	/**
	 * Returns the <CODE>Release</CODE> that a <CODE>Conversion</CODE>
	 * converts from.
	 * 
	 * @return	The <CODE>Release</CODE> to convert from.
	 * @since	TFP 1.0
	 */
	public abstract Release getSourceRelease ();
	
	/**
	 * Returns the <CODE>Release</CODE> that a <CODE>Conversion</CODE>
	 * converts to.
	 * 
	 * @return	The <CODE>Release</CODE> to convert to.
	 * @since	TFP 1.0
	 */
	public abstract Release getTargetRelease ();
	
	/**
	 * Applies the <CODE>Conversion</CODE> to a <CODE>Document</CODE> 
	 * instance to create a new <CODE>Document</CODE>.
	 * 
	 * @param 	document		The <CODE>Document</CODE> to be converted.
	 * @param	helper			A <CODE>Helper</CODE> instance to assit conversion
	 * @return	A new <CODE>Document</CODE> containing the transformed
	 * 			data.
	 * @throws	ConversionException If a problem occurs during the conversion.
	 * @since	TFP 1.0 
	 */
	public abstract Document convert (Document document, Helper helper)
		throws ConversionException;
	
	/**
	 * Constructs a <CODE>Conversion</CODE> instance.
	 * @since	TFP 1.0
	 */
	protected Conversion ()
	{ }
	
	/**
	 * Returns a count of the number of stages in the conversion. This
	 * is used to pick the shorter of two possible conversion paths when
	 * searching the release graph.
	 * 
	 * @return	A value indicating the complexity of a <CODE>conversion</CODE>.
	 * @since	TFP 1.0
	 */
	protected abstract int complexity ();
	
	/**
	 * Recursively explores the <CODE>Release</CODE> definitions to determine
	 * the shortest conversion path between two releases.
	 * 
	 * @param 	source			The source <CODE>Release</CODE> for the search.
	 * @param 	target			The target <CODE>Release</CODE> for the search.
	 * @param 	stack			A <CODE>Stack</CODE> used to detect cycles.
	 * @return	A <CODE>Conversion</CODE> that transforms between the source
	 * 			and target releases or <CODE>null</CODE> if no conversion is
	 * 			possible.
	 * @since	TFP 1.0
	 */
	private static Conversion depthFirstSearch (Release source, Release target,
			Stack<Release> stack)
	{
		Conversion		best   = null;
		
		if (!stack.contains (source)) {
			stack.push (source);
			
			Enumeration<Conversion> targets = source.getSourceConversions ();
			while (targets.hasMoreElements ()) {
				Conversion first   = targets.nextElement ();
				Release    release = first.getTargetRelease ();
				Conversion result  = null;
				
				if (release == target)
					result = first;
				else {
					Conversion second = depthFirstSearch (release, target, stack);
					if (second != null)
						result = new IndirectConversion (first, second);
				}
				
				if (result != null) {
					if ((best == null)||(result.complexity () < best.complexity ()))
							best = result;
				}
			}
			
			stack.pop ();
		}
		return (best);
	}
}