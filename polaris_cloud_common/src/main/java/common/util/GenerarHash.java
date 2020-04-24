package common.util;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class GenerarHash {

    private GenerarHash() {
    }

    public static String generar(List<String> data, TipoHash tipoHash) {
        String password = data.stream().collect(Collectors.joining());
        return generar(password.getBytes(), tipoHash);
    }

    public static String generar(byte[] bytes, TipoHash tipoHash) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(tipoHash.getCodigo());
            md.update(bytes);
            byte[] mb = md.digest();
            return String.copyValueOf(Hex.encodeHex(mb));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
