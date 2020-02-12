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

package com.handcoded.fpml.schemes;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.handcoded.framework.Application;
import com.handcoded.xml.parser.SAXParser;

/**
 * The <CODE>SchemeCollection</CODE> class maintains a set of <CODE>Scheme</CODE>
 * instances that are used to validate the data within an FpML document.
 * <P>
 * The <CODE>SchemeCollection</CODE> class automatically initialises itself with a
 * default set of scheme definitions at startup. Two sets of defaults are
 * provided, namely 'data/schemeDefinitions.xml' containing all the values
 * listed in the FpML standard, and 'data/additionalDefinitions.xml' which
 * contains non-normative definitions. Both files are located via the
 * system <CODE>ClassLoader</CODE> so that they can be packaged within a Java
 * archive.
 *
 * @author	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class SchemeCollection
{
	/**
	 * Constructs an empty <B>SchemeCollection</B>.
	 * @since 	TFP 1.0
	 */
	public SchemeCollection ()
	{ }
	
	/**
	 * Parses the scheme definition in the indicated file and adds them to
	 * this collection.
	 * 
	 * @param 	uri			The URI of the source XML document.
	 * @since	TFP 1.0
	 */
	public void parse (String uri)
	{
		try {
			SAXParser parser = new SAXParser (false, true, false, false, null, null);
			
			try {
				parser.parse (new InputSource (Application.openStream (uri)), new BootStrap ());
			}
			catch (Exception error) {
				logger.log (Level.SEVERE, "Unable to load standard FpML schemes", error);	
			}
		}
		catch (Exception error) {
			logger.log (Level.SEVERE, "No suitable JAXP implementation installed", error);
		}
	}
	
	/**
	 * Adds the given <CODE>Scheme</CODE> instance to the extent set. If the
	 * <CODE>Scheme</CODE> has the same URI as an existing entry then it will
	 * replace the old definition.
	 *
	 * @param	scheme		The <CODE>Scheme</CODE> instance to be added.
	 * @return	A reference to the old <CODE>Scheme</CODE> object replaced by
	 *			this action, <CODE>null</CODE> otherwise.
	 * @since	TFP 1.0
	 */
	public Scheme add (final Scheme scheme)
	{
		Scheme		resultA = schemes.put (scheme.getUri (), scheme);
		Scheme		resultB = null;
		
		if (scheme.getCanonicalUri () != null)
			resultB = schemes.put (scheme.getCanonicalUri (), scheme);
		
		return ((resultA != null) ? resultA : resultB);
	}
	
	/**
	 * Removes the given <CODE>Scheme</CODE> instance from the extent set.
	 *
	 * @param	scheme		The <CODE>Scheme</CODE> instance to be removed.
	 * @return	A reference to the <CODE>Scheme</CODE> if it was in the extent
	 *			set, <CODE>null</CODE> if it was not.
	 * @since	TFP 1.0
	 */
	public Scheme remove (final Scheme scheme)
	{
		Scheme		resultA = schemes.remove (scheme.getUri());
		Scheme		resultB = schemes.remove (scheme.getCanonicalUri());
		
		return ((resultA != null) ? resultA : resultB);
	}
	
	/**
	 * Removes the <CODE>Scheme</CODE> identifed by the given URI if it is
	 * contained within the extent set.
	 *
	 * @param	uri			The URI of the target <CODE>Scheme</CODE>.
	 * @return	A reference to the <CODE>Scheme</CODE> if it was in the extent
	 *			set, <CODE>null</CODE> if it was not.
	 * @since	TFP 1.0
	 */
	public Scheme remove (final String uri)
	{
		Scheme		scheme = findSchemeForUri (uri);
		
		if (scheme != null)
			return (remove (scheme));
		
		return (null);
	}
	
	/**
	 * Examines the <CODE>Scheme</CODE> cache to see if an instance has been
	 * provided for the given URI.
	 *
	 * @param	uri			The URI of the required <CODE>Scheme</CODE>.
	 * @return	The corresponding <CODE>Scheme</CODE> instance or <CODE>null
	 *			</CODE> if not defined.
	 * @since	TFP 1.0
	 */
	public Scheme findSchemeForUri (final String uri)
	{
		return (schemes.get (uri));
	}

	/**
	 * The <CODE>BootStrap</CODE> class provides a simple extension of the
	 * SAX <CODE>DefaultHandler</CODE> that captures the key values that
	 * define an FpML scheme from an XML initialisation file.
	 * @since	TFP 1.0
	 */
	private final class BootStrap extends DefaultHandler
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
			int			index;

			if (localName.equals ("scheme")) {
				if ((index = attributes.getIndex ("uri")) >= 0) {
					int		index2 = attributes.getIndex ("canonicalUri");
					
					if (index2 >= 0)
						add (scheme = new ClosedScheme (attributes.getValue (index),
														attributes.getValue (index2)));
					else
						add (scheme = new ClosedScheme (attributes.getValue (index)));
				}
				else {
					logger.warning ("uri attribute missing from scheme in bootstrap definitions");
					scheme = null;
				}
			}
			else if (localName.equals ("schemeValue")) {
				if ((index = attributes.getIndex ("name")) >= 0)
					code = attributes.getValue (index);
				else {
					logger.warning ("name attribute missing from schemeValue in bootstrap definitions");
					code = null;
				}
					
				if ((index = attributes.getIndex ("schemeValueSource")) >= 0)
					source = attributes.getValue (index);
				else
					source = null;
					
				description = null;
			}
			else if (localName.equals ("paragraph"))
				text = null;
		}
		
		/**
		 * This method is called when the XML parser detects that end of an
		 * element definition. It is used to trigger the processing of data
		 * cached during the elements processing.
		 * 
		 * @since	TFP 1.0
		 */
		public void endElement (String ns, String localName, String qName)
		{
			if (localName.equals ("schemeValue")) {
				if ((scheme != null) && (code != null))
					scheme.add (new Value (code, source, description));
			}
			else if (localName.equals ("paragraph")) {
				if (description == null)
					description = text.trim ();
			}
		}
		
		/**
		 * Captures the characters that comprise the body of the current
		 * element.
		 * 
		 * @since	TFP 1.0
		 */
		public void characters (char [] ch, int start, int length)
		{
			text = new String (ch, start, length);	
		}
		
		/**
		 * Reference to the <CODE>Scheme</CODE> instance under construction.
		 * @since	TFP 1.0
		 */
		private CachedScheme	scheme	= null;
		
		/**
		 * A temporary copy of the current code being parsed.
		 * @since 	TFP 1.0
		 */
		private String			code;
		
		/**
		 * A temporary copy of the source for the code being parsed.
		 * @since	TFP 1.0
		 */
		private String			source;
		
		/**
		 * A temporary copy of the description for the code being parsed.
		 * @since	TFP 1.0
		 */
		private String			description;
		
		/**
		 * Temporary string used to store text values.
		 * @since	TFP 1.0
		 */
		private String			text;
	}

	/**
	 * <CODE>Logger</CODE> instance used to record problems.
	 * @since	TFP 1.0
	 */
	private static Logger		logger
		= Logger.getLogger ("com.handcoded.fpml.schemes.SchemeCollection");
		
	/**
	 * The extent set of all currently defined <CODE>Scheme</CODE> instances.
	 * @since	TFP 1.0
	 */	
	private Hashtable<String, Scheme> schemes
		= new Hashtable<String, Scheme> ();
}