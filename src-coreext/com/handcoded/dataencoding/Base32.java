// Copyright (C),2006-2012 HandCoded Software Ltd.
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

package com.handcoded.dataencoding;

/**
 * The <CODE>Base32</CODE> class provides a utility function for converting a
 * <CODE>byte</CODE> array into a printable string.
 * 
 * @author	BitWise
 * @version	$Id: Base32.java 23 2012-08-20 18:24:33Z andrew $
 * @since	TFP 1.6
 */
public final class Base32
{
	/**
	 * Encodes an array of bytes as a BASE32 string using the lexicon defined
	 * in RFC 4648.
	 * 
	 * @param 	bytes		The array of bytes to be encoded.
	 * @return	The encode data as a <CODE>String</CODE>.
	 */
    public static String encode (byte [] bytes)
    {
        int             bits    = 0;
        int             count   = 0;
        int             index   = 0;
        StringBuilder   buffer  = new StringBuilder ();

        // Encode bits until the byte array is exhausted
        while (index < bytes.length) {
            if (count < 5) {
                bits = (bits << 8) | bytes [index++];
                count += 8;
            }
            buffer.append (LEXICON.charAt ((bits >> (count - 5)) & 0x1f));
            count -= 5;
        }

        // Encode any remaining buffered bits
        while (count >= 5) {
            buffer.append (LEXICON.charAt ((bits >> (count - 5)) & 0x1f));
            count -= 5;
        }
        if (count > 0) {
            buffer.append (LEXICON.charAt ((bits << (5 - count)) & 0x1f));
            count -= 5;
        }

        // And add padding to end of quantum
        while (count != 0) {
            if (count < 5) count += 8;
            buffer.append ('=');
            count -= 5;
        }

        return (buffer.toString ());
    }

    /**
     * The lexicon of characters allowed in the encoded string.
     * @since	TFP 1.6
     */
    private static final String LEXICON
        = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    /**
     * Prevents any instances from being constructed.
   	 * @since	TFP 1.6
   	 */
    private Base32 ()
    { }
}