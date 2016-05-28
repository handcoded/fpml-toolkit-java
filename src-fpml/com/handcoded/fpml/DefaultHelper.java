// Copyright (C),2005-2009 HandCoded Software Ltd.
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

package com.handcoded.fpml;

import org.w3c.dom.Element;

/**
 * The <CODE>DefaultHelper</CODE> class provides an implementation of all the
 * interfaces defined by the conversion objects and can be used by
 * applications that don't want to define thier own instance.
 * 
 * @author	BitWise
 * @version	$Id$
 * @since	TFP 1.3
 */
public class DefaultHelper implements Conversions.R4_0__R4_1.Helper
{
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.3
	 */
	public String getReferenceCurrency (final Element context)
	{
		return ("???");
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.3
	 */
	public String getQuantoCurrency1 (final Element context)
	{
		return ("???");
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.3
	 */
	public String getQuantoCurrency2 (final Element context)
	{
		return ("???");
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.3
	 */
	public String getQuantoCurrencyBasis (final Element context)
	{
		return ("???");
	}
}
