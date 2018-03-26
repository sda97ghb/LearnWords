package com.divanoapps.learnwords.data.api2;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceExecutor {
    private static void checkClass(Class clazz) {
        if (!clazz.isInterface())
            throw new IllegalArgumentException("API declarations must be interfaces.");

        if (clazz.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }

        for (Method classMethod : clazz.getMethods()) {
            ApiRequest apiRequest = classMethod.getAnnotation(ApiRequest.class);
            if (apiRequest == null)
                continue;

            for (Annotation[] parameterAnnotations : classMethod.getParameterAnnotations()) {
                boolean f = true;
                for (Annotation annotation : parameterAnnotations) {
                    if (annotation.getClass() == ApiParameter.class) {
                        f = false;
                        break;
                    }
                }
                if (f)
                    throw new IllegalArgumentException(classMethod.getName() +
                        " has a parameter not annotated with " + ApiParameter.class.getName() + ".");
            }
        }
    }

    private static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(ApiRequestService.getApiUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    private static final ApiRequestService apiRequestService = retrofit.create(ApiRequestService.class);

    private static final Gson gson = new Gson();

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz) {
        checkClass(clazz);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
                // If the method is a method from Object then defer to normal invocation.
                if (method.getDeclaringClass() == Object.class)
                    return method.invoke(this, args);

                Map<String, Object> t = new HashMap<>();

                ApiRequest apiRequestAnnotation = method.getAnnotation(ApiRequest.class);
                t.put("entity", apiRequestAnnotation.entity());
                t.put("method", apiRequestAnnotation.method());

                if (args != null) {
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                    for (int i = 0; i < parameterAnnotations.length; ++i) {
                        String name = null;
                        for (Annotation annotation : parameterAnnotations[i])
                            if (annotation.getClass() == ApiParameter.class)
                                name = ((ApiParameter) annotation).value();
                        if (name != null)
                            t.put(name, args[i]);
                    }
                }

                final String request = gson.toJson(t);

                return Single.fromCallable(() -> {
                    Response<ApiResponse> response = apiRequestService.request(request).execute();
                    if (!response.isSuccessful())
                        throw new Exception(response.code() + response.message());

                    ApiResponse apiResponse = response.body();

                    if (apiResponse == null)
                        throw new Exception("Fuck at line 98!");

                    if (apiResponse.getError() != null)
                        throw new Exception(apiResponse.getError());

                    return gson.fromJson(apiResponse.getResponse().toString(), method.getReturnType());
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
            }
        });
    }
}
