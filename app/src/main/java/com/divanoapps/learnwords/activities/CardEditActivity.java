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
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.RepositoryModule;
import com.divanoapps.learnwords.data.local.TimestampFactory;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.entities.TranslationOption;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CardEditActivity extends AppCompatActivity
    implements TranslationListAdapter.OnTranslationOptionSelectedListener {

    public static String getDeckNameExtraName() {
        return "DECK_NAME_EXTRA";
    }

    public static String getWordExtraName() {
        return "WORD_EXTRA";
    }

    public static String getCommentExtraName() {
        return "COMMENT_EXTRA";
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

    private String deckName;
    private String oldWord;
    private String oldComment;

    private int difficulty = Card.getDefaultDifficulty();
    private boolean visibility = true;

    TranslationListAdapter translationListAdapter;

    RepositoryModule repositoryModule;

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

        repositoryModule.getCardRepository().find(deckName, oldWord, oldComment)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::showCard)
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
        if (translationOptionsView.getAdapter() == null)
            return;
        List<TranslationOption> translationOptions = YandexDictionaryService.convert(dictionaryResult);
        translationListAdapter.setTranslationOptions(translationOptions);
    }

    private void checkExistence() {
        Card card = getCurrentStateAsCard();
        repositoryModule.getCardRepository().find(card.getDeckName(), card.getWord(), card.getComment())
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
    private void showCard(Card card) {
        wordEdit.setText(card.getWord());
        commentEdit.setText(card.getComment());
        translationEdit.setText(card.getTranslation());

        difficulty = card.getDifficulty();
        difficultyView.setText(card.getDifficulty().toString());
        difficultyMaximumView.setText(Card.getMaxDifficulty().toString());

        visibility = !card.isHidden();
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

    private Card getCurrentStateAsCard() {
        Card card = new Card();
        card.setTimestamp(TimestampFactory.getTimestamp());
        card.setDeckName(deckName);
        card.setWord(wordEdit.getText().toString());
        card.setComment(commentEdit.getText().toString());
        card.setTranslation(translationEdit.getText().toString());
        card.setDifficulty(difficulty);
        card.setHidden(!visibility);
        return card;
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
        editCard();
    }

    private void editCard() {
        Card card = getCurrentStateAsCard();
        repositoryModule.getCardRepository().replace(deckName, oldWord, oldComment, card)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::finish)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    @OnClick(R.id.increase_difficulty_button)
    @SuppressLint("SetTextI18n")
    public void onIncreaseDifficultyClicked() {
        ++difficulty;
        if (difficulty > Card.getMaxDifficulty())
            difficulty = Card.getMaxDifficulty();
        difficultyView.setText(Integer.valueOf(difficulty).toString());
    }

    @OnClick(R.id.decrease_difficulty_button)
    @SuppressLint("SetTextI18n")
    public void onDecreaseDifficultyClicked() {
        --difficulty;
        if (difficulty < Card.getMinDifficulty())
            difficulty = Card.getMinDifficulty();
        difficultyView.setText(Integer.valueOf(difficulty).toString());
    }

    @OnClick(R.id.visibility_button)
    public void onToggleVisibilityClicked() {
        visibility = !visibility;
        visibilityButton.setImageDrawable(visibility ? visibleIcon : invisibleIcon);
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
