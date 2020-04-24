package common.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import common.exception.InvalidEnumValueException;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TipoHash {

    SHA_512("SHA-512"), SHA_1("SHA-1"), MD_5("MD5");

    private String codigo;

    TipoHash(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static TipoHash fromValue(String value) {
        if (value != null && value.isEmpty()) {
            return null;
        }
        for (TipoHash p : values()) {
            if (p.name().equals(value) || p.name().toLowerCase().equals(value)) {
                return p;
            }
        }
        throw new InvalidEnumValueException("TipoHash", value);
    }
}
