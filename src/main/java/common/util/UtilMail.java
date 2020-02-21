package common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UtilMail {
    private UtilMail() {
    }

    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", 2);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
