package com.evandev.redomesticate.util;

import com.evandev.redomesticate.Constants;

public class LangUtil {
    public static String text(String name) {
        return "text." + Constants.MOD_ID + "." + name;
    }

    public static String conf(String name) {
        return Constants.MOD_ID + ".configuration." + name;
    }
}
