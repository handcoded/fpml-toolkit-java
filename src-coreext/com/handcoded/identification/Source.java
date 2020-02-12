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
 * The <CODE>Source</CODE> interface defines a standard method used to locate
 * data in a context <CODE>Object</CODE>.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
public interface Source
{
	/**
	 * Invokes the <CODE>Source</CODE> against the context <CODE>Object</CODE>
	 * to cause some data to be located for extraction.
	 * 
	 * @param	context			The <CODE>Object</CODE> to invoke against.
	 * @return	The data value located by the <CODE>Source</CODE> instance.
	 * @since	TFP 1.6
	 */
	public Object findSource (final Object context);
}