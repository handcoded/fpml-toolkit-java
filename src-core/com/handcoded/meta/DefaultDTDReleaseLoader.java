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

import org.w3c.dom.Element;

import com.handcoded.xml.Types;
import com.handcoded.xml.XPath;

/**
 * An instance of the <CODE>DefaultDTDReleaseLoader</CODE> class will
 * extract the description of a DTD based grammar from the bootstrap data
 * file and construct a <CODE>DTDRelease</CODE> to hold it.
 * 
 * @author	BitWise
 * @version	$Id: DefaultDTDReleaseLoader.java 698 2012-11-30 18:15:39Z andrew_jacobs $
 * @since	TFP 1.5
 */
public class DefaultDTDReleaseLoader implements ReleaseLoader
{
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.5
	 */
	public void loadData (Specification specification, Element context,
			Hashtable<String, SchemaRelease> loadedSchemas)
	{
		new DTDRelease (specification, getVersion (context),
				getPublicId (context), getSystemId (context),
				getRootElement (context));
	}
	
	/**
	 * Extracts the release's version number string from the XML section
	 * describing the DTD.
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
	 * Extracts the release's public name from the XML section
	 * describing the DTD.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The public name string.
	 * @since	TFP 1.5
	 */
	protected final String getPublicId (Element context)
	{
		return (Types.toToken (XPath.path (context, "publicName")));
	}
	
	/**
	 * Extracts the release's system identifier from the XML section
	 * describing the DTD.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The system identifier string.
	 * @since	TFP 1.5
	 */
	protected final String getSystemId (Element context)
	{
		return (Types.toToken (XPath.path (context, "systemId")));
	}
	
	/**
	 * Extracts the set of root element names from the XML section
	 * describing the DTD.
	 * 
	 * @param	context			The context <CODE>Element</CODE> for the section.
	 * @return	The set of root element names.
	 * @since	TFP 1.5
	 */
	protected final String getRootElement (Element context)
	{
		return (Types.toToken (XPath.path (context, "rootElement")));
	}
}