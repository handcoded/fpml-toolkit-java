// Copyright (C),2005-2018 HandCoded Software Ltd.
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

package com.handcoded.xml.difference;

import java.util.Vector;

public final class DifferenceList
{
	public double getTotalScore ()
	{
		return (totalScore);
	}
	
	public int size ()
	{
		return (differences.size());
	}
	
	public Difference elementAt (int index)
	{
		return (differences.elementAt (index));
	}
	
	protected DifferenceList ()
	{ }
	
	protected void add (Difference difference)
	{
		differences.add (difference);
		totalScore += difference.getScore ();
	}
	
	protected void add (DifferenceList list)
	{
		for (Difference difference : list.differences)
			add (difference);
	}
	
	private double totalScore = 0.0;
	
	private Vector<Difference>	differences
		= new Vector<Difference> ();
}