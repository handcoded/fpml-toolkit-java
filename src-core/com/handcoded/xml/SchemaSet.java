// Copyright (C),2007-2020 HandCoded Software Ltd.
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

package com.handcoded.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.handcoded.framework.Application;
import com.handcoded.meta.SchemaRelease;
import com.handcoded.xml.resolver.Catalog;

/**
 * The <CODE>SchemaSet</CODE> class hold a collection of resolved file paths
 * that reference schemas known to the application. The collection can be
 * compiled into a <CODE>Schema</CODE> instance for use in the JAXP validation
 * framework.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
public final class SchemaSet
{
	/**
	 * Constructs a <CODE>SchemaSet</CODE>.
	 * @since	TFP 1.0
	 */
	public SchemaSet ()
	{ }

	/**
	 * Resolve the location of the indicated <CODE>SchemaRelease</CODE> (and
	 * any that it imports) to the schema set using default XML catalog to
	 * resolve the schema location.
	 * 
	 * @param 	release			The <CODE>SchemaRelease</CODE> to be added.
	 * @since	TFP 1.1
	 */
	public void add (SchemaRelease release)
	{
		schemas.add (release);
		schema = null;
	}
		
	/**
	 * Returns the compiled representation of the schema(s), if necessary compiling
	 * them from their source streams.
	 *
	 * @return	The compiled schema representation for the set.
	 * @since	TFP 1.0
	 */
	public Schema getSchema ()
	{
		return (getSchema (XmlUtility.getDefaultCatalog ()));
	}
	
	/**
	 * Returns the compiled representation of the schema(s), if necessary compiling
	 * them from their source streams.
	 *
	 * @param	catalog			The <CODE>Catalog</CODE> used to locate schemas.
	 * @return	The compiled schema representation for the set.
	 * @since	TFP 1.0
	 */
	public Schema getSchema (Catalog catalog)
	{
		if (schema == null) {
			sources.clear ();		
			
			// Find all the schemas we need
			for (SchemaRelease release : schemas) {
				for (SchemaRelease imports : release.getImportSet ()) {
					try {
						String source = catalog.resolve (imports.getNamespaceUri());
						
						if (source == null)
							logger.log (Level.SEVERE, "Failed to resolve namespace URI '" + imports.getNamespaceUri () +"'");
						else  {
							if (!sources.contains (source))
								sources.add (source);
						}
					}
					catch (SAXException error) {
						logger.log (Level.SEVERE, "Unexpected SAX exception creating schema source", error);
						System.exit (2);
					}
				}
			}
			
			// The create an source array to fetch them
			Source [] sourceArray = new StreamSource [sources.size()];
			
			for (int index = 0; index < sources.size (); ++index) {
				String systemId = sources.get (index);

				sourceArray [index] =  new StreamSource (Application.openStream (systemId), systemId);
			}

			// And compile them into a compiled collection
			try {
				schema = SchemaFactory.newInstance (XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema (sourceArray);
			}
			catch (SAXException error) {
				logger.log (Level.SEVERE, "Unexpected SAX Exception", error);
			}
		}
		return (schema);
	}

	/**
	 * A <CODE>Logger</CODE> instance used to report serious errors.
	 * @since	TFP 1.0
	 */
	private static Logger	logger
		= Logger.getLogger ("com.handcoded.xml.SchemaSet");

	/**
	 * The set of <CODE>SchemaReleases</CODE> added to the set.
	 * @since	TFP 1.1
	 */
	private HashSet<SchemaRelease> schemas = new HashSet<> ();
	
	/**
	 * The set of <CODE>String</CODE> instances for the schemas paths.
	 * @since	TFP 1.0
	 */
	private ArrayList<String>	sources	= new ArrayList<> ();
	
	/**
	 * The compiled schema representation of the schemas.
	 * @since	TFP 1.0
	 */
	private Schema			schema		= null;	
}