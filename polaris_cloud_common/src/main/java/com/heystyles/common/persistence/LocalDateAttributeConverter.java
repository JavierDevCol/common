package com.heystyles.common.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(
        autoApply = true
)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
    public LocalDateAttributeConverter() {
    }

    public Date convertToDatabaseColumn(LocalDate locDate) {
        return locDate == null ? null : Date.valueOf(locDate);
    }

    public LocalDate convertToEntityAttribute(Date sqlDate) {
        return sqlDate == null ? null : sqlDate.toLocalDate();
    }
}
