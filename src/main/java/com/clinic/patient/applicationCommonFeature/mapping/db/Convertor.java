package com.clinic.patient.applicationCommonFeature.mapping.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;


@Converter
public class  Convertor<T> implements AttributeConverter<List<T>, String> {

    private  final Class<T> elementType;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Convertor(Class<T> ele){
        elementType= ele;
    }

    @Override
    public String convertToDatabaseColumn(List<T> o) {
        try {
         return   objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) {
                return null;
            }

            return objectMapper.readValue(
                    dbData,
                    new TypeReference<List<T>>() {
                    }
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to List"+ e.getMessage());
        }
    }
}