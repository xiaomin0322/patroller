package com.preapm.sdk.zipkin.util;

/**
 * 
 * <pre>
 * IdConversion
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:18:42
 */
public class IdConversion {

    public static String convertToString(final long id) {
        return Long.toHexString(id);
    }

    public static Long convertToLong(final String id) {
        if (id == null || id.length() == 0 || id.length() > 16) {
            return null;
        }

        long result = 0;

        for (char c : id.toCharArray()) {
            result <<= 4;

            if (c >= '0' && c <= '9') {
                result |= c - '0';
            } else if (c >= 'a' && c <= 'f') {
                result |= c - 'a' + 10;
            } else {
                throw new NumberFormatException("character " + c + " not lower hex in " + id);
            }
        }

        return result;
    }
}
