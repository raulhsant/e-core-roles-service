package com.ecore.rolesservice;

import org.jeasy.random.EasyRandom;

import java.util.ArrayList;
import java.util.List;

public class TestDataGen {
    private static final EasyRandom random = new EasyRandom();
    private static final int MAX_LIST_SIZE = 100;

    public static String getString() {
        return getObject(String.class);
    }

    public static <T> T getObject(Class<T> type) {
        return random.nextObject(type);
    }

    public static <T> List<T> getListOfObject(Class<T> type) {
        return getListOfObject(type, random.nextInt(MAX_LIST_SIZE));
    }

    public static <T> List<T> getListOfObject(Class<T> type, int size) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(getObject(type));
        }

        return list;
    }
}
