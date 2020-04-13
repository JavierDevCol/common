package common.util;

import common.annotation.Atributo;
import common.annotation.Tabla;
import common.annotation.ToMap;
import common.types.Entity;
import common.types.IdObject;
import common.types.MapEmbebido;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static common.util.UtilFormat.cammelCase;

public final class UtilJavaReflection {

    private static final String PREFIJO_METODO = "get";
    private static final String PREFIJO_METODO_SET = "set";

    private UtilJavaReflection() {
    }

    public static Object getValueField(Object objeto, Field attribute) {
        return getValueField(objeto, objeto.getClass(), attribute);
    }

    public static Object getValueField(Object objeto, Class clase, Field attribute) {
        if (objeto == null || clase == null || attribute == null) {
            return null;
        }
        Object valor = null;
        try {
            String nombreAttributeSinNomalizar = attribute.getName();
            String nombreNormalizado = cammelCase(nombreAttributeSinNomalizar);
            String nombreMetodo = PREFIJO_METODO + nombreNormalizado;
            Method method = clase.getMethod(nombreMetodo);
            valor = method.invoke(objeto);
        }
        catch (IllegalAccessException e) {
            //e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            //e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            //e.printStackTrace();
        }
        return valor;
    }

    public static void setValueField(Object objeto, String nombreAttributeSinNomalizar, Object value, Class claseResultante) {
        setValueField(objeto, objeto.getClass(), nombreAttributeSinNomalizar, value, claseResultante);
    }

    public static void setValueField(Object objeto, String nombreAttributeSinNomalizar, Object value) {
        setValueField(objeto, objeto.getClass(), nombreAttributeSinNomalizar, value);
    }

    public static void setValueField(Object objeto, Class clase, String nombreAttributeSinNomalizar, Object value, Class claseParameter) {
        if (objeto == null || clase == null || nombreAttributeSinNomalizar == null) {
            return;
        }
        try {
            String nombreNormalizado = cammelCase(nombreAttributeSinNomalizar);
            String nombreMetodo = PREFIJO_METODO_SET + nombreNormalizado;
            Method method = clase.getMethod(nombreMetodo, claseParameter);
            method.invoke(objeto, value);
        }
        catch (IllegalAccessException e) {
            //e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            //e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            //e.printStackTrace();
        }
    }

    public static void setValueField(Object objeto, Class clase, String nombreAttributeSinNomalizar, Object value) {
        Class claseParameter = value.getClass();
        setValueField(objeto, clase, nombreAttributeSinNomalizar, value, claseParameter);
    }

    public static ArrayList<String> getNamesFieldByClassAndAnnotationAtributo(Object object) {
        return getNamesFieldByClassAndAnnotationAtributo(object.getClass());
    }

    public static String getNameClassByAnnotationTabla(Class clase) {
        if (clase == null) {
            return null;
        }
        if (clase.isAnnotationPresent(Tabla.class)) {
            Tabla tabla = (Tabla) clase.getAnnotation(Tabla.class);
            return tabla.nombre();
        }
        return clase.getSimpleName();
    }

    public static ArrayList<String> getNamesFieldByAnnotationAtributo(Field[] fields) {
        ArrayList<String> var = new ArrayList();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                String nombre = field.getName();
                if (field.isAnnotationPresent(Atributo.class)) {
                    Atributo atributo = (Atributo) field.getAnnotation(Atributo.class);
                    nombre = atributo.nombre();
                }
                var.add(nombre);
            }
        }
        return var;
    }

    public static ArrayList<String> getNamesFieldByClassAndAnnotationAtributo(Class clase) {
        ArrayList<String> var = new ArrayList();
        if (clase != null) {
            Field[] fields = clase.getDeclaredFields();
            for (Field field : fields) {
                String nombre = field.getName();
                if (field.isAnnotationPresent(Atributo.class)) {
                    Atributo atributo = (Atributo) field.getAnnotation(Atributo.class);
                    nombre = atributo.nombre();
                }
                var.add(nombre);
            }
        }
        return var;
    }

    public static List<Field> camposConAnotacion(Object object, Class annotation) {
        List<Field> listResponse = new ArrayList<>();
        if (object != null && annotation != null && !annotation.equals(IdObject.class)) {
            Class<?> instanciaClase = object.getClass();
            List<Field> camposActuales = camposConAnnotationEmbebido(object, instanciaClase, annotation);
            listResponse.addAll(camposActuales);

            List<Field> camposPadre = camposConAnnotationEmbebido(object, instanciaClase.getSuperclass(), annotation);
            listResponse.addAll(camposPadre);
        }
        return listResponse;
    }

    private static List<Field> camposConAnnotationEmbebido(Object object, Class instanciaClase, Class annotation) {
        List<Field> listResponse = new ArrayList<>();
        if (object != null && annotation != null && !annotation.equals(IdObject.class)) {
            for (Field declaredField : instanciaClase.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(annotation)) {
                    listResponse.add(declaredField);
                }
            }
        }
        return listResponse;
    }

    public static void pasarEntityToMaps(Entity source) {
        if (source == null) {
            return;
        }
        List<Field> campos = camposConAnotacion(source, ToMap.class);
        campos.forEach(field -> {
            ToMap anotacion = field.getAnnotation(ToMap.class);
            for (String nombreAttributo : anotacion.nombreAttributo()) {
                Object objectoRecibido = getValueField(source, field);
                if (objectoRecibido.getClass().equals(ArrayList.class)) {
                    Collection lista = (Collection) objectoRecibido;
                    Collection<MapEmbebido> listaResponse = new ArrayList();
                    lista.forEach(o -> {
                        Entity entity = (Entity) o;
                        Map<String, String> mapaCreate = entityToMap(entity);
                        MapEmbebido mapEmbebido = new MapEmbebido();
                        mapEmbebido.setMapa(mapaCreate);
                        listaResponse.add(mapEmbebido);
                    });
                    setValueField(source, nombreAttributo, listaResponse, List.class);
                }
                else {
                    Entity entity = (Entity) objectoRecibido;
                    Map<String, String> mapaCreate = entityToMap(entity);
                    MapEmbebido mapEmbebido = new MapEmbebido();
                    mapEmbebido.setMapa(mapaCreate);
                    setValueField(source, nombreAttributo, mapEmbebido, MapEmbebido.class);
                }
            }
        });
    }

    public static Map<String, String> entityToMap(Entity entity) {
        Class<?> instanciaClase = entity.getClass();
        Map<String, String> mapaResponse = new HashMap<>();
        for (Field declaredField : instanciaClase.getDeclaredFields()) {
            String nombreVariable = declaredField.getName();
            Object valorObject = getValueField(entity, declaredField);

            mapaResponse.put(nombreVariable, String.valueOf(valorObject));
        }
        Map<String, String> mapaPadre = entityToMap(instanciaClase.getSuperclass(), entity);
        mapaResponse.putAll(mapaPadre);
        return mapaResponse;
    }


    public static Map<String, String> entityToMap(Class clasePadre, Object objetoBase) {
        Map<String, String> mapaResponse = new HashMap<>();
        if (!clasePadre.equals(Object.class)) {
            for (Field declaredField : clasePadre.getDeclaredFields()) {
                String nombreVariable = declaredField.getName();
                Object valorObject = getValueField(objetoBase, declaredField);

                mapaResponse.put(nombreVariable, String.valueOf(valorObject));
            }
        }
        return mapaResponse;
    }
}
