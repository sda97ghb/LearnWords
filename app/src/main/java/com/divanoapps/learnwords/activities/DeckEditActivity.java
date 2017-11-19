package com.divanoapps.learnwords.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.divanoapps.learnwords.auxiliary.RussianNumberConjugation;
import com.divanoapps.learnwords.adapters.CardListAdapter;
import com.divanoapps.learnwords.data.DB;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.Deck;
import com.divanoapps.learnwords.entities.DeckId;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.dialogs.RenameDeckDialogFragment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DeckEditActivity extends AppCompatActivity implements
        RenameDeckDialogFragment.RenameDeckDialogListener {

    public static String getDeckIdExtraName() {
        return "DECK_NAME";
    }

    private Deck mDeck = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_edit);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup "Add card" FAB
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> onAddCardClicked());

        // Expand activity to make transparent notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Setup card list
        RecyclerView cardListView = (RecyclerView) findViewById(R.id.card_list_view);
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

        // Get deck with given name
        requestDeck();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDeck();
    }

    private void requestDeck() {
        DeckId deckId = (DeckId) getIntent().getExtras().getSerializable(getDeckIdExtraName());
        DB.getDeck(deckId)
            .setOnDoneListener(this::onDeckReceived)
            .setOnErrorListener(this::onDeckNotFound)
            .execute();
    }

    private void onDeckReceived(Deck deck) {
        mDeck = deck;
        updateUi();
    }

    private void onDeckNotFound(DB.Error error) {
        mDeck = new Deck.Builder().setName("ERROR:(")
            .setCards(Collections.singletonList(
                    new Card.Builder()
                            .setWord("Your deck has been lost.")
                            .setWordComment("Don't worry, we have a copy!")
                            .setTranslation("Press back and select deck again.")
                            .setTranslationComment(error.getMessage())
                            .setDifficulty(1)
                            .build()))
            .build();
        updateUi();
    }

    @SuppressLint("SetTextI18n")
    private void updateUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mDeck.getName());

        // Set language from and language to
        ((TextView) findViewById(R.id.language_from)).setText(mDeck.getLanguageFrom());
        ((TextView) findViewById(R.id.language_to)).setText(mDeck.getLanguageTo());

        // Set number of cards and number of hidden cards views
        ((TextView) findViewById(R.id.number_of_cards))
                .setText(Integer.valueOf(mDeck.getNumberOfCards()).toString());
        ((TextView) findViewById(R.id.number_of_hidden_card))
                .setText(Integer.valueOf(mDeck.getNumberOfHiddenCards()).toString());

        if (getResources().getString(R.string.language_code).equals("ru")) {
            ((TextView) findViewById(R.id.decorator_number_of_cards))
                    .setText(RussianNumberConjugation.getCards(mDeck.getNumberOfCards()));
            ((TextView) findViewById(R.id.decorator_number_of_hidden_card))
                    .setText("из них " + RussianNumberConjugation.getHidden(mDeck.getNumberOfHiddenCards()));
        }

        RecyclerView cardListView = (RecyclerView) findViewById(R.id.card_list_view);
        CardListAdapter cardListAdapter = (CardListAdapter) cardListView.getAdapter();
        cardListAdapter.setCards(mDeck.getCards());
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
        RenameDeckDialogFragment.newInstance(mDeck.getName()).show(getSupportFragmentManager(),
                uniqueDialogTag);
    }

    private void deleteCurrentDeck() {
        Snackbar.make(findViewById(R.id.coordinator_layout), "Delete deck " + mDeck.getName(),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof RenameDeckDialogFragment) {
            RenameDeckDialogFragment renameDeckDialog = (RenameDeckDialogFragment) dialog;
            Snackbar.make(findViewById(R.id.coordinator_layout), "Rename deck " + mDeck.getName() +
                    " to " + renameDeckDialog.getNewDeckName(), Snackbar.LENGTH_LONG).show();
        }
    }

    public void onAddCardClicked() {
        Intent intent = new Intent(this, CardEditActivity.class);
        intent.putExtra(CardEditActivity.getModeExtraKey(), CardEditActivity.Mode.ADD_CARD);
        intent.putExtra(CardEditActivity.getDeckIdExtraKey(), mDeck.getId());
        startActivity(intent);
    }

    public void onEditCardClicked(CardId id) {
        Intent intent = new Intent(DeckEditActivity.this, CardEditActivity.class);
        intent.putExtra(CardEditActivity.getModeExtraKey(), CardEditActivity.Mode.EDIT_CARD);
        intent.putExtra(CardEditActivity.getCardIdExtraKey(), id);
        startActivity(intent);
    }

    public void onToggleCardEnabledClicked(final CardId id) {
        // Get the card to know its visibility
        DB.getCard(id)
            .setOnDoneListener(result -> {
                Map<String, Object> properties = new HashMap<>();
                properties.put("isHidden", !(result.isHidden()));
                DB.modifyCard(id, properties)
                    .setOnDoneListener(this::requestDeck)
                    .setOnErrorListener(DeckEditActivity.this::showErrorMessage)
                    .execute();
            })
            .setOnErrorListener(this::showErrorMessage)
            .execute();
    }

    public void onDeleteCardClicked(CardId id) {
        DB.deleteCard(id)
            .setOnDoneListener(this::requestDeck)
            .setOnErrorListener(this::showErrorMessage)
            .execute();
    }

    private void showErrorMessage(String message) {
        MessageOkDialogFragment.newInstance(message).show(
                getSupportFragmentManager(),
                MessageOkDialogFragment.getUniqueTag());
    }

    private void showErrorMessage(DB.Error error) {
        showErrorMessage(error.getMessage());
    }
}
