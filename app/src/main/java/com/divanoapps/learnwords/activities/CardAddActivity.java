package com.divanoapps.learnwords.activities;

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
import com.divanoapps.learnwords.data.api2.ApiCard;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.TranslationOption;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Activity for adding new card. Started from DeckEdit activity.
 * The name of the deck in which new card should be added must be passed as
 * an extra {@code getDeckNameExtraName()} of the intent.
 */
public class CardAddActivity extends AppCompatActivity
    implements TranslationListAdapter.OnTranslationOptionSelectedListener {

    // UI components

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

    // Constants

    public static String getDeckNameExtraName() {
        return "DECK_NAME";
    }

    // Other private fields

    private String deckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_add);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deckName = getIntent().getStringExtra(getDeckNameExtraName());
        if (deckName == null)
            deckName = Application.getDefaultDeckName();

        deckNameView.setText(deckName);

        wordEdit.requestFocus();
        wordEdit.clearFocus();

        translationOptionsView.setLayoutManager(new LinearLayoutManager(this));
        TranslationListAdapter translationListAdapter = new TranslationListAdapter();
        translationListAdapter.setOnTranslationOptionSelectedListener(this);
        translationOptionsView.setAdapter(translationListAdapter);

        RxTextView.textChanges(wordEdit)
            .subscribe(charSequence -> {
                requestTranslations(charSequence.toString());
                checkExistence();
            });

        RxTextView.textChanges(commentEdit).subscribe(charSequence -> checkExistence());
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

    /**
     * Returns new card object filled with values from this activity.
     * @return new card object filled with values from this activity.
     */
    private ApiCard getCurrentStateAsApiCard() {
        ApiCard apiCard = new ApiCard();
        apiCard.setOwner(Application.getGoogleSignInAccount().getEmail());
        apiCard.setDeck(deckName);
        apiCard.setWord(wordEdit.getText().toString());
        apiCard.setComment(commentEdit.getText().toString());
        apiCard.setTranslation(translationEdit.getText().toString());
        apiCard.setDifficulty(Card.getDefaultDifficulty());
        apiCard.setHidden(false);
        return apiCard;
    }

    /**
     * Will be called when user clicked done toolbar button.
     */
    private void onDoneClicked() {
        ApiCard apiCard = getCurrentStateAsApiCard();
        Application.getApi().getCard(apiCard.getDeck(), apiCard.getWord(), apiCard.getComment())
            .doOnSuccess(unused -> showErrorMessage("Card already exists."))
            .doOnError(unused -> Application.getApi().saveCard(apiCard)
                .doOnComplete(this::finish)
                .doOnError(this::showErrorMessage)
                .subscribe())
            .subscribe();
    }

    /**
     * Request translations of {@code text} from YandexDictionary service and view them.
     * @param text Text, translations of which will be requested.
     */
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

    /**
     * Converts DictionaryResult from YandexDictionary service to list of TranslationOptions and
     * set this list as translation list adapter's list.
     * @param dictionaryResult
     */
    private void viewDictionaryResult(DictionaryResult dictionaryResult) {
        if (translationOptionsView.getAdapter() == null)
            return;
        List<TranslationOption> translationOptions = YandexDictionaryService.convert(dictionaryResult);
        TranslationListAdapter adapter = (TranslationListAdapter) translationOptionsView.getAdapter();
        adapter.setTranslationOptions(translationOptions);
    }

    /**
     * Shows a dialog with text.
     * @param message Text of the error.
     */
    private void showErrorMessage(String message) {
        MessageOkDialogFragment.show(this, message);
    }

    /**
     * Calls {@code showErrorMessage(String message)} with {@code throwable.getMessage()} as message.
     * @param throwable Any throwable
     */
    private void showErrorMessage(Throwable throwable) {
        if (throwable instanceof ApiError)
            showErrorMessage(((ApiError) throwable).getType() + ":" + throwable.getMessage());
        else
            showErrorMessage(throwable.getMessage());
    }

    /**
     * Check if current deck already has card with same word and comment and visualize result.
     */
    private void checkExistence() {
        ApiCard apiCard = getCurrentStateAsApiCard();
        Application.getApi().getCard(apiCard.getDeck(), apiCard.getWord(), apiCard.getComment())
            .doOnSuccess(unused -> commentEditWrapper.setError("Already exists"))
            .doOnError(throwable -> {
                commentEditWrapper.setError(null);
                commentEditWrapper.setErrorEnabled(false);
            })
            .subscribe();
    }

    /**
     * Sets selected option as translation.
     * @param option Will be set as translation.
     */
    @Override
    public void onTranslationOptionSelected(TranslationOption option) {
        translationEdit.setText(option.getTranslation());
        translationEdit.requestFocus();
        translationEdit.setSelection(translationEdit.length());
    }
}
