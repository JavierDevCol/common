package common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class UtilStackTrace {
    private UtilStackTrace() {
    }

    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
