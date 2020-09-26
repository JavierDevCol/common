package common.util;

public final class UtilFormat {

    private UtilFormat() {
    }

    public static String cammelCase(String nombreSinNormalizar) {
        if (nombreSinNormalizar == null || nombreSinNormalizar.isEmpty()) {
            return nombreSinNormalizar;
        }
        String primeraLetra = String.valueOf(nombreSinNormalizar.charAt(0));
        String subCadena = "";
        if (nombreSinNormalizar.length() > 1) {
            subCadena = nombreSinNormalizar.substring(1);
        }
        return primeraLetra.toUpperCase() + subCadena;
    }

}
