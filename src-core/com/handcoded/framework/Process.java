// Copyright (C),2005-2007 HandCoded Software Ltd.
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

package com.handcoded.framework;

/**
 * The <CODE>Process</CODE> class provides a basic process framework.
 * Derived classes extend its functionality and specialise it to a
 * particular task.
 *
 * @author	BitWise
 * @version	$Id: Process.java 155 2007-05-24 07:36:21Z andrew_jacobs $
 * @since	TFP 1.1
 */
public abstract class Process
{
	/**
	 * Causes the <CODE>Process</CODE> to begin execution.
	 * @since	TFP 1.0
	 */
	public void run ()
	{
		startUp ();
		while (!finished)
			execute ();
		cleanUp ();
	}

	/**
	 * Provides access to the flag used to determine <CODE>Application</CODE>
	 * execution.
	 *
	 * @return	<CODE>true</CODE> if execution is finished, <CODE>false</CODE>
	 *			otherwise.
	 * @since	TFP 1.0
	 */
	public final boolean isFinished ()
	{
		return (finished);
	}

	/**
	 * Allows the caller to change the value of the finished flag.
	 *
	 * @param	finished			The new value for the flag.
	 * @since	TFP 1.0
	 */
	public final void setFinished (final boolean finished)
	{
		this.finished = finished;
	}

	/**
	 * Constructs a <CODE>Process</CODE> instance.
	 * @since	TFP 1.1
	 */
	protected Process ()
	{ }
	
	/**
	 * The <CODE>startUp</CODE> method provides a place to actions
	 * to be performed as the <CODE>Process</CODE> initialises.
	 * @since	TFP 1.1
	 */
	protected void startUp ()
	{ }
	
	/**
	 * The <CODE>execute</CODE> method should perform one program execution
	 * cycle. The method is called repeatedly until the finished flag is set.
	 * @since	TFP 1.0
	 */
	protected abstract void execute ();

	/**
	 * The <CODE>cleanUp</CODE> method provides a place to actions
	 * to be performed as the <CODE>Process</CODE> terminates.
	 * @since	TFP 1.1
	 */
	protected void cleanUp ()
	{ }
	
	/**
	 * A <CODE>boolean</CODE> flag to indicate that we are done
	 * @since	TFP 1.0
	 */
	private boolean				finished 			= false;
}