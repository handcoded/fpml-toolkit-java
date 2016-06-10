// Copyright (C),2006-2011 HandCoded Software Ltd.
// All rights reserved.
//
// This software is the confidential and proprietary information of HandCoded
// Software Ltd. ("Confidential Information").  You shall not disclose such
// Confidential Information and shall use it only in accordance with the terms
// of the license agreement you entered into with HandCoded Software.
//
// HANDCODED SOFTWARE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
// SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. HANDCODED SOFTWARE SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
// OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

package com.handcoded.classification.xml;

import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.handcoded.classification.Category;
import com.handcoded.classification.ClassificationScheme;
import com.handcoded.framework.Application;
import com.handcoded.meta.Release;
import com.handcoded.meta.Specification;
import com.handcoded.xml.DOM;
import com.handcoded.xml.XPath;
import com.handcoded.xml.XmlUtility;

/**
 * The <CODE>ClassificationLoader</CODE> class provides the logic to convert a
 * classification defined within an XML file into a set of <CODE>Category</CODE>
 * instance held in an indexed <CODE>Classification</CODE>
 * 
 * @author 	BitWise
 * @version	$Id: ClassificationLoader.java 17 2011-11-16 00:30:12Z andrew $
 * @since	TFP 1.6
 */
public final class ClassificationLoader
{
	/**
	 * Parses the XML classification file indicated by the filename and
	 * constructs an appropriate set of <CODE>Category</CODE> instances
	 * held in an indexed <CODE>Classification</CODE>.
	 *  
	 * @param 	filename		The name of the configuration file.
	 * @return	A populated <CODE>Classification</CODE> instance.
	 * @since	TFP 1.6
	 */
	public static ClassificationScheme load (final String filename)
	{
		ClassificationScheme	classification	= new ClassificationScheme ();
		Hashtable<String, Category>	idMap = new Hashtable<String, Category> ();
		
		Document document = XmlUtility.nonValidatingParse (
				new InputSource (Application.openStream (filename)));

		NodeList list = DOM.getChildElements (document.getDocumentElement ());
		for (int index = 0; index < list.getLength (); ++index) {
			Element context = (Element) list.item (index);
			
			if (context.getLocalName ().equals ("category")) {
				String	id		= context.getAttribute ("id");
				String	name 	= context.getAttribute ("name");
				String	supers 	= context.getAttribute ("superClasses");
				String	abstr	= context.getAttribute ("abstract");
				ExprNode expr	= loadExpr (DOM.getFirstChild (context));
				
				boolean concrete = (expr != null) && !((abstr != null) && (abstr.equals ("true")));
				Category category;
				
				if ((supers != null) && !supers.isEmpty ()) {
					String [] ids = supers.split (" ");
					Category [] objs = new Category [ids.length];
					
					for (int count = 0; count < ids.length; ++count)
						objs [count] = idMap.get (ids [count]);
					
					category = new XmlCategory (classification, name, concrete, objs, expr);
				}
				else
					category = new XmlCategory (classification, name, concrete, expr);

				if ((id != null) && !id.isEmpty())
					idMap.put(id, category);
			}
		}
		return (classification);
	}
	
	/**
	 * Ensures instances of <CODE>ClassificationLoader</CODE> cannot be constructed.
	 * @since	TFP 1.6
	 */
	private ClassificationLoader ()
	{ }
	
	/**
	 * Recursively constructs the <CODE>ExprNode</CODE> instance used to
	 * represent the classification expressions.
	 * 
	 * @param 	element			The context <CODE>Element</CODE>.
	 * @return	A <CODE>ExprNode</CODE> representing the expression.
	 * @since	TFP 1.6
	 */
	private static ExprNode loadExpr (Element element)
	{
		if (element != null) {
			if (element.getLocalName ().equals ("if")) {
				Element	testElement = XPath.path (element, "test");
				Element	thenElement = XPath.path (element, "then");
				Element	elseElement = XPath.path (element, "else");
				ExprNode testExpr;
				ExprNode thenExpr;
				ExprNode elseExpr;
				
				if (testElement.hasAttribute ("test"))
					testExpr = new XPathNode (testElement.getAttribute ("test"));
				else
					testExpr = loadExpr (DOM.getFirstChild (testElement));
					
				if (thenElement.hasAttribute ("test"))
					thenExpr = new XPathNode (thenElement.getAttribute ("test"));
				else
					thenExpr = loadExpr (DOM.getFirstChild (thenElement));
	
				if (elseElement.hasAttribute ("test"))
					elseExpr = new XPathNode (elseElement.getAttribute ("test"));
				else
					elseExpr = loadExpr (DOM.getFirstChild (elseElement));
	
				return (new IfNode (testExpr, thenExpr, elseExpr));
			}
			else if (element.getLocalName ().equals ("release")) {
				Specification	spec	= Specification.forName (element.getAttribute ("specification"));
				Release			vers	= spec.getReleaseForVersion (element.getAttribute ("version"));
				
				return (new ReleaseNode (spec, vers));
			}
			else if (element.getLocalName ().equals ("range")) {
				Specification	spec	= Specification.forName (element.getAttribute ("specification"));
				Release			lower	= null;
				Release			upper	= null;
				String			version;
				
				if ((version = element.getAttribute ("lower")) != null)
					lower = spec.getReleaseForVersion (version);
				
				if ((version = element.getAttribute ("upper")) != null)
					upper = spec.getReleaseForVersion (version);
			
				return (new RangeNode (spec, lower, upper));
			}
			else if (element.getLocalName ().equals ("xpath")) {
				String	test	= element.getAttribute ("test");
				
				return (new XPathNode (test));
			}
			// And
			// Or
		}
		return (null);
	}
}