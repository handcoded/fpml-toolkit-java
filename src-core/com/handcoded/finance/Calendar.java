// Copyright (C),2005-2012 HandCoded Software Ltd.
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

package com.handcoded.finance;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.handcoded.framework.Application;
import com.handcoded.xml.XmlUtility;
import com.handcoded.xml.parser.SAXParser;

/**
 * <CODE>Calendar</CODE> instances implement the rules for determining is a
 * <CODE>Date</CODE> is a business day in the location it represents.
 * <P>
 * The rules for public holidays vary from country to country and with the
 * exception of special occasions (e.g. royal weddings, funerals, etc.) can
 * usually be predicted years in advance.
 * <P>
 * On first reference this class bootstraps an initial set of <CODE>Calendar
 * </CODE> instance by processing XML file data/calendars.xml contained in the
 * Java archive.
 *
 * @author	BitWise
 * @version	$Id: Calendar.java 666 2012-10-10 15:47:13Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class Calendar
{
	/**
	 * If a <CODE>Calendar</CODE> with the given name exists in the extent
	 * set then a reference to it is returned to the caller.
	 * 
	 * @param 	name			The required <CODE>Calendar</CODE> name.
	 * @return	A reference to a <CODE>Calendar</CODE> instance or <CODE>null
	 * 			</CODE> if the name was not found.
	 * @since	TFP 1.0
	 */
	public static Calendar forName (final String name)
	{
		return (extent.get (name));
	}
	
	/**
	 * Constructs an array containing all of the <CODE>Calendars</CODE>
	 * instances in the extent set at the time its method is called. 
	 * 
	 * @return	The members of the extent set as an array.
	 * @since	TFP 1.0
	 */
	public synchronized static Calendar []  extentAsArray ()
	{
		Enumeration<String>	cursor 	= extent.keys ();
		Calendar []		result	= new Calendar [extent.size ()];
	
		for (int index = 0; cursor.hasMoreElements (); ++index)
			result [index] = (Calendar) extent.get (cursor.nextElement());
		
		return (result);
	}
	
	/**
	 * Provides access to the name of the <CODE>Calendar</CODE>.
	 *  
	 * @return	The name of the <CODE>Calendar</CODE>.
	 * @since	TFP 1.0
	 */
	public final String getName ()
	{
		return (name);
	}
	
	/**
	 * Determines if the <CODE>Date</CODE> provided falls on a business day
	 * in this <CODE>Calendar</CODE> (eg not a holiday or the weekend).
	 *
	 * @param	date			The <CODE>Date</CODE> to be tested.
	 * @return	<CODE>true</CODE> if the date is a business day,
	 *			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public abstract boolean isBusinessDay (final Date date);
	
	/**
	 * Converts the <CODE>Calendar</CODE> to a string for debugging.
	 *
	 * @return 	A text description of the instance.
	 * @since	TFP 1.0
	 */
	public final String toString ()
	{
		return (getClass ().getName () + " [" + toDebug () + "]");
	}
	
	/**
	 * Constructs a <CODE>Calendar</CODE> instance with a given name.
	 *
	 * @param	name			The reference name for the instance.
	 * @since	TFP 1.0
	 */
	protected Calendar (final String name)
	{
		if ((this.name = name) != null)
			extent.put (name, this);
	}
	
	/**
	 * Produces a debugging string describing the state of the instance.
	 *
	 * @return 	The debugging string.
	 * @since	TFP 1.0
	 */
	protected String toDebug ()
	{
		return ("name=\"" + name + "\"");
	}
	
	/**
	 * The <CODE>BootStrap</CODE> class provides a simple extension of the
	 * SAX <CODE>DefaultHandler</CODE> that captures the key values that
	 * define calendars from an XML initialisation file.
	 * 
	 * @since	TFP 1.0
	 */
	private static final class BootStrap extends DefaultHandler
	{
		/**
		 * This method is called when the XML parser has just processed the
		 * start of an XML element. For some elements this triggers an
		 * immediate response, for others it saves information is temporary
		 * variables to be acted upon later. 
		 * @since 	TFP 1.0 
		 */
		public void startElement (String ns, String localName, String qName, Attributes attributes)
		{
			if (localName.equals ("calendar")) {
				String		name	= attributes.getValue ("name");
				Weekend		weekend	= Weekend.forName (attributes.getValue ("weekend"));

				if ((name != null) && (weekend != null))
					calendar = new RuleBasedCalendar (name, weekend);
				else
					calendar = null;
			}
			else if (localName.equals ("fixed")) {
				String		name	 = attributes.getValue("name");
				int			from	 = Integer.parseInt (
											isNull (attributes.getValue ("from"), DEFAULT_FROM));
				int			until	 = Integer.parseInt (
											isNull (attributes.getValue ("until"), DEFAULT_UNTIL));
				int			month	 = convertMonth (attributes.getValue ("month"));
				int			date	 = Integer.parseInt (attributes.getValue ("date"));
				DateRoll	dateRoll = DateRoll.forName (
											isNull (attributes.getValue ("roll"), DEFAULT_ROLL));
				 
				if ((calendar != null) && (month != 0) && (dateRoll != null))
					calendar.addRule (new CalendarRule.Fixed (name, from, until, month, date, dateRoll));
			}
			else if (localName.equals ("offset")) {
				String		name	= attributes.getValue("name");
				int			from	 = Integer.parseInt (
											isNull (attributes.getValue ("from"), DEFAULT_FROM));
				int			until	 = Integer.parseInt (
											isNull (attributes.getValue ("until"), DEFAULT_UNTIL));
				int			month	= convertMonth (attributes.getValue ("month"));
				int			when	= convertWhen (attributes.getValue ("when"));
				int 		weekday = convertWeekday (attributes.getValue("weekday"));
				
				if ((calendar != null) && (month != 0) && (when != 0) && (weekday != 0))
					calendar.addRule (new CalendarRule.Offset (name, from, until, when, weekday, month));
			}
			else if (localName.equals ("easter")) {
				String		name	 = attributes.getValue("name");
				int			from	 = Integer.parseInt (
											isNull (attributes.getValue ("from"), DEFAULT_FROM));
				int			until	 = Integer.parseInt (
											isNull (attributes.getValue ("until"), DEFAULT_UNTIL));
				int			offset	 = Integer.parseInt (attributes.getValue ("offset"));
				
				if (calendar != null)
					calendar.addRule (new CalendarRule.Easter (name, from, until, offset));
			}
		}
		
		/**
		 * The <CODE>Calendar</CODE> instance to add rules to.
		 * @since	TFP 1.0
		 */
		private RuleBasedCalendar	calendar = null;
		
		/**
		 * Converts a month name read from the XML file into a numeric code.
		 * 
		 * @param	name		The month name to convert.		
		 * @return	The numeric code for the month (1-12).
		 * @since	TFP 1.0
		 */
		private static int convertMonth (final String name)
		{
			if (name.equals ("JAN")) return (1);
			if (name.equals ("FEB")) return (2);
			if (name.equals ("MAR")) return (3);
			if (name.equals ("APR")) return (4);
			if (name.equals ("MAY")) return (5);
			if (name.equals ("JUN")) return (6);
			if (name.equals ("JUL")) return (7);
			if (name.equals ("AUG")) return (8);
			if (name.equals ("SEP")) return (9);
			if (name.equals ("OCT")) return (10);
			if (name.equals ("NOV")) return (11);
			if (name.equals ("DEC")) return (12);
			
			return (0);
		}
		
		/**
		 * Convert a string code used to indicate when a holiday occurs to
		 * a numeric value.
		 * 
		 * @param 	name		The code value to convert.
		 * @return	The equivalent internal numeric code.
		 * @since	TFP 1.0
		 */
		private static int convertWhen (final String name)
		{
			if (name.equals ("FIRST")) 	return (1);
			if (name.equals ("SECOND")) return (2);
			if (name.equals ("THIRD")) 	return (3);
			if (name.equals ("FOURTH")) return (4);
			if (name.equals ("LAST")) 	return (-1);
			
			return (0);
		}

		/**
		 * Converts a weekday name to the numeric code used internally.
		 * 
		 * @param 	name		The weekday name to convert.
		 * @return	The equivalent numeric code.
		 * @since	TFP 1.0
		 */
		private static int convertWeekday (final String name)
		{
			if (name.equals ("MON")) return (Date.MONDAY);
			if (name.equals ("TUE")) return (Date.TUESDAY);
			if (name.equals ("WED")) return (Date.WEDNESDAY);
			if (name.equals ("THU")) return (Date.THURSDAY);
			if (name.equals ("FRI")) return (Date.FRIDAY);
			if (name.equals ("SAT")) return (Date.SATURDAY);
			if (name.equals ("SUN")) return (Date.SUNDAY);
			
			return (0);
		}
		
		/**
		 * If the <CODE>value</CODE> is <CODE>null</CODE> then return the
		 * <CODE>nullValue</CODE> instead.
		 * 
		 * @param 	value		A possibly <CODE>null</CODE> value.
		 * @param 	nullValue	The default value if <CODE>null</CODE>.
		 * @return	A copy <CODE>value</CODE> if it is not <CODE>null</CODE>,
		 * 			otherwise the <CODE>nullValue</CODE>.
		 * @since	TFP 1.6
		 */
		private static String isNull (String value, String nullValue)
		{
			return ((value != null) ? value : nullValue);
		}
	}

	/**
	 * A <CODE>Logger</CODE> instance used to report run-time problems.
	 * @since	TFP 1.0
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.finance.Calendar");
	
	/**
	 * The set of all names <CODE>Calendar</CODE> instances.
	 * @since	TFP 1.0
	 */
	private static Hashtable<String, Calendar> extent
		= new Hashtable<String, Calendar> ();
	
	private static final String	DEFAULT_FROM	= "1901";
	
	private static final String DEFAULT_UNTIL	= "2099";
	
	private static final String DEFAULT_ROLL	= "NONE";
	
	/**
	 * The name of this <CODE>Calendar</CODE>, <CODE>null</CODE> if not
	 * named.
	 * @since	TFP 1.0
	 */
	private final String		name;
		
	/**
	 * Initialises the <CODE>Calendar</CODE> cache by parsing a standard set of
	 * calendar definitions held in an XML file distributed with the Java
	 * archive.
	 * @since	TFP 1.0
	 */
	static {
		logger.info ("Bootstrapping Calendars");
		
		try {
			SAXParser parser = new SAXParser (false, true, false, false, XmlUtility.getDefaultCatalog (), null);
			
			try {
				parser.parse (
						new InputSource (Application.openStream ("files-core/data/calendars.xml")),
						new BootStrap ());
			}
			catch (Exception error) {
				logger.log (Level.SEVERE, "Unable to load calendars definitions", error);	
			}
		}
		catch (Exception error) {
			logger.log (Level.SEVERE, "No suitable JAXP implementation installed", error);
		}
		
		logger.info ("Completed");
	}
}