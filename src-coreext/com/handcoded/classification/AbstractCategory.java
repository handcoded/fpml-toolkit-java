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

/**
 * An <CODE>AbstractCategory</CODE> is used to relate a set of sub-category
 * instances.
 *
 * @author	BitWise
 * @version	$Id: AbstractCategory.java 17 2011-11-16 00:30:12Z andrew $
 * @since	TFP 1.0
 */
public class AbstractCategory extends Category
{
	/**
	 * Construct an <CODE>AbstractCategory</CODE> with a given name.
	 * 
	 * @param	scheme			The owning <CODE>ClassificationScheme</CODE>.
	 * @param 	name			The name of the <CODE>Category</CODE>.
	 * @since	TFP 1.0
	 */
	public AbstractCategory (ClassificationScheme scheme, final String name)
	{
		super (scheme, name, false);
	}
	
	/**
	 * Construct an <CODE>AbstractCategory</CODE> that is a sub-classification
	 * of another <CODE>Category</CODE>.
	 * 
	 * @param	scheme			The owning <CODE>ClassificationScheme</CODE>.
	 * @param 	name			The name of the <CODE>Category</CODE>.
	 * @param 	parent			The parent <CODE>Category</CODE>.
	 * @since	TFP 1.0
	 */
	public AbstractCategory (ClassificationScheme scheme, final String name, Category parent)
	{
		super (scheme, name, false, parent);
	}
	
	/**
	 * Construct an <CODE>AbstractCategory</CODE> that is a sub-classification
	 * of other <CODE>Category</CODE> instances.
	 * 
	 * @param	scheme			The owning <CODE>ClassificationScheme</CODE>.
	 * @param	name			The name of the <CODE>Category</CODE>.
	 * @param 	parents			The parent <CODE>Category</CODE> instances.
	 * @since	TFP 1.0
	 */
	public AbstractCategory (ClassificationScheme scheme, final String name, Category [] parents)
	{
		super (scheme, name, false, parents);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected boolean isApplicable (final Object value)
	{
		return (true);
	}
}