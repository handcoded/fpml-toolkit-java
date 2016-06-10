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

package com.handcoded.fpml.identification;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handcoded.classification.Category;
import com.handcoded.fpml.classification.ISDATaxonomy;
import com.handcoded.fpml.infoset.ProductInfoset;
import com.handcoded.identification.IdentifierRule;
import com.handcoded.identification.RuleBook;
import com.handcoded.identification.xml.RuleBookLoader;

/**
 * Instances of the <CODE>AlternateUPI</CODE> contain a derived product identifier
 * based on product characteristic values.
 * 
 * @author	BitWise
 * @version	$Id: AlternateUPI.java 23 2012-08-20 18:24:33Z andrew $
 * @since	TFP 1.6
 */
public final class AlternateUPI
{
	/**
	 * Derives a <b>UPI</b> from the values in a trade description
     * represented by the indicated DOM <CODE>Element</CODE>.
     * 
     * @param	context 		The root <CODE>Element</CODE> of the trade.
     * @return	The derived <b>UPI</b> instance or <CODE>null</CODE>.
     * @since	TFP 1.6
	 */
    public static AlternateUPI forTrade (Element context)
    {
	    Document	infoset		= ProductInfoset.createInfoset (context);
		
	    if (infoset != null)
		    return (forProductInfoset (infoset.getDocumentElement (),
					    ISDATaxonomy.PRODUCT_TYPE.classify (infoset)));

	    return (null);
    }
	
    /**
     * Derives a <b>UPI</b> from the values in a trade description
     * represented by the indicated DOM <CODE>Element</CODE> based on the
     * target product taxonomy classification.
     * 
     * @param	context			The root <CODE>Element</CODE> of the trade.
     * @param	productType		The target product taxonomy classification.
     * @return	The derived <b>UPI</b> instance or <CODE>null</CODE>.
     * @since	TFP 1.6
     */
    public static AlternateUPI forTrade (Element context, Category productType)
    {
	    Document	infoset		= ProductInfoset.createInfoset (context);
		
	    if (infoset != null)
		    return (forProductInfoset (infoset.getDocumentElement (), productType));

	    return (null);
    }

    /**
     * Derives a <b>UPI</b> from the values in the product infoset
     * represented by the indicated DOM <CODE>Element</CODE>.
     * 
     * @param	infoset			The root <CODE>Element</CODE> of the product infoset.
     * @return	The derived <b>UPI</b> instance or <CODE>null</CODE>.
     * @since	TFP 1.6
     */
    public static AlternateUPI forProductInfoset (Element infoset)
    {
	    return (forProductInfoset (infoset, ISDATaxonomy.productTypeForInfoset (infoset)));
    }
	
    /**
     * Derives a <b>UPI</b> from the values in the product infoset
     * represented by the indicated DOM <CODE>XmlElement</CODE> based on the
     * target product taxonomy classification.
     * 
     * @param	infoset			The root <CODE>Element</CODE> of the product infoset.
     * @param	productType		The target product taxonomy classification.
     * @return	The derived <b>UPI</b> instance or <CODE>null</CODE>.
     * @since	TFP 1.6
     */
    public static AlternateUPI forProductInfoset (Element infoset, Category productType)
    {
	    if (productType != null) {
		    IdentifierRule	rule	= ruleBook.find (productType.getName ());
			
		    return ((rule != null) ? new AlternateUPI (rule.getIdentifier (infoset)) : null);			
	    }
	    return (null);
    }

    /**
     * Contains the UPI code string.
     * 
     * @return	The UPI code string
     * @since	TFP 1.6
     */
    public String getCode ()
    {
        return (code);
    }

    /**
     * The <CODE>RuleBook</CODE> that defines how to format UPI infosets.
     * @since	TFP 1.6
     */
    private final static RuleBook	ruleBook
	    = RuleBookLoader.load ("files-fpmlext/upi-alternate.xml");

    /**
     * The <b>UPI</b> code string.
     * @since	TFP 1.6
     */
    private	final String 	code;

    /**
     * Constructs a <b>UPI</b> instance for the indicates code value.
     * 
     * @param	code			The UPI code value.
     * @since	TFP 1.6
     */
    private AlternateUPI (String code)
    {
	    this.code = code;
    }
}