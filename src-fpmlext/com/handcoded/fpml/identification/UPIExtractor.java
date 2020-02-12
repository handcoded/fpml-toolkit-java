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

import com.handcoded.identification.Extractor;
import com.handcoded.identification.Source;

/**
 * The <CODE>UPIExtractor</CODE> class combines the individual source strings
 * using a '~' character as a delimiter.
 * 
 * @author	Andrew Jacobs
 * @since	TFP 1.6
 */
public final class UPIExtractor implements Extractor
{
    /**
     * Constructs a <b>UPIExtractor</b> instance.
     * @since	TFP 1.6
     */
    public UPIExtractor ()
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
				
			    for (int index = 0; index < sources.length; ++index) {
                    if (index != 0) buffer.append ('~');
				    buffer.append ((String) sources [index].findSource (context));
                }
				
			    return (buffer.toString ());
		    }
	    }
	    return (null);
    }
	
    /**
     * The <CODE>StringBuilder</CODE> used to buffer the intermediate value.
     * @since	TFP 1.6
     */
    private static StringBuilder	buffer = new StringBuilder ();
}