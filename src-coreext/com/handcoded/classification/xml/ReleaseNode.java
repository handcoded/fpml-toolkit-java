// Copyright (C),2006-2011 HandCoded Software Ltd.
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

/**
 * 
 * @author 	BitWise
 * @version	$Id: ReleaseNode.java 5 2011-09-23 17:21:23Z andrew $
 * @since	TFP 1.6
 */
final class ReleaseNode extends ExprNode
{
	/**
	 * 
	 * @param 	specification
	 * @param 	release
	 * @since	TFP 1.6
	 */
	public ReleaseNode (final Specification specification, final Release release)
	{
		this.specification = specification;
		this.release	   = release;
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public boolean evaluate (final Object value)
	{
		Document	document = ((Element) value).getOwnerDocument ();
		
		return (specification.getReleaseForDocument (document) == release);
	}

	/**
	 * The specification the document must match.
	 * @since	TFP 1.6
	 */
	private final Specification	specification;
	
	/**
	 * The version the document must match.
	 * @since	TFP 1.6
	 */
	private final Release		release;
}