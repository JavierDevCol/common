package common.util;


import java.lang.reflect.Field;
import java.util.Objects;

public class UtilOfuscacion {

    public UtilOfuscacion() {
    }

    public static boolean validarOfuscacion(Object o) {
        Class clase = o.getClass();
        Field attributes[] = clase.getFields();
        for (Field attribute : attributes) {
            Object valor = UtilJavaReflection.getValueField(o, attribute);
            if (valor.toString().contains("*")) {
                return true;
            }
        }
        return false;
    }

    public static Object mergeDataOfuscada(Object oReq, Object oBd) {
        if (Objects.isNull(oBd)) {
            return oReq;
        }

        Class claseReq = oReq.getClass();
        Class claseBd = oBd.getClass();

        Field attributtesReq[] = claseReq.getFields();
        Field attributtesBd[] = claseBd.getFields();

        for (int i = 0; i < attributtesReq.length; i++) {
            Object valorReq = UtilJavaReflection.getValueField(oReq, attributtesReq[i]);
            Object valorBd = UtilJavaReflection.getValueField(oBd, attributtesBd[i]);

            if (Objects.nonNull(valorReq) && Objects.nonNull(valorBd) && !Objects.equals(valorReq, valorBd)) {
                if (valorReq.toString().contains("*")) {
                    UtilJavaReflection.setValueField(oReq, attributtesReq[i].getName(), valorBd);
                }
            }
        }
        return oReq;
    }
}
