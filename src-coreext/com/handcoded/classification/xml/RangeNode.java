// Copyright (C),2006-2020 HandCoded Software Ltd.
// All rights reserved.
//
// This software is the confidential and proprietary information of HandCoded
// Software Ltd. ("Confidential Information").  You shall not disclose such
// Confidential Information and shall use it only in accordance with the terms
// of the license agreement you entered into with HandCoded Software.
//
// HANDCODED SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
// SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. HANDCODED SOFTWARE SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
// OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

package com.handcoded.classification.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handcoded.meta.Specification;
import com.handcoded.meta.Release;
import com.handcoded.fpml.util.Version;

/**
 * A <CODE>RangeNode</CODE> instance determines if the document under test uses
 * a version of FpML in the specified range.
 *   
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
final class RangeNode extends ExprNode
{
	/**
	 * 
	 * @param 	specification
	 * @param 	lower
	 * @param 	upper
	 * @since	TFP 1.6
	 */
	public RangeNode (final Specification specification, final Release lower, final Release upper)
	{
		this.specification = specification;
		this.lower	   	   = (lower != null) ? Version.parse (lower.getVersion ()) : null;
		this.upper	   	   = (upper != null) ? Version.parse (upper.getVersion ()) : null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public boolean evaluate (final Object value)
	{
		Document	document = ((Element) value).getOwnerDocument ();
		Release		release	 = specification.getReleaseForDocument (document);
		Version		version	 = Version.parse (release.getVersion ());
		
		boolean validMin = (lower != null) ? (version.compareTo (lower) >= 0) : true;
		boolean validMax = (upper != null) ? (version.compareTo (upper) <= 0) : true;
		
		return (validMin & validMax);
	}

	/**
	 * The specification the document must match.
	 * @since	TFP 1.6
	 */
	private final Specification	specification;
	
	/**
	 * @since	TFP 1.6
	 */
	private final Version		lower;
	
	/**
	 * @since	TFP 1.6
	 */
	private final Version		upper;
}