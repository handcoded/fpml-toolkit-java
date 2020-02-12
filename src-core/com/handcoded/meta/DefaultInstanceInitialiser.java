// Copyright (C),2005-2020 HandCoded Software Ltd.
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

package com.handcoded.meta;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * The <CODE>DefaultInstanceInitialiser</CODE> class performs the population of
 * attributes and values on the root element of a new document.
 *  
 * @author	Andrew Jacobs
 * @since	TFP 1.1
 */
public class DefaultInstanceInitialiser implements InstanceInitialiser
{
	/**
	 * Constructs a <CODE>DefaultInstanceInitialiser</CODE> that performs the
	 * default actions.
	 * @since	TFP 1.1
	 */
	public DefaultInstanceInitialiser ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.1
	 */
	public void initialise (SchemaRelease release, Element root, boolean isDefaultNamespace)
	{
		String		namespaceUri	= release.getNamespaceUri ();
		String		preferredPrefix	= release.getPreferredPrefix ();
		String		schemaLocation	= release.getSchemaLocation ();
		
		if (isDefaultNamespace)
			root.setAttributeNS (Schema.NAMESPACES_URL, "xmlns", namespaceUri);
		else
			root.setAttributeNS (Schema.NAMESPACES_URL, "xmlns:" + preferredPrefix, namespaceUri);
		
		// The following works around a problem in DOM
		NamedNodeMap attrs = root.getAttributes ();
		Attr		attr = null;
		
		for (int index = 0; index < attrs.getLength (); ++index) {
			if ((attr = (Attr) attrs.item (index)).getName ().equals ("xsi:schemaLocation")) break;
		}

		String value = attr.getValue ();
		if (value.length () != 0) value += " ";
		value += namespaceUri + " " + schemaLocation;
		attr.setValue (value);
	}
}