package com.divanoapps.learnwords;

import com.divanoapps.learnwords.YandexDictionary.YandexDictionaryService;
import com.divanoapps.learnwords.data.api2.Api2;
import com.divanoapps.learnwords.data.api2.ServiceExecutor;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Application {
    public static final Api2 api = ServiceExecutor.create(Api2.class);

    public static final String FAKE_EMAIL = "sda97g@gmail.com";

    public static final Retrofit yandexRetrofit = new Retrofit.Builder()
        .baseUrl(YandexDictionaryService.getApiUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    public static final YandexDictionaryService yandexDictionaryService = yandexRetrofit.create(YandexDictionaryService.class);

    public static final String FAKE_DIRECTION = "en-ru";

    public static String getDefaultDeckName() {
        return "Unsorted";
    }
}
