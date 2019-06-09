package com.workouttracker.util;

public class StringUtils {

    public static String floatToStringWithoutTrailingZeros(float f){
        return String.valueOf(f).replaceAll("\\.?0*$", "");
    }
}
