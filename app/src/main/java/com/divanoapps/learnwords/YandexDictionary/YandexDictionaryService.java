package com.divanoapps.learnwords.YandexDictionary;

import com.divanoapps.learnwords.entities.TranslationOption;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
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

    static List<TranslationOption> convert(@Nullable DictionaryResult dictionaryResult) {
        List<TranslationOption> translationOptions = new LinkedList<>();

        if (dictionaryResult == null)
            return translationOptions;

        for (Definition definition : dictionaryResult.getDefinitions()) {
            for (Translation translation : definition.getTranslations()) {
                TranslationOption item = new TranslationOption();

                item.setPartOfSpeech(definition.getPartOfSpeech());
                item.setTranslation(translation.getText());

                if (translation.getMeans() == null || translation.getMeans().isEmpty()) {
                    item.setMeans("");
                }
                else {
                    StringBuilder meansBuilder = new StringBuilder();
                    meansBuilder.append(translation.getMeans().get(0).getText());
                    for (int i = 1; i < translation.getMeans().size(); ++i)
                        meansBuilder.append(", ").append(translation.getMeans().get(i).getText());
                    item.setMeans(meansBuilder.toString());
                }

                translationOptions.add(item);
            }
        }

        return translationOptions;
    }
}
