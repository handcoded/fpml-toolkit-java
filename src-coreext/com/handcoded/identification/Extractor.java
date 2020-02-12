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
 * The <CODE>Extractor</CODE> interface defines the method used to extract the
 * value of a property from sources within the context object.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public interface Extractor
{
	/**
	 * Extract a value of a property using the context object and the given
	 * source instances to find it.
	 * 
	 * @param	context			The source <CODE>Object</CODE> to obtain data from.
	 * @param	sources			An array of <CODE>Source</CODE> instances that define
	 * 							where the data is located.
	 * @return	The extracted data <CODE>String</CODE> or <CODE>null</CODE>.
	 */
	public String extract (final Object context, final Source [] sources);
}