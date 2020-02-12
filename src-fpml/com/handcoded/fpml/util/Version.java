// Copyright (C),2005-2020 HandCoded Software Ltd.
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

package com.handcoded.fpml.util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Instances of the <CODE>Version</CODE> class hold a version number expressed
 * as two major and minor component values. Instances can be compared to
 * determine equality and relative ordering.
 *  
 * @author	Andrew Jacobs
 * @since	TFP 1.5
 */
public final class Version implements Serializable, Comparable<Version>
{
	/**
	 * Parse the FpML version number given as an argument and return a corresponding
	 * <CODE>Version</CODE> instance. A hash table is used to lookup previously
	 * converted strings.
	 * 
	 * @param 	version			The version number string.
	 * @return	A <CODE>Version</CODE> instance containing the decoded major and
	 * 			minor values.
	 * @since	TFP 1.6
	 */
	public static Version parse (final String version)
	{
		return (extent.computeIfAbsent (version, k -> new Version (k)));
	}
	
	/**
	 * Provides access to the major number component.
	 * @return	The major number.
	 * @since	TFP 1.5
	 */
	public int getMajor ()
	{
		return (major);
	}
	
	/**
	 * Provides access to the major number component.
	 * @return	The minor number.
	 * @since	TFP 1.5
	 */
	public int getMinor ()
	{
		return (minor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.5
	 */
	public boolean equals (final Object other)
	{
		return ((other instanceof Version) && equals ((Version) other));
	}
	
	/**
	 * Compares two <CODE>Version</CODE> instances and determines if they
	 * contain the same version number.
	 * 
	 * @param	other			The <CODE>Version</CODE> instance to compare with.
	 * @return	<CODE>true</CODE> if both instances contain the same major and
	 * 			minor component values.
	 * @since	TFP 1.5
	 */
	public boolean equals (final Version other)
	{
		return ((major == other.major) && (minor == other.minor));
	}
		
	/**
	 * Compares two <CODE>Version</CODE> instances to determine their relative
	 * ordering. 
	 * 
	 * @param	other			The <CODE>Version</CODE> instance to compare with.
	 * @return	A negative value if this instance is less than the other, a
	 * 			positive value if it is greater and zero if both are the same.
	 * @since	TFP 1.5
	 */
	public int compareTo (final Version other)
	{
		if (major == other.major) {
			if (minor == other.minor)
				return (0);
			else
				return ((minor < other.minor) ? -1 : 1);
		}
		else
			return ((major < other.major) ? -1 : 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.5
	 */
	public int hashCode ()
	{
		return (toString ().hashCode ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.5
	 */
	public String toString ()
	{
		return (major + "-" + minor);
	}
		
	/**
	 * Serialization UID
	 * @since	TFP 1.5
	 */
	private static final long	serialVersionUID	= 8982448319622238464L;
	
	/**
	 * The extent set of all <CODE>Version</CODE> instances.
	 * @since	TFP 1.6
	 */
	private static HashMap<String, Version> extent
		= new HashMap<> ();
	
	/**
	 * The major component of the version number.
	 * @since	TFP 1.5
	 */
	private final int		major;
	
	/**
	 * The minor component of the version number.
	 * @since	TFP 1.5
	 */
	private final int		minor;
	
	/**
	 * Constructs a <CODE>Version</CODE> instance from a 'major-minor' format
	 * string value.
	 * 
	 * @param 	version			The version number string.
	 * @since	TFP 1.5
	 */
	private Version (final String version)
	{
		this (version.split ("-"));
	}
	
	/**
	 * Constructs a <CODE>Version</CODE> instance from an array of two
	 * strings containing the component values.
	 * 
	 * @param	parts			The version number parts.
	 * @since	TFP 1.5
	 */
	private Version (final String [] parts)
	{
		this (Integer.parseInt (parts [0]), Integer.parseInt (parts [1]));
	}
	
	/**
	 * Constructs a <CODE>Version</CODE> instance from major and minor
	 * component values.
	 * 
	 * @param	major			The major number component.
	 * @param	minor			The minor number component.
	 * @since	TFP 1.5
	 */
	private Version (int major, int minor)
	{
		this.major = major;
		this.minor = minor;
	}
}