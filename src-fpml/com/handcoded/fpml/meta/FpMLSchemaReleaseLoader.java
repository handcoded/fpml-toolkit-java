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

package com.handcoded.fpml.meta;

import java.util.HashMap;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.fpml.schemes.SchemeCollection;
import com.handcoded.meta.DefaultSchemaReleaseLoader;
import com.handcoded.meta.Specification;
import com.handcoded.xml.Types;
import com.handcoded.xml.XPath;

/**
 * An instance of the <CODE>FpMLSchemaReleaseLoader</CODE> class will
 * extract the description of an FpML XML Schema based grammar from the bootstrap data
 * file and construct a <CODE>SchemaRelease</CODE> to hold it.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.5
 */
public class FpMLSchemaReleaseLoader extends DefaultSchemaReleaseLoader
{
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.5
	 */
	@Override
	public void loadData (Specification specification, Element context,
			HashMap<String, com.handcoded.meta.SchemaRelease> loadedSchemas)
	{
		Attr		id 		= context.getAttributeNode ("id");
		
		SchemaRelease release = new SchemaRelease (specification,
				getVersion (context), getNamespaceUri (context),
				getSchemaLocation (context), getPreferredPrefix (context),
				getAlternatePrefix (context),
				new FpMLInstanceInitialiser (),
				new FpMLSchemaRecogniser (), getRootElements (context),
				getSchemeDefaults (context), getSchemeCollection (context));
		
		handleImports (release, context, loadedSchemas);
		
		if (id != null) loadedSchemas.put (id.getValue (), release);
	}
	
	/**
	 * Extracts the release's scheme defaults data from the XML section
	 * describing the schema.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	A populated <CODE>SchemeDefaults</CODE> instance.
	 * @since	TFP 1.5
	 */
	protected SchemeDefaults getSchemeDefaults (Element context)
	{
		NodeList list = XPath.paths (context, "schemeDefault");
		String [][] values	= new String [list.getLength ()][2];
		
		for (int index = 0; index < list.getLength (); ++index) {
			Element node = (Element) list.item (index);
			values [index][0] = Types.toToken (XPath.path (node, "attribute"));
			values [index][1] = Types.toToken (XPath.path (node, "schemeUri"));
		}
		
		return (new SchemeDefaults (values));
	}
	
	/**
	 * Build a <CODE>SchemeCollection</CODE> instance for the release using
	 * the scheme filenames from the XML section describing the schema.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	A populated <CODE>SchemeCollection</CODE> instance.
	 * @since	TFP 1.5
	 */
	protected SchemeCollection getSchemeCollection (Element context)
	{
		SchemeCollection schemes = new SchemeCollection ();
		NodeList list = XPath.paths (context, "schemes");
		
		for (int index = 0; index < list.getLength (); ++index)
			schemes.parse (Types.toToken (list.item (index)));
		
		return (schemes);
	}
}
