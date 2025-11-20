package com.itsutra.project.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SecurityContextUtil {

    private static final List<String> USERS = Arrays.asList(
            "alice",
            "bob",
            "charlie",
            "dave",
            "eve"
    );

    private static final Random RANDOM = new Random();

    public static String getCurrentUsername() {
        // Pick a random index from 0 to USERS.size() - 1
        int randomIndex = RANDOM.nextInt(USERS.size());
        return USERS.get(randomIndex);
    }

//    public String getCurrentUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null ? authentication.getName() : null;
//        return
//    }
}
