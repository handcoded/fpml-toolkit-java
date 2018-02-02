package com.handcoded.view;

import java.util.HashMap;

final class VariableSet
{
	public VariableSet ()
	{ }
	
	public Value get (String name)
	{
		return (values.get (name));
	}
	
	public void add (String name, Value value)
	{
		// TODO: Remove debugging
		System.out.println ("VS: " + name + " -> " + value.toDebug ());
		values.put (name, value);
	}
	
	public void add (Variable variable, Value value)
	{
		add (variable.getName (), value);
	}
	
	public void remove (String name)
	{
		values.remove (name);
	}
	
	public void remove (Variable variable)
	{
		remove (variable.getName ());
	}

	private HashMap<String, Value> values
		= new HashMap<String, Value> ();
}