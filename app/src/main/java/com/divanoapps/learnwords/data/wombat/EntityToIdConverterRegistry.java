package com.divanoapps.learnwords.data.wombat;

import java.util.Map;

/**
 * Created by dmitry on 14.05.18.
 */

public class EntityToIdConverterRegistry {
    private Map<Class, EntityToIdConverter> converters;

    public void addConverter(EntityToIdConverter converter) {
        converters.put(converter.getEntityClass(), converter);
    }

    public EntityToIdConverter getConverter(Class klass) {
        return converters.get(klass);
    }
}
