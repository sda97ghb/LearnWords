package com.divanoapps.learnwords.data.dogfish;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ServiceExecutor<T> {
    private RequestExecutor requestExecutor;
    private Gson gson;
    private T service;
    private String idToken;

    public ServiceExecutor(RequestExecutor requestExecutor, Gson gson, Class<T> clazz) {
        this.requestExecutor = requestExecutor;
        this.gson = gson;
        this.idToken = null;
        this.service = create(clazz);
    }

    public ServiceExecutor(RequestExecutor requestExecutor, Gson gson, Class<T> clazz, String idToken) {
        this.requestExecutor = requestExecutor;
        this.gson = gson;
        this.service = create(clazz);
        this.idToken = idToken;
    }

    public T getService() {
        return service;
    }

    @SuppressWarnings("unchecked")
    private T create(Class<T> clazz) {
        Aux.checkClass(clazz);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
                // If the method is a method from Object then defer to normal invocation.
                if (method.getDeclaringClass() == Object.class)
                    return method.invoke(this, args);

                Map<String, Object> request = new HashMap<>();

                ApiRequest apiRequestAnnotation = method.getAnnotation(ApiRequest.class);
                request.put("entity", apiRequestAnnotation.entity());
                request.put("method", apiRequestAnnotation.method());

                if (method.isAnnotationPresent(ApiRequireAuthorization.class))
                    request.put("idToken", idToken);

                // add method parameters to request
                if (args != null) {
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                    for (int i = 0; i < parameterAnnotations.length; ++i) {
                        String name = null;
                        for (Annotation annotation : parameterAnnotations[i])
                            if (annotation instanceof ApiParameter)
                                name = ((ApiParameter) annotation).value();
                        if (name != null)
                            request.put(name, args[i]);
                    }
                }

                ApiResponse apiResponse = requestExecutor.request(request);

                if (apiResponse == null)
                    throw new Exception("Fuck! See ServiceExecutor at line 132.");

                if (apiResponse.getError() != null)
                    throw new Exception(apiResponse.getError());

                Class methodReturnClass = method.getReturnType();
                if (methodReturnClass.equals(void.class))
                    return null;
                else
                    return gson.fromJson(gson.toJson(apiResponse.getResponse()), methodReturnClass);
            }
        });
    }

    private static class Aux {
        static void checkClass(Class clazz) {
            if (!clazz.isInterface())
                throw new IllegalArgumentException("API declarations must be interfaces.");

            if (clazz.getInterfaces().length > 0) {
                throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
            }

            for (Method classMethod : clazz.getMethods()) {
                ApiRequest apiRequest = classMethod.getAnnotation(ApiRequest.class);
                if (apiRequest == null)
                    continue;

                int parameterNumber = 0;
                for (Annotation[] parameterAnnotations : classMethod.getParameterAnnotations()) {
                    boolean f = true;
                    for (Annotation annotation : parameterAnnotations) {
                        if (annotation instanceof ApiParameter) {
                            f = false;
                            break;
                        }
                    }
                    if (f)
                        throw new IllegalArgumentException(classMethod.getName() +
                            " has a parameter with number " + parameterNumber +
                            " not annotated with " + ApiParameter.class.getName() + ".");
                    ++parameterNumber;
                }
            }
        }
    }
}
