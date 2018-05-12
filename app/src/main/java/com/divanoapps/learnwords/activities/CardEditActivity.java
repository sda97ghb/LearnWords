package com.divanoapps.learnwords.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.divanoapps.learnwords.Application;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.YandexDictionary.DictionaryResult;
import com.divanoapps.learnwords.YandexDictionary.YandexDictionaryService;
import com.divanoapps.learnwords.adapters.TranslationListAdapter;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.CardSpecificationsFactory;
import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.local.RepositoryModule;
import com.divanoapps.learnwords.data.local.TimestampFactory;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.entities.TranslationOption;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CardEditActivity extends AppCompatActivity
    implements TranslationListAdapter.OnTranslationOptionSelectedListener {

    public static String getDeckExtraName() {
        return "DECK_EXTRA";
    }

    public static String getCardExtraName() {
        return "CARD_EXTRA";
    }

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

    private Deck deck;
    private Card card;

    TranslationListAdapter translationListAdapter;

    RepositoryModule repositoryModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deck = getIntent().getParcelableExtra(getDeckExtraName());
        card = getIntent().getParcelableExtra(getCardExtraName());

        wordEdit.setText(card.getWord());
        commentEdit.setText(card.getComment());
        translationEdit.setText(card.getTranslation());

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

        List<Card> cards = new LinkedList<>();
        cards.add(card);
        showCard(cards);
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
        if (translationOptionsView.getAdapter() == null)
            return;
        List<TranslationOption> translationOptions = YandexDictionaryService.convert(dictionaryResult);
        translationListAdapter.setTranslationOptions(translationOptions);
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

    @SuppressLint("SetTextI18n")
    private void showCard(List<Card> cards) {
        Card card = cards.get(0);

        wordEdit.setText(card.getWord());
        commentEdit.setText(card.getComment());
        translationEdit.setText(card.getTranslation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_edit_activity_toolbar, menu);
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
        Card newCard = new Card();
        newCard.setTimestamp(TimestampFactory.getTimestamp());
        newCard.setDeckName(deck.getName());
        newCard.setWord(wordEdit.getText().toString());
        newCard.setComment(commentEdit.getText().toString());
        newCard.setTranslation(translationEdit.getText().toString());
        newCard.setDifficulty(card.getDifficulty());
        newCard.setHidden(card.isHidden());
        return newCard;
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

//    private void showCardAlreadyExistsErrorMessage() {
//        final String message = getString(R.string.card_with_id_already_exists);
//        showErrorMessage(message);
//    }

    private void onDoneClicked() {
        saveChanges();
    }

    private void saveChanges() {
        Card newCard = getCurrentStateAsCard();
        Completable completable;
        if (newCard.getWord().equals(card.getWord()) && newCard.getComment().equals(card.getComment())) {
            completable = repositoryModule.getCardRxRepository().update(newCard);
        }
        else {
            completable = Completable.fromAction(() -> {
                List<Card> cards = repositoryModule.getCardRepository()
                    .query(CardSpecificationsFactory.byDeckNameAndWordAndComment(newCard.getDeckName(), newCard.getWord(), newCard.getComment()));
                if (cards.isEmpty()) {
                    repositoryModule.getCardRepository().delete(card);
                    repositoryModule.getCardRepository().insert(newCard);
                }
                else
                    throw new Exception("Card already exists.");

            });
        }
        completable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::finish)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

//    private void onSelectPictureClicked() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivity(Intent.createChooser(intent, "Select Picture"));
//    }

    @Override
    public void onTranslationOptionSelected(TranslationOption option) {
        translationEdit.setText(option.getTranslation());
        translationEdit.requestFocus();
        translationEdit.setSelection(translationEdit.length());
    }
}
