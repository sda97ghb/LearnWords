package com.divanoapps.learnwords.auxiliary;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by dmitry on 12.05.18.
 */

public class Lazy<T> {
    private volatile T instance;

    private Initializer<T> initializer;

    public Lazy(Class<T> klass) {
        this.initializer = () -> klass.getDeclaredConstructor().newInstance();
    }

    public Lazy(Class<T> klass, Object... args) {
        this.initializer = () -> {
            Class[] argClasses = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argClasses[i] = args[i].getClass();
            }
            return klass.getDeclaredConstructor(argClasses).newInstance(args);
        };
    }

    public Lazy(Initializer<T> initializer) {
        this.initializer = initializer;
    }

    public T get() {
        T localRef = instance;
        if (localRef == null) {
            synchronized (this) {
                localRef = instance;
                if (localRef == null) {
                    try {
                        instance = localRef = initializer.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return localRef;
    }

    public T getPedantic() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        T localRef = instance;
        if (localRef == null) {
            synchronized (this) {
                localRef = instance;
                if (localRef == null) {
                    instance = localRef = initializer.newInstance();
                }
            }
        }
        return localRef;
    }

    public interface Initializer<T> {
        T newInstance() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
    }
}
