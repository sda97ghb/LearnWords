package com.divanoapps.learnwords.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.divanoapps.learnwords.Application;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.adapters.CardListAdapter;
import com.divanoapps.learnwords.auxiliary.RussianNumberConjugation;
import com.divanoapps.learnwords.data.api2.ApiCard;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.data.api2.ApiExpandedDeck;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.dialogs.RenameDeckDialogFragment;
import com.divanoapps.learnwords.dialogs.YesNoMessageDialogFragment;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.DeckId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeckEditActivity extends AppCompatActivity implements
        RenameDeckDialogFragment.RenameDeckDialogListener {

    public static String getDeckIdExtraName() {
        return "DECK_NAME";
    }

    private ApiExpandedDeck mDeck = null;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.card_list_view)
    RecyclerView cardListView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.language_from)
    TextView fromLanguageView;

    @BindView(R.id.language_to)
    TextView toLanguageView;

    @BindView(R.id.number_of_cards)
    TextView numberOfCardsView;

    @BindView(R.id.number_of_hidden_card)
    TextView numberOfHiddenCardsView;

    @BindString(R.string.version_language_code)
    String versionLanguageCode;

    @BindView(R.id.decorator_number_of_cards)
    TextView numberOfCardsDecorator;

    @BindView(R.id.decorator_number_of_hidden_card)
    TextView numberOfHiddenCardsDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_edit);
        ButterKnife.bind(this);

        // Setup "Add card" FAB
        fab.setOnClickListener(view -> onAddCardClicked());

        // Expand activity to make transparent notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Setup card list
        cardListView.setLayoutManager(new LinearLayoutManager(this));

        CardListAdapter cardListAdapter = new CardListAdapter(this);
        cardListAdapter.setEditCardClickedListener(this::onEditCardClicked);
        cardListAdapter.setToggleCardEnabledClickedListener(this::onToggleCardEnabledClicked);
        cardListAdapter.setDeleteCardClickedListener(this::onDeleteCardClicked);
        cardListView.setAdapter(cardListAdapter);

        cardListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    fab.hide();
                else
                    fab.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDeck();
    }

    private void requestDeck() {
        DeckId deckId = (DeckId) getIntent().getExtras().getSerializable(getDeckIdExtraName());
        Application.api.getExpandedDeck(Application.FAKE_EMAIL, deckId.getName())
            .doOnSuccess(this::showDeck)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    private void showDeck(ApiExpandedDeck deck) {
        mDeck = deck;
        updateUi();
    }

    @SuppressLint("SetTextI18n")
    private void updateUi() {
        toolbar.setTitle(mDeck.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mDeck.getName());

        // Set language from and language to
        fromLanguageView.setText(mDeck.getFromLanguage());
        toLanguageView.setText(mDeck.getToLanguage());

        if (mDeck.getCards() != null) {
            int numberOfCards = mDeck.getCards().size();
            int numberOfHiddenCards = 0;
            for (ApiCard card: mDeck.getCards())
                if (card.isHidden())
                    ++ numberOfHiddenCards;

            // Set number of cards and number of hidden cards views
            numberOfCardsView.setText(Integer.valueOf(numberOfCards).toString());
            numberOfHiddenCardsView.setText(Integer.valueOf(numberOfHiddenCards).toString());

            if (versionLanguageCode.equals("ru")) {
                numberOfCardsDecorator.setText(RussianNumberConjugation.getCards(numberOfCards));
                numberOfHiddenCardsDecorator.setText("из них " + RussianNumberConjugation.getHidden(numberOfHiddenCards));
            }

            List<Card> cards = new ArrayList<>(mDeck.getCards().size());
            for (ApiCard card: mDeck.getCards())
                cards.add(new Card.Builder()
                    .setDeckName(card.getDeck())
                    .setWord(card.getWord())
                    .setWordComment(card.getComment())
                    .setTranslation(card.getTranslation())
                    .setDifficulty(card.getDifficulty())
                    .setHidden(card.isHidden())
                    .build());
            ((CardListAdapter) cardListView.getAdapter()).setCards(cards);
        }
        else {
            // Set number of cards and number of hidden cards views
            numberOfCardsView.setText("0");
            numberOfHiddenCardsView.setText("0");

            if (versionLanguageCode.equals("ru")) {
                numberOfCardsDecorator.setText(RussianNumberConjugation.getCards(0));
                numberOfHiddenCardsDecorator.setText("из них " + RussianNumberConjugation.getHidden(0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_deck_edit_activity_toolbar, menu);

        // Add search
        MenuItem settingsMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(settingsMenuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_rename: renameCurrentDeck(); return true;
            case R.id.action_search: /* Nothing to do */ return true;
            case R.id.action_delete: deleteCurrentDeck(); return true;
            case android.R.id.home: NavUtils.navigateUpFromSameTask(this); return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    private void renameCurrentDeck() {
        String uniqueDialogTag = "com.divanoapps.learnwords.dialogs.RenameDeckDialogFragment." + mDeck.getName();
        RenameDeckDialogFragment.newInstance(mDeck.getName())
                .show(getSupportFragmentManager(), uniqueDialogTag);
    }

    private void deleteCurrentDeck() {
        YesNoMessageDialogFragment.show(this, getString(R.string.are_you_shure_delete_deck_question), () ->
            Application.api.deleteDeck(Application.FAKE_EMAIL, mDeck.getName())
                .doOnComplete(() -> NavUtils.navigateUpFromSameTask(this))
                .doOnError(this::showErrorMessage)
                .subscribe()
        );
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof RenameDeckDialogFragment) {
            final String newDeckName = ((RenameDeckDialogFragment) dialog).getNewDeckName();
            Map<String, Object> properties = new HashMap<>();
            properties.put("name", newDeckName);
            Application.api.updateDeck(Application.FAKE_EMAIL, mDeck.getName(), properties)
                .doOnComplete(() -> {
                    getIntent().putExtra(getDeckIdExtraName(), new DeckId(newDeckName));
                    requestDeck();
                })
                .doOnError(this::showErrorMessage)
                .subscribe();
        }
    }

    public void onAddCardClicked() {
        Intent intent = new Intent(this, CardAddActivity.class);
        intent.putExtra(CardAddActivity.getDeckNameExtraName(), mDeck.getName());
        startActivity(intent);
    }

    public void onEditCardClicked(CardId id) {
        Intent intent = new Intent(DeckEditActivity.this, CardEditActivity.class);
        intent.putExtra(CardEditActivity.getDeckNameExtraName(), id.getDeckName());
        intent.putExtra(CardEditActivity.getWordExtraName(), id.getWord());
        intent.putExtra(CardEditActivity.getCommentExtraName(), id.getWordComment());
        startActivity(intent);
    }

    public void onToggleCardEnabledClicked(final CardId id) {
        // Get the card to know its visibility
        Application.api.getCard(Application.FAKE_EMAIL, id.getDeckName(), id.getWord(), id.getWordComment())
            .doOnSuccess(apiCard -> {
                Map<String, Object> properties = new HashMap<>();
                properties.put("hidden", !(apiCard.isHidden()));
                Application.api.updateCard(Application.FAKE_EMAIL, id.getDeckName(), id.getWord(), id.getWordComment(), properties)
                    .doOnComplete(this::requestDeck)
                    .doOnError(this::showErrorMessage)
                    .subscribe();
            })
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    public void onDeleteCardClicked(CardId id) {
        Application.api.deleteCard(Application.FAKE_EMAIL, id.getDeckName(), id.getWord(), id.getWordComment())
            .doOnComplete(this::requestDeck)
            .doOnError(this::showErrorMessage)
            .subscribe();
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
}
