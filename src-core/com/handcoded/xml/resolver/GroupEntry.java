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

package com.handcoded.xml.resolver;

import java.util.Stack;
import java.util.Vector;

import org.xml.sax.SAXException;

/**
 * The <CODE>GroupEntry</CODE> class provides a container for other
 * catalog components.
 * 
 * @author	BitWise
 * @version	$Id: GroupEntry.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.1
 */
class GroupEntry extends RelativeEntry  
{
	/**
	 * Constructs a <CODE>GroupEntry</CODE> given its containing entry
	 * and attribute values.
	 * 
	 * @param 	parent			The containing element.
	 * @param 	prefer			Optional <CODE>prefer</CODE> value.
	 * @param 	xmlbase			Optional <CODE>xml:base</CODE> value.
	 * @since	TFP 1.1
	 */
	public GroupEntry (final GroupEntry parent, final String prefer, final String xmlbase)
	{
		super (parent, xmlbase);
		
		this.prefer	= prefer;
	}
	
	/**
	 * Provides access to the value of the <CODE>prefer</CODE> attribute,
	 * possibly obtaining it from a containing element.
	 * 
	 * @return	The value of the <CODE>prefer</CODE> attribute.
	 * @since	TFP 1.1
	 */
	public final String getPrefer ()
	{
		if (prefer != null)
			return (prefer);
		else if (getParent () != null)
			return (getParent ().getPrefer());
		else
			return (null); 
	}
	
	/**
	 * Adds a rule to the catalog directing the given public identifier to
	 * the indicated URI.
	 *
	 * @param	publicId		The public identifier to be mapped.
	 * @param	uri				The corresponding URI.
	 * @param	xmlbase			Optional <CODE>xml:base</CODE> value.
	 * @return	The constructed <CODE>PublicEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final PublicEntry addPublic (final String publicId, final String uri, final String xmlbase)
	{
		PublicEntry		result = new PublicEntry (this, publicId, uri, xmlbase);
		
		rules.add (result);
		return (result);
	}
	
	/**
	 * Adds a rule to the catalog directing the given public identifier to
	 * the indicated URI.
	 *
	 * @param	publicId		The public identifier to be mapped.
	 * @param	uri				The corresponding URI.
	 * @return	The constructed <CODE>PublicEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final PublicEntry addPublic (final String publicId, final String uri)
	{
		return (addPublic (publicId, uri, null));
	}

	/**
	 * Adds a rule to the catalog directing the given system identifier to
	 * the indicated URI.
	 *
	 * @param	systemId		The system identifier to be mapped.
	 * @param	uri				The corresponding URI.
	 * @param	xmlbase			Optional xml:base value.
	 * @return	The constructed <CODE>SystemEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final SystemEntry addSystem (final String systemId, final String uri, final String xmlbase)
	{
		SystemEntry		result = new SystemEntry (this, systemId, uri, xmlbase);
		
		rules.add (result);
		return (result);
	}

	/**
	 * Adds a rule to the catalog directing the given system identifier to
	 * the indicated URI.
	 *
	 * @param	systemId		The system identifier to be mapped.
	 * @param	uri				The corresponding URI.
	 * @return	The constructed <CODE>SystemEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final SystemEntry addSystem (final String systemId, final String uri)
	{
		return (addSystem (systemId, uri, null));
	}

	/**
	 * Adds a rule to the catalog which will cause a matching system identifier
	 * to its starting prefix replaced.
	 *
	 * @param	startString		The system identifier prefix to match.
	 * @param	rewritePrefix	The new prefix to replace with.
	 * @return	The constructed <CODE>RewriteSystemEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final RewriteSystemEntry addRewriteSystem (final String startString, final String rewritePrefix)
	{
		RewriteSystemEntry	result = new RewriteSystemEntry (this, startString, rewritePrefix);
		
		rules.add (result);
		return (result);
	}

	/**
	 * Adds a rule to the catalog which will direct the resolution for any
	 * system identifier that starts with the given prefix to a sub-catalog
	 * file.
	 *
	 * @param	startString		The system identifier prefix to match
	 * @param	catalog			The URI of the sub-catalog.
	 * @param	xmlbase			Optional xml:base value.
	 * @return	The constructed <CODE>DelegateSystemEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final DelegateSystemEntry addDelegateSystem (final String startString, final String catalog,
			final String xmlbase)
	{
		DelegateSystemEntry	result  = new DelegateSystemEntry (this, startString, catalog, xmlbase);
		
		rules.add (result);
		return (result);
	}

	/**
	 * Adds a rule to the catalog which will direct the resolution for any
	 * system identifier that starts with the given prefix to a sub-catalog
	 * file.
	 *
	 * @param	startString		The system identifier prefix to match.
	 * @param	catalog			The URI of the sub-catalog.
	 * @return	The constructed <CODE>DelegateSystemEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final DelegateSystemEntry addDelegateSystem (final String startString, final String catalog)
	{
		return (addDelegateSystem (startString, catalog, null));
	}

	/**
	 * Adds a rule to the catalog which will direct the resolution for any
	 * public identifier that starts with the given prefix to a sub-catalog
	 * file.
	 *
	 * @param	startString		The public identifier prefix to match.
	 * @param	catalog			The URI of the sub-catalog.
	 * @param	xmlbase			The optional xml:base URI.
	 * @return	The constructed <CODE>DelegatePublicEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final DelegatePublicEntry addDelegatePublic (final String startString, final String catalog,
			final String xmlbase)
	{
		DelegatePublicEntry	result  = new DelegatePublicEntry (this, startString, catalog, xmlbase);
		
		rules.add (result);
		return (result);
	}

	/**
	 * Adds a rule to the catalog which will direct the resolution for any
	 * public identifier that starts with the given prefix to a sub-catalog
	 * file.
	 *
	 * @param	startString		The public identifier prefix to match.
	 * @param	catalog			The URI of the sub-catalog.
	 * @return	The constructed <CODE>DelegatePublicEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final DelegatePublicEntry addDelegatePublic (final String startString, final String catalog)
	{
		return (addDelegatePublic (startString, catalog, null));
	}
	
	/**
	 * Adds a rule to the catalog which will direct one URI access to a
	 * replacement URI.
	 * 
	 * @param 	name			The name to be replaced.
	 * @param 	uri				The replacement URI.
	 * @param 	xmlbase			The optional xml:base URI.
	 * @return	The constructed <CODE>UriEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final UriEntry addUri (final String name, final String uri, final String xmlbase)
	{
		UriEntry	result  = new UriEntry (this, name, uri, xmlbase);
		
		rules.add (result);
		return (result);
	}
	
	/**
	 * Adds a rule to the catalog which will direct one URI access to a
	 * replacement URI.
	 * 
	 * @param 	name			The name to be replaced.
	 * @param 	uri				The replacement URI.
	 * @return	The constructed <CODE>UriEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final UriEntry addUri (final String name, final String uri)
	{
		return (addUri (name, uri, null));
	}
	
	/**
	 * Adds a rule to the catalog that rewrites the prefix of URIs.
	 * 
	 * @param 	startString		The URI prefix to match.
	 * @param 	rewritePrefix	The text to replace with.
	 * @return	The constructed <CODE>RewriteUriEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final RewriteUriEntry addRewriteUri (final String startString, final String rewritePrefix)
	{
		RewriteUriEntry		result = new RewriteUriEntry (this, startString, rewritePrefix);
		
		rules.add (result);
		return (result);
	}
	
	/**
	 * Adds a rule that delegates processinf of matched URIs to another
	 * catalog.
	 * 
	 * @param 	startString		The URI prefix to match.
	 * @param 	catalog			The name of catalog to delegate to.
	 * @param 	xmlbase			The optional xml:base URI.
	 * @return	The constructed <CODE>DelegateUriEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final DelegateUriEntry addDelegateUri (final String startString, final String catalog, final String xmlbase)
	{
		DelegateUriEntry	result = new DelegateUriEntry (this, startString, catalog, xmlbase);
		
		rules.add (result);
		return (result);
	}
	
	/**
	 * Adds a rule that delegates processinf of matched URIs to another
	 * catalog.
	 * 
	 * @param 	startString		The URI prefix to match.
	 * @param 	catalog			The name of catalog to delegate to.
	 * @return	The constructed <CODE>DelegateUriEntry</CODE> instance.
	 * @since	TFP 1.0
	 */
	public final DelegateUriEntry addDelegateUri (final String startString, final String catalog)
	{
		return (addDelegateUri (startString, catalog, null));
	}
	
	/**
	 * Adds a rule which will cause resolution to chain to another catalog
	 * if no match can be found in this one.
	 * <P>
	 * This method is only available to the <CODE>XmlCatalogManager</CODE>
	 * class.
	 *
	 * @param	catalog			The URI of the catalog to chain to.
	 * @param	xmlbase			The optional xml:base URI
	 * @return	The new <CODE>NextCatalogEntry</CODE>.
	 * @since	TFP 1.0
	 */
	public final NextCatalogEntry addNextCatalog (final String catalog, final String xmlbase)
	{
		NextCatalogEntry	result = new NextCatalogEntry (this, catalog, xmlbase);
		
		rules.add (result);
		return (result);
	}

	/**
	 * Adds a rule which will cause resolution to chain to another catalog
	 * if no match can be found in this one.
	 * <P>
	 * This method is only available to the <CODE>CatalogManager</CODE>
	 * class.
	 *
	 * @param	catalog			The URI of the catalog to chain to.
	 * @return	The new <CODE>NextCatalogEntry</CODE>.
	 * @since	TFP 1.0
	 */
	public final NextCatalogEntry addNextCatalog (final String catalog)
	{
		return (addNextCatalog (catalog, null));
	}

	/**
	 * The set of all rules in order of definition.
	 * @since	TFP 1.0
	 */
	protected Vector<CatalogComponent> rules
		= new Vector<CatalogComponent> ();

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	protected String toDebug ()
	{
		StringBuffer		buffer 	= new StringBuffer ();
		
		buffer.append ("prefer=" + ((prefer == null) ? "null" : ("\"" + prefer + "\"")) + ",");
		buffer.append ("rules={");
		for (int index = 0; index < rules.size (); ++index) {
			if (index != 0) buffer.append (',');
			
			buffer.append (rules.elementAt (index).toString ());
		}
		buffer.append ('}');
		buffer.append (super.toDebug());
		
		return (buffer.toString ());
	}
	
	/**
	 * Implements the OASIS search rules for entity resources.
	 *
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws	SAXException If an error occurs during processing.
	 * @since	TFP 1.0
	 */
	protected String applyRules (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String				result = null;

		if (catalogs.contains (this))
			throw new SAXException ("Circular dependency in the XML Catalogs");

		catalogs.push (this);

		// If a system identifier is provided then try to match it explicitly
		// or through a rewriting rule or delegation.
		if ((systemId != null) && (systemId.length () > 0)) {
			if ((result = applySystemEntries (publicId, systemId, catalogs)) != null) {
				catalogs.pop ();
				return (result);
			}
			
			if ((result = applyRewriteSystemEntries (publicId, systemId, catalogs)) != null) {
				catalogs.pop ();
				return (result);
			}
			
			if ((result = applyDelegateSystemEntries (publicId, systemId, catalogs)) != null) {
				catalogs.pop ();
				return (result);
			}
		}

		// If a public identifier is provided then try to match it explicitly
		// or through delegation.
		if ((publicId != null) && (publicId.length () > 0)) {
			if ((result = applyPublicEntries (publicId, systemId, catalogs)) != null) {
				catalogs.pop ();
				return (result);
			}
			
			if ((result = applyDelegatePublicEntries (publicId, systemId, catalogs)) != null) {
				catalogs.pop ();
				return (result);
			}
		}

		// Finally try any other chained catalogs
		if ((result = applyNextCatalogEntries (publicId, systemId, catalogs)) != null) {
			catalogs.pop ();
			return (result);
		}

		catalogs.pop ();
		return (null);
	}

	/**
	 * Implements OASIS search rules for URI based resources.
	 * 
	 * @param 	uri				The URI of the required resource.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws	SAXException If an error occurs during processing.
	 */
	protected String applyRules (final String uri, Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String				result = null;

		if (catalogs.contains (this))
			throw new SAXException ("Circular dependency in the XML Catalogs");

		catalogs.push (this);

		if ((uri != null) && (uri.length () > 0)) {
			if ((result = applyUriEntries (uri, catalogs)) != null) {
				catalogs.pop ();
				return (result);
			}
			
			if ((result = applyRewriteUriEntries (uri, catalogs)) != null) {
				catalogs.pop ();
				return (result);
			}
			
			if ((result = applyDelegateUriEntries (uri, catalogs)) != null) {
				catalogs.pop ();
				return (result);
			}
		}

		// Finally try any other chained catalogs
		if ((result = applyNextCatalogEntries (uri, catalogs)) != null) {
			catalogs.pop ();
			return (result);
		}

		catalogs.pop ();
		return (null);
	}

	/**
	 * The value of the prefer attribute.
	 * @since	TFP 1.1
	 */
	private final String		prefer;
	
	/**
	 * Applies all the <CODE>SystemEntry</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws 	SAXException	If an occur was detected during processing.
	 * @since	TFP 1.0 
	 */
	private String applySystemEntries (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof SystemEntry) {
				if ((result = ((EntityRule) rule)
						.applyTo (publicId, systemId, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applySystemEntries (publicId, systemId, catalogs)) != null)
					break;
			}
		}
		return (result);
	}

	/**
	 * Applies all the <CODE>RewriteSystemEntry</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws 	SAXException	If an occur was detected during processing.
	 * @since	TFP 1.0 
	 */
	private String applyRewriteSystemEntries (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof RewriteSystemEntry) {
				if ((result = ((EntityRule) rule)
						.applyTo (publicId, systemId, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyRewriteSystemEntries (publicId, systemId, catalogs)) != null)
					break;
			}
		}
		return (result);
	}

	/**
	 * Applies all the <CODE>DelegateSystemEntry</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws 	SAXException	If an occur was detected during processing.
	 * @since	TFP 1.0 
	 */
	private String applyDelegateSystemEntries (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof DelegateSystemEntry) {
				if ((result = ((EntityRule) rule)
						.applyTo (publicId, systemId, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyDelegateSystemEntries (publicId, systemId, catalogs)) != null)
					break;
			}
		}
		return (result);
	}

	/**
	 * Applies all the <CODE>PublicEntry</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws 	SAXException	If an occur was detected during processing.
	 * @since	TFP 1.0 
	 */
	private String applyPublicEntries (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof PublicEntry) {
				if ((result = ((EntityRule) rule)
						.applyTo (publicId, systemId, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyPublicEntries (publicId, systemId, catalogs)) != null)
					break;
			}
		}
		return (result);
	}

	/**
	 * Applies all the <CODE>DelegatePublicEntry</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws 	SAXException	If an occur was detected during processing.
	 * @since	TFP 1.0 
	 */
	private String applyDelegatePublicEntries (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof DelegatePublicEntry) {
				if ((result = ((EntityRule) rule)
						.applyTo (publicId, systemId, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyDelegatePublicEntries (publicId, systemId, catalogs)) != null)
					break;
			}
		}
		return (result);
	}

	/**
	 * Applies all the <CODE>NextCatalog</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws 	SAXException	If an occur was detected during processing.
	 * @since	TFP 1.0 
	 */
	private String applyNextCatalogEntries (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof NextCatalogEntry) {
				if ((result = ((EntityRule) rule)
						.applyTo (publicId, systemId, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyNextCatalogEntries (publicId, systemId, catalogs)) != null)
					break;
			}
		}
		return (result);
	}

	/**
	 * Applies all the <CODE>UriEntry</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param 	uri				The URI of the required resource.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws	SAXException If an error occurs during processing.
	 * @since	TFP 1.0 
	 */
	private String applyUriEntries (final String uri, Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof UriEntry) {
				if ((result = ((UriRule) rule)
						.applyTo (uri, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyUriEntries (uri, catalogs)) != null)
					break;
			}
		}
		return (result);
	}
	
	/**
	 * Applies all the <CODE>RewriteUriEntry</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param 	uri				The URI of the required resource.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws	SAXException If an error occurs during processing.
	 * @since	TFP 1.0 
	 */
	private String applyRewriteUriEntries (final String uri, Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof RewriteUriEntry) {
				if ((result = ((UriRule) rule)
						.applyTo (uri, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyRewriteUriEntries (uri, catalogs)) != null)
					break;
			}
		}
		return (result);
	}
	
	/**
	 * Applies all the <CODE>DelegateUriEntry</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param 	uri				The URI of the required resource.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws	SAXException If an error occurs during processing.
	 * @since	TFP 1.0 
	 */
	private String applyDelegateUriEntries (final String uri, Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof DelegateUriEntry) {
				if ((result = ((UriRule) rule)
						.applyTo (uri, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyDelegateUriEntries (uri, catalogs)) != null)
					break;
			}
		}
		return (result);
	}

	/**
	 * Applies all the <CODE>NextCatalog</CODE> rules in the current
	 * catalog recursing into <CODE>Group</CODE> definitions.
	 * 
	 * @param 	uri				The URI of the required resource.
	 * @param	catalogs		A stack of catalogs being processed used to
	 *							detect circular dependency.
	 * @return  The URI of the resolved entity or <CODE>null</CODE>.
	 * @throws	SAXException If an error occurs during processing.
	 * @since	TFP 1.0 
	 */
	private String applyNextCatalogEntries (final String uri, Stack<GroupEntry> catalogs)
		throws SAXException
	{
		String		result = null;
		
		for (int index = 0; index < rules.size (); ++index) {
			CatalogComponent	rule = (CatalogComponent) rules.elementAt (index);
			
			if (rule instanceof NextCatalogEntry) {
				if ((result = ((UriRule) rule)
						.applyTo (uri, catalogs)) != null)
					break;
			}
			
			if (rule instanceof GroupEntry) {
				if ((result = ((GroupEntry) rule)
						.applyNextCatalogEntries (uri, catalogs)) != null)
					break;
			}
		}
		return (result);
	}
}