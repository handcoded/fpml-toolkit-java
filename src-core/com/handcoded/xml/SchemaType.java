package com.handcoded.xml;
//Copyright (C),2005-2020 HandCoded Software Ltd.
//All rights reserved.
//
//This software is licensed in accordance with the terms of the 'Open Source
//License (OSL) Version 3.0'. Please see 'license.txt' for the details.
//
//HANDCODED SOFTWARE LTD MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
//SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
//LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//PARTICULAR PURPOSE, OR NON-INFRINGEMENT. HANDCODED SOFTWARE LTD SHALL NOT BE
//LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
//OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.


import java.util.Objects;

import org.w3c.dom.TypeInfo;

/**
 * Instances of <CODE>SchemaType</CODE> are used to represent unique types
 * discovered while indexing a DOM document.
 * 
 * @author 	Andrew Jacobs
 * @since	TFP 1.11
 */
final class SchemaType
{
	/**
	 * Constructs a <CODE>SchemaType</CODE> instance for the given namespace
	 * and type name.
	 * 
	 * @param	ns				The namespace.
	 * @param	name			The type name.
	 */
	public SchemaType (String ns, String name)
	{
		this.ns   	  = ns;
		this.name 	  = name;
	}
	
	/**
	 * Constructs a <CODE>SchemaType</CODE> from the values within a <CODE>
	 * TypeInfo</CODE> instance.
	 * 
	 * @param	typeInfo		The source <CODE>TypeInfo</CODE> instance.
	 * @return	A <CODE>SchemaType</CODE> constructed from the type's namespace
	 * 			and name.
	 * @since	TFP 1.11
	 */
	public static SchemaType forTypeInfo (TypeInfo typeInfo)
	{
		return (new SchemaType (typeInfo.getTypeNamespace (), typeInfo.getTypeName ()));
	}
	
	/**
	 * Provides access to the namespace.
	 * 
	 * @return	The namespace.
	 * @since	TFP 1.11
	 */
	public String getNamespace ()
	{
		return (ns);
	}
	
	/**
	 * Provides access to the type name,
	 * 
	 * @return	The type name.
	 * @since	TFP 1.11
	 */
	public String getName ()
	{
		return (name);
	}
	
	/**
	 * Returns the hashcode for this instance.
	 * 
	 * @return	The hashcode value.
	 * @since	TFP 1.11
	 */
	@Override
	public int hashCode ()
	{
		return (Objects.hash (ns, name));
	}
	
	/**
	 * Returns a string formed from the namespace and type name.
	 * 
	 * @return	A formatted display string.
	 * @since	TFP 1.11
	 */
	@Override
	public String toString ()
	{
		return (ns + ":" + name);
	}
	
	/**
	 * Determines if this instance is identical to another <CODE>Object
	 * </CODE>.
	 * 
	 * @param	other			The <CODE>Object</CODE> to compare with.
	 * @return	<CODE>true</CODE> if both objects are identical.
	 * @since	TFP 1.11
	 */
	@Override
	public boolean equals (Object other)
	{
		return ((other instanceof SchemaType) && equals ((SchemaType) other));
	}
	
	/**
	 * Determines if this instance contains the same values as another.
	 * 
	 * @param 	other			The <CODE>SchemaType</CODE> to compare with.
	 * @return	<CODE>true</CODE> if both objects are identical.
	 * @since	TFP 1.11
	 */
	public boolean equals (SchemaType other)
	{
		return ((other != null) && matches (other.ns, other.name));
	}
	
	/**
	 * Determine if an instance matches the specified namespace and type name.
	 * 
	 * @param	targetNS		The target namespace.
	 * @param	targetName		The target type name.
	 * @return	A flag indicating if the namespace and type name match.
	 */
	public boolean matches (String targetNS, String targetName)
	{
		return (ns.equals (targetNS) && name.equals (targetName));
	}
	
	/**
	 * Determine if an instance matches the type described by a <CODE>TypeInfo
	 * </CODE> instance.
	 * 
	 * @param	typeInfo		The target <CODE>TypeInfo</CODE> instance.
	 * @return	A flag indicating if instance values match.
	 */
	public boolean matches (TypeInfo typeInfo)
	{
		return (matches (typeInfo.getTypeNamespace (), typeInfo.getTypeName ()));
	}
	
	/**
	 * The namespace.
	 * @since	TFP 1.11
	 */
	private final String	ns;
	
	/**
	 * The type name
	 * @since	TFP 1.11
	 */
	private final String	name;
}