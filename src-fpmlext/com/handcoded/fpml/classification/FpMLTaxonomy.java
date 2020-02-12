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

package com.handcoded.fpml.classification;

import org.w3c.dom.Node;

import com.handcoded.classification.Category;
import com.handcoded.classification.ClassificationScheme;
import com.handcoded.classification.xml.ClassificationLoader;

/**
 * The <CODE>FpMLTaxonomy</CODE> class provides easy access the to the
 * categories defined in the FpML product based taxonomy provided by
 * HandCoded Software.
 * 
 * @author 	Andrew Jacobs
 * @since	FTP 1.6
 */
public final class FpMLTaxonomy
{
	public static final class AssetClass
	{
		/**
		 * A <CODE>Category</CODE> representing all documents forms.
		 * @since	TFP 1.6
		 */
		public static final Category	ASSET_CLASS
			= defaultClassification.getCategoryByName ("AssetClass");
		
		/**
		 * A <CODE>Category</CODE> representing all documents forms.
		 * @since	TFP 1.6
		 */
		public static final Category	INTEREST_RATE
			= defaultClassification.getCategoryByName ("InterestRate");
		
		/**
		 * A <CODE>Category</CODE> representing all documents forms.
		 * @since	TFP 1.6
		 */
		public static final Category	CREDIT
			= defaultClassification.getCategoryByName ("Credit");
		
		/**
		 * A <CODE>Category</CODE> representing all documents forms.
		 * @since	TFP 1.6
		 */
		public static final Category	EQUITY
			= defaultClassification.getCategoryByName ("Equity");
		
		/**
		 * A <CODE>Category</CODE> representing all documents forms.
		 * @since	TFP 1.6
		 */
		public static final Category	FOREIGN_EXCHANGE
			= defaultClassification.getCategoryByName ("ForeignExchange");
		
		/**
		 * A <CODE>Category</CODE> representing all documents forms.
		 * @since	TFP 1.6
		 */
		public static final Category	COMMODITY
			= defaultClassification.getCategoryByName ("Commodity");

		/**
		 * Prevents an instance being created.
		 * @since	TFP 1.6
		 */
		private AssetClass ()
		{ }
	}
	
	public static final class DocumentForm
	{
		/**
		 * A <CODE>Category</CODE> representing all documents forms.
		 * @since	TFP 1.6
		 */
		public static final Category	DOCUMENT_FORM
			= defaultClassification.getCategoryByName ("DocumentForm");
		
		/**
		 * A <CODE>Category</CODE> representing all short form documents in which some
		 * details of the trade are defined outside the transaction record.
		 * @since	TFP 1.6
		 */
		public static final Category	SHORT_FORM
			= defaultClassification.getCategoryByName ("ShortForm");

		/**
		 * A <CODE>Category</CODE> representing all long form documents in which all
		 * the details of the trade are defined within the transaction record.
		 * @since	TFP 1.6
		 */
		public static final Category	LONG_FORM
			= defaultClassification.getCategoryByName ("LongForm");
		
		/**
		 * Prevents an instance being created.
		 * @since	TFP 1.6
		 */
		private DocumentForm ()
		{ }
	}

	public static final class InstrumentType
	{
		/**
		 * A <CODE>Category</CODE> representing all instrument types.
		 * @since	TFP 1.6
		 */
		public static final Category	INSTRUMENT_TYPE
			= defaultClassification.getCategoryByName ("InstrumentType");
		
		/**
		 * A <CODE>Category</CODE> representing all instrument types.
		 * @since	TFP 1.6
		 */
		public static final Category	SECURITIES
			= defaultClassification.getCategoryByName ("Securities");
		
		/**
		 * A <CODE>Category</CODE> representing all instrument types.
		 * @since	TFP 1.6
		 */
		public static final Category	CASH
			= defaultClassification.getCategoryByName ("Cash");
		
		/**
		 * A <CODE>Category</CODE> representing all instrument types.
		 * @since	TFP 1.6
		 */
		public static final Category	EXCHANGE_TRADED
			= defaultClassification.getCategoryByName ("ExchangeTraded");
		
		/**
		 * A <CODE>Category</CODE> representing all instrument types.
		 * @since	TFP 1.6
		 */
		public static final Category	OTC_DERIVATIVE
			= defaultClassification.getCategoryByName ("OTCDerivative");
		
		/**
		 * Prevents an instance being created.
		 * @since	TFP 1.6
		 */
		private InstrumentType ()
		{ }	
	}
	
	public static final class OptionFeature
	{
		/**
		 * A <CODE>Category</CODE> representing all product types.
		 * @since	TFP 1.6
		 */
		public static final Category	OPTION_FEATURE
			= defaultClassification.getCategoryByName ("OptionFeature");

		/**
		 * A <CODE>Category</CODE> representing all product types.
		 * @since	TFP 1.6
		 */
		public static final Category	DIGITAL
			= defaultClassification.getCategoryByName ("Digital");
		
		/**
		 * A <CODE>Category</CODE> representing all product types.
		 * @since	TFP 1.6
		 */
		public static final Category	ASIAN
			= defaultClassification.getCategoryByName ("Asian");
		
		/**
		 * A <CODE>Category</CODE> representing all product types.
		 * @since	TFP 1.6
		 */
		public static final Category	BARRIER
			= defaultClassification.getCategoryByName ("Barrier");
		
		/**
		 * Prevents an instance being created.
		 * @since	TFP 1.6
		 */
		private OptionFeature ()
		{ }	
	}	
	
	public static final class ProductType
	{
		/**
		 * A <CODE>Category</CODE> representing any product type.
		 * @since	TFP 1.6
		 */
		public static final Category	PRODUCT_TYPE
			= defaultClassification.getCategoryByName ("ProductType");
		
		/**
		 * A <CODE>Category</CODE> representing any structure product.
		 * @since	TFP 1.6
		 */
		public static final Category	STRUCTURED_PRODUCT
			= defaultClassification.getCategoryByName ("StructuredProduct");
		
		/**
		 * A <CODE>Category</CODE> representing any option strategy.
		 * @since	TFP 1.6
		 */
		public static final Category	OPTION_STRATEGY
			= defaultClassification.getCategoryByName ("OptionStrategy");
		
		/**
		 * A <CODE>Category</CODE> representing a simple exchange of one thing
		 * for another.
		 * @since	TFP 1.6
		 */
		public static final Category	EXCHANGE
			= defaultClassification.getCategoryByName ("Exchange");
		
		/**
		 * A <CODE>Category</CODE> representing any type of derivative.
		 * @since	TFP 1.6
		 */
		public static final Category	DERIVATIVE
			= defaultClassification.getCategoryByName ("Derivative");
		
		/**
		 * A <CODE>Category</CODE> representing any future.
		 * @since	TFP 1.6
		 */
		public static final Category	FUTURE
			= defaultClassification.getCategoryByName ("Future");
		
		/**
		 * A <CODE>Category</CODE> representing any forward.
		 * @since	TFP 1.6
		 */
		public static final Category	FORWARD
			= defaultClassification.getCategoryByName ("Forward");
		
		/**
		 * A <CODE>Category</CODE> representing any option.
		 * @since	TFP 1.6
		 */
		public static final Category	OPTION
			= defaultClassification.getCategoryByName ("Option");
		
		/**
		 * A <CODE>Category</CODE> representing any compound option.
		 * @since	TFP 1.6
		 */
		public static final Category	COMPOUND_OPTION
			= defaultClassification.getCategoryByName ("CompoundOption");
		
		/**
		 * A <CODE>Category</CODE> representing any option strip.
		 * @since	TFP 1.6
		 */
		public static final Category	OPTION_STRIP
			= defaultClassification.getCategoryByName ("OptionStrip");
		
		/**
		 * A <CODE>Category</CODE> representing any cap.
		 * @since	TFP 1.6
		 */
		public static final Category	CAP
			= defaultClassification.getCategoryByName ("Cap");
		
		/**
		 * A <CODE>Category</CODE> representing any floor.
		 * @since	TFP 1.6
		 */
		public static final Category	FLOOR
			= defaultClassification.getCategoryByName ("Floor");
		
		/**
		 * A <CODE>Category</CODE> representing any collar.
		 * @since	TFP 1.6
		 */
		public static final Category	COLLAR
			= defaultClassification.getCategoryByName ("Collar");
		
		/**
		 * A <CODE>Category</CODE> representing any swap.
		 * @since	TFP 1.6
		 */
		public static final Category	SWAP
			= defaultClassification.getCategoryByName ("Swap");
		
		/**
		 * A <CODE>Category</CODE> representing any swaption.
		 * @since	TFP 1.6
		 */
		public static final Category	SWAPTION
			= defaultClassification.getCategoryByName ("Swaption");
		
		/**
		 * Prevents an instance being created.
		 * @since	TFP 1.6
		 */
		private ProductType ()
		{ }	
	}
	
	/**
	 * Apply the FpML taxonomy to a trade (or contract) element to determine
	 * its categorisation.
	 * 
	 * @param	context			The trade (or contract) context.
	 * @return	The <CODE>Category</CODE> instance representing the recognised
	 * 			product type.
	 * @since	TFP 1.6
	 */
	public static Category categoryForTrade (final Node context)
	{
		return (FPML.classify (context));
	}
	
	/**
	 * The default FpML Product classification.
	 * @since	TFP 1.6
	 */
	private static ClassificationScheme	defaultClassification
		= ClassificationLoader.load ("files-fpmlext/fpml-classification.xml");
	
	/**
	 * A <CODE>Category</CODE> representing all product types.
	 * @since	TFP 1.6
	 */
	public static final Category	FPML
		= defaultClassification.getCategoryByName ("FpML");

	/**
	 * Prevents an instance being created.
	 * @since	TFP 1.6
	 */
	private FpMLTaxonomy ()
	{ }
}