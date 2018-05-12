package com.divanoapps.learnwords.activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.local.RepositoryModule;
import com.divanoapps.learnwords.exercise.CardDispenser;
import com.divanoapps.learnwords.exercise.CardDispenserFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExerciseActivity extends AppCompatActivity {
    public static String getDeckExtraName() {
        return "DECK_EXTRA";
    }

    public static String getOrderExtraName() {
        return "ORDER_EXTRA";
    }

    @BindView(R.id.root_layout)
    ConstraintLayout rootLayout;

    @BindView(R.id.word_view)
    TextView wordView;

    @BindView(R.id.comment_view)
    TextView commentView;

    @BindView(R.id.translation_view)
    TextView translationView;

    @BindView(R.id.hide_button)
    Button hideButton;

    @BindView(R.id.decrease_difficulty_button)
    ImageButton decreaseDifficultyButton;

    @BindView(R.id.increase_difficulty_button)
    ImageButton increaseDifficultyButton;

    @BindView(R.id.difficulty_view)
    TextView difficultyView;

    @BindView(R.id.difficulty_maximum_view)
    TextView difficultyMaximumView;

    @BindView(R.id.bottom_panel_group)
    Group bottomPanelGroup;

    @BindView(R.id.second_side_group)
    Group secondSideGroup;

    Deck deck;
    Card currentCard;
    boolean isFirstSide;

    CardDispenser dispenser;

    RepositoryModule repositoryModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise);

        ButterKnife.bind(this);

        repositoryModule = new RepositoryModule(this);

        startExerciseForDeck(getIntent().getParcelableExtra(getDeckExtraName()));
    }

    private CardDispenserFactory.Order getOrder() {
        return (CardDispenserFactory.Order) getIntent().getSerializableExtra(getOrderExtraName());
    }

    private void showErrorMessageAndFinish(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
        finish();
    }

    private void startExerciseForDeck(Deck deck) {
        this.deck = deck;
        dispenser = CardDispenserFactory.create(deck.getCards(), getOrder());
        takeNextCard();
    }

    private void finishExercise() {
        currentCard = new Card(0L, "", "Finished", "", "", 0, false);
        showFirstSide();
    }

    private void takeNextCard() {
        if (!dispenser.hasNext()) {
            finishExercise();
        }
        else {
            currentCard = dispenser.getNext();
            showFirstSide();
        }
    }

    private void showFirstSide() {
        isFirstSide = true;

        secondSideGroup.setVisibility(View.INVISIBLE);
        bottomPanelGroup.setVisibility(View.GONE);

        wordView.setText(currentCard.getWord());
        commentView.setText(currentCard.getComment());
    }

    private void showSecondSide() {
        isFirstSide = false;

        secondSideGroup.setVisibility(View.VISIBLE);
        bottomPanelGroup.setVisibility(View.VISIBLE);

        wordView.setText(currentCard.getWord());
        commentView.setText(currentCard.getComment());
        translationView.setText(currentCard.getTranslation());
        difficultyView.setText(currentCard.getDifficulty().toString());
    }

    @OnClick(R.id.root_layout)
    public void onNextClicked() {
        if (isFirstSide)
            showSecondSide();
        else
            takeNextCard();
    }

    @OnClick(R.id.hide_button)
    public void onHideClicked() {
        currentCard.setHidden(true);
        repositoryModule.getCardRxRepository()
            .update(currentCard)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::takeNextCard)
            .doOnError(this::showErrorMessageAndFinish)
            .subscribe();
    }

    @OnClick(R.id.decrease_difficulty_button)
    public void onDecreaseDifficultyClicked() {
        int difficulty = currentCard.getDifficulty() - 1;
        if (difficulty < Card.getMinDifficulty())
            difficulty = Card.getMinDifficulty();
        currentCard.setDifficulty(difficulty);
        repositoryModule.getCardRxRepository()
            .update(currentCard)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::takeNextCard)
            .doOnError(this::showErrorMessageAndFinish)
            .subscribe();
    }

    @OnClick(R.id.increase_difficulty_button)
    public void onIncreaseDifficultyClicked() {
        int difficulty = currentCard.getDifficulty() + 1;
        if (difficulty > Card.getMaxDifficulty())
            difficulty = Card.getMaxDifficulty();
        currentCard.setDifficulty(difficulty);
        repositoryModule.getCardRxRepository()
            .update(currentCard)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::takeNextCard)
            .doOnError(this::showErrorMessageAndFinish)
            .subscribe();
    }
}
