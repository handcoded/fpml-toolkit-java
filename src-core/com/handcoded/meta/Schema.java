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

package com.handcoded.meta;

/**
 * The <CODE>Schema</CODE> interface provides access to information that
 * describes an XML Schema based grammar.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.0
 */
public interface Schema extends Grammar
{
	/**
	 * The standard URL for XML namespaces.
	 * @since	TFP 1.0
	 */
	public static final String 	NAMESPACES_URL
		= "http://www.w3.org/2000/xmlns/";
	
	/**
	 * The standard URL for XML schema 1.0
	 * @since	TFP 1.0
	 */
	public static final String	SCHEMA_URL
		= "http://www.w3.org/2001/XMLSchema";
	
	/**
	 * The standard URL from XML schema 1.0 instances.
	 * @since	TFP 1.0
	 */
	public static final String	INSTANCE_URL
		= "http://www.w3.org/2001/XMLSchema-instance";
	
	/**
	 * Returns the namespace URI associated with this <CODE>Schema</CODE>
	 * instance.
	 * 
	 * @return	The <CODE>Schema</CODE> instance's namespace URI.
	 * @since	TFP 1.0
	 */
	public String getNamespaceUri ();
	
	/**
	 * Returns the schema location associated with this <CODE>Schema</CODE>
	 * instance.
	 * 
	 * @return	The <CODE>Schema</CODE> instance's schema location.
	 * @since	TFP 1.0
	 */
	public String getSchemaLocation ();
	
	/**
	 * Returns the preferred prefix to be used to reference the namespace.
	 * 
	 * @return	The preferred prefix string.
	 * @since	TFP 1.0
	 */
	public String getPreferredPrefix ();
	
	/**
	 * Returns an alternative prefix for the namespace to be used if the
	 * preferred prefix has already been used. The alternate profix should
	 * be defined such that it will be unlikely to be used by another schema
	 * (e.g. for FpML 4-0 the preferred prefix might be 'fpml' and the
	 * alternate 'fpml4-0').
	 * 
	 * @return	The alternate prefix string.
	 * @since	TFP 1.0
	 */
	public String getAlternatePrefix ();
}