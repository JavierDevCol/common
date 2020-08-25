package common.util;


import com.polaris.cloud.client.OfuscacionClient;
import com.polaris.cloud.core.dto.OfuscarObjetoListaRequestDto;
import com.polaris.cloud.core.dto.OfuscarObjetoRequestDto;
import common.http.util.HttpRequestContextHolder;
import org.springframework.data.cassandra.core.mapping.Table;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class UtilOfuscacion {


    UtilOfuscacion() {
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

        Field attributtesReq[] = claseReq.getSuperclass().getDeclaredFields();
        Field attributtesBd[] = claseBd.getSuperclass().getDeclaredFields();

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

    public static <T> T ofuscacion(T response, Class<T> type, OfuscacionClient ofuscacionClient) {
        if (Objects.nonNull(HttpRequestContextHolder.getUsuario())) {
            OfuscarObjetoRequestDto ofuscarObjetoRequestDto = new OfuscarObjetoRequestDto();
            ofuscarObjetoRequestDto.setObjeto(response);
            ofuscarObjetoRequestDto.setNombreTabla(type.getAnnotation(Table.class).value());
            Object objetoOfuscado = ofuscacionClient.ofuscarObjeto(HttpRequestContextHolder.getUsuario(), ofuscarObjetoRequestDto);
            response = UtilObject.objectToClass(objetoOfuscado, type);
        }
        return response;
    }

    public static <T> List<T> ofuscacion(List<T> listResponse, Class<T> type, OfuscacionClient ofuscacionClient) {
        if (Objects.nonNull(HttpRequestContextHolder.getUsuario())) {
            OfuscarObjetoListaRequestDto ofuscarObjetoListaRequestDto = new OfuscarObjetoListaRequestDto();
            ofuscarObjetoListaRequestDto.setListaObjeto(Collections.singletonList(listResponse));
            ofuscarObjetoListaRequestDto.setNombreTabla(type.getAnnotation(Table.class).value());
            List<Object> listObjetoOfuscado = ofuscacionClient.ofuscarObjetoLista(HttpRequestContextHolder.getUsuario(),
                    ofuscarObjetoListaRequestDto);
            listResponse = UtilObject.objectListToClassList(listObjetoOfuscado, type);
        }
        return listResponse;
    }
}
