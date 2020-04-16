package common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.annotation.ToEntity;
import common.types.Entity;
import common.types.MapEmbebido;
import common.util.Constants;
import org.springframework.data.cassandra.core.mapping.event.AbstractCassandraEventListener;
import org.springframework.data.cassandra.core.mapping.event.AfterConvertEvent;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static common.util.UtilJavaReflection.camposConAnotacion;
import static common.util.UtilJavaReflection.findTypeDataVariablesByClase;
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

    private Object toEntity(Map map, Class<?> clase) {
        Map<String, Object> mapaResponse = new HashMap<>();
        if (map == null || clase == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Class> tipoDatoVariables = findTypeDataVariablesByClase(clase);
        map.forEach((nombreAttributo, valorAttributo) -> {
            Object valor = null;
            if (valorAttributo != null && !valorAttributo.toString().equals("null")) {
                try {
                    Map mapaValorAttributo = objectMapper.readValue(valorAttributo.toString(), HashMap.class);
                    Class tipoDatoVariable = tipoDatoVariables.get(nombreAttributo);
                    if (tipoDatoVariable != null && mapaValorAttributo != null) {
                        valor = toEntity(mapaValorAttributo, tipoDatoVariable);
                    }
                }
                catch (JsonProcessingException e) {
                    List listarValores = null;
                    List listaResponse = new ArrayList();
                    try {
                        listarValores = objectMapper.readValue(valorAttributo.toString(), List.class);
                        Class tipoDatoVariable = tipoDatoVariables.get(nombreAttributo);
                        if (tipoDatoVariable != null && listarValores != null) {
                            listarValores.forEach(o -> listaResponse.add(toEntity((Map) o, tipoDatoVariable)));
                        }
                        valor = listaResponse;
                    }
                    catch (JsonProcessingException ex) {
                        try {
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE);
                            LocalDate fecha = LocalDate.parse(valorAttributo.toString(), dateTimeFormatter);
                            valor = null;
                        }
                        catch (Exception ef) {
                            try {
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_LOCAL_TIME_HM);
                                LocalTime tiempo = LocalTime.parse(valorAttributo.toString(), dateTimeFormatter);
                                valor = null;
                            }
                            catch (Exception efg) {
                                valor = valorAttributo;
                            }
                        }
                    }
                }
            }
            mapaResponse.put(nombreAttributo.toString(), valor);
        });
        return objectMapper.convertValue(mapaResponse, clase);
    }

}
