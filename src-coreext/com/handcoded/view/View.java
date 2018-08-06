// Copyright (C),2005-2017 HandCoded Software Ltd.
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

package com.handcoded.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;

import org.w3c.dom.Element;

/**
 * The <CODE>View</CODE> class defined a set of variable and facet XPaths that
 * will be evaluated against a document with the help of some namespace and
 * external function definitions. Variables and facets are evaluated in the
 * order they are defined followed by any active sub-facet sets.
 * 
 * @author 	BitWise
 * @since	TFP 1.9
 */
public abstract class View
{
	/**
	 * Returns the parent <CODE>View</CODE> or <CODE>null</CODE>.
	 * 
	 * @return	The parent <CODE>View</CODE> or <CODE>null</CODE>.
	 * @since	TFP 1.9
	 */
	public final View getParent ()
	{
		return (parent);
	}
	
	/**
	 * Adds a namespace definition to the set of recognized namespaces used
	 * in XPaths.
	 * 
	 * @param	prefix		The prefix used in XPath expressions
	 * @param	uri			The full namespace URI.
	 * @since	TFP 1.9
	 */
	public void addNamespace (String prefix, String uri)
	{
		namespaces.put (prefix, uri);
	}
	
	/**
	 * Adds a <CODE>Function</CODE> instance to the set of functions that can
	 * be used in XPath expressions.
	 * 
	 * @param	function	The <CODE>Function</CODE> instance to be added.
	 * @since	TFP 1.9
	 */
	public void addFunction (Function function)
	{
		functions.put (function.getName (), function);
	}
	
	/**
	 * Adds a <CODE>Variable</CODE> instance to the set of variables that can
	 * be used in XPath expressions.
	 * 
	 * @param	variable	The <CODE>Variable</CODE> instance to be added.
	 * @since	TFP 1.9
	 */
	public void addVariable (Variable variable)
	{
		variables.add (variable);
	}
	
	/**
	 * Adds a <CODE>Facet</CODE> to the set of facets defined in this
	 * <CODE>View</CODE>.
	 * 
	 * @param	facet		The <CODE>Facet</CODE> instance to be added.
	 * @since	TFP 1.9
	 */
	public void addFacet (Facet facet)
	{
		facets.add (facet);
	}
	
	/**
	 * Adds a <CODE>FacetSet</CODE> to the set of facet sets defined in this
	 * <CODE>View</CODE>.
	 * 
	 * @param	facetSet	The <CODE>FacetSet</CODE> instance to be added.
	 * @since	TFP 1.9
	 */
	public void addFacetSet (FacetSet facetSet)
	{
		facetSets.add (facetSet);
	}

	/**
	 * Search the <CODE>View</CODE> hierarchy trying to map a prefix back to
	 * the namespace URI.
	 * 
	 * @param	prefix		The target prefix
	 * @return	The corresponding namespace URI or <CODE>null</CODE>.
	 * @since	TFP 1.9
	 */
	public final String findNamespace (String prefix)
	{
		String			uri = namespaces.get (prefix);
	
		if ((uri == null) && (parent != null))
			uri = parent.findNamespace (prefix);
		
		return (uri);
	}
	
	/**
	 * The parent <CODE>View</CODE> instance.
	 * @since	TFP 1.9
	 */
	protected final View parent;

	/**
	 * The set of namespaces defined in this view indexed by prefix.
	 * @since	TFP 1.9
	 */
	protected HashMap<String, String> namespaces
		= new HashMap<String, String> ();
	
	/**
	 * The set of functions defined in this view indexed by name.
	 * @since	TFP 1.9
	 */
	protected HashMap<QName, Function>	functions
		= new HashMap<QName, Function> ();
	
	/**
	 * The set of variables defined in this view.
	 * @since	TFP 1.9
	 */
	protected Vector<Variable> variables
		= new Vector<Variable> ();
	
	/**
	 * The set of facets defined in this view.
	 * @since	TFP 1.9
	 */
	protected Vector<Facet> facets
		= new Vector<Facet> ();
	
	/**
	 * The set of facetSets defined in this view.
	 * @since	TFP 1.9
	 */
	protected Vector<FacetSet> facetSets
		= new Vector<FacetSet> ();

	/**
	 * Constructs a <CODE>View</CODE> and links it to its parent <CODE>View</CODE>
	 * instance (or <CODE>null</CODE>).
	 * 
	 * @param	parent		The parent <CODE>View</CODE> or <CODE>null</CODE>.
	 */
	protected View (View parent)
	{
		this.parent = parent;
		
		xpath.setXPathFunctionResolver (new FunctionResolver ());
	}

	/**
	 * Apply a <CODE>View</CODE> to the DOM tree indicated by the context
	 * <CODE>Element</CODE> using the current state of the <CODE>VariableSet</CODE>
	 * to derive the resulting facet <CODE>ValueSet</CODE>.
	 * 
	 * @param	context		The root <CODE>Element</CODE> defining the context.
	 * @param	values		The value of all evaluated variables.
	 * @param	results		The value of previously evaluated <CODE>Facet</CODE> instances.
	 * @return	The resulting <CODE>Facet</CODE> values.
	 * @since	TFP 1.9
	 */
	protected ValueSet applyTo (Element context, VariableSet values, ValueSet results)
	{
		// Evaluate all the variables
		for (Variable variable : variables)
			values.add (variable, evaluate (context, variable.getExpr (), values, variable.getType ()));
		
		// Then all the mandatory facets
		for (Facet facet : facets)
			results.add (facet, evaluate (context, facet.getExpr (), values, facet.getType ()));
		
		// Evaluate any facetSet whose test is true
		for (FacetSet facetSet : facetSets) {
			if (evaluate (context, facetSet.getTest (), values, XPathConstants.BOOLEAN).toBoolean ())
				facetSet.applyTo (context, values, results);
		}
		
		// Remove variables going out of scope
		for (Variable variable : variables)
			values.remove (variable);
	
		return (results);
	}
	
	/**
	 * Evaluates an XPath expression against the DOM tree starting at the
	 * context <CODE>Element</CODE> and wrap the result as a <CODE>Value</CODE>.
	 * 
	 * @param	context		The root <CODE>Element</CODE> defining the context.
	 * @param	path		The XPath expression string.
	 * @param	values		The value of all evaluated variables.
	 * @param	type		The type of result required.
	 * @return	A wrapped result <CODE>Value</CODE> or <CODE>null</CODE>.
	 * @since	TFP 1.9
	 */
	protected Value evaluate (Element context, String path, VariableSet values, QName type)
	{
		xpath.setNamespaceContext (new NamespaceResolver ());
		xpath.setXPathVariableResolver(new VariableResolver (values));

		try {
			return (new Value (xpath.evaluate (path, context, type), type));
		} catch (XPathExpressionException error) {
			logger.log (Level.SEVERE, "Failed to execute XPath: " + path, error);
		}
		
		return (null);
	}
	
	/**
	 * The <CODE>NamespaceResolver</CODE> implements a simple search up through
	 * the FacetSets and DataView to locate a namespace definition.
	 * 
	 * @author	BitWise
	 * @since	TFP 1.9
	 */
	private class NamespaceResolver implements NamespaceContext
	{
		/**
		 * {@inheritDoc}
		 * @since	TFP 1.9
		 */
		public String getNamespaceURI (String prefix)
		{
			for (View view = View.this; view != null;) {
				if (view.namespaces.containsKey (prefix))
					return (view.namespaces.get (prefix));
				view = view.parent;
			}
			return (null);
		}

		/**
		 * {@inheritDoc}
		 * @since	TFP 1.9
		 */
		public String getPrefix (String namespaceURI)
		{
			for (View view = View.this; view != null; view = parent) {
				for (String prefix : view.namespaces.keySet ())
					if (view.namespaces.get (prefix).equals(namespaceURI))
						return (prefix);				
			}
			return (null);
		}

		/**
		 * {@inheritDoc}
		 * @since	TFP 1.9
		 */
		@SuppressWarnings("rawtypes")
		public Iterator getPrefixes (String namespaceURI)
		{
			return (null);
		}
	}
	
	/**
	 * The <CODE>FunctionResolver</CODE> implements a simple search up through
	 * the FacetSets and DataView to locate a function definition.
	 * 
	 * @author	BitWise
	 * @since	TFP 1.9
	 */
	private class FunctionResolver implements XPathFunctionResolver
	{
		/**
		 * Construct a <CODE>FunctionResolver</CODE> instance.
		 */
		public FunctionResolver ()
		{ }
		
		/**
		 * {@inheritDoc}
		 */
		public XPathFunction resolveFunction (QName name, int arity)
		{
			// Search up the view hierarchy for a matching function
			for (View view = View.this; view != null; view = view.parent) {
				Function function = view.functions.get (name);
				
				if (function != null) return (function.getInstance ());
			}
			return (null);
		}
	}
	
	/**
	 * The <CODE>VariableResolver</CODE> implements a simple search up through
	 * the FacetSets and DataView to locate a variable definition.
	 * 
	 * @author	BitWise
	 * @since	TFP 1.9
	 */
	private static class VariableResolver implements XPathVariableResolver
	{
		/*
		 * Constructs a <CODE>VariableResolver</CODE> attached to the indicates
		 * <CODE>VariableSet</CODE>.
		 */
		public VariableResolver (VariableSet variables)
		{
			this.variables = variables;
		}
		
		/*
		 * {@inheritDoc}
		 */
		public Object resolveVariable(QName name)
		{
			Value 			value = variables.get (name.getLocalPart ());
			
			return ((value != null) ? value.getObject () : null);
		}
		
		private VariableSet		variables;
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.9
	 */
	private static Logger	logger
		= Logger.getLogger ("com.handcoded.view.View");

	/**
	 * The <CODE>XPathFactory</CODE> used to create new <CODE>xPath</CODE>
	 * instances.
	 * @since	TFP 1.9
	 */
	private static XPathFactory	factory	= XPathFactory.newInstance ();
	
	/**
	 * The <CODE>XPath</CODE> instance that will be used to evaluate path
	 * expression defined by this views.
	 * @since	TFP 1.9
	 */
	private XPath			xpath 	= factory.newXPath (); 
}