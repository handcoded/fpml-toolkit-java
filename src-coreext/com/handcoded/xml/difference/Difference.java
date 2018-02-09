// Copyright (C),2005-2018 HandCoded Software Ltd.
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

package com.handcoded.xml.difference;

import org.w3c.dom.Node;

/**
 * Instances of the <CODE>Difference</CODE> class record differences discovered
 * between the constituents of two XML documents.
 * 
 * @author	BitWise
 * @since	TFP 1.9
 */
public final class Difference
{
	/**
	 * Returns the <CODE>NODE</CODE> in the left-hand document where a
	 * difference was found or <CODE>null</CODE> if there is no matching node.
	 * 
	 * @return	The left-hand <CODE>Node</CODE> instance or <CODE>null</CODE>.
	 * @since	TFP 1.9
	 */
	public Node getLhs ()
	{
		return (lhs);
	}
	
	/**
	 * Returns the <CODE>NODE</CODE> in the right-hand document where a
	 * difference was found or <CODE>null</CODE> if there is no matching node.
	 * 
	 * @return	The right-hand <CODE>Node</CODE> instance or <CODE>null</CODE>.
	 * @since	TFP 1.9
	 */
	public Node getRhs ()
	{
		return (rhs);
	}
	
	public double getScore ()
	{
		return (score);
	}
	
	/**
	 * Constructs a <CODE>Difference</CODE> instance from the provided argument
	 * values.
	 * 
	 * @param	lhs				The left-hand <CODE>Node</CODE> where the difference lies or <CODE>null</CODE>.
	 * @param	rhs				The right-hand <CODE>Node</CODE> where the difference lies or <CODE>null</CODE>.
	 * @param	score			The weighted score assigned to this difference. 
	 * @since	TFP 1.9
	 */
	protected Difference (Node lhs, Node rhs, double score)
	{
		this.lhs = lhs;
		this.rhs = rhs;
		this.score = score;
	}
	
	/**
	 * 
	 */
	private	final Node	lhs;
	
	private final Node	rhs;
	
	private final double score;
}