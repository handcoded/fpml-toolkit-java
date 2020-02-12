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

package com.handcoded.identification;

/**
 * Each instance of the <CODE>Property</CODE> class represents a data value
 * used in the formation of an identifier.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class Property
{
	/**
	 * Constructs a <CODE>Property</CODE> with the given name, extractor and
	 * source instances.
	 * 
	 * @param 	name			The <CODE>Property</CODE> name.
	 * @param 	extractor		The <CODE>Extractor</CODE> instance.
	 * @param 	sources			The <CODE>Source</CODE> instances.
	 * @since	TFP 1.6
	 */
	public Property (final String name, final Extractor extractor, final Source [] sources)
	{
		this.name 	   = name;
		this.extractor = extractor;
		this.sources   = sources;
	}
	
	/**
	 * Returns the name of this <CODE>Property</CODE>.
	 * 
	 * @return	The <CODE>Property</CODE> name string.
	 * @since	TFP 1.6
	 */
	public String getName ()
	{
		return (name);
	}
	
	/**
	 * Returns the value if this <CODE>Property</CODE> for the indicated context
	 * <CODE>Object</CODE>.
	 * 
	 * @param 	context			The context <CODE>Object</CODE>.
	 * @return	The value derived by extracting and combining the data sources.
	 * @since	TFP 1.6
	 */
	public String getValue (final Object context)
	{
		return (extractor.extract(context, sources));
	}
	
	/**
	 * The <CODE>Property</CODE> name.
	 * @since	TFP 1.6
	 */
	private final String	name;
	
	/**
	 * The <CODE>Extractor</CODE> instance used to extract the value.
	 * @since	TFP 1.6
	 */
	private final Extractor	extractor;
	
	/**
	 * An array of <CODE>Source</CODE> instance used to locate the values.
	 * @since	TFP 1.6
	 */
	private final Source []	sources;
}