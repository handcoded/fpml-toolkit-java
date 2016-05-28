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

package com.handcoded.fpml.meta;

import java.util.Hashtable;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.handcoded.fpml.schemes.SchemeCollection;
import com.handcoded.meta.DefaultDTDReleaseLoader;
import com.handcoded.meta.Specification;
import com.handcoded.xml.Types;
import com.handcoded.xml.XPath;

/**
 * An instance of the <CODE>FpMLDTDReleaseLoader</CODE> class will
 * extract the description of an FpML DTD based grammar from the bootstrap data
 * file and construct a <CODE>DTDRelease</CODE> to hold it.
 * 
 * @author	BitWise
 * @version	$Id: FpMLDTDReleaseLoader.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.5
 */
public class FpMLDTDReleaseLoader extends DefaultDTDReleaseLoader
{
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.5
	 */
	public void loadData (Specification specification, Element context,
			Hashtable<String, com.handcoded.meta.SchemaRelease> loadedSchemas)
	{
		new DTDRelease (specification, getVersion (context),
				getPublicId (context), getSystemId (context),
				getRootElement (context), getSchemeDefaults (context),
				getSchemeCollection (context));
	}

	/**
	 * Extracts the release's scheme defaults data from the XML section
	 * describing the DTD.
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

		list = XPath.paths (context, "defaultAttribute");
		String [][] names	= new String [list.getLength ()][2];

		for (int index = 0; index < list.getLength (); ++index) {
			Element node = (Element) list.item (index);
			names [index][0] = Types.toToken (XPath.path (node, "attribute"));
			names [index][1] = Types.toToken (XPath.path (node, "default"));
		}

		return (new SchemeDefaults (values, names));
	}
	
	/**
	 * Build a <CODE>SchemeCollection</CODE> instance for the release using
	 * the scheme filenames from the XML section describing the DTD.
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
