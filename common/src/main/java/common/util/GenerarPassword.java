package common.util;

import java.util.UUID;

public final class GenerarPassword {

    private GenerarPassword() {
    }

    public static String generarPassword() {
        return UUID.randomUUID().toString();
    }
}
