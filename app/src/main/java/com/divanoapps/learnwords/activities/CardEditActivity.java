package com.divanoapps.learnwords.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.data.DB;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.DeckId;
import com.divanoapps.learnwords.R;

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

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Expand activity to make transparent notification bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Set difficulty adjustment buttons
        findViewById(R.id.increase_difficulty_button)
                .setOnClickListener(v -> onIncreaseDifficultyClicked());

        findViewById(R.id.decrease_difficulty_button)
                .setOnClickListener(v -> onDecreaseDifficultyClicked());

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
        DB.getCard(mCardId)
            .setOnDoneListener(this::onCardReceived)
            .setOnErrorListener(this::onCardRequestError)
            .execute();
    }

    @SuppressLint("SetTextI18n")
    private void onCardReceived(Card card) {
        ((EditText) findViewById(R.id.word_edit)).setText(card.getWord());
        ((EditText) findViewById(R.id.word_comment_edit)).setText(card.getWordComment());
        ((EditText) findViewById(R.id.translation_edit)).setText(card.getTranslation());
        ((EditText) findViewById(R.id.translation_comment_edit)).setText(card.getTranslationComment());

        mDifficulty = card.getDifficulty();
        ((TextView) findViewById(R.id.difficulty_view))
                .setText(Integer.valueOf(card.getDifficulty()).toString());
        ((TextView) findViewById(R.id.difficulty_maximum_view))
                .setText(Integer.valueOf(Card.getMaxDifficulty()).toString());
    }

    private void onCardRequestError(RequestError error) {
        showErrorMessage(error.getMessage());
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
        return new Card.Builder()
                .setDeckName(mDeckId.getName())
                .setWord(((EditText) findViewById(R.id.word_edit)).getText().toString())
                .setWordComment(((EditText) findViewById(R.id.word_comment_edit)).getText().toString())
                .setTranslation(((EditText) findViewById(R.id.translation_edit)).getText().toString())
                .setTranslationComment(((EditText) findViewById(R.id.translation_comment_edit)).getText().toString())
                .setDifficulty(mDifficulty)
                .setHidden(mVisibility)
                .setCropPicture(false)
                .setPictureUrl("")
                .build();
    }

    private void showErrorMessage(String message) {
        MessageOkDialogFragment.show(this, message);
    }

    private void showErrorMessage(RequestError error) {
        showErrorMessage(error.getMessage());
    }

    private void showCardAlreadyExistsErrorMessage() {
        final String message = "Card with this word and its comment already exists.\n" +
                "Cards with same word and its comment are indistinguishable and can't be saved in one deck.\n" +
                "Change word or its comment (or both of them) and save the card again.";
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
        final Card card = getCurrentStateAsCard();

        DB.getCard(card.getId())
            .setOnDoneListener(this::showCardAlreadyExistsErrorMessage)
            .setOnErrorListener(() ->
                DB.saveCard(card)
                        .setOnDoneListener(this::finish)
                        .setOnErrorListener(this::showErrorMessage)
                        .execute()
            )
            .execute();
    }

    private void editCard() {
        final Card card = getCurrentStateAsCard();

        DB.updateCard(mCardId, card)
            .setOnDoneListener(this::finish)
            .setOnErrorListener(error -> {
                if (error.getType() == RequestError.Type.AlreadyExists)
                    showCardAlreadyExistsErrorMessage();
                else
                    showErrorMessage(error.getMessage());
            })
            .execute();
    }

    @SuppressLint("SetTextI18n")
    private void onIncreaseDifficultyClicked() {
        ++ mDifficulty;
        if (mDifficulty > Card.getMaxDifficulty())
            mDifficulty = Card.getMaxDifficulty();
        ((TextView) findViewById(R.id.difficulty_view))
                .setText(Integer.valueOf(mDifficulty).toString());
    }

    @SuppressLint("SetTextI18n")
    private void onDecreaseDifficultyClicked() {
        -- mDifficulty;
        if (mDifficulty < Card.getMinDifficulty())
            mDifficulty = Card.getMinDifficulty();
        ((TextView) findViewById(R.id.difficulty_view))
                .setText(Integer.valueOf(mDifficulty).toString());
    }
}
