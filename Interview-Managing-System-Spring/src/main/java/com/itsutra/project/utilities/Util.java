package com.itsutra.project.utilities;

import jakarta.servlet.http.HttpServletRequest;

import java.security.SecureRandom;


public class Util {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generate5DigitRandomString() {
        int randomNum = 10000 + SECURE_RANDOM.nextInt(90000);
        return String.valueOf(randomNum);
    }

    public static  String getClientIpAddress(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }


}
