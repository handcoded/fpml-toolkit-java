// Copyright (C),2005-2007 HandCoded Software Ltd.
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

import com.handcoded.fpml.schemes.SchemeCollection;
import com.handcoded.meta.InstanceInitialiser;
import com.handcoded.meta.SchemaRecogniser;
import com.handcoded.meta.Specification;

/**
 * The <CODE>SchemaRelease</CODE> adds FpML specific knowledge to the base
 * <CODE>com.handcoded.xml.SchemaRelease</CODE>. It ensures that the <CODE>FpML
 * </CODE> element is automatically assigned the correct version identifier
 * and holds a reference to a schemes description for the release.
 * 
 * @author	BitWise
 * @version	$Id: SchemaRelease.java 245 2007-12-20 11:01:42Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class SchemaRelease extends com.handcoded.meta.SchemaRelease implements SchemeAccess
{
	/**
	 * Constructs a <CODE>SchemaRelease</CODE> instance describing a schema
	 * based release of a particular <CODE>Specification</CODE>.
	 * 
	 * @param 	specification	A reference to the owning specification.
	 * @param 	version			The version identifier for this release.
	 * @param 	namespaceUri	The namespace used to identify the schema.
	 * @param 	schemaLocation	The default schema location.
	 * @param 	preferredPrefix	The preferred prefix for the namespace.
	 * @param 	alternatePrefix	The alternate prefix for the namespace.
	 * @param 	rootElement		The normal root element.
	 * @param	schemeDefaults	The scheme defaults for the release.
	 * @param	schemeCollection The scheme collection for the release.
	 * @since	TFP 1.0
	 */
	public SchemaRelease (Specification specification, final String version,
			final String namespaceUri, final String schemaLocation,
			final String preferredPrefix, final String alternatePrefix,
			final String rootElement, SchemeDefaults schemeDefaults,
			SchemeCollection schemeCollection)
	{
		super (specification, version, namespaceUri, schemaLocation,
				preferredPrefix, alternatePrefix, rootElement);
		
		this.schemeDefaults   = schemeDefaults;
		this.schemeCollection = schemeCollection;
	}

	/**
	 * Constructs a <CODE>SchemaRelease</CODE> instance describing a schema
	 * based release of a particular <CODE>Specification</CODE>.
	 * 
	 * @param 	specification	A reference to the owning specification.
	 * @param 	version			The version identifier for this release.
	 * @param 	namespaceUri	The namespace used to identify the schema.
	 * @param 	schemaLocation	The default schema location.
	 * @param 	preferredPrefix	The preferred prefix for the namespace.
	 * @param 	alternatePrefix	The alternate prefix for the namespace.
	 * @param 	rootElements	The possible root elements.
	 * @param	schemeDefaults	The scheme defaults for the release.
	 * @param	schemeCollection The scheme collection for the release.
	 * @since	TFP 1.1
	 */
	public SchemaRelease (Specification specification, final String version,
			final String namespaceUri, final String schemaLocation,
			final String preferredPrefix, final String alternatePrefix,
			final String [] rootElements, SchemeDefaults schemeDefaults,
			SchemeCollection schemeCollection)
	{
		super (specification, version, namespaceUri, schemaLocation,
				preferredPrefix, alternatePrefix, rootElements);
		
		this.schemeDefaults   = schemeDefaults;
		this.schemeCollection = schemeCollection;
	}

	/**
	 * Constructs a <CODE>SchemaRelease</CODE> instance describing a schema
	 * based release of a particular <CODE>Specification</CODE>.
	 * 
	 * @param 	specification	A reference to the owning specification.
	 * @param 	version			The version identifier for this release.
	 * @param 	namespaceUri	The namespace used to identify the schema.
	 * @param 	schemaLocation	The default schema location.
	 * @param 	preferredPrefix	The preferred prefix for the namespace.
	 * @param 	alternatePrefix	The alternate prefix for the namespace.
	 * @param	initialiser		The <CODE>InstanceInitialise</CODE> or <CODE>null</CODE>.
	 * @param	recogniser		The <CODE>SchemaRecogniser</CODE> or <CODE>null</CODE>.
	 * @param 	rootElement		The normal root element.
	 * @param	schemeDefaults	The scheme defaults for the release.
	 * @param	schemeCollection The scheme collection for the release.
	 * @since	TFP 1.1
	 */
	public SchemaRelease (Specification specification, final String version,
			final String namespaceUri, final String schemaLocation,
			final String preferredPrefix, final String alternatePrefix,
			final InstanceInitialiser initialiser, final SchemaRecogniser recogniser,
			final String rootElement, SchemeDefaults schemeDefaults,
			SchemeCollection schemeCollection)
	{
		super (specification, version, namespaceUri, schemaLocation,
				preferredPrefix, alternatePrefix,
				initialiser, recogniser, rootElement);
		
		this.schemeDefaults   = schemeDefaults;
		this.schemeCollection = schemeCollection;
	}
	
	/**
	 * Constructs a <CODE>SchemaRelease</CODE> instance describing a schema
	 * based release of a particular <CODE>Specification</CODE>.
	 * 
	 * @param 	specification	A reference to the owning specification.
	 * @param 	version			The version identifier for this release.
	 * @param 	namespaceUri	The namespace used to identify the schema.
	 * @param 	schemaLocation	The default schema location.
	 * @param 	preferredPrefix	The preferred prefix for the namespace.
	 * @param 	alternatePrefix	The alternate prefix for the namespace.
	 * @param	initialiser		The <CODE>InstanceInitialise</CODE> or <CODE>null</CODE>.
	 * @param	recogniser		The <CODE>SchemaRecogniser</CODE> or <CODE>null</CODE>.
	 * @param 	rootElements	The possible root elements.
	 * @param	schemeDefaults	The scheme defaults for the release.
	 * @param	schemeCollection The scheme collection for the release.
	 * @since	TFP 1.1
	 */
	public SchemaRelease (Specification specification, final String version,
			final String namespaceUri, final String schemaLocation,
			final String preferredPrefix, final String alternatePrefix,
			final InstanceInitialiser initialiser, final SchemaRecogniser recogniser,
			final String [] rootElements, SchemeDefaults schemeDefaults,
			SchemeCollection schemeCollection)
	{
		super (specification, version, namespaceUri, schemaLocation,
				preferredPrefix, alternatePrefix,
				initialiser, recogniser, rootElements);
		
		this.schemeDefaults   = schemeDefaults;
		this.schemeCollection = schemeCollection;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 	TFP 1.0
	 */
	public final SchemeDefaults getSchemeDefaults ()
	{
		return (schemeDefaults);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public final SchemeCollection getSchemeCollection ()
	{
		return (schemeCollection);
	}
	
	/**
	 * The <CODE>SchemeDefaults</CODE>instance describing scheme values for
	 * this release.
	 * @since	TFP 1.0
	 */
	private final SchemeDefaults	schemeDefaults;
	
	/**
	 * The <CODE>SchemeCollection</CODE> instance containg the schemes used
	 * for validation.
	 * @since	TFP 1.0
	 */
	private final SchemeCollection	schemeCollection;
}