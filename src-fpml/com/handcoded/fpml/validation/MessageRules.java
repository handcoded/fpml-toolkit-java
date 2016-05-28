// Copyright (C),2005-2013 HandCoded Software Ltd.
// All rights reserved.
//
// This software is licensed in accordance with the terms of the 'Open Source
// License (OSL) Version 3.0'. Please see 'license.txt' for the details.
//
// HANDCODED SOFTWARE LTD MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
// SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. HANDCODED SOFTWARE LTD SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
// OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

package com.handcoded.fpml.validation;

import com.handcoded.validation.Precondition;
import com.handcoded.validation.Rule;
import com.handcoded.validation.RuleSet;
import com.handcoded.validation.ValidationErrorHandler;
import com.handcoded.xml.NodeIndex;

/**
 * The <CODE>MessageRules</CODE> class contains a <CODE>RuleSet</CODE>
 * initialised with FpML defined validation rules for messages.
 *
 * @author	BitWise
 * @version	$Id: MessageRules.java 770 2013-11-12 20:25:11Z andrew_jacobs $
 * @since	TFP 1.7
 */
public final class MessageRules extends FpMLRuleSet
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects reporting, recordkeeping
     * or transparency view documents.
	 * @since	TFP 1.7
	 */	
	private static final Precondition	REPO_RECO_TRAN
		= Precondition.or (Preconditions.REPORTING,
				Precondition.or (Preconditions.RECORDKEEPING, Preconditions.TRANSPARENCY));
	
	/**
	 * A rule that ensures that only novation messages can have more than
	 * two onBehalfOf elements.
	 * @since	TFP 1.7
	 */
	public static final Rule 	RULE05 = new Rule (REPO_RECO_TRAN, "msg-5")
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected boolean validate (NodeIndex nodeIndex,
				ValidationErrorHandler errorHandler)
		{
			if (nodeIndex.getElementsByName ("onBehalfOf").getLength () > 2) {
				if (nodeIndex.getElementsByName ("novation").getLength () > 0)
					return (true);
				
				errorHandler.error ("305", nodeIndex.getDocument ().getDocumentElement (),
						"Only novation messages can be on behalf of more than two parties",
						getDisplayName (), null);
				
				return (false);		
			}
			return (true);
		}
	};
	
	/**
	 * Provides access to the message validation rule set.
	 *
	 * @return	The message validation rule set.
	 * @since	TFP 1.7
	 */
	public static RuleSet getRules ()
	{
		return (rules);
	}

	/**
	 * A <CODE>RuleSet</CODE> containing all the standard FpML defined
	 * validation rules for the collateral process.
	 * @since	TFP 1.7
	 */
	private static final RuleSet	rules = RuleSet.forName ("MessageRules");

	/**
	 * Ensures no instances can be created.
	 * @since	TFP 1.7
	 */
	private MessageRules ()
	{ }
}