// Copyright (C),2006-2020
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
		return ((expression != null) ? expression.evaluate (value) : true);
	}
	
	/**
	 * The underlying expression used to differentiate.
	 * @since	TFP 1.6
	 */
	private final ExprNode	expression;
}