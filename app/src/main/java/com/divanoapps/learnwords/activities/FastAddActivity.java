package com.divanoapps.learnwords.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.YandexDictionary.Definition;
import com.divanoapps.learnwords.YandexDictionary.DictionaryResult;
import com.divanoapps.learnwords.YandexDictionary.Translation;
import com.divanoapps.learnwords.YandexDictionary.YandexDictionaryService;
import com.divanoapps.learnwords.adapters.TranslationListAdapter;
import com.divanoapps.learnwords.entities.TranslationOption;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FastAddActivity extends AppCompatActivity {

    @BindView(R.id.word_edit)
    EditText wordEdit;

    @BindView(R.id.word_comment_edit)
    EditText wordCommentEdit;

    @BindView(R.id.translation_edit)
    EditText translationEdit;

    @BindView(R.id.translation_options_view)
    RecyclerView translationOptionsView;

    List<TranslationOption> convert(DictionaryResult dictionaryResult) {
        List<TranslationOption> translationOptions = new LinkedList<>();

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

    private void viewDictionaryResult(DictionaryResult dictionaryResult) {
        List<TranslationOption> translationOptions = convert(dictionaryResult);
        TranslationListAdapter adapter = (TranslationListAdapter) translationOptionsView.getAdapter();
        adapter.setTranslationOptions(translationOptions);
//        wordEdit.setText(dictionaryResult.getDefinitions().get(0).getText());
//        translationCommentEdit.setText("");
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

        wordEdit.setText(text);
        wordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    return;
                yandexDictionaryService.lookup(YandexDictionaryService.getApiKey(), direction, wordEdit.getText().toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(FastAddActivity.this::viewDictionaryResult)
                    .doOnError(Throwable::printStackTrace)
                    .subscribe();
            }
        });

        yandexDictionaryService.lookup(YandexDictionaryService.getApiKey(), direction, text)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::viewDictionaryResult)
            .doOnError(Throwable::printStackTrace)
            .subscribe();

        wordEdit.requestFocus();
        wordEdit.clearFocus();

        translationOptionsView.setLayoutManager(new LinearLayoutManager(this));
        translationOptionsView.setAdapter(new TranslationListAdapter());
    }
}
