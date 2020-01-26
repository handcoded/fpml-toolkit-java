// Copyright (C),2006-2020 HandCoded Software Ltd.
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

package com.handcoded.classification;

/**
 * Instances of the <CODE>AmbiguousClassification</CODE> exception are thrown
 * when an <CODE>Object</CODE> matches two unrelated concrete categories.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.10
 */
public class AmbiguousClassification extends RuntimeException
{
	/**
	 * Constructs an <CODE>AmbiguousClassification</CODE> exception
	 * @param	first			The first matching <CODE>Category</CODE>.
	 * @param	second			The second matching <CODE>Category</CODE>.
	 * @since	TFP 1.10
	 */
	public AmbiguousClassification (Category first, Category second)
	{
		super ("Object cannot be unambiguously classified (" + first+ " & " + second + ")");
		
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Provides access to the first matching <CODE>Category</CODE>.
	 * @return The first matching <CODE>Category</CODE>.
	 * @since	TFP 1.10
	 */
	public final Category getFirst ()
	{
		return (first);
	}
	
	/**
	 * Provides access to the second matching <CODE>Category</CODE>.
	 * @return The second matching <CODE>Category</CODE>.
	 * @since	TFP 1.10
	 */
	public final Category getSecond ()
	{
		return (second);
	}
	
	/**
	 * The unique serialization identifier.
	 * @since	TFP 1.10
	 */
	private static final long serialVersionUID = -2927331385485814358L;

	/**
	 * The first matching <CODE>Category</CODE>.
	 * @since	TFP 1.10
	 */
	private final Category	first;
	
	/**
	 * The second matching <CODE>Category</CODE>.
	 * @since	TFP 1.10
	 */
	private final Category	second;
}