package common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class UtilObject {
    private UtilObject() {
    }

    private static DecimalFormat getDecimalFormat() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Constants.LOCALE);
        return format;
    }

    public static String getValueString(Object value) {
        String strValue = null;
        if (value != null && !"".equals(value)) {
            strValue = value.toString();
        }

        return strValue;
    }

    public static Integer getValueInteger(Object value) {
        Integer intValue = null;
        if (value != null && !"".equals(value)) {
            try {
                intValue = Integer.valueOf(value.toString());
            }
            catch (NumberFormatException var3) {
                System.err.println("Formato de integer no apropiado " + value);
            }
        }

        return intValue;
    }

    public static Long getValueLong(Object value) {
        Long longValue = null;
        if (value != null && !"".equals(value)) {
            try {
                longValue = Long.valueOf(value.toString());
            }
            catch (NumberFormatException var3) {
                System.err.println("Formato de Long no apropiado " + value);
            }
        }

        return longValue;
    }

    public static Double getValueDouble(Object value) {
        Double dblValue = null;
        if (value != null && !"".equals(value)) {
            try {
                dblValue = stringToDouble(value.toString().trim());
            }
            catch (ParseException var3) {
                System.err.println("Formato de double no apropiado " + value);
            }
        }

        return dblValue;
    }

    public static Double getDouble(Object value) {
        Double dblValue = null;
        if (value != null && !"".equals(value)) {
            try {
                dblValue = new Double(value.toString().trim());
            }
            catch (NumberFormatException var3) {
                System.err.println("Formato de double no apropiado " + value);
            }
        }

        return dblValue;
    }

    public static double stringToDouble(String valor) throws ParseException {
        DecimalFormat format = getDecimalFormat();
        format.applyPattern("#,##0.00");
        return format.parse(valor).doubleValue();
    }

    public static Boolean getValueBoolean(Object value, Boolean defaultValue) {
        Boolean bolValue = null;
        if (value != null) {
            if ("true".equalsIgnoreCase(value.toString())) {
                bolValue = Boolean.TRUE;
            }
            else if ("false".equalsIgnoreCase(value.toString())) {
                bolValue = Boolean.FALSE;
            }
        }

        if (bolValue == null) {
            bolValue = defaultValue;
        }

        return bolValue;
    }

    public static <T> T objectToClass(Object object, Class<T> type) {
        String var = UtilJson.toString(object);
        return UtilJson.toObject(var, type);
    }

    public static <T> List<T> objectListToClassList(List<Object> objectList, Class<T> type) {
        List<T> list = new ArrayList<>();
        for (Object object : objectList) {
            list.add(objectToClass(object, type));
        }
        return list;
    }

    public static List<Object> convertObjectToList(Object obj) {
        List<Object> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        }
        else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<Object>) obj);
        }
        return list;
    }

}

