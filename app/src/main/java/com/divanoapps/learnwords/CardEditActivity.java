package com.divanoapps.learnwords;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.divanoapps.learnwords.Data.DB;
import com.divanoapps.learnwords.Entities.Card;
import com.divanoapps.learnwords.Entities.CardId;

public class CardEditActivity extends AppCompatActivity {
    private Card mCard;
    private int mDifficulty;

    public static String getCardIdExtraKey() {
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
        findViewById(R.id.increase_difficulty_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIncreaseDifficultyClicked();
            }
        });

        findViewById(R.id.decrease_difficulty_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDecreaseDifficultyClicked();
            }
        });

        // Request card
        CardId cardId = (CardId) getIntent().getExtras().getSerializable(getCardIdExtraKey());
        Snackbar.make(findViewById(R.id.coordinator_layout),
                cardId.getDeckName() + " " + cardId.getWord() + " " + cardId.getWordComment(),
                Snackbar.LENGTH_LONG).show();
        DB.getCard(cardId)
            .setOnDoneListener(new DB.Request.OnDoneListener<Card>() {
                @Override
                public void onDone(Card result) {
                    onCardReceived(result);
                }
            })
            .setOnErrorListener(new DB.Request.OnErrorListener() {
                @Override
                public void onError(DB.Error error) {
                    onCardRequestError(error);
                }
            })
            .execute();
    }

    private void onCardReceived(Card card) {
        mCard = card;

        ((EditText) findViewById(R.id.word_edit)).setText(mCard.getWord());
        ((EditText) findViewById(R.id.word_comment_edit)).setText(mCard.getWordComment());
        ((EditText) findViewById(R.id.translation_edit)).setText(mCard.getTranslation());
        ((EditText) findViewById(R.id.translation_comment_edit)).setText(mCard.getTranslationComment());

        mDifficulty = mCard.getDifficulty();
        ((TextView) findViewById(R.id.difficulty_view))
                .setText(Integer.valueOf(mCard.getDifficulty()).toString());
        ((TextView) findViewById(R.id.difficulty_maximum_view))
                .setText(Integer.valueOf(Card.getMaxDifficulty()).toString());
    }

    private void onCardRequestError(DB.Error error) {
        Snackbar.make(findViewById(R.id.coordinator_layout), error.getMessage(),
                Snackbar.LENGTH_LONG).show();
    }

    private void onIncreaseDifficultyClicked() {
        ++ mDifficulty;
        if (mDifficulty > Card.getMaxDifficulty())
            mDifficulty = Card.getMaxDifficulty();
        ((TextView) findViewById(R.id.difficulty_view))
                .setText(Integer.valueOf(mDifficulty).toString());
    }

    private void onDecreaseDifficultyClicked() {
        -- mDifficulty;
        if (mDifficulty < Card.getMinDifficulty())
            mDifficulty = Card.getMinDifficulty();
        ((TextView) findViewById(R.id.difficulty_view))
                .setText(Integer.valueOf(mDifficulty).toString());
    }
}
