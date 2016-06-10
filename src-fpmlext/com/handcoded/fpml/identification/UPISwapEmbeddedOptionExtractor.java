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

import com.handcoded.identification.Extractor;
import com.handcoded.identification.Source;

public class UPISwapEmbeddedOptionExtractor implements Extractor
{
    /**
     * Constructs a <CODE>SwapEmbeddedOptionExtractor</CODE> instance.
     * @since	TFP 1.6
     */
    public UPISwapEmbeddedOptionExtractor ()
    { }

    /**
     * {@inheritDoc}
	 * @since	TFP 1.6
     */
    public String extract (Object context, Source [] sources)
    {
        if ((context != null) && (sources.length == 3)) {
            boolean early   = Boolean.parseBoolean ((String)(sources [0].findSource (context)));
            boolean cancel  = Boolean.parseBoolean ((String)(sources [1].findSource (context)));
            boolean extend  = Boolean.parseBoolean ((String)(sources [2].findSource (context)));

            if (early || cancel || extend)
                return (((early)  ? "T" : "") +
                        ((cancel) ? "C" : "") +
                        ((extend) ? "X" : ""));
        }
        return ("");
    }
}