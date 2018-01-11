// Copyright (C),2005-2017 HandCoded Software Ltd.
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

package com.handcoded.xml.writer;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The <CODE>XmlWriter</CODE> class creates and manages the <CODE>PrintWriter
 * </CODE> used to output an XML document. The derived classes determine how
 * the actual content is formatted on the stream.
 * 
 * @author	BitWise
 * @version	$Id: XmlWriter.java 115 2007-03-12 15:12:41Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class XmlWriter
{
	/**
	 * Returns the name of the character encoding used to produce the output
	 * characters suitable for used in the 'encoding' attribute of an XML
	 * document preamble.
	 * <P>
	 * The XML and Java standards differ slightly in the naming of some of
	 * encodings. This function will aways return the name required by XML,
	 * translating if necessary.
	 * 
	 * @return	The name of the character encoding applied to the stream.
	 * @since	TFP 1.0
	 */
	public final String getEncoding ()
	{
		if (encoding.equals ("UTF8")) return ("UTF-8");
		
		return (encoding);
	}
	
	/**
	 * Formats and writes the indicated <CODE>Document</CODE> to the output
	 * stream using the style implemented by the class instance.
	 * 
	 * @param 	document		The <CODE>Document</CODE> to be formatted.
	 * @since	TFP 1.0
	 */
	public abstract void write (Document document);
	
	/**
	 * Formats and writes the indicated <CODE>Element</CODE> to the output
	 * stream using the style implemented by the class instance.
	 * 
	 * @param 	element		The <CODE>Element</CODE> to be formatted.
	 * @since	TFP 1.9
	 */
	public abstract void write (Element element);
	
	
	/**
	 * The <CODE>PrintWriter</CODE> to use to display formatted text.
	 * @since	TFP 1.0
	 */
	protected PrintWriter	output;
	
	/**
	 * Constructs a <CODE>XmlWriter</CODE> that will output XML to the given
	 * <CODE>Writer</CODE> using UTF-8 character encoding.
	 * 
	 * @param 	writer			The <CODE>Writer</CODE> to write to. 
	 * @since	TFP 1.0
	 */
	protected XmlWriter (Writer writer)
	{
		this (writer, "UTF8");
	}
	
	/**
	 * Constructs a <CODE>XmlWriter</CODE> that will output XML to the given
	 * <CODE>OutputStream</CODE> using UTF-8 character encoding.
	 * 
	 * @param 	stream			The <CODE>OutputStream</CODE> to write to. 
	 * @throws 	UnsupportedEncodingException If the requested character encoding
	 * 			is not supported by the Java run-time.
	 * @since	TFP 1.0
	 */
	protected XmlWriter (OutputStream stream)
		throws UnsupportedEncodingException
	{
		this (stream, "UTF8");
	}
	
	/**
	 * Constructs a <CODE>XmlWriter</CODE> that will output XML to the given
	 * <CODE>OutputStream</CODE> using the specified character encoding.
	 * 
	 * @param 	writer			The <CODE>Writer</CODE> to write to. 
	 * @param 	encoding		The character encoding to use.
	 * @since	TFP 1.0
	 */
	protected XmlWriter (Writer writer, final String encoding)
	{
		this.encoding = encoding;
		output = new PrintWriter (writer);
	}
	
	/**
	 * Constructs a <CODE>XmlWriter</CODE> that will output XML to the given
	 * <CODE>OutputStream</CODE> using the specified character encoding.
	 * 
	 * @param 	stream			The <CODE>OutputStream</CODE> to write to. 
	 * @param 	encoding		The character encoding to use.
	 * @throws 	UnsupportedEncodingException If the requested character encoding
	 * 			is not supported by the Java run-time.
	 * @since	TFP 1.0
	 */
	protected XmlWriter (OutputStream stream, final String encoding)
		throws UnsupportedEncodingException
	{
		output = new PrintWriter (new OutputStreamWriter (stream, this.encoding = encoding));
	}
	
	/**
	 * Outputs a character string converting any characters used by XML for
	 * control purposes to their escaped format.
	 * 
	 * @param 	text			The text to be output.
	 * @param 	isAttribute		<CODE>true</CODE> if the text is an attribute value.
	 * @since	TFP 1.0
	 */
	protected final void escape (final String text, boolean isAttribute)
	{
		for (int index = 0; index < text.length (); ++index) {
			char			ch;
			
			switch (ch = text.charAt (index)) {
			case '&':	output.print ("&amp;");		break;
			case '<':	output.print ("&lt;");		break;
			case '>':	output.print ("&gt;");		break;
			
			// Quotes must be escaped in attribute values
			case '"':
				{
					if (isAttribute)
						output.print ("&quot;");
					else
						output.print (ch);
					break;
				}
				
			default:	output.print (ch);			break;
			}
		}
	}
	
	/**
	 * The output character encoding.
	 */
	private final String	encoding;
}