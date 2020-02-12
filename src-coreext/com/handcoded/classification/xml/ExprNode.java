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

package com.handcoded.classification.xml;

/**
 * An instance of a class derived from the abstract <CODE>ExprNode</CODE> class
 * represents part of an expression used to classify a business object.
 *  
 * @author 	Andrew Jacobs
 * @since	TFP 1.6
 */
abstract class ExprNode
{
	/**
	 * Evaluates an criteria defined by this <CODE>ExprNode</CODE> against a
	 * selected context object.
	 * 
	 * @param 	context			The context for the evaluation.
	 * @return	A <CODE>boolean</CODE> indicating of the criteria was
	 * 			satisfied or not.
	 * @since	TFP 1.6
	 */
	public abstract boolean evaluate (final Object context);
}