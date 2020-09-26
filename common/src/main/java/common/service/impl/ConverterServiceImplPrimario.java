package common.service.impl;

import com.google.common.collect.Lists;
import common.service.ConverterService;
import common.types.AuditableWithAuthorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class ConverterServiceImplPrimario implements ConverterService {

    @Autowired(required = false)
    private Set<Converter<?, ?>> converters;

    private TreeMap<String, TreeMap<Class<?>, TreeMap<Class<?>, Converter<?, ?>>>> mapaConverters = new TreeMap<>();

    public ConverterServiceImplPrimario() {
    }

    public <S, T> T convertTo(S source, Class<T> targetClass) {
        return this.convert(source, targetClass);
    }

    public <S, T> List<T> convertTo(List<S> source, Class<T> targetClass) {
        List<T> result = null;
        if (source != null) {
            result = Lists.newArrayList();
            Iterator var4 = source.iterator();

            while (var4.hasNext()) {
                S s = (S) var4.next();
                result.add(this.convert(s, targetClass));
            }
        }

        return result;
    }

    private <S, T> T convert(S source, Class<T> targetClass) {
        TreeMap<Class<?>, TreeMap<Class<?>, Converter<?, ?>>> mapaPackage = mapaConverters.get(targetClass.getPackage());
        TreeMap<Class<?>, Converter<?, ?>> mapaConvert = mapaPackage.get(targetClass);
        Converter<S, T> converter = (Converter<S, T>) mapaConvert.get(source.getClass());
        return converter.convert(source);
    }

    public <S, T> boolean canConvertTo(Class<S> sourceClass, Class<T> targetClass) {
        TreeMap<Class<?>, TreeMap<Class<?>, Converter<?, ?>>> mapaPackage = mapaConverters.get(targetClass.getPackage());
        if (mapaPackage != null) {
            TreeMap<Class<?>, Converter<?, ?>> mapaConvert = mapaPackage.get(targetClass);
            if (mapaConvert != null) {
                Converter<S, T> converter = (Converter<S, T>) mapaConvert.get(sourceClass.getClass());
                if (converter != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        if (this.converters != null) {
            Iterator var1 = this.converters.iterator();

            while (var1.hasNext()) {
                Converter<?, ?> converter = (Converter<?, ?>) var1.next();
                Class clase = converter.getClass();
                List<Class<?>> resultantes = findGenericArgumentLocation(clase, Converter.class);
                Class source = resultantes.get(0);
                Class target = resultantes.get(1);

                List<Class<?>> targets = new ArrayList<>();
                targets.add(target);

                findFathers(target, AuditableWithAuthorEntity.class, targets);

                targets.forEach(aClass -> {
                    String nombrePackage = aClass.getPackage().getName();
                    TreeMap<Class<?>, TreeMap<Class<?>, Converter<?, ?>>> mapaPackage = mapaConverters
                            .computeIfAbsent(nombrePackage, a -> new TreeMap<Class<?>, TreeMap<Class<?>, Converter<?, ?>>>());

                    TreeMap<Class<?>, Converter<?, ?>> mapaClase = mapaPackage
                            .computeIfAbsent(aClass, a -> new TreeMap<Class<?>, Converter<?, ?>>());
                    mapaClase.put(source, converter);
                });
            }
        }

    }

    private void findFathers(Class clase, Class comparatorFinal, List<Class<?>> response) {
        Type type = clase.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Class<?> claseFather = (Class<?>) ((ParameterizedType) type).getRawType();
            if (claseFather.equals(comparatorFinal)) {
                return;
            }
            response.add(claseFather);
            findFathers(claseFather, comparatorFinal, response);
        }
    }

    private List<Class<?>> findGenericArgumentLocation(Class<?> clase, Class<?> claseBuscada) {
        if (clase.getGenericInterfaces().length > 0) {
            for (Type genericInterface : clase.getGenericInterfaces()) {
                if (genericInterface instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                    if (parameterizedType.getRawType().equals(claseBuscada)) {
                        List<Class<?>> listResponse = new ArrayList<>();
                        for (Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {
                            listResponse.add((Class<?>) actualTypeArgument);
                        }
                        return listResponse;
                    }
                }
            }
        }
        Type type = clase.getGenericSuperclass();
        if (type instanceof Object) {
            return new ArrayList<>();
        }
        return findGenericArgumentLocation((Class) type, claseBuscada);
    }

    public void setConverters(Set<Converter<?, ?>> converters) {
        this.converters = converters;
    }

}
