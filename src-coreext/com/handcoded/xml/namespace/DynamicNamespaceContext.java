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

package com.handcoded.xml.namespace;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Element;

/**
 * The <CODE>DynamicNamespaceContext</CODE> class provides an implementation
 * of the <CODE>NamespaceContext</CODE> that maps the namespace prefix 'dyn'
 * to the namespace URI of an indicated element. This allows XPaths to be written
 * that will execute against multiple versions of the FpML schema, adjusting
 * to the document being tested dynamically as the class name suggests.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class DynamicNamespaceContext implements NamespaceContext
{
	/**
	 * Constructs a <CODE>DynamicNamespaceContext</CODE> based on the
	 * indicated context <CODE>Element</CODE>.
	 * 
	 * @param 	context			The context <CODE>Element</CODE>.
	 * @since	TFP 1.6
	 */
	public DynamicNamespaceContext (Element context)
	{
		this.context = context;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public String getNamespaceURI (String prefix)
	{
		if (prefix.equals ("dyn"))
			return (context.getNamespaceURI ());
		
		return (null);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public String getPrefix (String namespaceURI)
	{
		if (namespaceURI.equals (context.getNamespaceURI ()))
			return ("dyn");
		
		return (null);
	}

	/**
	 * {@inheritDoc}
	 * Dummy implementation. Always returns <CODE>null</CODE>.
	 * 
	 * @since	TFP 1.6
	 */
	public Iterator<String> getPrefixes (String namespaceURI)
	{
		return (null);
	}
	
	/**
	 * The context element to derive the namespace from.
	 * @since	TFP 1.6
	 */
	private final Element	context;
}