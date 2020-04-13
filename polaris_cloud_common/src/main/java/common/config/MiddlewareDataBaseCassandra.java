package common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.annotation.ToEntity;
import common.types.Entity;
import common.types.MapEmbebido;
import common.util.UtilJson;
import org.springframework.data.cassandra.core.mapping.event.AbstractCassandraEventListener;
import org.springframework.data.cassandra.core.mapping.event.AfterConvertEvent;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static common.util.UtilJavaReflection.camposConAnotacion;
import static common.util.UtilJavaReflection.getValueField;
import static common.util.UtilJavaReflection.setValueField;

public class MiddlewareDataBaseCassandra extends AbstractCassandraEventListener<Entity> {

    @Override
    public void onAfterConvert(AfterConvertEvent<Entity> event) {
        List<Field> campos = camposConAnotacion(event.getSource(), ToEntity.class);
        campos.forEach(field -> {
            ToEntity anotacion = field.getAnnotation(ToEntity.class);
            for (String nombreAttributo : anotacion.nombreAttributo()) {
                Object valorField = getValueField(event.getSource(), field);
                if (field.getType().equals(List.class)) {
                    Collection<MapEmbebido> lista = (Collection) valorField;
                    if (lista != null) {
                        List listResponse = lista
                                .stream()
                                .map(mapa -> toEntity(mapa.getMapa(), anotacion.claseConvertir()))
                                .collect(Collectors.toList());
                        setValueField(event.getSource(), nombreAttributo, listResponse, List.class);
                    }
                }
                else {
                    MapEmbebido mapEmbebido = (MapEmbebido) valorField;
                    if (mapEmbebido != null) {
                        Map<String, String> mapa = mapEmbebido.getMapa();
                        Object value = toEntity(mapa, anotacion.claseConvertir());
                        setValueField(event.getSource(), nombreAttributo, value);
                    }
                }
            }
        });
        super.onAfterConvert(event);
    }

    private Object toEntity(Map<String, String> map, Class<?> clase) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> mapaResponse = new HashMap<>();
        map.forEach((nombreAttributo, valorAttributo) -> {
            Object valor = null;
            if (valorAttributo != null && !valorAttributo.equals("null")) {
                valor = valorAttributo;
            }
            mapaResponse.put(nombreAttributo, valor);
        });
        String json = null;
        try {
            json = objectMapper.writeValueAsString(mapaResponse);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return UtilJson.toObject(json, clase);
    }

}
