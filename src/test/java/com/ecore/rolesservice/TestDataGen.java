package com.ecore.rolesservice;

import org.jeasy.random.EasyRandom;

public class TestDataGen {
    private static final EasyRandom random = new EasyRandom();

    public static String getString() {
        return getObject(String.class);
    }

    public static <T> T getObject(Class<T> type) {
        return random.nextObject(type);
    }
}
