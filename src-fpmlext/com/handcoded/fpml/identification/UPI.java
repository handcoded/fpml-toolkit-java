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

package com.handcoded.fpml.identification;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handcoded.classification.Category;
import com.handcoded.dataencoding.Base32;
import com.handcoded.fpml.classification.ISDATaxonomy;
import com.handcoded.fpml.infoset.ProductInfoset;
import com.handcoded.identification.IdentifierRule;
import com.handcoded.identification.RuleBook;
import com.handcoded.identification.xml.RuleBookLoader;

/**
 * Each instance of the <CODE>UPI</CODE> class contains a 'Universal Product
 * Identifier' code. The <CODE>UPI</CODE> class provides utility methods to
 * derive identifiers from trades or product infosets.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class UPI
{
	/**
	 * Derives a <CODE>UPI</CODE> from the values in a trade description
	 * represented by the indicated DOM <CODE>Element</CODE>.
	 * 
	 * @param 	context			The root <CODE>Element</CODE> of the trade.
	 * @return	The derived <CODE>UPI</CODE> instance or <CODE>null</CODE>.
	 * @since	TFP 1.6
	 */
	public static UPI forTrade (final Element context)
	{
		Document	infoset		= ProductInfoset.createInfoset (context);
		
		if (infoset != null)
			return (forProductInfoset (infoset.getDocumentElement (),
						ISDATaxonomy.PRODUCT_TYPE.classify (infoset)));

		return (null);
	}
	
	/**
	 * Derives a <CODE>UPI</CODE> from the values in a trade description
	 * represented by the indicated DOM <CODE>Element</CODE> based on the
	 * target product taxonomy classification.
	 * 
	 * @param 	context			The root <CODE>Element</CODE> of the trade.
	 * @param 	productType		The target product taxonomy classification.
	 * @return	The derived <CODE>UPI</CODE> instance or <CODE>null</CODE>.
	 * @since	TFP 1.6
	 */
	public static UPI forTrade (final Element context, final Category productType)
	{
		Document	infoset		= ProductInfoset.createInfoset (context);
		
		if (infoset != null)
			return (forProductInfoset (infoset.getDocumentElement (), productType));

		return (null);
	}
	
	/**
	 * Derives a <CODE>UPI</CODE> from the values in the product infoset
	 * represented by the indicated DOM <CODE>Element</CODE>.
	 * 
	 * @param 	infoset			The root <CODE>Element</CODE> of the product infoset.
	 * @return	The derived <CODE>UPI</CODE> instance or <CODE>null</CODE>.
	 * @since	TFP 1.6
	 */
	public static UPI forProductInfoset (final Element infoset)
	{
		return (forProductInfoset (infoset, ISDATaxonomy.productTypeForInfoset (infoset)));
	}
	
	/**
	 * Derives a <CODE>UPI</CODE> from the values in the product infoset
	 * represented by the indicated DOM <CODE>Element</CODE> based on the
	 * target product taxonomy classification.
	 * 
	 * @param 	infoset			The root <CODE>Element</CODE> of the product infoset.
	 * @param	productType		The target product taxonomy classification.
	 * @return	The derived <CODE>UPI</CODE> instance or <CODE>null</CODE>.
	 * @since	TFP 1.6
	 */
	public static UPI forProductInfoset (final Element infoset, final Category productType)
	{
		if (productType != null) {
			IdentifierRule	rule	= ruleBook.find (productType.getName ());
			
			return ((rule != null) ? new UPI (rule.getIdentifier (infoset)) : null);			
		}
		return (null);
	}
	
	/**
	 * Returns the UPI code string.
	 * 
	 * @return	The UPI code string.
	 * @since	TFP 1.6
	 */
	public final String getCode ()
	{
		return (code);
	}
	
	/**
	 * Returns the UPI hash string.
	 * 
	 * @return	The UPI hash string.
	 * @since	TFP 1.6
	 */
	public final String getHash ()
	{
		return (hash);
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	@Override
	public final String toString ()
	{
		return (code + " {" + hash + "}");
	}
	
	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.6
	 */
	private static Logger	logger
		= Logger.getLogger ("com.handcoded.fpml.identification.UPI");

	/**
	 * The <CODE>RuleBook</CODE> that defines how to format UPI identifiers.
	 * @since	TFP 1.6
	 */
	private static final RuleBook	ruleBook
		= RuleBookLoader.load ("files-fpmlext/upi-identifiers.xml");
	
	/**
	 * The <CODE>MessageDigest</CODE> used to create the hashed value.
	 * @since	TFP 1.6
	 */
	private static MessageDigest 	messageDigest;
		
	/**
	 * The <CODE>UPI</CODE> code.
	 * @since	TFP 1.6
	 */
	private	final String	code;
	
	/**
	 * The <CODE>UPI</CODE> hash.
	 * @since	TFP 1.6
	 */
	private	final String	hash;

	/**
	 * Constructs a <CODE>UPI</CODE> instance for the indicates code value.
	 * Computes the hash value by passing the code through a one-way hash
	 * function and converting the result into a hexadecimal string.
	 * 
	 * @param 	key				The UPI key value.
	 * @since	TFP 1.6
	 */
	private UPI (final String code)
	{
		if ((this.code = code) != null) {
			synchronized (messageDigest) {
				byte []	digest = messageDigest.digest (code.getBytes ());
				
				hash = Base32.encode (digest);
			}
		}
		else
			hash = null;
	}
	
	/**
	 * Ensure the SHA-256 message digest algorithm is available.
	 * @since	TFP 1.6
	 */
	static {
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException error) {
			logger.log (Level.SEVERE, "Could not construct SHA-256 message digest - Must be installed", error);
		}
	}
}