package com.khachungbg97gmail.carservice.VinDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 10/14/2018.
 */

public class VinConstants {
    public static Map<String,Integer> YEAR_INDEX=new HashMap<>();
    public static String YEAR_CHARS[] = {
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R"
            , "S", "T", "V", "W", "X", "Y", "1", "2", "3", "4", "5", "6", "7"
            , "8", "9", "0"};
    public static final Map<String, Integer> ALPHABET_INDEX = new HashMap<>();
    public static final String ALPHABET_CHARS[] = {
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R"
            , "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7"
            , "8", "9", "0"};
    public static  final Map<String,Integer> WEIGHTS=new HashMap<>();
    public static final int ALPHABET_WEIGHTS[]={
            1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 7, 9,
            2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 0};
    public static int WEIGHT_FACTOR[] =
            {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};

    static {
        int len=VinConstants.ALPHABET_CHARS.length;
        for (int i = 0; i < len; i++) {
            ALPHABET_INDEX.put(VinConstants.ALPHABET_CHARS[i], i);
        }
        for (int i = 0; i < len; i++) {
            WEIGHTS.put(VinConstants.ALPHABET_CHARS[i], ALPHABET_WEIGHTS[i]);
        }
        for(int i=0;i<VinConstants.YEAR_CHARS.length;i++){
            YEAR_INDEX.put(VinConstants.YEAR_CHARS[i],i);
        }


    }

}
