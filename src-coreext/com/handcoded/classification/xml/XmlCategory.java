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

package com.handcoded.classification.xml;

import com.handcoded.classification.Category;
import com.handcoded.classification.ClassificationScheme;

/**
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
final class XmlCategory extends Category
{
	/**
	 * Construct an <CODE>XmlCategory</CODE> with a given name.
	 * 
	 * @param	classification	The owning <CODE>Classification</CODE>.
	 * @param 	name			The name of the <CODE>Category</CODE>.
	 * @param	expression		The internal representation of the expression.
	 * @since	TFP 1.6
	 */
	public XmlCategory (ClassificationScheme classification, final String name, final boolean concrete, final ExprNode expression)
	{
		super (classification, name, concrete);
		
		this.expression = expression;
	}
	
	/**
	 * Construct an <CODE>RefinableCategory</CODE> that is a sub-classification
	 * of other <CODE>Category</CODE> instances.
	 * 
	 * @param	classification	The owning <CODE>Classification</CODE>.
	 * @param 	name			The name of the <CODE>Category</CODE>.
	 * @param 	parents			The parent <CODE>Category</CODE> instances.
	 * @param	expression		The internal representation of the expression.
	 * @since	TFP 1.6
	 */
	public XmlCategory (ClassificationScheme classification, final String name, final boolean concrete, Category [] parents, final ExprNode expression)
	{
		super (classification, name, concrete, parents);

		this.expression = expression;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	protected boolean isApplicable (final Object value)
	{
		return ((expression == null) || expression.evaluate (value));
	}
	
	/**
	 * Unique serialization identifier.
	 * @since	TFP 1.11
	 */
	private static final long serialVersionUID = -205810885704201493L;

	/**
	 * The underlying expression used to differentiate.
	 * @since	TFP 1.6
	 */
	private final ExprNode	expression;
}