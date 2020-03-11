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

package com.handcoded.framework;

import java.util.ArrayList;

/**
 * The <CODE>Option</CODE> class provide a basic command line processing
 * capability. Instances of <CODE>Option</CODE> define the keywords to look
 * for and the presence of associated parameters. The application should pass
 * the entire set of command line strings received from <CODE>main</CODE> to
 * the processing function. The state of any option referenced by the strings
 * is updated and any remaining strings are returned to the caller.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class Option
{
	/**
	 * Constructs a <CODE>Option</CODE> instance for an option that has an
	 * associated parameter value (e.g. -output &lt;file&gt;).
	 *
	 * @param	name			The name of the option (e.g. -output).
	 * @param	description		A description of the options purpose.
	 * @param	parameter		A description of the required parameter or
	 *							<CODE>null</CODE> if none allowed.
	 * @since	TFP 1.0
	 */
	public Option (final String	name, final String description, final String parameter)
	{
		this.name		 = name;
		this.description = description;
		this.parameter   = parameter;
		
		options.add (this);	
	}
	
	/**
	 * Constructs a <CODE>Option</CODE> instance for an option that does not
	 * have a parameter.
	 *
	 * @param	name			The name of the option (e.g. -help).
	 * @param	description		A description of the options purpose.
	 * @since	TFP 1.0
	 */
	public Option (final String	name, final String description)
	{
		this (name, description, null);
	}
	
	/**
	 * After processing indicates if the <CODE>Option</CODE> was present on
	 * the command line.
	 *
	 * @return	<CODE>true</CODE> if the <CODE>Option</CODE> was present.
	 * @since	TFP 1.0
	 */
	public final boolean isPresent ()
	{
		return (present);
	}
	
	/**
	 * After processing allows access to the value of provided parameter.
	 *
	 * @return	The value supplied as a parameter or <CODE>null</CODE> if
	 *			the option was not present.
	 * @since	TFP 1.0
	 */
	public String getValue ()
	{
		return (value);
	}
	
	/**
	 * Converts the instance data members to a <CODE>String</CODE> representation
	 * that can be displayed for debugging purposes.
	 *
	 * @return 	The object's <CODE>String</CODE> representation.
	 * @since	TFP 1.0
	 */ 
	public String toString ()
	{
		return (getClass ().getName () + "[" + toDebug () + "]");
	}
	
	/**
	 * Processes the command line arguments to extract options and parameter
	 * values.
	 *
	 * @param	arguments		The command line arguments passed to <CODE>main</CODE>.
	 * @return	The remaining command line arguments after options have been
	 *			removed.
	 * @since	TFP 1.0
	 */
	public static String [] processArguments (final String [] arguments)
	{
		int				index;
		String []		remainder;
		
		// Attempt to match options with command line
		for (index = 0; index < arguments.length; ++index) {
			boolean matched = false;
			
			for (Option option : options) {
				if (arguments [index].equals (option.name)) {
					option.present = true;
					matched = true;
					
					if (option.parameter != null)
						option.value = arguments [++index];
					break;
				}
			}
				
			if (!matched) break;			
		}

		// Copy the tail of the argument list to a new array
		remainder = new String [arguments.length - index];
		for (int count = 0; index < arguments.length; ++count)
			remainder [count] = arguments [index++];
		
		return (remainder);
	}
	
	/**
	 * Returns a string describing the available command line options.
	 * 
	 * @return	A string describing the command line options.
	 * @since	TFP 1.0
	 */
	public static String listOptions ()
	{
		StringBuilder	buffer = new StringBuilder ();
		
		for (Option option : options) {			
			if (buffer.length () == 0) buffer.append (' ');
			
			buffer.append ('[');
			buffer.append (option.name);
			if (option.parameter != null) {
				buffer.append (' ');
				buffer.append (option.parameter);
			}
			buffer.append (']');
		}
		
		return (buffer.toString ());
	}
	
	/**
	 * Prints out a description of the options and their parameters.
	 * 
	 * @since	TFP 1.0
	 */
	public static void describeOptions ()
	{
		String 		spaces = "                                            ";

		for (Option option : options) {
			if (option.parameter != null)
				System.out.println ("    "
					+ (option.name + " " + option.parameter + spaces).substring (0, 16)
					+ " " + option.description);
			else
				System.out.println ("    "
					+ (option.name + spaces).substring (0, 16)
					+ " " + option.description);
		}
	}

	/**
	 * Converts the instance's member values to <CODE>String</CODE> representations
	 * and concatenates them all together. This function is used by toString and
	 * may be overriden in derived classes.
	 *
	 * @return	The object's <CODE>String</CODE> representation.
	 * @since	TFP 1.0
	 */
	protected String toDebug ()
	{
		StringBuilder		buffer 	= new StringBuilder ();
		
		buffer.append ("name="
			+ ((name != null) ? name : "null"));
		buffer.append (",description="
			+ ((description != null) ? description : "null"));
		buffer.append (",parameter="
			+ ((parameter != null) ? parameter : "null"));
		buffer.append (",present=" + present);
		buffer.append (",value="
				+ ((value != null) ? value : "null"));
		
		return (buffer.toString ());
	}
	
	/**
	 * The set of all defined <CODE>Option</CODE> instances.
	 * @since	TFP 1.0
	 */
	private static ArrayList<Option>	options	
		= new ArrayList<> ();
	
	/**
	 * The name of the option (including any leading dash).
	 * @since	TFP 1.0
	 */
	private final String	name;
	
	/**
	 * A brief description of the purpose.
	 * @since	TFP 1.0
	 */
	private final String	description;
	
	/**
	 * A brief description of the parameter (if any).
	 * @since	TFP 1.0
	 */
	private final String	parameter;
	
	/**
	 * A flag to indicate that the option was present.
	 * @since	TFP 1.0
	 */
	private boolean			present 	= false;
	
	/**
	 * The actual value provided on the command line.
	 * @since	TFP 1.0
	 */
	private String			value 		= null;
}