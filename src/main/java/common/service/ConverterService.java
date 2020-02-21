package common.service;

import java.util.List;

public interface ConverterService {
    <S, T> T convertTo(S var1, Class<T> var2);

    <S, T> List<T> convertTo(List<S> var1, Class<T> var2);

    <S, T> boolean canConvertTo(Class<S> var1, Class<T> var2);
}

