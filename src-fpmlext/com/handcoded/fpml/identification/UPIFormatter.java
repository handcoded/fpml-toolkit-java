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

import java.util.Map;

import com.handcoded.identification.Formatter;

/**
 * The <CODE>UPIFormatter</CODE> class implements a <CODE>Formatter</CODE>
 * for UPI strings that uses a simple format string contained in a <CODE>Property</CODE>
 * called 'FormatString'.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public class UPIFormatter implements Formatter
{
	/**
	 * Constructs a <CODE>UPIFormatter</CODE>.
	 * @since	TFP 1.6
	 */
	public UPIFormatter ()
	{ }
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.6
	 */
	public String format (Map<String, String> values)
	{
		char []			format = values.get ("FormatString").toCharArray ();
		int				index  = 0;
		
		synchronized (buffer) {
			buffer.setLength (0);
			
			while (index < format.length) {
				if (format [index] == '%') {
					name.setLength (0);
					++index;
					
					while ((index < format.length) && (format [index] != '%'))
						name.append (format [index++]);
					
					buffer.append (values.get (name.toString ()));
					++index;					
				}
				else
					buffer.append (format [index++]);
			}
			return (buffer.toString ());
		}
	}
	
	/**
	 * A <CODE>StringBuilder</CODE> used to buffer the identifier as it is
	 * built up.
	 * @since	TFP 1.6
	 */
	private static StringBuilder buffer = new StringBuilder ();
	
	/**
	 * A <CODE>StringBuilder</CODE> used to buffer property names as they
	 * as parsed.
	 * @since	TFP 1.6
	 */
	private static StringBuilder name = new StringBuilder ();
}