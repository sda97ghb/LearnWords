package com.divanoapps.learnwords.YandexDictionary;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YandexDictionaryService {

    static String getApiKey() {
        return "dict.1.1.20180305T094636Z.c9f58d31fdeffb45.687550088f7228fd6bf3dc67900729ece459b178";
    }

    static String getApiUrl() {
        return "https://dictionary.yandex.net/api/v1/dicservice.json/";
    }

    @GET("lookup")
    Single<DictionaryResult> lookup(@Query("key") String apiKey, @Query("lang") String lang, @Query("text") String text);
}
