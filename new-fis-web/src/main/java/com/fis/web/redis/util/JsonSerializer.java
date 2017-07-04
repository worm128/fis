package com.fis.web.redis.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.web.redis.exception.SerializeException;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class JsonSerializer {

    private final ObjectMapper objectMapper;
    private final JavaType javaType;

    public JsonSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        javaType = objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, Object.class);
    }


    public String serialize(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            log.error("failed to serialize http session {} to json,cause:{}", o, Throwables.getStackTraceAsString(e));
            throw new SerializeException("failed to serialize http session to json", e);
        }
    }

    public Map<String, Object> deserialize(String o) {
        try {
            return objectMapper.readValue(o, javaType);
        } catch (Exception e) {
            log.error("failed to deserialize string  {} to http session,cause:{} ", o, Throwables.getStackTraceAsString(e));
            throw new SerializeException("failed to deserialize string to http session", e);
        }
    }


    public <T> T deserializeForObject(String o, Class<T> aClass) {
        try {
            return objectMapper.readValue(o, aClass);
        } catch (Exception e) {
            log.error("failed to deserializeForObject string  {} to http session,cause:{} ", o, Throwables.getStackTraceAsString(e));
            throw new SerializeException("failed to deserializeForObject string to http session", e);
        }
    }

    public <T> T deserializeForObject(String o, Class<T> aClass, Class<T> elementClasses) {
        try {
            return objectMapper.readValue(o, getCollectionType(objectMapper, aClass, elementClasses));
        } catch (Exception e) {
            log.error("failed to deserializeForObject string  {} to http session,cause:{} ", o, Throwables.getStackTraceAsString(e));
            throw new SerializeException("failed to deserializeForObject string to http session", e);
        }
    }

    public JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
