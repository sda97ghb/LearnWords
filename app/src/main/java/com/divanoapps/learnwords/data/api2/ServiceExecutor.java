package com.divanoapps.learnwords.data.api2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceExecutor<T> {
    static {
//        RxJavaPlugins.setErrorHandler(throwable -> {
//            throwable.printStackTrace();
//        });
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
    }

    private final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(ApiRequestService.getApiUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    private final ApiRequestService apiRequestService = retrofit.create(ApiRequestService.class);

    private final Gson gson = new Gson();

    private String idToken;
    private T service;

    public ServiceExecutor(Class<T> clazz) {
        this.idToken = null;
        this.service = create(clazz);
    }

    public ServiceExecutor(Class<T> clazz, String idToken) {
        this.idToken = idToken;
        this.service = create(clazz);
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

                Class methodReturnClass = method.getReturnType();
                if (methodReturnClass == Completable.class)
                    return makeCompletable(request);
                else if (methodReturnClass == Single.class)
                    return makeSingle(request, Aux.getClassFromGenericType(method.getGenericReturnType().toString()));
                else
                    throw new Exception("Fuck! See ServiceExecutor at line 155.");
            }
        });
    }

    private Completable makeCompletable(Map<String, Object> request) {
        return Completable.create(e -> {
            final CompletableEmitter emitter = e;
            apiRequestService.request(request).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    ApiResponse apiResponse = response.body();

                    if (apiResponse == null) {
                        emitter.onError(new Exception("Fuck! See ServiceExecutor at line 102."));
                        return;
                    }

                    if (apiResponse.getError() != null) {
                        emitter.onError(new Exception(apiResponse.getError()));
                        return;
                    }

                    emitter.onComplete();
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread());
    }

    private <G> Single makeSingle(Map<String, Object> request, Class<G> returnType) {
        return Single.create(e -> {
            final SingleEmitter<Object> emitter = e;
            apiRequestService.request(request).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    ApiResponse apiResponse = response.body();

                    if (apiResponse == null) {
                        emitter.onError(new Exception("Fuck! See ServiceExecutor at line 132."));
                        return;
                    }

                    if (apiResponse.getError() != null) {
                        emitter.onError(new Exception(apiResponse.getError()));
                        return;
                    }

                    emitter.onSuccess(gson.fromJson(gson.toJson(apiResponse.getResponse()), returnType));
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread());
    }

    private static class Aux {
        /**
         * Transform string like "Container&lt;ItemClass&gt;" to class object ItemClass.class.
         *
         * @param type Generic type like "Container&lt;ItemClass&gt;"
         * @return parameter of generic type or null if this class does not exist.
         */
        static Class getClassFromGenericType(String type) {
            int openBracketIndex = type.indexOf("<");
            int closeBracketIndex = type.lastIndexOf(">");
            if (openBracketIndex < 0 || openBracketIndex >= type.length() ||
                closeBracketIndex < 0 || closeBracketIndex >= type.length() ||
                (closeBracketIndex - openBracketIndex) < 2)
                throw new IllegalArgumentException(type);
            try {
                return Class.forName(type.substring(openBracketIndex + 1, closeBracketIndex));
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

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
