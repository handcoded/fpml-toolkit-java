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

package com.handcoded.classification;

import java.util.Hashtable;

/**
 * A <CODE>ClassificationScheme</CODE> instance provides a container for a set of
 * related <CODE>Category</CODE> instances.
 * 
 * @author 	BitWise
 * @version	$Id: ClassificationScheme.java 8 2011-10-14 21:55:03Z andrew $
 * @since	TFP 1.6
 */
public final class ClassificationScheme
{
	/**
	 * Constructs an empty <CODE>Classification</CODE> instance.
	 * @since	TFP 1.6
	 */
	public ClassificationScheme ()
	{ }
	
	/**
	 * Locates a <CODE>Category</CODE> within the <CODE>Classification</CODE>
	 * using its name string.
	 * 
	 * @param 	name			The target name string.
	 * @return	The matching <CODE>Category</CODE> instance or <CODE>null</CODE>
	 * 			if no match was found.
	 * @since	TFP 1.6
	 */
	public final Category getCategoryByName (final String name)
	{
		return (extent.get (name));
	}
	
	/**
	 * Adds a <CODE>Category</CODE> instance to the extent set.
	 * 
	 * @param 	category		The <CODE>Category</CODE> instance to add.
	 * @since	TFP 1.6
	 */
	protected void add (Category category)
	{
		extent.put (category.getName (), category);
	}
	
	/**
	 * The underlying hash table of categories indexed by name.
	 * @since	TFP 1.6
	 */
	private Hashtable<String, Category>	extent
		= new Hashtable<String, Category> ();
}