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
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * The <CODE>CompactWriter</CODE> produces an XML document in which as little
 * whitespace as possible is used. No attempt is made to format the output, it
 * is structured as one long continuous line of text. For example:
 * <PRE>
 * &lt;?XML version="1.0" encoding="UTF-8"?&gt;&lt;root&gt;&lt;element attr="value&gt;&lt;leaf&gt;text&lt;/leaf&gt;&lt;leaf/&gt;&lt;/element&gt;&lt;/root&gt;
 * </PRE> 
 * 
 * @author	BitWise
 * @version	$Id: CompactWriter.java 115 2007-03-12 15:12:41Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class CompactWriter extends XmlWriter
{
	/**
	 * Constructs a <CODE>CompactWriter</CODE> that will use UTF-8 encoding for
	 * output.
	 * 
	 * @param 	writer			The <CODE>Writer</CODE> to write to.
	 * @since	TFP 1.0
	 */
	public CompactWriter (Writer writer)
	{
		super (writer);
	}

	/**
	 * Constructs a <CODE>CompactWriter</CODE> that will use UTF-8 encoding for
	 * output.
	 * 
	 * @param 	stream			The <CODE>OutputStream</CODE> to write to.
	 * @throws 	UnsupportedEncodingException If the requested character encoding
	 * 			is not supported by the Java run-time.
	 * @since	TFP 1.0
	 */
	public CompactWriter (OutputStream stream)
		throws UnsupportedEncodingException
	{
		super (stream);
	}

	/**
	 * Constructs a <CODE>CompactWriter</CODE> that will output XML to the given
	 * <CODE>Writer</CODE> using the specified character encoding.
	 * 
	 * @param 	writer			The <CODE>Writer</CODE> to write to. 
	 * @param 	encoding		The character encoding to use.
	 * @since	TFP 1.0
	 */
	public CompactWriter (Writer writer, final String encoding)
	{
		super (writer, encoding);
	}
	
	/**
	 * Constructs a <CODE>CompactWriter</CODE> that will output XML to the given
	 * <CODE>OutputStream</CODE> using the specified character encoding.
	 * 
	 * @param 	stream			The <CODE>OutputStream</CODE> to write to. 
	 * @param 	encoding		The character encoding to use.
	 * @throws 	UnsupportedEncodingException If the requested character encoding
	 * 			is not supported by the Java run-time.
	 * @since	TFP 1.0
	 */
	public CompactWriter (OutputStream stream, final String encoding)
		throws UnsupportedEncodingException
	{
		super (stream, encoding);
	}
	
	/**
	 * Converts an entire <CODE>Document</CODE> into a string using a
	 * <CODE>CompactWriter</CODE> instance.
	 * 
	 * @param 	document		The <CODE>Document</CODE> to convert.
	 * @return	The <CODE>Document</CODE> as a formatted XML string.
	 * @since	TFP 1.0
	 */
	public static String toString (final Document document)
	{
		StringWriter	writer	= new StringWriter ();
		new CompactWriter (writer).write (document);
		return (writer.toString ());
	}
	
	/**
	 * Converts an <CODE>Element</CODE> and its child content into a string
	 * using a <CODE>CompactWriter</CODE> instance.
	 * 
	 * @param 	element		The <CODE>Element</CODE> to convert.
	 * @return	The <CODE>Element</CODE> as a formatted XML string.
	 * @since	TFP 1.9
	 */
	public static String toString (final Element element)
	{
		StringWriter	writer	= new StringWriter ();
		new CompactWriter (writer).write (element);
		return (writer.toString ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
	public void write (final Document document)
	{
		DocumentType	doctype = document.getDoctype ();
		
		output.print ("<?xml version=\"1.0\" encoding=\"" + getEncoding () + "\"?>");
		
		if (doctype != null) {
			output.print ("<!DOCTYPE " + doctype.getName ());

			if (doctype.getPublicId () != null) {
				output.print (" PUBLIC \"" + doctype.getPublicId () + "\"");
				output.print (" \"" + doctype.getSystemId () +"\"");
			}
			else if (doctype.getSystemId () != null)
				output.print (" SYSTEM \"" + doctype.getSystemId () + "\"");

			if (doctype.getInternalSubset () != null) {
				output.print (" [");
				output.print (doctype.getInternalSubset ());
			}
			output.print (">");
		}
		write (document.getDocumentElement ());
	}
	
	/**
	 * {@inheritDoc}
	 * @since	TFP 1.9
	 */
	public void write (final Element element)
	{
		write ((Node) element);
		output.flush ();
	}
	
	/**
	 * Recursively traverses the DOM tree starting at the given <CODE>Node</CODE>
	 * to produce formatted XML output.
	 * 
	 * @param 	node			The current <CODE>Node</CODE> under consideration.
	 * @since	TFP 1.0
	 */
	private void write (Node node)
	{
		switch (node.getNodeType ()) {
		case Node.ELEMENT_NODE:
			{
				Element			element = (Element) node;
				NamedNodeMap	attributes = element.getAttributes ();
				
				output.print ('<');
				output.print (element.getNodeName ());
				
				for (int index = 0; index < attributes.getLength (); ++index) {
					Attr			attr = (Attr) attributes.item (index);
										
					output.print (' ');
					output.print (attr.getNodeName ());
					output.print ("=\"");
					escape (attr.getNodeValue (), true);
					output.print ('\"');
				}
			
				if (element.hasChildNodes ()) {
					output.print ('>');
					
					for (Node child = element.getFirstChild (); child != null;) {
						write (child);
						child = child.getNextSibling ();
					}
				
					output.print ("</");
					output.print (element.getNodeName ());
					output.print ('>');
				}
				else
					output.print ("/>");
				
				break;
			}
		
		case Node.COMMENT_NODE:
			{
				output.print ("<!--");
				output.print (((Comment) node).getData ());
				output.print ("-->");
				break;
			}
		
		case Node.TEXT_NODE:
			{
				output.print (((Text) node).getData ());
				break;
			}
		
		case Node.CDATA_SECTION_NODE:
			{
				output.print ("<![CDATA[");
				output.print (((CDATASection) node).getData ());
				output.print ("]]>");
				break;
			}
		
		case Node.ENTITY_REFERENCE_NODE:
			{
				output.print ('&');
				output.print (((EntityReference) node).getNodeValue ());
				output.print (';');
				break;
			}
		
		case Node.PROCESSING_INSTRUCTION_NODE:
			{
				output.print ("<?");
				output.print (((ProcessingInstruction) node).getNodeName ());
		
				String value = ((ProcessingInstruction) node).getNodeValue ();
				if ((value != null) && (value.length () > 0)) {
					output.print (' ');
					output.print (value);
				}
				output.print ("?>");
				break;
			}
		}
	}
}