package common.http.util;

public final class HttpRequestContextHolder {

    private HttpRequestContextHolder() {
        super();
    }

    private static final ThreadLocal<String> USUARIO_HOLDER = new ThreadLocal<>();

    public static void setUsuario(String usuario) {
        USUARIO_HOLDER.set(usuario);
    }

    public static String getUsuario() {
        return USUARIO_HOLDER.get();
    }

    public static void clearUsuario() {
        USUARIO_HOLDER.remove();
    }


    private static final ThreadLocal<String> CLIENTE_HOLDER = new ThreadLocal<>();

    public static void setCliente(String cliente) {
        CLIENTE_HOLDER.set(cliente);
    }

    public static String getCliente() {
        return CLIENTE_HOLDER.get();
    }

    public static void clearCliente() {
        CLIENTE_HOLDER.remove();
    }
}
