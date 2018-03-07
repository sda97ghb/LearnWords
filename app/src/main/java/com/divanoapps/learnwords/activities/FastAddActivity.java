package com.divanoapps.learnwords.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.YandexDictionary.Definition;
import com.divanoapps.learnwords.YandexDictionary.DictionaryResult;
import com.divanoapps.learnwords.YandexDictionary.Mean;
import com.divanoapps.learnwords.YandexDictionary.Translation;
import com.divanoapps.learnwords.YandexDictionary.YandexDictionaryService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FastAddActivity extends AppCompatActivity {

    @BindView(R.id.translation_view)
    TextView translationView;

    private void printDictionaryResult(DictionaryResult dictionaryResult) {
        StringBuilder result = new StringBuilder();
        for (Definition definition : dictionaryResult.getDefinitions()) {
            result.append(definition.getPartOfSpeech()).append(":\n");
            for (Translation translation : definition.getTranslations()) {
                result.append("\t")
                    .append(translation.getText())
                    .append(" (");
                for (Mean mean : translation.getMeans())
                    result.append(mean.getText()).append(", ");
                result.append(")\n");
            }
        }
        translationView.setText(result.toString());
    }

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(YandexDictionaryService.getApiUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    YandexDictionaryService yandexDictionaryService = retrofit.create(YandexDictionaryService.class);

    String direction = "en-ru";
    String text = "time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_add);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
            yandexDictionaryService.lookup(YandexDictionaryService.getApiKey(), direction, text)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::printDictionaryResult)
                .doOnError(Throwable::printStackTrace)
                .subscribe()
        );
    }
}
