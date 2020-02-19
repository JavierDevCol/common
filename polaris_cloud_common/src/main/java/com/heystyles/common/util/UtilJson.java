package com.heystyles.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;

public final class UtilJson {
    private UtilJson() {
    }

    public static <T> T toObject(String json, Class<T> type) {
        ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.findAndRegisterModules();

        try {
            return mapperObj.readValue(json, type);
        }
        catch (Exception var4) {
            return null;
        }
    }

    public static String toString(Object datos) {
        ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.findAndRegisterModules();

        try {
            return mapperObj.writeValueAsString(datos);
        }
        catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }
}
