package com.divanoapps.learnwords.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divanoapps.learnwords.Application;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.data.api2.ApiCard;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.DeckId;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CardEditActivity extends AppCompatActivity {
    enum Mode {
        ADD_CARD,
        EDIT_CARD
    }

    private CardId mCardId;
    private DeckId mDeckId;
    private int mDifficulty = Card.getDefaultDifficulty();
    private boolean mVisibility = true;
    private Mode mMode;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.word_edit)
    EditText wordEdit;

    @BindView(R.id.word_comment_edit)
    EditText commentEdit;

    @BindView(R.id.translation_edit)
    EditText translationEdit;

    @BindView(R.id.difficulty_view)
    TextView difficultyView;

    @BindView(R.id.difficulty_maximum_view)
    TextView difficultyMaximumView;

    @BindView(R.id.increase_difficulty_button)
    Button increaseDifficultyButton;

    @BindView(R.id.decrease_difficulty_button)
    Button decreaseDifficultyButton;

    @BindView(R.id.select_picture_button)
    Button selectPictureButton;

    @BindView(R.id.visibility_button)
    ImageButton visibilityButton;

    @BindDrawable(R.drawable.ic_card_edit_visible)
    Drawable visibleIcon;

    @BindDrawable(R.drawable.ic_card_edit_invisible)
    Drawable invisibleIcon;

    public static String getModeExtraName() {
        return "MODE_EXTRA";
    }
    public static String getCardIdExtraName() {
        return "CARD_ID_EXTRA";
    }
    public static String getDeckIdExtraName() {
        return "CARD_ID_EXTRA";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);
        ButterKnife.bind(this);

        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Expand activity to make transparent notification bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Set difficulty adjustment buttons
        increaseDifficultyButton.setOnClickListener(v -> onIncreaseDifficultyClicked());
        decreaseDifficultyButton.setOnClickListener(v -> onDecreaseDifficultyClicked());
        visibilityButton.setOnClickListener(v -> onVisibilityClicked());
        selectPictureButton.setOnClickListener(v -> onSelectPictureClicked());

        // Run required mode
        // TODO: Add exception when mode is not passed
        mMode = (Mode) getIntent().getSerializableExtra(getModeExtraName());
        switch (mMode) {
            case ADD_CARD:  runAddCardMode();  break;
            case EDIT_CARD: runEditCardMode(); break;
        }
    }

    private void runAddCardMode() {
        mDeckId = (DeckId) getIntent().getSerializableExtra(getDeckIdExtraName());
    }

    private void runEditCardMode() {
        mCardId = (CardId) getIntent().getSerializableExtra(getCardIdExtraName());
        mDeckId = new DeckId(mCardId.getDeckName());
        Application.api.getCard(Application.FAKE_EMAIL, mCardId.getDeckName(), mCardId.getWord(), mCardId.getWordComment())
            .doOnSuccess(this::showCard)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

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

    private ApiCard getCurrentStateAsApiCard() {
        ApiCard apiCard = new ApiCard();
        apiCard.setDeck(mDeckId.getName());
        apiCard.setWord(wordEdit.getText().toString());
        apiCard.setComment(commentEdit.getText().toString());
        apiCard.setTranslation(translationEdit.getText().toString());
        apiCard.setDifficulty(mDifficulty);
        apiCard.setHidden(!mVisibility);
        apiCard.setOwner(Application.FAKE_EMAIL);
        return apiCard;
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

    private void showCardAlreadyExistsErrorMessage() {
        final String message = getString(R.string.card_with_id_already_exists);
        showErrorMessage(message);
    }

    private void onDoneClicked() {
        // TODO: add exception when card id is empty
        switch (mMode) {
            case ADD_CARD:  addCard();  break;
            case EDIT_CARD: editCard(); break;
        }
    }

    private void addCard() {
        final ApiCard card = getCurrentStateAsApiCard();

        Application.api.getCard(Application.FAKE_EMAIL, card.getDeck(), card.getWord(), card.getComment())
            .doOnSuccess(apiCard -> showCardAlreadyExistsErrorMessage())
            .doOnError(throwable ->
                Application.api.saveCard(Application.FAKE_EMAIL, card)
                    .doOnComplete(this::finish)
                    .doOnError(this::showErrorMessage)
                    .subscribe()
            )
            .subscribe();
    }

    private void editCard() {
        final ApiCard card = getCurrentStateAsApiCard();
        Map<String, Object> properties = new HashMap<>();
        properties.put("word", card.getWord());
        properties.put("comment", card.getComment());
        properties.put("translation", card.getTranslation());
        properties.put("difficulty", card.getDifficulty());
        properties.put("hidden", card.isHidden());
        Application.api.updateCard(Application.FAKE_EMAIL, mCardId.getDeckName(), mCardId.getWord(), mCardId.getWordComment(), properties)
            .doOnComplete(this::finish)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    @SuppressLint("SetTextI18n")
    private void onIncreaseDifficultyClicked() {
        ++ mDifficulty;
        if (mDifficulty > Card.getMaxDifficulty())
            mDifficulty = Card.getMaxDifficulty();
        difficultyView.setText(Integer.valueOf(mDifficulty).toString());
    }

    @SuppressLint("SetTextI18n")
    private void onDecreaseDifficultyClicked() {
        -- mDifficulty;
        if (mDifficulty < Card.getMinDifficulty())
            mDifficulty = Card.getMinDifficulty();
        difficultyView.setText(Integer.valueOf(mDifficulty).toString());
    }

    private void onVisibilityClicked() {
        mVisibility = !mVisibility;
        visibilityButton.setImageDrawable(mVisibility ? visibleIcon : invisibleIcon);
    }

    private void onSelectPictureClicked() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivity(Intent.createChooser(intent, "Select Picture"));
    }
}
