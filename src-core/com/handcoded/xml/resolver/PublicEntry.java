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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

import org.xml.sax.SAXException;

/**
 * The <CODE>PublicEntry</CODE> class implements simple public identifier
 * matching.
 * 
 * @author	BitWise
 * @version	$Id: PublicEntry.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.1
 */
final class PublicEntry extends RelativeEntry implements EntityRule
{
	/**
	 * Constructs a <CODE>PublicEntry</CODE> instance that will replace
	 * a public identifier with a URI.
	 *
	 * @param	parent			The containing element.
	 * @param	publicId		The public identifier to be matched
	 * @param	uri				The replacement URI.
	 * @param	xmlbase			The optional xml:base URI
	 * @since	TFP 1.0
	 */
	public PublicEntry (final GroupEntry parent, final String publicId,
			final String uri, final String xmlbase)
	{
		super (parent, xmlbase);
		
		this.publicId = publicId;
		this.uri      = uri;
	}

	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public String applyTo (final String publicId, final String systemId,
			Stack<GroupEntry> catalogs)
		throws SAXException
	{
		URI					targetUri;
		URI					publicUri;
		
		// Convert the target PublicId value to a URI
		try {
			if (publicId.startsWith ("file:"))
				targetUri = new File (publicId.substring (5)).toURI ();
			else
				targetUri = new URI (unwrap (publicId));
		}
		catch (URISyntaxException error) {
			throw new SAXException ("Failed to normalise targetId", error);
		}
	
		// Convert the catalog PublicId value to a URI
		try {
			publicUri = baseAsUri ().resolve (new URI (unwrap (this.publicId)));
		}
		catch (URISyntaxException error) {
			throw new SAXException ("Failed to normalise publicId", error);
		}
		
		// If they match then replace with the catalog URI
		if (publicUri.equals (targetUri)) {
			try {
				return (baseAsUri ().resolve (new URI (uri)).toString ());
			}
			catch (URISyntaxException error) {
				throw new SAXException ("Failed to resolve target URI", error);
			}
		}

		return (null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	@Override
	protected String toDebug ()
	{
		return ("publicId=\"" + publicId + "\",uri=\"" + uri + "\"," + super.toDebug ());
	}

	/**
	 * The publicId to match against.
	 * @since	TFP 1.0
	 */
	private final String	publicId;

	/**
	 * The URI to replace with.
	 * @since	TFP 1.0
	 */
	private final String	uri;
	
	/**
	 * If the supplied name is a public name then it is 'unwrapped' according
	 * to the rules defined in RFC 3151.
	 * <P>
	 * Public names for DTDs (such as those used by DocBook and FpML 1-0 thru
	 * 3-0) are not valid URIs and must undergo a number of character
	 * replacements. This rountine detects a public name by looking for '//'
	 * anywhere in the string.
	 * 
	 * @param 	name			The public name to be wrapped.
	 * @return	A valid URI string, either the original input unmodified or
	 * 			a new URI constructed by the unwrapping process.
	 * @since	TFP 1.0
	 */
	private String unwrap (String name)
	{
		if (name.indexOf ("//") != -1) {
			StringBuffer buffer = new StringBuffer ();
			
			int		length = name.length ();
			char	ch;
			
			buffer.append ("urn:publicid:");
			for (int index = 0; index < length; ++index) {
				switch (ch = name.charAt (index)) {
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					{
						int			buflen = buffer.length ();
						
						if ((buflen == 0) || (buffer.charAt (buflen - 1) != '+'))
							buffer.append ('+');
						break;
					}
					
				case '/':
					{
						if (((index + 1) < length) && (name.charAt (index + 1) == '/')) {
							buffer.append (':');
							++index;
						}
						else
							buffer.append ("%2F");
						break;
					}
				
				case ':':
				{
					if (((index + 1) < length) && (name.charAt (index + 1) == ':')) {
						buffer.append (';');
						++index;
					}
					else
						buffer.append ("%3A");
					break;
				}
				
				case '+':
					buffer.append ("%2B");
					break;
					
				case ';':
					buffer.append ("%3B");
					break;
					
				case '\'':
					buffer.append ("%27");
					break;
				
				case '?':
					buffer.append ("%3F");
					break;
					
				case '#':
					buffer.append ("%23");
					break;
					
				case '%':
					buffer.append ("%25");
					break;
					
				default:
					buffer.append (ch);
				}
			}
			
			return (buffer.toString ());
		}
			
		return (name);
	}
}