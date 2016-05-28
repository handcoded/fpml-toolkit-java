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

package com.handcoded.fpml.validation;

import java.util.Hashtable;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.meta.SchemaRelease;
import com.handcoded.validation.Precondition;
import com.handcoded.xml.NodeIndex;

/**
 * The <CODE>NamespacePrecondition</CODE> class checks that the FpML root
 * element uses an element belonging to a specific namespace URI.
 * 
 * @author	BitWise
 * @version	$Id: NamespacePrecondition.java 672 2012-10-30 21:56:09Z andrew_jacobs $
 * @since	TFP 1.7
 */
public final class NamespacePrecondition extends Precondition
{
	/**
	 * Constructs a <CODE>NamespacePrecondition</CODE> instance for the
	 * specified <CODE>SchemaRelease</CODE> instance.
	 * 
	 * @param 	release			The target <CODE>SchemaRelease</CODE>
	 * @since	TFP 1.7
	 */
	public NamespacePrecondition (final SchemaRelease release)
	{
		this (release.getNamespaceUri ());
	}
	
	/**
	 * Constructs a <CODE>NamespacePrecondition</CODE> instance for the
	 * specified namespace URI.
	 * 
	 * @param 	namespaceUri	The target namespace URI.
	 * @since	TFP 1.7
	 */
	public NamespacePrecondition (final String namespaceUri)
	{
		this.namespaceUri = namespaceUri;
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.7
	 */
	public boolean evaluate (NodeIndex nodeIndex,
			Hashtable<Precondition, Boolean> cache)
	{
		Element		rootElement;
		
		// Find the document element
		NodeList list = nodeIndex.getElementsByName ("FpML");
		if (list.getLength () > 0)
			rootElement = (Element) list.item (0);
		else {
			list = nodeIndex.getAttributesByName ("fpmlVersion");
			if (list.getLength () > 0)
				rootElement = ((Attr) list.item (0)).getOwnerElement ();
			else
				return (false);
		}
		
		String ns = rootElement.getNamespaceURI ();
		return ((ns != null) ? (ns.compareTo (namespaceUri) == 0) : false);
	}

	/**
	 * The target namespace URI.
	 * @since	TFP 1.7
	 */
	private final String	namespaceUri;
}