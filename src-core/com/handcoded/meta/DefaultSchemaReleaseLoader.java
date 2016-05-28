// Copyright (C),2005-2011 HandCoded Software Ltd.
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

import java.util.Hashtable;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.xml.Types;
import com.handcoded.xml.XPath;

/**
 * An instance of the <CODE>DefaultSchemaReleaseLoader</CODE> class will
 * extract the description of an XML Schema based grammar from the bootstrap data
 * file and construct a <CODE>SchemaRelease</CODE> to hold it.
 * 
 * @author	BitWise
 * @version	$Id: DefaultSchemaReleaseLoader.java 698 2012-11-30 18:15:39Z andrew_jacobs $
 * @since	TFP 1.5
 */
public class DefaultSchemaReleaseLoader implements ReleaseLoader
{
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.5
	 */
	public void loadData (Specification specification, Element context,
			Hashtable<String, SchemaRelease> loadedSchemas)
	{
		Attr		id 		= context.getAttributeNode ("id");
		
		SchemaRelease release = new SchemaRelease (specification,
				getVersion (context), getNamespaceUri (context),
				getSchemaLocation (context), getPreferredPrefix (context),
				getAlternatePrefix (context), getRootElements (context));
		
		handleImports (release, context, loadedSchemas);
		
		if (id != null) loadedSchemas.put (id.getValue (), release);
	}
	
	/**
	 * Extracts the release's version number string from the XML section
	 * describing the schema.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The release version number string.
	 * @since	TFP 1.5
	 */
	protected final String getVersion (Element context)
	{
		return (Types.toToken (XPath.path (context, "version")));
	}
	
	/**
	 * Extracts the release's namespace URI from the XML section
	 * describing the schema.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The namespace URI.
	 * @since	TFP 1.5
	 */
	protected final String getNamespaceUri (Element context)
	{
		return (Types.toToken (XPath.path (context, "namespaceUri")));
	}
	
	/**
	 * Extracts the release's default schema location path from the XML section
	 * describing the schema.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The default schema location path.
	 * @since	TFP 1.5
	 */
	protected final String getSchemaLocation (Element context)
	{
		return (Types.toToken (XPath.path (context, "schemaLocation")));
	}
	
	/**
	 * Extracts the release's preferred prefix string from the XML section
	 * describing the schema.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The preferred prefix string.
	 * @since	TFP 1.5
	 */
	protected final String getPreferredPrefix (Element context)
	{
		return (Types.toToken (XPath.path (context, "preferredPrefix")));
	}
	
	/**
	 * Extracts the release's alternate prefix string from the XML section
	 * describing the schema.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The alternate prefix string.
	 * @since	TFP 1.5
	 */
	protected final String getAlternatePrefix (Element context)
	{
		return (Types.toToken (XPath.path (context, "alternatePrefix")));
	}
	
	/**
	 * Extracts the release's root element names from the XML section
	 * describing the schema.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The set of root element names.
	 * @since	TFP 1.5
	 */
	protected final String [] getRootElements (Element context)
	{
		NodeList	list = XPath.paths (context, "rootElement");
		String []	rootElements = new String [list.getLength ()];
		
		for (int index = 0; index < list.getLength (); ++index)
			rootElements [index] = Types.toToken (list.item (index));
		
		return (rootElements);
	}
	
	/**
	 * Connects this schema to any other schemas that it imports.
	 * 
	 * @param	release			The <CODE>SchemaRelease</CODE> for this schema.
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @param	loadedSchemas	A <CODE>Hashtable</CODE> of previous bootstrapped schemas.
	 * @since	TFP 1.5
	 */
	protected final void handleImports (SchemaRelease release, Element context,
			Hashtable<String, SchemaRelease> loadedSchemas)
	{
		NodeList	list = XPath.paths (context, "import");
		
		for (int index = 0; index < list.getLength (); ++index) {
			Attr href = ((Element) list.item (index)).getAttributeNode ("href");
			if (href != null)
				release.addImport (loadedSchemas.get (href.getValue ()));
		}
	}
}