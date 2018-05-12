package com.divanoapps.learnwords.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.divanoapps.learnwords.Application;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.YandexDictionary.DictionaryResult;
import com.divanoapps.learnwords.YandexDictionary.YandexDictionaryService;
import com.divanoapps.learnwords.adapters.TranslationListAdapter;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.CardSpecificationsFactory;
import com.divanoapps.learnwords.data.local.DeckSpecificationsFactory;
import com.divanoapps.learnwords.data.local.RepositoryModule;
import com.divanoapps.learnwords.data.local.TimestampFactory;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.entities.TranslationOption;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FastAddActivity extends AppCompatActivity
    implements TranslationListAdapter.OnTranslationOptionSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.deck_name_view)
    TextView deckNameView;

    @BindView(R.id.word_edit)
    EditText wordEdit;

    @BindView(R.id.comment_edit_wrapper)
    TextInputLayout commentEditWrapper;

    @BindView(R.id.comment_edit)
    EditText commentEdit;

    @BindView(R.id.translation_edit)
    EditText translationEdit;

    @BindView(R.id.translation_options_view)
    RecyclerView translationOptionsView;

    TranslationListAdapter translationListAdapter;

    private String deckName;

    RepositoryModule repositoryModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_add);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deckName = Application.getDefaultDeckName();
        deckNameView.setText(deckName);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                wordEdit.setText(sharedText);
                requestTranslations(sharedText);
            }
        }
//        else {
//            // Handle other intents, such as being started from the home screen
//        }

        wordEdit.requestFocus();
        wordEdit.clearFocus();

        translationOptionsView.setLayoutManager(new LinearLayoutManager(this));
        translationListAdapter = new TranslationListAdapter();
        translationListAdapter.setOnTranslationOptionSelectedListener(this);
        translationOptionsView.setAdapter(translationListAdapter);

        RxTextView.textChanges(wordEdit)
            .subscribe(charSequence -> {
                requestTranslations(charSequence.toString());
                checkExistence();
            });

        RxTextView.textChanges(commentEdit).subscribe(charSequence -> checkExistence());

        repositoryModule = new RepositoryModule(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_add_activity_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: finish(); return true;// NavUtils.navigateUpFromSameTask(this); return true;
            case R.id.action_done: onDoneClicked(); return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    private Card getCurrentStateAsCard() {
        Card card = new Card();
        card.setTimestamp(TimestampFactory.getTimestamp());
        card.setDeckName(deckName);
        card.setWord(wordEdit.getText().toString());
        card.setComment(commentEdit.getText().toString());
        card.setTranslation(translationEdit.getText().toString());
        card.setDifficulty(Card.getDefaultDifficulty());
        card.setHidden(false);
        return card;
    }

    private void onDoneClicked() {
        Card card = getCurrentStateAsCard();
        Completable.fromAction(() -> {
            List<Card> cards = repositoryModule.getCardRepository()
                .query(CardSpecificationsFactory.byDeckNameAndWordAndComment(card.getDeckName(), card.getWord(), card.getComment()));
            if (cards.isEmpty())
                repositoryModule.getCardRxRepository().insert(card);
            else
                showErrorMessage("Card already exists.");
        })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(() -> {
                Toast.makeText(this, "Card saved", Toast.LENGTH_SHORT).show();
                finish();
            })
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    private void requestTranslations(String text) {
        if (text.isEmpty()) {
            viewDictionaryResult(null);
            return;
        }
        Application.yandexDictionaryService.lookup(YandexDictionaryService.getApiKey(), Application.FAKE_DIRECTION, text)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::viewDictionaryResult)
            .doOnError(Throwable::printStackTrace)
            .subscribe();
    }

    private void viewDictionaryResult(DictionaryResult dictionaryResult) {
        List<TranslationOption> translationOptions = YandexDictionaryService.convert(dictionaryResult);
        translationListAdapter.setTranslationOptions(translationOptions);
    }

    private void showErrorMessage(String message) {
        MessageOkDialogFragment.show(this, message);
    }

    private void showErrorMessage(Throwable throwable) {
        if (throwable instanceof ApiError)
            showErrorMessage(((ApiError) throwable).getType() + ":" + throwable.getMessage());
        else
            showErrorMessage(throwable.getMessage());
    }

    private void checkExistence() {
        Card card = getCurrentStateAsCard();
        repositoryModule.getCardRxRepository()
            .query(CardSpecificationsFactory.byDeckNameAndWordAndComment(card.getDeckName(), card.getWord(), card.getComment()))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(unused -> commentEditWrapper.setError("Already exists"))
            .doOnError(throwable -> {
                commentEditWrapper.setError(null);
                commentEditWrapper.setErrorEnabled(false);
            })
            .subscribe();
    }

    @Override
    public void onTranslationOptionSelected(TranslationOption option) {
        translationEdit.setText(option.getTranslation());
        translationEdit.requestFocus();
        translationEdit.setSelection(translationEdit.length());
    }

    @OnClick(R.id.select_deck_button)
    public void onSelectDeckButtonClicked() {
        repositoryModule.getDeckRxRepository()
            .query(DeckSpecificationsFactory.allDecks())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(decks -> {
                CharSequence[] items = new CharSequence[decks.size()];
                for (int i = 0; i < decks.size(); ++ i)
                items[i] = decks.get(i).getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select deck")
                    .setItems(items, (dialog, which) -> {
                        deckName = decks.get(which).getName();
                        deckNameView.setText(deckName);
                    }).create().show();
            })
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showErrorMessageToast(connectionResult.getErrorMessage());
        finish();
    }

    private void showErrorMessageToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
