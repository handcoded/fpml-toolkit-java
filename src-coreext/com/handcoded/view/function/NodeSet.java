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

package com.handcoded.view.function;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.handcoded.xml.MutableNodeList;

public final class NodeSet implements XPathFunction
{
	public NodeSet ()
	{ }
	
	@SuppressWarnings("rawtypes")
	public Object evaluate (List args) throws XPathFunctionException
	{
		MutableNodeList		list = new MutableNodeList ();
		
		for (int index = 0; index < args.size (); ++index) {
			Object object = args.get (index);
			
			if (object instanceof Node)
				list.add ((Node) object);
			else if (object instanceof NodeList)
				list.addAll ((NodeList) object);
			else
				throw new XPathFunctionException ("Bad argument (" + (index + 1) + ")");
		}
		
		return (list);
	}
}