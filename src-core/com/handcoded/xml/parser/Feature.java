// Copyright (C),2005-2007 HandCoded Software Ltd.
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

package com.handcoded.xml.parser;

/**
 * The <CODE>Feature</CODE> class holds a number of strings used to identity
 * options that can be selected when creating an XML parser.
 *
 * @author	BitWise
 * @version	$Id: Feature.java 87 2007-01-18 13:00:13Z andrew_jacobs $
 * @since	TFP 1.0
 */
final class Feature
{
	/**
	 * Namespaces feature id (http://xml.org/sax/features/namespaces).
	 * @since	TFP 1.0
	 */
    public static final String		NAMESPACES_FEATURE_ID
		= "http://xml.org/sax/features/namespaces";

    /**
     * Validation feature id (http://xml.org/sax/features/validation).
	 * @since	TFP 1.0
     */
    public static final String		VALIDATION_FEATURE_ID
		= "http://xml.org/sax/features/validation";

    /**
     * Schema validation feature id (http://apache.org/xml/features/validation/schema).
	 * @since	TFP 1.0
     */
    public static final String		SCHEMA_VALIDATION_FEATURE_ID
		= "http://apache.org/xml/features/validation/schema";

    /**
     * Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking).
	 * @since	TFP 1.0
     */
    public static final String		SCHEMA_FULL_CHECKING_FEATURE_ID
		= "http://apache.org/xml/features/validation/schema-full-checking";

    /**
     * Deferred node expansion feature if (http://apache.org/xml/features/dom/defer-node-expansion).
     * @since	TFP 1.0.1
     */
    public static final String		DEFER_NODE_EXPANSION_FEATURE_ID
    	= "http://apache.org/xml/features/dom/defer-node-expansion";
    
	/**
	 * Ensures that an instance cannot be constructed.
	 * @since	TFP 1.0
	 */
	private Feature ()
	{ }
}