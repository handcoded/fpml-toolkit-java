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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The <CODE>Category</CODE> class represents a possible classification of an
 * <CODE>Object</CODE>. <CODE>Category</CODE> instances can be linked to each
 * other to create graphs of interrelated items, such as multiple inheritance
 * based structures.
 *
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public abstract class Category implements Serializable
{
	/**
	 * Returns the name of this <CODE>Category</CODE>.
	 *
	 * @return	The <CODE>Category</CODE> name string.
	 * @since	TFP 1.6
	 */
	public final String getName ()
	{
		return (name);
	}
	
	/**
	 * Indicates if the <CODE>Category</CODE> is concrete.
	 * 
	 * @return	A <CODE>boolean</CODE> value.
	 * @since	TFP 1.6
	 */
	public final boolean isConcrete ()
	{
		return (concrete);
	}
	
	/**
	 * Indicates if the <CODE>Category</CODE> is abstract.
	 * 
	 * @return	A <CODE>boolean</CODE> value.
	 * @since	TFP 1.6
	 */
	public final boolean isAbstract ()
	{
		return (!concrete);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	public final String toString ()
	{
		return (name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	public final int hashCode ()
	{
		return (name.hashCode ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.10
	 */
	@Override
	public boolean equals (Object other)
	{
		return ((other instanceof Category) && equals ((Category) other));
	}
	
	/**
	 * Determines if this <CODE>Category</CODE> has the same name as another.
	 * 
	 * @param	other			The <CODE>Category</CODE> to compare with.
	 * @return	The result of the comparison.
	 * @since	TFP 1.10
	 */
	public boolean equals (Category other)
	{
		return ((other != null) && name.equals (other.name));
	}

	/**
	 * Returns an <CODE>Iterator</CODE> of super-categories.
	 *
	 * @return	An <CODE>Iterator</CODE> of super-categories.
	 * @since	TFP 1.10
	 */
	public final Iterator<Category> getSuperCategories ()
	{
		return (superCategories.iterator ());
	}

	/**
	 * Returns an <CODE>Iterator</CODE> of sub-categories.
	 *
	 * @return	An <CODE>Iterator</CODE> of sub-categories.
	 * @since	TFP 1.10
	 */
	public final Iterator<Category> getSubCategories ()
	{
		return (subCategories.iterator ());
	}

	/**
	 * Determines if this <CODE>Category</CODE> is the same as or is a
	 * sub-category of another <CODE>Category</CODE> (e.g. a Swaption
	 * is-a Option).
	 *
	 * @param 	superCategory		The super category.
	 * @return	<CODE>true</CODE> if there is a 'is-a' relationship
	 * 			between the two categories.
	 */
	public final boolean isA (final Category superCategory)
	{
		if (this == superCategory) return (true);

		for (Category parent : superCategories)
			if (parent.isA (superCategory)) return (true);
		
		return (false);
	}

	/**
	 * Determine if the given <CODE>Object</CODE> can be classified by the
	 * graph of <CODE>Category</CODE> instances related to this entry point.
	 *
	 * @param 	value			The <CODE>Object</CODE> to be classified.
	 * @return 	The matching <CODE>Category</CODE> for the <CODE>Object</CODE>
	 * 			or <CODE>null</CODE> if none could be determined.
	 * @since	TFP 1.0
	 */
	public final Category classify (final Object value)
	{
		return (classify (value, new HashSet<Category> ()));
	}

	/**
	 * Construct a <CODE>Category</CODE> with a given name.
	 *
	 * @param	scheme			The owning <CODE>ClassificationScheme</CODE>.
	 * @param 	name			The name of the <CODE>Category</CODE>.
	 * @param	concrete		Indicates if the category is concrete.
	 * @since	TFP 1.0
	 */
	protected Category (ClassificationScheme scheme, final String name, final boolean concrete)
	{
		this.name = name;
		this.concrete = concrete;
		
		scheme.add (this);
	}

	/**
	 * Construct a <CODE>Category</CODE> with a given name.
	 *
	 * @param	scheme			The owning <CODE>ClassificationScheme</CODE>.
	 * @param 	name			The name of the <CODE>Category</CODE>.
	 * @param	concrete		Indicates if the category is concrete.
	 * @param 	parent			The parent <CODE>Category</CODE>.
	 * @since	TFP 1.0
	 */
	protected Category (ClassificationScheme scheme, final String name, final boolean concrete, Category parent)
	{
		this (scheme, name, concrete);

		this.superCategories.add (parent);
		parent.subCategories.add (this);
	}

	/**
	 * Construct a <CODE>Category</CODE> with a given name.
	 *
	 * @param	scheme			The owning <CODE>ClassificationScheme</CODE>.
	 * @param 	name			The name of the <CODE>Category</CODE>.
	 * @param	concrete		Indicates if the category is concrete.
	 * @param 	parents			The parent <CODE>Category</CODE> instances.
	 * @since	TFP 1.0
	 */
	protected Category (ClassificationScheme scheme, final String name, final boolean concrete, Category [] parents)
	{
		this (scheme, name, concrete);

		for (int index = 0; index < parents.length; ++index) {
			this.superCategories.add (parents [index]);
			parents [index].subCategories.add (this);
		}
	}
	
	/**
	 * Determine if the given <CODE>Object</CODE> can be classified by the
	 * graph of <CODE>Category</CODE> instances related to this entry point.
	 * 
	 * @param 	value			The <CODE>Object</CODE> to be classified.		
	 * @param 	visited			A <CODE>HashSet</CODE> used to track visited nodes.
	 * @return	The matching <CODE>Category</CODE> for the <CODE>Object</CODE>
	 * 			or <CODE>null</CODE> if none could be determined.
	 * @throws	AmbiguousClassification If the <CODE>Object</CODE> matches more
	 * 			than one possible <CODE>Category</CODE>.
	 * @since	TFP 1.0
	 */
	protected Category classify (final Object value, HashSet<Category> visited) // NOSONAR
	{
		Category		result	= null;

		visited.add (this);
		if (isApplicable (value)) {
			for (Category category : subCategories) {
				Category			match;

				if (!visited.contains (category) && (match = category.classify (value, visited)) != null) {
					if ((result != null) && (result != match)) {
						if (result.isA (match)) continue;
			
						throw new AmbiguousClassification (result, match);
					}
					result = match;
				}				
			}
						
			if (concrete && (result == null)) result = this;
		}
		return (result);
	}

	/**
	 * Determines if this <CODE>Category</CODE> (and its sub-categories)
	 * is applicable to the given <CODE>Object</CODE>.
	 *
	 * @param 	value			The <CODE>Object</CODE> to be tested.
	 * @return	<CODE>true</CODE> if the <CODE>Object</CODE> is applicable,
	 * 			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.6
	 */
	protected abstract boolean isApplicable (final Object value);

	/**
	 * The unique serialization identifier.
	 * @since	TFP 1.10
	 */
	private static final long serialVersionUID = 7471433781817994680L;

	/**
	 * The name of this <CODE>Category</CODE>.
	 *
	 * @since	TFP 1.0
	 */
	private final String	name;
	
	/**
	 * Indicates if the class is concrete.
	 * @since	TFP 1.6
	 */
	private final boolean	concrete;
	
	/**
	 * <CODE>Category</CODE> instances that reference this instance.
	 *
	 * @since	TFP 1.0
	 */
	private ArrayList<Category> superCategories = new ArrayList<> ();

	/**
	 * <CODE>Category</CODE> instances referenced by this instance.
	 *
	 * @since	TFP 1.0
	 */
	private ArrayList<Category> subCategories	= new ArrayList<> ();
}