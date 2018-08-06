// Copyright (C),2005-2012 HandCoded Software Ltd.
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

package com.handcoded.validation;

import java.util.Hashtable;

import com.handcoded.xml.NodeIndex;

/**
 * A <CODE>Precondition</CODE> instance is used to determine the applicability
 * of a <CODE>Rule</CODE> to a <CODE>Document</CODE>.
 * 
 * @author	BitWise
 * @version	$Id: Precondition.java 672 2012-10-30 21:56:09Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class Precondition
{
	/**
	 * A <CODE>Precondition</CODE> instance that always returns <CODE>true
	 * </CODE>.
	 * @since	TFP 1.0
	 */
	public static Precondition ALWAYS
		= new Precondition ()
			{
				/**
				 * {@inheritDoc}
				 * @since	TFP 1.0
				 */
				@Override
				public boolean evaluate (final NodeIndex nodeIndex, Hashtable<Precondition, Boolean> cache)
				{
					return (true);
				}
				
				/**
				 * Returns a <CODE>String</CODE> describing expression this <CODE>Precondition
				 * evaluates, in this case "true".
				 * 
				 * @return	The <CODE>Precondition</CODE> expression as a string.
				 * @since	TFP 1.0
				 */
				@Override
				public String toString ()
				{
					return ("true");
				}
			};

	/**
	 * A <CODE>Precondition</CODE> instance that always returns <CODE>false
	 * </CODE>.
	 * @since	TFP 1.0
	 */
	public static Precondition NEVER
		= new Precondition ()
			{
				/**
				 * {@inheritDoc}
				 * @since	TFP 1.0
				 */
			    @Override
				public boolean evaluate (final NodeIndex nodeIndex, Hashtable<Precondition, Boolean> cache)
				{
					return (true);
				}
				
				/**
				 * Returns a <CODE>String</CODE> describing expression this <CODE>Precondition
				 * evaluates, in this case "false".
				 * 
				 * @return	The <CODE>Precondition</CODE> expression as a string.
				 * @since	TFP 1.0
				 */
			    @Override
				public String toString ()
				{
					return ("false");
				}
			};

	/**
	 * Creates and returns a <CODE>Precondition</CODE> which will invert the
	 * given <CODE>Precondition</CODE> instance.
	 *  
	 * @param	pre			The underlying <CODE>Precondition</CODE>.
	 * @return	The constructed <CODE>Precondition</CODE> instance. 
	 * @since	TFP 1.0
	 */
	public static Precondition not (final Precondition pre)
	{
		return (new Not (pre));
	}

	/**
	 * Creates and returns a <CODE>Precondition</CODE> which will connect
	 * the indicated <CODE>Precondition</CODE> instances with a logical and.
	 * 
	 * @param	lhs			The left hand <CODE>Precondition</CODE>.
	 * @param	rhs			The right hand <CODE>Precondition</CODE>.
	 * @return	The constructed <CODE>Precondition</CODE> instance. 
	 * @since	TFP 1.0
	 */
	public static Precondition and (final Precondition lhs, final Precondition rhs)
	{
		return (new And (lhs, rhs));
	}
	
	/**
	 * Creates and returns a <CODE>Precondition</CODE> instance that will
	 * evaluate the logical AND of a list of two or more <CODE>Precondition</CODE>
	 * instances.
	 * 
	 * @param 	list		A list of two or more <CODE>Precondition</CODE> instances.
	 * @return	A <CODE>Precondition</CODE> instance that evaluates the
	 * 			logical AND of all the input <CODE>Precondition</CODE>
	 * 			instances.
	 * @since	TFP 1.7
	 */
	public static Precondition and (final Precondition [] list)
	{
		if (list.length < 2)
			throw new IllegalArgumentException (
					"A list of at least two Preconditions must be provided");
		
		Precondition 	result = new And (list [0], list [1]);
		
		for (int index = 2; index < list.length; ++index)
			result = new And (list [index], result);
		
		return (result);
	}
	
	/**
	 * Creates and returns a <CODE>Precondition</CODE> which will connect
	 * the indicated <CODE>Precondition</CODE> instances with a logical or.
	 * 
	 * @param	lhs			The left hand <CODE>Precondition</CODE>.
	 * @param	rhs			The right hand <CODE>Precondition</CODE>.
	 * @return	The constructed <CODE>Precondition</CODE> instance. 
	 * @since	TFP 1.0
	 */
	public static Precondition or (final Precondition lhs, final Precondition rhs)
	{
		return (new Or (lhs, rhs));
	}
	
	/**
	 * Creates and returns a <CODE>Precondition</CODE> instance that will
	 * evaluate the logical OR of a list of two or more <CODE>Precondition</CODE>
	 * instances.
	 * 
	 * @param 	list		A list of two or more <CODE>Precondition</CODE> instances.
	 * @return	A <CODE>Precondition</CODE> instance that evaluates the
	 * 			logical OR of all the input <CODE>Precondition</CODE>
	 * 			instances.
	 * @since	TFP 1.7
	 */
	public static Precondition or (final Precondition [] list)
	{
		if (list.length < 2)
			throw new IllegalArgumentException (
					"A list of at least two Preconditions must be provided");
		
		Precondition 	result = new Or (list [0], list [1]);
		
		for (int index = 2; index < list.length; ++index)
			result = new Or (list [index], result);
		
		return (result);
	}
	
	/**
	 * Evaluates this <CODE>Precondition</CODE> against the contents of the
	 * indicated <CODE>NodeIndex</CODE>.
	 * 
	 * @param 	nodeIndex		The <CODE>NodeIndex</CODE> of a <CODE>Document</CODE>.
	 * @param	cache			A <CODE>HashTable</CODE> of previous calculated precondition
	 * 							results.
	 * @return	A <CODE>boolean</CODE> value indicating the applicability of this
	 * 			<CODE>Precondition</CODE> to the <CODE>Document</CODE>.
	 * @since	TFP 1.0
	 */
	public abstract boolean evaluate (final NodeIndex nodeIndex, Hashtable<Precondition, Boolean> cache);

	/**
	 * The <CODE>BinaryPrecondition</CODE> class records the left and right
	 * hand side arguments for some binary logical operator.
	 * 
	 * @since	TFP 1.0
	 */
	protected static abstract class UnaryPrecondition extends Precondition
	{
		/**
		 * Constructs a <CODE>UnaryPrecondition</CODE> that will operate on
		 * the given underlying <CODE>Precondition</CODE> instance.
		 * 
		 * @param	pre			The underlying <CODE>Precondition</CODE>.
		 * @since	TFP 1.0
		 */
		protected UnaryPrecondition (final Precondition pre)
		{
			this.pre = pre;
		}

		/**
		 * The underlying <CODE>Precondition</CODE>.
		 * @since	TFP 1.0
		 */
		protected final Precondition	pre;
	}

	/**
	 * The <CODE>BinaryPrecondition</CODE> class records the left and right
	 * hand side arguments for some binary logical operator.
	 * 
	 * @since	TFP 1.0
	 */
	protected static abstract class BinaryPrecondition extends Precondition
	{
		/**
		 * Constructs a <CODE>BinaryPrecondition</CODE> that will operate on
		 * the given left and right hand side <CODE>Precondition</CODE>
		 * instances.
		 * 
		 * @param	lhs			The left hand <CODE>Precondition</CODE>.
		 * @param	rhs			The right hand <CODE>Precondition</CODE>.
		 * @since	TFP 1.0
		 */
		protected BinaryPrecondition (final Precondition lhs, final Precondition rhs)
		{
			this.lhs = lhs;
			this.rhs = rhs;
		}

		/**
		 * The <CODE>Precondition</CODE> defining the left hand side
		 * @since	TFP 1.0
		 */
		protected final Precondition	lhs;
		
		/**
		 * The <CODE>Precondition</CODE> defining the right hand side
		 * @since	TFP 1.0
		 */
		protected final Precondition	rhs;		
	}
	
	/**
	 * The <CODE>Not</CODE> class implements a logical not of an underlying
	 * <CODE>Predicate</CODE> instance.
	 * 
	 * @since	TFP 1.0
	 */
	protected static final class Not extends UnaryPrecondition
	{
		/**
		 * Constructs a <CODE>Not</CODE> instance that inverts the underlying
		 * <CODE>Precondition</CODE> instance.
		 * 
		 * @param	pre			The underlying <CODE>Precondition</CODE>.
		 * @since	TFP 1.0
		 */
		public Not (final Precondition pre)
		{
			super (pre);
		}
		
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		public boolean evaluate (final NodeIndex nodeIndex, Hashtable<Precondition, Boolean> cache)
		{
			Boolean	preValue = cache.get (pre);
			
			if (preValue == null)
				cache.put (pre, preValue = pre.evaluate (nodeIndex, cache));

			return (!preValue);
		}

		/**
		 * Returns a <CODE>String</CODE> describing expression this <CODE>Precondition
		 * </CODE> evaluates, in this case "!(exp)".
		 * 
		 * @return	The <CODE>Precondition</CODE> expression as a string.
		 * @since	TFP 1.0
		 */
		public String toString ()
		{
			return ("!(" + pre + ")");
		}
	}

	/**
	 * The <CODE>And</CODE> class implements a logical and between two
	 * <CODE>Predicate</CODE> instances.
	 * 
	 * @since	TFP 1.0
	 */
	protected static final class And extends BinaryPrecondition
	{
		/**
		 * Constructs an <CODE>Or</CODE> instance that connects two underlying
		 * <CODE>Precondition</CODE> instances.
		 * 
		 * @param	lhs			The left hand <CODE>Precondition</CODE>.
		 * @param	rhs			The right hand <CODE>Precondition</CODE>.
		 * @since	TFP 1.0
		 */
		public And (final Precondition lhs, final Precondition rhs)
		{
			super (lhs, rhs);
		}
		
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		public boolean evaluate (final NodeIndex nodeIndex, Hashtable<Precondition, Boolean> cache)
		{
			Boolean	lhsValue = cache.get (lhs);
			Boolean rhsValue = cache.get (rhs);
			
			if (lhsValue == null)
				cache.put (lhs, lhsValue = lhs.evaluate (nodeIndex, cache));
			
			if (!lhsValue) return (false);
				
			if (rhsValue == null)
				cache.put (rhs, rhsValue = rhs.evaluate (nodeIndex, cache));
			
			if (!rhsValue) return (false);
			
			return (true);
		}

		/**
		 * Returns a <CODE>String</CODE> describing expression this <CODE>Precondition
		 * </CODE> evaluates, in this case "(lhs &amp;&amp; rhs)".
		 * 
		 * @return	The <CODE>Precondition</CODE> expression as a string.
		 * @since	TFP 1.0
		 */
		public String toString ()
		{
			return ("(" + lhs + " && " + rhs + ")");
		}
	}

	/**
	 * The <CODE>Or</CODE> class implements a logical or between two
	 * <CODE>Predicate</CODE> instances.
	 * 
	 * @since	TFP 1.0
	 */
	protected static final class Or extends BinaryPrecondition
	{
		/**
		 * Constructs an <CODE>Or</CODE> instance that connects two underlying
		 * <CODE>Precondition</CODE> instances.
		 * 
		 * @param	lhs			The left hand <CODE>Precondition</CODE>.
		 * @param	rhs			The right hand <CODE>Precondition</CODE>.
		 * @since	TFP 1.0
		 */
		public Or (final Precondition lhs, final Precondition rhs)
		{
			super (lhs, rhs);
		}
		
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.0
		 */
		public boolean evaluate (NodeIndex nodeIndex, Hashtable<Precondition, Boolean> cache)
		{
			Boolean	lhsValue = cache.get (lhs);
			Boolean rhsValue = cache.get (rhs);
			
			if (lhsValue == null)
				cache.put (lhs, lhsValue = lhs.evaluate (nodeIndex, cache));
				
			if (lhsValue) return (true);
			
			if (rhsValue == null)
				cache.put (rhs, rhsValue = rhs.evaluate (nodeIndex, cache));
			
			if (rhsValue) return (true);
			
			return (false);
		}
		
		/**
		 * Returns a <CODE>String</CODE> describing expression this <CODE>Precondition
		 * </CODE> evaluates, in this case "(lhs || rhs)".
		 * 
		 * @return	The <CODE>Precondition</CODE> expression as a string.
		 * @since	TFP 1.0
		 */
		public String toString ()
		{
			return ("(" + lhs + " || " + rhs + ")");
		}
	}

	/**
	 * Constructs a <CODE>Precondition</CODE> instance.
	 * @since	TFP 1.0
	 */
	protected Precondition ()
	{ }
}