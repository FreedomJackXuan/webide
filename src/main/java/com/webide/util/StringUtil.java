package com.webide.util;

public class StringUtil {
    public static String subStrByStrAndLen(String str, int len) {
        return null != str ? str.substring(0, str.length() > len ? len : str.length()) : null;
    }
}
