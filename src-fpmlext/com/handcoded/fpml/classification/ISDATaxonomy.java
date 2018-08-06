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

package com.handcoded.fpml.classification;

import org.w3c.dom.Element;

import com.handcoded.classification.Category;
import com.handcoded.classification.ClassificationScheme;
import com.handcoded.classification.xml.ClassificationLoader;

/**
 * The <CODE>ISDATaxonomy</CODE> class provides easy access the to the
 * categories defined in the taxonomy provided by the ISDA product
 * working groups initiated in response to the Dodd-Frank Act.
 * 
 * The ISDA asset class and taxonomy categorisations are designed to be
 * applied to a product infoset (e.g. a normalised product description)
 * rather than a trade.
 *
 * @author 	BitWise
 * @version	$Id: ISDATaxonomy.java 10 2011-10-22 09:58:26Z andrew $
 * @since	FTP 1.6
 */
public final class ISDATaxonomy
{
	/**
	 * Attempts to classify the indicated infoset using the asset class taxonomy.
	 * 
	 * @param	infoset			The product infoset as a DOM tree.
	 * @return	The <CODE>Category</CODE> instance representing the asset class
	 * 			corresponding to the product infoset.
	 * @since	TFP 1.6
	 */
	public static Category assetClassForInfoset (final Element infoset)
	{
		return ((infoset != null) ? ASSET_CLASS.classify (infoset) : null);
	}
	
	/**
	 * Attempts to classify the indicated infoset using the product type taxonomy.
	 * 
	 * @param	infoset			The product infoset as a DOM tree.
	 * @return	The <CODE>Category</CODE> instance representing the product type
	 * 			corresponding to the product infoset.
	 * @since	TFP 1.6
	 */
	public static Category productTypeForInfoset (final Element infoset)
	{
		return ((infoset != null) ? PRODUCT_TYPE.classify (infoset) : null);
	}

	/**
	 * The ISDA asset class taxonomy.
	 * @since	TFP 1.6
	 */
	private static final ClassificationScheme	assetClassClassification
		= ClassificationLoader.load ("files-fpmlext/isda-asset-class.xml");
	
	/**
	 * The ISDA product type taxonomy.
	 * @since	TFP 1.6
	 */
	private static final ClassificationScheme	productTypeClassification
		= ClassificationLoader.load ("files-fpmlext/isda-product-type.xml");
		
	/**
	 * A <CODE>Category</CODE> representing all product types.
	 * @since	TFP 1.0
	 */
	public static final Category	ASSET_CLASS
		= assetClassClassification.getCategoryByName ("{ISDA}");
		
	/**
	 * A <CODE>Category</CODE> representing all product types.
	 * @since	TFP 1.0
	 */
	public static final Category	PRODUCT_TYPE
		= productTypeClassification.getCategoryByName ("{ISDA}");

	/**
	 * Prevents an instance being created.
	 * @since	TFP 1.6
	 */
	private ISDATaxonomy ()
	{ }
}