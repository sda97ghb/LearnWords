package com.divanoapps.learnwords.data.wombat;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Registry of Entity to Id converters.
 */
public class EntityToIdConverterRegistry {
    private Map<Class, EntityToIdConverter> converters;

    /**
     * Add new converter to the registry.
     * @param converter NonNull. Converter.
     */
    public void addConverter(@NonNull EntityToIdConverter converter) {
        converters.put(converter.getEntityClass(), converter);
    }

    /**
     * Returns converter for specified entity class or null if there is not such converter.
     * @param klass Entity class for requested converter.
     * @return Converter for specified entity class or null if there is not such converter.
     */
    public EntityToIdConverter getConverter(Class klass) {
        return converters.get(klass);
    }
}
