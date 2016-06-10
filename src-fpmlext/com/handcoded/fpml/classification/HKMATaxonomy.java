// Copyright (C),2006-2012 HandCoded Software Ltd.
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

package com.handcoded.fpml.classification;

import org.w3c.dom.Node;

import com.handcoded.classification.Category;
import com.handcoded.classification.ClassificationScheme;
import com.handcoded.classification.xml.ClassificationLoader;

/**
 * The <CODE>HKMATaxonomy</CODE> class provides easy access the to the
 * categories defined in the product based taxonomy provided by Hong Kong
 * Monetary Authority.
 * 
 * @author	BitWise
 * @version	$Id$
 * @since	TFP 1.6
 */
public final class HKMATaxonomy
{
	/**
	 * Apply the HKMA taxonomy to a trade (or contract) element to determine
	 * its categorisation.
	 * 
	 * @param	context			The trade (or contract) context.
	 * @return	The <CODE>Category</CODE> instance representing the recognised
	 * 			product type.
	 * @since	TFP 1.6
	 */
	public static Category categoryForTrade (final Node context)
	{
		return (HKMA.classify (context));
	}
	
	/**
	 * The default FpML Product classification.
	 * @since	TFP 1.6
	 */
	private static ClassificationScheme	defaultClassification
		= ClassificationLoader.load ("files-fpmlext/hkma-classification.xml");
	
	/**
	 * A <CODE>Category</CODE> representing all product types.
	 * @since	TFP 1.6
	 */
	public static final Category	HKMA
		= defaultClassification.getCategoryByName ("{HKMA}");

	/**
	 * Prevents an instance being created.
	 * @since	TFP 1.6
	 */
	private HKMATaxonomy ()
	{ }
}
