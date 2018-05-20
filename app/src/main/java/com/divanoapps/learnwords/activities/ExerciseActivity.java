package com.divanoapps.learnwords.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.divanoapps.learnwords.CardRetriever;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.Deck;

/**
 * Not implemented yet.
 */
public class ExerciseActivity extends AppCompatActivity {

    private enum Side {
        First,
        Second
    }

    TextView mWordView;
    TextView mWordCommentView;
    TextView mTranslationView;
    TextView mTranslationCommentView;

    View mCardPartsSeparator;

    Button mEasyButton;
    Button mHardButton;
    Button mHideButton;

    View mRootView;

    CardRetriever.Order mOrder;
    Deck mDeck;
    CardRetriever mCardRetriever;
    Card mCurrentCard;
    Side mCurrentSide;

    public static String getDeckIdExtraName() {
        return "DECK_ID_EXTRA";
    }

    public static String getOrderExtraName() {
        return "ORDER_EXTRA";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise);

        findUi();
        setOnClickListeners();

        Intent intent = getIntent();
        if (!intent.hasExtra(getDeckIdExtraName()) ||
            !intent.hasExtra(getOrderExtraName()))
            finish();
        mOrder = (CardRetriever.Order) intent.getSerializableExtra(getOrderExtraName());
//        DB.getDeck((DeckId) intent.getSerializableExtra(getDeckIdExtraName()))
//            .setOnDoneListener(this::startExerciseForDeck)
//            .setOnErrorListener(this::finish)
//            .execute();
    }

    private void startExerciseForDeck(Deck deck) {
        mDeck = deck;
        mCardRetriever = new CardRetriever(mOrder, mDeck);
        showNextCard();
    }

    private void showNextCard() {
        hideSecondSide();
        mCurrentSide = Side.First;

        mCurrentCard = mCardRetriever.getNext();
        if (mCurrentCard == null)
            finishExercise();
        else
            setTextFromCurrentCard();
    }

    private void setTextFromCurrentCard() {
        mWordView.setText(mCurrentCard.getWord());
        mWordCommentView.setText(mCurrentCard.getWordComment());
        mTranslationView.setText(mCurrentCard.getTranslation());
        mTranslationCommentView.setText(mCurrentCard.getTranslationComment());
    }

    private void hideSecondSide() {
        mCardPartsSeparator.setVisibility(View.GONE);
        mTranslationView.setVisibility(View.GONE);
        mTranslationCommentView.setVisibility(View.GONE);
    }

    private void showSecondSide() {
        mCardPartsSeparator.setVisibility(View.VISIBLE);
        mTranslationView.setVisibility(View.VISIBLE);
        mTranslationCommentView.setVisibility(View.VISIBLE);
    }

    private void findUi() {
        mWordView               = (TextView) findViewById(R.id.word_view);
        mWordCommentView        = (TextView) findViewById(R.id.word_comment_view);
        mTranslationView        = (TextView) findViewById(R.id.translation_view);
        mTranslationCommentView = (TextView) findViewById(R.id.translation_comment_view);

        mCardPartsSeparator     =            findViewById(R.id.card_parts_separator);

        mEasyButton             = (Button)   findViewById(R.id.easy_button);
        mHardButton             = (Button)   findViewById(R.id.hard_button);
        mHideButton             = (Button)   findViewById(R.id.hide_button);

        mRootView               =            findViewById(R.id.root_layout);
    }

    private void setOnClickListeners() {
        mEasyButton.setOnClickListener(v -> onEasyClicked());
        mHardButton.setOnClickListener(v -> onHardClicked());
        mHideButton.setOnClickListener(v -> onHideClicked());

        mRootView.setOnClickListener(v -> onNextClicked());
        mCardPartsSeparator.setOnClickListener(v -> onNextClicked());
        mWordView.setOnClickListener(v -> onNextClicked());
        mWordCommentView.setOnClickListener(v -> onNextClicked());
        mTranslationView.setOnClickListener(v -> onNextClicked());
        mTranslationCommentView.setOnClickListener(v -> onNextClicked());
    }

    private void onEasyClicked() {
        if (mCurrentCard == null)
            return;

        switch (mCurrentSide) {
            case First:  onNextClicked();      break;
            case Second: decreaseDifficulty(); break;
        }
    }

    private void onHardClicked() {
        if (mCurrentCard == null)
            return;

        switch (mCurrentSide) {
            case First:  onNextClicked();      break;
            case Second: increaseDifficulty(); break;
        }
    }

    private void onHideClicked() {
        if (mCurrentCard == null)
            return;

        Card newCard = new Card.Builder(mCurrentCard)
                .setHidden(!mCurrentCard.isHidden())
                .build();
//        DB.saveCard(newCard)
//                .setOnDoneListener(this::showNextCard)
//                .setOnErrorListener(this::showErrorMessage)
//                .execute();
    }

    private void onNextClicked() {
        if (mCurrentCard == null)
            return;

        switch (mCurrentSide) {
            case First:
                showSecondSide();
                mCurrentSide = Side.Second;
                break;
            case Second:
                showNextCard();
                break;
        }
    }

    private void finishExercise() {
        mWordView.setText("Finished.");
        mWordCommentView.setText("");
        hideSecondSide();
    }

    private void increaseDifficulty() {
//        int difficulty = mCurrentCard.getDifficulty() + 1;
//        if (difficulty > Card.getMaxDifficulty())
//            difficulty = Card.getMinDifficulty();
//        Card newCard = new Card.Builder(mCurrentCard)
//                .setDifficulty(difficulty)
//                .build();
//        DB.saveCard(newCard)
//                .setOnDoneListener(this::showNextCard)
//                .setOnErrorListener(this::showErrorMessage)
//                .execute();
    }

    private void decreaseDifficulty() {
//        int difficulty = mCurrentCard.getDifficulty() - 1;
//        if (difficulty < Card.getMinDifficulty())
//            difficulty = Card.getMinDifficulty();
//        Card newCard = new Card.Builder(mCurrentCard)
//                .setDifficulty(difficulty)
//                .build();
//        DB.saveCard(newCard)
//                .setOnDoneListener(this::showNextCard)
//                .setOnErrorListener(this::showErrorMessage)
//                .execute();
    }

    private void showErrorMessage(String message) {
        MessageOkDialogFragment.show(this, message);
    }
}
