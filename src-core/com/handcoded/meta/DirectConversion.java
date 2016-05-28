// Copyright (C),2005-2011 HandCoded Software Ltd.
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

package com.handcoded.meta;

/**
 * The <CODE>DirectConversion</CODE> class describes a transformation
 * implemented by a derived class.
 * 
 * @author 	BitWise
 * @version	$Id: DirectConversion.java 485 2011-03-18 18:25:57Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class DirectConversion extends Conversion
{
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public final Release getSourceRelease ()
	{
		return (sourceRelease);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public final Release getTargetRelease ()
	{
		return (targetRelease);
	}
	
	/**
	 * Creates a simple string showing the versions that are the source and
	 * target for this <CODE>Conversion</CODE>.
	 * 
	 * @return	A debugging string describing the <CODE>Conversion</CODE>.
	 * @since	TFP 1.0 
	 */
	public final String toString ()
	{
		return (sourceRelease.getVersion () + "->" + targetRelease.getVersion ());
	}
	
	/**
	 * Constructs a <CODE>DirectConversion</CODE> which will transform between
	 * the specified releases.
	 *  
	 * @param 	sourceRelease	The <CODE>Release</CODE> to convert from.
	 * @param 	targetRelease	The <CODE>Release</CODE> to convert to.
	 * @since	TFP 1.0
	 */
	protected DirectConversion (Release sourceRelease, Release targetRelease)
	{
		this.sourceRelease = sourceRelease;
		this.targetRelease = targetRelease;
		
		if ((sourceRelease != null) && (targetRelease != null)) {
			sourceRelease.addSourceConversion (this);
			targetRelease.addTargetConversion (this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	protected final int complexity ()
	{
		return (1);
	}
	
	/**
	 * The source <CODE>Release</CODE> for this <CODE>Conversion</CODE>.
	 * @since	TFP 1.0
	 */
	private final Release		sourceRelease;
	
	/**
	 * The target <CODE>Release</CODE> for this <CODE>Conversion</CODE>.
	 * @since	TFP 1.0
	 */
	private final Release		targetRelease;
}