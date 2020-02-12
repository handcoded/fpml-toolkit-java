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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;

/**
 * The <CODE>DefaultSchemaRecognise</CODE> class uses the following strategy
 * to see if an XML <CODE>Document</CODE> could be an instance of the
 * indicated <CODE>SchemaRelease</CODE>.
 * <P>
 * The approach taken depends on the nature of the <CODE>SchemaRelease</CODE>:
 * <UL>
 * <LI>If the <CODE>SchemaRelease</CODE> defines one or more possible root elements
 * then this implementation checks of the documents root element name and
 * namespace match the schema.</LI>
 * <LI>If the <CODE>SchemaRelease</CODE> is a pure extension schema (e.g. one
 * containing only new types or substituting elements) then code looks for a
 * reference to the schema's namespace on the root element. This might not be
 * thorough enough for some documents.</LI>
 * </UL>
 *   
 * @author	Andrew Jacobs
 * @since	TFP 1.1
 */
public class DefaultSchemaRecogniser implements SchemaRecogniser
{
	/**
	 * Constructs a <CODE>SchemaRecogniser</CODE> instance.
	 * @since	TFP 1.1
	 */
	public DefaultSchemaRecogniser ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.1
	 */
	public boolean recognises (SchemaRelease release, Document document)
	{
		if (release.isExtensionOnly ()) {
			// If the schema is a pure extension then check to see if any
			// xmlns attribute references its namespace.
			
			NamedNodeMap	attrs		= document.getDocumentElement ().getAttributes ();
			String			namespace 	= release.getNamespaceUri ();
			
			for (int index = 0; index < attrs.getLength (); ++index) {
				Attr attr = (Attr) attrs.item (index);

				if (attr.getName ().startsWith ("xmlns") && attr.getValue ().equals (namespace))
					return (true);
			}
		}
		else {
			// If the schema declares root elements then check the name and
			// namespace matches.
			
			Element			root	= document.getDocumentElement ();
			
			if (root != null) {
				String			namespace = root.getNamespaceURI ();
				
				if ((namespace != null) && namespace.equals (release.getNamespaceUri ())
						&& release.hasRootElement (root.getLocalName ()))
					return (true);
			}
		}
		return (false);
	}
}