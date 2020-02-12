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

package com.handcoded.identification.xml;

import com.handcoded.identification.Extractor;
import com.handcoded.identification.Source;

/**
 * A <CODE>StringExtractor</CODE> instance is used recover the string
 * values of one or more <CODE>Source</CODE> locations and combine them
 * into a result string.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class StringExtractor implements Extractor
{
	/**
	 * Constructs a <CODE>StringExtractor</CODE> instance.
	 * @since	TFP 1.6
	 */
	public StringExtractor ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public String extract (Object context, Source [] sources)
	{
		if (context != null) {
			synchronized (buffer) {
				buffer.setLength (0);
				
				for (int index = 0; index < sources.length; ++index)
					buffer.append ((String) sources [index].findSource (context));
				
				return (buffer.toString ());
			}
		}
		return (null);
	}
	
	/**
	 * <CODE>StringBuilder</CODE> used to buffer the intermediate value.
	 * @since	TFP 1.6
	 */
	private static StringBuilder		buffer = new StringBuilder ();
}