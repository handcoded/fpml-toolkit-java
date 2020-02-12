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

import java.util.Map;

/**
 * The <CODE>Formatter</CODE> interface defines the method used to format
 * a set of property data values into an identifier.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public interface Formatter
{
	/**
	 * Uses the set of property values extracted from the context object to
	 * create a formatted identifier string.
	 * 
	 * @param	values			The set of property values.
	 * @return	The identifier created from the values.
	 */
	public String format (final Map<String, String> values);
}