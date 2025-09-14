package com.kinga.utils;

import org.springframework.util.StringUtils;

public class KingaUtils {
    private static String SUFFLE_STRING ="tLR4hpeTaQjvGHC0S2zogWPkyq5d3cuMKXlm7FDfiI-BAEJ_Uns/6ZO9YVb1wxrN8@&";
    private static String NORMAL_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/-_\\=";
    public static String decodeText(String text) {
        char[] toChange = text.toCharArray();
        char[] changes  = new char[toChange.length];
        int i = 0;
        for(char chr:toChange){
            changes[i] =  SUFFLE_STRING.indexOf(chr)==-1? chr : NORMAL_STRING.charAt(SUFFLE_STRING.indexOf(chr));
            i++;
        }
        return new String(changes);
    }
    public static String encodeText(String text) {
        if (StringUtils.isEmpty(text))
            return "";
        char[] toChange = text.toCharArray();
        char[] changes  = new char[toChange.length];
        int i = 0;
        for(char chr:toChange){
            changes[i] = NORMAL_STRING.indexOf(chr) ==-1? chr : SUFFLE_STRING.charAt(NORMAL_STRING.indexOf(chr));
            i++;
        }
        return new String(changes);
    }
}
