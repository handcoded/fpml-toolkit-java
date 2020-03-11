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

package com.handcoded.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
 * @author	Andrew Jacobs
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
	 * @param	name			The required type name.
	 * @return	A <CODE>NodeList</CODE> of corresponding nodes.
	 * @since	TFP 1.1
	 */
	public NodeList getElementsByType (final String ns, final String name)
	{
		SchemaType	target	= new SchemaType (ns, name);
		MutableNodeList result = compatibleElements.get (target);
		
//		System.err.println ("%% Looking for " + target);

		// Derive compatible elements on the first call 
		if (result == null) {
			result = new MutableNodeList ();
			
			// Find all the related types in the document
			ArrayList<SchemaType> types = new ArrayList<> ();
			
			for (Entry<SchemaType, TypeInfo> entry : uniqueTypes.entrySet ()) {
				if (entry.getKey ().matches (ns, name)
					|| entry.getValue ().isDerivedFrom (ns, name, TypeInfo.DERIVATION_EXTENSION | TypeInfo.DERIVATION_RESTRICTION))
				types.add (entry.getKey ());
			}
			
			// Then find all the elements of the compatible types
			for (SchemaType type : types) {
//				System.err.println ("%% + " + type);
				NodeList	list = elementsByType.get (type);
				if (list != null) result.addAll (list);
			}
						
			compatibleElements.put (target, result);
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
		NodeList list = attributesByName.get (name);
		
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
	 * A <CODE>HashMap</CODE> containing <CODE>MutableNodeList</CODE>
	 * instances indexed by element name.
	 * @since	TFP 1.0
	 */
	private HashMap<String, MutableNodeList> elementsByName
		= new HashMap<> ();
	
	/**
	 * A <CODE>HashMap</CODE> containing <CODE>MutableNodeList</CODE>
	 * instances indexed by element type.
	 * @since	TFP 1.1
	 */
	private HashMap<SchemaType, MutableNodeList> elementsByType
		= new HashMap<> ();
	
	/**
	 * A <CODE>HashMap</CODE> containing <CODE>Element</CODE>
	 * instances indexed by id value.
	 * @since	TFP 1.0
	 */
	private HashMap<String, Node> elementsById
		= new HashMap<> ();
	
	/**
	 * A <CODE>HashMap</CODE> containing <CODE>SchemaType</CODE>
	 * instances indexed by name.
	 * @since	TFP 1.2
	 */
	private HashMap<String, ArrayList<SchemaType>> typesByName
		= new HashMap<> ();
	
	/**
	 * A <CODE>HashSet</CODE> of all the unique <CODE>TypeInfo</CODE> instances
	 * referenced from the document.
	 * @since	TFP 1.11
	 */
	private HashMap<SchemaType,TypeInfo> uniqueTypes
		= new HashMap<> ();
	
	/**
	 * For each explored type <CODE>compatibleElemenrs</CODE> contains a
	 * <CODE>MutableNodeList</CODE> of elements based on types derived by
	 * extension or restriction.
	 * @since	TFP 1.11
	 */
	private HashMap<SchemaType, MutableNodeList> compatibleElements
		= new HashMap<> ();
	
	/**
	 * A <CODE>HashMap</CODE> containing <CODE>MutableNodeList</CODE>
	 * instances indexed by attribute name.
	 * @since	TFP 1.5
	 */
	private HashMap<String, MutableNodeList> attributesByName
		= new HashMap<> ();
	
	/**
	 * Recursively walks a DOM tree creating an index of the elements by
	 * their local name.
	 *
	 * @param	node 			The next node to be indexed.
	 * @since	TFP 1.0
	 */
	private void indexNodes (Node node)	// NOSONAR
	{
		switch (node.getNodeType ()) {
		case Node.DOCUMENT_NODE:
				indexNodes (((Document) node).getDocumentElement ());
				break;
				
		case Node.ELEMENT_NODE:
			{
				String name = ((Element) node).getLocalName ();
				
				MutableNodeList list = elementsByName.computeIfAbsent (name,
						key -> new MutableNodeList ());
			
				list.add (node);
								
				TypeInfo typeInfo = ((Element) node).getSchemaTypeInfo ();
				if ((typeInfo != null) && ((name = typeInfo.getTypeName ()) != null)) {
					ArrayList<SchemaType> types = typesByName.computeIfAbsent (name, key -> new ArrayList<> ());
					SchemaType		type = null;
					
//					System.err.println ("%% Found " + typeInfo.getTypeNamespace () + ":" + typeInfo.getTypeName ());
					for (SchemaType item : types) {
						if (item.matches (typeInfo)) {
							type = item;
							break;
						}
					}
					if (type == null) {
						type = SchemaType.forTypeInfo (typeInfo);
						types.add (type);
						
						uniqueTypes.put (type, typeInfo);
//						System.err.println ("%% Added " + type);
					}
					
					list = elementsByType.computeIfAbsent (type,
							key -> new MutableNodeList ());
					
					list.add (node);
				}
				
				Attr id = ((Element) node).getAttributeNode ("id");
				
				if (id != null) elementsById.put (id.getValue (), node);
				
				NamedNodeMap map = ((Element) node).getAttributes ();
				for (int index = 0; index < map.getLength (); ++index) {
					Attr attr = (Attr) map.item (index);
					
					list = attributesByName.computeIfAbsent (attr.getName (),
							key -> new MutableNodeList ());
					
					list.add (attr);					
				}
				
				for (Node child = node.getFirstChild (); child != null; child = child.getNextSibling ()) {
					if (child.getNodeType () == Node.ELEMENT_NODE)
						indexNodes (child);
				}
				break;
			}
			
		default:
		}
	}
}