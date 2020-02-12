// Copyright (C),2006-2020 HandCoded Software Ltd.
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

import com.handcoded.meta.DefaultSchemaRecogniser;
import com.handcoded.meta.SchemaRelease;

/**
 * The <CODE>FpMLSchemaRecogniser</CODE> extends the behaviour of the
 * <CODE>DefaultSchemaRecogniser</CODE> to look for a 'version' attribute
 * containing an appropriate version number.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.1
 */
public final class FpMLSchemaRecogniser extends DefaultSchemaRecogniser
{
	/**
	 * Constructs a <CODE>FpMLSchemaRecogniser</CODE> instance.
	 * @since	TFP 1.1
	 */
	public FpMLSchemaRecogniser ()
	{ }

	/**
	 * {@inheritDoc}
	 * <P>
	 * Also checks that the FpML version attribute matches the <CODE>SchemaRelease</CODE>
	 * instance.
	 * 
	 * @since	TFP 1.1
	 */
	public boolean recognises (SchemaRelease release, Document document)
	{
		if (super.recognises (release, document)) {
			int majorVersion = Integer.parseInt (release.getVersion().split("-")[0]);

			if (majorVersion <= 4) {
				if (document.getDocumentElement ().getAttribute ("version").equals (release.getVersion ()))
					return (true);
			}
			else {
				if (document.getDocumentElement ().getAttribute ("fpmlVersion").equals (release.getVersion ()))
					return (true);
			}
		}
		return (false);
	}
}