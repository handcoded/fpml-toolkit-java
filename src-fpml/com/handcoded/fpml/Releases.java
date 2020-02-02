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

package com.handcoded.fpml;

import java.util.Enumeration;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.TypeInfo;

import com.handcoded.fpml.meta.DTDRelease;
import com.handcoded.fpml.meta.SchemaRelease;
import com.handcoded.fpml.util.Version;
import com.handcoded.meta.Conversion;
import com.handcoded.meta.Release;
import com.handcoded.meta.Schema;
import com.handcoded.meta.Specification;

/**
 * The <CODE>Releases</CODE> class contains a set of static objects describing
 * the FpML specification and its various releases.
 * 
 * @author 	BitWise
 * @since	TFP 1.0
 */
public final class Releases
{
	/**
	 * A <CODE>Specification</CODE> instance representing FpML as a whole.
	 * @since	TFP 1.0
	 */
	public static Specification	FPML
		= Specification.forName ("FpML");
	
	/**
	 * A <CODE>DTDRelease</CODE> instance containing the details for the
	 * FpML 1-0 recommendation.
	 * @since	TFP 1.0
	 */
	public static DTDRelease	R1_0
		= (DTDRelease) FPML.getReleaseForVersion ("1-0");
	
	/**
	 * A <CODE>DTDRelease</CODE> instance containing the details for the
	 * FpML 2-0 recommendation.
	 * @since	TFP 1.0
	 */
	public static DTDRelease	R2_0
		= (DTDRelease) FPML.getReleaseForVersion ("2-0");

	/**
	 * A <CODE>DTDRelease</CODE> instance containing the details for the
	 * FpML 3-0 trial recommendation.
	 * @since	TFP 1.0
	 */
	public static DTDRelease	R3_0
		= (DTDRelease) FPML.getReleaseForVersion ("3-0");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-0 recommendation.
	 * @since	TFP 1.0
	 */
	public static SchemaRelease	R4_0
		= (SchemaRelease) FPML.getReleaseForVersion ("4-0");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-1 recommendation.
	 * @since	TFP 1.0
	 */
	public static SchemaRelease	R4_1
		= (SchemaRelease) FPML.getReleaseForVersion ("4-1");
			
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-2 recommendation.
	 * @since	TFP 1.0
	 */
	public static SchemaRelease	R4_2
		= (SchemaRelease) FPML.getReleaseForVersion ("4-2");
			
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-3 recommendation.
	 * @since	TFP 1.0
	 */
	public static SchemaRelease	R4_3
		= (SchemaRelease) FPML.getReleaseForVersion ("4-3");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-4 recommendation.
	 * @since	TFP 1.2
	 */
	public static SchemaRelease	R4_4
		= (SchemaRelease) FPML.getReleaseForVersion ("4-4");

	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-5 recommendation.
	 * @since	TFP 1.2
	 */
	public static SchemaRelease	R4_5
		= (SchemaRelease) FPML.getReleaseForVersion ("4-5");

	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-6 recommendation.
	 * @since	TFP 1.3
	 */
	public static SchemaRelease	R4_6
	= (SchemaRelease) FPML.getReleaseForVersion ("4-6");

	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-7 recommendation.
	 * @since	TFP 1.4
	 */
	public static SchemaRelease	R4_7
		= (SchemaRelease) FPML.getReleaseForVersion ("4-7");

	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-8 recommendation.
	 * @since	TFP 1.4
	 */
	public static SchemaRelease	R4_8
		= (SchemaRelease) FPML.getReleaseForVersion ("4-8");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-9 recommendation.
	 * @since	TFP 1.5
	 */
	public static SchemaRelease	R4_9
		= (SchemaRelease) FPML.getReleaseForVersion ("4-9");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 4-10 recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R4_10
		= (SchemaRelease) FPML.getReleaseForVersion ("4-10");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-0 confirmation view recommendation.
	 * @since	TFP 1.3
	 */
	public static SchemaRelease	R5_0_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-0", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-0 reporting view recommendation.
	 * @since	TFP 1.3
	 */
	public static SchemaRelease	R5_0_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-0", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-1 confirmation view recommendation.
	 * @since	TFP 1.5
	 */
	public static SchemaRelease	R5_1_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-1", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-1 reporting view recommendation.
	 * @since	TFP 1.5
	 */
	public static SchemaRelease	R5_1_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-1", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-2 confirmation view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_2_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-2", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-2 reporting view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_2_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-2", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-3 confirmation view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_3_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-3", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-3 reporting view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_3_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-3", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-3 record keeping view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_3_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-3", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-3 transparency view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_3_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-3", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-4 confirmation view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_4_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-4", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-4 reporting view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_4_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-4", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-4 record keeping view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_4_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-4", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-4 transparency view recommendation.
	 * @since	TFP 1.6
	 */
	public static SchemaRelease	R5_4_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-4", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-5 confirmation view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_5_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-5", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-5 reporting view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_5_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-5", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-5 record keeping view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_5_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-5", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-5 transparency view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_5_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-5", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-5 pre-trade view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_5_PRETRADE
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-5", "http://www.fpml.org/FpML-5/pretrade");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-6 confirmation view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_6_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-6", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-6 reporting view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_6_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-6", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-6 record keeping view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_6_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-6", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-6 transparency view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_6_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-6", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-6 pre-trade view recommendation.
	 * @since	TFP 1.7
	 */
	public static SchemaRelease	R5_6_PRETRADE
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-6", "http://www.fpml.org/FpML-5/pretrade");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-7 confirmation view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_7_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-7", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-7 reporting view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_7_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-7", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-7 record keeping view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_7_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-7", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-7 transparency view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_7_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-7", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-7 pre-trade view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_7_PRETRADE
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-7", "http://www.fpml.org/FpML-5/pretrade");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-7 legal view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_7_LEGAL
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-7", "http://www.fpml.org/FpML-5/legal");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-8 confirmation view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_8_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-8", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-8 reporting view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_8_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-8", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-8 record keeping view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_8_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-8", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-8 transparency view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_8_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-8", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-8 pre-trade view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_8_PRETRADE
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-8", "http://www.fpml.org/FpML-5/pretrade");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-8 legal view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_8_LEGAL
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-8", "http://www.fpml.org/FpML-5/legal");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-9 confirmation view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_9_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-9", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-9 reporting view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_9_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-9", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-9 record keeping view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_9_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-9", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-9 transparency view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_9_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-9", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-9 pre-trade view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_9_PRETRADE
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-9", "http://www.fpml.org/FpML-5/pretrade");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-9 legal view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_9_LEGAL
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-9", "http://www.fpml.org/FpML-5/legal");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-9 confirmation view recommendation.
	 * @since	TFP 1.8
	 */
	public static SchemaRelease	R5_10_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-10", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-10 reporting view recommendation.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_10_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-10", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-10 record keeping view recommendation.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_10_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-10", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-10 transparency view recommendation.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_10_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-10", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-10 pre-trade view recommendation.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_10_PRETRADE
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-10", "http://www.fpml.org/FpML-5/pretrade");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-10 legal view recommendation.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_10_LEGAL
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-10", "http://www.fpml.org/FpML-5/legal");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-11 confirmation view working draft.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_11_CONFIRMATION
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-11", "http://www.fpml.org/FpML-5/confirmation");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-11 reporting view working draft.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_11_REPORTING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-11", "http://www.fpml.org/FpML-5/reporting");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-11 record keeping view working draft.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_11_RECORDKEEPING
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-11", "http://www.fpml.org/FpML-5/recordkeeping");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-11 transparency view working draft.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_11_TRANSPARENCY
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-11", "http://www.fpml.org/FpML-5/transparency");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-11 pre-trade view working draft.
	 * @since	TFP 1.10
	 */
	public static SchemaRelease	R5_11_PRETRADE
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-11", "http://www.fpml.org/FpML-5/pretrade");
	
	/**
	 * A <CODE>SchemaRelease</CODE> instance containing the details for
	 * FpML 5-11 legal view working draft.
	 * @since	TFP 1.11
	 */
	public static SchemaRelease	R5_11_LEGAL
		= (SchemaRelease) FPML.getReleaseForVersionAndNamespace ("5-11", "http://www.fpml.org/FpML-5/legal");
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 1-0 to 2-0
	 * transformation. The specific changes needed (other than basic DOCTYPE
	 * changes) are:
	 * <UL>
	 * <LI>The &lt;product&gt; container element was removed.</LI>
	 * <LI>Superfluous <CODE>type</CODE> attributes are removed.</LI>
	 * </UL> 
	 * @since	TFP 1.0
	 */
	public static final Conversion R1_0__R2_0
		= new Conversions.R1_0__R2_0 ();
		
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 2-0 to 3-0
	 * transformation. The specific changes needed (other than basic DOCTYPE
	 * changes) are:
	 * <UL>
	 * <LI><CODE>href</CODE> attributes are come <CODE>IDREF</CODE> strings
	 * rather then XLink expressions.</LI>
	 * <LI>Superfluous <CODE>type</CODE> attributes are removed.</LI>
	 * </UL> 
	 * @since	TFP 1.0
	 */
	public static final Conversion R2_0__R3_0
		= new Conversions.R2_0__R3_0 ();
			
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 3-0 to 4-0
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The document is becomes XML schema referencing.</LI>
	 * <LI>Legacy documents become FpML DataDocument instances.</LI>
	 * <LI>The <b>dateRelativeTo</b> referencing mechanism is changed.</LI>
	 * <LI>The value set for &lt;fraDiscounting&gt; was modified.</LI>
	 * <LI>The element &lt;calculationAgentPartyReference&gt; was moved from
	 * the trade header into the trade structure.</LI>
	 * <LI>The &lt;informationSource&gt; element is renamed &lt;primaryRateSource&gt;
	 * within &lt;fxSpotRateSource&gt; elements.</LI>
	 * <LI>The structure of the <B>equityOption</B> element is changed.</li>
	 * <LI>SchemeDefaults are removed and non-defaulted schemes appear
	 * on referencing elements,</LI>
	 * </UL>
	 * @since	TFP 1.0
	 */
	public static final Conversion R3_0__R4_0
		= new Conversions.R3_0__R4_0 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-0 to 4-1
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.0
	 */
	public static final Conversion R4_0__R4_1
		= new Conversions.R4_0__R4_1 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-1 to 4-2
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.1
	 */
	public static final Conversion R4_1__R4_2
		= new Conversions.R4_1__R4_2 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-2 to 4-3
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.2
	 */
	public static final Conversion R4_2__R4_3
		= new Conversions.R4_2__R4_3 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-3 to 4-4
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.2
	 */
	public static final Conversion R4_3__R4_4
		= new Conversions.R4_3__R4_4 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-4 to 4-5
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.2
	 */
	public static final Conversion R4_4__R4_5
		= new Conversions.R4_4__R4_5 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-5 to 4-6
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.3
	 */
	public static final Conversion R4_5__R4_6
		= new Conversions.R4_5__R4_6 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-6 to 4-7
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.6
	 */
	public static final Conversion R4_6__R4_7
		= new Conversions.R4_6__R4_7 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-7 to 4-8
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.6
	 */
	public static final Conversion R4_7__R4_8
		= new Conversions.R4_7__R4_8 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-8 to 4-9
	 * transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.6
	 */
	public static final Conversion R4_8__R4_9
		= new Conversions.R4_8__R4_9 ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-9 to 5-0
	 * confirmation view transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.7
	 */
	public static final Conversion R4_9__R5_0_CONFIRMATION
		= new Conversions.R4_9__R5_0_CONFIRMATION ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 4-9 to 5-0
	 * reporting view transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.7
	 */
	public static final Conversion R4_9__R5_0_REPORTING
		= new Conversions.R4_9__R5_0_REPORTING ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 5-0 to 5-1
	 * confirmation view transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.6
	 */
	public static final Conversion R5_0__R5_1_CONFIRMATION
		= new Conversions.R5_0__R5_1_CONFIRMATION ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 5-1 to 5-2
	 * confirmation view transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.6
	 */
	public static final Conversion R5_1__R5_2_CONFIRMATION
		= new Conversions.R5_1__R5_2_CONFIRMATION ();
	
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 5-2 to 5-3
	 * confirmation view transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.6
	 */
	public static final Conversion R5_2__R5_3_CONFIRMATION
		= new Conversions.R5_2__R5_3_CONFIRMATION ();
		
	/**
	 * A <CODE>Conversion</CODE> instance configured for FpML 5-3 to 5-4
	 * confirmation view transformation. The specific changes needed are:
	 * <UL>
	 * <LI>The FpML XML schema namespace URI is updated.</LI>
	 * </UL>
	 * @since	TFP 1.6
	 */
	public static final Conversion R5_3__R5_4_CONFIRMATION
		= new Conversions.R5_3__R5_4_CONFIRMATION ();
		
	/**
	 * Examines the provided <CODE>Document</CODE> to determine the associated
	 * FpML <CODE>Release</CODE> instance.
	 * 
	 * @param	document		A DOM <CODE>Document</CODE> instance.
	 * @return	The corresponding FpML <CODE>Release</CODE> or <CODE>null</CODE>
	 * 			if it cannot be determined.
	 * @since	TFP 1.0
	 */
	public static Release releaseFor (Document document)
	{
		Element		root = document.getDocumentElement ();
		String		version;
		
		if (root.getLocalName ().equals ("FpML")) {
			if ((version = root.getAttribute ("version")) != null)
				return (FPML.getReleaseForVersion (version));
		}
		
		if ((version = root.getAttribute ("fpmlVersion")) != null)
			return (FPML.getReleaseForVersionAndNamespace (version, root.getNamespaceURI ()));
		
		return (null);
	}

	/**
	 * Determine which <CODE>Release</CODE> could be used to represent the indicated
	 * <CODE>Document</CODE> at a specific target version. If the target version has
	 * multiple views then the message type is used to make the selection.
	 * 
	 * @param 	document			The source document being converted.
	 * @param 	targetVersionNumber	The target version number.
	 * @return	The <CODE>Release</CODE> instance for the target schema.
	 * @since	TFP 1.7
	 */
	public static Release compatibleRelease (Document document, String targetVersionNumber)
	{
		Release source = Specification.releaseForDocument (document);
		
		if (source != null)
			return (compatibleRelease (document, source, targetVersionNumber));
		
		return (null);
	}
	
	/**
	 * Determine which <CODE>Release</CODE> could be used to represent the indicated
	 * <CODE>Document</CODE> at a specific target version. If the target version has
	 * multiple views then the message type is used to make the selection.
	 * 
	 * @param 	document			The source document being converted.
	 * @param	source				The <CODE>Release</CODE> object for the source document.
	 * @param 	targetVersionNumber	The target version number.
	 * @return	The <CODE>Release</CODE> instance for the target schema.
	 * @since	TFP 1.7
	 */
	public static Release compatibleRelease (Document document, Release source, String targetVersionNumber)
	{
		Version		sourceVersion = Version.parse (source.getVersion ());
		Version		targetVersion = Version.parse (targetVersionNumber);
		String 		view 		= null;
		String		type		= null;

		// If the target this not 5.0 or later its a direct conversion
		if (targetVersion.getMajor () <= 4)
			return (Releases.FPML.getReleaseForVersion (targetVersionNumber));

		// Otherwise determine the target view based on version and message type
		if (sourceVersion.getMajor () <= 3)
			view = "confirmation";
		else if (sourceVersion.getMajor () == 4) {
			Element root = document.getDocumentElement ();
			TypeInfo info = root.getSchemaTypeInfo ();
			
			if ((info == null) || (info.getTypeName () == null)) {
				NamedNodeMap list = root.getAttributes ();
				for (int index = 0; index < list.getLength (); ++index) {
					Attr attr = (Attr) list.item (index);
					
					if ((attr.getNamespaceURI () != null)
							&& attr.getNamespaceURI ().equals (Schema.INSTANCE_URL)
							&& attr.getLocalName ().equals ("type")) {
						type = attr.getValue ();
						break;
					}
				}
			}
			else
				type = info.getTypeName ();

			// Look for messages that are in the reporting view
			if (type.equals ("CancelTradeCashflows")
					|| type.equals ("CreditEventNotification")
					|| type.equals ("PositionAcknowledged")
					|| type.equals ("PositionAsserted")
					|| type.equals ("PositionMatchResults")
					|| type.equals ("PositionReport")
					|| type.equals ("RequestPortfolio")
					|| type.equals ("RequestPositionReport")
					|| type.equals ("RequestValuationReport")
					|| type.equals ("TradeCashflowsAsserted")
					|| type.equals ("TradeCashflowsMatchResult")
					|| type.equals ("ValuationReport"))
				view = "reporting";
			else
				view = "confirmation";
		}
		else
			view = extractView (((SchemaRelease) source).getNamespaceUri ());
		
		// Find a release that matches the target version and view
		Enumeration<Release> cursor = Releases.FPML.releases ();
		while (cursor.hasMoreElements ()) {
			Release target = cursor.nextElement ();
			if (target.getVersion ().equals (targetVersionNumber)) {
				if (view.equals (extractView (((SchemaRelease) target).getNamespaceUri ())))
					return (target);
			}
		}
		
		// Otherwise no possible release
		return (null);
	}
	
	public static boolean isConfirmationView (Release release)
	{
		if (release.getVersion ().startsWith ("5-"))
			return (extractView (((SchemaRelease) release).getNamespaceUri ()).equals ("confirmation"));
		else
			return (true);
	}
	
	public static boolean isRecordKeepingView (Release release)
	{
		if (release.getVersion ().startsWith ("5-"))
			return (extractView (((SchemaRelease) release).getNamespaceUri ()).equals ("recordkeeping"));
		else
			return (true);
	}
	
	public static boolean isTransparencyView (Release release)
	{
		if (release.getVersion ().startsWith ("5-"))
			return (extractView (((SchemaRelease) release).getNamespaceUri ()).equals ("transparency"));
		else
			return (true);
	}
	
	public static boolean isReportingView (Release release)
	{
		if (release.getVersion ().startsWith ("5-"))
			return (extractView (((SchemaRelease) release).getNamespaceUri ()).equals ("reporting"));
		else
			return (true);
	}
	
	/**
	 * Ensures that no instances can be constructed.
	 * @since	TFP 1.0
	 */
	private Releases ()
	{ }
	
	/**
	 * Extract the name of the FpML view from the final section of the namespace
	 * URI string.
	 * 
	 * @param	uri				The namespace URI string.
	 * @return	The view name string (e.g. confirmation, reporting, etc.).
	 * @since	TFP 1.7
	 */
	private static String extractView (String uri)
	{
		int index 		= uri.lastIndexOf ('/');
		
		return ((index != -1) ? uri.substring (index + 1) : null);
	}
}