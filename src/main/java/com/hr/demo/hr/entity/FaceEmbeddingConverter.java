package com.hr.demo.hr.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

/** PostgreSQL vector text is not a PostgreSQL float8[] JDBC array. */
@Converter
public class FaceEmbeddingConverter implements AttributeConverter<double[], String> {
    @Override public String convertToDatabaseColumn(double[] value) {
        if (value == null) return null;
        if (value.length != 512 || Arrays.stream(value).anyMatch(v -> !Double.isFinite(v))) {
            throw new IllegalArgumentException("Expected 512 finite face embedding values");
        }
        return Arrays.toString(value);
    }

    @Override public double[] convertToEntityAttribute(String value) {
        if (value == null) return null;
        String text = value.trim();
        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Invalid stored face embedding");
        }
        double[] result = Arrays.stream(text.substring(1, text.length()-1).split(","))
                .map(String::trim).mapToDouble(Double::parseDouble).toArray();
        if (result.length != 512 || Arrays.stream(result).anyMatch(v -> !Double.isFinite(v))) {
            throw new IllegalArgumentException("Invalid stored face embedding");
        }
        return result;
    }
}
