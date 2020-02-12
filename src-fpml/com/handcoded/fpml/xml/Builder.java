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

package com.handcoded.fpml.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handcoded.fpml.meta.SchemaRelease;

/**
 * The <CODE>Builder</CODE> class extends <CODE>com.handcoded.xml.Builder</CODE>
 * with an understanding of FpML and its namespaces.
 *
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class Builder extends com.handcoded.xml.Builder
{
	/**
	 * Constructs a <CODE>Builder</CODE> instance attached to the root element
	 * of the given <CODE>Document</CODE>.
	 *
	 * @param	document		The <CODE>Document</CODE> to attach to.
	 * @since	TFP 1.0
	 */
	public Builder (Document document)
	{
		super (document);
	}

	/**
	 * Constructs a <CODE>Builder</CODE> instance attached to the specified
	 * <CODE>Element</CODE> of the given <CODE>Document</CODE>.
	 *
	 * @param 	document		The <CODE>Document</CODE> to attach to.
	 * @param	context			The initial context <CODE>Element</CODE>.
	 * @since	TFP 1.0
	 */
	public Builder (Document document, Element context)
	{
		super (document, context);
	}

	/**
	 * Constructs a <CODE>Builder</CODE> instance attached the root element of
	 * a new FpML document of the given <CODE>SchemaRelease</CODE>.
	 *
	 * @param	release			The FpML <CODE>SchemaRelease</CODE> to construct.
	 * @since	TFP 1.0
	 */
	public Builder (SchemaRelease release)
	{
		super (release.newInstance ("FpML"));
	}

	/**
	 * Constructs a <CODE>Builder</CODE> instance attached the root element of
	 * a new FpML document of the given <CODE>SchemaRelease</CODE> and which
	 * has the indicated message type.
	 *
	 * @param	release			The FpML <CODE>SchemaRelease</CODE> to construct.
	 * @param	type			The document or message type to construct.
	 */
	public Builder (SchemaRelease release, final String type)
	{
		this (release);

		setAttribute (SchemaRelease.INSTANCE_URL, "xsi:type", type);
	}
}