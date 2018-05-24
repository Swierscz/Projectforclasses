package com.example.sierzega.projectforclasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Sierżęga on 24.05.2018.
 */

public class ListConversionUtils {

    public static List<String> convertIntegersToStringsInList(List<Integer> listOfIntegers) {
        List<String> listOfString = new ArrayList<>(30);
        for(int i : listOfIntegers){
            listOfString.add("" + i);
        }
        return listOfString;
    }

}
