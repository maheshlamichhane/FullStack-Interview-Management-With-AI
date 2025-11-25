package com.itsutra.project.utilities;

import java.security.SecureRandom;


public class Util {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generate5DigitRandomString() {
        int randomNum = 10000 + SECURE_RANDOM.nextInt(90000);
        return String.valueOf(randomNum);
    }


}
