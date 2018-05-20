package com.divanoapps.learnwords.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Activity for editing a card. Started from DeckEdit activity.
 * Values of {@code getDeckNameExtraName(), getWordExtraName(), getCommentExtraName()}
 * must be passed with the intent.
 */
public class CardEditActivity extends AppCompatActivity
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

    @BindView(R.id.difficulty_view)
    TextView difficultyView;

    @BindView(R.id.difficulty_maximum_view)
    TextView difficultyMaximumView;

    @BindView(R.id.visibility_button)
    ImageButton visibilityButton;

    @BindDrawable(R.drawable.ic_card_edit_visible)
    Drawable visibleIcon;

    @BindDrawable(R.drawable.ic_card_edit_invisible)
    Drawable invisibleIcon;

    // Other fields

    private String deckName;
    private String oldWord;
    private String oldComment;

    private int mDifficulty = Card.getDefaultDifficulty();
    private boolean mVisibility = true;

    // Constants

    public static String getDeckNameExtraName() {
        return "DECK_NAME_EXTRA";
    }
    public static String getWordExtraName() {
        return "WORD_EXTRA";
    }
    public static String getCommentExtraName() {
        return "COMMENT_EXTRA";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deckName = getIntent().getStringExtra(getDeckNameExtraName());
        if (deckName == null)
            deckName = Application.getDefaultDeckName();
        oldWord = getIntent().getStringExtra(getWordExtraName());
        oldComment = getIntent().getStringExtra(getCommentExtraName());

        wordEdit.setText(oldWord);
        commentEdit.setText(oldComment);

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

        Application.getApi().getCard(deckName, oldWord, oldComment)
            .doOnSuccess(this::showCard)
            .doOnError(this::showErrorMessage)
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
     * Sets views and editors values from values of the card.
     * @param card
     */
    @SuppressLint("SetTextI18n")
    private void showCard(ApiCard card) {
        wordEdit.setText(card.getWord());
        commentEdit.setText(card.getComment());
        translationEdit.setText(card.getTranslation());

        mDifficulty = card.getDifficulty();
        difficultyView.setText(Integer.valueOf(card.getDifficulty()).toString());
        difficultyMaximumView.setText(Integer.valueOf(Card.getMaxDifficulty()).toString());

        mVisibility = !card.isHidden();
        visibilityButton.setImageDrawable(card.isHidden() ? invisibleIcon : visibleIcon);
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

    /**
     * Returns new card object filled with values from this activity.
     * @return new card object filled with values from this activity.
     */
    private ApiCard getCurrentStateAsApiCard() {
        ApiCard apiCard = new ApiCard();
        apiCard.setDeck(deckName);
        apiCard.setWord(wordEdit.getText().toString());
        apiCard.setComment(commentEdit.getText().toString());
        apiCard.setTranslation(translationEdit.getText().toString());
        apiCard.setDifficulty(mDifficulty);
        apiCard.setHidden(!mVisibility);
        apiCard.setOwner(Application.getGoogleSignInAccount().getEmail());
        return apiCard;
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

//    private void showCardAlreadyExistsErrorMessage() {
//        final String message = getString(R.string.card_with_id_already_exists);
//        showErrorMessage(message);
//    }

    /**
     * Will be called when user clicked done toolbar button.
     */
    private void onDoneClicked() {
        editCard();
    }

    /**
     * Saves edited card and finishes this activity.
     */
    private void editCard() {
        final ApiCard card = getCurrentStateAsApiCard();
        Map<String, Object> properties = new HashMap<>();
        properties.put("word", card.getWord());
        properties.put("comment", card.getComment());
        properties.put("translation", card.getTranslation());
        properties.put("difficulty", card.getDifficulty());
        properties.put("hidden", card.isHidden());
        Application.getApi().updateCard(deckName, oldWord, oldComment, properties)
            .doOnComplete(this::finish)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    @OnClick(R.id.increase_difficulty_button)
    @SuppressLint("SetTextI18n")
    public void onIncreaseDifficultyClicked() {
        ++ mDifficulty;
        if (mDifficulty > Card.getMaxDifficulty())
            mDifficulty = Card.getMaxDifficulty();
        difficultyView.setText(Integer.valueOf(mDifficulty).toString());
    }

    @OnClick(R.id.decrease_difficulty_button)
    @SuppressLint("SetTextI18n")
    public void onDecreaseDifficultyClicked() {
        -- mDifficulty;
        if (mDifficulty < Card.getMinDifficulty())
            mDifficulty = Card.getMinDifficulty();
        difficultyView.setText(Integer.valueOf(mDifficulty).toString());
    }

    @OnClick(R.id.visibility_button)
    public void onToggleVisibilityClicked() {
        mVisibility = !mVisibility;
        visibilityButton.setImageDrawable(mVisibility ? visibleIcon : invisibleIcon);
    }

//    private void onSelectPictureClicked() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivity(Intent.createChooser(intent, "Select Picture"));
//    }

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
