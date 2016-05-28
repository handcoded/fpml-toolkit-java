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

import com.handcoded.meta.Release;

public final class MessageType 
{
	public MessageType (final Release release, final String message)
	{
		this.release = release;
		this.message = message;
	}
	
	public Release getRelease ()
	{
		return (release);
	}
	
	public String getMessage ()
	{
		return (message);
	}
	
	public int hashCode ()
	{
		return (release.hashCode () ^ message.hashCode ());
	}
	
	public boolean equals (Object other)
	{
		return ((other instanceof MessageType) && equals ((MessageType) other));
	}
	
	public boolean equals (MessageType other)
	{
		return (release.equals (other.release) && message.equals (other.message));
	}
	
	public String toString ()
	{
		return (release.getVersion () + " : " + message);
	}
	
	private final Release		release;
	
	private final String		message;
}
