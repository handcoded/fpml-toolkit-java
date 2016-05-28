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

package com.handcoded.xml;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

/**
 * The <CODE>NodeIndex</CODE> class builds an index of the elements that
 * comprise a DOM tree to allow subsequent queries to be efficiently executed.
 * <P>
 * A DOM <CODE>Document</CODE> instance performs an expensive in-order
 * traversal of the DOM tree every time the <CODE>getElementsByName</CODE>
 * method is called. This class does a one off traversal on construction and
 * then uses the cached results to return <CODE>NodeList</CODE> instances.
 * <P>
 * The <CODE>NodeIndex</CODE> also indexes 'id' attributes as the DOM has been
 * found to be unreliable at indexing these during post parse schema validation.
 * <P>
 * Since TFP 1.2 the <CODE>NodeIndex</CODE> also attempts to capture schema
 * type information for the DOM <CODE>Document</CODE> as it explores it.
 * Derivation relationships between types are determined and cached as calls
 * to <CODE>getElementsByType</CODE> are made.
 *
 * @author	BitWise
 * @version	$Id: NodeIndex.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class NodeIndex
{
	/**
	 * Constructs a <CODE>NodeIndex</CODE> that contains an index to all the
	 * elements in the given document.
	 *
	 * @param	document		The DOM <CODE>Document</CODE> to be indexed.
	 * @since	TFP 1.0
	 */
	public NodeIndex (Document document)
	{
		indexNodes (this.document = document);
	}
	
	/**
	 * Provides access to the <CODE>Document</CODE> that this instance
	 * indexes.
	 *
	 * @return	The original XML <CODE>Document</CODE>
	 * @since	TFP 1.0
	 */
	public Document getDocument ()
	{
		return (document);
	}
	
	/**
	 * Determines if the <CODE>NodeIndex</CODE> has managed to acquire
	 * type information during the indexing process.
	 * 
	 * @return	<CODE>true</CODE> if type information is available,
	 * 			<CODE>false</CODE> otherwise.
	 * @since	TFP 1.1
	 */
	public boolean hasTypeInformation ()
	{
		return (!typesByName.isEmpty ());
	}
	
	/**
	 * Creates a (possibly empty) <CODE>NodeList</CODE> containing all the
	 * element nodes identified by the given name string.
	 *
	 * @param	name			The required element name string.
	 * @return	A <CODE>NodeList</CODE> of corresponding nodes.
	 * @since	TFP 1.0
	 */
	public NodeList getElementsByName (final String name)
	{
		NodeList list = elementsByName.get (name);
		
		return ((list != null) ? list : EMPTY);
	}
	
	/**
	 * Creates a (possibly empty) <CODE>NodeList</CODE> containing all the
	 * element nodes identified by the given name strings.
	 *
	 * @param	names			An array of name strings for elements.
	 * @return	A <CODE>NodeList</CODE> of corresponding nodes.
	 * @since	TFP 1.0
	 */
	public NodeList getElementsByName (final String [] names)
	{
		MutableNodeList		result = new MutableNodeList ();
		
		for (int index = 0; index < names.length; ++index)
			result.addAll (getElementsByName (names [index]));
			
		return (result);
	}

	/**
	 * Creates a (possibly empty) <CODE>NodeList</CODE> containing all the
	 * element nodes of a given type (or a derived sub-type).
	 *
	 * @param	ns				The required namespace URI.
	 * @param	type			The required type name.
	 * @return	A <CODE>NodeList</CODE> of corresponding nodes.
	 * @since	TFP 1.1
	 */
	public NodeList getElementsByType (final String ns, final String type)
	{
		Vector<TypeInfo>	matches = compatibleTypes.get (type);
		
		if (matches == null) {
			compatibleTypes.put (type, matches = new Vector<TypeInfo> ());
			
//			System.err.println ("%% Looking for " + ns + ":" + type);
			
			Enumeration<String> cursor = typesByName.keys ();
			while (cursor.hasMoreElements ()) {
				String key  = (String) cursor.nextElement ();
				Vector<TypeInfo> types = typesByName.get (key);
				
				for (int index = 0; index < types.size (); ++index) {
					TypeInfo info = types.elementAt (index);
					
					if (type.equals (info.getTypeName ()) || info.isDerivedFrom (ns, type,
							TypeInfo.DERIVATION_EXTENSION | TypeInfo.DERIVATION_RESTRICTION)) {
						matches.add (info);
//						System.err.println ("%% Found: " + info.getTypeName ());
					}
				}
			}
		}
		
		MutableNodeList		result = new MutableNodeList ();
		
		for (int index = 0; index < matches.size (); ++index) {
			TypeInfo info  = matches.elementAt (index);
			NodeList nodes = elementsByType.get (info.getTypeName ());
			
//			System.err.println ("-- Matching elements of type: " + info.getTypeName ());
			
			for (int count = 0; count < nodes.getLength (); ++count) {
				Element  element  = (Element) nodes.item (count);
				TypeInfo typeInfo = element.getSchemaTypeInfo ();
				
				if (typeInfo.getTypeName().equals (info.getTypeName ()) &&
					typeInfo.getTypeNamespace().equals (info.getTypeNamespace ())) {
					result.add (element);
					
//					System.err.println ("-- Matched: " + element.getLocalName ());
				}
			}
		}
		
		return (result);
	}
	
	/**
	 * Returns the <CODE>Element</CODE> in the indexed document that has
	 * an id attribute with the given value.
	 * 
	 * @param 	id				The required id attribute value.
	 * @return	The matching <CODE>Element</CODE> or <CODE>null</CODE> if
	 * 			none. 
	 * @since	TFP 1.0
	 */
	public Element getElementById (final String id)
	{
		return ((Element) elementsById.get(id));
	}
	
	/**
	 * Creates a (possibly empty) <CODE>NodeList</CODE> containing all the
	 * attribute nodes identified by the given name string.
	 *
	 * @param	name			The required attrbute name string.
	 * @return	A <CODE>NodeList</CODE> of corresponding nodes.
	 * @since	TFP 1.5
	 */
	public NodeList getAttributesByName (final String name)
	{
		NodeList list = (NodeList) attributesByName.get (name);
		
		return ((list != null) ? list : EMPTY);
	}
	
	/**
	 * An empty <CODE>MutableNodeList</CODE> used when a requested element is
	 * not present.
	 * @since	TFP 1.0
	 */
	private static final NodeList EMPTY 	= new MutableNodeList ();
	
	/**
	 * The DOM <CODE>Document</CODE> from which this index is derived.
	 * @since	TFP 1.0
	 */
	private Document		document;
	
	/**
	 * A <CODE>Hashtable</CODE> containing <CODE>MutableNodeList</CODE>
	 * instances indexed by element name.
	 * @since	TFP 1.0
	 */
	private Hashtable<String, MutableNodeList> elementsByName
		= new Hashtable<String, MutableNodeList> ();
	
	/**
	 * A <CODE>Hashtable</CODE> containing <CODE>MutableNodeList</CODE>
	 * instances indexed by element type.
	 * @since	TFP 1.1
	 */
	private Hashtable<String, MutableNodeList> elementsByType
		= new Hashtable<String, MutableNodeList> ();
	
	/**
	 * A <CODE>Hashtable</CODE> containing <CODE>Element</CODE>
	 * instances indexed by id value.
	 * @since	TFP 1.0
	 */
	private Hashtable<String, Node> elementsById
		= new Hashtable<String, Node> ();
	
	/**
	 * A <CODE>Hashtable</CODE> containing <CODE>TypeInfo</CODE>
	 * instances indexed by name.
	 * @since	TFP 1.2
	 */
	private Hashtable<String, Vector<TypeInfo>>	typesByName
		= new Hashtable<String, Vector<TypeInfo>> ();
	
	/**
	 * For each explored type <CODE>compatibleType</CODE> contains a
	 * <CODE>Vector</CODE> of types derived by extension or restriction.
	 * @since	TFP 1.2
	 */
	private Hashtable<String, Vector<TypeInfo>> compatibleTypes
		= new Hashtable<String, Vector<TypeInfo>> ();
	
	/**
	 * A <CODE>Hashtable</CODE> containing <CODE>MutableNodeList</CODE>
	 * instances indexed by attribute name.
	 * @since	TFP 1.5
	 */
	private Hashtable<String, MutableNodeList> attributesByName
		= new Hashtable<String, MutableNodeList> ();
	
	/**
	 * Recursively walks a DOM tree creating an index of the elements by
	 * their local name.
	 *
	 * @param	node 			The next node to be indexed.
	 * @since	TFP 1.0
	 */
	private void indexNodes (Node node)
	{
		switch (node.getNodeType ()) {
		case Node.DOCUMENT_NODE:
				indexNodes (((Document) node).getDocumentElement ());
				break;
				
		case Node.ELEMENT_NODE:
			{
				String name = ((Element) node).getLocalName ();
				
				MutableNodeList list = (MutableNodeList) elementsByName.get (name);
			
				if (list == null)
					elementsByName.put (name, list = new MutableNodeList ());
					
				list.add (node);
								
				TypeInfo typeInfo = ((Element) node).getSchemaTypeInfo ();
				if ((typeInfo != null) && ((name = typeInfo.getTypeName ()) != null)) {
					Vector<TypeInfo> types = typesByName.get (name);
					int		index;
					
					if (types == null)
						typesByName.put(name, types = new Vector<TypeInfo> ());
					
					for (index = 0; index < types.size (); ++index) {
						TypeInfo info = (TypeInfo) types.elementAt (index);
						
						if (typeInfo.getTypeNamespace ().equals (info.getTypeNamespace ())) break;
					}
					if (index == types.size ()) types.add (typeInfo);
					
					list = (MutableNodeList) elementsByType.get (name);
					
					if (list == null)
						elementsByType.put (name, list = new MutableNodeList ());
						
					list.add (node);
				}
				
				Attr id = ((Element) node).getAttributeNode ("id");
				
				if (id != null) elementsById.put (id.getValue (), node);
				
				NamedNodeMap map = ((Element) node).getAttributes ();
				for (int index = 0; index < map.getLength (); ++index) {
					Attr attr = (Attr) map.item (index);
					
					list = (MutableNodeList) attributesByName.get (attr.getName ());
					
					if (list == null)
						attributesByName.put (attr.getName (), list = new MutableNodeList ());
					
					list.add (attr);					
				}
				
				for (Node child = node.getFirstChild (); child != null;) {
					if (child.getNodeType () == Node.ELEMENT_NODE)
						indexNodes (child);
						
					child = child.getNextSibling ();
				}
				break;
			}
		}
	}
}