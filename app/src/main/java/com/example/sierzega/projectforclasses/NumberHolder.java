package com.example.sierzega.projectforclasses;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Sierżęga on 24.05.2018.
 */

public class NumberHolder {

    private static NumberHolder INSTANCE;

    private NumberHolder() {
    }

    public static NumberHolder getInstance() {
        if (INSTANCE == null)
            synchronized (NumberHolder.class) {
                if (INSTANCE == null)
                    INSTANCE = new NumberHolder();
            }
        return INSTANCE;
    }

    public static void init(Context context) {
        INSTANCE = new NumberHolder();
    }

    private List<Integer> listOfIntegers = new ArrayList<Integer>(30);

    public List<Integer> getListOfIntegers() {
        return listOfIntegers;
    }

    public void clear() {
        listOfIntegers.clear();
    }

    public void fillListWithData(List<Integer> passedList) {
        listOfIntegers.clear();
        for (int i : passedList) {
            listOfIntegers.add(i);
        }
    }
}