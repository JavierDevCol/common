package com.heystyles.common.util;

import org.apache.commons.codec.binary.Base64;

public final class UtilBase64 {
    private UtilBase64() {
    }

    public static String encode(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    public static byte[] decode(String data) {
        return Base64.decodeBase64(data);
    }
}
