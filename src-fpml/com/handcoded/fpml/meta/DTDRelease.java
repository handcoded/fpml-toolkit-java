// Copyright (C),2005-2006 HandCoded Software Ltd.
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handcoded.meta.Specification;
import com.handcoded.fpml.schemes.SchemeCollection;

/**
 * The <CODE>DTDRelease</CODE> adds FpML specific knowledge to the base
 * <CODE>com.handcoded.xml.DTDRelease</CODE>. It ensures that the <CODE>FpML
 * </CODE> element is automatically assigned the correct version identifier
 * and holds a reference to a schemes description for the release.
 * 
 * @author	BitWise
 * @version	$Id: DTDRelease.java 38 2006-08-31 20:40:21Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class DTDRelease extends com.handcoded.meta.DTDRelease implements SchemeAccess
{
	/**
	 * Constructs a <CODE>DTDRelease</CODE> instance describing a DTD based
	 * release of a particular <CODE>Specification</CODE>.
	 * 
	 * @param 	specification	A reference to the owning specification.
	 * @param 	version			The version identifier for this release.
	 * @param 	publicId		The public identifier URI.
	 * @param 	systemId		The system identifier.
	 * @param 	rootElement		The normal root element.
	 * @param	schemeDefaults	The scheme defaults for the release.
	 * @param	schemeCollection The scheme collection for the release.
	 * @since 	TFP 1.0
	 */
	public DTDRelease (Specification specification, String version,
		final String publicId, final String systemId, final String rootElement,
		SchemeDefaults schemeDefaults, SchemeCollection schemeCollection)
	{
		super (specification, version, publicId, systemId, rootElement);
		
		this.schemeDefaults   = schemeDefaults;
		this.schemeCollection = schemeCollection;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public Document newInstance (final String rootElement)
	{
		Document		document 	= super.newInstance (rootElement);
		Element			root		= document.getDocumentElement ();
		
		root.setAttributeNS (null, "version", getVersion ());
		
		return (document);
	}

	/**
	 * {@inheritDoc}
	 * @since 	TFP 1.0
	 */
	public SchemeDefaults getSchemeDefaults ()
	{
		return (schemeDefaults);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public SchemeCollection getSchemeCollection ()
	{
		return (schemeCollection);
	}
	
	/**
	 * The <CODE>SchemeDefaults</CODE> instance describing scheme values for
	 * this release.
	 */
	private final SchemeDefaults	schemeDefaults;
	
	/**
	 * The <CODE>SchemeCollection</CODE> instance containg the schemes used
	 * for validation.
	 */
	private final SchemeCollection	schemeCollection;
}