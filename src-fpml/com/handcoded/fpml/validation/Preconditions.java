// Copyright (C),2005-2019 HandCoded Software Ltd.
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

package com.handcoded.fpml.validation;

import com.handcoded.fpml.Releases;
import com.handcoded.validation.Precondition;

/**
 * The <CODE>Preconditions</CODE> interface defines a set of <CODE>Precondition
 * </CODE> instances used to detect releases and various products.
 *
 * @author	BitWise
 * @since	TFP 1.0
 */
public interface Preconditions
{
	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 1-0 compatible
	 * documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition 	R1_0
		= new VersionPrecondition ("1-0");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 2-0 compatible
	 * documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition 	R2_0
		= new VersionPrecondition ("2-0");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 3-0 compatible
	 * documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition 	R3_0
		= new VersionPrecondition ("3-0");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-0 compatible
	 * documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition 	R4_0
		= new VersionPrecondition ("4-0");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-1 compatible
	 * documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition 	R4_1
		= new VersionPrecondition ("4-1");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-2 compatible
	 * documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition 	R4_2
		= new VersionPrecondition ("4-2");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-3 compatible
	 * documents.
	 * @since	TFP 1.1
	 */
	public static final Precondition 	R4_3
		= new VersionPrecondition ("4-3");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-4 compatible
	 * documents.
	 * @since	TFP 1.2
	 */
	public static final Precondition 	R4_4
		= new VersionPrecondition ("4-4");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-5 compatible
	 * documents.
	 * @since	TFP 1.2
	 */
	public static final Precondition 	R4_5
		= new VersionPrecondition ("4-5");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-6 compatible
	 * documents.
	 * @since	TFP 1.3
	 */
	public static final Precondition 	R4_6
		= new VersionPrecondition ("4-6");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-7 compatible
	 * documents.
	 * @since	TFP 1.4
	 */
	public static final Precondition 	R4_7
		= new VersionPrecondition ("4-7");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-8 compatible
	 * documents.
	 * @since	TFP 1.4
	 */
	public static final Precondition 	R4_8
		= new VersionPrecondition ("4-8");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 4-9 compatible
	 * documents.
	 * @since	TFP 1.5
	 */
	public static final Precondition 	R4_9
		= new VersionPrecondition ("4-9");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-0 compatible
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_0
		= new VersionPrecondition ("5-0");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-1 compatible
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_1
		= new VersionPrecondition ("5-1");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-2 compatible
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_2
		= new VersionPrecondition ("5-2");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-3 compatible
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_3
		= new VersionPrecondition ("5-3");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-4 compatible
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_4
		= new VersionPrecondition ("5-4");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-5 compatible
	 * documents.
	 * @since	TFP 1.8
	 */
	public static final Precondition 	R5_5
		= new VersionPrecondition ("5-5");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-6 compatible
	 * documents.
	 * @since	TFP 1.8
	 */
	public static final Precondition 	R5_6
		= new VersionPrecondition ("5-6");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-7 compatible
	 * documents.
	 * @since	TFP 1.8
	 */
	public static final Precondition 	R5_7
		= new VersionPrecondition ("5-7");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-8 compatible
	 * documents.
	 * @since	TFP 1.8
	 */
	public static final Precondition 	R5_8
		= new VersionPrecondition ("5-8");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-x confirmation
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_0__LATER_CONFIRMATION
		= new NamespacePrecondition (Releases.R5_0_CONFIRMATION);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-x reporting
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_0__LATER_REPORTING
		= new NamespacePrecondition (Releases.R5_0_REPORTING);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-x transparency
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_3__LATER_TRANSPARENCY
		= new NamespacePrecondition (Releases.R5_3_TRANSPARENCY);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-x record keeping
	 * documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition 	R5_3__LATER_RECORDKEEPING
		= new NamespacePrecondition (Releases.R5_3_RECORDKEEPING);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML 5-x pretrade
	 * documents.
	 * @since	TFP 1.8
	 */
	public static final Precondition 	R5_5__LATER_PRETRADE
		= new NamespacePrecondition (Releases.R5_5_PRETRADE);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions that use
	 * XPointer syntax for intra-document links.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R1_0__R2_0
		= new VersionRangePrecondition ("1-0", "2-0");

    /**
	 * A <CODE>Precondition</CODE> instance that detects either FpML 1-0,
	 * 2-0 or 3-0 compatible documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R1_0__R3_0
		= new VersionRangePrecondition ("1-0", "3-0");

    /**
	 * A <CODE>Precondition</CODE> instance that detects FpML 1-0 up to
	 * 4-6 compatible documents.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R1_0__R4_6
		= new VersionRangePrecondition ("1-0", "4-6");

    /**
	 * A <CODE>Precondition</CODE> instance that detects FpML 1-0 up to
	 * 4-9 compatible documents.
	 * @since	TFP 1.7
	 */
	public static final Precondition	R1_0__R4_9
		= new VersionRangePrecondition ("1-0", "4-9");

    /**
	 * A <CODE>Precondition</CODE> instance that detects either FpML 2-0 or
	 * 3-0 compatible documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R2_0__R3_0
		= new VersionRangePrecondition ("2-0", "3-0");

    /**
	 * A <CODE>Precondition</CODE> instance that detects either FpML 3-0 or
	 * 4-0 compatible documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R3_0__R4_0
		= new VersionRangePrecondition ("3-0", "4-0");

    /**
	 * A <CODE>Precondition</CODE> instance that detects either FpML 3-0 or
	 * 4-* compatible documents.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R3_0__R4_X
		= new VersionRangePrecondition ("3-0", "4-9999");

    /**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-0
	 * or 4-1 compatible document.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R4_0__R4_1
		= new VersionRangePrecondition ("4-0", "4-1");

    /**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-*
	 * compatible document.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R4_0__R4_X
		= new VersionRangePrecondition ("4-0", "4-9999");

    /**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-1 or
	 * later 4-* compatible document.
	 * @since	TFP 1.7
	 */
	public static final Precondition	R4_1__R4_X
		= new VersionRangePrecondition ("4-1", "4-9999");

    /**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-2
	 * thru 4-4 compatible document.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R4_2__R4_4
		= new VersionRangePrecondition ("4-2", "4-4");

    /**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-*
	 * compatible document from 4-2 onwards.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R4_2__R4_X
		= new VersionRangePrecondition ("4-2", "4-9999");

    /**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-*
	 * compatible document from 4-3 onwards.
	 * @since	TFP 1.7
	 */
	public static final Precondition	R4_3__R4_X
		= new VersionRangePrecondition ("4-3", "4-9999");

    /**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 4-*
	 * compatible document from 4-4 onwards.
	 * @since	TFP 1.7
	 */
	public static final Precondition	R4_4__R4_X
		= new VersionRangePrecondition ("4-4", "4-9999");

    /**
	 * A <CODE>Precondition</CODE> instance that detects any FpML 5-0
	 * to 5-3 compatible document.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R5_0__R5_3
		= new VersionRangePrecondition ("5-0", "5-3");

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML version 1-0 and
	 * later.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R1_0__LATER
		= new VersionRangePrecondition ("1-0", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML version 2-0 and
	 * later.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R2_0__LATER
		= new VersionRangePrecondition ("2-0", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML version 3-0 and
	 * later.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R3_0__LATER
		= new VersionRangePrecondition ("3-0", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-0 and
	 * later.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R4_0__LATER
		= new VersionRangePrecondition ("4-0", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-1 and
	 * later.
	 * @since	TFP 1.0
	 */
	public static final Precondition	R4_1__LATER
		= new VersionRangePrecondition ("4-1", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-2 and
	 * later.
	 * @since	TFP 1.1
	 */
	public static final Precondition	R4_2__LATER
		= new VersionRangePrecondition ("4-2", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-3 and
	 * later.
	 * @since	TFP 1.2
	 */
	public static final Precondition	R4_3__LATER
		= new VersionRangePrecondition ("4-3", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-4 and
	 * later.
	 * @since	TFP 1.2
	 */
	public static final Precondition	R4_4__LATER
		= new VersionRangePrecondition ("4-4", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-5 and
	 * later.
	 * @since	TFP 1.2
	 */
	public static final Precondition	R4_5__LATER
		= new VersionRangePrecondition ("4-5", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-6 and
	 * later.
	 * @since	TFP 1.3
	 */
	public static final Precondition	R4_6__LATER
		= new VersionRangePrecondition ("4-6", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-7 and
	 * later.
	 * @since	TFP 1.4
	 */
	public static final Precondition	R4_7__LATER
		= new VersionRangePrecondition ("4-7", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-8 and
	 * later.
	 * @since	TFP 1.4
	 */
	public static final Precondition	R4_8__LATER
		= new VersionRangePrecondition ("4-8", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 4-8 and
	 * later.
	 * @since	TFP 1.5
	 */
	public static final Precondition	R4_9__LATER
		= new VersionRangePrecondition ("4-9", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-0 and
	 * later.
	 * @since	TFP 1.4
	 */
	public static final Precondition	R5_0__LATER
		= new VersionRangePrecondition ("5-0", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-1 and
	 * later.
	 * @since	TFP 1.5
	 */
	public static final Precondition	R5_1__LATER
		= new VersionRangePrecondition ("5-1", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-2 and
	 * later.
	 * @since	TFP 1.7
	 */
	public static final Precondition	R5_2__LATER
		= new VersionRangePrecondition ("5-2", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-3 and
	 * later.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R5_3__LATER
		= new VersionRangePrecondition ("5-3", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-4 and
	 * later.
	 * @since	TFP 1.6
	 */
	public static final Precondition	R5_4__LATER
		= new VersionRangePrecondition ("5-4", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-5 and
	 * later.
	 * @since	TFP 1.7
	 */
	public static final Precondition	R5_5__LATER
		= new VersionRangePrecondition ("5-5", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-6 and
	 * later.
	 * @since	TFP 1.7
	 */
	public static final Precondition	R5_6__LATER
		= new VersionRangePrecondition ("5-6", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-7 and
	 * later.
	 * @since	TFP 1.8
	 */
	public static final Precondition	R5_7__LATER
		= new VersionRangePrecondition ("5-7", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-8 and
	 * later.
	 * @since	TFP 1.8
	 */
	public static final Precondition	R5_8__LATER
		= new VersionRangePrecondition ("5-8", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-9 and
	 * later.
	 * @since	TFP 1.10
	 */
	public static final Precondition	R5_9__LATER
		= new VersionRangePrecondition ("5-9", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-10 and
	 * later.
	 * @since	TFP 1.10
	 */
	public static final Precondition	R5_10__LATER
		= new VersionRangePrecondition ("5-10", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects FpML versions 5-11 and
	 * later.
	 * @since	TFP 1.10
	 */
	public static final Precondition	R5_11__LATER
		= new VersionRangePrecondition ("5-11", null);

	/**
	 * A <CODE>Precondition</CODE> instance that detects all FpML versions except
	 * 4-0.
	 * @since	TFP 1.0
	 */
	public static final Precondition	NOT_R4_0
		= Precondition.not (R4_0);

	/**
	 * A <CODE>Precondition</CODE> instance that detects all confirmation
     * view documents.
     * @since	TFP 1.7
	 */
	public static final	Precondition	CONFIRMATION
		= Precondition.or (new Precondition []
		   	{
				R1_0__R3_0,
				R4_0__R4_X,
				R5_0__LATER_CONFIRMATION
		   	});

	/**
	 * A <CODE>Precondition</CODE> instance that detects all reporting
     * view documents.
     * @since	TFP 1.7
	 */
	public static final	Precondition	REPORTING
		= Precondition.or (new Precondition []
		   	{
				R4_1__R4_X,
				R5_0__LATER_REPORTING
		   	});
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects all record keeping
     * view documents.
     * @since	TFP 1.7
	 */
	public static final	Precondition	RECORDKEEPING
		= R5_3__LATER_RECORDKEEPING;
	
	/**
	 * A <CODE>Precondition</CODE> instance that detects all transparency
     * view documents.
     * @since	TFP 1.7
	 */
	public static final	Precondition	TRANSPARENCY
		= R5_3__LATER_TRANSPARENCY;

	/**
	 * A <CODE>Precondition</CODE> instance that detects all pre-trade
     * view documents.
     * @since	TFP 1.8
	 */
	public static final	Precondition	PRETRADE
		= R5_5__LATER_PRETRADE;
}